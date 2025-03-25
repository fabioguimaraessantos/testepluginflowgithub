package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.ContratoPraticaController;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;

import java.util.List;

public class SelecaoDealFiscalValidation implements DealFiscalValidation {

    private static final String VALIDATION_FISCALDEAL_INCOMPLETE_ASSOCIATION_WITH_FLAG_MSG = "_nls.contrato_pratica.msg.error.fiscal_deal.incomplete.association";

    private ContratoPraticaController contratoPraticaController;

    @Override
    public void validate(ContratoPratica clob, List<DealFiscal> selected) throws BusinessException {
        if (clob.getIndicadorMultiDealFiscal().equals(Constants.YES) && selected.size() <= 1) {
            throw new BusinessException(BundleUtil.getBundle(VALIDATION_FISCALDEAL_INCOMPLETE_ASSOCIATION_WITH_FLAG_MSG));
        }
    }
}
