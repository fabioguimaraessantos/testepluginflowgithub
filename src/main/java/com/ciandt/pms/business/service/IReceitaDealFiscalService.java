package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.TaxaImpostoNotFoundException;
import com.ciandt.pms.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * A classe IReceitaDealFiscalService proporciona a interface de acesso a camada
 * de serviço referente a entidade ReceitaDealFiscal.
 * 
 * @since 04/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IReceitaDealFiscalService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createReceitaDealFiscal(final ReceitaDealFiscal entity);

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 */
	@Transactional
	void updateReceitaDealFiscal(final ReceitaDealFiscal entity);

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 */
	@Transactional
	void removeReceitaDealFiscal(final ReceitaDealFiscal entity);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	ReceitaDealFiscal findReceitaDealById(final Long entityId);

	/**
	 * Busca uma lista de entidades pela receita.
	 * 
	 * @param entity
	 *            entidade do tipo Receita.
	 * 
	 * @return lista de entidades relacionadas com a receita passada por
	 *         parametro.
	 */
	List<ReceitaDealFiscal> findReceitaDealByReceita(final Receita entity);

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
	List<ReceitaDealFiscal> findReceitaDealByFilter(
			final Long idContratoPratica, final Long idEmpresa, final Date dataMes,
			final String status);

	/**
	 * 
	 * @param cp
	 *            ContratoPratica
	 * @param cli
	 *            Cliente
	 * @param dataMesInicio
	 *            Data
	 * @param dataMesFim
	 *            Data
	 * @param receita
	 *            Receita
	 * @param indicadorStatus
	 *            Status
	 * @return busca com fitros selecionados
	 */
	@Deprecated
	List<ReceitaDealFiscal> findReceitaDealByFilterSub(
			final ContratoPratica cp, final Cliente cli,
			final Date dataMesInicio, final Date dataMesFim,
			final Receita receita, final DealFiscal dealFiscal);

	/**
	 * Busca uma lista de entidades pelo contrato-pratica and data-mes.
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
	List<ReceitaDealFiscal> findByReceitaDealFiscalContratoPraticaAndDataMesToAjuste(
			final Long idContratoPratica, final Date dataMes);

	/**
	 * Executa a integração da receita-deal com o ERP.
	 * 
	 * @param rdf
	 *            - ReceitaDealFiscal a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execução da integração.
	 */
	Boolean integrarReceitaDeal(final ReceitaDealFiscal rdf);

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
	List<ReceitaDealFiscal> findReceitaDealByFilter(final ContratoPratica cp,
			final Cliente cli, final CentroLucro cl,
			final NaturezaCentroLucro natureza, final Date dataMesInicio,
			final Date dataMesFim);

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
	 * Serviço que soma os valores das receitas publicadas relacionado com o
	 * deal até a dataFim.
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
	 * Realiza a reintegração da receita-deal.
	 * 
	 * @param rdf
	 *            entidade ReceitaDealFiscal.
	 */
	@Transactional
	void reIntegrarReceitaDeal(final ReceitaDealFiscal rdf);

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
	ReceitaDealFiscal findReceitaDealByReceitaAndDeal(final Receita receita,
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
	List<ReceitaDealFiscal> findReceitaDFiscalByActiveAndCLobAndMoeda(
			final ContratoPratica contratoPratica, final Moeda moeda);

	/**
	 * Calcula a media dos impostos dos TiposServico do DealFiscal e da Empresa.
	 * Caso não encontre a TaxaImposto associada à Empresa e TipoServico, não
	 * termina o cáuculo e retorna uma exception para que a Receita não seja
	 * publicada.
	 * 
	 * @param receitaDealFiscal
	 *            a {@link ReceitaDealFiscal} a ser calculada
	 * 
	 * @param dataMes
	 *            data da receita - usado para buscar a vigencia da taxa de
	 *            imposto
	 * 
	 * @exception TaxaImpostoNotFoundException
	 *                - excecao lancada quando não encontrado a TaxaImposto
	 *                associada à Empresa e TipoServico
	 * 
	 * @return retorna o valor calculado da taxa de imposto
	 */
	BigDecimal calculaMediaPercentualImposto(
			final ReceitaDealFiscal receitaDealFiscal, final Date dataMes)
			throws TaxaImpostoNotFoundException;

	/**
	 * Busca receitaDealFiscal por ReceitaMOeda.
	 * 
	 * @param receitaMoeda
	 *            receitaMoeda.
	 * @return listReuslt.
	 */
	List<ReceitaDealFiscal> findReceitaDealFiscalByReceitaMoeda(
			final ReceitaMoeda receitaMoeda);

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

	void updateStatusReceitaDealFiscal(StatusReceitaDealFiscal statusReceitaDealFiscal);

	Long findNotIntegratedRevenues(final Long revenueCurrencyCode);


}