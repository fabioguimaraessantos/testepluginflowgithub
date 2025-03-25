package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

/**
 * 
 * @author peter
 * 
 */
public class TipoFaturamentoMsaTBConverter implements Converter {

	/**
	 * Converte o valor do bundle Debit Note, Receipt nos valores "ND" ou "NF" .
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 * 
	 * @return retorna o valor do componente convertido em object.
	 * 
	 */
	@Override
	public Object getAsObject(final FacesContext context,
			final UIComponent component, final String value) {

		String result = null;

		if (value != null) {

			String strValue = value.trim();
			String debitValue = BundleUtil
					.getBundle(Constants.TIPO_FATURAMENTO_MSA_TB_DEBITO);
			String receiptValue = BundleUtil
					.getBundle(Constants.TIPO_FATURAMENTO_MSA_TB_NOTA_FISCAL);

			if (debitValue.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.TIPO_FATURAMENTO_MSA_TB_DEBITO_VL);
			} else if (receiptValue.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.TIPO_FATURAMENTO_MSA_TB_NOTA_FISCAL_VL);
			}

		}

		return result;
	}

	/**
	 * Converte o valor "ND" (Debit Note) ou o valor "NF" (Fiscal Note) no
	 * bundle.
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

			if (BundleUtil.getBundle(
					Constants.TIPO_FATURAMENTO_MSA_TB_DEBITO_VL).equals(
					strValue)) {
				result = BundleUtil
						.getBundle(Constants.TIPO_FATURAMENTO_MSA_TB_DEBITO);
			} else if (BundleUtil.getBundle(
					Constants.TIPO_FATURAMENTO_MSA_TB_NOTA_FISCAL_VL).equals(
					strValue)) {
				result = BundleUtil
						.getBundle(Constants.TIPO_FATURAMENTO_MSA_TB_NOTA_FISCAL);
			}
		}
		return result;
	}

}
