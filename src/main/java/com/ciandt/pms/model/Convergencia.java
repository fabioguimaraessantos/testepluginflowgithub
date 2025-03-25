package com.ciandt.pms.model;

import javax.persistence.*;

@Entity
@Table(name = "CONVERGENCIA")
@NamedNativeQueries({
		@NamedNativeQuery(name =  Convergencia.FIND_ACTIVE_PROJECTS_BY_FORM_FILTER, query = "SELECT t.conv_cd_convergencia, t.conv_cd_ccusto_mega, t.conv_sg_tipo, gc.grcu_cd_grupo_custo, gc.grcu_nm_grupo_custo, cp.copr_cd_contrato_pratica , cp.copr_nm_contrato_pratica, t.conv_cd_projeto_mega, t.erp_cd_pro_pad_proj, e.empr_nm_empresa, c.clie_nm_cliente, t.CONV_NM_PROJETO_MEGA\n" +
				"FROM Convergencia t \n" +
				" INNER JOIN grupo_custo gc on gc.grcu_cd_grupo_custo = t.grcu_cd_grupo_custo\n" +
				" INNER JOIN empresa e on e.empr_cd_empresa = gc.empr_cd_empresa\n" +
				" LEFT JOIN contrato_pratica cp on cp.copr_cd_contrato_pratica= t.copr_cd_contrato_pratica\n" +
				" LEFT JOIN msa m on m.msaa_cd_msa = cp.msaa_cd_msa\n" +
				" LEFT JOIN cliente c on c.clie_cd_cliente = m.clie_cd_cliente \n" +
				" WHERE (c.clie_nm_cliente = :nomeCliente OR :nomeCliente IS NULL) \n" +
				" AND (e.empr_nm_empresa = :nomeEmpresa OR :nomeEmpresa IS NULL) \n" +
				" AND ((cp.copr_nm_contrato_pratica = :nomeContratoPratica AND cp.copr_in_ativo = 'A' AND cp.copr_in_delete_logico = 'N' ) OR :nomeContratoPratica IS NULL) \n" +
				" AND (gc.grcu_nm_grupo_custo = :nomeGrupoCusto OR :nomeGrupoCusto IS NULL) " +
				" AND (cp.copr_in_ativo = 'A' or cp.copr_cd_contrato_pratica is null) AND (gc.grcu_in_ativo = 'A') ", resultSetMapping = "scalarEmpresa")})
