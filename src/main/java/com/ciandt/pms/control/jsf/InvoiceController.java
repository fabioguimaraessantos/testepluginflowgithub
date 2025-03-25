package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Invoice;
import java.util.logging.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class InvoiceController {

	private final Logger log = Logger.getLogger(InvoiceController.class.getName());

	private static final String OUTCOME_COST_CENTER_EDIT = "costCenter_edit";

	public Invoice activate() {
		Invoice invoice = null;
		try {
		} catch (ConstraintViolationException e) {
			log.warning(e.getMessage());
			Messages.showWarning("activate",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		} catch (Exception e) {
			log.warning(e.getMessage());
			Messages.showWarning("activate",
					Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
			return null;
		} 
		return invoice;
	}

	public String activateAndNext() {
		Invoice invoice = activate();
		if (invoice == null) {
			return null;
		}
		Messages.showSucess("activateAndNext",
				Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_GRUPO_CUSTO);
		return OUTCOME_COST_CENTER_EDIT;
	}
}