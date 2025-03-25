package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Pratica;


/**
 * 
 * A classe IPraticaDao proporciona a inteface de acesso para o banco de dados
 * referente a entidade Pratica.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IPraticaDao extends IAbstractDao<Long, Pratica> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Pratica> findAll();

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro
     */
    List<Pratica> findByFilter(final Pratica filter);

}
