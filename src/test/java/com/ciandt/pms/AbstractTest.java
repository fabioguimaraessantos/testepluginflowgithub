/**
 * @(#) AbstractTest.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms;

import org.springframework.test.jpa.AbstractJpaTests;

/**
 * A classe AbstractTest configura o contexto do Spring para os testes
 * unitarios.
 * 
 * @since 25/08/2009
 * @author <a href="mailto:marceloa@ciandt.com">Marcelo Ansante</a>
 * 
 */
public abstract class AbstractTest extends AbstractJpaTests {

    /**
     * Metodo que retorna os arquivos de configuracao do Spring.
     * 
     * @return array com os nomes dos arquivos.
     */
    protected String[] getConfigLocations() {
        return new String[] {"config-test.xml", "spring-utils.xml",
                "spring-control.xml", "spring-business.xml",
                "spring-persistence.xml"};
    }

}
