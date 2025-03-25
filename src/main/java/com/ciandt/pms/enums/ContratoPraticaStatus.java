package com.ciandt.pms.enums;

import com.ciandt.pms.model.ContratoPratica;

/**
 * Enum que representa o indicador de status da entidade {@link ContratoPratica}
 * .
 * 
 * @since Dez 15, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public enum ContratoPraticaStatus {

	COMPLETE("C"), INCOMPLETE("I");

	private String indicadorStatus;

	public String getIndicadorStatus() {
		return indicadorStatus;
	}

	ContratoPraticaStatus(String indicadorStatus) {
		this.indicadorStatus = indicadorStatus;
	}

}
