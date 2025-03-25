package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AlocacaoOverhead;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 19/07/2010
 */
@Repository
public interface IAlocacaoOverheadDao extends
        IAbstractDao<Long, AlocacaoOverhead> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<AlocacaoOverhead> findByFilter(final AlocacaoOverhead filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AlocacaoOverhead> findAll();

}