package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;


/**
 * 
 * A classe AlocacaoOverheadConverter proporciona a funcionalidade 
 * de converção do atributo indicadorStatus da entidade AlocacaoOverhead.
 *
 * @since 19/07/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 *
 */
public class AlocacaoOverheadConverter implements Converter {

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
            String planned = BundleUtil
                    .getBundle(Constants.ALOCACAO_OH_BUNDLE_STATUS_PLANNED);
            String performed = BundleUtil
                    .getBundle(Constants.ALOCACAO_OH_BUNDLE_STATUS_PERFORMED);

            if (planned.equals(strValue)) {
                result = Constants.ALOCACAO_OH_STATUS_PLANNED;
            } else if (performed.equals(strValue)) {
                result = Constants.ALOCACAO_OH_STATUS_PERFORMED;
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

            if (Constants.ALOCACAO_OH_STATUS_PLANNED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.ALOCACAO_OH_BUNDLE_STATUS_PLANNED);
                
            } else if (Constants.ALOCACAO_OH_STATUS_PERFORMED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.ALOCACAO_OH_BUNDLE_STATUS_PERFORMED);
                
            } else if (Constants.ALL.equals(strValue)) {
               result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}