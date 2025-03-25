/**
 * TipoContratoBean - BackingBean do TipoContrato
 */
package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.TipoContrato;


/**
 * 
 * A classe TipoContratoBean proporciona as funcionalidades de backingBean para
 * para o TipoContrato.
 * 
 * @since 27/05/2011
 * @author cmantovani
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TipoContratoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private TipoContrato to = new TipoContrato();

    /** TipoContrato para edicao. */
    private TipoContrato tipoContratoEdit = new TipoContrato();

    /** lista de resultados da pesquisa. */
    private List<TipoContrato> resultList = new ArrayList<TipoContrato>();

    /** Lista para o combobox com os TipoContrato. */
    private List<String> tipoContratoList = new ArrayList<String>();

    /** Lista para o combobox com as TipoContrato. */
    private Map<String, Long> tipoContratoMap = new HashMap<String, Long>();

    /** Lista para o combobox com as Moedas. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox com as Moedas. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indica se esta atualizando. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Nome da moeda selecionada no combo. */
    private String nomeMoedaSelected = "";

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
        nomeMoedaSelected = "";
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new TipoContrato();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<TipoContrato>();
    }

    /**
     * @return the to
     */
    public TipoContrato getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final TipoContrato to) {
        this.to = to;
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
     * @return the resultList
     */
    public List<TipoContrato> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<TipoContrato> resultList) {
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
     * @return the nomeMoedaSelected
     */
    public String getNomeMoedaSelected() {
        return nomeMoedaSelected;
    }

    /**
     * @param nomeMoedaSelected
     *            the nomeMoedaSelected to set
     */
    public void setNomeMoedaSelected(final String nomeMoedaSelected) {
        this.nomeMoedaSelected = nomeMoedaSelected;
    }

    /**
     * @return the tipoContratoEdit
     */
    public TipoContrato getTipoContratoEdit() {
        return tipoContratoEdit;
    }

    /**
     * @param tipoContratoEdit
     *            the tipoContratoEdit to set
     */
    public void setTipoContratoEdit(final TipoContrato tipoContratoEdit) {
        this.tipoContratoEdit = tipoContratoEdit;
    }

}