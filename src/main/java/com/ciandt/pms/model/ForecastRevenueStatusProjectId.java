package com.ciandt.pms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class ForecastRevenueStatusProjectId implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FRSP_CD_MAPA_ALOCACAO", referencedColumnName = "MAAL_CD_MAPA_ALOCACAO", nullable = false)
	private MapaAlocacao allocationMapId;

	@Temporal(TemporalType.DATE)
	@Column(name = "FRSP_DT_RECEITA", length = 7, nullable = false)
	private Date revenueDate;
}
