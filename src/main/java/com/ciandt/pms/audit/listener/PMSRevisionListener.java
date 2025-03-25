/**
 * @(#) PMSRevisionListener.java
 * Copyright (c) 2009 Ci&T Software S/A.
 * All Rights Reserved.
 */

package com.ciandt.pms.audit.listener;

import com.ciandt.pms.model.PMSRevisionEntity;
import com.ciandt.pms.util.LoginUtil;
import org.hibernate.envers.RevisionListener;


/**
 * A classe PMSRevisionListener proporciona a funcionalidade de listener para
 * auditoria. Todas vez que as entidades com anotacao @Audited forem utilizadas
 * para manipulacao de dados, essa classe eh executada.
 * 
 * @since 22/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class PMSRevisionListener implements RevisionListener {

    private static final String SYSTEM_LOGIN = "system";
    
    /**
     * Metodo executado ao se detectar uma manipulacao em uma Entidade auditada.
     * Pega essa entidade em memoria e seta o codigo do login corrente para
     * gravar no log o autor da manipulacao.
     * 
     * @param revisionEntity
     *            - Entidade da revisao (REVINFO)
     */
    public void newRevision(final Object revisionEntity) {

        String loggedUsername = LoginUtil.getLoggedUsername();
		if (loggedUsername == null || loggedUsername.isEmpty()) {
            loggedUsername = SYSTEM_LOGIN;
        }
        PMSRevisionEntity revinfo = (PMSRevisionEntity) revisionEntity;
        revinfo.setCodigoAutor(loggedUsername);
    }
    
}