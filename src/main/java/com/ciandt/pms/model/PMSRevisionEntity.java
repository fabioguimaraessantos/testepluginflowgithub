/**
 * @(#) PMSRevisionEntity.java
 * Copyright (c) 2009 Ci&T Software S/A.
 * All Rights Reserved.
 */

package com.ciandt.pms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.ciandt.pms.audit.listener.PMSRevisionListener;


/**
 * Entity gerado a partir da tabela REVINFO. Relacao de revisoes da auditoria.
 * 
 * @since 22/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Entity
@RevisionEntity(PMSRevisionListener.class)
@Table(name = "REVINFO")
public class PMSRevisionEntity {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Atributo gerado a partir da coluna REV.
     */
    @Id
    @GeneratedValue(generator = "PMSRevisionEntitySeq")
    @SequenceGenerator(name = "PMSRevisionEntitySeq", sequenceName = "HIBERNATE_SEQUENCE", allocationSize = 1)
    @RevisionNumber
    @Column(name = "REV", precision = 18, scale = 0)
    private Long rev;

    /**
     * Atributo gerado a partir da coluna REVTSTMP.
     */
    @RevisionTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REVTSTMP", length = 7)
    private Date revtstmp;

    /**
     * Atributo gerado a partir da coluna REVI_CD_AUTOR.
     */
    @Column(name = "REVI_CD_AUTOR", length = 50)
    private String codigoAutor;

    /**
     * @return the rev
     */
    public Long getRev() {
        return rev;
    }

    /**
     * @param rev
     *            the rev to set
     */
    public void setRev(final Long rev) {
        this.rev = rev;
    }

    /**
     * @return the revtstmp
     */
    public Date getRevtstmp() {
        return revtstmp;
    }

    /**
     * @param revtstmp
     *            the revtstmp to set
     */
    public void setRevtstmp(final Date revtstmp) {
        this.revtstmp = revtstmp;
    }

    /**
     * @return the codigoAutor
     */
    public String getCodigoAutor() {
        return codigoAutor;
    }

    /**
     * @param codigoAutor
     *            the codigoAutor to set
     */
    public void setCodigoAutor(final String codigoAutor) {
        this.codigoAutor = codigoAutor;
    }

}