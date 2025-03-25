package com.ciandt.pms.exception;

public class DocumentoLegalVigenciaException extends Exception {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public DocumentoLegalVigenciaException() {
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
	public DocumentoLegalVigenciaException(final String message,
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
	public DocumentoLegalVigenciaException(final String message) {
		super(message);
	}

}