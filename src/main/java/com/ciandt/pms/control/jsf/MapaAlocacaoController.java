/**
 * Classe MapaAlocacaoController - Classe da camada de apresentação do MapaAlocacao.
 */
package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoPeriodoService;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IEtiquetaService;
import com.ciandt.pms.business.service.IFatorUrMesService;
import com.ciandt.pms.business.service.IMapaAlocacaoPessoaService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.business.service.IPessoaCidadeBaseService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.IVwPmsFinancialsService;
import com.ciandt.pms.comparator.CidadeBaseAlocacaoComparator;
import com.ciandt.pms.comparator.IndicadorEstagioAlocacaoComparator;
import com.ciandt.pms.comparator.PerfilVendidoAlocacaoComparator;
import com.ciandt.pms.control.jsf.bean.AlocacaoBean;
import com.ciandt.pms.control.jsf.bean.MapaAlocacaoBean;
import com.ciandt.pms.control.jsf.bean.MessageControlBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Etiqueta;
import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaCidadeBase;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.VwPmsFinancials;
import com.ciandt.pms.model.VwPmsFinancialsId;
import com.ciandt.pms.model.vo.AlocacaoCell;
import com.ciandt.pms.model.vo.AlocacaoContratoPraticaRow;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.model.vo.MapDashboardRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * A classe MapaAlocacaoController proporciona as funcionalidades da camada de
 * apresentação para a entidade MapaAlocacao.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_MANAGER",
		"ROLE_PMS_PEOPLE_MANAGER", "ROLE_PMS_SR_MANAGER" })
public class MapaAlocacaoController {

	/*********** OUTCOMES **************************/

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_MAPA_ALOCACAO_ADD = "mapaAlocacao_add";
	/** outcome tela pesquisa da entidade. */
	private static final String OUTCOME_MAPA_ALOCACAO_RESEARCH = "mapaAlocacao_research";
	/** outcome tela gerenciamento da entidade. */
	private static final String OUTCOME_MAPA_ALOCACAO_MANAGE = "mapaAlocacao_manage";
	/** outcome tela gerenciamento da entidade - view. */
	private static final String OUTCOME_MAPA_ALOCACAO_MANAGE_VIEW = "mapaAlocacao_manage_view";
	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_MAPA_ALOCACAO_DASHBOARD = "mapaAlocacao_dashboard";

	/*********** SERVICES **************************/

	/** Instancia do servico. */
	@Autowired
	private IMapaAlocacaoService mapaAlocacaoService;

	/** Instancia do servico Contrato Pratica. */
	@Autowired
	private IContratoPraticaService contratoPraticaService;

	/** Instancia do servico Cidade Base. */
	@Autowired
	private ICidadeBaseService cidadeBaseService;

	/** Instancia do servico Cidade Base. */
	@Autowired
	private IPessoaCidadeBaseService pessoaCidadeBaseService;

	/** Instancia do servico Papel Vendido. */
	@Autowired
	private IPerfilVendidoService perfilVendidoService;

	/** Instancia do servico AlocacaoPeridoService. */
	@Autowired
	private IAlocacaoPeriodoService alocacaoPeriodoService;

	/** Instancia do servico RecursoService. */
	@Autowired
	private IRecursoService recursoService;

	/** Instancia do servico PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do servico AlocacaoService. */
	@Autowired
	private IAlocacaoService alocacaoService;

	/** Instancia do servico EtiquetaService. */
	@Autowired
	private IEtiquetaService etiquetaService;

	/** Instancia do servico ClienteService. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do servico NaturezaCentroLucro. */
	@Autowired
	private INaturezaCentroLucroService naturezaService;

	/** Instancia do servico Msa. */
	@Autowired
	private IMsaService msaService;

	/** Instancia do servico Contrato. */
	@Autowired
	private ICentroLucroService centroLucroService;

	/** Instancia do servico FatorUrMes. */
	@Autowired
	private IFatorUrMesService fatorUrService;

	/** Instancia do servico MapaAlocacaoPessoa. */
	@Autowired
	private IMapaAlocacaoPessoaService mapaAlocPessoaService;

	/** Instancia do servico ReceitaService. */
	@Autowired
	private IReceitaService receitaService;

	/** Instancia do DAO VwPmsFinancialsService. */
	@Autowired
	private IVwPmsFinancialsService vwPmsFinancialsService;
	/*********** BEANS **************************/

	/** Instancia do bean. */
	@Autowired
	private MapaAlocacaoBean bean = null;

	/** Instancia do bean de alocação. */
	@Autowired
	private AlocacaoBean alocacaoBean = null;

	/** Instancia do bean controle de mensagnes. */
	@Autowired
	private MessageControlBean messageConntrolBean = null;

	/********************************************/

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * @return the messageConntrolBean
	 */
	public MessageControlBean getMessageConntrolBean() {
		return messageConntrolBean;
	}

	/**
	 * @param messageConntrolBean
	 *            the messageConntrolBean to set
	 */
	public void setMessageConntrolBean(
			final MessageControlBean messageConntrolBean) {
		this.messageConntrolBean = messageConntrolBean;
	}

	/**
	 * Pega o bean MapaAlocacaoBean.
	 * 
	 * @return retorna o bean
	 */
	public MapaAlocacaoBean getBean() {
		return bean;
	}

	/**
	 * Seta o bean MapaAlocacaoBean.
	 * 
	 * @param bean
	 *            do tipo MapaAlocacaoBean
	 */
	public void setBean(final MapaAlocacaoBean bean) {
		this.bean = bean;
	}

	/**
	 * Pega o bean do alocacao.
	 * 
	 * @return AlocacaoBean
	 */
	public AlocacaoBean getAlocacaoBean() {
		return alocacaoBean;
	}

	/**
	 * Seta o AlocacaoBean.
	 * 
	 * @param alocacaoBean
	 *            do tipo AlocacaoBean
	 */
	public void setAlocacaoBean(final AlocacaoBean alocacaoBean) {
		this.alocacaoBean = alocacaoBean;
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareResearch() {
		bean.reset();

		this.loadContratoPraticaList(contratoPraticaService
				.findContratoPraticaAllComplete());

		this.loadClienteList(clienteService.findClienteAllClientePai());
		this.loadMsaList(msaService.findMsaAll());
		this.loadNaturezaList(naturezaService.findNaturezaCentroLucroAll());

		refreshMapFollow();

		return OUTCOME_MAPA_ALOCACAO_RESEARCH;
	}

	/**
	 * Prepara a tela para criar uma entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareCreate() {
		messageConntrolBean.setDisplayMessages(Boolean.valueOf(true));

		bean.reset();
		// marca como modo de inserção
		bean.setIsUpdate(Boolean.FALSE);
		// carrega a lista ContratoPratica
		this.loadContratoPraticaList(contratoPraticaService
				.findContratoPraticaAllCompleteAndActive());

		return OUTCOME_MAPA_ALOCACAO_ADD;
	}

	/**
	 * Prepara a tela de gerenciamento do MapaAlocacao - criacao.
	 * 
	 * @return pagina de destino
	 */
	public String prepareCreateNext() {

		bean.setShowModalSaveAs(Boolean.FALSE);
		// atribui nome para o mapa de alocação
		setNewTxtTitulo();

		// valida a vigência
		if (DateUtil.validateValidityDate(bean.getValidityMonthBeg(),
				bean.getValidityYearBeg(), bean.getValidityMonthEnd(),
				bean.getValidityYearEnd(),
				Constants.DEFAULT_LABEL_VALIDITY_DATE_MAPA_ALOCACAO,
				bean.getIsUpdate(), this.getClosingDate())) {

			// verifica o MapaAlocacao e atualiza o ContratoPratica
			MapaAlocacao mapaAlocacao = this.verifyCreateOrUpdate();

			// se existir o ContratoPratica redireciona para a
			// tela de gerenciamento do MapaAlocacao
			if (mapaAlocacao != null) {

				// valida ContratoPratica x versão do MapaAlocacao
				if (!mapaAlocacaoService.isValidAllocationMapVersion(
						mapaAlocacao.getContratoPratica()
								.getCodigoContratoPratica(), mapaAlocacao
								.getIndicadorVersao())) {

					// se o MapaAlocacao não for válido, retorna null
					return null;
				}

				// seta a data inicio e fim do mapa de alocação
				mapaAlocacao.setDataInicio(DateUtil.getDate(
						bean.getValidityMonthBeg(), bean.getValidityYearBeg()));
				// seta a data inicio da janela de visualização
				mapaAlocacao.setDataInicioJanela(mapaAlocacao.getDataInicio());
				// seta a data fim do mapa de aloação
				mapaAlocacao.setDataFim(DateUtil.getDate(
						bean.getValidityMonthEnd(), bean.getValidityYearEnd()));

				// seta a lista de FatorUrMes do mapa de alocação
				List<FatorUrMes> fatorUrList = mapaAlocacaoService
						.createUrMonthList(mapaAlocacao);

				mapaAlocacao.setFatorUrMeses(fatorUrList);

				// *********** Cria a lista de FatorUr visivel *****************
				List<FatorUrMes> fatorUrVisibleList = new ArrayList<FatorUrMes>();

				Date endDateWin = mapaAlocacaoService
						.getMapaAlocacaoEndDateWindow(mapaAlocacao);

				for (FatorUrMes fatorUrMes : fatorUrList) {
					if (!DateUtil.before(endDateWin, fatorUrMes.getDataMes())) {
						fatorUrVisibleList.add(fatorUrMes);
					} else {
						break;
					}
				}
				// ************************************************************

				// seta a lista de fatores ur visiveis
				bean.setFatorUrMesList(fatorUrVisibleList);

				// seta a janela de visualização
				this.setMapPeriodWindow(mapaAlocacao);

				bean.setAlocacaoRowList(new ArrayList<AlocacaoRow>());

				// seta o mapa de alocacao no bean
				bean.setTo(mapaAlocacao);

				return this.create();

				// se o ContratoPratica nao existir na base de dados
			} else {
				Messages.showError("prepareCreateNext",
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
						Constants.ENTITY_NAME_CONTRATO_PRATICA);
			}
		}

		// retorna null caso a vigência seja inválida
		return null;
	}

	/**
	 * Insere uma entidade.
	 * 
	 * @return pagina de destino
	 */
	public String create() {
		updateAlocacaoTotalValues();

		// habilita as mensagens de validação
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);

		// se a matriz foi "traduzida" corretamente
		// if (success) {
		MapaAlocacao mapaAlocacao = bean.getTo();

		// seta a data inicio do mapa
		mapaAlocacao.setDataInicio(DateUtil.getDate(bean.getValidityMonthBeg(),
				bean.getValidityYearBeg()));
		// seta a data fim do mapa
		mapaAlocacao.setDataFim(DateUtil.getDate(bean.getValidityMonthEnd(),
				bean.getValidityYearEnd()));

		// atribui o ContratoPratica
		mapaAlocacao.setContratoPratica(contratoPraticaService
				.findContratoPraticaById(mapaAlocacao.getContratoPratica()
						.getCodigoContratoPratica()));

		// ********** Cria a lista de FatorUrMes ***************************
		List<FatorUrMes> fatorUrMesList = bean.getFatorUrMesList();
		List<FatorUrMes> fatorUrMesListAux = new ArrayList<FatorUrMes>(
				fatorUrMesList);
		Map<Long, FatorUrMes> fatorUrMesMapAux = new HashMap<Long, FatorUrMes>();
		for (FatorUrMes fatorUrMes : fatorUrMesList) {
			fatorUrMesMapAux.put(fatorUrMes.getDataMes().getTime(), fatorUrMes);
		}

		List<FatorUrMes> fatorUrMeses = mapaAlocacao.getFatorUrMeses();
		for (FatorUrMes fatorUrMes : fatorUrMeses) {

			if (!fatorUrMesMapAux
					.containsKey(fatorUrMes.getDataMes().getTime())) {
				fatorUrMesListAux.add(fatorUrMes);
			}
		}
		// ******************************************************************

		// seta a lista de FatorUrMes
		mapaAlocacao.setFatorUrMeses(fatorUrMesListAux);

		if (mapaAlocacao.getCodigoMapaAlocacao() == null) {
			// cria o MapaAlocacao
			mapaAlocacaoService.createMapaAlocacao(mapaAlocacao);
		}

		// mensagem de sucesso
		Messages.showSucess("create", Constants.DEFAULT_MSG_SUCCESS_CREATE,
				Constants.ENTITY_NAME_MAPA_ALOCACAO);

		bean.reset();
		bean.setCurrentRowId(mapaAlocacao.getCodigoMapaAlocacao());

		// carrega a tela de gerenciamento (alteração do MapaAlocacao)
		return prepareManage();

	}

