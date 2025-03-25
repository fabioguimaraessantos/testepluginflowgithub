package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPayrollEventService;
import com.ciandt.pms.business.service.IPayrollEventTypeService;
import com.ciandt.pms.control.jsf.bean.PayrollEventBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.PayrollEvent;
import com.ciandt.pms.model.PayrollEventType;
import com.ciandt.pms.util.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BOF.PAYROLL_EVENTS:VW"})
public class PayrollEventController {

    private static final String OUTCOME_PAYROLL_EVENTS = "payroll_events_edit";

    @Autowired
    private IPayrollEventService payrollEventService;

    @Autowired
    private IPayrollEventTypeService payrollEventTypeService;

    @Autowired
    private PayrollEventBean bean;

    private boolean isITSupport = false;

    public PayrollEventBean getBean() {
        return bean;
    }

    public void setBean(PayrollEventBean bean) {
        this.bean = bean;
    }

    /**
     * Prepares the output
     * @return payroll_events_edit output
     */
    public String preparePayrollEvents() {
        bean.reset();
        setIsITSupport(validateITSupport());
        // populates thes lists
        this.loadPayrollEventTypeList(payrollEventTypeService.findAll());
        bean.setPayrollEvents(payrollEventService.findAll());

        return OUTCOME_PAYROLL_EVENTS;
    }

    /**
     * Method to be called on changed reclassification event
     */
    public void selectReclass() {
        PayrollEvent to = bean.getTo();
        // set the object from map, based on selection
        to.setPayrollEventType(bean.getPayrollEventTypeMap().get(to.getReclass()));
        // adds to changed list
        bean.getChangedPayrollEvents().add(to);
        bean.resetTo();
        setIsITSupport(validateITSupport());
    }

    /**
     * Saves the changed Payroll Events
     */
    public void save() {
        // updates each changed object
        for (PayrollEvent payrollEvent : bean.getChangedPayrollEvents()) {
            payrollEventService.update(payrollEvent);
        }
        // reset the values
        bean.setPayrollEvents(payrollEventService.findAll());
        bean.resetChanged();

        Messages.showSucess("updatePayrollEvents",
                Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                "Payroll Event");
    }

    /**
     * Populates the event types list for combobox.
     *
     * @param payrollEventTypes
     */
    private void loadPayrollEventTypeList(final List<PayrollEventType> payrollEventTypes) {

        Map<String, PayrollEventType> payrollEventMap = new HashMap<String, PayrollEventType>();
        List<String> payrollEventList = new ArrayList<String>();

        for (PayrollEventType payrollEventType : payrollEventTypes) {
            payrollEventMap.put(payrollEventType.getDescription(), payrollEventType);
            payrollEventList.add(payrollEventType.getDescription());
        }

        bean.setPayrollEventTypeMap(payrollEventMap);
        bean.setPayrollEventTypeList(payrollEventList);
    }

    public void findByFilter() {
        bean.setPayrollEvents(payrollEventService.findByCodeAndName(bean.getFilter().getCode(), bean.getFilter().getName()));

        if (bean.getPayrollEvents() == null || bean.getPayrollEvents().size() <= 0) {
            bean.setShowTable(false);
        } else {
            bean.setShowTable(true);
        }
        setIsITSupport(validateITSupport());
    }

    private Boolean validateITSupport() {
        GrantedAuthority[] loggedUserAuthorities = LoginUtil
                .getLoggedUserAuthorities();
        boolean isItSupport = false;
        for (GrantedAuthority authority : loggedUserAuthorities) {

            if(authority.getAuthority().equals("BOF.PAYROLL_EVENTS:ED")
                    ||authority.getAuthority().equals("BOF.PAYROLL_EVENTS:CR")){
                isItSupport = false;
                continue;
            }
            if (authority.getAuthority().equals("BOF.PAYROLL_EVENTS:VW")) {
                isItSupport = true;
            }
        }
        return isItSupport;
    }
    public boolean getIsITSupport() {
        return isITSupport;
    }

    public void setIsITSupport(boolean isITSupport) {
        this.isITSupport = isITSupport;
    }
}