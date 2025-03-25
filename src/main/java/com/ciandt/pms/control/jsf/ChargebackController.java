package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IChargebackCPService;
import com.ciandt.pms.business.service.IChargebackPessService;
import com.ciandt.pms.business.service.IChbackPessLogSincService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IConvergenciaService;
import com.ciandt.pms.business.service.ICustoTiRecursoService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IInvoiceMegaService;
import com.ciandt.pms.business.service.ILicencaSwDetailService;
import com.ciandt.pms.business.service.ILicencaSwProjetoParcelaService;
import com.ciandt.pms.business.service.ILicencaSwProjetoPessoaService;
import com.ciandt.pms.business.service.ILicencaSwProjetoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.ITiRecursoService;
import com.ciandt.pms.business.service.IVwChbackSoftwareService;
import com.ciandt.pms.control.jsf.bean.ChargebackBean;
import com.ciandt.pms.control.jsf.bean.IntegrarLicencaSoftwareBean;
import com.ciandt.pms.control.jsf.components.impl.InvoiceMegaSelect;
import com.ciandt.pms.control.jsf.components.impl.InvoiceProjectMegaSelect;
import com.ciandt.pms.control.jsf.interfaces.chargeback.ChargebackWorksheetHandler;
import com.ciandt.pms.control.jsf.interfaces.chargeback.IChargebackWorksheet;
import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.message.dto.InvoiceMegaDTO;
import com.ciandt.pms.message.dto.InvoiceProjectMegaDTO;
import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ChargebackPessoa;
import com.ciandt.pms.model.ChbackPessLogSincronizacao;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoParcela;
import com.ciandt.pms.model.LicencaSwProjetoPessoa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.VwChbackSoftware;
import com.ciandt.pms.model.VwChbackSoftwareId;
import com.ciandt.pms.model.vo.ChargebackRow;
import com.ciandt.pms.model.vo.LicencaSwDetail;
import com.ciandt.pms.model.vo.LicencaSwProjetoRow;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;
import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import static org.joda.time.LocalDate.now;


/**
 * 
 * A classe ChargebackController proporciona as funcionalidades da camada de
 * apresenta��o referente ao servi�os de Chargeback.
 * 
 * @since 16/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"ICB.SOFTWARE:VW", "ICB.SOFTWARE:CR", "ICB.HARDWARE:VW"})
public class ChargebackController {

    /* Logger */
    private static Logger logger = LogManager.getLogger(ChargebackController.class.getName());

    /** Instancias dos Servi�os */

    /** Instancia do Servi�o de ChargebackContratoPratica. */
    @Autowired
    private IChargebackCPService chbackCPService;

    /** Instancia do Servi�o de ChargebackContratoPessoa. */
    @Autowired
    private IChargebackPessService chbackPessService;

    /** Instancia do Servi�o de TiRecursoService. */
    @Autowired
    private ITiRecursoService tiRecursoService;

    @Autowired
    private ICustoTiRecursoService custoTiRecursoService;

    /** Instancia do Servi�o de ContratoPraticaService. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    @Autowired
    private IMoedaService moedaService;

    @Autowired
    private IMsaService msaService;

    /** Instancia do servico Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico VwChbackSoftware. */
    @Autowired
    private IVwChbackSoftwareService vwChbackSoftService;

    /** Inst�ncia do servi�o de ChbackPessLogSincronizacao. */
    @Autowired
    private IChbackPessLogSincService chbackPessLogSincService;

    @Autowired
    private ILicencaSwProjetoService licencaSwProjetoService;

    @Autowired
    private ILicencaSwProjetoParcelaService licencaSwProjetoParcelaService;

    @Autowired
    private ILicencaSwDetailService licencaSwDetailService;

    @Autowired
    private ILicencaSwProjetoPessoaService licencaSwProjetoPessoaService;

    @Autowired
    private IRecursoService recursoService;

    @Autowired
    private IGrupoCustoService grupoCustoService;

    @Autowired
    private IInvoiceMegaService invoiceService;

    @Autowired
    private IEmpresaService empresaService;

    @Autowired
    private IConvergenciaService convergenciaService;

    @Autowired
    private IModuloService moduloService;


    /*****************************************/

    /** Instancia do bean Chargeback. */
    @Autowired
    private ChargebackBean bean = null;

    @Autowired
    private IntegrarLicencaSoftwareBean integrarLicencaSoftwareBean;

    /*****************************************/

    @Autowired
    private ChargebackWorksheetHandler worksheetHandler;

    /** outcome tela gerenciamento da entidade. */
    private static final String OUTCOME_MANAGE_PER_ITRESOURCE =
            "chargeback_manage_per_itresource";

    /** outcome tela gerenciamento da entidade. */
    private static final String OUTCOME_MANAGE_PER_CONTRACT =
            "chargeback_manage_per_contract";

    /** outcome tela gerenciamento da entidade. */
    private static final String OUTCOME_MANAGE_PER_PESSOA =
            "chargeback_manage_per_pessoa";

    /** outcome tela busca da entidade ChargebackPessoa. */
    private static final String OUTCOME_CHBACK_PESS_SEARCH =
            "chbackPess_search";

    /** outcome tela busca da entidade ChargebackPessoaProject. */
    private static final String OUTCOME_CHBACK_PESS_SEARCH_PROJECT =
            "chbackPess_search_project";

    private static final String OUTCOME_INTEGRATE_MANAGE = "integraLicencaSw_manager";

    /** outcome tela add da entidade ChargebackPessoa. */
    private static final String OUTCOME_CHBACK_PESS_ADD = "chbackPess_add";

    /** outcome tela add da entidade ChargebackPessoa. */
    private static final String OUTCOME_CHBACK_PESS_ADD_BY_CONTRACT = "chbackPess_add_by_contract";

    /** outcome tela add da entidade ChargebackPessoa. */
    private static final String OUTCOME_CHBACK_PESS_STOP_CHARGE = "chbackPess_stop_charge";

    /** outcome tela edit da entidade ChargebackPessoa. */
    private static final String OUTCOME_CHBACK_PESS_EDIT = "chbackPess_edit";

    /** outcome tela sincroniza��o da entidade ChargebackPessoa. */
    private static final String OUTCOME_CHBACK_PESS_SYNC = "chbackPess_sync";

    /** outcome tela edit da entidade ChargebackPessoa. */
