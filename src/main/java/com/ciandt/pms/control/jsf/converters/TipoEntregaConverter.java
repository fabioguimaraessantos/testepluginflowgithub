package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

public class TipoEntregaConverter implements Converter {

    @Override
    public Object getAsObject(final FacesContext context,
                              final UIComponent component, final String value) {

        String result = null;

        if (value != null) {

            String strValue = value.trim();
            String nacionalValue =
                    BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_ENTREGA_NACIONAL);
            String internationalValue =
                    BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_ENTREGA_INTERNACIONAL);

            if (nacionalValue.equals(strValue)) {
                result = Constants.TIPO_ENTREGA_NACIONAL;
            } else if (internationalValue.equals(strValue)) {
                result = Constants.TIPO_ENTREGA_INTERNACIONAL;
            } else {
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

            if (Constants.TIPO_ENTREGA_NACIONAL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_ENTREGA_NACIONAL);
            } else if (Constants.TIPO_ENTREGA_INTERNACIONAL.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_TIPO_ENTREGA_INTERNACIONAL);
            }
        }
        return result;
    }

}
