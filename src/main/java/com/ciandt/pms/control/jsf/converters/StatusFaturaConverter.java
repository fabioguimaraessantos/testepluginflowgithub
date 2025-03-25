/**
 * @(#) StatusFaturaConverter.java
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
 * A classe StatusFaturaConverter é um conversor para os valores open, approved,
 * submitted, integrated, error, canceled (status da Fatura).
 * 
 * @since 04/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class StatusFaturaConverter implements Converter {

    /**
     * Converte o valor do bundle Open, Approved, Submitted, Integrated, Error, Canceled, Processing
     * nos valores "OP", "AP", "SB", "IN", "ER", "CA", "PR".
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
            String openValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_OPEN);
            String approvedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_APPROVED);
            String submittedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_SUBMITTED);
            String integratedValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_INTEGRATED);
            String integrationErrorValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_INTEGRATION_ERROR);
            String canceledValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_CANCELED);
            String pendingValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_PENDING);
            String processingValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_PROCESSING);

            if (openValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_OPEN;
            } else if (approvedValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_APPROVED;
            } else if (submittedValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_SUBMITTED;
            } else if (integratedValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_INTEGRATED;
            } else if (integrationErrorValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_INTEGRATION_ERROR;
            } else if (canceledValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_CANCELED;
            } else if (pendingValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_PENDING;
            } else if (processingValue.equals(strValue)) {
                result = Constants.FATURA_STATUS_PROCESSING;
            }
            else {
                // Valor para todos ("All")
                result = null;
            }
        }

        return result;
    }

    /**
     * Converte o valor "OP" (Open), "AP" (Approved), "SB" (Submitted), "IN"
     * (Integrated), "ER" (Error), "CA" (Canceled) no bundle.
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

            if (Constants.FATURA_STATUS_OPEN.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_OPEN);
            } else if (Constants.FATURA_STATUS_APPROVED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_APPROVED);
            } else if (Constants.FATURA_STATUS_SUBMITTED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_SUBMITTED);
            } else if (Constants.FATURA_STATUS_INTEGRATED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_INTEGRATED);
            } else if (Constants.FATURA_STATUS_INTEGRATION_ERROR
                    .equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_INTEGRATION_ERROR);
            } else if (Constants.FATURA_STATUS_CANCELED.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_CANCELED);
            } else if (Constants.FATURA_STATUS_PENDING.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_PENDING);
            } else if (Constants.FATURA_STATUS_PROCESSING.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_FATURA_STATUS_PROCESSING);
            }
            else if (Constants.ALL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
        }
        return result;
    }

}
