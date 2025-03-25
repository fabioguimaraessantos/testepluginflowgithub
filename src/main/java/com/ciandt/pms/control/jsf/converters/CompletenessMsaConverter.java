package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

/**
 * A Classe CompletenessMsaConverter converter os valores de completude da
 * entidade MSA.
 * 
 * @since 26/11/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public class CompletenessMsaConverter implements Converter {

	/**
     * Converte o valor do bundle Complete, Incomplete nos
     * valores "Y" ou "N".
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
			String completo = BundleUtil
					.getBundle(Constants.BUNDLE_KEY_STATUS_MSA_COMPLETE);
			String incompleto = BundleUtil
					.getBundle(Constants.BUNDLE_KEY_STATUS_MSA_INCOMPLETE);

			if (completo.equals(strValue)) {
				result = Constants.YES;
			} else if (incompleto.equals(strValue)) {
				result = Constants.NO;
			} else {
				result = null;
			}

		}
		
		return result;

	}

	/**
     * Converte o valor "Y" (Yes) ou o valor "N" (No) no bundle.
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

			if (Constants.YES.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.BUNDLE_KEY_STATUS_MSA_COMPLETE);
			} else if (Constants.NO.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.BUNDLE_KEY_STATUS_MSA_INCOMPLETE);
			} else {
				result = null;
			}

		}

		return result;

	}

}
