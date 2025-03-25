package com.ciandt.pms.business.service;

import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import org.springframework.stereotype.Service;

@Service
public interface ILicencaSoftwareValidacaoService {
    public void validateRequiredFields(AccountingEntry accountingEntry);
}
