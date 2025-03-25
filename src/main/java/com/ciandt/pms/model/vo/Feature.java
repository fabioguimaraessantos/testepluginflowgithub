package com.ciandt.pms.model.vo;

import java.util.List;

public class Feature {

    private Long code;
    private String name;
    private String acronym;
    private Long applicationCode;
    private List<Operation> operations;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Long getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(Long applicationCode) {
        this.applicationCode = applicationCode;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}
