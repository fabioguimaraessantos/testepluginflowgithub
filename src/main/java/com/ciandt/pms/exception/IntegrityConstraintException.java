package com.ciandt.pms.exception;

/**
 * 
 * A classe IntegrityConstraintException ser� utilizada para 
 * tratamento de erros de refer�ncia de integridade.
 *
 * @since 11/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 *
 */
public class IntegrityConstraintException extends Exception {

    /** serial version Id. */
    private static final long serialVersionUID = 1L;
    
    private String relatedEntityName;

    /**
     * Metodo construtor padrao.
     */
    public IntegrityConstraintException() {
        super();
    }

    /**
     * Metodo construtor.
     * 
     * @param message - mensagem da exce��o
     * @param cause - causa da exce��o
     * 
     */
    public IntegrityConstraintException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Metodo construtor.
     * 
     * @param message - mensagem da exce��o
     * 
     */
    public IntegrityConstraintException(final String message) {
        super(message);
    }

    /**
     * Metodo construtor.
     * 
     * @param message - mensagem da exce��o
     * 
     */
    public IntegrityConstraintException(final String message, final String relatedEntityName) {
        super(message);
        this.relatedEntityName = relatedEntityName;
    }

    /**
     * Metodo construtor.
     * 
     * @param cause - causa da exce��o
     * 
     */
    public IntegrityConstraintException(final Throwable cause) {
        super(cause);
    }

    /**
     * @return the relatedEntityName
     */
    public String getRelatedEntityName() {
        return relatedEntityName;
    }
    
}