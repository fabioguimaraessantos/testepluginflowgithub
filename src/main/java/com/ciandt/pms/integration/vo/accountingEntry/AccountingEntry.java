package com.ciandt.pms.integration.vo.accountingEntry;

import com.ciandt.pms.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Marcos Vidolin
 * @since Set 17, 2018
 */
@Data
@ToString()
public class AccountingEntry {

    @Getter
    private final String lotPrefix = "1";

    @Getter
    private final String lotCode = "0408";

    private String fileName;

    private String operation = "I";

    private String companyCode;

    private String code;

    private String actionCode;

    private String date;

    private String persistOutOfDate = "S";

    private List<AccountingEntryCreditItem> creditItems;

    private List<AccountingEntryDebitItem> debitItems;

    public AccountingEntry(){
        if (this.date != null){
            this.code = lotPrefix + this.date.substring(3,5) + lotCode;
        } else {
            this.code = lotPrefix + DateUtil.getMonthString(new Date()) + lotCode;
        }

    }

    public String getCode() {
        if (this.date != null){
            this.code = lotPrefix + this.date.substring(3,5) + lotCode;
        } else {
            this.code = lotPrefix + DateUtil.getMonthString(new Date()) + lotCode;
        }
        return code;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        return gson.toJson(this);
    }

    public void setCode(String code) { this.code = code; }

}
