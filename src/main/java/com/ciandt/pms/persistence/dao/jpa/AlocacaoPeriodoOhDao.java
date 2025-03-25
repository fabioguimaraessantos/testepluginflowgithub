package com.ciandt.pms.persistence.dao.jpa;

import java.util.*;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IAlocacaoPeriodoOhDao;


/**
 * 
 * A classe AlocacaoPeriodoOhDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade AlocacaoPeriodoOh.
 * 
 * @since 11/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class AlocacaoPeriodoOhDao extends AbstractDao<Long, AlocacaoPeriodoOh>
        implements IAlocacaoPeriodoOhDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica do entity manager.
     */
    @Autowired
    public AlocacaoPeriodoOhDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AlocacaoPeriodoOh.class);
    }

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
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodoOh> findByPessoaAndMonthDate(
            final Pessoa pessoa, final Date monthDate) {
        List<AlocacaoPeriodoOh> listResult = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodoOh.FIND_BY_PESSOA_AND_MONTH_DATE,
                new Object[] {pessoa.getCodigoPessoa(), monthDate });

        return listResult;
    }

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
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodoOh> findByPessoaAndBetweenMonthDate(
            final Pessoa pessoa, final Date startMonthDate,
            final Date endMonthDate) {
        List<AlocacaoPeriodoOh> listResult = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodoOh.FIND_BY_PESSOA_AND_BETWEEN_MONTH_DATE,
                new Object[] {pessoa.getCodigoPessoa(), startMonthDate,
                        endMonthDate });

        return listResult;
    }

    /**
     * Retorna todas as entidades relacionadas com a AlocacaoOverhead passada
     * por parametro.
     * 
     * @param alocacaoOverhead
     *            - AlocacaoOverhead que se deseja buscar as AlocacaPeriodoOh.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoPeriodoOh> findByAlocacaoOverhead(
            final AlocacaoOverhead alocacaoOverhead) {
        List<AlocacaoPeriodoOh> listResult = getJpaTemplate().findByNamedQuery(
                AlocacaoPeriodoOh.FIND_BY_ALOCACAO_OVERHEAD,
                new Object[] {alocacaoOverhead.getCodigoAlocacaoOverhead() });

        return listResult;
    }

    @Override
    public List<AlocacaoPeriodoOh> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month) {
        List<Long> codes = new ArrayList<Long>();
        List<AlocacaoPeriodoOh> allocationPeriodOverheads = new ArrayList<AlocacaoPeriodoOh>();

        for (Long code : peopleCodes) {
            codes.add(code);

            if (codes.size() >= 999) {
                allocationPeriodOverheads.addAll(this.executeFindByPeopleCodeInAndMonth(codes, month));
                codes.clear();
            }
        }

        allocationPeriodOverheads.addAll(this.executeFindByPeopleCodeInAndMonth(codes, month));

        return allocationPeriodOverheads;
    }

    private List<AlocacaoPeriodoOh> executeFindByPeopleCodeInAndMonth(List<Long> codes, Date month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("peopleCodes", codes);
        params.put("month", month);

        return getJpaTemplate().findByNamedQueryAndNamedParams(AlocacaoPeriodoOh.FIND_BY_PESSOA_IN_AND_MONTH, params);
    }

}