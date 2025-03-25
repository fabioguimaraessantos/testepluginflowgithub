package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 29/04/2013
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AreaBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

}