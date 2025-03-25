/**
 * @(#) TipoFaturamentoConverter.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;


/**
 * A classe TipoFaturamentoConverter é um conversor para os valores TM e
 * FP conforme o idioma selecionado.
 * 
 * @since 16/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class TipoFaturamentoConverter implements Converter {

    /**
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
            String timeMaterailValue = 
                BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_FATURAMENTO_TM);
            String fixedPriceValue = 
                BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_FATURAMENTO_FP);

            if (timeMaterailValue.equals(strValue)) {
                result = Constants.TIPO_FATURAMENTO_TM;
            } else if (fixedPriceValue.equals(strValue)) {
                result = Constants.TIPO_FATURAMENTO_FP;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
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

            if (Constants.TIPO_FATURAMENTO_TM.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_FATURAMENTO_TM);
            } else if (Constants.TIPO_FATURAMENTO_FP.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_FATURAMENTO_FP);
            } else if (Constants.TIPO_FATURAMENTO_ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_FATURAMENTO_ALL);
            }
        }
        return result;
    }

}
