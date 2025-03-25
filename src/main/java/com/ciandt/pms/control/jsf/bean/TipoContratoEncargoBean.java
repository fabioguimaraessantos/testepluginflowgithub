/**
 * TipoContratoEncargoBean - BackingBean do TipoContratoEncargo
 */
package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.TipoContratoEncargo;


/**
 * 
 * A classe TipoContratoEncargoBean proporciona as funcionalidades de
 * backingBean para para o TipoContratoEncargo.
 * 
 * @since 01/06/2011
 * @author cmantovani
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TipoContratoEncargoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private TipoContratoEncargo to = new TipoContratoEncargo();

    /** TipoContratoEncargo para edicao. */
    private TipoContratoEncargo tipoContratoEncargoEdit = new TipoContratoEncargo();

    /** lista de resultados da pesquisa. */
    private List<TipoContratoEncargo> resultList = new ArrayList<TipoContratoEncargo>();

    /** Lista para o combobox com os TipoContrato. */
    private List<String> tipoContratoList = new ArrayList<String>();

    /** Map para o combobox com as TipoContrato. */
    private Map<String, Long> tipoContratoMap = new HashMap<String, Long>();

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Nome do tipoContratoEncargo selecionado no combo. */
    private String nomeTipoContratoSelected = "";

    /** Nome da moeda. */
    private String nomeMoeda = "";

    /** Propriedade do mes de inicio. */
    private String startMonth;

    /** Propriedade do ano de inicio. */
    private String startYear;

    /** Data do HistoryLock. */
    private Date historyLockDate;

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
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetResultList();
        nomeTipoContratoSelected = "";
        nomeMoeda = "";
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new TipoContratoEncargo();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<TipoContratoEncargo>();
    }

    /**
     * @return the to
     */
    public TipoContratoEncargo getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final TipoContratoEncargo to) {
        this.to = to;
    }

    /**
     * @return the resultList
     */
    public List<TipoContratoEncargo> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<TipoContratoEncargo> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the tipoContratoList
     */
    public List<String> getTipoContratoList() {
        return tipoContratoList;
    }

    /**
     * @param tipoContratoList
     *            the tipoContratoList to set
     */
    public void setTipoContratoList(final List<String> tipoContratoList) {
        this.tipoContratoList = tipoContratoList;
    }

    /**
     * @return the tipoContratoMap
     */
    public Map<String, Long> getTipoContratoMap() {
        return tipoContratoMap;
    }

    /**
     * @param tipoContratoMap
     *            the tipoContratoMap to set
     */
    public void setTipoContratoMap(final Map<String, Long> tipoContratoMap) {
        this.tipoContratoMap = tipoContratoMap;
    }

    /**
     * @return the nomeMoedaSelected
     */
    public String getNomeTipoContratoSelected() {
        return nomeTipoContratoSelected;
    }

    /**
     * @param nomeTipoContratoSelected
     *            the nomeTipoContratoSelected to set
     */
    public void setNomeTipoContratoSelected(
            final String nomeTipoContratoSelected) {
        this.nomeTipoContratoSelected = nomeTipoContratoSelected;
    }

    /**
     * @return the tipoContratoEdit
     */
    public TipoContratoEncargo getTipoContratoEncargoEdit() {
        return tipoContratoEncargoEdit;
    }

    /**
     * @param tipoContratoEncargoEdit
     *            the tipoContratoEncargoEdit to set
     */
    public void setTipoContratoEncargoEdit(
            final TipoContratoEncargo tipoContratoEncargoEdit) {
        this.tipoContratoEncargoEdit = tipoContratoEncargoEdit;
    }

    /**
     * @return the startMonth
     */
    public String getStartMonth() {
        return startMonth;
    }

    /**
     * @param startMonth
     *            the startMonth to set
     */
    public void setStartMonth(final String startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * @return the startYear
     */
    public String getStartYear() {
        return startYear;
    }

    /**
     * @param startYear
     *            the startYear to set
     */
    public void setStartYear(final String startYear) {
        this.startYear = startYear;
    }

    /**
     * @return the nomeMoeda
     */
    public String getNomeMoeda() {
        return nomeMoeda;
    }

    /**
     * @param nomeMoeda
     *            the nomeMoeda to set
     */
    public void setNomeMoeda(final String nomeMoeda) {
        this.nomeMoeda = nomeMoeda;
    }

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

}