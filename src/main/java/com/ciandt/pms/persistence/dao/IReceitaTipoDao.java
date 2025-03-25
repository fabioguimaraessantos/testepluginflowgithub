package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReceitaTipo;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Osvaldo</a>
 * @since 14/08/2014
 */
@Repository
public interface IReceitaTipoDao extends IAbstractDao<Long, ReceitaTipo> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ReceitaTipo> findAll();

    /**
     * Busca um {@link ReceitaTipo} pelo seu nome.
     *
     * @return {@link ReceitaTipo}
     */
    ReceitaTipo findByNomeReceitaTipo(final String nomeReceitaTipo);

    /**
     * Busca um {@link ReceitaTipo} pelo sua sigla.
     *
     * @return {@link ReceitaTipo}
     */
    ReceitaTipo findBySiglaReceitaTipo(final String siglaReceitaTipo);
}