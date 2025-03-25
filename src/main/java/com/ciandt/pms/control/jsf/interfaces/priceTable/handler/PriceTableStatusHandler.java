package com.ciandt.pms.control.jsf.interfaces.priceTable.handler;

import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableStatus;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow.PriceTableFlow;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status.ApprovedPriceTableStatus;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status.DeletedPriceTableStatus;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status.DraftPriceTableStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PriceTableStatusHandler {

    @Autowired
    @Qualifier("draftStatus")
    private DraftPriceTableStatus draftStatus;

    @Autowired
    @Qualifier("approvedStatus")
    private ApprovedPriceTableStatus approvedStatus;

    @Autowired
    @Qualifier("deletedStatus")
    private DeletedPriceTableStatus deletedStatus;

    public IPriceTableStatus findPriceTableStatus(Long status) {

        if (status != null) {
            if (status.equals(PriceTableFlow.STATUS_DRAFT))
                return draftStatus;

            if (status.equals(PriceTableFlow.STATUS_APPROVED))
                return approvedStatus;

            if (status.equals(PriceTableFlow.STATUS_DELETED))
                return deletedStatus;

        }

        return null;
    }
}