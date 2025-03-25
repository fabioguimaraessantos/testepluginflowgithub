package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MultiDealFiscalComAlocacaoVigenteValidation implements DealFiscalValidation {

    private static final String REMOVE_FISCAL_DEAL_WITH_ALLOCATIONS = "_nls.contrato_pratica.msg.error.fiscal_deal.allocations";

    private final Logger logger = LoggerFactory.getLogger(MultiDealFiscalComAlocacaoVigenteValidation.class);

    private final IAlocacaoService alocacaoService;
    private final ICidadeBaseService cidadeBaseService;
    private final IModuloService moduloService;

    public MultiDealFiscalComAlocacaoVigenteValidation(IAlocacaoService alocacaoService,
                                                       ICidadeBaseService cidadeBaseService,
                                                       IModuloService moduloService) {
        this.alocacaoService = alocacaoService;
        this.cidadeBaseService = cidadeBaseService;
        this.moduloService = moduloService;
    }

    @Override
    public void validate(ContratoPratica clob, List<DealFiscal> selected) throws BusinessException {
        logger.info("Starting execution of MultiDealFiscalComAlocacaoVigenteValidation");

        HashMap<String, Set<String>> allocationLoginsMap = new HashMap<String, Set<String>>();

        if (clob.getIndicadorMultiDealFiscal().equals(Constants.YES)) {
            logger.info("C-LOB marked as Multi Fiscal Deal, getting allocations by CLOB");
            List<Alocacao> allocations = this.getAllocationsByClob(clob.getCodigoContratoPratica());
            Map<CidadeBase, List<Alocacao>> cityBaseAllocationsMap = this.getCityBaseAllocations(allocations);

            if (!allocations.isEmpty()) {
                logger.info("Found {} allocations, checking if exists allocations for FD companies", allocations.size());
                List<CidadeBase> cityBaseByAllocations = this.getCityBasesByAllocations(allocations);
                for (CidadeBase cityBase : cityBaseByAllocations) {
                    logger.info("Checking if there are Fiscal Deal associated for City Base {}", cityBase);
                    if (!this.hasFiscalDealForCityBase(selected, cityBase.getCodigoCidadeBase())) {
                        logger.info("Not found Fiscal Deal for City Base {}", cityBase);
                        this.relateLoginsWithAllocationMap(allocationLoginsMap, cityBaseAllocationsMap.get(cityBase));
                    }
                }
            }

            if (!allocationLoginsMap.isEmpty()) {
                String mapLogins = this.getAllocationMapLogins(allocationLoginsMap);
                logger.info("Throwing exception cause there are allocated logins without FD associated. Logins: {}", mapLogins);
                throw new BusinessException(String.format(BundleUtil.getBundle(REMOVE_FISCAL_DEAL_WITH_ALLOCATIONS), mapLogins));
            }
        }

        logger.info("Ending execution of MultiDealFiscalComAlocacaoVigenteValidation");
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

    private boolean hasFiscalDealForCityBase(List<DealFiscal> selected, Long cityBaseId) {
        Long companyOfCityBase = cidadeBaseService.findEmpresaByCidadeBase(cityBaseId);
        for (DealFiscal selectedDealFiscal : selected) {
            if (selectedDealFiscal.getEmpresa().getCodigoEmpresa().equals(companyOfCityBase)) {
                return true;
            }
        }
        return false;
    }

    private List<CidadeBase> getCityBasesByAllocations(List<Alocacao> allocations) {
        List<CidadeBase> ids = new ArrayList<CidadeBase>();
        for (Alocacao allocation : allocations) {
            if (!ids.contains(allocation.getCidadeBase())) {
                ids.add(allocation.getCidadeBase());
            }
        }
        return ids;
    }

    private String getAllocationMapLogins(HashMap<String, Set<String>> allocationLoginMap) {
        String mapName = allocationLoginMap.entrySet().iterator().next().getKey();
        String logins = StringUtils.join(allocationLoginMap.get(mapName).toArray(), ", ");
        return String.format("%s - %s", mapName, logins);
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

    private List<Alocacao> getAllocationsByClob(Long clobCode) {
        Date closingMapDate = moduloService.getClosingDateMapaAlocacao();
        return alocacaoService.findVigentesByContratoPratica(clobCode, closingMapDate);
    }
}