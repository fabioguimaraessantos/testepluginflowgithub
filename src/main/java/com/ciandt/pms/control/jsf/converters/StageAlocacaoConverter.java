/**
 * @(#) StageAlocacaoConverter.java
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
 * A classe StageAlocacaoConverter é um conversor para 
 * os valores commited, prospected e compromise
 * conforme o idioma selecionado.
 * 
 * @since 10/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class StageAlocacaoConverter implements Converter {


    /**
     * Converte o valor do bundle commited, prospected e compromise
     * para os valores CM, PH, PL, RV.
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

            if (BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_COMMITED).equals(strValue)) {
                result = Constants.STAGE_COMMITED;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_PROSPECTED_LOW).equals(strValue)) {
                result = Constants.STAGE_PROSPECTED_LOW;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_PROSPECTED_HIGH).equals(strValue)) {
                result = Constants.STAGE_PROSPECTED_HIGH;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_RESERVED).equals(strValue)) {
                result = Constants.STAGE_RESERVED;
            }
        }

        return result;
    }

    /**
     * Converte o valor CM, PH, PL e RV no bundle conforme o idioma.
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

            if (Constants.STAGE_COMMITED.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_COMMITED);
            } else if (Constants.STAGE_RESERVED.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_RESERVED);
            } else if (Constants.STAGE_PROSPECTED_LOW.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_PROSPECTED_LOW);
            } else if (Constants.STAGE_PROSPECTED_HIGH.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_PROSPECTED_HIGH);
            }
        }
        return result;
    }

}
