/**
 * @(#) ActiveInactiveConverter.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;


/**
 * A classe NullValueConverter converte valores nulos para 'N/A'.
 * 
 * @since 10/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class NullValueConverter implements Converter {

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

        String result = value;

        if (Constants.NOT_AVAILABLE_VALUE.equals(result)) {
            result = null;
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

        String result = (String) value;

        if (value == null || "".equals(value)) {
            result = Constants.NOT_AVAILABLE_VALUE;
        }

        return result;
    }

}
