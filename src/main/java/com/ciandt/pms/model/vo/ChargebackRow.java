package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.TiRecurso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * A classe ChargebackRow representa a linha no Mapa
 * de Alocacao do ItChargeback.
 *
 * @since 19/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ChargebackRow implements Serializable {

    /** Default Serial Version. */
    private static final long serialVersionUID = 1L;

    /** Lista com as celulas de uma linha do Mapa de chargeback. */
    private List<ChargebackCell> cellList = new ArrayList<ChargebackCell>();
    
    /** Indica de a linha está selecionada. */
    private Boolean isSelected = Boolean.valueOf(false);
    
    /** Recurso da linha da linha. */
    private TiRecurso tiRecurso;
    
    /** ContratoPratica da linha. */
    private ContratoPratica cp;
    
    /** Pessoa da linha. */
    private Pessoa pessoa;

    /**
     * @return the cellList
     */
    public List<ChargebackCell> getCellList() {
        return cellList;
    }

    /**
     * @param cellList the cellList to set
     */
    public void setCellList(final List<ChargebackCell> cellList) {
        this.cellList = cellList;
    }
    
    /**
     * @return the tiRecurso
     */
    public TiRecurso getTiRecurso() {
        return tiRecurso;
    }

    /**
     * @param tiRecurso the tiRecurso to set
     */
    public void setTiRecurso(final TiRecurso tiRecurso) {
        this.tiRecurso = tiRecurso;
    }

    /**
     * @return the cp
     */
    public ContratoPratica getCp() {
        return cp;
    }

    /**
     * @param cp the cp to set
     */
    public void setCp(final ContratoPratica cp) {
        this.cp = cp;
    }
    
    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(final Pessoa pessoa) {
        this.pessoa = pessoa;
    }
    
    /**
     * @param isSelected the isSelected to set
     */
    public void setIsSelected(final Boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }
    
}