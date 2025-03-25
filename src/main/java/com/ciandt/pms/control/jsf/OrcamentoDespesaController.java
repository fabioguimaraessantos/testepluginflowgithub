package com.ciandt.pms.control.jsf;

import javax.annotation.security.RolesAllowed;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

/**
 * 
 * A classe TravelBudgetController proporciona as funcionalidades de Controller
 * para a entidade TravelBudget.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Deprecated
@Controller
@RolesAllowed({ "ROLE_PMS_ADMIN" })
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcamentoDespesaController {
	// deprecated. Just for history
}