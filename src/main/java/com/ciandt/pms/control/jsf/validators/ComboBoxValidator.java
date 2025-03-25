package com.ciandt.pms.control.jsf.validators;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.Messages;
import org.richfaces.component.html.HtmlComboBox;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;
import java.util.Map;


/**
 * 
 * A classe ComboBoxValidator proporciona as funcionalidades de valida��o de
 * valores de combo box de acordo com um Map de valores.
 * 
 * <h4>Utiliza��o</h4>
 * <p>
 * Inserir a tag internamente ao componente a ser validado. Ex:
 * </p>
 * 
 * <pre>
 *          xmlns:pms="http://pms.cit.com.br"
 *          .
 *          .
 *          .       
 *          <h:inputText ... >
 *              <pms:validateComboBox map="#{map}" />              
 *          </h:inputText>
 * </pre>
 * 
 * @since 14/04/2010
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
public class ComboBoxValidator implements Validator, Serializable {

    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributo map.
     */
	private Map<String, Object> map;

    /**
     * Construtor default.
     */
    public ComboBoxValidator() {
    }

    /**
     * Verifica se o valor 'value' � valido. Se o valor n�o � nulo e existe no
     * map, ent�o o valor � valido.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     * 
     * @throws ValidatorException - excecao na validacao
     */
    public void validate(final FacesContext context,
            final UIComponent component, final Object value)
            throws ValidatorException {
        Long cod = null;
        String strValue = (String) value;

        HtmlComboBox htmlComboBox = (HtmlComboBox) component;
        if(!htmlComboBox.getSubmittedValue().equals(strValue)) {
                strValue = htmlComboBox.getSubmittedValue().toString();
        }

        if ((strValue != null) && (!"".equals(strValue))) {

			Object v = map.get(strValue);
			if (v == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }

    }

    /**
     * @return the keysMap
     */
	public Map<String, Object> getMap() {
        return map;
    }

    /**
     * @param map
     *            the map to set
     */
	public void setMap(final Map<String, Object> map) {
        this.map = map;
    }

}
