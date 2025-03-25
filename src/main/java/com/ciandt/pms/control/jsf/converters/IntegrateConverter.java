package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.sun.tools.jxc.apt.Const;


/**
 * A classe IntegrateConverter é um conversor para os valores
 * integrate, non-integrate e error (status da integração).
 * 
 * @since 04/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class IntegrateConverter implements Converter {

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
            String integrated = BundleUtil
                    .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_INTEGRADO);
            String noneIntegrated = BundleUtil
                    .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_NAO_INTEGRADO);
            String error = BundleUtil.
                    getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_ERROR);
            String pending = BundleUtil.
                    getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_PENDENTE);

            String inQueue = BundleUtil.getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_NA_FILA);

            String oracle = BundleUtil.getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_ORACLE);

            String licenseSwWaitingApproval = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_AGUARDANDO_APROVACAO);

            String licenseSwApproved = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_APROVADO);

            String licenseSwPending = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_PENDENTE);

            String licenseSwIntegrated = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_INTEGRADO);

            String licenseSwError = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_ERRO);

            if (integrated.equals(strValue)) {
                result = Constants.INTEGRACAO_STATUS_INTEGRADO;
            } else if (noneIntegrated.equals(strValue)) {
                result = Constants.INTEGRACAO_STATUS_NAO_INTEGRADO;
            } else if (error.equals(strValue)) {
                result = Constants.INTEGRACAO_STATUS_ERROR;
            } else if (pending.equals(strValue)) {
                result = Constants.INTEGRACAO_STATUS_PENDENTE;
            } else if(inQueue.equals(Constants.INTEGRACAO_STATUS_NA_FILA)) {
                result = Constants.INTEGRACAO_STATUS_NA_FILA;
            } else if (oracle.equals(strValue)) {
                result = Constants.INTEGRACAO_STATUS_ORACLE;
            } else if (licenseSwPending.equals(strValue)) {
                result = Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE;
            } else if (licenseSwIntegrated.equals(strValue)) {
                result = Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO;
            } else if (licenseSwError.equals(strValue)) {
                result = Constants.LICENSE_SW_INTEGRACAO_STATUS_ERRO;
            } else if (licenseSwWaitingApproval.equals(strValue)) {
                result = Constants.LICENSE_SW_INTEGRACAO_STATUS_AGUARDANDO_APROVACAO;
            } else if (licenseSwApproved.equals(strValue)) {
                result = Constants.LICENSE_SW_INTEGRACAO_STATUS_APROVADO;
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

            if (Constants.INTEGRACAO_STATUS_INTEGRADO.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_INTEGRADO);
                
            } else if (Constants.INTEGRACAO_STATUS_NAO_INTEGRADO.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_NAO_INTEGRADO);

            } else if (Constants.INTEGRACAO_STATUS_PENDENTE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_PENDENTE);
                
            } else if (Constants.INTEGRACAO_STATUS_ERROR.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_ERROR);

            }  else  if (Constants.INTEGRACAO_STATUS_NA_FILA.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_NA_FILA);

            } else if (Constants.INTEGRACAO_STATUS_ORACLE.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.INTEGRACAO_BUNDLE_STATUS_ORACLE);

            } else if (Constants.ALL.equals(strValue)) {
               result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            } else if (Constants.LICENSE_SW_INTEGRACAO_STATUS_PENDENTE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_PENDENTE);
            } else if (Constants.LICENSE_SW_INTEGRACAO_STATUS_INTEGRADO.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_INTEGRADO);
            } else if (Constants.LICENSE_SW_INTEGRACAO_STATUS_ERRO.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_ERRO);
            } else if (Constants.LICENSE_SW_INTEGRACAO_STATUS_AGUARDANDO_APROVACAO.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_AGUARDANDO_APROVACAO);
            } else if (Constants.LICENSE_SW_INTEGRACAO_STATUS_APROVADO.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.LICENSE_SW_INTEGRACAO_BUNDLE_STATUS_APROVADO);
            }
        }
        return result;
    }

}
