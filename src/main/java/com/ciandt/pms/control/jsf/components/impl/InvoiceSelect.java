package com.ciandt.pms.control.jsf.components.impl;

import java.util.List;

public class InvoiceSelect extends Select<ObjectedSelected> {

    /**
     * @param objectList - List of Invoices
     */
    public InvoiceSelect(List<ObjectedSelected> objectList) {
        super(objectList);
    }

    @Override
    protected Long objectValue(ObjectedSelected entity) {
        return entity != null ? entity.getCode() : null;
    }

    @Override
    protected String objectKey(ObjectedSelected entity) {
        return entity != null ? entity.getName() : null;
    }

    @Override
    protected ObjectedSelected entityByValue(Long code) {
        return getEntityByCode(code);
    }

    /**
     * @param code - Code to find Invoice
     * @return Invoice
     */
    private ObjectedSelected getEntityByCode(Long code) {

        if (code != null) {
            for (ObjectedSelected entity : entities) {
                if (code.equals(entity.getCode()))
                    return entity;
            }
        }
        return null;
    }
}

