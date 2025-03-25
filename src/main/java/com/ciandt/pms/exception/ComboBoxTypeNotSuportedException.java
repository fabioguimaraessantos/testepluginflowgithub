package com.ciandt.pms.exception;

/**
 * 
 * A classe ComboBoxTypeNotSuportedException sera utilizada para tratamento de
 * erros referente a tipo de combo nao suportado..
 * 
 * @since Aug 18, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public class ComboBoxTypeNotSuportedException extends RuntimeException {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public ComboBoxTypeNotSuportedException() {
		super();
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da excecao
	 * @param cause
	 *            - causa da excecao
	 * 
	 */
	public ComboBoxTypeNotSuportedException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da excecao
	 * 
	 */
	public ComboBoxTypeNotSuportedException(final String message) {
		super(message);
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param message
	 *            - mensagem da excecao
	 * 
	 */
	public ComboBoxTypeNotSuportedException(final String message,
			final String relatedEntityName) {
		super(message);
	}

	/**
	 * Metodo construtor.
	 * 
	 * @param cause
	 *            - causa da excecao
	 * 
	 */
	public ComboBoxTypeNotSuportedException(final Throwable cause) {
		super(cause);
	}

}