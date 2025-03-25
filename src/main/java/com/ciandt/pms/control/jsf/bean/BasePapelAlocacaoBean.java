package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.PapelAlocacao;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 13/07/2011
 * @author cmantovani
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class BasePapelAlocacaoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private BasePapelAlocacao to = new BasePapelAlocacao();

    /** filter do backingBean. */
    private BasePapelAlocacao filter = new BasePapelAlocacao();

    /** toTcePapelAlocacao do backingBean. */
    // private TcePapelAlocacao toTcePapelAlocacao = new TcePapelAlocacao();
    /** lista de resultados da pesquisa. */
    private List<BasePapelAlocacao> resultList =
            new ArrayList<BasePapelAlocacao>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Indicador do modo em tempo de execucao - remove/view. */
    private Boolean isRemove = Boolean.valueOf(false);

    /** Indicador do modo em tempo de execucao - edit. */
    private Boolean isEdit = Boolean.valueOf(false);

    /** Lista para o combobox com os PapelAlocacao. */
    private List<String> papelAlocacaoList = new ArrayList<String>();

    /** Lista para o combobox com os PapelAlocacao. */
    private Map<String, Long> papelAlocacaoMap = new HashMap<String, Long>();

    /** Lista para o combobox com os PapelAlocacao. */
    private List<String> empresaList = new ArrayList<String>();

    /** Lista para o combobox com os PapelAlocacao. */
    private Map<String, Long> empresaMap = new HashMap<String, Long>();

    /** Lista para o combobox com as CidadeBase. */
    private List<String> cidadeBaseList = new ArrayList<String>();

    /** Lista para o combobox com as CidadeBase. */
    private Map<String, Long> cidadeBaseMap = new HashMap<String, Long>();

    /** Nome da Empresa Matriz. */
    private String nomeEmpresaMatriz;

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

    public void setNomeEmpresaMatriz(final String nomeEmpresaMatriz){this.nomeEmpresaMatriz = nomeEmpresaMatriz;}

    public String getNomeEmpresaMatriz() {return this.nomeEmpresaMatriz;}

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new BasePapelAlocacao();
        this.to.setCidadeBase(new CidadeBase());
        this.to.setPapelAlocacao(new PapelAlocacao());
        this.nomeEmpresaMatriz = "";
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new BasePapelAlocacao();
        this.filter.setCidadeBase(new CidadeBase());
        this.filter.setPapelAlocacao(new PapelAlocacao());
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<BasePapelAlocacao>();
    }

    /**
     * @return the to
     */
    public BasePapelAlocacao getTo() {
        if (to == null) {
            to = new BasePapelAlocacao();
        }
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final BasePapelAlocacao to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public BasePapelAlocacao getFilter() {
        if (filter == null) {
            filter = new BasePapelAlocacao();
        }
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final BasePapelAlocacao filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<BasePapelAlocacao> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<BasePapelAlocacao> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the papelAlocacaoList
     */
    public List<String> getPapelAlocacaoList() {
        return papelAlocacaoList;
    }

    /**
     * @param papelAlocacaoList
     *            the papelAlocacaoList to set
     */
    public void setPapelAlocacaoList(final List<String> papelAlocacaoList) {
        this.papelAlocacaoList = papelAlocacaoList;
    }

    /**
     * @return the papelAlocacaoMap
     */
    public Map<String, Long> getPapelAlocacaoMap() {
        return papelAlocacaoMap;
    }

    /**
     * @param papelAlocacaoMap
     *            the papelAlocacaoMap to set
     */
    public void setPapelAlocacaoMap(final Map<String, Long> papelAlocacaoMap) {
        this.papelAlocacaoMap = papelAlocacaoMap;
    }

    /**
     * @return the empresaList
     */
    public List<String> getEmpresaList() {
        return empresaList;
    }

    /**
     * @param empresaList
     *            the EmpresaList to set
     */
    public void setEmpresaList(final List<String> empresaList) {
        this.empresaList = empresaList;
    }

    /**
     * @return the papelAlocacaoMap
     */
    public Map<String, Long> getEmpresaMap() {
        return empresaMap;
    }

    /**
     * @param empresaMap
     *            the papelAlocacaoMap to set
     */
    public void setEmpresaMap(final Map<String, Long> empresaMap) {
        this.empresaMap = empresaMap;
    }

    /**
     * @return the cidadeBaseList
     */
    public List<String> getCidadeBaseList() {
        return cidadeBaseList;
    }

    /**
     * @param cidadeBaseList
     *            the cidadeBaseList to set
     */
    public void setCidadeBaseList(final List<String> cidadeBaseList) {
        this.cidadeBaseList = cidadeBaseList;
    }

    /**
     * @return the cidadeBaseMap
     */
    public Map<String, Long> getCidadeBaseMap() {
        return cidadeBaseMap;
    }

    /**
     * @param cidadeBaseMap
     *            the cidadeBaseMap to set
     */
    public void setCidadeBaseMap(final Map<String, Long> cidadeBaseMap) {
        this.cidadeBaseMap = cidadeBaseMap;
    }

    /**
     * @return the isEdit
     */
    public Boolean getIsEdit() {
        return isEdit;
    }

    /**
     * @param isEdit
     *            the isEdit to set
     */
    public void setIsEdit(final Boolean isEdit) {
        this.isEdit = isEdit;
    }

}