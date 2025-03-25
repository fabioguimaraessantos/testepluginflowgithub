package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;


/**
 * 
 * A classe ApropriacaoFaturaConverter proporciona as funcionalidades de 
 * conversão referente ao status da Apropriação de fatura.
 *
 * @since 20/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ApropriacaoFaturaConverter implements Converter {

    /**
     * Converte o valor do bundle para o real valor.
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
            
            String pending = BundleUtil
                    .getBundle(Constants.APROPRIACAO_FATURA_BUNDLE_STATUS_PENDENTE);
            String completed = BundleUtil
                    .getBundle(Constants.APROPRIACAO_FATURA_BUNDLE_STATUS_COMPLETO);

            if (pending.equals(strValue)) {
                result = Constants.APROPRIACAO_FATURA_STATUS_PENDENTE;
            } else if (completed.equals(strValue)) {
                result = Constants.APROPRIACAO_FATURA_STATUS_COMPLETO;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor real, para o valor do bundle.
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

            if (Constants.APROPRIACAO_FATURA_STATUS_PENDENTE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.APROPRIACAO_FATURA_BUNDLE_STATUS_PENDENTE);
                
            } else if (Constants.APROPRIACAO_FATURA_STATUS_COMPLETO.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.APROPRIACAO_FATURA_BUNDLE_STATUS_COMPLETO);
                
            } else if (Constants.ALL.equals(strValue)) {
               result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }
    
}
