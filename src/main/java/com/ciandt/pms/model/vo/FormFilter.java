package com.ciandt.pms.model.vo;

import java.util.Map;

/**
 * A interface Filter tem como objetivo padronizar os objetos de filtro de tela
 * de busca utilizadas no sistema.
 * 
 * @since Aug 12, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 */
public interface FormFilter {

	/**
	 * Adiciona muitos parametros de filtro de uma vez.
	 * 
	 * @param params
	 */
	void addParams(final Map<String, Object> params);

	/**
	 * Adiciona um parametro no GenericFormFilter.
	 * 
	 * @param key
	 * @param value
	 */
	void addParam(final String key, final Object value);

	/**
	 * Converte os atributos filtraveis em mapa.
	 * 
	 * @return Map
	 */
	Map<String, Object> toMap();

}