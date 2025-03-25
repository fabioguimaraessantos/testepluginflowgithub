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
	 *            - mensagem da exceção
	 * @param cause
	 *            - causa da exceção
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
	 *            - mensagem da exceção
	 * 
	 */
	public DocumentoLegalVigenciaException(final String message) {
		super(message);
	}

}