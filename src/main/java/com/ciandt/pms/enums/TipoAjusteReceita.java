package com.ciandt.pms.enums;

/**
 * Define os tipos de Ajuste de Receita.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
public enum TipoAjusteReceita {

	LOSS(1L), REPROVAL(2L), ADJUSTMENT(3L), LOSS_BALANCEONLY(4L);

	private Long code;

	public Long getCode() {
		return code;
	}

	TipoAjusteReceita(Long code) {
		this.code = code;
	}
}