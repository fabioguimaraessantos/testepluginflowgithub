package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITipoAjusteService;
import com.ciandt.pms.model.TipoAjuste;
import com.ciandt.pms.persistence.dao.ITipoAjusteDao;


/**
 * 
 * A classe TipoAjusteService proporciona as funcionalidades da camada de
 * negócio referente a entidade TipoAjuste.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
@Service
public class TipoAjusteService implements ITipoAjusteService {

    /** Instancia do Dao da entidade TipoAjuste. */
    @Autowired
    private ITipoAjusteDao tipoAjusteDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createTipoAjuste(final TipoAjuste entity) {
        tipoAjusteDao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * 
     */
    public void updateTipoAjuste(final TipoAjuste entity) {
        tipoAjusteDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     */
    public void removeTipoAjuste(final TipoAjuste entity) {
        tipoAjusteDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TipoAjuste findTipoAjusteById(final Long id) {
        return tipoAjusteDao.findById(id);
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoAjuste> findTipoAjusteAllActive() {
        return tipoAjusteDao.findAllActive();
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoAjuste> findTipoAjusteAll() {
        return tipoAjusteDao.findAll();
    }

}
