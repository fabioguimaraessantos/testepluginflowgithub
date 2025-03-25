package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CustoRealizado;
import com.ciandt.pms.model.Pessoa;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 24/02/2010
 */
@Service
public interface ICustoRealizadoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     */
    @Transactional
    void createCustoRealizado(final CustoRealizado entity);

    /**
     * Cria um objeto CustoRealizado para validacao.
     * 
     * @param alocacaoPeriodo
     *            - AlocacaoPeriodo que será validado
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param totalAlocacao
     *            - total das alocacoes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias para somar com o total das alocacoes
     *            e verificar se cria ou não CustoRealizado. Criara apenas se a
     *            soma dos totais for menor do que o percentual alocavel.
     * @param percentualAlocavelMes
     *            - percentual alocavel que a pessoa possui no mês
     */
    @Transactional
    void createCustoRealizado(final AlocacaoPeriodo alocacaoPeriodo,
            final Pessoa pPessoa, final Date dataMesValidar,
            final double totalAlocacao, final double totalOhFerias,
            final Double percentualAlocavelMes);

    /**
     * Cria um objeto CustoRealizado para validacao de um Recurso Fixo (not
     * Billable).
     * 
     * @param alocacaoPeriodo
     *            - AlocacaoPeriodo que será validado
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param totalAlocacao
     *            - total das alocacoes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias para somar com o total das alocacoes
     *            e verificar se cria ou não CustoRealizado. Criara apenas se a
     *            soma dos totais for menor do que o percentual alocavel.
     * @param percentualAlocavelMes
     *            - percentual alocavel que a pessoa possui no mês
     */
    @Transactional
    void createCustoRealizadoFixedResource(
            final AlocacaoPeriodo alocacaoPeriodo, final Pessoa pPessoa,
            final Date dataMesValidar, final double totalAlocacao,
            final double totalOhFerias, final Double percentualAlocavelMes);

    /**
     * Cria um objeto CustoRealizado Ferias para validacao.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidado
     *            - dataMes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     */
    @Transactional
    void createCustoRealizadoFerias(final Pessoa pPessoa,
            final Date dataMesValidado, final double totalOhFerias,
            final Double percentualAlocavelMes);

    /**
     * Cenario 5 wiki - desalocacao total e com ferias.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param closingDate
     *            - dataMes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     * 
     */
    @Transactional
    void createCustoRealizadoDesalocTotalClosingDate(final Pessoa pPessoa,
            final Date closingDate, final double totalOhFerias,
            final double percentualAlocavelMes);

    /**
     * Desalocacao total - Billable.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMes
     *            - dataMes que será validado
     * @param totalOhFerias
     *            - total Overhead / ferias
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     * 
     */
    @Transactional
    void createCustoRealizadoDesalocTotal(final Pessoa pPessoa,
            final Date dataMes, final double totalOhFerias,
            final double percentualAlocavelMes);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeCustoRealizado(final CustoRealizado entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoRealizado findCustoRealizadoById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoRealizado> findCustoRealizadoAll();

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
    List<CustoRealizado> findCustRealizByPessoaAndDataMes(final Pessoa pessoa,
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
    List<CustoRealizado> findCustoRealizadoByPessoaAndContratoPratica(
            final Pessoa pessoa, final Date dataMes, final ContratoPratica cp);

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
    CustoRealizado findCustoRealizMaxStartDateByPessoa(final Pessoa pessoa);

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
    List<CustoRealizado> findCustoRealizadoByContratoPraticaAndDate(
            final ContratoPratica cp, final Date dataMes);

}