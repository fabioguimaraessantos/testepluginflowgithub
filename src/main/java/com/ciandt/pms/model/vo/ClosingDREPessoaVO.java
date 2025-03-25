package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.*;

import java.util.List;

public class ClosingDREPessoaVO {

    private Long code;
    private String login;
    private Pessoa person;
    private List<AlocacaoPeriodo> allocationPeriods;
    private PessoaBillability billability;
    private PessoaGrupoCusto costCenter;
    private PessoaTipoContrato contractType;
    private List<AlocacaoPeriodoOh> overheads;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Pessoa getPerson() {
        return person;
    }

    public void setPerson(Pessoa person) {
        this.person = person;
    }

    public List<AlocacaoPeriodo> getAllocationPeriods() {
        return allocationPeriods;
    }

    public void setAllocationPeriods(List<AlocacaoPeriodo> allocationPeriods) {
        this.allocationPeriods = allocationPeriods;
    }

    public PessoaBillability getBillability() {
        return billability;
    }

    public void setBillability(PessoaBillability billability) {
        this.billability = billability;
    }

    public PessoaGrupoCusto getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(PessoaGrupoCusto costCenter) {
        this.costCenter = costCenter;
    }

    public PessoaTipoContrato getContractType() {
        return contractType;
    }

    public void setContractType(PessoaTipoContrato contractType) {
        this.contractType = contractType;
    }

    public List<AlocacaoPeriodoOh> getOverheads() {
        return overheads;
    }

    public void setOverheads(List<AlocacaoPeriodoOh> overheads) {
        this.overheads = overheads;
    }
}
