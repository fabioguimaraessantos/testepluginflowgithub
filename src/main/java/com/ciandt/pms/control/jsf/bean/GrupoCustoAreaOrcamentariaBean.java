package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;


/**
 * 
 * Define o BackingBean da entidade.
 *
 * @since 09/03/2015
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class GrupoCustoAreaOrcamentariaBean implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;
    
	private GrupoCustoAreaOrcamentaria to = new GrupoCustoAreaOrcamentaria();

	private String anoInicioVigencia = "";
	private String mesInicioVigencia = "";

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;
    private List<String> yearList = new ArrayList<String>();

    private Long gcaoToDeleteId;
    
    public void reset() {
    	this.to = new GrupoCustoAreaOrcamentaria();
    	this.anoInicioVigencia = "";
    	this.mesInicioVigencia = "";
    	this.monthList = Constants.MONTH_DAY_LIST;
    	this.yearList = new ArrayList<String>();
    	this.gcaoToDeleteId = null;
    }
    
    public void resetTo() {
    	this.to = new GrupoCustoAreaOrcamentaria();
    }

	public GrupoCustoAreaOrcamentaria getTo() {
		return to;
	}

	public void setTo(GrupoCustoAreaOrcamentaria to) {
		this.to = to;
	}

	public String getAnoInicioVigencia() {
		return anoInicioVigencia;
	}

	public void setAnoInicioVigencia(String anoInicioVigencia) {
		this.anoInicioVigencia = anoInicioVigencia;
	}

	public String getMesInicioVigencia() {
		return mesInicioVigencia;
	}

	public void setMesInicioVigencia(String mesInicioVigencia) {
		this.mesInicioVigencia = mesInicioVigencia;
	}

	public List<String> getMonthList() {
		return monthList;
	}

	public void setMonthList(List<String> monthList) {
		this.monthList = monthList;
	}

	public List<String> getYearList() {

        this.yearList = new ArrayList<String>();
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        Integer lastYear = year - 1;
		Integer nextYear = year + 1;
		Integer nextTwoYear = nextYear + 1;

        yearList.add(lastYear.toString());
        yearList.add(year.toString());
		yearList.add(nextYear.toString());
		yearList.add(nextTwoYear.toString());

        return yearList;
	}

	public void setYearList(List<String> yearList) {
		this.yearList = yearList;
	}

	public Long getGcaoToDeleteId() {
		return gcaoToDeleteId;
	}

	public void setGcaoToDeleteId(Long gcaoToDeleteId) {
		this.gcaoToDeleteId = gcaoToDeleteId;
	}
}
