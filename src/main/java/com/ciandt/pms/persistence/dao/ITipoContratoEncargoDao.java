package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.TipoContratoEncargo;


/**
 * 
 * A classe ITipoContratoEncargoDao proporciona a interface de acesso a camada
 * de persistencia referente a entidade TipoContratoEncargo.
 * 
 * @since 01/06/2011
 * @author cmantovani
 * 
 */
public interface ITipoContratoEncargoDao extends
        IAbstractDao<Long, TipoContratoEncargo> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoContratoEncargo> findAll();

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param tipoContratoEncargo
     *            - entidade do tipo TipoContratoEncargo.
     * 
     * @return retorna o proximo TipoContratoEncargo.
     */
    TipoContratoEncargo findNext(final TipoContratoEncargo tipoContratoEncargo);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param tipoContratoEncargo
     *            - entidade do tipo TipoContratoEncargo.
     * 
     * @return retorna o TipoContratoEncargo anterior.
     */
    TipoContratoEncargo findPrevious(
            final TipoContratoEncargo tipoContratoEncargo);

    /**
     * Busca pelo tipoContratoEncargo referente a data de inicio.
     * 
     * @param tipoContratoEncargo
     *            - entidade do tipo TipoContratoEncargo.
     * 
     * @return retorna o TipoContratoEncargo.
     */
    TipoContratoEncargo findByStartDate(
            final TipoContratoEncargo tipoContratoEncargo);

}