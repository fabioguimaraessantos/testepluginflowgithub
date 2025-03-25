package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.HistoricoReceita;


/**
 * 
 * A classe IHistoricoReceitaService proporciona a interface de acesso para a
 * camada de serviço referente a entidade HistoricoReceita.
 * 
 * @since 26/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IHistoricoReceitaService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createHistoricoReceita(final HistoricoReceita entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeHistoricoReceita(final HistoricoReceita entity);

    /**
     * Busca pelo id da entidade.
     * 
     * @param entityId
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    HistoricoReceita findHistoricoReceitaById(final Long entityId);

}