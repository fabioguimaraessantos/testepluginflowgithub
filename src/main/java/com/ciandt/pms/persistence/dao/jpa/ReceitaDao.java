/**
 * Classe ReceitaDao que implementa a camada de DAO da entidade Receita 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.persistence.dao.IReceitaDao;
import org.springframework.transaction.annotation.Transactional;


/**
 * 
 * A classe ReceitaDao proporciona as funcionalidades de acesso ao banco de
 * dados para a entidade Receita.
 * 
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class ReceitaDao extends AbstractDao<Long, Receita> implements
        IReceitaDao {
    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    /**
     * Construtor padrão.
     *
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public ReceitaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Receita.class);
        entityManager = factory.createEntityManager();
    }



    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Receita> findByFilter(final Receita filter) {
        Long codigoContratoPratica = Long.valueOf(0);
        if (filter.getContratoPratica() != null) {
            codigoContratoPratica =
                    filter.getContratoPratica().getCodigoContratoPratica();
        }

        List<Receita> listResult =
                getJpaTemplate().findByNamedQuery(
                        Receita.FIND_BY_FILTER,
                        new Object[]{codigoContratoPratica,
                                codigoContratoPratica, filter.getDataMes(),
                                filter.getIndicadorVersao(),
                                filter.getIndicadorVersao()});
        return listResult;
    }

    public Long findByRevenueDealFiscal(final Long revenueDealFiscalCode) {
        Object result = null;
        try {
            Query query = entityManager.createNamedQuery(Receita.FIND_BY_RECEITA_DEAL_FISCAL);

            query.setParameter("revenueDealFiscalCode", revenueDealFiscalCode);

            List listResult = query.getResultList();

            if (listResult != null) {
                result = listResult.get(0);
            }

            if (result == null) {
                result = Long.valueOf(0);
            }
        } catch (HibernateException e) {
            result = Long.valueOf(0);
            e.printStackTrace();
        } catch (Exception e) {
            result = Long.valueOf(0);
            e.printStackTrace();
        }

        return Long.valueOf(result.toString());
    }

    public Boolean updateStatusReceita(Long revenueCode, String revenueStatus) {
        Boolean isUpdated = Boolean.valueOf(false);
        EntityManager entityManager = null;
        try {

            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            Query query = entityManager.createNamedQuery(Receita.UPDATE_STATUS_RECEITA);

            query.setParameter("revenueStatus", revenueStatus);
            query.setParameter("revenueCode", revenueCode);

            query.executeUpdate();
            transaction.commit();

            isUpdated = Boolean.valueOf(true);
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            EntityManagerFactoryUtils.closeEntityManager(entityManager);
        }

        return isUpdated;
    }

    /**
     * Busca uma lista de entidades pelo filtro. Também carrega asentidades
     * relacionadas.
     * 
     * @param filter
     *            entidade populada com os valores do filtro e carrega.
     * @param cli
     *            entidade do tipo Cliente.
     * @param cl
     *            entidade do tipo CentroLucro
     * @param natureza
     *            entidade do tipo NaturezaCentroLucro
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Receita> findByFilter(final Receita filter, final Cliente cli,
            final CentroLucro cl, final NaturezaCentroLucro natureza) {

        Long codigoCentroLucro = Long.valueOf(0);
        Long codigoNatureza = Long.valueOf(0);
        Long codigoCliente = Long.valueOf(0);
        Long codigoPratica = Long.valueOf(0);

        // pega o código do contrato-pratica
        Pratica pratica = filter.getContratoPratica().getPratica();
        if (pratica != null) {
            codigoPratica = pratica.getCodigoPratica();
        }
        // pega o código do cliente
        if (cli != null) {
            codigoCliente = cli.getCodigoCliente();
        }
        // pega o códido so centro-lucro
        if (cl != null) {
            codigoCentroLucro = cl.getCodigoCentroLucro();
        }
        // pega o código da natureza
        if (natureza != null) {
            codigoNatureza = natureza.getCodigoNatureza();
        }

        List<Receita> listResult =
                getJpaTemplate().findByNamedQuery(
                        Receita.FIND_BY_FILTER_FETCH,
                        new Object[]{filter.getDataMes(), codigoCentroLucro,
                                codigoCentroLucro, codigoNatureza,
                                codigoNatureza, codigoCliente, codigoCliente,
                                codigoPratica, codigoPratica,
                                filter.getIndicadorVersao(),
                                filter.getIndicadorVersao(),
                                codigoCentroLucro,
                                filter.getDataMes()});
        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro, porém com status Published e
     * Integrated.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Receita> findAllNoWorkingByFilter(final Receita filter) {
        Long codigoContratoPratica = Long.valueOf(0);
        if (filter.getContratoPratica() != null) {
            codigoContratoPratica =
                    filter.getContratoPratica().getCodigoContratoPratica();
        }

        List<Receita> listResult =
                getJpaTemplate().findByNamedQuery(
                        Receita.FIND_ALL_NO_WORKING_BY_FILTER,
                        new Object[]{codigoContratoPratica,
                                codigoContratoPratica, filter.getDataMes()});
        return listResult;
    }

    /**
     * Busca uma lista de entidades nao fechadas, status diferente de Published
     * e Integrated.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Receita> findAllNotClosed(final Date dataMes) {

        List<Receita> listResult =
                getJpaTemplate().findByNamedQuery(Receita.FIND_ALL_NOT_CLOSED,
                        new Object[]{dataMes});

        return listResult;
    }

    /**
     * Busca uma todas as receitas de um determinado mês.
     * 
     * @param monthDate
     *            - data mês de referencia.
     * 
     * @return lista de receitas do mês passado por parametro.
     */
    @SuppressWarnings("unchecked")
    public List<Receita> findByMonthDate(final Date monthDate) {

        List<Receita> listResult =
                getJpaTemplate().findByNamedQuery(Receita.FIND_BY_MONTH_DATE,
                        new Object[]{monthDate});

        return listResult;
    }

    /**
     * 
     * @param cpcl
     *            ContratoPraticaCentroLucro
     * @param cl
     *            centroLucro
     * 
     * @param natureza
     *            natureza
     * @return List de receitas com o filtro especificado.
     */
    @SuppressWarnings("unchecked")
    public List<Receita> findByCpclAndCentroLucro(final CentroLucro cl) {

        List<Receita> listResult =
                getJpaTemplate().findByNamedQuery(
                        Receita.FIND_BY_CPCL_AND_CENTROLUCRO,
                        new Object[]{cl.getCodigoCentroLucro()});
        return listResult;

    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IReceitaDao#findUltimasReceitasByCP(java
	 * .lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Receita> findUltimasReceitasByCP(Long meses,
			Long codigoContratoPratica) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("meses", meses);
		params.put("cdContratoPratica", codigoContratoPratica);

		List<Receita> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Receita.FIND_LASTS_BY_DATA_AND_CPCL, params);
		return listResult;
	}

    public List<Receita> findAllReceitaForecastByCodigoContratoPratica(Long codigoContratoPratica) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoContratoPratica", codigoContratoPratica);

        List<Receita> listResult = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        Receita.FIND_ALL_RECEITA_FORECAST_BY_CODIGO_CONTRATO_PRATICA, params);
        return listResult;
    }

    public List<Receita> findNotIntegratedRevenueAfterClosingRevenueDate(Long codigoDealFiscal, Date closingRevenueDate) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoDealFiscal", codigoDealFiscal);
        params.put("closingRevenueDate", closingRevenueDate);

        List<Receita> listResult = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        Receita.FIND_NOT_INTEGRATED_REVENUE_AFTER_CLOSING_REVENUE_DATE, params);
        return listResult;
    }

    public List<Receita> findRevenuesByMsaAndDates(Long msa, Date startDate, Date endDate) {
        String namedQuery = Receita.FIND_WK_OR_PB_REVENUES_BY_MSA_AND_START_DATE;

          Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoMsa", msa);
        params.put("dtStart", startDate);

        if(endDate != null){
            namedQuery = Receita.FIND_WK_OR_PB_REVENUES_BY_MSA_AND_DATES;
            params.put("dtEnd", endDate);
        }

        return  getJpaTemplate().findByNamedQueryAndNamedParams(namedQuery, params);
    }

}