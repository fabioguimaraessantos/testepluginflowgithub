package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IFichaReajusteStatusService;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;
import com.ciandt.pms.persistence.dao.IFichaReajusteStatusDao;

/**
 * A classe FichaReajusteStatusService proporciona as funcionalidades de serviço referente
 * a entidade FichaReajusteStatus.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Service
public class FichaReajusteStatusService implements IFichaReajusteStatusService {

	/** Instancia do DAO da entidade. */
	@Autowired
	private IFichaReajusteStatusDao dao;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public FichaReajusteStatus findFichaReajusteStatusById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<FichaReajusteStatus> findFichaReajusteStatusAll() {
		return dao.findAll();
	}

    /**
     * Busca todos os status de {@link FichaReajuste} ativos.
     *
     * @return Lista de {@link FichaReajusteStatus}
     */
    public List<FichaReajusteStatus> findAllFichaReajusteStatusActive() {
    	return dao.findAllActive();
    }
    
    /**
     * Busca status de {@link FichaReajuste} com sigla igual a {@code siglaStatus}.
     *
     * @param siglaStatus
     * @return Lista de {@link FichaReajusteStatus}
     */
    public FichaReajusteStatus findFichaReajusteStatusBySiglaStatus(String siglaStatus) {
    	return dao.findBySiglaStatus(siglaStatus);
    }
}