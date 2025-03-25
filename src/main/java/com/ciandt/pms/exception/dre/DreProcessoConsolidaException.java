package com.ciandt.pms.exception.dre;

public class DreProcessoConsolidaException extends DreProcessoExecutavelException {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public DreProcessoConsolidaException() {
		super();
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da exce��o
	 * @param cause
	 *            - causa da exce��o
	 * 
	 */
	public DreProcessoConsolidaException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da exce��o
	 * 
	 */
	public DreProcessoConsolidaException(final String message) {
		super(message);
	}

}