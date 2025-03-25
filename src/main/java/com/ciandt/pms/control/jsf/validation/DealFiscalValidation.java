package com.ciandt.pms.control.jsf.validation;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;

import java.util.List;

public interface DealFiscalValidation {

    void validate(ContratoPratica clob, List<DealFiscal> selected)
            throws BusinessException;
}
