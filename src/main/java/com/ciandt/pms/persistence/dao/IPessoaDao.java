package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.TiRecurso;


/**
 * 
 * A classe IPessoaDao proporciona a interface de acesso ao banco de dados para
 * a entidade Pessoa.
 * 
 * @since 21/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IPessoaDao extends IAbstractDao<Long, Pessoa> {

    /**
     * Busca a pessoa pelo código do recurso.
     * 
     * @param recurso
     *            que será utilizado na busca por Pessoa
     * 
     * @return Pessoa
     */
    Pessoa findByRecurso(final Recurso recurso);

    /**
     * Busca a pessoa pelo código do recurso.
     * 
     * @param recurso
     *            que será utilizado na busca por Pessoa
     * @param tipo
     *            tipo do recurso
     * 
     * @return Pessoa
     */
    Pessoa findByRecursoTipo(final Recurso recurso, final String tipo);

    /**
     * Retorna todas as Pessoa.
     * 
     * @return retorna uma lista com todos as Pessoa.
     */
    List<Pessoa> findAll();

    /**
     * Retorna todas os recursos ativos na época da dataMes.
     * 
     * @param dataMes
     *            - data Mês a ser considerado na busca
     * 
     * @return retorna uma lista com os recursos que atendem o filtro.
     */
    List<Pessoa> findAllActiveByDate(final Date dataMes);

    /**
     * Retorna as pessoas do gerente.
     * 
     * @param loginMgr
     *            - Login do gerente de pessoa.
     * 
     * @param dataMes
     *            - data mes que se deseja fazer pegar os gerenciados.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    List<Pessoa> findByLoginMgrAndDate(final String loginMgr, final Date dataMes);

    /**
     * Retorna as pessoas nao validadas do mes passado por parametro.
     * 
     * @param dataMes
     *            - data mes que se deseja pegar os gerenciados.
     * 
     * @return retorna uma lista com todos os recursos nao validados.
     */
    List<Pessoa> findNotValidatedByDate(final Date dataMes);

    /**
     * Retorna a quantidade de pessoas nao validadas do mes passado por parametro.
     *
     * @param dataMes
     *            - data mes que se deseja pegar os gerenciados.
     *
     * @return retorna a quantidade de recursos nao validados no mês.
     */
    Long countNotValidatedByDate(final Date dataMes);

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
    List<Pessoa> findAllBusinessManager();

    /**
     * Retorna todas as pessoas que são Business Manager.
     * 
     * @return retorna uma lista de pessoas.
     */
    List<Pessoa> findAllAccountExecutive();

    /**
     * Retorna uma lista de pessoas.
     * 
     * @param login
     *            - login do usuário.
     * 
     * @return retorna uma lista com todos as pessoas.
     */
    List<Pessoa> findByLikeLogin(final String login);

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
     * Retorna uma lista de pessoas. Ativas e Inativas.
     * 
     * @param login
     *            - login do usuário.
     * 
     * @return retorna uma lista com todos as pessoas.
     */
    List<Pessoa> findByLikeLoginAll(final String login);

    /**
     * Retorna a pessoa referente ao login passado por parametro.
     * 
     * @param login
     *            - login do usuário.
     * 
     * @return retorna uma entidade Pessoa.
     */
    Pessoa findByLogin(final String login);

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
    List<Pessoa> findByFilter(final Pessoa pessoa, final Boolean isActiveOnly);

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
    List<Pessoa> findByFilterFollowingOnly(final Pessoa pessoa,
            final Pessoa pessoaFlwer);

    /**
     * @param codigoMapaAlocPessoa
     *            - codigo do MapaAlocacaoPessoa
     * 
     * @return Retorna a Pessoa Follower referente ao MapaAlocacaoPessoa.
     */
    Pessoa findFlwerByMapaAlocacaoPessoa(final Long codigoMapaAlocPessoa);

    /**
     * @param codigoPessoaPessoa
     *            - codigo da PessoaPessoa
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
    Pessoa findByCodigoMD5(final String codigoMD5);

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
    List<Pessoa> findByTiRecursoAndPeriod(final TiRecurso tiRecurso,
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
    List<Pessoa> findByMapaAlocacaoAndMesValidado(final MapaAlocacao mapa,
            final Date mesValidado);
    

	/**
	 * Retorna todas as pessoas com que possuem permissão de acesso ao PMS
	 * 
	 * @param mapa
	 * @param mesValidado
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
	 * @return Pessoas
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

	List<Pessoa> findAllReturnCodigoPessoaAndCodigoLogin();

	List<Pessoa> findAllManagerReturnCodigoPessoaAndCodigoLogin();

    List<Pessoa> findByLoginIn(List<String> logins);
}