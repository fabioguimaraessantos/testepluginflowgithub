package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAliquotaService;
import com.ciandt.pms.business.service.IAlocacaoService;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IContratoPraticaCentroLucroService;
import com.ciandt.pms.business.service.IContratoPraticaGrupoCustoService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IForecastRevenueStatusProjectService;
import com.ciandt.pms.business.service.IForecastRevenueStatusService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IImpostoService;
import com.ciandt.pms.business.service.IMapaAlocacaoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.business.service.IPerfilSistemaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPraticaService;
import com.ciandt.pms.business.service.ITecnologiaContratoPraticaService;
import com.ciandt.pms.business.service.ITipoModeloNegocioService;
import com.ciandt.pms.business.service.impl.ContratoPraticaGrupoCustoService;
import com.ciandt.pms.business.service.impl.GrupoCustoService;
import com.ciandt.pms.control.jsf.bean.AliquotaBean;
import com.ciandt.pms.control.jsf.bean.ContratoPraticaBean;
import com.ciandt.pms.control.jsf.bean.ContratoPraticaCentroLucroBean;
import com.ciandt.pms.control.jsf.bean.ContratoPraticaGrupoCustoBean;
import com.ciandt.pms.control.jsf.bean.ImpostoBean;
import com.ciandt.pms.control.jsf.bean.MessageControlBean;
import com.ciandt.pms.control.jsf.bean.PraticaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.control.jsf.validation.DealFiscalSameParametersValidation;
import com.ciandt.pms.control.jsf.validation.DealFiscalValidation;
import com.ciandt.pms.control.jsf.validation.FiscalDealIntercompanyAllocationsValidation;
import com.ciandt.pms.control.jsf.validation.MultiDealFiscalComAlocacaoVigenteValidation;
import com.ciandt.pms.control.jsf.validation.MultiFiscalDealIntercompanyValidation;
import com.ciandt.pms.control.jsf.validation.MultiFiscalDealSameCompanyValidation;
import com.ciandt.pms.control.jsf.validation.SelecaoDealFiscalValidation;
import com.ciandt.pms.enums.ContratoPraticaStatus;
import com.ciandt.pms.enums.ForecastRevenueStatusEnum;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.integration.queue.ProjectProducer;
import com.ciandt.pms.integration.queue.RecalculateMapProducer;
import com.ciandt.pms.message.dto.RecalculateMessageDTO;
import com.ciandt.pms.model.Aliquota;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaAud;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.ContratoPraticaGrupoCusto;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.CpraticaDfiscalId;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.ForecastRevenueStatus;
import com.ciandt.pms.model.ForecastRevenueStatusProject;
import com.ciandt.pms.model.ForecastRevenueStatusProjectId;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Imposto;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.model.TecnologiaContratoPratica;
import com.ciandt.pms.model.TipoModeloNegocio;
import com.ciandt.pms.model.vo.NaturezaContratoPraticaCLRow;
import com.ciandt.pms.model.vo.combo.ComboBox;
import com.ciandt.pms.model.vo.combo.type.AtivoInativoCombo;
import com.ciandt.pms.model.vo.combo.type.CentroLucroCombo;
import com.ciandt.pms.model.vo.combo.type.ComboFactory;
import com.ciandt.pms.model.vo.combo.type.GrupoCustoCombo;
import com.ciandt.pms.model.vo.combo.type.PessoaCombo;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * Define o Controller da entidade.
 *
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * @since 26/08/2009
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BUS.CONTRACT_LOB:VW", "BUS.CONTRACT_LOB:ED", "BUS.CONTRACT_LOB:DE", "BUS.CONTRACT_LOB:CR"})
public class ContratoPraticaController {

    /**
     * id do component que exibira a mensagem do faces context
     */
    private static final String REMOVE_CONTRATO_PRATICA = "removeContratoPratica";
    /**
     * id do component que exibira a mensagem do faces context
     */
    private static final String CONTRATO_PRATICA_NEXT = "contratoPraticaNext";

    private static final String INACTIVE_FISCAL_DEAL_ASSOCIATION_MSG = "_nls.contrato_pratica.msg.error.fiscal_deal.inactive";

    private static final String CANNOT_REMOVE_COST_CENTER = "_nls.contrato_pratica.cost_center_remove_error";
    private static final String LOB_CONTRACT_PRATICA_NOT_MATCH_PROFIT_CENTER = "_nls.contrato_pratica.msg.error.profit_center.not_match";

    private static final String NOT_FOUND_CURRENT_PROFIT_CENTER_LOB  = "_nls.contrato_pratica.msg.error.profit_center_lob.not_found";
    private static final String LABEL = "label";

    private static final String CREATE_TABELA_PRECO = "createTabelaPreco";

    private static final String CREATE_PROFIT_CENTER = "createProftCenter";
    private static final String LOB_PRATICA = "findLob";

    private static final String LOB_PRATICA_NOT_FOUND = "_nls.contrato_pratica.msg.error.lob.not.found";
    private static final String UPDATE_CONTRATO_PRATICA_DEAL_FISCAL = "updateContratoPraticaDealFiscal";

    private static final String CREATE_PROFIT_CENTER_VALIDATION = "_nls.contrato_pratica.msg.error.create.profit_center.valid.type";

    private static final String DATE_FORMAT = "MM/yyyy";

    private static final Long NATUREZA_LOB = 61L;

    private static final Long NATUREZA_BM = 3L;

    private static final Long NATUREZA_UKMT = 1L;

    private static final Long NATUREZA_SSO = 2L;

    private static final String INVALID_FISCAL_DEALS_ASSOCIATION_MSG = "_nls.contrato_pratica.msg.error.fiscal_deal.selected";

    private static final String INVALID_STATUS_EXPAND_WITH_WORK_AT_RISK = "_nls.contrato_pratica.msg.error.invalid_status_expand_with_work_at_risk";

    private static final String INVALID_STATUS_CHANGE_TO_EXPAND = "_nls.contrato_pratica.msg.error.invalid_status_change_to_expand";

    /**
     * Instancia do Logger.
     */
    private static Logger log = LogManager.getLogger(ContratoPraticaController.class);

    /**
     * Instancia do servico da entidade ContratoPratica.
     */
    @Autowired
    private IContratoPraticaService contratoPraticaService;
    /**
     * Instancia do servico da entidade ConvergenciaService.
     */
    @Autowired
    private IConvergenciaService convergenciaService;

    /**
     * Instancia do servico da entidade Pratica.
     */
    @Autowired
    private IPraticaService praticaService;

    /**
     * Instancia do servico da entidade Moeda.
     */
    @Autowired
    private IMoedaService moedaService;

    /**
     * Instancia do servico da entidade Contrato.
     */
    @Autowired
    private IMsaService msaService;

    /**
     * Instancia do servico da entidade TabelaPreco.
     */
    @Autowired
    private IContratoPraticaCentroLucroService cpclService;

    /**
     * Instancia do servico da entidade CentroLucro.
     */
    @Autowired
    private ICentroLucroService centroLucroService;

    /**
     * Instancia do servico da entidade NaturezaCentroLucro.
     */
    @Autowired
    private INaturezaCentroLucroService naturezaCentroLucroService;

    /**
     * Instancia do servico da entidade Imposto.
     */
    @Autowired
    private IImpostoService impostoService;

    /**
     * Instancia do servico da entidade Aliquota.
     */
    @Autowired
    private IAliquotaService aliquotaService;

    /**
     * Instancia do servico da entidade Aliquota.
     */
    @Autowired
    private IClienteService clienteService;

    /**
     * Instancia do servico Pessoa.
     */
    @Autowired
    private IPessoaService pessoaService;

    @Autowired
    private IAlocacaoService alocacaoService;

    @Autowired
    private IMapaAlocacaoService mapaAlocacaoService;

    @Autowired
    private ICidadeBaseService cidadeBaseService;

    @Autowired
    private IEmpresaService empresaService;

    /**
     * Instancia do servico Modulo.
     */
    @Autowired
    private IModuloService moduloService;

    /**
     * Instancia do servico DealFiscal.
     */
    @Autowired
    private IDealFiscalService dealFiscalService;

    /**
     * Instancia de perfilSistemaService.
     */
    @Autowired
    private IPerfilSistemaService perfilSistemaService;

    /**
     * Instancia do servico de {@link TecnologiaContratoPratica}..
     */
    @Autowired
    private ITecnologiaContratoPraticaService tecnologiaContratoPraticaService;

    /**
     * Instancia do servico de {@link ContratoPraticaGrupoCustoService}
     */
    @Autowired
    private IContratoPraticaGrupoCustoService contratoPraticaGrupoCustoService;

    /**
     * Instancia do servico da entidade Pratica.
     */
    @Autowired
    private ITipoModeloNegocioService tipoModeloNegocioService;

    /**
     * Instancia do bean.
     */
    @Autowired
    private ContratoPraticaBean bean = null;

    /**
     * Instancia do bean ContratoPraticaCentroLucro.
     */
    @Autowired
    private ContratoPraticaCentroLucroBean cpclBean = null;

    /**
     * Instancia do bean ContratoPraticaGrupoCusto.
     */
    @Autowired
    private ContratoPraticaGrupoCustoBean cpgcBean = null;

    @Autowired
    private PraticaBean pratBean = null;

    /**
     * Instancia do bean do Imposto.
     */
    @Autowired
    private ImpostoBean impostoBean = null;

    /**
     * Instancia do bean da Aliquota.
     */
    @Autowired
    private AliquotaBean aliquotaBean = null;

    /**
     * Instancia do bean controle de mensagnes.
     */
    @Autowired
    private MessageControlBean messageControlBean = null;

    /**
     * Instancia do servico {@link GrupoCustoService}.
     */
    @Autowired
    private IGrupoCustoService grupoCustoService;

	/** Envia novos projetos para as filas do RabbitMQ**/
	@Autowired
	private ProjectProducer projectProducer;

    @Autowired
    private RecalculateMapProducer recalculateMapProducer;

    @Autowired
    private Properties systemProperties;

    @Autowired
    private IForecastRevenueStatusService forecastRevenueStatusService;

    @Autowired
    private IForecastRevenueStatusProjectService forecastRevenueStatusProjectService;

	/** outcome tela de inclusao da entidade. */
	private static final String OUTCOME_CONTRATO_PRATICA_ADD = "contratoPratica_add";

    /**
     * outcome tela de busca da entidade.
     */
    private static final String OUTCOME_CONTRATO_PRATICA_RESEARCH = "contratoPratica_research";

    /**
     * outcome tela de view da entidade.
     */
    private static final String OUTCOME_CONTRATO_PRATICA_VIEW = "contratoPratica_view";

    /**
     * outcome tela de configure da entidade.
     */
    private static final String OUTCOME_CONTRATO_PRATICA_CONFIGURE = "contratoPratica_configure";

    /**
     * outcome tela de configure da entidade.
     */
    private static final String OUTCOME_CONTRATO_PRATICA_UPLOAD = "contratoPratica_upload";


    /**
     * outcome tela de configure da entidade.
     */
    private static final String PADRAO_ARQUIVO_CSV = "Padrão OpenOffice";


    /**
     * Status
     */
    private static final String ATIVO = "A";

    /**
     * outcome tela de edicao da entidade.
     */
    private static final String OUTCOME_CONTRATO_PRATICA_EDIT = "contratoPratica_edit";

    private List<DealFiscalValidation> validations = new ArrayList<DealFiscalValidation>();

    /**
     * Pega o bean.
     *
     * @return o bean ContratoPratica
     */
    public ContratoPraticaBean getBean() {
        return bean;
    }

