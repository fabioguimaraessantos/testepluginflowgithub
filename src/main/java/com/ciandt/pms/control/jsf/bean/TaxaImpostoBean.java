package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;
import com.ciandt.pms.util.DateUtil;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TaxaImpostoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;
    
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;
    
    /** to do backingBean. */
    private TaxaImposto to = new TaxaImposto();
    
    /** filtro para busca. */
    private TaxaImposto filter = new TaxaImposto();
    
    /** Lista para o combobox com as empresas. */
    private List<String> empresaList = new ArrayList<String>();

    /** Lista para o combobox com as empresas. */
    private Map<String, Long> empresaMap = new HashMap<String, Long>();

    /** Lista para o combobox com os tipos de serviço. */
    private List<String> tipoServicoList = new ArrayList<String>();

    /** Lista para o combobox com os tipos de serviço. */
    private Map<String, Long> tipoServicoMap = new HashMap<String, Long>();
    
    /** Lista dos possiveis valores de anos. */
    private List<String> validityYearList = new ArrayList<String>();
    
    /** Lista de resultados da busca. */
    private List<TaxaImposto> resultList = new ArrayList<TaxaImposto>();    
    
    /** Propriedade do mes de inicio da vigencia. */
    private String mesInicioVigencia;

    /** Propriedade do ano de inicio da vigencia. */
    private String anoInicioVigencia;
    
    /** Propriedade do mes de inicio da vigencia p ser utilizado no filtro de busca. */
    private String mesInicioVigenciaFiltro;

    /** Propriedade do ano de inicio da vigencia p ser utilizado no filtro de busca. */
    private String anoInicioVigenciaFiltro;
    
    /** Propriedade do mes de fim da vigencia (usada somente para busca). */
    private String mesFimVigencia;

    /** Propriedade do ano de fim da vigencia (usada somente para busca). */
    private String anoFimVigencia;
    
    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);
    
    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);
    
    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.FALSE;
    
    /** Data do HistoryLock. */
    private Date historyLockDate;
    
    /**
     * Pega o TO.
     * 
     * @return to TaxaImposto
     */
    public TaxaImposto getTo() {
        if (to == null) {
            to = new TaxaImposto();
        }
        
        if (to.getEmpresa() == null) {
            to.setEmpresa(new Empresa());
        }
        
        if (to.getTipoServico() == null) {
            to.setTipoServico(new TipoServico());
        }
        
        return to;
    }
    
    /**
     * Seta o TO.
     * 
     * @param to
     *            Taxa Imposto
     */
    public void setTo(final TaxaImposto to) {
        this.to = to;
        // recupera string referente a mes e ano de inicio da vigencia da entidade
        mesInicioVigencia = DateUtil.getMonthString(to.getDataInicio());
        anoInicioVigencia = DateUtil.getYearString(to.getDataInicio());
    }
    
    /**
     * Pega a lista de empresas do o combo.
     * 
     * @return a lista
     */    
    public List<String> getEmpresaList() {
        return empresaList;
    }
    
    /**
     * Seta a lista de empresas para o combo.
     * 
     * @param empresaList
     *            the empresasList to set
     */    
    public void setEmpresaList(final List<String> empresaList) {
        this.empresaList = empresaList;
    }
    
    /**
     * Pega o mapa de empresas do combo.
     * 
     * @return o mapa
     */    
    public Map<String, Long> getEmpresaMap() {
        return empresaMap;
    }

    /**
     * Seta o mapa de empresas para o combo.
     * 
     * @param empresasMap
     *            the empresasMap to set
     */
    public void setEmpresaMap(final Map<String, Long> empresasMap) {
        this.empresaMap = empresasMap;
    }   
    
    /**
     * Pega a lista de tipos de serviço do combo.
     * 
     * @return o mapa
     */
    public List<String> getTipoServicoList() {
        return tipoServicoList;
    }

    /**
     * Seta a lista de tipos de serviço para o combo.
     * 
     * @param tipoServicoList
     *            the empresasMap to set
     */
    public void setTipoServicoList(final List<String> tipoServicoList) {
        this.tipoServicoList = tipoServicoList;
    }
    
    /**
     * Pega o mapa de tipo de serviços do combo.
     * 
     * @return o mapa
     */
    public Map<String, Long> getTipoServicoMap() {
        return tipoServicoMap;
    }
    
    /**
     * Pega o mapa de tipos de serviço dop combo.
     * 
     * @param tipoServicoMap
     *            the tipoServicoMap to set
     */
    public void setTipoServicoMap(final Map<String, Long> tipoServicoMap) {
        this.tipoServicoMap = tipoServicoMap;
    }
    
    /**
     * @return the validityMonthList
     */
    public List<String> getValidityMonthList() {
        return Constants.MONTH_DAY_LIST;
    }

    /**
     * @return lista de anos da vigencia
     */
    public List<String> getValidityYearList() {

        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        validityYearList = yearList;

        return validityYearList;
    }
    
    /**
     * @return the mesInicioVigencia
     */
    public String getMesInicioVigencia() {
        return mesInicioVigencia;
    }

    /**
     * @param mesInicioVigencia
     *            the mesInicioVigencia to set
     */
    public void setMesInicioVigencia(final String mesInicioVigencia) {
        this.mesInicioVigencia = mesInicioVigencia;
    }
    
    /**
     * @return the anoInicioVigencia
     */
    public String getAnoInicioVigencia() {
        return anoInicioVigencia;
    }

    /**
     * @param mesInicioVigencia
     *            the mesInicioVigencia to set
     */
    public void setAnoInicioVigencia(final String mesInicioVigencia) {
        this.anoInicioVigencia = mesInicioVigencia;
    }
    
    /**
     * @return the anoFimVigencia
     */
    public String getAnoFimVigencia() {
        return anoFimVigencia;
    }

    /**
     * @param anoFimVigencia the anoFimVigencia to set
     */
    public void setAnoFimVigencia(final String anoFimVigencia) {
        this.anoFimVigencia = anoFimVigencia;
    }    

    /**
     * @return the mesFimVigencia
     */
    public String getMesFimVigencia() {
        return mesFimVigencia;
    }

    /**
     * @param mesFimVigencia the mesFimVigencia to set
     */
    public void setMesFimVigencia(final String mesFimVigencia) {
        this.mesFimVigencia = mesFimVigencia;
    }

    /**
     * @param validityYearList the validityYearList to set
     */
    public void setValidityYearList(final List<String> validityYearList) {
        this.validityYearList = validityYearList;
    }

    /**
     * @return the resultList
     */
    public List<TaxaImposto> getResultList() {
        return resultList;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(final List<TaxaImposto> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param currentPageId the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the filter
     */
    public TaxaImposto getFilter() {
        if (filter == null) {
            filter = new TaxaImposto();
        }
        
        if (filter.getEmpresa() == null) {
            filter.setEmpresa(new Empresa());
        }
        
        if (filter.getTipoServico() == null) {
            filter.setTipoServico(new TipoServico());
        }
        
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(final TaxaImposto filter) {
        this.filter = filter;
    }

    /**
     * @return the currentRowId
     */
    public Long getCurrentRowId() {
        return currentRowId;
    }

    /**
     * @param currentRowId the currentRowId to set
     */
    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @param isUpdate the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * @return the mesInicioVigenciaFiltro
     */
    public String getMesInicioVigenciaFiltro() {
        return mesInicioVigenciaFiltro;
    }

    /**
     * @param mesInicioVigenciaFiltro the mesInicioVigenciaFiltro to set
     */
    public void setMesInicioVigenciaFiltro(final String mesInicioVigenciaFiltro) {
        this.mesInicioVigenciaFiltro = mesInicioVigenciaFiltro;
    }

    /**
     * @return the anoInicioVigenciaFiltro
     */
    public String getAnoInicioVigenciaFiltro() {
        return anoInicioVigenciaFiltro;
    }

    /**
     * @param anoInicioVigenciaFiltro the anoInicioVigenciaFiltro to set
     */
    public void setAnoInicioVigenciaFiltro(final String anoInicioVigenciaFiltro) {
        this.anoInicioVigenciaFiltro = anoInicioVigenciaFiltro;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {        
        resetTo();
        resetFilter();
        resetResultList();
        
        mesInicioVigencia = "";
        anoInicioVigencia = "";
        mesFimVigencia = "";
        anoFimVigencia = "";  
        
        currentPageId = Integer.valueOf(0);
        
        isUpdate = Boolean.FALSE;
    }
    
    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new TaxaImposto();
    }
    
    /**
     * Reseta o filtro.
     */
    public void resetFilter() {
        this.filter = new TaxaImposto();
    }

    /**
     * Reseta a lista de resultados da busca.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<TaxaImposto>();        
    }

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
}