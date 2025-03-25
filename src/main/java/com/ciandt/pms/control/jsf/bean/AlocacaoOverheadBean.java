package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TipoOverhead;
import com.ciandt.pms.model.vo.AlocacaoOverheadRow;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 19/07/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AlocacaoOverheadBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private AlocacaoOverhead to = new AlocacaoOverhead();

    /** filter do backingBean. */
    private AlocacaoOverhead filter = new AlocacaoOverhead();

    /** lista de resultados da pesquisa. */
    private List<AlocacaoOverheadRow> resultList = new ArrayList<AlocacaoOverheadRow>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);
    
    /** Lista para o combobox com os TipoOverhead. */
    private List<String> tipoOverheadList = new ArrayList<String>();

    /** Map para o combobox com os TipoOverhead. */
    private Map<String, Long> tipoOverheadMap = new HashMap<String, Long>();

    /**
     * @return the tipoOverheadList
     */
    public List<String> getTipoOverheadList() {
        return tipoOverheadList;
    }

    /**
     * @param tipoOverheadList the tipoOverheadList to set
     */
    public void setTipoOverheadList(final List<String> tipoOverheadList) {
        this.tipoOverheadList = tipoOverheadList;
    }

    /**
     * @return the tipoOverheadMap
     */
    public Map<String, Long> getTipoOverheadMap() {
        return tipoOverheadMap;
    }

    /**
     * @param tipoOverheadMap the tipoOverheadMap to set
     */
    public void setTipoOverheadMap(final Map<String, Long> tipoOverheadMap) {
        this.tipoOverheadMap = tipoOverheadMap;
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
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new AlocacaoOverhead();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new AlocacaoOverhead();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<AlocacaoOverheadRow>();
    }

    /**
     * @return the to
     */
    public AlocacaoOverhead getTo() {
        if (to == null) {
            to = new AlocacaoOverhead();
        }
        if (to.getPessoa() == null) {
            to.setPessoa(new Pessoa());
        }
        if (to.getTipoOverhead() == null) {
            to.setTipoOverhead(new TipoOverhead());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final AlocacaoOverhead to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public AlocacaoOverhead getFilter() {
        if (filter == null) {
            filter = new AlocacaoOverhead();
        }
        if (filter.getPessoa() == null) {
            filter.setPessoa(new Pessoa());
        }
        if (filter.getTipoOverhead() == null) {
            filter.setTipoOverhead(new TipoOverhead());
        }

        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final AlocacaoOverhead filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<AlocacaoOverheadRow> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<AlocacaoOverheadRow> resultList) {
        this.resultList = resultList;
    }

}