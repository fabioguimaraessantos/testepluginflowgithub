package com.ciandt.pms.comparator;

import java.util.Comparator;

import com.ciandt.pms.model.vo.AlocacaoRow;


/**
 * 
 * A classe CidadeBaseAlocacaoComparator proporciona as funcionalidades de
 * compracao entre cidadeBase da Alocaocao.
 * 
 * @since 18/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public class CidadeBaseAlocacaoComparator implements Comparator<AlocacaoRow> {

    /**
     * Compara sigla da cidadeBase de cada AlocacaoRow.
     * @param obj1 AlocacaoRow1.
     * @param obj2 AlocacaoRow2.
     * @return comparacao integer.
     */
    public int compare(final AlocacaoRow obj1, final AlocacaoRow obj2) {
        String sgCidBase1 = obj1.getAlocacao().getCidadeBase().getSiglaCidadeBase();
        String sgCidBase2 = obj2.getAlocacao().getCidadeBase().getSiglaCidadeBase();
        
        return sgCidBase1.compareTo(sgCidBase2);
    }
    
}
