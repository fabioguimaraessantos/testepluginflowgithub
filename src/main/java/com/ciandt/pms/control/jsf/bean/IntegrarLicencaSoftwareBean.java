package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.components.ISelect;
import com.ciandt.pms.control.jsf.components.impl.InvoiceMegaSelect;
import com.ciandt.pms.control.jsf.components.impl.InvoiceProjectMegaSelect;
import com.ciandt.pms.control.jsf.components.impl.ObjectedSelected;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwProjetoRow;
import com.ciandt.pms.model.vo.LicencaSwUserRow;
import com.ciandt.pms.util.DateUtil;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.*;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntegrarLicencaSoftwareBean implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    private boolean isAllPending;

    /** to LicencaSwUserRow. */
    private LicencaSwUserRow toUser = null;

    /** to LicencaSwProjetoRow. */
    private LicencaSwProjetoRow toProject = null;

    private List<String> monthList = Arrays.asList("01", "02", "03", "04",
            "05", "06", "07", "08", "09", "10", "11", "12");

    private List<String> yearList = new ArrayList<String>();

    private String tipoRecurso = null;

    private String nomeTiRecurso = null;

    private List<String> tiRecursoList = new ArrayList<String>();

    private Map<String, Long> tiRecursoMap = new HashMap<String, Long>();

    private String status = null;

    private String month;

    private String year;

    private Date dateFrom = null;

    private Date dateTo = null;

    private Long codigoTiRecursoFiltered;

    private Date monthDateFiltered;

    private String statusFiltered;

    private Boolean hasIncIntegration;

    /** List para o combobox de Empresa. */
    private ISelect<Empresa> companySelect;

    private ISelect<TiRecurso> resourceTypeSelect;

    private Long invoiceNumber;

    private Long licenseID;

    private List<Long> invoiceNumberList;

    private List<Long> licenseIDList;


    private ISelect invoiceProjectMegaSelect;

    private List<String> tipoTiRecursoList;

    private List<LicencaSwProjetoRow> licencaSwProjetoRowList = new ArrayList<LicencaSwProjetoRow>();

    private List<LicencaSwDetail> licencaSwProjetoRowDetailList = new ArrayList<LicencaSwDetail>();

    private BigDecimal valorParcial;

    private Empresa empresa;

    private boolean telaIntegrate = true;

    private List<LicencaSwDetail> previousLicencaSwProjetoRowDetailList = new ArrayList<LicencaSwDetail>();


    private List<LicencaSwUserRow> licencaSwUserRowList = new ArrayList<LicencaSwUserRow>();

    private List<LicencaSwDetail> licencaSwUserRowListDetail = new ArrayList<LicencaSwDetail>();

    public void reset() {
        this.dateFrom = null;
        this.dateTo = null;
        this.month = null;
        this.year = null;
        this.status = null;
        this.monthDateFiltered = null;
        this.statusFiltered = null;
        this.tipoRecurso = "SOFTWARE_PROJECT";
        this.nomeTiRecurso = null;
        this.licencaSwProjetoRowList = new ArrayList<LicencaSwProjetoRow>();
        this.licencaSwUserRowList = new ArrayList<LicencaSwUserRow>();
        this.licencaSwProjetoRowDetailList = new ArrayList<LicencaSwDetail>();
        this.previousLicencaSwProjetoRowDetailList = new ArrayList<LicencaSwDetail>();
        this.licencaSwUserRowListDetail = new ArrayList<LicencaSwDetail>();
        this.isAllPending = true;
        this.toProject = null;
        this.toUser = null;
        this.codigoTiRecursoFiltered = null;
        this.companySelect = null;
        this.resourceTypeSelect = null;
        this.licenseID = null;
        this.licenseIDList  = new ArrayList<>();
        this.invoiceProjectMegaSelect = null;
        this.tipoTiRecursoList = new ArrayList<>();
        this.hasIncIntegration = Boolean.FALSE;
        this.resetSearchFilters();
    }

    public void resetSearchFilters(){
        this.invoiceNumber = null;
        this.invoiceNumberList = new ArrayList<>();
        this.invoiceProjectMegaSelect = new InvoiceProjectMegaSelect(new ArrayList<>());
        this.licenseID = null;
        this.licenseIDList  = new ArrayList<>();
        this.valorParcial = BigDecimal.ZERO;
        this.telaIntegrate = true;
    }

    public Boolean isProjectLicense(){
        if(this.licencaSwProjetoRowList != null && this.licencaSwProjetoRowList.size() > 0) {
            return Boolean.TRUE;
        }else {
            return Boolean.FALSE;
        }
    }

    public List<LicencaSwDetail> getLicencaSwProjetoRowDetailList() {
        return licencaSwProjetoRowDetailList;
    }

    public void setLicencaSwProjetoRowDetailList(List<LicencaSwDetail> licencaSwProjetoRowDetailList) {
        this.licencaSwProjetoRowDetailList = licencaSwProjetoRowDetailList;
    }

    public List<LicencaSwDetail> getLicencaSwUserRowListDetail() {
        return licencaSwUserRowListDetail;
    }

    public void setLicencaSwUserRowListDetail(List<LicencaSwDetail> licencaSwUserRowListDetail) {
        this.licencaSwUserRowListDetail = licencaSwUserRowListDetail;
    }

    public List<String> getMonthList() {
        return monthList;
    }

    public void setMonthList(List<String> monthList) {
        this.monthList = monthList;
    }

    public List<String> getYearList() {
        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yList.add("" + i);
        }

        yearList = yList;

        return yearList;
    }

    public boolean getIsAllPending() {
        return isAllPending;
    }

    public void setIsAllPending(boolean allPending) {
        isAllPending = allPending;
    }

    public LicencaSwUserRow getToUser() {
        return toUser;
    }

    public void setToUser(LicencaSwUserRow toUser) {
        this.toUser = toUser;
    }

    public LicencaSwProjetoRow getToProject() {
        return toProject;
    }

    public void setToProject(LicencaSwProjetoRow toProject) {
        this.toProject = toProject;
    }

    public void setYearList(List<String> yearList) {
        this.yearList = yearList;
    }

    public String getTipoRecurso() {
        return tipoRecurso;
    }

    public void setTipoRecurso(String tipoRecurso) {
        this.tipoRecurso = tipoRecurso;
    }

    public String getNomeTiRecurso() {
        return nomeTiRecurso;
    }

    public void setNomeTiRecurso(String nomeTiRecusto) {
        this.nomeTiRecurso = nomeTiRecusto;
    }

    public List<String> getTiRecursoList() {
        return tiRecursoList;
    }

    public void setTiRecursoList(List<String> tiRecursoList) {
        this.tiRecursoList = tiRecursoList;
    }

    public Map<String, Long> getTiRecursoMap() {
        return tiRecursoMap;
    }

    public void setTiRecursoMap(Map<String, Long> tiRecursoMap) {
        this.tiRecursoMap = tiRecursoMap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<LicencaSwProjetoRow> getLicencaSwProjetoRowList() {
        return licencaSwProjetoRowList;
    }

    public void setLicencaSwProjetoRowList(List<LicencaSwProjetoRow> licencaSwProjetoRowList) {
        this.licencaSwProjetoRowList = licencaSwProjetoRowList;
    }

    public List<LicencaSwUserRow> getLicencaSwUserRowList() {
        return licencaSwUserRowList;
    }

    public void setLicencaSwUserRowList(List<LicencaSwUserRow> licencaSwUserRowList) {
        this.licencaSwUserRowList = licencaSwUserRowList;
    }

    public void resetResultList() {
        this.licencaSwUserRowList = new ArrayList<LicencaSwUserRow>();
        this.licencaSwProjetoRowList = new ArrayList<LicencaSwProjetoRow>();
    }

    public Date getMonthDate(){ return DateUtil.getDate(this.getMonth(), this.getYear()); }

    public Date getMonthDateFiltered() { return monthDateFiltered; }

    public void setMonthDateFiltered(Date monthDateFiltered) { this.monthDateFiltered = monthDateFiltered; }

    public String getStatusFiltered() { return statusFiltered; }

    public void setStatusFiltered(String statusFiltered) { this.statusFiltered = statusFiltered; }

    public Long getCodigoTiRecursoFiltered() { return codigoTiRecursoFiltered; }

    public void setCodigoTiRecursoFiltered(Long codigoTiRecursoFiltered) { this.codigoTiRecursoFiltered = codigoTiRecursoFiltered;    }

    public ISelect<Empresa> getCompanySelect() {
        return companySelect;
    }

    public void setCompanySelect(ISelect companySelect) {
        this.companySelect = companySelect;
    }

    public List<String> getTipoTiRecursoList() {
        return tipoTiRecursoList;
    }

    public void setTipoTiRecursoList(List<String> tipoTiRecursoList) {
        this.tipoTiRecursoList = tipoTiRecursoList;
    }

    public Boolean getHasIncIntegration() {
        return hasIncIntegration;
    }

    public void setHasIncIntegration(Boolean hasIncIntegration) {
        this.hasIncIntegration = hasIncIntegration;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }
    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTo);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.dateTo = calendar.getTime();
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<Long> getInvoiceNumberList() {
        return invoiceNumberList;
    }

    public void setInvoiceNumberList(List<Long> invoiceNumberList) {
        this.invoiceNumberList = invoiceNumberList;
    }

    public ISelect getInvoiceProjectMegaSelect() {
        return invoiceProjectMegaSelect;
    }

    public void setInvoiceProjectMegaSelect(ISelect invoiceProjectMegaSelect) {
        this.invoiceProjectMegaSelect = invoiceProjectMegaSelect;
    }

    public Long getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(Long licenseID) {
        this.licenseID = licenseID;
    }

    public List<Long> getLicenseIDList() {
        return licenseIDList;
    }

    public void setLicenseIDList(List<Long> licenseIDList) {
        this.licenseIDList = licenseIDList;
    }

    public List<LicencaSwDetail> getPreviousLicencaSwProjetoRowDetailList() {
        return previousLicencaSwProjetoRowDetailList;
    }

    public void setPreviousLicencaSwProjetoRowDetailList(List<LicencaSwDetail> previousLicencaSwProjetoRowDetailList) {
        this.previousLicencaSwProjetoRowDetailList = previousLicencaSwProjetoRowDetailList;
    }

    public BigDecimal getValorParcial() {
        return valorParcial;
    }

    public void setValorParcial(BigDecimal valorParcial) {
        this.valorParcial = valorParcial;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public boolean isTelaIntegrate() {
        return telaIntegrate;
    }

    public void setTelaIntegrate(boolean telaIntegrate) {
        this.telaIntegrate = telaIntegrate;
    }

    public ISelect<TiRecurso> getResourceTypeSelect() {
        return resourceTypeSelect;
    }

    public void setResourceTypeSelect(ISelect<TiRecurso> resourceTypeSelect) {
        this.resourceTypeSelect = resourceTypeSelect;
    }
}
