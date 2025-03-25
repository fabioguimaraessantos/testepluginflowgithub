package com.ciandt.pms.integration.vo.licenses;

import java.util.List;

public class IntegLicense {

    private String ledgerName;
    private String batchName;
    private String batchDescription;
    private String accountingPeriodName;
    private String accountingDate;
    private String userSourceName;
    private String userCategoryName;
    private String journalName;
    private String reference5;
    private List<License> licenses;

    /* Getters and Setters */

    public List<License> getLicenses() {
        return licenses;
    }
    public void setLicenses(List<License> licenses) {
        this.licenses = licenses;
    }
    public String getBatchDescription() { return batchDescription; }
    public void setBatchDescription(String batchDescription) { this.batchDescription = batchDescription; }
    public String getLedgerName() { return ledgerName; }
    public void setLedgerName(String ledgerName) { this.ledgerName = ledgerName; }
    public String getBatchName() { return batchName; }
    public void setBatchName(String batchName) { this.batchName = batchName; }
    public String getAccountingPeriodName() { return accountingPeriodName; }
    public void setAccountingPeriodName(String accountingPeriodName) { this.accountingPeriodName = accountingPeriodName; }
    public String getUserSourceName() { return userSourceName; }
    public void setUserSourceName(String userSourceName) { this.userSourceName = userSourceName; }
    public String getJournalName() { return journalName; }
    public void setJournalName(String journalName) { this.journalName = journalName; }
    public String getReference5() { return reference5; }
    public void setReference5(String reference5) { this.reference5 = reference5; }
    public String getUserCategoryName() { return userCategoryName; }
    public void setUserCategoryName(String userCategoryName) { this.userCategoryName = userCategoryName; }
    public String getAccountingDate() { return accountingDate; }
    public void setAccountingDate(String accountingDate) { this.accountingDate = accountingDate; }
}