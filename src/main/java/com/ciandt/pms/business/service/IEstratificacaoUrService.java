/**
 * Classe EstratificacaoUrService - Implementação do serviço do EstratificacaoUR
 */
package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.EstratificacaoUr;


/**
 * A classe EstratificacaoUrService proporciona as funcionalidades de serviço
 * para a entidade EstratificacaoUr.
 * 
 * @since 14/04/2011
 * @author cmantovani
 * 
 */
@Service
public interface IEstratificacaoUrService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createEstratificacaoUr(final EstratificacaoUr entity);

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    @Transactional
    void updateEstratificacaoUr(final EstratificacaoUr entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeEstratificacaoUr(final EstratificacaoUr entity);

    /**
     * Busca pelo id da receita.
     * 
     * @param idReceita
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    List<EstratificacaoUr> findByIdReceita(final Long idReceita);

    /**
     * Busca pelo id do MapaAlocacao.
     * 
     * @param idMapaAlocacao
     *            - id da entidade
     * @param data
     *            - data da estratificação
     * 
     * @return retorna a entidade
     */
    EstratificacaoUr findEstratificacaoUrByIdMapaAlocacaoData(
            final Long idMapaAlocacao, final Date data);

}