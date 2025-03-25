package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.Pessoa;

/**
 * Define o BackingBean da entidade.
 * 
 * @since 29/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DreProcessosFechamentoByLoginBean extends Thread implements
		Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** to do backingBean. */
	private DreProcesso to = new DreProcesso();

	/** representa o mes selecionado. */
	private String monthBeg;

	/** representa o ano selecionado. */
	private String yearBeg;

	/** representa o codigo do login */
	private String codigoLogin;

	/** Indicador / numero da tela a ser exibida. */
	private Integer screenNumber;

	/** Lista de resultados da entidade Pessoa. */
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	/** Objeto pessoa a ser removido da lista */
	private Pessoa pessoa = new Pessoa();

	/**
	 * @return the to
	 */
	public DreProcesso getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(DreProcesso to) {
		this.to = to;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		resetValidityDate();
		this.screenNumber = Integer.valueOf(1);
		this.pessoas = new ArrayList<Pessoa>();
		this.pessoa = null;
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new DreProcesso();
	}

	/**
	 * Reseta a data do periodo da vigencia.
	 */
	public void resetValidityDate() {
		this.monthBeg = null;
		this.yearBeg = null;
	}

	/**
	 * @param monthBeg
	 *            the monthBeg to set
	 */
	public void setMonthBeg(final String monthBeg) {
		this.monthBeg = monthBeg;
	}

	/**
	 * @return the monthBeg
	 */
	public String getMonthBeg() {
		return monthBeg;
	}

	/**
	 * @param yearBeg
	 *            the yearBeg to set
	 */
	public void setYearBeg(final String yearBeg) {
		this.yearBeg = yearBeg;
	}

	/**
	 * @return the yearBeg
	 */
	public String getYearBeg() {
		return yearBeg;
	}

	/**
	 * @return the codigoLogin
	 */
	public String getCodigoLogin() {
		return codigoLogin;
	}

	/**
	 * @param codigoLogin
	 *            the codigoLogin to set
	 */
	public void setCodigoLogin(String codigoLogin) {
		this.codigoLogin = codigoLogin;
	}

	/**
	 * @return the screenNumber
	 */
	public Integer getScreenNumber() {
		return screenNumber;
	}

	/**
	 * @param screenNumber
	 *            the screenNumber to set
	 */
	public void setScreenNumber(Integer screenNumber) {
		this.screenNumber = screenNumber;
	}

	/**
	 * @return the pessoas
	 */
	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	/**
	 * @param pessoas
	 *            the pessoas to set
	 */
	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa the pessoa to set
	 */
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}