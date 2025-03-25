package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.model.PriceTableStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("deletedFlow")
public class DeletedFlow extends PriceTableFlow{

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_DELETED);
    }

    @Override
    protected String description() {
        return Constants.PRICE_TABLE_STATUS_DELETED;
    }

    protected List<String> findAcronyms(PriceTableMemberType member){
        List<String> acronyms = new ArrayList<String>();
        acronyms.add("DEL");
        return acronyms;
    }


    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }
}