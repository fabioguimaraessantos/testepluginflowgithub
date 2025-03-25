package com.ciandt.pms.model.vo;

public class PaymentCondition {
    private String name;

    private Long days;

    private Long agentCode;

    private Long companyCode;

    private String dueDate;

    private String agentName;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public Long getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(Long agentCode) {
        this.agentCode = agentCode;
    }

    public Long getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(Long companyCode) {
        this.companyCode = companyCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
