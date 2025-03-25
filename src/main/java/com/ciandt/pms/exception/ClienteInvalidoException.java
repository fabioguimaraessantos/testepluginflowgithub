package com.ciandt.pms.exception;

import java.io.IOException;

public class ClienteInvalidoException extends IOException {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public ClienteInvalidoException() {
		super();
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da excecao
	 * @param cause
	 *            - causa da excecao
	 */
	public ClienteInvalidoException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da excecao
	 */
	public ClienteInvalidoException(final String message) {
		super(message);
	}

}