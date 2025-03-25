package com.ciandt.pms.enums;

/**
 * Enum que define os status do DreMes.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
public enum StatusDreMes {

	COMPLETE("C", "Complete"), INCOMPLETE("I", "Inconplete");

	private String abbreviation;
	private String name;

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public String getName() {
		return this.name;
	}

	StatusDreMes(String abbreviation, String name) {
		this.abbreviation = abbreviation;
		this.name = name;
	}

}
