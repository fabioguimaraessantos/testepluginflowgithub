/**
 * Classe EstratificacaoUrService - Implementação do serviço do ItemEstratificacaoUR
 */
package com.ciandt.pms.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IItemEstratificacaoUrService;
import com.ciandt.pms.model.ItemEstratificacaoUr;
import com.ciandt.pms.persistence.dao.IItemEstratificacaoUrDao;


/**
 * A classe ItemEstratificacaoUrService proporciona as funcionalidades de
 * serviço para a entidade ItemEstratificacaoUr.
 * 
 * @since 14/04/2011
 * @author cmantovani
 * 
 */
@Service
public class ItemEstratificacaoUrService implements
        IItemEstratificacaoUrService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IItemEstratificacaoUrDao itemEstratificacaoUrDao;

    /**
     * Cria a entidade.
     * 
     * @param entity
     *            entidade a ser criada.
     */
    @Transactional
    public void createItemEstratificacaoUr(final ItemEstratificacaoUr entity) {
        itemEstratificacaoUrDao.create(entity);
    }

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    @Transactional
    public void updateItemEstratificacaoUr(final ItemEstratificacaoUr entity) {
        itemEstratificacaoUrDao.update(entity);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeItemEstratificacaoUr(final ItemEstratificacaoUr entity) {
        itemEstratificacaoUrDao.remove(entity);
    }

    /**
     * Busca a entidade pelo Id.
     * 
     * @param id
     *            id da entidade a ser buscada.
     * @return entidade buscada
     */
    public ItemEstratificacaoUr findItemEstratificacaoUrById(final Long id) {
        return itemEstratificacaoUrDao.findById(id);
    }

}