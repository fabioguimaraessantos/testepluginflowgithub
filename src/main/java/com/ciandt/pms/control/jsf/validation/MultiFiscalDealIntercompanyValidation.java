package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MultiFiscalDealIntercompanyValidation implements DealFiscalValidation {

    private final Logger logger = LoggerFactory.getLogger(MultiFiscalDealIntercompanyValidation.class);

    private static final String VALIDATION_MULTIFISCALDEAL_INTERCOMPANY_MSG = "_nls.contrato_pratica.msg.error.fiscal_deal.intercompany";

    @Override
    public void validate(ContratoPratica clob, List<DealFiscal> selected)
            throws BusinessException {
        logger.info("Starting execution of MultiFiscalDealIntercompanyValidation");

        if (clob.getIndicadorMultiDealFiscal().equals(Constants.YES) && selected != null && selected.size() >= 2) {
           for(DealFiscal dealFiscal : selected) {
                if(dealFiscal.getIndicadorIntercompany()) {
                    throw new BusinessException(BundleUtil.getBundle(VALIDATION_MULTIFISCALDEAL_INTERCOMPANY_MSG));
                }
           }
        }
    }
}