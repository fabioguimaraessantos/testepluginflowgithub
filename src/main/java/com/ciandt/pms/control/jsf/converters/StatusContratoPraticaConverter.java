/**
 * @(#) StatusContratoPraticaConverter.java
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
 * A classe StatusContratoPraticaConverter é um conversor para os valores
 * complete e incomplete (status do ContratoPratica) conforme o idioma
 * selecionado.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class StatusContratoPraticaConverter implements Converter {

    /**
     * Converte o valor do bundle Complete ou Incomplete nos valores "C" ou "I".
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
            String completeValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_STATUS_COMPLETE);
            String incompleteValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_STATUS_INCOMPLETE);

            if (completeValue.equals(strValue)) {
                result = Constants.STATUS_COMPLETE;
            } else if (incompleteValue.equals(strValue)) {
                result = Constants.STATUS_INCOMPLETE;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "C" (Complete) ou o valor "I" (Incomplete) no bundle
     * conforme o idioma.
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

            if (Constants.STATUS_COMPLETE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_STATUS_COMPLETE);
            } else if (Constants.STATUS_INCOMPLETE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_STATUS_INCOMPLETE);
            } else if (Constants.STATUS_ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STATUS_ALL);
            }
        }
        return result;
    }

}
