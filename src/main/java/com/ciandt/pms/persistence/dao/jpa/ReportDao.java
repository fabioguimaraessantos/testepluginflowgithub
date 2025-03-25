package com.ciandt.pms.persistence.dao.jpa;


import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.transaction.UserTransaction;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ReportRow;
import com.ciandt.pms.model.VwPmsContratoProfitCenter;
import com.ciandt.pms.persistence.dao.IReportDao;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ReportDao extends AbstractDao<Long, ReportRow> implements IReportDao {
	
	/** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public ReportDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ReportRow.class);

		entityManager = factory.createEntityManager();
	}
	
	/**
	 * Chama procedure que converte o valor de uma moeda para outra moeda.
	 * 
	 * @param dataValor
	 */
	@SuppressWarnings({ "unchecked" })
	public List<VwPmsContratoProfitCenter> getRevenueForecast(Date dataValor) {
		StringBuffer queryString = new StringBuffer();
		queryString.append("select cpc.CD_CONTRATO_PROFIT_CENTER, cpc.clie_nm_cliente, cpc.copr_nm_contrato_pratica, cpc.prat_nm_pratica, cpc.umkt, cpc.sso, cpc.business_manager, mr.dt_valor, mr.moed_sg_moeda, mr.vl_mapa, mr.vl_receita, mr.tipo_receita");
		queryString.append(" from pms20.VW_PMS_CONTRATO_PROFIT_CENTER cpc");
		queryString.append(" inner join pms20.VW_PMS_VALOR_MAPA_E_RECEITA mr on cpc.copr_cd_contrato_pratica = mr.copr_cd_contrato_pratica");
		queryString.append(" where mr.dt_valor = :dataValor");


		EntityManager entityManager = null;
		try {
			entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
			StatelessSession ss = ((Session) entityManager.getDelegate())
					.getSessionFactory().openStatelessSession();
			SQLQuery query = ss.createSQLQuery(queryString.toString());
			query.addEntity(VwPmsContratoProfitCenter.class);
			query.setParameter("dataValor", dataValor);

			List<VwPmsContratoProfitCenter> result = query.list();

			ss.close();

			if (result != null) {
				return result;
			}
		} finally {
			EntityManagerFactoryUtils.closeEntityManager(entityManager);
		}

		return null;
	}

	/**
	 * Este metodo realiza a chamada de integracao do Request Analisis com o
	 * sistema MEGA, importando as solicitacoes validadas.
	 */
	@Override
	@Transactional
	public void runProcPlannedCost(Long codigoContratoPratica) {

		String proc = new StringBuilder()
				.append("DECLARE result NUMBER; BEGIN pms20.usp_pms_forecast_calc_pln_cost(")
				.append(codigoContratoPratica).append(",result); END;").toString();

		EntityManager entityManager = null;
		try {
			entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
			EntityTransaction utx = entityManager.getTransaction();
			utx.begin();

			Query query = entityManager.createNativeQuery(proc);
			query.executeUpdate();

			utx.commit();
		} catch(Exception ex) {
			System.out.print(ex.getMessage());
		} finally {
			EntityManagerFactoryUtils.closeEntityManager(entityManager);
		}
	}
}
