package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CustoTceGrupoCusto;
import com.ciandt.pms.model.Pessoa;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 24/02/2010
 */
@Repository
public interface ICustoTceGrupoCustoDao extends
        IAbstractDao<Long, CustoTceGrupoCusto> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoTceGrupoCusto> findAll();

    /**
     * Busca uma lista de entidades pela Pessoa e dataMes.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<CustoTceGrupoCusto> findByPessoaAndDataMes(final Pessoa pessoa,
            final Date dataMes);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * 
     * @return lista de entidades que atendem ao criterio da Pessoa.
     */
    CustoTceGrupoCusto findMaxStartDateByPessoa(final Pessoa pessoa);

}