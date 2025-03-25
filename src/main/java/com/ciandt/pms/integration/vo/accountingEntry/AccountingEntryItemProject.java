package com.ciandt.pms.integration.vo.accountingEntry;

import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Marcos Vidolin
 * @since Set 17, 2018
 */
@Data
@ToString()
public class AccountingEntryItemProject {

    private String operation = "I";

    private String megaProjectCode;

    private String amount;

}
