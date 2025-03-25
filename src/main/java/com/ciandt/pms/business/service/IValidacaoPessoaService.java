package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.*;

import com.ciandt.pms.model.vo.ClosingDREPessoaVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;


/**
 * 
 * A classe IValidacaoPessoaService proporciona a interface de acesso a camada
 * de serviço referente as funcionalidades de validação de uma pessoa.
 * 
 * @since 08/12/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IValidacaoPessoaService {

    /**
     * Retorna uma lista com todos os gerenciados, referente a pessoa logada no
     * sistema.
     * 
     * @param dataMes
     *            - mes da busca.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    List<ValidacaoPessoaRow> findAllMyManaged(final Date dataMes);
    
    /**
     * Retorna uma lista com todos os delegados, referente a pessoa logada no
     * sistema.
     * 
     * @param dataMes
     *            - mes da busca.
     * 
     * @return retorna uma lista com todos os recursos delegados.
     */
    List<ValidacaoPessoaRow> findAllMyDelegated(final Date dataMes);

    /**
     * Retorna uma lista com todos os gerenciados, referente ao login passado
     * por parametro.
     * 
     * @param loginMgr
     *            - login do gerente.
     * @param dataMes
     *            - mes da busca.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    List<ValidacaoPessoaRow> findByManagerAndDataMes(final String loginMgr,
            final Date dataMes);

    /**
     * Retorna uma lista com as AlocacaoPeriodo de uma pessoa referente ao
     * primeiro mes não fechado.
     * 
     * @param p
     *            - do tipo Pessoa.
     * @param dataMes
     *            - mes da busca.
     * 
     * @return Lista de AlocacaoPeriodo.
     * 
     */
    List<AlocacaoPeriodo> getAlocacaoPeriodoList(final Pessoa p,
            final Date dataMes);

    /**
     * Realiza a validacao de uma pessoa.
     * 
     * @param alocacaoPeriodoList
     *            - lista de aloccações periodo de uma pessoa.
     * @param pessoa
     *            - entidade Pessoa que será validada.
     * @param dataMes
     *            - mes da validacao.
     * 
     * @return true se validacao ok, caso contrario retorna false
     */
    @Transactional
    Boolean validatePessoa(final Pessoa pessoa,
            final List<AlocacaoPeriodo> alocacaoPeriodoList, final Date dataMes);

    /**
     * Cria um objeto AlocacaoPeriodo associado a uma Alocacao.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa.
     * @param mapa
     *            - entidade do tipo MapaAlocacao.
     * @param perfilVendido
     *            - entidade do tipo PerfilVendido.
     * @param percentualAlocacao
     *            - percentual alocado.
     * @param dataMes
     *            - mes corrente.
     * @return retorna a AlocacaoPeriodo com a Alocacao associada.
     */
    @Transactional
    AlocacaoPeriodo addAlocacao(final Pessoa pessoa, final MapaAlocacao mapa,
            final PerfilVendido perfilVendido,
            final BigDecimal percentualAlocacao, final Date dataMes);

    /**
     * Remove a validacao da pessoa do mes corrente do sistema.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa.
     * @param dataMes
     *            - mes da remocao.
     */
    void removeValidate(final Pessoa pessoa, final Date dataMes);

    /**
     * Retorna uma lista com as AlocacaoPeriodo de uma pessoa referente ao
     * primeiro mes não fechado.
     * 
     * @param p
     *            - do tipo Pessoa.
     * @param dataMes
     *            - mes da busca.
     * 
     * @return Lista de AlocacaoPeriodo.
     * 
     */
    List<AlocacaoPeriodoOh> getAlocacaoPeriodoOhList(final Pessoa p,
            final Date dataMes);

    /**
     * Verifica CustoRealizado e/ou CustoTceGrupoCusto para a Pessoa e o mes
     * passado por parametro.
     * 
     * @param pessoa
     *            - pessoa que deseja verificar se existe validacao
     * @param dataMes
     *            - mes que deseja verificar se existe validacao
     * 
     * @return true existe validacao para o mes, caso contrario retorna false
     */
    Boolean hasValidationForTheMonth(final Pessoa pessoa, final Date dataMes);
    
	/**
	 * Registra o objeto observer (que fica "escutando" qualquer atualização no
	 * percentual de conclusão do processo)
	 * 
	 * @param obs
	 *            Objeto que ficara observando
	 */
	void registerObserver(Observer obs);

    List<ClosingDREPessoaVO> loadPeopleInformation(List<Pessoa> people, Date month);

    @Transactional
    Boolean validatePeople(final ClosingDREPessoaVO pessoa, final Date month);

    List<Pessoa> findPessoaByManagerAndDataMes(final String loginMgr, final Date dataMes);

    Long countPessoaByDataMes(final Date dataMes);
}