package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ChargebackPessoa;

import java.io.Serializable;


/**
 * 
 * A classe ChargebackCell representa a celula do
 * mapa de alocação de chargeback.
 *
 * @since 21/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ChargebackCell implements Serializable {

    /** Default Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** Instancia de ChargebackContratoPratica. */
    private ChargebackContratoPratica chargebackCP;
    
    /** Instancia de ChargebackPessoa. */
    private ChargebackPessoa chargebackPess;

    /** Indica se a celula é editável. */
    private Boolean readonly = Boolean.valueOf(false);

    /**
     * @param chargebackCP the chargebackCP to set
     */
    public void setChargebackCP(final ChargebackContratoPratica chargebackCP) {
        this.chargebackCP = chargebackCP;
    }

    /**
     * @return the chargebackCP
     */
    public ChargebackContratoPratica getChargebackCP() {
        return chargebackCP;
    }
    
    /**
     * @param chargebackPess the chargebackPess to set
     */
    public void setChargebackPess(final ChargebackPessoa chargebackPess) {
        this.chargebackPess = chargebackPess;
    }

    /**
     * @return the chargebackPess
     */
    public ChargebackPessoa getChargebackPess() {
        return chargebackPess;
    }

    /**
     * @param readOnly the readOnly to set
     */
    public void setReadonly(final Boolean readOnly) {
        this.readonly = readOnly;
    }

    /**
     * @return the readOnly
     */
    public Boolean getReadonly() {
        return readonly;
    }
    
    
}
