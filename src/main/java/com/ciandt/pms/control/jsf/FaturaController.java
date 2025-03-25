/**
 * Classe FaturaController - Classe da camada de apresenta��o do Fatura.
 */
package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.business.service.impl.FaturaService;
import com.ciandt.pms.business.service.impl.PaymentConditionService;
import com.ciandt.pms.control.jsf.bean.FaturaBean;
import com.ciandt.pms.control.jsf.bean.ItemFaturaBean;
import com.ciandt.pms.control.jsf.bean.MessageControlBean;
import com.ciandt.pms.control.jsf.converters.StatusFaturaConverter;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.FaturaRow;
import com.ciandt.pms.model.vo.ItemFaturaRow;
import com.ciandt.pms.model.vo.PaymentCondition;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.MailSenderUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import java.io.IOException;
import java.util.*;

/**
 * 
 * A classe FaturaController proporciona as funcionalidades da camada de
 * apresenta��o para a entidade Fatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BUS.INVOICE:CR", "BUS.INVOICE:ED", "BUS.INVOICE:VW", "BOF.INVOICING:CR", "BOF.INVOICING:ED", "BOF.INVOICING:VW", "BOF.INVOICE_PAYMENT:VW" })
public class FaturaController {

	private static final Logger logger = LoggerFactory.getLogger(FaturaController.class);
	@Autowired
	private Properties systemProperties;

	/*********** OUTCOMES **************************/

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_FATURA_ADD = "fatura_add";
	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_FATURA_SEARCH = "fatura_search";
	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_FATURA_SEARCH_FINANCIAL = "fatura_search_financial";
	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_ITEM_FATURA_SEARCH = "fatura_payment_search";
	/** outcome tela gerenciamento da entidade. */
	private static final String OUTCOME_FATURA_MANAGE = "fatura_manage";
	/** outcome tela gerenciamento da entidade. */
	private static final String OUTCOME_FATURA_MANAGE_FINANCIAL = "fatura_manage_financial";
	/** outcome tela gerenciamento da entidade - view. */
	private static final String OUTCOME_FATURA_MANAGE_VIEW = "fatura_manage_view";
	/** outcome tela gerenciamento da entidade - view. */
	private static final String OUTCOME_FATURA_MANAGE_FINANCIAL_VIEW = "fatura_manage_financial_view";

	/*********** SERVICES **************************/

	/** Instancia do servico. */
	@Autowired
	private IFaturaService faturaService;

	/** Instancia do servico Cliente. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico Moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do servico TipoServico. */
	@Autowired
	private ITipoServicoService tipoServicoService;

	/** Instancia do servico DealFiscal. */
	@Autowired
	private IDealFiscalService dealFiscalService;

	/** Instancia do servico ItemFatura. */
	@Autowired
	private IItemFaturaService itemFaturaService;

	/** Instancia do servico FonteReceita. */
	@Autowired
	private IFonteReceitaService fonteReceitaService;

	/** Instancia do servico ContratoPratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico Empresa. */
	@Autowired
	private IEmpresaService empresaService;

	/** Instancia do servico ComissaoFatura. */
	@Autowired
	private IComissaoFaturaService comissaoFaturaService;

	/** Instancia do servi�o de Msa. */
	@Autowired
	private IMsaService msaService;

	/** Instancia dp servico de centro de lucro. */
	@Autowired
	private ICentroLucroService centroLucroService;

	/** Instancia de ReceitaDealFiscalService. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	/** Instancia de FatuaReceitaSerivec. */
	@Autowired
	private IFaturaReceitaService faturaReceitaService;
	
	/** Instancia de vwFiscalBalanceAcumuladoService. */
	@Autowired
	private IVwFiscalBalanceAcumuladoService vwFiscalBalanceAcumuladoService;

	/** Instancia de HistoricoFaturaService */
	@Autowired
	private IHistoricoFaturaService historicoFaturaService;
	
	/** Instancia de FaturaApagadaService */
	@Autowired
	private IFaturaApagadaService faturaApagadaService;
	
	/** Instancia de ItemFaturaApagadaService */
	@Autowired
	private IItemFaturaApagadoService itemFaturaApagadoService;

	@Autowired
	private IPaymentConditionDealFiscalService paymentConditionDealFiscalService;

	@Autowired
	private ICompanyErpService companyErpService;

	@Autowired
	private PaymentConditionService paymentConditionService;

	@Autowired
	private MailSenderUtil mailSenderUtil;

	/*********** BEANS **************************/

	/** Instancia do bean. */
	@Autowired
	private FaturaBean bean = null;

	/** Instancia do bean ItemFatura. */
	@Autowired
	private ItemFaturaBean itemFaturaBean = null;

	/** Instancia do bean controle de mensagnes. */
	@Autowired
	private MessageControlBean messageControlBean = null;

	/********************************************/

	/**
	 * @return the messageControlBean
	 */
	public MessageControlBean getMessageControlBean() {
		return messageControlBean;
	}

	/**
	 * @param messageControlBean
	 *            the messageControlBean to set
	 */
	public void setMessageControlBean(
			final MessageControlBean messageControlBean) {
		this.messageControlBean = messageControlBean;
	}

	/**
	 * @return the itemFaturaBean
	 */
	public ItemFaturaBean getItemFaturaBean() {
		return itemFaturaBean;
	}

	/**
	 * @param itemFaturaBean
	 *            the itemFaturaBean to set
	 */
	public void setItemFaturaBean(final ItemFaturaBean itemFaturaBean) {
		this.itemFaturaBean = itemFaturaBean;
	}

	/**
	 * Pega o bean FaturaBean.
	 * 
	 * @return retorna o bean
	 */
	@PermitAll
	public FaturaBean getBean() {
		return bean;
	}

	/**
	 * Seta o bean FaturaBean.
	 * 
	 * @param bean
	 *            do tipo FaturaBean
	 */
	@PermitAll
	public void setBean(final FaturaBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela para criar uma entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareCreate() {

		bean.reset();
		// marca como modo de inser��o
		bean.setIsUpdate(Boolean.valueOf(false));

		// carrega as listas de entidades
		loadClienteList(clienteService.findClienteAllClientePai());
		// carrega a lista de AE
		loadAEList(pessoaService.findPessoaAllAccountExecutive());

		return OUTCOME_FATURA_ADD;
	}

	/**
	 * Verifica se o valor do atributo ContratoPratica � valido. Se o valor n�o
	 * � nulo e existe no contratoPraticaMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateDataPrevisao(final FacesContext context,
			final UIComponent component, final Object value) throws IOException {

		Date dtValue = (Date) value;

		if (dtValue != null) {
			bean.setEnableExpiryDate(Boolean.valueOf(true));

			Long codigoDealFiscal = bean.getDealFiscalMap().get(
					bean.getTo().getDealFiscal().getNomeDealFiscal());

			if (codigoDealFiscal == null) {
				
				codigoDealFiscal = bean.getTo().getDealFiscal()
						.getCodigoDealFiscal();
			}

			if (codigoDealFiscal != null) {
				DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(codigoDealFiscal);

				final PaymentConditionDealFiscal paymentConditionsAtual = paymentConditionDealFiscalService.findActualPaymentCondition(dealFiscal.getCodigoDealFiscal());

				Cliente client = dealFiscal.getCliente();

				Collection<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(client.getCodigoAgenteMega(), clienteService.getCompanyCodeByClientName(client.getCliente().getNomeCliente()));
				Long prazoRecebimento = this.getPrazoPagamentoByCondicaoPagamento(paymentConditions, paymentConditionsAtual);

				if (prazoRecebimento != null) {
					Date dataVencimento = DateUtil.sumDays(prazoRecebimento, dtValue);
					dataVencimento = DateUtil.nextWorkDay(dataVencimento);
					bean.getTo().setDataVencimento(dataVencimento);
				} else {
					Messages.showError("findById",
							Constants.INVOICE_NO_PAYMENT_PERIOD, dealFiscal.getNomeDealFiscal());
				}
			}
		}

		if (DateUtil.isSameDate(dtValue, new Date(), Calendar.DATE)) {
			bean.setShowMessageError(Boolean.valueOf(false));
		} else {
			bean.setShowMessageError(Boolean.valueOf(true));
		}
	}

	private Long getPrazoPagamentoByCondicaoPagamento(Collection<PaymentCondition> paymentConditions, PaymentConditionDealFiscal actual) {
		if (paymentConditions != null && paymentConditions.size() > 0 && actual != null){
			for (PaymentCondition paymentCondition : paymentConditions) {
				if (paymentCondition.getName().equals(actual.getPaymentConditionName())){
					return paymentCondition.getDays();
				}
			}
		}
		return null;
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @return pagina de destino
	 */
	public String create() {
		Fatura fatura = verifyCreateOrUpdate();

		if (fatura != null) {

			// Busca SSO
			Long codigoSSO = bean.getSSOMap().get(
					bean.getTo().getCentroLucroSso().getNomeCentroLucro());
			CentroLucro centroLucroSSO = centroLucroService
					.findCentroLucroById(codigoSSO);

			// Busca UMKT
			Long codigoUMKT = bean.getUMKTMap().get(
					bean.getTo().getCentroLucroUmkt().getNomeCentroLucro());
			CentroLucro centroLucroUMKT = centroLucroService
					.findCentroLucroById(codigoUMKT);

			// seta os centros de lucro
			fatura.setCentroLucroSso(centroLucroSSO);
			fatura.setCentroLucroUmkt(centroLucroUMKT);

			fatura.setIndicadorStatus(Constants.FATURA_STATUS_OPEN);

			// cria a Fatura
			if (faturaService.createFatura(fatura)) {

				// mensagem de sucesso
				Messages.showSucess("create",
						Constants.DEFAULT_MSG_SUCCESS_CREATE,
						Constants.ENTITY_NAME_FATURA);

				bean.setCurrentRowId(fatura.getCodigoFatura());

				// carrega a tela de gerenciamento (altera��o da Fatura)
				return prepareManage();

			} else {
				return null;
			}
		}

		// se existir algum erro com as entidades, retorna null
		return null;
	}

	/**
	 * Altera uma entidade.
	 * 
	 * @return pagina de destino
	 */
	public String update() {
		Fatura fatura = bean.getTo();
		Fatura faturaAux = faturaService.findFaturaById(fatura
				.getCodigoFatura());
		String indStatusOld = faturaAux.getIndicadorStatus();
		String indStatusNew = fatura.getIndicadorStatus();
		// se � Open e est� alterando para Approved (caso n�o seja nem ADMIN nem
		// CONTRACT) caso seja SR_MANAGER ou AE, se data da fatura < sysdate,
		// n�o edita. Somente vai editar se data >= sysdate
		if (!indStatusOld.equals(indStatusNew)) {
			if (!isFaturaEditable(indStatusNew)) {
				if (DateUtil.before(fatura.getDataPrevisao(), new Date(),
						Calendar.DAY_OF_MONTH)) {
					Messages.showError("update",
							Constants.FATURA_MSG_WARNG_OUTDATED);
					return null;
				}
			}
		}

		fatura = verifyCreateOrUpdate();

		if (fatura != null) {

			// atualiza a Fatura
			if (faturaService.updateFatura(fatura)) {
				// mensagem de sucesso
				Messages.showSucess("update",
						Constants.DEFAULT_MSG_SUCCESS_UPDATE,
						Constants.ENTITY_NAME_FATURA);

				bean.setCurrentRowId(fatura.getCodigoFatura());

				// carrega a tela de gerenciamento (altera��o da Fatura)
				return prepareManage();
			} else {
				return null;
			}
		}

		// se existir algum erro com as entidades, retorna null
		return null;
	}

	/**
	 * Verifica se o usuario e do tipo admin.
	 * 
	 * @return true se o usuario e admin, caso contrario false
	 */
	private Boolean isAdmin() {

		GrantedAuthority[] loggedUserAuthorities = LoginUtil
				.getLoggedUserAuthorities();

		for (GrantedAuthority authority : loggedUserAuthorities) {
			// ADMIN tem maior preced�ncia, portanto, se o
			// usu�rio tem este perfil, sai do loop. Caso
			// contr�rio, executa at� o fim do loop
			if (Constants.ROLE_ADMIN.equals(authority.getAuthority())) {
				return (Boolean.valueOf(true));
			}
		}

		return (Boolean.valueOf(false));
	}

	/**
	 * Verifica se a fatura � edit�vel segundo a regra se � Open e est�
	 * alterando para Approved (caso n�o seja nem ADMIN nem CONTRACT) caso seja
	 * SR_MANAGER ou AE, se data da fatura < sysdate, n�o edita. Somente vai
	 * editar se data >= sysdate
	 * 
	 * @param indicadorStatus
	 *            - novo status da Fatura
	 * 
	 * @return true se ela pode ser edit�vel, caso contr�rio false
	 */
	private Boolean isFaturaEditable(final String indicadorStatus) {
		Boolean isFaturaEditable = Boolean.valueOf(true);

		if (Constants.FATURA_STATUS_APPROVED.equals(indicadorStatus)) {
			GrantedAuthority[] loggedUserAuthorities = LoginUtil
					.getLoggedUserAuthorities();
			for (GrantedAuthority authority : loggedUserAuthorities) {
				// ADMIN ou CONTRACT tem maior preced�ncia, portanto, se o
				// usu�rio tem um destes perfis, sai do loop. Caso
				// contr�rio, executa at� o fim do loop
				if (Constants.ROLE_ADMIN.equals(authority.getAuthority())
						|| Constants.ROLE_CONTRACT.equals(authority
								.getAuthority())
						|| Constants.ROLE_FINANCIAL.equals(authority
								.getAuthority())) {
					isFaturaEditable = Boolean.valueOf(true);
					break;
				} else {
					isFaturaEditable = Boolean.valueOf(false);
				}
			}
		} else if (Constants.FATURA_STATUS_SUBMITTED.equals(indicadorStatus)) {
			isFaturaEditable = Boolean.valueOf(false);
		}

		return isFaturaEditable;
	}

	/**
	 * Altera uma entidade no estado SB.
	 * 
	 * @return pagina de destino
	 */
	public String updateSubmitted() {
		Fatura fatura = verifyCreateOrUpdate();

		if (fatura != null) {

			// verifica se foi pago comiss�o para algum item da fatura
			if (!commissionWasPaid(fatura.getItemFaturas())) {

				bean.setIsCommisioned(Boolean.FALSE);
				// caso n�o haja comiss�o paga atualiza a Fatura
				if (faturaService.updateFatura(fatura)) {
					// mensagem de sucesso
					Messages.showSucess("update",
							Constants.DEFAULT_MSG_SUCCESS_UPDATE,
							Constants.ENTITY_NAME_FATURA);

					bean.setCurrentRowId(fatura.getCodigoFatura());

					// carrega a tela de gerenciamento (altera��o da Fatura)
					return prepareManage();
				} else {
					return null;
				}
			} else {
				// retorna null caso haja comiss�o paga para exibi��o da
				// mensagem
				bean.setIsCommisioned(Boolean.TRUE);
				return null;
			}
		}
		// se existir algum erro com as entidades, retorna null
		return null;
	}

	/**
	 * Verifica se foi paga comissao para algum item da fatura.
	 * 
	 * @param itensFatura
	 *            - lista de itens da Fatura
	 * 
	 * @return Boolean
	 */
	public Boolean commissionWasPaid(final List<ItemFatura> itensFatura) {

		for (Iterator<ItemFatura> iterator = itensFatura.iterator(); iterator
				.hasNext();) {
			ItemFatura itemFatura = (ItemFatura) iterator.next();

			List<ComissaoFatura> comissaoFatura = comissaoFaturaService
					.findByItemFatura(itemFatura);

			if (!comissaoFatura.isEmpty()) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;

	}

	/**
	 * Prepara a tela de Remo��o da entidade.
	 * 
	 * @return retorna a pagina de Remo��o da Fatura
	 */
	public String prepareRemove() {
		// marca o modo de remo��o como true para que a tela mostre
		// o bot�o de excluir
		bean.setIsRemove(Boolean.valueOf(true));

		// carrega a Fatura em modo de visualiza��o
		loadFaturaView();

		// totaliza os ItemFatura
		updateItemFaturaTotalValues();

		return OUTCOME_FATURA_MANAGE_VIEW;
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @return pagina de destino
	 * 
	 */
	public String remove() {

		try {
			Fatura fatura = faturaService
					.findFaturaById(bean.getCurrentRowId());

			if (fatura.getFaturaReceitas().isEmpty()) {
				// tenta remover a Fatura
				faturaService.removeFatura(fatura);
			} else {
				Messages.showSucess("remove",
						Constants.FATURA_MSG_ERROR_REMOVE_APROPRIADA, ""
								+ fatura.getCodigoFatura());

				return null;
			}
		} catch (DataIntegrityViolationException e) {
			Messages.showError("remove",
					Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_FATURA);

			return null;
		}

		// caso seja removido com sucesso, d� mensagem de sucesso
		Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_FATURA);
		bean.resetTo();
		// faz a busca novamente para recarregar a lista da pesquisa
		findByFilter();

		return OUTCOME_FATURA_SEARCH;
	}

	/**
	 * Faz a verifica��o das entidades para criar ou editar uma Fatura.
	 * 
	 * @return Fatura - retorna a Fatura
	 */
	private Fatura verifyCreateOrUpdate() {

		if (bean.getTo().getDataVencimento() != null) {
			if (!bean.getTo().getDataVencimento()
					.after(bean.getTo().getDataPrevisao())) {
				Messages.showError("verifyCreateOrUpdate",
						Constants.DEFAULT_MSG_ERROR_DATE_GT_EXP_DATE);

				return null;
			}
		}

		Fatura to = bean.getTo();
		Fatura fatura = to;

		Long codigoDealFiscal;

		if (to.getDealFiscal() != null
				&& to.getDealFiscal().getCodigoDealFiscal() != null) {
			codigoDealFiscal = to.getDealFiscal().getCodigoDealFiscal();
		} else {
			codigoDealFiscal = bean.getDealFiscalMap().get(
					to.getDealFiscal().getNomeDealFiscal());
		}

		DealFiscal dealFiscal = null;
		if (codigoDealFiscal != null) {
			dealFiscal = dealFiscalService.findDealFiscalById(codigoDealFiscal);

			fatura.setDealFiscal(dealFiscal);
			// seta a moeda
			fatura.setMoeda(dealFiscal.getMoeda());
		}

		return fatura;
	}

	/**
	 * Prepara a tela de gerenciamento da Fatura - atualizacao.
	 * 
	 * @return pagina de destino
	 */
	public String prepareManage() {
		// limpa os dados do formul�rio do ItemFatura
		itemFaturaBean.reset();
		itemFaturaBean.setIsUpdate(false);
		// marca como modo de alter��o
		bean.setIsUpdate(Boolean.TRUE);
		
		// Seta o backTo para tela de view, no caso do usuario clicar no hiperlink do Published FB
		bean.setBackTo(FaturaBean.BACK_TO_FATURA_MANAGE);

		// busca a Fatura corrente pelo c�digo
		this.findById(bean.getCurrentRowId());

		// pega a entidade Fatura
		Fatura fatura = faturaService.findFaturaById(bean.getTo()
				.getCodigoFatura());

		// se existir a Fatura redireciona para a
		// tela de gerenciamento dos ItemFatura
		if (fatura != null) {
			// carrega as listas de entidades
			loadTipoServicoList(fatura.getDealFiscal().getTipoServicos());
			loadFonteReceitaList(fonteReceitaService.findFonteReceitaAll());

			// atribui a Moeda ao bean
			bean.setPatternCurrency(faturaService.getCurrency(fatura.getMoeda()));

			// totaliza os ItemFatura
			updateItemFaturaTotalValues();
			// atualiza a lista de HistoricoFatura
			this.prepareHistoricoFatura();

			// se status Opened edita normalmente. Se status Approved, somente
			// ADMIN e CONTRACT podem alterar a Fatura. Se status Submitted, n�o
			// edita
			bean.resetIsFaturaEditable();
			bean.setIsFaturaEditable(this.isFaturaEditable(fatura
					.getIndicadorStatus()));

			// Verifica se o usuario e ADMIN para edicao da data de vencimento
			bean.setIsAdmin(this.isAdmin());

			// desabilita combo de contrato pratica
			itemFaturaBean.setEnableCLob(Boolean.FALSE);

			return OUTCOME_FATURA_MANAGE;
			// se a Fatura nao existir na base de dados
		} else {
			Messages.showError("prepareManage",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_FATURA);
		}

		// retorna null caso n�o seja validado
		return null;
	}

	/**
	 * Calcula o published fiscal balance
	 * 
	 * @param dealFiscal
	 * @return
	 */
	private Double calculatePublishedFB(final DealFiscal dealFiscal) {
		List<ReceitaDealFiscal> receitaDealFiscalList = receitaDealFiscalService
				.findPublishedAndIntegrateByDealFiscal(dealFiscal);
		Double totalPublished = new Double(0);
		for (ReceitaDealFiscal receitaDealFiscal : receitaDealFiscalList) {
			totalPublished += faturaReceitaService
					.calculatePublishFiscalBalance(receitaDealFiscal);
		}

		return totalPublished;
	}

	/**
	 * Prepara a aba e carrega a lista de HistoricoFatura.
	 * 
	 */
	public void prepareHistoricoFatura() {

		List<HistoricoFatura> historicoFaturaList = new ArrayList<HistoricoFatura>();

		if (bean.getSearchDeletedInvoice()) {
			historicoFaturaList = historicoFaturaService
					.findByCodigoFatura(bean.getTo().getCodigoFatura());
		} else {
			// pega a entidade Fatura
			Fatura fatura = faturaService.findFaturaById(bean.getTo()
					.getCodigoFatura());
			historicoFaturaList = fatura
					.getHistoricoFaturas();
		}

		this.orderHistoricoFaturaList(historicoFaturaList);
		bean.setHistoricoFaturaList(historicoFaturaList);
	}

	/**
	 * Ordena a lista de HistoricoFatura.
	 * 
	 * @param historicoFaturaList
	 *            - lista de HistoricoFatura da Fatura corrente
	 */
	private void orderHistoricoFaturaList(
			final List<HistoricoFatura> historicoFaturaList) {
		Collections.sort(historicoFaturaList,
				new Comparator<HistoricoFatura>() {
					public int compare(final HistoricoFatura hf1,
							final HistoricoFatura hf2) {
						return hf1.getDataAlteracao().compareTo(
								hf2.getDataAlteracao());
					}
				});
	}

	/**
	 * Prepara os dados para a tela de gerenciamento do financeiro.
	 * 
	 * @return pagina de gerenciamento do financeiro
	 */
	public String prepareManageFinancial() {

		bean.setEnableDate(Boolean.valueOf(true));
		bean.setEnableExpiryDate(Boolean.valueOf(true));

		Fatura fatura = faturaService.findFaturaById(bean.getTo()
				.getCodigoFatura());

		bean.setDocEditable(canDocBeEditable(fatura));
		bean.setTo(fatura);

		// atualiza a lista de HistoricoFatura
		this.prepareHistoricoFatura();

		// atribui a Moeda ao bean
		bean.setPatternCurrency(faturaService.getCurrency(fatura.getMoeda()));

		// totaliza os ItemFatura
		updateItemFaturaTotalValues();

		// se status Opened edita normalmente. Se status Approved, somente
		// ADMIN e CONTRACT podem alterar a Fatura. Se status Submitted, n�o
		// edita
		bean.resetIsFaturaEditable();
		bean.setIsFaturaEditable(this.isFaturaEditable(fatura
				.getIndicadorStatus()));

		if (DateUtil.isSameDate(fatura.getDataPrevisao(), new Date(),
				Calendar.DATE)) {
			bean.setShowMessageError(Boolean.valueOf(false));
		} else {
			bean.setShowMessageError(Boolean.valueOf(true));
		}

		return OUTCOME_FATURA_MANAGE_FINANCIAL;
	}

	/**
	 * Prepara a tela de visualiza��o da entidade.
	 * 
	 * @return retorna a pagina de visualiza��o da Fatura
	 */
	@PermitAll
	public String prepareView() {
		setLoadValesView();

		// Seta o backTo para tela de view, no caso do usuario clicar no hiperlink do Published FB
		bean.setBackTo(FaturaBean.BACK_TO_FATURA_MANAGE_VIEW);

		return OUTCOME_FATURA_MANAGE_VIEW;
	}

	/**
	 * Prepara a tela de visualiza��o da entidade.
	 *
	 * @return retorna a pagina de visualiza��o da Fatura Invoicing
	 */
	@PermitAll
	public String prepareViewFinancial() {
		setLoadValesView();

		// Seta o backTo para tela de view, no caso do usuario clicar no hiperlink do Published FB
		bean.setBackTo(FaturaBean.BACK_TO_FATURA_MANAGE_FINANCIAL_VIEW);

		return OUTCOME_FATURA_MANAGE_FINANCIAL_VIEW;
	}

	private void setLoadValesView() {
		// marca o modo de remo��o como false (view) para que a tela n�o mostre
		// o bot�o de excluir
		bean.setIsRemove(Boolean.valueOf(false));

		// carrega a Fatura em modo de visualiza��o
		loadFaturaView();

		// totaliza os ItemFatura
		updateItemFaturaTotalValues();

		bean.setPublishedFiscalBalance(vwFiscalBalanceAcumuladoService
				.findPublishedFiscalBalanceByDealFiscal(bean.getTo()
						.getDealFiscal()));
		bean.setPublishedFiscalBalance2(this.calculatePublishedFB(bean.getTo()
				.getDealFiscal()));
	}

	/**
	 * Prepara o modal do view da fatura.
	 */
	@PermitAll
	public void prepareViewModal() {
		this.prepareView();
	}

	/**
	 * Prepara o modal do view da fatura.
	 */
	@PermitAll
	public void prepareViewFinancialModal() {
		this.prepareViewFinancial();
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareSearch() {
		bean.reset();

		// carrega as listas de entidades
		loadClienteList(clienteService.findClienteAllClientePai());
		loadMoedaList(moedaService.findMoedaAll());

		this.setFilterDate();

		return OUTCOME_FATURA_SEARCH;
	}

	/**
	 * Prepara a tela de busca por item de fatura.
	 * 
	 * @return retorna a pagina de busca.
	 */
	public String prepareFindItemFaturaByFilter() {

		setFilterDate();

		// carrega as listas de entidades
		loadClienteList(clienteService.findClienteAllClienteFilho());
		loadEmpresaList(empresaService.findEmpresaAllSubsidiary());

		return OUTCOME_ITEM_FATURA_SEARCH;
	}

	/**
	 * Seta o o range da datas do filtro. - Data inicial = (Data de hoje) - (1
	 * m�s) - Data final = (Data corrente) + (3 meses)
	 */
	private void setFilterDate() {
		Date date = DateUtils.truncate(new Date(), Calendar.MONTH);

		if (bean.getDataPrevisaoBeg() == null) {
			bean.setDataPrevisaoBeg(DateUtils.addMonths(date, -1));
			bean.setDataPrevisaoBeg2(DateUtils.addMonths(date, -1));
		}
		if (bean.getDataPrevisaoEnd() == null) {
			bean.setDataPrevisaoEnd(DateUtils.addMonths(date, 2));
			bean.setDataPrevisaoEnd2(DateUtils.addMonths(date, 2));
		}
	}

	/**
	 * Prepara a tela de busca para o financeiro.
	 * 
	 * @return pagina de busca.
	 */
	public String prepareSearchFinancial() {
		bean.reset();
		bean.setFinancialMode(Boolean.TRUE);

		// carrega as listas de entidades
		loadClienteList(clienteService.findClienteAllClientePai());
		loadMoedaList(moedaService.findMoedaAll());

		this.setFilterDate();

		return OUTCOME_FATURA_SEARCH_FINANCIAL;
	}

	/**
	 * Carrega os conteudo de uma Fatura para exibir na tela.
	 * 
	 */
	private void loadFaturaView() {
		// busca a Fatura corrente pelo c�digo
		findById(bean.getCurrentRowId());
	}

	/**
	 * Cancela a atualiza��o de uma Fatura.
	 * 
	 * @return pagina de destino
	 */
	public String cancelFatura() {
		findByFilter();

		return OUTCOME_FATURA_SEARCH;
	}

	/**
	 * Cancela a atualiza��o de uma Fatura.
	 *
	 * @return pagina de destino
	 */
	public String cancelFaturaFinancial() {
		findByFilter();

		return OUTCOME_FATURA_SEARCH_FINANCIAL;
	}

	/**
	 * Prepara a tela para o cancelamento da Fatura .
	 * 
	 */
	public void prepareCancelSubmitFatura() {
		bean.resetCancelFields();
		messageControlBean.setDisplayMessages(Boolean.valueOf(false));
	}

	/**
	 * Faz o cancelamento da Fatura caso ela j� tenha sido submetida. Verifica
	 * se tem associa��o com a Revenue (Fiscal Balance). Se N�O tiver, cancela.
	 * 
	 */
	public void cancelSubmitFatura() throws Exception {
		Fatura fatura = bean.getTo();
		Date dataCancelamento = bean.getDataCancelamento();
		String textoRazaoCancelamento = bean.getTextoRazaoCancelamento();

		// cancela a Fatura
		Boolean cancelSubmitFaturaOk = faturaService.cancelSubmitFatura(fatura,
				dataCancelamento, textoRazaoCancelamento);

		// se cancelamento da Fatura ok, volta o status normal das mensagens na
		// tela principal
		if (cancelSubmitFaturaOk) {
			messageControlBean.setDisplayMessages(Boolean.valueOf(true));

			findById(fatura.getCodigoFatura());
		}
	}

	/**
	 * Cria uma entidade do tipo ItemFatura.
	 */
	public void createItemFatura() {

		ItemFatura itemFatura = verifyCreateOrUpdateItemFatura();

		if (itemFatura.getValorItem().doubleValue() < 0){
			Messages.showError("createItemFatura",
					Constants.MSG_ERROR_ITEM_FATURA_NEGATIVE_VALUE);
			return;
		}

		// se a fonte de receita n�o for "Service", o contrato pr�tica �
		// obrigat�rio
		if (!Constants.TIPO_FONTE_RECEITA_SERVICE.equals(itemFatura
				.getFonteReceita().getIndicadorTipo())
				&& itemFatura.getContratoPratica() == null) {
			Messages.showError("createItemFatura",
					Constants.MSG_ERROR_ITEM_RECEITA_REQUIRED_CLOB);
			return;
		}

		// cria o ItemFatura
		itemFaturaService.createItemFatura(itemFatura);

		// mensagem de sucesso
		Messages.showSucess("createItemFatura",
				Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_ITEM_FATURA);

		// limpa o bean do ItemFatura
		itemFaturaBean.reset();
		// atualiza a lista de ItemFatura na Fatura corrente
		findById(bean.getTo().getCodigoFatura());

		// totaliza os ItemFatura
		updateItemFaturaTotalValues();
	}

	/**
	 * Prepara a tela para update de um ItemFatura.
	 */
	public void prepareUpdateItemFatura() {
		itemFaturaBean.setIsUpdate(Boolean.valueOf(true));

		itemFaturaBean.setEnableCLob(!Constants.TIPO_FONTE_RECEITA_SERVICE
				.equals(itemFaturaBean.getTo().getFonteReceita()
						.getIndicadorTipo()));
	}

	/**
	 * Atualiza uma entidade do tipo ItemFatura.
	 */
	public void updateItemFatura() {
		ItemFatura itemFatura = verifyCreateOrUpdateItemFatura();

		if (itemFatura.getValorItem().doubleValue() < 0){
			Messages.showError("createItemFatura",
					Constants.MSG_ERROR_ITEM_FATURA_NEGATIVE_VALUE);
			return;
		}

		// se a fonte de receita n�o for "Service", o contrato pr�tica �
		// obrigat�rio
		if (!Constants.TIPO_FONTE_RECEITA_SERVICE.equals(itemFatura
				.getFonteReceita().getIndicadorTipo())
				&& itemFatura.getContratoPratica() == null) {
			Messages.showError("createItemFatura",
					Constants.MSG_ERROR_ITEM_RECEITA_REQUIRED_CLOB);
			return;
		}

		// atualiza o ItemFatura
		itemFaturaService.updateItemFatura(itemFatura);

		this.historicoFaturaService.createHistoricoFatura(itemFatura.getFatura());

		// mensagem de sucesso
		Messages.showSucess("updateItemFatura",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_ITEM_FATURA);

		// limpa o bean do ItemFatura
		itemFaturaBean.reset();
		// atualiza a lista de ItemFatura na Fatura corrente
		findById(bean.getTo().getCodigoFatura());

		// totaliza os ItemFatura
		updateItemFaturaTotalValues();
	}

	/**
	 * Realiza o update da lista dos itens da fatura.
	 */
	public void updateItemFaturaList() {
		Fatura fatura = this.verifyCreateOrUpdate();

		if (fatura != null) {
			if (faturaService.saveItemFaturaList(fatura,
					fatura.getItemFaturas())) {
				// atualiza o bean da fatura
				bean.setTo(faturaService.findFaturaById(fatura
						.getCodigoFatura()));

				if (Constants.FATURA_STATUS_SUBMITTED.equals(bean.getTo()
						.getIndicadorStatus())) {
					bean.setIsFaturaEditable(Boolean.valueOf(false));
				}
				// mensagem de sucesso
				Messages.showSucess("updateItemFaturaList",
						Constants.DEFAULT_MSG_SUCCESS_UPDATE,
						Constants.ENTITY_NAME_ITEM_FATURA);
			} else {
				Messages.showError("updateFaturaStatus",
						Constants.FATURA_DUE_DATE_NULL);
			}
		}

		// atualiza a lista de HistoricoFatura
		this.prepareHistoricoFatura();
	}

	/**
	 * Remove uma entidade do tipo ItemFatura.
	 */
	public void removeItemFatura() {
		ItemFatura itemFatura = itemFaturaBean.getTo();

		try {
			// remove o ItemFatura
			itemFaturaService.removeItemFatura(itemFaturaService
					.findItemFaturaById(itemFatura.getCodigoItemFatura()),
					Boolean.valueOf(true));

			// mensagem de sucesso
			Messages.showSucess("removeItemFatura",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_ITEM_FATURA);

			// limpa o bean do ItemFatura
			itemFaturaBean.reset();
			// atualiza a lista de ItemFatura na Fatura corrente
			findById(bean.getTo().getCodigoFatura());

			// totaliza os ItemFatura
			updateItemFaturaTotalValues();

		} catch (DataIntegrityViolationException e) {
			Messages.showError("removeItemFatura",
					Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_ITEM_FATURA);
		}

	}

	/**
	 * Cancela a atualiza��o de um ItemFatura.
	 */
	public void cancelItemFatura() {
		itemFaturaBean.reset();

		// atualiza a lista de ItemFatura na Fatura corrente
		findById(bean.getTo().getCodigoFatura());
	}

	/**
	 * Faz a verifica��o das entidades para criar ou editar um ItemFatura.
	 * 
	 * @return ItemFatura - retorna o ItemFatura
	 */
	private ItemFatura verifyCreateOrUpdateItemFatura() {
		ItemFatura itemFatura = itemFaturaBean.getTo();

		// atualiza a referencia das entidades
		itemFatura.setTipoServico(tipoServicoService
				.findTipoServicoById(itemFaturaBean.getTipoServicoMap().get(
						itemFatura.getTipoServico().getNomeTipoServico())));

		itemFatura.setFatura(faturaService.findFaturaById(bean.getTo()
				.getCodigoFatura()));

		Long idFonteReceita = itemFaturaBean.getFonteReceitaMap().get(
				itemFatura.getFonteReceita().getNomeFonteReceita());
		itemFatura.setFonteReceita(fonteReceitaService
				.findFonteReceitaById(idFonteReceita));

		Long idContratoPratica = bean.getContratoPraticaMap().get(
				itemFatura.getContratoPratica().getNomeContratoPratica());
		if (idContratoPratica != null) {
			itemFatura.setContratoPratica(contratoPraticaService
					.findContratoPraticaById(idContratoPratica));
		} else {
			itemFatura.setContratoPratica(null);
		}

		return itemFatura;
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 */
	public void findById(final Long id) {

		Fatura fatura = new Fatura();
		if (bean.getSearchDeletedInvoice() != null && bean.getSearchDeletedInvoice()) {

			FaturaApagada faturaApagada = faturaApagadaService.findById(id);
			fatura = new Fatura(faturaApagada);

			List<ItemFaturaApagado> itemFaturaApagados = itemFaturaApagadoService
					.findByCodigoFaturaApagada(faturaApagada.getCodigoFatura());

			ItemFatura itemFatura = null;
			List<ItemFatura> itemFaturas = new ArrayList<ItemFatura>();
			for (ItemFaturaApagado itemFaturaApagado : itemFaturaApagados) {
				itemFatura = new ItemFatura(itemFaturaApagado, fatura);

				itemFaturas.add(itemFatura);
			}

			fatura.setItemFaturas(itemFaturas);
		} else {
			
			fatura = faturaService.findFaturaById(id);
			fatura.setItemFaturas(itemFaturaService.findItemFaturaByFatura(fatura));
		}
		bean.setTo(fatura);

		try {
			final Long dealFiscalParentCompany = fatura.getDealFiscal().getEmpresa().getEmpresa().getCodigoEmpresa();
			final CompanyErp companyErp = companyErpService.findActiveByCompany(dealFiscalParentCompany);
			bean.setCompanyErp(companyErp);
		} catch (final BusinessException ignored) {
		}

		if (bean.getTo() == null || bean.getTo().getCodigoFatura() == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		} else {
			Moeda moeda = fatura.getMoeda();
			if (moeda != null) {
				bean.setPatternCurrency(faturaService.getCurrency(moeda));
			}
		}
	}

	/**
	 * Busca as pessoas pela sugstam de login.
	 * 
	 * @param suggestLogin
	 *            - login sugerido.
	 * @return retorna uma lista de pessoas com o login sugerido
	 */
	public List<Pessoa> findPessoas(final Object suggestLogin) {
		return pessoaService
				.findAccountExecutiveByLikeLogin((String) suggestLogin);
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 */
	public void findByFilter() {
		boolean isValidDate = false;
		bean.setIsIntegrateButtonDisabled(true);
		bean.setFaturaRowsSelected(0);
		bean.setFaturaRowsSelectedToShowXeroLineIntegrationModal(0);
		bean.resetResultList();
		Fatura filter = bean.getFilter();
		bean.setAllPending(true);

		Date dataPrevisaoBeg = bean.getDataPrevisaoBeg();
		Date dataPrevisaoEnd = bean.getDataPrevisaoEnd();
		if (dataPrevisaoBeg != null && dataPrevisaoEnd != null) {
			if (DateUtil.validateValidityDate(dataPrevisaoBeg, dataPrevisaoEnd,
					Boolean.valueOf(false))) {
				isValidDate = true;
			}
		}

		if (filter.getCodigoFatura() != null || isValidDate) {

			Long codigoCliente = bean.getClienteMap().get(
					bean.getClienteFilter().getNomeCliente());
			Cliente cli = null;
			if (codigoCliente != null) {
				cli = clienteService.findClienteById(codigoCliente);
			}

			Long codigoMsa = bean.getMsaMap().get(
					bean.getMsaFilter().getNomeMsa());
			Msa msa = null;
			if (codigoMsa != null) {
				msa = msaService.findMsaById(codigoMsa);
			}

			Long codigoDealFiscal = bean.getDealFiscalMap().get(
					filter.getDealFiscal().getNomeDealFiscal());
			if (codigoDealFiscal != null) {
				filter.getDealFiscal().setCodigoDealFiscal(codigoDealFiscal);
			} else {
				filter.setDealFiscal(null);
			}

			Long codigoMoeda = bean.getMoedaMap().get(
					filter.getMoeda().getNomeMoeda());
			if (codigoMoeda != null) {
				filter.getMoeda().setCodigoMoeda(codigoMoeda);
			} else {
				filter.setMoeda(null);
			}

			if (bean.getSearchDeletedInvoice()) {

				bean.setResultList(faturaApagadaService.findFaturaByFilter(
						filter, cli, msa, dataPrevisaoBeg, dataPrevisaoEnd));
			} else {
				
				// realiza a busca pela Fatura
				bean.setResultList(faturaService.findFaturaByFilter(filter, cli,
						msa, dataPrevisaoBeg, dataPrevisaoEnd));
			}

			for (FaturaRow faturaRow : bean.getResultList()){
				if (faturaRow.getFatura().getIndicadorStatus() != null) {
					if (!faturaRow.getFatura().getIndicadorStatus().equals(Constants.FATURA_STATUS_PENDING)) {
						bean.setAllPending(false);
					}
				}
			}

			this.updateFaturaTotalValues();
			// se nenhum resultado encontrado
			if (bean.getResultList().size() == 0) {
				Messages.showWarning("findByFilter",
						Constants.DEFAULT_MSG_WARNG_NO_RESULT);
			}
		}
	}

	/**
	 * Busca para o item fatura.
	 */
	public void findPaymentInvoiceByFilter() {

		Date dataBeg = null;
		Date dataEnd = null;

		Boolean filtroData;
		if (bean.getRadioOption()) {
			filtroData = true;
			dataBeg = bean.getDataPrevisaoBeg();
			dataEnd = bean.getDataPrevisaoEnd();
		} else {
			filtroData = false;
			dataBeg = bean.getDataPrevisaoBeg2();
			dataEnd = bean.getDataPrevisaoEnd2();
		}

		Long codigoCliente = bean.getClienteMap().get(
				bean.getClienteFilter().getNomeCliente());
		Cliente cli = null;
		if (codigoCliente != null) {
			cli = clienteService.findClienteById(codigoCliente);
		}

		Long codigoEmpresa = bean.getEmpresaMap().get(
				bean.getEmpresaFilter().getNomeEmpresa());
		Empresa emp = null;
		if (codigoEmpresa != null) {
			emp = empresaService.findEmpresaById(codigoEmpresa);
		}

		Long codigoMsa = bean.getMsaMap().get(bean.getMsaSelected());
		Msa msa = null;
		if (codigoMsa != null) {
			msa = msaService.findMsaById(codigoMsa);
		}

		Fatura filter = bean.getFilter();
		filter.setIndicadorStatus(Constants.FATURA_STATUS_SUBMITTED);
		List<ItemFatura> itemFaturaList = itemFaturaService
				.findItemFaturaByFilter(dataBeg, dataEnd, filter,
						bean.getItemFaturaFilter(), cli, emp,
						bean.getNotPaidOnly(), filtroData, msa,
						bean.getDataPagamentoDe(), bean.getDataPagamentoAte());

		List<ItemFaturaRow> resultList = new ArrayList<ItemFaturaRow>();
		ItemFaturaRow row = null;
		for (ItemFatura itemFatura : itemFaturaList) {
			row = new ItemFaturaRow();

			row.setItemFatura(itemFatura);

			resultList.add(row);
		}

		bean.setItemFaturaRowList(resultList);

		if (resultList.isEmpty()) {
			Messages.showWarning("findItemFaturaByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * A��o que realiza a copia das faturas selecionadas.
	 */
	public void copyAllSelected() {
		if (isSomeSelectedFatura()) {

			if (faturaService.copyFaturas(bean.getResultList(),
					bean.getNumberOfCopies())) {

				Messages.showSucess("copyAllSelected",
						Constants.FATURA_MSG_SUCCESS_COPY);
			}

			this.findByFilter();
		} else {
			Messages.showWarning("copyAllSelected",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Realiza o update da data de pagamento dos itens de faturas selecionados.
	 */
	public void updateDueDate() {

		List<ItemFaturaRow> itemFaturaRowList = bean.getItemFaturaRowList();

		// verifica se algum item foi selecionado
		boolean selected = false;
		int contSelected = 0;
		for (ItemFaturaRow row : itemFaturaRowList) {
			if (row.getIsSelected()) {
				selected = true;
				contSelected++;
			}
		}

		List<ItemFaturaRow> itensFaturaValidas = this
				.isValidDueDates(itemFaturaRowList);

		if (selected) {
			faturaService.updateDueDate(itensFaturaValidas,
					bean.getDataVencimento());

			// mensagem de sucesso
			Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_FATURA);

			findPaymentInvoiceByFilter();
			bean.setDataVencimento(null);

			if (itensFaturaValidas.size() != contSelected) {
				Messages.showWarning("isValidDueDates",
						Constants.FATURA_MSG_WARNG_INVALID_DUE_DATE);
			}

		} else {
			Messages.showWarning("updateDueDate",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}

	}

	/**
	 * Retorna uma lista contendo {@link ItemFaturaRow} com due dates validos
	 * (due date > invoice date).
	 * 
	 * @param itemFaturaRowList
	 *            - recebe uma lista de {@link ItemFaturaRow}
	 * @return retorna um {@link List} de {@link ItemFaturaRow} contendo somente
	 *         os itens com datas validas.
	 */
	public List<ItemFaturaRow> isValidDueDates(
			final List<ItemFaturaRow> itemFaturaRowList) {

		List<ItemFaturaRow> itensFaturaValidos = new ArrayList<ItemFaturaRow>();

		for (ItemFaturaRow row : itemFaturaRowList) {
			Fatura fatura = row.getItemFatura().getFatura();

			if (row.getIsSelected()) {
				// Checa se due date � maior que invoice date.
				if (bean.getDataVencimento() == null
						|| bean.getDataVencimento().after(
								fatura.getDataPrevisao())) {
					// Adiciona item valido
					itensFaturaValidos.add(row);
				}
			}
		}

		return itensFaturaValidos;
	}

	/**
	 * Realiza o update da data de pagamento dos itens de faturas selecionados.
	 */
	public void updatePaymentDate() {

		List<ItemFaturaRow> itemFaturaRowList = bean.getItemFaturaRowList();

		// verifica se algum item foi selecionado
		boolean selected = false;
		int contSelected = 0;
		for (ItemFaturaRow row : itemFaturaRowList) {
			if (row.getIsSelected()) {
				selected = true;
				contSelected++;
			}
		}

		List<ItemFaturaRow> itensFaturaValidas = this
				.isValidPaymentDates(itemFaturaRowList);

		if (selected) {

			faturaService.updatePaymentDate(itensFaturaValidas,
					bean.getDataPagamento());

			// mensagem de sucesso
			Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_FATURA);

			findPaymentInvoiceByFilter();

			bean.setDataPagamento(null);

			if (itensFaturaValidas.size() != contSelected) {
				Messages.showWarning("isValidPaymentDates",
						Constants.FATURA_MSG_WARNG_INVALID_PAYMENT_DATE);
			}
		} else {
			Messages.showWarning("updatePaymentDate",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}

	}

	/**
	 * Retorna uma lista contendo {@link ItemFaturaRow} com payment dates
	 * validos (payment date > invoice date)..
	 * 
	 * @param itemFaturaRowList
	 *            - recebe uma lista de {@link ItemFaturaRow}
	 * @return retorna um {@link List} de {@link ItemFaturaRow} contendo somente
	 *         os itens com datas validas.
	 */
	public List<ItemFaturaRow> isValidPaymentDates(
			final List<ItemFaturaRow> itemFaturaRowList) {

		List<ItemFaturaRow> itensFaturaValidos = new ArrayList<ItemFaturaRow>();

		for (ItemFaturaRow row : itemFaturaRowList) {
			Fatura fatura = row.getItemFatura().getFatura();

			if (row.getIsSelected()) {
				// Checa se payment date � maior que invoice date.
				if (bean.getDataPagamento() == null
						|| bean.getDataPagamento().after(
								fatura.getDataPrevisao())) {
					// Adiciona item valido
					itensFaturaValidos.add(row);
				}
			}
		}

		return itensFaturaValidos;
	}

	/**
	 * Popula a lista de Cliente para combobox.
	 * 
	 * @param pClienteList
	 *            lista de Cliente.
	 * 
	 */
	private void loadClienteList(final List<Cliente> pClienteList) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : pClienteList) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}
		bean.setClienteMap(clienteMap);
		bean.setClienteList(clienteList);
	}

	/**
	 * Popula a lista de Msa para combobox de Msas.
	 * 
	 * @param msas
	 *            lista de Msa.
	 * 
	 */
	private void loadMsaList(final List<Msa> msas) {

		Map<String, Long> msaMap = new HashMap<String, Long>();
		List<String> msaList = new ArrayList<String>();

		for (Msa msa : msas) {
			msaMap.put(msa.getNomeMsa(), msa.getCodigoMsa());
			msaList.add(msa.getNomeMsa());
		}

		bean.setMsaMap(msaMap);
		bean.setMsaList(msaList);
	}

	/**
	 * Popula a lista de Empresa para combobox de Empresa.
	 * 
	 * @param empresas
	 *            lista de Empresa.
	 * 
	 */
	private void loadEmpresaList(final List<Empresa> empresas) {

		Map<String, Long> empresaMap = new HashMap<String, Long>();
		List<String> empresaList = new ArrayList<String>();

		for (Empresa empresa : empresas) {
			empresaMap
					.put(empresa.getNomeEmpresa(), empresa.getCodigoEmpresa());
			empresaList.add(empresa.getNomeEmpresa());
		}

		bean.setEmpresaMap(empresaMap);
		bean.setEmpresaList(empresaList);
	}

	/**
	 * Popula a lista de praticas para combobox.
	 * 
	 * @param pessoas
	 *            lista de praticas.
	 * 
	 */
	private void loadAEList(final List<Pessoa> pessoas) {

		Map<String, Long> pessoaMap = new HashMap<String, Long>();
		List<String> pessoaList = new ArrayList<String>();

		for (Pessoa pessoa : pessoas) {
			pessoaMap.put(pessoa.getCodigoLogin(), pessoa.getCodigoPessoa());
			pessoaList.add(pessoa.getCodigoLogin());
		}

		bean.setAeMap(pessoaMap);
		bean.setAeList(pessoaList);
	}

	public void loadAEListView() {

		List<Pessoa> pessoas = pessoaService.findPessoaAllAccountExecutive();
		Map<String, Long> pessoaMap = new HashMap<String, Long>();
		List<String> pessoaList = new ArrayList<String>();

		for (Pessoa pessoa : pessoas) {
			pessoaMap.put(pessoa.getCodigoLogin(), pessoa.getCodigoPessoa());
			pessoaList.add(pessoa.getCodigoLogin());
		}

		bean.setAeMap(pessoaMap);
		bean.setAeList(pessoaList);
	}

	/**
	 * Verifica se o valor do atributo Msa � valido. Se o valor n�o � nulo e
	 * existe no msaMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateMsa(final FacesContext context,
			final UIComponent component, final Object value) {

		Long msa = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			msa = bean.getMsaMap().get(strValue);
			if (msa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo Cliente � valido. Se o valor n�o � nulo e
	 * existe no clienteMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCliente(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoCliente = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCliente = bean.getClienteMap().get(strValue);
			if (codigoCliente == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Realiza a valida�ao do campo Login AE.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateLogin(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;

		if ((login != null) && (!"".equals(login))) {
			Pessoa p = pessoaService.findPessoaByLogin(login);

			if (p == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}

	}

	/**
	 * Popula o combobox do DealFiscal de acordo com o Cliente selecionado.
	 * 
	 * @param e
	 *            - evento de mudan�a
	 */
	public void prepareDealFiscalCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		bean.setEnableDate(Boolean.valueOf(true));
		bean.setEnableExpiryDate(Boolean.valueOf(true));
		bean.getTo().setDataPrevisao(null);

		if (value != null) {
			Long codigoMsa = bean.getMsaMap().get(value);
			Msa msa = null;

			// se o msa existir
			if (codigoMsa != null) {
				msa = msaService.findMsaById(codigoMsa);
				loadDealFiscalList(dealFiscalService
						.findDealFiscalActiveByMsa(msa));
				bean.getFilter().getDealFiscal().setNomeDealFiscal("");
				bean.getTo().getDealFiscal().setNomeDealFiscal("");
			} else {
				loadDealFiscalList(new ArrayList<DealFiscal>());
			}
		}
	}

	/**
	 * Popula o combobox de Msa de acordo com o Deal Fiscal selecionado.
	 * 
	 * @param e
	 *            - evento de mudan�a
	 */
	public void prepareMsaCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null) {
			Long codigoCliente = bean.getClienteMap().get(value);
			Cliente cliente = null;

			// bean.reset();

			// se o cliente existir
			if (codigoCliente != null) {
				cliente = clienteService.findClienteById(codigoCliente);
				loadMsaList(msaService.findMsaByCliente(cliente));
				bean.getFilter().getDealFiscal().getMsa().setNomeMsa("");
				bean.getTo().getDealFiscal().getMsa().setNomeMsa("");
			} else {
				loadDealFiscalList(new ArrayList<DealFiscal>());
			}
		}
	}

	/**
	 * Verifica se o valor do atributo Empresa � valido. Se o valor n�o � nulo e
	 * existe no empresaMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateEmpresa(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoEmpresa = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoEmpresa = bean.getEmpresaMap().get(strValue);
			if (codigoEmpresa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Moeda para combobox.
	 * 
	 * @param pMoedaList
	 *            lista de Moeda.
	 * 
	 */
	private void loadMoedaList(final List<Moeda> pMoedaList) {

		Map<String, Long> moedaMap = new HashMap<String, Long>();
		List<String> moedaList = new ArrayList<String>();

		for (Moeda moeda : pMoedaList) {
			moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
			moedaList.add(moeda.getNomeMoeda());
		}
		bean.setMoedaMap(moedaMap);
		bean.setMoedaList(moedaList);
	}

	/**
	 * Verifica se o valor do atributo Moeda � valido. Se o valor n�o � nulo e
	 * existe no moedaMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateMoeda(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoMoeda = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoMoeda = bean.getMoedaMap().get(strValue);
			if (codigoMoeda == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de TipoServico para combobox.
	 * 
	 * @param pTipoServicoList
	 *            lista de TipoServico.
	 * 
	 */
	public void loadTipoServicoList(final List<TipoServico> pTipoServicoList) {

		Map<String, Long> tipoServicoMap = new HashMap<String, Long>();
		Set<String> tipoServicoList = new HashSet<>();

		for (TipoServico tipoServico : pTipoServicoList) {
				tipoServicoMap.putIfAbsent(tipoServico.getNomeTipoServico(),
						tipoServico.getCodigoTipoServico());
				tipoServicoList.add(tipoServico.getNomeTipoServico());
		}
		itemFaturaBean.setTipoServicoMap(tipoServicoMap);
		itemFaturaBean.setTipoServicoList(new ArrayList<>(tipoServicoList));
	}

	/**
	 * Popula a lista de TipoServico para combobox.
	 * 
	 * @param pFonteReceitaList
	 *            lista de FonteReceita.
	 * 
	 */
	private void loadFonteReceitaList(final List<FonteReceita> pFonteReceitaList) {

		Map<String, Long> fonteReceitaMap = new HashMap<String, Long>();
		List<String> fonteReceitaList = new ArrayList<String>();

		for (FonteReceita fonteReceita : pFonteReceitaList) {
			fonteReceitaMap.put(fonteReceita.getNomeFonteReceita(),
					fonteReceita.getCodigoFonteReceita());
			fonteReceitaList.add(fonteReceita.getNomeFonteReceita());
		}

		itemFaturaBean.setFonteReceitaMap(fonteReceitaMap);
		itemFaturaBean.setFonteReceitaList(fonteReceitaList);
	}

	/**
	 * Verifica se o valor do atributo TipoServico � valido. Se o valor n�o �
	 * nulo e existe no tipoServicoMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateTipoServico(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoTipoServico = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoTipoServico = itemFaturaBean.getTipoServicoMap()
					.get(strValue);
			if (codigoTipoServico == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo TipoServico � valido. Se o valor n�o �
	 * nulo e existe no tipoServicoMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateFonteReceita(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoFonteReceita = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoFonteReceita = itemFaturaBean.getFonteReceitaMap().get(
					strValue);
			if (codigoFonteReceita == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de DealFiscal para combobox.
	 * 
	 * @param pDealFiscalList
	 *            lista de DealFiscal.
	 * 
	 */
	private void loadDealFiscalList(final List<DealFiscal> pDealFiscalList) {

		Map<String, Long> dealFiscalMap = new HashMap<String, Long>();
		List<String> dealFiscalList = new ArrayList<String>();

		for (DealFiscal dealFiscal : pDealFiscalList) {
			dealFiscalMap.put(dealFiscal.getNomeDealFiscal(),
					dealFiscal.getCodigoDealFiscal());
			dealFiscalList.add(dealFiscal.getNomeDealFiscal());
		}
		bean.setDealFiscalMap(dealFiscalMap);
		bean.setDealFiscalList(dealFiscalList);
	}

	/**
	 * Popula a lista de ContratoPratica para combobox de contratos praticas.
	 * 
	 * @param contratosPratica
	 *            lista de ContratoPratica.
	 * 
	 */
	private void loadContratoPraticaList(
			final List<ContratoPratica> contratosPratica) {

		Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
		List<String> contratoPraticaList = new ArrayList<String>();

		for (ContratoPratica cp : contratosPratica) {
			contratoPraticaMap.put(cp.getNomeContratoPratica(),
					cp.getCodigoContratoPratica());
			contratoPraticaList.add(cp.getNomeContratoPratica());
		}

		bean.setContratoPraticaMap(contratoPraticaMap);
		bean.setContratoPraticaList(contratoPraticaList);
	}

	/**
	 * Seta a data de previsao para o calculo da data de vencimento.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void setDate(final FacesContext context,
			final UIComponent component, final Object value) {

		Date data = (Date) value;

		if (data != null) {
			bean.getTo().setDataPrevisao(data);
		}
	}

	/**
	 * @deprecated Usar o m�todo validateDealFiscalFinancial
	 * 
	 *             Verifica se o valor do atributo DealFiscal � valido. Se o
	 *             valor n�o � nulo e existe no dealFiscalMap, ent�o o valor �
	 *             valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	@Deprecated
	public void validateDealFiscal(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoDealFiscal = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoDealFiscal = bean.getDealFiscalMap().get(strValue);
			if (codigoDealFiscal == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			} else {
				Long codigoFatura = bean.getTo().getCodigoFatura();
				if (codigoFatura != null) {
					Fatura fatura = faturaService.findFaturaById(codigoFatura);

					DealFiscal deal = dealFiscalService
							.findDealFiscalById(codigoDealFiscal);

					List<ItemFatura> itemFaturas = fatura.getItemFaturas();
					TipoServico ts = null;
					for (ItemFatura itemFatura : itemFaturas) {
						TipoServico tipoServico = itemFatura.getTipoServico();
						ts = tipoServicoService.findTipoServicoByIdAndDeal(
								tipoServico.getCodigoTipoServico(), deal);
						if (ts == null) {
							throw new ValidatorException(
									Messages.getMessageError(
											Constants.FATURA_MSG_ERROR_INVALID_SERVICE_TYPE,
											tipoServico.getNomeTipoServico()));
						}
					}
				}
			}
		}
	}

	/**
	 * Verifica se o valor do atributo DealFiscal � valido. Se o valor n�o �
	 * nulo e existe no dealFiscalMap, ent�o o valor � valido. Na altera��o do
	 * C-Lob, verificar se: - Fiscal Deal compat�vel (n�o precisa ser o mesmo
	 * ID) - Client Entity e Company devem ser os mesmos (em ambos os fiscal
	 * deals) - Par�metros intercompany dever�o ser id�nticos. - Service type
	 * selecionado dever�o ser tamb�m id�nticos.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateDealFiscalFinancial(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codDealFiscalNew = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codDealFiscalNew = bean.getDealFiscalMap().get(strValue);
			if (codDealFiscalNew == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			} else {
				Long codigoFatura = bean.getTo().getCodigoFatura();
				if (codigoFatura != null) {
					Fatura fatura = faturaService.findFaturaById(codigoFatura);
					DealFiscal dfOld = dealFiscalService
							.findDealFiscalById(fatura.getDealFiscal()
									.getCodigoDealFiscal());
					DealFiscal dfNew = dealFiscalService
							.findDealFiscalById(codDealFiscalNew);

					// Client Entity deve ser o mesmo (em ambos os fiscal deals)
					if (dfOld.getCliente().getCodigoCliente() != dfNew
							.getCliente().getCodigoCliente()) {
						throw new ValidatorException(
								Messages.getMessageError(Constants.FATURA_MSG_ERROR_DF_INVALID_CLIENT_ENTITY));
					}

					// Company deve ser o mesmo
					if (dfOld.getEmpresa().getCodigoEmpresa() != dfNew
							.getEmpresa().getCodigoEmpresa()) {
						throw new ValidatorException(
								Messages.getMessageError(Constants.FATURA_MSG_ERROR_DF_INVALID_COMPANY));
					}

					// Par�metros Intercompany devem ser os mesmos
					if (dfOld.getIndicadorIntercompany() != dfNew
							.getIndicadorIntercompany()) {
						throw new ValidatorException(
								Messages.getMessageError(Constants.FATURA_MSG_ERROR_DF_INVALID_INTERCOMP_PARAMS));
					} else if (dfOld.getIndicadorIntercompany()) {
						if (dfOld.getEmpresaIntercomp().getCodigoEmpresa() != dfNew
								.getEmpresaIntercomp().getCodigoEmpresa()) {
							throw new ValidatorException(
									Messages.getMessageError(Constants.FATURA_MSG_ERROR_DF_INVALID_INTERCOMP_PARAMS));
						} else if (dfOld.getPercentualIntercompanyVigente() != dfNew
								.getPercentualIntercompanyVigente()) {
							throw new ValidatorException(
									Messages.getMessageError(Constants.FATURA_MSG_ERROR_DF_INVALID_INTERCOMP_PARAMS));
						}
					}

					// Service Types devem ser v�lidos, os Tipos de Servicos dos
					// itens
					// da Fatura devem ser compat�veis aos Tipos de Servicos
					// ("existir", ser relacionado) no
					// novo DealFiscal
					List<ItemFatura> itemFaturas = fatura.getItemFaturas();
					TipoServico ts = null;
					for (ItemFatura itemFatura : itemFaturas) {
						TipoServico tipoServico = itemFatura.getTipoServico();
						ts = tipoServicoService.findTipoServicoByIdAndDeal(
								tipoServico.getCodigoTipoServico(), dfNew);
						if (ts == null) {
							throw new ValidatorException(
									Messages.getMessageError(
											Constants.FATURA_MSG_ERROR_INVALID_SERVICE_TYPE,
											tipoServico.getNomeTipoServico()));
						}
					}
				}
			}
		}
	}

	/**
	 * Verifica se o valor do atributo ContratoPratica � valido. Se o valor n�o
	 * � nulo e existe no contratoPraticaMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateContratoPratica(final FacesContext context,
			final UIComponent component, final Object value) {

		Long contratoPratica = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			contratoPratica = bean.getContratoPraticaMap().get(strValue);
			if (contratoPratica == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Habilita / desabilita os bot�es (+ maizinho) da lista de ItemFatura.
	 */
	public void showHideItemFatura() {
		FaturaRow faturaRow = bean.getFaturaRow();
		faturaRow.setShowItemFatura(!faturaRow.getShowItemFatura());
	}

	/**
	 * Atualiza o valor do total Geral das Faturas (Search).
	 */
	private void updateFaturaTotalValues() {
		bean.setShowTotalGeralFatura(Boolean.valueOf(true));
		double totalGeralFatura = 0;
		Long codigoMoeda = null;
		Moeda moeda = null;

		List<FaturaRow> faturaRowList = bean.getResultList();

		for (FaturaRow faturaRow : faturaRowList) {
			moeda = faturaRow.getFatura().getMoeda();
			if (moeda != null) {
				Long codigoMoedaAux = moeda.getCodigoMoeda();
				if (codigoMoeda != null && codigoMoeda != codigoMoedaAux) {
					bean.setShowTotalGeralFatura(Boolean.valueOf(false));
					break;
				} else if (codigoMoeda == null) {
					codigoMoeda = codigoMoedaAux;
				}
			}

			totalGeralFatura += faturaRow.getTotalFatura().doubleValue();
		}

		bean.resetTotalValues();
		if (bean.getShowTotalGeralFatura()) {
			if (moeda != null) {
				bean.setPatternCurrency(faturaService.getCurrency(moeda));
			}
			bean.updateTotalValues(totalGeralFatura);
		}
	}

	/**
	 * Atualiza os valores do total da Fatura (soma dos ItemFatura).
	 */
	private void updateItemFaturaTotalValues() {
		double totalGeralItemFatura = 0;

		Fatura fatura = bean.getTo();

		Moeda moeda = fatura.getMoeda();
		if (moeda != null) {
			itemFaturaBean.setPatternCurrency(faturaService.getCurrency(moeda));
		}

		List<ItemFatura> itemFaturaList = fatura.getItemFaturas();
		for (ItemFatura itemFatura : itemFaturaList) {
			totalGeralItemFatura += itemFatura.getValorItem().doubleValue();
		}

		itemFaturaBean.resetTotalValues();
		itemFaturaBean.updateTotalValues(totalGeralItemFatura);
	}

	/**
	 * Remove todas as Fatura selecionadas.
	 */
	public void removeAllFatura() throws Exception {
		// verifica se algum item foi selecionado
		if (isSomeSelectedFatura()) {
			List<FaturaRow> faturaRowList = bean.getResultList();
			Boolean allSuccessRemove = true;

			// itera a lista de Faturas para remover as que foram selecionadas
			for (FaturaRow row : faturaRowList) {
				if (row.getIsSelected()) {
					Fatura fatura = faturaService.findFaturaById(row
							.getFatura().getCodigoFatura());

					try {
						// verifica se NAO foi removida com sucesso
						if (!faturaService.removeFatura(fatura)) {
							allSuccessRemove = false;
						}
						// pega a exception de vila��o de integridade
					} catch (DataIntegrityViolationException e) {
						Messages.showError(
								"removeAllFatura",
								Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
								Constants.ENTITY_NAME_FATURA);

						allSuccessRemove = false;
					}
				}
			}

			if (allSuccessRemove) {
				// caso seja removido com sucesso, d� mensagem de sucesso
				Messages.showSucess("removeAllFatura",
						Constants.DEFAULT_MSG_SUCCESS_REMOVE,
						Constants.ENTITY_NAME_FATURA);
			}

			findByFilter();

			updateFaturaTotalValues();
		} else {
			Messages.showWarning("removeAllFatura",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Aprova (altera o status para Approved) de todas as Fatura selecionadas.
	 */
	public void approveAllFatura() {
		// verifica se algum item foi selecionado
		if (isSomeSelectedFatura()) {
			List<FaturaRow> faturaRowList = bean.getResultList();

			Boolean success = true;
			// itera a lista de Faturas para remover as que foram selecionadas
			for (FaturaRow row : faturaRowList) {
				if (row.getIsSelected()) {
					Fatura fatura = row.getFatura();
					if (!faturaService.approveFatura(fatura)) {
						// erro caso tentar aprovar uma
						// invoice diferente do status aprovado
						Messages.showError("approveAllFatura",
								Constants.FATURA_MSG_ERROR_APPROVED,
								fatura.getCodigoFatura() + "");

						success = false;
					}
				}
			}

			if (success) {
				// caso seja removido com sucesso, d� mensagem de sucesso
				Messages.showSucess("approveAllFatura",
						Constants.DEFAULT_MSG_SUCCESS_UPDATE,
						Constants.ENTITY_NAME_FATURA);
			}

			findByFilter();

			updateFaturaTotalValues();
		} else {
			Messages.showWarning("removeAllFatura",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Seleciona todas as Fatura.
	 */
	public void selectAllFatura() {
		for (final FaturaRow row : bean.getResultList()) {
			if (Boolean.TRUE.equals(row.getIsSelected()) || row.getFatura() == null ||
					!Constants.FATURA_STATUS_APPROVED.equalsIgnoreCase(row.getFatura().getIndicadorStatus()))
				continue;

			row.setIsSelected(Boolean.TRUE);

			bean.addToFaturaRowsSelected(1);
			if (isCompanyHasXeroIntegrationAndHasRevenueSourceContractors(row.getFatura())) {
				bean.addToFaturaRowsSelectedToShowXeroLineIntegrationModal(1);
			}
		}

		bean.setIsIntegrateButtonDisabled(bean.getFaturaRowSelected() < 1);
	}

	/**
	 * Deseleciona todas as Fatura.
	 */
	public void unselectAllFatura() {
		for (final FaturaRow row : bean.getResultList()) {
			if (!Boolean.TRUE.equals(row.getIsSelected())) continue;

			row.setIsSelected(Boolean.FALSE);

			bean.addToFaturaRowsSelected(-1);
			if (isCompanyHasXeroIntegrationAndHasRevenueSourceContractors(row.getFatura())) {
				bean.addToFaturaRowsSelectedToShowXeroLineIntegrationModal(-1);
			}
		}

		bean.setIsIntegrateButtonDisabled(bean.getFaturaRowSelected() < 1);
	}

	/**
	 * Verifica se algum item foi selecionado.
	 * 
	 * @return true se algum item selecionado, caso contrario retorna false.
	 */
	private Boolean isSomeSelectedFatura() {
		List<FaturaRow> faturaRowList = bean.getResultList();
		for (FaturaRow faturaRow : faturaRowList) {
			if (faturaRow.getIsSelected()) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	public void onFaturaRowSelectionChange(final ValueChangeEvent event) {
		if (Boolean.TRUE.equals(event.getNewValue()))
			bean.addToFaturaRowsSelected(1);
		else if (Boolean.FALSE.equals(event.getNewValue()))
			bean.addToFaturaRowsSelected(-1);

		bean.setIsIntegrateButtonDisabled(bean.getFaturaRowSelected() < 1);

		final FaturaRow faturaRow = (FaturaRow) event.getComponent().getAttributes().get("faturaRow");
		if (faturaRow == null ||
				!isCompanyHasXeroIntegrationAndHasRevenueSourceContractors(faturaRow.getFatura())
		) return;

		if (Boolean.TRUE.equals(event.getNewValue()))
			bean.addToFaturaRowsSelectedToShowXeroLineIntegrationModal(1);
		else if (Boolean.FALSE.equals(event.getNewValue()))
			bean.addToFaturaRowsSelectedToShowXeroLineIntegrationModal(-1);
	}

	private boolean isCompanyHasXeroIntegrationAndHasRevenueSourceContractors(final Fatura fatura) {
		if (fatura == null ||
				fatura.getDealFiscal() == null ||
				fatura.getDealFiscal().getEmpresa() == null ||
				CollectionUtils.isEmpty(fatura.getItemFaturas())
		) return false;

		if (!Constants.COMPANIES_WITH_XERO_INTEGRATION_AND_ALLOWED_TO_SUMMARIZE_INVOICE.contains(
				fatura.getDealFiscal().getEmpresa().getCodigoEmpresa())
		) return false;

		for (final ItemFatura itemFatura : fatura.getItemFaturas()) {
			if (itemFatura.getFonteReceita() != null &&
					Constants.REVENUE_SOURCES_THAT_CAN_BE_SUMMARIZED.contains(itemFatura.getFonteReceita().getCodigoFonteReceita())
			) return true;
		}

		return false;
	}

	public void closeXeroLineIntegrationModal() {
		bean.setXeroLineIntegration(null);
		bean.setShowXeroLineIntegrationModal(false);
	}

	public void openXeroIntegrationSettingsModalOrIntegrateAll() throws Exception {
		if (bean.getFaturaRowsSelectedToShowXeroLineIntegrationModal() > 0) {
			bean.setShowXeroLineIntegrationModal(true);
			return;
		}

		bean.setShowXeroLineIntegrationModal(false);
		this.integrateAll();
	}

	/**
	 * A��o que integra todas as Fatura selecionadas.
	 */
	public void integrateAll() throws Exception {

		final Map<Long, String> companyErp;
		try {
			companyErp = companyErpService.findAllActive();
		} catch (final BusinessException e) {
			Messages.showError("integrateAll", e.getMessage());
			return;
		}

		List<FaturaRow> faturaRowList = bean.getResultList();
		// conversor do status da fatura
		StatusFaturaConverter c = new StatusFaturaConverter();

		int successCount = 0;
		int count = 0;

		if (this.isSomeSelectedFatura()) {
			for (FaturaRow faturaRow : faturaRowList) {
				if (faturaRow.getIsSelected()) {
					if(faturaRow.getTotalFatura().doubleValue() > 0) {
						Fatura fatura = faturaRow.getFatura();
						if (fatura.getDataPrevisao() != null) {
							if (Constants.FATURA_STATUS_APPROVED.equals(fatura
									.getIndicadorStatus())
									|| Constants.FATURA_STATUS_INTEGRATION_ERROR
									.equals(fatura.getIndicadorStatus())) {
								DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(fatura.getDealFiscal().getCodigoDealFiscal());

								if(this.getPaymentCondition(dealFiscal) != null){
									try {
										if (isCompanyHasXeroIntegrationAndHasRevenueSourceContractors(fatura)) {
											fatura.setXeroLineIntegration(bean.getXeroLineIntegration());
										}
										faturaService.integrateFatura(companyErp, fatura);
										successCount++;
									}catch (NullPointerException npe) {
										String nullPointerMessage = npe.getMessage();
										String nullPointerTrace = npe.getStackTrace().toString();

										logger.info("Exception:  " + npe);
										String errorMsg = fatura.getCodigoFatura() + " -> " + npe.getMessage();
										mailSenderUtil.sendTextMail(systemProperties.getProperty(Constants.EMAIL_ADDRESS_ERROR_KEY), BundleUtil.getBundle(
														"_nls.fatura_integracao.mail.exception.subject", fatura.getCodigoFatura()),
												BundleUtil.getBundle("_nls.fatura_integracao.mail.exception.message", nullPointerMessage + " \n " + nullPointerTrace));
										Messages.showError("reIntegrarReceitaDeal", Constants.INTEGRACAO_RECEITA_MSG_INFO_NULL, errorMsg);
									} catch(Exception ex) {
										logger.info("Exception 2:  " + ex);
										mailSenderUtil.sendTextMail(systemProperties.getProperty(Constants.EMAIL_ADDRESS_ERROR_KEY), BundleUtil.getBundle("_nls.fatura.mail.exception.subject"), ex.getMessage());
										Messages.showError("reIntegrarReceitaDeal", Constants.INTEGRACAO_FATURA_MSG_COMPLETE);
									}
								}else {
									Messages.showError("findById",
											Constants.INVOICE_NO_PAYMENT_PERIOD, fatura.getDealFiscal().getNomeDealFiscal());
								}


							} else {
								String status = c.getAsString(null, null,
										fatura.getIndicadorStatus());

								Messages.showError(
										"integrateAll",
										Constants.INTEGRACAO_FATURA_INTEGRATE_MSG_ERROR_STATUS,
										new Object[] { fatura.getCodigoFatura(),
												status });
							}
						} else {
							Messages.showError(
									"integrateAll",
									Constants.INTEGRACAO_FATURA_INTEGRATE_MSG_ERROR_DATES,
									new Object[] { fatura.getCodigoFatura() });
						}

					}else{
						Messages.showError(
								"integrateAll",
								Constants.INTEGRACAO_FATURA_INTEGRATE_MSG_ERROR_ZERO_VALUE_TOTAL,
								new Object[] { faturaRow.getFatura().getCodigoFatura() });
					}
					count++;
				}
			}

			// se todas Fatura integradas com sucesso
			if (successCount == count) {
				Messages.showSucess("integrateAll",
						Constants.INTEGRACAO_FATURA_MSG_SUCCESS);
				// se algumas Fatura integradas com sucesso
			} else if (successCount > 0) {
				Messages.showWarning("integrateAll",
						Constants.INTEGRACAO_FATURA_MSG_COMPLETE);
				// se nenhuma Fatura integrada
			} else {
				Messages.showError("integrateAll",
						Constants.INTEGRACAO_FATURA_MSG_ERROR);
			}

			closeXeroLineIntegrationModal();
			this.findByFilter();
		} else {
			Messages.showWarning("integrateAll",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * A��o que integra todas as faturas selecionadas.
	 */
	public void reintegrate() {
		Fatura fatura = bean.getTo();
		faturaService.reIntegrarFatura(fatura);

		findById(fatura.getCodigoFatura());
	}

	/**
	 * Habilita a edicao das data de previsao.
	 */
	public void enableDate() {
		bean.setEnableDate(Boolean.valueOf(true));
	}

	/**
	 * Verifica o tipo da Fonte de Receita.
	 * 
	 * @param e
	 *            - evento da mudan�a
	 */
	public void verificaFonteReceita(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		// recupera a fonte de receita selecionada
		Long codigoFonteReceita = itemFaturaBean.getFonteReceitaMap()
				.get(value);
		FonteReceita fonteReceita = fonteReceitaService
				.findFonteReceitaById(codigoFonteReceita);

		// habilita se o tipo de fonte de receita for Other
		itemFaturaBean.setEnableCLob(fonteReceita != null
				&& Constants.TIPO_FONTE_RECEITA_OTHER.equals(fonteReceita
						.getIndicadorTipo()));
	}

	public void prepareCentroLucro() {

		// busca dealFiscal selecionado no combobox
		Long codigoDealFiscal = bean.getDealFiscalMap().get(
				bean.getTo().getDealFiscal().getNomeDealFiscal());
		DealFiscal dealFiscal = dealFiscalService
				.findDealFiscalById(codigoDealFiscal);

		// Monta lista de SSO
		List<CentroLucro> listaSSO = centroLucroService
				.findByDealFiscalAndNatureza(dealFiscal, 2L,
						DateUtil.getDate(bean.getTo().getDataPrevisao()));

		// Monta lista de UMKT
		List<CentroLucro> listaUMKT = centroLucroService
				.findByDealFiscalAndNatureza(dealFiscal, 1L,
						DateUtil.getDate(bean.getTo().getDataPrevisao()));

		bean.getTo().setCentroLucroSso(new CentroLucro());
		bean.getTo().setCentroLucroUmkt(new CentroLucro());

		// Carrega lista e seus respectivos mapas
		this.loadSSOList(listaSSO);
		this.loadUMKTList(listaUMKT);

	}

	/**
	 * Carrega lista de SSO e seu respectivo mapa.
	 * 
	 * @param listaCentroLucro
	 *            lista de centro de lucro
	 */
	private void loadSSOList(final List<CentroLucro> listaCentroLucro) {
		List<String> listaSSO = new ArrayList<String>();
		Map<String, Long> mapSSO = new HashMap<String, Long>();

		for (CentroLucro cl : listaCentroLucro) {
			listaSSO.add(cl.getNomeCentroLucro());
			mapSSO.put(cl.getNomeCentroLucro(), cl.getCodigoCentroLucro());
		}

		bean.setSSOList(listaSSO);
		bean.setSSOMap(mapSSO);
	}

	/**
	 * Carrega lista de UMKT e seu respectivo mapa.
	 * 
	 * @param listaCentroLucro
	 *            lista de centro de lucro
	 */
	private void loadUMKTList(final List<CentroLucro> listaCentroLucro) {
		List<String> listaUMKT = new ArrayList<String>();
		Map<String, Long> mapUMKT = new HashMap<String, Long>();

		for (CentroLucro cl : listaCentroLucro) {
			listaUMKT.add(cl.getNomeCentroLucro());
			mapUMKT.put(cl.getNomeCentroLucro(), cl.getCodigoCentroLucro());
		}

		bean.setUMKTList(listaUMKT);
		bean.setUMKTMap(mapUMKT);
	}

	/**
	 * Prepara a lista de {@link Msa} a ser populado quando o combobox de
	 * {@link Cliente} for alterado.
	 * 
	 * @param event
	 *            the change event
	 * 
	 */
	public void prepareComboboxMsa(final ValueChangeEvent event) {
		String value = (String) event.getNewValue();
		if (value != null && !value.isEmpty()) {
			Long c = bean.getClienteMap().get(value);

			Cliente cliente = clienteService.findClienteById(c);
			Long codigoClientePai = cliente.getCliente().getCodigoCliente();

			List<Msa> msas = msaService.findMsaByCliente(clienteService
					.findClienteById(codigoClientePai));
			this.loadMsaList(msas);
		}
	}

	/**
	 * Resta o filtro de data de pagamento quando checado o "Only Not Paid".
	 * 
	 */
	public void resetPaymentDate() {
		bean.setDataPagamentoDe(null);
		bean.setDataPagamentoAte(null);
	}

	/**
	 * Verifica se o valor do atributo ContratoPratica � valido. Se o valor n�o
	 * � nulo e existe no contratoPraticaMap, ent�o o valor � valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateDataPrevisaoManage(final FacesContext context,
			final UIComponent component, final Object value) throws IOException {

		Date dtValue = (Date) value;

		if (dtValue != null) {
			bean.setEnableExpiryDate(Boolean.valueOf(true));

			Long codigoDealFiscal = bean.getTo().getDealFiscal()
					.getCodigoDealFiscal();

			if (codigoDealFiscal != null) {
				DealFiscal dealFiscal = dealFiscalService
						.findDealFiscalById(codigoDealFiscal);

				//Long prazoRecebimento = dealFiscal.getCliente().getNumeroDiasPrazoRecebimento();
				final PaymentConditionDealFiscal paymentConditionsAtual = paymentConditionDealFiscalService.findActualPaymentCondition(dealFiscal.getCodigoDealFiscal());
				Long companyCode = clienteService.getCompanyCodeByClientName(dealFiscal.getCliente().getCliente().getNomeCliente());
				Collection<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(dealFiscal.getCliente().getCodigoAgenteMega(), companyCode);
				logger.info("paymentConditions validateDataPrevisaoManage" + paymentConditions.size());
				Long prazoRecebimento = this.getPrazoPagamentoByCondicaoPagamento(paymentConditions, paymentConditionsAtual);

				if (prazoRecebimento != null) {
					Date dataVencimento = DateUtil.sumDays(prazoRecebimento,
							dtValue);
					dataVencimento = DateUtil.nextWorkDay(dataVencimento);
					bean.getTo().setDataVencimento(dataVencimento);
				} else {
					Messages.showError("findById",
							Constants.INVOICE_NO_PAYMENT_PERIOD, dealFiscal.getNomeDealFiscal());
				}
			}
		}

		if (DateUtil.isSameDate(dtValue, new Date(), Calendar.DATE)) {
			bean.setShowMessageError(Boolean.valueOf(false));
		} else {
			bean.setShowMessageError(Boolean.valueOf(true));
		}
	}

	public Long getPaymentCondition(DealFiscal dealFiscal) throws IOException{
		final PaymentConditionDealFiscal paymentConditionsAtual = paymentConditionDealFiscalService.findActualPaymentCondition(dealFiscal.getCodigoDealFiscal());
		Long companyCode = clienteService.getCompanyCodeByClientName(dealFiscal.getCliente().getCliente().getNomeCliente());
		Collection<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(dealFiscal.getCliente().getCodigoAgenteMega(), companyCode);
		logger.info("paymentConditions getPaymentCondition" + paymentConditions.size());
		Long prazoRecebimento = this.getPrazoPagamentoByCondicaoPagamento(paymentConditions, paymentConditionsAtual);

		return prazoRecebimento;
	}

	/**
	 * Método responsável por informar
	 * se o atributo DOC pode ser editado.
	 *
	 * Condições: Status da Fatura ser Approved ou Submitted. E
	 * Fatura ser Intercompany OU a empresa for uma das listadas abaixo:
	 * USA, UK, Portugal, CinqUS, Dextra Inc.
	 *
	 *
	 * @return Boolean
	 */
	protected Boolean canDocBeEditable(Fatura fatura){

		if(Constants.FATURA_STATUS_APPROVED.equals(fatura.getIndicadorStatus()) ||
				Constants.FATURA_STATUS_SUBMITTED.equals(fatura.getIndicadorStatus())){

			if(fatura.getDealFiscal().getIndicadorIntercompany())
				return Boolean.TRUE;

			if(canDocBeEditableByEmpresa(fatura.getDealFiscal().getEmpresa()))
				return Boolean.TRUE;

		}

		return Boolean.FALSE;
	}

	/**
	 * Método que verifica se a empresa passada por parâmetro
	 * pode editar o atributo DOC.
	 *
	 * @param empresa
	 * @return Boolean
	 */
	private Boolean canDocBeEditableByEmpresa(Empresa empresa){

		if(Constants.CD_EMPRESA_INC.equals(empresa.getCodigoEmpresa()))
			return Boolean.TRUE;

		if(empresaService.isUk(empresa))
			return Boolean.TRUE;

		if(empresaService.isPortugal(empresa))
			return Boolean.TRUE;

		if(empresaService.isCinqUs(empresa))
			return Boolean.TRUE;

		if(empresaService.isDextraInc(empresa))
			return Boolean.TRUE;

		return Boolean.FALSE;
	}


	public void loadAeList(final ActionEvent event){
		if(bean.getAeList() == null || bean.getAeList().isEmpty()) {
			loadAEList(pessoaService.findPessoaAllAccountExecutive());
		}
	}

	public void loadClobList(final ActionEvent event){
		if(bean.getContratoPraticaList() == null || bean.getContratoPraticaList().isEmpty()) {
			Fatura fatura = faturaService.findFaturaById(bean.getTo().getCodigoFatura());
			// lista de Contrato Pratica dentro do Deal Fiscal
			List<ContratoPratica> clobs = new ArrayList<ContratoPratica>();

			for (CpraticaDfiscal cpraticaDfiscal : fatura.getDealFiscal()
					.getCpraticaDfiscals()) {
				// Desconsidera os deletados logicamente
				if (cpraticaDfiscal.getContratoPratica()
						.getIndicadorDeleteLogico().equals(Constants.NO)) {
					clobs.add(cpraticaDfiscal.getContratoPratica());
				}
			}

			// ordena a lista de contratos-pratica
			Collections.sort(clobs, new Comparator<ContratoPratica>() {
				public int compare(final ContratoPratica cp1,
								   final ContratoPratica cp2) {
					return cp1.getNomeContratoPratica().compareToIgnoreCase(
							cp2.getNomeContratoPratica());
				}
			});
			this.loadContratoPraticaList(clobs);
		}
	}

}