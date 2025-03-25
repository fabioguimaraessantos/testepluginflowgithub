package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.model.BmDn;

import java.util.List;

public class BmDnSelect extends Select<BmDn> {

    /**
     * @param bmDns - List of Bmdn
     */
    public BmDnSelect(List<BmDn> bmDns) {
        super(bmDns);
    }

    @Override
    protected Long objectValue(BmDn entity) {
        return entity != null ? entity.getCode() : null;
    }

    @Override
    protected String objectKey(BmDn entity) {
        return entity != null ? entity.getName() : null;
    }

    @Override
    protected BmDn entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     * @param code - Code to find BM DN
     * @return BmDnDTO - BM DN
     */
    private BmDn getEntityByCode(Long code) {

        if (code != null) {
            for (BmDn bmDn : entities) {
                if (code.equals(bmDn.getCode()))
                    return bmDn;
            }
        }

        return null;
    }
}