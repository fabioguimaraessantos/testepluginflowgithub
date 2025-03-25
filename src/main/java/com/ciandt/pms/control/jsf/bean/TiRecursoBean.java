package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.control.jsf.components.ISelect;
import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.TiRecurso;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.*;


/**
 * 
 *  Define o BackingBean da entidade.
 *
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TiRecursoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private TiRecurso to = new TiRecurso();

    /** CcustoTiRecurso do backingBean. */
    private CustoTiRecurso custoTiRecurso = new CustoTiRecurso();
    
    /** filter do backingBean. */
    private TiRecurso filter = new TiRecurso();

    /** lista de resultados da pesquisa. */
    private List<TiRecurso> resultList = new ArrayList<TiRecurso>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.FALSE;

    /** Indicador do modo em tempo de execucao - remove/view. */
    private Boolean isRemove = Boolean.FALSE;
    
    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdateCusto = Boolean.FALSE;
    
    /** Mes vigencia - inicio. */
    private String monthBeg = null;

    /** Ano vigencia - inicio. */
    private String yearBeg = null;

    /** Lista para o combobox com as Moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox com as Moeda. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();
    
    /** Data do HistoryLock. */
    private Date historyLockDate;

    /** List para o combobox de Empresa. */
    private ISelect<Empresa> companySelect;

    /** List para o combobox de Tipo Alocacao. */
    private List<String> tipoAlocacaoList;
    
    /**
     * @return the historyLockDate
     */
    public Date getHistoryLockDate() {
        return historyLockDate;
    }

    /**
     * @param historyLockDate the historyLockDate to set
     */
    public void setHistoryLockDate(final Date historyLockDate) {
        this.historyLockDate = historyLockDate;
    }

    /**
     * @return the moedaList
     */
    public List<String> getMoedaList() {
        return moedaList;
    }

    /**
     * @param moedaList
     *            the moedaList to set
     */
    public void setMoedaList(final List<String> moedaList) {
        this.moedaList = moedaList;
    }

    /**
     * @return the moedaMap
     */
    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    /**
     * @param moedaMap
     *            the moedaMap to set
     */
    public void setMoedaMap(final Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    /**
     * @return the isRemove
     */
    public Boolean getIsRemove() {
        return isRemove;
    }

    /**
     * @param isRemove
     *            the isRemove to set
     */
    public void setIsRemove(final Boolean isRemove) {
        this.isRemove = isRemove;
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
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        resetValidityDate();
        resetCustoTiRecurso();
        resetList();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new TiRecurso();
        this.to.setIndicadorTipoAlocacao("SU");
    }

    /**
     * Reseta a data de vigencia.
     */
    public void resetValidityDate() {
        this.setMonthBeg("");
        this.setYearBeg("");
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new TiRecurso();
    }
    
    /**
     * Reseta o TO CustoTiRecurso.
     */
    public void resetCustoTiRecurso() {
        this.custoTiRecurso = new CustoTiRecurso();
        this.resetValidityDate();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<TiRecurso>();
    }

    /**
     * Reseta as listas de Combo
     */
    public void resetList(){
        this.companySelect = null;
        this.tipoAlocacaoList = new ArrayList<>();
    }

    /**
     * @return the to
     */
    public TiRecurso getTo() {
        if (to == null) {
            to = new TiRecurso();
        }
        if (to.getMoeda() == null) {
            to.setMoeda(new Moeda());
        }
        if (to.getEmpresa() == null) {
            to.setEmpresa(new Empresa());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final TiRecurso to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public TiRecurso getFilter() {
        if (filter == null) {
            filter = new TiRecurso();
        }
        if (filter.getMoeda() == null) {
            filter.setMoeda(new Moeda());
        }
        if (filter.getEmpresa() == null) {
            filter.setEmpresa(new Empresa());
        }
        
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final TiRecurso filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<TiRecurso> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<TiRecurso> resultList) {
        this.resultList = resultList;
    }

    /**
     * @param monthBeg the monthBeg to set
     */
    public void setMonthBeg(final String monthBeg) {
        this.monthBeg = monthBeg;
    }

    /**
     * @return the monthBeg
     */
    public String getMonthBeg() {
        return monthBeg;
    }

    /**
     * @param yearBeg the yearBeg to set
     */
    public void setYearBeg(final String yearBeg) {
        this.yearBeg = yearBeg;
    }

    /**
     * @return the yearBeg
     */
    public String getYearBeg() {
        return yearBeg;
    }

    /**
     * @param custoTiRecurso the custoTiRecurso to set
     */
    public void setCustoTiRecurso(final CustoTiRecurso custoTiRecurso) {
        this.custoTiRecurso = custoTiRecurso;
    }

    /**
     * @return the custoTiRecurso
     */
    public CustoTiRecurso getCustoTiRecurso() {
        return custoTiRecurso;
    }

    /**
     * @param isUpdateCusto the isUpdateCusto to set
     */
    public void setIsUpdateCusto(final Boolean isUpdateCusto) {
        this.isUpdateCusto = isUpdateCusto;
    }

    /**
     * @return the isUpdateCusto
     */
    public Boolean getIsUpdateCusto() {
        return isUpdateCusto;
    }

    /**
     * @return the companySelect
     */
    public ISelect<Empresa> getCompanySelect() {
        return companySelect;
    }

    /**
     * @param companySelect the companySelect to set
     */
    public void setCompanySelect(ISelect<Empresa> companySelect) {
        this.companySelect = companySelect;
    }

    /**
     * @return the tipoAlocacaoList
     */
    public List<String> getTipoAlocacaoList() {
        return tipoAlocacaoList;
    }

    /**
     * @param tipoAlocacaoList the tipoAlocacaoList to set
     */
    public void setTipoAlocacaoList(List<String> tipoAlocacaoList) {
        this.tipoAlocacaoList = tipoAlocacaoList;
    }
}