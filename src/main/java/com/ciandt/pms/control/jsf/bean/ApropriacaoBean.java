package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.model.UploadItem;

import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.UploadArquivo;

/**
 * Define o BackingBean da entidade.
 * 
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * @since 22/01/2014
 * 
 */
public class ApropriacaoBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** representa o arquivo que foi feito o upload. */
	private UploadArquivo uploadArquivo;

	/** representa o item que foi realizado o upload. */
	private UploadItem uploadItem;

	/** Lista para o combobox com padraoArquivo. */
	private List<String> padraoArquivoList = new ArrayList<String>();

	/** Lista para o combobox com padraoArquivo. */
	private Map<String, Long> padraoArquivoMap = new HashMap<String, Long>();

	/** padrao do arquivo selecionado. */
	private PadraoArquivo padraoArquivo = new PadraoArquivo();

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(0);

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Lista com os erroas da importação. */
	private List<String> errorList;

	/** representa o mes selecionado. */
	private String monthBeg;

	/** representa o ano selecionado. */
	private String yearBeg;

	/** Objeto Pessoa. */
	private Pessoa pessoa = new Pessoa();

	/** Lista para o combobox Moeda. */
	private List<String> moedaList = new ArrayList<String>();

	/** Lista para o combobox Moeda. */
	private Map<String, Long> moedaMap = new HashMap<String, Long>();

	/** Flag que indica que a tela é para edicao */
	private boolean editItem;

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		monthBeg = "";
		yearBeg = "";
		pessoa = new Pessoa();
		editItem = false;
		uploadArquivo = null;
		errorList = null;
		moedaList = new ArrayList<String>();
		moedaMap = new HashMap<String, Long>();
	}

	/**
	 * @return the uploadArquivo
	 */
	public UploadArquivo getUploadArquivo() {
		return uploadArquivo;
	}

	/**
	 * @param uploadArquivo
	 *            the uploadArquivo to set
	 */
	public void setUploadArquivo(UploadArquivo uploadArquivo) {
		this.uploadArquivo = uploadArquivo;
	}

	/**
	 * @return the uploadItem
	 */
	public UploadItem getUploadItem() {
		return uploadItem;
	}

	/**
	 * @param uploadItem
	 *            the uploadItem to set
	 */
	public void setUploadItem(UploadItem uploadItem) {
		this.uploadItem = uploadItem;
	}

	/**
	 * @return the padraoArquivoList
	 */
	public List<String> getPadraoArquivoList() {
		return padraoArquivoList;
	}

	/**
	 * @param padraoArquivoList
	 *            the padraoArquivoList to set
	 */
	public void setPadraoArquivoList(List<String> padraoArquivoList) {
		this.padraoArquivoList = padraoArquivoList;
	}

	/**
	 * @return the padraoArquivoMap
	 */
	public Map<String, Long> getPadraoArquivoMap() {
		return padraoArquivoMap;
	}

	/**
	 * @param padraoArquivoMap
	 *            the padraoArquivoMap to set
	 */
	public void setPadraoArquivoMap(Map<String, Long> padraoArquivoMap) {
		this.padraoArquivoMap = padraoArquivoMap;
	}

	/**
	 * @return the padraoArquivo
	 */
	public PadraoArquivo getPadraoArquivo() {
		return padraoArquivo;
	}

	/**
	 * @param padraoArquivo
	 *            the padraoArquivo to set
	 */
	public void setPadraoArquivo(PadraoArquivo padraoArquivo) {
		this.padraoArquivo = padraoArquivo;
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
	public void setCurrentPageId(Integer currentPageId) {
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
	public void setCurrentRowId(Long currentRowId) {
		this.currentRowId = currentRowId;
	}

	/**
	 * @return the errorList
	 */
	public List<String> getErrorList() {
		return errorList;
	}

	/**
	 * @param errorList
	 *            the errorList to set
	 */
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	/**
	 * @return the monthBeg
	 */
	public String getMonthBeg() {
		return monthBeg;
	}

	/**
	 * @param monthBeg
	 *            the monthBeg to set
	 */
	public void setMonthBeg(String monthBeg) {
		this.monthBeg = monthBeg;
	}

	/**
	 * @return the yearBeg
	 */
	public String getYearBeg() {
		return yearBeg;
	}

	/**
	 * @param yearBeg
	 *            the yearBeg to set
	 */
	public void setYearBeg(String yearBeg) {
		this.yearBeg = yearBeg;
	}

	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
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
	public void setMoedaList(List<String> moedaList) {
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
	public void setMoedaMap(Map<String, Long> moedaMap) {
		this.moedaMap = moedaMap;
	}

	/**
	 * @return the editItem
	 */
	public boolean isEditItem() {
		return editItem;
	}

	/**
	 * @param editItem
	 *            the editItem to set
	 */
	public void setEditItem(boolean editItem) {
		this.editItem = editItem;
	}
}