package com.ciandt.pms.comparator;

import java.util.Comparator;

import com.ciandt.pms.model.ControleReajuste;


/**Classe para comparar calores de AjusteReceita e ordenar as listas dos mesmos.*/
public class ControleReajusteDataPrevistaComparator implements Comparator<ControleReajuste> {

    /**
     * @param cr1
     *            ControleReajuste a ser comparado.
     * 
     * @param cr2
     *            ControleReajuste a ser comparado.
     * 
     * @return inteiro com a ordem de datas de reajuste.
     */
    public int compare(final ControleReajuste cr1, final ControleReajuste cr2) {

		return cr1.getDataPrevista().compareTo(cr2.getDataPrevista());
    }

}
