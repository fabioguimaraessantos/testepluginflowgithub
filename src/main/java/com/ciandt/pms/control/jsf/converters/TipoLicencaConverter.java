package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 * 
 * A classe TipoLicencaConverter é um conversor para os valores
 * 'Contract Server'-(CS) e 'Software User'-(SU).
 *
 * @since 15/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class TipoLicencaConverter implements Converter {


    @Override
    public Object getAsObject(final FacesContext context,
            final UIComponent component, final String value) {

        String result = null;

        if (value != null) {

            String strValue = value.trim();

            if ("Internal".equals(strValue)) {
                result = "INTERNAL";
            } else if ("Production".equals(strValue)) {
                result = "PRODUCTION";
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    @Override
    public String getAsString(final FacesContext context,
            final UIComponent component, final Object value) {

        String result = "";

        if ((value != null) && (value instanceof String)) {
            String strValue = ((String) value).trim();

            if ("INTERNAL".equals(strValue)) {
                result = "Internal";
                
            } else if ("PRODUCTION".equals(strValue)) {
                result = "Production";
            }
        }
        return result;
    }
}
