/**
 * @(#) ActiveInactiveConverter.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 * A classe ActiveInactiveConverter � um conversor para os valores ativos e
 * inativo conforme o idioma selecionado.
 *
 * @since 10/08/2009
 * @author <a href="mailto:marceloa@ciandt.com">Marcelo Ansante</a>
 *
 */
public class ActiveInactiveConverter implements Converter {

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
            String prospectValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_PROSPECT);
            String requestInactivationValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION);

            /* TODO: AJEITAR O STATUS NEW_ACTIVE E NEW_INACTIVE PARA NÃO QUEBRAR OUTRAS TELAS QUE UTILIZAM DESSA CLASSE UTILITÁRIA */
            if (activeValue.equals(strValue)) {
                result = Constants.ACTIVE;
            } else if (inactiveValue.equals(strValue)) {
                result = Constants.INACTIVE;
            } else if (prospectValue.equals(strValue)) {
                result = Constants.PROSPECT;
            } else if (requestInactivationValue.equals(strValue)) {
                result = Constants.REQUEST_INACTIVATION_OLD;
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

        String result = null;

        if ((value != null) && (value instanceof String)) {
            String strValue = ((String) value).trim();

            if (Constants.ACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            } else if (Constants.INACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
            } else if (Constants.PROSPECT.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_PROSPECT);
            } else if (Constants.ACTIVE_INACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE_INACTIVE);
            } else if (Constants.REQUEST_INACTIVATION_OLD.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        } else if (value instanceof Boolean) {
            if (Boolean.TRUE.equals(value)) result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            else result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
        }
        return result;
    }

}
