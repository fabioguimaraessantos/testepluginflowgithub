package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.ArrayList;
import java.util.List;

public class AllYesNoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        List<String> allYesNoList = new ArrayList<>();

        if (value != null) {

            String strValue = value.trim();
            String allValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            String yesValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_YES);
            String noValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_NO);

            if (allValue.equals(strValue)) {
                allYesNoList.add(Constants.YES);
                allYesNoList.add(Constants.NO);
                return allYesNoList;
            }
            if (yesValue.equals(strValue)) {
                allYesNoList.add(Constants.YES);
                return allYesNoList;
            }
            if (noValue.equals(strValue)) {
                allYesNoList.add(Constants.NO);
                return allYesNoList;
            }
        }

        allYesNoList.add(Constants.YES);
        allYesNoList.add(Constants.NO);
        return allYesNoList;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {

        if ((value instanceof String)) {

            String strValue = ((String) value).trim();
            String allValue = Constants.ALL;
            String yesValue = Constants.YES;
            String noValue = Constants.NO;

            if (allValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
            }
            if (yesValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_YES);
            }
            if (noValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_NO);
            }
        }

        return "";
    }
}
