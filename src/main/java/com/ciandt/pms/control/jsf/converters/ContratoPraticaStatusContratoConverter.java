package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ContratoPraticaStatusContratoConverter implements Converter {


    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {

        if (value != null) {

            String strValue = value.trim();
            String activeValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            String inactiveValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
            String contractRenewalValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_CONTRACT_RENEWAL);
            String expandValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_EXPAND);
            String requestInactivationOldValue = BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION);

            if (activeValue.equals(strValue)) {
                return Constants.ACTIVE;
            }
            if (inactiveValue.equals(strValue)) {
                return Constants.INACTIVE;
            }
            if (contractRenewalValue.equals(strValue)) {
                return Constants.PROSPECT;
            }
            if (expandValue.equals(strValue)) {
                return Constants.EXPAND;
            }

            if (requestInactivationOldValue.equals(strValue)) {
                return Constants.REQUEST_INACTIVATION_OLD;
            }
        }

        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {

        if ((value instanceof String)) {

            String strValue = ((String) value).trim();
            String activeValue = Constants.ACTIVE;
            String inactiveValue = Constants.INACTIVE;
            String contractRenewalValue = Constants.PROSPECT;
            String expandValue = Constants.EXPAND;
            String requestInactivationOldValue = Constants.REQUEST_INACTIVATION_OLD;

            if (activeValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_ACTIVE);
            }
            if (inactiveValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_INACTIVE);
            }
            if (contractRenewalValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_CONTRACT_RENEWAL);
            }
            if (expandValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_EXPAND);
            }
            if (requestInactivationOldValue.equals(strValue)) {
                return BundleUtil.getBundle(Constants.BUNDLE_KEY_REQUEST_INACTIVATION);
            }
        }

        return "";
    }
}
