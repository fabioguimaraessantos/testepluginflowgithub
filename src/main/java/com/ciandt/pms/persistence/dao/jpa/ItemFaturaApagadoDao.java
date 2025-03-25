package com.ciandt.pms.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.ItemFaturaApagado;
import com.ciandt.pms.persistence.dao.IItemFaturaApagadoDao;

/**
 * 
 * A classe ItemFaturaDao proporciona as funcionalidades de acesso ao banco para
 * referentes a entidade ItemFatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class ItemFaturaApagadoDao extends AbstractDao<Long, ItemFaturaApagado> implements
		IItemFaturaApagadoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ItemFaturaApagadoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ItemFaturaApagado.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ItemFaturaApagado> findByFaturaApagada(
			FaturaApagada faturaApagada) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("faturaApagadaId", faturaApagada.getCodigoFatura());

		List<ItemFaturaApagado> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ItemFaturaApagado.FIND_BY_FATURA_APAGADA, params);

		if (results.isEmpty()) {
			return null;
		}

		return results;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemFaturaApagado> findByCodigoFaturaApagada(
			Long codigoFaturaApagada) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("faturaApagadaId", codigoFaturaApagada);
		
		List<ItemFaturaApagado> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ItemFaturaApagado.FIND_BY_FATURA_APAGADA, params);
		
		if (results.isEmpty()) {
			return new ArrayList<ItemFaturaApagado>();
		}
		
		return results;
	}

}