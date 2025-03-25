/**
 * Classe EstratificacaoUrService - Implementação do serviço do EstratificacaoUR
 */
package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ItemEstratificacaoUr;


/**
 * A classe ItemEstratificacaoUrService proporciona as funcionalidades de
 * serviço para a entidade ItemEstratificacaoUr.
 * 
 * @since 14/04/2011
 * @author cmantovani
 * 
 */
@Service
public interface IItemEstratificacaoUrService {

    /**
     * Cria a entidade.
     * 
     * @param entity
     *            entidade a ser criada.
     */
    @Transactional
    void createItemEstratificacaoUr(final ItemEstratificacaoUr entity);

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    @Transactional
    void updateItemEstratificacaoUr(final ItemEstratificacaoUr entity);

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    @Transactional
    void removeItemEstratificacaoUr(final ItemEstratificacaoUr entity);

    /**
     * Busca a entidade pelo Id.
     * 
     * @param id
     *            id da entidade a ser buscada.
     * @return entidade buscada
     */
    ItemEstratificacaoUr findItemEstratificacaoUrById(final Long id);

}