/**
 * @(#) VersionMapaAlocacaoConverter.java
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
 * A classe VersionMapaAlocacaoConverter é um conversor para os valores draft,
 * publish e validate conforme o idioma selecionado.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class VersionMapaAlocacaoConverter implements Converter {

    /**
     * Converte o valor do bundle draft ou publish ou validate nos valores "DF"
     * ou "PB" ou "VD".
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
            String draftValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_VERSION_DRAFT);
            String publishValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_VERSION_PUBLISHED);
            String validateValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_VERSION_VALIDATED);

            if (draftValue.equals(strValue)) {
                result = Constants.VERSION_DRAFT;
            } else if (publishValue.equals(strValue)) {
                result = Constants.VERSION_PUBLISHED;
            } else if (validateValue.equals(strValue)) {
                result = Constants.VERSION_VALIDATE;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "DF" (Draft) ou o valor "PB" (Publish) ou "VD"
     * (Validate) no bundle conforme o idioma.
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

            if (Constants.VERSION_DRAFT.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_VERSION_DRAFT);
            } else if (Constants.VERSION_PUBLISHED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_VERSION_PUBLISHED);
            } else if (Constants.VERSION_VALIDATE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_VERSION_VALIDATED);
            } else if (Constants.VERSION_ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_VERSION_ALL);
            }
        }
        return result;
    }

}
