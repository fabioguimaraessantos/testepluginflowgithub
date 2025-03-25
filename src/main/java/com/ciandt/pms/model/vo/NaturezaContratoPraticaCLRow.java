package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.NaturezaCentroLucro;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * A classe NaturezaContratoPraticaCLRow utlizada para listar(view do contrato-pratica)
 *  a associção do ContratoPratica com o CentroLucro.
 *
 * @since 06/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class NaturezaContratoPraticaCLRow implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Intancia da NaturezaCentroLucro. */
    private NaturezaCentroLucro natureza;
    
    /** Lista de ContratoPRaticaCentroLucro relacionado com a natureza. */
    private List<ContratoPraticaCentroLucro> contratoPraticaCLList;

    /**
     * @param natureza the natureza to set
     */
    public void setNatureza(final NaturezaCentroLucro natureza) {
        this.natureza = natureza;
    }

    /**
     * @return the natureza
     */
    public NaturezaCentroLucro getNatureza() {
        return natureza;
    }

    /**
     * @param contratoPraticaCL the contratoPraticaCL to set
     */
    public void setContratoPraticaCLList(
            final List<ContratoPraticaCentroLucro> contratoPraticaCL) {
        this.contratoPraticaCLList = contratoPraticaCL;
    }

    /**
     * @return the contratoPraticaCL
     */
    public List<ContratoPraticaCentroLucro> getContratoPraticaCLList() {
        return contratoPraticaCLList;
    }

}
