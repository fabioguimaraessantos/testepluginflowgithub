package com.ciandt.pms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.ciandt.pms.Constants;

/**
 * A entidade VwMegaCCusto representa um Cost Center pendente, ou seja, um cost
 * center do mega que ainda nao foi ativado no PMS. Entity gerado a partir da
 * view VW_MEGA_CCUSTO.
 * 
 * @since Out, 11 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Entity
@Table(name = "VW_MEGA_CCUSTO")
@NamedQueries({
		@NamedQuery(name = VwMegaCCusto.FIND_ALL, query = "SELECT t FROM VwMegaCCusto t JOIN FETCH t.empresa order by t.descricao"),
		@NamedQuery(name = VwMegaCCusto.FIND_ALL_FOR_COMBOBOX, query = "SELECT new VwMegaCCusto(t.descricao) FROM VwMegaCCusto t order by t.descricao"),
		@NamedQuery(name = VwMegaCCusto.FIND_ALL_PENDING, query = "SELECT t FROM VwMegaCCusto t JOIN FETCH t.empresa "
				+ " WHERE t.indicadorPendente = 'S' order by t.descricao"),
		@NamedQuery(name = VwMegaCCusto.FIND_PENDING_BY_FORM_FILTER, query = "SELECT t FROM VwMegaCCusto t JOIN FETCH t.empresa "
				+ " WHERE TRIM(UPPER(t.descricao)) LIKE '%'||TRIM(UPPER(:name))||'%' "
				+ " AND t.indicadorPendente = 'S' order by t.descricao") })
public class VwMegaCCusto implements Serializable {

	/** Seial number ID. */
	private static final long serialVersionUID = 5800356678216333012L;

	/** Constante para NamedQuery "VwMegaCCusto.findAll". */
	public static final String FIND_ALL = "VwMegaCCusto.findAll";

	public static final String FIND_ALL_FOR_COMBOBOX = "VwMegaCCusto.findAllForCombobox";

	/** Constante para NamedQuery "VwMegaCCusto.findAllPending". */
	public static final String FIND_ALL_PENDING = "VwMegaCCusto.findAllPending";

	/** Constante para NamedQuery "VwMegaCCusto.findByFormFilter". */
	public static final String FIND_PENDING_BY_FORM_FILTER = "VwMegaCCusto.findPendingByFormFilter";

	/**
	 * Atributo gerado a partir da coluna CUS_IN_REDUZIDO.
	 */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "CUS_IN_REDUZIDO", column = @Column(name = "CUS_IN_REDUZIDO", nullable = false, precision = 7, scale = 0)),
			@AttributeOverride(name = "CUS_PAD_IN_CODIGO", column = @Column(name = "CUS_PAD_IN_CODIGO", nullable = false, precision = 7, scale = 0)) })
	private VwMegaCCustoId vwMegaCCustoId;

	/**
	 * Atributo gerado a partir da coluna CUS_ST_DESCRICAO.
	 */
	@Column(name = "CUS_ST_DESCRICAO", length = 200)
	private String descricao;

	/**
	 * Atributo gerado a partir da coluna IN_PENDENTE.
	 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPR_CD_EMPRESA")
	private Empresa empresa;

	public VwMegaCCusto() {
	}

	public VwMegaCCusto(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtem o valor do atributo {@link VwMegaCCusto#empresa}.<BR>
	 *
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * Atualiza o valor do atributo empresa.<BR>
	 *
	 * @param empresa Novo valor para o atributo {@link VwMegaCCusto#empresa}.
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * Atributo gerado a partir da coluna IN_PENDENTE.
	 */
	@Column(name = "IN_PENDENTE")
	private String indicadorPendente;

	/**
	 * Obtem o valor do atributo descricao.<BR>
	 * Atributo gerado a partir da coluna CUS_ST_DESCRICAO.
	 * 
	 * @return Valor do atributo descricao.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atualiza o valor do atributo descricao.<BR>
	 * Atributo gerado a partir da coluna CUS_ST_DESCRICAO.
	 * 
	 * @param descricao
	 *            Novo valor para o atributo descricao.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtem o valor do atributo indicadorPendente.<BR>
	 * Atributo gerado a partir da coluna IN_PENDENTE.
	 * 
	 * @return Valor do atributo indicadorPendente.
	 */
	public String getIndicadorPendente() {
		return indicadorPendente;
	}

	/**
	 * Atualiza o valor do atributo indicadorPendente.<BR>
	 * Atributo gerado a partir da coluna IN_PENDENTE.
	 * 
	 * @param indicadorPendente
	 *            Novo valor para o atributo indicadorPendente.
	 */
	public void setIndicadorPendente(String indicadorPendente) {
		this.indicadorPendente = indicadorPendente;
	}

	/**
	 * Converte a entidade corrente para um objeto do tipo {@link GrupoCusto}.
	 * 
	 * @return {@link GrupoCusto}
	 */
	public GrupoCusto toGrupoCusto() {
		GrupoCusto gcusto = new GrupoCusto();
		gcusto.setNomeGrupoCusto(this.descricao);
		gcusto.setIndicadorAtivo("A");
		List<GrupoCustoPeriodo> grupoCustoPeriodos = new ArrayList<GrupoCustoPeriodo>();
		GrupoCustoPeriodo grupoCustoPeriodo = new GrupoCustoPeriodo();
		grupoCustoPeriodo.setGrupoCusto(gcusto);
		grupoCustoPeriodo.setDataInicio(new Date());
		grupoCustoPeriodos.add(grupoCustoPeriodo);
		gcusto.setErpCodigoCentroCusto(this.vwMegaCCustoId.getCodigoReduzido());
		gcusto.setErpCentroCustoPadrao(this.vwMegaCCustoId.getCodigoPadrao());
		gcusto.setGrupoCustoPeriodos(grupoCustoPeriodos);
		gcusto.setIndicadorDeletado(Constants.NO);
		return gcusto;
	}

	/**
	 * Converte a entidade corrente para um objeto do tipo {@link Convergencia}.
	 * 
	 * @return {@link Convergencia}
	 */
	public Convergencia toConvergencia() {
		Convergencia convergencia = new Convergencia();
		convergencia.setCodigoCentroCustoMega(vwMegaCCustoId.getCodigoReduzido());
		convergencia.setSiglaTipo("GC");
		convergencia.setGrupoCusto(this.toGrupoCusto());
		return convergencia;
	}

}