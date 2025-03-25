package com.ciandt.pms.control.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.model.vo.GenericFormFilter;

/**
 * Define o BackingBean da entidade Cost Center.
 * 
 * @since Aug 11, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CostCenterBean {

	/** Campo name da tela referente ao filtro de {@link VwMegaCCusto}.. */
	private String name;

	/** Instancia da entidade {@link VwMegaCCusto}. */
	private VwMegaCCusto to;

	/** Lista de Cost Center (VwMegaCCusto) pendentes. */
	private List<VwMegaCCusto> pendentes = new ArrayList<VwMegaCCusto>();

	/**
	 * @return the pendentes
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param pendentes
	 *            the pendentes to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the to
	 */
	public VwMegaCCusto getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(VwMegaCCusto to) {
		this.to = to;
	}

	/**
	 * @return the pendentes
	 */
	public List<VwMegaCCusto> getPendentes() {
		return pendentes;
	}

	/**
	 * @param pendentes
	 *            the pendentes to set
	 */
	public void setPendentes(List<VwMegaCCusto> pendentes) {
		this.pendentes = pendentes;
	}

	/**
	 * Reseta o bean.
	 */
	public void reset() {
		this.name = null;
		this.to = null;
		this.pendentes = new ArrayList<VwMegaCCusto>();
	}

	/**
	 * Obtem o filtro generico da tela de Ativacao de Cost Center.
	 * 
	 * @return {@link FormFilter}
	 */
	public FormFilter getPendentesFormFilter() {
		FormFilter filter = new GenericFormFilter();
		filter.addParam("name", this.name);
		return filter;
	}

}