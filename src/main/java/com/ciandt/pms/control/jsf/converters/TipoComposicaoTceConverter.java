/**
 * @(#) TipoComposicaoTceConverter.java
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
 * A classe TipoComposicaoTceConverter é um conversor para os valores
 * Manual e Sincronização.
 * 
 * @since 08/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class TipoComposicaoTceConverter implements Converter {

    /**
     * Converte o valor do bundle Manual e Synchronization nos valores "MN" ou
     * "SY".
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
            String manualValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_COMP_TCE_TYPE_MANUAL);
            String syncValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_COMP_TCE_TYPE_SYNC);

            if (manualValue.equals(strValue)) {
                result = Constants.TYPE_MANUAL;
            } else if (syncValue.equals(strValue)) {
                result = Constants.TYPE_SYNC;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "MN" Manual ou "SY" Synchronization no bundle.
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

            if (Constants.TYPE_MANUAL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_COMP_TCE_TYPE_MANUAL);
            } else if (Constants.TYPE_SYNC.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_COMP_TCE_TYPE_SYNC);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}