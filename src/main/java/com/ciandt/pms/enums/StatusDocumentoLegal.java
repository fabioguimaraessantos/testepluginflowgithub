package com.ciandt.pms.enums;

/**
 * Enum que define os status do Documento Legal
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
public enum StatusDocumentoLegal {

	ACTIVE("A", "Active"), INACTIVE("I", "Inactive"), CLOSED("C", "Closed");

	private String abbreviation;
	private String name;

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public String getName() {
		return this.name;
	}

	StatusDocumentoLegal(String abbreviation, String name) {
		this.abbreviation = abbreviation;
		this.name = name;
	}
	
	public static StatusDocumentoLegal getByAbbreviation(String abreviation) {
		for (StatusDocumentoLegal status : StatusDocumentoLegal.values()) {
			if (status.abbreviation.equals(abreviation)) {
				return status;
			}
		}
		return null;
	}
	
	public static StatusDocumentoLegal getByName(String name) {
		for (StatusDocumentoLegal status : StatusDocumentoLegal.values()) {
			if (status.name.equals(name)) {
				return status;
			}
		}
		return null;
	}

}
