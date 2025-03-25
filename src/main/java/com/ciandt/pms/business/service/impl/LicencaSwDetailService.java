package com.ciandt.pms.business.service.impl;


import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
public class LicencaSwDetailService implements ILicencaSwDetailService {


    @Autowired
    private IConvergenciaService  convergenciaService;

    @Autowired
    private IGrupoCustoService grupoCustoService;

    @Autowired
    private IContratoPraticaService contratoPraticaService;

    @Autowired
    private Properties systemProperties;

    @Autowired
    private IVwPmsGrupoContaContabilService vwPmsGrupoContaContabilService;

    public List<LicencaSwDetail> convertLicencaSwParcela(List<LicencaSwProjetoParcela> licencaSwProjetoParcelas){
        List<LicencaSwDetail> detailList = new ArrayList<LicencaSwDetail>();
        for (LicencaSwProjetoParcela licencaSwProjetoParcela : licencaSwProjetoParcelas) {
            LicencaSwDetail detail = new LicencaSwDetail();
            Convergencia convergencia;
            if(licencaSwProjetoParcela.getContratoPratica() != null){
                convergencia = convergenciaService.findByContratoPraticaId(licencaSwProjetoParcela.getContratoPratica().getCodigoContratoPratica());
                detail.setCodigoContratoPratica(convergencia.getContratoPratica().getCodigoContratoPratica());
            } else {
                convergencia = convergenciaService.findByCostGroupId(licencaSwProjetoParcela.getGrupoCusto().getCodigoGrupoCusto());
            }

            detail.setIdLicenca(licencaSwProjetoParcela. getCodigoLicencaSwProjetoParcela());
            detail.setNumeroParcela(licencaSwProjetoParcela.getNumeroParcela());
            detail.setCodigoGrupoCusto(convergencia.getGrupoCusto().getCodigoGrupoCusto());
            detail.setCodigoCentroCustoErp(convergencia.getCodigoCentroCustoMega());
            detail.setCodigoProjetoErp(convergencia.getCodigoProjetoMega());
            detail.setNomeGrupoCusto(convergencia.getGrupoCusto().getNomeGrupoCusto());
            detail.setNomeProjetoErp(convergencia.getNomeProjetoMega());
            detail.setValor(licencaSwProjetoParcela.getValorParcela());
            detail.setTipoParcela(licencaSwProjetoParcela.getTipoParcela());
            detail.setNotaFiscal(licencaSwProjetoParcela.getLicencaSwProjeto().getNotaFiscal().toString());
            detail.setDataParcela(licencaSwProjetoParcela.getDataParcela());
            detail.setIsUserLicense(false);
            detailList.add(detail);
        }
        return detailList;
    }

    public Map<String, List<LicencaSwDetail>> createMapFromAccounting(List<LicencaSwDetail> licencaSwDetails) throws Exception{
        Map<String, List<LicencaSwDetail>> map = new HashMap<String, List<LicencaSwDetail>>();

        for(LicencaSwDetail detail : licencaSwDetails){
            VwPmsGrupoContaContabil contaContabil = vwPmsGrupoContaContabilService.findByCodigoContratoPratica(detail.getCodigoContratoPratica());
            if(contaContabil == null){
                contaContabil = vwPmsGrupoContaContabilService.findByCodigoCentroCusto(detail.getCodigoCentroCustoErp());

                if (contaContabil == null){
                    throw new Exception(Constants.LICENSE_SW_INTEGRACAO_ACCOUNTING_NOT_FOUND);
                }
            }

            detail.setContaContabilCredito(contaContabil.getContaContabilCredito());

            if(detail.isLongTerm()){
                detail.setContaContabilDebito(contaContabil.getContaContabilDebitoLongoPrazo());
            } else {
                detail.setContaContabilDebito(contaContabil.getContaContabilDebito());
            }

            if(map.get(detail.getContaContabilDebito()) != null && map.get(detail.getContaContabilDebito()).size() > 0){
                map.get(detail.getContaContabilDebito()).add(detail);
            } else {
                map.put(detail.getContaContabilDebito(),new ArrayList<LicencaSwDetail>());
                map.get(detail.getContaContabilDebito()).add(detail);
            }
        }

        return map;
    }

    public List<String> validateCostCenterAndProjectActive(List<LicencaSwDetail> licencaSwDetailList) throws Exception {
        Map<String, Set<String>> messages = new HashMap<String, Set<String>>();
        Boolean isUserLicense = false;
        for (LicencaSwDetail licencaSwDetail : licencaSwDetailList) {
            GrupoCusto gc = grupoCustoService.findGrupoCustoById(licencaSwDetail.getCodigoGrupoCusto());
            isUserLicense = licencaSwDetail.getIsUserLicense();
            if (gc.getIndicadorAtivo().equals(Constants.INACTIVE)) {
                if (messages.containsKey(gc.getNomeGrupoCusto())) {
                    messages.get(gc.getNomeGrupoCusto()).add(licencaSwDetail.getNotaFiscal());
                } else {
                    Set<String> notasFiscais = new HashSet<String>();
                    notasFiscais.add(licencaSwDetail.getNotaFiscal());
                    messages.put(gc.getNomeGrupoCusto(), notasFiscais);
                }
            }
            if (licencaSwDetail.getCodigoContratoPratica() != null) {
                ContratoPratica cp = contratoPraticaService.findContratoPraticaById(licencaSwDetail.getCodigoContratoPratica());

                if (!cp.getIndicadorAtivo().equals(Constants.ACTIVE)) {
                    if (messages.containsKey(cp.getNomeContratoPratica())) {
                        messages.get(cp.getNomeContratoPratica()).add(licencaSwDetail.getNotaFiscal());
                    } else {
                        Set<String> notasFiscais = new HashSet<String>();
                        notasFiscais.add(licencaSwDetail.getNotaFiscal());
                        messages.put(cp.getNomeContratoPratica(), notasFiscais);
                    }
                }
            }
        }
        List<String> inconsistencyMessages = new ArrayList<String>();
        if (!messages.isEmpty()) {

            for (Map.Entry<String, Set<String>> entry : messages.entrySet()) {
                if (isUserLicense) {
                    inconsistencyMessages.add(String.format(BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_USER_COST_CENTER_CLOB_INACTIVE), StringUtils.join(entry.getValue(),", "), entry.getKey()));
                } else {
                    inconsistencyMessages.add(String.format(BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_PROJECT_COST_CENTER_CLOB_INACTIVE), StringUtils.join(entry.getValue(),", "), entry.getKey()));
                }
            }
            return inconsistencyMessages;
        }
        return null;
    }

    public String getValorTotal(List<LicencaSwDetail> licencaSwDetails){
        BigDecimal total = new BigDecimal("0");
        for(LicencaSwDetail detail : licencaSwDetails){
            total = total.add(new BigDecimal(detail.getValor().toString()));
        }

        return total.toString().replace('.',',');
    }
}