@NamedQueries({
		@NamedQuery(name = Convergencia.FIND_ALL, query = "SELECT t FROM Convergencia t "),
		@NamedQuery(name = Convergencia.FIND_PROJECT_BY_CONTRATO_PRATICA, query = "SELECT t FROM Convergencia t WHERE t.contratoPratica.codigoContratoPratica = :contratoPratica "),
		@NamedQuery(name = Convergencia.FIND_PROJECT_BY_COST_GROUP, query = "SELECT t FROM Convergencia t WHERE t.grupoCusto.codigoGrupoCusto = :grupoCusto AND siglaTipo = 'GC' "),
		@NamedQuery(name = Convergencia.FIND_PROJECT_BY_CL_COST_GROUP, query = "SELECT t FROM Convergencia t INNER JOIN FETCH t.contratoPratica WHERE t.grupoCusto.codigoGrupoCusto = :grupoCusto AND t.siglaTipo = 'CL' "),
		@NamedQuery(name = Convergencia.HAS_PROJECT_TO_ACTIVATE, query = "SELECT t FROM Convergencia t WHERE t.grupoCusto.codigoGrupoCusto = :grupoCusto AND (codigoProjetoMega IS NULL OR codigoProjetoMega = '' AND siglaTipo = 'GC') "),
		@NamedQuery(name = Convergencia.FIND_INACTIVE_PROJECTS_BY_FORM_FILTER, query = "SELECT convergencia FROM Convergencia convergencia "
				+ "INNER JOIN FETCH convergencia.grupoCusto grupoCusto "
				+ "INNER JOIN FETCH grupoCusto.empresa empresa "
				+ "LEFT JOIN FETCH convergencia.contratoPratica contratoPratica "
				+ "LEFT JOIN FETCH contratoPratica.msa msa "
				+ "LEFT JOIN FETCH msa.cliente cliente "
				+ "WHERE (cliente.nomeCliente = :nomeCliente OR :nomeCliente IS NULL) "
				+ "AND (empresa.nomeEmpresa = :nomeEmpresa OR :nomeEmpresa IS NULL) "
				+ "AND ((contratoPratica.nomeContratoPratica = :nomeContratoPratica AND contratoPratica.indicadorAtivo = 'A' AND contratoPratica.indicadorDeleteLogico = 'N' ) OR :nomeContratoPratica IS NULL) "
				+ "AND (grupoCusto.nomeGrupoCusto = :nomeGrupoCusto OR :nomeGrupoCusto IS NULL) "
				+ "AND (convergencia.codigoProjetoMega IS NULL OR convergencia.codigoProjetoMega IS EMPTY) "
				+ "AND (contratoPratica.indicadorAtivo = 'A' OR contratoPratica.codigoContratoPratica is NULL) AND (grupoCusto.indicadorAtivo = 'A') "),
		@NamedQuery(name = Convergencia.FIND_ALL_PROJECTS_BY_FORM_FILTER, query = "SELECT convergencia  FROM Convergencia convergencia "
				+ "INNER JOIN FETCH convergencia.grupoCusto grupoCusto "
				+ "INNER JOIN FETCH grupoCusto.empresa empresa "
				+ "LEFT JOIN FETCH convergencia.contratoPratica contratoPratica "
				+ "LEFT JOIN FETCH contratoPratica.msa msa "
				+ "LEFT JOIN FETCH msa.cliente cliente "
				+ "WHERE (cliente.nomeCliente = :nomeCliente OR :nomeCliente IS NULL) "
				+ "AND (empresa.nomeEmpresa = :nomeEmpresa OR :nomeEmpresa IS NULL) "
				+ "AND ((contratoPratica.nomeContratoPratica = :nomeContratoPratica AND contratoPratica.indicadorAtivo = 'A' AND contratoPratica.indicadorDeleteLogico = 'N' ) OR :nomeContratoPratica IS NULL) "
				+ "AND (grupoCusto.nomeGrupoCusto = :nomeGrupoCusto OR :nomeGrupoCusto IS NULL) "
				+ "AND (contratoPratica.indicadorAtivo = 'A' OR contratoPratica.codigoContratoPratica is NULL) AND (grupoCusto.indicadorAtivo = 'A') "),
		@NamedQuery(name = Convergencia.FIND_PROJECT_BY_CD_PROJECT, query = "SELECT t FROM Convergencia t INNER JOIN FETCH t.grupoCusto gc INNER JOIN FETCH gc.empresa e WHERE t.codigoProjetoMega = :codigoProjetoMega ") })
public class Convergencia implements java.io.Serializable {

	/** Serial Version ID. */
	private static final long serialVersionUID = 2170155077233881627L;

	/** Constante para namedQuery Convergencia.findAll. */
	public static final String FIND_ALL = "Convergencia.findAll";

	public static final String FIND_INACTIVE_PROJECTS_BY_FORM_FILTER = "Convergencia.findInativeProjectByFormFilter";

	public static final String FIND_ACTIVE_PROJECTS_BY_FORM_FILTER = "Convergencia.findActiveProjectByFormFilter";
	
	public static final String FIND_ALL_PROJECTS_BY_FORM_FILTER = "Convergencia.findAllProjectByFormFilter";

	public static final String FIND_PROJECT_BY_CONTRATO_PRATICA = "Convergencia.findByContratoPraticaId";

	public static final String HAS_PROJECT_TO_ACTIVATE = "Convergencia.hasProjectToActivate";

	public static final String FIND_PROJECT_BY_COST_GROUP = "Convergencia.findByCostGroupId";

	public static final String FIND_PROJECT_BY_CL_COST_GROUP = "Convergencia.findByCLCostGroupId";

	public static final String FIND_PROJECT_BY_CD_PROJECT = "Convergencia.findConvergenciaByProjeto";

