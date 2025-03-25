package com.ciandt.pms.model.vo;

import javax.servlet.http.HttpSession;

/**
 * 
 * A classe SessionRow representa a linha na listagem de sessoes. 
 *
 * @since 27/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class SessionRow implements java.io.Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Indica se a linha esta selecionada. */
    private boolean isSelected = false;

    /** Indica o login do usuário. */
    private String username;
    
    /** Sessao Http. */
    private HttpSession session;
    
    
    
    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param session the session to set
     */
    public void setSession(final HttpSession session) {
        this.session = session;
    }

    /**
     * @return the session
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
