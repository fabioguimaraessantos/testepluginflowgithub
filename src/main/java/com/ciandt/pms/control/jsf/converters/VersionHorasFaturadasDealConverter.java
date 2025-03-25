/**
 * @(#) VersionHorasFaturadasDealConverter.java
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
 * A classe VersionHorasFaturadasDealConverter é um conversor para os valores
 * working, publish conforme o idioma selecionado.
 * 
 * @since 17/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class VersionHorasFaturadasDealConverter implements Converter {

    /**
     * Converte o valor do bundle working ou publish nos valores "WK" ou "PB".
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
            String workingValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_HORAS_FAT_DEAL_VERSION_WORKING);
            String publishedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_HORAS_FAT_DEAL_VERSION_PUBLISHED);
            String unavailableValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_VERSION_UNAVAILABLE);

            if (workingValue.equals(strValue)) {
                result = Constants.VERSION_HORAS_FAT_DEAL_WORKING;
            } else if (publishedValue.equals(strValue)) {
                result = Constants.VERSION_HORAS_FAT_DEAL_PUBLISHED;
            } else if (unavailableValue.equals(strValue)) {
                result = Constants.VERSION_UNAVAILABLE;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "WK" (Working) ou o valor "PB" (Publish) no bundle
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

            if (Constants.VERSION_HORAS_FAT_DEAL_WORKING.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_HORAS_FAT_DEAL_VERSION_WORKING);
            } else if (Constants.VERSION_HORAS_FAT_DEAL_PUBLISHED
                    .equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_HORAS_FAT_DEAL_VERSION_PUBLISHED);
            } else if (Constants.VERSION_UNAVAILABLE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_VERSION_UNAVAILABLE);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}
