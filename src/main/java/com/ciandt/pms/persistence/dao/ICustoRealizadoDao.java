package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CustoRealizado;
import com.ciandt.pms.model.Pessoa;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 24/02/2010
 */
@Repository
public interface ICustoRealizadoDao extends IAbstractDao<Long, CustoRealizado> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoRealizado> findAll();

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
    List<CustoRealizado> findByPessoaAndDataMes(final Pessoa pessoa,
            final Date dataMes);

    /**
     * Busca uma lista de entidades pela Pessoa, dataMes e ContratoPratica.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<CustoRealizado> findByPessoaAndContratoPratica(final Pessoa pessoa,
            final Date dataMes, final ContratoPratica cp);

    /**
     * Busca uma lista de entidades pela Pessoa, dataMes e ContratoPratica.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    Double getTotalAlocacaoPessoa(final Pessoa pessoa, final Date dataMes,
            final ContratoPratica cp);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * 
     * @return lista de entidades que atendem ao criterio da Pessoa.
     */
    CustoRealizado findMaxStartDateByPessoa(final Pessoa pessoa);

    /**
     * Busca uma lista de entidades pelo ContratoPratica e dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<CustoRealizado> findByContratoPraticaAndDate(final ContratoPratica cp,
            final Date dataMes);

}