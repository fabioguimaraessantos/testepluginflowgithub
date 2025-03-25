package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.PayrollEvent;
import com.ciandt.pms.model.PayrollEventType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amanda on 31/01/17.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PayrollEventBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    private PayrollEvent to = new PayrollEvent();

    private PayrollEvent filter = new PayrollEvent();

    private List<PayrollEvent> payrollEvents = new ArrayList<PayrollEvent>();

    private List<String> payrollEventTypeList = new ArrayList<String>();

    private Map<String, PayrollEventType> payrollEventTypeMap = new HashMap<String, PayrollEventType>();

    private List<PayrollEvent> changedPayrollEvents = new ArrayList<PayrollEvent>();

    private Integer currentPageId = Integer.valueOf(1);

    private Boolean showTable = true;

    public void reset() {
        this.resetChanged();
        this.payrollEventTypeList = new ArrayList<String>();
        this.payrollEventTypeMap = new HashMap<String, PayrollEventType>();
        this.resetFilter();
    }

    public void resetChanged() {
        this.resetTo();
        this.changedPayrollEvents = new ArrayList<PayrollEvent>();
    }

    public void resetTo() {
        this.to = new PayrollEvent();
    }

    public PayrollEvent getTo() {
        return to;
    }

    public void setTo(PayrollEvent to) {
        this.to = to;
    }

    public void resetFilter() {
        this.filter = new PayrollEvent();
    }

    public PayrollEvent getFilter() {
        return filter;
    }

    public void setFilter(PayrollEvent filter) {
        this.filter = filter;
    }

    public List<PayrollEvent> getPayrollEvents() {
        return payrollEvents;
    }

    public void setPayrollEvents(List<PayrollEvent> payrollEvents) {
        this.payrollEvents = payrollEvents;
    }

    public List<String> getPayrollEventTypeList() {
        return payrollEventTypeList;
    }

    public void setPayrollEventTypeList(List<String> payrollEventTypeList) {
        this.payrollEventTypeList = payrollEventTypeList;
    }

    public Map<String, PayrollEventType> getPayrollEventTypeMap() {
        return payrollEventTypeMap;
    }

    public void setPayrollEventTypeMap(Map<String, PayrollEventType> payrollEventTypeMap) {
        this.payrollEventTypeMap = payrollEventTypeMap;
    }

    public List<PayrollEvent> getChangedPayrollEvents() {
        return changedPayrollEvents;
    }

    public void setChangedPayrollEvents(List<PayrollEvent> changedPayrollEvents) {
        this.changedPayrollEvents = changedPayrollEvents;
    }


    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    public Integer getCurrentPageId() {
        return currentPageId;
    }

    public Boolean getShowTable() {
        return showTable;
    }

    public void setShowTable(Boolean showTable) {
        this.showTable = showTable;
    }
}
