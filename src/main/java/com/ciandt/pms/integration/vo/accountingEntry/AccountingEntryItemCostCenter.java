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
public class AccountingEntryItemCostCenter {

    public enum Type {

        DEBT("D"), CREDIT("C");

        private String acronym;

        Type(String acronym) {
            this.acronym = acronym;
        }

        public String getAcronym() { return this.acronym;}
    }

    private String operation = "I";

    private String costCenterReduceCode;

    private String amount;

    private String type;

    private AccountingEntryItemProject project;

}
