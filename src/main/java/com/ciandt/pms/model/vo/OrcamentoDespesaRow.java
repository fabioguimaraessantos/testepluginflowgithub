package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.OrcamentoDespesa;

import java.math.BigDecimal;


/**
 * 
 * A classe OrcamentoDespesaRow representa uma linha da tabela de OrcamentoDespesa.
 *
 * @since 24/08/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public class OrcamentoDespesaRow {

    /** Orcamento Despesa. */
    private OrcamentoDespesa orcamentoDespesa;
    
    /** Valor total sacado. */
    private BigDecimal withdrawnValue;

    /**
     * @return the orcamentoDespesa
     */
    public OrcamentoDespesa getOrcamentoDespesa() {
        return orcamentoDespesa;
    }

    /**
     * @param orcamentoDespesa the orcamentoDespesa to set
     */
    public void setOrcamentoDespesa(final OrcamentoDespesa orcamentoDespesa) {
        this.orcamentoDespesa = orcamentoDespesa;
    }

    /**
     * @return the withdrawnValue
     */
    public BigDecimal getWithdrawnValue() {
        return withdrawnValue;
    }

    /**
     * @param withdrawnValue the withdrawnValue to set
     */
    public void setWithdrawnValue(final BigDecimal withdrawnValue) {
        this.withdrawnValue = withdrawnValue;
    }
    
    
    
}
