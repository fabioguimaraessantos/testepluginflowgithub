package com.ciandt.pms.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ContratoPraticaAudId implements java.io.Serializable {
	
	private static final long serialVersionUID = -2617534381945672220L;

	@Column(name = "COPR_CD_CONTRATO_PRATICA", nullable = false, precision = 18, scale = 0)
	private Long codigoContratoPratica;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REV", nullable = false, insertable = false, updatable = false)
	private PMSRevisionEntity revinfo;

	public ContratoPraticaAudId() {
	}

	public Long getCodigoContratoPratica() {
		return codigoContratoPratica;
	}

	public void setCodigoContratoPratica(Long codigoContratoPratica) {
		this.codigoContratoPratica = codigoContratoPratica;
	}

	public PMSRevisionEntity getRevinfo() {
		return revinfo;
	}

	public void setRevinfo(PMSRevisionEntity revinfo) {
		this.revinfo = revinfo;
	}

}
