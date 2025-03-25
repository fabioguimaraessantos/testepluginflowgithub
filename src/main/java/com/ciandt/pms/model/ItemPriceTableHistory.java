package com.ciandt.pms.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "ITEM_PRICE_TABLE_HISTORY")
@NamedQueries({

        @NamedQuery(name = "ItemPriceTableHistory.findItemsToApproveByPriceTableId",
                query = "SELECT i FROM ItemPriceTableHistory i"
                        + " WHERE i.updatedIn IS NULL "
                        + " AND i.idPriceTable = ? "),

        @NamedQuery(name = "ItemPriceTableHistory.findAllItemsHistoryByPriceTableId",
                query = "SELECT i FROM ItemPriceTableHistory i"
                        + " WHERE i.idPriceTable = ? "),

        @NamedQuery(name = "ItemPriceTableHistory.findItemsHistoryByPriceTableId",
                query = "SELECT i FROM ItemPriceTableHistory i"
                        + " WHERE i.updatedIn IS NOT NULL "
                        + " AND i.idPriceTable = ? "),

        @NamedQuery(name = "ItemPriceTableHistory.findAllItemsHistoryByPriceTableHistory",
                query = "SELECT i FROM ItemPriceTableHistory i"
                        + " WHERE i.idPriceTableHistory = ? "),

        @NamedQuery(name = "ItemPriceTableHistory.findAllItemsHistoryByPriceTableHistories",
                query = "SELECT i FROM ItemPriceTableHistory i"
                        + " WHERE i.idPriceTableHistory IN (:histories) "
                        + " ORDER BY i.idPriceTableHistory DESC "),
})
public class ItemPriceTableHistory {

    public static final String FIND_ALL_BY_PRICE_TABLE = "ItemPriceTableHistory.findAllItemsHistoryByPriceTableId";
    public static final String FIND_TO_APPROVE_BY_PRICE_TABLE = "ItemPriceTableHistory.findItemsToApproveByPriceTableId";
    public static final String FIND_HISTORY_BY_PRICE_TABLE = "ItemPriceTableHistory.findItemsHistoryByPriceTableId";
    public static final String FIND_ALL_BY_PRICE_TABLE_HISTORY = "ItemPriceTableHistory.findAllItemsHistoryByPriceTableHistory";
    public static final String FIND_ALL_BY_PRICE_TABLE_HISTORIES = "ItemPriceTableHistory.findAllItemsHistoryByPriceTableHistories";

    @Id
    @GeneratedValue(generator = "ItemPriceTableHistorySeq")
    @SequenceGenerator(name = "ItemPriceTableHistorySeq", sequenceName = "SQ_IPTH_CD_ITEM_PRICE_TABLE_HISTORY", allocationSize = 1)
    @Column(name = "IPTH_CD_ITEM_PRICE_TABLE_HISTORY", unique = true, nullable = false, precision = 18, scale = 0)
    private Long id;

    @Column(name = "PTHI_CD_PRICE_TABLE_HISTORY", precision = 18)
    private Long idPriceTableHistory;

    @Column(name = "TAPR_CD_TABELA_PRECO", precision = 18)
    private Long idPriceTable;

    @Column(name = "PEVE_CD_PERFIL_VENDIDO", precision = 18)
    private Long idSaleProfile;

    @Column(name = "IPTH_VL_INITIAL", precision = 22, scale = 0)
    private BigDecimal initialValue;

    @Column(name = "IPTH_PR_INITIAL", precision = 15, scale = 13)
    private BigDecimal initialPercent;

    @Column(name = "IPTH_VL_UPDATED", precision = 22, scale = 0)
    private BigDecimal updatedValue;

    @Column(name = "IPTH_PR_UPDATED", precision = 15, scale = 13)
    private BigDecimal updatedPercent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "IPTH_DT_UPDATE")
    private Date updatedIn;

    @Column(name = "IPTH_IN_APPROVED", length = 1)
    private String indicadorApproved;

    @Column(name = "IPTH_IN_ACTION_STATUS", length = 1)
    private String indicadorActionStatus;

    /* Getters and Setters */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPriceTableHistory() {
        return idPriceTableHistory;
    }

    public void setIdPriceTableHistory(Long idPriceTableHistory) {
        this.idPriceTableHistory = idPriceTableHistory;
    }

    public Long getIdPriceTable() {
        return idPriceTable;
    }

    public void setIdPriceTable(Long idPriceTable) {
        this.idPriceTable = idPriceTable;
    }

    public Long getIdSaleProfile() {
        return idSaleProfile;
    }

    public void setIdSaleProfile(Long idSaleProfile) {
        this.idSaleProfile = idSaleProfile;
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public BigDecimal getInitialPercent() {
        return initialPercent;
    }

    public void setInitialPercent(BigDecimal initialPercent) {
        this.initialPercent = initialPercent;
    }

    public BigDecimal getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(BigDecimal updatedValue) {
        this.updatedValue = updatedValue;
    }

    public BigDecimal getUpdatedPercent() {
        return updatedPercent;
    }

    public void setUpdatedPercent(BigDecimal updatedPercent) {
        this.updatedPercent = updatedPercent;
    }

    public Date getUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(Date updatedIn) {
        this.updatedIn = updatedIn;
    }

    public String getIndicadorApproved() {
        return indicadorApproved;
    }

    public void setIndicadorApproved(String indicadorApproved) {
        this.indicadorApproved = indicadorApproved;
    }

    public String getIndicadorActionStatus() {
        return indicadorActionStatus;
    }

    public void setIndicadorActionStatus(String indicadorActionStatus) {
        this.indicadorActionStatus = indicadorActionStatus;
    }
}