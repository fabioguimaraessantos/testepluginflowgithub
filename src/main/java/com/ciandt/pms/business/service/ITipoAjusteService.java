package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.TipoAjuste;


/**
 * 
 * A classe ITipoAjusteService proporciona a interface de acesso a camada de
 * serviço referente a entidade TipoAjusteService.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
@Service
public interface ITipoAjusteService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createTipoAjuste(final TipoAjuste entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que sera atualizada.
     * 
     */
    @Transactional
    void updateTipoAjuste(final TipoAjuste entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que sera apagada.
     */
    @Transactional
    void removeTipoAjuste(final TipoAjuste entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TipoAjuste findTipoAjusteById(final Long id);

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoAjuste> findTipoAjusteAllActive();

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoAjuste> findTipoAjusteAll();

}
