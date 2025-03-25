package com.ciandt.pms.model.vo;

import java.util.HashMap;
import java.util.Map;

/**
 * Entidade de filtro generica.
 * 
 * @since Aug 12, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
public class GenericFormFilter implements FormFilter {

	/** Map dos parametros do filtro. */
	private Map<String, Object> params = new HashMap<String, Object>();

	/**
	 * Adiciona muitos parametros de filtro de uma vez.
	 * 
	 * @param params
	 */
	public void addParams(final Map<String, Object> params) {
		this.params.putAll(params);
	}

	/**
	 * Adiciona um parametro no GenericFormFilter.
	 * 
	 * @param key
	 * @param value
	 */
	public void addParam(final String key, final Object value) {
		params.put(key, value);
	}

	/**
	 * Converte os atributos filtraveis em mapa.
	 */
	@Override
	public Map<String, Object> toMap() {
		return this.params;
	}

}