//    private static final String OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT = "chbackPess_edit_by_contract";
    private static final String OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT = "chbackPess_edit_by_contract_new";

    private static final String OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT_INTEGRATION = "chbackPess_edit_by_contract_integration";

    private static final String TIPO_LICENSA_PRODUCTION = "PRODUCTION";

    private static final String TIPO_LICENSA_INTERNAL = "INTERNAL";

    private static final String OUTCOME_INTEGRATE_VIEW = "integraLicencaSw_view";

    /**
     * Prepara a tela de gerencimaneto.
     * 
     * @return retorna a pagina de gerenciamento
     */
    public String prepareManagePerResource() {
        bean.reset();

        this
                .loadTiRecursoList(tiRecursoService
                        .findTiRecursoByType(Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE));

        this.setDefaultInitialValues();

        return OUTCOME_MANAGE_PER_ITRESOURCE;
    }

    /**
     * Prepara a tela de gerencimaneto.
     * 
     * @return retorna a pagina de gerenciamento
     */
    public String prepareManagePerContract() {
        bean.reset();

        this.loadContratoPraticaList(contratoPraticaService
                .findAllCompleteForCombobox());

        this.setDefaultInitialValues();

        return OUTCOME_MANAGE_PER_CONTRACT;
    }

    /**
     * Prepara a tela de gerencimaneto.
     * 
     * @return retorna a pagina de gerenciamento
     */
    public String prepareManagePerPessoa() {
        bean.reset();

        this.setDefaultInitialValues();

        return OUTCOME_MANAGE_PER_PESSOA;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareChbackPessSearch() {
        String indicadorTipoAlocacao = Constants.TI_RECURSO_TYPE_SOFTWARE_USER;
        bean.reset();
        bean.setIndicadorTipoAlocacao(indicadorTipoAlocacao);
        this.loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(indicadorTipoAlocacao));

        return OUTCOME_CHBACK_PESS_SEARCH;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     *
     * @return pagina de destino
     */
    public String prepareChbackPessSearchProject() {
        String indicadorTipoAlocacao = Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT;
        bean.reset();
        bean.setIndicadorTipoAlocacao(indicadorTipoAlocacao);
        this.loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(indicadorTipoAlocacao));

        return OUTCOME_CHBACK_PESS_SEARCH_PROJECT;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareChbackPessCreate() {
        bean.reset();
        bean.setIsUpdate(Boolean.valueOf(false));
        this.loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_USER));

        setCurrentMonthAndYear();

        return OUTCOME_CHBACK_PESS_ADD;
    }

    public String prepareChbackPessCreateByContract() {
        bean.reset();
        bean.setIsUpdate(Boolean.FALSE);
        bean.setHasIntegratedInstallments(false);
        bean.setNomeMoeda(Constants.COTACAO_MOEDA_REAL);
        bean.setTicketAtendimento("0");
        bean.setContratoPraticas(new LinkedHashMap<>());

        loadInvoiceList();
        loadTiRecursoList(tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT));
        loadMoedaList(moedaService.findMoedaAll());
        loadMsaList(msaService.findAllReturnCodigoAndNomeMsa());

        setCurrentMonthAndYear();

        return OUTCOME_CHBACK_PESS_ADD_BY_CONTRACT;
    }

    private void loadInvoiceList() {
        bean.setInvoiceProjectMegaSelect(new InvoiceProjectMegaSelect(new ArrayList<>()));
        bean.setInvoiceMegaSelect(new InvoiceMegaSelect(invoiceService.findAllInvoices()));
    }

    private void loadLicenseInvoiceList() {
        bean.setInvoiceNumberList(licencaSwProjetoService.findInvoiceByDate(
                bean.getSearchStartDate(),
                bean.getSearchEndDate()));
    }

    private void loadProjectList() {
        List<InvoiceProjectMegaDTO> invoiceList = licencaSwProjetoService.findProjectByFilter(
                bean.getSearchStartDate(),
                bean.getSearchEndDate(),
                bean.getInvoiceNumber() == null ? 0 : bean.getInvoiceNumber()
        );
        bean.setInvoiceProjectMegaSelect(new InvoiceProjectMegaSelect(invoiceList));
    }

    public void loadLicenseID() {
        bean.setLicenseID(null);
        bean.setLicenseIDList(licencaSwProjetoParcelaService.findLicenseIdByDateStart(
                bean.getSearchStartDate(),
                bean.getSearchEndDate(),
                bean.getInvoiceNumber(),
                bean.getProject()
                )
        );
    }

    public void setCurrentMonthAndYear(){
        LocalDate currentDate = now();
        String monthBeg = String.valueOf(currentDate.getMonthOfYear());
        String yearBeg = String.valueOf(currentDate.getYearOfEra());

        bean.setMonthBeg((monthBeg.length() < 2) ? "0" + monthBeg : monthBeg);
        bean.setYearBeg(yearBeg);
    }

    public String chbackPessStopCharge() {
        findChbackPessById(bean.getCurrentRowId());
        bean.setIsUpdate(Boolean.valueOf(true));
        bean.setIsStopCharge(Boolean.TRUE);
        this.setDefaultInitialValues();

        // guarda o c�digo do login que est� sendo atualizado para caso trocar,
        // deve validar na base se o novo login/m�s j� existe ou n�o
        bean.setCodChbackPessUpdate(bean.getToPess().getCodigoChargebackPess());

        updateChbackPessStopCharge();

        return OUTCOME_CHBACK_PESS_STOP_CHARGE;

    }

    public String prepareChbackPessStopCharge(){
        findChbackPessById(bean.getCurrentRowId());
        bean.setTicketAtendimento(bean.getToPess().getTicketAtendimento());
        return OUTCOME_CHBACK_PESS_STOP_CHARGE;
    }

    /**
     *
     */
    public String cancelLoginsNotCommitedChback(){
        this.resetChback();
        return OUTCOME_CHBACK_PESS_ADD;
    }

    /**
     *
     */
    private void resetChback(){
        bean.resetTo();
        bean.resetPeriod();
        bean.setTicketAtendimento("");
        bean.resetLoginsChargeback();
    }

    /**
     * Insere uma entidade.
     *
     */
    public void createChbackPess() {
        Date startDate =
                DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        // verifica o Closing Date. Se startDate n�o for v�lido, d� mensagem de
        // erro
        if (!chbackPessService.verifyChbackClosingDate(startDate, Boolean
                .valueOf(true))) {
            return;
        }

        ChargebackPessoa toPess = bean.getToPess();
        Long codigoTiRecurso =
                bean.getTiRecursoMap().get(
                        toPess.getTiRecurso().getNomeTiRecurso());

        if (!chbackPessService.verifyChbackMonthApproved(startDate, codigoTiRecurso)) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_APPROVED_NEW_CHARGEBACK);
            return;
        }

        if(!bean.getHasMultipleLogins()){
            singleCreateChbackPess(startDate, codigoTiRecurso, toPess);
            return;
        }

        List<LoginChargebackPojo> incorrectLogins = multipleCreateChbackPess(startDate, codigoTiRecurso);
        if(incorrectLogins.isEmpty()){
            this.resetChback();
            Messages.showSucess("createChbackPess",
                    Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_IT_CHARGEBACK);

            return;
        }

        this.bean.setHasErrorLoginsChargeback(Boolean.TRUE);
        this.bean.setHasLoginsNotCommited(Boolean.TRUE);
        this.bean.setLoginsChargeback(incorrectLogins);
    }

    /**
     *
     * @param startDate
     * @param codigoTiRecurso
     * @param toPess
     * @param login
     */
    private void setCreateToPess(Date startDate, Long codigoTiRecurso, ChargebackPessoa toPess, String login){

        if(toPess == null)
            toPess = bean.getToPess();

        toPess.setDataCriacao(new Date());
        toPess.setCodigoLoginCriador(LoginUtil.getLoggedUsername());
        toPess.setTicketAtendimento(bean.getTicketAtendimento());

        toPess.setPessoa(pessoaService.findPessoaByLogin(login));

        if (codigoTiRecurso != null) {
            toPess.setTiRecurso(tiRecursoService
                    .findTiRecursoById(codigoTiRecurso));
        }

        toPess.setDataMes(startDate);
        toPess.setIndicadorTipo(Constants.TYPE_MANUAL);
        toPess.setNumeroUnidades(BigDecimal.valueOf(1));

        bean.setToReactivate(chbackPessService.findByPessoaAndTiRecursoAndEndDate(toPess));
    }

    /**
     *
     * @param startDate
     * @param codigoTiRecurso
     * @param toPess
     */
    private void singleCreateChbackPess(Date startDate, Long codigoTiRecurso, ChargebackPessoa toPess){
        setCreateToPess(startDate, codigoTiRecurso, toPess, toPess.getPessoa().getCodigoLogin());

        if (bean.getToReactivate() == null) {
            if (chbackPessService.createChbackPess(toPess)) {
                resetChback();

                Messages.showSucess("createChbackPess",
                        Constants.DEFAULT_MSG_SUCCESS_CREATE,
                        Constants.ENTITY_NAME_IT_CHARGEBACK);

                return;
            }
        } else {
            ChargebackPessoa active = chbackPessService.findChbackPessByUniqueKey(
                    bean.getToReactivate().getTiRecurso(),
                    bean.getToReactivate().getPessoa(),
                    startDate);

            if (active == null || active.getDataFimVigencia() != null) {
                bean.setShowConfirmReactivateModal(Boolean.TRUE);
                return;
            }
        }

        Messages.showError("createChbackPess",
                Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                Constants.ENTITY_NAME_IT_CHARGEBACK);

    }

    /**
     *
     * @param startDate
     * @param codigoTiRecurso
     */
    private List<LoginChargebackPojo> multipleCreateChbackPess(Date startDate, Long codigoTiRecurso){

        List<LoginChargebackPojo> incorrectLogins = new ArrayList<LoginChargebackPojo>();
        for (LoginChargebackPojo login : bean.getLoginsChargeback()) {

            ChargebackPessoa toPess = new ChargebackPessoa();
            setCreateToPess(startDate, codigoTiRecurso, toPess, login.getLogin());

            if (bean.getToReactivate() == null) {
                if (chbackPessService.createChbackPess(toPess))
                    continue;

            } else {
                ChargebackPessoa active = chbackPessService.findChbackPessByUniqueKey(
                        bean.getToReactivate().getTiRecurso(),
                        bean.getToReactivate().getPessoa(),
                        startDate);

                if (active == null || active.getDataFimVigencia() != null) {
                    login.setCorrect(Boolean.FALSE);
                    login.setError("There is already a current license for this resource with the selected Month/Year. Must be reactivate");
                    incorrectLogins.add(login);

                    continue;
                }
            }

            login.setCorrect(Boolean.FALSE);
            login.setError("This IT Charge Back already exists in data base!");
            incorrectLogins.add(login);
        }

        return incorrectLogins;
    }

    public void reactivateChbackPessoa() {
        if (bean.getToReactivate() != null) {
            ChargebackPessoa toReactivate = bean.getToReactivate();
            toReactivate.setDataFimVigencia(null);
            toReactivate.setDataAtualizacao(new Date());
            toReactivate.setTicketAtendimento(bean.getTicketAtendimento());

            if (chbackPessService.updateChbackPess(toReactivate, false)) {
                bean.resetTo();
                bean.resetPeriod();
                bean.setTicketAtendimento("");
                Messages.showSucess("createChbackPess",
                        Constants.DEFAULT_MSG_SUCCESS_REACTIVATE,
                        Constants.ENTITY_NAME_IT_CHARGEBACK);
            } else {
                Messages.showError("createChbackPess",
                        Constants.CHBACK_PESS_MSG_ERROR_REACTIVATE_CHBACK_PESS);
            }
        } else {
            Messages.showError("createChbackPess",
                    Constants.CHBACK_PESS_MSG_ERROR_REACTIVATE_CHBACK_PESS);
        }
    }

    public void cancelReactivationChbackPessoa() {
        bean.setShowConfirmReactivateModal(Boolean.FALSE);
    }

    public void createChbackPessByContract() {

        if (validateRequiredFieldsLicencaSwProjeto(true)) {
            Date startDate = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

            if (Boolean.FALSE.equals(chbackPessService.verifyChbackClosingDate(startDate, Boolean.TRUE))) {
                return;
            }

            if (bean.getValorLicenca().longValue() == 0) {
                Messages.showError("verifyChbackAmountValue",
                        Constants.INTEGRACAO_LICENCA_SOFTWARE_WITHOUT_VALUE);
                return;
            }

            LicencaSwProjeto licencaSwProjeto = new LicencaSwProjeto();
            licencaSwProjeto.setMoeda(moedaService.findMoedaById(bean.getMoedaMap().get(bean.getNomeMoeda())));
            licencaSwProjeto.setDescricao(bean.getDescricao());
            licencaSwProjeto.setDataInicio(startDate);
            licencaSwProjeto.setNotaFiscal(bean.getCodigoFatura());
            licencaSwProjeto.setQuantidadeParcela(bean.getParcelasLicenca());
            licencaSwProjeto.setTicketId(bean.getTicketAtendimento());
            licencaSwProjeto.setValorTotal(bean.getValorLicenca());
            licencaSwProjeto.setCodigoProcurify(bean.getCodigoProcurify());
            if (bean.getContratoPratica() != null) {
                licencaSwProjeto.setCdClob(bean.getContratoPratica().getCodigoContratoPratica());
            }
            if (bean.getGrupoCusto() != null) {
                licencaSwProjeto.setCdCC(bean.getGrupoCusto().getCodigoGrupoCusto());
            }
            licencaSwProjeto.setEmpresa(bean.getEmpresa());
            licencaSwProjeto.setProviderCode(bean.getCodigoFornecedor());
            licencaSwProjeto.setProviderName(bean.getNomeFornecedor());
            licencaSwProjeto.setErpProjectCode(bean.getCodigoProjetoErp());
            licencaSwProjeto.setTiRecursoErpName(bean.getNomeProduto());
            licencaSwProjeto.setProviderName(bean.getNomeFornecedor());
            Long codigoTiRecurso = bean.getTiRecursoMap().get(bean.getNomeTiRecurso());
            if (codigoTiRecurso != null) {
                licencaSwProjeto.setTiRecurso(tiRecursoService.findTiRecursoById(codigoTiRecurso));
            }

            if (Boolean.TRUE.equals(licencaSwProjetoService.create(licencaSwProjeto, bean.getContratoPraticaCodigosSelectedList(),bean.getIndicadorTipoLicenca().equals(TIPO_LICENSA_INTERNAL), bean.getLogins()))) {
                bean.resetTo();
                bean.resetPeriod();
                Messages.showSucess("createChbackPess",
                        Constants.DEFAULT_MSG_SUCCESS_CREATE,
                        Constants.ENTITY_NAME_IT_CHARGEBACK);
            } else {
                Messages.showError("createChbackPess",
                        Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                        Constants.ENTITY_NAME_IT_CHARGEBACK);
            }

            prepareChbackPessCreateByContract();
        }
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareChbackPessUpdate() {
        findChbackPessById(bean.getCurrentRowId());
        bean.setIsUpdate(Boolean.valueOf(true));

        this.loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_USER));

        Date dataMes = bean.getToPess().getDataMes();
        bean.setMonthBeg(DateUtil.getMonthString(dataMes));
        bean.setYearBeg(DateUtil.getYearString(dataMes));

        // guarda o c�digo do login que est� sendo atualizado para caso trocar,
        // deve validar na base se o novo login/m�s j� existe ou n�o
        bean.setCodChbackPessUpdate(bean.getToPess().getCodigoChargebackPess());
        bean.setTicketAtendimento(bean.getToPess().getTicketAtendimento());

        return OUTCOME_CHBACK_PESS_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String updateChbackPess() {
        ChargebackPessoa toPess = bean.getToPess();
        String codigoLogin = toPess.getPessoa().getCodigoLogin();
        toPess.setPessoa(pessoaService.findPessoaByLogin(codigoLogin));
        Long codigoTiRecursoNew =
                bean.getTiRecursoMap().get(
                        toPess.getTiRecurso().getNomeTiRecurso());
        if (codigoTiRecursoNew != null) {
            toPess.setTiRecurso(tiRecursoService
                    .findTiRecursoById(codigoTiRecursoNew));
        }
        toPess.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean
                .getYearBeg()));
        toPess.setIndicadorTipo(Constants.TYPE_MANUAL);
        toPess.setDataAtualizacao(new Date());
        toPess.setTicketAtendimento(bean.getTicketAtendimento());

        ChargebackPessoa chbackPessOld =
                chbackPessService.findChbackPessByUniqueKey(toPess.getTiRecurso(), toPess.getPessoa(), toPess.getDataMes());

        Boolean isDifferentReg =
                (chbackPessOld == null || chbackPessOld.getCodigoChargebackPess().compareTo(bean.getCodChbackPessUpdate()) != 0);

        if (chbackPessService.updateChbackPess(toPess, isDifferentReg)) {
            bean.resetTo();
            bean.resetPeriod();
            Messages.showSucess("updateChbackPess",
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_IT_CHARGEBACK);
            this.findByFilterChbackPess();
            return OUTCOME_CHBACK_PESS_SEARCH;
        } else {
            Messages.showError("updateChbackPess",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_IT_CHARGEBACK);
            return null;
        }
    }

    /**
     * Executa um update da entidade.
     *
     * @return pagina de destino
     *
     */
    public String updateChbackPessStopCharge() {
        ChargebackPessoa toPess = bean.getToPess();
        String codigoLogin = toPess.getPessoa().getCodigoLogin();
        toPess.setPessoa(pessoaService.findPessoaByLogin(codigoLogin));
        Long codigoTiRecursoNew =bean.getTiRecursoMap().get(toPess.getTiRecurso().getNomeTiRecurso());

        if (codigoTiRecursoNew != null) {
            toPess.setTiRecurso(tiRecursoService.findTiRecursoById(codigoTiRecursoNew));
        }
        //toPess.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg()));
        toPess.setIndicadorTipo(Constants.TYPE_MANUAL);
        toPess.setDataAtualizacao(new Date());
        toPess.setTicketAtendimento(bean.getTicketAtendimento());

        if(bean.getIsStopCharge()){
            toPess.setDataFimVigencia(DateUtil.getDateFirstDayOfMonth(DateUtil.getNextMonth(new Date())));
        }

        ChargebackPessoa chbackPessOld = chbackPessService.findChbackPessByUniqueKey(toPess.getTiRecurso(), toPess.getPessoa(), toPess.getDataMes());


        if (chbackPessService.updateChbackPess(toPess, false)) {
            bean.resetTo();
            bean.resetPeriod();
            Messages.showSucess("updateChbackPess",
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_IT_CHARGEBACK);
            this.findByFilterChbackPess();
            return OUTCOME_CHBACK_PESS_SEARCH;
        } else {
            Messages.showError("updateChbackPess",
                    Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                    Constants.ENTITY_NAME_IT_CHARGEBACK);
            return null;
        }
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String removeChbackPess() {
        ChargebackPessoa chbackPess =
                chbackPessService.findChbackPessById(bean.getCurrentRowId());

        // verifica o Closing Date. Se startDate n�o for v�lido, d� mensagem de
        // erro
        if (!chbackPessService.verifyChbackClosingDate(chbackPess.getDataMes(),
                Boolean.valueOf(true))) {
            return null;
        }

        chbackPessService.removeChbackPess(chbackPess);
        Messages.showSucess("removeChbackPess",
                Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_IT_CHARGEBACK);
        bean.resetTo();
        bean.resetPeriod();
        this.findByFilterChbackPess();
        return OUTCOME_CHBACK_PESS_SEARCH;
    }


    /**
    * Remove uma determinada Licenca de Projeto e suas parcelas.
    *
    * @return pagina de destino
    *
    */
    public String removeSoftwareProjectLicense() {
        findLicencaProjectById(bean.getCurrentRowId());
        String tipoAlocao = bean.getIndicadorTipoAlocacao();

        List<LicencaSwProjetoPessoa> entities = licencaSwProjetoPessoaService
                .findByLicencaSwProjeto(bean.getToLicencaSwProjeto());

        for (LicencaSwProjetoPessoa entity : entities) {
            licencaSwProjetoPessoaService.removePersonLicencaSwProjeto(entity);
        }

        licencaSwProjetoService.removeSoftwareProjectLicenseAndInstallments(bean.getToLicencaSwProjeto());

        Messages.showSucess("removeSoftwareProjectLicense",
            Constants.DEFAULT_MSG_SUCCESS_REMOVE,
            Constants.ENTITY_NAME_LICENSE);
        bean.resetTo();
        bean.resetPeriod();
        this.findByFilterChbackPess();

        return Objects.equals(bean.getIndicadorTipoAlocacao(), Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT) ? OUTCOME_CHBACK_PESS_SEARCH_PROJECT : OUTCOME_CHBACK_PESS_SEARCH;
    }

    /**
     * Cancela a edicao e volta para a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String cancelChbackPess() {
        if (bean.getIsIntegrationOrigin()) {
            return OUTCOME_INTEGRATE_MANAGE;
        } else {
            return OUTCOME_CHBACK_PESS_SEARCH;
        }
    }

    public String cancelChbackPessByContract() {
        if (bean.getIsIntegrationOrigin()) {
            return OUTCOME_INTEGRATE_MANAGE;
        } else {
            bean.resetChbackProjectSearchFilters();
            return OUTCOME_CHBACK_PESS_SEARCH_PROJECT;
        }
    }

    /**
     * Prepara a tela de sincroniza��o do ChbackPess.
     * 
     * @return pagina de destino
     */
    public String prepareChbackPessSync() {
        bean.reset();

        return OUTCOME_CHBACK_PESS_SYNC;
    }

    /**
     * Executa a sincroniza��o de Software completa: apaga tudo e grava
     * novamente.
     */
    public void chbackPessSyncFull() {
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        try {
            if (chbackPessService.syncChbackPessFull(dataMes)) {
                if (this.syncChbackPess(dataMes)) {
                    Messages.showSucess("chbackPessSyncFull",
                            Constants.CHBACK_PESS_MSG_SUCCESS_SYNC_FULL);
                } else {
                    Messages.showWarning("chbackPessSyncFull",
                            Constants.CHBACK_PESS_MSG_WARNG_SYNC);
                }
            }
        } catch (IntegrityConstraintException e) {
            Messages.showError("chbackPessSyncFull",
                    Constants.CHBACK_PESS_MSG_ERROR_SYNC);
        }
    }

    /**
     * Executa a sincroniza��o do Software parcial: mant�m os registros do tipo
     * Manual, apaga somente os do tipo Sincronizado e grava novamente.
     */
    public void chbackPessSyncPartial() {
        Date dataMes = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

        try {
            if (chbackPessService.syncChbackPessPartial(dataMes)) {
                if (this.syncChbackPess(dataMes)) {
                    Messages.showSucess("chbackPessSyncPartial",
                            Constants.CHBACK_PESS_MSG_SUCCESS_SYNC_PARTIAL);
                } else {
                    Messages.showWarning("chbackPessSyncPartial",
                            Constants.CHBACK_PESS_MSG_WARNG_SYNC);
                }
            }
        } catch (IntegrityConstraintException e) {
            Messages.showError("chbackPessSyncPartial",
                    Constants.CHBACK_PESS_MSG_ERROR_SYNC);
        }
    }

    /**
     * Executa a sincroniza��o do ChargebackPessoa.
     * 
     * @param dataMes
     *            - data m�s
     * 
     * @return true se a sincroniza��o ocorrou corretamente. False caso
     *         contr�rio.
     */
    private Boolean syncChbackPess(final Date dataMes) {
        // vari�veis do log
        final String titleLine = "********** ";
        final String breakLine = "\n";
        final String logError = " ERROR ";
        StringBuffer textoLog = new StringBuffer();
        textoLog.append(titleLine
                + BundleUtil.getBundle(Constants.LABEL_CHBACK_PESS_SYNC)
                + breakLine);

        // flag indicador processo realizado com sucesso ou n�o.
        Boolean isSyncOk = Boolean.valueOf(true);

        // verifica a lista de registros de software da view
        // para depois gravar na ChargebackPessoa. Caso ocorra erros/duplica��o,
        // guarda no textoLog e depois grava na ChbackPessLogSincronizacao

        // busca a lista de registros da view
        List<VwChbackSoftware> vwChbackSoftList =
                vwChbackSoftService.findAllByDate(dataMes);

        // guarda em um map e j� guarda em uma vari�vel o log dos registros
        // duplicados
        Map<VwChbackSoftwareId, VwChbackSoftware> vwChbackSoftMap =
                new HashMap<VwChbackSoftwareId, VwChbackSoftware>();
        for (VwChbackSoftware vwChbackSoft : vwChbackSoftList) {           
            Long codigoPessoa = vwChbackSoft.getCodigoPessoa();
            Long codigoTiRecurso = vwChbackSoft.getCodigoTiRecurso();
            VwChbackSoftwareId id = vwChbackSoft.getId();

            if (codigoPessoa == null) {
                isSyncOk = Boolean.valueOf(false);
                String codigoLogin = id.getCodigoLogin();
                Messages.showError("syncChbackPess",
                        Constants.CHBACK_PESS_MSG_ERROR_SYNC_LOGIN_NOT_EXIST,
                        codigoLogin);

                textoLog
                        .append(DateUtil.formatDate(new Date(),
                                Constants.DEFAULT_DATE_PATTERN_FULL,
                                Constants.DEFAULT_CALENDAR_LOCALE)
                                + logError
                                + (BundleUtil
                                        .getBundle(Constants.CHBACK_PESS_MSG_ERROR_SYNC_LOGIN_NOT_EXIST))
                                        .replace("{0}", codigoLogin)
                                + breakLine);
            } else if (codigoTiRecurso == null) {
                isSyncOk = Boolean.valueOf(false);
                String codigoMnemonico = id.getCodigoMnemonico();
                Messages.showError("syncChbackPess",
                        Constants.CHBACK_PESS_MSG_ERROR_SYNC_TI_RECURSO_NOT_EXIST,
                        codigoMnemonico);

                textoLog
                        .append(DateUtil.formatDate(new Date(),
                                Constants.DEFAULT_DATE_PATTERN_FULL,
                                Constants.DEFAULT_CALENDAR_LOCALE)
                                + logError
                                + (BundleUtil
                                        .getBundle(Constants.CHBACK_PESS_MSG_ERROR_SYNC_TI_RECURSO_NOT_EXIST))
                                        .replace("{0}", codigoMnemonico)
                                + breakLine);
            }
            if (!vwChbackSoftMap.containsKey(id)) {
                vwChbackSoftMap.put(id, vwChbackSoft);
            } else {
                isSyncOk = Boolean.valueOf(false);
                String codigoLogin = id.getCodigoLogin();
                Messages.showError("syncChbackPess",
                        Constants.CHBACK_PESS_MSG_ERROR_SYNC_LOGIN_DUPLIC,
                        codigoLogin);

                textoLog
                        .append(DateUtil.formatDate(new Date(),
                                Constants.DEFAULT_DATE_PATTERN_FULL,
                                Constants.DEFAULT_CALENDAR_LOCALE)
                                + logError
                                + (BundleUtil
                                        .getBundle(Constants.CHBACK_PESS_MSG_ERROR_SYNC_LOGIN_DUPLIC))
                                        .replace("{0}", codigoLogin)
                                + breakLine);
            }
        }

        // busca a lista de registros que est�o na ChargebackPessoa, para
        // considerar os registros do tipo Manual que n�o s�o removidos na
        // sincroniza��o (quando a escolha � "No")
        ChargebackPessoa filter = new ChargebackPessoa();
        filter.setDataMes(dataMes);
        filter.setIndicadorTipo(Constants.TYPE_MANUAL);
        List<ChargebackPessoa> chbackPessMNList =
                chbackPessService.findChbackPessByFilter(filter,
                        new HashMap<Long, String>());

        // guarda em um map
        Map<Long, ChargebackPessoa> chbackPessMNMap =
                new HashMap<Long, ChargebackPessoa>();
        for (ChargebackPessoa chbackPessMN : chbackPessMNList) {
            chbackPessMNMap.put(chbackPessMN.getPessoa().getCodigoPessoa(),
                    chbackPessMN);
        }

        // vari�veis auxiliares
        double cont = 0;
        double totalReg = vwChbackSoftMap.size();
        Boolean isPersistEnable = null;
        ChargebackPessoa chbackPess = null;
        int contPersist = 0;

        // itera a lista da view. Caso o c�digo tenha sido retornado na lista
        // da view, utiliza estes valores. � primeiramente considerado o map
        // gerado a partir da
        // tabela ChargebackPessoa pois se existem registros do tipo Manual e
        // estes est�o sendo
        // considerados, estes n�o podem ser inseridos novamente. Portanto na
        // itera��o devem ser desconsiderados.
        Iterator<VwChbackSoftwareId> it = vwChbackSoftMap.keySet().iterator();
        while (it.hasNext()) {
            VwChbackSoftwareId nextId = it.next();
            isPersistEnable = Boolean.valueOf(true);
            chbackPess = new ChargebackPessoa();

            // verifica primeiramente se existe registros do tipo Manual, se sim
            // desconsidera os novos
            if (chbackPessMNMap.get(nextId) != null) {
                isPersistEnable = Boolean.valueOf(false);
                // caso contr�rio, faz a sincronizacao
            } else {
                VwChbackSoftware vwChbackSoft =
                        vwChbackSoftMap.get(nextId);

                if (vwChbackSoft != null) {
                    chbackPess.setPessoa(pessoaService
                            .findPessoaById(vwChbackSoft.getCodigoPessoa()));
                    Long codigoTiRecurso = vwChbackSoft.getCodigoTiRecurso();
                    if (codigoTiRecurso != null) {
                        chbackPess.setTiRecurso(tiRecursoService
                                .findTiRecursoById(codigoTiRecurso));
                    }
                    chbackPess.setDataMes(vwChbackSoft.getId().getDataMes());
                    chbackPess.setNumeroUnidades(vwChbackSoft
                            .getNumeroUnidades());
                }
            }

            // ser� false somente quando existir o registro na ChargebackPessoa
            // com
            // o tipo Manual. Nesse caso n�o grava no banco e desconsidera o
            // registro
            if (isPersistEnable) {
                chbackPess.setIndicadorTipo(Constants.TYPE_SYNC);

                chbackPessService.createChbackPess(chbackPess);
                contPersist++;
            }

            // faz o c�lculo da porcentagem do progresso do processo para
            // mostrar na tela
            cont++;
            bean.setProgressPercent((cont / totalReg) * 100);
        }
        
        Messages.showSucess("chbackPessSyncFull",
                Constants.CHBACK_PESS_MSG_SUCCESS_SYNC_QTDE_REG, String
                        .valueOf(contPersist));

        // grava o log
        ChbackPessLogSincronizacao chbackPessLogSinc =
                new ChbackPessLogSincronizacao();
        chbackPessLogSinc.setDataMes(dataMes);
        chbackPessLogSinc.setDataExecucao(new Date());
        chbackPessLogSinc.setCodigoAutor(LoginUtil.getLoggedUsername());
        if (!isSyncOk) {
            chbackPessLogSinc.setTextoLog(textoLog.toString());
        } else {
            textoLog.append("SUCCESS");
            chbackPessLogSinc.setTextoLog(textoLog.toString());
        }
        chbackPessLogSincService.createChbackPessLogSinc(chbackPessLogSinc);

        bean.setIsProgressFinished(Boolean.valueOf(true));

        return isSyncOk;
    }

    /**
     * Seta os valores default inicialis.
     */
    private void setDefaultInitialValues() {
        if (bean.getStartDate() == null || bean.getEndDate() == null) {

            Date defaultStartDate = chbackCPService.getDefaultStartDate();
            Date defaultEndDate = chbackCPService.getDefaultEndDate();

            bean.setMonthBeg(DateUtil.getMonthString(defaultStartDate));
            bean.setYearBeg(DateUtil.getYearString(defaultStartDate));

            bean.setMonthEnd(DateUtil.getMonthString(defaultEndDate));
            bean.setYearEnd(DateUtil.getYearString(defaultEndDate));
        }
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findChbackPessById(final Long id) {
        bean.setToPess(chbackPessService.findChbackPessById(id));
        if (bean.getToPess() == null
                || bean.getToPess().getCodigoChargebackPess() == null) {
            Messages.showWarning("findChbackPessById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca por recurso.
     */
    public void findByResource() {
        TiRecurso tiRecurso = this.getTiRecurso();

        Date startDate =
                DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());
        bean.setStartDate(startDate);

        Date endDate = DateUtil.getDate(bean.getMonthEnd(), bean.getYearEnd());
        bean.setEndDate(endDate);

        List<ChargebackRow> rowList = null;
        String indicadorTipoAlocacao = tiRecurso.getIndicadorTipoAlocacao();
        if (Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE
                .equals(indicadorTipoAlocacao)) {
            rowList =
                    chbackCPService.findAndPrepareManagePerResource(tiRecurso,
                            startDate, endDate);

            bean.setIsChbackCP(Boolean.valueOf(true));
        } else {
            rowList =
                    chbackPessService
                            .findChbackPessAndPrepareManagePerItResource(
                                    tiRecurso, startDate, endDate);
            bean.setIsChbackCP(Boolean.valueOf(false));
        }

        bean.setDateList(DateUtil.getValidityDateList(startDate, endDate));
        bean.setChargebackRowList(rowList);

        if (rowList.isEmpty()) {
            // mensagem de nenhum resultado encontrado
            Messages.showWarning("findByResource",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca por Contrato-Pratica.
     */
    public void findByContract() {
        ContratoPratica contratoPratica = this.getContratoPratica();

        Date startDate =
                DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());
        bean.setStartDate(startDate);

        Date endDate = DateUtil.getDate(bean.getMonthEnd(), bean.getYearEnd());
        bean.setEndDate(endDate);

        List<ChargebackRow> rowList =
                chbackCPService.findAndPrepareManagePerContract(
                        contratoPratica, startDate, endDate);

        bean.setDateList(DateUtil.getValidityDateList(startDate, endDate));
        bean.setChargebackRowList(rowList);

        if (rowList.isEmpty()) {
            // mensagem de nenhum resultado encontrado
            Messages.showWarning("findByContract",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca por Pessoa.
     */
    public void findByPessoa() {
        Pessoa pessoa = this.getPessoa();

        Date startDate =
                DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());
        bean.setStartDate(startDate);

        Date endDate = DateUtil.getDate(bean.getMonthEnd(), bean.getYearEnd());
        bean.setEndDate(endDate);

        List<ChargebackRow> rowList =
                chbackPessService.findChbackPessAndPrepareManagePerPessoa(
                        pessoa, startDate, endDate);

        bean.setDateList(DateUtil.getValidityDateList(startDate, endDate));
        bean.setChargebackRowList(rowList);

        if (rowList.isEmpty()) {
            // mensagem de nenhum resultado encontrado
            Messages.showWarning("findByPessoa",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    public void validPeriodSixtyDays(){
        int days = Days.daysBetween(new DateTime(bean.getSearchStartDate()), new DateTime(bean.getSearchEndDate())).getDays();

        if (days > 60) {
            Messages.showError("required", Constants.CHBACK_PESS_MSG_WARNG_INVALID_DATE);
        }else{
            bean.resetChbackProjectSearchFilters();
            loadLicenseInvoiceList();
            loadProjectList();
            loadLicenseID();
        }

    }

    public void resetChbackProjectSearchFilters() {
        bean.setSearchEndDate(null);
        bean.resetChbackProjectSearchFilters();
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilterChbackPess() {
        bean.setResultListChbackPess(new ArrayList<ChargebackPessoa>());
        bean.setLicencasProjeto(new ArrayList<LicencaSwProjeto>());

        List<String> selectedTiRecursoList = bean.getSelectedTiRecursoList();

        ChargebackPessoa filter = bean.getFilterPess();

        if (filter.getPessoa().getCodigoLogin() == null){
            filter.getPessoa().setCodigoLogin("");
        }

        Map<Long, String> multCodTiRecMap = new HashMap<Long, String>();
        if (selectedTiRecursoList.size() > 0) {
            for (String nomeTiRecurso : selectedTiRecursoList) {
                multCodTiRecMap.put(bean.getTiRecursoMap().get(nomeTiRecurso),
                        nomeTiRecurso);
            }
        } else if (bean.getIndicadorTipoAlocacao() == null && filter.getPessoa().getCodigoLogin().isEmpty()) {
            Messages.showWarning("required", Constants.CHBACK_PESS_MSG_WARNG_SEARCH_REQUIRED);
            return;
        }

        filter.setDataMes(DateUtil.getDate("01", "2017"));

        if (bean.getIndicadorTipoAlocacao() != null) {
            if (bean.getIndicadorTipoAlocacao().equals(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT) && filter.getPessoa().getCodigoLogin().isEmpty()) {

                List<LicencaSwProjeto> licencaSwProjetos = licencaSwProjetoService.findByFilter(
                        multCodTiRecMap.isEmpty() ? null : multCodTiRecMap.keySet(),
                        bean.getCodigoProcurify().isEmpty() ? null : bean.getCodigoProcurify(),
                        Boolean.TRUE.equals(bean.getIsNotasVaziasOnly()) ? null : 0L,
                        bean.getSearchStartDate(),
                        bean.getSearchEndDate(),
                        bean.getInvoiceNumber(),
                        bean.getProject(),
                        bean.getLicenseID(),
                        bean.getResourceName()
                );

                List<LicencaSwProjetoParcela> licencaSwProjetoParcela = null;
                for (LicencaSwProjeto licencaSwProjeto : licencaSwProjetos) {
                    licencaSwProjeto.setHasOpenedInstallments(false);
                    licencaSwProjeto.setHasIntegratedInstallments(false);
                    licencaSwProjeto.setIsExcludable(true);
                    Hibernate.initialize(licencaSwProjeto.getMoeda());

                    licencaSwProjetoParcela = licencaSwProjetoParcelaService.findByLicencaSwProjeto(licencaSwProjeto.getCodigoLicencaSwProjeto());

                    for (LicencaSwProjetoParcela swProjetoParcela : licencaSwProjetoParcela) {
                        if (swProjetoParcela.getDataParcela().equals(licencaSwProjeto.getDataInicio())){
                            licencaSwProjeto.setLicencaSwProjetoParcela(swProjetoParcela.getCodigoLicencaSwProjetoParcela());
                        }

                        if (swProjetoParcela.getStatus() != null && (swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                                || swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName()))) {
                            licencaSwProjeto.setHasOpenedInstallments(true);
                        }

                        if (null != swProjetoParcela.getStatus() && (swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.PENDENTE.getFieldName())
                                || swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName()))) {
                            licencaSwProjeto.setHasIntegratedInstallments(true);
                        }
                        if (null != swProjetoParcela.getStatus() && (swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.PENDENTE.getFieldName())
                                || swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName())
                                    || swProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.ERRO.getFieldName()))) {
                            licencaSwProjeto.setIsExcludable(false);
                        }
                    }

                    Set<String> nomesCpAndCc = new HashSet<String>();
                    List<LicencaSwProjetoParcela> parcelas = new ArrayList<LicencaSwProjetoParcela>();
                    if (licencaSwProjeto.getHasOpenedInstallments()) {
                        parcelas = licencaSwProjetoParcelaService.getFirstOpenedInstalments(licencaSwProjetoParcela);
                    } else {
                        parcelas = licencaSwProjetoParcelaService.getLastIntegratedInstallments(licencaSwProjetoParcela);
                    }
                    for (LicencaSwProjetoParcela parcela : parcelas) {
                        if (parcela.getContratoPratica() != null) {
                            nomesCpAndCc.add(parcela.getContratoPratica().getNomeContratoPratica());
                        } else {
                            nomesCpAndCc.add(parcela.getGrupoCusto().getNomeGrupoCusto());
                        }
                    }
                    licencaSwProjeto.setClobs(this.getClobsOrCostCentersNames(nomesCpAndCc));

                    Set<String> loginsLicenca = new HashSet<String>();
                    List<LicencaSwProjetoPessoa> licencaSwProjetoPessoas = new ArrayList<LicencaSwProjetoPessoa>();
                    licencaSwProjetoPessoas = licencaSwProjetoPessoaService.findByLicencaSwProjeto(licencaSwProjeto);

                    for (LicencaSwProjetoPessoa licencaSwProjetoPessoa : licencaSwProjetoPessoas) {
                        loginsLicenca.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin());
                    }
                    licencaSwProjeto.setLogins(getLoginsLicenca(loginsLicenca));
                }

                bean.setLicencasProjeto(licencaSwProjetos);

            } else if (bean.getIndicadorTipoAlocacao().equals(Constants.TI_RECURSO_TYPE_SOFTWARE_USER)){
                bean.setResultListChbackPess(chbackPessService
                        .findChbackPessByFilter(filter, multCodTiRecMap));
            }
        } else {
            if (!filter.getPessoa().getCodigoLogin().isEmpty()){
                bean.setResultListChbackPess(chbackPessService
                        .findChbackPessByFilter(filter, multCodTiRecMap));
            }
        }

        if ((bean.getResultListChbackPess() == null || bean.getResultListChbackPess().size() == 0)
                && (bean.getLicencasProjeto() == null || bean.getLicencasProjeto().size() == 0)) {
            Messages.showWarning("findByFilterChbackPess",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    private String getClobsOrCostCentersNames(Set<String> nomesCpAndCc) {
        String clobs = "";
        for (String s : nomesCpAndCc) {
            if (!clobs.isEmpty()) {
                clobs = clobs + " - " + s;
            } else {
                clobs = s;
            }
        }

        return clobs;
    }

    private String getLoginsLicenca(Set<String> loginsLicenca) {
        String logins = "";
        for (String s : loginsLicenca) {
            if (!logins.isEmpty()) {
                logins = logins + " - " + s;
            } else {
                logins = s;
            }
        }

        return logins;
    }

    /**
     * Ao finalizar a sincroniza��o, clica em Acknowledge e redireciona para
     * tela de busca.
     * 
     * @return p�gina de destino
     * 
     */
    public String acknowledge() {
        bean.resetFilter();
        ChargebackPessoa filter = bean.getFilterPess();
        filter.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean
                .getYearBeg()));
        filter.setIndicadorTipo(Constants.ALL);
        bean.setIsMissingBlankValues(Boolean.valueOf(true));

        bean.setResultListChbackPess(chbackPessService
                .findChbackPessByFilterMissBlank(filter));

        if (bean.getResultListChbackPess().size() == 0) {
            Messages.showWarning("acknowledge",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);

        // atualiza os combos dos filtros da tela
        bean.setMonthBegFilter(bean.getMonthBeg());
        bean.setYearBegFilter(bean.getYearBeg());

        this.loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_USER));

        return OUTCOME_CHBACK_PESS_SEARCH;
    }

    /**
     * Realiza o update do mapa, na vis�o por recurso.
     */
    public void updateChargebackManagePerResource() {
        this.updateChargebackCP();

        this.findByResource();
    }

    /**
     * Realiza o update do mapa, na vis�o por recurso.
     */
    public void updateChbackPessManagePerResource() {
        this.updateChargebackPess();

        this.findByResource();
    }

    /**
     * Realiza o update do mapa, na vis�o por contrato-pratica.
     */
    public void updateChargebackManagePerContract() {
        this.updateChargebackCP();

        this.findByContract();
    }

    /**
     * Realiza o update do mapa, na vis�o por Pessoa.
     */
    public void updateChargebackManagePerPessoa() {
        this.updateChargebackPess();

        this.findByPessoa();
    }

    /**
     * Realiza o update no mapa.
     */
    private void updateChargebackCP() {
        chbackCPService.updateManageChargeback(bean.getChargebackRowList());

        // mensagem de sucesso
        Messages.showSucess("updateChargeback",
                Constants.DEFAULT_MSG_SUCCESS_SAVE,
                Constants.ENTITY_NAME_IT_CHARGEBACK);
    }

    /**
     * Realiza o update no mapa.
     */
    private void updateChargebackPess() {
        chbackPessService.updateManageChargebackPess(bean
                .getChargebackRowList());

        // mensagem de sucesso
        Messages.showSucess("updateChargebackPess",
                Constants.DEFAULT_MSG_SUCCESS_SAVE,
                Constants.ENTITY_NAME_IT_CHARGEBACK);
    }

    /**
     * Perapara o modal para inserir uma nova linha.
     */
    public void prepareAddRowPerResource() {
        this.bean.resetTo();

        loadContratoPraticaList(contratoPraticaService
                .findContratoPraticaAllComplete());
    }

    /**
     * Perapara o modal para inserir uma nova linha.
     */
    public void prepareAddRowChbackPessPerResource() {
        this.bean.resetTo();
    }

    /**
     * Perapara o modal para inserir uma nova linha.
     */
    public void prepareAddRowPerContract() {
        this.bean.resetTo();

        loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(Constants.TI_RECURSO_TYPE_CONTRCT_SERVICE));
    }

    /**
     * Perapara o modal para inserir uma nova linha.
     */
    public void prepareAddRowPerPessoa() {
        this.bean.resetTo();

        loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_USER));
    }

    /**
     * Adiciona uma nova linha no Mapa.
     */
    public void addRowPerResource() {
        this.addRowPerContract(this.getContratoPratica(), bean.getTiRecurso());

        this.findByResource();
    }

    /**
     * Adiciona uma nova linha no Mapa.
     */
    public void addRowChbackPessPerResource() {
        this.addRowPerPessoa(this.getPessoa(), bean.getTiRecurso());

        this.findByResource();
    }

    /**
     * Adiciona uma nova linha no Mapa.
     */
    public void addRowPerContract() {
        this.addRowPerContract(bean.getContratoPratica(), getTiRecurso());

        this.findByContract();
    }

    /**
     * Adiciona uma nova linha no Mapa.
     */
    public void addRowPerPessoa() {
        this.addRowPerPessoa(bean.getPessoa(), this.getTiRecurso());

        this.findByPessoa();
    }

    /**
     * Adiciona uma novo linha ao mapa.
     * 
     * @param cp
     *            - entidade do tipo ContratoPratica
     * 
     * @param recurso
     *            - entidade do tipo TiRecurso
     */
    private void addRowPerContract(final ContratoPratica cp,
            final TiRecurso recurso) {
        ChargebackContratoPratica chargeback = bean.getTo();
        chargeback.setContratoPratica(cp);
        chargeback.setTiRecurso(recurso);

        ChargebackRow row =
                chbackCPService.createRow(chargeback, bean.getStartDate(), bean
                        .getEndDate());

        // mensagem de sucesso
        Messages.showSucess("addRow", Constants.GENEREC_MSG_SUCCESS_ADD);

        bean.getChargebackRowList().add(row);
    }

    /**
     * Adiciona uma novo linha ao mapa.
     * 
     * @param pessoa
     *            - entidade do tipo Pessoa
     * 
     * @param tiRecurso
     *            - entidade do tipo TiRecurso
     */
    private void addRowPerPessoa(final Pessoa pessoa, final TiRecurso tiRecurso) {
        ChargebackPessoa chbackPess = bean.getToPess();
        chbackPess.setPessoa(pessoa);
        chbackPess.setTiRecurso(tiRecurso);

        ChargebackRow row =
                chbackPessService.createRow(chbackPess, bean.getStartDate(),
                        bean.getEndDate());

        // mensagem de sucesso
        Messages.showSucess("addRowPerPessoa",
                Constants.GENEREC_MSG_SUCCESS_ADD);

        bean.getChargebackRowList().add(row);
    }

    /**
     * Remove todas as linhas selecionadas, na vis�o por recurso.
     */
    public void removeAllSelectedRowPerResource() {
        if (this.removeAllSelected()) {
            this.findByResource();
        }
    }

    /**
     * Remove todas as linhas selecionadas, na vis�o por recurso.
     */
    public void removeAllSelectedRowChbackPessPerResource() {
        if (this.removeAllSelectedPess()) {
            this.findByResource();
        }
    }

    /**
     * Remove todas as linhas selecionadas, na vis�o por contrato.
     */
    public void removeAllSelectedRowPerContract() {
        if (this.removeAllSelected()) {
            this.findByContract();
        }
    }

    /**
     * Remove todas as linhas selecionadas, na vis�o por Pessoa.
     */
    public void removeAllSelectedRowPerPessoa() {
        if (this.removeAllSelectedPess()) {
            this.findByPessoa();
        }
    }

    /**
     * Remove todas as linhas selecionadas.
     * 
     * @return true se a fun��o foi realizada, caso contr�rio false
     */
    private Boolean removeAllSelected() {
        if (this.isSomeChargebackRowSelected()) {
            chbackCPService.removeAllSelectedRow(bean.getChargebackRowList());

            // mensagem de sucesso
            Messages.showSucess("removeAllSelected",
                    Constants.GENEREC_MSG_SUCCESS_REMOVE);

            return Boolean.valueOf(true);
        } else {
            Messages.showWarning("removeAllSelectedPess",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);

            return Boolean.valueOf(false);
        }
    }

    /**
     * Remove todas as linhas selecionadas.
     * 
     * @return true se a fun��o foi realizada, caso contr�rio false
     */
    private Boolean removeAllSelectedPess() {
        if (this.isSomeChargebackRowSelected()) {
            chbackPessService.removeAllSelectedRow(bean.getChargebackRowList());

            // mensagem de sucesso
            Messages.showSucess("removeAllSelectedPess",
                    Constants.GENEREC_MSG_SUCCESS_REMOVE);

            return Boolean.valueOf(true);
        } else {
            Messages.showWarning("removeAllSelectedPess",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);

            return Boolean.valueOf(false);
        }
    }

    /**
     * Realiza o update do n�mero de unidades de todas as linhas selecionadas,
     * na vis�o por recurso ti.
     */
    public void updateAllNumUnitsPerResource() {
        if (this.updateAllNumUnitsCP()) {
            this.findByResource();
        }
    }

    /**
     * Realiza o update do n�mero de unidades de todas as linhas selecionadas,
     * na vis�o por recurso ti.
     */
    public void updateAllNumUnitsChbackPessPerResource() {
        if (this.updateAllNumUnitsPess()) {
            this.findByResource();
        }
    }

    /**
     * Realiza o update do n�mero de unidades de todas as linhas selecionadas,
     * na vis�o por contrato-pratica.
     */
    public void updateAllNumUnitsPerContract() {
        if (this.updateAllNumUnitsCP()) {
            this.findByContract();
        }
    }

    /**
     * Realiza o update do n�mero de unidades de todas as linhas selecionadas,
     * na vis�o por Pessoa.
     */
    public void updateAllNumUnitsPerPessoa() {
        if (this.updateAllNumUnitsPess()) {
            this.findByPessoa();
        }
    }

    /**
     * Realiza o update do n�mero de unidades de todas as linhas selecionadas.
     * 
     * @return true se a fun��o foi realizada, caso contr�rio false
     */
    private Boolean updateAllNumUnitsCP() {
        if (this.isSomeChargebackRowSelected()) {
            ChargebackContratoPratica to = bean.getTo();

            chbackCPService.updateAllNumUnits(to.getNumeroUnidades(), bean
                    .getChargebackRowList());

            to.setNumeroUnidades(BigDecimal.valueOf(0.0));

            // mensagem de sucesso
            Messages.showSucess("updateAllNumUnits",
                    Constants.GENEREC_MSG_SUCCESS_UPDATE);

            return Boolean.valueOf(true);
        } else {
            Messages.showWarning("updateAllNumUnitsPess",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);

            return Boolean.valueOf(false);
        }
    }

    /**
     * /** Realiza o update do n�mero de unidades de todas as linhas
     * selecionadas.
     * 
     * @return true se a fun��o foi realizada, caso contr�rio false
     */
    private Boolean updateAllNumUnitsPess() {
        if (this.isSomeChargebackRowSelected()) {
            ChargebackPessoa to = bean.getToPess();

            chbackPessService.updateAllNumUnitsPess(to.getNumeroUnidades(),
                    bean.getChargebackRowList());

            to.setNumeroUnidades(BigDecimal.valueOf(0));

            // mensagem de sucesso
            Messages.showSucess("updateAllNumUnitsPess",
                    Constants.GENEREC_MSG_SUCCESS_UPDATE);

            return Boolean.valueOf(true);
        } else {
            Messages.showWarning("updateAllNumUnitsPess",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);

            return Boolean.valueOf(false);
        }
    }

    /**
     * Pega o recurso selecionado.
     * 
     * @return retorna uma entidade TiRecurso.
     */
    private TiRecurso getTiRecurso() {
        TiRecurso tiRecurso =
                tiRecursoService.findTiRecursoById(bean.getTiRecursoMap().get(
                        bean.getTiRecurso().getNomeTiRecurso()));

        bean.setTiRecurso(tiRecurso);

        return tiRecurso;
    }

    /**
     * Pega o ContratoPratica selecionado.
     * 
     * @return retorna o ContratoPratica selecionado.
     */
    private ContratoPratica getContratoPratica() {
        ContratoPratica cp =
                contratoPraticaService.findContratoPraticaById(bean
                        .getContratoPraticaMap().get(
                                bean.getContratoPratica()
                                        .getNomeContratoPratica()));

        bean.setContratoPratica(cp);

        return cp;
    }

    /**
     * Pega o Pessoa selecionada.
     * 
     * @return retorna a Pessoa selecionado.
     */
    private Pessoa getPessoa() {
        Pessoa pessoa =
                pessoaService.findPessoaByLogin(bean.getPessoa()
                        .getCodigoLogin());

        bean.setPessoa(pessoa);

        return pessoa;
    }

    /**
     * Popula a lista de TiRecurso para combobox.
     * 
     * @param tiRecursoList
     *            lista de TiRecurso.
     * 
     */
    private void loadTiRecursoList(final List<TiRecurso> tiRecursoList) {

        Map<String, Long> tiRecursoMap = new HashMap<String, Long>();
        List<String> tiRecList = new ArrayList<String>();
        List<SelectItem> tiRecSelItemList = new ArrayList<SelectItem>();

        for (TiRecurso tiRecurso : tiRecursoList) {
            tiRecursoMap.put(tiRecurso.getNomeTiRecurso(), tiRecurso
                    .getCodigoTiRecurso());

            tiRecList.add(tiRecurso.getNomeTiRecurso());
        }

        Collections.sort(tiRecList);
        for (String s : tiRecList) {
            tiRecSelItemList.add(new SelectItem(s));
        }

        bean.setTiRecursoMap(tiRecursoMap);
        bean.setTiRecursoList(tiRecList);
        bean.setTiRecursoSelItemList(tiRecSelItemList);
    }

    /**
     * Popula a lista de moedas para combobox.
     *
     * @param moedas
     *            lista de moedas.
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
        List<SelectItem> contratoPraticaItems = new ArrayList<SelectItem>();
        LinkedHashMap<String, Object> contratoPraticas = new LinkedHashMap<String, Object>();


        for (ContratoPratica cp : contratosPratica) {
            contratoPraticaMap.put(cp.getNomeContratoPratica(), cp
                    .getCodigoContratoPratica());
            contratoPraticaList.add(cp.getNomeContratoPratica());

            contratoPraticas.put(cp.getNomeContratoPratica(), cp.getCodigoContratoPratica());
        }

        bean.setContratoPraticaMap(contratoPraticaMap);

        Collections.sort(contratoPraticaList);
        bean.setContratoPraticaList(contratoPraticaList);
        bean.setContratoPraticas(contratoPraticas);
    }

    private void loadContratoPraticaListWithGrupoCusto(
            final List<GrupoCusto> contratosPratica) {

        Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
        List<String> contratoPraticaList = new ArrayList<String>();
        List<SelectItem> contratoPraticaItems = new ArrayList<SelectItem>();
        LinkedHashMap<String, Object> contratoPraticas = new LinkedHashMap<String, Object>();


        for (GrupoCusto cp : contratosPratica) {
            contratoPraticaMap.put(cp.getNomeGrupoCusto(), cp
                    .getCodigoGrupoCusto());
            contratoPraticaList.add(cp.getNomeGrupoCusto());

            contratoPraticas.put(cp.getNomeGrupoCusto(), cp.getCodigoGrupoCusto());
        }

        bean.setContratoPraticaMap(contratoPraticaMap);

        Collections.sort(contratoPraticaList);
        bean.setContratoPraticaList(contratoPraticaList);
        bean.setContratoPraticas(contratoPraticas);
    }

    public void prepareInvoiceNumberCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        if (value == null || "".equals(value)) return;
        Long code = (Long) bean.getInvoiceMegaSelect().map().get(value);
        bean.getInvoiceMegaSelect().select(code);
        bean.getInvoiceProjectMegaSelect().select(null);

        InvoiceMegaDTO invoiceMegaDTO = (InvoiceMegaDTO) bean.getInvoiceMegaSelect().entity();

        bean.setCodigoFatura(Long.parseLong(invoiceMegaDTO.getInvoiceNumber()));
        InvoiceProjectMegaSelect list = new InvoiceProjectMegaSelect(loadInvoiceProjectList(invoiceMegaDTO));

        bean.setHasProjects(false);
        if(list.getList().size()>0){
            bean.setHasProjects(true);
        }
        bean.setInvoiceProjectMegaSelect(list);
    }

    private List<InvoiceProjectMegaDTO> loadInvoiceProjectList(InvoiceMegaDTO invoiceMegaDTO) {
        List<InvoiceProjectMegaDTO> invoiceProjectMegaDTOS = invoiceService.findInvoiceProjectByInvoice(invoiceMegaDTO);

        List<LicencaSwProjeto> projetosUtilizados = licencaSwProjetoService.findProjetosUtilizados(Long.parseLong(invoiceMegaDTO.getInvoiceNumber()), invoiceMegaDTO.getAgnCode());

        List<InvoiceProjectMegaDTO> results = new ArrayList<>();

        for(InvoiceProjectMegaDTO invoiceProjectMegaDTO :invoiceProjectMegaDTOS) {
            boolean utilizou = false;
            for (LicencaSwProjeto utilizado: projetosUtilizados) {
                if ((utilizado.getErpProjectCode() != null && utilizado.getErpProjectCode().equals(invoiceProjectMegaDTO.getProjectCode()))) {
                    utilizou = true;
                    break;
                }
            }
            if (!utilizou) {
                results.add(invoiceProjectMegaDTO);
            }
        }

        return results;
    }
    public void prepareSeachInvoiceNumberCombo() {
        loadProjectList();
        loadLicenseID();
    }

    public void prepareSeachInvoiceProjectCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        if (value == null || "".equals(value)) return;
        Long code = (Long) bean.getInvoiceProjectMegaSelect().map().get(value);
        bean.getInvoiceProjectMegaSelect().select(code);

        InvoiceProjectMegaDTO invoiceProjectMegaDTO = (InvoiceProjectMegaDTO) bean.getInvoiceProjectMegaSelect().entity();

        bean.setProject(invoiceProjectMegaDTO.getProjectCode());
        loadLicenseID();
    }

    public void prepareInvoiceProjectCombo(final ValueChangeEvent e) {

        String value = (String) e.getNewValue();
        if (value == null || "".equals(value)) return;
        Long code = (Long) bean.getInvoiceProjectMegaSelect().map().get(value);
        bean.getInvoiceProjectMegaSelect().select(code);

        InvoiceProjectMegaDTO invoiceProjectMegaDTO = (InvoiceProjectMegaDTO) bean.getInvoiceProjectMegaSelect().entity();

        bean.setNomeProduto(invoiceProjectMegaDTO.getProductDescription());
        bean.setEmpresa(getEmpresa(invoiceProjectMegaDTO));

        loadTiRecursoList(tiRecursoService.findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT));

        Convergencia convergencia = getConvergencia(invoiceProjectMegaDTO.getProjectCode());

        if (convergencia.getContratoPratica() != null) {
            ContratoPratica contratoPratica = convergencia.getContratoPratica();
            bean.setContratoPratica(contratoPratica);
            Set<Long> selectedCodes = new HashSet<>();
            selectedCodes.add(contratoPratica.getCodigoContratoPratica());
            bean.setContratoPraticaCodigosSelected(selectedCodes.toArray(new Long[selectedCodes.size()]));
            bean.setIndicadorTipoLicenca(TIPO_LICENSA_PRODUCTION);
            bean.setTipoLicencaReadOnly(TIPO_LICENSA_PRODUCTION);
            bean.setNomeMsa(contratoPratica.getMsa().getNomeMsa());
            bean.setClientReadOnly(contratoPratica.getMsa().getNomeMsa());
        } else if (convergencia.getGrupoCusto() != null) {
            GrupoCusto grupoCusto = convergencia.getGrupoCusto();
            bean.setGrupoCusto(grupoCusto);
            Set<Long> selectedCodes = new HashSet<>();
            selectedCodes.add(grupoCusto.getCodigoGrupoCusto());
            bean.setContratoPraticaCodigosSelected(selectedCodes.toArray(new Long[selectedCodes.size()]));
            bean.setIndicadorTipoLicenca(TIPO_LICENSA_INTERNAL);
            bean.setTipoLicencaReadOnly(TIPO_LICENSA_INTERNAL);
            bean.setNomeMsa("");
            bean.setClientReadOnly("");
        }

        bean.setNomeFornecedor(invoiceProjectMegaDTO.getProviderName());
        bean.setCodigoFornecedor(invoiceProjectMegaDTO.getProviderCode());
        bean.setCodigoProjetoErp(invoiceProjectMegaDTO.getProjectCode());
        bean.setValorLicenca(invoiceProjectMegaDTO.getProjectValue());
        bean.setDescricao(invoiceProjectMegaDTO.getProductDescription());

    }

    public void prepareCalculateInstallments(final ValueChangeEvent e) {
        Integer value = (Integer) e.getNewValue();
        if (value == null || value.equals(0)) return;
        bean.setValorParcela(bean.getValorLicenca().divide(BigDecimal.valueOf(value), 2, RoundingMode.HALF_UP));
    }

    private Convergencia getConvergencia(Long projectCode) {
        List<Convergencia> convergencias = convergenciaService.findConvergenciaByProjeto(projectCode);
        if (convergencias.isEmpty()) throw new ValidatorException(Messages.getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND, "Project"));
        return convergencias.get(0);
    }

    private Empresa getEmpresa(InvoiceProjectMegaDTO invoiceProjectMegaDTO) {
        Empresa empresa = empresaService.findEmpresaByERP(invoiceProjectMegaDTO.getMegaBranchCode());
        return empresa;
    }

    public void prepareContratoPraticaCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        Msa msa = new Msa();
        msa.setCodigoMsa(bean.getMsaMap().get(value));
        List<ContratoPratica> contratoPraticas = contratoPraticaService.findContratoPraticaByContrato(msa);
        this.loadContratoPraticaList(contratoPraticas);
    }

    public void prepareTipoAlocacaoCombo(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        bean.getFilterPess().getPessoa().setCodigoLogin("");
        bean.setCodigoProcurify("");
        bean.setIsNotasVaziasOnly(false);

        if (value != null) {
            this.loadTiRecursoSelItemLitByTipoAlocacao(value);

            bean.setIndicadorTipoAlocacao(value);
        }
    }

    public void prepareTipoLicenca(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        bean.setIndicadorTipoLicenca(value);
        if (value.equals(TIPO_LICENSA_INTERNAL)) {
            bean.setNomeMsa("");
            List<GrupoCusto> gcs = grupoCustoService.findAllActiveReturnCodigoAndNomeGrupoCusto();
            this.loadContratoPraticaListWithGrupoCusto(gcs);
        } else {
            this.loadMsaList(msaService.findMsaAll());
            this.loadContratoPraticaListWithGrupoCusto(new ArrayList<GrupoCusto>());
        }
    }

    private void loadTiRecursoSelItemLitByTipoAlocacao(String indicadorTipoAlocacao) {
        this.loadTiRecursoList(tiRecursoService.findTiRecursoByType(indicadorTipoAlocacao));
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

        Collections.sort(msaList);
        bean.setMsaList(msaList);
    }

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
     * @return the bean
     */
    public ChargebackBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final ChargebackBean bean) {
        this.bean = bean;
    }

    /**
     * Reseta os valores das progressBars.
     */
    public void resetBar() {
        bean.resetBar();
    }

    /**
     * Realiza a valida�ao do campo Login.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validatePessoa(final FacesContext context,
            final UIComponent component, final Object value) {

        String login = (String) value;

        if ((login != null) && (!"".equals(login))) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(login);

            if (pessoa == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }
    }

    /**
     * A��o utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
     * 
     * @param value
     *            - valor (login) utilizado na busca das Pessoas
     * 
     * @return retorna uma lista de recurso
     */
    public List<Pessoa> autoCompletePessoa(final Object value) {
        return pessoaService.findPessoaByLikeLogin((String) value);
    }

    /**
     * Verifica se algum item foi selecionado.
     * 
     * @return true se algum item selecionado, caso contrario retorna false.
     */
    private Boolean isSomeChargebackRowSelected() {
        List<ChargebackRow> chargebackRowList = bean.getChargebackRowList();
        for (ChargebackRow chbackRow : chargebackRowList) {
            if (chbackRow.getIsSelected()) {
                return Boolean.valueOf(true);
            }
        }

        return Boolean.valueOf(false);
    }

    public String prepareView() {
        LicencaSwProjetoParcela licencaSwProjetoParcela = findLicencaProjetoParcelaById(bean.getCurrentParcelaId());
        LicencaSwProjetoRow row = new LicencaSwProjetoRow();

        BigDecimal valorParcela = licencaSwProjetoParcelaService.findInstallmentValue(licencaSwProjetoParcela.getDataParcela(), licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto(), licencaSwProjetoParcela.getNumeroParcela());
        String nomeLicenca = licencaSwProjetoParcela.getLicencaSwProjeto().getDescricao();
        if (licencaSwProjetoParcela.getLicencaSwProjeto().getTiRecurso() != null) {
            nomeLicenca = licencaSwProjetoParcela.getLicencaSwProjeto().getTiRecurso().getNomeTiRecurso();
        }

        row.setNomeProjeto(getNomeProjeto(licencaSwProjetoParcela));
        row.setValorParcela(licencaSwProjetoParcela.getValorParcela());
        row.setMoeda(licencaSwProjetoParcela.getLicencaSwProjeto().getMoeda());
        row.setEmpresa(licencaSwProjetoParcela.getLicencaSwProjeto().getEmpresa());
        row.setNomeFornecedor(licencaSwProjetoParcela.getLicencaSwProjeto().getProviderName());
        row.setNomeLicenca(nomeLicenca);
        row.setCodigoLicencaSwProjetoParcela(licencaSwProjetoParcela.getCodigoLicencaSwProjetoParcela());
        row.setCodigoLicencaSwProjeto(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto());
        row.setNotaFiscal(licencaSwProjetoParcela.getLicencaSwProjeto().getNotaFiscal());
        row.setParcelaApropriada(licencaSwProjetoParcela.getNumeroParcela());
        row.setStatus(licencaSwProjetoParcela.getStatus());
        row.setTextoErro(licencaSwProjetoParcela.getTextoError());

        List<LicencaSwProjetoParcela> licencaSwProjetoParcelas = licencaSwProjetoParcelaService.findByLicencaSwProjetoAndStatus(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto(), LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName());
        row.setValorApropriacao(licencaSwProjetoParcelaService.sumValorLicencaSwProjetoParcela(licencaSwProjetoParcelas));

        row.setQtdeParcelas(licencaSwProjetoParcela.getLicencaSwProjeto().getQuantidadeParcela());
        row.setSaldoParcelas(licencaSwProjetoParcelaService.findBalanceByLicencaSwProjetoAndMonth(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto()));
        row.setDataParcela(licencaSwProjetoParcela.getDataParcela());
        row.setDataInicio(licencaSwProjetoParcela.getLicencaSwProjeto().getDataInicio());
        row.setValorTotal(licencaSwProjetoParcela.getLicencaSwProjeto().getValorTotal());
        row.setValorParcial(valorParcela);
        row.setIsEditable(licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                || licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName()));
        row.setIsAppropriate(checkIsAppropriate(licencaSwProjetoParcela));
        row.setHasIntegratedInstallments(false);

        List<LicencaSwProjetoParcela> licencasProjeto = licencaSwProjetoParcelaService.findByLicencaSwProjeto(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto());
        for (LicencaSwProjetoParcela swProjetoParcela : licencasProjeto) {
            if (swProjetoParcela.getStatus().equals(LicencaSwProjetoParcela.Status.INTEGRADO.getFieldName())
                    || swProjetoParcela.getStatus().equals(LicencaSwProjetoParcela.Status.PENDENTE.getFieldName())) {
                row.setHasIntegratedInstallments(true);
            }
        }

        integrarLicencaSoftwareBean.setToProject(row);
        integrarLicencaSoftwareBean.setTelaIntegrate(false);
        integrarLicencaSoftwareBean.setTipoRecurso(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT);

        List<LicencaSwProjetoParcela> licencaSwProjetoParcelaList = licencaSwProjetoParcelaService.findByLicencaSwProjeto(licencaSwProjetoParcela.getLicencaSwProjeto().getCodigoLicencaSwProjeto());
        List<LicencaSwDetail> detailList = licencaSwDetailService.convertLicencaSwParcela(licencaSwProjetoParcelaList);

        Set<String> loginsLicenca = new HashSet<>();
        List<LicencaSwProjetoPessoa> licencaSwProjetoPessoas = licencaSwProjetoPessoaService.findByLicencaSwProjeto(licencaSwProjetoParcela.getLicencaSwProjeto());

        for (LicencaSwProjetoPessoa licencaSwProjetoPessoa : licencaSwProjetoPessoas) {
            loginsLicenca.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin());
        }

        integrarLicencaSoftwareBean.getToProject().setLogins(getLoginsLicenca(loginsLicenca));
        integrarLicencaSoftwareBean.setLicencaSwProjetoRowDetailList(detailList);
        integrarLicencaSoftwareBean.setPreviousLicencaSwProjetoRowDetailList(null);

        return OUTCOME_INTEGRATE_VIEW;
    }

    private LicencaSwProjetoParcela findLicencaProjetoParcelaById(Long id) {
        return licencaSwProjetoParcelaService.findById(id);
    }

    private String getNomeProjeto(LicencaSwProjetoParcela licencaSwProjetoParcela) {
        if (licencaSwProjetoParcela.getContratoPratica() != null) {
            return licencaSwProjetoParcela.getContratoPratica().getNomeContratoPratica();
        }
        if (licencaSwProjetoParcela.getGrupoCusto() != null) {
            return licencaSwProjetoParcela.getGrupoCusto().getNomeGrupoCusto();
        }
        return "not found";
    }

    private Boolean checkIsAppropriate(LicencaSwProjetoParcela licencaSwProjetoParcela) {
        if (Boolean.TRUE.equals(DateUtil.after(licencaSwProjetoParcela.getDataParcela(), moduloService.getClosingDateChargeBack()))) {
            if (licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                    || licencaSwProjetoParcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                if (!licencaSwProjetoParcela.getLicencaSwProjeto().getQuantidadeParcela().equals(licencaSwProjetoParcela.getNumeroParcela())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Prepara a tela de edicao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareLicencaProjetoUpdate() {

        findLicencaProjectById(bean.getCurrentRowId());
        bean.setIsUpdate(Boolean.valueOf(true));
        Date installmentDate = null;

        Date dataMes = bean.getToLicencaSwProjeto().getDataInicio();
        bean.setMonthBeg(DateUtil.getMonthString(dataMes));
        bean.setYearBeg(DateUtil.getYearString(dataMes));

        bean.setStartDate(bean.getToLicencaSwProjeto().getDataInicio());

        this.loadTiRecursoList(tiRecursoService
                .findTiRecursoByType(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT));
        List<LicencaSwProjetoParcela> licencaSwParcelas = licencaSwProjetoParcelaService.findByLicencaSwProjeto(bean.getCurrentRowId());

        bean.setNomeTiRecurso(bean.getToLicencaSwProjeto().getTiRecurso() == null ? bean.getToLicencaSwProjeto().getTiRecursoErpName(): bean.getToLicencaSwProjeto().getTiRecurso().getNomeTiRecurso());
        bean.setTicketAtendimento(bean.getToLicencaSwProjeto().getTicketId());
        bean.setCodigoFatura(bean.getToLicencaSwProjeto().getNotaFiscal());
        bean.setDescricao(bean.getToLicencaSwProjeto().getDescricao());
        bean.setNomeMoeda(bean.getToLicencaSwProjeto().getMoeda().getNomeMoeda());
        bean.setValorLicenca(bean.getToLicencaSwProjeto().getValorTotal());
        bean.setParcelasLicenca(bean.getToLicencaSwProjeto().getQuantidadeParcela());
        this.loadMoedaList(moedaService.findMoedaAll());
        bean.setEmpresa(bean.getToLicencaSwProjeto().getEmpresa());
        bean.setNomeFornecedor(bean.getToLicencaSwProjeto().getProviderName());
        bean.setResourceName(bean.getToLicencaSwProjeto().getTiRecursoErpName());
        bean.setCodigoFornecedor(bean.getToLicencaSwProjeto().getProviderCode());
        bean.setCodigoProjetoErp(bean.getToLicencaSwProjeto().getErpProjectCode());
        bean.setNomeProduto(bean.getToLicencaSwProjeto().getTiRecursoErpName());
        bean.setNomeFornecedor(bean.getToLicencaSwProjeto().getProviderName());

        if (bean.getHasIntegratedInstallments()) {
            licencaSwParcelas = licencaSwProjetoParcelaService.getFirstOpenedInstalments(licencaSwParcelas);
        }

        bean.setListLicencaSwProjetoPessoa(licencaSwProjetoPessoaService
                .findByLicencaSwProjeto(bean.getToLicencaSwProjeto()));
        Set<String> loginsLicenca = new HashSet<String>();
        List<String> loginsList = new ArrayList<String>();
        for (LicencaSwProjetoPessoa licencaSwProjetoPessoa : bean.getListLicencaSwProjetoPessoa()) {
            if (licencaSwProjetoPessoa.getPessoa().getDataRescisao() != null) {
                loginsList.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin() + " (Inactive)");
                loginsLicenca.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin() + " (Inactive)");
            }
            else {
                loginsList.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin());
                loginsLicenca.add(licencaSwProjetoPessoa.getPessoa().getCodigoLogin());
            }
        }
        bean.setLogins(loginsList);
        bean.setInstallmentLogins(this.getLoginsLicenca(loginsLicenca));

        List<ContratoPratica> contratoPraticas = new ArrayList<ContratoPratica>();
        List<GrupoCusto> grupoCustos = new ArrayList<GrupoCusto>();
        Set<Long> selectedCodes = new HashSet<Long>();
        Set<String> nomesCpAndCc = new HashSet<String>();
        for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
            if (null != bean.getHasIntegratedInstallments() && bean.getHasIntegratedInstallments()) {
                if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())
                        && (installmentDate == null || parcela.getDataParcela().before(installmentDate))) {
                        installmentDate = parcela.getDataParcela();
                }
            }else{
                installmentDate = bean.getToLicencaSwProjeto().getDataInicio();
            }

            if (parcela.getDataParcela().equals(installmentDate)){
                bean.setInstallmentDate(installmentDate);
                bean.setLicenseID(parcela.getCodigoLicencaSwProjetoParcela());
                bean.setValorParcela(parcela.getValorParcela());
                bean.setInstallmentNumber(parcela.getNumeroParcela());
            }

            if (null != parcela.getContratoPratica()
                    && null != parcela.getContratoPratica().getCodigoContratoPratica()) {
                bean.setIndicadorTipoLicenca(TIPO_LICENSA_PRODUCTION);
                bean.setTipoLicencaReadOnly(TIPO_LICENSA_PRODUCTION);
                contratoPraticas.add(parcela.getContratoPratica());
                bean.setNomeMsa(parcela.getContratoPratica().getMsa().getNomeMsa());
                bean.setClientReadOnly(parcela.getContratoPratica().getMsa().getNomeMsa());
                if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                    selectedCodes.add(parcela.getContratoPratica().getCodigoContratoPratica());
                    nomesCpAndCc.add(parcela.getContratoPratica().getNomeContratoPratica());
                }
            } else if (null != parcela.getGrupoCusto()
                    && null != parcela.getGrupoCusto().getCodigoGrupoCusto()) {
                bean.setIndicadorTipoLicenca(TIPO_LICENSA_INTERNAL);
                bean.setTipoLicencaReadOnly(TIPO_LICENSA_INTERNAL);
                bean.setNomeMsa("");
                bean.setClientReadOnly("");
                grupoCustos.add(parcela.getGrupoCusto());
                if (parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName())
                        || parcela.getStatus().equalsIgnoreCase(LicencaSwProjetoParcela.Status.APROVADO.getFieldName())) {
                    selectedCodes.add(parcela.getGrupoCusto().getCodigoGrupoCusto());
                    nomesCpAndCc.add(parcela.getGrupoCusto().getNomeGrupoCusto());
                }
            }
        }

        bean.setClobNamesReadOnly(this.getClobsOrCostCentersNames(nomesCpAndCc));

        if (null != contratoPraticas && !contratoPraticas.isEmpty()) {
            this.loadMsaList(msaService.findMsaAll());
            this.loadContratoPraticaList(contratoPraticaService.findContratoPraticaByContrato(contratoPraticas.get(0).getMsa()));

            bean.setContratoPraticaCodigosSelected(selectedCodes.toArray(new Long[selectedCodes.size()]));
        } else if (null != grupoCustos && !grupoCustos.isEmpty()) {
            List<GrupoCusto> gcs = grupoCustoService.findAllActiveReturnCodigoAndNomeGrupoCusto();
            this.loadContratoPraticaListWithGrupoCusto(gcs);

            bean.setContratoPraticaCodigosSelected(selectedCodes.toArray(new Long[selectedCodes.size()]));
        }

        return OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT;
    }

    public String prepareLicencaProjetoUpdateFromIntegration(String tipoRecurso, Long codigoLicencaSwProjeto, Boolean hasIntegratedInstallments) {
        bean.setCurrentRowId(codigoLicencaSwProjeto);
        bean.setIsIntegrationOrigin(true);
        bean.setHasIntegratedInstallments(hasIntegratedInstallments);
        String indicadorTipoAlocacao = null;
        if(tipoRecurso.contains(Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT)){
            indicadorTipoAlocacao  = Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT;
        }
        bean.setIndicadorTipoAlocacao(indicadorTipoAlocacao);
        return this.prepareLicencaProjetoUpdate();
    }

    public String prepareLicencaProjetoAppropriation(LicencaSwProjetoRow licencaSwProjetoRow) {
        bean.setCurrentRowId(licencaSwProjetoRow.getCodigoLicencaSwProjeto());
        bean.setCurrentParcelaId(licencaSwProjetoRow.getCodigoLicencaSwProjetoParcela());
        bean.setIsIntegrationOrigin(true);
        bean.setRemainingInstallments((licencaSwProjetoRow.getQtdeParcelas() - licencaSwProjetoRow.getParcelaApropriada()) + 1);
        bean.setTotalToAppropriate(licencaSwProjetoParcelaService.findBalanceToAppropriateByLicencaSwProjetoAndMonth(licencaSwProjetoRow.getCodigoLicencaSwProjeto(), licencaSwProjetoRow.getDataParcela()));
        bean.setHasIntegratedInstallments(licencaSwProjetoRow.getHasIntegratedInstallments());
        return this.prepareLicencaProjetoUpdate();
    }

    private void findLicencaProjectById(final Long id) {
        bean.setToLicencaSwProjeto(licencaSwProjetoService.findById(id));

        if (null == bean.getToLicencaSwProjeto()
                || null == bean.getToLicencaSwProjeto().getCodigoLicencaSwProjeto()) {
            Messages.showWarning("findChbackPessById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Executa um update da entidade de LicencaSwProjeto.
     *
     * @return pagina de destino
     *
     */
    public String updateLicencaProjeto() {
        if (null != bean.getHasIntegratedInstallments() && bean.getHasIntegratedInstallments()) {
            if (Boolean.TRUE.equals(this.validateRequiredFieldsLicencaSwProjeto(false))
                    && Boolean.TRUE.equals(licencaSwProjetoParcelaService.updateParcelasNaoIntegradas(
                            bean.getToLicencaSwProjeto(),
                            bean.getContratoPraticaCodigosSelectedList(),
                            bean.getIndicadorTipoLicenca().equals("INTERNAL")))) {

                licencaSwProjetoPessoaService.updatePeopleLicencaSwProjeto(bean.getLogins(), bean.getToLicencaSwProjeto());

                Messages.showSucess("updateChbackPess", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                        Constants.ENTITY_NAME_IT_CHARGEBACK);

                if (bean.getIsIntegrationOrigin()) {
                    for (LicencaSwProjetoRow licencaSwProjetoRow : integrarLicencaSoftwareBean.getLicencaSwProjetoRowList()) {
                        if (licencaSwProjetoRow.getCodigoLicencaSwProjeto().equals(bean.getToLicencaSwProjeto().getCodigoLicencaSwProjeto())) {
                            licencaSwProjetoRow.setStatus(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName());
                            break;
                        }
                    }
                     return OUTCOME_INTEGRATE_MANAGE;
                } else {
                    this.findByFilterChbackPess();
                    bean.resetChbackProjectSearchFilters();
                    return Objects.equals(bean.getIndicadorTipoAlocacao(), Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT) ? OUTCOME_CHBACK_PESS_SEARCH_PROJECT : OUTCOME_CHBACK_PESS_SEARCH;
                }
            }
        } else {
            if (this.validateRequiredFieldsLicencaSwProjeto(false)) {
                Date startDate = DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg());

                if (!chbackPessService.verifyChbackClosingDate(startDate, Boolean
                        .valueOf(true))) {
                    return OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT;
                }

                LicencaSwProjeto licencaSwProjeto = new LicencaSwProjeto();
                licencaSwProjeto.setCodigoLicencaSwProjeto(bean.getToLicencaSwProjeto().getCodigoLicencaSwProjeto());
                licencaSwProjeto.setMoeda(moedaService.findMoedaById(bean.getMoedaMap().get(bean.getNomeMoeda())));
                licencaSwProjeto.setDescricao(bean.getDescricao());
                licencaSwProjeto.setDataInicio(startDate);
                licencaSwProjeto.setNotaFiscal(bean.getCodigoFatura());
                licencaSwProjeto.setQuantidadeParcela(bean.getParcelasLicenca());
                licencaSwProjeto.setTicketId(bean.getTicketAtendimento());
                licencaSwProjeto.setValorTotal(bean.getValorLicenca());
                licencaSwProjeto.setCodigoProcurify(bean.getCodigoProcurify());
                licencaSwProjeto.setEmpresa(bean.getEmpresa());
                licencaSwProjeto.setProviderCode(bean.getCodigoFornecedor());
                licencaSwProjeto.setErpProjectCode(bean.getCodigoProjetoErp());
                licencaSwProjeto.setTiRecursoErpName(bean.getNomeProduto());
                licencaSwProjeto.setProviderName(bean.getNomeFornecedor());
                Long codigoTiRecurso = bean.getTiRecursoMap().get(bean.getNomeTiRecurso());
                if (codigoTiRecurso != null) {
                    licencaSwProjeto.setTiRecurso(tiRecursoService.findTiRecursoById(codigoTiRecurso));
                }

                if (Boolean.TRUE.equals(licencaSwProjetoService.update(licencaSwProjeto,
                        bean.getContratoPraticaCodigosSelectedList(),
                        bean.getIndicadorTipoLicenca().equals("INTERNAL"), bean.getLogins()))) {
                    bean.resetTo();
                    bean.resetPeriod();
                    bean.setCodigoProcurify("");
                    bean.setContratoPraticaCodigosSelected(new Long[0]);
                    Messages.showSucess("updateChbackPess",
                            Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                            Constants.ENTITY_NAME_IT_CHARGEBACK);

                    if (bean.getIsIntegrationOrigin()) {
                        for (LicencaSwProjetoRow licencaSwProjetoRow : integrarLicencaSoftwareBean.getLicencaSwProjetoRowList()) {
                            if (licencaSwProjetoRow.getCodigoLicencaSwProjeto().equals(licencaSwProjeto.getCodigoLicencaSwProjeto())) {
                                licencaSwProjetoRow.setNomeLicenca(licencaSwProjeto.getTiRecurso().getNomeTiRecurso());
                                licencaSwProjetoRow.setNotaFiscal(licencaSwProjeto.getNotaFiscal());
                                licencaSwProjetoRow.setQtdeParcelas(licencaSwProjeto.getQuantidadeParcela());
                                licencaSwProjetoRow.setStatus(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName());
                                List<LicencaSwProjetoParcela> byLicencaSwProjetoAndMonth = licencaSwProjetoParcelaService.findByLicencaSwProjetoAndMonth(licencaSwProjeto.getCodigoLicencaSwProjeto(), licencaSwProjetoRow.getDataParcela());
                                licencaSwProjetoRow.setValorParcial(LicencaSwProjetoParcela.sumParcelas(byLicencaSwProjetoAndMonth));
                                break;
                            }
                        }
                        return OUTCOME_INTEGRATE_MANAGE;
                    } else {

                        this.findByFilterChbackPess();
                        bean.resetChbackProjectSearchFilters();
                        return Objects.equals(bean.getIndicadorTipoAlocacao(), Constants.TI_RECURSO_TYPE_SOFTWARE_PROJECT) ? OUTCOME_CHBACK_PESS_SEARCH_PROJECT : OUTCOME_CHBACK_PESS_SEARCH;
                    }
                } else {
                    Messages.showError("updateChbackPess",
                            Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                            Constants.ENTITY_NAME_IT_CHARGEBACK);


                    return OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT;
                }
            }
        }

        return OUTCOME_CHBACK_PESS_EDIT_BY_CONTRACT;
    }

    private void updateLicencaSwProjetoPessoas(LicencaSwProjeto licencaSwProjeto) {

    }

    public String appropriateInstallments() {
        LicencaSwProjeto currentlicencaSwProjeto = licencaSwProjetoService.findById(bean.getCurrentRowId());
        LicencaSwProjetoParcela current = licencaSwProjetoParcelaService.findById(bean.getCurrentParcelaId());
        List<LicencaSwProjetoParcela> licencaSwParcelas = licencaSwProjetoParcelaService.findByLicencaSwProjeto(bean.getCurrentRowId());
        Integer divisorByProjectCount = getCountProjectsOrCostCenter(licencaSwParcelas);
        BigDecimal newValorParcela = new BigDecimal(0L);
        for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
            if (parcela.getNumeroParcela().equals(current.getNumeroParcela())) {

                newValorParcela = bean.getTotalToAppropriate().divide(new BigDecimal(divisorByProjectCount));
                parcela.setValorParcela(newValorParcela);
                parcela.setStatus(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.getFieldName());
                licencaSwProjetoParcelaService.update(parcela);

                currentlicencaSwProjeto.setQuantidadeParcela(current.getNumeroParcela());
                licencaSwProjetoService.update(currentlicencaSwProjeto);
            } else if (parcela.getNumeroParcela() > current.getNumeroParcela()) {
                licencaSwProjetoParcelaService.remove(parcela);
            }
        }

        updateIsAppropriateFromLicencaSwProjetoRow(currentlicencaSwProjeto.getCodigoLicencaSwProjeto(), newValorParcela.multiply(new BigDecimal(divisorByProjectCount)));

        Messages.showSucess("appropriateChbackPess",
                Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_IT_CHARGEBACK);

        return OUTCOME_INTEGRATE_MANAGE;
    }

    private int getCountProjectsOrCostCenter(List<LicencaSwProjetoParcela> licencaSwParcelas) {
        List<Long> codesDistinct = new ArrayList<Long>(licencaSwParcelas.size());
        for (LicencaSwProjetoParcela parcela : licencaSwParcelas) {
            Long code = parcela.getGrupoCusto() != null ? parcela.getGrupoCusto().getCodigoGrupoCusto() : parcela.getContratoPratica().getCodigoContratoPratica();
           if (!codesDistinct.contains(code)){
               codesDistinct.add(code);
           }
        }
        return codesDistinct.size();
    }

    private void updateIsAppropriateFromLicencaSwProjetoRow(Long codigoLicencaSwProjeto, BigDecimal newValorParcial) {
        for (LicencaSwProjetoRow licencaSwProjetoRow : integrarLicencaSoftwareBean.getLicencaSwProjetoRowList()) {
            if (licencaSwProjetoRow.getCodigoLicencaSwProjeto().equals(codigoLicencaSwProjeto)) {
                licencaSwProjetoRow.setIsAppropriate(false);
                licencaSwProjetoRow.setStatus(LicencaSwProjetoParcela.Status.AGUARDANDO_APROVACAO.toString());
                licencaSwProjetoRow.setValorParcial(newValorParcial);
            }
        }
    }

    private Boolean validateRequiredFieldsLicencaSwProjeto(boolean isCreateByProject) {
        if (null == bean.getMonthBeg() || bean.getMonthBeg().equalsIgnoreCase("")) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_BEGIN_MONTH_REQUIRED);
            return false;
        }
        if (null == bean.getYearBeg() || bean.getYearBeg().equalsIgnoreCase("")) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_BEGIN_YEAR_REQUIRED);
            return false;
        }
        if ( !isCreateByProject && (null == bean.getNomeTiRecurso() || bean.getNomeTiRecurso().equalsIgnoreCase("")) ) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_IT_RESOURCE_REQUIRED);
            return false;
        }
        if ( !isCreateByProject && (null == bean.getTicketAtendimento() || bean.getTicketAtendimento().equalsIgnoreCase("")) ) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_TICKET_ID_REQUIRED);
            return false;
        }
        if ( !isCreateByProject && (null == bean.getDescricao() || bean.getDescricao().equalsIgnoreCase("")) ) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_DESCRIPTION_REQUIRED);
            return false;
        }
        if (null == bean.getParcelasLicenca() || bean.getParcelasLicenca() <= 0) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_INSTALMENTS_REQUIRED);
            return false;
        }
        if (null == bean.getIndicadorTipoLicenca() || bean.getIndicadorTipoLicenca().equalsIgnoreCase("")) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_TYPE_REQUIRED);
            return false;
        }
        if (bean.getIndicadorTipoLicenca().equalsIgnoreCase("PRODUCTION") && (null == bean.getNomeMsa() || bean.getNomeMsa().equalsIgnoreCase(""))) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_CLIENT_REQUIRED);
            return false;
        }
        if (null == bean.getContratoPraticaCodigosSelected() || bean.getContratoPraticaCodigosSelected().length == 0) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_CLOB_COSTCENTER_REQUIRED);
            return false;
        }
        if (null != bean.getToLicencaSwProjeto() && null != bean.getToLicencaSwProjeto().getCodigoLicencaSwProjeto() && Boolean.TRUE.equals(licencaSwProjetoParcelaService.hasIncorrectStatusAfterOpenedInstallment(bean.getToLicencaSwProjeto().getCodigoLicencaSwProjeto()))) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_EDIT_INTEGRATED_INSTALLMENTS);
            return false;
        }

        if (isCreateByProject && bean.getLogins().isEmpty()) {
            Messages.showError("createChbackPess", Constants.CHBACK_PESS_MSG_ERROR_LOGINS);
            return false;
        }

        return true;
    }

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
        }
    }

    public List<Recurso> autoCompleteRecurso(final Object value) {
        String tipoRecurso = Constants.RESOURCE_TYPE_PE;
        return recursoService.quickSearch((String) value, tipoRecurso);
    }

    public void addPersonLicencaSwProjeto() {
        List<String> listLogins = bean.getLogins();

        if (!listLogins.contains(bean.getLogin()) && !listLogins.contains(bean.getLogin() + " (Inactive)")) {
            listLogins.add(bean.getLogin());
            bean.setLogins(listLogins);
        }

        bean.setLogin("");
    }

    public void removePersonLicencaSwProjeto () {
        List<String> loginList = new ArrayList<String>();
        loginList = bean.getLogins();
        loginList.remove(bean.getLogin());
        bean.setLogins(loginList);
        bean.setLogin("");
    }

    /* Ações para o controle dos logins chargeback no modal */

    /**
     * Método responsável por iniciar o modal para o carregamento
     * dos logins através de upload da planilha.
     */
    public void prepareModalLoginChargeback(){
        Messages.clearMessages();
    }

    /**
     * Método responsável por obter o Evento de Upload e
     * chamar a interface para obter os logins validados.
     *
     */
    public void send(){
        IChargebackWorksheet worksheet = worksheetHandler.findChargebackWorksheet(this.bean.getDataLoginChargeback().getType());
        this.bean.setLoginsChargeback(worksheet.readLoginsFromWorksheet(this.bean.getDataLoginChargeback().getData()));

        if(this.bean.getLoginsChargeback() == null || this.bean.getLoginsChargeback().isEmpty()){
            Messages.showError("loginsChback", Constants.MSG_ERROR_MODULO_CHARGEBACK_UPLOAD_FILE_LOGINS);
            return;
        }

        verifyErrorLogins();
    }

    /**
     * Método responsável por remover um login incorreto de acordo
     * com o objeto passado por parâmetro
     *
     */
    public void removeIncorrectLogin(){
        if(this.bean.getLoginsChargeback() != null && this.bean.getIdLoginToRemove() != null) {
            LoginChargebackPojo login = getLoginById(this.bean.getIdLoginToRemove());
            if(login != null){
                this.bean.getLoginsChargeback().remove(login);
                verifyErrorLogins();
            }
        }
    }

    /**
     * Método responsável por remover todos os logins incorretos.
     *
     */
    public void removeAllIncorrectLogins(){

        if(this.bean.getLoginsChargeback() == null || this.bean.getLoginsChargeback().isEmpty())
            return;

        List<LoginChargebackPojo> logins = new ArrayList<LoginChargebackPojo>(this.bean.getLoginsChargeback());
        for (LoginChargebackPojo login: logins) {
            if(!login.isCorrect())
                this.bean.getLoginsChargeback().remove(login);
        }

        verifyErrorLogins();
    }

    /**
     * Método responsável por criar a planilha com  todos
     * os logins não commitados.
     */
    public String downloadNonCommitedLogins(){
        return download("Logins Not Commited.xls");
    }

    /**
     * Método responsável por criar a planilha com  todos
     * os logins incorretos.
     */
    public String downloadIncorrectLogins(){
        return download("Incorrect logins.xls");
    }

    /**
     * Método responsável por criar a planilha com  todos
     * os logins que deram erros.
     */
    private String download(String filename){
        try{
            IChargebackWorksheet worksheet = worksheetHandler.findChargebackWorksheet(this.bean.getDataLoginChargeback().getType());

            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

            response.reset();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            worksheet.createErrorLoginsWorksheet(getIncorectLoginsChargeback(), response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();

            context.responseComplete();

        }catch (Exception e){
           logger.error("Error trying to download incorrect logins.", e);
        }

        return OUTCOME_CHBACK_PESS_ADD;
    }

    /**
     * Método responsável por gerar a ação final de importação dos logins.
     */
    public String saveLoginsChargeback(){

        if(this.bean.getLoginsChargeback() != null && !this.bean.getLoginsChargeback().isEmpty())
            this.bean.setHasMultipleLogins(Boolean.TRUE);

        return OUTCOME_CHBACK_PESS_ADD;
    }

    /**
     *
     * @return
     */
    public String discardLoginsChargeback(){
        this.bean.resetLoginsChargeback();
        return  OUTCOME_CHBACK_PESS_ADD;
    }

    /**
     *
     * @param event
     * @throws IOException
     */
    public void uploadFileListener(final UploadEvent event) throws IOException {

        UploadItem uploadFile = event.getUploadItem();
        if(uploadFile == null || uploadFile.getData() == null)
            return;

        this.bean.getDataLoginChargeback().setName(uploadFile.getFileName());
        this.bean.getDataLoginChargeback().setType(uploadFile.getContentType());
        this.bean.getDataLoginChargeback().setData(uploadFile.getData());
        this.bean.getDataLoginChargeback().setSize(getFileSizeKb(uploadFile.getFileSize()));
        this.bean.getDataLoginChargeback().setExtension(worksheetHandler.findExtension(uploadFile.getContentType()));
        this.bean.getDataLoginChargeback().setUpload(Boolean.TRUE);
    }

    /**
     * Método que trabalha nas mensagens de logins incorretos.
     */
    private void verifyErrorLogins(){
        Messages.clearMessages();

        this.bean.setHasErrorLoginsChargeback(hasIncorrectLoginsChargeback());
        if(this.bean.getHasErrorLoginsChargeback())
            Messages.showError("loginsChback", Constants.MSG_ERROR_MODULO_CHARGEBACK_FILE_HAS_LOGINS_INCORRECT);

    }

    /**
     * Método reponsável por informar se há algum login
     * incorreto na lista.
     *
     * @return Boolean
     */
    private Boolean hasIncorrectLoginsChargeback(){

        if(this.bean.getLoginsChargeback() != null && !this.bean.getLoginsChargeback().isEmpty()){
            for (LoginChargebackPojo login : this.bean.getLoginsChargeback()) {
                if(!login.isCorrect())
                    return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     *
     * @param idLogin
     * @return
     */
    private LoginChargebackPojo getLoginById(Long idLogin){

        for (LoginChargebackPojo login: this.bean.getLoginsChargeback()) {
            if(idLogin.equals(login.getId()))
                return login;
        }

        return null;
    }

    /**
     *
     * @return
     */
    private List<LoginChargebackPojo> getIncorectLoginsChargeback(){

        if(!this.bean.getHasErrorLoginsChargeback())
            return null;

        List<LoginChargebackPojo> list = new ArrayList<LoginChargebackPojo>();
        for (LoginChargebackPojo login :this.bean.getLoginsChargeback()) {
            if(!login.isCorrect())
                list.add(login);
        }

        return list;
    }

    /**
     *
     * @param filesize
     * @return
     */
    private int getFileSizeKb(int filesize){
        if(filesize <= 0)
            return 0;

        int sizeKB = filesize / 1024;
        return sizeKB + (filesize % 1024 == 0 ? 0 : 1);
    }
}