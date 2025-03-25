package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MultiFiscalDealSameCompanyValidation implements DealFiscalValidation {

    private final Logger logger = LoggerFactory.getLogger(MultiFiscalDealSameCompanyValidation.class);

    private static final String INVALID_FISCAL_DEAL_ASSOCIATION_SAME_COMPANY_MSG = "_nls.contrato_pratica.msg.error.fiscal_deal.same.company";

    @Override
    public void validate(ContratoPratica clob, List<DealFiscal> selected) throws BusinessException {
        logger.info("Starting execution of MultiFiscalDealSameCompanyValidation");

        if (clob.getIndicadorMultiDealFiscal().equals(Constants.FLAG_YES) &&
                this.hasDuplicatedCompanyFiscalDeals(selected)) {
            throw new BusinessException(BundleUtil.getBundle(INVALID_FISCAL_DEAL_ASSOCIATION_SAME_COMPANY_MSG));
        }
    }

    private Boolean hasDuplicatedCompanyFiscalDeals(List<DealFiscal> fiscalDealList) {
        for (DealFiscal df : fiscalDealList) {
            Long empresa = df.getEmpresa().getCodigoEmpresa();
            for (DealFiscal df1 : fiscalDealList) {
                if (!df1.getCodigoDealFiscal().equals(df.getCodigoDealFiscal()) &&
                        empresa.equals(df1.getEmpresa().getCodigoEmpresa())) {
                    return true;
                }
            }
        }
        return false;
    }
}