package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ItemTabPerPadrao;

/**
 * 
 * A classe ItemTabPerPadraoRow proporciona as funcionalidades de montar um
 * objeto para a tabela de ItemPerfilPadrao.
 * 
 * @since 22/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public class ItemTabPerPadraoRow {
    
    
    /** Nome Cargo. */
    private String nomeCargo;
    
    /** ItemTabPerPadrao. */
    private ItemTabPerPadrao itemTabPerPadrao;
    
    /** Indicador Ativo. */
    private String indicadorAtivo;

    /**
     * @return the nomeCargo
     */
    public String getNomeCargo() {
        return nomeCargo;
    }

    /**
     * @param nomeCargo the nomeCargo to set
     */
    public void setNomeCargo(final String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    /**
     * @return the itemTabPerPadrao
     */
    public ItemTabPerPadrao getItemTabPerPadrao() {
        return itemTabPerPadrao;
    }

    /**
     * @param itemTabPerPadrao the itemTabPerPadrao to set
     */
    public void setItemTabPerPadrao(final ItemTabPerPadrao itemTabPerPadrao) {
        this.itemTabPerPadrao = itemTabPerPadrao;
    }

    /**
     * @return the indicadorAtivo
     */
    public String getIndicadorAtivo() {
        return indicadorAtivo;
    }

    /**
     * @param indicadorAtivo the indicadorAtivo to set
     */
    public void setIndicadorAtivo(final String indicadorAtivo) {
        this.indicadorAtivo = indicadorAtivo;
    }
    

    
    
    

   
    

}
