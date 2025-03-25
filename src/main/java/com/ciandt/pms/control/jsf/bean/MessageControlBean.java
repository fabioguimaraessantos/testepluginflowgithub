package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * A classe MessageControlBean proporciona as funcionalidades de controle das
 * mensagens de validação dos formulários.
 * 
 * @since 31/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MessageControlBean implements Serializable {

    /** defaul serial version. */
    private static final long serialVersionUID = 1L;

    /** Atributo que define se as mensagens de valiação devem ser exibidas. */
    private Boolean displayMessages = Boolean.TRUE;

    /**
     * Seta o atributo showMessage.
     * 
     * @param displayMsg
     *            valor para atribuir ao atributo
     */
    public void setDisplayMessages(final Boolean displayMsg) {
        this.displayMessages = displayMsg;
    }

    /**
     * Pega o valor do atributo showMessages.
     * 
     * @return retorna o valor do atributo
     */
    public Boolean getDisplayMessages() {
        return displayMessages;
    }

    /**
     * Oculta as mensagens do layout principal.
     * 
     * @param evt
     *            - evento corrente
     */
    public void hideMessages(final ActionEvent evt) {
        this.displayMessages = Boolean.FALSE;
    }

    /**
     * Mostra as mensagens do layout principal.
     * 
     * @param evt
     *            - evento corrente
     */
    public void showMessages(final ActionEvent evt) {
        this.displayMessages = Boolean.TRUE;
    }
}
