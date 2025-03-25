/**
 * @(#) TipoClienteConverter.java
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
 * sA classe TipoClienteConverter é um conversor para os valores nacional e
 * internacional.
 * 
 * @since 19/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class TipoClienteConverter implements Converter {

    /**
     * Converte o valor do bundle nacional, internacional nos valores "NAC" ou
     * "INT".
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
            String nationalValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_CLIENTE_TYPE_NATIONAL);
            String internationalValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_CLIENTE_TYPE_INTERNATIONAL);

            if (nationalValue.equals(strValue)) {
                result = Constants.CLIENTE_TYPE_NATIONAL;
            } else if (internationalValue.equals(strValue)) {
                result = Constants.CLIENTE_TYPE_INTERNATIONAL;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "NAC" nacional ou "INT" internacional no bundle.
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

            if (Constants.CLIENTE_TYPE_NATIONAL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_CLIENTE_TYPE_NATIONAL);
            } else if (Constants.CLIENTE_TYPE_INTERNATIONAL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_CLIENTE_TYPE_INTERNATIONAL);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}
