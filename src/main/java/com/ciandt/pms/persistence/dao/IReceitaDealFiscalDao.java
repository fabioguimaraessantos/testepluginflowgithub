package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.*;

/**
 * 
 * A classe IReceitaDealFiscalDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade ReceitaDealFiscal.
 * 
 * @since 04/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IReceitaDealFiscalDao extends
		IAbstractDao<Long, ReceitaDealFiscal> {

	/**
	 * Busca uma lista de entidades pela receita.
	 * 
	 * @param entity
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de entidades relacionadas com a receita passada por
	 *         parametro.
	 */
	List<ReceitaDealFiscal> findByReceita(Long codigoReceita);

	Long findNotIntegratedRevenues(final Long revenueCurrencyCode);

	/**
	 * Busca uma lista de entidades pelo contrato-pratica and data-mes.
	 * 
	 * @param idContratoPratica
	 *            id da entidade do tipo ContratoPratica.
	 * 
	 * @param dataMes
	 *            objeto do tipo Date.
	 * 
	 * @param status
	 *            status da receita.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	List<ReceitaDealFiscal> findByFilter(final Long idContratoPratica, final Long idEmpresa,
	final Date dataMes, final String status);

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param idContratoPratica
	 *            id da entidade do tipo ContratoPratica.
	 * 
	 * @param dataMes
	 *            objeto do tipo Date.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	List<ReceitaDealFiscal> findByContratoPraticaAndDataMesToAjuste(
			final Long idContratoPratica, final Date dataMes);

	/**
	 * Realiza a integração da receita referente ao id passado por parametro.
	 * Esta integração é realizada através da chamada de uma procedure.
	 * 
	 * @param idReceitaDeal
	 *            id da entidade ReceitaDealFiscal.
	 * 
	 * @return lista de entidades relacionadas com a ContratoPratica passada por
	 *         parametro.
	 */
	Integer integrateReceita(final Long idReceitaDeal);

	/**
	 * Realiza a busca da entidade, atravez dos criterios do filtro.
	 * 
	 * @param cp
	 *            - ContratoPratica
	 * @param cli
	 *            - Cliente
	 * @param cl
	 *            - CentroLucro
	 * @param natureza
	 *            - NaturezaCentroLucro
	 * @param dataMesInicio
	 *            - data mes inicio
	 * @param dataMesFim
	 *            - data mes fim
	 * 
	 * @return retorna uma lista de ReceitaDealFiscal
	 */
	List<ReceitaDealFiscal> findByFilter(final ContratoPratica cp,
			final Cliente cli, final CentroLucro cl,
			final NaturezaCentroLucro natureza, final Date dataMesInicio,
			final Date dataMesFim);

	/**
	 * Realiza a busca da entidade, atravez dos criterios do filtro.
	 * 
	 * @param cp
	 *            - ContratoPratica
	 * @param cli
	 *            - Cliente
	 * @param dataMesInicio
	 *            - data mes inicio
	 * @param dataMesFim
	 *            - data mes fim
	 * 
	 * @return retorna uma lista de ReceitaDealFiscal
	 */
	@Deprecated
	List<ReceitaDealFiscal> findByFilterSub(final ContratoPratica cp,
			final Cliente cli, final Date dataMesInicio, final Date dataMesFim,
			Receita receita, final DealFiscal dealFiscal);

	/**
	 * Pega o valor total da receita relacionado com o deal e a receita,
	 * passados por parametro.
	 * 
	 * @param receita
	 *            - Receita em questão
	 * @param deal
	 *            - DealFiscal em questão
	 * 
	 * @return retorna o valor total da receita
	 */
	BigDecimal getTotalReceitaByDealAndReceita(final Receita receita,
			final DealFiscal deal);

	/**
	 * Serviço que pega o valor total das receitas publicadas relacionado com o
	 * deal e data.
	 * 
	 * @param dealFiscal
	 *            Objeto {@link DealFiscal} Consulta pode ser feita pelo do
	 *            DealFiscal
	 * @param dataFim
	 *            Data da final para consulta de receitas
	 * 
	 * @return retorna o valor total da receita
	 */
	BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal dealFiscal, final Date dataFim);

	/**
	 * NOVA IMPLEMENTACAO - Integração Intercompany Verifica se o pedido foi
	 * cancelado no ERP(Mega). Para que possa ser desfeito a integração da
	 * Receita
	 * 
	 * @param rdf
	 *            entidade ReceitaDealFiscal.
	 * 
	 * @return retorna true se pedido cancelado, caso contrario false.
	 */
	/*
	 * Boolean isErpOrderCanceled(final ReceitaDealFiscal rdf, final BigDecimal
	 * erpPedido);
	 */

	/**
	 * IMPLEMENTAÇÃO ANTIGA Verifica se o pedido foi cancelado no ERP(Mega).
	 * Para que possa ser desfeito a integração da Receita
	 * 
	 * @param rdf
	 *            entidade ReceitaDealFiscal.
	 * 
	 * @return retorna true se pedido cancelado, caso contrario false.
	 */
	Boolean isErpOrderCanceled(final ReceitaDealFiscal rdf);

	/**
	 * Busca a Receita Deal Fiscal pela receita e deal fiscal.
	 * 
	 * @param receita
	 *            - Receita em questão
	 * @param deal
	 *            - DealFiscal em questão
	 * 
	 * @return retorna um {@link ReceitaDealFiscal}
	 */
	ReceitaDealFiscal findByReceitaAndDeal(final Receita receita,
			final DealFiscal deal);

	/**
	 * Busca todos os {@link ReceitaDealFiscal} ativos dado um
	 * {@link ContratoPratica} e uma {@link Moeda}.
	 * 
	 * @param contratoPratica
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return uma lista de {@link ReceitaDealFiscal}
	 */
	List<ReceitaDealFiscal> findByActiveAndCLobAndMoeda(
			final ContratoPratica contratoPratica, final Moeda moeda);

	/**
	 * Busca ReceitaDealFiscal por ReceitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            receitaMoeda.
	 * @return listResult.
	 */
	List<ReceitaDealFiscal> findByReceitaMoeda(Long codigoReceitaMoeda);

	/**
	 * Busca ReceitaDealFiscal por DealFiscal.
	 * 
	 * @param DealFiscal
	 *            DealFiscal.
	 * @return listResult.
	 */
	List<ReceitaDealFiscal> findPublishedAndIntegrateByDealFiscal(
			final DealFiscal dealFiscal);

	/**
	 * Busca ReceitaDealFiscal por DealFiscal e periodo.
	 * 
	 * @param DealFiscal
	 *            DealFiscal.
	 * @return listResult.
	 */
	List<ReceitaDealFiscal> findByDealFiscalAndPeriod(
			final DealFiscal dealFiscal, final Date startDate,
			final Date endDate);

	/**
	 * Busca ReceitaDealFiscal por DealFiscal
	 * 
	 * @param codigoDealFiscal
	 *            Código(PK) do {@link DealFiscal} vinculado a
	 *            {@link ReceitaDealFiscal}
	 * 
	 * @return Lista de {@link ReceitaDealFiscal}
	 */
	List<ReceitaDealFiscal> findByDealFiscal(final Long codigoDealFiscal);

	Boolean updateStatusReceitaDealFiscal(StatusReceitaDealFiscal statusReceitaDealFiscal);
}
