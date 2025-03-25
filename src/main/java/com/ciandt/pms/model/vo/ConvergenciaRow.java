/*
 * @(#) AlocacaoRow.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.control.jsf.ActiveProjectController;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Convergencia;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.IntegerConverter;

/**
 * Classe que representa a linha da matriz de Alocacao.
 * 
 */
public class ConvergenciaRow implements java.io.Serializable {

	/**
	 * texto para o o botao de acao
	 */
	private static final String ACTIVE_LABEL = "_nls.activate.label";

	/**
	 * texto para o o botao de acao
	 */
	private static final String VALIDATE_LABEL = "_nls.button.validate_next";

	/** Valor do serialVersionUID. */
	private static final long serialVersionUID = 5631585813087927354L;

	private Convergencia convergencia;
	private Boolean valid = false;
	private String nomeProjetoMega;
	private Long codigoProjetoMega;

	private ActiveProjectController activeProjectController;
	private Boolean showValidateButton = true;
	
	private NumberConverter numberConverter = new NumberConverter();

	/**
	 * Obtem o valor do atributo {@link ConvergenciaRow}.<BR>
	 * 
	 * @return the btnLabel
	 */
	public String getBtnLabel() {
		String label = VALIDATE_LABEL;
		if (valid) {
			label = ACTIVE_LABEL;
		}

		return BundleUtil.getBundle(label);
	}

	/**
	 * Obtem o valor do atributo {@link ConvergenciaRow#nomeProjetoMega}.<BR>
	 * 
	 * @return the nomeProjetoMega
	 */
	public String getNomeProjetoMega() {
		return nomeProjetoMega;
	}

	/**
	 * Atualiza o valor do atributo nomeProjetoMega.<BR>
	 * 
	 * @param nomeProjetoMega
	 *            Novo valor para o atributo
	 *            {@link ConvergenciaRow#nomeProjetoMega}.
	 */
	public void setNomeProjetoMega(String nomeProjetoMega) {
		this.nomeProjetoMega = nomeProjetoMega;
	}

	/**
	 * @param activeProjectController
	 * 
	 */
	public ConvergenciaRow(Convergencia convergencia,
			ActiveProjectController activeProjectController) {
		this.convergencia = convergencia;
		this.activeProjectController = activeProjectController;

		if (convergencia.getCodigoProjetoMega() != null && convergencia.getCodigoProjetoMega() != -999999) {
			this.setShowValidateButton(false);
		}
	}

	/**
	 * Obtem o valor do atributo {@link ConvergenciaRow#convergencia}.<BR>
	 * 
	 * @return the convergencia
	 */
	public Convergencia getConvergencia() {
		return convergencia;
	}

	/**
	 * Atualiza o valor do atributo convergencia.<BR>
	 * 
	 * @param convergencia
	 *            Novo valor para o atributo
	 *            {@link ConvergenciaRow#convergencia}.
	 */
	public void setConvergencia(Convergencia convergencia) {
		this.convergencia = convergencia;
	}

	/**
	 * Obtem o valor do atributo {@link ConvergenciaRow#valid}.<BR>
	 * 
	 * @return the valid
	 */
	public Boolean getValid() {
		return valid;
	}

	/**
	 * Atualiza o valor do atributo valid.<BR>
	 * 
	 * @param valid
	 *            Novo valor para o atributo {@link ConvergenciaRow#valid}.
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public void checkProject() {
		Long codigoProjetoMega = convergencia.getCodigoProjetoMega();

		if (codigoProjetoMega != null) {	
			String projectName = activeProjectController.getProjectName(codigoProjetoMega);

			if (projectName != null) {
				convergencia.setNomeProjetoMega(projectName);
				valid = true;
				return;
			}
		}
		invalidate();
	}

	private void invalidate() {
		valid = false;
		nomeProjetoMega = null;
	}

	class NumberConverter extends IntegerConverter {
		/*
		 * @see
		 * javax.faces.convert.IntegerConverter#getAsObject(javax.faces.context
		 * .FacesContext, javax.faces.component.UIComponent, java.lang.String)
		 */
		@Override
		public Object getAsObject(FacesContext context, UIComponent component,
				String value) {
			try {
				return super.getAsObject(context, component, value);
			} catch (ConverterException e) {
				invalidate();
				throw e;
			}
		}
	}
	
	/**
	 * Obtem o valor do atributo {@link ConvergenciaRow#numberConverter}.<BR>
	 *
	 * @return the numberConverter
	 */
	public NumberConverter getNumberConverter() {
		return numberConverter;
	}

	/**
	 * @return the showValidateButton
	 */
	public Boolean getShowValidateButton() {
		return showValidateButton;
	}

	/**
	 * @param showValidateButton the showValidateButton to set
	 */
	public void setShowValidateButton(Boolean showValidateButton) {
		this.showValidateButton = showValidateButton;
	}

	/**
	 * @return the codigoProjetoMega
	 */
	public Long getCodigoProjetoMega() {
		return codigoProjetoMega;
	}

	/**
	 * @param codigoProjetoMega the codigoProjetoMega to set
	 */
	public void setCodigoProjetoMega(Long codigoProjetoMega) {
		this.codigoProjetoMega = codigoProjetoMega;
	}
}