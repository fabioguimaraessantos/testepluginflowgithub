package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.control.jsf.components.ISelect;
import com.ciandt.pms.model.AnexoTabelaPreco;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.HistoricoPercentualIntercomp;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaChargeMethod;
import com.ciandt.pms.model.PaymentConditionDealFiscal;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PriceTableApprover;
import com.ciandt.pms.model.PriceTableEditor;
import com.ciandt.pms.model.vo.MsaSaldoMoedaRow;
import com.ciandt.pms.model.vo.UserProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.richfaces.model.UploadItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.faces.model.SelectItem;
import java.io.Serializable;

/**
 * Define o BackingBean da entidade.
 *
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * @since 29/09/2009
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MsaBean implements Serializable {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * to do BackingBean.
     */
    private Msa to = new Msa();

    /**
     * filter do backingBean.
     */
    private Msa filter = new Msa();

    /**
     * To DealFiscal do backingBean.
     */
    private DealFiscal toDealFiscal = new DealFiscal();

    /**
     * Indicador do modo em tempo de execucao - insert.
     */
    private Boolean isCreate = Boolean.valueOf(false);

    /**
     * Indicador do modo em tempo de execucao - insert.
     */
    private Boolean indicadorReembolsavel = Boolean.valueOf(false);

    /**
     * Indicador do modo em tempo de execucao - create/update.
     */
    private Boolean isUpdate = Boolean.valueOf(false);

    private Boolean isUpdateHistorico = Boolean.FALSE;

    /**
     * Id da pagina corrente na lista de pesquisa.
     */
    private Integer currentPageId = Integer.valueOf(0);

    /**
     * Id da entidade Msa corrente selecionada na lista de pesquisa.
     */
    private Long currentMsaId = Long.valueOf(0);

    /**
     * Id da entidade DealFiscal corrente selecionada na lista de pesquisa.
     */
    private Long currentDealFiscalId = Long.valueOf(0);

    /**
     * Lista de resultados da pesquisa.
     */
    private List<Msa> resultList = new ArrayList<Msa>();

    /**
     * List para o combobox com clientes.
     */
    private List<String> clienteList = new ArrayList<String>();

    /**
     * Map para o combobox com clientes.
     */
    private Map<String, Long> clienteMap = new HashMap<String, Long>();

    /** List para o combobox com industries types. */
    private ISelect industrySelect;

    /** List para o combobox com bmdns. */
    private ISelect bmDnSelect;

    /**
     * List para o combobox com Moedas.
     */
    private List<String> moedaList = new ArrayList<String>();

    /**
     * List para o combobox com Moedas da secao de Search.
     */
    private List<String> moedaSearchList = new ArrayList<String>();

    /**
     * Map para o combobox com Moedas.
     */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /**
     * Map para o combobox com Moedas da secao de Search.
     */
    private Map<String, Long> moedaSearchMap = new HashMap<String, Long>();

    /**
     * Lista para o combobox com Empresa.
     */
    private List<String> empresaList = new ArrayList<String>();

    /**
     * Lista para o combobox com Empresa.
     */
    private Map<String, Long> empresaMap = new HashMap<String, Long>();

    /**
     * Lista para o combobox com Empresa.
     */
    private List<String> empresaInterCompList = new ArrayList<String>();

    private List<String> paymentConditionList = new ArrayList<String>();

    /**
     * Lista para o combobox com Empresa.
     */
    private Map<String, Long> empresaInterCompMap = new HashMap<String, Long>();

    /**
     * Lista para o combobox com Empresa.
     */
    private List<String> empresaInterCompIntermediateList = new ArrayList<String>();

    /**
     * Lista para o combobox com Empresa.
     */
    private Map<String, Long> empresaInterCompIntermediateMap = new HashMap<String, Long>();

    /**
     * Lista de MsaSaldoMoedaRow.
     */
    private List<MsaSaldoMoedaRow> listMsaSalMoeRow = new ArrayList<MsaSaldoMoedaRow>();




    /**
     * contém o valor da sequnece do deal que sera criado.
     */
    private String dfSequence;

    /**
     * Indica se o dealFiscal possui relacionamento com outras entidades.
     */
    private Boolean hasRelationship = Boolean.valueOf(false);

    /**
     * Indica se o dealFiscal possui receita.
     */
    private Boolean hasRevenue = Boolean.valueOf(false);

    /**
     * Data do HistoryLock.
     */
    private Date historyLockDate;

    /**
     * Item upload.
     */
    private UploadItem uploadItem;

    /**
     * Anexo.
     */
    private AnexoTabelaPreco anexoTabelaPreco;

    /**
     * Flag para selecionar a aba correta.
     */
    private String selectedTab;

    /**
     * Nome da Empresa Intercompany.
     */
    private String nomeEmpresaIntercomp;

    private String paymentConditionName;

    private String priceTableStatusSelectName;


    private String notApproveReasonsDescription;

    /**
     * Nome da Empresa Intercompany.
     */
    private String nomeEmpresaIntercompIntermediate;

    private Double percentualIntercompanyIntermediate;

    private Double percentualIntercompany;

    /**
     * Propriedade do mes de inicio da vigencia.
     */
    private String mesInicioVigencia;

    /**
     * Propriedade do ano de inicio da vigencia.
     */
    private String anoInicioVigencia;

    /**
     * Lista de DealFiscal.
     */
    private List<DealFiscal> dealFiscalList = new ArrayList<DealFiscal>();

    /**
     * Flag para checkbox do filtro de lista do DealFiscal.
     */
    private Boolean flagCheckBoxDealFiscalList = Boolean.valueOf(true);

    /**
     * Flag usado para filtrar os ativos na lista de Sale Profile no MSA.
     */
    private Boolean isCheckedSaleProfileActiveOnly = Boolean.valueOf(true);

    /**
     * Flag usado para filtrar os ativos na lista de TabelaPreco no MSA.
     */
    private Boolean isCheckedPriceTableActiveOnly = Boolean.valueOf(true);

    /**
     * Lista para o combobox com as Pessoas.
     */
    private List<String> pessoaList = new ArrayList<String>();

    /**
     * Lista para o combobox com as Pessoas.
     */
    private Map<String, Long> pessoaMap = new HashMap<String, Long>();

    /**
     * Tipo de Fatura selecionada
     */
    private String tipoFaturaSelecionada = "";

    /**
     * Lista de Faturas para o Combobox da tela de MsaAdd.xhtml.
     */
    private List<String> listaTipoFaturaCombobox = new ArrayList<String>();

    /**
     * Lista de Faturas para o Combobox da tela de MsaAdd.xhtml.
     */
    private Map<String, String> mapTipoFatura = new HashMap<String, String>();

    /**
     * Map de tipo de servico.
     */
    private Map<String, Long> tipoServicoMap = new HashMap<String, Long>();

    /**
     * Lista de tipo de servico.
     */
    private List<String> tipoServicoList = new ArrayList<String>();

    /**
     * Tipo de servico selecionado.
     */
    private String tipoServicoSelected = "";

    /**
     * Lista com todos os tipos de serviço.
     */
    private List<SelectItem> tipoServicoListPickList = new ArrayList<SelectItem>();

    private List<HistoricoPercentualIntercomp> historicosPercentuaisIntercomp = new ArrayList<HistoricoPercentualIntercomp>();

    private List<PaymentConditionDealFiscal> paymentConditionsDealFiscal = new ArrayList<PaymentConditionDealFiscal>();

    /**
     * Lista com todos os tipos de serviço selecionados (incluidos).
     */
    private String[] tipoServicoListSelected;

    private Long currentHistoricoId;

    /** Nome Arquivo Update em tela*/
    private String nomeArquivoUpload = "";

    private List<Map.Entry<String, List<String>>> errorEntryList;

    /** Lista para os erros no arquivo de upload. */
    private Map<String, List<String>> mapUploadError = new HashMap<>();



    /**
     * Flag para edição da data de tabelaPreco.
     */
    private Boolean flagEditData = Boolean.valueOf(false);

    /**
     * Indicador se contrato utilizara calculo por dias uteis ao inves de 168 horas
     */
    private Boolean flagCalculateBusinessDay = Boolean.valueOf(false);

    /**
     * Indicador se contrato utilizara calculo arredondamento para cima de diarias ex: 17.5 dias para 18 dias
     */
    private Boolean flagRoundUp = Boolean.valueOf(false);

    private Boolean enableCheckRoundUp = Boolean.FALSE;

    private Boolean visibleByPermission = Boolean.FALSE;

    private List<SelectItem> msaChargeMethods = new ArrayList<SelectItem>();

    private String selectedChargeMethod = new String();

    private Map<Long, MsaChargeMethod> msaChargeMethodMap = new HashMap<Long, MsaChargeMethod>();

    private List<String> msaSettingsLoginsList = new ArrayList<String>();

    private List<String> msaSettingsPriceTableEditorList = new ArrayList<String>();

    private List<String> msaSettingsPriceTableApproverList = new ArrayList<String>();

    private List<PriceTableEditor> allPriceTableEditorToRemove = new ArrayList<PriceTableEditor>();

    private List<PriceTableApprover> allPriceTableApproverToRemove = new ArrayList<PriceTableApprover>();

    private List<UserProfile> lastSearchUserPriceTableAutocomplete = new ArrayList<UserProfile>();

    private String newLoginAdded = "";

    private String newPriceTableEditorLoginAdded = "";

    private String newPriceTableApproverLoginAdded = "";

    private String loginToRemove = "";

    public Boolean getFlagCalculateBusinessDay() {
        return this.flagCalculateBusinessDay;
    }

    public void setFlagCalculateBusinessDay(Boolean flagCalculateBusinessDay) {
        this.flagCalculateBusinessDay = flagCalculateBusinessDay;
    }

    public Boolean getFlagRoundUp() {
        return this.flagRoundUp;
    }

    public void setFlagRoundUp(Boolean flagRoundUp) {
        this.flagRoundUp = flagRoundUp;
    }

    public Boolean getVisibleByPermission() {
        return this.visibleByPermission;
    }

    public void setVisibleByPermission(Boolean visibleByPermission) {
        this.visibleByPermission = visibleByPermission;
    }

    public Boolean getEnableCheckRoundUp() {
        return this.enableCheckRoundUp;
    }

    public void setEnableCheckRoundUp(Boolean enableCheckRoundUp) {
        this.enableCheckRoundUp = enableCheckRoundUp;
    }

    public void enableCheck() {
        if (this.getFlagCalculateBusinessDay()) {
            setEnableCheckRoundUp(Boolean.FALSE);
        } else {
            setEnableCheckRoundUp(Boolean.TRUE);
        }
    }


    public String getNewLoginAdded() {
        return newLoginAdded;
    }

    public void setNewLoginAdded(String newLoginAdded) {
        this.newLoginAdded = newLoginAdded;
    }

    public String getNewPriceTableEditorLoginAdded() {
        return newPriceTableEditorLoginAdded;
    }

    public void setNewPriceTableEditorLoginAdded(String newPriceTableEditorLoginAdded) {
        this.newPriceTableEditorLoginAdded = newPriceTableEditorLoginAdded;
    }

    public String getNewPriceTableApproverLoginAdded() {
        return newPriceTableApproverLoginAdded;
    }

    public void setNewPriceTableApproverLoginAdded(String newPriceTableApproverLoginAdded) {
        this.newPriceTableApproverLoginAdded = newPriceTableApproverLoginAdded;
    }

    public List<String> getMsaSettingsLoginsList() {
        return msaSettingsLoginsList;
    }

    public void setMsaSettingsLoginsList(List<String> msaSettingsLoginsList) {
        this.msaSettingsLoginsList = msaSettingsLoginsList;
    }

    /**
     * @return the isCreate
     */
    public Boolean getIsCreate() {
        return isCreate;
    }

    /**
     * @param isCreate the isCreate to set
     */
    public void setIsCreate(final Boolean isCreate) {
        this.isCreate = isCreate;
    }

    /**
     * @return the isCreate
     */
    public Boolean getIndicadorReembolsavel() {
        return indicadorReembolsavel;
    }

    /**
     * @param indicadorReembolsavel the isCreate to set
     */
    public void setIndicadorReembolsavel(final Boolean indicadorReembolsavel) {
        this.indicadorReembolsavel = indicadorReembolsavel;
    }

    /**
     * @return the canEdit
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isUpdate the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdateHistorico() {
        return isUpdateHistorico;
    }

    /**
     * @param isUpdateHistorico the isUpdateHistorico to set
     */
    public void setIsUpdateHistorico(final Boolean isUpdateHistorico) {
        this.isUpdateHistorico = isUpdateHistorico;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param currentPageId the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the currentMsaId
     */
    public Long getCurrentMsaId() {
        return currentMsaId;
    }

    /**
     * @param currentMsaId the currentMsaId to set
     */
    public void setCurrentMsaId(final Long currentMsaId) {
        this.currentMsaId = currentMsaId;
    }

    /**
     * @return the currentDealFiscalId
     */
    public Long getCurrentDealFiscalId() {
        return currentDealFiscalId;
    }

    /**
     * @param currentDealFiscalId the currentDealFiscalId to set
     */
    public void setCurrentDealFiscalId(final Long currentDealFiscalId) {
        this.currentDealFiscalId = currentDealFiscalId;
    }

    /**
     * @return the currentHistoricoId
     */
    public Long getCurrentHistoricoId() {
        return currentHistoricoId;
    }

    /**
     * @param currentHistoricoId the currentDealFiscalId to set
     */
    public void setCurrentHistoricoId(final Long currentHistoricoId) {
        this.currentHistoricoId = currentHistoricoId;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        resetToDealFiscal();
        this.listMsaSalMoeRow = new ArrayList<MsaSaldoMoedaRow>();
        this.historicosPercentuaisIntercomp = new ArrayList<HistoricoPercentualIntercomp>();
        this.paymentConditionsDealFiscal = new ArrayList<PaymentConditionDealFiscal>();
        this.setListaTipoFaturaCombobox(new ArrayList<String>());
        this.mapTipoFatura = new HashMap<String, String>();
        this.tipoFaturaSelecionada = "";
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new Msa();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.setFilter(new Msa());
    }

    /**
     * Reseta a lista de Resultados.
     */
    public void resetResultList() {
        this.setResultList(new ArrayList<Msa>());
    }

    /**
     * Reseta os TOs Auxiliares.
     */
    public void resetToDealFiscal() {
        toDealFiscal = new DealFiscal();
        toDealFiscal.setMsa(to);
        toDealFiscal.setMoeda(new Moeda());
        toDealFiscal.setCliente(new Cliente());
        toDealFiscal.setEmpresa(new Empresa());
        toDealFiscal.setEmpresaIntercomp(null);
        toDealFiscal.setIndicadorIntercompany(Boolean.valueOf(false));
        toDealFiscal.setHistoricoPercentualIntercomps(new HashSet<HistoricoPercentualIntercomp>(0));

        hasRelationship = Boolean.valueOf(false);
        hasRevenue = Boolean.FALSE;
        dfSequence = null;
        nomeEmpresaIntercomp = null;
        paymentConditionName = null;
        nomeEmpresaIntercompIntermediate = null;
        historicosPercentuaisIntercomp = null;

        isCreate = Boolean.valueOf(false);
        isUpdate = Boolean.valueOf(false);
    }

    /**
     * @return the to
     */
    public Msa getTo() {
        // Verifica se o Cliente é nulo, se verdade instancia um novo.
        if (to != null && to.getCliente() == null) {
            to.setCliente(new Cliente());
        }

        if (to.getPessoa() == null) {
            to.setPessoa(new Pessoa());
        }
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final Msa to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public Msa getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(final Msa filter) {
        // Verifica se o Cliente é nulo, se verdade instancia um novo.
        if (filter != null && filter.getCliente() == null) {
            filter.setCliente(new Cliente());
        }

        // Verifica se o BmDn é nulo, se verdade instancia um novo.
        if (filter != null && filter.getBmDn() == null) {
            filter.setBmDn(0L);
        }

        this.filter = filter;
    }

    /**
     * Pega a lista de clientes do combo.
     *
     * @return a lista
     */
    public List<String> getClienteList() {
        return clienteList;
    }

    /**
     * Seta a lista e pratica para o combo.
     *
     * @param clienteList the clienteList to set
     */
    public void setClienteList(final List<String> clienteList) {
        this.clienteList = clienteList;
    }

    /**
     * Pega o map do combo.
     *
     * @return o map
     */
    public Map<String, Long> getClienteMap() {
        return clienteMap;
    }

    /**
     * Seta o map do combo.
     *
     * @param clienteMap the clienteMap to set
     */
    public void setClienteMap(final Map<String, Long> clienteMap) {
        this.clienteMap = clienteMap;
    }

    /**
     * @return the resultList
     */
    public List<Msa> getResultList() {
        return resultList;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(final List<Msa> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the listMsaSalMoeRow
     */
    public List<MsaSaldoMoedaRow> getListMsaSalMoeRow() {
        return listMsaSalMoeRow;
    }

    /**
     * @param listMsaSalMoeRow the listMsaSalMoeRow to set
     */
    public void setListMsaSalMoeRow(
            final List<MsaSaldoMoedaRow> listMsaSalMoeRow) {
        this.listMsaSalMoeRow = listMsaSalMoeRow;
    }

    /**
     * @return the historyLockDate
     */
    public Date getHistoryLockDate() {
        return historyLockDate;
    }

    /**
     * @param historyLockDate the historyLockDate to set
     */
    public void setHistoryLockDate(final Date historyLockDate) {
        this.historyLockDate = historyLockDate;
    }

    /**
     * @return the uploadItem
     */
    public UploadItem getUploadItem() {
        return uploadItem;
    }

    /**
     * @param uploadItem the uploadItem to set
     */
    public void setUploadItem(final UploadItem uploadItem) {
        this.uploadItem = uploadItem;
    }

    /**
     * @return the anexoTabelaPreco
     */
    public AnexoTabelaPreco getAnexoTabelaPreco() {
        return anexoTabelaPreco;
    }

    /**
     * @param anexoTabelaPreco the anexoTabelaPreco to set
     */
    public void setAnexoTabelaPreco(final AnexoTabelaPreco anexoTabelaPreco) {
        this.anexoTabelaPreco = anexoTabelaPreco;
    }

    /**
     * @return the dfSequence
     */
    public String getDfSequence() {
        return dfSequence;
    }

    /**
     * @param dfSequence the dfSequence to set
     */
    public void setDfSequence(final String dfSequence) {
        this.dfSequence = dfSequence;
    }

    /**
     * @return the hasRelationship
     */
    public Boolean getHasRelationship() {
        return hasRelationship;
    }

    /**
     * @param hasRelationship the hasRelationship to set
     */
    public void setHasRelationship(final Boolean hasRelationship) {
        this.hasRelationship = hasRelationship;
    }

    /**
     * @return the hasRevenue
     */
    public Boolean getHasRevenue() {
        return hasRevenue;
    }

    /**
     * @param hasRevenue the hasRevenue to set
     */
    public void setHasRevenue(final Boolean hasRevenue) {
        this.hasRevenue = hasRevenue;
    }

    /**
     * @return the empresaInterCompList
     */
    public List<String> getEmpresaInterCompList() {
        return empresaInterCompList;
    }

    /**
     * @param empresaInterCompList the empresaInterCompList to set
     */
    public void setEmpresaInterCompList(final List<String> empresaInterCompList) {
        this.empresaInterCompList = empresaInterCompList;
    }

    public List<String> getPaymentConditionList() {
        return paymentConditionList;
    }

    public void setPaymentConditionList(List<String> paymentConditionList) {
        this.paymentConditionList = paymentConditionList;
    }

    /**
     * @return the empresaInterIntermediateCompList
     */
    public List<String> getEmpresaInterCompIntermediateList() {
        return empresaInterCompIntermediateList;
    }

    /**
     * @param empresaInterCompIntermediateList the empresaInterCompList to set
     */
    public void setEmpresaInterCompIntermediateList(final List<String> empresaInterCompIntermediateList) {
        this.empresaInterCompIntermediateList = empresaInterCompIntermediateList;
    }

    /**
     * @return the empresaInterCompMap
     */
    public Map<String, Long> getEmpresaInterCompMap() {
        return empresaInterCompMap;
    }

    /**
     * @param empresaInterCompMap the empresaInterCompMap to set
     */
    public void setEmpresaInterCompMap(
            final Map<String, Long> empresaInterCompMap) {
        this.empresaInterCompMap = empresaInterCompMap;
    }

    /**
     * @return the empresaInterCompMap
     */
    public Map<String, Long> getEmpresaInterCompIntermediateMap() {
        return empresaInterCompIntermediateMap;
    }

    /**
     * @param empresaInterCompIntermediateMap the empresaInterCompMap to set
     */
    public void setEmpresaInterCompIntermediateMap(final Map<String, Long> empresaInterCompIntermediateMap) {
        this.empresaInterCompIntermediateMap = empresaInterCompIntermediateMap;
    }

    /**
     * @return the empresaList
     */
    public List<String> getEmpresaList() {
        return empresaList;
    }

    /**
     * @param empresaList the empresaList to set
     */
    public void setEmpresaList(final List<String> empresaList) {
        this.empresaList = empresaList;
    }

    /**
     * @return the empresaMap
     */
    public Map<String, Long> getEmpresaMap() {
        return empresaMap;
    }

    /**
     * @param empresaMap the empresaMap to set
     */
    public void setEmpresaMap(final Map<String, Long> empresaMap) {
        this.empresaMap = empresaMap;
    }

    /**
     * @return the toDealFiscal
     */
    public DealFiscal getToDealFiscal() {
        return toDealFiscal;
    }

    /**
     * @param toDealFiscal the toDealFiscal to set
     */
    public void setToDealFiscal(final DealFiscal toDealFiscal) {
        this.toDealFiscal = toDealFiscal;
    }

    /**
     * @return the moedaList
     */
    public List<String> getMoedaList() {
        return moedaList;
    }

    /**
     * @param moedaList the moedaList to set
     */
    public void setMoedaList(final List<String> moedaList) {
        this.moedaList = moedaList;
    }

    public List<String> getMoedaSearchList() {
        return moedaSearchList;
    }

    public void setMoedaSearchList(final List<String> moedaSearchList) {
        this.moedaSearchList = moedaSearchList;
    }

    /**
     * @return the moedaMap
     */
    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    /**
     * @param moedaMap the moedaMap to set
     */
    public void setMoedaMap(final Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    public Map<String, Long> getMoedaSearchMap() {
        return moedaSearchMap;
    }

    /**
     * @param moedaSearchMap the moedaMap to set
     */
    public void setMoedaSearchMap(final Map<String, Long> moedaSearchMap) {
        this.moedaSearchMap = moedaSearchMap;
    }

    /**
     * @return the selectedTab
     */
    public String getSelectedTab() {
        return selectedTab;
    }

    /**
     * @param selectedTab the selectedTab to set
     */
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    /**
     * @return the nomeEmpresaIntercomp
     */
    public String getNomeEmpresaIntercomp() {
        return nomeEmpresaIntercomp;
    }

    /**
     * @param nomeEmpresaIntercomp the nomeEmpresaIntercomp to set
     */
    public void setNomeEmpresaIntercomp(final String nomeEmpresaIntercomp) {
        this.nomeEmpresaIntercomp = nomeEmpresaIntercomp;
    }

    public String getPaymentConditionName() {
        return paymentConditionName;
    }

    public void setPaymentConditionName(String paymentConditionName) {
        this.paymentConditionName = paymentConditionName;
    }

    /**
     * @return the nomeEmpresaIntercompIntermediate
     */
    public String getNomeEmpresaIntercompIntermediate() {
        return nomeEmpresaIntercompIntermediate;
    }

    /**
     * @param nomeEmpresaIntercompIntermediate the nomeEmpresaIntercompIntermediate to set
     */
    public void setNomeEmpresaIntercompIntermediate(final String nomeEmpresaIntercompIntermediate) {
        this.nomeEmpresaIntercompIntermediate = nomeEmpresaIntercompIntermediate;
    }

    /**
     * @return the dealFiscalList
     */
    public List<DealFiscal> getDealFiscalList() {
        return dealFiscalList;
    }

    /**
     * @param dealFiscalList the dealFiscalList to set
     */
    public void setDealFiscalList(final List<DealFiscal> dealFiscalList) {
        this.dealFiscalList = dealFiscalList;
    }

    /**
     * @return the flagCheckBoxDealFiscalList
     */
    public Boolean getFlagCheckBoxDealFiscalList() {
        return flagCheckBoxDealFiscalList;
    }

    /**
     * @param flagCheckBoxDealFiscalList the flagCheckBoxDealFiscalList to set
     */
    public void setFlagCheckBoxDealFiscalList(
            final Boolean flagCheckBoxDealFiscalList) {
        this.flagCheckBoxDealFiscalList = flagCheckBoxDealFiscalList;
    }

    /**
     * @return the isCheckedSaleProfileActiveOnly
     */
    public Boolean getIsCheckedSaleProfileActiveOnly() {
        return isCheckedSaleProfileActiveOnly;
    }

    /**
     * @param isCheckedSaleProfileActiveOnly the isCheckedSaleProfileActiveOnly to set
     */
    public void setIsCheckedSaleProfileActiveOnly(
            final Boolean isCheckedSaleProfileActiveOnly) {
        this.isCheckedSaleProfileActiveOnly = isCheckedSaleProfileActiveOnly;
    }

    /**
     * @return the isCheckedPriceTableActiveOnly
     */
    public Boolean getIsCheckedPriceTableActiveOnly() {
        return isCheckedPriceTableActiveOnly;
    }

    /**
     * @param isCheckedPriceTableActiveOnly the isCheckedPriceTableActiveOnly to set
     */
    public void setIsCheckedPriceTableActiveOnly(
            final Boolean isCheckedPriceTableActiveOnly) {
        this.isCheckedPriceTableActiveOnly = isCheckedPriceTableActiveOnly;
    }

    /**
     * @return the pessoaList
     */
    public List<String> getPessoaList() {
        return pessoaList;
    }

    /**
     * @param pessoaList the pessoaList to set
     */
    public void setPessoaList(final List<String> pessoaList) {
        this.pessoaList = pessoaList;
    }

    /**
     * @return the pessoaMap
     */
    public Map<String, Long> getPessoaMap() {
        return pessoaMap;
    }

    /**
     * @param pessoaMap the pessoaMap to set
     */
    public void setPessoaMap(final Map<String, Long> pessoaMap) {
        this.pessoaMap = pessoaMap;
    }

    /**
     * @return the tipoFaturaSelecionada
     */
    public String getTipoFaturaSelecionada() {
        return tipoFaturaSelecionada;
    }

    /**
     * @param tipoFaturaSelecionada the tipoFaturaSelecionada to set
     */
    public void setTipoFaturaSelecionada(String tipoFaturaSelecionada) {
        this.tipoFaturaSelecionada = tipoFaturaSelecionada;
    }

    /**
     * @return the listaTipoFaturaCombobox
     */
    public List<String> getListaTipoFaturaCombobox() {
        return listaTipoFaturaCombobox;
    }

    /**
     * @param listaTipoFaturaCombobox the listaTipoFaturaCombobox to set
     */
    public void setListaTipoFaturaCombobox(List<String> listaTipoFaturaCombobox) {
        this.listaTipoFaturaCombobox = listaTipoFaturaCombobox;
    }

    /**
     * @return the mapTipoFatura
     */
    public Map<String, String> getMapTipoFatura() {
        return mapTipoFatura;
    }

    /**
     * @param mapTipoFatura the mapTipoFatura to set
     */
    public void setMapTipoFatura(Map<String, String> mapTipoFatura) {
        this.mapTipoFatura = mapTipoFatura;
    }

    /**
     * @return the tipoServicoMap
     */
    public Map<String, Long> getTipoServicoMap() {
        return tipoServicoMap;
    }

    /**
     * @param tipoServicoMap the tipoServicoMap to set
     */
    public void setTipoServicoMap(Map<String, Long> tipoServicoMap) {
        this.tipoServicoMap = tipoServicoMap;
    }

    /**
     * @return the tipoServicoSelected
     */
    public String getTipoServicoSelected() {
        return tipoServicoSelected;
    }

    /**
     * @param tipoServicoSelected the tipoServicoSelected to set
     */
    public void setTipoServicoSelected(String tipoServicoSelected) {
        this.tipoServicoSelected = tipoServicoSelected;
    }

    /**
     * @return the tipoServicoListPickList
     */
    public List<SelectItem> getTipoServicoListPickList() {
        return tipoServicoListPickList;
    }

    /**
     * @param tipoServicoListPickList the tipoServicoListPickList to set
     */
    public void setTipoServicoListPickList(List<SelectItem> tipoServicoListPickList) {
        this.tipoServicoListPickList = tipoServicoListPickList;
    }

    /**
     * @return the historicosPercentuaisIntercomp
     */
    public List<HistoricoPercentualIntercomp> getHistoricosPercentuaisIntercomp() {
        return historicosPercentuaisIntercomp;
    }

    /**
     * @param historicosPercentuaisIntercomp the historicosPercentuaisIntercomp to set
     */
    public void setHistoricosPercentuaisIntercomp(List<HistoricoPercentualIntercomp> historicosPercentuaisIntercomp) {

        Collections.sort(historicosPercentuaisIntercomp, new Comparator<HistoricoPercentualIntercomp>() {
            @Override
            public int compare(HistoricoPercentualIntercomp o1, HistoricoPercentualIntercomp o2) {
                return o1.getDataInicio().compareTo(o2.getDataInicio());
            }
        });

        this.historicosPercentuaisIntercomp = historicosPercentuaisIntercomp;
    }

    public List<PaymentConditionDealFiscal> getPaymentConditionsDealFiscal() {
        return paymentConditionsDealFiscal;
    }

    public void setPaymentConditionsDealFiscal(List<PaymentConditionDealFiscal> paymentConditionsDealFiscal) {
        this.paymentConditionsDealFiscal = paymentConditionsDealFiscal;
    }

    /**
     * @return the tipoServicoListSelected
     */
    public String[] getTipoServicoListSelected() {
        return tipoServicoListSelected;
    }

    /**
     * @param tipoServicoListSelected the tipoServicoListSelected to set
     */
    public void setTipoServicoListSelected(String[] tipoServicoListSelected) {
        this.tipoServicoListSelected = tipoServicoListSelected;
    }

    /**
     * @return the tipoServicoList
     */
    public List<String> getTipoServicoList() {
        return tipoServicoList;
    }

    /**
     * @param tipoServicoList the tipoServicoList to set
     */
    public void setTipoServicoList(List<String> tipoServicoList) {
        this.tipoServicoList = tipoServicoList;
    }

    public Double getPercentualIntercompanyIntermediate() {
        return this.percentualIntercompanyIntermediate;
    }

    public void setPercentualIntercompanyIntermediate(Double percentualIntercompanyIntermediate) {
        this.percentualIntercompanyIntermediate = percentualIntercompanyIntermediate;
    }

    public Double getPercentualIntercompany() {
        return this.percentualIntercompany;
    }

    public void setPercentualIntercompany(Double percentualIntercompany) {
        this.percentualIntercompany = percentualIntercompany;
    }

    /**
     * @return the mesInicioVigencia
     */
    public String getMesInicioVigencia() {
        return mesInicioVigencia;
    }

    /**
     * @param mesInicioVigencia the mesInicioVigencia to set
     */
    public void setMesInicioVigencia(final String mesInicioVigencia) {
        this.mesInicioVigencia = mesInicioVigencia;
    }

    /**
     * @return the anoInicioVigencia
     */
    public String getAnoInicioVigencia() {
        return anoInicioVigencia;
    }

    /**
     * @param anoInicioVigencia the anoInicioVigencia to set
     */
    public void setAnoInicioVigencia(final String anoInicioVigencia) {
        this.anoInicioVigencia = anoInicioVigencia;
    }

    /**
     * @return the flagEditData
     */
    public Boolean getFlagEditData() {
        return flagEditData;
    }

    /**
     * @param flagEditData the flagEditData to set
     */
    public void setFlagEditData(final Boolean flagEditData) {
        this.flagEditData = flagEditData;
    }

    public List<SelectItem> getMsaChargeMethods() {
        return msaChargeMethods;
    }

    public void setMsaChargeMethods(List<SelectItem> msaChargeMethods) {
        this.msaChargeMethods = msaChargeMethods;
    }

    public String getSelectedChargeMethod() {
        return selectedChargeMethod;
    }

    public void setSelectedChargeMethod(String selectedChargeMethod) {
        this.selectedChargeMethod = selectedChargeMethod;
    }

    public Map<Long, MsaChargeMethod> getMsaChargeMethodMap() {
        return msaChargeMethodMap;
    }

    public void setMsaChargeMethodMap(Map<Long, MsaChargeMethod> msaChargeMethodMap) {
        this.msaChargeMethodMap = msaChargeMethodMap;
    }

    public String getLoginToRemove() {
        return loginToRemove;
    }

    public void setLoginToRemove(String loginToRemove) {
        this.loginToRemove = loginToRemove;
    }

    public ISelect getIndustrySelect() {
        return industrySelect;
    }
    public void setIndustrySelect(ISelect industrySelect) {
        this.industrySelect = industrySelect;
    }
    public ISelect getBmDnSelect() {
        return bmDnSelect;
    }
    public void setBmDnSelect(ISelect bmDnSelect) {
        this.bmDnSelect = bmDnSelect;
    }

    public List<String> getMsaSettingsPriceTableEditorList() {
        return msaSettingsPriceTableEditorList;
    }

    public void setMsaSettingsPriceTableEditorList(List<String> msaSettingsPriceTableEditorList) {
        this.msaSettingsPriceTableEditorList = msaSettingsPriceTableEditorList;
    }

    public List<String> getMsaSettingsPriceTableApproverList() {
        return msaSettingsPriceTableApproverList;
    }

    public void setMsaSettingsPriceTableApproverList(List<String> msaSettingsPriceTableApproverList) {
        this.msaSettingsPriceTableApproverList = msaSettingsPriceTableApproverList;
    }

    public List<PriceTableEditor> getAllPriceTableEditorToRemove() {
        return allPriceTableEditorToRemove;
    }

    public void setAllPriceTableEditorToRemove(PriceTableEditor allPriceTableEditorToRemove) {
        this.allPriceTableEditorToRemove.add(allPriceTableEditorToRemove);
    }

    public List<PriceTableApprover> getAllPriceTableApproverToRemove() {
        return allPriceTableApproverToRemove;
    }

    public void addPriceTableApproverToRemove(PriceTableApprover approver) {
        this.allPriceTableApproverToRemove.add(approver);
    }

    public List<UserProfile> getLastSearchUserPriceTableAutocomplete() {
        return lastSearchUserPriceTableAutocomplete;
    }

    public void setLastSearchUserPriceTableAutocomplete(List<UserProfile> lastSearchUserPriceTableAutocomplete) {
        this.lastSearchUserPriceTableAutocomplete = lastSearchUserPriceTableAutocomplete;
    }


    public String getPriceTableStatusSelectName() {
        return priceTableStatusSelectName;
    }

    public void setPriceTableStatusSelectName(String priceTableStatusSelectName) {
        this.priceTableStatusSelectName = priceTableStatusSelectName;
    }


    public String getNotApproveReasonsDescription() {
        return notApproveReasonsDescription;
    }

    public void setNotApproveReasonsDescription(String notApproveReasonsDescription) {
        this.notApproveReasonsDescription = notApproveReasonsDescription;
    }

    /**
     * @return the nomeArquivoUpload
     */
    public String getNomeArquivoUpload() {
        return nomeArquivoUpload;
    }


    /**
     * @param nomeArquivoUpload
     *            the nomeArquivoUpload to set
     */
    public void setNomeArquivoUpload(String nomeArquivoUpload) {
        this.nomeArquivoUpload = nomeArquivoUpload;
    }

    /**
     * @param errorEntryList
     *            the mapUploadError to set
     */
    public void setErrorEntryList(List<Map.Entry<String, List<String>>> errorEntryList) {
        this.errorEntryList = errorEntryList;
    }


    /**
     * @return the mapUploadError
     */
    public List<Map.Entry<String, List<String>>> getErrorEntryList() {
        return errorEntryList;
    }

    /**
     * @param mapUploadError
     *            the mapUploadError to set
     */
    public void setMapUploadError(Map<String, List<String>> mapUploadError) {
        this.mapUploadError = mapUploadError;
    }


    /**
     * @return the mapUploadError
     */
    public Map<String, List<String>> getMapUploadError() {
        return mapUploadError;
    }


}
