package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.AjusteReceita;

/**
 * 
 * @author peter
 * 
 */
public class AjusteReceitaRow {
	
	public static final String ORIGINAL_DEAL = "Orig. Deal";
	public static final String NEW_DEAL = "New Deal";
	public static final String EMPTY_DEAL = "-";

	private AjusteReceita ajusteReceita;
	private boolean exibeLinha;
	private int quantidadeRowSpan;
	private String dealReferencia;

	/**
	 * @return the ajusteReceita
	 */
	public AjusteReceita getAjusteReceita() {
		return ajusteReceita;
	}

	/**
	 * @param ajusteReceita
	 *            the ajusteReceita to set
	 */
	public void setAjusteReceita(AjusteReceita ajusteReceita) {
		this.ajusteReceita = ajusteReceita;
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
	
	public boolean isOriginalDeal() {
		return dealReferencia.equals(ORIGINAL_DEAL)
				|| dealReferencia.equals(EMPTY_DEAL);
	}
	

}
