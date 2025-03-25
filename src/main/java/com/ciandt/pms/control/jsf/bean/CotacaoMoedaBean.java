package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.CotacaoMoeda;
import com.ciandt.pms.model.CotacaoMoedaMedia;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe CotacaoMoedaBean é utilizado como backingBean para a entidade
 * CotacaoMoeda.
 * 
 * @since 16/03/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CotacaoMoedaBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** filter do backingBean. */
    private CotacaoMoedaMedia filter = new CotacaoMoedaMedia();

    /** lista de resultados da pesquisa. */
    private List<CotacaoMoedaMedia> resultList = new ArrayList<CotacaoMoedaMedia>();

    /** Lista para o combobox Moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox MoedaDe. */
    private Map<String, Long> moedaDeMap = new HashMap<String, Long>();
    
    /** Lista para o combobox MoedaPara. */
    private Map<String, Long> moedaParaMap = new HashMap<String, Long>();

    /** dataDia inicio. */
    private Date dataDiaBeg = null;

    /** dataDia fim. */
    private Date dataDiaEnd = null;

    /** Indicador do padrão para exibição de valores de moeda. */
    private String patternCurrency = "";
    
    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Armazena o mes selecionado no combo. */
    private String month = "";
    
    /** Armazena o ano selecionado no combo. */
    private String year = "";

    private Date cotacaoCopyFrom = null;
    private Date cotacaoCopyUntil = null;
    private Date cotacaoDeleteFrom = null;
    private Date cotacaoDeleteUntil = null;

    private String password = null;
    private String ticket = null;

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
     * @return the dataDiaBeg
     */
    public Date getDataDiaBeg() {
        return dataDiaBeg;
    }

    /**
     * @param dataDiaBeg
     *            the dataDiaBeg to set
     */
    public void setDataDiaBeg(final Date dataDiaBeg) {
        this.dataDiaBeg = dataDiaBeg;
    }

    /**
     * @return the dataDiaEnd
     */
    public Date getDataDiaEnd() {
        return dataDiaEnd;
    }

    /**
     * @param dataDiaEnd
     *            the dataDiaEnd to set
     */
    public void setDataDiaEnd(final Date dataDiaEnd) {
        this.dataDiaEnd = dataDiaEnd;
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
    public Map<String, Long> getMoedaDeMap() {
        return moedaDeMap;
    }

    /**
     * @param moedaMap
     *            the moedaMap to set
     */
    public void setMoedaDeMap(final Map<String, Long> moedaDeMap) {
        this.moedaDeMap = moedaDeMap;
    }
    
    /**
     * @return the moedaMap
     */
    public Map<String, Long> getMoedaParaMap() {
    	return moedaParaMap;
    }
    
    /**
     * @param moedaMap
     *            the moedaMap to set
     */
    public void setMoedaParaMap(final Map<String, Long> moedaParaMap) {
    	this.moedaParaMap = moedaParaMap;
    }

    /**
     * @return the patternCurrency
     */
    public String getPatternCurrency() {
        return patternCurrency + " ";
    }

    /**
     * @param patternCurrency
     *            the patternCurrency to set
     */
    public void setPatternCurrency(final String patternCurrency) {
        this.patternCurrency = patternCurrency;
    }

    /**
     * @return the resultList
     */
    public List<CotacaoMoedaMedia> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<CotacaoMoedaMedia> resultList) {
        this.resultList = resultList;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final CotacaoMoedaMedia filter) {
        this.filter = filter;
    }

    /**
     * 
     * @return o filtro do TO
     */
    public CotacaoMoedaMedia getFilter() {
        if (filter != null) {
            if (filter.getMoedaDe() == null) {
                filter.setMoedaDe(new Moeda());
            }
            if (filter.getMoedaPara() == null) {
            	filter.setMoedaPara(new Moeda());
            }
        }

        return filter;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetResultList();
        resetPeriodFilter();
        resetCopy();
    }
    
    /**
     * Reseta o TO.
     */
    public void resetTo() {
        this.month = "";
        this.year = "";
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new CotacaoMoedaMedia();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<CotacaoMoedaMedia>();
    }

    /**
     * Reseta a data de vigencia.
     */
    public void resetPeriodFilter() {
        this.dataDiaBeg = null;
        this.dataDiaEnd = null;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(final String month) {
        this.month = month;
    }

    /**
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * @param year the year to set
     */
    public void setYear(final String year) {
        this.year = year;
    }

    /**
     * @return the year
     */
    public String getYear() {
        return year;
    }

    public void resetCopy() {
        this.cotacaoCopyFrom = null;
        this.cotacaoCopyUntil = null;
        this.cotacaoDeleteFrom = null;
        this.cotacaoDeleteUntil = null;
        this.password = null;
        this.ticket = null;
    }

    public Date getCotacaoCopyFrom() {
        return cotacaoCopyFrom;
    }

    public void setCotacaoCopyFrom(Date cotacaoCopyFrom) {
        this.cotacaoCopyFrom = cotacaoCopyFrom;
    }

    public Date getCotacaoCopyUntil() {
        return cotacaoCopyUntil;
    }

    public void setCotacaoCopyUntil(Date cotacaoCopyUntil) {
        this.cotacaoCopyUntil = cotacaoCopyUntil;
    }

    public Date getCotacaoDeleteFrom() {
        return cotacaoDeleteFrom;
    }

    public void setCotacaoDeleteFrom(Date cotacaoDeleteFrom) {
        this.cotacaoDeleteFrom = cotacaoDeleteFrom;
    }

    public Date getCotacaoDeleteUntil() {
        return cotacaoDeleteUntil;
    }

    public void setCotacaoDeleteUntil(Date cotacaoDeleteUntil) {
        this.cotacaoDeleteUntil = cotacaoDeleteUntil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}