/*
 * @(#) TabelaPreco.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow.PriceTableFlow;
import org.hibernate.annotations.Type;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Entity gerado a partir da tabela TABELA_PRECO.
 * 
 * @author Generated by Hibernate Tools 3.2.4.GA
 * @since 12/08/2009 17:14:06
 * @version 09/01/19 1.1 10:08:00
 */
@Entity
@Table(name = "TABELA_PRECO")
@Audited
@AuditTable(value = "TABELA_PRECO_AUD")
@NamedQueries({
		@NamedQuery(name = "TabelaPreco.findAll", query = "SELECT t FROM TabelaPreco t"
				+ " ORDER BY t.dataInicioVigencia ASC"),
		@NamedQuery(name = "TabelaPreco.findByMsa", query = "SELECT tp FROM TabelaPreco tp "
				+ "WHERE tp.msa.codigoMsa = ? "
				+ "ORDER BY tp.dataInicioVigencia ASC"),
		@NamedQuery(name = "TabelaPreco.findMaxStartDateByContratoPratica", query = "SELECT tp FROM TabelaPreco tp "
				+ " WHERE tp.msa.codigoMsa = ? "
				+ " AND tp.dataInicioVigencia = "
				+ "(SELECT MAX(tp1.dataInicioVigencia) FROM TabelaPreco tp1 "
				+ " WHERE tp1.msa.codigoMsa = ?)"),
		@NamedQuery(name = "TabelaPreco.findByContratoPraticaAndName", query = "SELECT tp FROM TabelaPreco tp "
				+ "LEFT JOIN FETCH tp.msa msa "
				+ "LEFT JOIN FETCH msa.contratoPraticas cp "
				+ " WHERE TRIM((UPPER(tp.descricaoTabelaPreco))) like '%'||TRIM(UPPER(?))||'%' "
				+ " AND cp.codigoContratoPratica = ?"),
		@NamedQuery(name = "TabelaPreco.findMaxStartDateByMsaAndCurrency", query = "SELECT tp FROM TabelaPreco tp "
				+ "WHERE tp.msa.codigoMsa = ? "
				+ "AND tp.moeda.codigoMoeda = ? "
				+ "AND tp.priceTableStatus.code != 6 "
				+ "AND tp.dataInicioVigencia = "
				+ "(SELECT MAX(t.dataInicioVigencia) FROM TabelaPreco t WHERE t.moeda.codigoMoeda = ? "
				+ "AND t.priceTableStatus.code != 6 "
				+ "AND t.msa.codigoMsa = ?)"),
		@NamedQuery(name = "TabelaPreco.findMaxStartDateByMsaAndCurrencyApproved", query = "SELECT tp FROM TabelaPreco tp "
				+ "WHERE tp.msa.codigoMsa = ? "
				+ "AND tp.moeda.codigoMoeda = ? "
				+ "AND tp.priceTableStatus.code = 4 "
				+ "AND tp.dataInicioVigencia = "
				+ "(SELECT MAX(t.dataInicioVigencia) FROM TabelaPreco t WHERE t.moeda.codigoMoeda = ? "
				+ "AND t.priceTableStatus.code = 4 "
				+ "AND t.msa.codigoMsa = ?)"),

		@NamedQuery(name = "TabelaPreco.findByMsaAndName", query = "SELECT tp FROM TabelaPreco tp "
				+ "WHERE tp.msa.codigoMsa = ? "
				+ "AND TRIM(tp.descricaoTabelaPreco) like TRIM(?)"),
		@NamedQuery(name = "TabelaPreco.findByMsaAndActive", query = "SELECT tp FROM TabelaPreco tp "
				+ "WHERE tp.msa.codigoMsa = ? "
				+ "AND tp.indicadorDeleteLogico = 'N' "
				+ "ORDER BY tp.dataInicioVigencia ASC"),
		@NamedQuery(name = "TabelaPreco.findByCode", query = "SELECT tp FROM TabelaPreco tp "
				+ "WHERE tp.codigoTabelaPreco = ? ")})
