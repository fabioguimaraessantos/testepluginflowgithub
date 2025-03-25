package com.ciandt.pms.model;

import org.hibernate.envers.Audited;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Auditing entity for Contrato Pratica
 * 
 * @author fabianor
 */
@Entity
@Table(name = "CONTRATO_PRATICA_AUD")
@NamedQueries({
		@NamedQuery(name = "ContratoPraticaAud.findAll", query = "SELECT t FROM ContratoPraticaAud t"),
		@NamedQuery(name = "ContratoPraticaAud.findByCodigo", query = "SELECT t FROM ContratoPraticaAud t " 
				+ "WHERE t.id.codigoContratoPratica = ? ORDER BY t.id.revinfo.revtstmp"),
		@NamedQuery(name = "ContratoPraticaAud.findByCodigoAndRevtype", query = "SELECT t FROM ContratoPraticaAud t "
				+ "WHERE t.id.codigoContratoPratica = ? "
				+ "AND t.revType = ?") })
public class ContratoPraticaAud implements java.io.Serializable {

	private static final long serialVersionUID = 6256841205718480333L;

	public static final String FIND_ALL = "ContratoPraticaAud.findAll";

	public static final String FIND_BY_CODIGO = "ContratoPraticaAud.findByCodigo";

	public static final String FIND_BY_CODIGO_AND_REVTYPE = "ContratoPraticaAud.findByCodigoAndRevtype";

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "codigoContratoPratica", column = @Column(name = "COPR_CD_CONTRATO_PRATICA", nullable = false, precision = 18, scale = 0)),
			@AttributeOverride(name = "revinfo.rev", column = @Column(name = "REV", nullable = false, precision = 18, scale = 0)) })
	private ContratoPraticaAudId id;
	
	@Column(name = "REVTYPE", precision = 3, scale = 0)
	private Long revType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MSAA_CD_MSA")
	private Msa msa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRAT_CD_PRATICA")
	private Pratica pratica;

	@Column(name = "COPR_NM_CONTRATO_PRATICA", unique = true, length = 240)
	private String nomeContratoPratica;

	@Column(name = "COPR_DS_CONTRATO_PRATICA", length = 200)
	private String descricaoContratoPratica;

	@Column(name = "COPR_IN_STATUS", length = 2)
	private String indicadorStatus;

	@Column(name = "COPR_IN_ATIVO", nullable = false, length = 1)
	private String indicadorAtivo;

	@Column(name = "COPR_IN_DELETE_LOGICO", length = 1)
	private String indicadorDeleteLogico;

	@Column(name = "COPR_NM_COMPOUND", length = 240)
	private String nomeCompound;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PESS_CD_APROVADOR")
	private Pessoa aprovador;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PESS_CD_GERENTE")
	private Pessoa gerenteAprovador;

	@Temporal(TemporalType.DATE)
	@Column(name = "COPR_DT_INICIAL", length = 7)
	private Date dataInicial;

	@Temporal(TemporalType.DATE)
	@Column(name = "COPR_DT_FINAL", length = 7)
	private Date dataFinal;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TIMO_CD_TIPO_MODELO_NEGOCIO")
	private TipoModeloNegocio tipoModeloNegocio;

	/**
	 * Atributo gerado a partir da coluna COPR_IN_REEMBOLSAVEL.
	 */
	@Column(name = "COPR_IN_REEMBOLSAVEL", length = 1)
	private String indicadorReembolsavel;

	public ContratoPraticaAudId getId() {
		return id;
	}

	public void setId(ContratoPraticaAudId id) {
		this.id = id;
	}

	public Msa getMsa() {
		return msa;
	}

	public void setMsa(Msa msa) {
		this.msa = msa;
	}

	public Pratica getPratica() {
		return pratica;
	}

	public void setPratica(Pratica pratica) {
		this.pratica = pratica;
	}

	public String getNomeContratoPratica() {
		return nomeContratoPratica;
	}

	public void setNomeContratoPratica(String nomeContratoPratica) {
		this.nomeContratoPratica = nomeContratoPratica;
	}

	public String getDescricaoContratoPratica() {
		return descricaoContratoPratica;
	}

	public void setDescricaoContratoPratica(String descricaoContratoPratica) {
		this.descricaoContratoPratica = descricaoContratoPratica;
	}

	public String getIndicadorStatus() {
		return indicadorStatus;
	}

	public void setIndicadorStatus(String indicadorStatus) {
		this.indicadorStatus = indicadorStatus;
	}

	public String getIndicadorAtivo() {
		return indicadorAtivo;
	}

	public void setIndicadorAtivo(String indicadorAtivo) {
		this.indicadorAtivo = indicadorAtivo;
	}

	public String getIndicadorDeleteLogico() {
		return indicadorDeleteLogico;
	}

	public void setIndicadorDeleteLogico(String indicadorDeleteLogico) {
		this.indicadorDeleteLogico = indicadorDeleteLogico;
	}

	public String getNomeCompound() {
		return nomeCompound;
	}

	public void setNomeCompound(String nomeCompound) {
		this.nomeCompound = nomeCompound;
	}

	public Pessoa getAprovador() {
		return aprovador;
	}

	public void setAprovador(Pessoa aprovador) {
		this.aprovador = aprovador;
	}

	public Pessoa getGerenteAprovador() {
		return gerenteAprovador;
	}

	public void setGerenteAprovador(Pessoa gerenteAprovador) {
		this.gerenteAprovador = gerenteAprovador;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public TipoModeloNegocio getTipoModeloNegocio() {return this.tipoModeloNegocio;}

	public void setTipoModeloNegocio(TipoModeloNegocio tipoModeloNegocio) {
		this.tipoModeloNegocio = tipoModeloNegocio;
	}

	public String getIndicadorReembolsavel() {
		return indicadorReembolsavel;
	}

	public void setIndicadorReembolsavel(final String indicadorReembolsavel) {
		this.indicadorReembolsavel = indicadorReembolsavel;
	}
}
