package com.ciandt.pms.control.jsf.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;


/**
 * Responsável por abstrair parte da lógica de apresentação como: mensagens,
 * validação de formulário, popular dados do TO no form, etc.
 */
public final class Messages {

    /**
     * Contrutor privado.
     */
    private Messages() {
    }

    /** Instância do logger. */
    private static Logger logger = LogManager.getLogger(Messages.class);

    /**
     * Mensagem de Alerta sem parâmetros.
     * 
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showWarning(final String clientId, final String bundleKey) {

        showMessages(clientId, bundleKey, null, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de Alerta com um parâmetro.
     * 
     * @param param
     *            O parâmetro a ser substituído na mensagem.
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showWarning(final String clientId,
            final String bundleKey, final String param) {

        showMessages(clientId, bundleKey, new Object[]{param},
                FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de Alerta com parâmetros.
     * 
     * @param params
     *            Os parâmetros a serem substituídos na mensagem.
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showWarning(final String clientId,
            final String bundleKey, final Object[] params) {

        showMessages(clientId, bundleKey, params, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de Erro sem parâmetros.
     * 
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showError(final String clientId, final String bundleKey) {

        showMessages(clientId, bundleKey, null, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro com um parâmetro.
     * 
     * @param param
     *            O parâmetro a ser substituído na mensagem.
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showError(final String clientId, final String bundleKey,
            final String param) {

        showMessages(clientId, bundleKey, new Object[]{param},
                FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro com parâmetros.
     *
     * @param params
     *            Os parâmetros a serem substituídos na mensagem.
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
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
     *            O nome do elemento do form que está sendo validado.
     * @param message
     *            A mensagem a ser exibida
     */
    public static void showErrorWithoutBundle(final String clientId, final String message) {
        showMessagesWithoutBundle(clientId, message, null, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Erro sem passar pelo bundle com parâmetros.
     *
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param message
     *            A mensagem a ser exibida
     * @param params
     *            Os parâmetros a serem substituídos na mensagem.
     */
    public static void showErrorWithoutBundle(final String clientId, final String message,
                                            final Object[] params) {
        showMessages(clientId, message, params, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de WARN sem passar pelo bundle.
     * @param clientId O nome do elemento do form que está sendo validado.
     * @param message A mensagem a ser exibida
     */
    public static void showWarningWithoutBundle(final String clientId, final String message) {
        showMessagesWithoutBundle(clientId, message, null, FacesMessage.SEVERITY_WARN);
    }

    /**
     * Mensagem de WARN sem passar pelo bundle.
     * @param clientId O nome do elemento do form que está sendo validado.
     * @param message A mensagem a ser exibida
     * @param params Os parâmetros a serem substituídos na mensagem.
     */
    public static void showWarnWithoutBundle(final String clientId, final String message,
                                              final Object[] params) {
        showMessages(clientId, message, params, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Mensagem de Sucesso sem parâmetros.
     * 
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showSucess(final String clientId, final String bundleKey) {

        showMessages(clientId, bundleKey, null, FacesMessage.SEVERITY_INFO);
    }

    /**
     * Mensagem de Sucesso com um parâmetro.
     * 
     * @param param
     *            O parâmetro a ser substituído na mensagem.
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showSucess(final String clientId,
            final String bundleKey, final String param) {

        showMessages(clientId, bundleKey, new Object[]{param},
                FacesMessage.SEVERITY_INFO);
    }

    /**
     * Mensagem de Sucesso com parâmetros.
     * 
     * @param params
     *            Os parâmetros a serem substituídos na mensagem.
     * @param clientId
     *            O nome do elemento do form que está sendo validado.
     * @param bundleKey
     *            A chave a do arquivo de bundle.
     */
    public static void showSucess(final String clientId,
            final String bundleKey, final Object[] params) {

        showMessages(clientId, bundleKey, params, FacesMessage.SEVERITY_INFO);
    }

    /**
     * Salva as mensagens no FacesContext para exibição.
     * 
     * @param clientId
     *            O clientId.
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os parâmetros da mensagem.
     * @param severity
     *            A severidade do erro.
     */
    private static void showMessages(final String clientId,
            final String bundleKey, final Object[] params,
            final Severity severity) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        // Esta condição(if) é feita para os teste unitários,
        // pois não existe contexto ao se executar os testes unitários.
        if (facesContext != null) {
            facesContext.addMessage(clientId, getFacesMessage(severity,
                    bundleKey, params));
        }
    }

    /**
     * Salva as mensagens no FacesContext para exibição.
     *
     * @param clientId
     *            O clientId.
     * @param message
     *            A bundleKey.
     * @param params
     *            Os parâmetros da mensagem.
     * @param severity
     *            A severidade do erro.
     */
    private static void showMessagesWithoutBundle(final String clientId,
                                     final String message, final Object[] params,
                                     final Severity severity) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        // Esta condição(if) é feita para os teste unitários,
        // pois não existe contexto ao se executar os testes unitários.
        if (facesContext != null) {
            facesContext.addMessage(clientId, getFacesMessageWithoutBundle(severity,
                    message, params));
        }
    }

    /**
     * Método que cria mensagem de alerta para exibição.
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
     * Método que cria mensagem de alerta para exibição.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param param
     *            parâmetro da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageWarning(final String bundleKey,
            final String param) {

        return getFacesMessage(FacesMessage.SEVERITY_WARN, bundleKey,
                new Object[]{param});
    }

    /**
     * Método que cria mensagem de alerta para exibição.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os parâmetros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageWarning(final String bundleKey,
            final Object[] params) {

        return getFacesMessage(FacesMessage.SEVERITY_WARN, bundleKey, params);
    }

    /**
     * Método que cria mensagem de erro para exibição.
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
     * Método que cria mensagem de erro para exibição.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param param
     *            parâmetro da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageError(final String bundleKey,
            final String param) {

        return getFacesMessage(FacesMessage.SEVERITY_ERROR, bundleKey,
                new Object[]{param});
    }

    /**
     * Método que cria mensagem de erro para exibição.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os parâmetros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageError(final String bundleKey,
            final Object[] params) {

        return getFacesMessage(FacesMessage.SEVERITY_ERROR, bundleKey, params);
    }

    /**
     * Método que cria mensagem de sucesso para exibição.
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
     * Método que cria mensagem de sucesso para exibição.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param param
     *            parâmetro da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageSucess(final String bundleKey,
            final String param) {

        return getFacesMessage(FacesMessage.SEVERITY_INFO, bundleKey,
                new Object[]{param});
    }

    /**
     * Método que cria mensagem de sucesso para exibição.
     * 
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os parâmetros da mensagem.
     * 
     * @return retorna um objeto FacesMessage
     */
    public static FacesMessage getMessageSucess(final String bundleKey,
            final Object[] params) {

        return getFacesMessage(FacesMessage.SEVERITY_INFO, bundleKey, params);
    }

    /**
     * Método que cria mensagens para exibição.
     * 
     * @param severity
     *            A severidade do message.
     * @param bundleKey
     *            A bundleKey.
     * @param params
     *            Os parâmetros da mensagem.
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
     * Método que cria mensagens para exibição.
     *
     * @param severity
     *            A severidade do message.
     * @param messageText
     *            A messageText.
     * @param params
     *            Os parâmetros da mensagem.
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