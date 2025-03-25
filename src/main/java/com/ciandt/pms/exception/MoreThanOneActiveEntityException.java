package com.ciandt.pms.exception;

/**
 * A classe MoreThanOneActiveEntityException sera utilizada para tratamento de
 * erros no qual nao deve existir mais de um registro vigente ou ativo para uma
 * entidade.
 * 
 * @since Dez 15, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public class MoreThanOneActiveEntityException extends Exception {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public MoreThanOneActiveEntityException() {
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
	public MoreThanOneActiveEntityException(final String message,
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
	public MoreThanOneActiveEntityException(final String message) {
		super(message);
	}

}
