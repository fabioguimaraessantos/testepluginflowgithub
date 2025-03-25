package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ciandt.pms.integration.vo.IncomingInvoice;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.PaymentCondition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.vo.FaturaRow;
import com.ciandt.pms.model.vo.ItemFaturaRow;

/**
 * 
 * A classe IFaturaService proporciona a interface de acesso para a camada de
 * serviço referente a entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IFaturaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return retorna true se criado com sucesso, caso contrario false
	 */
	@Transactional
	boolean createFatura(final Fatura entity);

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 * 
	 * @return retorna true se update realizado com sucesso, caso contrario
	 *         retorna false.
	 */
	@Transactional
	boolean updateFatura(final Fatura entity);

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 * @return retorna true se removido com sucesso caso contrario false.
	 */
	@Transactional
	Boolean removeFatura(final Fatura entity);

	/**
	 * Faz o cancelamento da Fatura caso ela já tenha sido submetida. Verifica
	 * se tem associação com a Revenue (Fiscal Balance). Se NÃO tiver, cancela.
	 * 
	 * @param fatura
	 *            - fatura a ser cancelada
	 * @param dataCancelamento
	 *            - data do cancelamento da Fatura
	 * @param textoRazaoCancelamento
	 *            - razao do cancelamento
	 * 
	 * @return retorna true se removido com sucesso caso contrario false.
	 */
	@Transactional
	Boolean cancelSubmitFatura(final Fatura fatura,
			final Date dataCancelamento, final String textoRazaoCancelamento) throws Exception;

	void updateInvoiceFromERP(Long faturaCode, String invoiceStatus, BigDecimal orderID, String errorMessage);

	void setStatusErrorInvoice(Fatura fatura, String errorMessage);

	void setStatusSubmittedInvoice(Fatura fatura, BigDecimal orderID, String errorMessage);

	/**
	 * Busca pelo id da entidade.
	 * 
	 * @param entityId
	 *            id da entidade
	 * 
	 * @return retorna a entidade
	 */
	Fatura findFaturaById(final Long entityId);

	/**
	 * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente
	 * as entidades relacionadas a esta.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * @param dataPrevisaoBeg
	 *            dataPrevisao inicial.
	 * @param dataPrevisaoEnd
	 *            dataPrevisao final.
	 * @param cli
	 *            entidade cliente.
	 * @param cp
	 *            entidade msa.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<FaturaRow> findFaturaByFilter(final Fatura filter, final Cliente cli,
			final Msa msa, final Date dataPrevisaoBeg,
			final Date dataPrevisaoEnd);

	/**
	 * Recuprea a moeda da Fatura corrente.
	 * 
	 * @param moeda
	 *            - Moeda da Fatura corrente
	 * @return o pattern da Moeda.
	 */
	String getCurrency(final Moeda moeda);

	/**
	 * Metodó que realiza a cópia de faturas. Cada cópia tem a data previsão
	 * alterada para o próximo mês
	 * 
	 * @param faturasRow
	 *            - lista de faturas a serem copiadas
	 * @param numberOfCopies
	 *            - número de cópias
	 */
	@Transactional
	boolean copyFaturas(final List<FaturaRow> faturasRow, final int numberOfCopies);

	/**
	 * Realiza o update do ItemFatura selecionados.
	 * 
	 * @param rowList
	 *            - Lista com os itens faturas
	 * @param dueDate
	 *            - Data de vencimento
	 */
	@Transactional
	void updateDueDate(final List<ItemFaturaRow> rowList, final Date dueDate);

	/**
	 * Realiza o update do ItemFatura selecionados.
	 * 
	 * @param rowList
	 *            - Lista com os itens faturas
	 * @param paymentDate
	 *            - Data de pagamento
	 */
	@Transactional
	void updatePaymentDate(final List<ItemFaturaRow> rowList,
			final Date paymentDate);

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param rdf
	 *            entidade do tipo ReceitaDealFiscal.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Fatura> findInvoicePendingByDeal(final ReceitaDealFiscal rdf);

	/**
	 * Salva a lista de itens da fatura.
	 * 
	 * @param fatura
	 *            - Fatura que se deseja salvar os itens.
	 * 
	 * @param itemFaturaList
	 *            - lista de itens que se deseja salvar.
	 * @return true se sucesso, false se erro na mudanca de status
	 */
	@Transactional
	Boolean saveItemFaturaList(final Fatura fatura,
			final List<ItemFatura> itemFaturaList);

	/**
	 * Executa a integração da Fatura com o ERP.
	 * 
	 * @param fatura
	 *            - Fatura a ser integrada ao ERP.
	 * 
	 * @return retorna o status da execução da integração. Se retorno menor que
	 *         zero erro, caso contrario sucesso.
	 */
	Boolean integrateFatura(final Map<Long, String> companyErp, final Fatura fatura) throws Exception;

	/**
	 * Realiza a reintegração da fatura.
	 * 
	 * @param fatura
	 *            entidade Fatura.
	 * 
	 * @return retorna true se integração desfeita com sucesso, caso contrario
	 *         false.
	 */
	@Transactional
	boolean reIntegrarFatura(final Fatura fatura);

	/**
	 * Busca uma lista de entidades nao fechadas, status igual a Open e Aproved.
	 * 
	 * @param dataMes
	 *            - data do fechamento.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Fatura> findFaturaAllNotClosed(final Date dataMes);

	/**
	 * Busca todas as faturas atrasadas, ou seseja, com a data de pagamento
	 * passada e no status OPEN.
	 * 
	 * @return lista de faturas atrasadas.
	 */
	List<Fatura> findFaturaDelayed();

	/**
	 * Busca que estão com a data pagamento para ser submetidas, nos próximos 7
	 * dias.
	 * 
	 * @return lista de faturas a ser submetidas.
	 */
	List<Fatura> findFaturaToBeSubmitted();

	/**
	 * Envia o email das faturas atrasadas para os AE responsáveis.
	 */
	void sendFaturaMailDelayed();

	/**
	 * Envia o email das faturas a serem submetidas para os AE responsáveis.
	 */
	void sendFaturaMailToBeSubmitted();

	/**
	 * Realiza a aprovação de uma fatura.
	 * 
	 * @param fatura
	 *            - entidade do tipo Fatura.
	 * 
	 * @return retorna true se realizado com sucesso, caso contrario false
	 */
	@Transactional
	boolean approveFatura(final Fatura fatura);

	/**
	 * Busca uma lista de entidades no mês passado por parametro.
	 * 
	 * @param dataMes
	 *            - data do mes.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<Fatura> findFaturaByMonthDate(final Date dataMes);

	/**
	 * Seta o valor da cotação no atributo cotacaoMoeda da fatura.
	 * 
	 * @param fatura
	 *            entidade do tipo Fatura, que que tera a cotação setada.
	 * 
	 */
	void setCotacaoMoeda(final Fatura fatura);

	/**
	 * Calcula o Due date.
	 * 
	 * @param cliente
	 * @param dataPrevisao
	 * @return (data + payment periodo do cliente)
	 */
	Date calculateDueDate(Cliente cliente, Date dataPrevisao);

	/**
	 * 
	 * @param item
	 *            item de itemFatura
	 * @return status do itemFatura
	 */
	String statusItemFatura(final ItemFatura item);

	/**
	 * Retorna o status da comissao.
	 * 
	 * @param item
	 *            itemFatura
	 * @return status do item de fatura
	 */
	String statusItemComissao(final ItemFatura item);

	/**
	 * Status de pagamento.
	 * 
	 * 
	 * 
	 * @param fatura
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return string de status.
	 */
	String status(final Fatura fatura);

	Fatura findFaturaApagadaById(Long id);

	Long getPrazoPagamentoByCondicaoPagamento(Collection<PaymentCondition> paymentConditions, PaymentConditionDealFiscal actual);

	IncomingInvoice convertFromModel(List<VwPmsIntegFaturaNacional> model);

    void validateRequiredFields(VwPmsIntegFaturaNacional vwPmsIntegFaturaNacional);
}