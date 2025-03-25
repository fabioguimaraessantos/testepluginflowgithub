package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcDespesaCl;
import com.ciandt.pms.model.OrcDespesaGc;
import com.ciandt.pms.model.OrcamentoDespesa;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.*;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class OrcDespesaCloneBean implements Serializable {

    /** The serial version ID. */
    private static final long serialVersionUID = 121312321L;

    private OrcamentoDespesa to = new OrcamentoDespesa();

    private OrcDespesaGc toGc;

    private OrcDespesaCl toCl;

    private List<SelectItem> contratoPraticaPickList = new ArrayList<SelectItem>();

    private Long[] grantedCLobPickList;

    private List<String> moedaStringList = new ArrayList<String>();

    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    private String moedaSelecionda;

    private boolean isOrcDespesaClReembolsavel = Boolean.FALSE;

    private boolean isOrcDespesaExtra = false;

    private String nomeOrcamentoDespesaInplaceInput;

    private List<String> listaTipoFaturaCombobox = new ArrayList<String>();

    private Map<String, String> tipoFaturaMap = new HashMap<String, String>();

    private String tipoFaturaSelecionada;

    private Boolean orcDespesaWhiteListOnly = false;

    private Long orcDespesaToCloneCode;
    private String orcDespesaToCloneName;

    private Boolean isCopyWhiteList = true;
    private Boolean isCopyWhiteListDelegation = true;
    private Boolean isCopyFollow = true;

    private Date dataInicio;
    private Date dataFim;

    public void resetTo() {
        this.to = new OrcamentoDespesa();
        this.toCl = null;
        this.toGc = null;
    }

    public void reset() {
        this.resetTo();
        this.contratoPraticaPickList = new ArrayList<SelectItem>();
        this.moedaStringList = new ArrayList<String>();
        this.moedaMap = new HashMap<String, Long>();
        this.moedaSelecionda = "";
        this.grantedCLobPickList = null;
        this.isOrcDespesaClReembolsavel = false;
        this.listaTipoFaturaCombobox = new ArrayList<String>();
        this.tipoFaturaMap = new HashMap<String, String>();
        this.tipoFaturaSelecionada = "";
        this.isOrcDespesaExtra = false;
        this.isCopyFollow = true;
        this.isCopyWhiteList = true;
        this.isCopyWhiteListDelegation = true;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public OrcamentoDespesa getTo() {
        return to;
    }

    public void setTo(OrcamentoDespesa to) {
        this.to = to;
    }

    public OrcDespesaGc getToGc() {
        return toGc;
    }

    public void setToGc(OrcDespesaGc toGc) {
        this.toGc = toGc;
    }

    public OrcDespesaCl getToCl() {
        return toCl;
    }

    public void setToCl(OrcDespesaCl toCl) {
        this.toCl = toCl;
    }

    public List<SelectItem> getContratoPraticaPickList() {
        return contratoPraticaPickList;
    }

    public void setContratoPraticaPickList(List<SelectItem> contratoPraticaPickList) {
        this.contratoPraticaPickList = contratoPraticaPickList;
    }

    public Long[] getGrantedCLobPickList() {
        return grantedCLobPickList;
    }

    public void setGrantedCLobPickList(Long[] grantedCLobPickList) {
        this.grantedCLobPickList = grantedCLobPickList;
    }

    public List<String> getMoedaStringList() {
        return moedaStringList;
    }

    public void setMoedaStringList(List<String> moedaStringList) {
        this.moedaStringList = moedaStringList;
    }

    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    public void setMoedaMap(Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    public String getMoedaSelecionda() {
        return moedaSelecionda;
    }

    public void setMoedaSelecionda(String moedaSelecionda) {
        this.moedaSelecionda = moedaSelecionda;
    }

    public boolean getIsOrcDespesaClReembolsavel() {
        return isOrcDespesaClReembolsavel;
    }

    public void setIsOrcDespesaClReembolsavel(boolean orcDespesaClReembolsavel) {
        isOrcDespesaClReembolsavel = orcDespesaClReembolsavel;
    }

    public boolean getIsOrcDespesaExtra() {
        return isOrcDespesaExtra;
    }

    public void setIsOrcDespesaExtra(boolean orcDespesaExtra) {
        isOrcDespesaExtra = orcDespesaExtra;
    }

    public String getNomeOrcamentoDespesaInplaceInput() {
        return nomeOrcamentoDespesaInplaceInput;
    }

    public void setNomeOrcamentoDespesaInplaceInput(String nomeOrcamentoDespesaInplaceInput) {
        this.nomeOrcamentoDespesaInplaceInput = nomeOrcamentoDespesaInplaceInput;
    }

    public List<String> getListaTipoFaturaCombobox() {
        return listaTipoFaturaCombobox;
    }

    public void setListaTipoFaturaCombobox(List<String> listaTipoFaturaCombobox) {
        this.listaTipoFaturaCombobox = listaTipoFaturaCombobox;
    }

    public Map<String, String> getTipoFaturaMap() {
        return tipoFaturaMap;
    }

    public void setTipoFaturaMap(Map<String, String> tipoFaturaMap) {
        this.tipoFaturaMap = tipoFaturaMap;
    }

    public String getTipoFaturaSelecionada() {
        return tipoFaturaSelecionada;
    }

    public void setTipoFaturaSelecionada(String tipoFaturaSelecionada) {
        this.tipoFaturaSelecionada = tipoFaturaSelecionada;
    }

    public Boolean getOrcDespesaWhiteListOnly() {
        return orcDespesaWhiteListOnly;
    }

    public void setOrcDespesaWhiteListOnly(Boolean orcDespesaWhiteListOnly) {
        this.orcDespesaWhiteListOnly = orcDespesaWhiteListOnly;
    }

    public Long getOrcDespesaToCloneCode() {
        return orcDespesaToCloneCode;
    }

    public void setOrcDespesaToCloneCode(Long orcDespesaToCloneCode) {
        this.orcDespesaToCloneCode = orcDespesaToCloneCode;
    }

    public Boolean getIsCopyWhiteList() {
        return isCopyWhiteList;
    }

    public void setIsCopyWhiteList(Boolean copyWhiteList) {
        isCopyWhiteList = copyWhiteList;
    }

    public Boolean getIsCopyWhiteListDelegation() {
        return isCopyWhiteListDelegation;
    }

    public void setIsCopyWhiteListDelegation(Boolean copyWhiteListDelegation) {
        isCopyWhiteListDelegation = copyWhiteListDelegation;
    }

    public Boolean getIsCopyFollow() {
        return isCopyFollow;
    }

    public void setIsCopyFollow(Boolean copyFollow) {
        isCopyFollow = copyFollow;
    }

    public String getOrcDespesaToCloneName() {
        return orcDespesaToCloneName;
    }

    public void setOrcDespesaToCloneName(String orcDespesaToCloneName) {
        this.orcDespesaToCloneName = orcDespesaToCloneName;
    }

    public Boolean getIsOrcDespesaCl() {
        return this.toCl != null;
    }
}
