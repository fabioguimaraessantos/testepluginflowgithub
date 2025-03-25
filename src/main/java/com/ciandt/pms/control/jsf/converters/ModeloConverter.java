package com.ciandt.pms.control.jsf.converters;

import com.ciandt.pms.business.service.IModeloService;
import com.ciandt.pms.model.Modelo;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class ModeloConverter implements Converter {

	private final IModeloService modeloService;

	public ModeloConverter() {
		ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		modeloService = ctx.getBean(IModeloService.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value == null || value.isEmpty()) {
			return null;
		}
		return modeloService.findByName(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value instanceof Modelo) {
			return ((Modelo) value).getNomeModelo();
		} else {
			return "";
		}
	}
}
