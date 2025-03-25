package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ApropriacaoHoraExtra;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Pessoa;

/**
 * Define o BackingBean da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 22/10/2013
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ApropriacaoHoraExtraBean extends ApropriacaoBean implements
		Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** to do backingBean. */
	private ApropriacaoHoraExtra to = new ApropriacaoHoraExtra();

	/** lista de resultados da pesquisa. */
	private List<ApropriacaoHoraExtra> resultList;

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		super.reset();
		resetTo();
		resultList = new ArrayList<ApropriacaoHoraExtra>();
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new ApropriacaoHoraExtra();
		this.to.setMoeda(new Moeda());
	}

	/**
	 * @return the to
	 */
	public ApropriacaoHoraExtra getTo() {
		if (to == null) {
			to = new ApropriacaoHoraExtra();
		}
		if (to.getPessoa() == null) {
			to.setPessoa(new Pessoa());
		}

		return to;
	}

	/**
	 * @return the resultList
	 */
	public List<ApropriacaoHoraExtra> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(List<ApropriacaoHoraExtra> resultList) {
		this.resultList = resultList;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(ApropriacaoHoraExtra to) {
		this.to = to;
	}

}