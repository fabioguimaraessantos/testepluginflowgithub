package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ComposicaoTce;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Pessoa;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 08/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ComposicaoTceBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private ComposicaoTce to = new ComposicaoTce();

    /** filter do backingBean. */
    private ComposicaoTce filter = new ComposicaoTce();

    /** lista de resultados da pesquisa. */
    private List<ComposicaoTce> resultList;

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** representa o mes selecionado. */
    private String monthBeg;

    /** representa o ano selecionado. */
    private String yearBeg;

    /** representa o mes selecionado - tela search. */
    private String monthBegFilter;

    /** representa o ano selecionado - tela search. */
    private String yearBegFilter;

    /** Lista para o combobox com as Moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox com as Moeda. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /**
     * Indicador do flag para buscar registros com campos de valores nulos.
     */
    private Boolean isMissingBlankValues = Boolean.valueOf(false);

    /** Login do registro que está sendo atualizado. */
    private String codigoLoginUpdate = "";

    /** Indicador do valor da barra de progresso. */
    private double progressPercent;

    /** Indicador do status da progress bar. */
    private Boolean isProgressFinished = Boolean.valueOf(false);

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
     * @return the progressPercent
     */
    public double getProgressPercent() {
        return progressPercent;
    }

    /**
     * @param progressPercent
     *            the progressPercent to set
     */
    public void setProgressPercent(final double progressPercent) {
        this.progressPercent = progressPercent;
    }

    /**
     * @return the valueRounded
     */
    public long getValueRounded() {
        return Math.round(progressPercent);
    }

    /**
     * @return the codigoLoginUpdate
     */
    public String getCodigoLoginUpdate() {
        return codigoLoginUpdate;
    }

    /**
     * @param codigoLoginUpdate
     *            the codigoLoginUpdate to set
     */
    public void setCodigoLoginUpdate(final String codigoLoginUpdate) {
        this.codigoLoginUpdate = codigoLoginUpdate;
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
        this.resetTo();
        this.resetFilter();
        this.resetResultList();
        this.resetDataMes();
        this.resetDataMesFilter();
        this.isMissingBlankValues = Boolean.valueOf(false);
        this.filter.setIndicadorTipo(Constants.ALL);
        this.resetCodigoLoginUpdate();
        this.isProgressFinished = Boolean.valueOf(false);
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new ComposicaoTce();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new ComposicaoTce();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = null;
    }

    /**
     * Reseta os combos de mes e ano.
     */
    public void resetDataMes() {
        this.monthBeg = "";
        this.yearBeg = "";
    }

    /**
     * Reseta os combos de mes e ano - tela search.
     */
    public void resetDataMesFilter() {
        this.monthBegFilter = "";
        this.yearBegFilter = "";
    }

    /**
     * Reseta o campo auxiliar codigoLoginUpdate que guarda o login que está
     * sendo atualizado.
     */
    public void resetCodigoLoginUpdate() {
        this.codigoLoginUpdate = "";
    }

    /**
     * Reseta os valores da progressBar.
     */
    public void resetBar() {
        this.setProgressPercent(0);
    }

    /**
     * @return the to
     */
    public ComposicaoTce getTo() {
        if (to == null) {
            to = new ComposicaoTce();
        }
        if (to.getPessoa() == null) {
            to.setPessoa(new Pessoa());
        }
        if (to.getMoeda() == null) {
            to.setMoeda(new Moeda());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ComposicaoTce to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public ComposicaoTce getFilter() {
        if (filter == null) {
            filter = new ComposicaoTce();
        }
        if (filter.getPessoa() == null) {
            filter.setPessoa(new Pessoa());
        }
        if (filter.getMoeda() == null) {
            filter.setMoeda(new Moeda());
        }

        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final ComposicaoTce filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<ComposicaoTce> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<ComposicaoTce> resultList) {
        this.resultList = resultList;
    }

}