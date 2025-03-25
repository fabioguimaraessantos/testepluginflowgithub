package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CustoTceGrupoCusto;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Pessoa;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 24/02/2010
 */
@Service
public interface ICustoTceGrupoCustoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     */
    @Transactional
    void createCustoTceGrupoCusto(final CustoTceGrupoCusto entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeCustoTceGrupoCusto(final CustoTceGrupoCusto entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoTceGrupoCusto findCustoTceGrupoCustoById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoTceGrupoCusto> findCustoTceGrupoCustoAll();

    /**
     * Cria um objeto CustoTceGrupoCusto para validacao.
     * 
     * @param pPessoa
     *            - Pessoa que será validada
     * @param dataMesValidar
     *            - dataMes que será validado
     * @param difTotalAlocacao
     *            - diferenca / restante do total das alocacoes que será
     *            validado
     * @param pGrupoCusto
     *            - GrupoCusto que a Pessoa está associada
     * @param percentualAlocavelMes
     *            - percentual alocável da pessoa no mes corrente.
     */
    void createCustoTceGrupoCusto(final Pessoa pPessoa,
            final Date dataMesValidar, final double difTotalAlocacao,
            final GrupoCusto pGrupoCusto, final Double percentualAlocavelMes);

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
    List<CustoTceGrupoCusto> findCusTceGCByPessoaAndDataMes(
            final Pessoa pessoa, final Date dataMes);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * 
     * @return lista de entidades que atendem ao criterio da Pessoa.
     */
    CustoTceGrupoCusto findCusTceGCMaxStartDateByPessoa(final Pessoa pessoa);

}