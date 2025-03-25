package com.ciandt.pms.enums;

public enum ForecastRevenueStatusEnum {

	EXPAND(1L, "Expand", "X"),
	SIGNED(2L, "Signed", "A"),
	RENEWAL(3L, "Renewal", "P");

	private Long code;
	private String name;
	private String contractLobStatusAcronym;

	ForecastRevenueStatusEnum(Long code, String name, String contractLobStatusAcronym) {
		this.code = code;
		this.name = name;
		this.contractLobStatusAcronym = contractLobStatusAcronym;
	}

	public static Long getCodeByAcronym(String acronym) {
		if (EXPAND.contractLobStatusAcronym.equals(acronym)) {
			return EXPAND.code;
		} else if (SIGNED.contractLobStatusAcronym.equals(acronym)) {
			return SIGNED.code;
		}
		throw new IllegalArgumentException();
	}
}
