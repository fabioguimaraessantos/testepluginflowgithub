package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.*;
import com.ciandt.pms.control.jsf.bean.GrupoCustoBean;
import com.ciandt.pms.control.jsf.bean.PessoaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * A classe PessoaController proporciona as funcionalidades da camada de
 * apresenta��o referente a entidade Pessoa.
 *
 * @since 11/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"PRO.RESOURCE:VW", "PRO.RESOURCE:ED", "PER.RESOURCE:VW", "PER.RESOURCE:ED"})
public class PessoaController {

	/** bean do controlller. */
	@Autowired
	private PessoaBean bean;

	/** entidade grupoCustoBean. */
	@Autowired
	private GrupoCustoBean grupoCustoBean;

	/** intancia de PessoaService. */
	@Autowired
	private IPessoaService pessoaService;

	/** intancia de MoedaService. */
	@Autowired
	private IMoedaService moedaService;

	/** intancia de GrupoCustoService. */
	@Autowired
	private IGrupoCustoService grupoCustoService;

	/** intancia de PessoaGrupoCustoService. */
	@Autowired
	private IPessoaGrupoCustoService pessoaGrupoCustoService;

	/** intancia de GrupoCustoService. */
	@Autowired
	private ICidadeBaseService cidadeBaseService;

	/** intancia de PessoaGrupoCustoService. */
	@Autowired
	private IPessoaCidadeBaseService pessoaCidadeBaseService;

	/** intancia de TipoContratoService. */
	@Autowired
	private ITipoContratoService tipoContratoService;

	/** instancia de EmpresaService. */
	@Autowired
	private IEmpresaService empresaService;

	/** intancia de PessoaTipoContratoService. */
	@Autowired
	private IPessoaTipoContratoService pessoaTipoContratoService;

	/** intancia de PerfilSistemaService. */
	@Autowired
	private IPerfilSistemaService perfilSistemaService;

	/** instancia de PessoaPessoaService. */
	@Autowired
	private IPessoaPessoaService pessPessService;

	/** instancia de ModuloService. */
	@Autowired
	private IModuloService moduloService;

	/** instancia de AreaOrcamentariaService. */
	@Autowired
	private IAreaOrcamentariaService areaOrcamentariaService;

	@Autowired
	private IPessoaBillabilityService pessoaBillabilityService;

	@Autowired
	private IBillabilityService billabilityService;

	/*********** OUTCOMES **************************/

	/** outcome tela inclusao da entidade. */
	private static final String OUTCOME_SEARCH = "pessoa_search";
	/** outcome tela alteracao da entidade. */
	private static final String OUTCOME_MANAGE = "pessoa_manage";

	private static final String OUTCOME_MANAGE_VIEW = "pessoa_manage_view";

	/***********************************************/


