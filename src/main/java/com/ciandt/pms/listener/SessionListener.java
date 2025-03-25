/**
 * @(#) SessionListener.java
 * Copyright (c) 2009 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.listener;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.control.jsf.bean.SessionBean;
import com.ciandt.pms.model.MapaAlocacao;


/**
 * 
 * A classe SessionListener proporciona as funcionalidades de
 * listener de sessão da aplicação no moemento de criação de remoção.
 *
 * @since 12/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class SessionListener implements HttpSessionListener {
    
    /**
     * Método executado no momento da criação de uma sessão.
     * 
     * @param event
     *          evento que que acionou o metodo.
     */
    public void sessionCreated(final HttpSessionEvent event) {
        //adiciona a sessão no bean
        addSession(event.getSession());
    }

    /**
     * Método executado no momento da remoção de uma sessão. Este metodo é
     * responséval por remover o lock do mapa de alocação do usuário que teve sua
     * sessão destruida.
     * 
     * @param event
     *          evento que que acionou o metodo.
     */
    public void sessionDestroyed(final HttpSessionEvent event) {
        
        //remove a sessão do bean
        removeSession(event.getSession());  
        //remove todos os locks do mapa de alocação do usuario
        unlockAllAlocationMap(event);

    }
    
    /**
     * Remove todos os lock dos mapas de alocação do usuário logado no sistema.
     * 
     * @param event - entidade do tipo HttpSessionEvent.
     */
    private void unlockAllAlocationMap(final HttpSessionEvent event) {
        
        IMapaAlocacaoService mapaAlocacaoService = (IMapaAlocacaoService) 
            getBean(event.getSession(), "mapaAlocacaoService");
        
        String username = (String) event.getSession().getAttribute(
            "SPRING_SECURITY_LAST_USERNAME");
        
        //remove o lock de todos os mapas do usuário que a sessao esta sendo destruida
        List<MapaAlocacao> mapaAlocacaoList = mapaAlocacaoService.findByLoginTrava(username);
        for (MapaAlocacao mapaAlocacao : mapaAlocacaoList) {
            mapaAlocacao.setLoginTrava(null);
            mapaAlocacao.setDataTrava(null);
            mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);
        }
    }
    
    /**
     * Adiciona a sessão ao bean SessionBean.
     * 
     * @param session - HttpSession 
     */
    private void addSession(final HttpSession session) {
        
        SessionBean sessionBean = getSessionBean(session);
        
        sessionBean.addSession(session);
    }
    
    /**
     *  Remove a sessão do bean SessionBean.
     *  
     * @param session - HttpSession
     */
    private void removeSession(final HttpSession session) {
        
        SessionBean sessionBean = getSessionBean(session);
        
        sessionBean.removeSession(session);
    }
    
    /**
     * Pega o bean que armazena as sessões da aplicação. 
     * 
     * @param session - HttpSession
     * @return retorna o bean do tipo SessionBean.
     */
    private SessionBean getSessionBean(final HttpSession session) {
        
        return (SessionBean) getBean(session, "sessionBean");
    }
    
    /**
     * Retorna uma bean referente ao parametro passado.
     * 
     * @param session - sessão do tipo HttpSession.
     * @param beanName - nome do bean
     * 
     * @return retorna uma instancia do bean.
     */
    private Object getBean(final HttpSession session, final String beanName) {
        
        //pega o contexto da aplicação (Spring)
        ApplicationContext applicationContext = WebApplicationContextUtils
              .getWebApplicationContext(session.getServletContext());
        // pega o bean do serviço do mapa de alocação.
        return applicationContext.getBean(beanName);
    }

}
