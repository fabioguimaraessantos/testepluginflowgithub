package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.VwHrsCargo;
import com.ciandt.pms.model.vo.CargoPmsRow;


/**
 * 
 * Define o BackingBean da entidade Cargo.
 * 
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Carlos Augusto Ribeiro
 *         Mantovani</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CargoBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** To da entidade. */
    private CargoPms to = new CargoPms();

    /** Lista de itens para pickList. */
    private List<SelectItem> listPickList = new ArrayList<SelectItem>();

    /** Lista de cargos filtrados pela pickList. */
    private String[] grantedCargos;

    /** Lista de CargoPms. */
    private List<CargoPms> listaCargoPms = new ArrayList<CargoPms>();

    /** Map para combo. */
    private Map<String, Long> mapCargos = new HashMap<String, Long>();
    
    /** Lista de itens. */
    private List<VwHrsCargo> listaItens = new ArrayList<VwHrsCargo>();
    
    /** Lista de CargoPmsRow. */
    private List<CargoPmsRow> listaCargoPmsRow = new ArrayList<CargoPmsRow>();
    
    /** CargoRow. */
    private CargoPmsRow cargoPmsRow = new CargoPmsRow();
    
    /** Flag para editar cargoPms. */
    private Boolean flagUpdate = Boolean.valueOf(false);

    /**
     * @return the to
     */
    public CargoPms getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final CargoPms to) {
        this.to = to;
    }

    /**
     * @return the listPickList
     */
    public List<SelectItem> getListPickList() {
        return listPickList;
    }

    /**
     * @param listPickList
     *            the listPickList to set
     */
    public void setListPickList(final List<SelectItem> listPickList) {
        this.listPickList = listPickList;
    }

    /**
     * @return the grantedCargos
     */
    public String[] getGrantedCargos() {
        return grantedCargos;
    }

    /**
     * @param grantedCargos
     *            the grantedCargos to set
     */
    public void setGrantedCargos(final String[] grantedCargos) {
        this.grantedCargos = grantedCargos;
    }

    /**
     * @return the listaCargoPms
     */
    public List<CargoPms> getListaCargoPms() {
        return listaCargoPms;
    }

    /**
     * @param listaCargoPms
     *            the listaCargoPms to set
     */
    public void setListaCargoPms(final List<CargoPms> listaCargoPms) {
        this.listaCargoPms = listaCargoPms;
    }

    /**
     * @return the mapCargos
     */
    public Map<String, Long> getMapCargos() {
        return mapCargos;
    }

    /**
     * @param mapCargos
     *            the mapCargos to set
     */
    public void setMapCargos(final Map<String, Long> mapCargos) {
        this.mapCargos = mapCargos;
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.grantedCargos = null;
        this.to = new CargoPms();
    }

    /**
     * @return the listaItens
     */
    public List<VwHrsCargo> getListaItens() {
        return listaItens;
    }

    /**
     * @param listaItens the listaItens to set
     */
    public void setListaItens(final List<VwHrsCargo> listaItens) {
        this.listaItens = listaItens;
    }

    /**
     * @return the listaCargoPmsRow
     */
    public List<CargoPmsRow> getListaCargoPmsRow() {
        return listaCargoPmsRow;
    }

    /**
     * @param listaCargoPmsRow the listaCargoPmsRow to set
     */
    public void setListaCargoPmsRow(final List<CargoPmsRow> listaCargoPmsRow) {
        this.listaCargoPmsRow = listaCargoPmsRow;
    }

    /**
     * @return the cargoPmsRow
     */
    public CargoPmsRow getCargoPmsRow() {
        return cargoPmsRow;
    }

    /**
     * @param cargoPmsRow the cargoPmsRow to set
     */
    public void setCargoPmsRow(final CargoPmsRow cargoPmsRow) {
        this.cargoPmsRow = cargoPmsRow;
    }

    /**
     * @return the flagUpdate
     */
    public Boolean getFlagUpdate() {
        return flagUpdate;
    }

    /**
     * @param flagUpdate the flagUpdate to set
     */
    public void setFlagUpdate(final Boolean flagUpdate) {
        this.flagUpdate = flagUpdate;
    }
    
    

}
