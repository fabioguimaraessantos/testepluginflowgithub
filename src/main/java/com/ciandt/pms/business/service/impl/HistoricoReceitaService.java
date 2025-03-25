/**
 * Classe HistoricoReceitaService - Implementação do serviço da HistoricoReceita
 */
package com.ciandt.pms.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IHistoricoReceitaService;
import com.ciandt.pms.model.HistoricoReceita;
import com.ciandt.pms.persistence.dao.IHistoricoReceitaDao;


/**
 * A classe HistoricoReceitaService proporciona as funcionalidades de serviço
 * para a entidade HistoricoReceita.
 * 
 * @since 26/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class HistoricoReceitaService implements IHistoricoReceitaService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IHistoricoReceitaDao hiReDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createHistoricoReceita(final HistoricoReceita entity) {
        hiReDao.create(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeHistoricoReceita(final HistoricoReceita entity) {
        hiReDao.remove(entity);
    }

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public HistoricoReceita findHistoricoReceitaById(final Long entityId) {
        return hiReDao.findById(entityId);
    }

}