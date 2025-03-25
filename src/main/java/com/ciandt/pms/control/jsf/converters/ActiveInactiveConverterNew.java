/**
 * @(#) ActiveInactiveConverter.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ActiveInactiveConverterNew implements Converter {

    @Override
    public Object getAsObject(final FacesContext context,
                              final UIComponent component, final String value) {

        String result = null;

        if (value != null) {

            String strValue = value.trim();

            if (BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE).equals(strValue)) {
                result = Constants.NEW_ACTIVE;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE).equals(strValue)) {
                result = Constants.NEW_INACTIVE;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_CREATION).equals(strValue)) {
                result = Constants.REQUEST_CREATION;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_INTEGRATING_CREATION).equals(strValue)) {
                result = Constants.INTEGRATING_CREATION;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION).equals(strValue)) {
                result = Constants.REQUEST_INACTIVATION;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_INTEGRATING_INACTIVATION).equals(strValue)) {
                result = Constants.INTEGRATING_INACTIVATION;
            } else if (BundleUtil.getBundle(Constants.BUNDLE_KEY_CLOSED).equals(strValue)) {
                result = Constants.CLOSED;
            }
        }

        return result;
    }

    @Override
    public String getAsString(final FacesContext context,
                              final UIComponent component, final Object value) {

        String result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);

        if ((value instanceof String)) {

            String strValue = ((String) value).trim();

            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_INTEGRATING_INACTIVATION))) {
                strValue = Constants.INTEGRATING_INACTIVATION;
            }
            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE))) {
                strValue = Constants.NEW_ACTIVE;
            }
            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE))) {
                strValue = Constants.NEW_INACTIVE;
            }
            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_CLOSED))) {
                strValue = Constants.CLOSED;
            }
            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_CREATION))) {
                strValue = Constants.REQUEST_CREATION;
            }
            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION))) {
                strValue = Constants.REQUEST_INACTIVATION;
            }
            if (strValue.equals(BundleUtil.getBundle(Constants.BUNDLE_KEY_INTEGRATING_CREATION))) {
                strValue = Constants.INTEGRATING_CREATION;
            }

            if (Constants.NEW_ACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            } else if (Constants.NEW_INACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
            } else if (Constants.REQUEST_CREATION.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_CREATION);
            } else if (Constants.PROSPECT.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_PROSPECT);
            } else if (Constants.ACTIVE_INACTIVE.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE_INACTIVE);
            } else if (Constants.REQUEST_INACTIVATION.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION);
            } else if (Constants.INTEGRATING_CREATION.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INTEGRATING_CREATION);
            } else if (Constants.INTEGRATING_INACTIVATION.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_INTEGRATING_INACTIVATION);
            } else if (Constants.CLOSED.equals(strValue)) {
                result = BundleUtil.getBundle(Constants.BUNDLE_KEY_CLOSED);
            }

        }
        return result;
    }

}
