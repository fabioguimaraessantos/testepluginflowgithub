package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.TipoAjuste;


/**
 * 
 * A classe ITipoAjusteDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade TipoAjuste.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
public interface ITipoAjusteDao extends IAbstractDao<Long, TipoAjuste> {

    /**
     * Busca todos os TipoAjuste.
     * 
     * @return lista de TipoAjuste com todos os TipoAjuste
     */
    List<TipoAjuste> findAll();

    /**
     * Busca todos os TipoAjuste.
     * 
     * @return lista de TipoAjuste com todos os TipoAjuste ativos
     */
    List<TipoAjuste> findAllActive();
}
