package com.ciandt.pms.integration.vo.accountingEntry;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 *
 * @author Marcos Vidolin
 * @since Set 17, 2018
 */
@Data
@ToString()
public class AccountingEntryDebitItem {

    private String operation = "I";

    private String code;

    private String branchCompanyCode;

    private String conversionDate;

    private String budgetDate;

    private String debitAccountCode;

    private String historyCode = "1101";

    private String complement;

    private String amount;

    private List<AccountingEntryItemCostCenter> costCenters;

}
