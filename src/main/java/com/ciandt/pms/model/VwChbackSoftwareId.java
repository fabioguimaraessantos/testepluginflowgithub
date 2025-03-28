/*
 * @(#) VwChbackSoftwareId.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * VwChbackSoftwareId generated by hbm2java.
 *
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 13/07/2011 15:17:37
 * @version 09/01/19 1.1 10:08:00
 */
@Embeddable
public class VwChbackSoftwareId implements java.io.Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /** 
     * Constante para NamedQuery "VwChbackSoftwareId.findAll".
     */
    public static final String FIND_ALL = "VwChbackSoftwareId.findAll";

    /** Atributo gerado a partir da coluna SOSY_CD_LOGIN. */
    @Column(name = "SOSY_CD_LOGIN", length = 20)
    private String codigoLogin;

    /** Atributo gerado a partir da coluna SOSY_DT_MES. */
    @Column(name = "SOSY_DT_MES", length = 7)
    private Date dataMes;
    
    /** Atributo gerado a partir da coluna SOSY_CD_MNEMONICO. */
    @Column(name = "SOSY_CD_MNEMONICO", length = 20)
    private String codigoMnemonico;

    /**
     * Construtor default.
     */
    public VwChbackSoftwareId() {
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoLogin Valor do atributo codigoLogin;
     */
    public VwChbackSoftwareId(final String codigoLogin) {
        this.codigoLogin = codigoLogin;
    }

    /**
     * Construtor com preenchimento da entidade.
     * @param codigoLogin Valor do atributo codigoLogin;
     * @param dataMes Valor do atributo dataMes;
     */
    public VwChbackSoftwareId(final String codigoLogin,
            final Date dataMes) {
        this.codigoLogin = codigoLogin;
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo codigoLogin.<BR>

     * @return Valor do atributo codigoLogin.
     */
    public String getCodigoLogin() {
        return this.codigoLogin;
    }

    /**
     * Atualiza o valor do atributo codigoLogin.<BR>

     * @param codigoLogin Novo valor para o atributo codigoLogin.
     */
    public void setCodigoLogin(final String codigoLogin) {
        this.codigoLogin = codigoLogin;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>

     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>

     * @param dataMes Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }
    
    /**
     * Obtem o valor do atributo codigoMnemonico.<BR>

     * @return Valor do atributo codigoMnemonico.
     */
    public String getCodigoMnemonico() {
        return this.codigoMnemonico;
    }

    /**
     * Atualiza o valor do atributo codigoMnemonico.<BR>

     * @param codigoMnemonico Novo valor para o atributo codigoMnemonico.
     */
    public void setCodigoMnemonico(final String codigoMnemonico) {
        this.codigoMnemonico = codigoMnemonico;
    }

    /**
     * @see Object#equals(Object)
     * 
     * @param other - outra entidade.
     * 
     * @return retorna a entidade no formato de String
     */
    @Override
    public boolean equals(final Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof VwChbackSoftwareId)) {
            return false;
        }
        VwChbackSoftwareId castOther = (VwChbackSoftwareId) other;

        return ((this.getCodigoLogin() == castOther.getCodigoLogin()) || (this
                        .getCodigoLogin() != null
                        && castOther.getCodigoLogin() != null && this
                        .getCodigoLogin().equals(castOther.getCodigoLogin())))
                && ((this.getDataMes() == castOther.getDataMes()) || (this
                        .getDataMes() != null
                        && castOther.getDataMes() != null && this.getDataMes()
                        .equals(castOther.getDataMes())))
                && ((this.getCodigoMnemonico() == castOther.getCodigoMnemonico()) || (this
                        .getCodigoMnemonico() != null
                        && castOther.getCodigoMnemonico() != null && this.getCodigoMnemonico()
                        .equals(castOther.getCodigoMnemonico())));
    }

    /**
     * @see Object#hashCode()
     * @return retorna o codigo hash do objeto.
     */
    @Override
    public int hashCode() {
        int result = 17;

        result =
                37
                        * result
                        + (getCodigoLogin() == null ? 0 : this.getCodigoLogin()
                                .hashCode());
        result =
                37
                        * result
                        + (getDataMes() == null ? 0 : this.getDataMes()
                                .hashCode());
        result =
                37
                        * result
                        + (getCodigoMnemonico() == null ? 0 : this
                                .getCodigoMnemonico().hashCode());
        return result;
    }

}