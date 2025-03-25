package com.ciandt.pms.authentication;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.springframework.security.BadCredentialsException;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.userdetails.UsernameNotFoundException;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.Messages;


/**
 * 
 * A classe AuthenticationErrorPhaseListener proporciona as funcionalidades 
 * de listener para a Autenticação (login) do sistema.
 *
 * @since 30/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class AuthenticationErrorPhaseListener implements PhaseListener {

 
    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Metodo executado após a ação de login.
     * 
     * @param ev evento
     */
    public void beforePhase(final PhaseEvent ev) {
        Map<String, Object> sessionMap = 
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Exception e = (Exception) 
            sessionMap.get(
                AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);
 
        if (e instanceof BadCredentialsException) {
            
            sessionMap.put(
                    AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY, null);
            
            Messages.showError("beforePhase", Constants.MSG_ERRO_LOGIN_INVALID_USER_PASS);
            
            sessionMap.remove("SPRING_SECURITY_LAST_USERNAME");
        }

    }
 
    /**
     * Metodo executado antes da ação de login.
     * 
     * @param ev evento
     */
    public void afterPhase(final PhaseEvent ev) {
        
    }

    /**
     * Pega o PhaseId.
     * 
     * @return PhaseId
     */
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
 
}