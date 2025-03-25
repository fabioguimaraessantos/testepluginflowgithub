package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.AlocacaoOverhead;

public class AlocacaoOverheadRow {

	private AlocacaoOverhead alocacaoOverhead;
	private Boolean showEditButton;

	/**
	 * @return the alocacaoOverhead
	 */
	public AlocacaoOverhead getAlocacaoOverhead() {
		return alocacaoOverhead;
	}
	/**
	 * @param alocacaoOverhead the alocacaoOverhead to set
	 */
	public void setAlocacaoOverhead(AlocacaoOverhead alocacaoOverhead) {
		this.alocacaoOverhead = alocacaoOverhead;
	}
	/**
	 * @return the showEditButton
	 */
	public Boolean getShowEditButton() {
		return showEditButton;
	}
	/**
	 * @param showEditButton the showEditButton to set
	 */
	public void setShowEditButton(Boolean showEditButton) {
		this.showEditButton = showEditButton;
	}
}
