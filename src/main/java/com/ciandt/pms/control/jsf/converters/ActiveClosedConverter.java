package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

public class ActiveClosedConverter implements Converter {

	/**
	 * Converte o valor do bundle ativo ou inativo nos valores "C" (closed) ou
	 * "A" (ativo).
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 * 
	 * @return retorna o valor do componente convertido em object.
	 */
	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {

		String result = null;

		if (value != null) {

			String strValue = value.trim();
			String activeValue = "Active";
			String inactiveValue = "Closed";

			if (activeValue.equals(strValue)) {
				result = "A";
			} else if (inactiveValue.equals(strValue)) {
				result = "C";
			} else {
				// Valor para todos ("All")
				result = null;
			}
		}

		return result;
	}

	/**
	 * Converte o valor "I" (inativo) ou o valor "C" (closed) no bundle conforme
	 * o idioma.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 * 
	 * @return retorna o valor do componente convertido em String.
	 */
	@Override
	public String getAsString(final FacesContext context,
			final UIComponent component, final Object value) {

		String result = BundleUtil
				.getBundle(Constants.BUNDLE_KEY_ACTIVE_INACTIVE);

		if ((value != null) && (value instanceof String)) {
			String strValue = ((String) value).trim();

			if ("A".equals(strValue)) {
				result = "Active";
			} else if ("C".equals(strValue)) {
				result = "Closed";
			} else if ("I".equals(strValue)) {
				result = "Inactive";
			} 
		}
		return result;
	}
}
