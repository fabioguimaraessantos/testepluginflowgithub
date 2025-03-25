package com.ciandt.pms.exception;

/**
 * A classe TaxaImpostoNotFoundException será utilizada para tratamento de erros
 * referêntes à taxas de impostos nao encontradas em calculos.
 * 
 * @since 12/11/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public class TaxaImpostoNotFoundException extends Exception {

	/** serial version Id. */
	private static final long serialVersionUID = 1L;

	/**
	 * Metodo construtor padrao.
	 */
	public TaxaImpostoNotFoundException() {
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
	public TaxaImpostoNotFoundException(final String message,
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
	public TaxaImpostoNotFoundException(final String message) {
		super(message);
	}

}