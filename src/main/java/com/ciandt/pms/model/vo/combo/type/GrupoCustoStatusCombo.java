package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.GrupoCustoStatus;

import java.util.List;

public class GrupoCustoStatusCombo extends ComboFactory<GrupoCustoStatus, GrupoCustoStatus> {

    /**
     * @param t
     */
    public GrupoCustoStatusCombo(List<GrupoCustoStatus> t) {
        super(t);
    }

    /*
     * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
     */
    @Override
    public String getKey(GrupoCustoStatus input) {
        return input.getNomeStatus();
    }

    /*
     * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
     */
    @Override
    public GrupoCustoStatus getValue(GrupoCustoStatus input) {
        return input;
    }


}
