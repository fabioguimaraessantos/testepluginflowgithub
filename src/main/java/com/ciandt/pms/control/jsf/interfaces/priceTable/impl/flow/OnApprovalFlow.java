package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.model.PriceTableStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("onApprovalFlow")
public class OnApprovalFlow extends PriceTableFlow {

    @Autowired
    private IModuloService moduloService;

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_ON_APRROVAL);
    }

    @Override
    protected String description() {
        return Constants.PRICE_TABLE_STATUS_ON_APPROVAL;
    }

    protected List<String> findAcronyms(PriceTableMemberType member) {
        List<String> acronyms = new ArrayList<String>();
        acronyms.add("ONAP");
        if (member == PriceTableMemberType.APPROVER) {
            acronyms.add("APP");
            acronyms.add("NAPP");
        }
        return acronyms;
    }

    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }
}