    /**
     * Seta o bean.
     *
     * @param pBean do ContratoPratica
     */
    public void setBean(final ContratoPraticaBean pBean) {
        this.bean = pBean;
    }

    /**
     * @param impostoBean the impostoBean to set
     */
    public void setImpostoBean(final ImpostoBean impostoBean) {
        this.impostoBean = impostoBean;
    }

    /**
     * @return the impostoBean
     */
    public ImpostoBean getImpostoBean() {
        return impostoBean;
    }

    /**
     * @param aliquotaBean the aliquotaBean to set
     */
    public void setAliquotaBean(final AliquotaBean aliquotaBean) {
        this.aliquotaBean = aliquotaBean;
    }

    /**
     * @return the aliquotaBean
     */
    public AliquotaBean getAliquotaBean() {
        return aliquotaBean;
    }

    public ContratoPraticaGrupoCustoBean getCpgcBean() {
        return cpgcBean;
    }

    public void setCpgcBean(ContratoPraticaGrupoCustoBean cpgcBean) {
        this.cpgcBean = cpgcBean;
    }

    /**
     * Retorna para a tela de busca.
     *
     * @return OUTCOME_CONTRATO_PRATICA_RESEARCH
     */
    public String back() {
        bean.reset();
        return OUTCOME_CONTRATO_PRATICA_RESEARCH;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     *
     * @return pagina de destino
     */
    public String prepareSearch() {
        bean.reset();

        this.loadCombos();

        return OUTCOME_CONTRATO_PRATICA_RESEARCH;
    }

    /**
     * Prepara a tela para add um ContratoPratica.
     *
     * @return retorna a pagina de Add do ContratoPratica
     */
    public String prepareAdd() {
        this.bean.reset();
        this.bean.setIsCreation(Boolean.TRUE);

        this.loadCombos();
        prepareCreateContratoPraticaGrupoCusto();

        return OUTCOME_CONTRATO_PRATICA_ADD;
    }

    /**
     * Prepara a tela de visualizAcao da entidade.
     *
     * @return retorna a pagina de visualizAcao do ContratoPratica
     */
    public String prepareView() {
        // marca o modo de remo��o como false (view) para que a tela n�o
        // mostre
        // o botao de excluir
        bean.setIsRemove(Boolean.FALSE);

        // carrega o ContratoPratica em modo de visualizAcao
        this.loadContratoPraticaView();

        setIsITSupport(true);

		return OUTCOME_CONTRATO_PRATICA_VIEW;
	}

    /**
     * Prepara a tela para configure um ContratoPratica.
     *
     * @return retorna a pagina de configure do ContratoPratica
     */
    public String prepareConfigure() {

        bean.setIsUpdate(Boolean.TRUE);
        bean.setIsCreation(Boolean.FALSE);
        bean.setIsContractsPerfil(true);
		setIsITSupport(false);

        this.findById(bean.getTo().getCodigoContratoPratica());

        this.loadPickLists();

        // guarda a data do HistoryLock
        bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

        bean.setTo(contratoPraticaService.findContratoPraticaById(bean.getTo().getCodigoContratoPratica()));

        List<ContratoPraticaGrupoCusto> cpgcs = contratoPraticaGrupoCustoService.findByContratoPraticaId(bean.getTo().getCodigoContratoPratica());
        bean.getTo().setContratoPraticaGrupoCustos(cpgcs);
        bean.setShowUpdateMapaAlocacaoModal(Boolean.FALSE);

        return OUTCOME_CONTRATO_PRATICA_CONFIGURE;
    }

	private void setIsITSupport(boolean isView) {

		GrantedAuthority[] loggedUserAuthorities = LoginUtil
				.getLoggedUserAuthorities();
		boolean isItSupport = false;
		for (GrantedAuthority authority : loggedUserAuthorities) {

			if(authority.getAuthority().equals("BUS.CONTRACT_LOB.FISCAL_DEAL:ED")
					|| authority.getAuthority().equals("BUS.CONTRACT_LOB.FISCAL_DEAL:CR")
			        || authority.getAuthority().equals("BUS.CONTRACT_LOB:ED")
					|| authority.getAuthority().equals("BUS.CONTRACT_LOB:CR")){
				isItSupport = false;
				break;
			}
			if (authority.getAuthority().equals("BUS.CONTRACT_LOB.FISCAL_DEAL:VW")
			     || authority.getAuthority().equals("BUS.CONTRACT_LOB:VW")) {
				isItSupport = true;
			}
		}

		bean.setIsContractsPerfil(isView? isView : isItSupport);

	}

	private void loadDealFiscalValidations() {
		validations.add(new SelecaoDealFiscalValidation());
		validations.add(new DealFiscalSameParametersValidation());
		validations.add(new MultiFiscalDealIntercompanyValidation());
		validations.add(new MultiFiscalDealSameCompanyValidation());
		validations.add(new MultiDealFiscalComAlocacaoVigenteValidation(alocacaoService, cidadeBaseService, moduloService));
        validations.add(new FiscalDealIntercompanyAllocationsValidation(alocacaoService, cidadeBaseService, moduloService, empresaService));
	}

	/**
	 * Realiza a validacao do contrato pratica e vai para a proxima aba.
	 * 
	 * @return retorna o outcome da pagina destino ou string vazia caso nao
	 *         passe em alguma validacao
	 */
	public String contratoPraticaNext() {

        if (checkStatusExpandAndWorkAtRisk(bean.getAtivoInativoCombo().getSelectedItem(), bean.getIndicadorWorkAtRisk())) {
            Messages.showError(CONTRATO_PRATICA_NEXT, INVALID_STATUS_EXPAND_WITH_WORK_AT_RISK);
            return null;
        }

        ContratoPratica cp = bean.getTo();
        cp.setIndicadorDeleteLogico(Constants.NO);
        cp.setIndicadorMultiDealFiscal(Constants.NO);
        cp.setIndicadorWorkAtRisk(Boolean.TRUE.equals(bean.getIndicadorWorkAtRisk()) ? Constants.FLAG_YES : Constants.FLAG_NO);

        // Esse fluxo a partir de 12/09/2024 sempre permanecerá ativo.
        // TODO: futuramente essa flag pode ser desconsiderada
        cp.setIndicadorAprovaAjusteReceita(Constants.FLAG_YES);

        if (validaCentroLucroSelecionado()) return null;

        Long msaId = bean.getMsaComboMap().get(bean.getMsaSelected());
        Msa msa = msaService.findMsaById(msaId);

        Pratica pratica = cp.getPratica();

        if (pratica.getNomePratica() == null) {
            Messages.showError(CONTRATO_PRATICA_NEXT,
                    LOB_PRATICA_NOT_FOUND);
            return null;
        }

        pratica = praticaService.findPraticaById(bean.getPraticaMap().get(
                pratica.getNomePratica()));

        GrupoCusto grupoCusto = bean.getGrupoCustoCombo().getSelectedItem();
        cp.setMsa(msa);
        cp.setAprovador(bean.getAprovadorCombo().getSelectedItem());
        cp.setIndicadorAtivo(bean.getAtivoInativoCombo().getSelectedItem());
        cp.setGerenteAprovador(bean.getGerenteCombo().getSelectedItem());

        Date startDate = null;
        Date startDateBM = null;
        Date startDateSSO = null;
        Date startDateUKMT = null;
        String[] dateFormat = { "MM/yyyy" };
        try {
            startDate = DateUtils.parseDate(cpclBean.getMesInicioVigencia() + "/"
                    + cpclBean.getAnoInicioVigencia(), dateFormat);
            startDateBM = DateUtils.parseDate(bean.getMesInicioVigenciaBM() + "/"
                    + bean.getAnoInicioVigenciaBM(), dateFormat);
            startDateSSO = DateUtils.parseDate(bean.getMesInicioVigenciaSSO() + "/"
                    + bean.getAnoInicioVigenciaSSO(), dateFormat);
            startDateUKMT = DateUtils.parseDate(bean.getMesInicioVigenciaUKMT() + "/"
                    + bean.getAnoInicioVigenciaUKMT(), dateFormat);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ContratoPraticaCentroLucro cpcl = new ContratoPraticaCentroLucro();
        cpcl.setDataInicioVigencia(startDate);
        cpcl.setCentroLucro(bean.getCentroLucroCombo().getSelectedItem());

        ContratoPraticaCentroLucro cpclBM = new ContratoPraticaCentroLucro();
        cpclBM.setDataInicioVigencia(startDateBM);
        cpclBM.setCentroLucro(bean.getBusinessManagerCombo().getSelectedItem());

        ContratoPraticaCentroLucro cpclSSO = new ContratoPraticaCentroLucro();
        cpclSSO.setDataInicioVigencia(startDateSSO);
        cpclSSO.setCentroLucro(bean.getSsoCombo().getSelectedItem());

        ContratoPraticaCentroLucro cpclUKMT = new ContratoPraticaCentroLucro();
        cpclUKMT.setDataInicioVigencia(startDateUKMT);
        cpclUKMT.setCentroLucro(bean.getUkmtCombo().getSelectedItem());

        // se criAcao de um novo ContratoPratica
        if (cp.getCodigoContratoPratica() == null) {

            if (Boolean.FALSE.equals(moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE))) {

                return null;
            }

            cp.setNomeContratoPratica(this.composesClobName(cp.getMsa()
                    .getNomeMsa(), cp.getNomeCompound(), pratica
                    .getSiglaPratica()));
            cp.setMsa(msa);
            cp.setPratica(pratica);
            cp.setIndicadorDeleteLogico(Constants.NO);

            TipoModeloNegocio tmn = new TipoModeloNegocio();
            tmn.setCodigoTipoModeloNegocio(bean.getTipoModeloNegocioMap().get(bean.getTipoModeloNegocioSelected()));
            cp.setTipoModeloNegocio(tmn);

            try {
                List<ContratoPraticaCentroLucro> cpclList = new ArrayList<>();
                cpclList.add(cpcl);
                cpclList.add(cpclBM);
                cpclList.add(cpclSSO);
                cpclList.add(cpclUKMT);

                contratoPraticaService.createContratoPraticaWithConvergencia(
                        cp, grupoCusto, cpclList);

                contratoPraticaVerify();

                Messages.showSucess(CONTRATO_PRATICA_NEXT,
                        Constants.DEFAULT_MSG_SUCCESS_NEXT_STEP,
                        Constants.ENTITY_NAME_CONTRATO_PRATICA);

            } catch (IntegrityConstraintException e) {
                Messages.showWarning(CONTRATO_PRATICA_NEXT,
                        Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                        Constants.ENTITY_NAME_CONTRATO_PRATICA);

                // interrompe o processo e mantem na mesma pagina
                return null;
            }

            // se edicao de um ContratoPratica
        } else {
            ContratoPratica cpAux = contratoPraticaService
                    .findContratoPraticaByName(cp.getNomeContratoPratica());

            if (cpAux != null
                    && cpAux.getCodigoContratoPratica().compareTo(
                    cp.getCodigoContratoPratica()) != 0) {

                Messages.showWarning(CONTRATO_PRATICA_NEXT,
                        Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                        Constants.ENTITY_NAME_CONTRATO_PRATICA);

                // interrompe o processo e mantem na mesma pagina
                return null;

            } else {

                try {
                    contratoPraticaService.updateContratoPratica(cp);
                } catch (MoreThanOneActiveEntityException e) {
                    log.warn(e.getMessage());
                    Messages.showError(CONTRATO_PRATICA_NEXT, e.getMessage(),
                            Constants.ENTITY_NAME_CONTRATO_PRATICA);
                }

                Messages.showSucess(CONTRATO_PRATICA_NEXT,
                        Constants.DEFAULT_MSG_SUCCESS_NEXT_STEP,
                        Constants.ENTITY_NAME_CONTRATO_PRATICA);
            }
        }

        // Exibe a tela de configure (edicao de abas abilitada)
        return this.prepareConfigure();
    }

    private boolean validaCentroLucroSelecionado() {
        String selectedItemKey = null;
        for (Map.Entry<String, CentroLucro> entry : bean.getCentroLucroCombo().getMap().entrySet()) {
            if (entry.getValue().equals(bean.getCentroLucroCombo().getSelectedItem())) {
                selectedItemKey = entry.getKey();
                break;
            }
        }
        if (selectedItemKey == null || !bean.getCentroLucroCombo().getMap().containsKey(selectedItemKey)) {
            Messages.showError(CONTRATO_PRATICA_NEXT,
                    CREATE_PROFIT_CENTER_VALIDATION);
            return true;
        }
        if(!bean.getPraticaList().contains(bean.getCentroLucroCombo().getSelectedItem().getNomeCentroLucro())){
            Messages.showError(CREATE_PROFIT_CENTER,
                    LOB_PRATICA_NOT_FOUND);
            return true;
        }

        return false;
    }

    /**
     * Compoe o nome do {@link ContratoPratica} de acordo com os parametros
     * informados.
     *
     * @param nomeMsa      - nome da {@link Moeda}
     * @param freeText     - texto livre do usuario
     * @param siglaPratica - sigla da {@link Pratica}
     * @return nome do {@link ContratoPratica}
     */
    public static String composesClobName(final String nomeMsa,
                                    final String freeText, final String siglaPratica) {
        StringBuilder text = new StringBuilder(nomeMsa);
        if (freeText == null || freeText.isEmpty()) {
            return text.append("-").append(siglaPratica).toString();
        }
        return text.append(".").append(freeText).append("-")
                .append(siglaPratica).toString();
    }

    /**
     * Prepara a tela para a ContratoPraticaCentroLucro.
     */
    @SuppressWarnings("all")
    public void prepareContratoPraticaCentroLucro() {
        cpclBean.reset();

        // recupera um Map com as 3 listas geradas separadamente
        Map<Integer, List> naturezaCPCLListsMap = cpclService
                .prepareNaturezaCPCLListMandatory(bean.getTo());
        Integer cont = Integer.valueOf(0);

        // popula a lista de NaturezaCentroLucro obrigatorias + CPCL
        bean.setNaturezaCPCLRowList((List<NaturezaContratoPraticaCLRow>) naturezaCPCLListsMap
                .get(++cont));

        // popula a lista de NaturezaCentroLucro opcionais + seus respectivos
        // CPCL
        // carrega a lista de NaturezaCentroLucro para o combobox
        this.loadNaturezaList((List<NaturezaCentroLucro>) naturezaCPCLListsMap
                .get(++cont));

        cpclBean.setResultList((List<ContratoPraticaCentroLucro>) naturezaCPCLListsMap
                .get(++cont));

        // guarda a data do HistoryLock
        bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());
    }

