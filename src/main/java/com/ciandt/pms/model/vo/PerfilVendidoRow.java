package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.PerfilVendido;

/**
 * 
 * A classe PerfilVendidoRow proporciona as funcionalidades de montar um objeto para a tabela de PerfilVendido.
 *
 * @since 23/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public class PerfilVendidoRow {

    /** PerfilVendido. */
    private PerfilVendido perfilVendido;
    
    /** Nome do Cargo. */
    private String nomeCargo;
    

    /**
     * @return the perfilVendido
     */
    public PerfilVendido getPerfilVendido() {
        return perfilVendido;
    }

    /**
     * @param perfilVendido the perfilVendido to set
     */
    public void setPerfilVendido(final PerfilVendido perfilVendido) {
        this.perfilVendido = perfilVendido;
    }

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


    
    
    
}
