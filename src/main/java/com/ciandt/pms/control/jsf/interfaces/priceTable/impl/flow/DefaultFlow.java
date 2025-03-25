package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.model.PriceTableStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("defaultFlow")
public class DefaultFlow extends PriceTableFlow{

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_DRAFT);
    }

    @Override
    protected String description() {
        return "Created";
    }
    protected List<String> findAcronyms(PriceTableMemberType member){
        List<String> acronyms = new ArrayList<String>();
        acronyms.add("D");
        acronyms.add("REAP");
        acronyms.add("ONAP");
        acronyms.add("APP");
        acronyms.add("NAPP");
        acronyms.add("DEL");
        return acronyms;
    }


    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }
}