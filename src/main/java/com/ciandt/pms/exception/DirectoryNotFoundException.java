package com.ciandt.pms.exception;

import java.io.IOException;

public class DirectoryNotFoundException extends IOException {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public DirectoryNotFoundException() {
		super();
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da exceção
	 * @param cause
	 *            - causa da exceção
	 */
	public DirectoryNotFoundException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da exceção
	 */
	public DirectoryNotFoundException(final String message) {
		super(message);
	}

}