    /**
     * Prepara a tabela de Impostos.
     */
    public void prepareTabelaImposto() {
        ContratoPratica cp = contratoPraticaService
                .findContratoPraticaById(bean.getTo()
                        .getCodigoContratoPratica());

        BigDecimal totalTax = new BigDecimal(0);

        // lista impostos do contrato pratico
        List<Imposto> imp = cp.getImpostos();

        // lista a ser populada com aliquotas
        List<Aliquota> listaAliquota = new ArrayList<Aliquota>();

        for (Imposto imposto : imp) {

            Aliquota aliquota = aliquotaService
                    .findByImpostoByCurrentDate(imposto);

            if (aliquota == null) {
                aliquota = new Aliquota();
                aliquota.setValorAliquota(new BigDecimal(0));
                aliquota.setImposto(imposto);
            }
            totalTax = totalTax.add(aliquota.getValorAliquota());
            listaAliquota.add(aliquota);

        }
        aliquotaBean.setResultList(listaAliquota);
        bean.setAliquotaList(listaAliquota);
        bean.setTotalTax(totalTax);

    }

    /**
     * Prepara o modal para criacao dos CPCL das NaturezaCentroLucro Mandatory
     * (obrigatorias).
     */
    public void prepareCreateCPCLNatMandatory() {
        NaturezaCentroLucro naturezaCentroLucro = cpclBean.getTo()
                .getCentroLucro().getNaturezaCentroLucro();

        naturezaCentroLucro = naturezaCentroLucroService
                .findNaturezaCentroLucroById(naturezaCentroLucro
                        .getCodigoNatureza());
        loadCentroLucroListModal(naturezaCentroLucro.getCentroLucrosAtivos());
        if(Objects.equals(naturezaCentroLucro.getCodigoNatureza(), NATUREZA_LOB)){

            Iterator<String> iterator =  cpclBean.getCentroLucroListModal().iterator();

            GrupoCusto grupoCusto = bean.getGrupoCustoCombo().getMap().get(bean.getGrupoCustoCombo().getSelected());
            Long tipoArea = grupoCustoService.findTipoAreaByCentroCusto(grupoCusto.getCodigoGrupoCusto());

            while (iterator.hasNext()) {
                String item = iterator.next();
                if (tipoArea != null){
                    if (tipoArea == 1) {
                        if (item.equals("Sales")) {
                            iterator.remove();
                        }
                    } else if (tipoArea == 2 && !item.equals("Sales")) {
                        iterator.remove();
                    }
                }
            }
        }

        bean.setIsUpdateProfitCenter(Boolean.FALSE);
        messageControlBean.setDisplayMessages(Boolean.FALSE);

        }


    public void prepareUpdateCPCLNatMandatory(){

        prepareCreateCPCLNatMandatory();
        bean.setIsUpdateProfitCenter(Boolean.TRUE);
        Calendar calendar = Calendar.getInstance();
        bean.getCentroLucroCombo().setSelected(cpclBean.getTo().getCentroLucro().getNomeCentroLucro());
        calendar.setTime(cpclBean.getTo().getDataInicioVigencia());
        cpclBean.setAnoInicioVigencia(String.valueOf(calendar.get(Calendar.YEAR)));
        cpclBean.setMesInicioVigencia(String.valueOf(calendar.get(Calendar.MONTH) +1));
    }
    /**
     * Cria uma entidade CentroLucro associado a um ContratoPratica - Modal Add.
     */
    public void createCPCLNatMandatory() throws MoreThanOneActiveEntityException, ParseException {
        ContratoPraticaCentroLucro cpcl = cpclBean.getTo();

        Date startDate = null;
        try {
            // pega a data de inicio
            String startDateStr = cpclBean.getMesInicioVigencia() + "/"
                    + cpclBean.getAnoInicioVigencia();

            String[] dateFormat = {DATE_FORMAT};
            startDate = DateUtils.parseDate(startDateStr, dateFormat);

            // verifica o History Lock. Se startDate nao for valido, exibe
            // mensagem de erro
            if (Boolean.FALSE.equals(moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE))) {

                return;
            }

            if(!bean.getPraticaList().contains(cpcl.getCentroLucro().getNomeCentroLucro()) && Objects.equals(cpcl.getCentroLucro().getNaturezaCentroLucro().getCodigoNatureza(), NATUREZA_LOB)){
                Messages.showError(CREATE_PROFIT_CENTER,
                        LOB_PRATICA_NOT_FOUND);
                return;
            }
            // pega o contrato-pratica
            ContratoPratica cp = contratoPraticaService
                    .findContratoPraticaById(bean.getTo()
                            .getCodigoContratoPratica());

            // pega o centro-lucro
            CentroLucro cl = centroLucroService.findCentroLucroById(cpclBean
                    .getCentroLucroMapModal().get(
                            cpcl.getCentroLucro().getNomeCentroLucro()));

            cpcl.setDataInicioVigencia(startDate);

            processarContratoPraticaLOB(cpcl, cp,  cl);

            // seta com as entidades
            cpcl.setContratoPratica(cp);
            cpcl.setCentroLucro(cl);
            if (cpcl.getCodigoContratoPraticaCl() == null){
            // Caso o nome da Pratica do ContratoPratica seja diferente do nome
            // do Centro de Lucro, alterar o status do ContratoPratica para
            // Incompleto
            if (!isSameNamePraticaAndCentroLucro(cp, cl)) {
                cp.setIndicadorStatus(ContratoPraticaStatus.INCOMPLETE
                        .getIndicadorStatus());
                bean.getTo().setIndicadorStatus(
                        ContratoPraticaStatus.INCOMPLETE.getIndicadorStatus());
            }

            // verifica se foi inserido com sucesso
            if (Boolean.TRUE.equals(cpclService.createCPCL(cpcl))) {
                cpclBean.getNaturezaCPCLRow().setContratoPraticaCLList(
                        cpclService.findCPCLByContratoPraticaAndNatureza(cp,
                                cl.getNaturezaCentroLucro()));

                cpclBean.resetTo();

                messageControlBean.setDisplayMessages(Boolean.TRUE);
            }

            cp = contratoPraticaService.verifyContratoPraticaComplete(cp);
            contratoPraticaService.atualizaContratoPratica(cp);
            cp.setContratoPraticaGrupoCustos(contratoPraticaGrupoCustoService.findByContratoPraticaId(cp.getCodigoContratoPratica()));
            bean.setTo(cp);

            }else{
                cpclService.updateCPCL(cpcl);
                bean.setTo(cp);
                contratoPraticaService.atualizaContratoPratica(cp);
                cpclBean.reset();

            }

        } catch (MoreThanOneActiveEntityException e){
            Messages.showError(CONTRATO_PRATICA_NEXT,
                    Constants.CONTRATO_PRATICA_SAME_NAME);

        } catch (ParseException e) {
        e.printStackTrace();
        Messages.showError(CREATE_TABELA_PRECO,
                Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
       }

    }

    private void processarContratoPraticaLOB(ContratoPraticaCentroLucro cpcl, ContratoPratica cp, CentroLucro cl) throws MoreThanOneActiveEntityException {
        if (Objects.equals(cpcl.getCentroLucro().getNaturezaCentroLucro().getCodigoNatureza(), ContratoPraticaController.NATUREZA_LOB)) {
            if (isLobCentroLucroValidoParaVigencia(cpcl, cp)) {
                if (!cp.getPratica().getNomePratica().equalsIgnoreCase(cl.getNomeCentroLucro())) {
                    pratBean.getFilter().setNomePratica(cl.getNomeCentroLucro());
                    pratBean.getFilter().setIndicadorAtivo(ATIVO);

                    Optional<Pratica> lob = praticaService.findPraticaByFilter(pratBean.getFilter()).stream().findFirst();

                    if (lob.isPresent()) {
                        cp.setPratica(lob.get());
                        checkIfNameExists(cp, lob.get().getSiglaPratica());
                        cp.setNomeContratoPratica(this.composesClobName(cp.getMsa().getNomeMsa(), cp.getNomeCompound(), lob.get().getSiglaPratica()));
                    }
                }
            }
        }
    }

    private void checkIfNameExists(ContratoPratica cp, String sigla) throws  MoreThanOneActiveEntityException {
        String nomeClob = this.composesClobName(cp.getMsa().getNomeMsa(), cp.getNomeCompound(), sigla);
        ContratoPratica cpAux = contratoPraticaService.findContratoPraticaByName(nomeClob);

        if (cpAux != null && cpAux.getCodigoContratoPratica().compareTo(cp.getCodigoContratoPratica()) != 0) {
            throw new MoreThanOneActiveEntityException(Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS);
        }

    }

