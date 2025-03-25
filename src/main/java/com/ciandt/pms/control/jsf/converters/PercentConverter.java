package com.ciandt.pms.control.jsf.converters;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * 
 * A classe PercentConverter proporciona as funcionalidades de conversão 
 * de um valor decimal em formato de porcentagem.
 *
 * @since 08/12/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class PercentConverter implements Converter {

    /**
     * Converte o valor decimal em formato de porcentagem.
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
    public Object getAsObject(final FacesContext context, 
            final UIComponent component, final String value) {
        
        Double percentValue = Double.valueOf(value.substring(-1)) / Double.valueOf("100.0");
        
        return new BigDecimal(percentValue);
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
    public String getAsString(final FacesContext context, 
            final UIComponent component, final Object value) {
        
        BigDecimal percentValue = (BigDecimal) value;
        String ret = "";
        
        if (percentValue != null) {
            ret = percentValue.multiply(
                    new BigDecimal("100.0")).setScale(
                            0, BigDecimal.ROUND_HALF_UP) + "%";
        }

        return ret;
    }

}
