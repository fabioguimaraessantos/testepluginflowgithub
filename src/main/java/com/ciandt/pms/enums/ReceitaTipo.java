package com.ciandt.pms.enums;

/**
 * 
 * Enum que representa os tipos de receita.
 * 
 * @since Aug 19, 2015
 * @author <a href="mailto:mvidolin@ciandt.com">Luiz Souza</a>
 * 
 */
public enum ReceitaTipo {

	SERVICO("SERVICO"), LICENCA("SSO"), PLANTAO("PLANTAO");

	private String sigla;

	public String getSigla() {
		return sigla;
	}

	ReceitaTipo(String sigla) {
		this.sigla = sigla;
	}

}
