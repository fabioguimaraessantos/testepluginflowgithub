package com.ciandt.pms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * A entidade VwMegaProjeto exibe todos os projetos disponiveis no mega.
 * 
 * @since 04/09/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 */
@Entity
@Table(name = "VW_PMS_PROJETO")
@NamedQueries({ @NamedQuery(name = VwPMSProjeto.FIND_BY_PROJETO_EMPRESA, query = "SELECT t FROM VwPMSProjeto t JOIN FETCH t.empresa e WHERE e.codigoEmpresa = :codigoEmpresa AND t.codigoProjeto = :codigoProjeto "), })
public class VwPMSProjeto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2173304198331554956L;

	/** Constante para NamedQuery "VwPMSProjeto.findByProjetoEmpresa". */
	public static final String FIND_BY_PROJETO_EMPRESA = "VwPMSProjeto.findByProjetoEmpresa";

	/**
	 * Atributo gerado a partir da coluna CUS_IN_REDUZIDO.
	 */
	@Id
	@Column(name = "COD_IN_PROJETO")
	private Long codigoProjeto;

	/**
	 * Atributo gerado a partir da coluna CUS_ST_DESCRICAO.
	 */
	@Column(name = "PRO_ST_DESCRICAO", length = 200)
	private String descricao;

	/**
	 * Atributo gerado a partir da coluna PRO_PAD_IN_CODIGO.
	 */
	@Column(name = "PRO_PAD_IN_CODIGO", nullable = false, precision = 3, scale = 0)
	private Long codigoPadraoProjeto;

	/**
	 * Atributo gerado a partir da coluna IN_PENDENTE.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPR_CD_EMPRESA")
	private Empresa empresa;

	/**
	 * Obtem o valor do atributo {@link VwPMSProjeto#codigoProjeto}.<BR>
	 * 
	 * @return the codigoProjeto
	 */
	public Long getCodigoProjeto() {
		return codigoProjeto;
	}

	/**
	 * Atualiza o valor do atributo codigoProjeto.<BR>
	 * 
	 * @param codigoProjeto
	 *            Novo valor para o atributo {@link VwPMSProjeto#codigoProjeto}
	 *            .
	 */
	public void setCodigoProjeto(Long codigoProjeto) {
		this.codigoProjeto = codigoProjeto;
	}

	/**
	 * Obtem o valor do atributo {@link VwPMSProjeto#descricao}.<BR>
	 * 
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Atualiza o valor do atributo descricao.<BR>
	 * 
	 * @param descricao
	 *            Novo valor para o atributo {@link VwPMSProjeto#descricao}.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtem o valor do atributo {@link VwPMSProjeto#codigoPadraoProjeto}.<BR>
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
	 *            {@link VwPMSProjeto#codigoPadraoProjeto}.
	 */
	public void setCodigoPadraoProjeto(Long codigoPadraoProjeto) {
		this.codigoPadraoProjeto = codigoPadraoProjeto;
	}

	/**
	 * Obtem o valor do atributo {@link VwPMSProjeto#empresa}.<BR>
	 * 
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * Atualiza o valor do atributo empresa.<BR>
	 * 
	 * @param empresa
	 *            Novo valor para o atributo {@link VwPMSProjeto#empresa}.
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

}