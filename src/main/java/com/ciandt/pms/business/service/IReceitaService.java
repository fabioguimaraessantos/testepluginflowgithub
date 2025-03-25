/**
 * 
 */
package com.ciandt.pms.business.service;

import com.ciandt.pms.integration.vo.IncomingRevenue;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemReceitaRow;
import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;
import com.ciandt.pms.model.vo.ReceitaFilter;
import com.ciandt.pms.model.vo.ReceitaMoedaRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * A classe IReceitaService proporciona a interface de acesso para a camada de
 * servi�o referente a entidade Receita.
 * 
 * @since 28/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IReceitaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createReceita(final Receita entity);

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 * @param indStatusOld
	 *            indicador de Status antes da atualizacao
	 */
	@Transactional
	void updateReceita(final Receita entity, final String indStatusOld);

	/**
	 * Cria o HistoricoReceita da altera��o de status da Receita.
	 * 
	 * @param entity
	 *            - Receita corrente
	 * @param indStatusOld
	 *            - Status anterior � altera��o
	 */
	void createHistoricoReceita(final Receita entity, final String indStatusOld);

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 */
	@Transactional
	void removeReceita(final Receita entity);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	Receita findReceitaById(final Long entityId);

	Long findByRevenueDealFiscal(final Long revenueDealFiscalCode);

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Receita> findReceitaByFilter(final Receita filter);

	/**
	 * Busca uma lista de entidades pelo filtro. Tamb�m carrega asentidades
	 * relacionadas.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro e carrega.
	 * @param cli
	 *            entidade do tipo Cliente.
	 * @param cl
	 *            entidade do tipo CentroLucro
	 * @param natureza
	 *            entidade do tipo NaturezaCentroLucro
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Receita> findReceitaByFilter(final Receita filter, final Cliente cli,
			final CentroLucro cl, final NaturezaCentroLucro natureza);

	/**
	 * Busca uma lista de entidades pelo filtro, por�m com status Published e
	 * Integrated.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Receita> findReceitaAllNoWorkingByFilter(final Receita filter);

	/**
	 * 
	 * @param cl
	 *            CentroLucro
	 * @return Lista com filro
	 */
	List<Receita> findReceitaByCpclAndCentroLucro(final CentroLucro cl);

	/**
	 * Prepara a lista de ItemReceita para cria��o do Receita.
	 * 
	 * @param codigoContratoPratica
	 *            - c�digo do ContratoPratica
	 * @param validityMonth
	 *            - mes de vig�ncia
	 * @param validityYear
	 *            - ano de vig�ncia
	 * @param codigoMoeda
	 *            - codigo da moeda.
	 * @return lista de ItemReceitaRow - lista de ItemReceita
	 */
	List<ItemReceitaRow> prepareCreateListItemReceita(
			final Long codigoContratoPratica, final String validityMonth,
			final String validityYear, final Long codigoMoeda);

	/**
	 * Prepara a lista de ItemReceita para edi��o do Receita.
	 * 
	 * @param receitaMoeda
	 *            - ReceitaMoeda corrente
	 * @return lista de ItemReceitaRow - lista de ItemReceita
	 */
	List<ItemReceitaRow> prepareUpdateListItemReceita(
			final ReceitaMoeda receitaMoeda);

	/**
	 * Carrega uma lista de entidades ItemReceita com os dados da lista para
	 * persistir no banco.
	 * 
	 * @param receitaMoedaRow
	 *            - ReceitaMoedaRow corrente
	 * @param isDraft
	 *            - indicador do Status da Receita, se � Draft ou n�o
	 * 
	 * @return lista de ItemReceita
	 */
	List<ItemReceita> loadItemReceitaList(
			final ReceitaMoedaRow receitaMoedaRow, final Boolean isDraft, final Long codigoContratoPratica);

	/**
	 * Carrega uma lista de entidades ReceitaDealFiscal com os dados da lista
	 * para persistir no banco.
	 * 
	 * @param receitaMoedaRow
	 *            - ReceitaMoedaRow corrente
	 * 
	 * @return lista de ReceitaDealFiscal
	 */
	List<ReceitaDealFiscal> loadReceitaDealFiscalList(
			final ReceitaMoedaRow receitaMoedaRow);

	/**
	 * Verifica se o Receita que est� sendo criado � v�lido. Se n�o existir um
	 * Receita com o ContratoPratica, mes/ano e Vers�o passado por par�metro, a
	 * Receita � v�lida. Caso contr�rio � inv�lida.
	 * 
	 * @param codigoContratoPratica
	 *            - c�digo do ContratoPratica
	 * @param validityMonth
	 *            - mes da vigencia
	 * @param validityYear
	 *            - ano da vigencia
	 * @return true se o Receita for v�lido, false se j� existir um Receita
	 *         cadastrado com o ContratoPratica, mes/ano e Vers�o passados por
	 *         par�metro
	 */
	Boolean isValidReceitaVersion(final Long codigoContratoPratica,
			final String validityMonth, final String validityYear);

	/**
	 * Verifica se a Receita que est� sendo criado � v�lido. Verifica se o
	 * mes/ano passado por par�metro � menor do que a data atual. Se sim, a
	 * Receita � v�lido. Caso contr�rio � inv�lido.
	 * 
	 * @param validityMonth
	 *            - mes da vigencia
	 * @param validityYear
	 *            - ano da vigencia
	 * @return true se o Receita for v�lido se o mes/ano passados por par�metro
	 *         for menor do que a data atual. false caso contr�rio
	 */
	Boolean isValidReceitaMonthYear(final String validityMonth,
			final String validityYear);

	/**
	 * Adiciona o ItemReceita na lista de {@link ReceitaMoedaRow}.
	 * 
	 * @param itemReceita
	 *            - item para adicionar na lista
	 * @param receitaMoedaRow
	 *            - o {@link ReceitaMoedaRow} no qual o itme deve ser adicionado
	 * @param contratoPratica
	 *            - ContratoPratica da Receita
	 * @param percentualFaturamento
	 *            - percentual de faturamento (FTE) da Pessoa
	 * 
	 * @return ItemReceitaRow (ItemReceita) que acabou de ser adicionado na
	 *         lista
	 */
	ItemReceitaRow addItemReceita(final ItemReceita itemReceita,
			final ReceitaMoedaRow receitaMoedaRow,
			final ContratoPratica contratoPratica,
			final BigDecimal percentualFaturamento);

	/**
	 * Edita um ItemReceita na lista para ser atualizada posteriormente no
	 * banco.
	 * 
	 * @param contratoPratica
	 *            - ContratoPratica do item
	 * @param itemReceitaRow
	 *            - row do ItemReceita corrente
	 */
	void editItemReceita(final ContratoPratica contratoPratica,
			final ItemReceitaRow itemReceitaRow);

	/**
	 * Realiza a redistribui��o da receita para os itens selecionados.
	 * 
	 * @param itemReceitaRowList
	 *            - lista com todos os itens
	 * @param redistributionValue
	 *            - valor a ser redistribuidos.
	 */
	void calculateIncomeRedistribution(
			final List<ItemReceitaRow> itemReceitaRowList,
			final BigDecimal redistributionValue,
			final Receita receita);

	/**
	 * Realiza redistribui��o de receita de cada login(cidade base) em cada fiscal deal por cidade base.
	 * @param itemReceitaRowList
	 * @param receitaDealFiscalRowList
     */
	Boolean calculateDealFiscalRedistribution(final List<ItemReceitaRow> itemReceitaRowList, final List<ReceitaDealFiscalRow> receitaDealFiscalRowList);

	/**
	 * Realiza redistribui��o de receita de cada login(cidade base) em cada fiscal deal por cidade base.
	 * @param factor
	 * @param itemReceitaRowList
	 * @param receitaDealFiscalRowList
	 */
	Boolean calculateDealFiscalRedistribution(final BigDecimal factor, final List<ItemReceitaRow> itemReceitaRowList, final List<ReceitaDealFiscalRow> receitaDealFiscalRowList);

		/**
         * Busca uma lista de entidades pelo filtro.
         *
         * @param natureza
         *            entidade NaturezaCentroLucro.
         * @param cl
         *            - entidade CentroLucro.
         * @param p
         *            - entidade Pratica.
         * @param cli
         *            - entidade Cliente.
         * @param status
         *            - valor do Status.
         * @param dataMes
         *            - valor da data (mes/ano)
         *
         * @return lista de entidades que atendem ao criterio de filtro.
         */
	List<ReceitaFilter> findReceitaByFilter(final NaturezaCentroLucro natureza,
			final CentroLucro cl, final Pratica p, final Cliente cli,
			final String status, final Date dataMes);

	/**
	 * Recuprea a moeda corrente.
	 * 
	 * @param moeda
	 *            - Moeda corrente
	 * @param isSiglaMoeda
	 *            - booleano se retorna sigla da Moeda ou o nome
	 * @return o pattern da Moeda.
	 */
	String getCurrency(final Moeda moeda, final Boolean isSiglaMoeda);

	/**
	 * Atualiza os valores do FTE dos itens selecionados.
	 * 
	 * @param itemReceitaRowList
	 *            - Lista com as linhas da Receita.
	 * @param updateFte
	 *            - valor a ser atualizado do FTE na lista de ItemReceita.
	 */
	void updateIncomeFte(final List<ItemReceitaRow> itemReceitaRowList,
			final BigDecimal updateFte, final Receita receita);

	/**
	 * Atualiza os valores do FTE dos itens selecionados.
	 * 
	 * @param itemReceitaRowList
	 *            - Lista com as linhas da Receita.
	 * @param updateHours
	 *            - valor a ser atualizado da Receita na lista de ItemReceita.
	 */
	void updateIncomeHours(final List<ItemReceitaRow> itemReceitaRowList,
			final BigDecimal updateHours, final Receita receita);

	/**
	 * Pega a data de fechamento do horas faturadas.
	 * 
	 * @return retorna a Data de Fechamento.
	 */
	Date getClosingDateReceita();

	/**
	 * Exibir toda a lista (atribuir true para o atributo isView da
	 * ItemReceitaRow).
	 * 
	 * @param itemReceitaRowList
	 *            - lista de ItemReceita
	 */
	void showFullListItemReceita(final List<ItemReceitaRow> itemReceitaRowList);

	/**
	 * Calcula o valor total da lista de Receita convertido na Moeda Real.
	 * 
	 * @param receitaFilterTotalsList
	 *            - lista das ReceitaFilter que est�o sendo exibidas no
	 *            relat�rio
	 * @return o valor total da lista de Receita convertido
	 */
	BigDecimal calculateTotalReceitaConvert(
			final List<ReceitaFilter> receitaFilterTotalsList);

	/**
	 * Calcula o total das receitas de cada moeda. IMPORTANTE: o valor n�o �
	 * convertido.
	 * 
	 * @param receitaFilterList
	 *            - lista das {@link ReceitaFilter}
	 * @return lista de {@link ReceitaFilter} cada moeda com seu respectivo
	 *         total
	 */
	List<ReceitaFilter> calculateTotalReceitaByMoeda(
			final List<ReceitaFilter> receitaFilterList);

	/**
	 * Busca uma lista de entidades nao fechadas, status diferente de Published
	 * e Integrated.
	 * 
	 * @param dataMes
	 *            - data do fechamento.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Receita> findReceitaAllNotClosed(final Date dataMes);

	/**
	 * Busca uma todas as receitas de um determinado m�s.
	 * 
	 * @param monthDate
	 *            - data m�s de referencia.
	 * 
	 * @return lista de receitas do m�s passado por parametro.
	 */
	List<Receita> findReceitaByMonthDate(final Date monthDate);

	/**
	 * Valida��o do ClosingDateRevenue. Caso startDate (data de in�cio da
	 * vig�ncia) seja maior do que a data Revenue, a vig�ncia � v�lida e pode
	 * ser adicionada ou removida. Caso contr�rio, a vig�ncia n�o pode ser
	 * adicionada nem removida.
	 * 
	 * @param startDate
	 *            - data de in�cio da vig�ncia
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDate n�o seja v�lido
	 * 
	 * @return true se startDate for maior do que ClosingDateRevenue. false caso
	 *         contr�rio
	 */
	Boolean verifyClosingDateRevenue(final Date startDate,
			final Boolean showMsgError);

	/**
	 * Valida��o do ClosingDateRevenue. Caso startDate (data de in�cio da
	 * vig�ncia) seja maior do que a data Revenue, a vig�ncia � v�lida e pode
	 * ser adicionada ou removida. Caso contr�rio, a vig�ncia n�o pode ser
	 * adicionada nem removida.
	 * 
	 * the value 0 if the argument Date is equal to this Date; a value less than
	 * 0 if this Date is before the Date argument; and a value greater than 0 if
	 * this Date is after the Date argument.
	 * 
	 * @param startDate
	 *            - data de in�cio da vig�ncia
	 * 
	 * @return true se startDate for maior do que ClosingDateRevenue. false caso
	 *         contr�rio
	 */
	int verifyClosingDateRevenueCompareTo(final Date startDate);

	/**
	 * Retorna uma lista de Deal Fiscal referente a receita informada.
	 * 
	 * @param receita
	 *            - recebe uma receita
	 * 
	 * @return retorna uma lista de deal fiscal
	 */
	List<DealFiscal> getAllFiscalDealIntercompany(final Receita receita);

	/**
	 * Obtem a diferenca (em percentual) entre dois valores.
	 * 
	 * @param value1
	 *            valor um
	 * @param value2
	 *            valor dois
	 * @return o percentual correspondente a diferenca entre os valores.
	 */
	BigDecimal getPercentualDiferenca(final BigDecimal value1,
			final BigDecimal value2);

	/**
	 * Checa se a diferen�a em percentual entre o valor da Receita e o valor da
	 * {@link ReceitaResultado} (Foto) � superior ao percentual toleravel.
	 * 
	 * @param valorTotalReceita
	 *            recebe o valor total de uma receita
	 * 
	 * @param valorFotoReceita
	 *            recebe o valor total da receita de um {@link ReceitaResultado}
	 * 
	 * @return retorna um booleano - true caso o valor excede o percentual
	 *         tolerado.
	 */
	boolean excedePercentualToleravel(final BigDecimal valorTotalReceita,
			final BigDecimal valorFotoReceita);

	/**
	 * Remove a receita e limpa todas as justificativas ShortTermRevenue por
	 * receita moeda caso a receita a ser excluida possua alguma justificativa.
	 * Remove a lista de Ajustes de Receita referente a receita.
	 * 
	 * @param receita
	 *            receita a ser removida (juntamente com as justificativas)
	 */
	@Transactional
	void removeReceitaAndShortTermRevenueAndAdjusts(final Receita receita);

	/**
	 * Metodo utilizado para saber qual regra de claculo deve ser adotada para
	 * uma determinada receita na tela de Financials. Retorna um codigo
	 * indicando qual das regras listadas abaixo a receita se encaixa. 0 =
	 * Projetada, 1 = Receitada e 2 = None.
	 * 
	 * <table border="2">
	 * <tr>
	 * <td><strong>Mes Fechado (closing date)?</strong></td>
	 * <td><strong>Tem Receita Publicada?</strong></td>
	 * <td><strong>Fonte</strong></td>
	 * <td><strong>Retorno</strong></td>
	 * </tr>
	 * <tr>
	 * <td>NAO</td>
	 * <td>NAO</td>
	 * <td>MapaAlocacao</td>
	 * <td>0</td>
	 * </tr>
	 * <tr>
	 * <td>SIM</td>
	 * <td>NAO</td>
	 * <td>Zero com excecao do FTE total que vem do MapaAlocacao</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td>NAO</td>
	 * <td>SIM</td>
	 * <td>Receita</td>
	 * <td>1</td>
	 * </tr>
	 * <tr>
	 * <td>SIM</td>
	 * <td>SIM</td>
	 * <td>Receita</td>
	 * <td>1</td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataMes
	 *            - mes referencia
	 * 
	 * @param contratoPratica
	 *            - c-lob em questao
	 * 
	 * @param moeda
	 *            - moeda em questao
	 * 
	 * @return indicador de regra de calculo.
	 */
	int getRevenueCalculationRule(final Date dataMes,
			final ContratoPratica contratoPratica, final Moeda moeda);

	/**
	 * Prepara a lista de {@link ItemReceita} para a visualizacao.
	 * 
	 * @param receitaMoeda
	 *            a {@link ReceitaMoeda} base para a busca dos itens
	 * @return lista de {@link ItemReceitaRow}
	 */
	List<ItemReceitaRow> prepareViewListItemReceita(
			final ReceitaMoeda receitaMoeda);
	
	/**
	 * Consulta receita dos ultimos x meses por contrato pratica
	 * 
	 * @param meses
	 *            Numero de meses que ser� usado na busca para retorar as
	 *            receitas que possuem data maior que (data atual - meses)
	 * @param codigoContratoPratica
	 *            C�digo do contrato pr�tica
	 * @return Lista de {@link Receita}
	 */
	List<Receita> findUltimasReceitasByCP(Long meses, Long codigoContratoPratica);

	/**
	 * Valida a data de inicio de Revenue. � necess�rio que o inicio da vig�ncia
	 * da Revenue seja maior que a data de closing.
	 * 
	 * @param startDateRevenue
	 *            - data do inicio da vig�ncia da Revenue
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDateRevenue n�o seja
	 *            v�lido
	 * 
	 * @return true caso {@code startDateRevenue} seja maior que a data de
	 *         fechamento de Revenue Internacional. E false caso contr�rio
	 */
	Boolean verifyClosingDateInternationalRevenue(final Date startDateRevenue, final Boolean showMsgError);

	/**
	 * Compara {@code startDateRevenue} com a data de fechamento de
	 * International Revenue.
	 * 
	 * @param {@code startDateRevenue} - Data de inicio da vig�ncia da Receita
	 * 
	 * @return o valor 0 se o {@code startDateRevenue} � igual a data de
	 *         fechamento de Internatinal Revenue; um valor menor que 0 se a
	 *         data de fechamento for antes/menor que {@code startDateRevenue};
	 *         e um valor maior que 0 se a data de fechamento for depois/maior
	 *         que {@code startDateRevenue}.
	 */
	int verifyClosingDateInternationalRevenueCompareTo(final Date startDateRevenue);

	/**
	 * Checa se eh uma receita internacional sem recursos intercompany.
	 *
	 * @param receita
     * @param dealFiscal
	 * @return boolean
	 */
	boolean isInternationalRevenueWithoutIntercompanyResource(final Map<Long, String> companyErp, final Receita receita, final DealFiscal dealFiscal);

	boolean isInternationalRevenueWithIntercompany(final Map<Long, String> companyErp, final Receita receita, final DealFiscal dealFiscal);

	/**
	 * Verifica se a empresa de {@code receita} � internacional.
	 *
	 * @param receita
	 * @return {@link Boolean}
	 */
	Boolean isInternationalRevenue(final Map<Long, String> companyErp, final Receita receita);

	List<Receita> findAllReceitaForecastByCodigoContratoPratica(Long codigoContratoPratica);

	void updateAllReceitaForecastDealFiscalByContratoPratica(List<DealFiscal> dealFiscals, Long codigoContratoPratica);

	double getNumberHoursMonth(final Long codigoContratoPratica, final Long codigoPessoaCidadeBase, final Date dataReceita, final Double fte);

	void updateServiceRevenueFromMega(ReceitaDealFiscal receitaDealFiscal, String revenueStatus, BigDecimal megaOrderID, String errorMessage);

	void updateStatusReceita(Long revenueCode, String revenueStatus);

	void setStatusIntegratedRevenue(ReceitaDealFiscal receitaDealFiscal, BigDecimal megaOrderID, String errorMessage);

	void setStatusErrorRevenue(ReceitaDealFiscal receitaDealFiscal, BigDecimal megaOrderID, String errorMessage);

    /**
     * M�todo responsavel para for�ar receita para Integrated os casos onde n�o fazem integra�ao com
     * MEGA (China, Japao e Intercompany sem colaboradores BR).
     *
     * @param receita
     */
    void updateRevenueStatusAsIntegrated(Receita receita);

	/**
	 * Metodo para converter o objeto retornado direto da View para o JSON que ser� enviado para a fila de integra��o.
	 *
	 * @param model
	 * @return
	 */
	IncomingRevenue convertFromModel(VwPmsIntegReceitaNacional model) throws Exception;

    /**
     * Integra uma receita inviando para o ERP.
     * @param receita {@link VwPmsIntegReceitaNacional}
     * @return retorna a {@link VwPmsIntegReceitaNacional} da receita integrada
     */
    VwPmsIntegReceitaNacional integrate(VwPmsIntegReceitaNacional receita) throws Exception;

	/**
	 * Metodo que valida se todos os campos obrigatorios do XML que vai para o MEGA est� preenchido.
	 *
	 * @param vwPmsIntegReceitaNacional
	 * @return
	 */
	void validateRequiredFields(VwPmsIntegReceitaNacional vwPmsIntegReceitaNacional);

	void updateRevenueStatus(Long revenueDealFiscalCode);

	public List<Receita> findNotIntegratedRevenueAfterClosingRevenueDate(final Long codigoDealFiscal,
																		 final Date closingMapDate);

	/**
	 * Verifica se o cliente de um dos Fiscal Deals de um dado C-LOB � internacional.
	 *
	 * @param receita
	 * @return {@link Boolean}
	 */
	Boolean isInternationalClient(Receita receita);

	/**
	 * @param msa Msa to find Revenues through c-lob
	 * @param startDate  Price Table Start Date
	 * @param endDate  Price Table End Date
	 * @return List - Receita - Revenue List filter by params
	 */
	List<Receita> findRevenuesByMsaAndDates(Long msa, Date startDate, Date endDate);
}