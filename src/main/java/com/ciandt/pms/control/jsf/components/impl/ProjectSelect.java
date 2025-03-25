package com.ciandt.pms.control.jsf.components.impl;

import java.util.List;

public class ProjectSelect extends Select<ObjectedSelected>{

    /**
     * @param objectList - List of Projects
     */
    public ProjectSelect(List<ObjectedSelected> objectList) {
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
     * @param code - Code to find Project
     * @return Project
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
