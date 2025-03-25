package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TipoServico;

import java.util.List;

public class DealFiscalSameParametersValidation implements DealFiscalValidation {

    private static final String VALIDATION_FISCALDEAL_INCOMPLETE_ASSOCIATION_MSG = "_nls.contrato_pratica.msg.error.fiscal_deal.association.unmarked_flag";
    
    private List<DealFiscal> dealFiscalList;

    int selectedParamSearch = 0;
    int currentPositionList = 0;
    int countFiscalDealWithSameParams = 0;

    @Override
    public void validate(ContratoPratica clob, List<DealFiscal> selected) throws BusinessException {
        this.dealFiscalList = selected;
        if (clob.getIndicadorMultiDealFiscal().equals(Constants.NO) && selected.size() > 1
            && !this.isFiscalDealWithSameCurrencyAndCompanyAndIntercompanyAndServiceType()) {
            throw new BusinessException(BundleUtil.getBundle(VALIDATION_FISCALDEAL_INCOMPLETE_ASSOCIATION_MSG));
        }
    }

    private boolean isFiscalDealWithSameCurrencyAndCompanyAndIntercompanyAndServiceType() {

        for (int i = 0; i < this.dealFiscalList.size(); i++) {

            DealFiscal paramSearch = this.dealFiscalList.get(selectedParamSearch);
            currentPositionList += 1;

            if (currentPositionList > this.dealFiscalList.size() - 1) {
                currentPositionList -= 1;
            }

            if (searchLinearDealFiscal(paramSearch.getMoeda().getCodigoMoeda(),
                    paramSearch.getTipoServicos(), paramSearch.getEmpresa().getCodigoEmpresa(),
                    paramSearch.getEmpresaIntercomp(), currentPositionList)) {
                countFiscalDealWithSameParams += 1;
            }

            if (currentPositionList == this.dealFiscalList.size() - 1) {
                selectedParamSearch++;
            }

            if (countFiscalDealWithSameParams == this.dealFiscalList.size()) {
                return true;
            }
        }
        return false;
    }

    public boolean searchLinearDealFiscal(Long moeda,
                                          List<TipoServico> tipoServico,
                                          Long empresa,
                                          Empresa empresaIntercompany, int position) {

        return compareTo(moeda, tipoServico, empresa,
                empresaIntercompany, this.dealFiscalList.get(position));
    }

    private boolean compareMoeda(Long paramSearch, Long currentList) {
        return paramSearch.equals(currentList);
    }

    private boolean compareTipoServico(List<TipoServico> servicos, List<TipoServico> servicosCompared) {
        for (TipoServico tipoServico : servicos) {
            for (TipoServico servico : servicosCompared) {
                if (!tipoServico.getCodigoTipoServico().equals(servico.getCodigoTipoServico())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean compareEmpresa(Long paramSearch, Long currentList) {
        return paramSearch.equals(currentList);
    }

    private boolean compareEmpresaIntercompany(Empresa intercompany, Empresa intercompanyCompared) {
        if (intercompany != null && intercompanyCompared != null) {
            return intercompany.getCodigoEmpresa().equals(intercompanyCompared.getCodigoEmpresa());
        }
        return true;
    }

    private Boolean compareTo(Long moeda, List<TipoServico> tipoServico, Long empresa, Empresa empresaIntercompany,
                              DealFiscal dealFiscal) {

        return compareMoeda(moeda, dealFiscal.getMoeda().getCodigoMoeda()) && compareEmpresa(empresa, dealFiscal.getEmpresa().getCodigoEmpresa())
                && compareTipoServico(tipoServico, dealFiscal.getTipoServicos()) && compareEmpresaIntercompany(
                empresaIntercompany, dealFiscal.getEmpresaIntercomp());
    }
}


