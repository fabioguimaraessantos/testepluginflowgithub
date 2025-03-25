package com.ciandt.pms.exception.dre;

public class DreProcessoPlantaoHorasExtrasException extends DreProcessoExecutavelException {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public DreProcessoPlantaoHorasExtrasException() {
		super();
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da exceção
	 * @param cause
	 *            - causa da exceção
	 * 
	 */
	public DreProcessoPlantaoHorasExtrasException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da exceção
	 * 
	 */
	public DreProcessoPlantaoHorasExtrasException(final String message) {
		super(message);
	}

}