package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;


/**
 * 
 * A classe PessoaGrupoCustoConverter proporciona a funcionalidade 
 * de converção do atributo indicadorStatus da entidade PessoaGrupoCusto.
 *
 * @since 17/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class PessoaGrupoCustoConverter implements Converter {

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
            String billable = BundleUtil
                    .getBundle(Constants.PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_FATURAVEL);
            String notBillable = BundleUtil
                    .getBundle(Constants.PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_NAO_FATURAVEL);
            String available = BundleUtil.
                    getBundle(Constants.PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_DISPONIVEL);

            if (billable.equals(strValue)) {
                result = Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL;
            } else if (notBillable.equals(strValue)) {
                result = Constants.PESSOA_GRUPO_CUSTO_STATUS_NAO_FATURAVEL;
            } else if (available.equals(strValue)) {
                result = Constants.PESSOA_GRUPO_CUSTO_STATUS_DISPONIVEL;
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

            if (Constants.PESSOA_GRUPO_CUSTO_STATUS_FATURAVEL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_FATURAVEL);
                
            } else if (Constants.PESSOA_GRUPO_CUSTO_STATUS_NAO_FATURAVEL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_NAO_FATURAVEL);
                
            } else if (Constants.PESSOA_GRUPO_CUSTO_STATUS_DISPONIVEL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.PESSOA_GRUPO_CUSTO_BUNDLE_STATUS_DISPONIVEL);
                
            } else if (Constants.ALL.equals(strValue)) {
               result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }     
        
        return result;
    }

}
