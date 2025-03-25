package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMapaAlocacaoPessoaService;
import com.ciandt.pms.business.service.IPessoaPessoaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IPessoaDao;
import com.ciandt.pms.util.LoginUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 
 * A classe PessoaService proporciona as funcionalidades de serviços referentes
 * a entidade Pessoa.
 * 
 * @since 23/09/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class PessoaService implements IPessoaService {

	/** Instancia do DAO da entidade Pessoa. */
	@Autowired
	private IPessoaDao pessoaDao;

	/** Instancia do Servico da entidade RecursoService. */
	@Autowired
	private IRecursoService recursoService;

	/** Instancia do Servico da entidade PessoaPessoaService. */
	@Autowired
	private IPessoaPessoaService pessPessService;

	/** Instancia do Servico da entidade MapaAlocacaoPessoaService. */
	@Autowired
	private IMapaAlocacaoPessoaService maPessService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se criado com sucesso, caso contrario retorna false
	 */
	public Boolean createPessoa(final Pessoa entity) {
		pessoaDao.create(entity);

		return Boolean.valueOf(true);
	}

	/**
	 * Executa um update na entidade passada por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	public Boolean updatePessoa(final Pessoa entity) {
		pessoaDao.update(entity);

		return Boolean.valueOf(true);
	}

	/**
	 * Realiza o update das pemissões de uma pessoa.
	 * 
	 * @param pessoa
	 *            - entidade Pessoa em questão
	 * @param authorityList
	 *            - Lista de PerfilSistema da pessoa
	 * 
	 * @return retorna true se update realizado com sucesso.
	 */
	public Boolean updatePessoaAuthority(final Pessoa pessoa,
			final Set<PerfilSistema> authorityList) {

		Pessoa p = findPessoaById(pessoa.getCodigoPessoa());
		Set<PerfilSistema> authorities = p.getPerfilSistemas();

		authorities.removeAll(CollectionUtils.subtract(authorities,
				authorityList));
		authorities.addAll(authorityList);

		updatePessoa(p);

		return true;
	}

	public Boolean updatePessoaAreaOrcamentaria(final Pessoa pessoa,
										 final Set<AreaOrcamentaria> areas) {

		Pessoa p = findPessoaById(pessoa.getCodigoPessoa());
		Set<AreaOrcamentaria> authorities = p.getAreasOrcamentarias();

		authorities.removeAll(CollectionUtils.subtract(authorities,
				areas));
		authorities.addAll(areas);

		updatePessoa(p);

		return true;
	}

	public Boolean updatePessoaEmpresaOrcamentaria(final Pessoa pessoa, final Set<Empresa> empresas) {

		Pessoa p = findPessoaById(pessoa.getCodigoPessoa());
		Set<Empresa> authorities = p.getEmpresasOrcamentarias();

		authorities.removeAll(CollectionUtils.subtract(authorities, empresas));
		authorities.addAll(empresas);

		updatePessoa(p);
		return true;
	}

	/**
	 * Realiza a inativação do recurso/pessoa.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	public void inactivePessoa(final Pessoa entity) {

		Pessoa pessoa = findPessoaById(entity.getCodigoPessoa());

		Recurso r = recursoService.findRecursoById(pessoa.getRecurso()
				.getCodigoRecurso());

		r.setIndicadorAtivo(Constants.INACTIVE);
		recursoService.updateRecurso(r);

		pessoa.setDataRescisao(entity.getDataRescisao());
		this.updatePessoa(pessoa);

	}

	/**
	 * Realiza a ativação do recurso/pessoa.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	public void activePessoa(final Pessoa entity) {
		Pessoa pessoa = findPessoaById(entity.getCodigoPessoa());

		Recurso r = recursoService.findRecursoById(pessoa.getRecurso()
				.getCodigoRecurso());

		r.setIndicadorAtivo(Constants.ACTIVE);
		recursoService.updateRecurso(r);

		pessoa.setDataRescisao(null);
		this.updatePessoa(pessoa);
	}

	/**
	 * Remove a entidade passada por parametro, exclusao na tela de abas,
	 * verifica as Alocacao e remove os ItemTabelaPreco.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @return retorna true se sucesso senao false
	 */
	public boolean removePessoa(final Pessoa entity) {
		Long codigoPessoa = entity.getCodigoPessoa();
		// remove a lista de PessoaPessoa - Pessoa corrente está sendo seguida
		List<PessoaPessoa> pessPessList = pessPessService
				.findPessPessByPessoa(codigoPessoa);
		for (PessoaPessoa pessPess : pessPessList) {
			pessPessService.unfollowPessoa(pessPess.getPessoa(),
					pessPess.getPessoaFlwer());
		}
		// remove a lista de PessoaPessoa - Pessoa corrente é follower, está
		// seguindo
		List<PessoaPessoa> pessPessListFlwer = pessPessService
				.findPessPessByPessoaFlwer(codigoPessoa);
		for (PessoaPessoa pessPess : pessPessListFlwer) {
			pessPessService.unfollowPessoa(pessPess.getPessoa(),
					pessPess.getPessoaFlwer());
		}

		// remove a lista de MapaAlocacaoPessoa da Pessoa.
		List<MapaAlocacaoPessoa> maPessList = entity.getMapaAlocacaoPessoas();
		for (MapaAlocacaoPessoa maPess : maPessList) {
			maPessService.unfollowMapaAlocacao(maPess.getMapaAlocacao(),
					maPess.getPessoa());
		}

		pessoaDao.remove(entity);

		return Boolean.valueOf(true);
	}

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	public Pessoa findPessoaById(final Long entityId) {
		return pessoaDao.findById(entityId);
	}

	/**
	 * Busca a pessoa pelo código do recurso.
	 * 
	 * @param recurso
	 *            que será utilizado na busca por Pessoa
	 * 
	 * @return Pessoa
	 */
	public Pessoa findPessoaByRecurso(final Recurso recurso) {
		return pessoaDao.findByRecurso(recurso);
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
	public Pessoa findPessoaByRecursoTipo(final Recurso recurso,
			final String tipo) {
		return pessoaDao.findByRecursoTipo(recurso, tipo);
	}

	/**
	 * Retorna todas as Pessoa.
	 * 
	 * @return retorna uma lista com todos as Pessoa.
	 */
	public List<Pessoa> findPessoaAll() {
		return pessoaDao.findAll();
	}

	/**
	 * Retorna todas os recursos ativos na época da dataMes.
	 * 
	 * @param dataMes
	 *            - data Mês a ser considerado na busca
	 * 
	 * @return retorna uma lista com os recursos que atendem o filtro.
	 */
	public List<Pessoa> findPessoaAllActiveByDate(final Date dataMes) {
		return pessoaDao.findAllActiveByDate(dataMes);
	}

	/**
	 * Retorna todas os recursos.
	 * 
	 * @param loginMgr
	 *            - Login do gerente de pessoa.
	 * 
	 * @param dataMes
	 *            - data mes que se deseja fazer pegar os gerenciados.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	public List<Pessoa> findPessoaByLoginMgrAndDate(final String loginMgr,
			final Date dataMes) {
		return pessoaDao.findByLoginMgrAndDate(loginMgr, dataMes);
	}

	/**
	 * Retorna as pessoas nao validadas do mes passado por parametro.
	 * 
	 * @param dataMes
	 *            - data mes que se deseja pegar os gerenciados.
	 * 
	 * @return retorna uma lista com todos os recursos nao validados.
	 */
	public List<Pessoa> findPessoaNotValidatedByDate(final Date dataMes) {
		return pessoaDao.findNotValidatedByDate(dataMes);
	}

	public Long countPessoaNotValidatedByDate(final Date dataMes) {
		return pessoaDao.countNotValidatedByDate(dataMes);
	}

	/**
	 * Retorna todas as pessoas que são gerente.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	public List<Pessoa> findAllManager() {
		return pessoaDao.findAllManager();
	}

	/**
	 * Retorna todas as pessoas que são Business Manager.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	public List<Pessoa> findPessoaAllAccountExecutive() {
		return pessoaDao.findAllAccountExecutive();
	}

	/**
	 * Retorna todas as pessoas que são Business Manager.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	public List<Pessoa> findPessoaAllBusinessManager() {
		return pessoaDao.findAllBusinessManager();
	}

	/**
	 * Retorna uma lista de pessoas.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista com todos as pessoas.
	 */
	public List<Pessoa> findPessoaByLikeLogin(final String login) {
		return pessoaDao.findByLikeLogin(login);
	}

	/**
	 * Retorna uma lista dos AEs que o login bate com o passado por parametro.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	public List<Pessoa> findAccountExecutiveByLikeLogin(final String login) {
		return pessoaDao.findAccountExecutiveByLikeLogin(login);
	}

	/**
	 * Retorna uma lista de pessoas. Ativas e Inativas.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista com todos as pessoas.
	 */
	public List<Pessoa> findPessoaByLikeLoginAll(final String login) {
		return pessoaDao.findByLikeLoginAll(login);
	}

	/**
	 * Retorna a pessoa referente ao login passado por parametro.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma entidade Pessoa.
	 */
	public Pessoa findPessoaByLogin(final String login) {
		return pessoaDao.findByLogin(login);
	}

	/**
	 * Retorna uma lista de pessoas que atende ao criterio do filtro.
	 * 
	 * @param pessoa
	 *            - Objeto Pessoa que será utilizada como filtro.
	 * @param isActiveOnly
	 *            - flag somente avito.
	 * @param isFollowingOnly
	 *            - flag somente Pessoa que a Pessoa corrente está seguindo.
	 * 
	 * @return retorna uma lista de Pessoa.
	 */
	public List<Pessoa> findPessoaByFilter(final Pessoa pessoa,
			final Boolean isActiveOnly, final Boolean isFollowingOnly) {
		// se Following Only, retorna a lista de Pessoas que o follower está
		// seguindo e depois verifica isActiveOnly
		if (isFollowingOnly) {
			List<Pessoa> pessListFlwOnly = pessoaDao.findByFilterFollowingOnly(
					pessoa, LoginUtil.getLoggedUser());

			if (isActiveOnly) {
				List<Pessoa> pessListFlwOnlyAux = new ArrayList<Pessoa>();
				for (Pessoa pess : pessListFlwOnly) {
					if ((Constants.ACTIVE).equals(pess.getRecurso()
							.getIndicadorAtivo())
							&& pess.getDataRescisao() == null) {
						pessListFlwOnlyAux.add(pess);
					}
				}
				return pessListFlwOnlyAux;
			} else {
				return pessListFlwOnly;
			}
			// se não Folling Only, faz a busca comum
		} else {
			return pessoaDao.findByFilter(pessoa, isActiveOnly);
		}
	}

	/**
	 * @param codigoMapaAlocPessoa
	 *            - codigo do MapaAlocacaoPessoa
	 * 
	 * @return Retorna a Pessoa Follower referente ao MapaAlocacaoPessoa.
	 */
	public Pessoa findFlwerByMapaAlocacaoPessoa(final Long codigoMapaAlocPessoa) {
		return pessoaDao.findFlwerByMapaAlocacaoPessoa(codigoMapaAlocPessoa);
	}

	/**
	 * @param codigoPessoaPessoa
	 *            - codigo do PessoaPessoa
	 * 
	 * @return Retorna a Pessoa Follower referente ao PessoaPessoa.
	 */
	public Pessoa findFlwerByPessoaPessoa(final Long codigoPessoaPessoa) {
		return pessoaDao.findFlwerByPessoaPessoa(codigoPessoaPessoa);
	}

	/**
	 * Busca a Pessoa segundo parametro codigoMD5.
	 * 
	 * @param codigoMD5
	 *            - chave encriptada para usar pelo Unsubscribe do Follow
	 * 
	 * @return Pessoa
	 */
	public Pessoa findPessByCodigoMD5(final String codigoMD5) {
		return pessoaDao.findByCodigoMD5(codigoMD5);
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
	public List<Pessoa> findPessByTiRecursoAndPeriod(final TiRecurso tiRecurso,
			final Date startDate, final Date endDate) {
		return pessoaDao
				.findByTiRecursoAndPeriod(tiRecurso, startDate, endDate);
	}

	/**
	 * Retorna todas as Pessoas associadas a um determinado mapa e que possuem
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
	public List<Pessoa> findPessoaByMapaAlocacaoAndMesValidado(
			final MapaAlocacao mapa, final Date mesValidado) {
		return pessoaDao.findByMapaAlocacaoAndMesValidado(mapa, mesValidado);
	}

	/**
	 * Retorna todas as pessoas com que possuem permissão de acesso ao PMS
	 * 
	 * @return
	 */
	@Override
	public List<Pessoa> quickSearchPMSUser(final String value) {
		return pessoaDao.quickSearchPMSUser(value);
	}

	/**
	 * Busca pessoa com perfil de Executive Manager, Senior Manager e Manager e
	 * login.
	 * 
	 * @return
	 */
	@Override
	public Pessoa findByLoginExecutiveSeniorManager(final String login) {
		return pessoaDao.findByLoginExecutiveSeniorManager(login);
	}

	/**
	 * Retorna lista de {@link Pessoa} que são donas de um voucher aberto que
	 * foi criado antes de {@code date}.
	 * 
	 * @param date
	 * @return Pessoa
	 */
	@Override
	public List<Pessoa> findWithOpenVoucherBeforeDate(final Date date) {
		return pessoaDao.findWithOpenVoucherBeforeDate(date);
	}

	/**
	 * Retorna lista de {@link Pessoa} que segue um {@link OrcamentoDespesa} com
	 * um ou mais Vouchers abertos que foram criados antes de {@code date}.
	 * 
	 * @param date
	 * @return Pessoas
	 */
	@Override
	public List<Pessoa> findFollowingOrcDespWithOpenVoucherBeforeDate(
			final Date date) {
		return pessoaDao.findFollowingOrcDespWithOpenVoucherBeforeDate(date);
	}

	/**
	 * Verifica se uma pessoa possui determinado perfil.
	 * 
	 * @param pessoa
	 * @param perfilSistema
	 * @return
	 */
	public Boolean pessoaPertenceAoPerfil(final Pessoa pessoa,
			final PerfilSistema perfilSistema) {
		List<PerfilSistema> listPerfilSistema = new ArrayList<PerfilSistema>(
				pessoa.getPerfilSistemas());
		boolean perfilReturn = false;
		for (PerfilSistema ps : listPerfilSistema) {
			if (ps.getCodigoPerfilSistema() == perfilSistema
					.getCodigoPerfilSistema()) {
				perfilReturn = true;
				break;
			}
		}
		return Boolean.valueOf(perfilReturn);
	}

	/**
	 * Retorna os atributos de combo box para todas as pessoas do sistema.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@Override
	public List<Pessoa> findAllForComboBox() {
		return pessoaDao.findAllReturnCodigoPessoaAndCodigoLogin();
	}
	
	/**
	 * Retorna os atributos de combo box para todas as pessoas do sistema que
	 * são gerentes.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	@Override
	public List<Pessoa> findAllManagerForComboBox() {
		return pessoaDao.findAllManagerReturnCodigoPessoaAndCodigoLogin();
	}

	@Override
	public List<Pessoa> findPessoaByLoginIn(List<String> logins) {
		return pessoaDao.findByLoginIn(logins);
	}
}