    /**
     * O método verifica se o contrato prática possui centros de lucro da natureza LOB
     * e se a Data de Início de Vigência é igual ou anterior ao mês atual ou se não existe outro cadastrado anteriormente.
     *
     * @param cpcl ContratoPraticaCentroLucro - O centro de lucro que está sendo cadastrado no contrato prática
     * @param cp ContratoPratica - O contrato prática que contém os centros de lucro.
     * @return boolean - True se o centro de lucro do tipo LOB que está sendo cadastrado está dentro do período de vigência atual,
     *                  ou se não há outros centros de lucro associados da natureza LOB; False caso contrário.
     */
    private boolean isLobCentroLucroValidoParaVigencia(ContratoPraticaCentroLucro cpcl, ContratoPratica cp) {
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());

        Calendar calPeriod = Calendar.getInstance();
        calPeriod.setTime(cpcl.getDataInicioVigencia());

        List<CentroLucro> lobCentrosLucro = new ArrayList<>();
        for (CentroLucro centroLucro : cp.getCentroLucros()) {
            if (NATUREZA_LOB.equals(centroLucro.getNaturezaCentroLucro().getCodigoNatureza())) {
                lobCentrosLucro.add(centroLucro);
            }
        }
        return lobCentrosLucro.isEmpty() || (lobCentrosLucro.size() == 1 && cpcl.getCodigoContratoPraticaCl() != null) ||
                (calPeriod.get(Calendar.YEAR) < currentCal.get(Calendar.YEAR)) ||
                (calPeriod.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) &&
                        calPeriod.get(Calendar.MONTH) <= currentCal.get(Calendar.MONTH));
    }

    /**
     * Retorna um booleano indicando se o nome da Pratica do
     * {@link ContratoPratica} eh igual ao nome do {@link CentroLucro}. Obs.:
     * Case ignorado. "xpto" = "XPTO"
     *
     * @param cp {@link ContratoPratica}
     * @param cl {@link CentroLucro}
     * @return boolean
     */
    private boolean isSameNamePraticaAndCentroLucro(ContratoPratica cp,
                                                    CentroLucro cl) {

        String nomeCentroLucro = cl.getNomeCentroLucro();
        String nomePratica = cp.getPratica().getNomePratica();

        if ("Pacific".equalsIgnoreCase(nomeCentroLucro)
                || "Pacific".equalsIgnoreCase(nomePratica)) {
            return Boolean.TRUE;
        }

        return nomePratica.equalsIgnoreCase(nomeCentroLucro);
    }

    /**
     * Cria uma entidade CentroLucro associado a um ContratoPratica.
     */
    public void createContratoPraticaCentroLucro() throws MoreThanOneActiveEntityException {
        ContratoPraticaCentroLucro cpcl = cpclBean.getTo();
        Date startDate = null;

        try {
            // pega a data de inicio
            String startDateStr = cpclBean.getMesInicioVigencia() + "/"
                    + cpclBean.getAnoInicioVigencia();

            String[] dateFormat = {DATE_FORMAT};
            startDate = DateUtils.parseDate(startDateStr, dateFormat);

            // verifica o History Lock. Se startDate nao for valido, exibe a
            // mensagem de erro
            if (!moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE)) {
                return;
            }

            cpcl.setDataInicioVigencia(startDate);

            // pega o contrato-pratica
            ContratoPratica cp = contratoPraticaService
                    .findContratoPraticaById(bean.getTo()
                            .getCodigoContratoPratica());
            // pega o centro-lucro
            CentroLucro cl = centroLucroService.findCentroLucroById(cpclBean
                    .getCentroLucroMap().get(
                            cpcl.getCentroLucro().getNomeCentroLucro()));
            // seta com as entidades
            cpcl.setContratoPratica(cp);
            cpcl.setCentroLucro(cl);

            // verifica se foi inserido com sucesso
            if (cpclService.createCPCL(cpcl)) {
                cpclBean.resetTo();
            }

            cpclBean.setResultList(cpclService
                    .findCPCLByContratoPraticaAndNaturezaOptional(cp));

            messageControlBean.setDisplayMessages(Boolean.TRUE);

        } catch (ParseException e) {
            e.printStackTrace();
            Messages.showError(CREATE_TABELA_PRECO,
                    Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
        }
    }

    /**
     * Prepara a tela para criar o Imposto.
     */
    public void prepareImposto() {

        impostoBean.reset();

        this.loadImpostoList(impostoService.findImpostoAll());

        if (bean.getTo().getCodigoContratoPratica() != null) {
            prepareTabelaImposto();
        }
    }

    /**
     * Cria uma entidade TabelaPreco associado a um ContratoPratica.
     */
    public void createImposto() {

        // pega o contrato-pratica
        ContratoPratica cp = contratoPraticaService
                .findContratoPraticaById(bean.getTo()
                        .getCodigoContratoPratica());
        // pega o imposto
        Imposto i = impostoService.findImpostoById(impostoBean.getImpostoMap()
                .get(impostoBean.getTo().getNomeImposto()));

        List<Imposto> impostos = cp.getImpostos();
        boolean exists = false;

        for (Imposto imposto : impostos) {
            if (imposto.getCodigoImposto().compareTo(i.getCodigoImposto()) == 0) {
                exists = true;
                break;
            }
        }

        if (exists) {
            Messages.showWarning("createImposto", Constants.MSG_EXIST_ALIQUOTA);
        } else {
            impostos.add(i);
            try {
                contratoPraticaService.updateContratoPratica(cp);
            } catch (MoreThanOneActiveEntityException e) {
                log.warn(e.getMessage());
                Messages.showError(CONTRATO_PRATICA_NEXT, e.getMessage(),
                        Constants.ENTITY_NAME_CONTRATO_PRATICA);
            }
            impostoBean.resetTo();
            prepareTabelaImposto();
        }
    }

    /**
     * Remove uma ContratoPraticaCentroLucro das NaturezaCentroLucro
     * obrigatorias.
     */
    public void removeCPCLNatMandatory() throws MoreThanOneActiveEntityException {

        ContratoPraticaCentroLucro cpcl = cpclService.findCPCLById(cpclBean
                .getTo().getCodigoContratoPraticaCl());

        NaturezaCentroLucro natureza = cpcl.getCentroLucro()
                .getNaturezaCentroLucro();

        cpclService.removeCPCL(cpcl);
        ContratoPratica cp = contratoPraticaService.verifyContratoPraticaComplete(contratoPraticaService
                .findContratoPraticaById(cpcl.getContratoPratica().getCodigoContratoPratica()));

        ContratoPraticaCentroLucro contratoPraticaCentroLucroVigente = null;

        contratoPraticaCentroLucroVigente = cpclService
                .findPresentByContratoPraticaAndNatureza(bean.getTo(), natureza);

        // condicao para nao validar caso o unico item for removido
        if (contratoPraticaCentroLucroVigente != null
                && contratoPraticaCentroLucroVigente.getCentroLucro() != null) {
            CentroLucro centroLucroVigente = centroLucroService
                    .findCentroLucroById(contratoPraticaCentroLucroVigente
                            .getCentroLucro().getCodigoCentroLucro());

            if (!isSameNamePraticaAndCentroLucro(bean.getTo(), centroLucroVigente)) {
                try {
                    processarContratoPraticaLOB(cpcl, cp, contratoPraticaCentroLucroVigente.getCentroLucro());
                    contratoPraticaService.atualizaContratoPratica(cp);
                } catch (MoreThanOneActiveEntityException e) {
                    log.warn(e.getMessage());
                }
            }
        }

        cpclBean.getNaturezaCPCLRow().setContratoPraticaCLList(
                cpclService.findCPCLByContratoPraticaAndNatureza(bean.getTo(),
                        natureza));

        cpclBean.resetTo();
        cp.setContratoPraticaGrupoCustos(contratoPraticaGrupoCustoService.findByContratoPraticaId(cp.getCodigoContratoPratica()));
        bean.setTo(cp);
    }

    /**
     * Remove uma ContratoPraticaCentroLucro.
     */
    public void removeContratoPraticaCentroLucro() {

        ContratoPraticaCentroLucro cpcl = cpclService.findCPCLById(cpclBean
                .getTo().getCodigoContratoPraticaCl());

        cpclService.removeCPCL(cpcl);

        List<ContratoPraticaCentroLucro> cpclList = cpclService
                .findCPCLByContratoPraticaAndNaturezaOptional(bean.getTo());

        cpclBean.setResultList(cpclList);
        cpclBean.resetTo();
    }

    /**
     * Remove um Imposto.
     */
    public void removeImposto() {
        ContratoPratica cp = contratoPraticaService
                .findContratoPraticaById(bean.getTo()
                        .getCodigoContratoPratica());
        List<Imposto> impostos = cp.getImpostos();

        impostos.remove(impostoBean.getRowNumber().intValue());

        try {
            contratoPraticaService.updateContratoPratica(cp);
        } catch (MoreThanOneActiveEntityException e) {
            log.warn(e.getMessage());
            Messages.showError(CONTRATO_PRATICA_NEXT, e.getMessage(),
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
        }
        impostoBean.resetTo();
        prepareTabelaImposto();
    }

    /**
     * verifica se o contrato pratica esta completo ou incompleto.
     *
     * @return retorna a pagina de destino.
     */
    public String contratoPraticaVerify() {
        ContratoPratica cp = bean.getTo();

        cp = contratoPraticaService.verifyContratoPraticaComplete(cp);
        if (cp.getIndicadorStatus().equals(ContratoPraticaStatus.COMPLETE.getIndicadorStatus())) {
            Messages.showSucess("contratoPraticaVerify",
                    Constants.MSG_SUCCESS_CONTRATO_PRATICA_COMPLETE);

            findByFilter();
            bean.reset();

            return OUTCOME_CONTRATO_PRATICA_RESEARCH;
        } else {
            Messages.showWarning("contratoPraticaVerify",
                    Constants.MSG_ERROR_CONTRATO_PRATICA_INCOMPLETE);

            return null;
        }
    }

    /**
     * Prepara a tela de remocao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareRemove() {
        // marca o modo de remocao para que a tela mostre o botao de excluir
        bean.setIsRemove(Boolean.TRUE);

        // carrega o ContratoPratica em modo de visualizacao
        loadContratoPraticaView();
		setIsITSupport(false);
        return OUTCOME_CONTRATO_PRATICA_VIEW;
    }

    /**
     * Remove um contrato pratica.
     *
     * @return pagina de destino
     */
    @Transactional
    public String removeContratoPratica() {

        ContratoPratica contratoPratica = bean.getTo();
        try {

            tecnologiaContratoPraticaService.removeAllByClob(contratoPratica);

            ContratoPratica contratoPraticaEncontrado = contratoPraticaService.findContratoPraticaById(contratoPratica.getCodigoContratoPratica());

            if (Boolean.FALSE.equals(contratoPraticaService.removeContratoPratica(contratoPraticaEncontrado))) {
                return null;
            }
        } catch (DataIntegrityViolationException e) {
            Messages.showError(REMOVE_CONTRATO_PRATICA,
                    Constants.GENERIC_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE,
                    new Object[]{Constants.ENTITY_NAME_CONTRATO_PRATICA});

            return null;
        }

        Messages.showSucess(REMOVE_CONTRATO_PRATICA,
                Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_CONTRATO_PRATICA);

        bean.resetTo();
        findByFilter();

        return OUTCOME_CONTRATO_PRATICA_RESEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     *
     * @param id da entidade.
     */
    public void findById(final Long id) {
        ContratoPratica cp = contratoPraticaService.findContratoPraticaById(id);

        if (cp != null) {
            bean.setTo(cp);
            if (cp.getIndicadorReembolsavel() != null) {
                bean.setIndicadorReembolsavel(cp.getIndicadorReembolsavel().equalsIgnoreCase("N") ? "NR" : "RB");
            }
            bean.setIndicadorMultiFiscalDeal(Constants.YES.equals(cp.getIndicadorMultiDealFiscal()) ? Boolean.TRUE : Boolean.FALSE);
            bean.setIndicadorAprovaAjusteReceita(Constants.YES.equals(cp.getIndicadorAprovaAjusteReceita()) ? Boolean.TRUE : Boolean.FALSE);
            bean.setIndicadorWorkAtRisk(Constants.FLAG_YES.equals(cp.getIndicadorWorkAtRisk()) ? Boolean.TRUE : Boolean.FALSE);

            loadSelectedItems(cp);
        } else {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    private void loadSelectedItems(ContratoPratica cp) {
        Pessoa gerenteAprovador = cp.getGerenteAprovador();
        String gerenteLogin = "";
        if (gerenteAprovador != null) {
            gerenteLogin = gerenteAprovador.getCodigoLogin();
        }
        Pessoa aprovador = cp.getAprovador();
        String aprovadorLogin = "";
        if (aprovador != null) {
            aprovadorLogin = aprovador.getCodigoLogin();
        }
        String nomeMsa = cp.getMsa().getNomeMsa();
        Convergencia convergencia = convergenciaService
                .findByContratoPraticaId(cp.getCodigoContratoPratica());
        String nomeGrupoCusto = "";
        if (convergencia != null) {
            GrupoCusto grupoCusto = convergencia.getGrupoCusto();
            if (grupoCusto != null) {
                StringBuilder bd = new StringBuilder();
                bd.append(grupoCusto.getNomeGrupoCusto());
                Empresa empresa = grupoCusto.getEmpresa();
                if (empresa != null) {
                    bd.append(" - ");
                    bd.append(empresa.getNomeEmpresa());
                }
                nomeGrupoCusto = bd.toString();
            }
        }

        bean.getGerenteCombo().setSelected(gerenteLogin);
        bean.getAprovadorCombo().setSelected(aprovadorLogin);
        bean.setMsaSelected(nomeMsa);
        bean.getGrupoCustoCombo().setSelected(nomeGrupoCusto);
        cp.setTipoModeloNegocio(tipoModeloNegocioService.findTipoModeloNegocioById(cp.getTipoModeloNegocio().getCodigoTipoModeloNegocio()));
        bean.setTipoModeloNegocioSelected(cp.getTipoModeloNegocio().getNomeTipoModeloNegocio());
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     */
    public void findByFilter() {

        ContratoPratica filter = bean.getFilter();

        // pega o id contrato
        Long idMsa = bean.getMsaComboMap().get(bean.getMsaSelected());

        // se o contrato existir
        filter.getMsa().setCodigoMsa(null);
        if (idMsa != null) {
            filter.getMsa().setCodigoMsa(idMsa);
        }
        // pega o id da pratica
        Long idPratica = bean.getPraticaMap().get(
                filter.getPratica().getNomePratica());
        // se a pratica existir
        filter.getPratica().setCodigoPratica(null);
        if (idPratica != null) {
            filter.getPratica().setCodigoPratica(idPratica);
        }

        // pega o cliente
        Cliente cliente = bean.getCliente();
        Long idCliente = bean.getClienteMap().get(cliente.getNomeCliente());
        cliente.setCodigoCliente(idCliente);

        // pega a natureza
        NaturezaCentroLucro natureza = bean.getNatureza();
        Long idNatureza = bean.getNaturezaMap().get(natureza.getNomeNatureza());
        natureza.setCodigoNatureza(idNatureza);

        // pega o centro-lucro
        CentroLucro cp = bean.getCentroLucro();
        Long idCentroLucro = bean.getCentroLucroMap().get(
                cp.getNomeCentroLucro());
        cp.setCodigoCentroLucro(idCentroLucro);

        GrupoCusto grupoCusto = bean.getGrupoCustoCombo().getMap()
                .get(bean.getFilterGrupoCusto());

        filter.setIndicadorWorkAtRisk(bean.getTo().getIndicadorWorkAtRisk());
        bean.setIndicadorWorkAtRisk(Constants.FLAG_YES.equals(bean.getTo().getIndicadorWorkAtRisk()) ? Boolean.TRUE : Boolean.FALSE);

		// realiza a busca pelo contrato pratica
		bean.setResultList(contratoPraticaService.findContratoPraticaByFilterFetch(
                filter, cliente, natureza, cp, grupoCusto, bean.getIndicadorWorkAtRiskFilter()));

		// se nenhum resultado encontrado
		if (bean.getResultList().isEmpty()) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

        // volta para a primeira pagina
        bean.setCurrentPageId(0);
    }

    /**
     * Seta o a lista do combobox com os CentroLucro de acordo com a natureza
     * escolhida.
     *
     * @param e - evento de mudan�a
     */
    public void findCentroLucroByNatureza(final ValueChangeEvent e) {

        String naturezaName = (String) e.getNewValue();

        if (naturezaName != null) {
            Long idNatureza = cpclBean.getNaturezacentroLucroMap().get(
                    naturezaName);

            NaturezaCentroLucro natureza = naturezaCentroLucroService
                    .findNaturezaCentroLucroById(idNatureza);

            this.loadCentroLucroList(centroLucroService
                    .findActiveByNatureza(natureza));

            cpclBean.resetTo();
        }

    }

    public void loadCombosApprover(ActionEvent event) {
        List<Pessoa> peopleList = pessoaService.findAllForComboBox();
        ComboBox<Pessoa> aprovador = ComboFactory.create(new PessoaCombo(
                peopleList));
    }

    public void loadCombosApproverMGR(ActionEvent event) {
        List<Pessoa> gerenteList = pessoaService.findAllManagerForComboBox();
        ComboBox<Pessoa> gerente = ComboFactory.create(new PessoaCombo(
                gerenteList));
    }

    /**
     * Carrega os combobox da tela.
     */
    private void loadCombos() {

        List<Moeda> allMoeda = moedaService.findMoedaAll();
        this.loadMoedaList(allMoeda);

        this.loadPraticaList(praticaService.findPraticaAll());

        this.loadTipoModeloNegocioList(tipoModeloNegocioService.findAllActive());

        ComboBox<GrupoCusto> comboGrupoCusto= ComboFactory
                .create(new GrupoCustoCombo(grupoCustoService.findGrupoCustoAllActive()));

       ComboBox<CentroLucro> comboCentroLucro = ComboFactory
                .create(new CentroLucroCombo(centroLucroService.findActiveByNatureza(naturezaCentroLucroService.findNaturezaCentroLucroById(NATUREZA_LOB))));

        ComboBox<CentroLucro> businessManagerCombo = ComboFactory
                .create(new CentroLucroCombo(centroLucroService.findActiveByNatureza(naturezaCentroLucroService.findNaturezaCentroLucroById(NATUREZA_BM))));

        ComboBox<CentroLucro> ssoCombo = ComboFactory
                .create(new CentroLucroCombo(centroLucroService.findActiveByNatureza(naturezaCentroLucroService.findNaturezaCentroLucroById(NATUREZA_SSO))));

        ComboBox<CentroLucro> ukmtCombo = ComboFactory
                .create(new CentroLucroCombo(centroLucroService.findActiveByNatureza(naturezaCentroLucroService.findNaturezaCentroLucroById(NATUREZA_UKMT))));

        ComboBox<String> comboAtivo = ComboFactory
                .create(new AtivoInativoCombo(new String[] { Constants.ACTIVE, Constants.INACTIVE, Constants.EXPAND }));

        ComboBox<String> requisicaoInativacaoControladoriaCombo = ComboFactory
                .create(new AtivoInativoCombo(new String[] { Constants.ACTIVE, Constants.INACTIVE, Constants.REQUEST_INACTIVATION_OLD, Constants.EXPAND }));

        ComboBox<String> requisicaoInativacaoGerenteCombo =  ComboFactory
                .create(new AtivoInativoCombo(new String[] { Constants.ACTIVE, Constants.REQUEST_INACTIVATION_OLD, Constants.EXPAND }));

        List<Pessoa> peopleList = pessoaService.findAllForComboBox();
        ComboBox<Pessoa> aprovador = ComboFactory.create(new PessoaCombo(
                peopleList));

        List<Pessoa> gerenteList = pessoaService.findAllManagerForComboBox();

        ComboBox<Pessoa> gerente = ComboFactory.create(new PessoaCombo(
                gerenteList));

        bean.setGrupoCustoCombo(comboGrupoCusto);
        bean.setCentroLucroCombo(comboCentroLucro);
        bean.setAtivoInativoCombo(comboAtivo);
        bean.setRequisicaoInativacaoControladoriaCombo(requisicaoInativacaoControladoriaCombo);
        bean.setRequisicaoInativacaoGerenteCombo(requisicaoInativacaoGerenteCombo);
        bean.setAprovadorCombo(aprovador);
        bean.setGerenteCombo(gerente);
        bean.setBusinessManagerCombo(businessManagerCombo);
        bean.setUkmtCombo(ukmtCombo);
        bean.setSsoCombo(ssoCombo);

        this.loadMsaList(msaService.findAllReturnCodigoAndNomeMsa());

        this.loadImpostoList(impostoService.findImpostoAll());

        this.loadNaturezaListForFilter(naturezaCentroLucroService
                .findNaturezaCentroLucroAll());

        this.loadClienteList(clienteService.findClienteAllClientePai());

        this.loadBusinessManagerList(pessoaService
                .findPessoaAllBusinessManager());
    }

    /**
     * Carrega os picklists.
     */
    private void loadPickLists() {
        this.findById(bean.getTo().getCodigoContratoPratica());
        // s� os deals acossiados ao MSA
        this.loadDealFiscalPickList(dealFiscalService
                .findDealFiscalByMsaActive(bean.getTo().getMsa()));
    }

    /**
     * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
     *
     * @param e - evento de mudan�a
     */
    public void prepareCentroLucroCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            Long codNatureza = bean.getNaturezaMap().get(value);
            NaturezaCentroLucro natureza = null;

            // se o centro de lucro existir
            if (codNatureza != null) {
                natureza = naturezaCentroLucroService
                        .findNaturezaCentroLucroById(codNatureza);
                this.loadCentroLucroListForFilter(centroLucroService
                        .findCentroLucroByNatureza(natureza));
                // senao existir cria uma lista vazia de centro-lucro
            } else {
                this.loadCentroLucroList(new ArrayList<CentroLucro>());
            }
        } else {
            this.loadCentroLucroList(centroLucroService.findCentroLucroAll());
        }
    }

    public void prepareCLProdCom(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        if (value != null && !value.isEmpty()) {

            ComboBox<CentroLucro> comboCentroLucro = ComboFactory
                    .create(new CentroLucroCombo(centroLucroService.findActiveByNatureza(naturezaCentroLucroService.findNaturezaCentroLucroById(NATUREZA_LOB))));
            bean.setCentroLucroCombo(comboCentroLucro);

            GrupoCusto grupoCusto = bean.getGrupoCustoCombo().getMap().get(value);
            Long tipoArea = grupoCustoService.findTipoAreaByCentroCusto(grupoCusto.getCodigoGrupoCusto());

            Map<String, ?> map = bean.getCentroLucroCombo().getMap();

            for (String chave : new ArrayList<>(map.keySet())) {
                if (tipoArea == 1) {
                    if (chave.equals("Sales")) {
                        map.remove(chave);
                    }
                } else if (tipoArea == 2 && !chave.equals("Sales")) {
                    map.remove(chave);
                }
            }
            comboCentroLucro.getList().clear();
            comboCentroLucro.getList().addAll(map.keySet());
            bean.setCentroLucroCombo(comboCentroLucro);
        }

    }

    /**
     * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
     *
     * @param e - evento de mudan�a
     */
    public void prepareContratoCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            Long codCli = bean.getClienteMap().get(value);
            Cliente cliente = null;

            // se o Clinte existir
            if (codCli != null) {
                cliente = clienteService.findClienteById(codCli);
                this.loadMsaList(msaService.findMsaByCliente(cliente));
                // senao existir cria uma lista vazia de centro-lucro
            } else {
                this.loadCentroLucroList(new ArrayList<CentroLucro>());
            }
        } else {
            this.loadMsaList(msaService.findMsaAll());
        }
    }

    /**
     * Verifica se o valor do atributo Cliente � valido. Se o valor n�o �
     * nulo e existe no clienteMap, ent�o o valor � valido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateCliente(final FacesContext context,
                                final UIComponent component, final Object value) {

        Long codigoCliente = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            codigoCliente = bean.getClienteMap().get(strValue);
            if (codigoCliente == null) {
                String label = (String) component.getAttributes().get(LABEL);
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * Verifica se o MSA � valido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateMsa(final FacesContext context,
                            final UIComponent component, final Object value) {

        Long contratoId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get(LABEL);

            contratoId = bean.getMsaComboMap().get(strValue);
            if (contratoId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (msaService.findMsaById(contratoId) == null) {
                    throw new ValidatorException(Messages.getMessageError(
                            Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
                }
            }
        }
    }

    /**
     * Verifica se o centro-lucro � valido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateCentroLucro(final FacesContext context,
                                    final UIComponent component, final Object value) {

        Long clId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get(LABEL);

            clId = cpclBean.getCentroLucroMap().get(strValue);
            if (clId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (centroLucroService.findCentroLucroById(clId) == null) {
                    throw new ValidatorException(Messages.getMessageError(
                            Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
                }
            }
        }
    }

    /**
     * Verifica se o centro-lucro � valido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateCentroLucroModal(final FacesContext context,
                                         final UIComponent component, final Object value) {

        Long clId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get(LABEL);

            clId = cpclBean.getCentroLucroMapModal().get(strValue);
            if (clId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (centroLucroService.findCentroLucroById(clId) == null) {
                    throw new ValidatorException(Messages.getMessageError(
                            Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
                }
            }
        }
    }

    /**
     * Verifica se a pratica � valida.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validatePratica(final FacesContext context,
                                final UIComponent component, final Object value) {

        Long praticaId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get(LABEL);

            praticaId = bean.getPraticaMap().get(strValue);
            if (praticaId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (praticaService.findPraticaById(praticaId) == null) {
                    throw new ValidatorException(Messages.getMessageError(
                            Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
                }
            }
        }
    }

    /**
     * Verifica se a natureza eh valida.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateNaturezaCentroLucro(final FacesContext context,
                                            final UIComponent component, final Object value) {

        Long naturezaId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            naturezaId = cpclBean.getNaturezacentroLucroMap().get(strValue);

            String label = (String) component.getAttributes().get(LABEL);
            if (naturezaId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (naturezaCentroLucroService
                        .findNaturezaCentroLucroById(naturezaId) == null) {
                    throw new ValidatorException(Messages.getMessageError(
                            Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
                }
            }
        }
    }

    /**
     * Verifica se o imposto � valido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateImposto(final FacesContext context,
                                final UIComponent component, final Object value) {

        Long impostoId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get(LABEL);

            impostoId = impostoBean.getImpostoMap().get(strValue);

            if (impostoId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (impostoService.findImpostoById(impostoId) == null) {
                    throw new ValidatorException(
                            Messages.getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND));
                }
            }
        }
    }

    /**
     * Popula a lista de praticas para combobox.
     *
     * @param praticas lista de praticas.
     */
    private void loadPraticaList(final List<Pratica> praticas) {

        Map<String, Long> praticaMap = new HashMap<String, Long>();
        List<String> praticaList = new ArrayList<String>();

        for (Pratica pratica : praticas) {
            praticaMap
                    .put(pratica.getNomePratica(), pratica.getCodigoPratica());
            praticaList.add(pratica.getNomePratica());
        }

        bean.setPraticaMap(praticaMap);
        bean.setPraticaList(praticaList);
    }

    /**
     * Popula a lista de tipo modelo negocio para combobox.
     *
     * @param tipoModeloNegocios lista de praticas.
     */
    private void loadTipoModeloNegocioList(final List<TipoModeloNegocio> tipoModeloNegocios) {

        Map<String, Long> tipoModeloNegocioMap = new HashMap<String, Long>();
        List<String> tipoModeloNegocioList = new ArrayList<String>();

        for (TipoModeloNegocio tipoModeloNegocio : tipoModeloNegocios) {
            tipoModeloNegocioMap.put(tipoModeloNegocio.getNomeTipoModeloNegocio(), tipoModeloNegocio.getCodigoTipoModeloNegocio());
            tipoModeloNegocioList.add(tipoModeloNegocio.getNomeTipoModeloNegocio());
        }

        bean.setTipoModeloNegocioMap(tipoModeloNegocioMap);
        bean.setTipoModeloNegocioList(tipoModeloNegocioList);
    }

    /**
     * Popula a lista de praticas para combobox.
     *
     * @param pessoas lista de praticas.
     */

    private void loadBusinessManagerList(final List<Pessoa> pessoas) {

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
     * Popula a lista de moedas para combobox.
     *
     * @param moedas lista de moedas.
     */
    private void loadMoedaList(final List<Moeda> moedas) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        for (Moeda pratica : moedas) {
            moedaMap.put(pratica.getNomeMoeda(), pratica.getCodigoMoeda());
            moedaList.add(pratica.getNomeMoeda());
        }

        bean.setMoedaMap(moedaMap);
        bean.setMoedaList(moedaList);
    }

    /**
     * Popula a lista de Msa para combobox.
     *
     * @param msas lista de contratos.
     */
    private void loadMsaList(final List<Msa> msas) {

        Map<String, Long> msaMap = new HashMap<String, Long>();
        List<String> msaList = new ArrayList<String>();

        for (Msa msa : msas) {
            msaMap.put(msa.getNomeMsa(), msa.getCodigoMsa());
            msaList.add(msa.getNomeMsa());
        }

        bean.setMsaCombo(msaList);
        bean.setMsaComboMap(msaMap);
    }

    /**
     * Popula a lista de centroLucros para combobox.
     *
     * @param centroLucros lista de contratos.
     */
    private void loadCentroLucroList(final List<CentroLucro> centroLucros) {

        Map<String, Long> centroLucroMap = new HashMap<String, Long>();
        List<String> centroLucroList = new ArrayList<String>();

        for (CentroLucro centroLucro : centroLucros) {
            centroLucroMap.put(centroLucro.getNomeCentroLucro(),
                    centroLucro.getCodigoCentroLucro());
            centroLucroList.add(centroLucro.getNomeCentroLucro());
        }

        cpclBean.setCentroLucroMap(centroLucroMap);
        cpclBean.setCentroLucroList(centroLucroList);
    }

    /**
     * Popula a lista de centroLucros para combobox - Modal Add.
     *
     * @param centroLucros lista de contratos.
     */
    private void loadCentroLucroListModal(final List<CentroLucro> centroLucros) {

        Map<String, Long> centroLucroMapModal = new HashMap<String, Long>();
        List<String> centroLucroListModal = new ArrayList<String>();

        for (CentroLucro centroLucro : centroLucros) {
            centroLucroMapModal.put(centroLucro.getNomeCentroLucro(),
                    centroLucro.getCodigoCentroLucro());
            centroLucroListModal.add(centroLucro.getNomeCentroLucro());
        }

        cpclBean.setCentroLucroMapModal(centroLucroMapModal);

        Collections.sort(centroLucroListModal);

        cpclBean.setCentroLucroListModal(centroLucroListModal);
    }

    /**
     * Popula a lista de centroLucros para combobox.
     *
     * @param centroLucros lista de contratos.
     */
    private void loadCentroLucroListForFilter(
            final List<CentroLucro> centroLucros) {

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
     * @param naturezas lista de NaturezaCentroLucro.
     */
    private void loadNaturezaList(final List<NaturezaCentroLucro> naturezas) {

        Map<String, Long> naturezaMap = new HashMap<String, Long>();
        List<String> naturezaList = new ArrayList<String>();

        for (NaturezaCentroLucro natureza : naturezas) {
            naturezaMap.put(natureza.getNomeNatureza(),
                    natureza.getCodigoNatureza());
            naturezaList.add(natureza.getNomeNatureza());
        }

        cpclBean.setNaturezacentroLucroMap(naturezaMap);
        cpclBean.setNaturezacentroLucroList(naturezaList);
    }

    /**
     * Popula a lista de Natureza para combobox.
     *
     * @param naturezas lista de NaturezaCentroLucro.
     */
    private void loadNaturezaListForFilter(
            final List<NaturezaCentroLucro> naturezas) {

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
     * @param clientes lista de clientes.
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
     * Carrega os conteudo de uma mapa de alocAcao (MapaAlocacao, Alocacao,
     * AlocacaoPeriodo), para exibir na tela.
     */
    private void loadContratoPraticaView() {

        ContratoPratica contratoPratica = contratoPraticaService
                .findContratoPraticaById(bean.getTo()
                        .getCodigoContratoPratica());

        // popula o to na tela para ser exibido
        bean.setTo(contratoPratica);

        // popula a lista de Centro-Lucro
        bean.setNaturezaCPCLRowList(cpclService
                .prepareNaturezaContratoPraticaCLList(contratoPratica));

        // popula a lista de DealFiscal do contrato-pratica
        bean.setDealFiscalList(contratoPratica.getAllActivesDealFiscals());
		bean.setIndicadorMultiFiscalDeal(contratoPratica.getIndicadorMultiDealFiscal().equals(Constants.YES) ? Boolean.TRUE : Boolean.FALSE);

        bean.setIndicadorWorkAtRisk(Constants.FLAG_YES.equals(contratoPratica.getIndicadorWorkAtRisk()) ? Boolean.TRUE : Boolean.FALSE);

		this.setSelectedDealFiscalPickList();this.prepareTabelaImposto();
        loadSelectedItems(bean.getTo());
    }

    /**
     * Popula a lista de Imposto para combobox.
     *
     * @param impostos lista de Imposto.
     */
    private void loadImpostoList(final List<Imposto> impostos) {

        Map<String, Long> impostoMap = new HashMap<String, Long>();
        List<String> impostoList = new ArrayList<String>();

        for (Imposto imposto : impostos) {
            impostoMap
                    .put(imposto.getNomeImposto(), imposto.getCodigoImposto());
            impostoList.add(imposto.getNomeImposto());
        }

        impostoBean.setImpostoMap(impostoMap);
        impostoBean.setImpostoList(impostoList);
    }

    /**
     * @return the cpclBean
     */
    public ContratoPraticaCentroLucroBean getCpclBean() {
        return cpclBean;
    }

    /**
     * @param cpclBean the cpclBean to set
     */
    public void setCpclBean(final ContratoPraticaCentroLucroBean cpclBean) {
        this.cpclBean = cpclBean;
    }

    /**
     * Realiza a validacao do campo Login.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validatePessoa(final FacesContext context,
                               final UIComponent component, final Object value) {

        String login = (String) value;

        if ((login != null) && (!"".equals(login))) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(login);

            if (pessoa == null) {
                String label = (String) component.getAttributes().get(LABEL);
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }

    }

    /**
     * Acao utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
     *
     * @param value - valor (login) utilizado na busca das Pessoas
     * @return retorna uma lista de recurso
     */
    public List<Pessoa> autoCompletePessoa(final Object value) {
        return pessoaService.findPessoaByLikeLogin((String) value);
    }

    /**
     * Preenche o picklist de {@link DealFiscal} (aba configuration).
     *
     * @param dealFiscalList lista de Discal Deal
     */
    private void loadDealFiscalPickList(final List<DealFiscal> dealFiscalList) {

        // obtem todas os DealFiscals
        List<SelectItem> items = new ArrayList<SelectItem>();

        for (DealFiscal dealFiscal : dealFiscalList) {
            if (dealFiscal.getIndicadorStatus().equals("A")) {
                items.add(new SelectItem(dealFiscal.getCodigoDealFiscal()
                        .toString(), dealFiscal.getNomeDealFiscal()));
            }
        }

        bean.setDealFiscalPickList(items);

		// obtem as DealFiscal selecionadas
		if (bean.getIsUpdate()) {
			setSelectedDealFiscalPickList();
		}
	}

	private void setSelectedDealFiscalPickList() {

		if (bean.getTo().getCodigoContratoPratica() != null) {
			List<DealFiscal> lista = dealFiscalService.findDealFiscalByContratoPraticaAndActive(bean.getTo());

            String[] selecionadas = new String[lista.size()];

            // popula o array com os deal fiscal do contrato pratica
            for (int i = 0; i < selecionadas.length; i++) {
                selecionadas[i] = String.valueOf(lista.get(i)
                        .getCodigoDealFiscal());
            }
            bean.setSelectedDealFiscalPickList(selecionadas);
        }
    }

    /**
     * Atualiza a lista de {@link DealFiscal} selecionadas no picklist do
     * {@link ContratoPratica}.
     */
    /**
     * Atualiza a lista de {@link DealFiscal} selecionadas no picklist do
     * {@link ContratoPratica}.
     */
    private void updateContratoPraticaDealFiscal() {

        ContratoPratica contratoPratica = contratoPraticaService
                .findContratoPraticaById(bean.getTo()
                        .getCodigoContratoPratica());
        if (contratoPratica == null) {
            Messages.showError(UPDATE_CONTRATO_PRATICA_DEAL_FISCAL,
                    Constants.DEFAULT_MSG_ERROR_NOT_FOUND, new Object[]{"Contract-LOB"});
            return;
        }

        contratoPratica.setIndicadorMultiDealFiscal(Boolean.TRUE.equals(bean.getIndicadorMultiFiscalDeal()) ? Constants.YES : Constants.NO);

        List<CpraticaDfiscal> cpraticaDfiscals = new ArrayList<CpraticaDfiscal>();
        List<DealFiscal> dealFiscals = new ArrayList<DealFiscal>();

        // Percorre os itens selecionados e monta uma lista de
        // CpraticaDfiscal
        for (String codigoItem : bean.getSelectedDealFiscalPickList()) {
            DealFiscal df = dealFiscalService.findDealFiscalById(Long
                    .valueOf(codigoItem));
            if (!df.getIndicadorStatus().equals("A")) {
                Messages.showError(UPDATE_CONTRATO_PRATICA_DEAL_FISCAL, INACTIVE_FISCAL_DEAL_ASSOCIATION_MSG);
                return;
            }
            dealFiscals.add(df);
        }

        for (DealFiscal dealFiscal : dealFiscals) {
            CpraticaDfiscalId cpmId = new CpraticaDfiscalId(
                    contratoPratica.getCodigoContratoPratica(),
                    dealFiscal.getCodigoDealFiscal());
            CpraticaDfiscal cpm = new CpraticaDfiscal(cpmId,
                    contratoPratica, dealFiscal);
            cpraticaDfiscals.add(cpm);
        }


        this.loadDealFiscalValidations();
        for (DealFiscalValidation validation : validations) {
            try {
                validation.validate(contratoPratica, dealFiscals);
            } catch (BusinessException e) {
                Messages.showErrorWithoutBundle(UPDATE_CONTRATO_PRATICA_DEAL_FISCAL, e.getMessage());
                return;
            }
        }

        contratoPraticaService.updateCpraticaDfiscal(contratoPratica,cpraticaDfiscals);
        // repopula o bean
        this.findById(contratoPratica.getCodigoContratoPratica());

        sendMapToRecalculateByClob();

        Messages.showSucess("saveConfigurationTab",
                    Constants.DEFAULT_MSG_SUCCESS_SAVE,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
	}

    private void sendMapToRecalculateByClob(){
        try {
            MapaAlocacao mapaAlocacao = mapaAlocacaoService.findMapaAlocacaoByContratoPratica(bean.getTo());
            RecalculateMessageDTO message = RecalculateMessageDTO.builder()
                    .mapCode(mapaAlocacao.getCodigoMapaAlocacao())
                    .retryCount(0)
                    .build();
            this.recalculateMapProducer.send(new GsonBuilder().create().toJson(message));
        } catch (Exception e) {
            Messages.showWarning(UPDATE_CONTRATO_PRATICA_DEAL_FISCAL, e.getMessage());
        }
    }

    /**
     * Salva as alteracoes realizadas na aba Configuration.
     */
    public void saveConfigurationTab() {
        // persiste os DealFiscal settadas no picklist do contrato pratica
        this.updateContratoPraticaDealFiscal();
    }

	/**
	 * Prepara a tela para edit um ContratoPratica.
	 * 
	 * @return retorna a pagina de Edit do ContratoPratica
	 */
	public String prepareEdit() {
		this.loadCombos();
		bean.setIsUpdate(Boolean.TRUE);

		Long codigoContratoPratica = bean.getTo().getCodigoContratoPratica();
		this.findById(codigoContratoPratica);

        Convergencia convergencia = convergenciaService
                .findByContratoPraticaId(codigoContratoPratica);

        bean.setIsCostCenterComboboxEditable(convergencia.isNull());

        loadSelectedItems(bean.getTo());
        bean.setShowUpdateMapaAlocacaoModal(Boolean.FALSE);
        return OUTCOME_CONTRATO_PRATICA_EDIT;
    }

    public String prepareSaveContratoPratica() {
        ContratoPratica cp = bean.getTo();
        MapaAlocacao mapaAlocacao = mapaAlocacaoService.findMapaAlocacaoByContratoPratica(cp);

        // Se o mapa de alocação existe, insere os registros de ForecastRevenueStatusProject no banco de dados
        if (mapaAlocacao == null) {
            return saveContratoPratica();
        }

        ContratoPratica cpSaved = contratoPraticaService.findContratoPraticaById(cp.getCodigoContratoPratica());

        Boolean indicadorAtivoChangedToActive = Constants.ACTIVE.equals(cp.getIndicadorAtivo()) && !cp.getIndicadorAtivo().equals(cpSaved.getIndicadorAtivo());
        Boolean indicadorAtivoChangedToExpand = Constants.EXPAND.equals(cp.getIndicadorAtivo()) && !cp.getIndicadorAtivo().equals(cpSaved.getIndicadorAtivo());

        if (indicadorAtivoChangedToExpand) {
            Messages.showError(OUTCOME_CONTRATO_PRATICA_CONFIGURE, INVALID_STATUS_CHANGE_TO_EXPAND);
            return null;
        }
        if (!indicadorAtivoChangedToActive) return saveContratoPratica();

        bean.setShowUpdateMapaAlocacaoModal(Boolean.TRUE);
        return null; // faz nova request, renderiza HTML e mantém bean vivo por causa do keepAlive
    }

    public String createOrUpdateForecastRevenueStatusProject() {
        ContratoPratica cp = bean.getTo();
        MapaAlocacao mapaAlocacao = mapaAlocacaoService.findMapaAlocacaoByContratoPratica(cp);

        Date closingDate = moduloService.getClosingDateMapaAlocacao();
        Date currentMonth = DateUtil.addMonths(closingDate, 1);
        boolean revenueDateBeforeMapaAlocacaoEndDate = DateUtil.after(currentMonth, mapaAlocacao.getDataFim());

        // Enquanto a data da receita não for superior à data do fim do mapa de alocação
        while (!revenueDateBeforeMapaAlocacaoEndDate) {

            ForecastRevenueStatus revenueStatus = forecastRevenueStatusService.findById(ForecastRevenueStatusEnum.getCodeByAcronym(cp.getIndicadorAtivo()));

            ForecastRevenueStatusProjectId forecastRevenueStatusProjectId = new ForecastRevenueStatusProjectId(mapaAlocacao, currentMonth);
            ForecastRevenueStatusProject forecastRevenueStatusProject = new ForecastRevenueStatusProject();
            forecastRevenueStatusProject.setId(forecastRevenueStatusProjectId);
            forecastRevenueStatusProject.setContratoPratica(mapaAlocacao.getContratoPratica());
            forecastRevenueStatusProject.setRevenueStatusCode(revenueStatus);

            ForecastRevenueStatusProject newForecastRevenueStatusProject = forecastRevenueStatusProjectService.createOrUpdate(forecastRevenueStatusProject);
            log.info("ForecastRevenueStatusProject saved: " + newForecastRevenueStatusProject);

            currentMonth = DateUtil.addMonths(currentMonth, 1);
            revenueDateBeforeMapaAlocacaoEndDate = DateUtil.after(currentMonth, mapaAlocacao.getDataFim());
        }

        return saveContratoPratica();
    }

    /**
     * Realiza a validacao do contrato pratica e salva.
     *
     * @return pagina
     */
    public String saveContratoPratica() {

        ContratoPratica cp = bean.getTo();

        if (checkStatusExpandAndWorkAtRisk(bean.getTo().getIndicadorAtivo(), bean.getIndicadorWorkAtRisk())) {
            Messages.showError(CONTRATO_PRATICA_NEXT, INVALID_STATUS_EXPAND_WITH_WORK_AT_RISK);
            return null;
        }

        cp.setIndicadorDeleteLogico(Constants.NO);

        Long msaId = bean.getMsaComboMap().get(bean.getMsaSelected());
        Msa msa = msaService.findMsaById(msaId);

        Pratica pratica = cp.getPratica();
        pratica = praticaService.findPraticaById(bean.getPraticaMap().get(
                pratica.getNomePratica()));

        TipoModeloNegocio tmn = new TipoModeloNegocio();
        tmn.setCodigoTipoModeloNegocio(bean.getTipoModeloNegocioMap().get(bean.getTipoModeloNegocioSelected()));

        String nameCLob = this.composesClobName(cp.getMsa().getNomeMsa(),
                cp.getNomeCompound(), pratica.getSiglaPratica());

        ContratoPratica cpAux = contratoPraticaService
                .findContratoPraticaByName(nameCLob);

        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(new Date());
        ContratoPraticaCentroLucro contratoPraticaCentroLucro = buscarProfitCenterVigenteDoTipo(cp, NATUREZA_LOB);

        if(contratoPraticaCentroLucro == null) {
            Messages.showError(CONTRATO_PRATICA_NEXT,
                    NOT_FOUND_CURRENT_PROFIT_CENTER_LOB,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
            return null;
        }
        if (!contratoPraticaCentroLucro.getCentroLucro().getNomeCentroLucro().equalsIgnoreCase(cp.getPratica().getNomePratica())) {
            Messages.showError(CONTRATO_PRATICA_NEXT,
                    LOB_CONTRACT_PRATICA_NOT_MATCH_PROFIT_CENTER,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
            return null;
        }

        GrupoCusto grupoCusto = bean.getGrupoCustoCombo().getSelectedItem();
        cp.setMsa(msa);
        cp.setAprovador(bean.getAprovadorCombo().getSelectedItem());
        cp.setGerenteAprovador(bean.getGerenteCombo().getSelectedItem());
        cp.setTipoModeloNegocio(tmn);
        if (!bean.getIndicadorReembolsavel().equalsIgnoreCase("")) {
            cp.setIndicadorReembolsavel(bean.getIndicadorReembolsavel().equalsIgnoreCase("NR") ? "N" : "Y");
        }
        cp.setIndicadorWorkAtRisk(bean.getIndicadorWorkAtRisk() ? Constants.FLAG_YES : Constants.FLAG_NO);

        if (cpAux != null && cpAux.getCodigoContratoPratica().compareTo(cp.getCodigoContratoPratica()) != 0) {

            Messages.showWarning(CONTRATO_PRATICA_NEXT,
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);

            // interrompe o processo e mantem na mesma pagina
            return null;
        }

        cp.setNomeContratoPratica(this.composesClobName(cp.getMsa()
                .getNomeMsa(), cp.getNomeCompound(), pratica
                .getSiglaPratica()));
        cp.setMsa(msa);
        cp.setPratica(pratica);

        String cpStatus = contratoPraticaService.findContratoPraticaById(cp.getCodigoContratoPratica()).getIndicadorAtivo();

        if(cp.getGerenteAprovador() == null){
            Messages.showError("update", Constants.CONTRATO_PRATICA_APPROVER_MANAGER_NULL);
            return null;
        }

        if(cp.getAprovador() == null){
            Messages.showError("update", Constants.CONTRATO_PRATICA_MANAGER_NULL);
            return null;
        }

        try {
            contratoPraticaService.updateContratoPraticaWithConvergencia(
                    cp, grupoCusto);
        } catch (MoreThanOneActiveEntityException e) {
            log.warn(e.getMessage());
            Messages.showError(CONTRATO_PRATICA_NEXT, e.getMessage(),
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
        }

        boolean fromRequestInactivationToInactive = !cpStatus.equals(Constants.REQUEST_INACTIVATION_OLD) && cp.getIndicadorAtivo().equals(Constants.REQUEST_INACTIVATION_OLD);

        if (fromRequestInactivationToInactive) {
            Messages.showSucess(CONTRATO_PRATICA_NEXT,
                    Constants.MSG_SUCCESS_REQUEST_INACTIVATION,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
        } else {
            Messages.showSucess(CONTRATO_PRATICA_NEXT,
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA);
        }

        bean.reset();

        // Exibe a tela de configure (edicao de abas abilitada)
        return OUTCOME_CONTRATO_PRATICA_RESEARCH;
    }

    /**
     * Busca o Profit Center vigente do tipo especificado para o contrato prática.
     *
     * Se houver apenas um Profit Center do tipo desejado, ele é considerado vigente.
     * Caso contrário, verifica as datas de vigência para determinar qual é o vigente.
     *
     * @param cp   Contrato prática para o qual buscar o Profit Center vigente.
     * @param tipo Código do tipo de Profit Center desejado.
     * @return ContratoPraticaCentroLucro - O Profit Center vigente do tipo especificado.
     */
    public ContratoPraticaCentroLucro buscarProfitCenterVigenteDoTipo(ContratoPratica cp, Long tipo) {
        Date dataAtual = new Date();
        ContratoPraticaCentroLucro profitCenterVigente = null;

        List<ContratoPraticaCentroLucro> listCpcl = cpclService.findCPCLByContratoPratica(cp);

        if (listCpcl == null || listCpcl.isEmpty()) {return null;}

        List<ContratoPraticaCentroLucro> profitCentersDoTipo = new ArrayList<>();

        for (ContratoPraticaCentroLucro cpcl : listCpcl) {
            if (cpcl.getCentroLucro().getNaturezaCentroLucro().getCodigoNatureza().equals(tipo)) {
                profitCentersDoTipo.add(cpcl);
            }
        }

        if (profitCentersDoTipo.isEmpty()) {return null;}

        if (profitCentersDoTipo.size() == 1) {
            return profitCentersDoTipo.get(0);
        }

        for (ContratoPraticaCentroLucro cpcl : profitCentersDoTipo) {
            if (cpcl.getDataInicioVigencia() != null && cpcl.getDataInicioVigencia().before(dataAtual) &&
                    (cpcl.getDataFimVigencia() == null || cpcl.getDataFimVigencia().compareTo(dataAtual) >= 0)) {
                profitCenterVigente = cpcl;
                break;
            }
        }

        return profitCenterVigente;
    }

    public void updateNomeEmpresa(ValueChangeEvent e) {

        ContratoPratica cp = bean.getTo();
        Msa msa = cp.getMsa();
        Pratica pratica = cp.getPratica();
        String nomeCompound = cp.getNomeCompound() == null ? "" : cp
                .getNomeCompound();
        String nomeMsa = msa.getNomeMsa() == null ? "" : msa.getNomeMsa();
        String sgPratica = pratica.getSiglaPratica() == null ? "" : pratica
                .getSiglaPratica();

        String id = e.getComponent().getId();
        String newValue = e.getNewValue().toString();
          if ("msa".equals(id)) {
            nomeMsa = newValue;
            msa.setNomeMsa(nomeMsa);
        } else if ("compoundNames".contains(id)) {
            nomeCompound = newValue;
            cp.setNomeCompound(nomeCompound);
        }else if ("centroLucro".equals(id)) {
            pratBean.getFilter().setNomePratica(newValue);
            pratBean.getFilter().setIndicadorAtivo(ATIVO);
            Optional<Pratica> lob =  praticaService.findPraticaByFilter(pratBean.getFilter()).stream().findFirst();
            if (lob.isPresent()) {
                this.bean.getTo().setPratica( lob.get() );
                sgPratica = bean.getTo().getPratica().getSiglaPratica();
            }else{
                sgPratica = "";
            }
        } else {
            Long idPratica = bean.getPraticaMap().get(newValue);
            if (idPratica != null) {
                Pratica lob = praticaService.findPraticaById(idPratica);
                sgPratica = lob.getSiglaPratica();
            } else {
                sgPratica = "";
            }
            pratica.setSiglaPratica(sgPratica);
        }

        cp.setNomeContratoPratica(this.composesClobName(nomeMsa, nomeCompound,
                sgPratica));
    }

    public void updateIndicadorAtivo(ValueChangeEvent e) {
        ContratoPratica cp = bean.getTo();
        String newIndicadorAtivo = e.getNewValue().toString();
        cp.setIndicadorAtivo(newIndicadorAtivo);

        if (Constants.EXPAND.equals(newIndicadorAtivo)) {
            bean.setIndicadorWorkAtRisk(Boolean.FALSE);
            bean.getTo().setIndicadorWorkAtRisk(Constants.FLAG_NO);
        }
    }

    public void prepareCreateContratoPraticaGrupoCusto() {
        ComboBox<GrupoCusto> comboGrupoCusto = ComboFactory
                .create(new GrupoCustoCombo(grupoCustoService.findGrupoCustoActiveProdCom()));
        bean.setGrupoCustoCombo(comboGrupoCusto);
        bean.getCentroLucroCombo().getList().clear();

        messageControlBean.setDisplayMessages(Boolean.FALSE);
    }

    public void createContratoPraticaGrupoCusto() {
        ContratoPraticaGrupoCusto cpgc = cpgcBean.getTo();
        Date startDate = null;

        String[] dateFormat = {DATE_FORMAT};
        try {
            startDate = DateUtils.parseDate(cpgcBean.getMesInicioVigencia()
                    + "/" + cpgcBean.getAnoInicioVigencia(), dateFormat);

            // verifica o History Lock. Se startDate nao for valido, exibe
            // mensagem de erro
            if (!moduloService.verifyHistoryLock(startDate, Boolean.TRUE, Boolean.FALSE)) {
                return;
            }

            cpgc.setDataInicioVigencia(startDate);
            cpgc.setContratoPratica(bean.getTo());
            cpgc.setGrupoCusto(bean.getGrupoCustoCombo().getSelectedItem());

            contratoPraticaGrupoCustoService.create(cpgc);

            cpgcBean.reset();

            bean.setTo(contratoPraticaService.findContratoPraticaById(bean
                    .getTo().getCodigoContratoPratica()));

            prepareConfigure();

        } catch (ParseException e) {
            e.printStackTrace();
            Messages.showError(CREATE_TABELA_PRECO,
                    Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
        }
    }

    public void removeContratoPraticaGrupoCusto() {

        ContratoPraticaGrupoCusto cpgcToDelete = contratoPraticaGrupoCustoService
                .findById(cpgcBean.getTo().getCodigoContratoPraticaGrupoCusto());

        if(cpgcToDelete.getDataFimVigencia() != null){
            Messages.showError("removeContratoPraticaGrupoCusto",
                    CANNOT_REMOVE_COST_CENTER);
            return;

        }

        contratoPraticaGrupoCustoService.remove(cpgcToDelete);

        cpgcBean.resetTo();

        bean.setTo(contratoPraticaService.findContratoPraticaById(bean.getTo()
                .getCodigoContratoPratica()));

        List<ContratoPraticaGrupoCusto> cpgcList = contratoPraticaGrupoCustoService
                .findByContratoPraticaId(bean.getTo()
                        .getCodigoContratoPratica());
        bean.getTo().setContratoPraticaGrupoCustos(cpgcList);

        if (!cpgcList.isEmpty()) {

            loadSelectedItems(bean.getTo());

        }
    }

    public void preparePraticaContratoHistory() {
        List<ContratoPraticaAud> auditors = contratoPraticaService
                .findHistoryByCodigo(bean.getTo().getCodigoContratoPratica());
        bean.setHistoryList(auditors);
    }

    public void prepareSelectedDealFiscal() {
        loadPickLists();
    }

    /**
     * Prepara a tela de load do arquivo.
     *
     * @return retorna a página de upload.
     */
    public String prepareUpload() {
        bean.reset();
        NaturezaCentroLucro naturezaFilter = new NaturezaCentroLucro();
        naturezaFilter.setIndicadorAtivo(Constants.ACTIVE);
        naturezaFilter.setCodigoNatureza(3L);
        bean.setCentroLucroList(centroLucroService.findNameActiveByNatureza(naturezaFilter));
        bean.setErrorEntryList(new ArrayList<>());
        return OUTCOME_CONTRATO_PRATICA_UPLOAD;
    }

    /**
     * Listener para o upload de arquivo.
     *
     * @param event evento do upload
     * @throws Exception lança uma exception
     */
    public void uploadContratoPraticaListener(final UploadEvent event) throws Exception {

        UploadItem item = event.getUploadItem();
        bean.setNomeArquivoUpload(item.getFileName());
        Map<String, List<String>> map = contratoPraticaService.uploadContratoPratica(item, bean.getCentroLucroList());


        if(map != null){
            bean.setMapUploadError(map);
            bean.setErrorEntryList(new ArrayList<>(bean.getMapUploadError().entrySet()));
        }else{
            bean.setErrorEntryList(null);
        }
    }

	public boolean checkStatusExpandAndWorkAtRisk(String indicadorAtivo, Boolean indicadorWorkAtRisk) {
		return Constants.EXPAND.equals(indicadorAtivo) && Boolean.TRUE.equals(indicadorWorkAtRisk);
	}
    public boolean isDataPrivacyRendered(){
        if(StringUtils.isEmpty(bean.getTo().getDataPrivacy()) ||
                bean.getTo().getDataPrivacy().equals(systemProperties.getProperty(Constants.DATA_PRIVACY_INITIAL)))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    public String getDataPrivacyLink(){
        if(StringUtils.isEmpty(bean.getTo().getDataPrivacy()))
            return StringUtils.EMPTY;

        return systemProperties.getProperty(Constants.DATA_PRIVACY_LINK).concat(bean.getTo().getDataPrivacy());
    }
}
