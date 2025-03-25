package com.ciandt.pms.control.jsf;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAlocacaoPeriodoService;
import com.ciandt.pms.business.service.IAnexoTabelaPrecoService;
import com.ciandt.pms.business.service.IBmDnService;
import com.ciandt.pms.business.service.ICargoService;
import com.ciandt.pms.business.service.ICidadeBaseService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IDealFiscalService;
import com.ciandt.pms.business.service.IDocumentoLegalAnexoArquivoService;
import com.ciandt.pms.business.service.IDocumentoLegalResponsavelService;
import com.ciandt.pms.business.service.IDocumentoLegalService;
import com.ciandt.pms.business.service.IDocumentoLegalTipoService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.business.service.IHistoricoPercentualIntercompService;
import com.ciandt.pms.business.service.IIndustryTypeService;
import com.ciandt.pms.business.service.IInterCompanyConfigService;
import com.ciandt.pms.business.service.IItemPriceTableHistoryService;
import com.ciandt.pms.business.service.IItemTabelaPrecoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IMsaChargeMethodService;
import com.ciandt.pms.business.service.IMsaCommercialPartnerService;
import com.ciandt.pms.business.service.IMsaContratoCnpjService;
import com.ciandt.pms.business.service.IMsaContratoFilialService;
import com.ciandt.pms.business.service.IMsaContratoService;
import com.ciandt.pms.business.service.IMsaContratoTipoServicoService;
import com.ciandt.pms.business.service.IMsaSaldoMoedaService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.IMsaStatusContratoService;
import com.ciandt.pms.business.service.IMsaTipoContratoService;
import com.ciandt.pms.business.service.IMsaTipoMontanteDespesaService;
import com.ciandt.pms.business.service.IPaymentConditionDealFiscalService;
import com.ciandt.pms.business.service.IPaymentConditionService;
import com.ciandt.pms.business.service.IPerfilPadraoService;
import com.ciandt.pms.business.service.IPerfilSistemaService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IPmgService;
import com.ciandt.pms.business.service.IPriceTableApproverService;
import com.ciandt.pms.business.service.IPriceTableEditorService;
import com.ciandt.pms.business.service.IPriceTableHistoryService;
import com.ciandt.pms.business.service.IPriceTableStatusService;
import com.ciandt.pms.business.service.IReceitaService;
import com.ciandt.pms.business.service.IRecursoService;
import com.ciandt.pms.business.service.ITabelaPrecoService;
import com.ciandt.pms.business.service.ITipoServicoDealFiscalService;
import com.ciandt.pms.business.service.ITipoServicoService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.comparator.EmpresaNomeEmpresaComparator;
import com.ciandt.pms.control.jsf.bean.AnexoTabelaPrecoBean;
import com.ciandt.pms.control.jsf.bean.DocumentoLegalBean;
import com.ciandt.pms.control.jsf.bean.DocumentoLegalConfigureBean;
import com.ciandt.pms.control.jsf.bean.ItemTabelaPrecoBean;
import com.ciandt.pms.control.jsf.bean.MsaBean;
import com.ciandt.pms.control.jsf.bean.MsaContratoBean;
import com.ciandt.pms.control.jsf.bean.MsaContratoCnpjBean;
import com.ciandt.pms.control.jsf.bean.PerfilVendidoBean;
import com.ciandt.pms.control.jsf.bean.PriceTableHistoryBean;
import com.ciandt.pms.control.jsf.bean.TabelaPrecoBean;
import com.ciandt.pms.control.jsf.components.impl.BmDnSelect;
import com.ciandt.pms.control.jsf.components.impl.IndustrySelect;
import com.ciandt.pms.control.jsf.components.impl.PriceTableStatusSelect;
import com.ciandt.pms.control.jsf.components.impl.TabelaPrecoSelect;
import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableFlow;
import com.ciandt.pms.control.jsf.interfaces.priceTable.handler.PriceTableFlowHandler;
import com.ciandt.pms.control.jsf.interfaces.priceTable.history.FlowHistory;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.enums.StatusDocumentoLegal;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.metrics.PrometheusMetrics;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import com.ciandt.pms.model.vo.MsaSaldoMoedaRow;
import com.ciandt.pms.model.vo.PaymentCondition;
import com.ciandt.pms.model.vo.UserProfile;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.FacesUtil;
import com.ciandt.pms.util.LoginUtil;
import com.ciandt.pms.util.NumberUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.persistence.Transient;
import static org.apache.commons.lang.StringUtils.deleteWhitespace;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"BUS.MSA:VW", "BUS.MSA:ED", "BUS.MSA:DE", "BUS.MSA:CR", "BUS.MSA.BACKOFFICE:ED", "BUS.MSA.SETTINGS:ED"})
public class MsaController {


    /** Logger. */
    private static final Log logger = LogFactory.getLog(MsaController.class.getName());


    /**
     * outcome tela pesquisa da entidade.
     */
    private static final String OUTCOME_MSA_SEARCH = "msa_search";

    /*********** SERVICES **************************/
    /**
     * outcome tela criacao da entidade.
     */
    private static final String OUTCOME_MSA_ADD = "msa_add";
    /**
     * outcome tela edi��o da entidade.
     */
    private static final String OUTCOME_MSA_EDIT = "msa_edit";
    /**
     * outcome da tela deleta da entidade.
     */
    private static final String OUTCOME_MSA_DELETE = "msa_delete";
    /**
     * outcome tela manage da entidade.
     */
    private static final String OUTCOME_MSA_MANAGE = "msa_manage";
    /**
     * outcome tela consulta da entidade.
     */
    private static final String OUTCOME_MSA_VIEW = "msa_view";
    /**
     * outcome tela view.
     */
    private static final String OUTCOME_ITEM_PRICE_TABLE_VIEW = "itemPriceTable_view";

    /**
     * outcome tela view dsabled.
     */
    private static final String OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED = "itemPriceTable_view_disabled";

    /**
     * outcome tela item price table history view.
     */
    private static final String OUTCOME_ITEM_PRICE_TABLE_HISTORY = "itemPriceTable_history_view";

    /**
     * outcome tela configuracao do documento legal.
     */
    private static final String OUTCOME_CONFIGURE_DOCUMENTO_LEGAL = "legal_doc_configure";
    /**
     * outcome tela consulta do Fiscal Deal.
     */
    private static final String OUTCOME_FISCAL_DEAL_VIEW = "fiscal_deal_view";

    private static final long STATUS_DRAFT = 1L;

    private static final String UPDATE_PERFIL_VENDIDO = "updatePerfilVendido";


    /**
     * outcome tela de configure da entidade.
     */
    private static final String OUTCOME_MSA_UPLOAD_LEGAL_DOC = "msa_uploadLegalDoc";



    /**
     * System Properties.
     */
    @Autowired
    private Properties systemProperties;
    /**
     * Instancia dos servicos.
     */
    @Autowired
    private IMsaService msaService;
    /**
     * Instancia dos servicos da entidade Cliente.
     */
    @Autowired
    private IClienteService clienteService;

    /** Instancia dos servicos da entidade Industry Type. */
    @Autowired
    private IIndustryTypeService industryTypeService;

    /** Instancia dos servicos da entidade BmDn. */
    @Autowired
    private IBmDnService bmDnService;

    /**
     * Instancia dos servicos da entidade PriceTableStatus.
     */
    @Autowired
    private IPriceTableStatusService priceTableStatusService;


    /**
     * Instancia dos servicos da entidade DealFiscal.
     */
    @Autowired
    private IDealFiscalService dealFiscalService;
    /**
     * Instancia dos servicos da entidade Receita.
     */
    @Autowired
    private IReceitaService receitaService;
    /**
     * Instancia dos servicos da entidade HistoricoPercentualIntercompService.
     */
    @Autowired
    private IHistoricoPercentualIntercompService historicoPercentualIntercompService;
    @Autowired
    private IPaymentConditionDealFiscalService paymentConditionDealFiscalService;
    @Autowired
    private IPaymentConditionService paymentConditionService;
    /**
     * Instancia dos servicos da entidade Empresa.
     */
    @Autowired
    private IEmpresaService empresaService;
    /**
     * Instancia dos servicos da entidade Moeda.
     */
    @Autowired
    private IMoedaService moedaService;
    /**
     * Instancia dos servicos da entidade TipoServico.
     */
    @Autowired
    private ITipoServicoService tipoServicoService;
    /**
     * Instancia do servico cidadeBase.
     */
    @Autowired
    private ICidadeBaseService cidadeBaseService;
    /**
     * Inastancia do servico Pmg.
     */
    @Autowired
    private IPmgService pmgService;
    /**
     * Instancia do servico cargoPms.
     */
    @Autowired
    private ICargoService cargoPmsService;
    /**
     * Instancia do servico perfilVendido.
     */
    @Autowired
    private IPerfilVendidoService perfilVendidoService;
    /**
     * Instancia do servico de PerfilPadrao.
     */
    @Autowired
    private IPerfilPadraoService perfilPadraoService;
    /**
     * Instancia do servico de TabelaPreco.
     */
    @Autowired
    private ITabelaPrecoService tabelaPrecoService;
    /**
     * Instancia do servico de Modulo.
     */
    @Autowired
    private IModuloService moduloService;
    /**
     * Instancia do servico de Uplaod de arquivo.
     */
    @Autowired
    private IUploadArquivoService uploadArquivoService;
    /**
     * Instancia do servico de anexoTabelaPreco.
     */
    @Autowired
    private IAnexoTabelaPrecoService anexoTabelaPrecoService;
    /**
     * Instancia do servico de itemtabelapreco.
     */
    @Autowired
    private IItemTabelaPrecoService itemTabelaPrecoService;
    /**
     * Instancia do servico de MsaSaldoMoeda.
     */
    @Autowired
    private IMsaSaldoMoedaService msaSaldoMoedaService;
    /**
     * Instancia do servico de Pessoa.
     */
    @Autowired
    private IPessoaService pessoaService;
    /**
     * Instancia do servico de DocumentoLegal.
     */
    @Autowired
    private IDocumentoLegalService documentoLegalService;
    /**
     * Instancia do servico de DocumentoLegalTipo.
     */
    @Autowired
    private IDocumentoLegalTipoService documentoLegalTipoService;
    /**
     * Instancia do servico de DocumentoLegalAnexoArquivo.
     */
    @Autowired
    private IDocumentoLegalAnexoArquivoService docLegalAnexoArquivoService;
    /**
     * Instancia do servico de FichaReajusteService.
     */
    @Autowired
    private IFichaReajusteService fichaReajusteService;
    /**
     * Instancia do servico de ControleReajusteService.
     */
    @Autowired
    private IControleReajusteService controleReajusteService;
    /**
     * Instancia de tipoServicoDealFiscalService.
     */
    @Autowired
    private ITipoServicoDealFiscalService tipoServicoDealFiscalService;
    @Autowired
    private IDocumentoLegalResponsavelService documentoLegalResponsavelService;
    @Autowired
    private IPerfilSistemaService perfilSistemaService;
    @Autowired
    private IMsaTipoContratoService msaTipoContratoService;
    /*********** BEANS **************************/
    @Autowired
    private IMsaTipoMontanteDespesaService msaTipoMontanteDespesaService;
    @Autowired
    private IMsaStatusContratoService msaStatusContratoService;
    @Autowired
    private IMsaContratoService msaContratoService;
    @Autowired
    private IMsaContratoFilialService msaContratoFilialService;
    @Autowired
    private IMsaContratoTipoServicoService msaContratoTipoServicoService;
    @Autowired
    private IMsaChargeMethodService msaChargeMethodService;
    @Autowired
    private IMsaContratoCnpjService msaContratoCnpjService;
    @Autowired
    private IMsaCommercialPartnerService msaCommercialPartnerService;
    @Autowired
    private IPriceTableEditorService priceTableEditorService;
    @Autowired
    private IPriceTableApproverService priceTableApproverService;
    @Autowired
    private IRecursoService recursoService;
    /**
     * Instancia do bean.
     */
    @Autowired
    private MsaBean bean = null;
    /**
     * Instancia do bean PerfilVendido.
     */
    @Autowired
    private PerfilVendidoBean perfilVendidoBean = null;

    /*********** OUTCOMES **************************/
    /**
     * Instancia do bean de Tabela de Preco.
     */
    @Autowired
    private TabelaPrecoBean tabelaPrecoBean = null;
    /**
     * Instancia do bean de AnexoTabelaPreco.
     */
    @Autowired
    private AnexoTabelaPrecoBean anexoTabelaPrecoBean = null;
    /**
     * Instancia do bean de ItemTabelaPreco.
     */
    @Autowired
    private ItemTabelaPrecoBean itemTabelaPrecoBean = null;
    /**
     * Instancia do bean de DocumentoLegal (Reajuste).
     */
    @Autowired
    private DocumentoLegalBean documentoLegalBean = null;
    /**
     * Instancia do bean de DocumentoLegalConfigure.
     */
    @Autowired
    private DocumentoLegalConfigureBean documentoLegalConfigureBean = null;
    @Autowired
    private MsaContratoBean msaContratoBean;
    @Autowired
    private MsaContratoCnpjBean msaContratoCnpjBean;
    /**
     * Instancia do servico de InterCompanyConfig.
     */
    @Autowired
    private IInterCompanyConfigService interCompanyConfigService;
    /**
     * Instancia do servico de AlocacaoPeriodoService.
     */
    @Autowired
    private IAlocacaoPeriodoService alocacaoPeriodoService;

    @Autowired
    private PriceTableFlowHandler handler;

    @Autowired
    private IPriceTableHistoryService priceTableHistoryService;

    @Autowired
    private IItemPriceTableHistoryService itemPriceTableHistoryService;

    @Autowired
    private FlowHistory flowHistory;

    @Autowired
    private PriceTableHistoryBean priceTableHistoryBean;

    /**
     * @return the bean
     */
    public MsaBean getBean() {
        return bean;
    }

    /**
     * @param bean the bean to set
     */
    public void setBean(final MsaBean bean) {
        this.bean = bean;
    }

    /**
     * @return the perfilVendidoBean
     */
    public PerfilVendidoBean getPerfilVendidoBean() {
        return perfilVendidoBean;
    }

    @Transient
    private boolean isAdminMsa = validateAdminMsa();

    @Transient
    Date closingDateAllocationMap = null;

    /**
     * @param perfilVendidoBean bean to ser
     */
    public void setPerfilVendidoBean(final PerfilVendidoBean perfilVendidoBean) {
        this.perfilVendidoBean = perfilVendidoBean;
    }

    /**
     * get bean tabela preco.
     *
     * @return bean tabela preco.
     */
    public TabelaPrecoBean getTabelaPrecoBean() {
        return tabelaPrecoBean;
    }

    /**
     * set bean tabela preco.
     *
     * @param tabelaPrecoBean tebela preco bean.
     */
    public void setTabelaPrecoBean(final TabelaPrecoBean tabelaPrecoBean) {
        this.tabelaPrecoBean = tabelaPrecoBean;
    }

    public boolean getIsAdminMsa() {
        return this.isAdminMsa;
    }

    public void setIsAdminMsa(boolean isAdminMsa) {
        this.isAdminMsa = isAdminMsa;
    }

    /**
     * get AnexoTabelaPrecoBean.
     *
     * @return AnextoTabelaPrecoBean.
     */
    public AnexoTabelaPrecoBean getAnexoTabelaPrecoBean() {
        return anexoTabelaPrecoBean;
    }

    /**
     * set AnexoTabelaPrecoBean.
     *
     * @param anexoTabelaPrecoBean anexotabelaprecobean
     */
    public void setAnexoTabelaPrecoBean(
            final AnexoTabelaPrecoBean anexoTabelaPrecoBean) {
        this.anexoTabelaPrecoBean = anexoTabelaPrecoBean;
    }

    /**
     * get ItemTabelaPrecoBean.
     *
     * @return ItemTabelaPrecoBean
     */
    public ItemTabelaPrecoBean getItemTabelaPrecoBean() {
        return itemTabelaPrecoBean;
    }

    /**
     * set ItemTabelaPrecoBean.
     *
     * @param itemTabelaPrecoBean itemtabelaprecobean.
     */
    public void setItemTabelaPrecoBean(
            final ItemTabelaPrecoBean itemTabelaPrecoBean) {
        this.itemTabelaPrecoBean = itemTabelaPrecoBean;
    }

    /**
     * get documentoLegalBean.
     *
     * @return the documentoLegalBean
     */
    public DocumentoLegalBean getDocumentoLegalBean() {
        return documentoLegalBean;
    }

    /**
     * set documentoLegalBean
     *
     * @param documentoLegalBean the documentoLegalBean to set
     */
    public void setDocumentoLegalBean(DocumentoLegalBean documentoLegalBean) {
        this.documentoLegalBean = documentoLegalBean;
    }

    /**
     * @return the documentoLegalConfigureBean
     */
    public DocumentoLegalConfigureBean getDocumentoLegalConfigureBean() {
        return documentoLegalConfigureBean;
    }

    /**
     * @param documentoLegalConfigureBean the documentoLegalConfigureBean to set
     */
    public void setDocumentoLegalConfigureBean(
            DocumentoLegalConfigureBean documentoLegalConfigureBean) {
        this.documentoLegalConfigureBean = documentoLegalConfigureBean;
    }

    /**
     * Prepara a tela de insercao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        bean.getTo().setBmDn(0L);
        loadClienteList(clienteService.findClienteAllClientePai());
        loadIndustryTypeList();
        loadBmDnList();
        bean.setIsUpdate(Boolean.FALSE);
        return OUTCOME_MSA_ADD;
    }

    private void loadIndustryTypeList() {
        try{
            bean.setIndustrySelect(new IndustrySelect(industryTypeService.findAllActive()));
        }catch (BusinessException e){
            bean.setIndustrySelect(new IndustrySelect(Collections.EMPTY_LIST));
            logger.info("List of actives industries types not found: "+ e.getMessage());
        }
    }
    private void loadBmDnList() {
        try{
            bean.setBmDnSelect(new BmDnSelect(bmDnService.findAllActive()));
        }catch (BusinessException e){
            bean.setBmDnSelect(new BmDnSelect(Collections.EMPTY_LIST));
            logger.info("List of actives bmdns not found: "+ e.getMessage());
        }
    }

    /**
     * Prepara a tela de insercao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareView() {
        Msa msa = msaService.findMsaById(bean.getCurrentMsaId());
        bean.setTo(msa);
        bean.setIndicadorReembolsavel(msa.getIndicadorReembolsavel().equalsIgnoreCase("N") ? Boolean.FALSE : Boolean.TRUE);
        bean.setFlagCalculateBusinessDay(getFlagCalculateBusinessDay(msa));
        bean.setFlagRoundUp(getFlagRoundUp(msa));
        bean.getIndustrySelect().select(msa.getIndustryType());
        bean.getBmDnSelect().select(msa.getBmDn());
        bean.enableCheck();

        List<TabelaPreco> tabelaPrecosList =  bean.getTo().getTabelaPrecos();
        if(tabelaPrecosList != null && !tabelaPrecosList.isEmpty()){
            TabelaPreco tabelaPreco = tabelaPrecosList.get(tabelaPrecosList.size() - 1);
            tabelaPreco.setIsGreaterThenClosingDate(tabelaPreco.getDataFimVigencia() != null ? (moduloService.dateAfterHistoryLock(tabelaPreco.getDataInicioVigencia())
            ) : Boolean.FALSE);
            this.loadPriceTableStatusList(tabelaPreco);
        }

        loadClienteList(clienteService.findClienteAllClientePai());
        bean.setIsUpdate(Boolean.FALSE);
        return OUTCOME_MSA_VIEW;
    }

    /**
     * Prepara a tela de insercao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareDealFiscalView() {
        return OUTCOME_FISCAL_DEAL_VIEW;
    }

    private boolean getFlagCalculateBusinessDay(Msa msa) {
        return !(msa.getIndicadorCalculoDiasUteis() == null || !msa.getIndicadorCalculoDiasUteis().equals(Constants.YES));
    }

    private boolean getFlagRoundUp(Msa msa) {
       return !(msa.getIndicadorArredondamentoDiarias() == null || !msa.getIndicadorArredondamentoDiarias().equals(Constants.YES));
    }

    /**
     * Prepara a tela de edicao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareUpdate() {

        findById(bean.getCurrentMsaId());

        Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());
        msa.setIndicadorCompleto(Constants.YES);
        bean.setIndicadorReembolsavel(msa.getIndicadorReembolsavel().equalsIgnoreCase("N") ? Boolean.FALSE : Boolean.TRUE);
        bean.setFlagCalculateBusinessDay(getFlagCalculateBusinessDay(msa));
        bean.setFlagRoundUp(getFlagRoundUp(msa));
        bean.getIndustrySelect().select(msa.getIndustryType());
        bean.getBmDnSelect().select(msa.getBmDn());
        bean.enableCheck();
        // Verifica se msa esta completo ou nao e o atualiza. (Regra para
        // atualizar contratos do PMS antigo)
        if (!this.validateAllTabs(msa)) {
            msa.setIndicadorCompleto(Constants.NO);
            Messages.showWarning("validateAndFinish",
                    Constants.MSA_INCOMPLETO_WARNING_UPDATE);
        }

        try {
            if (msa.getPessoa().getCodigoPessoa() == null) {
                msa.setPessoa(null);
            }
            msaService.updateMsa(msa);
        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[]{
                    Constants.ENTITY_NAME_MSA,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA});
        }

        loadClienteList(clienteService.findClienteAllClientePai());
        bean.setIsUpdate(Boolean.TRUE);
        return OUTCOME_MSA_EDIT;
    }

    /**
     * Prepara a tela de remocao da entidade.
     *
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentMsaId());

        if (bean.getTo() != null) {
            bean.getIndustrySelect().select(bean.getTo().getIndustryType());
        }

        return OUTCOME_MSA_DELETE;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     *
     * @return pagina de destino
     */
    public String prepareSearch() {
        bean.reset();
        loadClienteList(clienteService.findClienteAllClientePai());
        loadBmDnList();
        loadIndustryTypeList();
        return OUTCOME_MSA_SEARCH;
    }

    /**
     * Prepara a tela de manage da entidade.
     *
     * @return pagina de manage da entidade
     */
    public String prepareManage() {

        Msa msa = msaService.findMsaById(bean.getCurrentMsaId());

        msa.setIndicadorCompleto(Constants.YES);

        // Verifica se msa esta completo ou nao e o atualiza. (Regra para
        // atualizar contratos do PMS antigo)
        if (!hasOnlyAdminMsaProfile() && !this.validateAllTabs(msa)) {
            msa.setIndicadorCompleto(Constants.NO);
            Messages.showWarning("validateAndFinish",
                    Constants.MSA_INCOMPLETO_WARNING);
        }

        try {
            msaService.updateMsa(msa);
        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[]{
                    Constants.ENTITY_NAME_MSA,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA});
        }
        prepareTabBudget();
        bean.getBmDnSelect().select(msa.getBmDn());
        bean.getIndustrySelect().select(msa.getIndustryType());
        prepareMsaSettings();
        priceTableHistoryBean.init();