public class TabelaPreco implements java.io.Serializable {

	// ========================================================================
	// BEGIN - Coloque aqui o codigo manual
	// ========================================================================

	/** Constante para NamedQuery "TabelaPreco.findByContratoPratica". */
	public static final String FIND_BY_CONTRATO_PRATICA = "TabelaPreco.findByContratoPratica";

	/** Constante para NamedQuery "TabelaPreco.findMaxDateByContratoPratica". */
	public static final String FIND_BY_MAX_START_DATE_BY_CONTRATO_PRATICA = "TabelaPreco.findMaxStartDateByContratoPratica";

	/** Constante para NamedQuery "TabelaPreco.findByContratoPraticaAndName". */
	public static final String FIND_BY_CONTRATO_PRATICA_AND_NAME = "TabelaPreco.findByContratoPraticaAndName";

	/** Constante para NamedQery "TabelaPreco.findByMsa". */
	public static final String FIND_BY_MSA = "TabelaPreco.findByMsa";

	/** Constante para NamedQery "TabelaPreco.findMaxStartDateByMsaAndCurrency". */
	public static final String FIND_MAX_START_DATE_BY_MSA_AND_CURRENCY = "TabelaPreco.findMaxStartDateByMsaAndCurrency";

	/** Constante para NamedQery "TTabelaPreco.findMaxStartDateByMsaAndCurrencyApproved". */
	public static final String FIND_MAX_START_DATE_BY_MSA_AND_CURRENCY_APPROVED = "TabelaPreco.findMaxStartDateByMsaAndCurrencyApproved";

	/** Constante para NamedQery "TabelaPreco.findByMsaAndName". */
	public static final String FIND_BY_MSA_AND_NAME = "TabelaPreco.findByMsaAndName";
	
	/** Constante para NamedQery "TabelaPreco.findByMsaAndActive". */
	public static final String FIND_BY_MSA_AND_ACTIVE = "TabelaPreco.findByMsaAndActive";

	/** Constante para NamedQuery "TabelaPreco.findByCode". */
	public static final String FIND_BY_CODE = "TabelaPreco.findByCode";

	/** Flag que verifica se a data de fim de vigencia da Tabela Preco ? maior que o Closing Date. */
	@Transient
	private Boolean isGreaterThenClosingDate = Boolean.FALSE;

	@Transient
	private boolean isViewPriceTableButton = Boolean.FALSE;

	@Transient
	private boolean isConfigurePriceTableButton = Boolean.FALSE;

	@Transient
	private boolean isEditPriceTableButton = Boolean.FALSE;


	@Transient
	private boolean isDeletePriceTableButton = Boolean.FALSE;



	@Transient
	private boolean isApprovedCheckboxEdit = Boolean.FALSE;

	@Transient
	private boolean isApprovedCheckboxView = Boolean.FALSE;

	@Transient
	private boolean isAddItemPriceTableButton = Boolean.FALSE;


	@Transient
	private boolean isItemPriceTableField = Boolean.FALSE;


	@Transient
	private boolean isItemPriceTableControl = Boolean.FALSE;

	/**
	 * Relacionamento um pra muitos.
	 */

	@NotAudited
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tabelaPreco")
	private List<ItemTabelaPreco> itemTabelaPrecos = new ArrayList<ItemTabelaPreco>(
			0);

	/**
	 * Relacionamento um pra muitos.
	 */

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tabelaPreco")
	private List<AnexoTabelaPreco> anexoTabelaPrecos = new ArrayList<AnexoTabelaPreco>(
			0);

	// ========================================================================
	// END
	// ========================================================================

	/**
	 * Valor do serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "TabelaPreco.findAll".
	 */
	public static final String FIND_ALL = "TabelaPreco.findAll";

