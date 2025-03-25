package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.TiRecurso;

import java.math.BigDecimal;


/**
 * 
 * A classe LicensaEmailRow  se refere aos objetos listados na lista de licensas evniadas por email.
 *
 * @since 16/03/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public class LicensaEmailRow {

    /**
     * Recurso (licensa).
     */
    private TiRecurso tiRecurso = new TiRecurso();
    
    /**
     * Numero de unidades que o colaborador possui da licensa.
     */
    private BigDecimal numeroUnidades = new BigDecimal(0);
    
    /**
     * Valor total da licensa (unidades * valor do recurso).
     */
    private BigDecimal valorTotal = new BigDecimal(0);
    
    /**
     * Valor da licensa.
     */
    private BigDecimal custoUnitario = new BigDecimal(0);

    
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
     * @return the numeroUnidades
     */
    public BigDecimal getNumeroUnidades() {
        return numeroUnidades;
    }

    /**
     * @param numeroUnidades the numeroUnidades to set
     */
    public void setNumeroUnidades(final BigDecimal numeroUnidades) {
        this.numeroUnidades = numeroUnidades;
    }

    /**
     * @return the valorTotal
     */
    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(final BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the custoUnitario
     */
    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    /**
     * @param custoUnitario the custoUnitario to set
     */
    public void setCustoUnitario(final BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }
    
    
    
    
    
 
    
}
