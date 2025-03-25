package com.ciandt.pms.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PRICE_TABLE_APPROVER")
@Audited
@AuditTable(value = "PRICE_TABLE_APPROVER_AUD")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "PriceTableApprover.findByMsa", query = "SELECT t FROM PriceTableApprover t"
                + " WHERE t.msa.codigoMsa = :msaCode"),
        @NamedQuery(name = "PriceTableApprover.findByLogin", query = "SELECT t from PriceTableApprover t WHERE t.login = ?1"),
        @NamedQuery(name = "PriceTableApprover.findByLoginAndMsa", query = "SELECT t from PriceTableApprover t "
                + " WHERE t.login = :login "
                + " AND t.msa.codigoMsa = :msaCode"
        )
})
public class PriceTableApprover implements Serializable {

    public static final String FIND_BY_MSA = "PriceTableApprover.findByMsa";
    public static final String FIND_BY_LOGIN = "PriceTableApprover.findByLogin";
    public static final String FIND_BY_LOGIN_AND_MSA = "PriceTableApprover.findByLoginAndMsa";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PriceTableApproverSeq")
    @SequenceGenerator(name = "PriceTableApproverSeq", sequenceName = "SQ_PTA_CD_PRICE_TB_APPROVER", allocationSize = 1)
    @Column(name = "PTA_CD_PRICE_TB_APPROVER", unique = true, nullable = false, precision = 18)
    private Long code;

    @Audited
    @Column(name = "PTA_TX_LOGIN", length = 20)
    private String login;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MSAA_CD_MSA")
    private Msa msa;
}