	/**
	 * @return the bean
	 */
	public PessoaBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final PessoaBean bean) {
		this.bean = bean;
	}

	/**
	 * @param grupoCustoBean
	 *            the grupoCustoBean to set
	 */
	public void setGrupoCustoBean(final GrupoCustoBean grupoCustoBean) {
		this.grupoCustoBean = grupoCustoBean;
	}

	/**
	 * @return the grupoCustoBean
	 */
	public GrupoCustoBean getGrupoCustoBean() {
		return grupoCustoBean;
	}

	/**
	 * Prepara a tela de busca.
	 *
	 * @return retorna a pagina de busca.
	 */
	public String prepareSearch() {
		bean.reset();

		this.loadPessoaList(pessoaService.findAllManager());

		// this.refreshMapFollow();

		return OUTCOME_SEARCH;
	}

	/**
	 * Prepara a tela de update.
	 *
	 * @return retorna a pagina de update
	 */
	public String prepareUpdate() {
		bean.resetExtraFields();

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());

		bean.setIsPeopleManager(Boolean.TRUE);
		bean.setTo(pessoa);

		// verifica a associacao do GrupoCusto, TipoContrato e Salario para
		// exibir os valores corretamente conforme vigencia (data atual)
		Date currentDate = DateUtil.getDate(new Date());
		PessoaGrupoCusto pessoaGC = pessoaGrupoCustoService
				.findPessGCByPessoaAndDate(pessoa, currentDate);
		CidadeBase cidadeBase = cidadeBaseService
				.findCurrentCidadeBaseByPessoa(pessoa);
		PessoaTipoContrato pessoaTC = pessoaTipoContratoService
				.findPessTCByPessoaAndDate(pessoa, currentDate);

		if (pessoaGC != null) {
			bean.setNomeGrupoCusto(pessoaGC.getGrupoCusto().getNomeGrupoCusto());
		}

		if (cidadeBase != null) {
			bean.setNomeCidadeBase(cidadeBase.getNomeCidadeBase());
		}

		if (pessoaTC != null) {
			bean.setNomeTipoContrato(pessoaTC.getTipoContrato()
					.getNomeTipoContrato());
			bean.setPercentualAlocavel(pessoaTC.getPercentualAlocavel());
			bean.setValorJornada(pessoaTC.getValorJornada());
		}

		// guarda a data do HistoryLock
		bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

		this.prepareGrupoCusto();

		return OUTCOME_MANAGE;
	}


	/**
	 * Prepara a tela de update.
	 *
	 * @return retorna a pagina de update
	 */
	public String prepareView() {

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());

		bean.setIsPeopleManager(Boolean.TRUE);
		bean.setTo(pessoa);

		// verifica a associacao do GrupoCusto, TipoContrato e Salario para
		// exibir os valores corretamente conforme vigencia (data atual)
		Date currentDate = DateUtil.getDate(new Date());
		PessoaGrupoCusto pessoaGC = pessoaGrupoCustoService
				.findPessGCByPessoaAndDate(pessoa, currentDate);
		CidadeBase cidadeBase = cidadeBaseService
				.findCurrentCidadeBaseByPessoa(pessoa);
		PessoaTipoContrato pessoaTC = pessoaTipoContratoService
				.findPessTCByPessoaAndDate(pessoa, currentDate);

		if (pessoaGC != null) {
			bean.setNomeGrupoCusto(pessoaGC.getGrupoCusto().getNomeGrupoCusto());
		}

		if (cidadeBase != null) {
			bean.setNomeCidadeBase(cidadeBase.getNomeCidadeBase());
		}

		if (pessoaTC != null) {
			bean.setNomeTipoContrato(pessoaTC.getTipoContrato()
					.getNomeTipoContrato());
			bean.setPercentualAlocavel(pessoaTC.getPercentualAlocavel());
			bean.setValorJornada(pessoaTC.getValorJornada());
		}

		// guarda a data do HistoryLock
		bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

		this.prepareGrupoCusto();

		return OUTCOME_MANAGE_VIEW;
	}


	/**
	 * A��o que realiza o update da pessoa.
	 */
	public void updatePessoa() {
		Pessoa pessoa = bean.getTo();

		pessoaService.updatePessoa(pessoa);

		Messages.showSucess("updatePessoa",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_PESSOA);
	}

	/**
	 * Volta para a pagina de busca, e realiza a busca novamento.
	 *
	 * @return retorna a p�gina de busca.
	 */
	public String backToResearch() {
		this.findByFilter();

		return OUTCOME_SEARCH;
	}

	/**
	 * Busca pelo Login do usuario.
	 */
	public void findByFilter() {
		Boolean isActiveOnlye = bean.getIsActiveOnly();
		if (isActiveOnlye == null) {
			isActiveOnlye = Boolean.valueOf(false);
		}

		/*
		 * Boolean isFollowingOnly = bean.getIsFollowingOnly(); if
		 * (isFollowingOnly == null) { isFollowingOnly = Boolean.valueOf(false);
		 * }
		 */

		bean.setResultList(pessoaService.findPessoaByFilter(bean.getFilter(),
				isActiveOnlye, /* isFollowingOnly */Boolean.valueOf(false)));

		if (bean.getResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}
	}

	/**
	 * Prepara a tela de associacao de grupo custo.
	 */
	public void prepareGrupoCusto() {
		bean.resetGrupoCustoForm();

		this.loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());

		if (pessoa != null && !pessoa.getPessoaGrupoCustos().isEmpty()) {
			Date historyLock = moduloService.getClosingDateHistoryLock();
			for (PessoaGrupoCusto p : pessoa.getPessoaGrupoCustos()) {
				p.setRemovable(p.getDataInicio().after(historyLock));
			}
		}

		bean.setTo(pessoa);
	}
	public void updatePessoaGrupoCusto() {
		List<PessoaGrupoCusto> list = bean.getTo().getPessoaGrupoCustos();
		PessoaGrupoCusto pgc = list.get(list.size() - 1);
		Boolean success = pessoaGrupoCustoService.updatePessoaGrupoCusto(pgc);
		if (success) {
			Messages.showSucess("createPessoaGrupoCusto",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
		}
	}

	/**
	 * Cria uma entidade PessoaGrupoCusto.
	 */
	public void createPessoaGrupoCusto() {
		Date startDate = DateUtil.getDate(bean.getStartMonth(),
				bean.getStartYear());

		// verifica o History Lock. Se startDate n�o for v�lido, d� mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
			return;
		}

		GrupoCusto grupoCusto = bean.getGrupoCusto();

		GrupoCusto gc = grupoCustoService.findGrupoCustoById(bean
				.getGrupoCustoMap().get(grupoCusto.getNomeGrupoCusto()));

		PessoaGrupoCusto pgc = bean.getPessoaGrupoCusto();
		pgc.setDataInicio(startDate);
		pgc.setPessoa(bean.getTo());
		pgc.setGrupoCusto(gc);

		boolean success = pessoaGrupoCustoService.createPessoaGrupoCusto(pgc);

		if (success) {
			Messages.showSucess("createPessoaGrupoCusto",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_GRUPO_CUSTO);

			this.prepareGrupoCusto();
		}

	}

	/**
	 * Remova a associação da pessoa com o GrupoCusto.
	 */
	public void removePessoaGrupoCusto() {
		PessoaGrupoCusto toDelete = bean.getPessoaGrupoCusto();

		String ticketId = bean.getDeletePessoaGrupoCustoTicketId().trim();
		if (ticketId.isEmpty()) {
			Messages.showWarning("removePessoaGrupoCusto",
					Constants.REMOVE_PESSOA_GRUPO_CUSTO_TICKET_REQUIRED_ERROR);
			return;
		}

		// check and show message if date before/equal history lock
		if (!moduloService.verifyHistoryLock(toDelete.getDataInicio(),
				true, false)) {
			return;
		}

		if (pessoaGrupoCustoService.removePessoaGrupoCusto(toDelete, ticketId)) {
			Messages.showSucess("removePessoaGrupoCusto",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_GRUPO_CUSTO);
		}

		this.prepareGrupoCusto();
	}

	/**
	 * Prepara a tela de associacao de grupo custo.
	 */
	public void prepareCidadeBase() {
		bean.resetCidadeBaseForm();

		this.loadCidadeBaseList(cidadeBaseService.findCidadeBaseAllActive());

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo().getCodigoPessoa());

		List<PessoaCidadeBase> listCidadeBase = Lists.newArrayList(pessoa.getPessoaCidadeBases());
		bean.setPessoaCidadeBases(listCidadeBase);
	}

	/**
	 * Cria uma entidade PessoaCidadeBase.
	 */
	public void createPessoaCidadeBase() {
		Date startDate = DateUtil.getDate(bean.getStartMonth(),
				bean.getStartYear());

		// verifica o History Lock. Se startDate n�o for v�lido, d� mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
			return;
		}

		CidadeBase cidadeBase = bean.getCidadeBase();

		CidadeBase cb = cidadeBaseService.findCidadeBaseById(bean
				.getCidadeBaseMap().get(cidadeBase.getNomeCidadeBase()));

		PessoaCidadeBase pessoaCidadeBase = bean.getPessoaCidadeBase();
		pessoaCidadeBase.setDataInicio(startDate);
		pessoaCidadeBase.setPessoa(bean.getTo());
		pessoaCidadeBase.setCidadeBase(cb);

		boolean success = pessoaCidadeBaseService
				.createPessoaCidadeBase(pessoaCidadeBase);

		if (success) {
			Messages.showSucess("createPessoaCidadeBase",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_PESSOA_CIDADE_BASE);

			this.prepareCidadeBase();
		}

	}

	/**
	 * Remova a associa��o da pessoa com o CidadeBase.
	 */
	public void removePessoaCidadeBase() {
		boolean success = pessoaCidadeBaseService.removePessoaCidadeBase(bean
				.getPessoaCidadeBase());

		if (success) {
			Messages.showSucess("removePessoaCidadeBase",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_PESSOA_CIDADE_BASE);
		}

		this.prepareCidadeBase();
	}

	/**
	 * Prepara a tela de associacao de TipoContrato.
	 */
	public void prepareTipoContrato() {
		bean.resetTipoContratoForm();

		this.loadEmpresaList(empresaService.findEmpresaAllParentCompany());
		this.loadTipoContratoList(tipoContratoService
				.findTipoContratoAllActive());

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());

		bean.setTo(pessoa);
	}

	/**
	 * Cria uma entidade PessoaTipoContrato.
	 */
	public void createPessoaTipoContrato() {
		Date startDate = DateUtil.getDate(bean.getStartMonth(),
				bean.getStartYear());

		// verifica o History Lock. Se startDate n�o for v�lido, d� mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
			return;
		}

		Empresa empresa = bean.getEmpresa();
		Empresa emp = empresaService.findEmpresaById(bean.getEmpresaMap().get(
				empresa.getNomeEmpresa()));

		TipoContrato tipoContrato = bean.getTipoContrato();
		TipoContrato tc = tipoContratoService.findTipoContratoById(bean
				.getTipoContratoMap().get(tipoContrato.getNomeTipoContrato()));

		PessoaTipoContrato ptc = bean.getPessoaTipoContrato();
		ptc.setDataInicio(startDate);
		ptc.setPessoa(bean.getTo());
		ptc.setTipoContrato(tc);
		ptc.setEmpresa(emp);

		boolean success = pessoaTipoContratoService
				.createPessoaTipoContrato(ptc);

		if (success) {
			Messages.showSucess("createPessoaTipoContrato",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_TIPO_CONTRATO);

			this.prepareTipoContrato();
		}

	}

	/**
	 * Remova a associa��o da pessoa com o TipoContrato.
	 */
	public void removePessoaTipoContrato() {
		boolean success = pessoaTipoContratoService
				.removePessoaTipoContrato(bean.getPessoaTipoContrato());

		if (success) {
			Messages.showSucess("removePessoaTipoContrato",
					Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_TIPO_CONTRATO);
		}

		this.prepareTipoContrato();
	}

	public void prepareRemovePessoaBillability() {
		bean.setPessoaBillability(bean.getPessoaBillability());
	}
	/**
	 * Remova a associacao da pessoa com a Billability.
	 */
	public void removePessoaBillability() {

		PessoaBillability toRemove = pessoaBillabilityService.findPessoaBillabilityById(bean.getToPessoaBillabilty().getPessoa().getCodigoPessoa(),
				bean.getToPessoaBillabilty().getBillability().getCodigoBillability(),
				bean.getToPessoaBillabilty().getDataInicio());

		pessoaBillabilityService.removePessoaBillability(toRemove);

		Messages.showSucess("removePessoaBillability",
				Constants.DEFAULT_MSG_SUCCESS_REMOVE,
				Constants.ENTITY_NAME_BILLABILITY);


		this.preparePessoaBillability();
	}

	/**
	 * Adiciona a associacao da pessoa com a Billability.
	 */
	public void addPessoaBillability() {
		Date startDate = DateUtil.getDate(bean.getStartMonth(), bean.getStartYear());
		// verifica o History Lock. Se startDate nao for valido, d� mensagem de erro
		if (!moduloService.verifyHistoryLock(startDate, true, false)) {
			return;
		}

		Billability billability = billabilityService.findByName(bean.getNewBillabilitySelected());

		bean.getPessoaBillability().setPessoa(bean.getTo());
		bean.getPessoaBillability().setDataInicio(startDate);
		bean.getPessoaBillability().setBillability(billability);

		boolean isValid = pessoaBillabilityService.isValidToCreate(bean.getPessoaBillability());

		if (isValid) {
			pessoaBillabilityService.createPessoaBillability(bean.getPessoaBillability());

			Messages.showSucess("addPessoaBillability",
					Constants.DEFAULT_MSG_SUCCESS_CREATE,
					Constants.ENTITY_NAME_BILLABILITY);
		}

		this.preparePessoaBillability();
	}

	public void updatePessoaBillability() {

		PessoaBillability toRemove = pessoaBillabilityService.findPessoaBillabilityById(
				bean.getPessoaBillabilityList().get(0).getPessoa().getCodigoPessoa(),
				bean.getPessoaBillabilityList().get(0).getBillability().getCodigoBillability(),
				bean.getPessoaBillabilityList().get(0).getDataInicio());

		pessoaBillabilityService.removePessoaBillability(toRemove);

		Billability billability = billabilityService.findByName(bean.getBillabilitySelected());

		bean.getPessoaBillability().setPessoa(bean.getPessoaBillabilityList().get(0).getPessoa());
		bean.getPessoaBillability().setDataInicio(bean.getPessoaBillabilityList().get(0).getDataInicio());
		bean.getPessoaBillability().setBillability(billability);

		pessoaBillabilityService.addPessoaBillability(bean.getPessoaBillability());

		Messages.showSucess("updatePessoaBillability",
				Constants.DEFAULT_MSG_SUCCESS_UPDATE,
				Constants.ENTITY_NAME_BILLABILITY);

		this.preparePessoaBillability();
	}

	/**
	 * Prepara a tela de associacao de TipoContrato.
	 */
	public void preparePessoaBillability() {
		bean.resetPessoaBillabityForm();

		bean.setPessoaBillabilityList(pessoaBillabilityService.findByPessoa(bean.getTo()));

		if (bean.getPessoaBillabilityList().size() == 1) {
			bean.setBillabilitySelected(bean.getPessoaBillabilityList().get(0).getBillability().getNomeBillability());
		}

		Collections.sort(bean.getPessoaBillabilityList(), new Comparator<PessoaBillability>() {
			@Override
			public int compare(PessoaBillability pb1, PessoaBillability pb2) {
				return pb1.getDataInicio().compareTo(pb2.getDataInicio());
			}
		});

		bean.loadBillabilityActionsConfiguration(moduloService.getClosingDateHistoryLock());
	}

	/**
	 * Popula a lista de GrupoCusto para combobox de Grupo de Custo.
	 *
	 * @param grupoCustos
	 *            lista de GrupoCusto.
	 *
	 */
	private void loadGrupoCustoList(final List<GrupoCusto> grupoCustos) {

		Map<String, Long> grupoCustoMap = new HashMap<String, Long>();
		List<String> grupoCustoList = new ArrayList<String>();

		for (GrupoCusto gc : grupoCustos) {
			grupoCustoMap.put(gc.getNomeGrupoCusto(), gc.getCodigoGrupoCusto());
			grupoCustoList.add(gc.getNomeGrupoCusto());
		}

		bean.setGrupoCustoMap(grupoCustoMap);
		bean.setGrupoCustoList(grupoCustoList);
	}

	/**
	 * Verifica se o valor do atributo GrupoCusto � valido. Se o valor n�o �
	 * nulo e existe no grupoCustoMap, ent�o o valor � valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateGrupoCusto(final FacesContext context,
								   final UIComponent component, final Object value) {

		Long codGrupocusto = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codGrupocusto = bean.getGrupoCustoMap().get(strValue);
			if (codGrupocusto == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de CidadeBase para combobox de Cidade Base.
	 *
	 * @param cidadeBases
	 *            lista de CidadeBase.
	 *
	 */
	private void loadCidadeBaseList(final List<CidadeBase> cidadeBases) {

		Map<String, Long> cidadeBaseMap = new HashMap<String, Long>();
		List<String> cidadeBaseList = new ArrayList<String>();

		for (CidadeBase cb : cidadeBases) {
			cidadeBaseMap.put(cb.getNomeCidadeBase(), cb.getCodigoCidadeBase());
			cidadeBaseList.add(cb.getNomeCidadeBase());
		}

		bean.setCidadeBaseMap(cidadeBaseMap);
		bean.setCidadeBaseList(cidadeBaseList);
	}

	/**
	 * Verifica se o valor do atributo CidadeBase � valido. Se o valor n�o �
	 * nulo e existe no cidadeBaseMap, ent�o o valor � valido.
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

		Long codCidadeBase = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codCidadeBase = bean.getCidadeBaseMap().get(strValue);
			if (codCidadeBase == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de TipoContrato para o combobox.
	 *
	 * @param tipoContratos
	 *            lista de TipoContrato.
	 *
	 */
	private void loadTipoContratoList(final List<TipoContrato> tipoContratos) {

		Map<String, Long> tipoContratoMap = new HashMap<String, Long>();
		List<String> tipoContratoList = new ArrayList<String>();

		for (TipoContrato tc : tipoContratos) {
			tipoContratoMap.put(tc.getNomeTipoContrato(),
					tc.getCodigoTipoContrato());
			tipoContratoList.add(tc.getNomeTipoContrato());
		}

		bean.setTipoContratoMap(tipoContratoMap);
		bean.setTipoContratoList(tipoContratoList);
	}

	/**
	 * Verifica se o valor do atributo TipoContrato � valido. Se o valor n�o �
	 * nulo e existe no tipoContratoMap, ent�o o valor � valido.
	 *
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateTipoContrato(final FacesContext context,
									 final UIComponent component, final Object value) {

		Long codTipoContrato = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codTipoContrato = bean.getTipoContratoMap().get(strValue);
			if (codTipoContrato == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Empresa para o combobox.
	 *
	 * @param empresas
	 *            lista de Empresa.
	 *
	 */
	private void loadEmpresaList(final List<Empresa> empresas) {

		Map<String, Long> empresaMap = new HashMap<String, Long>();
		List<String> empresaList = new ArrayList<String>();

		for (Empresa emp : empresas) {
			empresaMap.put(emp.getNomeEmpresa(), emp.getCodigoEmpresa());
			empresaList.add(emp.getNomeEmpresa());
		}

		bean.setEmpresaMap(empresaMap);
		bean.setEmpresaList(empresaList);
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

		Long codEmpresa = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codEmpresa = bean.getEmpresaMap().get(strValue);
			if (codEmpresa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Popula a lista de Pessoa para combobox de base.
	 *
	 * @param pessoas
	 *            lista de Pessoa.
	 *
	 */
	private void loadPessoaList(final List<Pessoa> pessoas) {
		Map<String, Long> pessoaMap = new HashMap<String, Long>();
		List<String> pessoaList = new ArrayList<String>();

		for (Pessoa pessoa : pessoas) {
			pessoaMap.put(pessoa.getCodigoLogin(), pessoa.getCodigoPessoa());
			pessoaList.add(pessoa.getCodigoLogin());
		}

		bean.setPessoaMap(pessoaMap);
		bean.setPessoaList(pessoaList);
	}

	/**
	 * Popula a lista de Moeda para o combobox.
	 *
	 * @param moedas
	 *            lista de Moeda.
	 *
	 */
	private void loadMoedaList(final List<Moeda> moedas) {

		Map<String, Long> moedaMap = new HashMap<String, Long>();
		List<String> moedaList = new ArrayList<String>();

		for (Moeda moeda : moedas) {
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

		Long codMoeda = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codMoeda = bean.getMoedaMap().get(strValue);
			if (codMoeda == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Atualiza o valor do PercentuAlocavel de acordo com o valor padrao
	 * cadastrado no TipoContrato.
	 *
	 * @param e
	 *            - evento de mudan�a
	 */
	public void refreshPercAlocField(final ValueChangeEvent e) {
		String value = (String) e.getNewValue();

		PessoaTipoContrato pessoaTipoContrato = bean.getPessoaTipoContrato();

		if (value != null && !"".equals(value)) {
			Long codTC = bean.getTipoContratoMap().get(value);
			TipoContrato tipoContrato = null;

			// se o TipoContrato existir
			if (codTC != null) {
				tipoContrato = tipoContratoService.findTipoContratoById(codTC);
				pessoaTipoContrato.setPercentualAlocavel(tipoContrato
						.getPercentualAlocavelPadrao());
				pessoaTipoContrato.setValorJornada(tipoContrato
						.getValorJornadaPadrao());
				// senao existir atribui zero
			} else {
				pessoaTipoContrato.setPercentualAlocavel(BigDecimal.valueOf(0));
				pessoaTipoContrato.setValorJornada(null);
			}
		} else {
			pessoaTipoContrato.setPercentualAlocavel(BigDecimal.valueOf(0));
			pessoaTipoContrato.setValorJornada(null);
		}
	}

	/**
	 * Prepara a tela de gerenciamento de Perfis do sistema.
	 */
	public void prepareAuthority() {

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());

		// recupera todos os PerfilSistema
		List<PerfilSistema> perfilSistemaList = perfilSistemaService
				.findPerfilSistemaAll();

		List<SelectItem> itens = new ArrayList<SelectItem>();

		for (PerfilSistema perfilSistema : perfilSistemaList) {
			itens.add(new SelectItem(""
					+ perfilSistema.getCodigoPerfilSistema(), perfilSistema
					.getNomePerfilSistema()));
		}
		bean.setAuthorityList(itens);

		List<PerfilSistema> list = new ArrayList<PerfilSistema>(
				pessoa.getPerfilSistemas());

		String[] selecionados = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			selecionados[i] = String.valueOf(list.get(i)
					.getCodigoPerfilSistema());
		}

		bean.setGrantedAuthorities(selecionados);
	}


	/**
	 * Prepara a tela de gerenciamento de Area orcamentaria do usuario.
	 */
	public void prepareAreaOrcamentaria() {
		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());

		// recupera todos os PerfilSistema
		List<AreaOrcamentaria> areasOrcamentarias = areaOrcamentariaService.findAllAreaOrcamentariaFilho();

		List<SelectItem> itens = new ArrayList<SelectItem>();

		for (AreaOrcamentaria areaOrcamentaria : areasOrcamentarias) {
			itens.add(new SelectItem(""
					+ areaOrcamentaria.getCodigoAreaOrcamentaria(), areaOrcamentaria
					.getNomeAreaOrcamentaria()));
		}
		bean.setAreasOrcamentarias(itens);

		List<AreaOrcamentaria> list = new ArrayList<AreaOrcamentaria>(
				pessoa.getAreasOrcamentarias());


		String[] selecionados = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			selecionados[i] = String.valueOf(list.get(i)
					.getCodigoAreaOrcamentaria());
		}

		bean.setAreasOrcamentariasAtribuidas(selecionados);
	}

	public void prepareEmpresaOrcamentaria() {

		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo().getCodigoPessoa());

		List<SelectItem> itens = new ArrayList<SelectItem>();
		List<Empresa> empresaList = empresaService.findEmpresaAllParentCompany();
		for (Empresa empresa : empresaList) {
			itens.add(new SelectItem("" + empresa.getCodigoEmpresa(), empresa.getNomeEmpresa()));
		}
		bean.setEmpresasOrcamentarias(itens);

		List<Empresa> list = new ArrayList<Empresa>(pessoa.getEmpresasOrcamentarias());
		String[] selecionados = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			selecionados[i] = String.valueOf(list.get(i).getCodigoEmpresa());
		}
		bean.setEmpresasOrcamentariasAtribuidas(selecionados);
	}

	/**
	 * Realiza o update das permiss�es de uma pessoa.
	 */
	public void updateAuthority() {

		String[] grantedAuthorities = bean.getGrantedAuthorities();
		Set<PerfilSistema> newPerfilList = new HashSet<PerfilSistema>();

		// cria a nova lista de permiss�es
		for (String codPerfilSistema : grantedAuthorities) {
			newPerfilList.add(perfilSistemaService.findPefilSistemaById(Long
					.valueOf(codPerfilSistema)));
		}

		if (pessoaService.updatePessoaAuthority(bean.getTo(), newPerfilList)) {
			// exibe mensagem de sucesso
			Messages.showSucess("updateAuthority",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_PERFIL_SISTEMA);
		}
	}

	/**
	 * Realiza o update das permiss�es de uma pessoa.
	 */
	public void updatePessoaAreaOrcamentaria() {
		String[] areasOrcamentariasAtribuidas = bean.getAreasOrcamentariasAtribuidas();
		Set<AreaOrcamentaria> newAreasOrcamentarias = new HashSet<AreaOrcamentaria>();

		for (String codAreaOrcamentaria : areasOrcamentariasAtribuidas) {
			newAreasOrcamentarias.add(areaOrcamentariaService.findById(Long
					.valueOf(codAreaOrcamentaria)));
		}

		if (pessoaService.updatePessoaAreaOrcamentaria(bean.getTo(), newAreasOrcamentarias)) {
			// exibe mensagem de sucesso
			Messages.showSucess("updateAuthority",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_AREA_ORCAMENTARIA);
		}
	}

	public void updatePessoaEmpresaOrcamentaria() {

		String[] empresasOrcamentariasAtribuidas = bean.getEmpresasOrcamentariasAtribuidas();
		Set<Empresa> newEmpresasOrcamentarias = new HashSet<Empresa>();

		for (String codeEmpresaOrcamentaria : empresasOrcamentariasAtribuidas) {
			newEmpresasOrcamentarias.add(empresaService.findEmpresaById(Long.valueOf(codeEmpresaOrcamentaria)));
		}

		if (pessoaService.updatePessoaEmpresaOrcamentaria(bean.getTo(), newEmpresasOrcamentarias)) {
			// exibe mensagem de sucesso
			Messages.showSucess("updateAuthority",
					Constants.DEFAULT_MSG_SUCCESS_UPDATE,
					Constants.ENTITY_NAME_EMPRESA_ORCAMENTARIA);
		}
	}

	/**
	 * Realiza o follow da Pessoa corrente.
	 *
	 */
	public void followPessoa() {
		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());
		Pessoa pessoaFlwer = LoginUtil.getLoggedUser();

		pessPessService.followPessoa(pessoa, pessoaFlwer);

		bean.getMapFollow().put(pessoa.getCodigoPessoa(),
				pessoaFlwer.getCodigoPessoa());
	}

	/**
	 * Realiza o unfollow da Pessoa corrente.
	 *
	 */
	public void unfollowPessoa() {
		Pessoa pessoa = pessoaService.findPessoaById(bean.getTo()
				.getCodigoPessoa());
		Pessoa pessoaFlwer = LoginUtil.getLoggedUser();

		pessPessService.unfollowPessoa(pessoa, pessoaFlwer);

		bean.getMapFollow().remove(pessoa.getCodigoPessoa());

		findByFilter();
	}
}
