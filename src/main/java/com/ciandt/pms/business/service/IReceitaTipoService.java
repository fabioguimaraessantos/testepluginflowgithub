package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ReceitaTipo;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2014
 */
@Service
public interface IReceitaTipoService {

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