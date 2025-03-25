/**
 * @(#) StringConverter.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * A classe StringConverter é responsável por remover os espaços no inicio e fim
 * de uma String.
 * 
 * @since 10/08/2009
 * @author <a href="mailto:marceloa@ciandt.com">Marcelo Ansante</a>
 * 
 */
public class StringConverter implements Converter {
    
    /**
     * Remove os espaços do valor da String.
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
    public Object getAsObject(final FacesContext context, final UIComponent component,
            final String value) {
        
        return value.trim();
    }

    /**
     * Remove os espaços do valor do Objeto.
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
    public String getAsString(final FacesContext context, final UIComponent component,
            final Object value) {
        
        return ((String) value).trim();
    }
}
