package com.ciandt.pms.exception;

/**
 * A classe TaxaImpostoNotFoundException ser� utilizada para tratamento de erros
 * refer�ntes � taxas de impostos nao encontradas em calculos.
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
	 *            - mensagem da exce��o
	 * @param cause
	 *            - causa da exce��o
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
	 *            - mensagem da exce��o
	 * 
	 */
	public TaxaImpostoNotFoundException(final String message) {
		super(message);
	}

}