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
 * listener de sess�o da aplica��o no moemento de cria��o de remo��o.
 *
 * @since 12/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class SessionListener implements HttpSessionListener {
    
    /**
     * M�todo executado no momento da cria��o de uma sess�o.
     * 
     * @param event
     *          evento que que acionou o metodo.
     */
    public void sessionCreated(final HttpSessionEvent event) {
        //adiciona a sess�o no bean
        addSession(event.getSession());
    }

    /**
     * M�todo executado no momento da remo��o de uma sess�o. Este metodo �
     * respons�val por remover o lock do mapa de aloca��o do usu�rio que teve sua
     * sess�o destruida.
     * 
     * @param event
     *          evento que que acionou o metodo.
     */
    public void sessionDestroyed(final HttpSessionEvent event) {
        
        //remove a sess�o do bean
        removeSession(event.getSession());  
        //remove todos os locks do mapa de aloca��o do usuario
        unlockAllAlocationMap(event);

    }
    
    /**
     * Remove todos os lock dos mapas de aloca��o do usu�rio logado no sistema.
     * 
     * @param event - entidade do tipo HttpSessionEvent.
     */
    private void unlockAllAlocationMap(final HttpSessionEvent event) {
        
        IMapaAlocacaoService mapaAlocacaoService = (IMapaAlocacaoService) 
            getBean(event.getSession(), "mapaAlocacaoService");
        
        String username = (String) event.getSession().getAttribute(
            "SPRING_SECURITY_LAST_USERNAME");
        
        //remove o lock de todos os mapas do usu�rio que a sessao esta sendo destruida
        List<MapaAlocacao> mapaAlocacaoList = mapaAlocacaoService.findByLoginTrava(username);
        for (MapaAlocacao mapaAlocacao : mapaAlocacaoList) {
            mapaAlocacao.setLoginTrava(null);
            mapaAlocacao.setDataTrava(null);
            mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);
        }
    }
    
    /**
     * Adiciona a sess�o ao bean SessionBean.
     * 
     * @param session - HttpSession 
     */
    private void addSession(final HttpSession session) {
        
        SessionBean sessionBean = getSessionBean(session);
        
        sessionBean.addSession(session);
    }
    
    /**
     *  Remove a sess�o do bean SessionBean.
     *  
     * @param session - HttpSession
     */
    private void removeSession(final HttpSession session) {
        
        SessionBean sessionBean = getSessionBean(session);
        
        sessionBean.removeSession(session);
    }
    
    /**
     * Pega o bean que armazena as sess�es da aplica��o. 
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
     * @param session - sess�o do tipo HttpSession.
     * @param beanName - nome do bean
     * 
     * @return retorna uma instancia do bean.
     */
    private Object getBean(final HttpSession session, final String beanName) {
        
        //pega o contexto da aplica��o (Spring)
        ApplicationContext applicationContext = WebApplicationContextUtils
              .getWebApplicationContext(session.getServletContext());
        // pega o bean do servi�o do mapa de aloca��o.
        return applicationContext.getBean(beanName);
    }

}
