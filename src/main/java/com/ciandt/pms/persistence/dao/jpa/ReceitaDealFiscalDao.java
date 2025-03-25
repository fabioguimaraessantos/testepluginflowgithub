package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.*;

import com.ciandt.pms.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.persistence.dao.IReceitaDealFiscalDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * A classe ReceitaDealFiscalDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade ReceitaDealFiscal.
 * 
 * @since 04/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ReceitaDealFiscalDao extends AbstractDao<Long, ReceitaDealFiscal>
		implements IReceitaDealFiscalDao {

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/**
	 * Construtor padr�o.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ReceitaDealFiscalDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ReceitaDealFiscal.class);

		entityManager = factory.createEntityManager();
	}

	/**
	 * Busca uma lista de entidades pela receita.
	 * 
	 * @param codigoReceita
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de entidades relacionadas com a receita passada por
	 *         parametro.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByReceita(Long codigoReceita) {

		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_RECEITA,
				new Object[] { codigoReceita });

		return listResult;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param idContratoPratica
	 *            id da entidade do tipo ContratoPratica.
	 * 
	 * @param dataMes
	 *            objeto do tipo Date.
	 * 
	 * @param status
	 *            status da receita.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByFilter(final Long idContratoPratica, final Long idEmpresa,
			final Date dataMes, final String status) {

		Long idCont = Long.valueOf(-1);
		Long idEmp = Long.valueOf(-1);
		if (idContratoPratica != null)
			idCont = idContratoPratica;

		if (idEmpresa != null)
			idEmp = idEmpresa;


		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_CONTRATO_PRATICA_AND_DATA_MES,
				new Object[] { idCont, idCont, idEmp, idEmp, dataMes, status, status });

		return listResult;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param idContratoPratica
	 *            id da entidade do tipo ContratoPratica.
	 * 
	 * @param dataMes
	 *            objeto do tipo Date.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByContratoPraticaAndDataMesToAjuste(
			final Long idContratoPratica, final Date dataMes) {

		Long id = Long.valueOf(-1);
		if (idContratoPratica != null) {
			id = idContratoPratica;
		}

		List<ReceitaDealFiscal> listResult = getJpaTemplate()
				.findByNamedQuery(
						ReceitaDealFiscal.FIND_BY_CONTRATO_PRATICA_AND_DATA_MES_TO_AJUSTE,
						new Object[] { id, id, dataMes });

		return listResult;
	}

	/**
	 * Realiza a integra��o da receita referente ao id passado por parametro.
	 * Esta integra��o � realizada atrav�s da chamada de uma procedure.
	 * 
	 * @param idReceitaDeal
	 *            id da entidade ReceitaDealFiscal.
	 * 
	 * 
	 * @return c�digo do resultado da execu��o da integra��o
	 */
	@SuppressWarnings("unchecked")
	public Integer integrateReceita(final Long idReceitaDeal) {
		Object result = null;
		try {
			Query query = entityManager
					.createNamedQuery(ReceitaDealFiscal.INTEGRATE_RECEITA);

			query.setParameter("param1", idReceitaDeal);

			List listResult = query.getResultList();

			if (listResult != null) {
				result = listResult.get(0);
			}

			if (result == null) {
				result = Integer.valueOf(0);
			}
		} catch (HibernateException e) {
			result = Integer.valueOf(0);
			e.printStackTrace();
		} catch (Exception e) {
			result = Integer.valueOf(0);
			e.printStackTrace();
		}

		return Integer.valueOf(result.toString());
	}

	/**
	 * NOVA IMPLEMENTACAO - Integra��o Intercompany Busca se o pedido foi
	 * cancelado no ERP.
	 * 
	 * @param rdf
	 *            entidade ReceitaDealFiscal.
	 * @param erpPedido
	 *            numero do pedido
	 * 
	 * 
	 * @return retorna true se cancelado ou n�o existe, caso contrario false.
	 *         Retorna null caso ocorra uma excetion ao executar a consulta.
	 */
	/*
	 * @SuppressWarnings({"deprecation"}) public Boolean
	 * isErpOrderCanceled(final ReceitaDealFiscal rdf, final BigDecimal
	 * erpPedido) { try { Session session = (Session)
	 * entityManager.getDelegate();
	 * 
	 * // realiza a consulta do pedido no ERP String query =
	 * "SELECT p.ped_in_sequencia, p.ped_ch_situacao " +
	 * " FROM ven_pedidovenda p " + " WHERE p.ped_in_sequencia = ? ";
	 * 
	 * PreparedStatement st = session.connection().prepareStatement(query);
	 * 
	 * st.setBigDecimal(1, erpPedido);
	 * 
	 * ResultSet rs = st.executeQuery(); // pega o resultado if (rs.next()) {
	 * String canceledStatus = systemProperties
	 * .getProperty("erp.pedido.status.cancelado");
	 * 
	 * String status = rs.getString(2); // verifica se o status � igual a
	 * cancelado if (canceledStatus.equals(status)) { return true; } return
	 * false; } else { return true; }
	 * 
	 * } catch (HibernateException e) { e.printStackTrace(); } catch
	 * (SQLException e) { e.printStackTrace(); }
	 * 
	 * return null; }
	 */

	/**
	 * IMPLEMENTA��O ANTIGA Busca se o pedido foi cancelado no ERP.
	 * 
	 * @param rdf
	 *            entidade ReceitaDealFiscal.
	 * 
	 * 
	 * @return retorna true se cancelado ou n�o existe, caso contrario false.
	 *         Retorna null caso ocorra uma excetion ao executar a consulta.
	 */
	@SuppressWarnings({ "deprecation" })
	public Boolean isErpOrderCanceled(final ReceitaDealFiscal rdf) {
		try {
			Session session = (Session) entityManager.getDelegate();

			// realiza a consulta do peido no ERP
			String query = "SELECT p.ped_in_sequencia, p.ped_ch_situacao "
					+ " FROM pms20.ven_pedidovenda p "
					+ " WHERE p.ped_in_sequencia = ? ";

			PreparedStatement st = session.connection().prepareStatement(query);

			st.setBigDecimal(1, rdf.getCodigoErpPedido());

			ResultSet rs = st.executeQuery();
			// pega o resultado
			if (rs.next()) {
				String canceledStatus = systemProperties
						.getProperty("erp.pedido.status.cancelado");

				String status = rs.getString(2);
				// verifica se o status � igual a cancelado
				if (canceledStatus.equals(status)) {
					return true;
				}
				return false;
			} else {
				return true;
			}

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Boolean updateStatusReceitaDealFiscal(StatusReceitaDealFiscal statusReceitaDealFiscal) {
		Boolean isUpdated = Boolean.valueOf(false);
		try {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
			Query query = entityManager.createNamedQuery(ReceitaDealFiscal.UPDATE_STATUS_RECEITA_DEAL_FISCAL);

			query.setParameter("revenueStatus", statusReceitaDealFiscal.getRevenueStatus());
			query.setParameter("errorMessage", statusReceitaDealFiscal.getErrorMessage());
			if (statusReceitaDealFiscal.isError()) {
                query.setParameter("megaOrderID", "");
            } else {
                query.setParameter("megaOrderID", statusReceitaDealFiscal.getMegaOrderID());
            }
			query.setParameter("revenueDealFiscalCode", statusReceitaDealFiscal.getRevenueDealFiscalCode());

			query.executeUpdate();
			transaction.commit();

			isUpdated = Boolean.valueOf(true);
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isUpdated;
	}

	public Long findNotIntegratedRevenues(final Long revenueCurrencyCode) {
		Object result = null;
		try {
			Query query = entityManager.createNamedQuery(ReceitaDealFiscal.FIND_NOT_INTEGRATED_REVENUES);

			query.setParameter("revenueCurrencyCode", revenueCurrencyCode);

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

	/**
	 * Realiza a busca da entidade, atravez dos criterios do filtro.
	 * 
	 * @param cp
	 *            - ContratoPratica
	 * @param cli
	 *            - Cliente
	 * @param cl
	 *            - CentroLucro
	 * @param natureza
	 *            - NaturezaCentroLucro
	 * @param dataMesInicio
	 *            - Data mes inicio
	 * @param dataMesFim
	 *            - Data mes fim
	 * 
	 * @return retorna uma lista de ReceitaDealFiscal
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByFilter(final ContratoPratica cp,
			final Cliente cli, final CentroLucro cl,
			final NaturezaCentroLucro natureza, final Date dataMesInicio,
			final Date dataMesFim) {

		Long codigoContratoPratica = 0L;
		if (cp != null) {
			codigoContratoPratica = cp.getCodigoContratoPratica();
		}

		Long codigoCliente = 0L;
		if (cli != null) {
			codigoCliente = cli.getCodigoCliente();
		}

		Long codigoCentroLucro = 0L;
		if (cl != null) {
			codigoCentroLucro = cl.getCodigoCentroLucro();
		}

		Long codigoNatureza = 0L;
		if (natureza != null) {
			codigoNatureza = natureza.getCodigoNatureza();
		}

		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_FILTER,
				new Object[] { codigoContratoPratica, codigoContratoPratica,
						codigoCliente, codigoCliente, codigoCentroLucro,
						codigoCentroLucro, codigoNatureza, codigoNatureza,
						dataMesInicio, dataMesFim });

		return listResult;
	}

	/**
	 * Realiza a busca da entidade, atravez dos criterios do filtro.
	 * 
	 * @param cp
	 *            - ContratoPratica
	 * @param cli
	 *            - Cliente
	 * @param dataMesInicio
	 *            - Data mes inicio
	 * @param dataMesFim
	 *            - Data mes fim
	 * 
	 * @param receita
	 *            receita
	 * 
	 * @return retorna uma lista de ReceitaDealFiscal
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<ReceitaDealFiscal> findByFilterSub(final ContratoPratica cp,
			final Cliente cli, final Date dataMesInicio, final Date dataMesFim,
			final Receita receita, final DealFiscal dealFiscal) {

		Long codigoReceita = 0L;
		if (receita != null) {
			codigoReceita = receita.getCodigoReceita();
		}

		Long codigoCliente = 0L;
		if (cli != null) {
			codigoCliente = cli.getCodigoCliente();
		}

		Long codigoContratoPratica = 0L;
		if (cp != null) {
			codigoContratoPratica = cp.getCodigoContratoPratica();
		}

		Long codigoDealFiscal = 0L;
		if (dealFiscal != null) {
			codigoDealFiscal = dealFiscal.getCodigoDealFiscal();
		}

		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_FILTER_SUB,
				new Object[] { codigoContratoPratica, codigoContratoPratica,
						codigoCliente, codigoCliente, dataMesInicio,
						dataMesFim, codigoReceita, codigoReceita,
						codigoDealFiscal, codigoDealFiscal });

		return listResult;
	}

	/**
	 * Pega o valor total da receita relacionado com o deal e a receita,
	 * passados por parametro.
	 * 
	 * @param receita
	 *            - Receita em quest�o
	 * @param deal
	 *            - DealFiscal em quest�o
	 * 
	 * @return retorna o valor total da receita
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getTotalReceitaByDealAndReceita(final Receita receita,
			final DealFiscal deal) {

		List total = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.GET_TOTAL_BY_DEAL_AND_RECEITA,
				new Object[] { receita.getCodigoReceita(),
						deal.getCodigoDealFiscal() });

		if (total.isEmpty()) {
			return BigDecimal.valueOf(0);
		}

		return (BigDecimal) total.get(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IReceitaDealFiscalDao#
	 * getTotalPublishedByDealFiscalAndDate(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal deal, final Date dataFim) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoDealFiscal", deal.getCodigoDealFiscal());
		params.put("dataFim", dataFim);

		List<BigDecimal> total = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ReceitaDealFiscal.GET_TOTAL_PUBLISHED_BY_DEAL_FISCAL_AND_DATE,
						params);

		if (total.isEmpty() || total.get(0) == null) {
			return BigDecimal.valueOf(0);
		}

		return (BigDecimal) total.get(0);
	}

	/**
	 * Busca a Receita Deal Fiscal pela receita e deal fiscal.
	 * 
	 * @param receita
	 *            - Receita em quest�o
	 * @param deal
	 *            - DealFiscal em quest�o
	 * 
	 * @return retorna um {@link ReceitaDealFiscal}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ReceitaDealFiscal findByReceitaAndDeal(final Receita receita,
			final DealFiscal deal) {

		List<ReceitaDealFiscal> result = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_RECEITA_AND_DEAL,
				new Object[] { receita.getCodigoReceita(),
						deal.getCodigoDealFiscal() });

		if (result != null && !result.isEmpty()) {
			return result.get(0);
		}

		return null;
	}

	/**
	 * Busca todos os {@link ReceitaDealFiscal} ativos dado um
	 * {@link ContratoPratica} e uma {@link Moeda}.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return uma lista de {@link ReceitaDealFiscal}
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByActiveAndCLobAndMoeda(
			final ContratoPratica contratoPratica, final Moeda moeda) {
		List<ReceitaDealFiscal> result = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_ACTIVE_AND_CLOB_AND_MOEDA,
				new Object[] { contratoPratica.getCodigoContratoPratica(),
						moeda.getCodigoMoeda() });

		return result;
	}

	/**
	 * Busca ReceitaDealFiscal por ReceitaMoeda.
	 * 
	 * @param codigoReceitaMoeda
	 *            receitaMoeda.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByReceitaMoeda(
			Long codigoReceitaMoeda) {
		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_RECEITA_MOEDA,
				new Object[] { codigoReceitaMoeda });
		return listResult;
	}

	/**
	 * Busca ReceitaDealFiscal por DealFiscal.
	 * 
	 * @param dealFiscal
	 *            DealFiscal.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findPublishedAndIntegrateByDealFiscal(
			final DealFiscal dealFiscal) {
		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_PUBLISHED_AND_INTEGRATE_BY_DEAL_FISCAL,
				new Object[] { dealFiscal.getCodigoDealFiscal() });
		return listResult;
	}

	/**
	 * Busca ReceitaDealFiscal por DealFiscal e periodo.
	 * 
	 * @param dealFiscal
	 *            DealFiscal.
	 * @return listResult.
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByDealFiscalAndPeriod(
			final DealFiscal dealFiscal, final Date startDate,
			final Date endDate) {
		
		StringBuilder sql = new StringBuilder("SELECT t FROM ReceitaDealFiscal t");
		sql.append(" JOIN t.receitaMoeda rm");
		sql.append(" JOIN rm.receita r");
		sql.append(" WHERE t.dealFiscal.codigoDealFiscal = :codigoDealFiscal");
		
		if (startDate != null) {
			sql.append(" AND r.dataMes >= :startDate");			
		}
		
		if (endDate != null) {
			sql.append(" AND r.dataMes <= :endDate");
		}
		
		Query sqlQuery = entityManager.createQuery(sql.toString());
		sqlQuery.setParameter("codigoDealFiscal", dealFiscal.getCodigoDealFiscal());
		
		if (startDate != null) {
			sqlQuery.setParameter("startDate", startDate, TemporalType.DATE);			
		}
		
		if (endDate != null) {
			sqlQuery.setParameter("endDate", endDate, TemporalType.DATE);
		}
		
		List<ReceitaDealFiscal> list = sqlQuery.getResultList();
		
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IReceitaDealFiscalDao#findByDealFiscal
	 * (java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ReceitaDealFiscal> findByDealFiscal(final Long codigoDealFiscal) {
		List<ReceitaDealFiscal> listResult = getJpaTemplate().findByNamedQuery(
				ReceitaDealFiscal.FIND_BY_DEAL_FISCAL,
				new Object[] { codigoDealFiscal });
		return listResult;
	}
}
