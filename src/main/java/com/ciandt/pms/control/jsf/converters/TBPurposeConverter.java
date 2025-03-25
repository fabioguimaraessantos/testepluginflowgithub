package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class TBPurposeConverter implements Converter {

    @Override
    public Object getAsObject(final FacesContext context, final UIComponent component, final String value) {

        String result = null;

        if (value != null) {

            String strValue = value.trim();
            String generalValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_TB_PURPOSE_GENERAL);
            String benefitValue = BundleUtil
                    .getBundle(Constants.BUNDLE_KEY_TB_PURPOSE_BENEFIT);

            if (benefitValue.equals(strValue)) {
                result = Constants.TB_PURPOSE_BENEFIT;
            } else if (generalValue.equals(strValue)) {
                result = Constants.TB_PURPOSE_GENERAL;
            }
        }

        return result;
    }

    @Override
    public String getAsString(final FacesContext context, final UIComponent component, final Object value) {

        String result = "";

        if ((value != null) && (value instanceof String)) {
            String strValue = ((String) value).trim();

            if (Constants.TB_PURPOSE_BENEFIT.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_TB_PURPOSE_BENEFIT);
            } else if (Constants.TB_PURPOSE_GENERAL.equals(strValue)) {
                result = BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_TB_PURPOSE_GENERAL);
            }
        }

        return result;
    }

}