	/**
	 * Atributo gerado a partir da coluna TAPR_CD_TABELA_PRECO.
	 */
	@Id
	@GeneratedValue(generator = "TabelaPrecoSeq")
	@SequenceGenerator(name = "TabelaPrecoSeq", sequenceName = "SQ_TAPR_CD_TABELA_PRECO", allocationSize = 1)
	@Column(name = "TAPR_CD_TABELA_PRECO", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoTabelaPreco;

	/**
	 * Atributo gerado a partir da coluna MOED_CD_MOEDA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOED_CD_MOEDA", nullable = false)
	private Moeda moeda;

	/**
	 * Atributo gerado a partir da coluna MSAA_CD_MSA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MSAA_CD_MSA", nullable = false)
	private Msa msa;

	/**
	 * Atributo gerado a partir da coluna TAPR_DS_TABELA_PRECO.
	 */
	@Audited
	@Column(name = "TAPR_DS_TABELA_PRECO", length = 200)
	private String descricaoTabelaPreco;

	/**
	 * Atributo gerado a partir da coluna TAPR_DT_INICIO_VIGENCIA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@Temporal(TemporalType.DATE)
	@Column(name = "TAPR_DT_INICIO_VIGENCIA", length = 7)
	private Date dataInicioVigencia;

	/**
	 * Atributo gerado a partir da coluna TAPR_DT_FIM_VIGENCIA.
	 */
	@Audited
	@Temporal(TemporalType.DATE)
	@Column(name = "TAPR_DT_FIM_VIGENCIA", length = 7)
	private Date dataFimVigencia;

	/**
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 */
	@Audited
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COPR_CD_CONTRATO_PRATICA_AUX", nullable = true)
	private ContratoPratica contratoPratica;

	/**
	 * Atributo gerado a partir da coluna TAPR_IN_DELETE_LOGICO.
	 */
	@Audited
	@Column(name = "TAPR_IN_DELETE_LOGICO", length = 1)
	private String indicadorDeleteLogico;

	@Audited
	@Type(type = "yes_no")
	@Column(name = "TAPR_IN_RECALC_RECEITA", length = 1)
	private Boolean recalculaReceita;


	/**
	 * Atributo gerado a partir da coluna MSAA_CD_MSA.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PTS_CD_PRICE_TB_STATUS", nullable = false)
	private PriceTableStatus priceTableStatus;

	/**
	 * Atributo gerado a partir da coluna PTS_CD_PRICE_TB_STATUS_FLOW_PMS.
	 */
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PTS_CD_PRICE_TB_STATUS_FLOW_PMS", nullable = false)
	private PriceTableStatus priceTableStatusFlowPms;


	/**
	 * Atributo gerado a partir da coluna TAPR_TX_NOT_APPROVAL.
	 */
	@Audited
	@Column(name = "TAPR_TX_NOT_APPROVAL", length = 1000)
	private String notApproveReasonsDescription;


	/**
	 * Construtor default.
	 */
	public TabelaPreco() {
	}

	/**
	 * @return the itemTabelaPrecos
	 */
	public List<ItemTabelaPreco> getItemTabelaPrecos() {
		return itemTabelaPrecos;
	}

	/**
	 * @param itemTabelaPrecos
	 *            the itemTabelaPrecos to set
	 */
	public void setItemTabelaPrecos(final List<ItemTabelaPreco> itemTabelaPrecos) {
		this.itemTabelaPrecos = itemTabelaPrecos;
	}

	/**
	 * @return the anexoTabelaPrecos
	 */
	public List<AnexoTabelaPreco> getAnexoTabelaPrecos() {
		return anexoTabelaPrecos;
	}

	/**
	 * @param anexoTabelaPrecos
	 *            the anexoTabelaPrecos to set
	 */
	public void setAnexoTabelaPrecos(
			final List<AnexoTabelaPreco> anexoTabelaPrecos) {
		this.anexoTabelaPrecos = anexoTabelaPrecos;
	}

	/**
	 * @return the codigoTabelaPreco
	 */
	public Long getCodigoTabelaPreco() {
		return codigoTabelaPreco;
	}

	/**
	 * @param codigoTabelaPreco
	 *            the codigoTabelaPreco to set
	 */
	public void setCodigoTabelaPreco(final Long codigoTabelaPreco) {
		this.codigoTabelaPreco = codigoTabelaPreco;
	}

	/**
	 * @return the moeda
	 */
	public Moeda getMoeda() {
		return moeda;
	}

	/**
	 * @param moeda
	 *            the moeda to set
	 */
	public void setMoeda(final Moeda moeda) {
		this.moeda = moeda;
	}

	/**
	 * @return the msa
	 */
	public Msa getMsa() {
		return msa;
	}

	/**
	 * @param msa
	 *            the msa to set
	 */
	public void setMsa(final Msa msa) {
		this.msa = msa;
	}

	/**
	 * @return the descricaoTabelaPreco
	 */
	public String getDescricaoTabelaPreco() {
		return descricaoTabelaPreco;
	}

	/**
	 * @param descricaoTabelaPreco
	 *            the descricaoTabelaPreco to set
	 */
	public void setDescricaoTabelaPreco(final String descricaoTabelaPreco) {
		this.descricaoTabelaPreco = descricaoTabelaPreco;
	}

	/**
	 * @return the dataInicioVigencia
	 */
	public Date getDataInicioVigencia() {
		return dataInicioVigencia;
	}

	/**
	 * @param dataInicioVigencia
	 *            the dataInicioVigencia to set
	 */
	public void setDataInicioVigencia(final Date dataInicioVigencia) {
		this.dataInicioVigencia = dataInicioVigencia;
	}

	/**
	 * @return the dataFimVigencia
	 */
	public Date getDataFimVigencia() {
		return dataFimVigencia;
	}

	/**
	 * @param dataFimVigencia
	 *            the dataFimVigencia to set
	 */
	public void setDataFimVigencia(final Date dataFimVigencia) {
		this.dataFimVigencia = dataFimVigencia;
	}

	/**
	 * Obtem o valor do atributo codigoContratoPratica.<BR>
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 * 
	 * @return Valor do atributo contratoPratica.
	 */
	public ContratoPratica getContratoPratica() {
		return this.contratoPratica;
	}

	/**
	 * Atualiza o valor do atributo codigoContratoPratica.<BR>
	 * Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA.
	 * 
	 * @param contratoPratica
	 *            Novo valor para o atributo contratoPratica.
	 */
	public void setContratoPratica(final ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

	/**
	 * @return the indicadorDeleteLogico
	 */
	public String getIndicadorDeleteLogico() {
		return indicadorDeleteLogico;
	}

	/**
	 * @param indicadorDeleteLogico
	 *            the indicadorDeleteLogico to set
	 */
	public void setIndicadorDeleteLogico(final String indicadorDeleteLogico) {
		this.indicadorDeleteLogico = indicadorDeleteLogico;
	}

	public Boolean getRecalculaReceita() {
		return recalculaReceita;
	}

	public void setRecalculaReceita(Boolean recalculaReceita) {
		this.recalculaReceita = recalculaReceita;
	}

	public PriceTableStatus getPriceTableStatus() {
		return priceTableStatus;
	}

	public void setPriceTableStatus(PriceTableStatus priceTableStatus) {
		this.priceTableStatus = priceTableStatus;
	}

	public PriceTableStatus getPriceTableStatusFlowPms() {
		return priceTableStatusFlowPms;
	}

	public void setPriceTableStatusFlowPms(PriceTableStatus priceTableStatusFlowPms) {
		this.priceTableStatusFlowPms = priceTableStatusFlowPms;
	}

	public void delete() {
		this.setIndicadorDeleteLogico(Constants.SIM);
		PriceTableStatus deletedStatus = new PriceTableStatus();
		deletedStatus.setCode(PriceTableFlow.STATUS_DELETED);
		this.setPriceTableStatus(deletedStatus);
		this.setPriceTableStatusFlowPms(deletedStatus);
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
		buffer.append("codigoTabelaPreco").append("='")
				.append(getCodigoTabelaPreco()).append("' ");
		buffer.append("moeda").append("='").append(getMoeda()).append("' ");
		buffer.append("msa").append("='").append(getMsa()).append("' ");
		buffer.append("descricaoTabelaPreco").append("='")
				.append(getDescricaoTabelaPreco()).append("' ");
		buffer.append("dataInicioVigencia").append("='")
				.append(getDataInicioVigencia()).append("' ");
		buffer.append("dataFimVigencia").append("='")
				.append(getDataFimVigencia()).append("' ");
		buffer.append("indicadorDeleteLogico").append("='")
				.append(getIndicadorDeleteLogico()).append("' ");
		buffer.append("recalculaReceita").append("='")
				.append(getRecalculaReceita().toString()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}


	public Boolean getIsGreaterThenClosingDate() {
		return this.isGreaterThenClosingDate;
	}

	public void setIsGreaterThenClosingDate(Boolean isGreaterThenClosingDate) {
		this.isGreaterThenClosingDate = isGreaterThenClosingDate;
	}

	public String getNotApproveReasonsDescription() {
		return notApproveReasonsDescription;
	}

	public void setNotApproveReasonsDescription(String notApproveReasonsDescription) {
		this.notApproveReasonsDescription = notApproveReasonsDescription;
	}


	public boolean getIsConfigurePriceTableButton() {
		return isConfigurePriceTableButton;
	}

	public void setIsConfigurePriceTableButton(boolean configurePriceTableButton) {
		isConfigurePriceTableButton = configurePriceTableButton;
	}

	public boolean getIsViewPriceTableButton() {
		return isViewPriceTableButton;
	}

	public void setIsViewPriceTableButton(boolean viewPriceTableButton) {
		isViewPriceTableButton = viewPriceTableButton;
	}

	public boolean getIsEditPriceTableButton() {
		return isEditPriceTableButton;
	}

	public void setIsEditPriceTableButton(boolean editPriceTableButton) {
		isEditPriceTableButton = editPriceTableButton;
	}


	public boolean getIsDeletePriceTableButton() {
		return isDeletePriceTableButton;
	}

	public void setIsDeletePriceTableButton(boolean deletePriceTableButton) {
		isDeletePriceTableButton = deletePriceTableButton;
	}

	public boolean getIsApprovedCheckboxEdit() {
		return isApprovedCheckboxEdit;
	}

	public void setIsApprovedCheckboxEdit(boolean approvedCheckboxEdit) {
		isApprovedCheckboxEdit = approvedCheckboxEdit;
	}

	public boolean getIsApprovedCheckboxView() {
		return isApprovedCheckboxView;
	}

	public void setIsApprovedCheckboxView(boolean approvedCheckboxView) {
		isApprovedCheckboxView = approvedCheckboxView;
	}

	public boolean getIsAddItemPriceTableButton() {
		return isAddItemPriceTableButton;
	}

	public void setIsAddItemPriceTableButton(boolean addItemPriceTableButton) {
		isAddItemPriceTableButton = addItemPriceTableButton;
	}


	public boolean getIsItemPriceTableField() {
		return isItemPriceTableField;
	}

	public void setIsItemPriceTableField(boolean itemPriceTableField) {
		isItemPriceTableField = itemPriceTableField;
	}

	public boolean getIsItemPriceTableControl() {
		return isItemPriceTableControl;
	}

	public void setIsItemPriceTableControl(boolean itemPriceTableControl) {
		isItemPriceTableControl = itemPriceTableControl;
	}
}