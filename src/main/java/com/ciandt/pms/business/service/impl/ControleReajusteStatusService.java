package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IControleReajusteStatusService;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteStatus;
import com.ciandt.pms.persistence.dao.IControleReajusteStatusDao;

/**
 * A classe ClassNameService proporciona as funcionalidades de serviço referente
 * a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class ControleReajusteStatusService implements IControleReajusteStatusService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IControleReajusteStatusDao dao;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	@Override
	public ControleReajusteStatus findControleReajusteStatusById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	public List<ControleReajusteStatus> findControleReajusteStatusAll() {
		return dao.findAll();
	}

    /**
     * Retorna todos os status de {@link ControleReajuste} ativos.
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
	@Override
    public List<ControleReajusteStatus> findControleReajusteAllActive() {
    	return dao.findAllActive();
    }
    
    /**
     * Retorna o status de {@link ControleReajuste} em que possui a sigla igual a .
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
	@Override
    public ControleReajusteStatus findControleReajusteStatusBySiglaControleReajusteStatus(String siglaStatus) {
    	return dao.findBySiglaControleReajusteStatus(siglaStatus);
    }
}