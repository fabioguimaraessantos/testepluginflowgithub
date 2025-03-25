package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

/**
 * A Classe LogicDeleteConverter converter os valores de delete logico.
 * 
 * @since 26/11/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public class LogicDeleteConverter implements Converter {
	
	
	/**
     * Converte o valor do bundle Deleted, Not deleted nos
     * valores "S" ou "N".
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
			String deleted = BundleUtil
					.getBundle(Constants.BUNDLE_LOGICAL_DELETE);
			String notDeleted = BundleUtil
					.getBundle(Constants.BUNDLE_NOT_LOGICAL_DELETE);

			if (deleted.equals(strValue)) {
				result = Constants.SIM;
			} else if (notDeleted.equals(strValue)) {
				result = Constants.NO;
			} else {
				result = null;
			}

		}
		
		return result;

	}

	/**
     * Converte o valor "S" (SIM) ou o valor "N" (No) no bundle.
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

		String result = "";

		if ((value != null) && (value instanceof String)) {
			String strValue = ((String) value).trim();

			if (Constants.SIM.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.BUNDLE_LOGICAL_DELETE);
			} else if (Constants.NO.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.BUNDLE_NOT_LOGICAL_DELETE);
			} else {
				result = null;
			}

		}

		return result;

	}
}
