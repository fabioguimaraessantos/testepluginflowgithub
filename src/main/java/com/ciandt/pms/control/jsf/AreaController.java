package com.ciandt.pms.control.jsf;

import javax.annotation.security.RolesAllowed;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;


/**
 * Define o Controller da entidade.
 * 
 * @since 29/04/2013
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila</a>
 * 
 */
@Controller
@RolesAllowed({ "ROLE_PMS_ADMIN" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AreaController {


}