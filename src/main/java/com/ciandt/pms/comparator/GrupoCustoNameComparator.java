package com.ciandt.pms.comparator;

import com.ciandt.pms.model.GrupoCusto;

import java.util.Comparator;


/**Classe para comparar calores de GrupoCusto e ordenar as listas dos mesmos.*/
public class GrupoCustoNameComparator implements Comparator<GrupoCusto> {

    public int compare(final GrupoCusto gc1, final GrupoCusto gc2) {
        return gc1.getNomeGrupoCusto().compareTo(gc2.getNomeGrupoCusto());
    }

}
