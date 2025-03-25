package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IFichaReajusteIndiceService;
import com.ciandt.pms.model.FichaReajusteIndice;
import com.ciandt.pms.persistence.dao.IFichaReajusteIndiceDao;

/**
 * A classe ClassNameService proporciona as funcionalidades de serviço referente
 * a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class FichaReajusteIndiceService implements IFichaReajusteIndiceService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IFichaReajusteIndiceDao dao;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public FichaReajusteIndice findFichaReajusteIndiceById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<FichaReajusteIndice> findFichaReajusteIndiceAll() {
		return dao.findAll();
	}

	/**
	 * Retorna o indice com nome igual a {@code nomeIndice}
	 *
	 * @param nome
	 * @return {@link FichaReajusteIndice}
	 */
	public FichaReajusteIndice findFichaReajusteIndiceByNome(String nomeIndice) {
		return dao.findByNome(nomeIndice);
	}
}