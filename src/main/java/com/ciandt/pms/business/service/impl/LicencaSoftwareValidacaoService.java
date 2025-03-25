package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.ILicencaSoftwareValidacaoService;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntry;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryCreditItem;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryDebitItem;
import com.ciandt.pms.integration.vo.accountingEntry.AccountingEntryItemCostCenter;
import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

@Service
public class LicencaSoftwareValidacaoService implements ILicencaSoftwareValidacaoService {

    public void validateRequiredFields(AccountingEntry accountingEntry) {
        Preconditions.checkNotNull(accountingEntry.getCompanyCode(), "Company Code cannot be null");
        Preconditions.checkNotNull(accountingEntry.getCode(), "Code cannot be null");
        Preconditions.checkNotNull(accountingEntry.getActionCode(), "Action Code cannot be null");
        Preconditions.checkNotNull(accountingEntry.getDate(), "Date cannot be null");
        Preconditions.checkNotNull(accountingEntry.getPersistOutOfDate(), "Persist Out of Date cannot be null");

        //CreditItem
        for (AccountingEntryCreditItem accountingEntryCreditItem : accountingEntry.getCreditItems()) {
            Preconditions.checkNotNull(accountingEntryCreditItem.getCode(), "Code of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getBranchCompanyCode(), "Branch Company Code of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getConversionDate(), "Conversion Date of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getBudgetDate(), "Budget Date of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getCreditAccountCode(), "Credit Account Code of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getHistoryCode(), "History of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getComplement(), "Complement of Credit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryCreditItem.getAmount(), "Amount of Credit Item cannot be null");

            for (AccountingEntryItemCostCenter accountingEntryItemCostCenter : accountingEntryCreditItem.getCostCenters()) {
                //CostCenterCreditItem
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getCostCenterReduceCode(), "Cost Center Reduce Code of Credit Item cannot be null");
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getAmount(), "Cost Center Amount of Credit Item cannot be null");
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getType(), "Cost Center Type of Credit Item cannot be null");
                //ProjectCreditItem
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getProject().getMegaProjectCode(), "Project Mega Code of Credit Item cannot be null");
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getProject().getAmount(), "Project Amount of Credit Item cannot be null");
            }
        }


        //DebitItem
        for (AccountingEntryDebitItem accountingEntryDebitItem : accountingEntry.getDebitItems()) {
            Preconditions.checkNotNull(accountingEntryDebitItem.getCode(), "Code of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getBranchCompanyCode(), "Branch Company Code of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getConversionDate(), "Conversion Date of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getBudgetDate(), "Budget Date of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getDebitAccountCode(), "Debit Account Code of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getHistoryCode(), "History of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getComplement(), "Complement of Debit Item cannot be null");
            Preconditions.checkNotNull(accountingEntryDebitItem.getAmount(), "Amount of Debit Item cannot be null");

            for (AccountingEntryItemCostCenter accountingEntryItemCostCenter : accountingEntryDebitItem.getCostCenters()) {
                //CostCenterDebitItem
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getCostCenterReduceCode(), "Cost Center Reduce Code of Debit Item cannot be null");
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getAmount(), "Cost Center Amount of Debit Item cannot be null");
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getType(), "Cost Center Type of Debit Item cannot be null");
                //ProjectDebitItem
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getProject().getMegaProjectCode(), "Project Mega Code of Debit Item cannot be null");
                Preconditions.checkNotNull(accountingEntryItemCostCenter.getProject().getAmount(), "Project Amount of Debit Item cannot be null");
            }
        }
    }
}
