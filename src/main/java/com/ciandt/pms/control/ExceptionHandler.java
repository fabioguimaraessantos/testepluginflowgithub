/**
 * @(#) ExceptionHandler.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.control;

import java.io.PrintWriter;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.AccessDeniedException;


import com.ciandt.pms.Constants;
import com.sun.faces.application.ActionListenerImpl;

/**
 * Classe responsável por tratar erros inesperados.
 * 
 */
public class ExceptionHandler extends ActionListenerImpl {

    /** Instância do logger. */
    private static Logger logger = LogManager.getLogger(ExceptionHandler.class);

    /**
     * Processa a ação.
     * 
     * @param event - O ActionEvent
     * @throws AbortProcessingException - Exceção para abortar o processo
     */
    public void processAction(final ActionEvent event)
            throws AbortProcessingException {

        // Obtem o contexto JSF
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // Executa o método da classe Pai
            super.processAction(event);

        } catch (Throwable e) {

            // loga o erro;
            logger.error(e);

            HttpServletRequest req = (HttpServletRequest) context
                    .getExternalContext().getRequest();

            req.setAttribute("error_exception", e);
            req.setAttribute("error_message", e.getMessage());

            java.io.CharArrayWriter stackTrace = new java.io.CharArrayWriter();
            PrintWriter printStream = new PrintWriter(stackTrace);

            e.printStackTrace(printStream);
            req.setAttribute("error_stackTrace", (String) stackTrace.toString());

            // pega a causa raiz do erro
            Throwable rootCause = e;
            while (rootCause.getCause() != null) {
                rootCause = rootCause.getCause();
            }
            
            if (rootCause instanceof AccessDeniedException) {
                context.getApplication().getNavigationHandler().
                    handleNavigation(context, null, Constants.DEFAULT_ACCESS_DENIED_ERROR_OUTCOME);
            } else {
                // Redireciona para a pagina de erro.
                context.getApplication().getNavigationHandler().handleNavigation(
                        context, null, Constants.DEFAULT_ERROR_OUTCOME);
            }

        }
    }
}