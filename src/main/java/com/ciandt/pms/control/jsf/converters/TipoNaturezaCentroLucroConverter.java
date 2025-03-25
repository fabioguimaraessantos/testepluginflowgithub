/**
 * @(#) TipoNaturezaCentroLucroConverter.java
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
 * A classe TipoNaturezaCentroLucroConverter é um conversor para os valores
 * Obrigatorio e Facultativo.
 * 
 * @since 04/05/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class TipoNaturezaCentroLucroConverter implements Converter {

    /**
     * Converte o valor do bundle Obrigatorio, Facultativo nos valores "O" ou
     * "F".
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
            String mandatoryValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_NATUREZA_TYPE_MANDATORY);
            String optionalValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_NATUREZA_TYPE_OPTIONAL);

            if (mandatoryValue.equals(strValue)) {
                result = Constants.NATUREZA_TYPE_MANDATORY;
            } else if (optionalValue.equals(strValue)) {
                result = Constants.NATUREZA_TYPE_OPTIONAL;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "O" Obrigatorio ou "F" Facultativo no bundle.
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

            if (Constants.NATUREZA_TYPE_MANDATORY.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_NATUREZA_TYPE_MANDATORY);
            } else if (Constants.NATUREZA_TYPE_OPTIONAL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_NATUREZA_TYPE_OPTIONAL);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}