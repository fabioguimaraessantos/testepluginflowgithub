package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.*;

import com.ciandt.pms.integration.vo.OrganizacaoPipedrive;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.Cliente;

import javax.faces.model.SelectItem;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ClienteBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private Cliente to = new Cliente();

    /** filter do backingBean. */
    private Cliente filter = new Cliente();

    /** lista de resultados da pesquisa. */
    private List<Cliente> resultList = null;

    /** Lista para o combobox com os Cliente pais. */
    private List<String> clientePaiList = new ArrayList<String>();

    /** Lista para o combobox com os Cliente pais. */
    private Map<String, Long> clientePaiMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de Cliente pai. */
    private String clientePaiSelected = null;

    private List<OrganizacaoPipedrive> organizacaoPipedrives = new ArrayList<OrganizacaoPipedrive>();

    private List<String> organizacaoPipedriveList = new ArrayList<String>();

    private Map<Long, String> organizacaoPipedriveMap = new HashMap<Long, String>();

    /** Valor do filtro selecionado no combo de Cliente pai. */
    private String clientePaiSelectedFilter = null;

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0L);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista para o combobox com as sugestoes - indicadores Ativo/Inativo. */
    private List<String> suggestionsListInAtivo = new ArrayList<String>();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Indica se o deal possui relacionamento com outras entidades. */
    private Boolean hasRelationship = Boolean.FALSE;

    private Boolean hasValidAgent = Boolean.FALSE;

    /** Lista para o combobox com os Potenciais. */
    private List<String> potencialList = new ArrayList<String>();

    /** Lista para o combobox com os Potenciais. */
    private Map<String, Long> potencialMap = new HashMap<String, Long>();

    /** Valor selecionado no combo de Potenciais. */
    private String potencialSelected = null;
    
    /** Mapa de seguidores do cliente. */
    private Map<Long, String> mapFollow = new HashMap<Long, String>();

    private String organizationName = "";
    private String organizationSearchTerm = "";

    private Boolean showOrganizationSearch = false;

    private List<SelectItem> conditionsPayment;
    private String megaAgentName = "";

    public String getMegaAgentName() {
        return megaAgentName;
    }

    public void setMegaAgentName(String megaAgentName) {
        this.megaAgentName = megaAgentName;
    }

    public List<SelectItem> getConditionsPayment() {
        return conditionsPayment;
    }

    public void setConditionsPayment(List<SelectItem> conditionsPayment) {
        this.conditionsPayment = conditionsPayment;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationSearchTerm() {
        return organizationSearchTerm;
    }

    public void setOrganizationSearchTerm(String organizationSearchTerm) {
        this.organizationSearchTerm = organizationSearchTerm;
    }

    /**
     * @return the clientePaiSelected
     */
    public String getClientePaiSelected() {
        return clientePaiSelected;
    }

    /**
     * @param clientePaiSelected
     *            the clientePaiSelected to set
     */
    public void setClientePaiSelected(final String clientePaiSelected) {
        this.clientePaiSelected = clientePaiSelected;
    }

    /**
     * @return the clientePaiSelectedFilter
     */
    public String getClientePaiSelectedFilter() {
        return clientePaiSelectedFilter;
    }

    /**
     * @param clientePaiSelectedFilter
     *            the clientePaiSelectedFilter to set
     */
    public void setClientePaiSelectedFilter(
            final String clientePaiSelectedFilter) {
        this.clientePaiSelectedFilter = clientePaiSelectedFilter;
    }

    /**
     * @return the currentRowId
     */
    public Long getCurrentRowId() {
        return currentRowId;
    }

    /**
     * @param currentRowId
     *            the currentRowId to set
     */
    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param currentPageId
     *            the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the suggestionsListInAtivo
     */
    public List<String> getSuggestionsListInAtivo() {
        return suggestionsListInAtivo;
    }

    /**
     * @param suggestionsListInAtivo
     *            the suggestionsListInAtivo to set
     */
    public void setSuggestionsListInAtivo(
            final List<String> suggestionsListInAtivo) {
        this.suggestionsListInAtivo = suggestionsListInAtivo;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isUpdate
     *            the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        this.resetTo();
        this.resetFilter();
        this.resetResultList();
        this.resetSuggestionsListInAtivo();
        this.showOrganizationSearch = false;
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new Cliente();
        to.setTipoRegistroFiscal("C");
        this.clientePaiSelected = null;
        this.potencialSelected = null;
        this.setOrganizationName("");
        this.setMegaAgentName("");
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new Cliente();
        this.clientePaiSelectedFilter = null;
        this.potencialSelected = null;
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<Cliente>();
    }

    /**
     * Reseta a lista de sugestoes - indicadores Ativo/Inativo.
     */
    public void resetSuggestionsListInAtivo() {
        this.suggestionsListInAtivo = new ArrayList<String>();
    }

    /**
     * @return the to
     */
    public Cliente getTo() {
        return to;
    }

    /**
     * @param cliente
     *            the to to set
     */
    public void setTo(final Cliente cliente) {
        this.to = cliente;
    }

    /**
     * @return the filter
     */
    public Cliente getFilter() {
        return filter;
    }

    /**
     * @param cliente
     *            the filter to set
     */
    public void setFilter(final Cliente cliente) {
        this.filter = cliente;
    }

    /**
     * @return the resultList
     */
    public List<Cliente> getResultList() {
        return resultList;
    }

    /**
     * @param result
     *            the resultList to set
     */
    public void setResultList(final List<Cliente> result) {
        this.resultList = result;
    }

    /**
     * @return the clientePaiList
     */
    public List<String> getClientePaiList() {
        return clientePaiList;
    }

    /**
     * @param clientePaiList
     *            the clientePaiList to set
     */
    public void setClientePaiList(final List<String> clientePaiList) {
        this.clientePaiList = clientePaiList;
    }

    /**
     * @return the clientePaiMap
     */
    public Map<String, Long> getClientePaiMap() {
        return clientePaiMap;
    }

    /**
     * @param clientePaiMap
     *            the clientePaiMap to set
     */
    public void setClientePaiMap(final Map<String, Long> clientePaiMap) {
        this.clientePaiMap = clientePaiMap;
    }

    /**
     * @param hasRelationship
     *            the hasRelationship to set
     */
    public void setHasRelationship(final Boolean hasRelationship) {
        this.hasRelationship = hasRelationship;
    }

    public Boolean getHasValidAgent() {
        return hasValidAgent;
    }

    public void setHasValidAgent(Boolean hasValidAgent) {
        this.hasValidAgent = hasValidAgent;
    }

    /**
     * @return the hasRelationship
     */
    public Boolean getHasRelationship() {
        return hasRelationship;
    }

    /**
     * @return the potencialList
     */
    public List<String> getPotencialList() {
        return potencialList;
    }

    /**
     * @param potencialList
     *            the potencialList to set
     */
    public void setPotencialList(final List<String> potencialList) {
        this.potencialList = potencialList;
    }

    /**
     * @return the potencialMap
     */
    public Map<String, Long> getPotencialMap() {
        return potencialMap;
    }

    /**
     * @param potencialMap
     *            the potencialMap to set
     */
    public void setPotencialMap(final Map<String, Long> potencialMap) {
        this.potencialMap = potencialMap;
    }

    /**
     * @return the potencialSelected
     */
    public String getPotencialSelected() {
        return potencialSelected;
    }

    /**
     * @param potencialSelected
     *            the potencialSelected to set
     */
    public void setPotencialSelected(final String potencialSelected) {
        this.potencialSelected = potencialSelected;
    }

    /**
     * @return the mapFollow
     */
    public Map<Long, String> getMapFollow() {
        return mapFollow;
    }

    /**
     * @param mapFollow the mapFollow to set
     */
    public void setMapFollow(final Map<Long, String> mapFollow) {
        this.mapFollow = mapFollow;
    }

    public List<OrganizacaoPipedrive> getOrganizacaoPipedrives() {
        if (organizacaoPipedrives == null && organizacaoPipedrives.size() <= 0) {
            this.resetOrganizacaoPipedrives();
        }

        return organizacaoPipedrives;
    }

    public void setOrganizacaoPipedrives(List<OrganizacaoPipedrive> organizacaoPipedrives) {
        this.organizacaoPipedrives = organizacaoPipedrives;
    }

    public void resetOrganizacaoPipedrives() {
        organizacaoPipedrives = new ArrayList<OrganizacaoPipedrive>();
        organizacaoPipedrives.add(new OrganizacaoPipedrive(1L, "Eleanor"));
    }

    public Map<Long, String> getOrganizacaoPipedriveMap() {
        return organizacaoPipedriveMap;
    }

    public void setOrganizacaoPipedriveMap(Map<Long, String> organizacaoPipedriveMap) {
        this.organizacaoPipedriveMap = organizacaoPipedriveMap;
    }

    public Boolean getShowOrganizationSearch() {
        return showOrganizationSearch;
    }

    public void setShowOrganizationSearch(Boolean showOrganizationSearch) {
        this.showOrganizationSearch = showOrganizationSearch;
    }
}