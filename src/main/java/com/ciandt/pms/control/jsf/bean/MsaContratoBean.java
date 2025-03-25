package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.*;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MsaContratoBean implements Serializable {

    List<String> tipoServicoDescription = new ArrayList<String>();
    private Boolean isUpdate = Boolean.FALSE;
    private MsaContrato to = new MsaContrato();
    private MsaContrato filter = new MsaContrato();
    private MsaContrato toDelete = new MsaContrato();
    private Map<String, Long> tipoContratoMap;
    private List<String> tipoContratoComboList;
    private String tipoContratoSelected;
    private String moedaSelected;
    private String moedaSearchSelected;
    private Map<String, Long> tipoMontanteDespesaMap;
    private List<String> tipoMontanteDespesaList;
    private String tipoMontanteDespesaSelected;
    private Map<String, Long> statusContratosMap;
    private Map<String, Long> statusContratosSearchMap;
    private List<String> statusContratosList;
    private List<String> statusContratosSearchList;
    private String statusContratoSelected;
    private String statusContratoSearchSelected;
    private Map<String, Long> mapBasesDocumentoLegal = new LinkedHashMap<String, Long>();
    private List<String> basesDocumentoLegalSelected = new ArrayList<String>();
    private Map<String, Long> mapServiceTypeDocumentoLegal = new LinkedHashMap<String, Long>();
    private List<String> serviceTypesDocumentoLegalSelected = new ArrayList<String>();

    private String serviceTypesDescriptions;

    private List<MsaContrato> resultList = new ArrayList<MsaContrato>();

    private Boolean notAssignerOrValidStatus = false;

    private Boolean disableTotalContract;

    private Boolean tipoMontanteDespesaIsPercent = false;
    private Boolean tipoMontanteDespesaIsValue = false;
    private Integer currentPageId = Integer.valueOf(0);

    private MsaContrato toEdit;

    private List<String> monthsList = new ArrayList<String>();

    private String monthSelected;
    /**
     * Lista de CNPJ para a tabela.
     */
    private List<MsaContratoCnpj> cnpjList = new ArrayList<MsaContratoCnpj>();
    private MsaContratoCnpj msaContratoCnpjToDelete = new MsaContratoCnpj();
    @Transient
    private boolean isITSupport = false;

    @Transient
    private boolean isPriceTableEditorLogin = false;

    @Transient
    private boolean isPriceTableApproverLogin = false;

    public List<String> getTipoServicoDescription() {
        return tipoServicoDescription;
    }

    public void setTipoServicoDescription(List<String> tipoServicoDescription) {
        this.tipoServicoDescription = tipoServicoDescription;
    }

    public Boolean getUpdate() {
        return isUpdate;
    }

    public void setUpdate(Boolean update) {
        isUpdate = update;
    }

    public MsaContrato getToDelete() {
        return toDelete;
    }

    public void setToDelete(MsaContrato toDelete) {
        this.toDelete = toDelete;
    }

    public Integer getCurrentPageId() {
        return currentPageId;
    }

    public void setCurrentPageId(Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    public Boolean getNotAssignerOrValidStatus() {
        return notAssignerOrValidStatus;
    }

    public void setNotAssignerOrValidStatus(Boolean notAssignerOrValidStatus) {
        this.notAssignerOrValidStatus = notAssignerOrValidStatus;
    }

    public List<MsaContrato> getResultList() {
        return resultList;
    }

    public void setResultList(List<MsaContrato> resultList) {
        this.resultList = resultList;
    }

    public MsaContrato getTo() {
        return to;
    }

    public void setTo(MsaContrato to) {
        this.to = to;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getMoedaSelected() {
        return moedaSelected;
    }

    public void setMoedaSelected(String moedaSelected) {
        this.moedaSelected = moedaSelected;
    }

    public String getMoedaSearchSelected() {
        return moedaSearchSelected;
    }

    public void setMoedaSearchSelected(String moedaSearchSelected) {
        this.moedaSearchSelected = moedaSearchSelected;
    }

    public Map<String, Long> getTipoContratoMap() {
        return tipoContratoMap;
    }

    public void setTipoContratoMap(Map<String, Long> tipoContratoMap) {
        this.tipoContratoMap = tipoContratoMap;
    }

    public List<String> getTipoContratoComboList() {
        return tipoContratoComboList;
    }

    public void setTipoContratoComboList(List<String> tipoContratoComboList) {
        this.tipoContratoComboList = tipoContratoComboList;
    }

    public String getTipoContratoSelected() {
        return tipoContratoSelected;
    }

    public void setTipoContratoSelected(String tipoContratoSelected) {
        this.tipoContratoSelected = tipoContratoSelected;
    }

    public Map<String, Long> getTipoMontanteDespesaMap() {
        return tipoMontanteDespesaMap;
    }

    public void setTipoMontanteDespesaMap(Map<String, Long> tipoMontanteDespesaMap) {
        this.tipoMontanteDespesaMap = tipoMontanteDespesaMap;
    }

    public List<String> getTipoMontanteDespesaList() {
        return tipoMontanteDespesaList;
    }

    public void setTipoMontanteDespesaList(List<String> tipoMontanteDespesaList) {
        this.tipoMontanteDespesaList = tipoMontanteDespesaList;
    }

    public String getTipoMontanteDespesaSelected() {
        return tipoMontanteDespesaSelected;
    }

    public void setTipoMontanteDespesaSelected(String tipoMontanteDespesaSelected) {
        this.tipoMontanteDespesaSelected = tipoMontanteDespesaSelected;
    }

    public Map<String, Long> getStatusContratosMap() {
        return statusContratosMap;
    }

    public void setStatusContratosMap(Map<String, Long> statusContratosMap) {
        this.statusContratosMap = statusContratosMap;
    }

    public Map<String, Long> getStatusContratosSearchMap() {
        return statusContratosSearchMap;
    }

    public void setStatusContratosSearchMap(Map<String, Long> statusContratosSearchMap) {
        this.statusContratosSearchMap = statusContratosSearchMap;
    }

    public List<String> getStatusContratosList() {
        return statusContratosList;
    }

    public void setStatusContratosList(List<String> statusContratosList) {
        this.statusContratosList = statusContratosList;
    }

    public List<String> getStatusContratosSearchList() {
        return statusContratosSearchList;
    }

    public void setStatusContratosSearchList(List<String> statusContratosSearchList) {
        this.statusContratosSearchList = statusContratosSearchList;
    }

    public String getStatusContratoSelected() {
        return statusContratoSelected;
    }

    public void setStatusContratoSelected(String statusContratoSelected) {
        this.statusContratoSelected = statusContratoSelected;
    }

    public String getStatusContratoSearchSelected() {
        return statusContratoSearchSelected;
    }

    public void setStatusContratoSearchSelected(String statusContratoSearchSelected) {
        this.statusContratoSearchSelected = statusContratoSearchSelected;
    }

    public Map<String, Long> getMapBasesDocumentoLegal() {
        return mapBasesDocumentoLegal;
    }

    public void setMapBasesDocumentoLegal(Map<String, Long> mapBasesDocumentoLegal) {
        this.mapBasesDocumentoLegal = mapBasesDocumentoLegal;
    }

    public List<String> getBasesDocumentoLegalSelected() {
        return basesDocumentoLegalSelected;
    }

    public void setBasesDocumentoLegalSelected(List<String> basesDocumentoLegalSelected) {
        this.basesDocumentoLegalSelected = basesDocumentoLegalSelected;
    }

    public Map<String, Long> getMapServiceTypeDocumentoLegal() {
        return mapServiceTypeDocumentoLegal;
    }

    public void setMapServiceTypeDocumentoLegal(Map<String, Long> mapServiceTypeDocumentoLegal) {
        this.mapServiceTypeDocumentoLegal = mapServiceTypeDocumentoLegal;
    }

    public List<String> getServiceTypesDocumentoLegalSelected() {
        return serviceTypesDocumentoLegalSelected;
    }

    public void setServiceTypesDocumentoLegalSelected(List<String> serviceTypesDocumentoLegalSelected) {
        this.serviceTypesDocumentoLegalSelected = serviceTypesDocumentoLegalSelected;
    }

    public Boolean getTipoMontanteDespesaIsPercent() {
        return tipoMontanteDespesaIsPercent;
    }

    public void setTipoMontanteDespesaIsPercent(Boolean tipoMontanteDespesaIsPercent) {
        this.tipoMontanteDespesaIsPercent = tipoMontanteDespesaIsPercent;
    }

    public Boolean getTipoMontanteDespesaIsValue() {
        return tipoMontanteDespesaIsValue;
    }

    public void setTipoMontanteDespesaIsValue(Boolean tipoMontanteDespesaIsValue) {
        this.tipoMontanteDespesaIsValue = tipoMontanteDespesaIsValue;
    }

    public MsaContrato getToEdit() {
        return toEdit;
    }

    public void setToEdit(MsaContrato toEdit) {
        this.toEdit = toEdit;
    }

    public Boolean getDisableTotalContract() {
        if (this.to.getFte() != null && this.to.getFte()) {
            if (this.to.getHasLimit() != null && this.to.getHasLimit()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public void setDisableTotalContract(Boolean disableTotalContract) {
        this.disableTotalContract = disableTotalContract;
    }

    public String getServiceTypesDescriptions() {
        return serviceTypesDescriptions;
    }

    public void setServiceTypesDescriptions(String serviceTypesDescriptions) {
        this.serviceTypesDescriptions = serviceTypesDescriptions;
    }

    public List<String> getMonthsList() {
        return monthsList;
    }

    public void setMonthsList(List<String> monthsList) {
        this.monthsList = monthsList;
    }

    public String getMonthSelected() {
        return monthSelected;
    }

    public void setMonthSelected(String monthSelected) {
        this.monthSelected = monthSelected;
    }

    public MsaContrato getFilter() {
        return filter;
    }

    public void setFilter(final MsaContrato filter) {
        // Verifica se a Moeda é nula, se verdade instancia um nova.
        if (filter != null && filter.getMoeda() == null) {
            filter.setMoeda(new Moeda());
        }
        this.filter = filter;
    }

    public void reset() {
        this.resetTo();
        this.resetFilter();
        this.setIsUpdate(Boolean.FALSE);
        this.tipoContratoMap = new HashMap<String, Long>();
        this.tipoContratoComboList = new ArrayList<String>();
        this.tipoContratoSelected = "";
        this.tipoMontanteDespesaMap = new HashMap<String, Long>();
        this.tipoMontanteDespesaList = new ArrayList<String>();
        this.tipoMontanteDespesaSelected = "";
        this.statusContratosMap = new HashMap<String, Long>();
        this.statusContratosSearchMap = new HashMap<String, Long>();
        this.statusContratosList = new ArrayList<String>();
        this.statusContratosSearchList = new ArrayList<String>();
        this.statusContratoSelected = "";
        this.statusContratoSearchSelected = "";
        this.notAssignerOrValidStatus = false;
        this.moedaSelected = "";
        this.moedaSearchSelected = "";
        this.monthSelected = "";
        this.getMapBasesDocumentoLegal().clear();
        this.getMapServiceTypeDocumentoLegal().clear();
        this.resetPagination();
    }

    public void resetTo() {
        this.to = new MsaContrato();
        this.getServiceTypesDocumentoLegalSelected().clear();
        this.getBasesDocumentoLegalSelected().clear();
    }

    public void resetFilter() {
        this.setFilter(new MsaContrato());
    }

    public void resetPagination() {
        this.currentPageId = Integer.valueOf(0);
    }

    public List<MsaContratoCnpj> getCnpjList() {
        return cnpjList;
    }

    public void setCnpjList(List<MsaContratoCnpj> cnpjList) {
        this.cnpjList = cnpjList;
    }

    public MsaContratoCnpj getMsaContratoCnpjToDelete() {
        return msaContratoCnpjToDelete;
    }

    public void setMsaContratoCnpjToDelete(MsaContratoCnpj msaContratoCnpjToDelete) {
        this.msaContratoCnpjToDelete = msaContratoCnpjToDelete;
    }

    public boolean getIsITSupport() {
        return isITSupport;
    }

    public void setIsITSupport(boolean isITSupport) {
        this.isITSupport = isITSupport;
    }

    public boolean getIsPriceTableEditorLogin() {
        return isPriceTableEditorLogin;
    }
    public boolean getIsPriceTableApproverLogin() {
        return isPriceTableApproverLogin;
    }

    public void setIsPriceTableEditorLogin(boolean isPriceTableEditorLogin) {
        this.isPriceTableEditorLogin = isPriceTableEditorLogin;
    }

    public void setIsPriceTableApproverLogin(boolean priceTableApproverLogin) {
        this.isPriceTableApproverLogin = priceTableApproverLogin;
    }
}
