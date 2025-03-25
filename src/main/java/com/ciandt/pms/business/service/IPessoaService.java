package com.ciandt.pms.business.service;

import com.ciandt.pms.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 
 * A classe IPessoaService proporciona a interface de acesso para o serviços
 * referentes a entidade Pessoa.
 * 
 * @since 23/09/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IPessoaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se criado com sucesso, caso contrario retorna false
	 */
	@Transactional
	Boolean createPessoa(final Pessoa entity);

	/**
	 * Executa um update na entidade passada por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	@Transactional
	Boolean updatePessoa(final Pessoa entity);

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
	@Transactional
	Boolean updatePessoaAuthority(final Pessoa pessoa,
			final Set<PerfilSistema> authorityList);

	@Transactional
	Boolean updatePessoaAreaOrcamentaria(final Pessoa pessoa,
								 final Set<AreaOrcamentaria> areas);

	@Transactional
	Boolean updatePessoaEmpresaOrcamentaria(final Pessoa pessoa, final Set<Empresa> areas);
	/**
	 * Realiza a inativação do recurso/pessoa.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void inactivePessoa(final Pessoa entity);

	/**
	 * Realiza a ativação do recurso/pessoa.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void activePessoa(final Pessoa entity);

	/**
	 * Remove a entidade passada por parametro, exclusao na tela de abas,
	 * verifica as Alocacao e remove os ItemTabelaPreco.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @return retorna true se sucesso senao false
	 */
	@Transactional
	boolean removePessoa(final Pessoa entity);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	Pessoa findPessoaById(final Long entityId);

	/**
	 * Busca a pessoa pelo código do recurso.
	 * 
	 * @param recurso
	 *            que será utilizado na busca por Pessoa
	 * 
	 * @return Pessoa
	 */
	Pessoa findPessoaByRecurso(final Recurso recurso);

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
	Pessoa findPessoaByRecursoTipo(final Recurso recurso, final String tipo);

	/**
	 * Retorna todas as Pessoa.
	 * 
	 * @return retorna uma lista com todos as Pessoa.
	 */
	List<Pessoa> findPessoaAll();

	/**
	 * Retorna todas os recursos ativos na época da dataMes.
	 * 
	 * @param dataMes
	 *            - data Mês a ser considerado na busca
	 * 
	 * @return retorna uma lista com os recursos que atendem o filtro.
	 */
	List<Pessoa> findPessoaAllActiveByDate(final Date dataMes);

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
	List<Pessoa> findPessoaByLoginMgrAndDate(final String loginMgr,
			final Date dataMes);

	/**
	 * Retorna as pessoas nao validadas do mes passado por parametro.
	 * 
	 * @param dataMes
	 *            - data mes que se deseja pegar os gerenciados.
	 * 
	 * @return retorna uma lista com todos os recursos nao validados.
	 */
	List<Pessoa> findPessoaNotValidatedByDate(final Date dataMes);

	Long countPessoaNotValidatedByDate(final Date dataMes);

	/**
	 * Retorna todas as pessoas que são gerente.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	List<Pessoa> findAllManager();

	/**
	 * Retorna todas as pessoas que são Business Manager.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	List<Pessoa> findPessoaAllAccountExecutive();

	/**
	 * Retorna todas as pessoas que são Business Manager.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	List<Pessoa> findPessoaAllBusinessManager();

	/**
	 * Retorna uma lista de pessoas.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista com todos as pessoas.
	 */
	List<Pessoa> findPessoaByLikeLogin(final String login);

	/**
	 * Retorna uma lista de pessoas. Ativas e Inativas.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista com todos as pessoas.
	 */
	List<Pessoa> findPessoaByLikeLoginAll(final String login);

	/**
	 * Retorna uma lista dos AEs que o login bate com o passado por parametro.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma lista de pessoas.
	 */
	List<Pessoa> findAccountExecutiveByLikeLogin(final String login);

	/**
	 * Retorna a pessoa referente ao login passado por parametro.
	 * 
	 * @param login
	 *            - login do usuário.
	 * 
	 * @return retorna uma entidade Pessoa.
	 */
	Pessoa findPessoaByLogin(final String login);

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
	List<Pessoa> findPessoaByFilter(final Pessoa pessoa,
			final Boolean isActiveOnly, final Boolean isFollowingOnly);

	/**
	 * @param codigoMapaAlocPessoa
	 *            - codigo do MapaAlocacaoPessoa
	 * 
	 * @return Retorna a Pessoa Follower referente ao MapaAlocacaoPessoa.
	 */
	Pessoa findFlwerByMapaAlocacaoPessoa(final Long codigoMapaAlocPessoa);

	/**
	 * @param codigoPessoaPessoa
	 *            - codigo do PessoaPessoa
	 * 
	 * @return Retorna a Pessoa Follower referente ao PessoaPessoa.
	 */
	Pessoa findFlwerByPessoaPessoa(final Long codigoPessoaPessoa);

	/**
	 * Busca a Pessoa segundo parametro codigoMD5.
	 * 
	 * @param codigoMD5
	 *            - chave encriptada para usar pelo Unsubscribe do Follow
	 * 
	 * @return Pessoa
	 */
	Pessoa findPessByCodigoMD5(final String codigoMD5);

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
	List<Pessoa> findPessByTiRecursoAndPeriod(final TiRecurso tiRecurso,
			final Date startDate, final Date endDate);

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
	List<Pessoa> findPessoaByMapaAlocacaoAndMesValidado(
			final MapaAlocacao mapa, final Date mesValidado);

	/**
	 * Retorna todas as pessoas com que possuem permissão de acesso ao PMS
	 * 
	 * @return
	 */
	List<Pessoa> quickSearchPMSUser(final String value);

	/**
	 * Busca pessoa com perfil de Executive Manager, Senior Manager e Manager e
	 * login.
	 * 
	 * @return
	 */
	Pessoa findByLoginExecutiveSeniorManager(final String login);
	
	/**
	 * Retorna lista de {@link Pessoa} que são donas de um voucher aberto que
	 * foi criado antes de {@code date}.
	 * 
	 * @param date
	 * @return Pessoa
	 */
	List<Pessoa> findWithOpenVoucherBeforeDate(final Date date);

	/**
	 * Retorna lista de {@link Pessoa} que segue um {@link OrcamentoDespesa} com
	 * um ou mais Vouchers abertos que foram criados antes de {@code date}.
	 * 
	 * @param date
	 * @return Pessoas
	 */
	List<Pessoa> findFollowingOrcDespWithOpenVoucherBeforeDate(final Date date);

	/**
	 * Verifica se uma pessoa possui determinado perfil.
	 * 
	 * @param pessoa
	 * @param perfilSistema
	 * @return
	 */
	Boolean pessoaPertenceAoPerfil(final Pessoa pessoa,
			final PerfilSistema perfilSistema);

	/**
	 * Retorna os atributos de combo box para todas as pessoas do sistema.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	List<Pessoa> findAllForComboBox();

	/**
	 * Retorna os atributos de combo box para todas as pessoas do sistema que
	 * são gerentes.
	 * 
	 * @return retorna uma lista com todos os recursos.
	 */
	List<Pessoa> findAllManagerForComboBox();

    List<Pessoa> findPessoaByLoginIn(List<String> logins);
}