package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * A classe IAlocacaoPeriodoOhDao proporciona a intercefe de acesso a camada de
 * persistencia referente a entidade AlocacaoPeriodoOh.
 * 
 * @since 11/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IAlocacaoPeriodoOhDao extends
        IAbstractDao<Long, AlocacaoPeriodoOh> {

    /**
     * Retorna todas as entidades relacionadas com a Pessoa e data mes.
     * 
     * @param pessoa
     *            - pessoa que se deseja buscar o relacionamento.
     * 
     * @param monthDate
     *            - data mes que se deseja buscar.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AlocacaoPeriodoOh> findByPessoaAndMonthDate(final Pessoa pessoa,
            Date monthDate);

    /**
     * Retorna todas as entidades relacionadas com a Pessoa e data mes.
     * 
     * @param pessoa
     *            - pessoa que se deseja buscar o relacionamento.
     * 
     * @param startMonthDate
     *            - data mes fim.
     * 
     * @param endMonthDate
     *            - data mes inicio
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AlocacaoPeriodoOh> findByPessoaAndBetweenMonthDate(
            final Pessoa pessoa, final Date startMonthDate,
            final Date endMonthDate);

    /**
     * Retorna todas as entidades relacionadas com a AlocacaoOverhead passada
     * por parametro.
     * 
     * @param alocacaoOverhead
     *            - AlocacaoOverhead que se deseja buscar as AlocacaPeriodoOh.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<AlocacaoPeriodoOh> findByAlocacaoOverhead(
            final AlocacaoOverhead alocacaoOverhead);

    List<AlocacaoPeriodoOh> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month);
}