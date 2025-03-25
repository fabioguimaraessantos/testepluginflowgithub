package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;


/**
 * 
 * A classe TipoTiRecursoConverter é um conversor para os valores
 * 'Contract Server'-(CS) e 'Software User'-(SU).
 *
 * @since 15/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class TipoTiRecursoConverter implements Converter {

    /**
     * Converte o valor do bundle. 'Contract Server' para 'CS' e 'Software User' para 'SU'.
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
            String csType = BundleUtil
                    .getBundle(Constants.TI_RECURSO_BUNDLE_KEY_TIPO_CS);
            String suType = BundleUtil
                    .getBundle(Constants.TI_RECURSO_BUNDLE_KEY_TIPO_SU);
            String spType = BundleUtil
                    .getBundle(Constants.TI_RECURSO_BUNDLE_KEY_TIPO_SP);

            if (csType.equals(strValue)) {
                result = Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE;
            } else if (suType.equals(strValue)) {
                result = Constants.TI_RECURSO_TYPE_SOFTWARE_USER;
            } else if (spType.equals(strValue)) {
                result = Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT;
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor 'CS' e 'SU' no bundle.
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

            if (Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.TI_RECURSO_BUNDLE_KEY_TIPO_CS);
                
            } else if (Constants.TI_RECURSO_TYPE_SOFTWARE_USER.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.TI_RECURSO_BUNDLE_KEY_TIPO_SU);
                
            } else if (Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.TI_RECURSO_BUNDLE_KEY_TIPO_SP);

            }else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }
}
