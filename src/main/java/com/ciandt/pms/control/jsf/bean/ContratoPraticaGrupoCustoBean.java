package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ContratoPraticaGrupoCusto;


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
public class ContratoPraticaGrupoCustoBean implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;
    
	private ContratoPraticaGrupoCusto to = new ContratoPraticaGrupoCusto();

	private String anoInicioVigencia = "";
	private String mesInicioVigencia = "";

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;
    private List<String> yearList = new ArrayList<String>();

    private Long cpgcToDeleteId;
    
    public void reset() {
    	this.to = new ContratoPraticaGrupoCusto();
    	this.anoInicioVigencia = "";
    	this.mesInicioVigencia = "";
    	this.monthList = Constants.MONTH_DAY_LIST;
    	this.yearList = new ArrayList<String>();
    	this.cpgcToDeleteId = null;
    }
    
    public void resetTo() {
    	this.to = new ContratoPraticaGrupoCusto();
    }

	public ContratoPraticaGrupoCusto getTo() {
		return to;
	}

	public void setTo(ContratoPraticaGrupoCusto to) {
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

	public Long getCpgcToDeleteId() {
		return cpgcToDeleteId;
	}

	public void setCpgcToDeleteId(Long cpgcToDeleteId) {
		this.cpgcToDeleteId = cpgcToDeleteId;
	}
}
