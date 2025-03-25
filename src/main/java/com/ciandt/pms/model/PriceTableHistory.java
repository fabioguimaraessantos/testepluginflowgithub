package com.ciandt.pms.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PRICE_TABLE_HISTORY")
@NamedQueries({

        @NamedQuery(name = "PriceTableHistory.findHistoryByPriceTableId",
                    query = "SELECT h FROM PriceTableHistory h "
                         + " WHERE h.idPriceTable = ? "
                         + " ORDER BY id DESC"),
})
public class PriceTableHistory {

    public static final String FIND_ALL_BY_PRICE_TABLE = "PriceTableHistory.findHistoryByPriceTableId";

    @Id
    @GeneratedValue(generator = "PriceTableHistorySeq")
    @SequenceGenerator(name = "PriceTableHistorySeq", sequenceName = "SQ_PTHI_CD_PRICE_TABLE_HISTORY", allocationSize = 1)
    @Column(name = "PTHI_CD_PRICE_TABLE_HISTORY", unique = true, nullable = false, precision = 18, scale = 0)
    private Long id;

    @Column(name = "TAPR_CD_TABELA_PRECO")
    private Long idPriceTable;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PTHI_DT_UPDATE")
    private Date updatedIn;

    @Column(name = "PTHI_TX_STATUS", length = 20)
    private String status;

    @Column(name = "PTHI_TX_LOGIN", length = 20)
    private String login;

    @Column(name = "PTHI_DS_REASON", length = 100)
    private String reason;

    /* Getters and Setters */
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getIdPriceTable() {
        return idPriceTable;
    }
    public void setIdPriceTable(Long idPriceTable) {
        this.idPriceTable = idPriceTable;
    }
    public Date getUpdatedIn() {
        return updatedIn;
    }
    public void setUpdatedIn(Date updateIn) {
        this.updatedIn = updateIn;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}