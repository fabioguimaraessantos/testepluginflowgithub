package com.ciandt.pms.persistence.dao.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ciandt.pms.model.AreaOrcamentaria;
import com.ciandt.pms.model.GrupoCusto;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;


/**
 * The persistent class for the GRUPO_CUSTO_AREA_SUB_ORCAMENTA database table.
 * 
 */
@Entity
@Audited
@AuditTable(value = "GRUPO_CUSTO_AREA_ORCAMENTARIA_AUD")
@Table(name="GRUPO_CUSTO_AREA_ORCAMENTARIA")
@NamedQueries({
	@NamedQuery(name="GrupoCustoAreaOrcamentaria.findAll", query="SELECT g FROM GrupoCustoAreaOrcamentaria g"),
	@NamedQuery(name="GrupoCustoAreaOrcamentaria.findByGrupoCustoId", query="SELECT g FROM GrupoCustoAreaOrcamentaria g "
			+ "WHERE g.grupoCusto.codigoGrupoCusto = :codigoGrupoCusto"),
	@NamedQuery(name = "GrupoCustoAreaOrcamentaria.findMaxDataInicioVigenciaByCodigoGrupoCusto", query = "SELECT t FROM GrupoCustoAreaOrcamentaria t "
			+ "WHERE t.grupoCusto.codigoGrupoCusto = :codigoGrupoCusto "
			+ "AND t.dataInicio = (SELECT MAX(c.dataInicio) FROM GrupoCustoAreaOrcamentaria c WHERE c.grupoCusto.codigoGrupoCusto = :codigoGrupoCusto)"),
	@NamedQuery(name = "GrupoCustoAreaOrcamentaria.findByAreaOrcamentariaAndVigencia", query = "SELECT t FROM GrupoCustoAreaOrcamentaria t "
			+ " WHERE t.areaOrcamentaria.codigoAreaOrcamentaria = :codigoAreaOrcamentaria"
			+ " AND :dataVigencia BETWEEN t.dataInicio AND NVL(t.dataFim, :dataVigencia + 30)")
})
public class GrupoCustoAreaOrcamentaria implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * Constante para NamedQuery "GrupoCustoAreaOrcamentaria.findAll".
     */
    public static final String FIND_ALL = "GrupoCustoAreaOrcamentaria.findAll";

    public static final String FIND_BY_GRUPO_CUSTO_ID = "GrupoCustoAreaOrcamentaria.findByGrupoCustoId";

    public static final String FIND_MAX_DATA_INICIO_BY_CODIGO_GRUPO_CUSTO = "GrupoCustoAreaOrcamentaria.findMaxDataInicioVigenciaByCodigoGrupoCusto";

    public static final String FIND_BY_AREA_ORCAMENTARIA_AND_VIGENCIA = "GrupoCustoAreaOrcamentaria.findByAreaOrcamentariaAndVigencia";

	@Audited
	@Id
	@GeneratedValue(generator="CodigoGrupoCustoAreaOrcamentariaSeq")
	@SequenceGenerator(name="CodigoGrupoCustoAreaOrcamentariaSeq", sequenceName="SQ_GCAO_CD_GRUPO_CUSTO_AREA_OR")
	@Column(name="GCAO_CD_GRUPO_CUSTO_AREA_ORCAM", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoGrupoCustoAreaOrcamentaria;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GRCU_CD_GRUPO_CUSTO", nullable = false)
	private GrupoCusto grupoCusto;

	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AROR_CD_AREA_ORCAMENTARIA", nullable = false)
	private AreaOrcamentaria areaOrcamentaria;

	@Audited
	@Temporal(TemporalType.DATE)
	@Column(name="GCAO_DT_INICIO")
	private Date dataInicio;

	@Audited
	@Temporal(TemporalType.DATE)
	@Column(name="GCAO_DT_FIM")
	private Date dataFim;

	public GrupoCustoAreaOrcamentaria() {
	}

	/**
	 * @return the grupoCusto
	 */
	public GrupoCusto getGrupoCusto() {
		return grupoCusto;
	}

	/**
	 * @param grupoCusto the grupoCusto to set
	 */
	public void setGrupoCusto(GrupoCusto grupoCusto) {
		this.grupoCusto = grupoCusto;
	}

	/**
	 * @return the codigoGrupoCustoAreaOrcamentaria
	 */
	public Long getCodigoGrupoCustoAreaOrcamentaria() {
		return codigoGrupoCustoAreaOrcamentaria;
	}

	/**
	 * @param codigoGrupoCustoAreaOrcamentaria the codigoGrupoCustoAreaOrcamentaria to set
	 */
	public void setCodigoGrupoCustoAreaOrcamentaria(
			Long codigoGrupoCustoAreaOrcamentaria) {
		this.codigoGrupoCustoAreaOrcamentaria = codigoGrupoCustoAreaOrcamentaria;
	}

	/**
	 * @return the areaOrcamentaria
	 */
	public AreaOrcamentaria getAreaOrcamentaria() {
		return areaOrcamentaria;
	}

	/**
	 * @param areaOrcamentaria the areaOrcamentaria to set
	 */
	public void setAreaOrcamentaria(AreaOrcamentaria areaOrcamentaria) {
		this.areaOrcamentaria = areaOrcamentaria;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim the dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
