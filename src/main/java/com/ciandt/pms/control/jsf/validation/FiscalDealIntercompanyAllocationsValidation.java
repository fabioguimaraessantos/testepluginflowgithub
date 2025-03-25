package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FiscalDealIntercompanyAllocationsValidation implements DealFiscalValidation {

    private static final String REMOVE_FISCAL_DEAL_WITH_ALLOCATIONS = "_nls.contrato_pratica.msg.error.fiscal_deal.allocations";

    private final Logger logger = LoggerFactory.getLogger(FiscalDealIntercompanyAllocationsValidation.class);

    private final IAlocacaoService alocacaoService;
    private final ICidadeBaseService cidadeBaseService;
    private final IModuloService moduloService;

    private final IEmpresaService empresaService;

    public FiscalDealIntercompanyAllocationsValidation(IAlocacaoService alocacaoService,
                                                       ICidadeBaseService cidadeBaseService,
                                                       IModuloService moduloService,
                                                       IEmpresaService empresaService) {
        this.alocacaoService = alocacaoService;
        this.cidadeBaseService = cidadeBaseService;
        this.moduloService = moduloService;
        this.empresaService = empresaService;
    }

    @Override
    public void validate(ContratoPratica clob, List<DealFiscal> selected) throws BusinessException {
        logger.info("Starting execution of FiscalDealIntercompanyAllocationsValidation");

        HashMap<String, Set<String>> allocationLoginsMap = new HashMap<String, Set<String>>();

        if (clob.getIndicadorMultiDealFiscal().equals(Constants.NO) && selected.size() > 0) {
            logger.info("C-LOB NOT marked as Multi Fiscal Deal, getting allocations by CLOB");
            List<Alocacao> allocations = this.getAllocationsByClob(clob.getCodigoContratoPratica());
            Map<CidadeBase, List<Alocacao>> cityBaseAllocationsMap = this.getCityBaseAllocations(allocations);

            for (Alocacao allocation : allocations) {
                CidadeBase allocationCityBase = allocation.getCidadeBase();
                Long companyCode = cidadeBaseService.findEmpresaByCidadeBase(allocationCityBase.getCodigoCidadeBase());
                Empresa allocationCompany = empresaService.findEmpresaById(companyCode);

                if (!this.hasValidCompanyOrIntercompanyFiscalDeal(selected, allocationCompany)) {
                    logger.info("Not found valid intercompany FD for City Base {}", allocationCityBase.getNomeCidadeBase());
                    this.relateLoginsWithAllocationMap(allocationLoginsMap, cityBaseAllocationsMap.get(allocationCityBase));
                }
            }

            if (!allocationLoginsMap.isEmpty()) {
                String mapLogins = this.getAllocationMapLogins(allocationLoginsMap);
                logger.info("Throwing exception cause there are allocated logins without FD associated. Logins: {}", mapLogins);
                throw new BusinessException(String.format(BundleUtil.getBundle(REMOVE_FISCAL_DEAL_WITH_ALLOCATIONS), mapLogins));
            }
        }

        logger.info("Ending execution of FiscalDealIntercompanyAllocationsValidation with success");
    }

    private boolean hasValidCompanyOrIntercompanyFiscalDeal(List<DealFiscal> selected, Empresa allocationCompany) {
        Long allocationParentCompanyCode = allocationCompany.getEmpresa().getCodigoEmpresa();

        for (DealFiscal dealFiscalSelected : selected) {
            // checks if company of FD is the same comparing with allocation
            if (dealFiscalSelected.getEmpresa() != null &&
                    dealFiscalSelected.getEmpresa().getCodigoEmpresa().equals(allocationCompany.getCodigoEmpresa())) {
                return true;
            }

            // checks if FD parent company is the same
            if (dealFiscalSelected.getEmpresa().getEmpresa() != null) {
                Empresa fdParentCompany = dealFiscalSelected.getEmpresa().getEmpresa();
                if (fdParentCompany.getCodigoEmpresa().equals(allocationParentCompanyCode)) {
                    return true;
                }
            }

            // checks if intercompany configured is with same parent company
            if (dealFiscalSelected.getIndicadorIntercompany()) {
                Set<HistoricoPercentualIntercomp> fdIntercompanies = dealFiscalSelected.getHistoricoPercentualIntercomps();
                if (fdIntercompanies != null && fdIntercompanies.size() > 0) {
                    for (HistoricoPercentualIntercomp fdIntercompany : fdIntercompanies) {
                        if (fdIntercompany.getDataFim() == null || fdIntercompany.getDataFim().after(DateUtil.getDateFirstDayOfMonth(new Date()))) {
                            Empresa fiscalDealParentCompany = fdIntercompany.getEmpresaIntercomp().getEmpresa();
                            if (fiscalDealParentCompany.getCodigoEmpresa().equals(allocationParentCompanyCode)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<Alocacao> getAllocationsByClob(Long clobCode) {
        Date closingMapDate = moduloService.getClosingDateMapaAlocacao();
        return alocacaoService.findVigentesByContratoPratica(clobCode, closingMapDate);
    }

    private Map<CidadeBase, List<Alocacao>> getCityBaseAllocations(List<Alocacao> allocations) {
        Map<CidadeBase, List<Alocacao>> cityBaseAllocations = new HashMap<CidadeBase, List<Alocacao>>();
        for (Alocacao allocation : allocations) {
            if (cityBaseAllocations.get(allocation.getCidadeBase()) == null) {
                cityBaseAllocations.put(allocation.getCidadeBase(), new ArrayList<Alocacao>());
            }
            cityBaseAllocations.get(allocation.getCidadeBase()).add(allocation);
        }
        return cityBaseAllocations;
    }

    private void relateLoginsWithAllocationMap(Map<String, Set<String>> map, List<Alocacao> allocations) {
        String allocationMapName = allocations.get(0).getMapaAlocacao().getTextoTitulo();
        if (map.get(allocationMapName) == null) {
            map.put(allocationMapName, new HashSet<String>());
        }

        for (Alocacao allocation : allocations) {
            map.get(allocationMapName).add(String.format("%s (%s)",
                    allocation.getRecurso().getCodigoMnemonico(), allocation.getCidadeBase().getSiglaCidadeBase()));
        }
    }

    private String getAllocationMapLogins(HashMap<String, Set<String>> allocationLoginMap) {
        String mapName = allocationLoginMap.entrySet().iterator().next().getKey();
        String logins = StringUtils.join(allocationLoginMap.get(mapName).toArray(), ", ");
        return String.format("%s - %s", mapName, logins);
    }
}