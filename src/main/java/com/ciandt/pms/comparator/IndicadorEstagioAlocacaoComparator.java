package com.ciandt.pms.comparator;

import java.util.Comparator;

import com.ciandt.pms.model.vo.AlocacaoRow;


/**
 * 
 * A classe IndicadorEstagioAlocacaoComparator proporciona as funcionalidades de
 * compartor para o indicador de estagio da alocacao.
 * 
 * @since 18/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public class IndicadorEstagioAlocacaoComparator implements Comparator<AlocacaoRow> {

    /**
     * Compara indicadorEstagio de cada AlocacaoRow.
     * @param obj1 AlocacaoRow1.
     * @param obj2 AlocacaoRow2.
     * @return comparacao integer.
     */
    public int compare(final AlocacaoRow obj1, final AlocacaoRow obj2) {
        String indEst1 = obj1.getAlocacao().getIndicadorEstagio();
        String indEst2 = obj2.getAlocacao().getIndicadorEstagio();
        
        return indEst1.compareTo(indEst2);
    }
}
