package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.ComissaoRow;

/**
 * 
 * A classe IComissaoService proporciona a interface de acesso para a camada de
 * serviço referente as comissões.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IComissaoService {

	/**
	 * Cria o uma comissao de acelerador.
	 * 
	 * @param cAcelerador
	 *            - entidade ComissaoAcelerador.
	 * 
	 * @return retorna true se criado com sucesso, caso contrario retorna false.
	 */
	@Transactional
	Boolean createComissaoAcelerador(final ComissaoAcelerador cAcelerador);

	/**
	 * Cria o uma comissao de fatura.
	 * 
	 * @param cFatura
	 *            - entidade ComissaoFatura.
	 * @param comments
	 *            - Comentário
	 * 
	 * @return retorna true se criado com sucesso, caso contrario retorna false.
	 */
	@Transactional
	Boolean createComissaoFatura(final ComissaoFatura cFatura,
			final String comments);

	/**
	 * Realiza o update de uma ComissaoAcelerador.
	 * 
	 * @param comissaoAcelerador
	 *            - entidade do tipo ComissaoAcelerador.
	 * 
	 * @return retorna true se update realizado com sucesso, caso contrario
	 *         false.
	 */
	@Transactional
	Boolean updateComissaoAcelerador(final ComissaoAcelerador comissaoAcelerador);

	/**
	 * Realiza o update de uma ComissaoFatura.
	 * 
	 * @param comissaoFatura
	 *            - entidade do tipo ComissaoFatura.
	 * 
	 * @return retorna true se update realizado com sucesso, caso contrario
	 *         false.
	 */
	@Transactional
	Boolean updateComissaoFatura(final ComissaoFatura comissaoFatura);

	/**
	 * Deleta uma comissao acelerador.
	 * 
	 * @param comissaoAcelerador
	 *            - entidade a ser removida
	 * 
	 * @return retorna true se removida com sucesso, caso contrario false.
	 */
	@Transactional
	Boolean removeComissaoAcelerador(final ComissaoAcelerador comissaoAcelerador);

	/**
	 * Altera o status das comissões para status aprovado.
	 * 
	 * @param rowList
	 *            - Lista de comissões
	 * 
	 * @return retorna true se operação realizado com sucesso, caso contrario
	 *         false.
	 */
	@Transactional
	Boolean setAllComissaoAceleradorToApprove(final List<ComissaoRow> rowList);

	/**
	 * Altera o status.
	 * 
	 * @param rowList
	 *            - Lista de ComissaoRow
	 * @param status
	 *            - status da comissao
	 * @param comment
	 *            - comentário realizado na mudança de status
	 * 
	 * @return retorna true se alterado com sucesso, caso contrario false
	 */
	@Transactional
	Boolean changeStatus(final List<ComissaoRow> rowList, final String status,
			final String comment);

	/**
	 * Realiza o update do percentual de comissao.
	 * 
	 * @param comissao
	 *            - comissao a ser atualizada.
	 * @param valorTotal
	 *            - valor total referente a comissao.
	 */
	@Transactional
	void updatePercentualComissao(final Comissao comissao,
			final BigDecimal valorTotal);

	/**
	 * Busca pelo filtro uma comissao de acelerador.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param c
	 *            - cliente
	 * @param cp
	 *            - contrato pratica
	 * @param a
	 *            - acelerador
	 * @param ae
	 *            - Pessoa AE
	 * @param dn
	 *            - Pessoa DN
	 * @param status
	 *            - status da comissao.
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	List<ComissaoRow> findComissaoAceleradorByFilterPerAe(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Acelerador a, final Pessoa ae, final Pessoa dn,
			final String status);

	/**
	 * Busca pelo filtro uma comissao de acelerador pelo DN.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param c
	 *            - cliente
	 * @param cp
	 *            - contrato pratica
	 * @param a
	 *            - acelerador
	 * @param ae
	 *            - pessoa AE
	 * @param status
	 *            - status da comissao.
	 * @param dn
	 *            - pessoa DN
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	List<ComissaoRow> findComissaoAceleradorByFilterPerDn(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Acelerador a, final Pessoa ae, final String status,
			final Pessoa dn);

	/**
	 * Busca pelo filtro uma comissao de acelerador.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param ae
	 *            - AE
	 * @param status
	 *            - status da comissao.
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	List<Comissao> findComissaoByFilterDp(final Date startDate,
			final Date endDate, final Pessoa ae,
			final String status);

	/**
	 * Busca pelo filtro a comissão fatura por AE.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param status
	 *            - status
	 * @param loginAe
	 *            - login do AE
	 * @param loginDn
	 *            - login do DN
	 * 
	 * @return retorna uma lista de ComissaoRow, referente ao resultado da
	 *         busca.
	 */
	List<ComissaoRow> findComissaoFaturaByFilterAe(final Date startDate,
			final Date endDate, final String status, final String loginAe,
			final String loginDn, final Msa msa, final Long invoiceCode);

	/**
	 * Busca pelo filtro a comissão fatura por DN.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param c
	 *            - entidade do tipo Cliente
	 * @param cp
	 *            - entidade do tipo ContratoPratica
	 * @param ae
	 *            - pessoa AE
	 * @param status
	 *            - status
	 * @param dn
	 *            - pessoa DN
	 * 
	 * @return retorna uma lista de ComissaoRow, referente ao resultado da
	 *         busca.
	 */
	List<ComissaoRow> findComissaoFaturaByFilterDn(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Pessoa ae, final String status, final Pessoa dn);

	/**
	 * Retorna a entidade referente ao id passado por parametro.
	 * 
	 * @param id
	 *            - Id da entidade
	 * 
	 * @return retorna uma entidade do tipo ComissaoAcelerador.
	 */
	ComissaoAcelerador findComissaoAceleradorById(final Long id);

	/**
	 * Retorna a entidade referente ao id passado por parametro.
	 * 
	 * @param id
	 *            - Id da entidade
	 * 
	 * @return retorna uma entidade do tipo ComissaoFatura.
	 */
	ComissaoFatura findComissaoFaturaById(final Long id);

	/**
	 * Retorna a entidade referente ao id passado por parametro.
	 * 
	 * @param id
	 *            - Id da entidade
	 * 
	 * @return retorna uma entidade do tipo Comissao.
	 */
	Comissao findComissaoById(final Long id);

	/**
	 * Calcula o total acumulado.
	 * 
	 * @param ca
	 *            - entidade do tipo ComissaoAcelerador
	 * 
	 * @return retorna o total acumulado.
	 */
	BigDecimal calculateTotalAcumulado(final ComissaoAcelerador ca);

	/**
	 * Converte o valor da comissão para a moeda padrão do sistema.
	 * 
	 * @param comissao
	 *            - entidade do tipo Comissao.
	 * 
	 * @return retorna o valor calculado
	 */
	BigDecimal convertComissaoValueToDefaulCurrency(final Comissao comissao);

}
