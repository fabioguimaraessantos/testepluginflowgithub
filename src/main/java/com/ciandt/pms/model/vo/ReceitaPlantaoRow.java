package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ReceitaPlantao;

/**
 * 
 * @author luizsj
 * 
 */
public class ReceitaPlantaoRow {

	private ReceitaPlantao receitaPlantao;
	private boolean exibeLinha;
	private int quantidadeRowSpan;
	private String dealReferencia;

	/**
	 * @return the receitaPlantao
	 */
	public ReceitaPlantao getReceitaPlantao() {
		return receitaPlantao;
	}

	/**
	 * @param receitaPlantao the receitaPlantao to set
	 */
	public void setReceitaPlantao(ReceitaPlantao receitaPlantao) {
		this.receitaPlantao = receitaPlantao;
	}

	/**
	 * @return the exibeLinha
	 */
	public boolean isExibeLinha() {
		return exibeLinha;
	}

	/**
	 * @param exibeLinha
	 *            the exibeLinha to set
	 */
	public void setExibeLinha(boolean exibeLinha) {
		this.exibeLinha = exibeLinha;
	}

	/**
	 * @return the quantidadeRowSpan
	 */
	public int getQuantidadeRowSpan() {
		return quantidadeRowSpan;
	}

	/**
	 * @param quantidadeRowSpan
	 *            the quantidadeRowSpan to set
	 */
	public void setQuantidadeRowSpan(int quantidadeRowSpan) {
		this.quantidadeRowSpan = quantidadeRowSpan;
	}

	/**
	 * @return the dealReferencia
	 */
	public String getDealReferencia() {
		return dealReferencia;
	}

	/**
	 * @param dealReferencia
	 *            the dealReferencia to set
	 */
	public void setDealReferencia(String dealReferencia) {
		this.dealReferencia = dealReferencia;
	}

}
