package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.persistence.dao.IConvergenciaDao;

/**
 * 
 * A classe {@link ConvergenciaDao} proporciona as funcionalidades de acesso ao
 * banco de dadso para a entidade {@link Convergencia}.
 * 
 * @since Aug 15, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class ConvergenciaDao extends AbstractDao<Long, Convergencia> implements
		IConvergenciaDao {

	private EntityManager entityManager;

	/**
	 * Contrutor padrao.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public ConvergenciaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Convergencia.class);
		entityManager = factory.createEntityManager();
	}

	/**
	 * Busca todas as {@link Convergencia}.
	 * 
	 * @return lista do tipo {@link Convergencia}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Convergencia> findAll() {
		return getJpaTemplate().findByNamedQuery(Convergencia.FIND_ALL);
	}

	/*
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#
	 * findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convergencia> findInativeProjectByFormFilter(FormFilter filter) {
		return getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.FIND_INACTIVE_PROJECTS_BY_FORM_FILTER,
				filter.toMap());
	}
	
	/*
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#
	 * findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convergencia> findActiveProjectByFormFilter(FormFilter filter) {

		try {
			Query q = entityManager.createNamedQuery(Convergencia.FIND_ACTIVE_PROJECTS_BY_FORM_FILTER);

			Map<String, ?> params = filter.toMap();

			for (Map.Entry<String, ?> entry : params.entrySet()) {
				q.setParameter(entry.getKey(), entry.getValue());
			}

			List<Object[]> resultset = q.getResultList();

			List<Convergencia> resultObject = new ArrayList<Convergencia>();

			for (Object[] map : resultset) {
				Convergencia convergencia = new Convergencia();
				convergencia.setCodigoConvergencia(((BigDecimal) map[0]).longValue());
				convergencia.setCodigoCentroCustoMega(map[1] != null ? ((BigDecimal) map[1]).longValue() : null);
				convergencia.setSiglaTipo((String) map[2]);
				Empresa empresa = new Empresa(null, (String)map[9]);
				convergencia.setGrupoCusto( new GrupoCusto(((BigDecimal) map[3]).longValue(), (String) map[4], empresa));
				Long codigoContratoPratica = map[5] != null ? ((BigDecimal) map[5]).longValue() : null;
				String nomeContratoPratica = map[6] != null ? (String) map[6] : null;
				Cliente cliente = new Cliente(null, (String)map[10]);
				Msa msa = new Msa(null, null, cliente);
				convergencia.setContratoPratica(new ContratoPratica(codigoContratoPratica, nomeContratoPratica, msa));
				convergencia.setCodigoProjetoMega(map[7] != null ? ((BigDecimal) map[7]).longValue() : null);
				convergencia.setCodigoPadraoProjeto(map[8] != null ? ((BigDecimal) map[8]).longValue() : null);
				convergencia.setNomeProjetoMega(map[11] != null ? (String) map[11] : null);
				resultObject.add(convergencia);
			}

			return resultObject;

		} catch (HibernateException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	/*
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#
	 * findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Convergencia> findAllProjectByFormFilter(FormFilter filter) {
		return getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.FIND_ALL_PROJECTS_BY_FORM_FILTER,
				filter.toMap());
	}

	/* 
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#findByContratoPraticaId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Convergencia findByContratoPraticaId(Long idContratoPratica) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("contratoPratica", idContratoPratica);
		List<Convergencia> list = getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.FIND_PROJECT_BY_CONTRATO_PRATICA, map);
		if(list.isEmpty()) {
			return new Convergencia();
		}
		return list.get(0);
	}
	/* 
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#hasProjectToActivate(com.ciandt.pms.model.GrupoCusto)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean hasProjectToActivate(GrupoCusto grupoCusto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grupoCusto", grupoCusto.getCodigoGrupoCusto());
		List<Convergencia> list = getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.HAS_PROJECT_TO_ACTIVATE, map);
		if(list.isEmpty()) {
			return false;
		}
		return true;
	}

	/* 
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#findByCostGroupId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Convergencia findByCostGroupId(Long codigoGrupoCusto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grupoCusto", codigoGrupoCusto);
		List<Convergencia> list = getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.FIND_PROJECT_BY_COST_GROUP, map);
		if(list.isEmpty()) {
			return new Convergencia();
		}
		return list.get(0);
	}
	
	/* 
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#findByCLCostGroupId(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Convergencia> findByCLCostGroupId(Long codigoGrupoCusto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("grupoCusto", codigoGrupoCusto);
		List<Convergencia> list = getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.FIND_PROJECT_BY_CL_COST_GROUP, map);
		return list;
	}

	/* 
	 * @see com.ciandt.pms.persistence.dao.IConvergenciaDao#findConvergenciaByProjeto(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Convergencia> findConvergenciaByProjeto(Long codigoProjetoMega) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("codigoProjetoMega", codigoProjetoMega);
		List<Convergencia> list = getJpaTemplate().findByNamedQueryAndNamedParams(
				Convergencia.FIND_PROJECT_BY_CD_PROJECT, map);
		return list;
	}

}