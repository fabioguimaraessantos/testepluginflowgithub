/**
 * @(#) ActiveInactiveConverter.java
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
 * A classe StatusActiveInactiveConverter � um conversor para os valores ativos e
 * inativo conforme o idioma selecionado.
 * 
 * @since 10/12/2015
 * @author <a href="mailto:marceloa@ciandt.com">Marcelo Ansante</a>
 * 
 */
public class StatusActiveInactiveConverter implements Converter {

    /**
     * Converte o valor do bundle ativo ou inativo nos valores "I" (inativo) ou
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
            String activeValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            String inactiveValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);

            if (activeValue.equals(strValue)) {
                result = Constants.ACTIVE;
            } else if (inactiveValue.equals(strValue)) {
                result = Constants.INACTIVE;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "I" (inativo) ou o valor "A" (ativo) no bundle conforme
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

        String result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);

        if ((value != null) && (value instanceof String)) {
            String strValue = ((String) value).trim();


            if (Constants.ACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            } else if (Constants.INACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
            }
        }
        return result;
    }

}
