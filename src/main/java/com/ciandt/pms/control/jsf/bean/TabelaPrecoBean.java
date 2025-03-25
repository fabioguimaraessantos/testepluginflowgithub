package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ciandt.pms.control.jsf.components.ISelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.TabelaPreco;


/**
 * Define o BackingBean da entidade TabelaPreco.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TabelaPrecoBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** to do backingBean. */
    private TabelaPreco to = new TabelaPreco();

    /** Lista de resultados. */
    private List<TabelaPreco> resultList = new ArrayList<TabelaPreco>();

    /** Flag que verifica se é um update. */
    private Boolean isUpdate = Boolean.FALSE;

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();

    /** Propriedade do mes de inicio da vigencia. */
    private String mesInicioVigencia;

    /** Propriedade do ano de inicio da vigencia. */
    private String anoInicioVigencia;
    
    /** Percentual de margem do ContratoPratica. */
    private BigDecimal percentualMargem;
    
    /** Flag para edição da data de tabelaPreco. */
    private Boolean flagEditData = Boolean.valueOf(false);
    
    /** Flag para voltar para a aba de tabelaPreco. */
    private String abaTabelaPreco;
    
    /** Map para combobox de moeda. */
    private Map<String, Long> mapMoeda = new HashMap<String, Long>();
    
    /** Lista de moedas para combobox. */
    private List<String> currencyComboList = new ArrayList<String>();
    
    /** String para moeda que vem da view. */
    private String moeda;

    /** Flag para selecionar a aba correta. */
    private String selectedTab;

    /** List para o combobox com Price Table Status. */
    private ISelect priceTableStatusSelect;

    /**
     * List para o combobox com Price Table Status.
     */
    private List<String> priceTableStatusList = new ArrayList<String>();


    /** String para descrição da não aprovação da price table. */
    private String notApproveReasonsDescription;


    /**
     * List para o combobox com Price Table Status.
     */
    public List<String> getPriceTableStatusList() {
        return priceTableStatusList;
    }


    /**
     * @return the percentualMargem
     */
    public BigDecimal getPercentualMargem() {
        return percentualMargem;
    }

    /**
     * @param percentualMargem the percentualMargem to set
     */
    public void setPercentualMargem(final BigDecimal percentualMargem) {
        this.percentualMargem = percentualMargem;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final TabelaPreco to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public TabelaPreco getTo() {
        return to;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<TabelaPreco> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the resultList
     */
    public List<TabelaPreco> getResultList() {
        return resultList;
    }

    /**
     * @param isUpdate
     *            the isUpdate to set
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * @return the isUpdate
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * @return the monthList
     */
    public List<String> getMonthList() {
        return monthList;
    }

    /**
     * @param yearList
     *            the yearList to set
     */
    public void setYearList(final List<String> yearList) {
        this.yearList = yearList;
    }

    /**
     * @return the yearList
     */
    public List<String> getYearList() {

        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> localYearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            localYearList.add("" + i);
        }

        this.yearList = localYearList;
        
        return this.yearList;

    }

    /**
     * @param mesInicioVigencia
     *            the mesInicioVigencia to set
     */
    public void setMesInicioVigencia(final String mesInicioVigencia) {
        this.mesInicioVigencia = mesInicioVigencia;
    }

    /**
     * @return the mesInicioVigencia
     */
    public String getMesInicioVigencia() {
        return mesInicioVigencia;
    }

    /**
     * @param anoInicioVigencia
     *            the anoInicioVigencia to set
     */
    public void setAnoInicioVigencia(final String anoInicioVigencia) {
        this.anoInicioVigencia = anoInicioVigencia;
    }

    /**
     * @return the anoInicioVigencia
     */
    public String getAnoInicioVigencia() {
        return anoInicioVigencia;
    }

    /**
     * Reseta o to do bean.
     */
    public void resetTo() {
        this.mesInicioVigencia = "";
        this.anoInicioVigencia = "";
        this.percentualMargem = null;
        this.moeda = "";
        this.flagEditData = Boolean.valueOf(false);
        this.to = new TabelaPreco();
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.resetTo();
        this.isUpdate = Boolean.valueOf(false);
    }

    /**
     * @return the flagEditData
     */
    public Boolean getFlagEditData() {
        return flagEditData;
    }

    /**
     * @param flagEditData the flagEditData to set
     */
    public void setFlagEditData(final Boolean flagEditData) {
        this.flagEditData = flagEditData;
    }

    /**
     * @return the abaTabelaPreco
     */
    public String getAbaTabelaPreco() {
        return abaTabelaPreco;
    }

    /**
     * @param abaTabelaPreco the abaTabelaPreco to set
     */
    public void setAbaTabelaPreco(final String abaTabelaPreco) {
        this.abaTabelaPreco = abaTabelaPreco;
    }

    /**
     * get mapmoeda.
     * @return mapmoeda.
     */
	public Map<String, Long> getMapMoeda() {
		return mapMoeda;
	}

	/**
	 * set mapMoeda
	 * @param mapMoeda map moeda.
	 */
	public void setMapMoeda(final Map<String, Long> mapMoeda) {
		this.mapMoeda = mapMoeda;
	}
	
	
	/**
	 * getCurrencyComboList.
	 * @return currencycombolist
	 */
	public List<String> getCurrencyComboList() {
		return currencyComboList;
	}

	/**
	 * set currencycombolist.
	 * @param currencyComboList currencycombolist
	 */
	public void setCurrencyComboList(final List<String> currencyComboList) {
		this.currencyComboList = currencyComboList;
	}

	/**
	 * getmoeda.
	 * @return moeda.
	 */
	public String getMoeda() {
		return moeda;
	}

	/**
	 * set moeda.
	 * @param moeda moeda.
	 */
	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}


    public void setPriceTableStatusList(List<String> priceTableStatusList) {
        this.priceTableStatusList = priceTableStatusList;
    }

    public ISelect getPriceTableStatusSelect() {
        return priceTableStatusSelect;
    }
    public void setPriceTableStatusSelect(ISelect priceTableStatusSelect) {
        this.priceTableStatusSelect = priceTableStatusSelect;
    }

    public String getNotApproveReasonsDescription() {
        return notApproveReasonsDescription;
    }

    public void setNotApproveReasonsDescription(String notApproveReasonsDescription) {
        this.notApproveReasonsDescription = notApproveReasonsDescription;
    }
    public String getSelectedTab() {
        return selectedTab;
    }
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }
}
