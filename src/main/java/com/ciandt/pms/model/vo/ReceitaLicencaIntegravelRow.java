package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ReceitaLicenca;

import java.io.Serializable;

/**
 * 
 * Classe que representa um objeto resultante da lista da tela de
 * "Integrate Revenues".
 * 
 * @since Sep 11, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public class ReceitaLicencaIntegravelRow implements Serializable {

	/** The serial version ID. */
	private static final long serialVersionUID = -7087381146820648623L;

	/** Variavel representa se o item esta selecionado. */
	private Boolean isSelected = Boolean.FALSE;

	/**
	 * Flag para indicar se a receita pode ser editada (pagina de receita edit).
	 */
	private Boolean isReceitaEditavel = Boolean.FALSE;

	/** TO para o {@link ReceitaLicenca}. */
	private ReceitaLicenca to;

	/**
	 * Construtor default.
	 */
	public ReceitaLicencaIntegravelRow() {
	}

	/**
	 * Construtor para ReceitaLicenca.
	 * 
	 * @param receitaLicenca
	 */
	public ReceitaLicencaIntegravelRow(final ReceitaLicenca receitaLicenca) {
		this.to = receitaLicenca;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(final Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Boolean getIsReceitaEditavel() {
		return isReceitaEditavel;
	}

	public void setIsReceitaEditavel(final Boolean isReceitaEditavel) {
		this.isReceitaEditavel = isReceitaEditavel;
	}

	public ReceitaLicenca getTo() {
		return to;
	}

	public void setTo(final ReceitaLicenca to) {
		this.to = to;
	}

}