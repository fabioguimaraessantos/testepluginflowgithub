package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;


/**
 * A classe StatusComissaoConverter é um conversor para os
 * status da comissão.
 * 
 * @since 30/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class StatusComissaoConverter implements Converter {

    /**
     * Converte o valor do bundle Prospected, Validated, Active ou Inactive nos
     * valores "PR" ou "VD" ou "A" ou "I".
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     * 
     * @return retorna o valor do componente convertido em object.
     * 
     */
    @Override
    public Object getAsObject(final FacesContext context,
            final UIComponent component, final String value) {

        String result = null;

        if (value != null) {

            String strValue = value.trim();
            
            String approvedValue = BundleUtil.getBundle(
                    Constants.COMISSAO_BUNDLE_STATUS_APPROVED);
            String comissioedValue = BundleUtil.getBundle(
                    Constants.COMISSAO_BUNDLE_STATUS_COMISSIONED);
            String forwardedValue = BundleUtil.getBundle(
                    Constants.COMISSAO_BUNDLE_STATUS_FORWARDED);
            String openValue = BundleUtil.getBundle(
                    Constants.COMISSAO_BUNDLE_STATUS_OPEN);
            String requestValue = BundleUtil.getBundle(
                    Constants.COMISSAO_BUNDLE_STATUS_REQUEST);
            String reviewValue = BundleUtil.getBundle(
                    Constants.COMISSAO_BUNDLE_STATUS_REVIEW);
            
            if (approvedValue.equals(strValue)) {
                result = Constants.COMISSAO_STATUS_APPROVED;
            } else if (comissioedValue.equals(strValue)) {
                result = Constants.COMISSAO_STATUS_COMISSIONED;
            } else if (forwardedValue.equals(strValue)) {
                result = Constants.COMISSAO_STATUS_FORWARDED;
            } else if (openValue.equals(strValue)) {
                result = Constants.COMISSAO_STATUS_OPEN;
            } else if (requestValue.equals(strValue)) {
                result = Constants.COMISSAO_STATUS_REQUEST;
            } else if (reviewValue.equals(strValue)) {
                result = Constants.COMISSAO_STATUS_REVIEW;  
            } else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "PR" (Prospected) ou o valor "VD" (Validated) ou o valor
     * "A" (Active) ou o valor "I" (Inactive) no bundle.
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

            if (Constants.COMISSAO_STATUS_APPROVED.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.COMISSAO_BUNDLE_STATUS_APPROVED);
                
            } else if (Constants.COMISSAO_STATUS_COMISSIONED.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.COMISSAO_BUNDLE_STATUS_COMISSIONED);
                
            } else if (Constants.COMISSAO_STATUS_FORWARDED.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.COMISSAO_BUNDLE_STATUS_FORWARDED);
                
            } else if (Constants.COMISSAO_STATUS_OPEN.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.COMISSAO_BUNDLE_STATUS_OPEN);
                
            } else if (Constants.COMISSAO_STATUS_REQUEST.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.COMISSAO_BUNDLE_STATUS_REQUEST);
            
            } else if (Constants.COMISSAO_STATUS_REVIEW.equals(strValue)) {
                result = BundleUtil.getBundle(
                        Constants.COMISSAO_BUNDLE_STATUS_REVIEW);
            }
        }
        return result;
    }
    
    /**
     * Retorna o status da comissão como uma string.
     * 
     * @param value - valor do status
     * 
     * @return retorna uma string que representa o status
     */
    public String getAsString(final Object value) {
        return getAsString(null, null, value);
    }
}
