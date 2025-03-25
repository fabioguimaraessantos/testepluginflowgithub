package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.*;

import com.ciandt.pms.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.ContratoPraticaCentroLucro;
import com.ciandt.pms.model.vo.NaturezaContratoPraticaCLRow;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ContratoPraticaCentroLucroBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties appConfig;

    /** to do backingBean. */
    private ContratoPraticaCentroLucro to = new ContratoPraticaCentroLucro();

    /** Lista de resultados. (Lista de ContratoPraticaCentroLucro das NaturezaCentroLucro opicionais.) */
    private List<ContratoPraticaCentroLucro> resultList = new ArrayList<ContratoPraticaCentroLucro>();

    /** Lista para o combobox com as PerfilVendido. */
    private List<String> centroLucroList = new ArrayList<String>();

    /** Lista para o combobox com as PerfilVendido. */
    private Map<String, Long> centroLucroMap = new HashMap<String, Long>();

    /** Lista para o combobox com as PerfilVendido. */
    private List<String> naturezacentroLucroList = new ArrayList<String>();

    /** Lista para o combobox com as PerfilVendido. */
    private Map<String, Long> naturezacentroLucroMap = new HashMap<String, Long>();

    /** Flag que verifica se é um update. */
    private Boolean isUpdate = Boolean.FALSE;

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();

    /** Propriedade do mes de inicio da vigencia. */
    private String mesInicioVigencia;

    private String  selectedMesInicioVigencia;

    /** Propriedade do ano de inicio da vigencia. */
    private String anoInicioVigencia;

    private String selectedAnoInicioVigencia;

    /** Nome da NaturezaCentroLucro selecionada no combobox. */
    private String nomeNatureza;
    
    /** Nome da NaturezaCentroLucro selecionada no combobox - Modal add. */
    private NaturezaContratoPraticaCLRow naturezaCPCLRow;
    
    /** Lista para o combobox com as PerfilVendido - Modal Add. */
    private List<String> centroLucroListModal = new ArrayList<String>();
    
    /** Lista para o combobox com as PerfilVendido - Modal Add. */
    private Map<String, Long> centroLucroMapModal = new HashMap<String, Long>();

    /**
     * @return the naturezaCPCLRow
     */
    public NaturezaContratoPraticaCLRow getNaturezaCPCLRow() {
        return naturezaCPCLRow;
    }

    /**
     * @param naturezaCPCLRow the naturezaCPCLRow to set
     */
    public void setNaturezaCPCLRow(final NaturezaContratoPraticaCLRow naturezaCPCLRow) {
        this.naturezaCPCLRow = naturezaCPCLRow;
    }

    /**
     * @return the centroLucroMapModal
     */
    public Map<String, Long> getCentroLucroMapModal() {
        return centroLucroMapModal;
    }

    /**
     * @param centroLucroMapModal the centroLucroMapModal to set
     */
    public void setCentroLucroMapModal(final Map<String, Long> centroLucroMapModal) {
        this.centroLucroMapModal = centroLucroMapModal;
    }

    /**
     * @return the centroLucroListModal
     */
    public List<String> getCentroLucroListModal() {
        return centroLucroListModal;
    }

    /**
     * @param centroLucroListModal the centroLucroListModal to set
     */
    public void setCentroLucroListModal(final List<String> centroLucroListModal) {
        this.centroLucroListModal = centroLucroListModal;
    }

    /**
     * @return the to
     */
    public ContratoPraticaCentroLucro getTo() {
        if (to == null) {
            to = new ContratoPraticaCentroLucro();
        }
        if (to.getCentroLucro() == null) {
            to.setCentroLucro(new CentroLucro());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ContratoPraticaCentroLucro to) {
        this.to = to;
    }

    /**
     * @return the resultList
     */
    public List<ContratoPraticaCentroLucro> getResultList() {
        return resultList;
    }


    public boolean isResultListGreatherThanOne() {
        return resultList != null && resultList.size() > 1;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<ContratoPraticaCentroLucro> resultList) {
        this.resultList = resultList;
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
     * @return the monthList
     */
    public List<String> getMonthList() {
        return monthList;
    }

    /**
     * @param monthList
     *            the monthList to set
     */
    public void setMonthList(final List<String> monthList) {
        this.monthList = monthList;
    }

    /**
     * @return the yearList
     */
    public List<String> getYearList() {

        int yearBegin = DateUtil.getPastYear(new Date());
        int range = Integer.parseInt(appConfig
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> localYearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            localYearList.add("" + i);
        }

        this.yearList = localYearList;

        return this.yearList;
    }

    /**
     * @param yearList
     *            the yearList to set
     */
    public void setYearList(final List<String> yearList) {
        this.yearList = yearList;
    }

    /**
     * @return the mesInicioVigencia
     */
    public String getMesInicioVigencia() {
        return mesInicioVigencia;
    }

    public String getSelectedMesInicioVigencia() {
        return  selectedMesInicioVigencia;
    }


    /**
     * @param mesInicioVigencia
     *            the mesInicioVigencia to set
     */
    public void setMesInicioVigencia(final String mesInicioVigencia) {
        this.mesInicioVigencia = mesInicioVigencia;
    }

    public void setSelectedMesInicioVigencia(final String selectedMesInicioVigencia) {
        this.selectedMesInicioVigencia = selectedMesInicioVigencia;
    }

    /**
     * @return the anoInicioVigencia
     */
    public String getAnoInicioVigencia() {
        return anoInicioVigencia;
    }

    public String getSelectedAnoInicioVigencia() {
        return selectedAnoInicioVigencia;
    }

    /**
     * @param anoInicioVigencia
     *            the anoInicioVigencia to set
     */
    public void setAnoInicioVigencia(final String anoInicioVigencia) {
        this.anoInicioVigencia = anoInicioVigencia;
    }

    public void setSelectedAnoInicioVigencia(final String selectedAnoInicioVigencia) {
        this.selectedAnoInicioVigencia = selectedAnoInicioVigencia;
    }

    /**
     * @param centroLucroList
     *            the centroLucroList to set
     */
    public void setCentroLucroList(final List<String> centroLucroList) {
        this.centroLucroList = centroLucroList;
    }

    /**
     * @return the centroLucroList
     */
    public List<String> getCentroLucroList() {
        return centroLucroList;
    }

    /**
     * @param centroLucroMap
     *            the centroLucroMap to set
     */
    public void setCentroLucroMap(final Map<String, Long> centroLucroMap) {
        this.centroLucroMap = centroLucroMap;
    }

    /**
     * @return the centroLucroMap
     */
    public Map<String, Long> getCentroLucroMap() {
        return centroLucroMap;
    }

    /**
     * Reseta o to do bean.
     */
    public void resetTo() {
        this.mesInicioVigencia = "";
        this.anoInicioVigencia = "";
        this.to = new ContratoPraticaCentroLucro();
        this.nomeNatureza = "";
        this.naturezaCPCLRow = new NaturezaContratoPraticaCLRow();
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.resetTo();
        this.resultList = new ArrayList<ContratoPraticaCentroLucro>();
        this.nomeNatureza = "";
        this.isUpdate = Boolean.valueOf(false);
        this.centroLucroList = new ArrayList<String>();
        this.centroLucroListModal = new ArrayList<String>();
    }

    /**
     * @param naturezacentroLucroList
     *            the naturezacentroLucroList to set
     */
    public void setNaturezacentroLucroList(
            final List<String> naturezacentroLucroList) {
        this.naturezacentroLucroList = naturezacentroLucroList;
    }

    /**
     * @return the naturezacentroLucroList
     */
    public List<String> getNaturezacentroLucroList() {
        return naturezacentroLucroList;
    }

    /**
     * @param naturezacentroLucroMap
     *            the naturezacentroLucroMap to set
     */
    public void setNaturezacentroLucroMap(
            final Map<String, Long> naturezacentroLucroMap) {
        this.naturezacentroLucroMap = naturezacentroLucroMap;
    }

    /**
     * @return the naturezacentroLucroMap
     */
    public Map<String, Long> getNaturezacentroLucroMap() {
        return naturezacentroLucroMap;
    }

    /**
     * @param nomeNatureza
     *            the nomeNatureza to set
     */
    public void setNomeNatureza(final String nomeNatureza) {
        this.nomeNatureza = nomeNatureza;
    }

    /**
     * @return the nomeNatureza
     */
    public String getNomeNatureza() {
        return nomeNatureza;
    }
}
