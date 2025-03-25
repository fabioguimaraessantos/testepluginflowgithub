package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.ciandt.pms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.persistence.dao.IPessoaDao;

/**
 * 
 * A classe PessoaDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade Pessoa.
 * 
 * @since 21/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class PessoaDao extends AbstractDao<Long, Pessoa> implements IPessoaDao {

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */

	@Autowired
	public PessoaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Pessoa.class);
		entityManager = factory.createEntityManager();
	}


	/**
	 * Busca a pessoa pelo código do recurso.
	 * 
	 * @param recurso
	 *            que será utilizado na busca por Pessoa
	 * 
	 * @return Pessoa
	 */
	@SuppressWarnings("unchecked")
	public Pessoa findByRecurso(final Recurso recurso) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_RECURSO,
				new Object[] { recurso.getCodigoRecurso() });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Busca a pessoa pelo código do recurso.
	 * 
	 * @param recurso
	 *            que será utilizado na busca por Pessoa
	 * @param tipo
	 *            tipo do Recurso
	 * 
	 * @return Pessoa
	 */
	@SuppressWarnings("unchecked")
	public Pessoa findByRecursoTipo(final Recurso recurso, final String tipo) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_RECURSO_TIPO,
				new Object[] { recurso.getCodigoRecurso(), tipo });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Retorna todas os recursos.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAll() {
		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ALL);

		return listResult;
	}
	
	/**
	 * Retorna todas os recursos.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAllReturnCodigoPessoaAndCodigoLogin() {
		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ALL_RETURN_CODIGO_PESSOA_AND_CODIGO_LOGIN);
		
		return listResult;
	}

	/**
	 * Retorna todas os recursos ativos na época da dataMes.
	 * 
	 * @param dataMes
	 *            - data Mês a ser considerado na busca
	 * 
	 * @return retorna uma lista com os recursos que atendem o filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAllActiveByDate(final Date dataMes) {
		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ALL_ACTIVE_BY_DATE, new Object[] { dataMes });

		return listResult;
	}

	/**
	 * Retorna as pessoas do gerente.
	 * 
	 * @param loginMgr
	 *            - Login do gerente de pessoa.
	 * 
	 * @param dataMes
	 *            - data mes que se deseja pegar os gerenciados.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findByLoginMgrAndDate(final String loginMgr,
			final Date dataMes) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_LOGIN_MGR_AND_DATE,
				new Object[] { loginMgr, loginMgr, dataMes, dataMes });

		return listResult;
	}

	/**
	 * Retorna as pessoas nao validadas do mes passado por parametro.
	 * 
	 * @param dataMes
	 *            - data mes que se deseja pegar os gerenciados.
	 * 
	 * @return retorna uma lista com todos os recursos nao validados.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findNotValidatedByDate(final Date dataMes) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_NOT_VALIDATED_BY_DATE,
				new Object[] { dataMes, dataMes, dataMes });

		return listResult;
	}

	/**
	 * Retorna quantidade de pessoas não validadas no Mês passado por parâmetro
	 *
	 * @param dataMes
	 *            - data mes.
	 *
	 * @return retorna contador dos recursos nao validados.
	 */
	public Long countNotValidatedByDate(final Date dataMes) {

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("dataMes", dataMes);
		List<Long> resultants = getJpaTemplate().findByNamedQueryAndNamedParams(Pessoa.COUNT_NOT_VALIDATED_BY_DATE, parametros);
		return resultants.get(0);

	}

	/**
	 * Retorna todas as pessoas que são gerente.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAllManager() {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ALL_MGR);

		return listResult;
	}
	
	/**
	 * Retorna todas as pessoas que são gerente.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAllManagerReturnCodigoPessoaAndCodigoLogin() {
		return getJpaTemplate().findByNamedQuery(Pessoa.FIND_BY_POSITION_MAP_IN);
	}

	@Override
	public List<Pessoa> findByLoginIn(List<String> logins) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("logins", logins);
		return getJpaTemplate().findByNamedQueryAndNamedParams(Pessoa.FIND_BY_LOGIN_IN, params);
	}

	/**
	 * Retorna todas as pessoas que são Business Manager.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAllBusinessManager() {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ALL_BUSINESS_MANAGER);

		return listResult;
	}

	/**
	 * Retorna todas as pessoas que são Business Manager.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAllAccountExecutive() {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ALL_ACCOUNT_EXECUTIVE);

		return listResult;
	}

	/**
	 * Retorna uma lista de pessoas.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista com todos as pessoas.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findByLikeLogin(final String login) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_LIKE_LOGIN, new Object[] { login, "A" });

		return listResult;
	}

	/**
	 * Retorna uma lista de pessoas. Ativas e Inativas.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista com todos as pessoas.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findByLikeLoginAll(final String login) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_LIKE_LOGIN, new Object[] { login, null });

		return listResult;
	}

	/**
	 * Retorna uma lista dos AEs que o login bate com o passado por parametro.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findAccountExecutiveByLikeLogin(final String login) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_ACCOUNT_EXECUTIVE_BY_LIKE_LOGIN,
				new Object[] { login });

		return listResult;
	}

	/**
	 * Retorna a pessoa referente ao login passado por parametro.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma entidade Pessoa.
	 */
	@SuppressWarnings("unchecked")
	public Pessoa findByLogin(final String login) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_LOGIN, new Object[] { login });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Retorna uma lista de pessoas que atende ao criterio do filtro.
	 * 
	 * @param pessoa
	 *            - Objeto Pessoa que será utilizada como filtro.
	 * @param isActiveOnly
	 *            - flag somente avito.
	 * 
	 * @return retorna uma lista de Pessoa.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findByFilter(final Pessoa pessoa,
			final Boolean isActiveOnly) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				(isActiveOnly ? Pessoa.FIND_BY_FILTER_ACTIVE_ONLY
						: Pessoa.FIND_BY_FILTER),
				new Object[] { pessoa.getCodigoLogin(),
						pessoa.getCodigoLogin(), pessoa.getCodigoLoginMgr(),
						pessoa.getCodigoLoginMgr() });

		return listResult;
	}

	/**
	 * Retorna uma lista de pessoas que atende ao criterio do filtro.
	 * 
	 * @param pessoa
	 *            - Objeto Pessoa que será utilizada como filtro.
	 * @param pessoaFlwer
	 *            - follower
	 * 
	 * @return retorna uma lista de Pessoa.
	 */
	@SuppressWarnings("unchecked")
	public List<Pessoa> findByFilterFollowingOnly(final Pessoa pessoa,
			final Pessoa pessoaFlwer) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_FILTER_FOLLOWING_ONLY,
				new Object[] { pessoa.getCodigoLogin(),
						pessoa.getCodigoLogin(), pessoa.getCodigoLoginMgr(),
						pessoa.getCodigoLoginMgr(),
						pessoaFlwer.getCodigoPessoa() });

		return listResult;
	}

	/**
	 * @param codigoMapaAlocPessoa
	 *            - codigo do MapaAlocacaoPessoa
	 * 
	 * @return Retorna a Pessoa Follower referente ao MapaAlocacaoPessoa.
	 */
	@SuppressWarnings("unchecked")
	public Pessoa findFlwerByMapaAlocacaoPessoa(final Long codigoMapaAlocPessoa) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_FLWER_BY_MAPA_ALOCACAO_PESSOA,
				new Object[] { codigoMapaAlocPessoa });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * @param codigoPessoaPessoa
	 *            - codigo da PessoaPessoa
	 * 
	 * @return Retorna a Pessoa Follower referente ao PessoaPessoa.
	 */
	@SuppressWarnings("unchecked")
	public Pessoa findFlwerByPessoaPessoa(final Long codigoPessoaPessoa) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_FLWER_BY_PESSOA_PESSOA,
				new Object[] { codigoPessoaPessoa });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Busca a Pessoa segundo parametro codigoMD5.
	 * 
	 * @param codigoMD5
	 *            - chave encriptada para usar pelo Unsubscribe do Follow
	 * 
	 * @return Pessoa
	 */
	@SuppressWarnings("unchecked")
	public Pessoa findByCodigoMD5(final String codigoMD5) {
		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_CODIGO_MD5, new Object[] { codigoMD5 });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Retorna todos as Pessoas relacionados com o TiRecurso e dentro de um
	 * periodo.
	 * 
	 * @param tiRecurso
	 *            - TiRecurso que se deseja buscar as Pessoas
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
	public List<Pessoa> findByTiRecursoAndPeriod(final TiRecurso tiRecurso,
			final Date startDate, final Date endDate) {

		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_TIRECURSO_AND_PERIOD,
				new Object[] { tiRecurso.getCodigoTiRecurso(), startDate,
						endDate });

		return listResult;
	}

	/**
	 * R Retorna todas as Pessoas associadas a um determinado mapa e que possuem
	 * data de validacao maior ou igual ao periodo informado.
	 * 
	 * @param mapa
	 *            - mapa alocacao
	 * 
	 * @param mesValidado
	 *            - mes referencia
	 * 
	 * @return retorna uma lista de Pessoas
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoa> findByMapaAlocacaoAndMesValidado(
			final MapaAlocacao mapa, final Date mesValidado) {

		List<Pessoa> result = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_MAPA_ALOCACAO_AND_MES_VALIDADO,
				new Object[] { mapa.getCodigoMapaAlocacao(), mesValidado });

		if (result != null && !result.isEmpty()) {
			return result;
		}

		return null;
	}

	/**
	 * Retorna todas as pessoas com que possuem permissão de acesso ao PMS
	 * 
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoa> quickSearchPMSUser(final String value) {
		List<Pessoa> result = getJpaTemplate().findByNamedQuery(
				Pessoa.QUICK_SEARCH_PMS_USER,
				new Object[] { value });

		return result;
	}

	/**
	 * Busca pessoa com perfil de Executive Manager, Senior Manager e
	 * Manager e login.
	 * 
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Pessoa findByLoginExecutiveSeniorManager(final String login) {
		List<Pessoa> listResult = getJpaTemplate().findByNamedQuery(
				Pessoa.FIND_BY_LOGIN_EXECUTIVE_SENIOR_MANAGER,
				new Object[] { login });

		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Retorna lista de {@link Pessoa} que são donas de um voucher aberto que
	 * foi criado antes de {@code date}.
	 * 
	 * @param date
	 * @return Pessoa
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoa> findWithOpenVoucherBeforeDate(final Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataCreation", date);
		
		List<Pessoa> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Pessoa.FIND_WITH_OPEN_VOUCHER_BEFORE_DATE, params);

		return listResult;
	}


	/**
	 * Retorna lista de {@link Pessoa} que segue um {@link OrcamentoDespesa} com
	 * um ou mais Vouchers abertos que foram criados antes de {@code date}.
	 * 
	 * @param date
	 * @return Pessoas
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Pessoa> findFollowingOrcDespWithOpenVoucherBeforeDate(final Date date) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataCreation", date);
		
		List<Pessoa> listResult = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Pessoa.FIND_FOLLOWING_ORC_DESP_WITH_OPEN_VOUCHER_BEFORE_DATE, params);

		return listResult;
	}
}