package com.ciandt.pms.comparator;

import java.util.Comparator;

import com.ciandt.pms.model.vo.AlocacaoRow;


/**
 * 
 * A classe PerfilVendidoComparator proporciona as funcionalidades de comparacao para AlocacaoRow e perfilVendido.
 * 
 * @since 18/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public class PerfilVendidoAlocacaoComparator implements Comparator<AlocacaoRow> {

    /**
     * Compare dos nomes do perfilVendido da alocacao.
     * @param obj1 AlocacaoRow1.
     * @param obj2 AlocacaoRow2.
     * @return compracao entre inteiros.
     */
    public int compare(final AlocacaoRow obj1, final AlocacaoRow obj2) {
        
        String nmPerVen1 = obj1.getAlocacao().getPerfilVendido().getNomePerfilVendido();
        String nmPerVen2 = obj2.getAlocacao().getPerfilVendido().getNomePerfilVendido();
              
        return nmPerVen1.compareTo(nmPerVen2);
    }
    
}
