package com.ciandt.pms.control.jsf.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.ciandt.pms.Constants;


/**
 * 
 * A classe BundelUtil proporciona as funcionalidades 
 * de acesso aos valores do arquivo de bundle.
 *
 * @since 18/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public final class BundleUtil {

    /**
     * Construtor privado.
     */
    private BundleUtil() { }

    /**
     * Método utlitário para recuperar valores do boundle.
     * 
     * @param bundleKey chave bundle.
     * @return valor do bundle.
     */
    public static String getBundle(final String bundleKey) {

        ResourceBundle bundle = ResourceBundle.getBundle(
                Constants.DEFAULT_RESOURCE_BUNDLE);

        return bundle.getString(bundleKey);
    }

    /**
     * Método utlitário para recuperar valores do bundle.
     * 
     * @param bundleKey chave bundle.
     * @param params parametros do bundle
     * 
     * @return valor do bundle.
     */
    public static String getBundle(final String bundleKey, final Object... params) {
        
        String text = getBundle(bundleKey);
        
        Object[] auxArrayParams = null;
        
        if (params != null) {
            auxArrayParams = new Object[params.length];
            
            for (int i = 0; i < params.length; i++) {
                Object objParam = params[i];
                try {
                    String param = (String) objParam;
                    if (param.startsWith(Constants.DEFAULT_BUNDLES_KEY_BEGIN)) {
                        auxArrayParams[i] =  BundleUtil.getBundle(param);
                    } else {
                        auxArrayParams[i] = param;
                    }
                } catch (ClassCastException e) {
                    auxArrayParams[i] = objParam;
                }
            }
            
            MessageFormat mf = new MessageFormat(text);
            text = mf.format(auxArrayParams, new StringBuffer(), null).toString();
        }
        
        return text;
    }
}