package com.ciandt.pms.exception.dre;

public class DreProcessoExecutavelException extends Exception {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public DreProcessoExecutavelException() {
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
	public DreProcessoExecutavelException(final String message,
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
	public DreProcessoExecutavelException(final String message) {
		super(message);
	}

}