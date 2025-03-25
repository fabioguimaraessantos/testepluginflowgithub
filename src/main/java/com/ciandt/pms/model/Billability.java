package com.ciandt.pms.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BILLABILITY")
@NamedQueries({
        @NamedQuery(name = "Billability.findAllActive", query = "SELECT b FROM Billability b "
                + " WHERE b.indicadorAtivo = 'Y'"),
        @NamedQuery(name = "Billability.findByName", query = "SELECT b FROM Billability b "
                + " WHERE UPPER(b.nomeBillability) = UPPER(?) ")
})
public class Billability implements java.io.Serializable, Cloneable {

    /** Constante para NamedQuery "Billability.findAllActive". */
    public static final String FIND_ALL_ACTIVE = "Billability.findAllActive";

    public static final String FIND_BY_NAME = "Billability.findByName";

    /**
     * Atributo gerado a partir da coluna BILL_CD_BILLABILITY.
     */
    @Id
    @Column(name = "BILL_CD_BILLABILITY", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoBillability;

    /**
     * Atributo gerado a partir da coluna BILL_NM_BILLABILITY.
     */

    @Column(name = "BILL_NM_BILLABILITY", nullable = false, length = 100)
    private String nomeBillability;

    /**
     * Atributo gerado a partir da coluna BILL_NM_BILLABILITY.
     */

    @Column(name = "BILL_SG_BILLABILITY", nullable = false, length = 2)
    private String siglaBillability;

    /**
     * Atributo gerado a partir da coluna AREA_IN_ATIVO.
     */
    @Column(name = "BILL_IN_ATIVO", nullable = false, length = 1)
    private String indicadorAtivo;

    /**
     * Relacionamento um pra muitos.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "billability")
    private List<PessoaBillability> pessoaBillabilities = new ArrayList<PessoaBillability>(
            0);

    public Long getCodigoBillability() {
        return codigoBillability;
    }

    public void setCodigoBillability(Long codigoBillability) {
        this.codigoBillability = codigoBillability;
    }

    public String getNomeBillability() {
        return nomeBillability;
    }

    public void setNomeBillability(String nomeBillability) {
        this.nomeBillability = nomeBillability;
    }

    public String getSiglaBillability() {
        return siglaBillability;
    }

    public void setSiglaBillability(String siglaBillability) {
        this.siglaBillability = siglaBillability;
    }

    public String getIndicadorAtivo() {
        return indicadorAtivo;
    }

    public void setIndicadorAtivo(String indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }

    public List<PessoaBillability> getPessoaBillabilities() {
        return pessoaBillabilities;
    }

    public void setPessoaBillabilities(List<PessoaBillability> pessoaBillabilities) {
        this.pessoaBillabilities = pessoaBillabilities;
    }
}