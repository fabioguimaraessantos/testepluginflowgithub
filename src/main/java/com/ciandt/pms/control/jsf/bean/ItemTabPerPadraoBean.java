package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.vo.ItemTabPerPadraoRow;


/**
 * 
 * A classe ItemTabPerPadraoBean proporciona as funcionalidades de ... para ...
 * 
 * @since 21/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ItemTabPerPadraoBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private ItemTabPerPadrao to;

    /** Pmg. */
    private String pmg;

    /** Cargo. */
    private String cargo;

    /** Base. */
    private String base;

    /** Valor. */
    private BigDecimal valor = new BigDecimal(0);

    /** Map para combo de pmg. */
    private Map<String, Long> mapPmg = new HashMap<String, Long>();

    /** Map para combo de cargo. */
    private Map<String, Long> mapCargo = new HashMap<String, Long>();

    /** Map para combo de base. */
    private Map<String, Long> mapBase = new HashMap<String, Long>();

    /** Lista de ItemTabPerPadrao. */
    private List<ItemTabPerPadrao> listaItemTabPerPadrao =
            new ArrayList<ItemTabPerPadrao>();

    /** Lista de itens para tabela da view. */
    private List<ItemTabPerPadraoRow> listaItemTabPerPadraoRow =
            new ArrayList<ItemTabPerPadraoRow>();

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /**
     * @return the to
     */
    public ItemTabPerPadrao getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ItemTabPerPadrao to) {
        this.to = to;
    }

    /**
     * @return the pmg
     */
    public String getPmg() {
        return pmg;
    }

    /**
     * @param pmg
     *            the pmg to set
     */
    public void setPmg(final String pmg) {
        this.pmg = pmg;
    }

    /**
     * @return the cargo
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * @param cargo
     *            the cargo to set
     */
    public void setCargo(final String cargo) {
        this.cargo = cargo;
    }

    /**
     * @return the base
     */
    public String getBase() {
        return base;
    }

    /**
     * @param base
     *            the base to set
     */
    public void setBase(final String base) {
        this.base = base;
    }

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor
     *            the valor to set
     */
    public void setValor(final BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the mapPmg
     */
    public Map<String, Long> getMapPmg() {
        return mapPmg;
    }

    /**
     * @param mapPmg
     *            the mapPmg to set
     */
    public void setMapPmg(final Map<String, Long> mapPmg) {
        this.mapPmg = mapPmg;
    }

    /**
     * @return the mapCargo
     */
    public Map<String, Long> getMapCargo() {
        return mapCargo;
    }

    /**
     * @param mapCargo
     *            the mapCargo to set
     */
    public void setMapCargo(final Map<String, Long> mapCargo) {
        this.mapCargo = mapCargo;
    }

    /**
     * @return the mapBase
     */
    public Map<String, Long> getMapBase() {
        return mapBase;
    }

    /**
     * @param mapBase
     *            the mapBase to set
     */
    public void setMapBase(final Map<String, Long> mapBase) {
        this.mapBase = mapBase;
    }

    /**
     * @return the listaItemTabPerPadrao
     */
    public List<ItemTabPerPadrao> getListaItemTabPerPadrao() {
        return listaItemTabPerPadrao;
    }

    /**
     * @param listaItemTabPerPadrao
     *            the listaItemTabPerPadrao to set
     */
    public void setListaItemTabPerPadrao(
            final List<ItemTabPerPadrao> listaItemTabPerPadrao) {
        this.listaItemTabPerPadrao = listaItemTabPerPadrao;
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.base = "";
        this.cargo = "";
        this.pmg = null;
        this.to = null;
        this.valor = new BigDecimal(0);

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
     * @return the listaItemTabPerPadraoRow
     */
    public List<ItemTabPerPadraoRow> getListaItemTabPerPadraoRow() {
        return listaItemTabPerPadraoRow;
    }

    /**
     * @param listaItemTabPerPadraoRow
     *            the listaItemTabPerPadraoRow to set
     */
    public void setListaItemTabPerPadraoRow(
            final List<ItemTabPerPadraoRow> listaItemTabPerPadraoRow) {
        this.listaItemTabPerPadraoRow = listaItemTabPerPadraoRow;
    }

 

}
