package com.ciandt.pms.enums;

import com.ciandt.pms.model.NaturezaCentroLucro;

/**
 * 
 * Enum que representa a sigla da entidade {@link NaturezaCentroLucro}.
 * 
 * @since Aug 19, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public enum NaturezaCentroLucroSigla {

	UMKT("UMKT"), SSO("SSO"), BUSINESS_MANAGER("BM"), SENIOR_MANAGER("SM"), LOB_MANAGER(
			"LOBM"), AE("AE"), LOB("LOB"), SUB_LOB("SLOB");

	private String sigla;

	public String getSigla() {
		return sigla;
	}

	NaturezaCentroLucroSigla(String sigla) {
		this.sigla = sigla;
	}

}
