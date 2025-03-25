package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.OrcDespDelegacao;

/**
 * Define o backBean da tela de delegation.
 * 
 * @author peter
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaDelegationBean implements Serializable {

	/** Default serial version UID. */
	private static final long serialVersionUID = 1L;

	/** To da entidade. */
	private OrcDespDelegacao to = new OrcDespDelegacao();

	/** Lista de delegados. */
	private List<OrcDespDelegacao> listOrcDespDelegacao = new ArrayList<OrcDespDelegacao>();

	/**
	 * @return the to
	 */
	public OrcDespDelegacao getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(OrcDespDelegacao to) {
		this.to = to;
	}

	/**
	 * @return the listOrcDespDelegacao
	 */
	public List<OrcDespDelegacao> getListOrcDespDelegacao() {
		return listOrcDespDelegacao;
	}

	/**
	 * @param listOrcDespDelegacao
	 *            the listOrcDespDelegacao to set
	 */
	public void setListOrcDespDelegacao(
			List<OrcDespDelegacao> listOrcDespDelegacao) {
		this.listOrcDespDelegacao = listOrcDespDelegacao;
	}

	/**
	 * Reset do bean.
	 */
	public void resetBean() {
		this.listOrcDespDelegacao = new ArrayList<OrcDespDelegacao>();
		this.to = new OrcDespDelegacao();
	}

}
