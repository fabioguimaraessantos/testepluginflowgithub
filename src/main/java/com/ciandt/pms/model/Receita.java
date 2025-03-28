/*
 * @(#) Receita.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.*;

/**
 * Entity gerado a partir da tabela RECEITA.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 21/12/2009 12:09:33
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "RECEITA")
@SqlResultSetMappings(@SqlResultSetMapping(name = "scalarReceita"))
@NamedNativeQueries({@NamedNativeQuery(name = "Receita.updateStatusReceita", query = "UPDATE RECEITA R " +
				" SET R.RECE_IN_VERSAO = :revenueStatus" +
				" WHERE R.RECE_CD_RECEITA = :revenueCode ", resultClass = Receita.class),
		@NamedNativeQuery(name = "Receita.findReceitaByReceitaDealFiscal", query = "SELECT r.rece_cd_receita FROM receita r " +
		" join receita_moeda rm on r.rece_cd_receita = rm.rece_cd_receita " +
		" join receita_deal_fiscal rdf on rm.remo_cd_receita_moeda = rdf.remo_cd_receita_moeda " +
		" where rdf.redf_cd_receita_dfiscal = :revenueDealFiscalCode ", resultSetMapping = "scalarReceita")})
@NamedQueries({
		@NamedQuery(name = "Receita.findAll", query = "SELECT t FROM Receita t"),

		@NamedQuery(name = "Receita.findByMonthDate", query = "SELECT rece FROM Receita rece "
				+ "WHERE ( TRUNC(rece.dataMes,'MONTH') = TRUNC(?,'MONTH') ) "),

		@NamedQuery(name = "Receita.findByFilter", query = "SELECT rece FROM Receita rece "
				+ "WHERE ( (rece.contratoPratica.codigoContratoPratica = ?) OR (? = 0L) ) "
				+ "AND ( TRUNC(rece.dataMes) = TRUNC(?) ) "
				+ "AND ( UPPER(rece.indicadorVersao) like '%'||TRIM(UPPER(?))||'%' OR (? is null) ) "
				+ "ORDER BY rece.contratoPratica.nomeContratoPratica ASC, "
				+ "to_date(rece.dataMes,'dd/mm/yyyy') ASC "),

		@NamedQuery(name = "Receita.findAllNotClosed", query = "SELECT rece FROM Receita rece "
				+ "WHERE to_char(rece.dataMes,'mm/yyyy') = to_char(?,'mm/yyyy') "
				+ "AND ( UPPER(rece.indicadorVersao) NOT IN ('PB','IN','PD') ) "
				+ "ORDER BY rece.contratoPratica.nomeContratoPratica ASC "),

		@NamedQuery(name = "Receita.findByFilterFetch", query = "SELECT distinct rece FROM Receita rece "
				+ "LEFT JOIN rece.contratoPratica cp "
				+ "LEFT JOIN cp.contratoPraticaCentroLucros contratoPraticaCL "
				+ "JOIN FETCH rece.receitaMoedas remo "
				+ "WHERE ( TRUNC(rece.dataMes) = TRUNC(?) ) "
                + " AND rece.indicadorVersao != 'FC' "
				+ " AND ( (contratoPraticaCL.centroLucro.codigoCentroLucro = ?) OR (? = 0L) ) "
				+ " AND ( (contratoPraticaCL.centroLucro.naturezaCentroLucro.codigoNatureza = ?) OR (? = 0L) ) "
				+ " AND ( (cp.msa.cliente.codigoCliente = ?) OR (? = 0L) ) "
				+ " AND ( (cp.pratica.codigoPratica = ?) OR (? = 0L) )"
				+ " AND (UPPER(rece.indicadorVersao) LIKE '%'||TRIM(UPPER(?))||'%' OR (? IS NULL)) "
				+ " AND ( (? = 0L) OR (TRUNC(?) BETWEEN TRUNC(contratoPraticaCL.dataInicioVigencia) AND "
				+ "                               (CASE WHEN contratoPraticaCL.dataFimVigencia IS NOT NULL "
				+ "                                 THEN TRUNC(contratoPraticaCL.dataFimVigencia) "
				+ "                                 ELSE TRUNC(to_date('31/12/9999','dd/mm/yyyy')) "
				+ "                               END)) ) "),

		@NamedQuery(name = "Receita.findAllNoWorkingByFilter", query = "SELECT rece FROM Receita rece "
				+ "WHERE ( (rece.contratoPratica.codigoContratoPratica = ?) OR (? = 0L) ) "
				+ "AND ( TRUNC(rece.dataMes) = TRUNC(?) ) "
				+ "AND ( UPPER(rece.indicadorVersao) = 'PB' OR UPPER(rece.indicadorVersao) = 'IN' ) "
				+ "ORDER BY rece.contratoPratica.nomeContratoPratica ASC, "
				+ "to_date(rece.dataMes,'dd/mm/yyyy') ASC "),

		@NamedQuery(name = "Receita.findByCpclAndCentroLucro", query = "SELECT r FROM Receita r "
				+ " LEFT JOIN r.contratoPratica.contratoPraticaCentroLucros cpcl "
				+ " LEFT JOIN cpcl.centroLucro cl "
				+ " WHERE cpcl.centroLucro.codigoCentroLucro = cl.codigoCentroLucro "
				+ " AND cpcl.contratoPratica.codigoContratoPratica = r.contratoPratica.codigoContratoPratica "
				+ " AND r.dataMes BETWEEN TRUNC(cpcl.dataInicioVigencia) "
				+ "                         and nvl(cpcl.dataFimVigencia, r.dataMes+1) "
				+ " AND cl.codigoCentroLucro = ? "),
				
		@NamedQuery(name = "Receita.findUltimasReceitasByDataAndCP", query = "SELECT r FROM Receita r "
				+ " WHERE r.contratoPratica.codigoContratoPratica = :cdContratoPratica "
				+ " AND r.dataMes >= ADD_MONTHS(r.dataMes, -:meses) "),

		@NamedQuery(name = "Receita.findAllReceitaForecastByContratoPratica", query = "SELECT r FROM Receita r "
				+ " WHERE r.indicadorVersao = 'FC' " +
                " AND r.contratoPratica.codigoContratoPratica = :codigoContratoPratica"),

		@NamedQuery(name = "Receita.findNotIntegratedRevenueAfterClosingRevenueDate", query = "SELECT DISTINCT r FROM Receita r "
				+ "JOIN r.receitaMoedas rm "
				+ "JOIN rm.receitaDealFiscals rdf "
				+ "JOIN rdf.dealFiscal df "
				+ " WHERE r.indicadorVersao NOT IN ('FC','IN') "
				+ "	AND r.dataMes >= :closingRevenueDate "
				+ " AND df.codigoDealFiscal = :codigoDealFiscal"),

		@NamedQuery(name = "Receita.findWkOrPbRevenuesByMsaAndStartDate", query = "SELECT DISTINCT r FROM Receita r "
				+ " JOIN r.contratoPratica cp "
				+ " JOIN cp.msa msa "
				+ " WHERE r.indicadorVersao IN ('WK','PB') "
				+ "	AND r.dataMes >= :dtStart "
				+ " AND msa.codigoMsa = :codigoMsa"),

		@NamedQuery(name = "Receita.findWkOrPbRevenuesByMsaAndStartDateEndDate", query = "SELECT DISTINCT r FROM Receita r "
				+ " JOIN r.contratoPratica cp "
				+ " JOIN cp.msa msa "
				+ " WHERE r.indicadorVersao IN ('WK','PB') "
				+ "	AND r.dataMes >= :dtStart "
				+ " AND r.dataMes < :dtEnd "
				+ " AND msa.codigoMsa = :codigoMsa")

})
public class Receita implements java.io.Serializable {

	// ========================================================================
	// BEGIN - Coloque aqui o codigo manual
	// ========================================================================

	/** Constante para NamedQuery "Receita.findByFilter". */
	public static final String FIND_BY_FILTER = "Receita.findByFilter";

	public static final String UPDATE_STATUS_RECEITA = "Receita.updateStatusReceita";

	public static final String FIND_BY_RECEITA_DEAL_FISCAL = "Receita.findReceitaByReceitaDealFiscal";

	/** Constante para NamedQuery "Receita.findByFilter". */
	public static final String FIND_BY_FILTER_FETCH = "Receita.findByFilterFetch";

	/** Constante para NamedQuery "Receita.findAllNoWorkingByFilter". */
	public static final String FIND_ALL_NO_WORKING_BY_FILTER = "Receita.findAllNoWorkingByFilter";

	/** Constante para NamedQuery "Receita.findAllNotClosed". */
	public static final String FIND_ALL_NOT_CLOSED = "Receita.findAllNotClosed";

	/** Constante para NamedQuery "Receita.findByMonthDate". */
	public static final String FIND_BY_MONTH_DATE = "Receita.findByMonthDate";

	/** Constante para NamedQuery "Receita.findByCpclAndCentroLucro". */
	public static final String FIND_BY_CPCL_AND_CENTROLUCRO = "Receita.findByCpclAndCentroLucro";
	
	/** Constante para NamedQuery "Receita.findUltimasReceitasByDataAndCP". */
	public static final String FIND_LASTS_BY_DATA_AND_CPCL = "Receita.findUltimasReceitasByDataAndCP";

	/** Constante para NamedQuery "Receita.findAllReceitaForecast". */
	public static final String FIND_ALL_RECEITA_FORECAST_BY_CODIGO_CONTRATO_PRATICA = "Receita.findAllReceitaForecastByContratoPratica";

	/** Constante para NamedQuery "Receita.findNotIntegratedRevenueAfterClosingRevenueDate". */
	public static final String FIND_NOT_INTEGRATED_REVENUE_AFTER_CLOSING_REVENUE_DATE = "Receita.findNotIntegratedRevenueAfterClosingRevenueDate";

	/** Constante para NamedQuery "Receita.findWkOrPbRevenuesByMsaAndStartDate ". */
	public static final String FIND_WK_OR_PB_REVENUES_BY_MSA_AND_START_DATE = "Receita.findWkOrPbRevenuesByMsaAndStartDate";

	/** Constante para NamedQuery "Receita.findWkOrPbRevenuesByMsaAndStartDateEndDate ". */
	public static final String FIND_WK_OR_PB_REVENUES_BY_MSA_AND_DATES = "Receita.findWkOrPbRevenuesByMsaAndStartDateEndDate";

	/**
	 * Lista de HistoricoReceita.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "receita")
	private List<HistoricoReceita> historicoReceitas = new ArrayList<HistoricoReceita>(
			0);

	/**
	 * Lista de ReceitaMoeda.
	 */
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "receita", cascade = CascadeType.ALL)
	private List<ReceitaMoeda> receitaMoedas = new ArrayList<ReceitaMoeda>(0);

	/**
	 * Realiza uma copia do objeto, porem com uma referencia diferente.
	 * 
	 * @return a c�pia do Receita
	 */
	public Receita getClone() {
		try {
			return (Receita) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

	// ========================================================================
	// END
	// ========================================================================

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "Receita.findAll".
	 */
	public static final String FIND_ALL = "Receita.findAll";

	/**
	 * Atributo gerado a partir da coluna RECE_CD_RECEITA.
	 */
	@Id
	@GeneratedValue(generator = "ReceitaSeq")
	@SequenceGenerator(name = "ReceitaSeq", sequenceName = "SQ_RECE_CD_RECEITA", allocationSize = 1)
	@Column(name = "RECE_CD_RECEITA", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoReceita;

	/**
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPR_CD_CONTRATO_PRATICA", nullable = false)
	private ContratoPratica contratoPratica;

	/**
	 * Atributo gerado a partir da coluna RECE_DT_MES.
	 */
	@Audited
	@Temporal(TemporalType.DATE)
	@Column(name = "RECE_DT_MES", nullable = false, length = 7)
	private Date dataMes;

	/**
	 * Atributo gerado a partir da coluna RECE_IN_VERSAO.
	 */
	@Audited
	@Column(name = "RECE_IN_VERSAO", length = 2)
	private String indicadorVersao;

	/**
	 * Atributo gerado a partir da coluna RECE_TX_OBSERVACAO.
	 */
	@Audited
	@Column(name = "RECE_TX_OBSERVACAO", length = 4000)
	private String textoObservacao;

	/**
	 * Atributo gerado a partir da coluna RECE_CD_LOGIN_CRIADOR.
	 */
	@Audited
	@Column(name = "RECE_CD_LOGIN_CRIADOR", length = 50)
	private String codigoLoginCriador;

	/**
	 * Atributo gerado a partir da coluna RECE_DT_CRIACAO.
	 */
	@Audited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECE_DT_CRIACAO", length = 7)
	private Date dataCriacao;

	/**
	 * Atributo gerado a partir da coluna RECE_DT_ATUALIZACAO.
	 */
	@Audited
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECE_DT_ATUALIZACAO", length = 7)
	private Date dataAtualizacao;

	/**
	 * Relacionamento um pra muitos.
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "receita")
	private Set<EstratificacaoUr> estratificacaoUrs = new HashSet<EstratificacaoUr>(
			0);

	/**
	 * Construtor default.
	 */
	public Receita() {
	}

	/**
	 * @return the historicoReceitas
	 */
	public List<HistoricoReceita> getHistoricoReceitas() {
		return historicoReceitas;
	}

	/**
	 * @param historicoReceitas
	 *            the historicoReceitas to set
	 */
	public void setHistoricoReceitas(
			final List<HistoricoReceita> historicoReceitas) {
		this.historicoReceitas = historicoReceitas;
	}

	/**
	 * @return the receitaMoedas
	 */
	public List<ReceitaMoeda> getReceitaMoedas() {
		return receitaMoedas;
	}

	/**
	 * @param receitaMoedas
	 *            the receitaMoedas to set
	 */
	public void setReceitaMoedas(final List<ReceitaMoeda> receitaMoedas) {
		this.receitaMoedas = receitaMoedas;
	}

	/**
	 * @return the codigoReceita
	 */
	public Long getCodigoReceita() {
		return codigoReceita;
	}

	/**
	 * @param codigoReceita
	 *            the codigoReceita to set
	 */
	public void setCodigoReceita(final Long codigoReceita) {
		this.codigoReceita = codigoReceita;
	}

	/**
	 * @return the contratoPratica
	 */
	public ContratoPratica getContratoPratica() {
		return contratoPratica;
	}

	/**
	 * @param contratoPratica
	 *            the contratoPratica to set
	 */
	public void setContratoPratica(final ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

	/**
	 * @return the dataMes
	 */
	public Date getDataMes() {
		return dataMes;
	}

	/**
	 * @param dataMes
	 *            the dataMes to set
	 */
	public void setDataMes(final Date dataMes) {
		this.dataMes = dataMes;
	}

	/**
	 * @return the indicadorVersao
	 */
	public String getIndicadorVersao() {
		return indicadorVersao;
	}

	/**
	 * @param indicadorVersao
	 *            the indicadorVersao to set
	 */
	public void setIndicadorVersao(final String indicadorVersao) {
		this.indicadorVersao = indicadorVersao;
	}

	/**
	 * @return the textoObservacao
	 */
	public String getTextoObservacao() {
		return textoObservacao;
	}

	/**
	 * @param textoObservacao
	 *            the textoObservacao to set
	 */
	public void setTextoObservacao(final String textoObservacao) {
		this.textoObservacao = textoObservacao;
	}

	/**
	 * @return the codigoLoginCriador
	 */
	public String getCodigoLoginCriador() {
		return codigoLoginCriador;
	}

	/**
	 * @param codigoLoginCriador
	 *            the codigoLoginCriador to set
	 */
	public void setCodigoLoginCriador(final String codigoLoginCriador) {
		this.codigoLoginCriador = codigoLoginCriador;
	}

	/**
	 * @return the dataCriacao
	 */
	public Date getDataCriacao() {
		return dataCriacao;
	}

	/**
	 * @param dataCriacao
	 *            the dataCriacao to set
	 */
	public void setDataCriacao(final Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	/**
	 * @return the dataAtualizacao
	 */
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	/**
	 * @param dataAtualizacao
	 *            the dataAtualizacao to set
	 */
	public void setDataAtualizacao(final Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	/**
	 * @return the estratificacaoUrs
	 */
	public Set<EstratificacaoUr> getEstratificacaoUrs() {
		return estratificacaoUrs;
	}

	/**
	 * @param estratificacaoUrs
	 *            the estratificacaoUrs to set
	 */
	public void setEstratificacaoUrs(
			final Set<EstratificacaoUr> estratificacaoUrs) {
		this.estratificacaoUrs = estratificacaoUrs;
	}

	/**
	 * @see Object#toString()
	 * @return representa��o String do Objeto
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@")
				.append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("codigoReceita").append("='").append(getCodigoReceita())
				.append("' ");
		buffer.append("contratoPratica").append("='")
				.append(getContratoPratica()).append("' ");
		buffer.append("dataMes").append("='").append(getDataMes()).append("' ");
		buffer.append("indicadorVersao").append("='")
				.append(getIndicadorVersao()).append("' ");
		buffer.append("textoObservacao").append("='")
				.append(getTextoObservacao()).append("' ");
		buffer.append("codigoLoginCriador").append("='")
				.append(getCodigoLoginCriador()).append("' ");
		buffer.append("dataCriacao").append("='").append(getDataCriacao())
				.append("' ");
		buffer.append("dataAtualizacao").append("='")
				.append(getDataAtualizacao()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}