package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.components.ISelect;
import com.ciandt.pms.control.jsf.components.impl.InvoiceMegaSelect;
import com.ciandt.pms.control.jsf.components.impl.InvoiceProjectMegaSelect;
import com.ciandt.pms.control.jsf.pojo.LoginChargebackDataPojo;
import com.ciandt.pms.control.jsf.pojo.LoginChargebackPojo;
import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ChargebackPessoa;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoPessoa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;
import com.ciandt.pms.model.vo.ChargebackCell;
import com.ciandt.pms.model.vo.ChargebackRow;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.faces.model.SelectItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


/**
 * 
 * 
 * A classe ChargebackBean proporciona as funcionalidades de backingBean para
 * para o Chargeback.
 * 
 * @since 16/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ChargebackBean implements java.io.Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /** Instancia do TO do backbean. */
    private ChargebackContratoPratica to;

    /** Instancia do TO do backbean. */
    private ChargebackPessoa toPess;

    /** Instancia do Filter do backbean. */
    private ChargebackPessoa filterPess;

    /** lista de resultados da pesquisa. */
    private List<ChargebackPessoa> resultListChbackPess;

    /** Instancia do TiRecurso. */
    private TiRecurso tiRecurso;

    /** Instancia do ContratoPratica. */
    private ContratoPratica contratoPratica;

    private GrupoCusto grupoCusto;

    /** Instancia do Pessoa. */
    private Pessoa pessoa;

    private Empresa empresa;

    private String nomeFornecedor;

    private Long codigoFornecedor;


    private Long codigoProjetoErp;

    private String nomeProduto;

    /** Map utilizado no combo do tiRecurso. */
    private Map<String, Long> tiRecursoMap = new HashMap<String, Long>();

    /** List utilizado no combo do tiRecurso. */
    private List<String> tiRecursoList = new ArrayList<String>();

    /** List utilizado no combo do tiRecurso. */
    private List<SelectItem> tiRecursoSelItemList = new ArrayList<SelectItem>();

    /** Map utilizado no combo do tiRecurso. */
    private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

    /** List utilizado no combo do tiRecurso. */
    private List<String> contratoPraticaList = new ArrayList<String>();

    /** Mes inicio selecionado. */
    private String monthBeg;

    /** Ano inicio selecionado. */
    private String yearBeg;

    /** representa o mes selecionado - tela search. */
    private String monthBegFilter;

    /** representa o ano selecionado - tela search. */
    private String yearBegFilter;

    /** Mes fim selecionado. */
    private String monthEnd;

    /** Ano fim selecionado. */
    private String yearEnd;

    /** Data inicio filtrado. */
    private Date startDate;

    /** Data fim filtrado. */
    private Date endDate;

    /** Lista de datas a serem exibidas no mapa. */
    private List<Date> dateList = new ArrayList<Date>();

    /** Lista de ChargebackRow. */
    private List<ChargebackRow> chargebackRowList = new ArrayList<ChargebackRow>();

    /** Flag Chargeback CP ou Pessoa. */
    private Boolean isChbackCP = Boolean.valueOf(true);

    /** Indicador do valor da barra de progresso. */
    private double progressPercent;

    /** Indicador do status da progress bar. */
    private Boolean isProgressFinished = Boolean.valueOf(false);

    /**
     * Indicador do flag para buscar registros com campos de valores nulos.
     */
    private Boolean isMissingBlankValues = Boolean.valueOf(false);

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    private Long currentParcelaId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista de TiRecurso selecionados. */
    private List<String> selectedTiRecursoList = new ArrayList<String>();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Indicador do modo em tempo de execucao - encerrando vigencia do chargeback. */
    private Boolean isStopCharge = Boolean.valueOf(false);

    /** Codigo ChargebackPessoa - registro que está sendo atualizado. */
    private Long codChbackPessUpdate = Long.valueOf(0);

    private String ticketAtendimento;

    // Nova tela de Chargeback por c-lob
    private Long codigoFatura;

    private String descricao;

    private String nomeTiRecurso;

    private Map<String, Long> moedaMap = new HashMap<String, Long>();
    private List<String> moedaList = new ArrayList<String>();
    private String nomeMoeda;

    private BigDecimal valorLicenca = null;
    private Integer parcelasLicenca;

    private BigDecimal valorParcela;

    private Map<String, Long> msaMap = new HashMap<String, Long>();
    private List<String> msaList = new ArrayList<String>();

    private ISelect invoiceMegaSelect;

    private ISelect invoiceProjectMegaSelect;

    private String nomeMsa;

    private String indicadorTipoLicenca;

    private String codigoProcurify;

    private Boolean isNotasVaziasOnly;

    private Boolean hasIntegratedInstallments;


    public Boolean getHasProjects() {
        return hasProjects;
    }

    public void setHasProjects(Boolean hasProjects) {
        this.hasProjects = hasProjects;
    }

    private Boolean hasProjects;


    private String clobNamesReadOnly;

    private String tipoLicencaReadOnly;

    private String clientReadOnly;

    private Integer remainingInstallments;

    private BigDecimal totalToAppropriate;

    // Tela de busca
    private String indicadorTipoAlocacao;

    private List<LicencaSwProjeto> licencasProjeto;

    private LicencaSwProjeto toLicencaSwProjeto;

    private Boolean isIntegrationOrigin;

    private Boolean showConfirmReactivateModal;

    private ChargebackPessoa toReactivate;

    private List<String> logins = new ArrayList<String>();

    private String login;

    private LicencaSwProjetoPessoa licencaSwProjetoPessoa = new LicencaSwProjetoPessoa();

    private List<LicencaSwProjetoPessoa> listLicencaSwProjetoPessoa = new ArrayList<LicencaSwProjetoPessoa>();

    private Date searchStartDate;
    private Date searchEndDate;
    private Long invoiceNumber;
    private List<Long> invoiceNumberList;
    private Long project;
    private List<String> projectList;
    private Long licenseID;
    private List<Long> licenseIDList;
    private String resourceName;
    private String branch;
    private Integer installmentNumber;
    private Date installmentDate;
    private String installmentLogins;

    /* Informações para o input de múltiplos logins no modal com upload */
    private List<LoginChargebackPojo> loginsChargeback;
    private Boolean hasErrorLoginsChargeback;
    private Long idLoginToRemove;
    private Boolean hasMultipleLogins;
    private LoginChargebackDataPojo dataLoginChargeback;
    private Boolean hasLoginsNotCommited;

    public List<String> getLogins() {
        return logins;
    }

    public void setLogins(List<String> logins) {
        this.logins = logins;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public ChargebackPessoa getToReactivate() {
        return toReactivate;
    }

    public void setToReactivate(ChargebackPessoa toReactivate) {
        this.toReactivate = toReactivate;
    }

    public LicencaSwProjetoPessoa getLicencaSwProjetoPessoa() {
        return licencaSwProjetoPessoa;
    }

    public void setLicencaSwProjetoPessoa(LicencaSwProjetoPessoa licencaSwProjetoPessoa) {
        this.licencaSwProjetoPessoa = licencaSwProjetoPessoa;
    }

    public List<LicencaSwProjetoPessoa> getListLicencaSwProjetoPessoa() {
        return listLicencaSwProjetoPessoa;
    }

    public void setListLicencaSwProjetoPessoa(List<LicencaSwProjetoPessoa> listLicencaSwProjetoPessoa) {
        this.listLicencaSwProjetoPessoa = listLicencaSwProjetoPessoa;
    }

    public Boolean getShowConfirmReactivateModal() {
        return showConfirmReactivateModal;
    }

    public void setShowConfirmReactivateModal(Boolean showConfirmReactivateModal) {
        this.showConfirmReactivateModal = showConfirmReactivateModal;
    }

    public Long getCurrentParcelaId() {
        return currentParcelaId;
    }

    public void setCurrentParcelaId(Long currentParcelaId) {
        this.currentParcelaId = currentParcelaId;
    }

    public Integer getRemainingInstallments() {
        return remainingInstallments;
    }

    public void setRemainingInstallments(Integer remainingInstallments) {
        this.remainingInstallments = remainingInstallments;
    }

    public BigDecimal getTotalToAppropriate() {
        return totalToAppropriate;
    }

    public void setTotalToAppropriate(BigDecimal totalToAppropriate) {
        this.totalToAppropriate = totalToAppropriate;
    }

    public String getTicketAtendimento() {return this.ticketAtendimento;}

    public  void setTicketAtendimento (final String ticketAtendimento){ this.ticketAtendimento = ticketAtendimento;}
    /**
     * @return the codChbackPessUpdate
     */
    public Long getCodChbackPessUpdate() {
        return codChbackPessUpdate;
    }

    /**
     * @param codChbackPessUpdate
     *            the codChbackPessUpdate to set
     */
    public void setCodChbackPessUpdate(final Long codChbackPessUpdate) {
        this.codChbackPessUpdate = codChbackPessUpdate;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isUpdate
     *            the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }


    /**
     * @return the isStopCharge
     */
    public Boolean getIsStopCharge() {
        return isStopCharge;
    }

    /**
     * @param isStopCharge
     *            the isStopCharge to set
     */
    public void setIsStopCharge(final Boolean isStopCharge) {
        this.isStopCharge = isStopCharge;
    }

    /**
     * @return the tiRecursoSelItemList
     */
    public List<SelectItem> getTiRecursoSelItemList() {
        return tiRecursoSelItemList;
    }

    /**
     * @param tiRecursoSelItemList
     *            the tiRecursoSelItemList to set
     */
    public void setTiRecursoSelItemList(
            final List<SelectItem> tiRecursoSelItemList) {
        this.tiRecursoSelItemList = tiRecursoSelItemList;
    }

    /**
     * @return the selectedTiRecursoList
     */
    public List<String> getSelectedTiRecursoList() {
        return selectedTiRecursoList;
    }

    /**
     * @param selectedTiRecursoList
     *            the selectedTiRecursoList to set
     */
    public void setSelectedTiRecursoList(
            final List<String> selectedTiRecursoList) {
        this.selectedTiRecursoList = selectedTiRecursoList;
    }

    /**
     * @return the to
     */
    public ChargebackContratoPratica getTo() {
        if (to == null) {
            to = new ChargebackContratoPratica();
        }
        if (to.getTiRecurso() == null) {
            to.setTiRecurso(new TiRecurso());
        }
        if (to.getContratoPratica() == null) {
            to.setContratoPratica(new ContratoPratica());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ChargebackContratoPratica to) {
        this.to = to;
    }

    /**
     * @return the toPess
     */
    public ChargebackPessoa getToPess() {
        if (toPess == null) {
            toPess = new ChargebackPessoa();
        }
        if (toPess.getTiRecurso() == null) {
            toPess.setTiRecurso(new TiRecurso());
        }
        if (toPess.getPessoa() == null) {
            toPess.setPessoa(new Pessoa());
        }

        return toPess;
    }

    /**
     * @param toPess
     *            the toPess to set
     */
    public void setToPess(final ChargebackPessoa toPess) {
        this.toPess = toPess;
    }

    /**
     * @return the filterPess
     */
    public ChargebackPessoa getFilterPess() {
        if (filterPess == null) {
            filterPess = new ChargebackPessoa();
        }
        if (filterPess.getPessoa() == null) {
            filterPess.setPessoa(new Pessoa());
        }
        if (filterPess.getTiRecurso() == null) {
            filterPess.setTiRecurso(new TiRecurso());
        }

        return filterPess;
    }

    /**
     * @param filterPess
     *            the filterPess to set
     */
    public void setFilterPess(final ChargebackPessoa filterPess) {
        this.filterPess = filterPess;
    }

    /**
     * @return the resultListChbackPess
     */
    public List<ChargebackPessoa> getResultListChbackPess() {
        return resultListChbackPess;
    }

    /**
     * @param resultListChbackPess
     *            the resultListChbackPess to set
     */
    public void setResultListChbackPess(
            final List<ChargebackPessoa> resultListChbackPess) {
        this.resultListChbackPess = resultListChbackPess;
    }

    /**
     * @return the monthBeg
     */
    public String getMonthBeg() {
        return monthBeg;
    }

    /**
     * @param monthBeg
     *            the monthBeg to set
     */
    public void setMonthBeg(final String monthBeg) {
        this.monthBeg = monthBeg;
    }

    /**
     * @return the yearBeg
     */
    public String getYearBeg() {
        return yearBeg;
    }

    /**
     * @param yearBeg
     *            the yearBeg to set
     */
    public void setYearBeg(final String yearBeg) {
        this.yearBeg = yearBeg;
    }

    /**
     * @return the monthBegFilter
     */
    public String getMonthBegFilter() {
        return monthBegFilter;
    }

    /**
     * @param monthBegFilter
     *            the monthBegFilter to set
     */
    public void setMonthBegFilter(final String monthBegFilter) {
        this.monthBegFilter = monthBegFilter;
    }

    /**
     * @return the yearBegFilter
     */
    public String getYearBegFilter() {
        return yearBegFilter;
    }

    /**
     * @param yearBegFilter
     *            the yearBegFilter to set
     */
    public void setYearBegFilter(final String yearBegFilter) {
        this.yearBegFilter = yearBegFilter;
    }

    /**
     * @return the monthEnd
     */
    public String getMonthEnd() {
        return monthEnd;
    }

    /**
     * @param monthEnd
     *            the monthEnd to set
     */
    public void setMonthEnd(final String monthEnd) {
        this.monthEnd = monthEnd;
    }

    /**
     * @return the yearEnd
     */
    public String getYearEnd() {
        return yearEnd;
    }

    /**
     * @param yearEnd
     *            the yearEnd to set
     */
    public void setYearEnd(final String yearEnd) {
        this.yearEnd = yearEnd;
    }

    /**
     * @param chargebackRowList
     *            the chargebackRowList to set
     */
    public void setChargebackRowList(final List<ChargebackRow> chargebackRowList) {
        this.chargebackRowList = chargebackRowList;
    }

    /**
     * @return the chargebackRowList
     */
    public List<ChargebackRow> getChargebackRowList() {
        return chargebackRowList;
    }

    /**
     * @param dateList
     *            the dateList to set
     */
    public void setDateList(final List<Date> dateList) {
        this.dateList = dateList;
    }

    /**
     * @return the dateList
     */
    public List<Date> getDateList() {
        return dateList;
    }

    /**
     * Retorna o número de colunas do Mapa.
     * 
     * @return the numColumn
     */
    public Integer getNumColumn() {
        if (dateList != null) {
            return dateList.size() + 2;
        }

        return Integer.valueOf(0);
    }

    /**
     * @param tiRecursoMap
     *            the tiRecursoMap to set
     */
    public void setTiRecursoMap(final Map<String, Long> tiRecursoMap) {
        this.tiRecursoMap = tiRecursoMap;
    }

    /**
     * @return the tiRecursoMap
     */
    public Map<String, Long> getTiRecursoMap() {
        return tiRecursoMap;
    }

    /**
     * @param tiRecursoList
     *            the tiRecursoList to set
     */
    public void setTiRecursoList(final List<String> tiRecursoList) {
        this.tiRecursoList = tiRecursoList;
    }

    /**
     * @return the tiRecursoList
     */
    public List<String> getTiRecursoList() {
        return tiRecursoList;
    }

    /**
     * @param contratoPraticaMap
     *            the contratoPraticaMap to set
     */
    public void setContratoPraticaMap(final Map<String, Long> contratoPraticaMap) {
        this.contratoPraticaMap = contratoPraticaMap;
    }

    /**
     * @return the contratoPraticaMap
     */
    public Map<String, Long> getContratoPraticaMap() {
        return contratoPraticaMap;
    }

    /**
     * @param contratoPraticaList
     *            the contratoPraticaList to set
     */
    public void setContratoPraticaList(final List<String> contratoPraticaList) {
        this.contratoPraticaList = contratoPraticaList;
    }

    /**
     * @return the contratoPraticaList
     */
    public List<String> getContratoPraticaList() {
        return contratoPraticaList;
    }

    /**
     * @param tiRecurso
     *            the tiRecurso to set
     */
    public void setTiRecurso(final TiRecurso tiRecurso) {
        this.tiRecurso = tiRecurso;
    }

    /**
     * @return the tiRecurso
     */
    public TiRecurso getTiRecurso() {
        return tiRecurso;
    }

    /**
     * @param contratoPratica
     *            the contratoPratica to set
     */
    public void setContratoPratica(final ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    /**
     * @return the contratoPratica
     */
    public ContratoPratica getContratoPratica() {
        return contratoPratica;
    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa
     *            the pessoa to set
     */
    public void setPessoa(final Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Reseta todo o Bean.
     */
    public void reset() {
        this.resetTo();
        this.resetFilter();
        this.resetPeriod();
        this.resetPeriodFilter();
        this.resetResultListChbackPess();
        this.resetChbackRowList();
        this.resetCodChbackPessUpdate();
        this.filterPess.setIndicadorTipo(Constants.ALL);
        this.tiRecurso = new TiRecurso();
        this.tiRecursoList = new ArrayList<String>();
        this.tiRecursoMap = new HashMap<String, Long>();
        this.contratoPratica = new ContratoPratica();
        this.pessoa = new Pessoa();
        this.isChbackCP = Boolean.valueOf(true);
        this.isProgressFinished = Boolean.valueOf(false);
        this.selectedTiRecursoList = new ArrayList<String>();
        this.isMissingBlankValues = Boolean.valueOf(false);
        this.tiRecursoSelItemList = new ArrayList<SelectItem>();
        this.showConfirmReactivateModal = Boolean.valueOf(false);
        this.invoiceNumber = null;
        this.invoiceNumberList = new ArrayList<Long>();
        this.project = null;
        this.projectList = new ArrayList<String>();
        this.licenseID = null;
        this.licenseIDList = new ArrayList<Long>();
        this.resourceName = "";
        this.installmentNumber = null;
        this.installmentDate = null;
        this.installmentLogins = null;

        this.codigoFatura = null;

        this.descricao = "";

        this.moedaMap = new HashMap<String, Long>();
        this.moedaList = new ArrayList<String>();
        this.nomeMoeda = "";

        this.parcelasLicenca = null;

        this.msaMap = new HashMap<String, Long>();
        this.msaList = new ArrayList<String>();
        this.nomeMsa = "";
        this.nomeTiRecurso = "";
        this.ticketAtendimento = "";
        this.valorLicenca = BigDecimal.ZERO;
        this.indicadorTipoAlocacao = "";
        this.indicadorTipoLicenca = "";
        this.codigoProcurify = "";
        this.isNotasVaziasOnly = false;

        licencasProjeto = new ArrayList<LicencaSwProjeto>();

        this.isIntegrationOrigin = false;
        this.login = "";
        this.logins = new ArrayList<String>();

        this.nomeProduto = "";
        this.empresa = null;
        this.nomeFornecedor = "";
        this.invoiceMegaSelect = new InvoiceMegaSelect(new ArrayList<>());
        this.invoiceProjectMegaSelect = new InvoiceProjectMegaSelect(new ArrayList<>());
        this.valorParcela = BigDecimal.ZERO;

        resetLoginsChargeback();
    }

    /**
     * Reset State of Modal Multiple Logins
     */
    public void resetLoginsChargeback(){
        this.loginsChargeback = new ArrayList<LoginChargebackPojo>();
        this.hasErrorLoginsChargeback = Boolean.FALSE;
        this.idLoginToRemove = null;
        this.hasMultipleLogins = Boolean.FALSE;
        this.dataLoginChargeback = new LoginChargebackDataPojo();
        this.hasLoginsNotCommited = Boolean.FALSE;
    }

    /**
     * Reseta o TO.
     */
    public void resetTo() {
        to = new ChargebackContratoPratica();
        to.setNumeroUnidades(BigDecimal.valueOf(0.0));
        toPess = new ChargebackPessoa();
        toPess.setNumeroUnidades(BigDecimal.valueOf(0));
        toPess.setTicketAtendimento("");
        toReactivate = new ChargebackPessoa();
        toReactivate.setNumeroUnidades(BigDecimal.valueOf(0));
        toReactivate.setTicketAtendimento("");
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filterPess = new ChargebackPessoa();
    }

    /**
     * Reseta o periodo.
     */
    public void resetPeriod() {
        this.startDate = null;
        this.endDate = null;
        this.monthBeg = "";
        this.yearBeg = "";
        this.monthEnd = "";
        this.yearEnd = "";
        this.searchStartDate = null;
        this.searchEndDate = null;
    }

    /**
     * Reseta os combos de mes e ano - tela search.
     */
    public void resetPeriodFilter() {
        this.monthBegFilter = "";
        this.yearBegFilter = "";
    }

    /**
     * Reseta a lista de resultados.
     */
    public void resetChbackRowList() {
        this.chargebackRowList = new ArrayList<ChargebackRow>();
    }

    /**
     * Reseta os valores da progressBar.
     */
    public void resetBar() {
        this.setProgressPercent(0);
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultListChbackPess() {
        this.resultListChbackPess = null;
    }

    /**
     * Reseta o campo auxiliar codigoLoginUpdate que guarda o login que está
     * sendo atualizado.
     */
    public void resetCodChbackPessUpdate() {
        this.codChbackPessUpdate = Long.valueOf(0);
    }

    public void resetChbackProjectSearchFilters(){
        this.invoiceNumber = null;
        this.invoiceNumberList = new ArrayList<Long>();
        this.codigoFatura = null;
        this.invoiceMegaSelect = new InvoiceMegaSelect(new ArrayList<>());
        this.invoiceProjectMegaSelect = new InvoiceProjectMegaSelect(new ArrayList<>());
        this.project = null;
        this.licenseID = null;
        this.resourceName = "";
        this.codigoProcurify = "";
        this.selectedTiRecursoList = new ArrayList<String>();
    }

    /**
     * @return the size of chargebackRowList
     */
    public Integer getChargebackRowListSize() {
        return chargebackRowList.size();
    }

    /**
     * @return the isChbackCP
     */
    public Boolean getIsChbackCP() {
        return isChbackCP;
    }

    /**
     * @param isChbackCP
     *            the isChbackCP to set
     */
    public void setIsChbackCP(final Boolean isChbackCP) {
        this.isChbackCP = isChbackCP;
    }

    /**
     * @return the progressPercent
     */
    public double getProgressPercent() {
        return progressPercent;
    }

    /**
     * @return the valueRounded
     */
    public long getValueRounded() {
        return Math.round(progressPercent);
    }

    /**
     * @param progressPercent
     *            the progressPercent to set
     */
    public void setProgressPercent(final double progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * @return the isProgressFinished
     */
    public Boolean getIsProgressFinished() {
        return isProgressFinished;
    }

    /**
     * @param isProgressFinished
     *            the isProgressFinished to set
     */
    public void setIsProgressFinished(final Boolean isProgressFinished) {
        this.isProgressFinished = isProgressFinished;
    }

    /**
     * @return the isMissingBlankValues
     */
    public Boolean getIsMissingBlankValues() {
        return isMissingBlankValues;
    }

    /**
     * @param isMissingBlankValues
     *            the isMissingBlankValues to set
     */
    public void setIsMissingBlankValues(final Boolean isMissingBlankValues) {
        this.isMissingBlankValues = isMissingBlankValues;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param currentPageId
     *            the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the currentRowId
     */
    public Long getCurrentRowId() {
        return currentRowId;
    }

    /**
     * @param currentRowId
     *            the currentRowId to set
     */
    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }

    /**
     * Soma os valores das colunas da da tabela de Chargeback. 
     *
     * @return Lista de {@link BigDecimal}
     */
    public List<BigDecimal> getChargebackTablecolumnSum() {
	    List<BigDecimal> chargebackTablecolumnSum = new ArrayList<BigDecimal>();
	    for (ChargebackRow chargebackRow : this.chargebackRowList) {
	    	for (ListIterator<ChargebackCell> iterator = chargebackRow.getCellList().listIterator(); iterator.hasNext();) {
	    		int iterationIndex = iterator.nextIndex();
				ChargebackCell chargebackCell = (ChargebackCell) iterator.next();
	
				if (chargebackTablecolumnSum.size() > iterationIndex) {
					BigDecimal sum = chargebackTablecolumnSum.get(iterationIndex).add(chargebackCell.getChargebackCP().getNumeroUnidades());
					chargebackTablecolumnSum.set(iterationIndex, sum);
				} else {
					chargebackTablecolumnSum.add(chargebackCell.getChargebackCP().getNumeroUnidades());
				}
			}
		}

	    return chargebackTablecolumnSum;
    }

    public List<String> getMoedaList() {
        return moedaList;
    }

    public void setMoedaList(List<String> moedaList) {
        this.moedaList = moedaList;
    }

    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    public void setMoedaMap(Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setNomeTiRecurso(String nomeTiRecurso) {
        this.nomeTiRecurso = nomeTiRecurso;
    }

    public String getNomeTiRecurso() {
        return nomeTiRecurso;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getCodigoFatura() {
        return codigoFatura;
    }

    public void setCodigoFatura(Long codigoFatura) {
        this.codigoFatura = codigoFatura;
    }

    public String getNomeMoeda() {
        return nomeMoeda;
    }

    public void setNomeMoeda(String nomeMoeda) {
        this.nomeMoeda = nomeMoeda;
    }

    public BigDecimal getValorLicenca() {
        return valorLicenca;
    }

    public void setValorLicenca(BigDecimal valorLicenca) {
        this.valorLicenca = valorLicenca;
    }

    public Integer getParcelasLicenca() {
        return parcelasLicenca;
    }

    public void setParcelasLicenca(Integer parcelasLicenca) {
        this.parcelasLicenca = parcelasLicenca;
    }

    public Map<String, Long> getMsaMap() {
        return msaMap;
    }

    public void setMsaMap(Map<String, Long> msaMap) {
        this.msaMap = msaMap;
    }

    public List<String> getMsaList() {
        return msaList;
    }

    public void setMsaList(List<String> msaList) {
        this.msaList = msaList;
    }

    public String getNomeMsa() {
        return nomeMsa;
    }

    public void setNomeMsa(String nomeMsa) {
        this.nomeMsa = nomeMsa;
    }

    private Long[] contratoPraticaCodigosSelected;

    private LinkedHashMap<String, Object> contratoPraticas;

    public Long[] getContratoPraticaCodigosSelected() {
        return contratoPraticaCodigosSelected;
    }

    public List<Long> getContratoPraticaCodigosSelectedList() {
        return Arrays.asList(contratoPraticaCodigosSelected);
    }

    public void setContratoPraticaCodigosSelected(Long[] contratoPraticaCodigosSelected) {
        this.contratoPraticaCodigosSelected = contratoPraticaCodigosSelected;
    }

    public LinkedHashMap<String, Object> getContratoPraticas() {
        return contratoPraticas;
    }

    public void setContratoPraticas(LinkedHashMap<String, Object> contratoPraticas) {
        this.contratoPraticas = contratoPraticas;
    }

    public String getIndicadorTipoAlocacao() {
        return indicadorTipoAlocacao;
    }

    public Boolean getIsProjectLicence() {
        return "indicadorTipoAlocacao".equals(this.indicadorTipoAlocacao);
    }

    public void setIndicadorTipoAlocacao(String indicadorTipoAlocacao) {
        this.indicadorTipoAlocacao = indicadorTipoAlocacao;
    }

    public List<LicencaSwProjeto> getLicencasProjeto() {
        return licencasProjeto;
    }

    public void setLicencasProjeto(List<LicencaSwProjeto> licencasProjeto) {
        this.licencasProjeto = licencasProjeto;
    }

    public String getIndicadorTipoLicenca() {
        return indicadorTipoLicenca;
    }

    public void setIndicadorTipoLicenca(String indicadorTipoLicenca) {
        this.indicadorTipoLicenca = indicadorTipoLicenca;
    }

    public String getLicenceTypeLabel() {
        return this.indicadorTipoAlocacao.equals("PRODUCTION") ? "C-LOBs" : "Cost Centers";
    }

    public String getCodigoProcurify() {
        return codigoProcurify;
    }

    public void setCodigoProcurify(String codigoProcurify) {
        this.codigoProcurify = codigoProcurify;
    }

    public Boolean getIsNotasVaziasOnly() {
        return isNotasVaziasOnly;
    }

    public void setIsNotasVaziasOnly(Boolean isNotasVaziasOnly) {
        this.isNotasVaziasOnly = isNotasVaziasOnly;
    }

    public LicencaSwProjeto getToLicencaSwProjeto() {
        return toLicencaSwProjeto;
    }

    public void setToLicencaSwProjeto(LicencaSwProjeto toLicencaSwProjeto) {
        this.toLicencaSwProjeto = toLicencaSwProjeto;
    }

    public Boolean getIsIntegrationOrigin() {
        return isIntegrationOrigin;
    }

    public void setIsIntegrationOrigin(Boolean integrationOrigin) {
        isIntegrationOrigin = integrationOrigin;
    }

    public Boolean getHasIntegratedInstallments() {
        return hasIntegratedInstallments;
    }

    public void setHasIntegratedInstallments(Boolean hasIntegratedInstallments) {
        this.hasIntegratedInstallments = hasIntegratedInstallments;
    }

    public String getClobNamesReadOnly() {
        return clobNamesReadOnly;
    }

    public void setClobNamesReadOnly(String clobNamesReadOnly) {
        this.clobNamesReadOnly = clobNamesReadOnly;
    }

    public String getTipoLicencaReadOnly() {
        return tipoLicencaReadOnly;
    }

    public void setTipoLicencaReadOnly(String tipoLicencaReadOnly) {
        this.tipoLicencaReadOnly = tipoLicencaReadOnly;
    }

    public String getClientReadOnly() {
        return clientReadOnly;
    }

    public void setClientReadOnly(String clientReadOnly) {
        this.clientReadOnly = clientReadOnly;
    }

    /* Getters and Setters para Logins ChargeBack. */
    public List<LoginChargebackPojo> getLoginsChargeback() {
        return loginsChargeback;
    }
    public void setLoginsChargeback(List<LoginChargebackPojo> loginsChargeback) {
        this.loginsChargeback = loginsChargeback;
    }
    public Boolean getHasErrorLoginsChargeback() {

        return hasErrorLoginsChargeback;
    }
    public void setHasErrorLoginsChargeback(Boolean hasErrorLoginsChargeback) {
        this.hasErrorLoginsChargeback = hasErrorLoginsChargeback;
    }
    public Long getIdLoginToRemove() {
        return idLoginToRemove;
    }
    public void setIdLoginToRemove(Long idLoginToRemove) {
        this.idLoginToRemove = idLoginToRemove;
    }
    public Boolean getHasMultipleLogins() {
        return hasMultipleLogins;
    }
    public void setHasMultipleLogins(Boolean hasMultipleLogins) {
        this.hasMultipleLogins = hasMultipleLogins;
    }
    public LoginChargebackDataPojo getDataLoginChargeback() {
        return dataLoginChargeback;
    }
    public void setDataLoginChargeback(LoginChargebackDataPojo dataLoginChargeback) {
        this.dataLoginChargeback = dataLoginChargeback;
    }
    public Boolean getHasLoginsNotCommited() {
        return hasLoginsNotCommited;
    }
    public void setHasLoginsNotCommited(Boolean hasLoginsNotCommited) {
        this.hasLoginsNotCommited = hasLoginsNotCommited;
    }
    public Date getSearchStartDate() {
        return this.searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return this.searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        if (searchEndDate == null) {
            this.searchEndDate = null;
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(searchEndDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        this.searchEndDate = calendar.getTime();
    }

    public ISelect getInvoiceMegaSelect() {
        return invoiceMegaSelect;
    }

    public void setInvoiceMegaSelect(ISelect invoiceMegaSelect) {
        this.invoiceMegaSelect = invoiceMegaSelect;
    }

    public ISelect getInvoiceProjectMegaSelect() {
        return invoiceProjectMegaSelect;
    }

    public void setInvoiceProjectMegaSelect(ISelect invoiceProjectMegaSelect) {
        this.invoiceProjectMegaSelect = invoiceProjectMegaSelect;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getNomeFornecedor() {
        return nomeFornecedor;
    }

    public void setNomeFornecedor(String nomeFornecedor) {
        this.nomeFornecedor = nomeFornecedor;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public GrupoCusto getGrupoCusto() {
        return grupoCusto;
    }

    public void setGrupoCusto(GrupoCusto grupoCusto) {
        this.grupoCusto = grupoCusto;
    }

    public Long getCodigoFornecedor() {
        return codigoFornecedor;
    }

    public void setCodigoFornecedor(Long codigoFornecedor) {
        this.codigoFornecedor = codigoFornecedor;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Long getCodigoProjetoErp() {
        return codigoProjetoErp;
    }

    public void setCodigoProjetoErp(Long codigoProjetoErp) {
        this.codigoProjetoErp = codigoProjetoErp;
    }

    public Long getInvoiceNumber() {
        return invoiceNumber;
    }
    public void setInvoiceNumber(Long invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    public List<Long> getInvoiceNumberList() {
        return invoiceNumberList;
    }
    public void setInvoiceNumberList(List<Long> invoiceNumberList) {
        this.invoiceNumberList = invoiceNumberList;
    }

    public Long getProject() {
        return this.project;
    }
    public void setProject(Long project) {
        this.project = project;
    }
    public List<String> getProjectList() {
        return this.projectList;
    }
    public void setProjectList(List<String> projectList) {
        this.projectList = projectList;
    }
    public Long getLicenseID() {
        return this.licenseID;
    }
    public void setLicenseID(Long licenseID) {
        this.licenseID = licenseID;
    }
    public List<Long>  getLicenseIDList() {
        return this.licenseIDList;
    }
    public void setLicenseIDList(List<Long> licenseIDList) {
        this.licenseIDList = licenseIDList;
    }
    public String getResourceName() {
       return this.resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getBranch() {
       return this.branch;
    }

    public Integer getInstallmentNumber() {
        return this.installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public String getInstallmentLogins() {
        return this.installmentLogins;
    }

    public void setInstallmentLogins(String installmentLogins) {
        this.installmentLogins = installmentLogins;
    }

    public Date getInstallmentDate() {
        return this.installmentDate;
    }

    public void setInstallmentDate(Date installmentDate) {
        this.installmentDate = installmentDate;
    }




}