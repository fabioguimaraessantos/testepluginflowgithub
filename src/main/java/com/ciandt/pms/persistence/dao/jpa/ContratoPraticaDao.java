package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;
import com.ciandt.pms.persistence.dao.IContratoPraticaDao;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * A classe ContratoPraticaDao proporciona as funcionalidades de acesso ao banco
 * de dados para a entidade ContratoPratica.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ContratoPraticaDao extends AbstractDao<Long, ContratoPratica>
		implements IContratoPraticaDao {

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/**
	 * Construtor padr�o.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ContratoPraticaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ContratoPratica.class);

		entityManager = factory.createEntityManager();
	}

	/**
	 * Busca por todos os ContratoPraticas.
	 * 
	 * @return uma lista com todos os ContratoPraticas.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findAll() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL);

		return listResult;
	}

	public List<ContratoPratica> findAllForComboBox() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL_FOR_COMBOBOX);

		return listResult;
	}

	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findAllCompleteForCombobox() {
		
		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL_COMPLETE_FOR_COMBOBOX);
		
		return listResult;
	}

	/**
	 * Busca por todos os ContratoPraticas completos.
	 * 
	 * @return uma lista com todos os ContratoPraticas.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findAllComplete() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL_COMPLETE);

		return listResult;
	}

	/**
	 * Busca por todos os ContratoPraticas Ativas.
	 *
	 * @return uma lista com todos os ContratoPraticas Ativas.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findAllContratoPraticaActive() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL_ACTIVE);

		return listResult;
	}

	public List<ContratoPratica> findByAproverRescinded() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_APPROVER_RESCINDED);

		return listResult;
	}


	/**
	 * Busca o ContratoPratica por Contrato e Pratica.
	 * 
	 * @param idPratica
	 *            id da entidade pratica
	 * @param idMsa
	 *            id do entidade Msa
	 * 
	 * @return o ContratoPratica caso exista
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ContratoPratica findByPraticaAndMsa(final Long idPratica,
			final Long idMsa) {
		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_PRATICA_AND_CONTRATO,
				new Object[] { idPratica, idMsa });

		if (!listResult.isEmpty()) {
			return listResult.get(0);
		}
		return null;
	}

	public List<ContratoPratica> findByPratica(final Long idPratica) {
		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_PRATICA,
				idPratica);

		if (!listResult.isEmpty()) {
			return listResult;
		}
		return null;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * @param cli
	 *            entidade Cliente
	 * @param natureza
	 *            entidade Natureza
	 * @param cl
	 *            entidade CentroLucro
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findByFilterFetch(
			final ContratoPratica filter,
			final Cliente cli,
			final NaturezaCentroLucro natureza,
			final CentroLucro cl,
			final GrupoCusto grupoCusto,
			final List<String> indicadorWorkAtRiskFilter) {

		Long idMsa = filter.getMsa().getCodigoMsa();
		if (idMsa == null) {
			idMsa = Long.valueOf(0L);
		}

		Long idPratica = filter.getPratica().getCodigoPratica();
		if (idPratica == null) {
			idPratica = Long.valueOf(0L);
		}

		Long idCliente = cli.getCodigoCliente();
		if (idCliente == null) {
			idCliente = Long.valueOf(0L);
		}

		Long idNatureza = natureza.getCodigoNatureza();
		if (idNatureza == null) {
			idNatureza = Long.valueOf(0L);
		}

		Long idCentroLucro = cl.getCodigoCentroLucro();
		if (idCentroLucro == null) {
			idCentroLucro = Long.valueOf(0L);
		}

		Long idGrupoCusto = null;
		if (grupoCusto != null) {
			idGrupoCusto = grupoCusto.getCodigoGrupoCusto();
		}
		if(idGrupoCusto == null) {
			idGrupoCusto = Long.valueOf(0L);
		}

		Map<String, Object> params = new HashMap<>();
		params.put("nomeContratoPratica", filter.getNomeContratoPratica());
		params.put("idMsa", idMsa);
		params.put("idPratica", idPratica);
		params.put("indicadorStatus", filter.getIndicadorStatus());
		params.put("idCliente", idCliente);
		params.put("idCentroLucro", idCentroLucro);
		params.put("idNatureza", idNatureza);
		params.put("idGrupoCusto", idGrupoCusto);
		params.put("indicadorAtivo", filter.getIndicadorAtivo());
		params.put("indicadorWorkAtRiskFilter", indicadorWorkAtRiskFilter);

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(
				ContratoPratica.FIND_BY_FILTER_FETCH, params);

		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findByCliente(final Cliente cliente) {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_CLIENTE,
				new Object[] { cliente.getCodigoCliente() });

		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cl
	 *            - CentroLucro que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findByCentroLucro(final CentroLucro cl) {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_CENTRO_LUCRO, new Object[] { cl });

		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com o NaturezaCentroLucro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findByNatureza(
			final NaturezaCentroLucro natureza) {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_NATUREZA, new Object[] { natureza });

		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com o Msa passado por parametro.
	 * 
	 * @param msa
	 *            - Msa que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findByMsa(final Msa msa) {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_CONTRATO,
				new Object[] { msa.getCodigoMsa() });

		return listResult;
	}

	/**
	 * Retorna todas as entidades sem Fiscal Deal associado.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findSemFiscalDeal() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_SEM_FISCAL_DEAL);

		return listResult;
	}

	/**
	 * Busca uma lista de entidades que possuem MapaAlocacao.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findAllWithMapaAlocacao() {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL_WITH_MAPA_ALOCACAO);

		return listResult;
	}

	/**
	 * Retorna todos os ContratoPratica relacionados com o chargeback e dentro
	 * de um periodo.
	 * 
	 * @param tiRecurso
	 *            - TiRecurso que se deseja buscar os ContratoPraticas
	 * 
	 * @param startDate
	 *            - Data inicio do periodo.
	 * 
	 * @param endDate
	 *            - Data fim do periodo
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findByTiRecursoAndPeriod(
			final TiRecurso tiRecurso, final Date startDate, final Date endDate) {

		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_TIRECURSO_AND_PERIOD,
				new Object[] { tiRecurso.getCodigoTiRecurso(), startDate,
						endDate });

		return listResult;
	}

	/**
	 * Busca um contrato pratica pelo nome.
	 * 
	 * @param nomeContratoPratica
	 *            - nome do ContratoPratica
	 * @return {@link ContratoPratica}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ContratoPratica findByName(final String nomeContratoPratica) {
		List<ContratoPratica> cp = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_BY_NAME,
				new Object[] { nomeContratoPratica });

		if (cp.isEmpty()) {
			return null;
		}

		return cp.get(0);
	}

	/**
	 * Busca todos os clobs comepletos e ativos.
	 * 
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<ContratoPratica> findAllCompleteAndActive() {
		List<ContratoPratica> listResult = getJpaTemplate().findByNamedQuery(
				ContratoPratica.FIND_ALL_COMPLETE_AND_ACTIVE);
		return listResult;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ContratoPraticaAud> findHistoryByCodigo(
			Long codigoContratoPratica) {
		return getJpaTemplate().findByNamedQuery(ContratoPraticaAud.FIND_BY_CODIGO, codigoContratoPratica);
	}

	public List<String> findManagerOfContratoPraticaWithoutDealFiscal(){

		try {
			Query query = entityManager.createNamedQuery(ContratoPratica.FIND_MANAGER_CP_WITHOUT_DEAL_FISCAL);

			List  listResult = query.getResultList();
			List<String> listManagers = new ArrayList<String>();
			for(Object manager : listResult){
				listManagers.add((String)manager);
			}

			return listManagers;

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}

	public List<Map<String,String>> findContratoPraticaWithoutDealFiscal(String loginManager){
		try {
			Query query = entityManager.createNamedQuery(ContratoPratica.FIND_CP_WITHOUT_DEAL_FISCAL);

			query.setParameter("login",loginManager);

			List<Object[]>  listResult = query.getResultList();
			List<Map<String,String>> listClob = new ArrayList<Map<String,String>>();
			for(Object[] clob : listResult){
				Map<String, String> map = new HashMap<String, String>();
				map.put((String) clob[0], (String)clob[1].toString());
				listClob.add(map);
			}
			return listClob;

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}

	/**
	 * Busca todos os Mapas de Alocação com o status "Prospect" que possuem alocacao
	 * 	de algum recurso no mês corrente.
	 *
	 * @return {@link List} of {@link MapaProspectComAlocacaoResultsetVO}
	 */
	@Override
	public List<MapaProspectComAlocacaoResultsetVO> findProspectMapWithAllocation() {
		try {
			Query q = entityManager.createNamedQuery(ContratoPratica.FIND_PROSPECT_MAP_WITH_ALLOCATION);

			List<Object[]> resultset = q.getResultList();

			List<MapaProspectComAlocacaoResultsetVO> resultObject = new ArrayList<MapaProspectComAlocacaoResultsetVO>();

			for (Object[] map : resultset) {
				MapaProspectComAlocacaoResultsetVO objVo = new MapaProspectComAlocacaoResultsetVO();
				objVo.setNomeMapaAlocacao((String) map[0]);
				objVo.setLoginManagerApprover((String) map[1]);
				objVo.setLoginBusinessManager((String) map[2]);
				objVo.setTotalAlocacao(((BigDecimal) map[3]).doubleValue());

				resultObject.add(objVo);
			}

			return resultObject;

		} catch (HibernateException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public String findActualBusinessManagerContratoPratica(Long codigoContratoPratica){
		try {
			Query query = entityManager.createNamedQuery(ContratoPratica.FIND_ACTUAL_BUSINESS_MANAGER_CP);

			query.setParameter("contratoPratica",codigoContratoPratica);

			List listResult = query.getResultList();
			String emailManager = listResult.get(0).toString();

			return emailManager;

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
}