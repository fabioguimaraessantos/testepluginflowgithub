package com.ciandt.pms.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class PessoaBillabilityId implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "PessoaBillabilityId.findAll".
     */
    public static final String FIND_ALL = "PessoaBillabilityId.findAll";

    @Column(name = "PESS_CD_PESSOA", nullable = false, precision = 18, scale = 0)
    private Long codigoPessoa;

    @Column(name = "BILL_CD_BILLABILITY", nullable = false, precision = 18, scale = 0)
    private Long codigoBillability;

    @Temporal(TemporalType.DATE)
    @Column(name = "PEBI_DT_INICIO", nullable = false, length = 7)
    private Date dataInicio;

    /**
     * Construtor default.
     */
    public PessoaBillabilityId() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoPessoa Valor do atributo codigoPessoa;
     * @param codigoBillability Valor do atributo codigoBillability;
     */
    public PessoaBillabilityId(Long codigoPessoa, Long codigoBillability, Date dataInicio) {
        this.codigoPessoa = codigoPessoa;
        this.codigoBillability = codigoBillability;
        this.dataInicio = dataInicio;
    }

    /**
     * Obtem o valor do atributo codigoPessoa.<BR>

     * @return Valor do atributo codigoPessoa.
     */
    public Long getCodigoPessoa() {
        return this.codigoPessoa;
    }

    /**
     * Atualiza o valor do atributo codigoPessoa.<BR>

     * @param codigoPessoa Novo valor para o atributo codigoPessoa.
     */
    public void setCodigoPessoa(Long codigoPessoa) {
        this.codigoPessoa = codigoPessoa;
    }

    /**
     * Obtem o valor do atributo codigoDealFiscal.<BR>

     * @return Valor do atributo codigoDealFiscal.
     */
    public Long getCodigoBillability() {
        return this.codigoBillability;
    }

    /**
     * Atualiza o valor do atributo codigoBillability.<BR>

     * @param codigoBillability Novo valor para o atributo codigoBillability.
     */
    public void setCodigoBillability(Long codigoBillability) {
        this.codigoBillability = codigoBillability;
    }

    /**
     * Obtem o valor do atributo dataInicio.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_INICIO.
     * @return Valor do atributo dataInicio.
     */
    public Date getDataInicio() {
        return this.dataInicio;
    }

    /**
     * Atualiza o valor do atributo dataInicio.<BR>
     * Atributo gerado a partir da coluna PEBI_DT_INICIO.
     * @param dataInicio Novo valor para o atributo dataInicio.
     */
    public void setDataInicio(final Date dataInicio) {
        this.dataInicio = dataInicio;
    }


    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof PessoaBillabilityId))
            return false;
        PessoaBillabilityId castOther = (PessoaBillabilityId) other;

        return ((this.getCodigoPessoa() == castOther
                .getCodigoPessoa()) || (this
                .getCodigoPessoa() != null
                && castOther.getCodigoPessoa() != null && this
                .getCodigoPessoa().equals(
                        castOther.getCodigoPessoa())))
                && ((this.getCodigoBillability() == castOther
                .getCodigoBillability()) || (this.getCodigoBillability() != null
                && castOther.getCodigoBillability() != null && this
                .getCodigoBillability().equals(
                        castOther.getCodigoBillability())))
                && ((this.getDataInicio() == castOther
                .getDataInicio()) || (this.getDataInicio() != null
                && castOther.getDataInicio() != null && this
                .getDataInicio().equals(
                        castOther.getDataInicio())));
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 17;

        result =
                37
                        * result
                        + (getCodigoPessoa() == null ? 0 : this
                        .getCodigoPessoa().hashCode());
        result =
                37
                        * result
                        + (getCodigoBillability() == null ? 0 : this
                        .getCodigoBillability().hashCode());
        result =
                37
                        * result
                        + (getDataInicio() == null ? 0 : this
                        .getDataInicio().hashCode());
        return result;
    }

}