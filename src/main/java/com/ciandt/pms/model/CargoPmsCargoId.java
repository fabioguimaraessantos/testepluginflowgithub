/*
 * @(#) CargoPmsCargoId.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CargoPmsCargoId generated by hbm2java.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 05/07/2012 12:54:26
 * @version 09/01/19 1.1 10:08:00
 */
@Embeddable
public class CargoPmsCargoId implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "CargoPmsCargoId.findAll".
     */
    public static final String FIND_ALL = "CargoPmsCargoId.findAll";

    
    /**
     * Atributo gerado a partir da coluna CAPM_CD_CARGO_PMS.
     */
    @Column(name = "CAPM_CD_CARGO_PMS", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoCargoPms;
    
    /**
     * Atributo gerado a partir da coluna CARG_CD_CARGO.
     */
    @Column(name = "CARG_CD_CARGO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoCargo;

    /**
     * Construtor default.
     */
    public CargoPmsCargoId() {
    }
    
    /**
     * Construtor default.
     */
    public CargoPmsCargoId(final Long codigoCargoPms, final Long codigoCargo) {
        this.codigoCargoPms = codigoCargoPms;
        this.codigoCargo = codigoCargo;
    }

    /**
     * @return the codigoCargoPms
     */
    public Long getCodigoCargoPms() {
        return codigoCargoPms;
    }

    /**
     * @param codigoCargoPms the codigoCargoPms to set
     */
    public void setCodigoCargoPms(final Long codigoCargoPms) {
        this.codigoCargoPms = codigoCargoPms;
    }

    /**
     * Obtem o valor do atributo codigoCargo.<BR>
     * Atributo gerado a partir da coluna CARG_CD_CARGO.
     * @return Valor do atributo codigoCargo.
     */
    public Long getCodigoCargo() {
        return codigoCargo;
    }

    /**
     * Atualiza o valor do atributo codigoCargo.<BR>
     * Atributo gerado a partir da coluna CARG_CD_CARGO.
     * @param codigoCargo Novo valor para o atributo codigoCargo.
     */
    public void setCodigoCargo(final Long codigoCargo) {
        this.codigoCargo = codigoCargo;
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
        if (!(other instanceof CargoPmsCargoId))
            return false;
        CargoPmsCargoId castOther = (CargoPmsCargoId) other;

        return ((this.getCodigoCargo() == castOther.getCodigoCargo()) || (this
                .getCodigoCargo() != null
                && castOther.getCodigoCargo() != null && this
                .getCodigoCargo().equals(castOther.getCodigoCargo())))
                && ((this.getCodigoCargo() == castOther.getCodigoCargo()) || (this
                        .getCodigoCargo() != null
                        && castOther.getCodigoCargo() != null && this
                        .getCodigoCargo().equals(castOther.getCodigoCargo())));
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
                        + (getCodigoCargo() == null ? 0 : this
                                .getCodigoCargo().hashCode());
        result =
                37
                        * result
                        + (getCodigoCargo() == null ? 0 : this.getCodigoCargo()
                                .hashCode());
        return result;
    }

}
