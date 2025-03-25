package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Recurso;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * A classe AlocacaoContratoPraticaRow utilizada como VO para armazenar as 
 * informações de um recurso sobre sua alocacao em um contrato-pratica.
 *
 * @since 13/10/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class AlocacaoContratoPraticaRow implements java.io.Serializable {

    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Instancia do ContratoPRatica. */
    private ContratoPratica contratoPratica;
    
    /** Instancia do Recurso. */
    private Recurso recurso;
    
    /** Lista de AlocacaoPeriodo relacionado com a Alocacao corrente. */
    private List<AlocacaoCell> alocacaoCellList = new ArrayList<AlocacaoCell>(0);

    /**
     * @param contratoPratica the contratoPratica to set
     */
    public void setContratoPratica(final ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    /**
     * @return the contratoPratica
     */
    public ContratoPratica getContratoPratica() {
        return contratoPratica;
    }

    /**
     * @param recurso the recurso to set
     */
    public void setRecurso(final Recurso recurso) {
        this.recurso = recurso;
    }

    /**
     * @return the recurso
     */
    public Recurso getRecurso() {
        return recurso;
    }

    /**
     * @param alocacaoCellList the alocacaoCellList to set
     */
    public void setAlocacaoCellList(final List<AlocacaoCell> alocacaoCellList) {
        this.alocacaoCellList = alocacaoCellList;
    }

    /**
     * @return the alocacaoCellList
     */
    public List<AlocacaoCell> getAlocacaoCellList() {
        return alocacaoCellList;
    }

}
