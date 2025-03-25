package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.VwHrsCargo;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * A classe CargoPmsRow proporciona as funcionalidades de VO para CargoPms.
 *
 * @since 11/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public class CargoPmsRow {
    
    /** Cargo Pms. */
    private CargoPms cargoPms;
    
    /** Lista de VwHrsCargo. */
    private List<VwHrsCargo> listaVwHrsCargo = new ArrayList<VwHrsCargo>();
    
    /** Flag para mostrar os cargos de CargoPms. */
    private Boolean showVwHrsCargo = Boolean.valueOf(false);
    
    /**
     * Construtor.
     * @param cargoPms cargoPms
     * @param listaVwHrsCargo lista
     */
    public CargoPmsRow(final CargoPms cargoPms, final List<VwHrsCargo> listaVwHrsCargo) {
        this.cargoPms = cargoPms;
        this.listaVwHrsCargo = listaVwHrsCargo;
    }
    
    /**
     * Default Constructor.
     */
    public CargoPmsRow() {
        
    }

    /**
     * @return the cargoPms
     */
    public CargoPms getCargoPms() {
        return cargoPms;
    }
    

    /**
     * @param cargoPms the cargoPms to set
     */
    public void setCargoPms(final CargoPms cargoPms) {
        this.cargoPms = cargoPms;
    }

    /**
     * @return the listaVwHrsCargo
     */
    public List<VwHrsCargo> getListaVwHrsCargo() {
        return listaVwHrsCargo;
    }

    /**
     * @param listaVwHrsCargo the listaVwHrsCargo to set
     */
    public void setListaVwHrsCargo(final List<VwHrsCargo> listaVwHrsCargo) {
        this.listaVwHrsCargo = listaVwHrsCargo;
    }

    /**
     * @return the showVwHrsCargo
     */
    public Boolean getShowVwHrsCargo() {
        return showVwHrsCargo;
    }

    /**
     * @param showVwHrsCargo the showVwHrsCargo to set
     */
    public void setShowVwHrsCargo(final Boolean showVwHrsCargo) {
        this.showVwHrsCargo = showVwHrsCargo;
    }
    
    
    
    

}
