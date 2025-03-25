package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.TipoContratoEncargo;


/**
 * 
 * A classe ITipoContratoEncargoService proporciona a interface de acesso
 * referente a entidade TipoContratoEncargo.
 * 
 * @since 01/06/2011
 * @author cmantovani
 * 
 */
@Service
public interface ITipoContratoEncargoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            a ser inserida
     * @return Boolean
     */
    @Transactional
    Boolean createTipoContratoEncargo(final TipoContratoEncargo entity);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoContratoEncargo> findTipoContratoEncargoAll();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TipoContratoEncargo findTipoContratoEncargoById(final Long id);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    @Transactional
    void updateTipoContratoEncargo(final TipoContratoEncargo entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * @return Boolean
     * 
     */
    @Transactional
    Boolean removeTipoContratoEncargo(final TipoContratoEncargo entity);
}
