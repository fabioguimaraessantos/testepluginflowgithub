package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.BasePapelAlocacao;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
public interface IBasePapelAlocacaoDao extends
        IAbstractDao<Long, BasePapelAlocacao> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<BasePapelAlocacao> findByFilter(final BasePapelAlocacao filter);

    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<BasePapelAlocacao> findByFilterFetch(final BasePapelAlocacao filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<BasePapelAlocacao> findAll();

    /**
     * Busca uma entidade pela entidade.
     * 
     * @param basePapelAlocacao
     *            - entidade populada .
     * 
     * @return entidade buscada.
     */
    BasePapelAlocacao findByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao);

}