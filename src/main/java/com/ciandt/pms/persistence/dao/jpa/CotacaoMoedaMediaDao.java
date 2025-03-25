package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ComposicaoTce;
import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.persistence.dao.ICotacaoMoedaMediaDao;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.*;


/**
 * 
 * A classe CotacaMoedaDao proporciona as funcionalidades de acesso ao banco de
 * dadso para a entidade CotacaoMoedaMedia.
 * 
 * @since 15/02/2016
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Repository
public class CotacaoMoedaMediaDao extends AbstractDao<Long, CotacaoMoedaMedia> implements
        ICotacaoMoedaMediaDao {

    private EntityManager entityManager;

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public CotacaoMoedaMediaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CotacaoMoedaMedia.class);
        entityManager = factory.createEntityManager();
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
     * @return retorna uma lista de CotacaoMoedaMedia.
     */
    @SuppressWarnings("unchecked")
    public List<CotacaoMoedaMedia> findByFilter(final CotacaoMoedaMedia filter,
            final Date dataDiaBeg, final Date dataDiaEnd) {

    	Long codigoMoedaDe = Long.valueOf(0);
        if (filter.getMoedaDe() != null) {
        	codigoMoedaDe = filter.getMoedaDe().getCodigoMoeda();
        }
    	Long codigoMoedaPara = Long.valueOf(0);
        if (filter.getMoedaPara() != null) {
        	codigoMoedaPara = filter.getMoedaPara().getCodigoMoeda();
        }

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", dataDiaBeg);
		params.put("endDate", dataDiaEnd);
		params.put("codigoMoedaDe", codigoMoedaDe);
		params.put("codigoMoedaPara", codigoMoedaPara);

		List<CotacaoMoedaMedia> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						CotacaoMoedaMedia.FIND_BY_MOEDA_AND_PERIOD,
						params);

        return results == null || results.isEmpty() ? new ArrayList<>() : results;
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
    public CotacaoMoedaMedia findLastByMonth(final Moeda moeda, final Date dateMonth) {

        List<CotacaoMoedaMedia> result = getJpaTemplate().findByNamedQuery(
                CotacaoMoedaMedia.FIND_LAST_BY_MONTH,
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
    public CotacaoMoedaMedia findByMoedaMesAndTipo(final Moeda moeda,
            final Date dateMonth, final String tipo) {

        List<CotacaoMoedaMedia> result = getJpaTemplate().findByNamedQuery(
                CotacaoMoedaMedia.FIND_BY_MOEDA_MES_AND_TIPO,
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
    public CotacaoMoedaMedia findByMoedaAndLastDayMonth(final Moeda moeda,
            final Date dataMes) {

        List<CotacaoMoedaMedia> listResult = getJpaTemplate().findByNamedQuery(
                CotacaoMoedaMedia.FIND_BY_MOEDA_AND_LAST_DAY_MONTH,
                new Object[] {moeda.getCodigoMoeda(), dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
	public List<CotacaoMoedaMedia> findByDeAndParaAndPeriodAndNovaCotacao(
			final Date startDate, final Date endDate, final Moeda moedaDe,
			final Moeda moedaPara, Boolean isNovaCotacao) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("codigoMoedaDe", moedaDe.getCodigoMoeda());
		params.put("codigoMoedaPara", moedaPara.getCodigoMoeda());

		if (isNovaCotacao == null) {
			params.put("isNovaCotacao", null);			
		} else if (isNovaCotacao) {			
			params.put("isNovaCotacao", Constants.YES);
		} else {			
			params.put("isNovaCotacao", Constants.NO);
		}

		List<CotacaoMoedaMedia> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						CotacaoMoedaMedia.FIND_BY_DE_AND_PARA_AND_PERIOD,
						params);
		return results;
    }

    @SuppressWarnings("unchecked")
    public Date findLastByMoedaDeAndMoedaPara(final Moeda moedaDe, final Moeda moedaPara) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("codigoMoedaDe", moedaDe.getCodigoMoeda());
    	params.put("codigoMoedaPara", moedaPara.getCodigoMoeda());
    	
    	List<CotacaoMoedaMedia> results = getJpaTemplate()
    			.findByNamedQueryAndNamedParams(
    					CotacaoMoedaMedia.FIND_LAST_BY_LAST_MOEDADE,
    					params);

    	if (results != null && !results.isEmpty()) {
			return results.get(0).getDataCotacao();
		}

    	return null;
    }

    public Date findLast() {
    	List<CotacaoMoedaMedia> results = getJpaTemplate().findByNamedQuery(CotacaoMoedaMedia.FIND_LAST);

    	if (results != null && !results.isEmpty()) {
			return results.get(0).getDataCotacao();
		}

    	return null;
    }

    @Override
    public List<CotacaoMoedaMedia> findByDate(Date date) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("date", date);
        List<CotacaoMoedaMedia> exchanges = getJpaTemplate().findByNamedQueryAndNamedParams(CotacaoMoedaMedia.FIND_BY_DATE, params);
        return exchanges != null ? exchanges : new ArrayList<>();
    }

    @Override
    public void create(List<CotacaoMoedaMedia> exchanges) {
        for (CotacaoMoedaMedia exchange : exchanges) {
            create(exchange);
        }
    }

    @Override
    public void deleteByDate(Date date) {
        EntityManager entityManager = null;
        try {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Query query = entityManager
                    .createNamedQuery(CotacaoMoedaMedia.DELETE_BY_DATE);
            query.setParameter("date", date);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}