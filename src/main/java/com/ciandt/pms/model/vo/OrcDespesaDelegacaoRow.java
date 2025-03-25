package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.OrcamentoDespesa;

/**
 * Classe que representa uma linha da tela de OrcDespesaDelegacao.
 *
 * @author peter
 *
 */
public class OrcDespesaDelegacaoRow {

	/** OrcamentoDespesa. */
	private OrcamentoDespesa orcDespesa = new OrcamentoDespesa();

	/** Nome do cliente ou grupo de custo . */
	private String nomeClientOrCostgroup;

	private Long codigoGrupoCusto;

	public Long getCodigoGrupoCusto(){return codigoGrupoCusto;}

	public void setCodigoGrupoCusto(Long codigoGrupoCusto) {this.codigoGrupoCusto = codigoGrupoCusto;}

	/**
	 * @return the orcDespesa
	 */
	public OrcamentoDespesa getOrcDespesa() {
		return orcDespesa;
	}

	/**
	 * @param orcDespesa
	 *            the orcDespesa to set
	 */
	public void setOrcDespesa(OrcamentoDespesa orcDespesa) {
		this.orcDespesa = orcDespesa;
	}

	/**
	 * @return the nomeClientOrCostgroup
	 */
	public String getNomeClientOrCostgroup() {
		return nomeClientOrCostgroup;
	}

	/**
	 * @param nomeClientOrCostgroup
	 *            the nomeClientOrCostgroup to set
	 */
	public void setNomeClientOrCostgroup(String nomeClientOrCostgroup) {
		this.nomeClientOrCostgroup = nomeClientOrCostgroup;
	}

}