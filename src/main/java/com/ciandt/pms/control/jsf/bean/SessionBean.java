package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ciandt.pms.model.vo.SessionRow;


/**
 * 
 * A classe SessionBean proporciona as funcionalidades
 * referente ao gerenciamento das sessões do sistema.
 *
 * @since 26/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Component
@Scope("singleton")
public class SessionBean implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;
    /** Map com as sessões ativas. */
    private static Map<String, SessionRow> activeSessions = new HashMap<String, SessionRow>();

    /** 
     * Adiciona uma nova sessão.
     * 
     * @param session - Sessão a ser adicionada.
     */
    public void addSession(final HttpSession session) {
        SessionRow row = new SessionRow(); 
        row.setSession(session);
        row.setUsername((String) session.getAttribute(
                "SPRING_SECURITY_LAST_USERNAME"));
        getActiveSessions().put(session.getId(), row);
    }
    
    /** 
     * Remove uma nova sessão.
     * 
     * @param session - Sessão a ser removida.
     */
    public void removeSession(final HttpSession session) {
        getActiveSessions().remove(session.getId());
    }
    
    
    /**
     * Pega as chaves do Map das sessões.
     *  
     * @return retorna uma lista com as chaves do Map.
     */
    public List<String> getItemKeys() {
        List<String> keys = new ArrayList<String>();
        keys.addAll(activeSessions.keySet());
        return keys;
     }
    
    /**
     * @param activeSessions the activeSessions to set
     */
//    public void setActiveSessions(final Map<String, SessionRow> activeSessions) {
//        this.activeSessions = activeSessions;
//        
//    }

    /**
     * @return the activeSessions
     */
    public synchronized Map<String, SessionRow> getActiveSessions() {
        return activeSessions;
    }
    
    /**
     * Pega o login do usuario.
     * 
     * @return retorna
     */
    public Map<String, String> getUsersLogin() {
        List<String> keyList = getItemKeys();
        Map<String, String> ret = new HashMap<String, String>();
        for (String key : keyList) {
            ret.put(key, (String) activeSessions.get(key).getSession().
                    getAttribute("SPRING_SECURITY_LAST_USERNAME"));
        }
        
        return ret;
    }
    
    /**
     * Selecionada todos.
     */
    public void selectAll() {
        List<String> keyList = getItemKeys();
        for (String key : keyList) {
            activeSessions.get(key).setSelected(true);
        }
    }
    
    /**
     * Seleciona nenhum.
     */
    public void selectNone() {
        List<String> keyList = getItemKeys();
        for (String key : keyList) {
            activeSessions.get(key).setSelected(false);
        }
    }
    
    /**
     * Invalida a sessão de todos os selecionados.
     */
    public void invalidateSelected() {
        List<String> keyList = getItemKeys();
        for (String key : keyList) {
            SessionRow sessionRow = activeSessions.get(key);
            if (sessionRow.isSelected()) {
                sessionRow.getSession().invalidate();
            }
        } 
    }
    
    /**
     * Invalida todas as sessões.
     */
    public void invalidateAll() {
        List<String> keyList = getItemKeys();
        for (String key : keyList) {
            SessionRow sessionRow = activeSessions.get(key);
            sessionRow.getSession().invalidate();
        }
    }
    
    /**
     * Pega a data corrente.
     * 
     * @return retorna a data corrente
     */
    public Date getCurrentDate() {
        return new Date();
    }
}
