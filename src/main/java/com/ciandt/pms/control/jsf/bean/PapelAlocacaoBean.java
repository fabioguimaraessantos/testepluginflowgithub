package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
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
import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.TcePapelAlocacao;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 26/01/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PapelAlocacaoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** to do backingBean. */
    private PapelAlocacao to = new PapelAlocacao();

    /** filter do backingBean. */
    private PapelAlocacao filter = new PapelAlocacao();

    /** toTcePapelAlocacao do backingBean. */
    private TcePapelAlocacao toTcePapelAlocacao = new TcePapelAlocacao();

    /** lista de resultados da pesquisa. */
    private List<PapelAlocacao> resultList = new ArrayList<PapelAlocacao>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Indicador do modo em tempo de execucao - remove/view. */
    private Boolean isRemove = Boolean.valueOf(false);

    /** Lista dos possiveis valores de meses. */
    private List<String> validityMonthList = Arrays.asList("01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12");

    /** Lista dos possiveis valores de anos. */
    private List<String> validityYearList = new ArrayList<String>();

    /** Mes vigencia - inicio. */
    private String validityMonthBeg = null;

    /** Ano vigencia - inicio. */
    private String validityYearBeg = null;

    /** Lista para o combobox com as Moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox com as Moeda. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /** Data do HistoryLock. */
    private Date historyLockDate;

    /**
     * @return the historyLockDate
     */
    public Date getHistoryLockDate() {
        return historyLockDate;
    }

    /**
     * @param historyLockDate
     *            the historyLockDate to set
     */
    public void setHistoryLockDate(final Date historyLockDate) {
        this.historyLockDate = historyLockDate;
    }

    /**
     * @return the moedaList
     */
    public List<String> getMoedaList() {
        return moedaList;
    }

    /**
     * @param moedaList
     *            the moedaList to set
     */
    public void setMoedaList(final List<String> moedaList) {
        this.moedaList = moedaList;
    }

    /**
     * @return the moedaMap
     */
    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    /**
     * @param moedaMap
     *            the moedaMap to set
     */
    public void setMoedaMap(final Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    /**
     * @return the toTcePapelAlocacao
     */
    public TcePapelAlocacao getToTcePapelAlocacao() {
        return toTcePapelAlocacao;
    }

    /**
     * @param toTcePapelAlocacao
     *            the toTcePapelAlocacao to set
     */
    public void setToTcePapelAlocacao(final TcePapelAlocacao toTcePapelAlocacao) {
        this.toTcePapelAlocacao = toTcePapelAlocacao;
    }

    /**
     * @return the isRemove
     */
    public Boolean getIsRemove() {
        return isRemove;
    }

    /**
     * @param isRemove
     *            the isRemove to set
     */
    public void setIsRemove(final Boolean isRemove) {
        this.isRemove = isRemove;
    }

    /**
     * @return the validityMonthBeg
     */
    public String getValidityMonthBeg() {
        return validityMonthBeg;
    }

    /**
     * @param validityMonthBeg
     *            the validityMonthBeg to set
     */
    public void setValidityMonthBeg(final String validityMonthBeg) {
        this.validityMonthBeg = validityMonthBeg;
    }

    /**
     * @return the validityYearBeg
     */
    public String getValidityYearBeg() {
        return validityYearBeg;
    }

    /**
     * @param validityYearBeg
     *            the validityYearBeg to set
     */
    public void setValidityYearBeg(final String validityYearBeg) {
        this.validityYearBeg = validityYearBeg;
    }

    /**
     * @return the validityMonthList
     */
    public List<String> getValidityMonthList() {
        return validityMonthList;
    }

    /**
     * @return lista de anos da vigencia
     */
    public List<String> getValidityYearList() {

        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        validityYearList = yearList;

        return validityYearList;
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
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        resetValidityDate();
        resetToTcePapelAlocacao();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new PapelAlocacao();
    }

    /**
     * Reseta o toTcePapelAlocacao.
     */
    public void resetToTcePapelAlocacao() {
        this.toTcePapelAlocacao = new TcePapelAlocacao();
    }

    /**
     * Reseta a data de vigencia.
     */
    public void resetValidityDate() {
        this.validityMonthBeg = "";
        this.validityYearBeg = "";
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new PapelAlocacao();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<PapelAlocacao>();
    }

    /**
     * @return the to
     */
    public PapelAlocacao getTo() {
        if (to == null) {
            to = new PapelAlocacao();
        }
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final PapelAlocacao to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public PapelAlocacao getFilter() {
        if (filter == null) {
            filter = new PapelAlocacao();
        }
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final PapelAlocacao filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<PapelAlocacao> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<PapelAlocacao> resultList) {
        this.resultList = resultList;
    }

}