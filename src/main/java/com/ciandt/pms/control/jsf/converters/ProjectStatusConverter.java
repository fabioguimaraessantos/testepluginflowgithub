package com.ciandt.pms.control.jsf.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;

public class ProjectStatusConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1,
			final String value) {

		String result = null;
		if (value != null) {
			String strValue = value.trim();
			String associate = BundleUtil
					.getBundle(Constants.ACTIVATION_PROJECT_ASSOCIATE);
			String notAssociate = BundleUtil
					.getBundle(Constants.ACTIVATION_PROJECT_NOT_ASSOCIATE);

			if (associate.equals(strValue)) {
				result = Constants.ACTIVATION_ASSOCIATE_SG;
			} else if (notAssociate.equals(strValue)) {
				result = Constants.ACTIVATION_NOT_ASSOCIATE_SG;
			} else {
				// Valor para todos ("All")
				result = null;
			}
		}

		return result;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1,
			final Object value) {
		String result = "";

		if ((value != null) && (value instanceof String)) {
			String strValue = ((String) value).trim();

			if (Constants.ACTIVATION_ASSOCIATE_SG.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.ACTIVATION_PROJECT_ASSOCIATE);
			} else if (Constants.ACTIVATION_NOT_ASSOCIATE_SG.equals(strValue)) {
				result = BundleUtil
						.getBundle(Constants.ACTIVATION_PROJECT_NOT_ASSOCIATE);
			} else if (Constants.ACTIVATION_ALL.equals(strValue)) {
				result = BundleUtil.getBundle(Constants.BUNDLE_KEY_STAGE_ALL);
			}
		}
		return result;
	}

}
