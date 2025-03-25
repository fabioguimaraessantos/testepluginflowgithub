package com.ciandt.pms.comparator;

import com.ciandt.pms.model.Empresa;

import java.util.Comparator;

public class EmpresaNomeEmpresaComparator implements Comparator<Empresa>{

    /**
     * Compara sigla da cidadeBase de cada AlocacaoRow.
     *
     * @param obj1 AlocacaoRow1.
     * @param obj2 AlocacaoRow2.
     * @return comparacao integer.
     */
    public int compare(final Empresa obj1, final Empresa obj2) {
        String nomeEmpresa1 = obj1.getNomeEmpresa();
        String nomeEmpresa2 = obj2.getNomeEmpresa();

        return nomeEmpresa1.compareTo(nomeEmpresa2);
    }
}