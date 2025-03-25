package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaDao;


/**
 * 
 * A classe CotacaMoedaDao proporciona as funcionalidades de acesso ao banco de
 * dadso para a entidade CotacaoMoeda.
 * 
 * @since 15/02/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class CotacaoMoedaDao extends AbstractDao<Long, CotacaoMoeda> implements
        ICotacaoMoedaDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public CotacaoMoedaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CotacaoMoeda.class);
    }

    /**
     * Busca pela Moeda e Periodo.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * @param dataDiaBeg
     *            Data inicio referente ao dia da cotação.
     * @param dataDiaEnd
     *            Data final referente ao dia da cotação.
     * 
     * @return retorna uma lista de CotacaoMoeda.
     */
    @SuppressWarnings("unchecked")
    public List<CotacaoMoeda> findByFilter(final CotacaoMoeda filter,
            final Date dataDiaBeg, final Date dataDiaEnd) {
        Long codigoMoeda = Long.valueOf(0);
        if (filter.getMoeda() != null) {
            codigoMoeda = filter.getMoeda().getCodigoMoeda();
        }

        List result = getJpaTemplate().findByNamedQuery(
                CotacaoMoeda.FIND_BY_MOEDA_AND_PERIOD,
                new Object[] {codigoMoeda, dataDiaBeg, dataDiaEnd,
                        filter.getIndicadorTipo() });

        return result;
    }

    /**
     * Busca a ultima cotação do mes de uma moeda.
     * 
     * @param moeda
     *            entidade do tipo Moeda.
     * @param dateMonth
     *            Data-mes referente a cotação.
     * 
     * @return retorna a ultimka cotação do mês, caso não exista nenhuma retorna
     *         null.
     */
    @SuppressWarnings("unchecked")
    public CotacaoMoeda findLastByMonth(final Moeda moeda, final Date dateMonth) {

        List<CotacaoMoeda> result = getJpaTemplate().findByNamedQuery(
                CotacaoMoeda.FIND_LAST_BY_MONTH,
                new Object[] {moeda.getCodigoMoeda(), moeda.getCodigoMoeda(),
                        dateMonth });

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    /**
     * Busca a cotação de uma moeda em un determinado mes.
     * 
     * @param moeda
     *            entidade do tipo Moeda.
     * @param dateMonth
     *            Data-mes referente a cotação.
     * @param tipo
     *            tipo da cotação (R-Realizada ou P-Planejada)
     * 
     * @return retorna a ultimka cotação do mês, caso não exista nenhuma retorna
     *         null.
     */
    @SuppressWarnings("unchecked")
    public CotacaoMoeda findByMoedaMesAndTipo(final Moeda moeda,
            final Date dateMonth, final String tipo) {

        List<CotacaoMoeda> result = getJpaTemplate().findByNamedQuery(
                CotacaoMoeda.FIND_BY_MOEDA_MES_AND_TIPO,
                new Object[] {moeda.getCodigoMoeda(), dateMonth, tipo });

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    /**
     * Busca uma entidade cadastrada no ultimo dia do mes.
     * 
     * @param moeda
     *            - Moeda corrente.
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return a entidade cadastrada no ultimo dia do mes.
     */
    @SuppressWarnings("unchecked")
    public CotacaoMoeda findByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes) {

        List<CotacaoMoeda> listResult = getJpaTemplate().findByNamedQuery(
                CotacaoMoeda.FIND_BY_MOEDA_AND_LAST_DAY_MONTH,
                new Object[] {moeda.getCodigoMoeda(), dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}