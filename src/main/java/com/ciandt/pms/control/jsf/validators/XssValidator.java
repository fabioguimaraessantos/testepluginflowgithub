package com.ciandt.pms.control.jsf.validators;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.errors.IntrusionException;
import org.owasp.esapi.errors.ValidationException;
import org.owasp.esapi.reference.validation.StringValidationRule;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.regex.Pattern;

@Component
public class XssValidator implements Validator, Serializable {
    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private void showErrorMessage() throws ValidatorException {
        String errorMessage = BundleUtil.getBundle(Constants.MSG_ERRO_INPUT_INVALID);

        FacesMessage facesMessage = new FacesMessage(errorMessage);
        facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);

        throw new ValidatorException(facesMessage);
    }

    /**
     * Valida o dado recebido e certifica de que não contem XSS
     *
     * @param context facesContext
     * @param component component
     * @param value value
     * @throws ValidatorException exception
     * */
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (!(value instanceof String)) {
            return;
        }

        String valueAsString = (String) value;
        String blackPatternName = "XSS";

        try {
            StringValidationRule svr = new StringValidationRule(blackPatternName, ESAPI.encoder());
            Pattern blackPattern = ESAPI.securityConfiguration().getValidationPattern(blackPatternName);

            svr.setAllowNull(false);
            svr.setCanonicalize(true);
            svr.addBlacklistPattern(blackPattern);

            svr.getValid("VALIDATION", valueAsString);
        } catch (IntrusionException e) {
            this.showErrorMessage();
        } catch (ValidationException e) {
            this.showErrorMessage();
        }
    }
}
