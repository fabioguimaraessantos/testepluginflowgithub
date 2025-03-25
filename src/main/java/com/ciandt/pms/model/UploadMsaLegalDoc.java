package com.ciandt.pms.model;

import org.jsefa.csv.annotation.CsvDataType;
import org.jsefa.csv.annotation.CsvField;

import java.util.Date;
import java.util.List;
import java.util.Set;

@CsvDataType
public class UploadMsaLegalDoc implements java.io.Serializable {


    @CsvField(pos = 1)
    private String msa;

    @CsvField(pos = 2)
    private String projectDescription;

    @CsvField(pos = 3)
    private String jiraCp;

    @CsvField(pos = 4)
    private String jiraLegal;

    @CsvField(pos = 5)
    private String sow;

    @CsvField(pos = 6)
    private String po;

    @CsvField(pos = 7)
    private String type;

    @CsvField(pos = 8)
    private String currency;

    @CsvField(pos = 9)
    private String fte;

    @CsvField(pos = 10)
    private String hasLimit;

    @CsvField(pos = 11)
    private String totalContractAmount;

    @CsvField(pos = 12)
    private String typeAmountExpenses;

    @CsvField(pos = 13)
    private String totalAmountExpenses;

    @CsvField(pos = 14)
    private String percentExpenses;

    @CsvField(pos = 15)
    private String status;

    @CsvField(pos = 16)
    private String startDate;

    @CsvField(pos = 17)
    private String endDate;

    @CsvField(pos = 18)
    private String indeterminate;

    @CsvField(pos = 19)
    private String notes;

    @CsvField(pos = 20)
    private String textoMesIpca;

    @CsvField(pos = 21)
    private List<String> tipoServico;

    @CsvField(pos = 22)
    private List<String> base;

    @CsvField(pos = 23)
    private List<String> cnpj;

    private Set<Long> tipoServicoIds;

    private Set<Long> baseIds;

    private Date startDateConverted;

    private Date endDateConverted;


    // Getters and Setters
    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getJiraCp() {
        return jiraCp;
    }

    public void setJiraCp(String jiraCp) {
        this.jiraCp = jiraCp;
    }

    public String getJiraLegal() {
        return jiraLegal;
    }

    public void setJiraLegal(String jiraLegal) {
        this.jiraLegal = jiraLegal;
    }

    public String getSow() {
        return sow;
    }

    public void setSow(String sow) {
        this.sow = sow;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFte() {
        return fte;
    }

    public void setFte(String fte) {
        this.fte = fte;
    }

    public String getTotalContractAmount() {
        return totalContractAmount;
    }

    public void setTotalContractAmount(String totalContractAmount) {
        this.totalContractAmount = totalContractAmount;
    }

    public String getTypeAmountExpenses() {
        return typeAmountExpenses;
    }

    public void setTypeAmountExpenses(String typeAmountExpenses) {
        this.typeAmountExpenses = typeAmountExpenses;
    }

    public String getTotalAmountExpenses() {
        return totalAmountExpenses;
    }

    public void setTotalAmountExpenses(String totalAmountExpenses) {
        this.totalAmountExpenses = totalAmountExpenses;
    }

    public String getPercentExpenses() {
        return percentExpenses;
    }

    public void setPercentExpenses(String percentExpenses) {
        this.percentExpenses = percentExpenses;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(String indeterminate) {
        this.indeterminate = indeterminate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMsa() {return msa;}

    public void setMsa(String msa) {this.msa = msa;}


    public String getHasLimit() {
        return hasLimit;
    }

    public void setHasLimit(String hasLimit) {
        this.hasLimit = hasLimit;
    }

    public String getTextoMesIpca() {
        return textoMesIpca;
    }

    public void setTextoMesIpca(String textoMesIpca) {
        this.textoMesIpca = textoMesIpca;
    }

    public List<String> getTipoServico() {return tipoServico;}

    public void setTipoServico(List<String> tipoServico) {this.tipoServico = tipoServico;}

    public List<String> getBase() {return base;}

    public void setBase(List<String> base) {this.base = base;}

    public List<String> getCnpj() {return cnpj;}

    public void setCnpj(List<String> cnpj) {this.cnpj = cnpj;}

    public Set<Long> getTipoServicoIds() {
        return tipoServicoIds;
    }

    public void setTipoServicoIds(Set<Long> tipoServicoIds) {
        this.tipoServicoIds = tipoServicoIds;
    }

    public Set<Long> getBaseIds() {
        return baseIds;
    }

    public void setBaseIds(Set<Long> baseIds) {
        this.baseIds = baseIds;
    }

    public Date getStartDateConverted() { return startDateConverted;}

    public void setStartDateConverted(Date startDateConverted) { this.startDateConverted = startDateConverted;}

    public Date getEndDateConverted() { return endDateConverted;}

    public void setEndDateConverted(Date endDateConverted) { this.endDateConverted = endDateConverted; }
}