package com.ciandt.pms.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "INTEGRATE_LICENSE")
@NamedQueries({

        @NamedQuery(name = "IntegrateLicense.findByResourceDateProject",
                    query = "SELECT il FROM IntegrateLicense il "
                            + " WHERE il.licenseId = ? "
                            + " AND il.licenseDate = ? "
                            + " AND il.project = ? "),

        @NamedQuery(name = "IntegrateLicense.findByResourceDateProjectCostCenter",
                query = "SELECT il FROM IntegrateLicense il "
                        + " WHERE il.licenseId = ? "
                        + " AND il.licenseDate = ? "
                        + " AND il.project = ? "
                        + " AND il.costCenter = ?"),
})
public class IntegrateLicense {

    public static final String FIND_BY_RESOURCE_DATE_PROJECT = "IntegrateLicense.findByResourceDateProject";
    public static final String FIND_BY_RESOURCE_DATE_PROJECT_COST_CENTER = "IntegrateLicense.findByResourceDateProjectCostCenter";

    @Id
    @GeneratedValue(generator = "IntegrateLicenseSeq")
    @SequenceGenerator(name = "IntegrateLicenseSeq", sequenceName = "SQ_INLI_CD_INTEGRATE_LICENSE", allocationSize = 1)
    @Column(name = "INLI_CD_INTEGRATE_LICENSE", unique = true, nullable = false, precision = 18, scale = 0)
    private Long id;

    @Column(name = "TIRE_CD_TI_RECURSO", nullable = false, precision = 18, scale = 0)
    private Long licenseId;

    @Temporal(TemporalType.DATE)
    @Column(name = "INLI_DT_COMPETENCIA", nullable = false)
    private Date licenseDate;

    @Column(name = "INLI_NM_PROJETO")
    private String project;

    @Column(name = "INLI_IN_STATUS")
    private String status;

    @Column(name = "INLI_TX_MENSAGEM")
    private String message;

    @Column(name = "INLI_TX_CONTEUDO")
    private String content;

    @Column(name = "INLI_TX_DOC_NUMBER")
    private String documentNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INLI_DT_UPDATE")
    private Date updateIn;

    @Column(name = "INLI_NM_GRUPO_CUSTO")
    private String costCenter;

    /* Getters and Setters */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getLicenseId() {
        return licenseId;
    }
    public void setLicenseId(Long licenseId) {
        this.licenseId = licenseId;
    }
    public Date getLicenseDate() {
        return licenseDate;
    }
    public void setLicenseDate(Date licenseDate) {
        this.licenseDate = licenseDate;
    }
    public String getProject() {
        return project;
    }
    public void setProject(String project) {
        this.project = project;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDocumentNumber() {
        return documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    public Date getUpdateIn() {
        return updateIn;
    }
    public void setUpdateIn(Date updateIn) {
        this.updateIn = updateIn;
    }
    public String getCostCenter() {
        return costCenter;
    }
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }
}