package com.ciandt.pms.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by josef on 01/02/2017.
 */
@Entity
@Table(name = "TIPO_MODELO_NEGOCIO")
@NamedQueries({
        @NamedQuery(name = "TipoModeloNegocio.findAllActive", query = "SELECT t FROM TipoModeloNegocio t where t.indicadorAtivo = 'A' ORDER BY t.nomeTipoModeloNegocio")
})
public class TipoModeloNegocio implements java.io.Serializable {

    /** Constante para NamedQuery "TipoContrato.findAllActive". */
    public static final String FIND_ALL_ACTIVE = "TipoModeloNegocio.findAllActive";
    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributo gerado a partir da coluna TIMO_CD_TIPO_MODELO_NEGOCIO.
     */
    @Id
    @Column(name = "TIMO_CD_TIPO_MODELO_NEGOCIO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoTipoModeloNegocio;

    /**
     * Atributo gerado a partir da coluna TIMO_NM_TIPO_MODELO_NEGOCIO.
     */

    @Column(name = "TIMO_NM_TIPO_MODELO_NEGOCIO", length = 100)
    private String nomeTipoModeloNegocio;


    /**
     * Atributo gerado a partir da coluna TIMO_IN_ATIVO.
     */

    @Column(name = "TIMO_IN_ATIVO", length = 1)
    private String indicadorAtivo;

    /**
     * Construtor default.
     */
    public TipoModeloNegocio() {
    }

    /**
     * Construtor com preenchimento da entidade.
     *
     * @param codigoTipoModeloNegocio
     *            Valor do atributo codigoTipoModeloNegocio;
     */
    public TipoModeloNegocio(final Long codigoTipoModeloNegocio) {
        this.codigoTipoModeloNegocio = codigoTipoModeloNegocio;
    }

    /**
     * Obtem o valor do atributo codigoTipoContrato.<BR>
     * Atributo gerado a partir da coluna TICO_CD_TIPO_CONTRATO.
     *
     * @return Valor do atributo codigoTipoModeloNegocio.
     */
    public Long getCodigoTipoModeloNegocio() {
        return this.codigoTipoModeloNegocio;
    }

    /**
     * Atualiza o valor do atributo codigoTipoContrato.<BR>
     * Atributo gerado a partir da coluna TIMO_CD_TIPO_MODELO_NEGOCIO.
     *
     * @param codigoTipoModeloNegocio
     *            Novo valor para o atributo codigoTipoModeloNegocio.
     */
    public void setCodigoTipoModeloNegocio(final Long codigoTipoModeloNegocio) {
        this.codigoTipoModeloNegocio = codigoTipoModeloNegocio;
    }

    /**
     * Obtem o valor do atributo nomeTipoModeloNegocio.<BR>
     * Atributo gerado a partir da coluna TIMO_NM_TIPO_MODELO_NEGOCIO.
     *
     * @return Valor do atributo nomeTipoModeloNegocio.
     */
    public String getNomeTipoModeloNegocio() {
        return this.nomeTipoModeloNegocio;
    }

    /**
     * Atualiza o valor do atributo nomeTipoModeloNegocio.<BR>
     * Atributo gerado a partir da coluna TIMO_NM_TIPO_MODELO_NEGOCIO.
     *
     * @param nomeTipoModeloNegocio
     *            Novo valor para o atributo nomeTipoContrato.
     */
    public void setNomeTipoModeloNegocio(final String nomeTipoModeloNegocio) {
        this.nomeTipoModeloNegocio = nomeTipoModeloNegocio;
    }

    /**
     * Obtem o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna TICO_IN_ATIVO.
     *
     * @return Valor do atributo indicadorAtivo.
     */
    public String getIndicadorAtivo() {
        return this.indicadorAtivo;
    }

    /**
     * Atualiza o valor do atributo indicadorAtivo.<BR>
     * Atributo gerado a partir da coluna TICO_IN_ATIVO.
     *
     * @param indicadorAtivo
     *            Novo valor para o atributo indicadorAtivo.
     */
    public void setIndicadorAtivo(final String indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }

    /**
     * Construtor com preenchimento da entidade.
     *
     * @param codigoTipoModeloNegocio
     *            Valor do atributo codigoTipoModeloNegocio;
     * @param nomeTipoModeloNegocio
     *            Valor do atributo nomeTipoModeloNegocio;
     * @param indicadorAtivo
     *            Valor do atributo indicadorAtivo;
     */
    public TipoModeloNegocio(final Long codigoTipoModeloNegocio,
                        final String nomeTipoModeloNegocio,
                        final String indicadorAtivo) {
        this.codigoTipoModeloNegocio = codigoTipoModeloNegocio;
        this.nomeTipoModeloNegocio = nomeTipoModeloNegocio;
        this.indicadorAtivo = indicadorAtivo;
    }

    /**
     * @see Object#toString()
     * @return toString
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append(getClass().getName()).append("@") .append(Integer.toHexString(hashCode())).append(" [");
        buffer.append("codigoTipoModeloNegocio").append("='").append(getCodigoTipoModeloNegocio()).append("' ");
        buffer.append("nomeTipoModeloNegocio").append("='").append(getNomeTipoModeloNegocio()).append("' ");
        buffer.append("indicadorAtivo").append("='").append(getIndicadorAtivo()).append("' ");

        buffer.append("]");

        return buffer.toString();
    }

}
