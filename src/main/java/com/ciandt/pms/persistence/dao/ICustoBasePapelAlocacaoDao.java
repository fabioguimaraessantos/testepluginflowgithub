package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.CustoBasePapelAlocacao;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author cmantovani
 * @since 13/07/2011
 */
public interface ICustoBasePapelAlocacaoDao extends
        IAbstractDao<Long, CustoBasePapelAlocacao> {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoBasePapelAlocacao findById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<CustoBasePapelAlocacao> findAll();

    /**
     * Busca uma lista de entidades pelo PapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    List<CustoBasePapelAlocacao> findByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao);

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    CustoBasePapelAlocacao findMaxStartDateByBasePapelAlocacao(
            final BasePapelAlocacao basePapelAlocacao);

    /**
     * Busca a entidade no qual intervalo possua a pada passado por parametro,
     * referente ao PapelAlocacao.
     * 
     * @param basePapelAlocacao
     *            entidade populada com os valores do BasePapelAlocacao.
     * @param date
     *            Data que se deseja saber o valor do CustoBase do
     *            PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do BasePapelAlocacao.
     */
    CustoBasePapelAlocacao findByBasePapelAndDate(
            final BasePapelAlocacao basePapelAlocacao, final Date date);

    /**
     * Busca pelo fator de reajuste referente a data de inicio.
     * 
     * @param custoBasePapelAlocacao
     *            - entidade do tipo CustoBasePapelAlocacao.
     * 
     * @return retorna o CustoBasePapelAlocacao.
     */
    CustoBasePapelAlocacao findByStartDate(
            final CustoBasePapelAlocacao custoBasePapelAlocacao);

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param custoBasePapelAlocacao
     *            - entidade do tipo CustoBasePapelAlocacao.
     * 
     * @return retorna o CustoBasePapelAlocacao.
     */
    CustoBasePapelAlocacao findNext(
            final CustoBasePapelAlocacao custoBasePapelAlocacao);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param custoBasePapelAlocacao
     *            - entidade do tipo CustoBasePapelAlocacao.
     * 
     * @return retorna o CustoBasePapelAlocacao anterior.
     */
    CustoBasePapelAlocacao findPrevious(
            final CustoBasePapelAlocacao custoBasePapelAlocacao);

    CustoBasePapelAlocacao findByBasePapelAndCurrentDate(
            final BasePapelAlocacao basePapelAlocacao);

    List<CustoBasePapelAlocacao> findByStartDateGreaterThan(
            final Long basePapelAlocacaoCode, final Date startDate);

}