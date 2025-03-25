package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

public class TipoFaturaTBConverter implements Converter {

	@Override
	public Object getAsObject(final FacesContext context,
							  final UIComponent component, final String value) {

		return value;
	}

	/**
	 * Converte os valores RB e NM para RB e NR.
	 */
	@Override
	public String getAsString(final FacesContext context,
		final UIComponent component, final Object value) {

		String result = "";

		if ((value != null) && (value instanceof String)) {

			String strValue = ((String) value).trim();
			String reimbursable = Constants.ORCAMENTO_DESPESA_REEMBOLSAVEL;
			String not_reimbursable = Constants.ORCAMENTO_DESPESA_NAO_REEMBOLSAVEL;

			if (strValue.equals(reimbursable)) {
				result = reimbursable;
			} else {
				result = not_reimbursable;
			}
		}

		return result;
	}
}
