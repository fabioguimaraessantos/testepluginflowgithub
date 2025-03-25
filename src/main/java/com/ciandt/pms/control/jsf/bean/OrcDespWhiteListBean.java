package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.OrcDespWhiteList;


/**
 * 
 * Define o Backbean.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespWhiteListBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** To. */
    private OrcDespWhiteList to = new OrcDespWhiteList();
    
    /** Lista de orcDespWhiteList. */
    private List<OrcDespWhiteList> listaOrcDespWhiteList = new ArrayList<OrcDespWhiteList>();
    
    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);
    

    /**
     * @return the to
     */
    public OrcDespWhiteList getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(final OrcDespWhiteList to) {
        this.to = to;
    }

    /**
     * @return the listaOrcDespWhiteList
     */
    public List<OrcDespWhiteList> getListaOrcDespWhiteList() {
        return listaOrcDespWhiteList;
    }

    /**
     * @param listaOrcDespWhiteList the listaOrcDespWhiteList to set
     */
    public void setListaOrcDespWhiteList(
            final List<OrcDespWhiteList> listaOrcDespWhiteList) {
        this.listaOrcDespWhiteList = listaOrcDespWhiteList;
    }
    
    /**
     * Reset do bean.
     */
    public void reset() {
        this.to = new OrcDespWhiteList();
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
  
    
}
