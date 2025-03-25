/**
 * Classe FaturaService - Implementa��o do servi�o da Fatura
 */
package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.audit.listener.AuditoriaJpaListener;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.converters.StatusFaturaConverter;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.integration.queue.InvoiceProducer;
import com.ciandt.pms.integration.queue.RevenueProducer;
import com.ciandt.pms.integration.service.ICancelFaturaService;
import com.ciandt.pms.integration.vo.IncomingInvoice;
import com.ciandt.pms.integration.vo.InvoiceInstallment;
import com.ciandt.pms.integration.vo.InvoiceObservation;
import com.ciandt.pms.integration.vo.RevenueItem;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.FaturaRow;
import com.ciandt.pms.model.vo.ItemFaturaRow;
import com.ciandt.pms.model.vo.PaymentCondition;
import com.ciandt.pms.persistence.dao.IFaturaDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A classe FaturaService proporciona as funcionalidades de servi�o para a
 * entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class FaturaService implements IFaturaService {

	private static final Logger logger = LoggerFactory.getLogger(FaturaService.class);

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Intancia de mailSender. */
	private MailSenderUtil mailSender;

	/** Instancia do DAO da entidade. */
	@Autowired
	private IFaturaDao faturaDao;

	/** Instancia do Service da entidade ItemFatura. */
	@Autowired
	private IItemFaturaService itemFaturaService;

	/** Instancia do Service da entidade ItemFatura. */
	@Autowired
	private IDealFiscalService dealService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IIntegFaturaParametroService intFatParamService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private ICotacaoMoedaService cotacaoMoedaService;

	/** Instancia do servico DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia do servico HistoricoFatura. */
	@Autowired
	private IHistoricoFaturaService hiFaService;

	@Autowired
	private IFaturaApagadaService faturaApagadaService;

	@Autowired
	private IItemFaturaApagadoService itemFaturaApagadoService;

	@Autowired
	private IVwPmsIntegFaturaNacionalService vwPmsIntegFaturaNacionalService;

	@Autowired
	private IVwPmsIntegReceitaNacionalService vwPmsIntegReceitaNacionalService;

	@Autowired
	private IReceitaService receitaService;

	@Autowired
	private IEmpresaService empresaService;

	@Autowired
	private ICancelFaturaService cancelFaturaService;

	/** Intancia que realiza a auditoria (Log). */
	private AuditoriaJpaListener auditoria = new AuditoriaJpaListener();

	@Autowired
	private RevenueProducer revenueProducer;
	private Fatura statusPendingInvoice;

	@Autowired
	private PaymentConditionService paymentConditionService;

	@Autowired
	private IPaymentConditionDealFiscalService paymentConditionDealFiscalService;

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private InvoiceProducer invoiceProducer;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return retorna true se criado com sucesso caso contrario false.
	 */
	public boolean createFatura(final Fatura entity) {
		// valida a fatura.
		if (!validateFatura(entity)) {
			return Boolean.valueOf(false);
		}

		// seta a cota��o e a moeda
		this.setCotacaoMoeda(entity);

		faturaDao.create(entity);

		hiFaService.createHistoricoFatura(entity);

		auditoria.postPersist(entity);

		this.sendFaturaMail(entity,
				BundleUtil.getBundle("_nls.fatura.mail.event.create"));

		return Boolean.valueOf(true);
	}

	/**
	 * Seta o valor da cota��o no atributo cotacaoMoeda da fatura.
	 * 
	 * @param fatura
	 *            entidade do tipo Fatura, que que tera a cota��o setada.
	 * 
	 */
	public void setCotacaoMoeda(final Fatura fatura) {
		// pega o deal fiscal
		DealFiscal dealFiscal = dealService.findDealFiscalById(fatura
				.getDealFiscal().getCodigoDealFiscal());

		// seta a moeda
		Moeda moeda = dealFiscal.getMoeda();
		fatura.setMoeda(moeda);

		// seta a cota��o da moeda
		fatura.setCotacaoMoeda(cotacaoMoedaService.findCotacaoMoedaByMonth(
				moeda, fatura.getDataPrevisao()));
	}

	/**
	 * Realiza a valida��o da fatura de acordo com a regra de negocio - closing
	 * date.
	 * 
	 * @param entity
	 *            fatura a ser validada.
	 * 
	 * @return retorna true se validada cpm sucesso, caso contrario false.
	 */
	private boolean validateFatura(final Fatura entity) {
		// se Fatura for SB, n�o valida a data
		if (Constants.FATURA_STATUS_SUBMITTED.equals(entity
				.getIndicadorStatus())) {
			return true;
		}

		// pega a data de fechamento da fatura.
		Date closingDate = this.getClosingDateOfInvoice();

		// se a data de fechamento � anterior
		// a data de previs�o da fatura retorna false.
		if (!DateUtil.after(entity.getDataPrevisao(), closingDate)) {
			// adiciona mensagem de erro
			Messages.showError("createFatura",
					Constants.INTEGRACAO_FATURA_MSG_ERROR_BEFORE_CLOSING_DATE);
			return false;
		}

		return true;
	}

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
	public boolean updateFatura(final Fatura entity) {

		if (!validateFatura(entity)) {
			return false;
		}

		// seta a cota��o
		setCotacaoMoeda(entity);

		// realiza o update
		faturaDao.update(entity);
		// atualiza o status da fatura
		this.updateFaturaStatus(entity, entity.getIndicadorStatus());
		// cria o HistoricoFatura
		hiFaService.createHistoricoFatura(entity);

		// log da auditoria
		auditoria.preUpdate(entity);

		// envia o email de evendo de update
		this.sendFaturaMail(entity,
				BundleUtil.getBundle("_nls.fatura.mail.event.update"));

		return true;
	}

	/**
	 * Realiza a aprova��o de uma fatura.
	 * 
	 * @param fatura
	 *            - entidade do tipo Fatura.
	 * 
	 * @return retorna true se realizado com sucesso, caso contrario false
	 */
	public boolean approveFatura(final Fatura fatura) {

		if (Constants.FATURA_STATUS_OPEN.equals(fatura.getIndicadorStatus())) {
			Fatura f = this.findFaturaById(fatura.getCodigoFatura());
			f.setIndicadorStatus(Constants.FATURA_STATUS_APPROVED);

			this.updateFatura(f);

			return true;
		}

		return false;
	}

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
	public Boolean saveItemFaturaList(final Fatura fatura,
			final List<ItemFatura> itemFaturaList) {

		Boolean retorno = Boolean.valueOf(true);

		Fatura f = this.findFaturaById(fatura.getCodigoFatura());
		// pega o status antigo / anterior da Fatura
		String indStatusOld = f.getIndicadorStatus();
		f.setDataPrevisao(fatura.getDataPrevisao());
		f.setDataVencimento(fatura.getDataVencimento());
		f.setItemFaturas(itemFaturaList);
		DealFiscal dealFiscal = fatura.getDealFiscal();
		f.setDealFiscal(dealFiscal);
		f.setMoeda(dealFiscal.getMoeda());

		faturaDao.update(f);

		retorno = updateFaturaStatus(f, fatura.getIndicadorStatus());
		// cria o HistoricoFatura caso tenha alterado o status
		hiFaService.createHistoricoFatura(f);

		auditoria.preUpdate(fatura);

		return retorno;
	}

	/**
	 * Seta o status da fatura. Se a fatura estiver com todos os seus itens
	 * preenchidos, ent�o o status � fica igual a 'SUBMITTED' caso contrario
	 * fica com o valor 'APPROVED'
	 * 
	 * @param fatura
	 *            - fatura a ser verificada
	 * @param newStatus
	 *            - novo status da fatura
	 * @return true se sucesso, false se erro na mudanca de status
	 */
	@Transactional
	private Boolean updateFaturaStatus(final Fatura fatura,
			final String newStatus) {
		List<ItemFatura> itemFaturas = fatura.getItemFaturas();

		Boolean retorno = Boolean.valueOf(true);

		boolean allFaturaIsSubmitted = true;

		if (itemFaturas.isEmpty()) {

			allFaturaIsSubmitted = false;

		} else {

			for (ItemFatura itemFatura : itemFaturas) {
				if (itemFatura.getNumeroNotaFiscal() == null
						|| "".equals(itemFatura.getNumeroNotaFiscal())) {

					allFaturaIsSubmitted = false;
					break;
				}
			}
		}

		if (allFaturaIsSubmitted) {
			if (fatura.getDataVencimento() != null) {
				fatura.setIndicadorStatus(Constants.FATURA_STATUS_SUBMITTED);
			} else {
				retorno = Boolean.valueOf(false);
			}
		} else if (Constants.FATURA_STATUS_SUBMITTED.equals(fatura
				.getIndicadorStatus())) {
			// verifico se a fatura foi integrada
			if (!faturaDao.isErpOrderCanceled(fatura)) {
				fatura.setIndicadorStatus(Constants.FATURA_STATUS_INTEGRATED);
			} else {
				fatura.setIndicadorStatus(Constants.FATURA_STATUS_APPROVED);
			}
		} else if (Constants.FATURA_STATUS_APPROVED.equals(fatura
				.getIndicadorStatus())
				|| Constants.FATURA_STATUS_OPEN.equals(fatura
						.getIndicadorStatus())
				|| Constants.FATURA_STATUS_CANCELED.equals(fatura
						.getIndicadorStatus())
				|| Constants.FATURA_STATUS_INTEGRATED.equals(fatura
						.getIndicadorStatus())) {
			fatura.setIndicadorStatus(newStatus);
		}

		faturaDao.update(fatura);

		return retorno;
	}

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 * 
	 * @return retorna true se removido com sucesso caso contrario false.
	 */
	public Boolean removeFatura(final Fatura entity) {
		hiFaService.createHistoricoFatura(entity);

		FaturaApagada faturaApagada = new FaturaApagada(entity);
		faturaApagadaService.create(faturaApagada);

		for (ItemFatura itemFatura : entity.getItemFaturas()) {
			ItemFaturaApagado itemFaturaApagado = new ItemFaturaApagado(itemFatura, faturaApagada);
			itemFaturaApagadoService.create(itemFaturaApagado);

			itemFaturaService.removeItemFatura(itemFatura, false);
		}

		// remove Fatura
		faturaDao.remove(entity);

		return true;
	}

	@Transactional
	public void updateInvoiceFromERP(Long invoiceCode, String invoiceStatus, BigDecimal orderID, String errorMessage){
		Fatura fatura = this.findFaturaById(invoiceCode);
		if (invoiceStatus.equals("A") && !fatura.getIndicadorStatus().equals(Constants.FATURA_STATUS_SUBMITTED)){
			this.setStatusSubmittedInvoice(fatura, orderID, errorMessage);
		}
		else if (invoiceStatus.equals("E") && !fatura.getIndicadorStatus().equals(Constants.FATURA_STATUS_SUBMITTED)){
			this.setStatusErrorInvoice(fatura, errorMessage);
		}
	}

	public void setStatusSubmittedInvoice(Fatura fatura, BigDecimal orderID,  String errorMessage){
		List<ItemFatura> itensFatura = itemFaturaService.findItemFaturaByFatura(fatura);

		for (ItemFatura itemFatura : itensFatura){
			itemFatura.setNumeroNotaFiscal(orderID.toString());
			itemFaturaService.updateItemFatura(itemFatura);
		}
		fatura.setIndicadorStatus(Constants.FATURA_STATUS_SUBMITTED);
		fatura.setTextoError(errorMessage);
		this.updateFatura(fatura);
	}

	public void setStatusErrorInvoice(Fatura fatura, String errorMessage){
		fatura.setIndicadorStatus(Constants.FATURA_STATUS_INTEGRATION_ERROR);
		fatura.setTextoError(errorMessage);
		mailSender.sendTextMail(Constants.EMAIL_ADDRESS_ERROR_KEY, BundleUtil.getBundle("_nls.fatura.mail.error.subject", fatura.getCodigoFatura()), errorMessage);
		this.updateFatura(fatura);
	}

	@Transactional
	public void setStatusPendingInvoice(Fatura invoice) {
		invoice.setIndicadorStatus(Constants.FATURA_STATUS_PENDING);

		if(invoice.getIntegratedIndicatorExternalERP() == null){
            invoice.setIntegratedIndicatorExternalERP(false);
        }

		this.updateFatura(invoice);

		return;
	}

	/**
	 * Faz o cancelamento da Fatura caso ela j� tenha sido submetida. Verifica
	 * se tem associa��o com a Revenue (Fiscal Balance). Se N�O tiver, cancela.
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
	public Boolean cancelSubmitFatura(final Fatura fatura,
			final Date dataCancelamento, final String textoRazaoCancelamento) throws Exception {
		Fatura newFatura = this.findFaturaById(fatura.getCodigoFatura());
		// pega o status antigo / anterior da Fatura
		String indStatusOld = newFatura.getIndicadorStatus();

		// verifica se a Fatura tem o status Submitted, se n�o msg de erro
		if (!(Constants.FATURA_STATUS_SUBMITTED).equals(indStatusOld)) {
			Messages.showError("cancelSubmitFatura",
					Constants.INTEGRACAO_FATURA_CANCEL_MSG_ERROR_STATUS,
					indStatusOld);

			return Boolean.valueOf(false);
		}

		// verifica se tem associa��o com a Revenue (FaturaReceita - Fiscal
		// Balance), se TEM msg de erro
		if (newFatura.getFaturaReceitas().size() > 0) {
			Messages.showError("cancelSubmitFatura",
					Constants.FATURA_MSG_ERROR_INTEGRITY_CONSTRAINT_CANCEL);

			return Boolean.valueOf(false);
		}

		// se passou nos testes, grava a dataCancelamento
		// e textoRazaoCancelamento, altera o status da Fatura para
		// "Canceled" e envia e-mail para o AE
		newFatura.setDataCancelamento(dataCancelamento);
		newFatura.setTextoRazaoCancelamento(textoRazaoCancelamento);

		newFatura.setIndicadorStatus(Constants.FATURA_STATUS_CANCELED);

		auditoria.preUpdate(fatura);

		faturaDao.update(newFatura);
		hiFaService.createHistoricoFatura(newFatura);

		// envia a fatura cancelada no PMS para a exchange de cancelamento
		if (fatura.isIntegradaERPExterno()) {
			try {
				cancelFaturaService.cancelExternalERP(fatura.getItemFaturas().get(0).getNumeroNotaFiscal());
			} catch (BusinessException be) {
				Messages.showWarningWithoutBundle("cancelSubmitFatura", be.getMessage());
			}
		}

		this.sendCancelFaturaMail(newFatura);

		// mensagem de sucesso
		Messages.showSucess("cancelSubmitFatura",
				Constants.DEFAULT_MSG_SUCCESS_CANCEL,
				Constants.ENTITY_NAME_FATURA);

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
	public Fatura findFaturaById(final Long entityId) {
		return faturaDao.findById(entityId);
	}

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
	 * @param msa
	 *            entidade msa.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<FaturaRow> findFaturaByFilter(final Fatura filter,
			final Cliente cli, final Msa msa, final Date dataPrevisaoBeg,
			final Date dataPrevisaoEnd) {

		List<Fatura> faturaList = new ArrayList<Fatura>();

		if (filter.getCodigoFatura() == null) {
			faturaList = faturaDao.findByFilter(filter, cli, msa,
					dataPrevisaoBeg, dataPrevisaoEnd);
		} else {
			Fatura fat = faturaDao.findById(filter.getCodigoFatura());
			if (fat != null) {
				faturaList.add(fat);
			}
		}

		List<FaturaRow> faturaRowList = new ArrayList<FaturaRow>();
		int rowCont = 0;

		for (Fatura fatura : faturaList) {
			if (fatura != null && fatura.getDealFiscal() != null) {
				Hibernate.initialize(fatura.getDealFiscal().getEmpresa());
			}
			List<ItemFatura> itemFaturaList = fatura.getItemFaturas();
			double totalFatura = Double.valueOf(0);

			// Flag para identificar se a fatura esta inteiramente comissionada.
			// Se tudo comissioned = YES
			boolean commission = Boolean.valueOf(true);

			for (ItemFatura itemFatura : itemFaturaList) {
				totalFatura += itemFatura.getValorItem().doubleValue();
				itemFatura.setStatusItem(this.statusItemFatura(itemFatura));
				itemFatura.setStatusComissao(this
						.statusItemComissao(itemFatura));

				// se existir algum item que n�o for "Comisssioned" setta como
				// false. Se houver um item nao comssioned = NO
				if (!(this.statusItemComissao(itemFatura)
						.equals(Constants.COMISSAO_STATUS_COMISSIONED))) {
					commission = Boolean.valueOf(false);
				}
			}

			FaturaRow faturaRow = new FaturaRow(fatura, rowCont++);
			faturaRow.setTotalFatura(BigDecimal.valueOf(totalFatura));
			faturaRow.setPatternCurrency(getCurrency(fatura.getMoeda()));
			faturaRow.setStatusPagamento(this.status(fatura));
			faturaRow.setIsCommission(commission);

			faturaRowList.add(faturaRow);
		}

		return faturaRowList;
	}

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
	public String status(final Fatura fatura) {
		List<ItemFatura> lista = itemFaturaService
				.findItemFaturaByFatura(fatura);
		int cont = 0;
		for (ItemFatura itemFatura : lista) {
			if (itemFatura.getDataPagamento() != null) {
				cont++;
			}
		}
		if (cont > 0 && cont < lista.size()) {
			return Constants.FATURA_STATUS_PARTLYPAG;
		} else if (cont == 0) {
			return Constants.FATURA_STATUS_NOTPAID;
		} else {
			return Constants.FATURA_STATUS_PAID;
		}
	}

	/**
	 * 
	 * @param item
	 *            item de itemFatura
	 * @return status do itemFatura
	 */
	public String statusItemFatura(final ItemFatura item) {
		String status = "";
		if (item.getDataPagamento() == null) {
			status = Constants.FATURA_STATUS_NOTPAID;
		}

		return status;
	}

	/**
	 * Retorna o status da comissao.
	 * 
	 * @param item
	 *            itemFatura
	 * @return status do item de fatura
	 */
	public String statusItemComissao(final ItemFatura item) {
		if (item.getComissaoFaturas() != null
				&& item.getComissaoFaturas().size() != 0) {
			return item.getComissaoFaturas().get(0).getComissao()
					.getIndicadorEstadoAtual();
		} else {
			return Constants.COMISSAO_STATUS_OPEN;
		}
	}

	/**
	 * Realiza o update do ItemFatura selecionados.
	 * 
	 * @param rowList
	 *            - Lista com os itens faturas
	 * @param dueDate
	 *            - Data de vencimento
	 */
	public void updateDueDate(final List<ItemFaturaRow> rowList,
			final Date dueDate) {

		for (ItemFaturaRow row : rowList) {
			if (row.getIsSelected()) {
				ItemFatura itemFatura = itemFaturaService
						.findItemFaturaById(row.getItemFatura()
								.getCodigoItemFatura());

				Fatura fatura = this.findFaturaById(itemFatura.getFatura()
						.getCodigoFatura());

				fatura.setDataVencimento(dueDate);

				this.updateFatura(fatura);
			}
		}
	}

	/**
	 * Realiza o update do ItemFatura selecionados.
	 * 
	 * @param rowList
	 *            - Lista com os itens faturas
	 * @param paymentDate
	 *            - Data de pagamento
	 */
	public void updatePaymentDate(final List<ItemFaturaRow> rowList,
			final Date paymentDate) {

		for (ItemFaturaRow row : rowList) {
			if (row.getIsSelected()) {
				ItemFatura itemFatura = itemFaturaService
						.findItemFaturaById(row.getItemFatura()
								.getCodigoItemFatura());

				itemFatura.setDataPagamento(paymentDate);

				itemFaturaService.updateItemFatura(itemFatura);
			}
		}
	}

	/**
	 * Metodo que realiza a c�pia de faturas. Cada c�pia tem a data previs�o
	 * alterada para o pr�ximo m�s
	 * 
	 * @param faturasRow
	 *            - lista de faturas a serem copiadas
	 * @param numberOfCopies
	 *            - n�mero de c�pias
	 */
	public boolean copyFaturas(final List<FaturaRow> faturasRow,
			final int numberOfCopies) {

		Fatura faturaClone = null;
		ItemFatura itemFaturaClone = null;
		List<ItemFatura> itemFaturaList = null;
		List<ItemFatura> itemFaturaListClone = null;

		for (FaturaRow faturaRow : faturasRow) {
			if (faturaRow.getIsSelected()) {

				for (int i = 1; i <= numberOfCopies; i++) {

					faturaClone = this.findFaturaById(
							faturaRow.getFatura().getCodigoFatura()).getClone();
					faturaClone
							.setIndicadorStatus(Constants.FATURA_STATUS_OPEN);
					faturaClone.setDataPrevisao(DateUtils.addMonths(
							faturaClone.getDataPrevisao(), i));

					faturaClone.setNumeroDoc(null);
					faturaClone.setIntegratedIndicatorExternalERP(false);

					// Verifica se o ClientEntity possui prazo de vencimento de
					// fatura
					Long codigoDealFiscal = faturaClone.getDealFiscal()
							.getCodigoDealFiscal();
					DealFiscal dealFiscal = dealFiscalService
							.findDealFiscalById(codigoDealFiscal);

					Long prazoRecebimento = dealFiscal.getCliente()
							.getNumeroDiasPrazoRecebimento();

					if (prazoRecebimento != null) {
						// Caso possua prazo de recebimento soma a data de
						// previsao
						Date dataVencimento = DateUtil
								.sumDays(prazoRecebimento,
										faturaClone.getDataPrevisao());
						dataVencimento = DateUtil.nextWorkDay(dataVencimento);
						faturaClone.setDataVencimento(dataVencimento);
					} else if (faturaClone.getDataVencimento() != null) {
						Messages.showError("findById",
								Constants.INVOICE_NO_PAYMENT_PERIOD, dealFiscal
										.getCliente().getNomeCliente());

						return false;
					}

					// pega o campo observa��o
					String textoObservacao = faturaClone.getTextoObservacao();
					if (textoObservacao == null) {
						textoObservacao = "";
					}
					faturaClone.setTextoObservacao("(COPY OF INVOICE "
							+ faturaClone.getCodigoFatura() + ") "
							+ textoObservacao);
					itemFaturaList = faturaClone.getItemFaturas();
					faturaClone.setCodigoFatura(null);

					itemFaturaListClone = new ArrayList<ItemFatura>();
					for (ItemFatura itemFatura : itemFaturaList) {
						itemFaturaClone = itemFatura.getClone();
						itemFaturaClone.setFatura(faturaClone);

						if (itemFaturaClone.getTextoObservacao() != null &&
								itemFaturaClone.getTextoObservacao().length() > Constants.INVOICE_ITEM_DESCRIPTION_MAX_LENGTH) {
							itemFaturaClone.setTextoObservacao(itemFaturaClone.getTextoObservacao().substring(0, Constants.INVOICE_ITEM_DESCRIPTION_MAX_LENGTH));
						}
						itemFaturaClone.setNumeroNotaFiscal(null);
						itemFaturaClone.setNumeroPedido(null);
						itemFaturaClone.setCodigoItemFatura(null);
						itemFaturaClone.setDataPagamento(null);
						itemFaturaClone.setComissaoFaturas(null);

						itemFaturaListClone.add(itemFaturaClone);
					}
					faturaClone.setItemFaturas(itemFaturaListClone);
					// for�a FaturaReceita null pq esses registros
					// n�o precisam ser duplicados
					faturaClone.setFaturaReceitas(null);
					faturaClone.setDataCancelamento(null);
					faturaClone.setTextoRazaoCancelamento(null);

					// zera o HistoricoFatura, pois a c�pia n�o precisa do
					// hist�rico da antiga
					faturaClone
							.setHistoricoFaturas(new ArrayList<HistoricoFatura>());

					this.createFatura(faturaClone);
				}
			}
		}
		return true;
	}

	/**
	 * Recuprea a moeda da Fatura corrente.
	 * 
	 * @param moeda
	 *            - Moeda da Fatura corrente
	 * @return o pattern da Moeda.
	 */
	public String getCurrency(final Moeda moeda) {
		if (moeda != null) {
			// atribui o pattern dos valores conforme sigla da moeda
			return moeda.getSiglaMoeda();
		}
		return "";
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param rdf
	 *            entidade do tipo ReceitaDealFiscal.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Fatura> findInvoicePendingByDeal(final ReceitaDealFiscal rdf) {
		return faturaDao.findInvoicePedingByDeal(rdf);
	}

	/**
	 * Cria e envia uma mensagem de email de uma fatura.
	 * 
	 * @param fatura
	 *            - entidade Fatura.
	 * @param evento
	 *            - nome do evento realizado.
	 */
	private void sendFaturaMail(final Fatura fatura, final String evento) {

		Fatura f = findFaturaById(fatura.getCodigoFatura());

		String subject = BundleUtil.getBundle("_nls.fatura.mail.subject",
				evento, f.getCodigoFatura(), f.getDealFiscal().getMsa()
						.getCliente().getNomeCliente());

		StatusFaturaConverter converter = new StatusFaturaConverter();

		Map<String, Object> dataSource = new HashMap<String, Object>();
		// cria o map com os dadso do email
		dataSource.put("title", subject);
		dataSource.put("author", LoginUtil.getLoggedUsername());
		dataSource.put("fatura", f);
		dataSource.put("status",
				converter.getAsString(null, null, f.getIndicadorStatus()));
		// pega o tamplete de email e insere os dados
		String message = mailSender.getTemplateMailMessage("fatura.vm",
				dataSource);

		String to = systemProperties
				.getProperty(Constants.EMAIL_ADDRESS_ADM_COMERCIAL_KEY);

		// envia o email
		mailSender.sendHtmlMail(to, subject, message);
	}

	/**
	 * Cria e envia uma mensagem de email do cancelamento de uma fatura.
	 * 
	 * @param fatura
	 *            - entidade Fatura.
	 */
	private void sendCancelFaturaMail(final Fatura fatura) {

		Fatura f = findFaturaById(fatura.getCodigoFatura());

		String subject = BundleUtil.getBundle("_nls.fatura.mail.subject",
				BundleUtil.getBundle("_nls.fatura.mail.event.cancel"),
				f.getCodigoFatura(), f.getDealFiscal().getMsa().getCliente()
						.getNomeCliente());

		StatusFaturaConverter converter = new StatusFaturaConverter();

		Map<String, Object> dataSource = new HashMap<String, Object>();
		// cria o map com os dadso do email
		dataSource.put("title", subject);
		dataSource.put("author", LoginUtil.getLoggedUsername());
		dataSource.put("fatura", f);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
		dataSource.put("dataPrevisao", sdf.format(f.getDataPrevisao()));
		dataSource.put("dataCancelamento", sdf.format(f.getDataCancelamento()));
		dataSource.put("status",
				converter.getAsString(null, null, f.getIndicadorStatus()));
		// pega o tamplete de email e insere os dados
		String message = mailSender.getTemplateMailMessage("faturaCancel.vm",
				dataSource);

		String codigoLoginAe = f.getCodigoLoginAe();
		if (!StringUtils.isEmpty(codigoLoginAe)) {
			Pessoa pessoaAE = pessoaService.findPessoaByLogin(codigoLoginAe);

			if (pessoaAE != null) {
				String to = pessoaAE.getTextoEmail();

				if (!StringUtils.isEmpty(to)) {
					// envia o email
					mailSender.sendHtmlMail(to, subject, message);
				}
			}
		}
	}

	/**
	 * @return the mailSender
	 */
	public MailSenderUtil getMailSender() {
		return mailSender;
	}

	/**
	 * @param mailSender
	 *            the mailSender to set
	 */
	public void setMailSender(final MailSenderUtil mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Executa a integra��o da Fatura com o ERP.
	 *
	 * @param fatura - Fatura a ser integrada ao ERP.
	 * @return retorna o status da execu��o da integra��o. Se retorno menor que
	 * zero erro, caso contrario sucesso.
	 */
	@Transactional
	public Boolean integrateFatura(final Map<Long, String> companyErp, final Fatura fatura) throws Exception {

		Fatura f = this.findFaturaById(fatura.getCodigoFatura());

		PaymentConditionDealFiscal paymentConditionDealFiscal = paymentConditionDealFiscalService.findActualPaymentCondition(f.getDealFiscal().getCodigoDealFiscal());
		if (null == paymentConditionDealFiscal || null == paymentConditionDealFiscal.getPaymentConditionName()) {
			logger.info("Fiscal Deal without payment condition configured");
			throw new NullPointerException("Fiscal Deal without payment condition configured");
		}

		List<VwPmsIntegFaturaNacional> faturaViewList = vwPmsIntegFaturaNacionalService.findByInvoiceCode(fatura.getCodigoFatura());
		if (!faturaViewList.isEmpty()) {
			//Movi para dentro do IF para validar somente em invoices nacionais.
			List<ItemFatura> itemFaturas = f.getItemFaturas();
			for (ItemFatura itemFatura : itemFaturas) {
				IntegFaturaParametro param = intFatParamService
						.findIntFatByTpServicoFtReceitaAndEmpresa(itemFatura
								.getTipoServico(), itemFatura.getFonteReceita(), f
								.getDealFiscal().getEmpresa());
				logger.info("IntegFaturaParametro " + param.toString());

				if (param == null) {
					Messages.showError("integrateFatura",
							Constants.INTEGRACAO_FATURA_PARAM_NOT_FIND,
							"" + f.getCodigoFatura());
					return false;
				}
			}

			for (VwPmsIntegFaturaNacional faturaView : faturaViewList) {
				this.validateRequiredFields(faturaView);
			}

			if(!DateUtil.isSameDay(new Date(), fatura.getDataPrevisao())){
				faturaViewList = this.updateDueDateFatura(fatura, faturaViewList, fatura.getDealFiscal().getCodigoDealFiscal());
				logger.info("updateDueDateFatura: " + fatura.getCodigoFatura());
			}

			IncomingInvoice incomingInvoice = this.convertFromModel(faturaViewList);
			this.revenueProducer.send(incomingInvoice.toJson(), Constants.INVOICE_PRODUCER);
			logger.info("incomingInvoice to json: " + incomingInvoice.toJson());
		}
		//Itens de Reembolso
		for (ItemFatura item : fatura.getItemFaturas()){
			if (item.getFonteReceita().getCodigoFonteReceita().equals(Constants.EXPENSE_REEMB_REVENUE_SOURCE) || item.getFonteReceita().getCodigoFonteReceita().equals(Constants.CONTRACTOR_REVENUE_SOURCE)){
				VwPmsIntegReceitaNacional receitaView = vwPmsIntegReceitaNacionalService.findById(item.getCodigoItemFatura());
				logger.info("VwPmsIntegReceitaNacional:  " + receitaView.toString());
				if (receitaView != null) {

					receitaService.integrate(receitaView);
					logger.info("integrate:  " + receitaView.toString());
				}
			}
		}

		DealFiscal dfFatura = dealFiscalService.findDealFiscalById(fatura.getDealFiscal().getCodigoDealFiscal());
		if (!Constants.LEGACY_ERP_BRAZIL.equals(companyErp.get(dfFatura.getEmpresa().getCodigoEmpresa()))
				&& !DateUtil.isSameDay(new Date(), fatura.getDataPrevisao())) {
			this.updateDueDateFaturaINC(fatura, dfFatura);
		}

		this.setStatusPendingInvoice(fatura);

		// sucesso na integracao
		return true;
	}

	@Transactional
	private List<VwPmsIntegFaturaNacional> updateDueDateFatura(Fatura fatura, List<VwPmsIntegFaturaNacional> vwIntegFaturaList, Long codigoDealFiscal) throws IOException {
			DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(codigoDealFiscal);
			logger.info("Deal fiscal " + dealFiscal.toString());

			final PaymentConditionDealFiscal paymentConditionsAtual = paymentConditionDealFiscalService.findActualPaymentCondition(dealFiscal.getCodigoDealFiscal());
		    logger.info("paymentConditionsAtual " + paymentConditionsAtual.getCode());

			Cliente client = dealFiscal.getCliente();
			logger.info("Cliente:  " + client.toString());

			Collection<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(client.getCodigoAgenteMega(), clienteService.getCompanyCodeByClientName(client.getCliente().getNomeCliente()));
		    logger.info("paymentConditions  " + paymentConditions.toString());

			VwPmsIntegFaturaNacional vwFatura = vwIntegFaturaList.get(0).getClone();
			vwIntegFaturaList.remove(0);
			//Set new date as of today adding days quantity of the payment condition
			vwFatura.setInstallmentDueDate(DateUtil.sumDays(this.getPrazoPagamentoByCondicaoPagamento(paymentConditions, paymentConditionsAtual), new Date()));

			Date newDueDate = DateUtils.truncate(vwFatura.getInstallmentDueDate(),Calendar.DATE);
			fatura.setDataVencimento(newDueDate);
			fatura.setDataPrevisao(DateUtils.truncate(new Date(),Calendar.DATE));
			faturaDao.update(fatura);
			vwIntegFaturaList.add(0, vwFatura);
			return vwIntegFaturaList;
	}

	public Long getPrazoPagamentoByCondicaoPagamento(Collection<PaymentCondition> paymentConditions, PaymentConditionDealFiscal actual) {
		if (paymentConditions != null && paymentConditions.size() > 0){
			for (PaymentCondition paymentCondition : paymentConditions) {
				if (paymentCondition.getName().equals(actual.getPaymentConditionName())){
					logger.info("PaymentCondition " + paymentCondition.getDays());
					return paymentCondition.getDays();
				}
			}
		}
		return null;
	}

	@Transactional
	private void updateDueDateFaturaINC(Fatura fatura, DealFiscal dealFiscal) {
		final PaymentConditionDealFiscal paymentConditionsAtual = paymentConditionDealFiscalService.findActualPaymentCondition(dealFiscal.getCodigoDealFiscal());
		Cliente client = dealFiscal.getCliente();
		Collection<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(client.getCodigoAgenteMega(), clienteService.getCompanyCodeByClientName(client.getCliente().getNomeCliente()));

		Date newDueDate = DateUtils.truncate(DateUtil.sumDays(this.getPrazoPagamentoByCondicaoPagamento(paymentConditions, paymentConditionsAtual), new Date()),Calendar.DATE);
		fatura.setDataVencimento(newDueDate);
		fatura.setDataPrevisao(DateUtils.truncate(new Date(),Calendar.DATE));
		faturaDao.update(fatura);
	}


	/**
	 * Realiza a reintegra��o da fatura.
	 * 
	 * @param fatura
	 *            entidade Fatura.
	 * 
	 * @return retorna true se integra��o desfeita com sucesso, caso contrario
	 *         false.
	 */
	public boolean reIntegrarFatura(final Fatura fatura) {
		Fatura fat = faturaDao.findById(fatura.getCodigoFatura());
		// pega o status antigo / anterior da Fatura
		String indStatusOld = fat.getIndicadorStatus();

		Boolean isCanceled = faturaDao.isErpOrderCanceled(fat);

		if (isCanceled == null) {
			Messages.showError("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_DATABASE_ACCESS_ERP_ERROR);
			return false;
		} else if (isCanceled) {

			List<ItemFatura> itemFaturas = fat.getItemFaturas();
			for (ItemFatura itemFatura : itemFaturas) {
				itemFatura.setNumeroNotaFiscal(null);
				itemFatura.setNumeroPedido(null);
			}

			fat.setTextoError(null);
			fat.setIndicadorStatus(Constants.FATURA_STATUS_APPROVED);
			faturaDao.update(fat);
			// cria o HistoricoFatura caso tenha alterado o status
			hiFaService.createHistoricoFatura(fat);

			Messages.showSucess("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_REINTEGRATE_MSG_SUCCESS);
			return true;
		} else {
			Messages.showError("reIntegrarReceitaDeal",
					Constants.INTEGRACAO_RECEITA_MSG_ERROR_NOT_CANCELED);
			return false;
		}

	}

	/**
	 * Pega a data de fechamento do horas faturadas.
	 * 
	 * @return retorna a Data de Fechamento.
	 */
	private Date getClosingDateOfInvoice() {
		// pega a data de fechamento do modulo da fatura
		Modulo moduloInvoice = moduloService.findModuloById(new Long(
				systemProperties.getProperty(Constants.MODULO_FATURA_CODE)));

		return DateUtils.truncate(moduloInvoice.getDataFechamento(),
				Calendar.MONTH);
	}

	/**
	 * Busca uma lista de entidades nao fechadas, status igual a Open e Aproved.
	 * 
	 * @param dataMes
	 *            - data do fechamento.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Fatura> findFaturaAllNotClosed(final Date dataMes) {
		return faturaDao.findAllNotClosed(dataMes);
	}

	/**
	 * Busca todas as faturas atrasadas, ou seseja, com a data de pagamento
	 * passada e no status OPEN.
	 * 
	 * @return lista de faturas atrasadas.
	 */
	public List<Fatura> findFaturaDelayed() {
		return faturaDao.findDelayed();
	}

	/**
	 * Busca que est�o com a data pagamento para ser submetidas, nos pr�ximos 7
	 * dias.
	 * 
	 * @return lista de faturas a ser submetidas.
	 */
	public List<Fatura> findFaturaToBeSubmitted() {
		return faturaDao.findToBeSubmitted();
	}

	/**
	 * Envia o email das faturas atrasadas para os AE respons�veis.
	 */
	public void sendFaturaMailDelayed() {

		String subject = systemProperties
				.getProperty("fatura.mail.ae.delayed.subject");

		String description = systemProperties
				.getProperty("fatura.mail.ae.delayed.description");

		sendFaturaMailToAE(findFaturaDelayed(), subject, description);
	}

	/**
	 * Envia o email das faturas a serem submetidas para os AE respons�veis.
	 */
	public void sendFaturaMailToBeSubmitted() {
		String subject = systemProperties
				.getProperty("fatura.mail.ae.to_submitted.subject");

		String description = systemProperties
				.getProperty("fatura.mail.ae.to_submitted.description");

		sendFaturaMailToAE(findFaturaToBeSubmitted(), subject, description);
	}

	/**
	 * Busca uma lista de entidades no m�s passado por parametro.
	 * 
	 * @param dataMes
	 *            - data do mes.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	public List<Fatura> findFaturaByMonthDate(final Date dataMes) {
		return faturaDao.findByMonthDate(dataMes);
	}

	/**
	 * Envia um email aos AE respons�veis.
	 * 
	 * @param faturaList
	 *            lista de faturas a ser envadas por email.
	 * 
	 * @param mailSubject
	 *            assunto do email
	 * 
	 * @param mailDescription
	 *            descri��o do corpo do email.
	 */
	private void sendFaturaMailToAE(final List<Fatura> faturaList,
			final String mailSubject, final String mailDescription) {

		List<Fatura> fatDelayedListPerAE = new ArrayList<Fatura>();

		int size = faturaList.size();
		for (int i = 0; i < size; i++) {
			Fatura fatura = faturaList.get(i);

			fatDelayedListPerAE.add(fatura);

			// verifica se o login do AE atual mudou,
			// se mudou um email deve ser enviado
			if ((i + 1) >= size
					|| !fatura.getCodigoLoginAe().equals(
							faturaList.get(i + 1).getCodigoLoginAe())) {

				Map<String, Object> dataSource = new HashMap<String, Object>();
				dataSource.put("faturaList", fatDelayedListPerAE);
				dataSource.put("description", mailDescription);

				// pega o tamplete de email e insere os dados
				String message = mailSender.getTemplateMailMessage(
						"faturaAE.vm", dataSource);
				String to = pessoaService.findPessoaByLogin(
						fatura.getCodigoLoginAe()).getTextoEmail();
				// envia o email
				mailSender.sendHtmlMail(to, mailSubject, message);
				// inicializa a lista novamente
				fatDelayedListPerAE = new ArrayList<Fatura>();
			}

		}
	}

	/**
	 * Calcula o Due date.
	 * 
	 * @param cliente
	 * @param dataPrevisao
	 * @return (dataPrevisao + payment periodo do cliente)
	 */
	public Date calculateDueDate(Cliente cliente, Date dataPrevisao) {

		Date dataVencimento = DateUtil.sumDays(
				cliente.getNumeroDiasPrazoRecebimento(), dataPrevisao);

		// pega o proximo dia util se necessario
		dataVencimento = DateUtil.nextWorkDay(dataVencimento);

		return dataVencimento;
	}

	public Fatura findFaturaApagadaById(Long id) {
		return faturaDao.findFaturaApagadaById(id);
	}


	public void validateRequiredFields(VwPmsIntegFaturaNacional vwPmsIntegFaturaNacional) {
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getBranchCompanyCode(), "Branch Company Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getIssueAt(), "Issue Date cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getSentAt(), "Sent Date cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getPaymentConditionName(), "Payment Condition Name cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getInvoiceSituation(), "Invoice Situation cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getDocumentTypeCode(), "Document Type cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getAuxCode(), "Auxiliary Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getCalcAgentCode(), "Calc Agent Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getCalcAgentTypeCode(), "Calc Agent Type Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getItemSequencial(), "Item Sequencial Number cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getItemCode(), "Item Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getItemQuantity(), "Item Quantity cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getItemApplicationCode(), "Item Applicatoin Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getClassType(), "Item Class Type cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getUnitValue(), "Item Unit Value cannot be null, check currency quotation exists");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getIdProjectCode(), "Project ID Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getProjectReduceCode(), "Project Reduce Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getIdCostCenterCode(), "ID CostCenter Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getCostCenterReduceCode(), "CostCenter Reduce Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getServiceCode(), "Item Service Code cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getInvoiceObservationType(), "Invoice Observation Type cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getInvoiceObservation(), "Invoice Observation cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getInstallmentNumber(), "Installment Number cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getInstallmentDueDate(), "Installment Due Date cannot be null");
		Preconditions.checkNotNull(vwPmsIntegFaturaNacional.getInstallmentValue(), "Installment Value cannot be null, check currency quotation exists");
	}

	public IncomingInvoice convertFromModel(List<VwPmsIntegFaturaNacional> model) {
		IncomingInvoice incomingInvoice = new IncomingInvoice();
		Long sequence = 1L;

		incomingInvoice.setBranchCompanyCode(model.get(0).getBranchCompanyCode());
		incomingInvoice.setIssueAt(model.get(0).getIssueAt());
		incomingInvoice.setSentAt(model.get(0).getSentAt());
		incomingInvoice.setPaymentConditionName(model.get(0).getPaymentConditionName());
		incomingInvoice.setInvoiceCode(model.get(0).getInvoiceCode());
		incomingInvoice.setInvoiceSituation(model.get(0).getInvoiceSituation());
		incomingInvoice.setDocumentTypeCode(model.get(0).getDocumentTypeCode());
		incomingInvoice.setAuxCode(model.get(0).getAuxCode());
		incomingInvoice.setCalcAgentCode(model.get(0).getCalcAgentCode());
		incomingInvoice.setCalcAgentTypeCode(model.get(0).getCalcAgentTypeCode());


		List<RevenueItem> invoiceItens = new ArrayList<RevenueItem>();

		for (VwPmsIntegFaturaNacional item : model){
			RevenueItem revenueItem = new RevenueItem();
			revenueItem.setItemOperationCode(item.getItemOperationCode());
			revenueItem.setItemSequencial(sequence);
			revenueItem.setItemCode(item.getItemCode());
			revenueItem.setItemQuantity(item.getItemQuantity());
			revenueItem.setUnitValue(item.getUnitValue());
			revenueItem.setItemApplicationCode(item.getItemApplicationCode());
			revenueItem.setClassType(item.getClassType());
			revenueItem.setIdProjectCode(item.getIdProjectCode());
			revenueItem.setProjectReduceCode(item.getProjectReduceCode());
			revenueItem.setIdCostCenterCode(item.getIdCostCenterCode());
			revenueItem.setCostCenterReduceCode(item.getCostCenterReduceCode());
			revenueItem.setServiceCode(item.getServiceCode());
			invoiceItens.add(revenueItem);
			sequence = sequence + 1;
		}

		incomingInvoice.setInvoiceItens(invoiceItens);


		InvoiceObservation invoiceObservation = new InvoiceObservation();
		invoiceObservation.setInvoiceObservationOperation(model.get(0).getInvoiceObservationOperation());
		invoiceObservation.setInvoiceObservationType(model.get(0).getInvoiceObservationType());
		invoiceObservation.setInvoiceObservation(model.get(0).getInvoiceObservation());

		incomingInvoice.setInvoiceObservation(invoiceObservation);

		InvoiceInstallment invoiceInstallment = new InvoiceInstallment();
		invoiceInstallment.setInstallmentOperationCode(model.get(0).getInstallmentOperationCode());
		invoiceInstallment.setInstallmentNumber(model.get(0).getInstallmentNumber());
		invoiceInstallment.setInstallmentValue(model.get(0).getInstallmentValue());
		invoiceInstallment.setInstallmentDueDate(model.get(0).getInstallmentDueDate());

		incomingInvoice.setInstallment(invoiceInstallment);
		logger.info("IncomingInvoice: " + incomingInvoice.toString());
		return incomingInvoice;
	}

}