package com.ciandt.pms.control.jsf.interfaces.priceTable.handler;


import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableFlow;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PriceTableFlowHandler {

    @Autowired
    @Qualifier("draftFlow")
    private DraftFlow draftFlow;

    @Autowired
    @Qualifier("readyForApprovalFlow")
    private ReadyForApprovalFlow readyForApprovalFlow;

    @Autowired
    @Qualifier("onApprovalFlow")
    private OnApprovalFlow onApprovalFlow;

    @Autowired
    @Qualifier("notApprovedFlow")
    private NotApprovedFlow notApprovedFlow;

    @Autowired
    @Qualifier("approvedFlow")
    private ApprovedFlow approvedFlow;

    @Autowired
    @Qualifier("deletedFlow")
    private DeletedFlow deletedFlow;

    @Autowired
    @Qualifier("defaultFlow")
    private DefaultFlow defaultFlow;

    /**
     * @param statusPms
     * @return
     */
    public IPriceTableFlow findPriceTableFlow(Long statusPms) {

        if (statusPms != null) {
            if (statusPms.equals(PriceTableFlow.STATUS_DRAFT))
                return draftFlow;

            if (statusPms.equals(PriceTableFlow.STATUS_READY_FOR_APRROVAL))
                return readyForApprovalFlow;

            if (statusPms.equals(PriceTableFlow.STATUS_ON_APRROVAL))
                return onApprovalFlow;

            if (statusPms.equals(PriceTableFlow.STATUS_NOT_APPROVED))
                return notApprovedFlow;

            if (statusPms.equals(PriceTableFlow.STATUS_APPROVED))
                return approvedFlow;

            if (statusPms.equals(PriceTableFlow.STATUS_DELETED))
                return deletedFlow;

        }

        return defaultFlow;
    }

    public IPriceTableFlow defaultFlow(){
        return defaultFlow;
    }
}