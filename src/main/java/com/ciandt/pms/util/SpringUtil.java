package com.ciandt.pms.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.context.FacesContext;

/**
 * 
 * A classe SpringUtil proporciona as funcionalidades utlitarias
 * referente ao Spring.
 *
 * @since 21/09/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public final class SpringUtil {

    /**
     * Construtor privado.
     */
    private SpringUtil() { }
    
    /**
     * Retorna uma intancia de um bean, referente a 
     * string passada por parametro.
     * 
     * @param beanName - nome do bean
     * 
     * @return retorna uma instancia do bean
     */
    public static Object getBean(final String beanName) {
        // pega o contexto da aplicação (Spring)
        ApplicationContext applicationContext = FacesContextUtils
                .getWebApplicationContext(FacesContext.getCurrentInstance());
        // pega o bean do serviço do mapa de alocação.
        return applicationContext.getBean(beanName);
    }
}
