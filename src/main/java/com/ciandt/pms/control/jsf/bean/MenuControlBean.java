package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;

import javax.faces.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * A classe MenuControlBean proporciona as funcionalidades de controle do menu.
 * 
 * @since 02/09/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MenuControlBean implements Serializable {

    /** defaul serial version. */
    private static final long serialVersionUID = 1L;

    /** Indicador do menu corrente. */
    private String menu;

    /**
     * @return the menu
     */
    public String getMenu() {
        if (!StringUtils.isEmpty(menu)) {
            return menu;
        } else {
            return "";
        }
    }

    /**
     * @param menu
     *            the menu to set
     */
    public void setMenu(final String menu) {
        this.menu = menu;
    }

    /**
     * Atualiza e marca o nome do grupo corrente para ser exibido.
     * 
     * @param evt
     *            - evento corrente
     */
    public void updateGroup(final ActionEvent evt) {
        menu = evt.getComponent().getParent().getId();
    }

}