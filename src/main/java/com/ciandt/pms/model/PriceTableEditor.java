package com.ciandt.pms.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "PRICE_TABLE_EDITOR")
@Audited
@AuditTable(value = "PRICE_TABLE_EDITOR_AUD")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "PriceTableEditor.findByMsa", query = "SELECT t FROM PriceTableEditor t"
                + " WHERE t.msa.codigoMsa = :msaCode"),
        @NamedQuery(name = "PriceTableEditor.findByLogin", query = "SELECT t from PriceTableEditor t WHERE t.login = ?1"),
        @NamedQuery(name = "PriceTableEditor.findByLoginAndMsa", query = "SELECT t from PriceTableEditor t " +
                "WHERE t.login = :login " +
                "AND t.msa.codigoMsa = :msaCode ")
})
public class PriceTableEditor implements Serializable {

    public static final String FIND_BY_MSA = "PriceTableEditor.findByMsa";
    public static final String FIND_BY_LOGIN = "PriceTableEditor.findByLogin";
    public static final String FIND_BY_LOGIN_AND_MSA = "PriceTableEditor.findByLoginAndMsa";

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PriceTableEditorSeq")
    @SequenceGenerator(name = "PriceTableEditorSeq", sequenceName = "SQ_PTE_CD_PRICE_TB_EDITOR", allocationSize = 1)
    @Column(name = "PTE_CD_PRICE_TB_EDITOR", unique = true, nullable = false, precision = 18)
    private Long code;

    @Audited
    @Column(name = "PTE_TX_LOGIN", length = 20)
    private String login;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MSAA_CD_MSA")
    private Msa msa;
}
