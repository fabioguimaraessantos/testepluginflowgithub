package com.ciandt.pms.control.jsf.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;


/**
 * Respons�vel por abstrair parte da l�gica de apresenta��o como: mensagens,
 * valida��o de formul�rio, popular dados do TO no form, etc.
 */
public final class Messages {

    /**
     * Contrutor privado.
     */
    private Messages() {
    }

    /** Inst�ncia do logger. */
    private static Logger logger = LogManager.getLogger(Messages.class);

    /**
     * Mensagem de Alerta sem par�metros.
     * 
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showWarning(final String clientId, final String bundleKey) {

        showMessages(clientId, bundleKey, null, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de Alerta com um par�metro.
     * 
     * @param param
     *            O par�metro a ser substitu�do na mensagem.
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showWarning(final String clientId,
            final String bundleKey, final String param) {

        showMessages(clientId, bundleKey, new Object[]{param},
                FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de Alerta com par�metros.
     * 
     * @param params
     *            Os par�metros a serem substitu�dos na mensagem.
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showWarning(final String clientId,
            final String bundleKey, final Object[] params) {

        showMessages(clientId, bundleKey, params, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de Erro sem par�metros.
     * 
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showError(final String clientId, final String bundleKey) {

        showMessages(clientId, bundleKey, null, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro com um par�metro.
     * 
     * @param param
     *            O par�metro a ser substitu�do na mensagem.
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showError(final String clientId, final String bundleKey,
            final String param) {

        showMessages(clientId, bundleKey, new Object[]{param},
                FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro com par�metros.
     *
     * @param params
     *            Os par�metros a serem substitu�dos na mensagem.
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showError(final String clientId, final String bundleKey,
                                 final Object[] params) {

        showMessages(clientId, bundleKey, params, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro sem passar pelo bundle.
     *
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param message
     *            A mensagem a ser exibida
     */
    public static void showErrorWithoutBundle(final String clientId, final String message) {
        showMessagesWithoutBundle(clientId, message, null, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro sem passar pelo bundle com par�metros.
     *
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param message
     *            A mensagem a ser exibida
     * @param params
     *            Os par�metros a serem substitu�dos na mensagem.
     */
    public static void showErrorWithoutBundle(final String clientId, final String message,
                                            final Object[] params) {
        showMessages(clientId, message, params, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de WARN sem passar pelo bundle.
     * @param clientId O nome do elemento do form que est� sendo validado.
     * @param message A mensagem a ser exibida
     */
    public static void showWarningWithoutBundle(final String clientId, final String message) {
        showMessagesWithoutBundle(clientId, message, null, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de WARN sem passar pelo bundle.
     * @param clientId O nome do elemento do form que est� sendo validado.
     * @param message A mensagem a ser exibida
     * @param params Os par�metros a serem substitu�dos na mensagem.
     */
    public static void showWarnWithoutBundle(final String clientId, final String message,
                                              final Object[] params) {
        showMessages(clientId, message, params, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Sucesso sem par�metros.
     * 
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showSucess(final String clientId, final String bundleKey) {

        showMessages(clientId, bundleKey, null, FacesMessage.SEVERITY_INFO);
    }

    /**
     * Mensagem de Sucesso com um par�metro.
     * 
     * @param param
     *            O par�metro a ser substitu�do na mensagem.
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showSucess(final String clientId,
            final String bundleKey, final String param) {

        showMessages(clientId, bundleKey, new Object[]{param},
                FacesMessage.SEVERITY_INFO);
    }

    /**
     * Mensagem de Sucesso com par�metros.
     * 
     * @param params
     *            Os par�metros a serem substitu�dos na mensagem.
     * @param clientId
     *            O nome do elemento do form que est� sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showSucess(final String clientId,
            final String bundleKey, final Object[] params) {

        showMessages(clientId, bundleKey, params, FacesMessage.SEVERITY_INFO);
    }

    /**
     * Salva as mensagens no FacesContext para exibi��o.
     * 
     * @param clientId
     *            O clientId.
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os par�metros da mensagem.
     * @param severity
     *            A severidade do erro.
     */
    private static void showMessages(final String clientId,
            final String bundleKey, final Object[] params,
            final Severity severity) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        // Esta condi��o(if) � feita para os teste unit�rios,
        // pois n�o existe contexto ao se executar os testes unit�rios.
        if (facesContext != null) {
            facesContext.addMessage(clientId, getFacesMessage(severity,
                    bundleKey, params));
        }
    }

    /**
     * Salva as mensagens no FacesContext para exibi��o.
     *
     * @param clientId
     *            O clientId.
     * @param message
     *            A bundleKey.
     * @param params
     *            Os par�metros da mensagem.
     * @param severity
     *            A severidade do erro.
     */
    private static void showMessagesWithoutBundle(final String clientId,
                                     final String message, final Object[] params,
                                     final Severity severity) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        // Esta condi��o(if) � feita para os teste unit�rios,
        // pois n�o existe contexto ao se executar os testes unit�rios.
        if (facesContext != null) {
            facesContext.addMessage(clientId, getFacesMessageWithoutBundle(severity,
                    message, params));
        }
    }

    /**
     * M�todo que cria mensagem de alerta para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageWarning(final String bundleKey) {

        return getFacesMessage(FacesMessage.SEVERITY_WARN, bundleKey, null);
    }

    /**
     * M�todo que cria mensagem de alerta para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param param
     *            par�metro da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageWarning(final String bundleKey,
            final String param) {

        return getFacesMessage(FacesMessage.SEVERITY_WARN, bundleKey,
                new Object[]{param});
    }

    /**
     * M�todo que cria mensagem de alerta para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os par�metros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageWarning(final String bundleKey,
            final Object[] params) {

        return getFacesMessage(FacesMessage.SEVERITY_WARN, bundleKey, params);
    }

    /**
     * M�todo que cria mensagem de erro para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageError(final String bundleKey) {

        return getFacesMessage(FacesMessage.SEVERITY_ERROR, bundleKey, null);
    }

    /**
     * M�todo que cria mensagem de erro para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param param
     *            par�metro da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageError(final String bundleKey,
            final String param) {

        return getFacesMessage(FacesMessage.SEVERITY_ERROR, bundleKey,
                new Object[]{param});
    }

    /**
     * M�todo que cria mensagem de erro para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os par�metros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageError(final String bundleKey,
            final Object[] params) {

        return getFacesMessage(FacesMessage.SEVERITY_ERROR, bundleKey, params);
    }

    /**
     * M�todo que cria mensagem de sucesso para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageSucess(final String bundleKey) {

        return getFacesMessage(FacesMessage.SEVERITY_INFO, bundleKey, null);
    }

    /**
     * M�todo que cria mensagem de sucesso para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param param
     *            par�metro da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageSucess(final String bundleKey,
            final String param) {

        return getFacesMessage(FacesMessage.SEVERITY_INFO, bundleKey,
                new Object[]{param});
    }

    /**
     * M�todo que cria mensagem de sucesso para exibi��o.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os par�metros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageSucess(final String bundleKey,
            final Object[] params) {

        return getFacesMessage(FacesMessage.SEVERITY_INFO, bundleKey, params);
    }

    /**
     * M�todo que cria mensagens para exibi��o.
     * 
     * @param severity
     *            A severidade do message.
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os par�metros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    private static FacesMessage getFacesMessage(final Severity severity,
            final String bundleKey, final Object[] params) {

        FacesMessage message = null;

        String text = null;

        try {
            text = BundleUtil.getBundle(bundleKey, params);
            message = new FacesMessage(severity, text, text);

        } catch (Exception e) {
            logger.error(e);
        }

        return message;
    }

    /**
     * M�todo que cria mensagens para exibi��o.
     *
     * @param severity
     *            A severidade do message.
     * @param messageText
     *            A messageText.
     * @param params
     *            Os par�metros da mensagem.
     *
     * @return retorna um objeto FacesMessage
     */
    private static FacesMessage getFacesMessageWithoutBundle(final Severity severity,
                                                final String messageText, final Object[] params) {

        FacesMessage message = null;

        String text = null;

        try {
            MessageFormat mf = new MessageFormat(messageText);
            text = mf.format(params, new StringBuffer(), null).toString();
            message = new FacesMessage(severity, text, text);

        } catch (Exception e) {
            logger.error(e);
        }

        return message;
    }

    /**
     * Limpa todas as mensagens do contexto.
     */
    public static void clearMessages() {
        FacesContext context = FacesContext.getCurrentInstance();

        // itera as mensagens exluindo cada uma
        for (Iterator<FacesMessage> iterator = context.getMessages(); iterator
                .hasNext();) {
            iterator.next();
            iterator.remove();
        }
    }

}