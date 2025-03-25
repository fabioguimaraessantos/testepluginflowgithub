/**
 * @(#) TipoCotacaoMoedaConverter.java
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
 * A classe TipoCotacaoMoedaConverter é um conversor para os valores real,
 * previsto (expected).
 * 
 * @since 16/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class TipoCotacaoMoedaConverter implements Converter {

    /**
     * Converte o valor do bundle real, previsto (expected) nos valores "R" ou
     * "P".
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
            String realValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_COTACAO_MOEDA_TIPO_REAL);
            String expectedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_COTACAO_MOEDA_TIPO_PREVISTO);

            if (realValue.equals(strValue)) {
                result = Constants.COTACAO_MOEDA_TIPO_REAL;
            } else if (expectedValue.equals(strValue)) {
                result = Constants.COTACAO_MOEDA_TIPO_PREVISTO;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "R" (real) ou "P" (expected / previsto) no bundle.
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

            if (Constants.COTACAO_MOEDA_TIPO_REAL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_COTACAO_MOEDA_TIPO_REAL);
            } else if (Constants.COTACAO_MOEDA_TIPO_PREVISTO.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_COTACAO_MOEDA_TIPO_PREVISTO);
            } else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}