	@Id
	@GeneratedValue(generator = "ConvergenciaSeq")
	@SequenceGenerator(name = "ConvergenciaSeq", sequenceName = "SQ_CONVE_CD_CONVERGENCIA")
	@Column(name = "CONV_CD_CONVERGENCIA", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoConvergencia;

	@Column(name = "CONV_CD_CCUSTO_MEGA")
	private Long codigoCentroCustoMega;

	@Column(name = "CONV_SG_TIPO")
	private String siglaTipo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRCU_CD_GRUPO_CUSTO", nullable = false)
	private GrupoCusto grupoCusto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPR_CD_CONTRATO_PRATICA", nullable = true)
	private ContratoPratica contratoPratica;

	@Column(name = "CONV_CD_PROJETO_MEGA")
	private Long codigoProjetoMega;

	@Column(name = "CONV_NM_PROJETO_MEGA", length = 255)
	private String nomeProjetoMega;

	@Column(name = "ERP_CD_PRO_PAD_PROJ")
	private Long codigoPadraoProjeto;

	public Long getCodigoConvergencia() {
		return codigoConvergencia;
	}

	public void setCodigoConvergencia(Long codigoConvergencia) {
		this.codigoConvergencia = codigoConvergencia;
	}

	public Long getCodigoCentroCustoMega() {
		return codigoCentroCustoMega;
	}

	public void setCodigoCentroCustoMega(Long codigoCentroCustoMega) {
		this.codigoCentroCustoMega = codigoCentroCustoMega;
	}

	public String getSiglaTipo() {
		return siglaTipo;
	}

	public void setSiglaTipo(String siglaTipo) {
		this.siglaTipo = siglaTipo;
	}

	public GrupoCusto getGrupoCusto() {
		return grupoCusto;
	}

	public void setGrupoCusto(GrupoCusto grupoCusto) {
		this.grupoCusto = grupoCusto;
	}

	public ContratoPratica getContratoPratica() {
		return contratoPratica;
	}

	public void setContratoPratica(ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

	public Long getCodigoProjetoMega() {
		return codigoProjetoMega;
	}

	public void setCodigoProjetoMega(Long codigoProjetoMega) {
		this.codigoProjetoMega = codigoProjetoMega;
	}

	/**
	 * Obtem o valor do atributo {@link Convergencia#nomeProjetoMega}.<BR>
	 * 
	 * @return the nomeProjetoMega
	 */
	public String getNomeProjetoMega() {
		return nomeProjetoMega;
	}

	/**
	 * Atualiza o valor do atributo nomeProjetoMega.<BR>
	 * 
	 * @param nomeProjetoMega
	 *            Novo valor para o atributo
	 *            {@link Convergencia#nomeProjetoMega}.
	 */
	public void setNomeProjetoMega(String nomeProjetoMega) {
		this.nomeProjetoMega = nomeProjetoMega;
	}

	/**
	 * Obtem o valor do atributo {@link Convergencia#codigoPadraoProjeto}.<BR>
	 * 
	 * @return the codigoPadraoProjeto
	 */
	public Long getCodigoPadraoProjeto() {
		return codigoPadraoProjeto;
	}

	/**
	 * Atualiza o valor do atributo codigoPadraoProjeto.<BR>
	 * 
	 * @param codigoPadraoProjeto
	 *            Novo valor para o atributo
	 *            {@link Convergencia#codigoPadraoProjeto}.
	 */
	public void setCodigoPadraoProjeto(Long codigoPadraoProjeto) {
		this.codigoPadraoProjeto = codigoPadraoProjeto;
	}

	/**
	 * Se os campos do objeto estiverem nulo, entao objeto considerado nulo.
	 * 
	 * @return boolean
	 */
	public boolean isNull() {
		return this.codigoConvergencia == null
				&& this.codigoCentroCustoMega == null
				&& this.codigoPadraoProjeto == null
				&& this.contratoPratica == null && this.grupoCusto == null
				&& this.nomeProjetoMega == null && this.siglaTipo == null;
	}

}