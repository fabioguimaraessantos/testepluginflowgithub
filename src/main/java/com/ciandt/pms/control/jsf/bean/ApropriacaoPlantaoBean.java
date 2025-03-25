package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ApropriacaoPlantao;
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
public class ApropriacaoPlantaoBean extends ApropriacaoBean implements
		Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** to do backingBean. */
	private ApropriacaoPlantao to = new ApropriacaoPlantao();

	/** lista de resultados da pesquisa. */
	private List<ApropriacaoPlantao> resultList;

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		super.reset();
		resetTo();
		resultList = new ArrayList<ApropriacaoPlantao>();
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new ApropriacaoPlantao();
		this.to.setMoeda(new Moeda());
	}

	/**
	 * @return the to
	 */
	public ApropriacaoPlantao getTo() {
		if (to == null) {
			to = new ApropriacaoPlantao();
		}
		if (to.getPessoa() == null) {
			to.setPessoa(new Pessoa());
		}

		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final ApropriacaoPlantao to) {
		this.to = to;
	}

	/**
	 * @return the resultList
	 */
	public List<ApropriacaoPlantao> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList
	 *            the resultList to set
	 */
	public void setResultList(List<ApropriacaoPlantao> resultList) {
		this.resultList = resultList;
	}

}