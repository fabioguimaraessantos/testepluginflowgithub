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

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.HistoricoControleReajusteRow;

/**
 * Define o Bean da tela de busca de {@link ControleReajuste}.
 * 
 * @since 06/01/2014
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ControleReajusteSearchBean implements Serializable {

	private static final long serialVersionUID = 1L;


    /** to do backingBean. */
    private ControleReajuste to = new ControleReajuste();
	
	/** Atributo para o primeiro campo do intervalo de data. */
	private Date dateFrom = null;
	
	/** Atributo para o segundo campo do intervalo de data. */
	private Date dateTo = null;

	/** Lista para o combobox com as NaturezaCentroLucro. */
	private List<String> naturezaList = new ArrayList<String>();

	/** Lista para o combobox com as NaturezaCentroLucro. */
	private Map<String, Long> naturezaMap = new HashMap<String, Long>();

	private String naturezaSelected = "";

	/** Lista para o combobox com as CentroLucro. */
	private List<String> centrosLucro = new ArrayList<String>();

	/** Lista para o combobox com as CentroLucro. */
	private Map<String, Long> centrosLucroMap = new HashMap<String, Long>();

	private String centroLucroSelected = "";

	/** Lista para o combobox com as Cliente. */
	private List<String> clientes = new ArrayList<String>();

	/** Lista para o combobox com as Cliente. */
	private Map<String, Long> clientesMap = new HashMap<String, Long>();

	private String clienteSelected = "";

	/** Lista para o combobox com as Msa. */
	private List<String> msaList = new ArrayList<String>();

	/** Lista para o combobox com as Msa. */
	private Map<String, Long> msaMap = new HashMap<String, Long>();

	private String msaSelected = "";

	/** Lista para o combobox com as ControleReajusteStatus. */
	private List<String> status = new ArrayList<String>();

	/** Lista para o combobox com as ControleReajusteStatus. */
	private Map<String, Long> statusMap = new HashMap<String, Long>();

	private String statusSelected = "";

	private String searchStatusSelected = "";

	private List<ControleReajuste> controlesReajuste = new ArrayList<ControleReajuste>();
    
	/** Lista com o historico de auditoria. */
	private List<HistoricoControleReajusteRow> historyList = new ArrayList<HistoricoControleReajusteRow>();

    /** {@link Msa} para exibir informações do mesmo em algumas telas. */
    private Msa msa = new Msa();

    /**
     * Reseta o backingBean.
     */
    public void reset() {
    	this.to = new ControleReajuste();
    	this.dateFrom = null;
    	this.dateTo = null;
    	this.controlesReajuste = new ArrayList<ControleReajuste>();
    	this.naturezaSelected = "";
    	this.centroLucroSelected = "";
    	this.clienteSelected = "";
    	this.msaSelected = "";
    	this.status = new ArrayList<String>();
    	this.statusMap = new HashMap<String, Long>();
    	this.statusSelected = "";
    	this.searchStatusSelected = "";
    	this.msa = new Msa();
    }

    public void resetTo() {
    	this.to = new ControleReajuste();
    }

    /**
     * @return the to
     */
    public ControleReajuste getTo() {
    	if (to == null) {
    		to = new ControleReajuste();
    	}
    	
    	return to;
    }

	/**
	 * @param to the to to set
	 */
	public void setTo(ControleReajuste to) {
		this.to = to;
	}

	/**
	 * @return the dateFrom
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * @param dateFrom the dateFrom to set
	 */
	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * @return the dateTo
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/**
	 * @param dateTo the dateTo to set
	 */
	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * @return the naturezaList
	 */
	public List<String> getNaturezaList() {
		return naturezaList;
	}

	/**
	 * @param naturezaList the naturezaList to set
	 */
	public void setNaturezaList(List<String> naturezaList) {
		this.naturezaList = naturezaList;
	}

	/**
	 * @return the naturezaMap
	 */
	public Map<String, Long> getNaturezaMap() {
		return naturezaMap;
	}

	/**
	 * @param naturezaMap the naturezaMap to set
	 */
	public void setNaturezaMap(Map<String, Long> naturezaMap) {
		this.naturezaMap = naturezaMap;
	}

	/**
	 * @return the naturezaSelected
	 */
	public String getNaturezaSelected() {
		return naturezaSelected;
	}

	/**
	 * @param naturezaSelected the naturezaSelected to set
	 */
	public void setNaturezaSelected(String naturezaSelected) {
		this.naturezaSelected = naturezaSelected;
	}

	/**
	 * @return the centrosLucro
	 */
	public List<String> getCentrosLucro() {
		return centrosLucro;
	}

	/**
	 * @param centrosLucro the centrosLucro to set
	 */
	public void setCentrosLucro(List<String> centrosLucro) {
		this.centrosLucro = centrosLucro;
	}

	/**
	 * @return the centrosLucroMap
	 */
	public Map<String, Long> getCentrosLucroMap() {
		return centrosLucroMap;
	}

	/**
	 * @param centrosLucroMap the centrosLucroMap to set
	 */
	public void setCentrosLucroMap(Map<String, Long> centrosLucroMap) {
		this.centrosLucroMap = centrosLucroMap;
	}

	/**
	 * @return the centroLucroSelected
	 */
	public String getCentroLucroSelected() {
		return centroLucroSelected;
	}

	/**
	 * @param centroLucroSelected the centroLucroSelected to set
	 */
	public void setCentroLucroSelected(String centroLucroSelected) {
		this.centroLucroSelected = centroLucroSelected;
	}

	/**
	 * @return the clientes
	 */
	public List<String> getClientes() {
		return clientes;
	}

	/**
	 * @param clientes the clientes to set
	 */
	public void setClientes(List<String> clientes) {
		this.clientes = clientes;
	}

	/**
	 * @return the clientesMap
	 */
	public Map<String, Long> getClientesMap() {
		return clientesMap;
	}

	/**
	 * @param clientesMap the clientesMap to set
	 */
	public void setClientesMap(Map<String, Long> clientesMap) {
		this.clientesMap = clientesMap;
	}

	/**
	 * @return the clienteSelected
	 */
	public String getClienteSelected() {
		return clienteSelected;
	}

	/**
	 * @param clienteSelected the clienteSelected to set
	 */
	public void setClienteSelected(String clienteSelected) {
		this.clienteSelected = clienteSelected;
	}

	/**
	 * @return the msaList
	 */
	public List<String> getMsaList() {
		return msaList;
	}

	/**
	 * @param msaList the msaList to set
	 */
	public void setMsaList(List<String> msaList) {
		this.msaList = msaList;
	}

	/**
	 * @return the msaMap
	 */
	public Map<String, Long> getMsaMap() {
		return msaMap;
	}

	/**
	 * @param msaMap the msaMap to set
	 */
	public void setMsaMap(Map<String, Long> msaMap) {
		this.msaMap = msaMap;
	}

	/**
	 * @return the msaSelected
	 */
	public String getMsaSelected() {
		return msaSelected;
	}

	/**
	 * @param msaSelected the msaSelected to set
	 */
	public void setMsaSelected(String msaSelected) {
		this.msaSelected = msaSelected;
	}

	/**
	 * @return the status
	 */
	public List<String> getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(List<String> status) {
		this.status = status;
	}

	/**
	 * @return the statusMap
	 */
	public Map<String, Long> getStatusMap() {
		return statusMap;
	}

	/**
	 * @param statusMap the statusMap to set
	 */
	public void setStatusMap(Map<String, Long> statusMap) {
		this.statusMap = statusMap;
	}

	/**
	 * @return the statusSelected
	 */
	public String getStatusSelected() {
		return statusSelected;
	}

	/**
	 * @param statusSelected the statusSelected to set
	 */
	public void setStatusSelected(String statusSelected) {
		this.statusSelected = statusSelected;
	}

	/**
	 * @return the searchStatusSelected
	 */
	public String getSearchStatusSelected() {
		return searchStatusSelected;
	}

	/**
	 * @param searchStatusSelected the searchStatusSelected to set
	 */
	public void setSearchStatusSelected(String searchStatusSelected) {
		this.searchStatusSelected = searchStatusSelected;
	}

	/**
	 * @return the controlesReajuste
	 */
	public List<ControleReajuste> getControlesReajuste() {
		return controlesReajuste;
	}

	/**
	 * @param controlesReajuste the controlesReajuste to set
	 */
	public void setControlesReajuste(List<ControleReajuste> controlesReajuste) {
		this.controlesReajuste = controlesReajuste;
	}

	/**
	 * @return the historyList
	 */
	public List<HistoricoControleReajusteRow> getHistoryList() {
		return historyList;
	}

	/**
	 * @param historyList the historyList to set
	 */
	public void setHistoryList(List<HistoricoControleReajusteRow> historyList) {
		this.historyList = historyList;
	}

	/**
	 * @return the msa
	 */
	public Msa getMsa() {
		return msa;
	}

	/**
	 * @param msa the msa to set
	 */
	public void setMsa(Msa msa) {
		this.msa = msa;
	}
}
