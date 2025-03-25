/**
 * @(#) StatusMapaAlocFotoConverter.java
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
 * A classe StatusMapaAlocFotoConverter é um conversor para os valores added, updated,
 * deleted (status do MapaAlocacaoFoto).
 * 
 * @since 11/03/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class StatusMapaAlocFotoConverter implements Converter {

    /**
     * Converte o valor do bundle Added, Updated, Submitted, Integrated, Error,
     * Canceled nos valores "OP", "AP", "SB", "IN", "ER", "CA".
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
            String addedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_STATUS_ADDED);
            String updatedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_STATUS_UPDATED);
            String deletedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_STATUS_DELETED);

            if (addedValue.equals(strValue)) {
                result = Constants.MAPA_FOTO_STATUS_ADDED;
            } else if (updatedValue.equals(strValue)) {
                result = Constants.MAPA_FOTO_STATUS_UPDATED;
            } else if (deletedValue.equals(strValue)) {
                result = Constants.MAPA_FOTO_STATUS_DELETED;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "A" (Added), "U" (Updated), "D" (Deleted) no bundle.
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

            if (Constants.MAPA_FOTO_STATUS_ADDED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_STATUS_ADDED);
            } else if (Constants.MAPA_FOTO_STATUS_UPDATED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_STATUS_UPDATED);
            } else if (Constants.MAPA_FOTO_STATUS_DELETED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_MAPA_FOTO_STATUS_DELETED);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }
    
    /**
     * Retorna o status do MapaAlocacaoFoto como uma string.
     * 
     * @param value - valor do status
     * 
     * @return retorna uma string que representa o status
     */
    public String getAsString(final Object value) {
        return getAsString(null, null, value);
    }

}