package com.ciandt.pms.comparator;

import com.ciandt.pms.model.ContratoPratica;

import java.util.Comparator;


/**Classe para comparar calores de GrupoCusto e ordenar as listas dos mesmos.*/
public class ContratoPraticaNameComparator implements Comparator<ContratoPratica> {

    public int compare(final ContratoPratica gc1, final ContratoPratica gc2) {
        return gc1.getNomeContratoPratica().compareTo(gc2.getNomeContratoPratica());
    }

    @Override
    public Comparator<ContratoPratica> reversed() {
        return null;
    }

}
