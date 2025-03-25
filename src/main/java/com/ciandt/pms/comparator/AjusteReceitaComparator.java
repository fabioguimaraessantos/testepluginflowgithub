package com.ciandt.pms.comparator;

import java.util.Comparator;

import com.ciandt.pms.model.AjusteReceita;


/**Classe para comparar calores de AjusteReceita e ordenar as listas dos mesmos.*/
public class AjusteReceitaComparator implements Comparator<AjusteReceita> {

    /**
     * @param ajr1
     *            AjusteReceita a ser comparado.
     * 
     * @param ajr2
     *            AjusteReceita a ser comparado.
     * 
     * @return inteiro com a ordem de datas de ajuste.
     */
    public int compare(final AjusteReceita ajr1, final AjusteReceita ajr2) {

        if (ajr1.getReceitaDealFiscal().getDealFiscal().getNomeDealFiscal()
                .compareTo(
                        ajr2.getReceitaDealFiscal().getDealFiscal()
                                .getNomeDealFiscal()) == 0) {
            
            return ajr1.getDataMesAjuste().compareTo(ajr2.getDataMesAjuste());
            
        }
        
        return ajr1.getReceitaDealFiscal().getDealFiscal().getNomeDealFiscal()
        .compareTo(
                ajr2.getReceitaDealFiscal().getDealFiscal()
                        .getNomeDealFiscal());
    }

}
