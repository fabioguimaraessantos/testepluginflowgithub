package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaDelegacao;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author cmantovani
 * @since 01/07/2011
 */
@Repository
public interface IPessoaDelegacaoDao extends
        IAbstractDao<Long, PessoaDelegacao> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PessoaDelegacao> findAll();

    /**
     * Busca as delegacoes pelo filtro no periodo.
     * 
     * @param pessoa
     *            - pessoa para consulta
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    PessoaDelegacao findByPessoaAndDate(final Pessoa pessoa, final Date data);

    /**
     * Busca as delegacoes nao expiradas.
     * 
     * @param pessoa
     *            - pessoa a ser pesquisada
     * @param data
     *            - data de pesquisa
     * @return pessoaDelegacao
     */
    PessoaDelegacao findByPessoaAndFinalDate(final Pessoa pessoa,
            final Date data);

}