        return OUTCOME_MSA_MANAGE;
    }

    /**
     * Insere uma entidade.
     *
     * @return a p�gina de destino
     */
    public String create() {
        Msa msa = bean.getTo();
        Long clienteId = bean.getClienteMap().get(
                msa.getCliente().getNomeCliente());

        Cliente cli = null;
        if (clienteId != null) {
            cli = clienteService.findClienteById(clienteId);
        }

        Long bmDnId =  bean.getBmDnSelect().value();
        if(bmDnId != null){
            msa.setBmDn(bmDnId);
        }
        Long industryTypeId =  bean.getIndustrySelect().value();
        if(industryTypeId != null){
            msa.setIndustryType(industryTypeId);
        }

        // Se existir cliente, cria o contrato
        if (cli != null) {
            msa.setCliente(cli);
            msa.setIndicadorCompleto(Constants.NO);
            msa.setPessoa(null);
            msa.setIndicadorReembolsavel(bean.getIndicadorReembolsavel() == Boolean.TRUE ? "Y" : "N");
            msa.setIndustryType(bean.getIndustrySelect().value());
            setIndicadoresCalculo(msa);
            if (msaService.createMsa(msa)) {
                Messages.showSucess("create",
                        Constants.DEFAULT_MSG_SUCCESS_CREATE,
                        Constants.ENTITY_NAME_MSA);
                bean.resetTo();

                bean.setCurrentMsaId(msa.getCodigoMsa());
                return prepareManage();
            } else {

                Messages.showError("create",
                        Constants.NAME_OF_MSA_ALREADY_EXISTS);

            }

        } else { // Se o cliente nao existir na base de dados
            Messages.showError("create", Constants.DEFAULT_MSG_ERROR_NOT_FOUND,
                    Constants.ENTITY_NAME_CLIENTE);
        }

        return null;
    }

    private void setIndicadoresCalculo(Msa msa) {
        msa.setIndicadorArredondamentoDiarias(bean.getFlagRoundUp() ? Constants.YES : Constants.NO);
        msa.setIndicadorCalculoDiasUteis(bean.getFlagCalculateBusinessDay() ? Constants.YES : Constants.NO);
    }

    public void enableRoundup() {
        bean.setFlagCalculateBusinessDay(bean.getFlagCalculateBusinessDay());
    }

    /**
     * Executa um update da entidade.
     *
     * @return pagina de destino
     */
    public String update() {
        Msa msa = bean.getTo();
        Long idCustomer = bean.getClienteMap().get(
                msa.getCliente().getNomeCliente());

        msa.setCliente(clienteService.findClienteById(idCustomer));
        msa.setIndustryType(bean.getIndustrySelect().value());
        msa.setBmDn(bean.getBmDnSelect().value());
        msa.setIndicadorReembolsavel(bean.getIndicadorReembolsavel() == Boolean.TRUE ? "Y" : "N");
        setIndicadoresCalculo(msa);
        try {
            msaService.updateMsa(msa);
        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[]{
                    Constants.ENTITY_NAME_MSA,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA});
            return null;
        }
        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_MSA);
        bean.resetTo();
        findByFilter();
        return OUTCOME_MSA_SEARCH;
    }

    /**
     * Deleta uma entidade.
     *
     * @return pagina de destino
     */
    public String remove() {
        try {
            msaService.removeMsa(msaService.findMsaById(bean.getTo()
                    .getCodigoMsa()));
        } catch (IntegrityConstraintException ice) {
            Messages.showError("remove", ice.getMessage(), new Object[]{
                    Constants.ENTITY_NAME_MSA, ice.getRelatedEntityName()});
            return null;
        }

        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_MSA);
        bean.resetTo();
        findByFilter();
        return OUTCOME_MSA_SEARCH;
    }

    /**
     * Prepara a aba de Budget.
     */
    public void prepareTabBudget() {
        findById(bean.getCurrentMsaId());
        bean.setListMsaSalMoeRow(msaService.getListMsaSaldoMoedaRow(bean
                .getTo()));
    }

    /**
     * Executa um update da Tab Budget.
     */
    public void updateBudget() {
        Msa to = bean.getTo();

        to.setCliente(clienteService.findClienteById(to.getCliente()
                .getCodigoCliente()));

        for (MsaSaldoMoedaRow row : bean.getListMsaSalMoeRow()) {
            if (row.getIsSelected()
                    && row.getMsaSalMoe().getValorOrcamento() == null) {
                Messages.showError("updateTabBudget",
                        Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED, row
                                .getMsaSalMoe().getMoeda().getNomeMoeda());
                return;
            }
        }

        try {
            if (to.getPessoa().getCodigoPessoa() == null) {
                to.setPessoa(null);
            }
            msaService.updateMsaTabBudget(to, bean.getListMsaSalMoeRow());

            Messages.showSucess("updateTabBudget",
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_MSA);

            prepareTabBudget();
        } catch (IntegrityConstraintException ice) {
            Messages.showError("updateTabBudget", ice.getMessage(),
                    new Object[]{Constants.ENTITY_NAME_MSA,
                            Constants.ENTITY_NAME_CONTRATO_PRATICA});
        }
    }

    /**
     * Prepara a aba de DealFiscal.
     */
    public void prepareTabDealFiscal() {
        bean.setFlagCheckBoxDealFiscalList(Boolean.TRUE);
        findById(bean.getCurrentMsaId());

        bean.setPaymentConditionsDealFiscal(null);
        bean.setPaymentConditionName(null);
        bean.setPaymentConditionList(null);

        bean.setTipoServicoListSelected(null);

        bean.resetToDealFiscal();

        // no DealFiscal carrega a lista de clientes filho do cliente pai que
        // está no MSA
        loadClienteList(clienteService.findClienteByClientePai(bean.getTo()
                .getCliente()));

        if(bean.getTo() != null){
            bean.getIndustrySelect().select(bean.getTo().getIndustryType());
            bean.getBmDnSelect().select(bean.getTo().getBmDn());
        }

        loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        loadMoedaList(this.logicaMoedaList(bean.getTo()));
        loadTipoServicoList(tipoServicoService.findTipoServicoAll());

        List<DealFiscal> dealFiscals = dealFiscalService
                .findDealFiscalByMsaAndNotLogicalDelete(bean.getTo());

        List<TabelaPreco> tabelaPrecosList =  bean.getTo().getTabelaPrecos();
        for (TabelaPreco tabelaPreco : tabelaPrecosList) {
            if(tabelaPrecoBean.getPriceTableStatusSelect() == null){
                loadPriceTableStatusList(tabelaPreco);
            }
            tabelaPrecoBean.getPriceTableStatusSelect().select(tabelaPreco.getPriceTableStatus().getCode());
        }

        Collections.sort(dealFiscals, new Comparator<DealFiscal>() {
            @Override
            public int compare(final DealFiscal df1, final DealFiscal df2) {
                return df1.getNomeDealFiscal().compareToIgnoreCase(
                        df2.getNomeDealFiscal());
            }
        });

        bean.setDealFiscalList(dealFiscals);
    }

    /**
     * carrega combo de tipo de servico.
     */
    private void loadTipoServicoList(final List<TipoServico> tipoServicoList) {
        Map<String, Long> tipoServicoMap = new HashMap<String, Long>();
        List<String> tipoServicoCombo = new ArrayList<String>();

        for (TipoServico ts : tipoServicoList) {
            tipoServicoMap.put(ts.getNomeTipoServico(),
                    ts.getCodigoTipoServico());
            tipoServicoCombo.add(ts.getNomeTipoServico());
        }

        bean.setTipoServicoMap(tipoServicoMap);
        bean.setTipoServicoList(tipoServicoCombo);

    }

    /**
     * Realiza o filtro da lista dealFiscal (checkbox Active only).
     */
    public void filterCheckBoxListDealFiscal() {

        List<DealFiscal> listResult = dealFiscalService
                .findDealFiscalByMsa(bean.getTo());

        // Se box estiver marcado, filtra a busca pelos ativos
        if (bean.getFlagCheckBoxDealFiscalList()) {
            listResult = dealFiscalService
                    .findDealFiscalByMsaAndNotLogicalDelete(bean.getTo());
        }

        // Ordena lista
        Collections.sort(listResult, new Comparator<DealFiscal>() {
            @Override
            public int compare(final DealFiscal df1, final DealFiscal df2) {
                return df1.getNomeDealFiscal().compareToIgnoreCase(
                        df2.getNomeDealFiscal());
            }
        });

        bean.setDealFiscalList(listResult);
    }

    /**
     * Realiza o filtro da lista {@link PerfilVendido} (checkbox Active only).
     */
    public void filterCheckBoxListPerfilVendido() {

        List<PerfilVendido> listResult = perfilVendidoService
                .findPerfilVendidoByMsa(bean.getTo());

        if (bean.getIsCheckedSaleProfileActiveOnly()) {
            listResult = perfilVendidoService
                    .findPerfilVendidoByMsaAndNotLogicalDelete(bean.getTo());
        }

        Collections.sort(listResult, new Comparator<PerfilVendido>() {
            @Override
            public int compare(final PerfilVendido pf1, final PerfilVendido pf2) {
                return pf1.getNomePerfilVendido().compareToIgnoreCase(
                        pf2.getNomePerfilVendido());
            }
        });

        perfilVendidoBean.setListaPerfilVendido(this
                .loadListaPerfilVendido(listResult));
    }

    /**
     * Realiza o filtro da lista {@link TabelaPreco} (checkbox Active only).
     */
    public void filterCheckBoxListPriceTable() {
        List<TabelaPreco> listResult = tabelaPrecoService
                .findTabelaPrecoByMsa(bean.getTo());

        if (bean.getIsCheckedPriceTableActiveOnly()) {
            listResult = tabelaPrecoService.findTabelaPrecoByMsaAndActive(bean
                    .getTo());
        }

        if(listResult != null && !listResult.isEmpty()){
            for(TabelaPreco tabelaPreco : listResult){
                setControlEnableComponents(tabelaPreco);
            }
        }

        tabelaPrecoBean.setResultList(listResult);
    }

    /**
     * Prepara a aba de View do DealFiscal.
     */
    public void prepareTabDealFiscalView() {
        findById(bean.getCurrentMsaId());

        bean.setFlagCheckBoxDealFiscalList(Boolean.TRUE);

        List<DealFiscal> dealFiscals = dealFiscalService
                .findDealFiscalByMsaAndNotLogicalDelete(bean.getTo());
        Collections.sort(dealFiscals, new Comparator<DealFiscal>() {
            @Override
            public int compare(final DealFiscal df1, final DealFiscal df2) {
                return df1.getNomeDealFiscal().compareToIgnoreCase(
                        df2.getNomeDealFiscal());
            }
        });

        bean.setDealFiscalList(dealFiscals);
    }

    /**
     * Abre o form para criação de Deal Fiscal.
     */
    public void newDealFiscal() {
        bean.resetToDealFiscal();
        bean.setTipoServicoSelected(null);
        bean.setIsCreate(Boolean.TRUE);
    }

    /**
     * Executa um create de DealFiscal.
     */
    public void createDealFiscal() {

        if (this.validatePaymentCondition()) {
            this.showPaymentConditionError();
            return;
        }

        Msa to = bean.getTo();
        Long numSeq = dealFiscalService.findDealFiscalMaxByMsa(to);
        if (numSeq == null) {
            numSeq = 1L;
        }

        DealFiscal toDealFiscal = getDfCommonFieldsFilled();

        toDealFiscal.setIndicadorStatus(Constants.ACTIVE);
        toDealFiscal.setNumeroSequencia(++numSeq);
        toDealFiscal.setIndicadorDeleteLogico(Constants.NO);

        dealFiscalService.createDealFiscal(toDealFiscal);
        historicoPercentualIntercompService.create(toDealFiscal.getHistoricoPercentualIntercomps());
        associatePaymentCondition();

        Messages.showSucess("createDealFiscal",
                Constants.DEFAULT_MSG_SUCCESS_CREATE,
                Constants.ENTITY_NAME_DEAL);

        TipoServicoDealFiscal t = new TipoServicoDealFiscal();
        TipoServicoDealFiscalId id = new TipoServicoDealFiscalId();
        DealFiscal deal = dealFiscalService.findDealFiscalById(toDealFiscal
                .getCodigoDealFiscal());
        id.setCodigoDealFiscal(deal.getCodigoDealFiscal());
        TipoServico tipoServico = tipoServicoService.findTipoServicoById(bean
                .getTipoServicoMap().get(bean.getTipoServicoSelected()));
        id.setCodigoTipoServico(tipoServico.getCodigoTipoServico());
        t.setDealFiscal(deal);
        t.setTipoServico(tipoServico);
        t.setId(id);

        tipoServicoDealFiscalService.createTipoServicoDealFiscal(t);
        prepareTabDealFiscal();
    }

    public void createHistoricoIntercompInvoke() {

        List<HistoricoPercentualIntercomp> listHistoricoIntercomp = bean.getHistoricosPercentuaisIntercomp();
        Long codEmpInterComp = bean.getEmpresaMap().get(bean.getNomeEmpresaIntercomp());
        int startYear = 0;
        int startMonth = 0;
        if (!"".equalsIgnoreCase(bean.getAnoInicioVigencia())) {
            startYear = Integer.parseInt(bean.getAnoInicioVigencia());
        } else {
            Messages.showError("updateDealFiscal",
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ENTITY_NAME_INTERCOMPANY);
            return;
        }

        if (!"".equalsIgnoreCase(bean.getMesInicioVigencia())) {
            startMonth = Integer.parseInt(bean.getMesInicioVigencia());
        } else {
            Messages.showError("updateDealFiscal",
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ENTITY_NAME_INTERCOMPANY);
            return;
        }

        for (HistoricoPercentualIntercomp h : listHistoricoIntercomp) {

            //verificar sem ja existe um historico para encerar o ciclo para iniciar um novo
            if (h.getEmpresaIntercomp().getCodigoEmpresa().equals(codEmpInterComp)) {
                Calendar dataInicio = Calendar.getInstance();
                dataInicio.setTime(h.getDataInicio());

                //Validando inicio de vigência menor que a vigência atual
                if (h.getDataFim() == null && ((dataInicio.get(Calendar.YEAR) > startYear) || (dataInicio.get(Calendar.YEAR) == startYear && dataInicio.get(Calendar.MONTH) > startMonth))) {
                    Messages.showError("createHistoricoIntercompInvoke",
                            Constants.DEFAULT_MSG_ERROR_VIGENCY_INTERCOMPANY_ALREADY_EXISTS,
                            h.getEmpresaIntercomp().getNomeEmpresa());
                    return;
                }

                if (h.getDataFim() == null && (dataInicio.get(Calendar.YEAR) < startYear || (dataInicio.get(Calendar.YEAR) == startYear && dataInicio.get(Calendar.MONTH) < startMonth - 1))) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
                    Date utilDate = null;
                    Date finalDate = null;
                    try {
                        utilDate = formatter.parse(startYear + "/" + startMonth);
                    } catch (ParseException e) {
                        Messages.showError("updateDealFiscal",
                                Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                                Constants.ENTITY_NAME_INTERCOMPANY);
                        return;
                    }
                    finalDate = new DateTime(utilDate).minusDays(1).toDate();
                    h.setDataFim(finalDate);
                    historicoPercentualIntercompService.update(h);
                }
                //se for mesmo mês, atualizar somente.
                else if (h.getDataFim() == null && (dataInicio.get(Calendar.YEAR) == startYear && dataInicio.get(Calendar.MONTH) == startMonth - 1)) {
                    h.setPercentualIntercompany(bean.getPercentualIntercompany());
                    if (bean.getPercentualIntercompanyIntermediate() != null) {
                        h.setPercentualIntermediate(bean.getPercentualIntercompanyIntermediate());
                    } else {
                        h.setPercentualIntermediate(0.0);
                    }
                    if (!bean.getNomeEmpresaIntercompIntermediate().isEmpty() && bean.getNomeEmpresaIntercompIntermediate() != null) {
                        h.setEmpresaIntercompIntermediate(empresaService.findEmpresaById(bean.getEmpresaMap().get(bean.getNomeEmpresaIntercompIntermediate())));
                    } else if (h.getEmpresaIntercompIntermediate() != null) {
                        h.setEmpresaIntercompIntermediate(null);
                    }
                    historicoPercentualIntercompService.update(h);
                    DealFiscal df = dealFiscalService.findDealFiscalById(bean.getCurrentDealFiscalId());
                    if (bean.getToDealFiscal().getIndicadorIntercompany() != df.getIndicadorIntercompany()) {
                        updateDealFiscal();
                    }
                    Messages.showSucess("updateDealFiscal",
                            Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                            Constants.ENTITY_NAME_DEAL);

                    bean.setHistoricosPercentuaisIntercomp(historicoPercentualIntercompService.findByDealFiscal(bean.getToDealFiscal().getCodigoDealFiscal()));
                    prepareAfterCreateHistoricoIntercomp();
                    return;
                }
            }
        }
        createHistoricoIntercomp();
        DealFiscal df = dealFiscalService.findDealFiscalById(bean.getCurrentDealFiscalId());
        if (bean.getToDealFiscal().getIndicadorIntercompany() != df.getIndicadorIntercompany()) {
            updateDealFiscal();
        }
        Messages.showSucess("updateDealFiscal",
                Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_DEAL);

        bean.setHistoricosPercentuaisIntercomp(historicoPercentualIntercompService.findByDealFiscal(bean.getCurrentDealFiscalId()));
        prepareAfterCreateHistoricoIntercomp();
    }

    private void prepareAfterCreateHistoricoIntercomp() {
        //limpando seleção dos campos para não poder editar os valores.
        bean.setNomeEmpresaIntercomp(null);
        bean.setNomeEmpresaIntercompIntermediate(null);
        bean.setPercentualIntercompanyIntermediate(null);
        bean.setPercentualIntercompany(null);
        prepareUpdateDealFiscal();
    }

    private void createHistoricoIntercomp() {
        HistoricoPercentualIntercomp toHistoricoIntercomp = new HistoricoPercentualIntercomp();
        Long codEmpInterComp = bean.getEmpresaMap().get(bean.getNomeEmpresaIntercomp());
        int startYear = Integer.parseInt(bean.getAnoInicioVigencia());
        int startMonth = Integer.parseInt(bean.getMesInicioVigencia());

        Calendar dtinicio = Calendar.getInstance(); // locale-specific
        dtinicio.set(Calendar.YEAR, startYear);
        dtinicio.set(Calendar.MONTH, startMonth - 1);
        dtinicio.set(Calendar.DAY_OF_MONTH, 1);
        toHistoricoIntercomp.setDataInicio(dtinicio.getTime());
        toHistoricoIntercomp.setDealFiscal(bean.getToDealFiscal());
        toHistoricoIntercomp.setEmpresaIntercomp(empresaService.findEmpresaById(codEmpInterComp));
        toHistoricoIntercomp.setPercentualIntercompany(bean.getPercentualIntercompany());
        if (!bean.getNomeEmpresaIntercompIntermediate().isEmpty()) {
            toHistoricoIntercomp.setEmpresaIntercompIntermediate(empresaService.findEmpresaById(bean.getEmpresaMap().get(bean.getNomeEmpresaIntercompIntermediate())));
            toHistoricoIntercomp.setPercentualIntermediate(bean.getPercentualIntercompanyIntermediate());
        } else {
            toHistoricoIntercomp.setEmpresaIntercompIntermediate(null);
            toHistoricoIntercomp.setPercentualIntermediate(0.0);
            bean.setPercentualIntercompanyIntermediate(0.0);
        }
        historicoPercentualIntercompService.create(toHistoricoIntercomp);
    }

    public void updateHistoricoIntercomp() {
        prepareUpdateDealFiscal();
        HistoricoPercentualIntercomp h = historicoPercentualIntercompService.findById(bean.getCurrentHistoricoId());
        Calendar dataInicio = Calendar.getInstance();
        dataInicio.setTime(h.getDataInicio());
        //Caso data inicio seja menor que o mes atual, devemos encerrar e criar um novo y1 < y2 || (y1 == y2 && m1 <= m2)
        if (h.getDataFim() == null && (dataInicio.get(Calendar.YEAR) < Calendar.getInstance().get(Calendar.YEAR) || (dataInicio.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) && dataInicio.get(Calendar.MONTH) < Calendar.getInstance().get(Calendar.MONTH)))) {
            Calendar cal = Calendar.getInstance(); // locale-specific
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            h.setDataFim(cal.getTime());

            historicoPercentualIntercompService.update(h);

            //Criando novo
            HistoricoPercentualIntercomp histNew = new HistoricoPercentualIntercomp();
            Calendar dtinicio = Calendar.getInstance(); // locale-specific
            dtinicio.setTime(new Date());
            dtinicio.set(Calendar.DAY_OF_MONTH, 1);
            dtinicio.set(Calendar.HOUR_OF_DAY, 0);
            dtinicio.set(Calendar.MINUTE, 0);
            dtinicio.set(Calendar.SECOND, 0);
            dtinicio.set(Calendar.MILLISECOND, 0);
            histNew.setDataInicio(dtinicio.getTime());
            histNew.setDealFiscal(bean.getToDealFiscal());
            histNew.setEmpresaIntercomp(empresaService.findEmpresaById(bean.getEmpresaMap().get(bean.getNomeEmpresaIntercomp())));
            histNew.setPercentualIntercompany(bean.getPercentualIntercompany());
            //Empresa e Percentual Intermediate pode ser null
            if (!bean.getNomeEmpresaIntercompIntermediate().isEmpty()) {
                histNew.setEmpresaIntercompIntermediate(empresaService.findEmpresaById(bean.getEmpresaMap().get(bean.getNomeEmpresaIntercompIntermediate())));
            }
            if (bean.getPercentualIntercompanyIntermediate() != null) {
                histNew.setPercentualIntermediate(bean.getPercentualIntercompanyIntermediate());
            } else {
                histNew.setPercentualIntermediate(0.0);
            }
            historicoPercentualIntercompService.create(histNew);

            Messages.showSucess("updateDealFiscal",
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_DEAL);

            //Atualizando registros do Grid
            bean.setHistoricosPercentuaisIntercomp(historicoPercentualIntercompService.findByDealFiscal(bean.getToDealFiscal().getCodigoDealFiscal()));
            bean.setIsUpdateHistorico(Boolean.FALSE);
        } else {
            //Caso data inicio seja no mesmo mês, somente atualiza o registro.
            h.setPercentualIntercompany(bean.getPercentualIntercompany());
            if (!bean.getNomeEmpresaIntercompIntermediate().isEmpty()) {
                h.setEmpresaIntercompIntermediate(empresaService.findEmpresaById(bean.getEmpresaMap().get(bean.getNomeEmpresaIntercompIntermediate())));
            } else {
                h.setEmpresaIntercompIntermediate(null);
            }
            if (bean.getPercentualIntercompanyIntermediate() != null) {
                h.setPercentualIntermediate(bean.getPercentualIntercompanyIntermediate());
            } else {
                h.setPercentualIntermediate(0.0);
                bean.setPercentualIntercompanyIntermediate(0.0);
            }

            historicoPercentualIntercompService.update(h);

            Messages.showSucess("updateDealFiscal",
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_DEAL);

            if (null != h.getEmpresaIntercomp() && null != h.getEmpresaIntercomp().getCodigoEmpresa()
                    && null != bean.getToDealFiscal() && null != bean.getToDealFiscal().getEmpresaIntercomp()
                    && h.getEmpresaIntercomp().getCodigoEmpresa() == bean.getToDealFiscal().getEmpresaIntercomp().getCodigoEmpresa()) {
                DealFiscal dealUpdate = bean.getToDealFiscal();
                dealUpdate.setPercentualIntercompany(bean.getPercentualIntercompany());
                dealFiscalService.updateDealFiscal(dealUpdate);
            }

            bean.setHistoricosPercentuaisIntercomp(historicoPercentualIntercompService.findByDealFiscal(bean.getCurrentDealFiscalId()));
            bean.setIsUpdateHistorico(Boolean.FALSE);
            bean.setFlagEditData(Boolean.TRUE);
            bean.setPercentualIntercompanyIntermediate(null);
            bean.setPercentualIntercompany(null);
            bean.setNomeEmpresaIntercomp(null);
            bean.setNomeEmpresaIntercompIntermediate(null);
        }
    }

    /**
     * Executa um update de DealFiscal.
     */
    public void prepareUpdateDealFiscal() {
        bean.setIsUpdate(Boolean.TRUE);
        bean.setFlagEditData(Boolean.TRUE);
        bean.setTipoServicoSelected(null);
        if (!bean.getIsUpdate()) {
            bean.setPercentualIntercompany(null);
            bean.setPercentualIntercompanyIntermediate(null);
        }
        // guarda a data do HistoryLock
        bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

        bean.setToDealFiscal(dealFiscalService.findDealFiscalById(bean.getCurrentDealFiscalId()));

        DealFiscal toDealFiscal = bean.getToDealFiscal();
        bean.getToDealFiscal().setIndicadorIntercompany(toDealFiscal.getIndicadorIntercompany());

        List<TipoServicoDealFiscal> tipoSerivoDFList = tipoServicoDealFiscalService
                .findByDealFiscal(toDealFiscal);

        bean.setHistoricosPercentuaisIntercomp(historicoPercentualIntercompService.findByDealFiscal(toDealFiscal.getCodigoDealFiscal()));
        bean.setPaymentConditionsDealFiscal(paymentConditionDealFiscalService.findByDealFiscal(toDealFiscal.getCodigoDealFiscal()));
        loadPaymentConditionList(toDealFiscal);

        if (bean.getPaymentConditionName() != null) {
            bean.setPaymentConditionName(bean.getPaymentConditionName());
        }

        Boolean indicadorIntercompany = toDealFiscal.getIndicadorIntercompany();

        for (HistoricoPercentualIntercomp h : bean.getHistoricosPercentuaisIntercomp()) {
            if (indicadorIntercompany && h.getDataInicio().getTime() > bean.getHistoryLockDate().getTime()) {
                h.setCanEdit(Boolean.TRUE);
            }
        }
        if (tipoSerivoDFList.size() == 1) {
            TipoServico ts = tipoSerivoDFList.get(0).getTipoServico();
            bean.getToDealFiscal().setTipoServico(ts);
            bean.setTipoServicoSelected(ts.getNomeTipoServico());
        }

        if (indicadorIntercompany) {
            loadEmpresaInterCompList(empresaService.findEmpresaAllSubsidiary());
            bean.setNomeEmpresaIntercomp(bean.getNomeEmpresaIntercomp());
            bean.setPercentualIntercompany(bean.getPercentualIntercompany());
            if (bean.getPercentualIntercompanyIntermediate() != null) {
                bean.setPercentualIntercompanyIntermediate(bean.getPercentualIntercompanyIntermediate());
            }
            if (bean.getNomeEmpresaIntercompIntermediate() != null) {
                bean.setNomeEmpresaIntercompIntermediate(bean.getNomeEmpresaIntercompIntermediate());
            }
        }

        prepareTipoServicoAssociation();
    }

    public void paymentConditionListener(final ValueChangeEvent e) {
        String paymentConditionName = (String) e.getNewValue();

        if (paymentConditionName != null) {

            bean.setPaymentConditionName(paymentConditionName);
        }

    }

    public void prepareUpdateHistoricoIntercomp() {
        HistoricoPercentualIntercomp historicoToUpdate = historicoPercentualIntercompService.findById(bean.getCurrentHistoricoId());

        bean.setPercentualIntercompany(historicoToUpdate.getPercentualIntercompany());
        bean.setPercentualIntercompanyIntermediate(historicoToUpdate.getPercentualIntermediate());
        bean.setNomeEmpresaIntercomp(historicoToUpdate.getEmpresaIntercomp().getNomeEmpresa());
        if (historicoToUpdate.getEmpresaIntercompIntermediate() != null) {
            bean.setNomeEmpresaIntercompIntermediate(historicoToUpdate.getEmpresaIntercompIntermediate().getNomeEmpresa());
        }

        // Recupera data de inicio e converte para string
        Date mesInicioVigencia = historicoToUpdate.getDataInicio();
        Calendar dataInicio = new GregorianCalendar();
        dataInicio.setTime(mesInicioVigencia);
        String mes = Integer.toString(dataInicio.get(Calendar.MONTH) + 1);
        String ano = Integer.toString(dataInicio.get(Calendar.YEAR));
        // Seta o formulario
        bean.setMesInicioVigencia(mes);
        bean.setAnoInicioVigencia(ano);

        bean.setFlagEditData(Boolean.FALSE);
        bean.setIsUpdateHistorico(Boolean.TRUE);
    }

    /**
     * Prepara a lista de TipoServico relacionados com o DealFiscal.
     */
    private void prepareTipoServicoAssociation() {
        DealFiscal toDealFiscal = bean.getToDealFiscal();

        // recupera todos os TipoServico relacionados com TaxaImposto
        List<TipoServico> tsTaxaImpoList = tipoServicoService
                .findTipServAllRelatTaxaImp(toDealFiscal.getEmpresa()
                        .getCodigoEmpresa());
        List<SelectItem> avaiableItens = new ArrayList<SelectItem>();

        for (TipoServico tipoServico : tsTaxaImpoList) {
            avaiableItens.add(new SelectItem(""
                    + tipoServico.getCodigoTipoServico(), tipoServico
                    .getNomeTipoServico()));
        }

        List<TipoServico> tsSelectedList = toDealFiscal.getTipoServicos();
        String[] selectedItens = new String[tsSelectedList.size()];
        for (int i = 0; i < tsSelectedList.size(); i++) {
            selectedItens[i] = String.valueOf(tsSelectedList.get(i)
                    .getCodigoTipoServico());
        }

        // Adiciona os itens já associados ao Fiscal Deal porem, sem relação com
        // a empresa - evita ter um item selecionado que nao consta na lista de
        // disponiveis
        for (TipoServico tsSelected : tsSelectedList) {
            if (!tsTaxaImpoList.contains(tsSelected)) {
                avaiableItens.add(new SelectItem(""
                        + tsSelected.getCodigoTipoServico(), tsSelected
                        .getNomeTipoServico()));
            }
        }

        bean.setTipoServicoListPickList(avaiableItens);
        bean.setTipoServicoListSelected(selectedItens);
    }

    /**
     * Executa um update da Tab DealFiscal.
     */
    public void updateDealFiscal() {

        if (this.validatePaymentCondition()) {
            this.showPaymentConditionError();
            return;
        }

        if (this.validateActiveAllocationsAndIntegratedRevenueAfterClosingDase()) {
            return;
        }

        DealFiscal toDealFiscal = getDfCommonFieldsFilled();

        dealFiscalService.updateDealFiscal(toDealFiscal);

        Messages.showSucess("updateDealFiscal",
                Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_DEAL);

        prepareTabDealFiscal();
    }

    /**
     * Troca o status do DealFiscal.
     */
    public void changeDealFiscalStatus() {
        DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(bean
                .getCurrentDealFiscalId());

        if (dealFiscal.getIndicadorStatus().equals(Constants.ACTIVE)) {
            dealFiscal.setIndicadorStatus(Constants.INACTIVE);
        } else {
            dealFiscal.setIndicadorStatus(Constants.ACTIVE);
        }

        dealFiscalService.updateDealFiscal(dealFiscal);

        prepareTabDealFiscal();

    }

    /**
     * Cancela a edição de um Deal Fiscal.
     */
    public void cancelDealFiscal() {
        prepareTabDealFiscal();
    }

    /**
     * Remove um DealFiscal.
     */
    public void removeDealFiscal() {
        DealFiscal toDealFiscal = dealFiscalService.findDealFiscalById(bean
                .getCurrentDealFiscalId());

        if (dealFiscalService.removeDealFiscal(toDealFiscal)) {
            Messages.showSucess("removeDealFiscal",
                    Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                    Constants.ENTITY_NAME_DEAL);

            prepareTabDealFiscal();
        }
    }

    /**
     * Prepara a aba do PerfilVendido.
     */
    public void prepareTabSaleProfile() {
        perfilVendidoBean.reset();
        bean.setIsCheckedSaleProfileActiveOnly(Boolean.TRUE);

        // Carrega Combos.
        this.loadBaseList(cidadeBaseService.findCidadeBaseAllActive());
        this.loadCargoList(cargoPmsService.findAllCargoPms());
        this.loadPmgList(pmgService.findPmgAll());
        this.loadMoedaList(this.logicaMoedaList(bean.getTo()));

        this.findById(bean.getCurrentMsaId());

        perfilVendidoBean
                .setListaPerfilVendido(this
                        .loadListaPerfilVendido(perfilVendidoService
                                .findPerfilVendidoByMsaAndNotLogicalDelete(bean
                                        .getTo())));
    }

    /**
     * Prepara a aba de View do PerfilVendido.
     */
    public void prepareTabSaleProfileView() {
        perfilVendidoBean.reset();
        bean.setIsCheckedSaleProfileActiveOnly(Boolean.TRUE);

        this.findById(bean.getCurrentMsaId());

        perfilVendidoBean
                .setListaPerfilVendido(this
                        .loadListaPerfilVendido(perfilVendidoService
                                .findPerfilVendidoByMsaAndNotLogicalDelete(bean
                                        .getTo())));
    }

    /**
     * Create a Sale Profile in Msa.
     */
    public void createPerfilVerdido() {

        PerfilVendido pv = perfilVendidoBean.getTo();
        pv.setMsa(msaService.findMsaById(bean.getTo().getCodigoMsa()));

        // Busca Pmg
        Long codigoPmg = perfilVendidoBean.getMapPmg().get(
                perfilVendidoBean.getPmg());
        Pmg pmg = pmgService.findPmgById(codigoPmg);

        // Busca cidadeBase
        Long codigoBase = perfilVendidoBean.getMapBase().get(
                perfilVendidoBean.getCidade());
        CidadeBase base = cidadeBaseService.findCidadeBaseById(codigoBase);

        // Busca cargo
        Long codigoCargoPms = perfilVendidoBean.getMapCargo().get(
                perfilVendidoBean.getCargo());
        CargoPms cargo = cargoPmsService.findCargoPmsById(codigoCargoPms);

        // Busca moeda
        Long codigoMoeda = perfilVendidoBean.getMapMoeda().get(
                perfilVendidoBean.getMoeda());

        Moeda moeda = moedaService.findMoedaById(codigoMoeda);

        // Perfil Padrao
        PerfilPadrao perfilPadrao = new PerfilPadrao();

        List<PerfilPadrao> perfilPadraoList = perfilPadraoService
                .findByCargoPmgAndCidadeBase(cargo, pmg, base);

        // Se perfilPadrao nao existir, cria um
        if (perfilPadraoList.size() == 0) {
            perfilPadrao.setPmg(pmg);
            perfilPadrao.setCargoPms(cargo);
            perfilPadrao.setCidadeBase(base);
            perfilPadraoService.createPerfilPadrao(perfilPadrao);
        } else {
            perfilPadrao = perfilPadraoList.get(0);
        }

        // Seta perfil atributos perfil vendido
        pv.setPerfilPadrao(perfilPadrao);
        pv.setMoeda(moeda);
        pv.setIndicadorAtivo(Constants.ACTIVE);
        pv.setIndicadorDeleteLogico(Constants.NO);

        // Cria perfil vendido
        if (perfilVendidoService.createPerfilVendido(pv)) {
            perfilVendidoBean.reset();
            bean.setIsCheckedSaleProfileActiveOnly(Boolean.TRUE);
            perfilVendidoBean.setListaPerfilVendido(this
                    .loadListaPerfilVendido(perfilVendidoService
                            .findPerfilVendidoByMsaAndNotLogicalDelete(bean
                                    .getTo())));
            Messages.showSucess("createPerfilVendido",
                    Constants.GENEREC_MSG_SUCCESS_ADD);
        }
    }

    /**
     * Remove um PerfilVendido de um ContratoPratica.
     */
    public void removePerfilVendido() {

        Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());

        PerfilVendido pv = perfilVendidoService
                .findPerfilVendidoById(perfilVendidoBean.getTo()
                        .getCodigoPerfilVendido());

        if (perfilVendidoService.removePerfilVendido(pv)) {

            List<PerfilVendido> perfilVendidoList = perfilVendidoService
                    .findPerfilVendidoByMsaAndNotLogicalDelete(msa);

            perfilVendidoBean.setListaPerfilVendido(this
                    .loadListaPerfilVendido(perfilVendidoList));

            Messages.showSucess("removePerfilVendido",
                    Constants.GENEREC_MSG_SUCCESS_REMOVE);
        }

        perfilVendidoBean.resetTo();
        bean.setIsCheckedSaleProfileActiveOnly(Boolean.TRUE);
        perfilVendidoBean.setIsUpdate(Boolean.FALSE);
    }

    /**
     * Prepara o update do perfil vendido.
     */
    public void prepareUpdatePerfilVendido() {
        perfilVendidoBean.setIsUpdate(Boolean.TRUE);

        if (perfilVendidoBean.getTo().getPerfilPadrao() != null) {
            perfilVendidoBean.setPmg(perfilVendidoBean.getTo()
                    .getPerfilPadrao().getPmg().getNomePmg());
            perfilVendidoBean.setCidade(perfilVendidoBean.getTo()
                    .getPerfilPadrao().getCidadeBase().getSiglaCidadeBase());
            perfilVendidoBean.setCargo(perfilVendidoBean.getTo()
                    .getPerfilPadrao().getCargoPms().getNomeCargoPms());
            perfilVendidoBean.setMoeda(perfilVendidoBean.getTo().getMoeda()
                    .getNomeMoeda());
        }
    }

    /**
     * Realiza o update do perfil vendido.
     */
    public void updatePerfilVendido() {

        // Busca Pmg
        Long codigoPmg = perfilVendidoBean.getMapPmg().get(
                perfilVendidoBean.getPmg());
        Pmg pmg = pmgService.findPmgById(codigoPmg);

        // Busca cidadeBase
        Long codigoBase = perfilVendidoBean.getMapBase().get(
                perfilVendidoBean.getCidade());
        CidadeBase base = cidadeBaseService.findCidadeBaseById(codigoBase);

        // Busca Cargo
        Long codigoCargo = perfilVendidoBean.getMapCargo().get(
                perfilVendidoBean.getCargo());
        CargoPms cargo = cargoPmsService.findCargoPmsById(codigoCargo);

        // Busca Moeda
        Long codigoMoeda = perfilVendidoBean.getMapMoeda().get(
                perfilVendidoBean.getMoeda());

        Moeda moeda = moedaService.findMoedaById(codigoMoeda);

        PerfilVendido perfilVendido = perfilVendidoService
                .findPerfilVendidoById(perfilVendidoBean.getTo()
                        .getCodigoPerfilVendido());

        if (this.validateActiveAllocationsAfterClosingDase(
                perfilVendido, codigoCargo, codigoBase, codigoPmg, codigoMoeda)) {
            return;
        }

        if (this.validateSaleProfileInPriceTableWithDifferentCurrency(perfilVendido, codigoMoeda)) {
            return;
        }

        List<PerfilPadrao> perfilPadraoList = perfilPadraoService
                .findByCargoPmgAndCidadeBase(cargo, pmg, base);

        PerfilPadrao perfilPadrao;

        if (perfilPadraoList.size() == 0) {
            perfilPadrao = new PerfilPadrao();
            perfilPadrao.setCidadeBase(base);
            perfilPadrao.setPmg(pmg);
            perfilPadrao.setCargoPms(cargo);
            perfilPadrao.setIndicadorAtivo(Constants.ACTIVE);
            perfilPadraoService.createPerfilPadrao(perfilPadrao);
        } else {
            perfilPadrao = perfilPadraoList.get(0);
        }

        Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());

        // PerfilVendido auxiliar para não setar o objeto na sess�o do Hibernate
        PerfilVendido perfilVendidoAux = new PerfilVendido();
        perfilVendidoAux.setNomePerfilVendido(perfilVendidoBean.getTo()
                .getNomePerfilVendido());
        perfilVendidoAux.setMsa(msa);
        perfilVendidoAux.setCodigoPerfilVendido(perfilVendido
                .getCodigoPerfilVendido());

        if (perfilVendidoService.isNameExists(perfilVendidoAux)) {
            Messages.showError(UPDATE_PERFIL_VENDIDO,
                    Constants.DEFAULT_MSG_ERROR_NAME_ALREADY_EXISTS,
                    perfilVendidoBean.getTo().getNomePerfilVendido());
        } else {
            perfilVendido.setNomePerfilVendido(perfilVendidoBean.getTo()
                    .getNomePerfilVendido());
            perfilVendido.setMoeda(moeda);
            perfilVendido.setPerfilPadrao(perfilPadrao);
            perfilVendidoService.updatePerfilVendido(perfilVendido);
            perfilVendidoBean.reset();
            perfilVendidoBean.setIsUpdate(Boolean.FALSE);
            Messages.showSucess(UPDATE_PERFIL_VENDIDO,
                    Constants.GENEREC_MSG_SUCCESS_UPDATE);
        }

        bean.setIsCheckedSaleProfileActiveOnly(Boolean.TRUE);
        // Carrega lista de PerfilVendido
        perfilVendidoBean
                .setListaPerfilVendido(this
                        .loadListaPerfilVendido(perfilVendidoService
                                .findPerfilVendidoByMsaAndNotLogicalDelete(bean
                                        .getTo())));
    }

    /**
     * Valida se há alocações ativas para um determinado Perfil Vendido após o Closing Date do Mapa de Alocações
     */
    private Boolean validateActiveAllocationsAfterClosingDase(PerfilVendido perfilVendido, Long cargo, Long cidade, Long pmg, Long moeda) {

        Boolean hasActiveAllocations = Boolean.FALSE;
        Date closingDateAllocationMap = moduloService.getClosingDateMapaAlocacao();

        PerfilPadrao perfilPadrao;
        if (perfilVendido.getPerfilPadrao() != null) {
            perfilPadrao = perfilVendido.getPerfilPadrao();

            if (!perfilPadrao.getCargoPms().getCodigoCargoPms().equals(cargo) ||
                    !perfilPadrao.getCidadeBase().getCodigoCidadeBase().equals(cidade) ||
                    !perfilPadrao.getPmg().getCodigoPmg().equals(pmg) ||
                    !perfilVendido.getMoeda().getCodigoMoeda().equals(moeda)) {

                List<AlocacaoPeriodo> alocacaoPeriodoList =
                        alocacaoPeriodoService.findByPerfilVendidoAndClosingDate(perfilVendido, closingDateAllocationMap);

                if (alocacaoPeriodoList != null && alocacaoPeriodoList.size() > 0) {
                    Messages.showError(UPDATE_PERFIL_VENDIDO,
                            Constants.MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_FOR_THIS_SALE_PROFILE);
                    hasActiveAllocations = Boolean.TRUE;
                }
            }
        }

        return hasActiveAllocations;
    }

    /**
     * Valida se há Sale Profile associado há uma Tabela de Preços vigente e com uma moeda diferente
     */
    private Boolean validateSaleProfileInPriceTableWithDifferentCurrency(PerfilVendido perfilVendido, Long moeda) {

        Boolean hasPriceTableWithDifferentCurrency = Boolean.FALSE;

        if (!perfilVendido.getMoeda().getCodigoMoeda().equals(moeda)) {

            List<ItemTabelaPreco> itemTabelaPrecoList =
                    itemTabelaPrecoService.findByPerfilAndMoeda(perfilVendido);

            if (itemTabelaPrecoList != null && itemTabelaPrecoList.size() > 0) {
                Messages.showError(UPDATE_PERFIL_VENDIDO,
                        Constants.MSG_ERROR_THERE_ARE_PRICE_TABLE_WITH_A_DIFFERENT_CURRENCY);
                hasPriceTableWithDifferentCurrency = Boolean.TRUE;
            }
        }

        return hasPriceTableWithDifferentCurrency;
    }

    /**
     * Troca o status do indicadorAtivo do item.
     */
    public void changeSaleProfileStatus() {
        PerfilVendido perfilVendido = perfilVendidoService
                .findPerfilVendidoById(perfilVendidoBean.getTo()
                        .getCodigoPerfilVendido());

        if (perfilVendido.getIndicadorAtivo().equals(Constants.ACTIVE)) {
            Date closingDateAllocationMap = moduloService.getClosingDateMapaAlocacao();
            Date today = DateUtil.getDate(new Date());

            List<AlocacaoPeriodo> alocacaoPeriodoList =
                    alocacaoPeriodoService.findByPerfilVendidoAndClosingDate(perfilVendido, closingDateAllocationMap);

            if (alocacaoPeriodoList != null && alocacaoPeriodoList.size() > 0) {
                if(Boolean.TRUE.equals(DateUtil.equalDateMonthAndYearPrecision(closingDateAllocationMap, today))){
                    for (AlocacaoPeriodo alocacaoPeriodo : alocacaoPeriodoList) {
                        if(Boolean.TRUE.equals(DateUtil.equalDateMonthAndYearPrecision(alocacaoPeriodo.getDataAlocacaoPeriodo(), today))){
                            Messages.showError(UPDATE_PERFIL_VENDIDO,
                                    Constants.MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_WITHIN_CURRENT_MONTH_YOU_CAN_NOT_INACTIVATE_IT);
                            return;
                        }else{
                            Messages.showError(UPDATE_PERFIL_VENDIDO,
                                    Constants.MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_FOR_THIS_SALE_PROFILE_YOU_CAN_NOT_INACTIVATE_IT);
                            return;
                        }
                    }
                }else{
                    Messages.showError(UPDATE_PERFIL_VENDIDO,
                            Constants.MSG_ERROR_THERE_ARE_RESOURCE_ALLOCATIONS_FOR_THIS_SALE_PROFILE_YOU_CAN_NOT_INACTIVATE_IT);
                }
                return;
            }
            perfilVendido.setIndicadorAtivo(Constants.INACTIVE);
        } else {
            perfilVendido.setIndicadorAtivo(Constants.ACTIVE);
        }

        perfilVendidoService.updatePerfilVendido(perfilVendido);

        perfilVendidoBean.reset();
        perfilVendidoBean
                .setListaPerfilVendido(this
                        .loadListaPerfilVendido(perfilVendidoService
                                .findPerfilVendidoByMsaAndNotLogicalDelete(bean
                                        .getTo())));

    }

    /**
     * Prepara a aba da TabelaPreco.
     */
    public void prepareTabPriceTable() {
        tabelaPrecoBean.reset();
        bean.setIsCheckedPriceTableActiveOnly(Boolean.TRUE);

        findById(bean.getCurrentMsaId());
        Msa msa = bean.getTo();

        // Carrega combo de moeda
        this.loadCurrencyCombo(this.logicaMoedaList(msa));

        // Seta se o usuário logado é um editor da price table
        this.msaContratoBean.setIsPriceTableEditorLogin(this.isPriceTableEditorLogin(msa));

        // Seta se o usuário logado é um aprovador da price table
        this.msaContratoBean.setIsPriceTableApproverLogin(this.isPriceTableApproverLogin(msa));

        // Seta a lista de tabelas de preços referente
        List<TabelaPreco> tabelaPrecosList = tabelaPrecoService
                .findTabelaPrecoByMsaAndActive(msa);

        if(tabelaPrecosList != null && !tabelaPrecosList.isEmpty()){
            for(TabelaPreco tabelaPreco : tabelaPrecosList){
                setControlEnableComponents(tabelaPreco);
            }
        }
        // Seta a lista de tabelas de preços referente
        tabelaPrecoBean.setResultList(tabelaPrecosList);


        // guarda a data do HistoryLock
        bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());


    }

    private void setControlEnableComponents(TabelaPreco tabelaPreco) {
        tabelaPreco.setIsGreaterThenClosingDate(tabelaPreco.getDataFimVigencia() != null ? (moduloService.dateAfterHistoryLock(tabelaPreco.getDataFimVigencia())) : Boolean.FALSE);

        tabelaPreco.setIsViewPriceTableButton(
        (msaContratoBean.getIsPriceTableEditorLogin() && tabelaPreco.getPriceTableStatusFlowPms() != null &&
            (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DELETED)))
        || (msaContratoBean.getIsPriceTableApproverLogin()
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DELETED)))
        || (!msaContratoBean.getIsPriceTableEditorLogin()  && !msaContratoBean.getIsPriceTableApproverLogin()));

        tabelaPreco.setIsConfigurePriceTableButton(
                (msaContratoBean.getIsPriceTableEditorLogin() && tabelaPreco.getPriceTableStatusFlowPms() != null
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED))
                && ((tabelaPreco.getIsGreaterThenClosingDate() && tabelaPreco.getDataFimVigencia() != null)
                || tabelaPreco.getDataFimVigencia() == null))
                ||
                (msaContratoBean.getIsPriceTableApproverLogin()
                      && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                      || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL))
                      && (tabelaPreco.getIsGreaterThenClosingDate() && tabelaPreco.getDataFimVigencia() != null)));

        tabelaPreco.setIsEditPriceTableButton(
                (msaContratoBean.getIsPriceTableEditorLogin() && tabelaPreco.getPriceTableStatusFlowPms() != null
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED))
                && ((tabelaPreco.getIsGreaterThenClosingDate() && tabelaPreco.getDataFimVigencia() != null)
                || tabelaPreco.getDataFimVigencia() == null))
                ||
                 (msaContratoBean.getIsPriceTableApproverLogin()
                   && tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                   && tabelaPreco.getIsGreaterThenClosingDate()));

        tabelaPreco.setIsDeletePriceTableButton(msaContratoBean.getIsPriceTableEditorLogin()
                && tabelaPreco.getPriceTableStatusFlowPms() != null
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED))
                && ((tabelaPreco.getIsGreaterThenClosingDate() && tabelaPreco.getDataFimVigencia() != null)
                || tabelaPreco.getDataFimVigencia() == null));

        tabelaPreco.setIsApprovedCheckboxView((msaContratoBean.getIsPriceTableEditorLogin()
                && tabelaPreco.getPriceTableStatusFlowPms() != null
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DELETED)))
                || (msaContratoBean.getIsPriceTableApproverLogin()
                        && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DELETED)))
                || (!msaContratoBean.getIsPriceTableEditorLogin()  && !msaContratoBean.getIsPriceTableApproverLogin())
                || (msaContratoBean.getIsPriceTableApproverLogin() && tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)));

        tabelaPreco.setIsApprovedCheckboxEdit((msaContratoBean.getIsPriceTableEditorLogin()
                && tabelaPreco.getPriceTableStatusFlowPms() != null
        && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.NOT_APPROVED))
        || (msaContratoBean.getIsPriceTableApproverLogin()
                && tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT))));


        tabelaPreco.setIsAddItemPriceTableButton((msaContratoBean.getIsPriceTableEditorLogin()
                && tabelaPreco.getPriceTableStatusFlowPms() != null
                && tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                && tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)
                || (msaContratoBean.getIsPriceTableApproverLogin()
                    && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                    || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)))));


        tabelaPreco.setIsItemPriceTableField((msaContratoBean.getIsPriceTableEditorLogin()
                && tabelaPreco.getPriceTableStatusFlowPms() != null
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)))
                || (msaContratoBean.getIsPriceTableApproverLogin()
                        && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                        || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL))));

        tabelaPreco.setIsItemPriceTableControl((msaContratoBean.getIsPriceTableEditorLogin()
                && tabelaPreco.getPriceTableStatusFlowPms() != null
                && (tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.READY_FOR_APPROVAL)
                || tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.ON_APPROVAL)))
                || (msaContratoBean.getIsPriceTableApproverLogin()
                && tabelaPreco.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DRAFT)));
    }

    /**
     * Prepara a aba de View da TabelaPreco.
     */
    public void prepareTabPriceTableView() {
        tabelaPrecoBean.reset();
        bean.setIsCheckedPriceTableActiveOnly(Boolean.TRUE);

        findById(bean.getCurrentMsaId());
        Msa msa = bean.getTo();

        // Seta a lista de tabelas de preços referente
        tabelaPrecoBean.setResultList(tabelaPrecoService
                .findTabelaPrecoByMsaAndActive(msa));
    }

    /**
     * Prepara a tela para a TabelaPreco.
     */
    public void prepareTabelaPreco() {
        tabelaPrecoBean.reset();

        Msa msa = bean.getTo();

        // Seta a lista de tabelas de pre�os referente
        tabelaPrecoBean.setResultList(tabelaPrecoService
                .findTabelaPrecoByMsa(msa));
    }

    /**
     * Cria uma entidade TabelaPreco associado a um ContratoPratica.
     */
    public void createTabelaPreco() {

        PrometheusMetrics pricetableCreateMetrics = new PrometheusMetrics("pms.pricetable.create.revenue");
        // prometheus - Conta quantas tabelas de preço estão sendo criadas
        pricetableCreateMetrics.incrementCounter();

        TabelaPreco tp = tabelaPrecoBean.getTo();
        Date startDate = null;
        PriceTableStatus priceTableStatus = null;
        try {
            String startDateStr = tabelaPrecoBean.getMesInicioVigencia() + "/"
                    + tabelaPrecoBean.getAnoInicioVigencia();

            String[] dateFormat = {"MM/yyyy"};

            startDate = DateUtils.parseDate(startDateStr, dateFormat);

            // verifica o History Lock. Se startDate não for válido, dá mensagem
            // de
            // erro
            if (!moduloService.verifyHistoryLock(startDate,
                    Boolean.TRUE, Boolean.FALSE)) {
                return;
            }

            Long codigoMoeda = tabelaPrecoBean.getMapMoeda().get(
                    tabelaPrecoBean.getMoeda());
            Moeda moeda = moedaService.findMoedaById(codigoMoeda);

            Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());

            tp.setMsa(msa);
            tp.setDataInicioVigencia(startDate);
            tp.setMoeda(moeda);
            tp.setRecalculaReceita(Boolean.TRUE);

            if (Boolean.TRUE.equals(handler.defaultFlow().create(tp))) {
                handler.findPriceTableFlow(IPriceTableFlow.STATUS_DRAFT).createHistory(tp.getCodigoTabelaPreco(), LoginUtil.getLoggedUsername());
                tabelaPrecoBean.resetTo();
                Messages.showSucess("createTabelaPreco",
                        Constants.GENEREC_MSG_SUCCESS_ADD);
            }

            bean.setIsCheckedPriceTableActiveOnly(Boolean.TRUE);

            List<TabelaPreco> tabelaPrecoList = tabelaPrecoService
                    .findTabelaPrecoByMsaAndActive(msa);

            for (TabelaPreco tabelaPreco : tabelaPrecoList){

                if(tabelaPreco.getPriceTableStatus()!= null
                        && tabelaPreco.getPriceTableStatus().getCode() != null
                          && tabelaPreco.getPriceTableStatus().getName() == null){
                    try {
                        priceTableStatus = priceTableStatusService.findPriceTableStatusById(tabelaPreco.getPriceTableStatus().getCode());
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        Messages.showError("createTabelaPreco",
                                Constants.DEFAULT_MSG_ERROR_INVALID_PRICE_TABLE_STATUS);
                    }
                    tabelaPreco.getPriceTableStatus().setName(priceTableStatus.getName());
                    tabelaPreco.getPriceTableStatus().setAcronym(priceTableStatus.getAcronym());
                    tabelaPreco.getPriceTableStatus().setDescription(priceTableStatus.getDescription());
                }

                if(tabelaPreco.getPriceTableStatusFlowPms()!= null
                        && tabelaPreco.getPriceTableStatusFlowPms().getCode() != null
                          && tabelaPreco.getPriceTableStatusFlowPms().getName() == null){
                    try {
                        priceTableStatus = priceTableStatusService.findPriceTableStatusById(tabelaPreco.getPriceTableStatusFlowPms().getCode());
                    } catch (BusinessException e) {
                        e.printStackTrace();
                        Messages.showError("createTabelaPreco",
                                Constants.DEFAULT_MSG_ERROR_INVALID_PRICE_TABLE_STATUS);
                    }
                    tabelaPreco.getPriceTableStatusFlowPms().setName(priceTableStatus.getName());
                    tabelaPreco.getPriceTableStatusFlowPms().setAcronym(priceTableStatus.getAcronym());
                    tabelaPreco.getPriceTableStatusFlowPms().setDescription(priceTableStatus.getDescription());
                }

                setControlEnableComponents(tabelaPreco);
            }

            tabelaPrecoBean.setResultList(tabelaPrecoList);

        } catch (ParseException e) {
            // provavelmente nunca ocorrera este erro,
            // pela interface com o usuário.
            e.printStackTrace();
            Messages.showError("createTabelaPreco",
                    Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
        }
    }

    /**
     * Remove uma TabelaPreco.
     */
    public void removeTabelaPreco() {
        PriceTableStatus priceTableStatus = null;
        TabelaPreco tp = tabelaPrecoService.findTabelaPrecoById(tabelaPrecoBean
                .getTo().getCodigoTabelaPreco());
        if(tp != null){
            boolean isApproved = tp.getPriceTableStatus().getCode().equals(IPriceTableFlow.STATUS_APPROVED);
            if (Boolean.TRUE.equals(tabelaPrecoService.removeTabelaPreco(tp))) {
                Messages.showSucess("removeTabelaPreco",
                        Constants.GENEREC_MSG_SUCCESS_REMOVE);

               if(isApproved)
                verifyLastPriceTable(tp);
            }
        }

        if(tp.getPriceTableStatusFlowPms() != null){
            try {
                priceTableStatus = priceTableStatusService.findPriceTableStatusById(tp.getPriceTableStatusFlowPms().getCode());
                tp.setPriceTableStatusFlowPms(priceTableStatus);
                handler.findPriceTableFlow(tp.getPriceTableStatusFlowPms().getCode()).createHistory(tp.getCodigoTabelaPreco(), LoginUtil.getLoggedUsername());
            } catch (BusinessException e) {
                e.printStackTrace();
                Messages.showError("createTabelaPreco",
                        Constants.DEFAULT_MSG_ERROR_INVALID_PRICE_TABLE_STATUS);
            }
        }
        List<TabelaPreco> tabelaPrecoList = tabelaPrecoService
                .findTabelaPrecoByMsaAndActive(bean.getTo());


        if(tabelaPrecoList != null && !tabelaPrecoList.isEmpty()){
            for(TabelaPreco tabelaPreco : tabelaPrecoList){
                setControlEnableComponents(tabelaPreco);
            }
        }

        bean.setIsCheckedPriceTableActiveOnly(Boolean.TRUE);
        tabelaPrecoBean.setResultList(tabelaPrecoList);
        tabelaPrecoBean.resetTo();
        setControlEnableComponents(tp);
    }

    /**
     * Prepare para edit da tabela de preco.
     */
    public void prepareEditPriceTable() {
        TabelaPreco tabelaPreco = tabelaPrecoBean.getTo();
        tabelaPrecoBean.getTo().setDescricaoTabelaPreco(
                tabelaPreco.getDescricaoTabelaPreco());

        // Recupera data de inicio e converte para string
        Date mesInicioVigencia = tabelaPreco.getDataInicioVigencia();

        Calendar dataInicio = new GregorianCalendar();
        dataInicio.setTime(mesInicioVigencia);

        String mes = Integer.toString(dataInicio.get(Calendar.MONTH) + 1);
        String ano = Integer.toString(dataInicio.get(Calendar.YEAR));

        // Seta o formulario
        tabelaPrecoBean.setMesInicioVigencia(mes);
        tabelaPrecoBean.setAnoInicioVigencia(ano);
        tabelaPrecoBean.setMoeda(tabelaPreco.getMoeda().getNomeMoeda());

        tabelaPrecoBean.setFlagEditData(Boolean.TRUE);
        tabelaPrecoBean.setIsUpdate(Boolean.TRUE);
    }

    /**
     * Atualiza tabela de Preco, só é possivel alterar o nome da mesma.
     */
    public void updateTabelaPreco() {

        PrometheusMetrics pricetableUpdateMetrics = new PrometheusMetrics("pms.pricetable.update.revenue");
        // prometheus - Conta quantos itens de tabela de preço estão sendo alteradas
        pricetableUpdateMetrics.incrementCounter();

        TabelaPreco tabelaPreco = tabelaPrecoService
                .findTabelaPrecoById(tabelaPrecoBean.getTo()
                        .getCodigoTabelaPreco());

        Long codigoMoeda = tabelaPrecoBean.getMapMoeda().get(
                tabelaPrecoBean.getMoeda());
        Moeda moeda = moedaService.findMoedaById(codigoMoeda);
        tabelaPreco.setMoeda(moeda);
        tabelaPreco.setDescricaoTabelaPreco(tabelaPrecoBean.getTo()
                .getDescricaoTabelaPreco());
        tabelaPreco.setRecalculaReceita(Boolean.TRUE);

        tabelaPrecoService.updateTabelaPreco(tabelaPreco);
        tabelaPrecoBean.setResultList(tabelaPrecoService
                .findTabelaPrecoByMsaAndActive(bean.getTo()));
        tabelaPrecoBean.reset();
        bean.setIsCheckedPriceTableActiveOnly(Boolean.TRUE);
        Messages.showSucess("updateTabelaPreco",
                Constants.GENEREC_MSG_SUCCESS_UPDATE);

    }

    /**
     * Cancela edicao da tabela de preco.
     */
    public void cancelEditTabelaPreco() {
        tabelaPrecoBean.reset();
        bean.setIsCheckedPriceTableActiveOnly(Boolean.TRUE);
        tabelaPrecoBean.setResultList(tabelaPrecoService
                .findTabelaPrecoByMsaAndActive(bean.getTo()));
    }

    /**
     * @return Outcome para tela de itens da tabela de preço
     */
    public String prepareItemPriceTable() {
        prepareItemPriceTableDetail();
        setWarningDisapproval();
        return OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }

    private void setWarningDisapproval() {
        bean.setNotApproveReasonsDescription("");
        if(tabelaPrecoBean.getTo().getNotApproveReasonsDescription() != null
                && !tabelaPrecoBean.getTo().getNotApproveReasonsDescription().isEmpty()){
            Messages.showWarning("updateLista", Constants.NOT_APPROVE_REASONS_DESCRIPTION, tabelaPrecoBean.getTo().getNotApproveReasonsDescription());
        }
    }

    /**
     * @return Outcome para tela de itens da tabela de preço
     */
    public String prepareItemPriceTableView() {
        prepareItemPriceTableDetail();
        setWarningDisapproval();
        return OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED;
    }

    /**
     *
     */
    private void prepareItemPriceTableDetail() {
        tabelaPrecoBean.setTo(tabelaPrecoService.findTabelaPrecoById(tabelaPrecoBean.getTo().getCodigoTabelaPreco()));
        tabelaPrecoBean.setNotApproveReasonsDescription(tabelaPrecoBean.getTo().getNotApproveReasonsDescription());
        PriceTablePojo pojo = PriceTablePojo.builder()
                                .tabelaPrecoBean(tabelaPrecoBean)
                                .itemTabelaPrecoBean(itemTabelaPrecoBean)
                                .msa(bean.getTo())
                                .build();

        handler.findPriceTableFlow(tabelaPrecoBean.getTo().getPriceTableStatusFlowPms().getCode()).prepareUpdate(pojo);
        setControlEnableComponents(tabelaPrecoBean.getTo());
        itemTabelaPrecoBean.getTo().setTabelaPreco(tabelaPrecoBean.getTo());
        prepareAnexoTabelaPrecoBean();
        loadPriceTableStatusList(tabelaPrecoBean.getTo());
        verifyRevenuesByPriceTableDatesAndMsa(tabelaPrecoBean.getTo());
    }

    /**
     *
     */
    private void prepareAnexoTabelaPrecoBean(){
        anexoTabelaPrecoBean.setListaAnexoTabelaPreco(anexoTabelaPrecoService
                .findAnexoTabelaPrecoByTabelaPreco(tabelaPrecoBean.getTo()));
    }

    /**
     * Funcionalidade do back do Msa Manage.
     *
     * @return página de destino
     */
    public String backMsaManage() {
        return OUTCOME_MSA_MANAGE;
    }

    /**
     * Prepare Add itemTabPreco.
     */
    public void prepareAddItemTabelaPreco() {

        itemTabelaPrecoBean.setNomeCargo(null);
        itemTabelaPrecoBean.reset();

        TabelaPreco tabelaPreco = tabelaPrecoService
                .findTabelaPrecoById(tabelaPrecoBean.getTo()
                        .getCodigoTabelaPreco());

        this.loadPerfilVendidoList(perfilVendidoService
                .findPerfilVendidoByMsaAndMoedaAndActive(bean.getTo(),
                        tabelaPreco.getMoeda()));
    }

    /**
     * Atualiza atributos no modal quando combobox � selecionada.
     *
     * @param e evento
     */
    public void trocaAtributos(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();
        PerfilVendido pv = perfilVendidoService
                .findPerfilVendidoById(itemTabelaPrecoBean
                        .getPerfilVendidoMap().get(value));

        itemTabelaPrecoBean.getTo().setPerfilVendido(pv);

    }

    /**
     * Cria uma entidade do tipo ItemTabelaPreco.
     */
    public void createItemTabelaPreco() throws BusinessException {

        PrometheusMetrics pricetableCreateItemMetrics = new PrometheusMetrics("pms.pricetable.create.item.revenue");
        // prometheus - Conta quantas itens de tabelas de preço estão sendo criadas
        pricetableCreateItemMetrics.incrementCounter();

        ItemTabelaPreco item = itemTabelaPrecoBean.getTo();

        if (item.getPerfilVendido().getNomePerfilVendido() != null
                && item.getPerfilVendido().getNomePerfilVendido() != "") {

            PerfilVendido pv = perfilVendidoService
                    .findPerfilVendidoById(itemTabelaPrecoBean
                            .getPerfilVendidoMap().get(
                                    item.getPerfilVendido()
                                            .getNomePerfilVendido()));

            TabelaPreco tp = tabelaPrecoBean.getTo();

            PerfilPadrao pp = null;

            if (pv.getPerfilPadrao() != null) {
                pp = perfilPadraoService.findPerfilPadraoById(pv
                        .getPerfilPadrao().getCodigoPerfilPadrao());
            }

            if (itemTabelaPrecoService.findByTabelaPrecoAndPerfil(tp, pv) != null) {
                Messages.showError("createItemTabelaPreco",
                        Constants.DEFAULT_MSG_ERROR_ALREADY_EXISTS,
                        Constants.ENTITY_NAME_ITEM_TABELA_PRECO);
            } else {

                item.setPerfilVendido(pv);
                item.setTabelaPreco(tp);

                item.getPerfilVendido().setPerfilPadrao(pp);

                // transforma FTE em preço/hora
                if (Boolean.TRUE.equals(itemTabelaPrecoBean.getIsPricePerFTE())) {
                    item.setValorItemTbPreco(BigDecimal.valueOf(item
                            .getValorItemTbPreco().doubleValue()
                            / this.getNumberHoursMonth()));
                    itemTabelaPrecoBean.setFte(item.getValorItemTbPreco());
                }

                setStatusToDraft(tp);

                IPriceTableFlow flow = handler.findPriceTableFlow(IPriceTableFlow.STATUS_DRAFT);
                if (Boolean.TRUE.equals(flow.createItemTabelaPreco(item, LoginUtil.getLoggedUsername()))) {
                    Messages.showSucess("createItemTabelaPreco",
                            Constants.GENEREC_MSG_SUCCESS_ADD);

                    tp.setRecalculaReceita(Boolean.TRUE);
                    tabelaPrecoService.updateTabelaPreco(tp);
                }

                itemTabelaPrecoBean.resetTo();
                itemTabelaPrecoBean.getTo().setTabelaPreco(tp);
                prepareItemPriceTableDetail();
            }
        } else {
            Messages.showError("createItemTabelaPreco",
                    Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    Constants.ENTITY_NAME_PERFIL_VENDIDO);
        }
    }

    /**
     * Recupera o n�mero de horas do m�s do arquivo de configura��es.
     *
     * @return n�mero de horas do m�s do arquivo de configura��es.
     */
    private double getNumberHoursMonth() {
        return Double.parseDouble(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_NUMBER_HOURS_MONTH));
    }

    /**
     * Remove um ItemTabelaPreco.
     */
    public void removeItemTabelaPreco() throws BusinessException {

        if(itemTabelaPrecoBean.getTo().getCodigoItemTbPreco() != null){
            ItemTabelaPreco item = itemTabelaPrecoService
                    .findItemTabelaPrecoById(itemTabelaPrecoBean.getTo()
                            .getCodigoItemTbPreco());

            TabelaPreco tp = tabelaPrecoBean.getTo();

            if (Boolean.FALSE.equals(itemTabelaPrecoService.canRemove(
                    item.getPerfilVendido().getCodigoPerfilVendido(), tp.getCodigoTabelaPreco()))) {
                Messages.showError("removeItemtabelaPrecoError", Constants.MSA_SALE_PROFILE_HAS_ALLOCATION);
                return;
            }

            setStatusToDraft(tp);

            IPriceTableFlow flow = handler.findPriceTableFlow(IPriceTableFlow.STATUS_DRAFT);
            if (Boolean.TRUE.equals(flow.removeItemTabelaPreco(item, LoginUtil.getLoggedUsername()))) {
                Messages.showSucess("removeItemtabelaPreco",
                        Constants.GENEREC_MSG_SUCCESS_REMOVE);

                tp.setRecalculaReceita(Boolean.TRUE);
                tabelaPrecoService.updateTabelaPreco(tp);
            }

            itemTabelaPrecoBean.getTo().setTabelaPreco(tp);
        }else{
            handler.findPriceTableFlow(IPriceTableFlow.STATUS_DRAFT).removeItemTabelaPreco(itemTabelaPrecoBean.getTo(), LoginUtil.getLoggedUsername());
        }

        itemTabelaPrecoBean.resetTo();
        perfilVendidoBean.setIsUpdate(Boolean.FALSE);
        prepareItemPriceTableDetail();
    }

    /**
     *
     */
    public String updateListItemTabPrecoRow() {
        if(IPriceTableFlow.STATUS_NOT_APPROVED.equals(tabelaPrecoBean.getPriceTableStatusSelect().value()))
            return "";

        return updateListItem();
    }

    /**
     * @return
     */
    private String getOutcome(boolean isError) {
        setControlEnableComponents(tabelaPrecoBean.getTo());
        itemTabelaPrecoBean.getTo().setTabelaPreco(tabelaPrecoBean.getTo());
        setControlEnableComponents(itemTabelaPrecoBean.getTo().getTabelaPreco());
        if(!isError && tabelaPrecoBean.getPriceTableStatusSelect().value().equals(IPriceTableFlow.STATUS_ON_APRROVAL)){
            return handler.findPriceTableFlow(tabelaPrecoBean.getPriceTableStatusSelect().value()).outcome(!this.msaContratoBean.getIsPriceTableApproverLogin());
        }

        if(isError && tabelaPrecoBean.getTo().getPriceTableStatusFlowPms().getCode().equals(IPriceTableFlow.STATUS_READY_FOR_APRROVAL)){
            return handler.findPriceTableFlow(tabelaPrecoBean.getTo().getPriceTableStatusFlowPms().getCode()).outcome(this.msaContratoBean.getIsPriceTableApproverLogin());
        }

        return handler.findPriceTableFlow(isError ? tabelaPrecoBean.getTo().getPriceTableStatusFlowPms().getCode() : tabelaPrecoBean.getPriceTableStatusSelect().value()).outcome(isError ? Boolean.FALSE : this.msaContratoBean.getIsPriceTableApproverLogin());
    }

    /**
     * @return
     */
    public String updateListItem() {
        boolean isError = Boolean.FALSE;
        try{
            Long status = tabelaPrecoBean.getPriceTableStatusSelect().value();

            if(status != null){
                setReasonNotApproved();
                PriceTablePojo pojo = PriceTablePojo.builder()
                        .tabelaPrecoBean(tabelaPrecoBean)
                        .itemTabelaPrecoBean(itemTabelaPrecoBean)
                        .msa(bean.getTo())
                        .login(LoginUtil.getLoggedUsername())
                        .build();

                IPriceTableFlow flow = handler.findPriceTableFlow(status);
                flow.update(pojo);
                flow.prepareUpdate(pojo);
            }

            Messages.showSucess("updateLista", Constants.GENEREC_MSG_SUCCESS_UPDATE);
            validateReasonNotApproved();

            itemTabelaPrecoBean.reset();
            bean.setNotApproveReasonsDescription("");

        }catch (PriceTableException e){
            Messages.showError("updateListItemPriceTable", e.getMessageBundleKey());
            isError = Boolean.TRUE;
        }

       return getOutcome(isError);
    }
    /**
     *
     */
    private void validateReasonNotApproved(){
        if(tabelaPrecoBean.getNotApproveReasonsDescription() != null
                && !tabelaPrecoBean.getNotApproveReasonsDescription().isEmpty()){
            Messages.showWarning("updateLista", Constants.NOT_APPROVE_REASONS_DESCRIPTION, tabelaPrecoBean.getNotApproveReasonsDescription());
        }
    }

    private void setReasonNotApproved() {
        if(bean.getPriceTableStatusSelectName().equals(Constants.NOT_APPROVED_DESCRIPTION)
            && bean.getNotApproveReasonsDescription() != null){
            tabelaPrecoBean.getTo().setNotApproveReasonsDescription(bean.getNotApproveReasonsDescription());
            tabelaPrecoBean.setNotApproveReasonsDescription(bean.getNotApproveReasonsDescription());
        }

        if(!bean.getPriceTableStatusSelectName().equals(Constants.NOT_APPROVED_DESCRIPTION)
            && tabelaPrecoBean.getTo().getPriceTableStatusFlowPms().getName().equals(Constants.NOT_APPROVED_DESCRIPTION)){
            tabelaPrecoBean.getTo().setNotApproveReasonsDescription("");
            tabelaPrecoBean.setNotApproveReasonsDescription("");
        }
    }

    /**
     * Prepare add anexo.
     */
    public void prepareAddAnexo() {
        anexoTabelaPrecoBean.setTextoComentario(null);
        anexoTabelaPrecoBean.setFlagButtonSaveUpload(Boolean.FALSE);
    }

    /**
     * Realiza o upload de um arquivo.
     *
     * @param event evento.
     * @throws Exception exception.
     */
    public void uploadArquivoItemTabPreco(final UploadEvent event)
            throws Exception {

        UploadItem item = event.getUploadItem();

        TabelaPreco tabelaPreco = tabelaPrecoService
                .findTabelaPrecoById(tabelaPrecoBean.getTo()
                        .getCodigoTabelaPreco());

        AnexoTabelaPreco anexo = anexoTabelaPrecoService.uploadTabelaPreco(
                item, tabelaPreco, anexoTabelaPrecoBean.getTextoComentario());

        bean.setAnexoTabelaPreco(anexo);
        bean.setUploadItem(item);
        anexoTabelaPrecoBean.setFlagButtonSaveUpload(true);
    }

    /**
     * Cancel ItemTabelaPreco.
     */
    public void cancelItemTabelaPreco() {
        itemTabelaPrecoBean.resetTo();

        itemTabelaPrecoBean.setIsUpdate(Boolean.FALSE);
    }

    /**
     * Salva o anexo no servidor.
     */
    public void saveAnexo() {
        try {

            if (bean.getUploadItem().getData() != null) {
                anexoTabelaPrecoService.saveUploadAnexo(bean
                        .getAnexoTabelaPreco(), bean.getUploadItem().getData());
            } else {
                Messages.showWarning("saveAnexo", Constants.ANEXO_EMPTY);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }

        TabelaPreco tp = tabelaPrecoService.findTabelaPrecoById(tabelaPrecoBean
                .getTo().getCodigoTabelaPreco());

        anexoTabelaPrecoBean.setListaAnexoTabelaPreco(anexoTabelaPrecoService
                .findAnexoTabelaPrecoByTabelaPreco(tp));
    }

    /**
     * Remove anexo da tabela.
     */
    public void removeAnexoTabelaPreco() {
        AnexoTabelaPreco anexo = anexoTabelaPrecoService
                .findAnexoTabelaPrecoById(anexoTabelaPrecoBean.getTo()
                        .getCodigoAnexoTabPreco());
        if (anexoTabelaPrecoService.removeAnexoTabelaPreco(anexo)) {
            Messages.showSucess("removeAnexo",
                    Constants.GENEREC_MSG_SUCCESS_REMOVE);
        }

        anexoTabelaPrecoBean.setListaAnexoTabelaPreco(anexoTabelaPrecoService
                .findAnexoTabelaPrecoByTabelaPreco(tabelaPrecoBean.getTo()));
    }

    /**
     * Funcionalidade do back do Msa Manage.
     *
     * @return p�gina de destino
     */
    public String back() {
        findById(bean.getCurrentMsaId());
        bean.setListMsaSalMoeRow(msaService.getListMsaSaldoMoedaRow(bean
                .getTo()));
        return OUTCOME_MSA_MANAGE;
    }

    /**
     * Realiza o download do anexo da tabela de precos.
     */
    public void downloadAnexo() {
        AnexoTabelaPreco anexoTabelaPreco = anexoTabelaPrecoBean.getTo();

        String fileName = anexoTabelaPreco.getTabelaPreco()
                .getCodigoTabelaPreco()
                + "-"
                + anexoTabelaPreco.getTextoNomeArquivo();

        String contentType = "application/download";

        String path = systemProperties
                .getProperty("upload.anexo_tabela_preco.path");

        uploadArquivoService.downloadFile(path, fileName, contentType);
    }

    /**
     * Busca uma entidade pelo id.
     *
     * @param id da entidade
     */
    public void findById(final long id) {
        bean.setTo(msaService.findMsaById(id));
        bean.setIndicadorReembolsavel(bean.getTo().getIndicadorReembolsavel() == "N" ? Boolean.FALSE : Boolean.TRUE);
        if (bean.getTo() == null || bean.getTo().getCodigoMsa() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     */
    public void findByFilter() {

        Cliente cliente = bean.getFilter().getCliente();
        cliente.setCodigoCliente(bean.getClienteMap().get(
                cliente.getNomeCliente()));

        bean.getFilter().setIndustryType(bean.getIndustrySelect().filter());
        bean.getFilter().setBmDn(bean.getBmDnSelect().filter());

        bean.setResultList(msaService.findMsaByFilter(bean.getFilter()));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    public void findMsaContratoByFilter() {

        Moeda moeda = msaContratoBean.getFilter().getMoeda();
        moeda.setCodigoMoeda(this.bean.getMoedaSearchMap().get(
                this.msaContratoBean.getMoedaSearchSelected()));

        this.msaContratoBean.getFilter().setStatus(new MsaStatusContrato());
        this.msaContratoBean.getFilter().getStatus().setCodigo(this.msaContratoBean.getStatusContratosSearchMap().get(
                this.msaContratoBean.getStatusContratoSearchSelected()));

        this.msaContratoBean.getFilter().setMsa(new Msa());
        this.msaContratoBean.getFilter().getMsa().setCodigoMsa(this.bean.getTo().getCodigoMsa());

        msaContratoBean.setResultList(msaContratoService.findMsaContratoByFilter(msaContratoBean.getFilter()));

        if (msaContratoBean.getResultList().size() == 0) {
            Messages.showWarning("findMsaContratoByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
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
     * Popula a lista de Empresa para combobox de Empresa.
     *
     * @param empresas lista de Empresa.
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
     * Popula a lista de moedas para combobox.
     *
     * @param moedas lista de moedas.
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

        perfilVendidoBean.setMapMoeda(moedaMap);
        perfilVendidoBean.setListaMoeda(moedaList);
    }

    private void loadMoedaSearchList(final List<Moeda> moedas) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        moedaMap.put(Constants.ALL, 0L);
        moedaList.add(Constants.ALL);

        for (Moeda moeda : moedas) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            moedaList.add(moeda.getNomeMoeda());
        }

        bean.setMoedaSearchMap(moedaMap);
        bean.setMoedaSearchList(moedaList);
    }

    /**
     * Popula a lista de Empresa para combobox de Empresa InterComp.
     *
     * @param empresas lista de Empresa.
     */
    private void loadEmpresaInterCompList(final List<Empresa> empresas) {

        Map<String, Long> empresaInterCompMap = new HashMap<String, Long>();
        List<String> empresaInterCompList = new ArrayList<String>();

        for (Empresa empresa : empresas) {
            empresaInterCompMap.put(empresa.getNomeEmpresa(), empresa.getCodigoEmpresa());
            empresaInterCompList.add(empresa.getNomeEmpresa());
        }

        bean.setEmpresaInterCompMap(empresaInterCompMap);
        bean.setEmpresaInterCompList(empresaInterCompList);
        bean.setEmpresaInterCompIntermediateList(empresaInterCompList);
        bean.setEmpresaInterCompIntermediateMap(empresaInterCompMap);
    }

    private void loadPaymentConditionList(final DealFiscal dealFiscal) {

        try {
            Long companyCode = clienteService.getCompanyCodeByClientName(dealFiscal.getCliente().getCliente().getNomeCliente());
            List<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(dealFiscal.getCliente().getCodigoAgenteMega(), companyCode);
            List<String> paymentConditionList = new ArrayList<String>();
            bean.setPaymentConditionList(null);

            if (paymentConditions == null) {
                Messages.showError("findByClientAndCompany",
                        Constants.CLIENT_WITHOUT_MEGA_AGENT_CODE);
                return;
            }

            for (PaymentCondition paymentCondition : paymentConditions) {
                paymentConditionList.add(paymentCondition.getName());
            }
            bean.setPaymentConditionList(paymentConditionList);
        } catch (IOException e) {
            Messages.showError("paymentTerms",
                    Constants.DEFAULT_MSG_ERROR_LOAD_PAYMENT_TERMS,
                    Constants.ENTITY_NAME_CLIENTE);
        }
    }

    public Boolean loadPaymentConditionList() throws IOException {

        Long companyCode = clienteService.getCompanyCodeByClientName(bean.getToDealFiscal().getCliente().getCliente().getNomeCliente());
        List<PaymentCondition> paymentConditions = paymentConditionService.findByClientAndCompany(bean.getToDealFiscal().getCliente().getCodigoAgenteMega(), companyCode);
        List<String> paymentConditionList = new ArrayList<String>();
        bean.setPaymentConditionList(null);

        if (paymentConditions == null) {
            Messages.showError("findByClientAndCompany",
                    Constants.CLIENT_WITHOUT_MEGA_AGENT_CODE);
            return false;
        }

        for (PaymentCondition paymentCondition : paymentConditions) {
            paymentConditionList.add(paymentCondition.getName());
        }
        bean.setPaymentConditionList(paymentConditionList);
        return true;
    }

    public void associatePaymentConditionValidate() {
        if (bean.getPaymentConditionName() == null || (bean.getPaymentConditionName() != null && bean.getPaymentConditionName().isEmpty())) {
            this.showPaymentConditionError();
        } else {
            associatePaymentCondition();
        }
    }

    public void associatePaymentCondition() {
        if (bean.getIsUpdate()) {
            paymentConditionDealFiscalService.saveByDealFiscal(bean.getToDealFiscal().getCodigoDealFiscal(), bean.getPaymentConditionName());

            bean.setPaymentConditionsDealFiscal(paymentConditionDealFiscalService.findByDealFiscal(bean.getToDealFiscal().getCodigoDealFiscal()));
            bean.setPaymentConditionName(null);
            prepareUpdateDealFiscal();
        } else {
            if (bean.getToDealFiscal().getCodigoDealFiscal() != null) {
                paymentConditionDealFiscalService.saveByDealFiscal(bean.getToDealFiscal().getCodigoDealFiscal(), bean.getPaymentConditionName());
                bean.setPaymentConditionsDealFiscal(null);
                bean.setPaymentConditionsDealFiscal(paymentConditionDealFiscalService.findByDealFiscal(bean.getToDealFiscal().getCodigoDealFiscal()));
            } else {
                List<PaymentConditionDealFiscal> paymentConditionDealFiscals = new ArrayList<PaymentConditionDealFiscal>();
                paymentConditionDealFiscals.add(paymentConditionDealFiscalService.preparePaymentConditionDealFiscalToCreateDealFiscal(bean.getPaymentConditionName()));
                bean.setPaymentConditionsDealFiscal(paymentConditionDealFiscals);
            }
        }

    }

    /**
     * Lista de cidadesBase para combobox e seu hashmap.
     *
     * @param lista lista
     */
    private void loadBaseList(final List<CidadeBase> lista) {
        Map<String, Long> baseMap = new HashMap<String, Long>();
        List<String> listaCombo = new ArrayList<String>();

        for (CidadeBase cidade : lista) {
            baseMap.put(cidade.getSiglaCidadeBase(),
                    cidade.getCodigoCidadeBase());
            listaCombo.add(cidade.getSiglaCidadeBase());
        }

        perfilVendidoBean.setMapBase(baseMap);
        perfilVendidoBean.setListaCidadeBaseCombobox(listaCombo);
    }

    /**
     * Lista de cargo para combobox e seu hashmap.
     *
     * @param lista lista
     */
    private void loadCargoList(final List<CargoPms> lista) {
        Map<String, Long> cargoMap = new HashMap<String, Long>();
        List<String> listaCombo = new ArrayList<String>();

        for (CargoPms cargo : lista) {
            cargoMap.put(cargo.getNomeCargoPms(), cargo.getCodigoCargoPms());
            listaCombo.add(cargo.getNomeCargoPms());
        }

        perfilVendidoBean.setMapCargo(cargoMap);
        perfilVendidoBean.setListaCargoCombobox(listaCombo);
    }

    /**
     * Lista de pmg para combobox e seu hashmap.
     *
     * @param lista lista
     */
    private void loadPmgList(final List<Pmg> lista) {
        Map<String, Long> pmgMap = new HashMap<String, Long>();
        List<String> listaCombo = new ArrayList<String>();

        for (Pmg pmg : lista) {
            pmgMap.put(pmg.getNomePmg(), pmg.getCodigoPmg());
            listaCombo.add(pmg.getNomePmg());
        }

        perfilVendidoBean.setMapPmg(pmgMap);
        perfilVendidoBean.setListaPmgCombobox(listaCombo);
    }

    /**
     * Lista de objetos para tabela da view.
     *
     * @param listaPerfilVendido lista
     * @return lista
     */
    private List<PerfilVendido> loadListaPerfilVendido(
            final List<PerfilVendido> listaPerfilVendido) {

        List<PerfilVendido> lista = new ArrayList<PerfilVendido>();

        for (PerfilVendido perfilVendido : listaPerfilVendido) {
            if (perfilVendido.getIndicadorAtivo().equals(Constants.ACTIVE)) {
                perfilVendido.setIndicadorAtivo(BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_ACTIVE));
            } else {
                perfilVendido.setIndicadorAtivo(BundleUtil
                        .getBundle(Constants.BUNDLE_KEY_INACTIVE));
            }
            lista.add(perfilVendido);
        }

        Collections.sort(lista, new Comparator<PerfilVendido>() {
            @Override
            public int compare(final PerfilVendido o1, final PerfilVendido o2) {
                return o1.getNomePerfilVendido().compareToIgnoreCase(
                        o2.getNomePerfilVendido());
            }
        });

        return lista;
    }

    /**
     * Carrega hashmap e lista de moedas para combobox.
     *
     * @param currencyList lista de moedas.
     */
    private void loadCurrencyCombo(final List<Moeda> currencyList) {
        Map<String, Long> currencyMap = new HashMap<String, Long>();
        List<String> currencyComboList = new ArrayList<String>();

        for (Moeda moeda : currencyList) {
            currencyMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            currencyComboList.add(moeda.getNomeMoeda());
        }

        tabelaPrecoBean.setMapMoeda(currencyMap);
        tabelaPrecoBean.setCurrencyComboList(currencyComboList);
    }

    private void loadTipoLegalDocCombo(
            final List<DocumentoLegalTipo> listaDocumentoLegalTipo) {
        Map<String, Long> documentoLegalTipoMap = new HashMap<String, Long>();
        List<String> documentoLegalTipoComboList = new ArrayList<String>();

        for (DocumentoLegalTipo tipo : listaDocumentoLegalTipo) {
            documentoLegalTipoMap.put(tipo.getNomeTipoDocumentoLegal(),
                    tipo.getCodigoTipoDocumentoLegal());
            documentoLegalTipoComboList.add(tipo.getNomeTipoDocumentoLegal());
        }

        documentoLegalBean.setMapDocumentoLegalTipo(documentoLegalTipoMap);
        documentoLegalBean
                .setDocumentoLegalTipoComboList(documentoLegalTipoComboList);
    }

    /**
     * Carrega hashmap e lista de tipos de pessoas para combobox.
     *
     * @param pessoas lista de moedas.
     */
    private void loadPessoasCombo(
            final List<Pessoa> pessoas) {
        Map<Long, String> pessoaComboMap = new HashMap<Long, String>();
        List<String> pessoaComboList = new ArrayList<String>();

        for (Pessoa p : pessoas) {
            pessoaComboMap.put(p.getCodigoPessoa(), p.getCodigoLogin());
            pessoaComboList.add(p.getCodigoLogin());
        }

        documentoLegalBean.setPessoaComboMap(pessoaComboMap);
        documentoLegalBean.setPessoaComboList(pessoaComboList);
    }

    private void loadStatusLegalDocCombo() {
        Map<String, String> documentoLegalStatusMap = new HashMap<String, String>();
        List<String> documentoLegalStatusComboList = new ArrayList<String>();

        for (StatusDocumentoLegal status : StatusDocumentoLegal.values()) {

            // O status Inativo n�o � mostrado no combo status
            if (!StatusDocumentoLegal.INACTIVE.equals(status)) {
                documentoLegalStatusMap.put(status.getName(),
                        status.getAbbreviation());
                documentoLegalStatusComboList.add(status.getName());
            }
        }

        documentoLegalBean.setMapDocumentoLegalStatus(documentoLegalStatusMap);
        documentoLegalBean
                .setDocumentoLegalStatusComboList(documentoLegalStatusComboList);
    }

    /**
     * Retorna uma lista de itens para a tabela da view de ItemTabelaPreco.
     *
     * @param lista lista
     * @return lista de itens para view.
     */
    private List<ItemTabelaPrecoRow> loadListItemTabPrecoRow(
            final List<ItemTabelaPreco> lista) {

        ItemTabelaPrecoRow itemTabPreRow = null;
        List<ItemTabelaPrecoRow> listaItens = new ArrayList<ItemTabelaPrecoRow>();

        // Monta Vo para view
        for (ItemTabelaPreco item : lista) {
            itemTabPreRow = new ItemTabelaPrecoRow();
            itemTabPreRow.setNomeCargo(null);
            // Seta o total de FTEs
            itemTabPreRow
                    .setFte((item.getValorItemTbPreco().floatValue() * 168));

            // Cria BigDecimal do total de FTE para calcular o valor da despesa
            BigDecimal valorFte = BigDecimal.valueOf(itemTabPreRow.getFte());

            // Calcula o valor da despesa (somente para mostrar na tela, nao �
            // persistido)
            if (item.getPercentualDespesa() != null) {
                item.setValorDespesa(valorFte.multiply(item
                        .getPercentualDespesa().divide(BigDecimal.valueOf(100))));
            } else {
                item.setValorDespesa(BigDecimal.valueOf(0));
            }

            itemTabPreRow.setItemTabelaPreco(item);

            itemTabPreRow.setApproved(item.getIndicadorApproved() == null ? Boolean.FALSE : item.getIndicadorApproved().equals(Constants.YES));

            listaItens.add(itemTabPreRow);
        }

        return listaItens;
    }

    /**
     * Lista de perfil Vendido para combobox.
     *
     * @param lista lista
     */
    private void loadPerfilVendidoList(final List<PerfilVendido> lista) {
        Map<String, Long> mapPerfilVendido = new HashMap<String, Long>();
        List<String> listaPerfilVendido = new ArrayList<String>();

        for (PerfilVendido perfilVendido : lista) {
            mapPerfilVendido.put(perfilVendido.getNomePerfilVendido(),
                    perfilVendido.getCodigoPerfilVendido());
            listaPerfilVendido.add(perfilVendido.getNomePerfilVendido());
        }

        itemTabelaPrecoBean.setPerfilVendidoList(listaPerfilVendido);
        itemTabelaPrecoBean.setPerfilVendidoMap(mapPerfilVendido);
    }

    /**
     * Verifica se o cliente � v�lido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateCliente(final FacesContext context,
                                final UIComponent component, final Object value) {

        Long clienteId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get("label");

            clienteId = bean.getClienteMap().get(strValue);

            if (clienteId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (clienteService.findClienteById(clienteId) == null) {
                    throw new ValidatorException(
                            Messages.getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND));
                }
            }
        }
    }


    /**
     * Verifica se o Empresa � v�lido.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateEmpresa(final FacesContext context,
                                final UIComponent component, final Object value) {

        Long empresaId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get("label");

            empresaId = bean.getEmpresaMap().get(strValue);

            if (empresaId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (empresaService.findEmpresaById(empresaId) == null) {
                    throw new ValidatorException(
                            Messages.getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND));
                }
            }
        }
    }

    /**
     * Verifica se a Empresa Intercompany � v�lida.
     *
     * @param context   contexto do faces.
     * @param component componente faces.
     * @param value     valor do componente.
     */
    public void validateEmpresaInterComp(final FacesContext context,
                                         final UIComponent component, final Object value) {

        Long empresaInterCompId = null;
        String strValue = (String) value;

        if ((strValue != null) && (!"".equals(strValue))) {
            String label = (String) component.getAttributes().get("label");

            empresaInterCompId = bean.getEmpresaInterCompMap().get(strValue);

            if (empresaInterCompId == null) {
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            } else {
                if (empresaService.findEmpresaById(empresaInterCompId) == null) {
                    throw new ValidatorException(
                            Messages.getMessageError(Constants.DEFAULT_MSG_ERROR_NOT_FOUND));
                } else {
                    Long dealFiscalCompanyId = bean.getToDealFiscal().getEmpresa().getCodigoEmpresa();
                    Boolean indicadorIntercompany = bean.getToDealFiscal().getIndicadorIntercompany();
                    Empresa mainCompany = this.empresaService.findEmpresaById(dealFiscalCompanyId);
                    Empresa interCompany = this.empresaService.findEmpresaById(empresaInterCompId);

                    if (indicadorIntercompany) {
                        this.validateIntercompanyBetweenSameCompany(mainCompany, interCompany);
                        this.validateIntercompanyConfiguration(mainCompany, interCompany);
                    }
                }
            }
        }
    }

    /**
     * Gera o nome do deal, para pr�-visualiza��o.
     */
    private void generateDealFiscalName() {
        Long dfSequence;
        DealFiscal dfAux = bean.getToDealFiscal();
        Empresa empresa = dfAux.getEmpresa();
        String mnemonicoEmpresa = empresa.getCodigoMnemonico();
        Cliente cliente = dfAux.getCliente();
        String mnemonicoCliente = cliente.getCodigoMnemonico();
        TipoServico tipoServico = dfAux.getTipoServico();
        String mnemonicoTipoServico = "";
        if (tipoServico != null) {
            mnemonicoTipoServico = tipoServico.getSiglaTipoServico();
        }

        if (Boolean.TRUE.equals(bean.getIsUpdate())) {
            dfSequence = dfAux.getNumeroSequencia();
        } else {
            Msa msaAux = msaService.findMsaById(bean.getTo().getCodigoMsa());

            dfSequence = dealFiscalService.findDealFiscalMaxByMsa(msaAux);
            if (dfSequence == null) {
                dfSequence = 1L;
            } else {
                dfSequence++;
            }

        }

        if (empresa.getCodigoMnemonico() == null) {
            mnemonicoEmpresa = "";
        }
        if (cliente.getCodigoMnemonico() == null) {
            mnemonicoCliente = "";
        }

        String name = systemProperties
                .getProperty(Constants.DEAL_FISCAL_DEFAULT_ACRONYM_NAME)
                + dfSequence
                + " - "
                + mnemonicoEmpresa
                + "/"
                + mnemonicoCliente + "-" + mnemonicoTipoServico;

        dfAux.setNomeDealFiscal(name);
    }

    /**
     * Metodo de Evento para o combo do cliente.
     *
     * @param e - evento
     */
    public void clienteListener(final ValueChangeEvent e) {
        String nomeCliente = (String) e.getNewValue();

        if (nomeCliente != null) {
            Long codCliente = bean.getClienteMap().get(nomeCliente);
            Cliente cli = clienteService.findClienteById(codCliente);

            bean.getToDealFiscal().setCliente(cli);

            generateDealFiscalName();
        }
    }

    /**
     * Método de evento para o combo de empresa.
     *
     * @param e - evento
     */
    public void empresaListener(final ValueChangeEvent e) {
        String nomeEmpresa = (String) e.getNewValue();

        if (nomeEmpresa != null) {
            Long codEmpresa = bean.getEmpresaMap().get(nomeEmpresa);
            Empresa emp = empresaService.findEmpresaById(codEmpresa);

            bean.getToDealFiscal().setEmpresa(emp);

            generateDealFiscalName();
        }
    }

    public void tipoServicoListener(final ValueChangeEvent e) {
        String nomeTipoServico = (String) e.getNewValue();

        if (nomeTipoServico != null) {
            Long codTS = bean.getTipoServicoMap().get(nomeTipoServico);
            TipoServico ts = tipoServicoService.findTipoServicoById(codTS);

            bean.getToDealFiscal().setTipoServico(ts);

            generateDealFiscalName();
        }
    }

    /**
     * Evento que seta a lista de empresas intercomp.
     *
     * @param e - evento
     */
    public void changeValueIndicadorIntercompany(final ValueChangeEvent e) {

        Boolean isInterCompany = (Boolean) e.getNewValue();

        if (isInterCompany != null) {

            DealFiscal toDealFiscal = bean.getToDealFiscal();
            DealFiscal dfAux = dealFiscalService.findDealFiscalById(toDealFiscal.getCodigoDealFiscal());
            bean.getToDealFiscal().setIndicadorIntercompany(isInterCompany);

            if (Boolean.TRUE.equals(isInterCompany)) {

                bean.getToDealFiscal().setIndicadorIntercompany(true);
                loadEmpresaInterCompList(empresaService.findEmpresaAllSubsidiary());

                if (dfAux != null) {
                    Empresa empresaIntercomp = dfAux.getEmpresaIntercomp();
                    if (empresaIntercomp != null) {
                        toDealFiscal.setEmpresaIntercomp(empresaIntercomp);
                        bean.setNomeEmpresaIntercomp(empresaIntercomp.getNomeEmpresa());

                    }
                }
            } else {
                toDealFiscal.setEmpresaIntercomp(null);
                bean.setNomeEmpresaIntercomp(null);
            }
        }
    }

    /**
     * Atualiza as refer�ncias das etidades relacionadas ao objeto.
     *
     * @return DealFiscal atualizado
     */
    private DealFiscal getDfCommonFieldsFilled() {
        DealFiscal toDealFiscal = bean.getToDealFiscal();

        toDealFiscal.setMsa(msaService.findMsaById(bean.getCurrentMsaId()));

        Long codCliente = bean.getClienteMap().get(toDealFiscal.getCliente().getNomeCliente());
        if (codCliente != null) {
            toDealFiscal.setCliente(clienteService.findClienteById(codCliente));
        }

        Long codEmpresa = bean.getEmpresaMap().get(
                toDealFiscal.getEmpresa().getNomeEmpresa());
        if (codEmpresa != null) {
            toDealFiscal.setEmpresa(empresaService.findEmpresaById(codEmpresa));
        }

        Long codMoeda = bean.getMoedaMap().get(
                toDealFiscal.getMoeda().getNomeMoeda());
        if (codMoeda != null) {
            toDealFiscal.setMoeda(moedaService.findMoedaById(codMoeda));
        }

        // Empresa Intercompany n�o � obrigat�rio
        Boolean indicadorIntercomp = toDealFiscal.getIndicadorIntercompany();
        if (Boolean.TRUE.equals(indicadorIntercomp)) {
            Long codEmpInterComp = null;
            Double percentIntercompany = null;

            if (null == bean.getNomeEmpresaIntercomp() || "".equalsIgnoreCase(bean.getNomeEmpresaIntercomp())) {
                DealFiscal dealFiscal = dealFiscalService.findDealFiscalById(toDealFiscal.getCodigoDealFiscal());
                if (null != dealFiscal && null != dealFiscal.getEmpresaIntercomp()) {
                    codEmpInterComp = dealFiscal.getEmpresaIntercomp().getCodigoEmpresa();
                    percentIntercompany = dealFiscal.getPercentualIntercompany();
                }
            } else {
                codEmpInterComp = bean.getEmpresaMap().get(bean.getNomeEmpresaIntercomp());
                percentIntercompany = bean.getPercentualIntercompany();
            }

            if (codEmpInterComp != null) {
                toDealFiscal.setEmpresaIntercomp(empresaService.findEmpresaById(codEmpInterComp));
                toDealFiscal.setIndicadorIntercompany(Boolean.TRUE);
                toDealFiscal.setPercentualIntercompany(percentIntercompany);
            } else {
                toDealFiscal.setEmpresaIntercomp(null);
                toDealFiscal.setIndicadorIntercompany(Boolean.FALSE);
            }
        } else {
            toDealFiscal.setEmpresaIntercomp(null);
        }
        return toDealFiscal;
    }

    /**
     * Carrega lista de moedas de acordo com as moedas do Budget. (Para combobox
     * do sale profile e price table)
     *
     * @param msa - the msa
     * @return listResult
     */
    private List<Moeda> logicaMoedaList(final Msa msa) {
        List<Moeda> listResult = new ArrayList<Moeda>();

        for (MsaSaldoMoeda msm : msaSaldoMoedaService
                .findMsaSalMoeByMsaAndActive(msa)) {
            listResult.add(msm.getMoeda());
        }

        return listResult;
    }

    public void clearErrorEntryList() {
        bean.setErrorEntryList(null);
    }

    private List<Moeda> logicaMoedaSearchList(final Msa msa) {
        List<Moeda> listResult = new ArrayList<Moeda>();

        for (MsaSaldoMoeda msm : msaSaldoMoedaService
                .findMsaSalMoeByMsa(msa)) {
            listResult.add(msm.getMoeda());
        }

        return listResult;
    }

    /**
     * Habilita campos de percentual e valor embutido de um novo item (A��o
     * tomada ao selecionar checkbox).
     */
    public void displayFieldsItemTabPreco() {
        if (Boolean.FALSE.equals(itemTabelaPrecoBean.getIsExpenseEmbedded())) {
            itemTabelaPrecoBean.getTo().setPercentualDespesa(null);
            itemTabelaPrecoBean.setVlrEmbedded(null);
        } else {
            itemTabelaPrecoBean.setIsExpenseEmbedded(Boolean.TRUE);
        }
    }

    /**
     * Calcula o valor embutido de um novo item (A��o tomada ao clicar fora do
     * input de percentual).
     */
    public void calculaVlrDespesaNovoItem() {
        if (itemTabelaPrecoBean.getTo().getPercentualDespesa() != null) {
            double valorItem = itemTabelaPrecoBean.getTo()
                    .getValorItemTbPreco()
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

            double percDespesa = itemTabelaPrecoBean.getTo()
                    .getPercentualDespesa()
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();

            itemTabelaPrecoBean.setVlrEmbedded(BigDecimal.valueOf(
                    (valorItem * percDespesa) / 100).setScale(2,
                    RoundingMode.HALF_UP));
        }
    }

    /**
     * Calcula o percentual embutido de um novo item (A��o tomada ao clicar fora
     * do input de valor).
     */
    public void calculaPrcDespesaNovoItem() {
        BigDecimal valorItem = itemTabelaPrecoBean.getTo()
                .getValorItemTbPreco();
        if (itemTabelaPrecoBean.getVlrEmbedded() != null) {
            if (valorItem.equals(BigDecimal.valueOf(0))) {
                itemTabelaPrecoBean.getTo().setPercentualDespesa(null);
            } else {
                double vlrEmbedded = itemTabelaPrecoBean.getVlrEmbedded()
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
                double vlrItem = valorItem
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();

                itemTabelaPrecoBean.getTo().setPercentualDespesa(
                        BigDecimal.valueOf((vlrEmbedded / vlrItem) * 100)
                                .setScale(2, RoundingMode.HALF_UP));
            }
        }
    }

    /**
     * Apaga os valores quando checkbox de valor embutido � desseleccionado.
     */
    public void apagaValoresNovoItem() {
        if (Boolean.TRUE.equals(itemTabelaPrecoBean.getIsExpenseEmbedded())) {
            itemTabelaPrecoBean.getTo().setPercentualDespesa(null);
            itemTabelaPrecoBean.setVlrEmbedded(null);
        }
    }

    /**
     * Verifica abas do Msa e altera status para completo caso todas elas
     * preenchidas.
     */
    public void validateAndFinish() {
        // Busca msa
        Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());

        msa.setIndicadorCompleto(Constants.NO);

        // Se todas as abas preenchidas
        if (this.validateAllTabs(msa)) {
            // Altera status de completo do msa
            msa.setIndicadorCompleto(Constants.YES);
        }

        try {

            msaService.updateMsa(msa);

        } catch (IntegrityConstraintException ice) {
            Messages.showError("update", ice.getMessage(), new Object[]{
                    Constants.ENTITY_NAME_MSA,
                    Constants.ENTITY_NAME_CONTRATO_PRATICA});
            return;
        }

        // Verifica status do MSA novamente para exibir mensagem e setar o bean
        // para renderizar a tela.
        if (msa.getIndicadorCompleto().equals(Constants.YES)) {
            bean.getTo().setIndicadorCompleto(Constants.YES);
            Messages.showSucess("validateAndFinish",
                    Constants.MSA_COMPLETO_SUCCESS);

        } else {
            bean.getTo().setIndicadorCompleto(Constants.NO);
            Messages.showWarning("validateAndFinish",
                    Constants.MSA_INCOMPLETO_WARNING);
        }
    }

    /**
     * Verifica se existe pelo menos um registro persistido em cada aba do Msa.
     *
     * @param msa - the {@link Msa}.
     * @return boolean true se todas as abas persistidas.
     */
    private boolean validateAllTabs(final Msa msa) {

        // Verifica se existe pelo menos um budget persistido
        if (msaSaldoMoedaService.findMsaSalMoeByMsa(msa).isEmpty()) {
            return Boolean.FALSE;
        }

        // Verifica se existe pelo menos um fiscal deal ativo persistido
        if (dealFiscalService.findDealFiscalActiveByMsa(msa).isEmpty()) {
            return Boolean.FALSE;
        }

        // Verifica se existe pelo menos um perfil vendido ativo persistido
        if (perfilVendidoService.findPerfilVendidoByMsaAndActive(msa).isEmpty()) {
            return Boolean.FALSE;
        }

        // Verifica se existe pelo menos uma tabela de preco persistida
        if (tabelaPrecoService.findTabelaPrecoByMsa(msa).isEmpty()) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;

    }

    /**
     * A��o do bot�o back para a tela de busca do MSA.
     *
     * @return OUTCOME_MSA_SEARCH.
     */
    public String backMsa() {
        this.findByFilter();
        this.loadClienteList(clienteService.findClienteAllClientePai());
        return OUTCOME_MSA_SEARCH;
    }

    public void prepareTabSales() {

        // carrega a lista de DNs
        this.loadBusinessManagerList(pessoaService
                .findPessoaAllBusinessManager());

        // atribui o Forecast Sales Tax
        bean.getTo().setForecastSalesTax(
                msaService.calculateForecastSalesTax(bean.getTo()));
    }

    /**
     * Popula a lista de DNs para combobox e seu respectivo mapa.
     *
     * @param pessoas lista de DNs.
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
     * Atualiza a aba Sales do MSA.
     */
    public void updateSales() {
        Msa to = bean.getTo();

        Long codigoPessoa = bean.getPessoaMap().get(
                to.getPessoa().getCodigoLogin());
        Pessoa pessoaDn = pessoaService.findPessoaById(codigoPessoa);
        to.setPessoa(pessoaDn);

        try {
            msaService.updateMsa(to);

            Messages.showSucess("updateTabBudget",
                    Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_MSA);

            // prepareTabBudget();
        } catch (IntegrityConstraintException ice) {
            Messages.showError("updateTabBudget", ice.getMessage(),
                    new Object[]{Constants.ENTITY_NAME_MSA,
                            Constants.ENTITY_NAME_CONTRATO_PRATICA});
        }
    }

    /**
     * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
     *
     * @param e - evento de mudan�a
     */
    public void addLegalDocumentResponsavel(final ValueChangeEvent e) {
        String value = (String) e.getNewValue();

        if (value != null && !"".equals(value)) {
            DocumentoLegalResponsavel dlr = new DocumentoLegalResponsavel();
            dlr.setPessoa(pessoaService.findPessoaByLogin(value));
        }
    }

    /**
     * Remove o DocumentoLegalResponsavel indicado pelo usu�rio da tela e do
     * banco de dados
     */
    public void removeDocumentoLegalResponsavel() {

        if (documentoLegalBean.getDocumentoLegalResponsavelToRemove() != null) {

            if (documentoLegalBean.getDocumentoLegalResponsavelToRemove()
                    .getCodigoDocumentoLegalResponsavel() != null) {
                DocumentoLegalResponsavel dlr = documentoLegalResponsavelService.findById(
                        documentoLegalBean.getDocumentoLegalResponsavelToRemove().getCodigoDocumentoLegalResponsavel());

                documentoLegalResponsavelService.delete(dlr);
            }

            documentoLegalBean.setDocumentoLegalResponsavelToRemove(null);
        }
    }

    /**
     * Prepara a tela para o Documento Legal (Reajuste).
     */
    public void prepareDocumentoLegal() {
        documentoLegalBean.reset();

        this.buscaListaDocumentoLegal();

        List<DocumentoLegalTipo> listaTipoDocumentoLegal = documentoLegalTipoService
                .findDocumentoLegalTipoAllActive();

        // Carrega combo de tipo legal doc
        this.loadTipoLegalDocCombo(listaTipoDocumentoLegal);

        List<Pessoa> pessoas = pessoaService.findAllForComboBox();
        this.loadPessoasCombo(pessoas);

        // Carrega combo de Status legal doc
        this.loadStatusLegalDocCombo();

        this.loadBasesLegalDocCheckboxList();

        this.loadServiceTypeLegalDocCheckboxList();

        this.loadMoedaList(this.logicaMoedaList(bean.getTo()));

        this.loadMoedaSearchList(this.logicaMoedaSearchList(bean.getTo()));

    }

    private void loadBasesLegalDocCheckboxList() {
        List<Empresa> empresas = empresaService.findByIndicadorExibicaoMsaContrato(true);

        Collections.sort(empresas, new EmpresaNomeEmpresaComparator());
        for (Empresa emp : empresas) {
            this.msaContratoBean.getMapBasesDocumentoLegal().put(emp.getNomeEmpresa(), emp.getCodigoEmpresa());
        }
    }

    private void loadServiceTypeLegalDocCheckboxList() {
        List<TipoServico> tipoServicoList = tipoServicoService.findByIndicadorExibeMsaContrato(true);

        for (TipoServico tipoServico : tipoServicoList) {
            this.msaContratoBean.getMapServiceTypeDocumentoLegal().put(tipoServico.getNomeTipoServico(), tipoServico.getCodigoTipoServico());
            this.msaContratoBean.getTipoServicoDescription().add(tipoServico.getDescricao());
        }
    }

    /**
     * Prepara tela de configuracao documento legal
     */
    public String prepareConfigureDocumentoLegal() {
        documentoLegalConfigureBean.reset();
        documentoLegalConfigureBean.setControlesReajuste(new ArrayList<ControleReajuste>());
        return OUTCOME_CONFIGURE_DOCUMENTO_LEGAL;
    }

    /**
     * Cria um Documento Legal (Reajuste)
     */
    public void createDocumentoLegal() {
        Messages.showSucess("createDocumentoLegal",
                Constants.GENEREC_MSG_SUCCESS_ADD);
    }

    /**
     * Prepara tela para atualizar um Documento Legal (Reajuste)
     */
    public void prepareUpdateDocumentoLegal() {
        documentoLegalBean.setIsUpdate(Boolean.TRUE);
    }

    /**
     * Atualiza um Documento Legal (Reajuste)
     */
    public void updateDocumentoLegal() {

        if (this.validDocumentoLegal(documentoLegalBean)) {
            // Busca o objeto DocumentoLegalTipo selecionado na tela para setar
            // no
            // DocumentoLegal
            Long cdTipo = documentoLegalBean.getMapDocumentoLegalTipo().get(
                    documentoLegalBean.getDocumentoLegalTipo());
            documentoLegalBean.reset();

            // Busca os Documentos Legais por MSA para atualizar a lista na tela
            this.buscaListaDocumentoLegal();

            Messages.showSucess("updateDocumentoLegal",
                    Constants.GENEREC_MSG_SUCCESS_UPDATE);
        }
    }

    /**
     * Realiza validações no bean.
     *
     * @return {@link Boolean}
     */
    private Boolean validDocumentoLegal(DocumentoLegalBean bean) {
        DocumentoLegal documentoLegal = bean.getTo();
        return true;
    }

    /**
     * Cancela edição de um Documento Legal (Reajuste)
     */
    public void cancelEditDocumentoLegal() {
        documentoLegalBean.reset();
    }

    public void removeMsaContrato() {
        MsaContrato msaContrato = msaContratoService.findById(msaContratoBean.getToDelete().getCodigo());
        msaContrato.setDataAtualizacao(new Date());
        msaContrato.setLoginAtualizacao(LoginUtil.getLoggedUsername());
        msaContrato.setDeleteLogico(true);

        msaContratoService.update(msaContrato);

        Messages.showSucess("deleteAnexoDocLegal", Constants.GENEREC_MSG_SUCCESS_REMOVE);
        this.loadAllMsaContratosByMsa();
    }

    /**
     * Exclui um Documento Legal (Reajuste)
     */
    public void removeDocumentoLegal() {

        DocumentoLegal documentoLegal = documentoLegalService
                .findDocumentoLegalById(documentoLegalBean.getDocLegalToDelete()
                        .getCodigoDocumentoLegal());

        // Remove o documento legal e os anexos das tabelas
        documentoLegalService.deleteDocumentoLegal(documentoLegal);

        Messages.showSucess("removeDocumentoLegal",
                Constants.GENEREC_MSG_SUCCESS_REMOVE);

        documentoLegalBean.reset();

        // Busca os Documentos Legais por MSA para atualizar a lista na tela
        this.buscaListaDocumentoLegal();
    }

    /**
     * Inativar um documento legal. Em casos em que um documento legal est�
     * vinculado com uma ficha que possui um controle de reajuste com
     * altera��es.
     */
    public void inactivateDocumentoLegal() {

        DocumentoLegal documentoLegal = documentoLegalService
                .findDocumentoLegalById(documentoLegalBean.getDocLegalToDelete()
                        .getCodigoDocumentoLegal());

        documentoLegal.setTextoObservacao(documentoLegalBean
                .getInactivateComment());

        documentoLegal.setIndicadorStatus(StatusDocumentoLegal.INACTIVE
                .getAbbreviation());

        documentoLegalService.updateDocumentoLegal(documentoLegal);

        // Busca os Documentos Legais por MSA para atualizar a lista na tela
        this.buscaListaDocumentoLegal();
        documentoLegalBean.reset();

        Messages.showSucess("inactivateDocumentoLegal",
                Constants.DOCUMENTO_LEGAL_SUCCESFULLY_INACTIVATED);
    }

    /**
     * Busca os Documentos Legais por MSA.
     */
    private void buscaListaDocumentoLegal() {
        Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());

        List<DocumentoLegal> documentoLegalList = documentoLegalService
                .findDocumentoLegalByMsa(msa);

        documentoLegalBean.setResultList(DocumentoLegal.orderList(documentoLegalList));
    }

    /**
     * Prepare Add Anexo docLegal
     */
    public void prepareAddAnexoDocLegal() {
        documentoLegalConfigureBean.setTextoComentarioAnexo(null);
        documentoLegalConfigureBean.setUploadItem(null);
    }

    public void uploadAnexoLegalDoc(final UploadEvent event) {
        documentoLegalConfigureBean.setUploadItem(event.getUploadItem());
    }

    public void saveAnexoDocumentoLegal() {


        DocumentoLegalAnexoArquivo documentoLegalAnexoArquivo = new DocumentoLegalAnexoArquivo();

        documentoLegalAnexoArquivo.setDataDocLegalAnexoArq(new Date());
        documentoLegalAnexoArquivo.setPessoa(LoginUtil.getLoggedUser());
        documentoLegalAnexoArquivo
                .setTextoComentario(documentoLegalConfigureBean.getTextoComentarioAnexo());
        try {

            if (documentoLegalConfigureBean.getUploadItem() != null) {

                documentoLegalAnexoArquivo
                        .setNomeDocLegalAnexoArq(documentoLegalConfigureBean
                                .getUploadItem().getFileName());

                docLegalAnexoArquivoService.saveUploadAnexo(
                        documentoLegalConfigureBean.getUploadItem(),
                        documentoLegalAnexoArquivo);
            } else {
                Messages.showWarning("uploadAnexoDocumentoLegal",
                        Constants.ANEXO_EMPTY);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }

    }

    /**
     * Realiza o download do anexo da tabela de precos.
     */
    public void downloadAnexoDocLegal() {

        String fileName = documentoLegalConfigureBean
                .getDocumentoLegalAnexoArquivo().getNomeDocLegalAnexoArq();

        docLegalAnexoArquivoService
                .downloadDocumentoLegalAnexoArquivo(fileName);
    }

    /**
     * Apaga um anexo.
     */
    public void deleteAnexoDocLegal() {

        DocumentoLegalAnexoArquivo anexo = docLegalAnexoArquivoService
                .findDocumentoLegalAnexoArquivoById(documentoLegalConfigureBean
                        .getDocumentoLegalAnexoArquivo()
                        .getCodigoDocLegalAnexoArq());
        docLegalAnexoArquivoService.deleteDocumentoLegalAnexoArquivo(anexo
                .getNomeDocLegalAnexoArq());
        docLegalAnexoArquivoService.deleteDocumentoLegalAnexoArquivo(anexo);

        Messages.showSucess("deleteAnexoDocLegal",
                Constants.GENEREC_MSG_SUCCESS_REMOVE);

    }

    /**
     * Retorna o a lista dos nomes de FichaReajuste.
     *
     * @return Lista de {@link FichaReajuste}.
     */
    public List<String> buildComboFichaReajuste(
            List<FichaReajuste> fichasReajuste) {

        List<String> nomeFichaReajuste = new ArrayList<String>();
        nomeFichaReajuste.add(Constants.FICHA_REAJUSTE_NEW);
        for (FichaReajuste fichaReajuste : fichasReajuste) {
            nomeFichaReajuste.add(fichaReajuste.getNomeFichaReajuste());
        }

        return nomeFichaReajuste;
    }

    private Boolean validatePaymentCondition() {
        if (bean.getPaymentConditionsDealFiscal() != null && bean.getPaymentConditionsDealFiscal().size() > 0) {
            for (PaymentConditionDealFiscal obj : bean.getPaymentConditionsDealFiscal()) {
                if (obj.getFinalDate() == null) {
                    return false;
                }
            }
        }

        return true;
    }

    private void showPaymentConditionError() {
        Messages.showError("associatePaymentCondition",
                Constants.PAYMENT_CONDITION_NAME_IS_REQUIRED);
    }

    private void showIntercompanyActiveAllocationsError() {
        Messages.showError("hasIntercompanyAllocation",
                Constants.MSG_ERROR_NOT_POSSIBLE_REMOVE_INTERCOMP_BECAUSE_THERE_ARE_ACTIVES_ALLOCATIONS);
    }

    private void showIntercompanyRevenuesNotIntegratedError() {
        Messages.showError("hasIntercompanyRevenues",
                Constants.MSG_ERROR_NOT_POSSIBLE_REMOVE_INTERCOMP_BECAUSE_THERE_ARE_REVENUES_NOT_INTEGRATED);
    }

    /**
     * Valida se a Empresa do Inter-Company é a mesma do Fiscal Deal
     *
     * @param mainCompany  Empresa do Fiscal Deal
     * @param interCompany Empresa do Inter-Company
     */
    private void validateIntercompanyBetweenSameCompany(Empresa mainCompany, Empresa interCompany) {

        if (mainCompany.getEmpresa().getCodigoEmpresa() != null && interCompany.getEmpresa().getCodigoEmpresa() != null) {
            if (mainCompany.getEmpresa().getCodigoEmpresa() == interCompany.getEmpresa().getCodigoEmpresa()) {
                throw new ValidatorException(
                        Messages.getMessageError(Constants.MSG_ERROR_INTERCOMP_BETWEEN_SAME_COMPANY));
            }
        }
    }

    /**
     * Valida a configuração de intercompany
     *
     * @param mainCompany  Empresa do deal fiscal
     * @param interCompany Empresa intercompany
     */
    private void validateIntercompanyConfiguration(Empresa mainCompany, Empresa interCompany) {

        List<IntercompanyConfig> intercompanyConfigs =
                interCompanyConfigService.findByCompanies(
                        mainCompany.getEmpresa().getCodigoEmpresa(),
                        interCompany.getEmpresa().getCodigoEmpresa());
    }

    /**
     * Valida se há alocações ativas e/ou receitas não integradas após o Closing Date respectivo
     */
    private Boolean validateActiveAllocationsAndIntegratedRevenueAfterClosingDase() {

        Boolean hasIntercompanyActiveAllocations = Boolean.FALSE;
        Boolean hasIntercompanyRevenueNotIntegrated = Boolean.FALSE;

        DealFiscal df = dealFiscalService.findDealFiscalById(bean.getToDealFiscal().getCodigoDealFiscal());
        Boolean oldValueInIntercompany = df.getIndicadorIntercompany();
        Boolean newValueInIntercompany = bean.getToDealFiscal().getIndicadorIntercompany();

        if (oldValueInIntercompany && !newValueInIntercompany) {

            if (this.validateHasIntercompanyActiveAllocations()) {
                this.showIntercompanyActiveAllocationsError();
                hasIntercompanyActiveAllocations = Boolean.TRUE;
            }
            if (this.validateNotIntegratedRevenue()) {
                this.showIntercompanyRevenuesNotIntegratedError();
                hasIntercompanyRevenueNotIntegrated = Boolean.TRUE;
            }

            if (!hasIntercompanyActiveAllocations && !hasIntercompanyRevenueNotIntegrated) {
                this.updateIntercompanyFinalDate(bean.getToDealFiscal());
            }

            if (hasIntercompanyActiveAllocations || hasIntercompanyRevenueNotIntegrated) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Valida se há alocações ativas após o Closing Date de Mapas de Alocação associados ao Fiscal Deal
     */
    private Boolean validateHasIntercompanyActiveAllocations() {

        Date closingDateAllocationMap = moduloService.getClosingDateMapaAlocacao();
        Long dealFiscalCompanyId = bean.getToDealFiscal().getEmpresa().getCodigoEmpresa();
        Empresa mainCompany = this.empresaService.findEmpresaById(dealFiscalCompanyId);

        List<DealFiscal> fiscalDealList =
                dealFiscalService.findFiscalDealWithActiveAllocationsInAllocationMapByFiscalDealAndClosingMapDate(
                        bean.getToDealFiscal().getCodigoDealFiscal(),
                        closingDateAllocationMap,
                        mainCompany.getEmpresa().getCodigoEmpresaERP());

        if (fiscalDealList != null && fiscalDealList.size() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Valida se há receitas não integradas após o Closing Date de Internacional Revenue associadas ao Fiscal Deal
     */
    private Boolean validateNotIntegratedRevenue() {

        Date closingDateInternationalRevenue = moduloService.getClosingDateInternationalRevenue();

        List<Receita> receitaList =
                receitaService.findNotIntegratedRevenueAfterClosingRevenueDate(bean.getToDealFiscal().getCodigoDealFiscal(),
                        closingDateInternationalRevenue);

        if (receitaList != null && receitaList.size() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Atualiza o Final Date de vigência dos Intercompanies configurados no Fiscal Deal
     */
    private void updateIntercompanyFinalDate(DealFiscal dealFiscal) {

        Set<HistoricoPercentualIntercomp> historicoPercentualIntercomps =
                dealFiscal.getHistoricoPercentualIntercomps();

        for (HistoricoPercentualIntercomp item : historicoPercentualIntercomps) {
            Date currentDate = DateUtil.getDate(new Date());
            if (item.getDataFim() == null) {
                item.setDataFim(DateUtil.getDateLastDayOfMonth(currentDate));
            }
        }
    }

    public MsaContratoBean getMsaContratoBean() {
        return msaContratoBean;
    }

    public void setMsaContratoBean(final MsaContratoBean msaContratoBean) {
        this.msaContratoBean = msaContratoBean;
    }

    private void loadMsaTipoContratoCombo(final List<MsaTipoContrato> msaTipoContratos) {
        List<String> tiposContratosNames = new ArrayList<String>();
        Map<String, Long> tiposContratosMap = new HashMap<String, Long>();
        for (MsaTipoContrato tipoContrato : msaTipoContratos) {
            tiposContratosNames.add(tipoContrato.getAcronimo());
            tiposContratosMap.put(tipoContrato.getAcronimo(), tipoContrato.getCodigo());
        }

        Collections.sort(tiposContratosNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        this.msaContratoBean.setTipoContratoComboList(tiposContratosNames);
        this.msaContratoBean.setTipoContratoMap(tiposContratosMap);

        // CONVERTER STRING EM ARRAY NO JAVASCRIPT:
        // VAR.SPLIT(",")
        // A INFORMAÇÃO DEVE ESTAR EM UMA UNICA STRING SEM O COLCHETES E SEPARADO POR VIRGULA;
    }

    private void loadMsaTipoMontanteDespesaCombo(final List<MsaTipoMontanteDespesa> msaTipoMontanteDespesas) {
        List<String> tipoMontanteDespesaNames = new ArrayList<String>();
        Map<String, Long> tipoMontanteDespesasMap = new HashMap<String, Long>();
        for (MsaTipoMontanteDespesa tipoMontanteDespesa : msaTipoMontanteDespesas) {
            tipoMontanteDespesaNames.add(tipoMontanteDespesa.getName());
            tipoMontanteDespesasMap.put(tipoMontanteDespesa.getName(), tipoMontanteDespesa.getCodigo());
        }

        Collections.sort(tipoMontanteDespesaNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        this.msaContratoBean.setTipoMontanteDespesaList(tipoMontanteDespesaNames);
        this.msaContratoBean.setTipoMontanteDespesaMap(tipoMontanteDespesasMap);
    }

    private void loadMsaStatusContratoCombo(final List<MsaStatusContrato> msaStatusContratos) {
        List<String> statusContratosNames = new ArrayList<String>();
        Map<String, Long> statusContratosMap = new HashMap<String, Long>();

        for (MsaStatusContrato statusContrato : msaStatusContratos) {
            statusContratosNames.add(statusContrato.getNome());
            statusContratosMap.put(statusContrato.getNome(), statusContrato.getCodigo());
        }

        Collections.sort(statusContratosNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        this.msaContratoBean.setStatusContratosList(statusContratosNames);
        this.msaContratoBean.setStatusContratosMap(statusContratosMap);
    }

    private void loadMsaStatusContratoSearchCombo(final List<MsaStatusContrato> msaStatusContratos) {
        List<String> statusContratosNames = new ArrayList<String>();
        Map<String, Long> statusContratosMap = new HashMap<String, Long>();

        statusContratosMap.put(Constants.ALL, 0L);
        statusContratosNames.add(Constants.ALL);

        for (MsaStatusContrato statusContrato : msaStatusContratos) {
            statusContratosNames.add(statusContrato.getNome());
            statusContratosMap.put(statusContrato.getNome(), statusContrato.getCodigo());
        }

        Collections.sort(statusContratosNames, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        this.msaContratoBean.setStatusContratosSearchList(statusContratosNames);
        this.msaContratoBean.setStatusContratosSearchMap(statusContratosMap);
    }

    private void loadMsaMonthOfReadjustment() {
        List<String> monthsList = new ArrayList<String>();
        monthsList.add("N/A");
        for (int i = 0; i < DateUtil.getMonthsList().toArray().length; i++) {
            monthsList.add(DateUtil.getMonthsList().get(i));
        }
        this.msaContratoBean.setMonthsList(monthsList);
    }

    public void prepareMsaContratos() {
        this.msaContratoBean.reset();
        this.msaContratoBean.getResultList().clear();
        this.msaContratoBean.getCnpjList().clear();
        this.loadAllMsaContratosByMsa();
        this.loadMsaTipoContratoCombo(msaTipoContratoService.findAllActive());
        this.loadBasesLegalDocCheckboxList();
        this.loadServiceTypeLegalDocCheckboxList();
        this.loadMsaTipoMontanteDespesaCombo(msaTipoMontanteDespesaService.findAllActive());
        this.loadMsaStatusContratoCombo(msaStatusContratoService.findAllActive());
        this.loadMsaStatusContratoSearchCombo(msaStatusContratoService.findAllActive());
        this.loadMoedaList(this.logicaMoedaList(bean.getTo()));
        this.loadMoedaSearchList(this.logicaMoedaSearchList(bean.getTo()));
        this.loadMsaMonthOfReadjustment();
        this.msaContratoBean.setIsITSupport(validateITSupport());
    }

    /**
     * Busca os Documentos Legais por MSA.
     */
    public void loadAllMsaContratosByMsa() {
        msaContratoBean.resetPagination();
        Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());

        List<MsaContrato> msaContratoList = msaContratoService.findByMsaAndMsaStatusContratoDefaultPesquisa(msa, msaContratoBean.getNotAssignerOrValidStatus());
        if (null != msaContratoList && !msaContratoList.isEmpty()) {
            for (MsaContrato msaContrato : msaContratoList) {
                msaContrato.setTipoServicos(msaContratoTipoServicoService.findByMsaContratoCode(msaContrato.getCodigo()));

                msaContrato.setFilials(msaContratoFilialService.findByMsaContratoCode(msaContrato.getCodigo()));
            }
        }

        msaContratoBean.setResultList(msaContratoList);
    }

    public void saveMsaContrato() {
        if (this.validateRequiredFieldsAndJiraExists()) {

            List<MsaContratoCnpj> msaContratoCnpjList = msaContratoBean.getCnpjList();
            MsaContrato msaContrato = msaContratoBean.getTo();
            msaContrato.setJiraCp(deleteWhitespace(msaContrato.getJiraCp()));
            msaContrato.setJiraLegal(deleteWhitespace(msaContrato.getJiraLegal()));
            msaContrato.setMonthOfReadjustment(this.msaContratoBean.getMonthSelected());

            Msa msa = msaService.findMsaById(bean.getTo().getCodigoMsa());
            msaContrato.setMsa(msa);

            Long typeCode = this.msaContratoBean.getTipoContratoMap().get(this.msaContratoBean.getTipoContratoSelected());
            MsaTipoContrato tipoContrato = msaTipoContratoService.findById(typeCode);
            msaContrato.setTipoContrato(tipoContrato);

            Long currencyCode = this.bean.getMoedaMap().get(this.msaContratoBean.getMoedaSelected());
            Moeda moeda = moedaService.findMoedaById(currencyCode);
            msaContrato.setMoeda(moeda);

            Long amountExpenseTypeCode = this.msaContratoBean.getTipoMontanteDespesaMap().get(this.msaContratoBean.getTipoMontanteDespesaSelected());
            MsaTipoMontanteDespesa tipoMontanteDespesa = msaTipoMontanteDespesaService.findById(amountExpenseTypeCode);
            msaContrato.setTipoDespesa(tipoMontanteDespesa);

            Long statusCode = this.msaContratoBean.getStatusContratosMap().get(this.msaContratoBean.getStatusContratoSelected());
            MsaStatusContrato statusContrato = msaStatusContratoService.findById(statusCode);
            msaContrato.setStatus(statusContrato);

            if (!msaContrato.getFte()) {
                msaContrato.setHasLimit(Boolean.FALSE);
            }

            MsaContrato msaContratoPersisted = msaContratoService.save(msaContrato);

            this.saveTiposServico(msaContratoPersisted.getCodigo());
            this.saveBasesFiliais(msaContratoPersisted.getCodigo());

            if (!msaContratoCnpjList.isEmpty()) {
                for (MsaContratoCnpj cnpj : msaContratoCnpjList) {
                    cnpj.setMsaContrato(msaContratoPersisted);
                }
                this.saveMsaContratoCnpj(msaContratoPersisted, msaContratoCnpjList);
            }

            if (this.msaContratoBean.getIsUpdate()) {
                Messages.showSucess("createDocumentoLegal", Constants.DEFAULT_MSG_SUCCESS_UPDATE, "Contract");
            } else {
                Messages.showSucess("createDocumentoLegal", Constants.DEFAULT_MSG_SUCCESS_CREATE, "Contract");
            }
            this.prepareMsaContratos();
        }
    }

    private void saveMsaContratoCnpj(MsaContrato msaContrato, List<MsaContratoCnpj> msaContratoCnpjList) {
        List<MsaContratoCnpj> cnpjList = msaContratoCnpjService.findByMsaContrato(msaContrato);
        for (MsaContratoCnpj msaContratoCnpj : cnpjList) {
            msaContratoCnpjService.delete(msaContratoCnpj);
        }

        for (MsaContratoCnpj contratoCnpj : msaContratoCnpjList) {
            MsaContratoCnpj novoCnpj = new MsaContratoCnpj();
            novoCnpj.setMsaContrato(contratoCnpj.getMsaContrato());
            novoCnpj.setMsaContratoCnpj(contratoCnpj.getMsaContratoCnpj());
            msaContratoCnpjService.save(novoCnpj);
        }
    }

    private void saveBasesFiliais(Long msaContratoCode) {
        List<MsaContratoFilial> filiaisSelected = new ArrayList<MsaContratoFilial>();
        List<Long> filiaisCodes = new ArrayList<Long>();

        for (String filial : this.msaContratoBean.getBasesDocumentoLegalSelected()) {
            filiaisCodes.add(Long.valueOf(filial));
        }
        List<Empresa> empresas = empresaService.findEmpresaByIdIn(filiaisCodes);

        for (Empresa empresa : empresas) {
            MsaContratoFilial filial = new MsaContratoFilial();
            filial.setCodigoMsaContrato(msaContratoCode);
            filial.setFilial(empresa);
            filiaisSelected.add(filial);
        }

        msaContratoFilialService.removeByMsaContratoCode(msaContratoCode);
        msaContratoFilialService.save(filiaisSelected);
    }

    private void saveTiposServico(Long msaContratoCode) {
        List<MsaContratoTipoServico> tiposServicoSelected = new ArrayList<MsaContratoTipoServico>();
        List<Long> tiposServicoCodes = new ArrayList<Long>();

        for (String tipoServico : this.msaContratoBean.getServiceTypesDocumentoLegalSelected()) {
            tiposServicoCodes.add(Long.valueOf(tipoServico));
        }
        List<TipoServico> tiposServicos = tipoServicoService.findByIdIn(tiposServicoCodes);

        for (TipoServico tipoServico : tiposServicos) {
            MsaContratoTipoServico msaContratoTipoServico = new MsaContratoTipoServico();
            msaContratoTipoServico.setCodigoMsaContrato(msaContratoCode);
            msaContratoTipoServico.setTipoServico(tipoServico);
            tiposServicoSelected.add(msaContratoTipoServico);
        }

        msaContratoTipoServicoService.removeByMsaContratoCode(msaContratoCode);
        msaContratoTipoServicoService.merge(tiposServicoSelected);
    }

    private Boolean validateRequiredFieldsAndJiraExists() {
        Boolean isValidated = Boolean.TRUE;
        MsaContrato msaContrato = this.msaContratoBean.getTo();

        if (null == msaContrato.getDescricao() || msaContrato.getDescricao().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.project_description.label"));
            isValidated = Boolean.FALSE;
        }
        if (null == msaContrato.getJiraCp() || msaContrato.getJiraCp().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.jira_cp.label"));
            isValidated = Boolean.FALSE;
        }
        if (null == this.msaContratoBean.getTipoContratoSelected() || this.msaContratoBean.getTipoContratoSelected().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.tipo.label"));
            isValidated = Boolean.FALSE;
        }
        if (null == this.msaContratoBean.getMoedaSelected() || this.msaContratoBean.getMoedaSelected().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.currency.label"));
            isValidated = Boolean.FALSE;
        }

        if (this.msaContratoBean.getMonthSelected() == null || this.msaContratoBean.getMonthSelected().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls_msa.documento_legal.month_of_readjustment.label"));
            isValidated = Boolean.FALSE;
        }

        if (null == msaContrato.getFte()) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.fte.label"));
            isValidated = Boolean.FALSE;
        }
        if (null != msaContrato.getFte() && !msaContrato.getFte()
                && (null == this.msaContratoBean.getTipoMontanteDespesaSelected()
                || (!this.tipoMontanteDespesaIsValue() && !this.tipoMontanteDespesaIsPercent()))) {
            if (null == msaContrato.getTotalContrato()) {
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                        BundleUtil.getBundle("_nls.msa.documento_legal.total_contract_amount.label"));
                isValidated = Boolean.FALSE;
            } else if (msaContrato.getTotalContrato() <= (double) 0) {
                Object[] params = {BundleUtil.getBundle("_nls.msa.documento_legal.total_contract_amount.label"), "0.00"};
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_GREATER_THAN,
                        params);
                isValidated = Boolean.FALSE;
            }
        }

        if (null != msaContrato.getFte() && msaContrato.getFte()) {
            if (msaContrato.getHasLimit() == null) {
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                        BundleUtil.getBundle("_nls.msa.documento_legal.haslimit.label"));
                isValidated = Boolean.FALSE;
            } else if (msaContrato.getTotalContrato() <= (double) 0 && msaContrato.getHasLimit()) {
                Object[] params = {BundleUtil.getBundle("_nls.msa.documento_legal.total_contract_amount.label"), "0.00"};
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_GREATER_THAN,
                        params);
                isValidated = Boolean.FALSE;
            }
        }
        if (null == this.msaContratoBean.getTipoMontanteDespesaSelected() || this.msaContratoBean.getTipoMontanteDespesaSelected().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.type_amount_expenses.label"));
            isValidated = Boolean.FALSE;
        }
        if (this.tipoMontanteDespesaIsPercent()) {
            if (null == msaContrato.getPorcentagemDespesa()) {
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                        BundleUtil.getBundle("_nls.msa.documento_legal.percent_expenses.label"));
                isValidated = Boolean.FALSE;
            } else if (msaContrato.getPorcentagemDespesa() <= (double) 0) {
                Object[] params = {BundleUtil.getBundle("_nls.msa.documento_legal.percent_expenses.label"), "0.00"};
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_GREATER_THAN,
                        params);
                isValidated = Boolean.FALSE;
            } else if (msaContrato.getPorcentagemDespesa() > 100.0) {
                Object[] params = {BundleUtil.getBundle("_nls.msa.documento_legal.percent_expenses.label"), "100.00"};
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_LOWER_THAN,
                        params);
                isValidated = Boolean.FALSE;
            }
        }
        if (this.tipoMontanteDespesaIsValue()) {
            if (null == msaContrato.getValorDespesa()) {
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                        BundleUtil.getBundle("_nls.msa.documento_legal.total_amount_expenses.label"));
                isValidated = Boolean.FALSE;
            } else if (msaContrato.getValorDespesa() <= (double) 0) {
                Object[] params = {BundleUtil.getBundle("_nls.msa.documento_legal.total_amount_expenses.label"), "0.00"};
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_GREATER_THAN,
                        params);
                isValidated = Boolean.FALSE;
            }
        }
        if (null == this.msaContratoBean.getStatusContratoSelected() || this.msaContratoBean.getStatusContratoSelected().equalsIgnoreCase("")) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.status.label"));
            isValidated = Boolean.FALSE;
        }
        if (null == msaContrato.getDataInicial()) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.data_inicio.label"));
            isValidated = Boolean.FALSE;
        }
        if (!msaContrato.getDataIndeterminada()) {
            if (null == msaContrato.getDataFinal()) {
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                        BundleUtil.getBundle("_nls.msa.documento_legal.data_fim.label"));
                isValidated = Boolean.FALSE;
            } else if (null != msaContrato.getDataInicial()
                    && msaContrato.getDataInicial().after(msaContrato.getDataFinal())) {
                Object[] params = {BundleUtil.getBundle("_nls.msa.documento_legal.data_fim.label"),
                        BundleUtil.getBundle("_nls.msa.documento_legal.data_inicio.label")};
                Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_GREATER_THAN,
                        params);
                isValidated = Boolean.FALSE;
            }
        }
        if (null == this.msaContratoBean.getServiceTypesDocumentoLegalSelected() || this.msaContratoBean.getServiceTypesDocumentoLegalSelected().isEmpty()) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.service_type.label"));
            isValidated = Boolean.FALSE;
        }
        if (null == this.msaContratoBean.getBasesDocumentoLegalSelected() || this.msaContratoBean.getBasesDocumentoLegalSelected().isEmpty()) {
            Messages.showError("createDocumentoLegal", Constants.DEFAULT_MSG_ERROR_VALUE_REQUIRED,
                    BundleUtil.getBundle("_nls.msa.documento_legal.bases.label"));
            isValidated = Boolean.FALSE;
        }

        if (isValidated && Boolean.FALSE.equals(msaContratoService.validateUniqueContract(msaContrato, this.bean.getTo().getCodigoMsa()))) {
            Messages.showError("createDocumentoLegal", Constants.MSA_CONTRATO_UNIQUE_KEY_ALREADY_EXISTS);
            isValidated = Boolean.FALSE;
        }

        return isValidated;
    }

    private Boolean tipoMontanteDespesaIsPercent() {
        this.msaContratoBean.setTipoMontanteDespesaIsPercent(
                null != this.msaContratoBean.getTipoMontanteDespesaSelected()
                        && !this.msaContratoBean.getTipoMontanteDespesaSelected().equalsIgnoreCase("")
                        && this.msaContratoBean.getTipoMontanteDespesaSelected().contains("%"));
        return this.msaContratoBean.getTipoMontanteDespesaIsPercent();
    }

    private Boolean tipoMontanteDespesaIsValue() {
        this.msaContratoBean.setTipoMontanteDespesaIsValue(
                null != this.msaContratoBean.getTipoMontanteDespesaSelected()
                        && !this.msaContratoBean.getTipoMontanteDespesaSelected().equalsIgnoreCase("")
                        && this.msaContratoBean.getTipoMontanteDespesaSelected().contains("$"));
        return this.msaContratoBean.getTipoMontanteDespesaIsValue();
    }

    public void clearValues() {
        if (!this.tipoMontanteDespesaIsValue()) {
            this.msaContratoBean.getTo().setValorDespesa(null);
        }
        if (!this.tipoMontanteDespesaIsPercent()) {
            this.msaContratoBean.getTo().setPorcentagemDespesa(null);
        }
        if (null != this.msaContratoBean.getTo().getDataIndeterminada()
                && this.msaContratoBean.getTo().getDataIndeterminada()) {
            this.msaContratoBean.getTo().setDataFinal(null);
        }
        if (null != this.getMsaContratoBean().getTo().getFte() && this.getMsaContratoBean().getTo().getFte()
                && this.getMsaContratoBean().getTo().getHasLimit() != null && !this.getMsaContratoBean().getTo().getHasLimit()) {
            this.msaContratoBean.getTo().setTotalContrato(null);
        }
    }

    public void prepareViewMsaContrato() {
        prepareUpdateMsaContrato();
        this.msaContratoBean.setIsITSupport(Boolean.TRUE);
    }

    public void prepareUpdateMsaContrato() {
        this.msaContratoBean.setTo(this.getNewMsaContrato(this.msaContratoBean.getToEdit()));
        this.msaContratoBean.setIsITSupport(validateITSupport());
        this.msaContratoBean.setTipoContratoSelected(this.msaContratoBean.getTo().getTipoContrato().getAcronimo());
        this.msaContratoBean.setMoedaSelected(this.msaContratoBean.getTo().getMoeda().getNomeMoeda());
        this.msaContratoBean.setTipoMontanteDespesaSelected(this.msaContratoBean.getTo().getTipoDespesa().getName());
        this.msaContratoBean.setStatusContratoSelected(this.msaContratoBean.getTo().getStatus().getNome());
        this.msaContratoBean.setMonthSelected(this.msaContratoBean.getTo().getMonthOfReadjustment());
        this.loadTipoServicoSelected();
        this.loadFiliaisSelected();
        this.msaContratoBean.setCnpjList(msaContratoCnpjService.findByMsaContrato(this.msaContratoBean.getTo()));

        this.clearValues();

        this.msaContratoBean.setIsUpdate(Boolean.TRUE);
    }

    private void loadFiliaisSelected() {
        List<MsaContratoFilial> filiaisPersisted = this.msaContratoBean.getTo().getFilials();
        List<String> filiaisSelected = new ArrayList<String>();

        for (MsaContratoFilial filial : filiaisPersisted) {
            filiaisSelected.add(filial.getFilial().getCodigoEmpresa().toString());
        }

        this.msaContratoBean.setBasesDocumentoLegalSelected(filiaisSelected);
    }

    private void loadTipoServicoSelected() {
        List<MsaContratoTipoServico> tipoServicosPersisted = this.msaContratoBean.getTo().getTipoServicos();
        List<String> tipoServicosSelected = new ArrayList<String>();

        for (MsaContratoTipoServico tipoServico : tipoServicosPersisted) {
            tipoServicosSelected.add(tipoServico.getTipoServico().getCodigoTipoServico().toString());
        }

        this.msaContratoBean.setServiceTypesDocumentoLegalSelected(tipoServicosSelected);
    }

    public void cancelEditMsaContrato() {
        this.prepareMsaContratos();
    }

    private MsaContrato getNewMsaContrato(MsaContrato contrato) {
        MsaContrato newInstance = new MsaContrato();
        newInstance.setCodigo(contrato.getCodigo());
        newInstance.setDescricao(contrato.getDescricao());
        newInstance.setJiraCp(contrato.getJiraCp());
        newInstance.setJiraLegal(contrato.getJiraLegal());
        newInstance.setSow(contrato.getSow());
        newInstance.setPo(contrato.getPo());
        newInstance.setTipoContrato(contrato.getTipoContrato());
        newInstance.setMoeda(contrato.getMoeda());
        newInstance.setFte(contrato.getFte());
        newInstance.setTotalContrato(contrato.getTotalContrato());
        newInstance.setTipoDespesa(contrato.getTipoDespesa());
        newInstance.setValorDespesa(contrato.getValorDespesa());
        newInstance.setPorcentagemDespesa(contrato.getPorcentagemDespesa());
        newInstance.setStatus(contrato.getStatus());
        newInstance.setDataInicial(contrato.getDataInicial());
        newInstance.setDataFinal(contrato.getDataFinal());
        newInstance.setDataIndeterminada(contrato.getDataIndeterminada());
        newInstance.setTipoServicos(contrato.getTipoServicos());
        newInstance.setFilials(contrato.getFilials());
        newInstance.setComentarios(contrato.getComentarios());
        newInstance.setMsa(contrato.getMsa());
        newInstance.setDeleteLogico(contrato.getDeleteLogico());
        newInstance.setDataAtualizacao(contrato.getDataAtualizacao());
        newInstance.setLoginAtualizacao(contrato.getLoginAtualizacao());
        newInstance.setHasLimit(contrato.getHasLimit());
        newInstance.setMonthOfReadjustment(contrato.getMonthOfReadjustment());

        return newInstance;
    }

    public void addMsaContratoCnpj() {

        /* Data source **/
        MsaContratoCnpj msaContratoCnpj = msaContratoCnpjBean.getTo();
        List<MsaContratoCnpj> cnpjListBean = this.msaContratoBean.getCnpjList();
        List<MsaContratoCnpj> cnpjList = new ArrayList<MsaContratoCnpj>();
        String newCnpj = msaContratoCnpj.getMsaContratoCnpj();

        /* Validations **/
        boolean isValidCnpj = (!newCnpj.isEmpty() && NumberUtil.isValidCnpj(newCnpj));
        boolean canAddCpnj = false;
        String returnMessage = "";

        if (isValidCnpj) {
            if (!cnpjListBean.isEmpty()) {
                boolean isDuplicatedCnpj = findCnpjDuplication(cnpjListBean, newCnpj);
                if (isDuplicatedCnpj) {
                    returnMessage = Constants.LEGAL_DOC_DUPLICATED_CNPJ;
                } else {
                    canAddCpnj = true;
                }
            } else {
                canAddCpnj = true;
            }
        }

        if (canAddCpnj) {
            cnpjList.add(msaContratoCnpj);
            msaContratoCnpjBean.reset();
        } else {
            if (returnMessage.isEmpty()) {
                returnMessage = Constants.LEGAL_DOC_INVALID_CNPJ;
            }
            Messages.showError("createDocumentoLegal", returnMessage);
        }

        cnpjListBean.addAll(cnpjList);
    }

    public boolean findCnpjDuplication(List<MsaContratoCnpj> cnpjList, String cnpj) {
        boolean isDuplicated = false;

        for (MsaContratoCnpj item : cnpjList) {
            if (item.getMsaContratoCnpj().equals(cnpj)) {
                isDuplicated = true;
                break;
            }
        }

        return isDuplicated;
    }

    public void removeCnpj() {
        String cnpj = this.msaContratoBean.getMsaContratoCnpjToDelete().getMsaContratoCnpj();
        List<MsaContratoCnpj> cnpjListBean = this.msaContratoBean.getCnpjList();
        List<MsaContratoCnpj> cnpjRemovido = new ArrayList<MsaContratoCnpj>();

        for (MsaContratoCnpj item : cnpjListBean) {
            if (cnpj.equals(item.getMsaContratoCnpj())) {
                cnpjRemovido.add(item);
                Messages.showWarning("remove", Constants.MSA_CONTRATO_CNPJ_WARNING_REMOVE);
            }
        }
        cnpjListBean.removeAll(cnpjRemovido);
    }

    public MsaContratoCnpjBean getMsaContratoCnpjBean() {
        return msaContratoCnpjBean;
    }

    public void setMsaContratoCnpjBean(MsaContratoCnpjBean msaContratoCnpjBean) {
        this.msaContratoCnpjBean = msaContratoCnpjBean;
    }

    public void prepareMsaSettings() {
        this.clearMsaSettingsFields();
        this.msaContratoBean.setIsITSupport(validateITSupport());
        this.setIsAdminMsa(validateAdminMsa());
        this.loadMsaChargeMethods(msaChargeMethodService.findAll());
        this.loadMsaCommercialPartnersLogins(
                msaCommercialPartnerService.findByMsaCode(this.bean.getTo().getCodigoMsa()));
        this.loadPriceTableEditorLogins(priceTableEditorService.findByMsaCode(this.bean.getTo().getCodigoMsa()));
        this.loadPriceTableApproverLogins(priceTableApproverService.findByMsaCode(this.bean.getTo().getCodigoMsa()));
    }

    private void loadMsaCommercialPartnersLogins(List<MsaCommercialPartner> commercialPartners) {
        List<String> logins = new ArrayList<String>();
        for (MsaCommercialPartner cp : commercialPartners) {
            logins.add(cp.getLogin());
        }
        this.bean.setMsaSettingsLoginsList(logins);
    }

    private void loadPriceTableEditorLogins(List<PriceTableEditor> priceTableEditors) {
        List<String> logins = new ArrayList<String>();
        for (PriceTableEditor pte : priceTableEditors) {
            logins.add(pte.getLogin());
        }
        this.bean.setMsaSettingsPriceTableEditorList(logins);
    }

    private void loadPriceTableApproverLogins(List<PriceTableApprover> priceTableApprovers) {
        List<String> logins = new ArrayList<String>();
        for (PriceTableApprover pta : priceTableApprovers) {
            logins.add(pta.getLogin());
        }
        this.bean.setMsaSettingsPriceTableApproverList(logins);
    }

    private void clearMsaSettingsFields() {
        this.bean.setMsaSettingsLoginsList(new ArrayList<String>());
        this.bean.setMsaSettingsPriceTableEditorList(new ArrayList<String>());
        this.bean.setMsaSettingsPriceTableApproverList(new ArrayList<String>());
        this.bean.setMsaChargeMethods(new ArrayList<SelectItem>());
        this.bean.setSelectedChargeMethod("");
    }

    private void loadMsaChargeMethods(List<MsaChargeMethod> msaChargeMethods) {
        for (MsaChargeMethod chargeMethod : msaChargeMethods) {
            SelectItem item = new SelectItem(chargeMethod.getCode().toString(), chargeMethod.getName());

            this.bean.getMsaChargeMethods().add(item);
            this.bean.getMsaChargeMethodMap().put(chargeMethod.getCode(), chargeMethod);

            if (this.bean.getTo().getChargeMethod() != null
                    && this.bean.getTo().getChargeMethod().getCode() != null
                    && this.bean.getTo().getChargeMethod().getCode().equals(chargeMethod.getCode())) {
                this.bean.setSelectedChargeMethod(this.bean.getTo().getChargeMethod().getCode().toString());
            }
        }
    }

    public void saveMsaSettings() {
        try {
            Msa msa = this.bean.getTo();

            MsaChargeMethod selectedMethod = this.bean.getMsaChargeMethodMap().get(
                    Long.valueOf(this.bean.getSelectedChargeMethod()));
            msa.setChargeMethod(selectedMethod);

            msaService.updateMsa(msa);

            List<MsaCommercialPartner> persistedLogins = msaCommercialPartnerService.update(
                    msa, this.bean.getMsaSettingsLoginsList());
            this.loadMsaCommercialPartnersLogins(persistedLogins);

            if (!this.bean.getAllPriceTableEditorToRemove().isEmpty()) {
                for (PriceTableEditor entityToRemove : this.bean.getAllPriceTableEditorToRemove()) {
                    this.priceTableEditorService.removePriceTableEditor(entityToRemove);
                }
            }
            if (!this.bean.getAllPriceTableApproverToRemove().isEmpty()) {
                for (PriceTableApprover approverToRemove : this.bean.getAllPriceTableApproverToRemove()) {
                    this.priceTableApproverService.removePriceTableApprover(approverToRemove);
                }
            }

            priceTableEditorService.update(msa,
                    this.bean.getMsaSettingsPriceTableEditorList());
            this.loadPriceTableEditorLogins(priceTableEditorService.findByMsaCode(msa.getCodigoMsa()));

            priceTableApproverService.update(msa, this.bean.getMsaSettingsPriceTableApproverList());
            this.loadPriceTableApproverLogins(priceTableApproverService.findByMsaCode(msa.getCodigoMsa()));


            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_MSA_SETTINGS);
        } catch (Exception ex) {
            Messages.showError("update", Constants.MSA_SETTINGS_GENERIC_ERROR);
        }
    }

    public void addLoginCommercialPartner() {
        String newLoginAdded = bean.getNewLoginAdded();

        if (!newLoginAdded.isEmpty()) {
            List<String> loginList = this.bean.getMsaSettingsLoginsList();
            String newLogin = bean.getNewLoginAdded();
            boolean isDuplicatedLogin = findSettingsLoginDuplication(loginList, newLogin);

            if (!isDuplicatedLogin) {
                loginList.add(newLogin);
                this.bean.setMsaSettingsLoginsList(loginList);
                this.bean.setNewLoginAdded("");
            } else {
                Messages.showError("createDocumentoLegal", Constants.MSA_SETTINGS_DUPLICATED_LOGIN);
            }
        }
    }

    public void addPriceTableEditorLogin() {
        this.setIsAdminMsa(true);
        String newLoginAdded = bean.getNewPriceTableEditorLoginAdded();

        if (!newLoginAdded.isEmpty() && !LoginUtil.getLoggedUsername().equals(newLoginAdded)) {
            List<String> editorLoginList = this.bean.getMsaSettingsPriceTableEditorList();
            String newLogin = bean.getNewPriceTableEditorLoginAdded();
            boolean isDuplicatedLogin = findSettingsLoginDuplication(editorLoginList, newLogin);

            Long msaCode = bean.getTo() != null ? bean.getTo().getCodigoMsa() : bean.getCurrentMsaId();
            PriceTableApprover isApproverPriceTableLogin = priceTableApproverService.findByLoginAndMsaCode(newLogin, msaCode);

            if (!isDuplicatedLogin && (isApproverPriceTableLogin == null && !this.bean.getMsaSettingsPriceTableApproverList().contains(newLogin))) {
                editorLoginList.add(newLogin);
                this.bean.setMsaSettingsPriceTableEditorList(editorLoginList);
                this.bean.setNewPriceTableEditorLoginAdded("");
            } else if (isApproverPriceTableLogin != null || this.bean.getMsaSettingsPriceTableApproverList().contains(newLogin)) {
                Messages.showError("msaSettingsLogin", Constants.MSM_ERROR_ADD_LOGIN_EXISTING_IN_EDITOR_OR_APPROVER);
            } else {
                Messages.showError("msaSettingsLogin", Constants.MSA_SETTINGS_DUPLICATED_LOGIN);
            }
        } else if (!newLoginAdded.isEmpty() && LoginUtil.getLoggedUsername().equals(newLoginAdded)) {
            Messages.showError("msaSettingsLogin", Constants.MSM_ERROR_ADD_YOUR_OWN_LOGIN_IN_EDITOR);
        }
    }

    public void addPriceTableApproverLogin() {
        String MSA_CLIENT_ID = "msaSettingsLogin";
        this.setIsAdminMsa(true);
        String newLoginAdded = bean.getNewPriceTableApproverLoginAdded().trim();
        if (newLoginAdded.isEmpty()) {
            return;
        }

        List<String> approverLoginList = this.bean.getMsaSettingsPriceTableApproverList();
        String newLogin = bean.getNewPriceTableApproverLoginAdded();

        boolean isDuplicatedLogin = findSettingsLoginDuplication(approverLoginList, newLogin);
        if (isDuplicatedLogin) {
            Messages.showError("msaSettingsLogin", Constants.MSA_SETTINGS_DUPLICATED_LOGIN);
            return;
        }

        if (this.isCurrentMsaEditor(newLogin)) {
            Messages.showError("msaSettingsLogin", Constants.MSM_ERROR_ADD_LOGIN_EXISTING_IN_EDITOR_OR_APPROVER);
            return;
        }

        if (this.isCurrentPriceTableEditor(newLogin, bean.getTo())){
            Messages.showError("msaSettingsLogin", Constants.MSG_ERROR_ADD_LOGIN_WITH_CHANGES_IN_PROGRESS);
            return;
        }

        approverLoginList.add(newLogin);
        this.bean.setMsaSettingsPriceTableApproverList(approverLoginList);
        this.bean.setNewPriceTableApproverLoginAdded("");
     }

    /**
     * Checks if the login is MSA editor being configured
     * @param login login to be verified
     * @return true if editors list contains the login
     */
    private boolean isCurrentMsaEditor(String login) {
        return !this.bean.getMsaSettingsPriceTableEditorList().isEmpty() &&
                this.bean.getMsaSettingsPriceTableEditorList().contains(login);
    }

    /**
     * Checks if the login is MSA editor and has editable values to approve
     * @param login login to be verified
     * @param msa to find Price Tables
     * @return true if editors list contains the login
     */
    private boolean isCurrentPriceTableEditor(String login, Msa msa) {

        List<TabelaPreco> priceTableList = tabelaPrecoService.findTabelaPrecoByMsa(msa);
        if(priceTableList != null && !priceTableList.isEmpty()){
            for (TabelaPreco priceTable : priceTableList) {
                List<PriceTableHistory> histories = priceTableHistoryService.findLastEditedHistories(priceTable.getCodigoTabelaPreco());
                if(histories != null && !histories.isEmpty()){
                    for (PriceTableHistory history : histories) {
                        if(history.getLogin().equals(login))
                            return Boolean.TRUE;
                    }
                }
            }
        }

        return Boolean.FALSE;
    }

    public boolean findSettingsLoginDuplication(List<String> loginList, String login) {
        boolean isDuplicated = false;

        for (String item : loginList) {
            if (item.equals(login)) {
                isDuplicated = true;
                break;
            }
        }

        return isDuplicated;
    }

    public void removeLoginCommercialPartner() {
        List<String> listToRemove = new ArrayList<String>();
        listToRemove.add(this.bean.getLoginToRemove());
        this.bean.getMsaSettingsLoginsList().removeAll(listToRemove);
    }

    public void removePriceTableEditorLogin() {
        List<String> listToRemove = new ArrayList<String>();
        listToRemove.add(this.bean.getLoginToRemove());
        this.bean.getMsaSettingsPriceTableEditorList().removeAll(listToRemove);

        PriceTableEditor priceTableEditorLoginRemoved =
                this.priceTableEditorService.findByLoginAndMsaCode(this.bean.getLoginToRemove(),
                        this.bean.getTo().getCodigoMsa());
        if (priceTableEditorLoginRemoved != null) {
            this.bean.setAllPriceTableEditorToRemove(priceTableEditorLoginRemoved);
        }

    }

    public void removePriceTableApproverLogin() {
        List<String> listToRemove = new ArrayList<String>();
        String loginToRemove = this.bean.getLoginToRemove();
        listToRemove.add(loginToRemove);

        // remove from display list
        this.bean.getMsaSettingsPriceTableApproverList().removeAll(listToRemove);

        // add to remove list that will be used on save MSA Settings
        PriceTableApprover currentApprover =
                this.priceTableApproverService.findByLoginAndMsaCode(loginToRemove, this.bean.getTo().getCodigoMsa());
        if (currentApprover != null) {
            this.bean.addPriceTableApproverToRemove(currentApprover);
        }
    }

    public void validateSettingsPriceTableLogin(final FacesContext context,
                                                final UIComponent component, final Object value) {
        String login = (String) value;
        if (bean.getLastSearchUserPriceTableAutocomplete() != null) {
            for (UserProfile profile : bean.getLastSearchUserPriceTableAutocomplete()) {
                if (profile.getLogin().equalsIgnoreCase(login)) {
                    return;
                }
            }
        }

        throw new ValidatorException(Messages.getMessageError(Constants.MSM_ERROR_ADD_USER_WITH_NO_PROFILE_ON_PRICE_TABLE_EDITOR,
                (String) component.getAttributes().get("label")));
    }

    /**
     * Realiza a validacao do Recurso e da Pessoa.
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
        }
    }

    /**
     * Acao utilizada no autocomplete login. Retorna uma lista de recursos.
     */
    public List<Recurso> autoCompleteRecurso(final Object value) {
        String tipoRecurso = Constants.RESOURCE_TYPE_PE;
        return recursoService.quickSearch((String) value, tipoRecurso);
    }

    public List<UserProfile> autoCompletePriceTable(final Object value) {
        List<UserProfile> userProfiles = priceTableEditorService.autoCompletePriceTable((String) value);
        bean.setLastSearchUserPriceTableAutocomplete(userProfiles);
        return userProfiles;
    }

    private Boolean validateITSupport() {
        GrantedAuthority[] loggedUserAuthorities = LoginUtil
                .getLoggedUserAuthorities();
        boolean isItSupport = false;
        for (GrantedAuthority authority : loggedUserAuthorities) {

            if (authority.getAuthority().equals("BUS.MSA:ED") || authority.getAuthority().equals("BUS.MSA:CR")
                    || authority.getAuthority().equals("BUS.MSA.BACKOFFICE:ED")) {
                return false;
            }
            if (authority.getAuthority().equals("BUS.MSA:VW")) {
                isItSupport = true;
            }
        }
        return isItSupport;
    }

    private Boolean isPriceTableEditorLogin(Msa msa) {
        boolean isPriceTableEditorLogin = false;
        List<PriceTableEditor> priceTableEditorList = priceTableEditorService.findByMsaCode(msa.getCodigoMsa());
        List<String> priceTableEditorNameList = new ArrayList<String>();

        for (PriceTableEditor priceTableEditor : priceTableEditorList) {
            priceTableEditorNameList.add(priceTableEditor.getLogin());
        }

        for (String editorName : priceTableEditorNameList) {
            if (priceTableEditorNameList.contains(LoginUtil.getLoggedUsername())) {
                isPriceTableEditorLogin = true;
            }
        }

        return isPriceTableEditorLogin;
    }


    private Boolean isPriceTableApproverLogin(Msa msa) {
        boolean isPriceTableApproverLogin = false;
        List<PriceTableApprover> priceTableApproverList = priceTableApproverService.findByMsaCode(msa.getCodigoMsa());
        List<String> priceTableApproverNameList = new ArrayList<String>();

        for (PriceTableApprover priceTableApprover : priceTableApproverList) {
            priceTableApproverNameList.add(priceTableApprover.getLogin());
        }

        for (String approverName : priceTableApproverNameList) {
            if (priceTableApproverNameList.contains(LoginUtil.getLoggedUsername())) {
                isPriceTableApproverLogin = true;
            }
        }

        return isPriceTableApproverLogin;
    }

    private Boolean validateAdminMsa() {
        GrantedAuthority[] loggedUserAuthorities = LoginUtil
                .getLoggedUserAuthorities();
        boolean isAdminMsa = false;
        for (GrantedAuthority authority : loggedUserAuthorities) {
            if (authority.getAuthority().equals("BUS.MSA.SETTINGS:ED")) {
                this.setIsAdminMsa(true);
            }
        }
        return this.getIsAdminMsa();
    }

    /**
     * @return Boolean - True - if user has only Admin MSA profile.
     */
    private Boolean hasOnlyAdminMsaProfile() {
        return this.isAdminMsa && (LoginUtil.getLoggedUserAuthorities() != null &&
                LoginUtil.getLoggedUserAuthorities().length == 1);
    }

    /**
     * Prepare Price Table Status List to Select.
     * @param tp - Price Table with Current Status
     */
    private void loadPriceTableStatusList(TabelaPreco tp) {
        if(tp != null && tp.getPriceTableStatusFlowPms() != null){
            PriceTableMemberType member = msaContratoBean.getIsPriceTableEditorLogin() ? PriceTableMemberType.EDITOR : PriceTableMemberType.APPROVER;
            List<PriceTableStatus> statusList = handler.findPriceTableFlow(tp.getPriceTableStatusFlowPms().getCode()).priceTableStatusList(member);
            tabelaPrecoBean.setPriceTableStatusSelect(new PriceTableStatusSelect(statusList));

            tabelaPrecoBean.getPriceTableStatusSelect().select(tp.getPriceTableStatusFlowPms().getCode());
            if(!isStatusInList(statusList, tp.getPriceTableStatusFlowPms()))
                tabelaPrecoBean.getPriceTableStatusSelect().select(statusList.get(0).getCode());

            bean.setPriceTableStatusSelectName("");
        }
    }

    /**
     * @param list - Price Table Status List
     * @param status - Status to verify
     * @return True - If Status passed is in list.
     */
    private boolean isStatusInList(List<PriceTableStatus> list, PriceTableStatus status){

        if(list != null && !list.isEmpty()){
            for (PriceTableStatus st : list) {
                if(st.getCode().equals(status.getCode()))
                    return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * Metodo de Evento para o combo do priceTableStatusSelect.
     *
     * @param e - evento
     */
    public void priceTableStatusSelectListener(final ValueChangeEvent e) {
        String priceTableStatusSelectDescription = (String) e.getNewValue();

        if (priceTableStatusSelectDescription != null) {
            bean.setPriceTableStatusSelectName(priceTableStatusSelectDescription);
        }
    }

    /**
     * @return Retorna para o MSA manage com a Tab Price Table Ativada
     */
    public String backToPriceTable() {
        bean.setSelectedTab("tabPrice");
        return OUTCOME_MSA_MANAGE;
    }

    /**
     * Prepara a Tab Panel caso o atributo selecedTab seja setado.
     */
    public void prepareComponentTabPanel() {

        if (bean.getSelectedTab() != null && !bean.getSelectedTab().isEmpty()) {
            prepareComponentTabPanel(bean.getSelectedTab(), "tabPanel");
            prepareTabPriceTable();
            bean.setSelectedTab("");
        }
    }

    /**
     * @param tp - Price Table to Search Revenues With Msa and Dates
     */
    private void verifyRevenuesByPriceTableDatesAndMsa(TabelaPreco tp){
        try{
            if(tp != null && !tp.getPriceTableStatusFlowPms().getAcronym().equals(Constants.DELETED) &&
                    !tp.getPriceTableStatusFlowPms().getAcronym().equals(Constants.APPROVED)) {

                List<Receita> revenues = receitaService.findRevenuesByMsaAndDates(tp.getMsa().getCodigoMsa(), tp.getDataInicioVigencia(), tp.getDataFimVigencia());
                if (revenues != null && !revenues.isEmpty())
                    Messages.showWarning("", Constants.MSG_WARN_PRICE_TABLE_VALUES_NOT_REFLECT_REVENUES);
            }
        }catch (Exception e){
            logger.error("Error trying to find revenues by Msa and Price Table's Dates");
        }
    }

    /**
     * @param tp
     */
    private void verifyLastPriceTable(TabelaPreco tp){
        TabelaPreco lastPriceTableVigente = tabelaPrecoService.findMaxStartDatebyMsaAndCurrencyApproved(tp.getMsa(), tp.getMoeda());
        if(lastPriceTableVigente != null){
            lastPriceTableVigente.setDataFimVigencia(null);
            tabelaPrecoService.updateTabelaPreco(lastPriceTableVigente);
        }
    }

    /**
     * PRICE TABLE HISTORY
     */
    public PriceTableHistoryBean getPriceTableHistoryBean() {
        return priceTableHistoryBean;
    }
    public void setPriceTableHistoryBean(PriceTableHistoryBean priceTableHistoryBean) {
        this.priceTableHistoryBean = priceTableHistoryBean;
    }

    /**
     * Prepara a aba de History da TabelaPreco.
     */
    public void prepareTabPriceTableHistory() {
        Long code = (priceTableHistoryBean.getPriceTableSelect() != null) ? priceTableHistoryBean.getPriceTableSelect().value() : null;
        priceTableHistoryBean.setPriceTableSelect(new TabelaPrecoSelect(tabelaPrecoService.findTabelaPrecoByMsa(bean.getTo())));
        if(code != null)
            priceTableHistoryBean.getPriceTableSelect().select(code);
    }

    /**
     * Prepara a aba de History da TabelaPreco.
     */
    public void filterPriceTableHistory() {

        try {
            Long code = priceTableHistoryBean.getPriceTableSelect().value();
            priceTableHistoryBean.getPriceTableSelect().select(code);

            priceTableHistoryBean.setResultList(flowHistory.findHistoryPriceTable(priceTableHistoryBean.getPriceTableSelect().entity()));
            if(priceTableHistoryBean.getResultList() == null || priceTableHistoryBean.getResultList().isEmpty())
                Messages.showWarning("", Constants.DEFAULT_MSG_WARN_NO_PRICE_TABLE_HISTORY_FOUND);

            priceTableHistoryBean.setCurrentPageId(0);
        }catch (Exception e){
            logger.error("Error trying to search price table histories. "+ e.getMessage());
        }
    }

    /**
     * Prepara a tela de histórico dos items da price table.
     */
    public String prepareItemPriceTableHistory() {

        try {
            if(priceTableHistoryBean.getHistory() != null && priceTableHistoryBean.getHistory().getId() != null){
                priceTableHistoryBean.setPriceTable(tabelaPrecoService.findTabelaPrecoById(priceTableHistoryBean.getPriceTableSelect().value()));
                priceTableHistoryBean.setItems(flowHistory.findHistoryPriceTableById(priceTableHistoryBean.getHistory().getId(), priceTableHistoryBean.getPriceTable()));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return OUTCOME_ITEM_PRICE_TABLE_HISTORY;
    }

    /**
     * @return
     */
    public String backToPriceTableHistory() {
        backToPriceTable();
        tabelaPrecoBean.setSelectedTab("tabPriceTableHistoryForm");
        return OUTCOME_MSA_MANAGE;
    }

    /**
     * Prepara a Tab Panel caso o atributo selecedTab seja setado.
     */
    public void prepareComponentTabPanelHistory() {

        if (tabelaPrecoBean.getSelectedTab() != null && !tabelaPrecoBean.getSelectedTab().isEmpty()) {
            prepareComponentTabPanel(tabelaPrecoBean.getSelectedTab(), "tabPriceTablePanel");
            tabelaPrecoBean.setSelectedTab("");
        }
    }

    /**
     * @param selectedTab
     * @param tabPanel
     */
    private void prepareComponentTabPanel(String selectedTab, String tabPanel) {

        if (selectedTab != null && !selectedTab.isEmpty()) {
            UIComponent component = FacesUtil.getComponentById(FacesContext.getCurrentInstance().getViewRoot(), tabPanel);
            if (component != null)
                component.getAttributes().put("value", selectedTab);
        }
    }

    private void setStatusToDraft(TabelaPreco tp) throws BusinessException {
        PriceTableStatus priceTableStatus = priceTableStatusService.findPriceTableStatusById(STATUS_DRAFT);
        tp.setPriceTableStatusFlowPms(priceTableStatus);
        tabelaPrecoBean.setTo(tp);
        updateListItemTabPrecoRow();
    }


    /**
     * Prepara a tela de load do arquivo.
     *
     * @return retorna a página de upload.
     */
    public String prepareLegalDocUpload() {
        bean.reset();
        bean.setErrorEntryList(new ArrayList<>());
        return OUTCOME_MSA_UPLOAD_LEGAL_DOC;
    }


    /**
     * Listener para o upload de arquivo.
     *
     * @param event evento do upload
     * @throws Exception lança uma exception
     */
    public void uploadLegalDocListener(final UploadEvent event) throws Exception {

        UploadItem item = event.getUploadItem();
        bean.setNomeArquivoUpload(item.getFileName());
        Map<String, List<String>> map = msaService.uploadLegalDoc(item);


        if(map != null){
            bean.setMapUploadError(map);
            bean.setErrorEntryList(new ArrayList<>(bean.getMapUploadError().entrySet()));
        }else{
            bean.setErrorEntryList(null);
        }
    }



}
