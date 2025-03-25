package com.ciandt.pms.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum com as siglas de tipo de serviço
 * 
 * @since 15/10/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
public enum SgTipoServico {

	DEVP("DEVP"), SUPT("SUPT"), LICE("LICE"), ELAB("ELAB"), PGEN("PGEN"), 
	SUPP("SUPP"), CONS("CONS"), TRNG("TRNG"), REIM("REIM");

	private String abbreviation;

	SgTipoServico(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public static List<String> getAllForServiceRevenue() {

		List<String> siglasTipoServico = new ArrayList<String>();

		siglasTipoServico.add(SgTipoServico.CONS.toString());
		siglasTipoServico.add(SgTipoServico.DEVP.toString());
		siglasTipoServico.add(SgTipoServico.ELAB.toString());
		siglasTipoServico.add(SgTipoServico.PGEN.toString());
		siglasTipoServico.add(SgTipoServico.REIM.toString());
		siglasTipoServico.add(SgTipoServico.SUPP.toString());
		siglasTipoServico.add(SgTipoServico.SUPT.toString());
		siglasTipoServico.add(SgTipoServico.TRNG.toString());

		return siglasTipoServico;
	}

	public static List<String> getAllForLicenseRevenue() {

		List<String> siglasTipoServico = new ArrayList<String>();

		siglasTipoServico.add(SgTipoServico.LICE.toString());

		return siglasTipoServico;
	}
}
