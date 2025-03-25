package com.ciandt.pms.enums;

/**
 * Enum com os Status do processamento
 * 
 * @since 15/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
public enum StatusDreProcesso {

	PERFORMED("PE", "Performed"), IN_PROGRESS("IP", "In Progress"), INVALIDATE(
			"IN", "Invalidated"), ERROR("ER", "Error"), LOCKED("LK", "Locked"), NOT_PERFORMED(
			"NP", "Not Performed");

	private String abbreviation;
	private String name;

	public String getAbbreviation() {
		return this.abbreviation;
	}

	public String getName() {
		return this.name;
	}

	StatusDreProcesso(String abbreviation, String name) {
		this.abbreviation = abbreviation;
		this.name = name;
	}

	public static StatusDreProcesso getByAbbreviation(String abreviation) {
		for (StatusDreProcesso status : StatusDreProcesso.values()) {
			if (status.abbreviation.equals(abreviation)) {
				return status;
			}
		}
		return null;
	}

}
