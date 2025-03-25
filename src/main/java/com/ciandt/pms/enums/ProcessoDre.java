package com.ciandt.pms.enums;

/**
 * Enum que define os processos do Closing DRE.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
public enum ProcessoDre {

	TCE_VALIDATION(1), GENERAL_VALIDATION(2), RESOURCE_VALIDATION(3), DUTY_HOURS_AND_OVERTIME(
			4), SET_EXCHANGE_RATES(5);

	private Integer codigo;

	public Integer getCodigo() {
		return codigo;
	}

	ProcessoDre(Integer codigo) {
		this.codigo = codigo;
	}

}