	/**
	 * Prepara a tela de gerenciamento do MapaAlocacao - atualizacao.
	 * 
	 * @return pagina de destino
	 */
	public String prepareManage() {
		bean.resetVigencia();
		bean.resetValidityDateList();
		bean.resetAlocacaoRowList();
		bean.resetEtiquetaFileds();
		bean.setShowModalSaveAs(Boolean.FALSE);

		// marca como modo de alterção
		bean.setIsUpdate(Boolean.TRUE);

		// busca o MapaAlocacao corrente pelo código
		findById(bean.getCurrentRowId());

		// verifica o MapaAlocacao e atualiza o ContratoPratica
		MapaAlocacao mapaAlocacao = bean.getTo();

		// se existir o ContratoPratica redireciona para a
		// tela de gerenciamento do MapaAlocacao
		if (mapaAlocacao != null) {

			// pega a lista de alocações do mapa
			List<Alocacao> alocacaoList = alocacaoService
					.findAlocacaoByMapaAlocacaoAndPeriod(mapaAlocacao);
			// cria a matriz
			bean.setAlocacaoRowList(mapaAlocacaoService
					.prepareUpdateMatrixAlocacao(alocacaoList,
							bean.getValidityDateList(), mapaAlocacao));

			// seta a lista de fatores ur visiveis
			bean.setFatorUrMesList(fatorUrService
					.findFatorUrMesByMapaAndPeriod(mapaAlocacao));

			// carrega a lista de Etiqueta do Cliente corrente
			Cliente cliente = getClienteByMapaAlocacao(mapaAlocacao);
			if (cliente != null) {
				loadEtiquetaList(etiquetaService
						.findEtiquetaAtivaByCliente(cliente.getCodigoCliente()));
			}

			// seta no beam o mapa de alocação
			bean.setTo(mapaAlocacao);
			updateAlocacaoTotalValues();

			mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);

			// realiza o lock do mapa de alocação
			if (mapaAlocacaoService.lockMapaAlocacao(mapaAlocacao)) {

				bean.setIsAlocacaoListButtonsEnabled(Boolean.TRUE);

				return OUTCOME_MAPA_ALOCACAO_MANAGE;

				// caso não seja possível fazer lock do mapa
			} else {
				Messages.showError("prepareManage",
						Constants.MSG_ERROR_MAPA_ALOCACAO_LOCK_UNLOCK,
						mapaAlocacao.getLoginTrava());

				return OUTCOME_MAPA_ALOCACAO_MANAGE_VIEW;
			}

			// se o ContratoPratica nao existir na base de dados
		} else {
			Messages.showError("prepareManage",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_CONTRATO_PRATICA);
		}

