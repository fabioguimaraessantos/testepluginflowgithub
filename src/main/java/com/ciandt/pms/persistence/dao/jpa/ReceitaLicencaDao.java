package com.ciandt.pms.persistence.dao.jpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.ciandt.pms.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.persistence.dao.IReceitaLicencaDao;

/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/08/2014
 */
@Repository
public class ReceitaLicencaDao extends AbstractDao<Long, ReceitaLicenca>
		implements IReceitaLicencaDao {

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * Construtor padrï¿½o.
	 * 
	 * @param factory
	 *            Entidade do tipo Atividade
	 */
	@Autowired
	public ReceitaLicencaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ReceitaLicenca.class);
		entityManager = factory.createEntityManager();
	}

	/**
	 * Busca uma {@link ReceitaLicenca} por todos os seus atributos.
	 * 
	 * @return Lista de {@link ReceitaLicenca}
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<ReceitaLicenca> findByDataMesAndContratoPratica(
			final Date dataMes, final ContratoPratica contratoPratica) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataMes", dataMes);
		params.put("codigoContratoPratica",
				contratoPratica.getCodigoContratoPratica());

		List<ReceitaLicenca> receitaLicencas = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ReceitaLicenca.FIND_BY_DATA_MES_AND_CONTRATO_PRATICA,
						params);

		return receitaLicencas;
	}

	public Boolean updateStatusReceitaLicenca(StatusReceitaLicenca statusReceitaLicenca) {
		Boolean isUpdated = Boolean.valueOf(false);

		EntityManager entityManager = null;
		try {

			entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			Query query = entityManager.createNamedQuery(ReceitaLicenca.UPDATE_STATUS_RECEITA_LICENCA);

			query.setParameter("revenueStatus", statusReceitaLicenca.getRevenueStatus());
			query.setParameter("revenueVersion", statusReceitaLicenca.getRevenueVersion());
			query.setParameter("errorMessage", statusReceitaLicenca.getErrorMessage());
			if (statusReceitaLicenca.isError()) {
				query.setParameter("megaOrderID", "");
			} else {
				query.setParameter("megaOrderID", statusReceitaLicenca.getMegaOrderID());
			}
			query.setParameter("revenueCode", statusReceitaLicenca.getRevenueCode());

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

	@SuppressWarnings("unchecked")
	@Override
	public List<ReceitaLicenca> findByCodigoPaiReceitaLicenca(
			final Long codigoPaiReceitaLicenca) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoPaiReceitaLicenca", codigoPaiReceitaLicenca);

		List<ReceitaLicenca> receitaLicencas = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ReceitaLicenca.FIND_BY_CODIGO_PAI_RECEITA_LICENCA,
						params);

		return receitaLicencas;
	}

	public List<ReceitaLicenca> findByFormFilter(
			final ReceitaLicenca receitaLicenca, final Cliente cliente,
			final CentroLucro centroLucro, final NaturezaCentroLucro natureza) {

		Long codigoCentroLucro = Long.valueOf(0);
		Long codigoNatureza = Long.valueOf(0);
		Long codigoCliente = Long.valueOf(0);
		Long codigoPratica = Long.valueOf(0);

		// pega o código do contrato-pratica
		Pratica pratica = receitaLicenca.getContratoPratica().getPratica();
		if (pratica != null) {
			codigoPratica = pratica.getCodigoPratica();
		}
		// pega o código do cliente
		if (cliente != null) {
			codigoCliente = cliente.getCodigoCliente();
		}
		// pega o código so centro-lucro
		if (centroLucro != null) {
			codigoCentroLucro = centroLucro.getCodigoCentroLucro();
		}
		// pega o código da natureza
		if (natureza != null) {
			codigoNatureza = natureza.getCodigoNatureza();
		}

		@SuppressWarnings("unchecked")
		List<ReceitaLicenca> receitaLicencas = getJpaTemplate()
				.findByNamedQuery(
						ReceitaLicenca.FIND_BY_FORM_FILTER,
						new Object[] { receitaLicenca.getDataMes(),
								codigoCentroLucro, codigoCentroLucro,
								codigoNatureza, codigoNatureza, codigoCliente,
								codigoCliente, codigoPratica, codigoPratica,
								receitaLicenca.getIndicadorVersao(),
								receitaLicenca.getIndicadorVersao(),
								codigoCentroLucro, receitaLicenca.getDataMes() });

		return receitaLicencas;
	}

	/**
	 * Obtem as receitas integraveis de acordo com o filtro informado.
	 * 
	 * @param filter
	 *            {@link FormFilter}
	 * @return lista de {@link ReceitaLicenca}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ReceitaLicenca> findIntegrableRevenueByFormFilter(
			final FormFilter filter) {
		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ReceitaLicenca.FIND_BY_INTEGRABLE_REVENUE, filter.toMap());
	}

	/**
	 * Executa a integracao entre a {@link ReceitaLicenca} e o ERP (Mega).
	 * 
	 * @param receitaLicenca
	 *            - {@link ReceitaLicenca}l a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execucao da integracao. Se retorno menor que
	 *         zero erro, caso contrario sucesso.
	 */
	@Override
	public Integer integrate(final ReceitaLicenca receitaLicenca) {
		Object result = null;
		try {
			Query query = entityManager
					.createNamedQuery(ReceitaLicenca.INTEGRATE_RECEITA_LICENCA);

			query.setParameter("codigoReceitaLicenca",
					receitaLicenca.getCodigoReceitaLicenca());

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
	 * Busca se o pedido foi cancelado no ERP.
	 * 
	 * @param receitaLicenca
	 *            entidade ReceitaDealFiscal.
	 * 
	 * @return retorna true se cancelado ou nao existe, caso contrario false.
	 *         Retorna null caso ocorra uma excetion ao executar a consulta.
	 * @throws SQLException
	 * @throws HibernateException
	 */
	@Override
	@SuppressWarnings({ "deprecation" })
	public Boolean isErpOrderCanceled(final ReceitaLicenca receitaLicenca)
			throws HibernateException, SQLException {
		Session session = (Session) entityManager.getDelegate();

		// realiza a consulta do peido no ERP
		String query = "SELECT p.ped_in_sequencia, p.ped_ch_situacao "
				+ " FROM ven_pedidovenda p  WHERE p.ped_in_sequencia = ? ";

		PreparedStatement st = session.connection().prepareStatement(query);

		st.setLong(1, receitaLicenca.getCodigoErpPedido());

		ResultSet rs = st.executeQuery();
		// pega o resultado
		if (rs.next()) {
			String canceledStatus = systemProperties
					.getProperty("erp.pedido.status.cancelado");

			String status = rs.getString(2);
			// verifica se o pedido esta cancelado
			return canceledStatus.equals(status);
		}

		// pedido nao encontrato
		return Boolean.TRUE;
	}
}