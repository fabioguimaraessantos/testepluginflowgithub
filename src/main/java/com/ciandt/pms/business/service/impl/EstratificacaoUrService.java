/**
 * Classe EstratificacaoUrService - Implementação do serviço do EstratificacaoUR
 */
package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IEstratificacaoUrService;
import com.ciandt.pms.business.service.IItemEstratificacaoUrService;
import com.ciandt.pms.model.EstratificacaoUr;
import com.ciandt.pms.model.ItemEstratificacaoUr;
import com.ciandt.pms.persistence.dao.IEstratificacaoUrDao;


/**
 * A classe EstratificacaoUrService proporciona as funcionalidades de serviço
 * para a entidade EstratificacaoUr.
 * 
 * @since 14/04/2011
 * @author cmantovani
 * 
 */
@Service
public class EstratificacaoUrService implements IEstratificacaoUrService {

    /** Instancia do servico de itemEstratificacaoUrService. */
    @Autowired
    private IItemEstratificacaoUrService itemEstratificacaoUrService;

    /** Instancia do DAO da entidade. */
    @Autowired
    private IEstratificacaoUrDao estratificacaoUrDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createEstratificacaoUr(final EstratificacaoUr entity) {
        estratificacaoUrDao.create(entity);

    }

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            entidade a ser atualizada.
     */
    public void updateEstratificacaoUr(final EstratificacaoUr entity) {
        estratificacaoUrDao.update(entity);

    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            entidade a ser removida.
     */
    public void removeEstratificacaoUr(final EstratificacaoUr entity) {
        this.deleteAllItemEstratificacaoUr(entity);
        estratificacaoUrDao.remove(entity);

    }

    /**
     * Busca pelo id da receita.
     * 
     * @param idReceita
     *            id da entidade
     * 
     * @return retorna a entidade
     */
    public List<EstratificacaoUr> findByIdReceita(final Long idReceita) {
        return estratificacaoUrDao.findByIdReceita(idReceita);
    }

    /**
     * Busca pelo id do MapaAlocacao.
     * 
     * @param idMapaAlocacao
     *            id da entidade
     * @param data
     *            data da estratificação
     * 
     * @return retorna a entidade
     */
    public EstratificacaoUr findEstratificacaoUrByIdMapaAlocacaoData(
            final Long idMapaAlocacao, final Date data) {
        return estratificacaoUrDao.findByIdMapaAlocacaoData(idMapaAlocacao,
                data);
    }

    /**
     * Deleta todos os ItemEstratificacaoUr.
     * 
     * @param estratificacaoUr
     *            - EstratificacaoUr
     */
    private void deleteAllItemEstratificacaoUr(
            final EstratificacaoUr estratificacaoUr) {
        List<ItemEstratificacaoUr> itensEstratificacaoUr =
                estratificacaoUr.getItemEstratificacaoUrs();

        for (ItemEstratificacaoUr item : itensEstratificacaoUr) {
            itemEstratificacaoUrService.removeItemEstratificacaoUr(item);
        }
    }
}