		// retorna null caso a vigência seja inválida
		return null;
	}

	/**
	 * Realiza o update do periodo do mapa de aloação.
	 * 
	 * @return pagina de destino
	 */
	public String updateMapPeriod() {
		// verifica o MapaAlocacao e atualiza o ContratoPratica
		MapaAlocacao to = this.verifyCreateOrUpdate();

		// valida a vigência
		if (mapaAlocacaoService.validateAlocationMapPeriod(to,
				bean.getValidityMonthBeg(), bean.getValidityYearBeg(),
				bean.getValidityMonthEnd(), bean.getValidityYearEnd())) {

			// busca novamente o MapaAlocacao para abrir
			// sessão de conexão com o banco (hibernate)
			MapaAlocacao mapaAlocacao = mapaAlocacaoService
					.findMapaAlocacaoById(to.getCodigoMapaAlocacao());

			bean.setTo(mapaAlocacao);

			// se existir o ContratoPratica redireciona para a
			// tela de gerenciamento do MapaAlocacao
			if (mapaAlocacao != null) {
				// seta a data inicio
				Date startDate = DateUtil.getDate(bean.getValidityMonthBeg(),
						bean.getValidityYearBeg());
				bean.setStartDateWindow(startDate);
				// seta a data fim
				Date endDate = DateUtil.getDate(bean.getValidityMonthEnd(),
						bean.getValidityYearEnd());

				mapaAlocacaoService.updateMapaAlocacaoPeriod(mapaAlocacao,
						startDate, endDate);

				return prepareManage();
				// se o ContratoPratica nao existir na base de dados
			} else {
				Messages.showError("updateMapPeriod",
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
						Constants.ENTITY_NAME_CONTRATO_PRATICA);
			}
		}

		// como não passou pela validação das datas,
		// seta o as datas de vigencia do bean com as datas originais
		this.setMapPeriodWindow(to);

		// retorna null caso a vigência seja inválida
		return null;
	}

	/**
	 * Realiza a atualizacao da entidade MapaAlocacao completa (Alocacao e
	 * AlocacaoPeriodo).
	 * 
	 */
	public void update() {
		updateAlocacaoTotalValues();

		// habilita as mensagens de validação
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);

		// carrega a lista de Alocacao x AlocacaoPeriodo para ser persistida no
		// banco a partir da matriz da tela
		Boolean success = mapaAlocacaoService.updateMatrixAlocacao(bean
				.getAlocacaoRowList());

		// se a matriz foi "traduzida" corretamente
		if (success) {
			// busca novamente o MapaAlocacao para abrir sessão com o
			// banco(hibernate)
			MapaAlocacao mapaAlocacao = mapaAlocacaoService
					.findMapaAlocacaoById(bean.getTo().getCodigoMapaAlocacao());

			mapaAlocacao.setFatorUrMeses(bean.getFatorUrMesList());

			// altera o MapaAlocacao
			mapaAlocacaoService.updateMapaAlocacao(mapaAlocacao);

			// mensagem de sucesso
			Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_MAPA_ALOCACAO);

			// atualiza o MapaAlocacao na memória
			bean.setTo(mapaAlocacao);

			this.prepareManage();
		}
	}

	/**
	 * Prepara a tela de remoção da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareRemove() {
		// marca o modo de remoção para que a tela mostre o botão de excluir
		bean.setIsRemove(Boolean.TRUE);

		// carrega o MapaAlocacao em modo de visualização
		loadMapaAlocacaoManageView();

		return OUTCOME_MAPA_ALOCACAO_MANAGE_VIEW;
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @return pagina de destino
	 * 
	 */
	public String remove() {
		Boolean success = false;

		try {
			// tenta remover o MapaAlocacao corrente
			success = mapaAlocacaoService
					.removeMapaAlocacao(mapaAlocacaoService
							.findMapaAlocacaoById(bean.getTo()
									.getCodigoMapaAlocacao()));

		} catch (DataIntegrityViolationException e) {
			// caso não passe na validação,
			// capitura a exceção e dá mensagem de erro
			Messages.showError("remove",
					Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
					Constants.ENTITY_NAME_MAPA_ALOCACAO);
		}

		if (success) {
			// caso seja removido com sucesso, dá mensagem de sucesso
			Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_MAPA_ALOCACAO);
			bean.resetTo();
			// faz a busca novamente para recarregar a lista da pesquisa
			findByFilter();
			return OUTCOME_MAPA_ALOCACAO_RESEARCH;
		} else {
			return null;
		}

	}

	/**
	 * Prepara a tela de visualização da entidade.
	 * 
	 * @return retorna a pagina de visualização do mapa de alocação
	 */
	public String prepareView() {
		// marca o modo de remoção como false (view) para que a tela não mostre
		// o botão de excluir
		bean.setIsRemove(Boolean.FALSE);
		bean.setIsUpdate(Boolean.FALSE);
		// carrega o MapaAlocacao em modo de visualização
		loadMapaAlocacaoManageView();

		return OUTCOME_MAPA_ALOCACAO_MANAGE_VIEW;
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 */
	public void findById(final Long id) {
		MapaAlocacao mapa = mapaAlocacaoService.findMapaAlocacaoById(id);
		bean.setTo(mapa);

		if (bean.getTo() == null
				|| bean.getTo().getCodigoMapaAlocacao() == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		} else {

			this.setMapPeriodWindow(mapa);

		}
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 */
	public void findByFilter() {
		MapaAlocacao filter = bean.getFilter();
		Long contratoPraticaId = bean.getContratoPraticaMap().get(
				filter.getContratoPratica().getNomeContratoPratica());

		if (contratoPraticaId != null) {
			filter.getContratoPratica().setCodigoContratoPratica(
					contratoPraticaId);

		} else if (!"".equals(filter.getContratoPratica()
				.getNomeContratoPratica())) {
			filter.getContratoPratica().setCodigoContratoPratica(-1L);

		} else {
			filter.getContratoPratica().setCodigoContratoPratica(0L);
		}

		Cliente cli = bean.getCliente();
		cli.setCodigoCliente(bean.getClienteMap().get(cli.getNomeCliente()));

		Msa msa = bean.getMsa();
		msa.setCodigoMsa(bean.getMsaMap().get(msa.getNomeMsa()));

		NaturezaCentroLucro natureza = bean.getNatureza();
		natureza.setCodigoNatureza(bean.getNaturezaMap().get(
				natureza.getNomeNatureza()));

		CentroLucro centroLucro = bean.getCentroLucro();
		if (centroLucro != null
				&& centroLucro.getNomeCentroLucro() != null
				&& !centroLucro.getNomeCentroLucro().trim().isEmpty()
				&& bean.getCentroLucroMap().get(
						centroLucro.getNomeCentroLucro()) == null) {
			Messages.showError("prepareCreateNext",
					Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
					Constants.ENTITY_NAME_CENTRO_LUCRO);
			bean.setResultList(null);
			return;
		}
		centroLucro.setCodigoCentroLucro(bean.getCentroLucroMap().get(
				centroLucro.getNomeCentroLucro()));

		Boolean isFollowingOnly = bean.getIsFollowingOnly();
		if (isFollowingOnly == null) {
			isFollowingOnly = Boolean.valueOf(false);
		}

		// realiza a busca pelo MapaAlocacao
		bean.setResultList(mapaAlocacaoService.findMapaAlocacaoByFilterFetch(
				filter, cli, msa, natureza, centroLucro, isFollowingOnly));
		// se nenhum resultado encontrado
		if (bean.getResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// volta para a primeira pagina
		bean.setCurrentPageId(0);
	}

	/**
	 * Prepara a tela para adicionar uma Alocacao.
	 * 
	 */
	private void prepareAddAlocacao() {
		alocacaoBean.reset();
		alocacaoBean.setIsUpdate(Boolean.FALSE);

		// edicao
		if (bean.getIsUpdate()) {
			MapaAlocacao mapa = mapaAlocacaoService.findMapaAlocacaoById(bean
					.getTo().getCodigoMapaAlocacao());

			this.loadPerfilVendidoList(perfilVendidoService
					.findPerfilVendidoByMsaWithCurrency(mapa
							.getContratoPratica().getMsa()));
			// insercao
		} else {

			this.loadPerfilVendidoList(perfilVendidoService
					.findPerfilVendidoByMsaWithCurrency(contratoPraticaService
							.findContratoPraticaById(
									bean.getTo().getContratoPratica()
											.getCodigoContratoPratica())
							.getMsa()));
		}

		messageConntrolBean.setDisplayMessages(Boolean.FALSE);
	}

	/**
	 * Prepara a tela para adicionar uma alocacao do tipo Papel.
	 */
	public void prepareAddPapel() {
		this.prepareAddAlocacao();

		loadCidadeBaseList(cidadeBaseService.findCidadeBaseAllActive());
		loadRecursoList(recursoService
				.findRecursoByTipo(Constants.RESOURCE_TYPE_PA));

		alocacaoBean.setIsAddPessoa(Boolean.FALSE);
		bean.setIsPessoa(Boolean.FALSE);
		alocacaoBean.setIsUpdate(Boolean.FALSE);
	}

	/**
	 * Prepara a tela para adicionar uma alocacao do tipo Pessoa.
	 */
	public void prepareAddPessoa() {
		this.prepareAddAlocacao();

		alocacaoBean.setIsAddPessoa(Boolean.TRUE);
		bean.setIsPessoa(Boolean.TRUE);
		alocacaoBean.setIsUpdate(Boolean.FALSE);

		alocacaoBean.setCidadeBaseList(null);
	}

	/**
	 * Adiciona uma Alocacao na lista de alocacoes do MapaAlocacao.
	 * 
	 */
	public void addAlocacao() {
		// popula a Alocacao corrente
		Alocacao alocacao = addOrEditAlocacao(alocacaoBean.getTo());

		if (alocacao != null) {
			// atribui o valor do Utilization Rate
			alocacao.setValorUr(alocacaoBean.getValorUr());
			
			// adiciona a Alocacao na matriz
			if (mapaAlocacaoService.addAlocacaoMatrix(alocacao,
					bean.getAlocacaoRowList(), bean.getValidityDateList(),
					alocacaoBean.getPercentualAlocacao())) {
				
				messageConntrolBean.setDisplayMessages(Boolean.valueOf(true));
				updateAlocacaoTotalValues();
				
			}			
		}
	}

	/**
	 * Prepara a tela para editar uma Alocacao.
	 * 
	 */
	public void prepareEditAlocacao() {
		alocacaoBean.reset();
		alocacaoBean.setIsUpdate(Boolean.TRUE);
		alocacaoBean.setIsAddPessoa(Boolean.TRUE);

		// loadRecursoList(recursoService.findRecursoAll());

		loadCidadeBaseList(cidadeBaseService.findCidadeBaseAllActive());

		// seta a alocação a ser editada no bean
		alocacaoBean.setTo(bean.getAlocacaoRow().getAlocacao());

		// edicao do mapa
		if (bean.getIsUpdate()) {
			MapaAlocacao mapa = mapaAlocacaoService.findMapaAlocacaoById(bean
					.getTo().getCodigoMapaAlocacao());

			this.loadPerfilVendidoList(perfilVendidoService
					.findPerfilVendidoByMsaWithCurrency(mapa
							.getContratoPratica().getMsa()));

			// insercao do mapa
		} else {
			this.loadPerfilVendidoList(perfilVendidoService
					.findPerfilVendidoByMsaWithCurrency(contratoPraticaService
							.findContratoPraticaById(
									bean.getTo().getContratoPratica()
											.getCodigoContratoPratica())
							.getMsa()));
		}

		bean.setIsPessoa(recursoService.isPessoa(alocacaoBean.getTo()
				.getRecurso().getCodigoMnemonico()));

		bean.setToAlocacao(null);
		messageConntrolBean.setDisplayMessages(Boolean.FALSE);

		// clones dos objetos para quebrar as referencias duplicadas
		Alocacao alocacao = alocacaoBean.getTo();
		alocacao.setRecurso(alocacao.getRecurso().getClone());
		alocacao.setCidadeBase(alocacao.getCidadeBase().getClone());
		alocacao.setPerfilVendido(alocacao.getPerfilVendido().getClone());
	}

	/**
	 * Ação utilizada no autocomplete do add/edit recurso. Retorna uma lista de
	 * recursos.
	 * 
	 * @param value
	 *            - valor (mnemonico) utilizado na busca dos recursos
	 * 
	 * @return retorna uma lista de recurso
	 */
	public List<Recurso> autoCompleteRecurso(final Object value) {
		String tipoRecurso = null;

		if (alocacaoBean.getIsUpdate()) {
			tipoRecurso = null;
		} else {
			if (alocacaoBean.getIsAddPessoa()) {
				tipoRecurso = Constants.RESOURCE_TYPE_PE;
				alocacaoBean.setIsAddPessoa(Boolean.TRUE);
			} else {
				tipoRecurso = Constants.RESOURCE_TYPE_PA;
				alocacaoBean.setIsAddPessoa(Boolean.FALSE);
			}
		}

		return recursoService.quickSearch((String) value, tipoRecurso);
	}

	/**
	 * Edita uma Alocacao na lista de alocacoes do MapaAlocacao.
	 * 
	 */
	public void editAlocacao() {
		// popula a Alocacao corrente
		Alocacao alocacao = alocacaoBean.getTo();
		addOrEditAlocacao(alocacao);

		alocacaoService.updateAlocacao(alocacao);

		messageConntrolBean.setDisplayMessages(Boolean.valueOf(true));
	}

	/**
	 * Realizao update do U.R. (utilization rate)
	 */
	public void updateUr() {

		Alocacao alocacao = alocacaoBean.getTo();

		Alocacao aloc = alocacaoService.findAlocacaoById(alocacao
				.getCodigoAlocacao());

		aloc.setValorUr(alocacao.getValorUr());

		alocacaoService.updateAlocacao(aloc);
	}

	/**
	 * Popula a Alocacao para ser adicionada ou editada na matriz.
	 * 
	 * @param alocacao
	 *            - a Alocacao a ser populada
	 * @return a Alocacao populada
	 */
	private Alocacao addOrEditAlocacao(final Alocacao alocacao) {

		Recurso recurso = null;

		// atribui o MapaAlocacao corrente para a Alocacao corrente
		alocacao.setMapaAlocacao(bean.getTo());

		// atribui o Recurso para a Alocacao corrente
		// Alocacao alocacaoTo = alocacaoBean.getTo();
		recurso = recursoService.findRecursoByMnemonico(alocacao.getRecurso()
				.getCodigoMnemonico());

		alocacao.setRecurso(recurso);

		// atribui o PerfilVendido para a Alocacao corrente
		Long codigoPerfilVendido = alocacaoBean.getPerfilVendidoMap().get(
				alocacao.getPerfilVendido().getNomePerfilVendido());
		if (codigoPerfilVendido != null) {
			alocacao.setPerfilVendido(perfilVendidoService
					.findPerfilVendidoById(codigoPerfilVendido));
		}

		CidadeBase base = null;
		// verifica se é uma pessoa. Se verdadeiro pega a base dela.
		if (recursoService.isPessoa(recurso.getCodigoMnemonico())) {
			Pessoa p = pessoaService.findPessoaByRecurso(recurso);

			PessoaCidadeBase pessCidBase = pessoaCidadeBaseService
					.findPessCBByPessoaAndDate(p, new Date());

			if (pessCidBase != null) {
				base = pessCidBase.getCidadeBase();
			}

			// senão pega a base passada pelo fromulário
		} else {
			// atribui a CidadeBase para a Alocacao corrente
			Long codigoCidadeBase = alocacaoBean.getCidadeBaseMap().get(
					alocacao.getCidadeBase().getNomeCidadeBase());
			if (codigoCidadeBase != null) {
				base = cidadeBaseService.findCidadeBaseById(codigoCidadeBase);
			}
		}
		// seta a base
		if (base != null) {
			alocacao.setCidadeBase(base);
		} else {
			Messages.showError("codigoMnemonico", Constants.MAPA_ALOCACAO_ERROR_PERSON_WITHOUT_CITY);

			return null;
		}

		return alocacao;
	}

	/**
	 * Deleta uma Alocacao na lista de alocacoes do MapaAlocacao.
	 * 
	 */
	public void deleteAlocacao() {
		// deleta a Alocacao na matriz
		mapaAlocacaoService.deleteAlocacaoMatrix(bean.getToAlocacao(),
				bean.getAlocacaoRowList());
		updateAlocacaoTotalValues();
	}

	/**
	 * Seta todas as alocações selecionadas como removidas.
	 */
	public void deleteAlocacaoAll() {
		// verifica se algum item foi selecionado
		if (isSomeAlocationSelected()) {
			List<AlocacaoRow> alocacaoRowList = bean.getAlocacaoRowList();

			mapaAlocacaoService.removeAlocacao(alocacaoRowList);

			updateAlocacaoTotalValues();

			Messages.showSucess("deleteAlocacaoAll",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_ALOCACAO);
		} else {
			Messages.showWarning("deleteAlocacaoAll",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Seleciona todas as alocações do Mapa.
	 */
	public void selectAllAlocacao() {
		List<AlocacaoRow> alocacaoRowList = bean.getAlocacaoRowList();

		for (AlocacaoRow row : alocacaoRowList) {
			if (!row.getIsRemove() && row.getIsView()) {
				row.setIsSelected(Boolean.valueOf(true));
			} else if (row.getIsRemove() || !row.getIsView()) {
				row.setIsSelected(Boolean.valueOf(false));
			}
		}
	}

	/**
	 * Deseleciona todas as alocações do Mapa.
	 */
	public void unselectAllAlocacao() {
		List<AlocacaoRow> alocacaoRowList = bean.getAlocacaoRowList();

		for (AlocacaoRow row : alocacaoRowList) {
			row.setIsSelected(Boolean.valueOf(false));
		}
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
	 * Verifica se o valor do atributo PerfilVendido é valido. Se o valor não é
	 * nulo e existe no getPerfilVendidoMapAlocClone, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePerfilVendidoAlocClone(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codStage = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codStage = bean.getPerfilVendidoMapAlocClone().get(strValue);
			if (codStage == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo CidadeBase é valido. Se o valor não é
	 * nulo e existe no getCidadeBaseMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCidadeBase(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codStage = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codStage = alocacaoBean.getCidadeBaseMap().get(strValue);
			if (codStage == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo PerfilVendido é valido. Se o valor não é
	 * nulo e existe no getPerfilVendidoMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validatePerfilVendido(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codStage = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codStage = alocacaoBean.getPerfilVendidoMap().get(strValue);
			if (codStage == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Perfil Vendido para combobox de Perfil vendido.
	 * 
	 * @param pVendidoList
	 *            lista de PerfilVendido.
	 * 
	 */
	private void loadPerfilVendidoListAlocClone(
			final List<PerfilVendido> pVendidoList) {
		Map<String, Long> perfilVendidoMapAlocClone = new HashMap<String, Long>();
		List<String> perfilVendidoListAlocClone = new ArrayList<String>();

		for (PerfilVendido pv : pVendidoList) {
			perfilVendidoMapAlocClone.put(pv.getNomePerfilVendido(),
					pv.getCodigoPerfilVendido());
			perfilVendidoListAlocClone.add(pv.getNomePerfilVendido());
		}

		bean.setPerfilVendidoMapAlocClone(perfilVendidoMapAlocClone);
		bean.setPerfilVendidoListAlocClone(perfilVendidoListAlocClone);
	}

	/**
	 * Verifica se o mnemonico escolhido é Pessoa ou Papel.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void verifyRecurso(final ValueChangeEvent e) {
		String mnemonico = (String) e.getNewValue();
		if (mnemonico != null) {
			if (recursoService.isPessoa(mnemonico)) {
				bean.setIsPessoa(Boolean.TRUE);
				alocacaoBean.getTo().getCidadeBase().setNomeCidadeBase("");
			} else {
				bean.setIsPessoa(Boolean.FALSE);
			}
		}
	}

	/**
	 * Popula a lista de Perfil Vendido para combobox de Perfil vendido.
	 * 
	 * @param pVendidoList
	 *            lista de PerfilVendido.
	 * 
	 */
	private void loadPerfilVendidoList(final List<PerfilVendido> pVendidoList) {
		Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();
		List<String> perfilVendidoList = new ArrayList<String>();

		for (PerfilVendido pv : pVendidoList) {
			perfilVendidoMap.put(pv.getNomePerfilVendido(),
					pv.getCodigoPerfilVendido());
			perfilVendidoList.add(pv.getNomePerfilVendido());
		}

		alocacaoBean.setPerfilVendidoMap(perfilVendidoMap);
		alocacaoBean.setPerfilVendidoList(perfilVendidoList);
	}

	/**
	 * Popula a lista de Cidade Base para combobox de base.
	 * 
	 * @param bases
	 *            lista de CidadeBase.
	 * 
	 */
	private void loadCidadeBaseList(final List<CidadeBase> bases) {
		Map<String, Long> cidadeMap = new HashMap<String, Long>();
		List<String> cidadeList = new ArrayList<String>();

		for (CidadeBase base : bases) {
			cidadeMap.put(base.getNomeCidadeBase(), base.getCodigoCidadeBase());
			cidadeList.add(base.getNomeCidadeBase());
		}
		alocacaoBean.setCidadeBaseMap(cidadeMap);
		alocacaoBean.setCidadeBaseList(cidadeList);
	}

	/**
	 * Popula a lista de Recurso para combobox de base.
	 * 
	 * @param recursos
	 *            lista de Recruso.
	 * 
	 */
	private void loadRecursoList(final List<Recurso> recursos) {
		Map<String, Long> recursoMap = new HashMap<String, Long>();
		List<String> recursoList = new ArrayList<String>();

		for (Recurso recurso : recursos) {
			recursoMap.put(recurso.getCodigoMnemonico(),
					recurso.getCodigoRecurso());
			recursoList.add(recurso.getCodigoMnemonico());
		}

		alocacaoBean.setRecursoMap(recursoMap);
		alocacaoBean.setRecursoList(recursoList);
	}

	/**
	 * Popula a lista de Etiqueta para combobox.
	 * 
	 * @param pEtiquetaList
	 *            lista de Etiqueta.
	 * 
	 */
	private void loadEtiquetaList(final List<Etiqueta> pEtiquetaList) {
		Map<String, Long> etiquetaMap = new HashMap<String, Long>();
		List<String> etiquetaList = new ArrayList<String>();

		String statusAll = BundleUtil.getBundle(Constants.BUNDLE_KEY_ALL);
		etiquetaMap.put(statusAll, Long.valueOf(-1));
		etiquetaList.add(statusAll);

		String statusWithoutTag = BundleUtil
				.getBundle(Constants.BUNDLE_KEY_WITHOUT_TAG);
		etiquetaMap.put(statusWithoutTag, Long.valueOf(0));
		etiquetaList.add(statusWithoutTag);

		for (Etiqueta etiqueta : pEtiquetaList) {
			etiquetaMap.put(etiqueta.getNomeEtiqueta(),
					etiqueta.getCodigoEtiqueta());
			etiquetaList.add(etiqueta.getNomeEtiqueta());
		}

		bean.setEtiquetaMap(etiquetaMap);
		bean.setEtiquetaList(etiquetaList);
	}

	/**
	 * Valida o MapaAlocacao antes de realizar o a copia (Save As).
	 * 
	 * @return se salvo com sucesso retorna true, caso contrario false.
	 */
	private Boolean validateSaveAs() {
		// habilita as mensagens de validação
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);

		// salva/atualiza a lista de alocações
		Boolean success = mapaAlocacaoService.updateMatrixAlocacao(bean
				.getAlocacaoRowList());

		return success;
	}

	/**
	 * Verifica se já existe um mapa de alocacao com o contrato-pratica e versão
	 * passados por parametro.
	 * 
	 * @return pagina de destino.
	 */
	public String existsMapaAlocacao() {
		MapaAlocacao mapa = bean.getTo();
		if (mapaAlocacaoService.isValidAllocationMapVersion(mapa
				.getContratoPratica().getCodigoContratoPratica(), bean
				.getMapaAlocacaoVersion())) {

			bean.setShowModalSaveAs(Boolean.FALSE);
			return saveAs();
		}

		bean.setShowModalSaveAs(Boolean.TRUE);
		messageConntrolBean.setDisplayMessages(Boolean.FALSE);
		return null;
	}

	/**
	 * Realiza a copia de um MapaAlocacao.
	 * 
	 * @return retorna a pagina de gerenciamento do mapa
	 */
	public String saveAs() {
		messageConntrolBean.setDisplayMessages(Boolean.TRUE);

		if (validateSaveAs()) {
			MapaAlocacao cloneMapa = mapaAlocacaoService.saveAs(bean.getTo(),
					bean.getMapaAlocacaoVersion());

			bean.setCurrentRowId(cloneMapa.getCodigoMapaAlocacao());

			Messages.showSucess("saveAs",
					Constants.MSG_SUCCESS_CONTRATO_PRATICA_SAVE_AS);
			if (cloneMapa.getEstratificacaoUrs().size() > 0) {
				Messages.showWarning(
						"saveAs",
						Constants.MSG_WARNG_MAPA_ALOCACAO_SAVE_AS_STRATIFICATION);
			}

			bean.setShowModalSaveAs(Boolean.FALSE);
			bean.setMapaAlocacaoVersion(null);

			refreshMapFollow();

			return this.prepareManage();
		}

		bean.setShowModalSaveAs(Boolean.FALSE);

		return null;
	}

	/**
	 * Gera o Dashboard do MapaAlocacao.
	 * 
	 * @return Dashboard
	 */
	public String saveAsDashboard() {
		if (validateSaveAs()) {
			return this.prepareFinancials();
		}

		return null;
	}

	/**
	 * Faz a verificação do ContratoPratica para criar ou editar um
	 * MapaAlocacao.
	 * 
	 * @return mapa - retrona o mapa alocacao
	 */
	private MapaAlocacao verifyCreateOrUpdate() {
		MapaAlocacao mapaAlocacao = bean.getTo();

		Long contratoPraticaId = bean.getContratoPraticaMap().get(
				mapaAlocacao.getContratoPratica().getNomeContratoPratica());

		ContratoPratica cp = null;
		if (contratoPraticaId != null) {
			cp = contratoPraticaService
					.findContratoPraticaById(contratoPraticaId);
		}

		// se existir o ContratoPratica é setado no mapa
		if (cp != null) {
			mapaAlocacao.setContratoPratica(cp);
			return mapaAlocacao;
		}

		return null;
	}

	/**
	 * Carrega os conteudo de uma mapa de alocação (MapaAlocacao, Alocacao,
	 * AlocacaoPeriodo), para exibir na tela.
	 * 
	 */
	private void loadMapaAlocacaoManageView() {
		MapaAlocacao mapa = mapaAlocacaoService.findMapaAlocacaoById(bean
				.getTo().getCodigoMapaAlocacao());
		// carrega a lista de AlocacaoRow
		bean.setAlocacaoRowList(mapaAlocacaoService
				.loadMapaAlocacaoManageView(mapa));

		// seta a vigencia do mapa no bean
		// seta a data inicial
		Date dataInicial = fatorUrService.findFatorUrMesMinDateByMapa(mapa);
		if (dataInicial != null) {
			bean.setValidityMonthBeg("" + DateUtil.getMonthNumber(dataInicial));
			bean.setValidityYearBeg("" + DateUtil.getYear(dataInicial));
		}
		// seta a data final
		Date dataFinal = fatorUrService.findFatorUrMesMaxDateByMapa(mapa);
		if (dataFinal != null && dataInicial != null) {
			bean.setValidityMonthEnd("" + DateUtil.getMonthNumber(dataFinal));
			bean.setValidityYearEnd("" + DateUtil.getYear(dataFinal));
		}
		// cria a lista de datas dado data inicial e final da vigência
		bean.setValidityDateList(DateUtil.getValidityDateList(dataInicial,
				dataFinal));
		// seta o TO
		bean.setTo(mapa);
		// carrega a lista de Etiqueta do Cliente corrente
		Cliente cliente = getClienteByMapaAlocacao(mapa);
		if (cliente != null) {
			loadEtiquetaList(etiquetaService.findEtiquetaAtivaByCliente(cliente
					.getCodigoCliente()));
		}

		updateAlocacaoTotalValues();
		bean.setIsUpdate(Boolean.FALSE);
	}

	/**
	 * Habilita / desabilita se os logins podem ser editados.
	 */
	public void changeStatusAlocacaoListButtons() {
		bean.setIsAlocacaoListButtonsEnabled(!bean
				.getIsAlocacaoListButtonsEnabled());
		if (bean.getIsAlocacaoListButtonsEnabled()) {
			updateAlocacaoTotalValues();
		}
	}

	/**
	 * Realiza o lock de um MapaAlocacao.
	 * 
	 * @return retorna a pagina de destino
	 */
	public String lockMapaAlocacao() {
		MapaAlocacao mapa = bean.getTo();

		if (mapaAlocacaoService.lockMapaAlocacao(mapa)) {
			bean.setIsLocked(Boolean.TRUE);

			return this.prepareManage();
		} else {
			Messages.showError("lockMapaAlocacao",
					Constants.MSG_ERROR_MAPA_ALOCACAO_LOCK_UNLOCK,
					mapa.getLoginTrava());

			return null;
		}
	}

	/**
	 * Realiza o unlock do MapaAlocacao.
	 * 
	 * @return retorna a pagina de destino
	 */
	public String unlockMapaAlocacao() {
		MapaAlocacao mapa = bean.getTo();

		if (mapaAlocacaoService.unlockMapaAlocacao(mapa)) {
			bean.setIsLocked(Boolean.FALSE);
			Messages.showSucess("unlockMapaAlocacao",
					Constants.MSG_SUCCESS_MAPA_ALOCACAO_UNLOCK);

			return this.prepareView();
		} else {
			Messages.showError("unlockMapaAlocacao",
					Constants.MSG_ERROR_MAPA_ALOCACAO_LOCK_UNLOCK,
					mapa.getLoginTrava());

			return null;
		}
	}

	/**
	 * Realiza a validaçao somente do Recurso.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateRecurso(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;

		if ((login != null) && (!"".equals(login))) {
			Recurso r = recursoService.findRecursoByMnemonico(login);

			if (r == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Realiza a validaçao do Recurso e da Pessoa.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateRecursoPessoa(final FacesContext context,
			final UIComponent component, final Object value) {

		String login = (String) value;

		if ((login != null) && (!"".equals(login))) {
			Recurso r = recursoService.findRecursoByMnemonico(login);

			if (r == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}

			if (alocacaoBean.getIsAddPessoa()) {
				Pessoa pess = pessoaService.findPessoaByRecurso(r);
				if (pess == null) {
					String label = (String) component.getAttributes().get(
							"label");
					throw new ValidatorException(Messages.getMessageError(
							Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
				}
			}
		}
	}

	/**
	 * Ação que realiza o unlick do MapaAlocacao na tela de research.
	 * 
	 */
	public void unlockMapaAlocacaoResearch() {
		this.unlockMapaAlocacao();

		this.findByFilter();
	}

	/**
	 * Ação do botão cancel do MapaAlocacao (manage).
	 * 
	 * @return paginas de search
	 */
	public String cancelManageMapaAlocacao() {
		MapaAlocacao mapa = bean.getTo();

		if (mapaAlocacaoService.unlockMapaAlocacao(mapa)) {
			bean.setIsLocked(Boolean.FALSE);
			Messages.showSucess("unlockMapaAlocacao",
					Constants.MSG_SUCCESS_MAPA_ALOCACAO_UNLOCK);
		} else {
			Messages.showError("unlockMapaAlocacao",
					Constants.MSG_ERROR_MAPA_ALOCACAO_LOCK_UNLOCK,
					mapa.getLoginTrava());

			return null;
		}

		this.findByFilter();

		return OUTCOME_MAPA_ALOCACAO_RESEARCH;
	}

	/**
	 * Ação do botão back do view do MapaAlocacao.
	 * 
	 * @return retorna para a pagina de searsh do mapa alocação.
	 */
	public String backViewMapaAlocacao() {
		this.findByFilter();

		return OUTCOME_MAPA_ALOCACAO_RESEARCH;
	}

	/**
	 * Ação do botão voltar da tela do Financials.
	 * 
	 * @return pagina do manage do mapa
	 */
	public String backToManage() {
		MapaAlocacao mapa = bean.getTo();

		this.setMapPeriodWindow(mapa);

		return OUTCOME_MAPA_ALOCACAO_MANAGE;
	}

	/**
	 * Seta a vigencia do mapa-alocacao.
	 * 
	 * @param mapa
	 *            - mapa para pegar a vigencia.
	 */
	private void setMapPeriodWindow(final MapaAlocacao mapa) {
		// seta a vigencia do mapa de alocação
		this.setMapPeriodo(mapa);

		Date dataInicioJanela = mapa.getDataInicioJanela();

		// seta a data inicial no bean
		bean.setStartDateWindow(dataInicioJanela);

		// pega a data final que deve ser exibido no mapa
		Date dataFinalJanela = mapaAlocacaoService
				.getMapaAlocacaoEndDateWindow(mapa);
		// seta a data final no bean
		bean.setEndDateWindow(dataFinalJanela);

		// cria a lista de datas dado data inicial e final da vigência
		bean.setValidityDateList(DateUtil.getValidityDateList(dataInicioJanela,
				dataFinalJanela));
	}

	/**
	 * Seta a vigencia do mapa de aloação.
	 * 
	 * @param mapa
	 *            - entidade do tipo Mapaalocacao.
	 */
	private void setMapPeriodo(final MapaAlocacao mapa) {
		// seta a data inicio do mapa de alocação
		bean.setValidityMonthBeg(DateUtil.getMonthString(mapa.getDataInicio()));
		bean.setValidityYearBeg(DateUtil.getYearString(mapa.getDataInicio()));

		// seta a data fim do mapa de alocação
		bean.setValidityMonthEnd(DateUtil.getMonthString(mapa.getDataFim()));
		bean.setValidityYearEnd(DateUtil.getYearString(mapa.getDataFim()));
	}

	/**
	 * Atualiza os valores totais da Alocacao.
	 */
	public void updateAlocacaoTotalValues() {
		int cont = 0;
		double totalUr = 0;

		List<AlocacaoRow> alocacaoRowList = bean.getAlocacaoRowList();
		List<BigDecimal> totalValuesList = new ArrayList<BigDecimal>();
		double[] totalValuesArray = new double[bean.getValidityDateList()
				.size()];

		for (AlocacaoRow alocacaoRow : alocacaoRowList) {
			if (!alocacaoRow.getIsRemove() && alocacaoRow.getIsView()) {
				cont++;
				BigDecimal valorUr = alocacaoRow.getAlocacao().getValorUr();
				if (valorUr != null) {
					totalUr += valorUr.doubleValue();
				}

				int i = 0;
				List<AlocacaoCell> alocacaoCellList = alocacaoRow
						.getAlocacaoCellList();

				Pessoa pessoa = null;
				for (AlocacaoCell alocacaoCell : alocacaoCellList) {

					pessoa = pessoaService.findPessoaByRecurso(alocacaoCell
							.getAlocacaoPeriodo().getAlocacao().getRecurso());
					if (pessoa != null) {
						// Busca data de recisao da pessoa
						Date dataRecisao = pessoa.getDataRescisao();

						// Seta flag de indicador ativo para pintar celula de
						// vermelho
						if (dataRecisao != null) {
							String indicadorAtivo = alocacaoCell
									.getAlocacaoPeriodo().getAlocacao()
									.getRecurso().getIndicadorAtivo();
							Date dataAlocacao = alocacaoCell
									.getAlocacaoPeriodo()
									.getDataAlocacaoPeriodo();
							// Pinta celula de vermelho
							if (indicadorAtivo.equals("I")
									&& dataAlocacao.after(dataRecisao)) {
								alocacaoCell
										.setFlagIndicadorAtivo(Boolean.TRUE);
							}
						}
					}

					BigDecimal percentualAlocacaoPeriodo = alocacaoCell
							.getAlocacaoPeriodo()
							.getPercentualAlocacaoPeriodo();
					if (percentualAlocacaoPeriodo != null) {
						totalValuesArray[i] = totalValuesArray[i]
								+ percentualAlocacaoPeriodo.doubleValue();
					}
					i++;
				}
			}
		}

		// se contador (qtde de AlocacaoRow) for maior do que zero, calcula a
		// média do total de U.R.
		if (cont > 0) {
			totalUr /= cont;
		}

		for (double totalValue : totalValuesArray) {
			totalValuesList.add(BigDecimal.valueOf(totalValue));
		}

		bean.resetTotalValues();
		bean.updateTotalValues(totalUr, totalValuesList);
	}

	/**
	 * Gera o Dashboard do mapa-alocacao.
	 * 
	 * @return retorna a pagina de destino.
	 */

	/*
	 * public String generateMapaAlocacaoDashboard() {
	 * 
	 * ContratoPratica clob = contratoPraticaService
	 * .findContratoPraticaById(bean.getTo().getContratoPratica()
	 * .getCodigoContratoPratica());
	 * 
	 * SortedMap<Date, MapDashboardRow> mapDashboard = mapaAlocacaoService
	 * .genereteMapaAlocacaoDashboard(clob, bean.getValidityDateList());
	 * 
	 * bean.setMapDashboard(mapDashboard);
	 * 
	 * bean.setClosingDateRevenue(receitaService.getClosingDateReceita());
	 * 
	 * this.totalizeCurrencyDashboard();
	 * 
	 * return OUTCOME_MAPA_ALOCACAO_DASHBOARD; }
	 */

	/**
	 * Calcula o total por moeda da tela de Dashboard (Financials).
	 * 
	 */

	public void totalizeCurrencyDashboard() {
		Map<String, Double> totals = new HashMap<String, Double>();
		List<String> colunas = bean.getColunasMapDashboard();

		int col = 0;
		for (String coluna : colunas) {
			for (MapDashboardRow row : bean.getMapDashboard().values()) {
				Double valor = row.getMapDashboardMoedaMapInteger().get(col);
				if (totals.containsKey(coluna)) {
					if (valor != null) {
						totals.put(coluna, totals.get(coluna) + valor);
					}
				} else {
					totals.put(coluna, valor);
				}
			}
			col++;
		}
		// bean.setTotalCurrencyFinancialMap(totals);
	}

	/**
	 * Ação que realiza o filtro do dashboard pelo intervalo das datas de inicio
	 * e fim.
	 * 
	 * @return pagina de destino
	 */
	public String filterDateDashboard() {

		MapaAlocacao mapaAlocacao = bean.getTo();

		Date startDate = DateUtil.getDate(bean.getValidityMonthBeg(),
				bean.getValidityYearBeg());

		Date endDate = DateUtil.getDate(bean.getValidityMonthEnd(),
				bean.getValidityYearEnd());

		Date minDateMap = alocacaoPeriodoService
				.findMinDateByMapa(mapaAlocacao);

		Date maxDateMap = alocacaoPeriodoService
				.findMaxDateByMapa(mapaAlocacao);

		if (!DateUtil.isBetween(startDate, minDateMap, maxDateMap,
				Calendar.MONTH)) {
			Messages.showWarning("filterDateDashboard",
					Constants.MAPA_ALOCACAO_DASHBOARD_MSG_ERROR_START_DATE);

			bean.setValidityMonthBeg("" + DateUtil.getMonthNumber(minDateMap));
			bean.setValidityYearBeg("" + DateUtil.getYear(minDateMap));
			return null;

		} else if (!DateUtil.isBetween(endDate, minDateMap, maxDateMap,
				Calendar.MONTH)) {
			Messages.showWarning("filterDateDashboard",
					Constants.MAPA_ALOCACAO_DASHBOARD_MSG_ERROR_END_DATE);

			bean.setValidityMonthEnd("" + DateUtil.getMonthNumber(maxDateMap));
			bean.setValidityYearEnd("" + DateUtil.getYear(maxDateMap));
			return null;

		} else if (!DateUtil.validateValidityDate(bean.getValidityMonthBeg(),
				bean.getValidityYearBeg(), bean.getValidityMonthEnd(),
				bean.getValidityYearEnd(),
				Constants.DEFAULT_LABEL_VALIDITY_DATE_MAPA_ALOCACAO,
				Boolean.valueOf(true), this.getClosingDate())) {
			return null;
		}

		bean.setValidityDateList(DateUtil.getValidityDateList(
				bean.getValidityMonthBeg(), bean.getValidityYearBeg(),
				bean.getValidityMonthEnd(), bean.getValidityYearEnd()));

		// return this.generateMapaAlocacaoDashboard();
		return this.prepareFinancials();

	}

	/**
	 * Realiza o update da AlocacaoPeriodo.
	 */
	public void updateAlocacaoPeriodo() {
		mapaAlocacaoService.updateAlocacaoPeriodo(bean.getAlocacaoPeriodo());
	}

	/**
	 * Realiza o update do FatorUrMes.
	 */
	public void updateFatorUrMes() {
		mapaAlocacaoService.updateFatorUrMes(bean.getFatorUr());
	}

	/**
	 * Atualiza o valor do U.R. de todas as Alocacao selecionadas
	 */
	public void updateIncomeUr() {
		if (bean.getUpdateUr() != null) {
			// verifica se algum item foi selecionado
			if (isSomeAlocationSelected()) {
				mapaAlocacaoService.updateIncomeUr(bean.getAlocacaoRowList(),
						bean.getUpdateUr());

				updateAlocacaoTotalValues();

				bean.setUpdateUr(BigDecimal.valueOf(0));
			} else {
				Messages.showWarning("updateIncomeUr",
						Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
			}
		} else {
			Messages.showWarning("updateIncomeUr",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.LABEL_ALOCACAO_UPDATE_UR);
		}
	}

	/**
	 * Atualiza o valor do FTE. de todas as Alocacao selecionadas
	 */
	public void updateIncomeFte() {
		if (bean.getUpdateFte() != null) {
			// verifica se algum item foi selecionado
			if (isSomeAlocationSelected()) {
				mapaAlocacaoService.updateMatrixIncomeFte(
						bean.getAlocacaoRowList(), bean.getUpdateFte());

				updateAlocacaoTotalValues();

				bean.setUpdateFte(BigDecimal.valueOf(0));
			} else {
				Messages.showWarning("updateIncomeFte",
						Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
			}
		} else {
			Messages.showWarning("updateIncomeFte",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.LABEL_ALOCACAO_UPDATE_FTE);
		}
	}

	/**
	 * Método que realiza a busca pelas alocações em outros mapas de um recuros.
	 */
	public void findAllAlocationsByRecurso() {
		AlocacaoRow alocacaoRow = bean.getAlocacaoRow();

		List<AlocacaoContratoPraticaRow> alocacaoCPRowList = mapaAlocacaoService
				.findMapaAlocacaoAllByRecurso(alocacaoRow.getAlocacao(),
						bean.getValidityDateList());

		alocacaoRow.setAlocacaoCPRowList(alocacaoCPRowList);

		// verificação realizada para exibir o icone de menos('-') no mapa
		// alocação
		alocacaoRow.setExistsAlocation(!alocacaoCPRowList.isEmpty());
		alocacaoRow.setShowAlocations(Boolean.FALSE);
	}

	/**
	 * Método que limpa a lista com as alocações de outros mapas.
	 */
	public void hideAllAlocationsByRecurso() {
		AlocacaoRow alocacaoRow = bean.getAlocacaoRow();

		alocacaoRow.getAlocacaoCPRowList().clear();

		alocacaoRow.setShowAlocations(Boolean.TRUE);
	}

	/**
	 * Verifica se algum item foi selecionado.
	 * 
	 * @return true se algum item selecionado, caso contrario retorna false.
	 */
	private Boolean isSomeAlocationSelected() {
		List<AlocacaoRow> alocacaoRowList = bean.getAlocacaoRowList();
		for (AlocacaoRow alocacaoRow : alocacaoRowList) {
			if (alocacaoRow.getIsSelected()) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * Seta o nome do mapa. Padrão: Map-[ContratoPratica].
	 */
	public void setNewTxtTitulo() {

		String cp = bean.getTo().getContratoPratica().getNomeContratoPratica();
		String acronym = (String) systemProperties
				.get(Constants.MAPA_ALOCACAO_DEFAULT_ACRONYM_NAME);

		if (cp != null) {
			bean.getTo().setTextoTitulo(acronym + cp);
		}
	}

	/**
	 * Adiciona uma Etiqueta na tabela Etiqueta relacionado ao Cliente corrente.
	 * 
	 */
	public void addEtiqueta() {
		// recupera o nome da Etiqueta a ser adicionado
		String nomeEtiquetaAdd = bean.getNomeEtiquetaAdd();
		Cliente cliente = getClienteByMapaAlocacao(bean.getTo());

		Etiqueta etiquetaAdd = etiquetaService.addEtiqueta(nomeEtiquetaAdd,
				cliente);

		// se adidionado com sucesso
		if (etiquetaAdd != null) {
			// mensagem de sucesso
			Messages.showSucess("addEtiqueta",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_ETIQUETA);

			// limpa o campo nome da Etiqueta
			bean.setNomeEtiquetaAdd("");
			// recarrega a lista de Etiqueta do combo
			loadEtiquetaList(etiquetaService.findEtiquetaAtivaByCliente(cliente
					.getCodigoCliente()));
		} else {
			Messages.showError("addEtiqueta",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ENTITY_NAME_ETIQUETA);
		}
	}

	/**
	 * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareCentroLucroCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codNatureza = bean.getNaturezaMap().get(value);
			NaturezaCentroLucro natureza = null;

			// se o centro de lucro existir
			if (codNatureza != null) {
				natureza = naturezaService
						.findNaturezaCentroLucroById(codNatureza);
				this.loadCentroLucroList(centroLucroService
						.findCentroLucroByNatureza(natureza));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadCentroLucroList(new ArrayList<CentroLucro>());
			}
		} else {
			this.loadCentroLucroList(centroLucroService.findCentroLucroAll());
		}
	}

	/**
	 * Popula o combobox do contrato de acordo com o cliente selecionado.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareContratoCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codCli = bean.getClienteMap().get(value);
			Cliente cliente = null;

			// se o centro de lucro existir
			if (codCli != null) {
				cliente = clienteService.findClienteById(codCli);
				this.loadMsaList(msaService.findMsaByCliente(cliente));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadMsaList(new ArrayList<Msa>());
			}
		} else {
			this.loadMsaList(msaService.findMsaAll());
		}
	}

	/**
	 * Popula o combobox do contrato-pratica de acordo com o contrato
	 * selecionado.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareContratoPraticaCombo(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		if (value != null && !"".equals(value)) {
			Long codContrato = bean.getMsaMap().get(value);
			Msa msa = null;

			// se o centro de lucro existir
			if (codContrato != null) {
				msa = msaService.findMsaById(codContrato);
				this.loadContratoPraticaList(contratoPraticaService
						.findContratoPraticaByContrato(msa));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadContratoPraticaList(new ArrayList<ContratoPratica>());
			}
		} else {
			this.loadContratoPraticaList(contratoPraticaService
					.findContratoPraticaAllComplete());
		}
	}

	/**
	 * Filtra somente as Alocacao da Etiqueta corrente.
	 * 
	 */
	public void filterEtiqueta() {
		// recupera o nome da Etiqueta selecionada
		String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
		if (!StringUtils.isEmpty(nomeEtiquetaSelected)) {
			// recupera o código da Etiqueta selecionada
			Long codigoEtiqueta = bean.getEtiquetaMap().get(
					nomeEtiquetaSelected);
			// se existir o código e for maior do que zero (diferente de All)
			if (codigoEtiqueta != null && codigoEtiqueta > 0) {
				etiquetaService.filterEtiquetaForAlocacaoRowList(
						codigoEtiqueta, bean.getTo().getCodigoMapaAlocacao(),
						bean.getAlocacaoRowList());
				// se igual a 'Without' tag
			} else if (codigoEtiqueta == 0) {
				etiquetaService.filterWithoutEtiquetaForAlocacaoRowList(bean
						.getAlocacaoRowList());
			} else {
				mapaAlocacaoService.showFullMatrixAlocacao(bean
						.getAlocacaoRowList());
			}
			updateAlocacaoTotalValues();
		} else {
			Messages.showError("filterEtiqueta",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ENTITY_NAME_ETIQUETA);
		}
	}

	/**
	 * Remove uma Etiqueta na tabela Etiqueta relacionado ao Cliente corrente.
	 * 
	 */
	public void deleteEtiqueta() {
		// recupera o nome da Etiqueta selecionado
		String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
		// recupera o cliente corrente
		Cliente cliente = getClienteByMapaAlocacao(bean.getTo());

		if (etiquetaService.deleteEtiqueta(nomeEtiquetaSelected, cliente)) {
			// mensagem de sucesso
			Messages.showSucess("deleteEtiqueta",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_ETIQUETA);

			// limpa o campo nome da Etiqueta
			bean.setNomeEtiquetaSelected("");
			// recarrega a lista de Etiqueta do combo
			loadEtiquetaList(etiquetaService.findEtiquetaAtivaByCliente(cliente
					.getCodigoCliente()));
		} else {
			Messages.showError("deleteEtiqueta",
					Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
					Constants.ENTITY_NAME_ETIQUETA);
		}
	}

	/**
	 * Aplica uma Etiqueta à Alocacao corrente.
	 * 
	 */
	public void applyEtiqueta() {
		// se pelo menos uma Alocacao estiver selecionada
		if (isSomeAlocationSelected()) {
			// recupera o nome da Etiqueta selecionado
			String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
			// verifica se não é vazio ou nulo
			if (!StringUtils.isEmpty(nomeEtiquetaSelected)) {
				// recupera a Etiqueta selecionada
				Long codigoEtiqueta = bean.getEtiquetaMap().get(
						nomeEtiquetaSelected);
				// se existir o código e for maior do que zero (diferente de
				// All)
				if (codigoEtiqueta != null && codigoEtiqueta > 0) {
					etiquetaService.applyEtiquetaForAlocacaoRowList(
							bean.getAlocacaoRowList(), codigoEtiqueta);
				} else {
					Messages.showError("applyEtiqueta",
							Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
							Constants.ENTITY_NAME_ETIQUETA);
				}
			} else {
				Messages.showError("applyEtiqueta",
						Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
						Constants.ENTITY_NAME_ETIQUETA);
			}
		} else {
			Messages.showWarning("applyEtiqueta",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Desaplica uma Etiqueta à Alocacao corrente.
	 * 
	 */
	public void unapplyEtiqueta() {
		// se pelo menos uma Alocacao estiver selecionada
		if (isSomeAlocationSelected()) {
			// recupera o nome da Etiqueta selecionado
			String nomeEtiquetaSelected = bean.getNomeEtiquetaSelected();
			// verifica se não é vazio ou nulo
			if (!StringUtils.isEmpty(nomeEtiquetaSelected)) {
				// recupera a Etiqueta selecionada
				Long codigoEtiqueta = bean.getEtiquetaMap().get(
						nomeEtiquetaSelected);
				// se existir o código e for maior do que zero (diferente de
				// All)
				if (codigoEtiqueta != null && codigoEtiqueta > 0) {
					etiquetaService.unapplyEtiquetaForAlocacaoRowList(
							bean.getAlocacaoRowList(), codigoEtiqueta);
				} else {
					Messages.showError("unapplyEtiqueta",
							Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
							Constants.ENTITY_NAME_ETIQUETA);
				}
			} else {
				Messages.showError("unapplyEtiqueta",
						Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
						Constants.ENTITY_NAME_ETIQUETA);
			}
		} else {
			Messages.showWarning("unapplyEtiqueta",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Edita a data de validação.
	 */
	public void editValidityAlocacao() {

		Alocacao alocacao = bean.getToAlocacao();
		Alocacao to = alocacaoBean.getTo();

		alocacaoService.updateAlocacaoValidity(alocacao, to.getDataInicio(),
				to.getDataFim());

		messageConntrolBean.setDisplayMessages(Boolean.TRUE);
	}

	/**
	 * Prepara a tela de edição da validação da alocação.
	 */
	public void prepareEditValidityAlocacao() {
		Alocacao alocacao = bean.getToAlocacao();
		Alocacao to = alocacaoBean.getTo();

		to.setDataInicio(alocacao.getDataInicio());
		to.setDataFim(alocacao.getDataFim());
	}

	/**
	 * Ação que realiza a ordenção pela coluna.
	 */
	public void columnSort() {
		// Busca numero da coluna clicada.
		int aux = bean.getAlocacaoRow().getStaticComparableField();

		// Ordena de acordo com a coluna clicada.
		switch (aux) {
		case 1:
			Collections.sort(bean.getAlocacaoRowList());
			break;
		case 2:
			Collections.sort(bean.getAlocacaoRowList(),
					new CidadeBaseAlocacaoComparator());
			break;
		case 3:
			Collections.sort(bean.getAlocacaoRowList(),
					new PerfilVendidoAlocacaoComparator());
			break;
		case 4:
			Collections.sort(bean.getAlocacaoRowList(),
					new IndicadorEstagioAlocacaoComparator());
			break;
		default:
			Collections.sort(bean.getAlocacaoRowList());
			break;
		}
	}

	/**
	 * Popula a lista de Msa para combobox.
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
	 * Popula a lista de centroLucros para combobox.
	 * 
	 * @param centroLucros
	 *            lista de contratos.
	 * 
	 */
	private void loadCentroLucroList(final List<CentroLucro> centroLucros) {

		Map<String, Long> centroLucroMap = new HashMap<String, Long>();
		List<String> centroLucroList = new ArrayList<String>();

		for (CentroLucro centroLucro : centroLucros) {
			centroLucroMap.put(centroLucro.getNomeCentroLucro(),
					centroLucro.getCodigoCentroLucro());
			centroLucroList.add(centroLucro.getNomeCentroLucro());
		}

		bean.setCentroLucroMap(centroLucroMap);
		bean.setCentroLucroList(centroLucroList);
	}

	/**
	 * Popula a lista de Natureza para combobox.
	 * 
	 * @param naturezas
	 *            lista de NaturezaCentroLucro.
	 * 
	 */
	private void loadNaturezaList(final List<NaturezaCentroLucro> naturezas) {

		Map<String, Long> naturezaMap = new HashMap<String, Long>();
		List<String> naturezaList = new ArrayList<String>();

		for (NaturezaCentroLucro natureza : naturezas) {
			naturezaMap.put(natureza.getNomeNatureza(),
					natureza.getCodigoNatureza());
			naturezaList.add(natureza.getNomeNatureza());
		}

		bean.setNaturezaMap(naturezaMap);
		bean.setNaturezaList(naturezaList);
	}

	/**
	 * Popula a lista de clientes para combobox.
	 * 
	 * @param clientes
	 *            lista de clientes.
	 * 
	 */
	private void loadClienteList(final List<Cliente> clientes) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : clientes) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}
		bean.setClienteMap(clienteMap);
		bean.setClienteList(clienteList);
	}

	/**
	 * Recupera o Cliente do MapaAlocacao corrente. t
	 * 
	 * @param mapaAlocacao
	 *            - MapaAlocacao corrente
	 * @return o Cliente do MapaAlocacao corrente
	 */
	private Cliente getClienteByMapaAlocacao(final MapaAlocacao mapaAlocacao) {

		ContratoPratica contratoPratica = contratoPraticaService
				.findContratoPraticaById(mapaAlocacao.getContratoPratica()
						.getCodigoContratoPratica());

		if (contratoPratica != null) {
			Msa msa = contratoPratica.getMsa();
			if (msa != null) {
				Cliente cliente = msa.getCliente();
				if (cliente != null) {
					return clienteService.findClienteById(cliente
							.getCodigoCliente());
				}
			}
		}
		return null;
	}

	/**
	 * Pega a data de fechamento do mapa de alocação.
	 * 
	 * @return retorna a Data de Fechamento.
	 */
	private Date getClosingDate() {
		// pega a data de fechamento do modulo do mapa de alocacao
		Modulo moduloMapaAlocacao = moduloService.findModuloById(new Long(
				systemProperties
						.getProperty(Constants.MODULO_ALOCATION_MAP_CODE)));

		return moduloMapaAlocacao.getDataFechamento();
	}

	/**
	 * Prepara a tela para copiar uma Alocacao.
	 * 
	 */
	public void prepareCopyAll() {
		bean.resetAlocCloneFields();

		if (bean.getIsUpdate()) {
			MapaAlocacao mapa = mapaAlocacaoService.findMapaAlocacaoById(bean
					.getTo().getCodigoMapaAlocacao());

			loadPerfilVendidoListAlocClone(perfilVendidoService
					.findPerfilVendidoByContratoPratica(mapa
							.getContratoPratica()));
			// insercao
		} else {
			loadPerfilVendidoListAlocClone(perfilVendidoService
					.findPerfilVendidoByContratoPratica(contratoPraticaService
							.findContratoPraticaById(bean.getTo()
									.getContratoPratica()
									.getCodigoContratoPratica())));
		}

		messageConntrolBean.setDisplayMessages(Boolean.valueOf(false));
	}

	/**
	 * Ação que realiza a copia das faturas selecionadas.
	 */
	public void copyAllSelected() {
		if (isSomeAlocationSelected()) {
			Long perfilVendidoId = bean.getPerfilVendidoMapAlocClone().get(
					bean.getPerfilVendidoAlocClone().getNomePerfilVendido());

			PerfilVendido perfilVendidoAlocClone = perfilVendidoService
					.findPerfilVendidoById(perfilVendidoId);

			alocacaoService.copyAlocacao(bean.getAlocacaoRowList(),
					perfilVendidoAlocClone, bean.getIndicadorEstagioClone(),
					bean.getStartMonthAlocClone(),
					bean.getStartYearAlocClone(), bean.getValidityMonthEnd(),
					bean.getValidityYearEnd());

			// força a ocultar o Modal do Copy
			bean.setShowModalCopy(false);
			messageConntrolBean.setDisplayMessages(Boolean.valueOf(true));

			MapaAlocacao mapaAlocacao = bean.getTo();
			// pega a lista de alocações do mapa
			List<Alocacao> alocacaoList = alocacaoService
					.findAlocacaoByMapaAlocacaoAndPeriod(mapaAlocacao);

			// cria a matriz
			bean.setAlocacaoRowList(mapaAlocacaoService
					.prepareUpdateMatrixAlocacao(alocacaoList,
							bean.getValidityDateList(), mapaAlocacao));

		} else {
			Messages.showWarning("copyAllSelected",
					Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
		}
	}

	/**
	 * Move a Janela do mapa de alocação.
	 */
	public void moveWindow() {

		mapaAlocacaoService.moveMapaAlocacaoWindow(bean.getTo(),
				bean.getNumberMonthsToMove());

		this.prepareManage();
	}

	/**
	 * Realiza o follow do MapaAlocacao corrente.
	 * 
	 */
	public void followMapaAlocacao() {
		MapaAlocacao mapaAlocacao = mapaAlocacaoService
				.findMapaAlocacaoById(bean.getTo().getCodigoMapaAlocacao());
		Pessoa pessoa = LoginUtil.getLoggedUser();

		mapaAlocPessoaService.followMapaAlocacao(mapaAlocacao, pessoa);

		bean.getMapFollow().put(mapaAlocacao.getCodigoMapaAlocacao(),
				pessoa.getCodigoPessoa());
	}

	/**
	 * Realiza o unfollow do MapaAlocacao corrente.
	 * 
	 */
	public void unfollowMapaAlocacao() {
		MapaAlocacao mapaAlocacao = mapaAlocacaoService
				.findMapaAlocacaoById(bean.getTo().getCodigoMapaAlocacao());
		Pessoa pessoa = LoginUtil.getLoggedUser();

		mapaAlocPessoaService.unfollowMapaAlocacao(mapaAlocacao, pessoa);

		bean.getMapFollow().remove(mapaAlocacao.getCodigoMapaAlocacao());

		findByFilter();
	}

	/**
	 * Atualiza o map MapFollow com a relação Mapa x Follower.
	 */
	private void refreshMapFollow() {
		bean.setMapFollow(mapaAlocPessoaService.getMapFollowByPessoa(LoginUtil
				.getLoggedUser()));
	}

	/**
	 * Verifica se a data de Admissão do Colaborador é depois da Data de Periodo
	 * Alocacao.
	 * 
	 * @return true se data Admissao é depois da data de Alocacao
	 */
	public Boolean getAdmissionDateAfterAllocationDate() {
		// verifica se a Pessoa tem a data de admissão depois do Periodo
		// Alocação
		if (bean.getAlocacaoPeriodo() != null) {
			Pessoa pessoa = pessoaService.findPessoaByRecurso(bean
					.getAlocacaoPeriodo().getAlocacao().getRecurso());
			// Se o recurso for pessoa, verifica se a data de admissao e depois
			// da data de alocacao
			// Se nao for pessoa, permite a edicao
			if (pessoa != null) {
				return DateUtil.before(bean.getAlocacaoPeriodo()
						.getDataAlocacaoPeriodo(), pessoa.getDataAdmissao(),
						Calendar.MONTH);
			} else {
				return Boolean.valueOf(false);
			}
		} else {
			return Boolean.valueOf(false);
		}
	}

	/**
	 * prepare da tela de financials
	 * 
	 * @return
	 */
	public String prepareFinancials() {

		ContratoPratica clob = contratoPraticaService
				.findContratoPraticaById(bean.getTo().getContratoPratica()
						.getCodigoContratoPratica());

		List<MapDashboardRow> dashboardRows = new ArrayList<MapDashboardRow>();
		MapDashboardRow row = null;
		List<VwPmsFinancials> listaFinancials = null;
		VwPmsFinancialsId vw = null;
		List<Moeda> listaMoedas = new ArrayList<Moeda>();
		for (Date dataMes : bean.getValidityDateList()) {
			row = new MapDashboardRow();
			listaFinancials = vwPmsFinancialsService
					.findVwPmsFinancialsByClobAndDataMes(clob, dataMes);

			BigDecimal totalFTE = new BigDecimal(0);
			BigDecimal totalUR = new BigDecimal(0);

			for (VwPmsFinancials vwFinancials : listaFinancials) {
				vw = vwFinancials.getId();

				int ruleRevenue = receitaService.getRevenueCalculationRule(
						dataMes, clob, vw.getMoeda());
				switch (ruleRevenue) {
				case 0:
					totalFTE = totalFTE.add(vw.getTotalFteProjetado());
					totalUR = totalUR.add(vw.getAvgUrProjetado());
					break;

				case 1:
					totalFTE = totalFTE.add(vw.getTotalFteReceitado());
					totalUR = totalUR.add(vw.getUrMedioReceitado());
					break;

				default:
					break;
				}

				boolean jaExiste = false;
				for (Moeda moeda : listaMoedas) {
					if (moeda.getCodigoMoeda() == vw.getMoeda().getCodigoMoeda()) {
						jaExiste = true;
					}
				}
				
				if (!jaExiste) {
					listaMoedas.add(vw.getMoeda());
				}

			}

			if (!totalUR.equals(new BigDecimal(0))) {
				totalUR = totalUR
						.divide(new BigDecimal(listaFinancials.size()));
			}

			row.setFteTotal(totalFTE.doubleValue());
			row.setAverageUtilizationRate(totalUR.doubleValue());
			row.setDataMes(dataMes);
			row.setMapMoeda(this.loadMapMoeda(listaFinancials, clob, dataMes));

			dashboardRows.add(row);

		}
		bean.setListaMoedas(listaMoedas);
		bean.setListResult(dashboardRows);

		bean.setTotalCurrencyFinancialMap(this
				.calculateTotalByCurrency(dashboardRows));

		return OUTCOME_MAPA_ALOCACAO_DASHBOARD;
	}

	/**
	 * carrega maps para cada linha da tela de financials (Moeda, Valor)
	 * 
	 * @param lista
	 * @param clob
	 * @param dataMes
	 * @return
	 */
	public HashMap<Moeda, Double> loadMapMoeda(List<VwPmsFinancials> lista,
			ContratoPratica clob, Date dataMes) {
		HashMap<Moeda, Double> hashMapResult = new HashMap<Moeda, Double>();
		for (VwPmsFinancials vw : lista) {
			int ruleRevenue = receitaService.getRevenueCalculationRule(dataMes,
					clob, vw.getId().getMoeda());
			switch (ruleRevenue) {
			case 0:
				hashMapResult.put(vw.getId().getMoeda(), vw.getId()
						.getTotalReceitaProjetado().doubleValue());
				break;
			case 1:
				hashMapResult.put(vw.getId().getMoeda(), vw.getId()
						.getTotalReceitaReceitado().doubleValue());
				break;
			default:
				break;
			}
		}
		return hashMapResult;
	}

	/**
	 * calcula o total por moeda da tela de financilas
	 * 
	 * @param lista
	 * @return
	 */
	public HashMap<Moeda, Double> calculateTotalByCurrency(
			List<MapDashboardRow> lista) {
		HashMap<Moeda, Double> mapTotais = new HashMap<Moeda, Double>();
		for (MapDashboardRow row : lista) {
			Set<Moeda> moedas = row.getMapMoeda().keySet();
			for (Moeda moeda : moedas) {
				if (!mapTotais.containsKey(moeda)) {
					mapTotais.put(moeda, row.getMapMoeda().get(moeda));
				} else {
					Double total = mapTotais.get(moeda);
					total += row.getMapMoeda().get(moeda);
					mapTotais.put(moeda, total);
				}
			}
		}
		return mapTotais;

	}

}