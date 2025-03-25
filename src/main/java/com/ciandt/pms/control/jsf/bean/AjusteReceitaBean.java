package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.model.TipoAjuste;


/**
 * Define o BackingBean da entidade Ajuste Receita.
 * 
 * @since 14/07/2011
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AjusteReceitaBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** To de AjusteReceita. */
    private AjusteReceita to;

    /** Mes do ajuste. */
    private Date mesAjuste;

    /** Ano do ajuste. */
    private Date anoAjuste;

    /** Ano e Mes da Receita na tela de Consulta. */
    private Date anoMesReceita;

    /** Ano e Mes do Ajuste na tela de Consulta. */
    private Date anoMesAjuste;

    /** Nome do Contrato Pratica selecionado na tela de Consulta. */
    private String nomeContratoPraticaConsulta;

    /** Nome do Contrato Pratica selecionado na tela de Add. */
    private String nomeContratoPraticaAdd;

    /** Lista para o combobox com as ContratoPratica. */
    private List<String> contratoPraticaList = new ArrayList<String>();

    /** Lista para o combobox com as ContratoPratica. */
    private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

    /** Nome do tipoAjuste selecionado no combobox. */
    private String nomeTipoAjuste;

    /** Lista para o combobox com os TipoAjuste. */
    private List<String> tipoAjusteList = new ArrayList<String>();

    /** Lista para o combobox com os TipoAjuste. */
    private Map<String, Long> tipoAjusteMap = new HashMap<String, Long>();

    /** Nome do dealFiscal selecionado no combo. */
    private String nomeDealFiscalSelected;

    /** Nome do dealFiscal selecionado no combo To. */
    private String nomeDealFiscalSelectedTo;

    /** Lista para o combobox com os dealFiscal. */
    private List<String> dealFiscalList = new ArrayList<String>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista com o resultado da pesquisa. */
    private List<AjusteReceita> resultList = new ArrayList<AjusteReceita>();

    /** Lista com o resultado da pesquisa. */
    private List<AjusteReceita> resultListAddEdit =
            new ArrayList<AjusteReceita>();

    /** Lista dos possiveis valores de meses. */
    private List<String> validityMonthList =
            Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09",
                    "10", "11", "12");

    /** Lista dos possiveis valores de anos. */
    private List<String> validityYearList = new ArrayList<String>();

    /** Flag para checar se é update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Mes ajuste. */
    private String validityMonthAdjust = "";

    /** Ano ajuste. */
    private String validityYearAdjust = "";

    /** Mes receita. */
    private String validityMonthRevenue = "";

    /** Ano receita. */
    private String validityYearRevenue = "";

    /** Mes ajuste. */
    private String validityMonthAdjustAddEdit = "";

    /** Ano ajuste. */
    private String validityYearAdjustAddEdit = "";

    /** Mes receita. */
    private String validityMonthRevenueAddEdit = "";

    /** Ano receita. */
    private String validityYearRevenueAddEdit = "";

    /** Flag para checar se é uma edição do ajuste receita. */
    private boolean showEditAjusteReceitaPanel = Boolean.valueOf(false);

    /** Flag para checar se é uma edição do ajuste receita. */
    private boolean showViewListVaziaPanel = Boolean.valueOf(false);

    /** Valor total do ajuste. */
    private BigDecimal valorTotal;

    /** Valor do balanco. */
    private BigDecimal balanco;

    /** Flag para checar se é uma edição do ajuste receita. */
    private boolean isEditAjusteReceita = Boolean.valueOf(false);

    /** Flag para mudar action de button back (View ajuste receita). */
    private boolean backFlag = Boolean.valueOf(false);

    /**
     * Flag para identificar se a visualizacao foi feita pela tela de search ou
     * nao.
     */
    private boolean isViewBySearch = Boolean.valueOf(false);

    /** Flag para setar disabled do combo To de ajuste. */
    private boolean flagComboTo = Boolean.valueOf(true);
    
    /** Flag para setar disabled do combo Deal e Type de ajuste. */
    private boolean flagComboDealAndType = Boolean.valueOf(false);
    
    /** Flag para mostrar a aba de ajuste receuta. */
    private boolean flagAbaAjusteReceita = Boolean.valueOf(true);
    
    /** ReceitaMoeda vinda da tela. */
    private ReceitaMoeda receitaMoeda;

	/**
     * @return the backFlag
     */
    public boolean getBackFlag() {
        return backFlag;
    }

    /**
     * @param backFlag
     *            the backFlag to set
     */
    public void setBackFlag(final boolean backFlag) {
        this.backFlag = backFlag;
    }

    /**
     * @return the showViewListVaziaPanel
     */
    public boolean getShowViewListVaziaPanel() {
        return showViewListVaziaPanel;
    }

    /**
     * @param showViewListVaziaPanel
     *            the showViewListVaziaPanel to set
     */
    public void setShowViewListVaziaPanel(final boolean showViewListVaziaPanel) {
        this.showViewListVaziaPanel = showViewListVaziaPanel;
    }

    /**
     * @return the showEditAjusteReceitaPanel
     */
    public boolean getShowEditAjusteReceitaPanel() {
        return showEditAjusteReceitaPanel;
    }

    /**
     * @param showEditAjusteReceitaPanel
     *            the showEditAjusteReceitaPanel to set
     */
    public void setShowEditAjusteReceitaPanel(
            final boolean showEditAjusteReceitaPanel) {
        this.showEditAjusteReceitaPanel = showEditAjusteReceitaPanel;
    }

    /**
     * @return the isEditAjusteReceita
     */
    public boolean getIsEditAjusteReceita() {
        return isEditAjusteReceita;
    }

    /**
     * @param isEditAjusteReceita
     *            the isEditAjusteReceita to set
     */
    public void setIsEditAjusteReceita(final boolean isEditAjusteReceita) {
        this.isEditAjusteReceita = isEditAjusteReceita;
    }

    /**
     * @return the nomeDealFiscalSelected
     */
    public String getNomeDealFiscalSelected() {
        return nomeDealFiscalSelected;
    }

    /**
     * @param nomeDealFiscalSelected
     *            the nomeDealFiscalSelected to set
     */
    public void setNomeDealFiscalSelected(final String nomeDealFiscalSelected) {
        this.nomeDealFiscalSelected = nomeDealFiscalSelected;
    }

    /**
     * @return the balanco
     */
    public BigDecimal getBalanco() {
        return balanco;
    }

    /**
     * @param balanco
     *            the balanco to set
     */
    public void setBalanco(final BigDecimal balanco) {
        this.balanco = balanco;
    }

    /**
     * @return the valorTotal
     */
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal
     *            the valorTotal to set
     */
    public void setValorTotal(final BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the dealFiscalList
     */
    public List<String> getDealFiscalList() {
        return dealFiscalList;
    }

    /**
     * @param dealFiscalList
     *            the dealFiscalList to set
     */
    public void setDealFiscalList(final List<String> dealFiscalList) {
        this.dealFiscalList = dealFiscalList;
    }

    /**
     * Limpa o Bean.
     */
    public void reset() {
        this.anoMesReceita = null;
        this.anoMesAjuste = null;
        this.contratoPraticaList = new ArrayList<String>();
        this.contratoPraticaMap = new HashMap<String, Long>();
        this.currentRowId = Long.valueOf(0);
        this.currentPageId = Integer.valueOf(0);
        this.resultList = new ArrayList<AjusteReceita>();

        this.resultListAddEdit = new ArrayList<AjusteReceita>();
        this.validityMonthAdjust = "";
        this.validityMonthRevenue = "";
        this.validityYearAdjust = "";
        this.validityYearRevenue = "";
        this.validityMonthAdjustAddEdit = "";
        this.validityMonthRevenueAddEdit = "";
        this.validityYearAdjustAddEdit = "";
        this.validityYearRevenueAddEdit = "";
        this.nomeContratoPraticaConsulta = "";
        this.nomeContratoPraticaAdd = "";
        this.showEditAjusteReceitaPanel = Boolean.valueOf(false);
        this.isEditAjusteReceita = Boolean.valueOf(false);
        this.isViewBySearch = Boolean.valueOf(false);
        this.resetTo();
        this.resetAjusteReceitaFields();
        this.setFlagComboDealAndType(Boolean.FALSE);
        this.setFlagComboTo(Boolean.FALSE);
        
        this.nomeDealFiscalSelected = "";
        this.nomeDealFiscalSelectedTo = "";
        
        
    }

    /**
     * Limpa o To.
     */
    public void resetTo() {
        this.to = new AjusteReceita();
        this.to.setReceitaDealFiscal(new ReceitaDealFiscal());
        this.to.setTipoAjuste(new TipoAjuste());
        this.to.setCodigoLoginAutor("");
    }

    /**
     * Limpa os outros campos do formulario.
     */
    public void resetAjusteReceitaFields() {
        this.resetTo();
        this.validityMonthAdjust = "";
        this.validityYearAdjust = "";
        this.nomeDealFiscalSelected = "";
        this.nomeTipoAjuste = "";
        this.nomeDealFiscalSelectedTo = "";
    }
    
    /**
     * Reset flag.
     */
    public void resetAjusteReceitaPanel() {
        this.showEditAjusteReceitaPanel = Boolean.valueOf(false);
    }

    /**
     * @return the to
     */
    public AjusteReceita getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final AjusteReceita to) {
        this.to = to;
    }

    /**
     * @return the anoMesReceita
     */
    public Date getAnoMesReceita() {
        return anoMesReceita;
    }

    /**
     * @param anoMesReceita
     *            the anoMesReceita to set
     */
    public void setAnoMesReceita(final Date anoMesReceita) {
        this.anoMesReceita = anoMesReceita;
    }

    /**
     * @return the anoMesAjuste
     */
    public Date getAnoMesAjuste() {
        return anoMesAjuste;
    }

    /**
     * @param anoMesAjuste
     *            the anoMesAjuste to set
     */
    public void setAnoMesAjuste(final Date anoMesAjuste) {
        this.anoMesAjuste = anoMesAjuste;
    }

    /**
     * @return the nomeContratoPraticaConsulta
     */
    public String getNomeContratoPraticaConsulta() {
        return nomeContratoPraticaConsulta;
    }

    /**
     * @param nomeContratoPraticaConsulta
     *            the nomeContratoPraticaConsulta to set
     */
    public void setNomeContratoPraticaConsulta(
            final String nomeContratoPraticaConsulta) {
        this.nomeContratoPraticaConsulta = nomeContratoPraticaConsulta;
    }

    /**
     * @return the contratoPraticaList
     */
    public List<String> getContratoPraticaList() {
        return contratoPraticaList;
    }

    /**
     * @param contratoPraticaList
     *            the contratoPraticaList to set
     */
    public void setContratoPraticaList(final List<String> contratoPraticaList) {
        this.contratoPraticaList = contratoPraticaList;
    }

    /**
     * @return the contratoPraticaMap
     */
    public Map<String, Long> getContratoPraticaMap() {
        return contratoPraticaMap;
    }

    /**
     * @param contratoPraticaMap
     *            the contratoPraticaMap to set
     */
    public void setContratoPraticaMap(final Map<String, Long> contratoPraticaMap) {
        this.contratoPraticaMap = contratoPraticaMap;
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
     * @return the resultList
     */
    public List<AjusteReceita> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<AjusteReceita> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the validityMonthList
     */
    public List<String> getValidityMonthList() {
        return validityMonthList;
    }

    /**
     * @param validityMonthList
     *            the validityMonthList to set
     */
    public void setValidityMonthList(final List<String> validityMonthList) {
        this.validityMonthList = validityMonthList;
    }

    /**
     * @return the validityYearList
     */
    public List<String> getValidityYearList() {
        int yearBegin =
                Integer
                        .parseInt(systemProperties
                                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range =
                Integer
                        .parseInt(systemProperties
                                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        validityYearList = yearList;

        return validityYearList;
    }

    /**
     * @param validityYearList
     *            the validityYearList to set
     */
    public void setValidityYearList(final List<String> validityYearList) {
        this.validityYearList = validityYearList;
    }

    /**
     * @return the validityMonthAdjust
     */
    public String getValidityMonthAdjust() {
        return validityMonthAdjust;
    }

    /**
     * @param validityMonthAdjust
     *            the validityMonthAdjust to set
     */
    public void setValidityMonthAdjust(final String validityMonthAdjust) {
        this.validityMonthAdjust = validityMonthAdjust;
    }

    /**
     * @return the validityYearAdjust
     */
    public String getValidityYearAdjust() {
        return validityYearAdjust;
    }

    /**
     * @param validityYearAdjust
     *            the validityYearAdjust to set
     */
    public void setValidityYearAdjust(final String validityYearAdjust) {
        this.validityYearAdjust = validityYearAdjust;
    }

    /**
     * @return the validityMonthRevenue
     */
    public String getValidityMonthRevenue() {
        return validityMonthRevenue;
    }

    /**
     * @param validityMonthRevenue
     *            the validityMonthRevenue to set
     */
    public void setValidityMonthRevenue(final String validityMonthRevenue) {
        this.validityMonthRevenue = validityMonthRevenue;
    }

    /**
     * @return the validityYearRevenue
     */
    public String getValidityYearRevenue() {
        return validityYearRevenue;
    }

    /**
     * @param validityYearRevenue
     *            the validityYearRevenue to set
     */
    public void setValidityYearRevenue(final String validityYearRevenue) {
        this.validityYearRevenue = validityYearRevenue;
    }

    /**
     * @return the nomeContratoPraticaAdd
     */
    public String getNomeContratoPraticaAdd() {
        return nomeContratoPraticaAdd;
    }

    /**
     * @param nomeContratoPraticaAdd
     *            the nomeContratoPraticaAdd to set
     */
    public void setNomeContratoPraticaAdd(final String nomeContratoPraticaAdd) {
        this.nomeContratoPraticaAdd = nomeContratoPraticaAdd;
    }

    /**
     * @return the tipoAjusteList
     */
    public List<String> getTipoAjusteList() {
        return tipoAjusteList;
    }

    /**
     * @param tipoAjusteList
     *            the tipoAjusteList to set
     */
    public void setTipoAjusteList(final List<String> tipoAjusteList) {
        this.tipoAjusteList = tipoAjusteList;
    }

    /**
     * @return the tipoAjusteMap
     */
    public Map<String, Long> getTipoAjusteMap() {
        return tipoAjusteMap;
    }

    /**
     * @param tipoAjusteMap
     *            the tipoAjusteMap to set
     */
    public void setTipoAjusteMap(final Map<String, Long> tipoAjusteMap) {
        this.tipoAjusteMap = tipoAjusteMap;
    }

    /**
     * @return the resultListAddEdit
     */
    public List<AjusteReceita> getResultListAddEdit() {
        return resultListAddEdit;
    }

    /**
     * @param resultListAddEdit
     *            the resultListAddEdit to set
     */
    public void setResultListAddEdit(final List<AjusteReceita> resultListAddEdit) {
        this.resultListAddEdit = resultListAddEdit;
    }

    /**
     * @return the validityMonthAdjustAddEdit
     */
    public String getValidityMonthAdjustAddEdit() {
        return validityMonthAdjustAddEdit;
    }

    /**
     * @param validityMonthAdjustAddEdit
     *            the validityMonthAdjustAddEdit to set
     */
    public void setValidityMonthAdjustAddEdit(
            final String validityMonthAdjustAddEdit) {
        this.validityMonthAdjustAddEdit = validityMonthAdjustAddEdit;
    }

    /**
     * @return the validityYearAdjustAddEdit
     */
    public String getValidityYearAdjustAddEdit() {
        return validityYearAdjustAddEdit;
    }

    /**
     * @param validityYearAdjustAddEdit
     *            the validityYearAdjustAddEdit to set
     */
    public void setValidityYearAdjustAddEdit(
            final String validityYearAdjustAddEdit) {
        this.validityYearAdjustAddEdit = validityYearAdjustAddEdit;
    }

    /**
     * @return the validityMonthRevenueAddEdit
     */
    public String getValidityMonthRevenueAddEdit() {
        return validityMonthRevenueAddEdit;
    }

    /**
     * @param validityMonthRevenueAddEdit
     *            the validityMonthRevenueAddEdit to set
     */
    public void setValidityMonthRevenueAddEdit(
            final String validityMonthRevenueAddEdit) {
        this.validityMonthRevenueAddEdit = validityMonthRevenueAddEdit;
    }

    /**
     * @return the validityYearRevenueAddEdit
     */
    public String getValidityYearRevenueAddEdit() {
        return validityYearRevenueAddEdit;
    }

    /**
     * @param validityYearRevenueAddEdit
     *            the validityYearRevenueAddEdit to set
     */
    public void setValidityYearRevenueAddEdit(
            final String validityYearRevenueAddEdit) {
        this.validityYearRevenueAddEdit = validityYearRevenueAddEdit;
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
     * @return the nomeTipoAjuste
     */
    public String getNomeTipoAjuste() {
        return nomeTipoAjuste;
    }

    /**
     * @param nomeTipoAjuste
     *            the nomeTipoAjuste to set
     */
    public void setNomeTipoAjuste(final String nomeTipoAjuste) {
        this.nomeTipoAjuste = nomeTipoAjuste;
    }

    /**
     * @return the mesAjuste
     */
    public Date getMesAjuste() {
        return mesAjuste;
    }

    /**
     * @param mesAjuste
     *            the mesAjuste to set
     */
    public void setMesAjuste(final Date mesAjuste) {
        this.mesAjuste = mesAjuste;
    }

    /**
     * @return the anoAjuste
     */
    public Date getAnoAjuste() {
        return anoAjuste;
    }

    /**
     * @param anoAjuste
     *            the anoAjuste to set
     */
    public void setAnoAjuste(final Date anoAjuste) {
        this.anoAjuste = anoAjuste;
    }

    /**
     * @return the isViewBySearch
     */
    public boolean getIsViewBySearch() {
        return isViewBySearch;
    }

    /**
     * @param isViewBySearch
     *            the isViewBySearch to set
     */
    public void setIsViewBySearch(final boolean isViewBySearch) {
        this.isViewBySearch = isViewBySearch;
    }


    /**
     * @return the flagComboTo
     */
    public boolean getFlagComboTo() {
        return flagComboTo;
    }

    /**
     * @param flagComboTo
     *            the flagComboTo to set
     */
    public void setFlagComboTo(final boolean flagComboTo) {
        this.flagComboTo = flagComboTo;
    }

    /**
     * @return the nomeDealFiscalSelectedTo
     */
    public String getNomeDealFiscalSelectedTo() {
        return nomeDealFiscalSelectedTo;
    }

    /**
     * @param nomeDealFiscalSelectedTo
     *            the nomeDealFiscalSelectedTo to set
     */
    public void setNomeDealFiscalSelectedTo(
            final String nomeDealFiscalSelectedTo) {
        this.nomeDealFiscalSelectedTo = nomeDealFiscalSelectedTo;
    }

    /**
     * @return the flagComboDealAndType
     */
    public boolean getFlagComboDealAndType() {
        return flagComboDealAndType;
    }

    /**
     * @param flagComboDealAndType the flagComboDealAndType to set
     */
    public void setFlagComboDealAndType(final boolean flagComboDealAndType) {
        this.flagComboDealAndType = flagComboDealAndType;
    }

    /**
     * @return the flagAbaAjusteReceita
     */
    public boolean getFlagAbaAjusteReceita() {
        return flagAbaAjusteReceita;
    }

    /**
     * @param flagAbaAjusteReceita the flagAbaAjusteReceita to set
     */
    public void setFlagAbaAjusteReceita(final boolean flagAbaAjusteReceita) {
        this.flagAbaAjusteReceita = flagAbaAjusteReceita;
    }

    
	/**
	 * @return the receitaMoeda
	 */
	public ReceitaMoeda getReceitaMoeda() {
		return receitaMoeda;
	}

	/**
	 * @param receitaMoeda the receitaMoeda to set
	 */
	public void setReceitaMoeda(final ReceitaMoeda receitaMoeda) {
		this.receitaMoeda = receitaMoeda;
	}

    

    
    
    
    
    
    

}
