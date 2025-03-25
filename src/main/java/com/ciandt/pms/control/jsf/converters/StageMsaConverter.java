/**
 * @(#) VersionContratoConverter.java
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
 * A classe StatusContratoConverter é um conversor para os valores prospectando
 * e validado (status do MSA).
 * 
 * @since 30/09/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
public class StageMsaConverter implements Converter {

    /**
     * Converte o valor do bundle Prospected, Validated, Active ou Inactive nos
     * valores "PR" ou "VD" ou "A" ou "I".
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     * 
     * @return retorna o valor do componente convertido em object.
     * 
     */
    @Override
    public Object getAsObject(final FacesContext context,
            final UIComponent component, final String value) {

        String result = null;

        if (value != null) {

            String strValue = value.trim();
            String prospectedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_STAGE_PROSPECTED);
            String validatedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_STAGE_EXISTING);
            String activeValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_ACTIVE);
            String inactiveValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_INACTIVE);

            if (prospectedValue.equals(strValue)) {
                result = Constants.STAGE_PROSPECTED;
            } else if (validatedValue.equals(strValue)) {
                result = Constants.VERSION_VALIDATE;
            } else if (activeValue.equals(strValue)) {
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
     * Converte o valor "PR" (Prospected) ou o valor "VD" (Validated) ou o valor
     * "A" (Active) ou o valor "I" (Inactive) no bundle.
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

            if (Constants.STAGE_PROSPECTED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_STAGE_PROSPECTED);
            } else if (Constants.VERSION_VALIDATE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_STAGE_EXISTING);
            } else if (Constants.ACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            } else if (Constants.INACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
            } else if (Constants.STATUS_MSA_ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_ALL);
            }
        }
        return result;
    }
}
