package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.model.UploadItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.model.UploadDespesa;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 13/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class DespesaBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private UploadDespesa uploadDespesa = new UploadDespesa();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** representa o arquivo que foi feito o upload. */
    private UploadArquivo uploadArquivo;

    /** representa o item que foi realizado o upload. */
    private UploadItem uploadItem;

    /** Lista para o combobox com padraoArquivo. */
    private List<String> padraoArquivoList = new ArrayList<String>();

    /** Lista para o combobox com padraoArquivo. */
    private Map<String, Long> padraoArquivoMap = new HashMap<String, Long>();

    /** Lista para o combobox com empresa. */
    private List<String> empresaList = new ArrayList<String>();

    /** Lista para o combobox com empresa. */
    private Map<String, Long> empresaMap = new HashMap<String, Long>();

    /** Lista para o combobox com grupo de custo. */
    private List<String> grupoCustoList = new ArrayList<String>();

    /** Lista para o combobox com grupo de custo. */
    private Map<String, Long> grupoCustoMap = new HashMap<String, Long>();
    
    /** Lista para o combobox com contrato pratica. */
    private List<String> contratoPraticaList = new ArrayList<String>();
    
    /** Lista para o combobox com grupo de custo. */
    private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

    /** Lista para o combobox com tipo de despesa. */
    private List<String> tipoDespesaList = new ArrayList<String>();

    /** Lista para o combobox com tipo de despesa. */
    private Map<String, Long> tipoDespesaMap = new HashMap<String, Long>();

    /** Lista para o combobox com moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Lista para o combobox com moeda. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /** padrao do arquivo selecionado. */
    private PadraoArquivo padraoArquivo = new PadraoArquivo();

    /** empresa selecionada. */
    private Empresa empresa = new Empresa();

    /** grupoCusto selecionado. */
    private GrupoCusto grupoCusto = new GrupoCusto();
    
    /** grupoCusto selecionado. */
    private ContratoPratica contratoPratica = new ContratoPratica();

    /** tipoDespesa selecionado. */
    private TipoDespesa tipoDespesa = new TipoDespesa();

    /** moeda selecionada. */
    private Moeda moeda = new Moeda();

    /** Lista com os erros da importação. */
    private List<String> errorList;

    /** Lista de Data mês disponiveis para validação. */
    private List<String> dataMesList = new ArrayList<String>();

    /** Data mês corrente da validação - selecionado. */
    private String selectedMonthDate = null;

    /** Data mês remocao. */
    private String dataRemocao = null;

    /** Descricao para busca. */
    private String descricao = null;

    /** Data de inicio para busca. */
    private Date dataInicio;

    /** Data de fim para busca. */
    private Date dataFim;

    /** Lista com resultados da pesquisa. */
    private List<UploadDespesa> resultList;

    /** Entidade para busca. */
    private UploadDespesa uploadDespesaFilter = new UploadDespesa();

    /** Indicacao de edicao. */
    private Boolean isUpdate = false;

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetUploadDespesa();
        resetUploadDespesaFilter();
        resetResultList();
        this.dataInicio = null;
        this.dataFim = null;
        this.empresa = new Empresa();
        this.grupoCusto = new GrupoCusto();
        this.tipoDespesa = new TipoDespesa();
        this.descricao = null;
        uploadArquivo = null;
        errorList = null;
    }

    /**
     * Reseta o uploadDespesa.
     */
    public void resetUploadDespesa() {
        this.uploadDespesa = new UploadDespesa();
        this.uploadDespesa.setEmpresa(new Empresa());
        this.uploadDespesa.setGrupoCusto(new GrupoCusto());
        this.uploadDespesa.setTipoDespesa(new TipoDespesa());
        this.uploadDespesa.setMoeda(new Moeda());

    }

    /**
     * Reseta o uploadDespesaFilter.
     */
    public void resetUploadDespesaFilter() {
        this.uploadDespesaFilter = new UploadDespesa();
    }

    /**
     * Reseta a lista de resultados da busca.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<UploadDespesa>();
    }

    /**
     * @return the uploadDespesa
     */
    public UploadDespesa getUploadDespesa() {
        if (uploadDespesa == null) {
            uploadDespesa = new UploadDespesa();
        }
        return uploadDespesa;
    }

    /**
     * @param uploadDespesa
     *            the uploadDespesa to set
     */
    public void setUploadDespesa(final UploadDespesa uploadDespesa) {
        this.uploadDespesa = uploadDespesa;
    }

    /**
     * @param uploadArquivo
     *            the uploadArquivo to set
     */
    public void setUploadArquivo(final UploadArquivo uploadArquivo) {
        this.uploadArquivo = uploadArquivo;
    }

    /**
     * @return the uploadArquivo
     */
    public UploadArquivo getUploadArquivo() {
        return uploadArquivo;
    }

    /**
     * @param uploadItem
     *            the uploadItem to set
     */
    public void setUploadItem(final UploadItem uploadItem) {
        this.uploadItem = uploadItem;
    }

    /**
     * @return the uploadItem
     */
    public UploadItem getUploadItem() {
        return uploadItem;
    }

    /**
     * @param padraoArquivoList
     *            the padraoArquivoList to set
     */
    public void setPadraoArquivoList(final List<String> padraoArquivoList) {
        this.padraoArquivoList = padraoArquivoList;
    }

    /**
     * @return the padraoArquivoList
     */
    public List<String> getPadraoArquivoList() {
        return padraoArquivoList;
    }

    /**
     * @param padraoArquivoMap
     *            the padraoArquivoMap to set
     */
    public void setPadraoArquivoMap(final Map<String, Long> padraoArquivoMap) {
        this.padraoArquivoMap = padraoArquivoMap;
    }

    /**
     * @return the padraoArquivoMap
     */
    public Map<String, Long> getPadraoArquivoMap() {
        return padraoArquivoMap;
    }

    /**
     * @param padraoArquivo
     *            the padraoArquivo to set
     */
    public void setPadraoArquivo(final PadraoArquivo padraoArquivo) {
        this.padraoArquivo = padraoArquivo;
    }

    /**
     * @return the padraoArquivo
     */
    public PadraoArquivo getPadraoArquivo() {
        return padraoArquivo;
    }

    /**
     * @param errorList
     *            the errorList to set
     */
    public void setErrorList(final List<String> errorList) {
        this.errorList = errorList;
    }

    /**
     * @return the errorList
     */
    public List<String> getErrorList() {
        return errorList;
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
     * @return the empresaList
     */
    public List<String> getEmpresaList() {
        return empresaList;
    }

    /**
     * @param empresaList
     *            the empresaList to set
     */
    public void setEmpresaList(final List<String> empresaList) {
        this.empresaList = empresaList;
    }

    /**
     * @return the empresaMap
     */
    public Map<String, Long> getEmpresaMap() {
        return empresaMap;
    }

    /**
     * @param empresaMap
     *            the empresaMap to set
     */
    public void setEmpresaMap(final Map<String, Long> empresaMap) {
        this.empresaMap = empresaMap;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa
     *            the empresa to set
     */
    public void setEmpresa(final Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the dataMesList
     */
    public List<String> getDataMesList() {
        return dataMesList;
    }

    /**
     * @param dataMesList
     *            the dataMesList to set
     */
    public void setDataMesList(final List<String> dataMesList) {
        this.dataMesList = dataMesList;
    }

    /**
     * @return the selectedMonthDate
     */
    public String getSelectedMonthDate() {
        return selectedMonthDate;
    }

    /**
     * @param selectedMonthDate
     *            the selectedMonthDate to set
     */
    public void setSelectedMonthDate(final String selectedMonthDate) {
        this.selectedMonthDate = selectedMonthDate;
    }

    /**
     * @return the dataRemocao
     */
    public String getDataRemocao() {
        return dataRemocao;
    }

    /**
     * @param dataRemocao
     *            the dataRemocao to set
     */
    public void setDataRemocao(final String dataRemocao) {
        this.dataRemocao = dataRemocao;
    }

    /**
     * @return the grupoCustoList
     */
    public List<String> getGrupoCustoList() {
        return grupoCustoList;
    }

    /**
     * @param grupoCustoList
     *            the grupoCustoList to set
     */
    public void setGrupoCustoList(final List<String> grupoCustoList) {
        this.grupoCustoList = grupoCustoList;
    }

    /**
     * @return the grupoCustoMap
     */
    public Map<String, Long> getGrupoCustoMap() {
        return grupoCustoMap;
    }

    /**
     * @param grupoCustoMap
     *            the grupoCustoMap to set
     */
    public void setGrupoCustoMap(final Map<String, Long> grupoCustoMap) {
        this.grupoCustoMap = grupoCustoMap;
    }

    /**
     * @return the tipoDespesaList
     */
    public List<String> getTipoDespesaList() {
        return tipoDespesaList;
    }

    /**
     * @param tipoDespesaList
     *            the tipoDespesaList to set
     */
    public void setTipoDespesaList(final List<String> tipoDespesaList) {
        this.tipoDespesaList = tipoDespesaList;
    }

    /**
     * @return the tipoDespesaMap
     */
    public Map<String, Long> getTipoDespesaMap() {
        return tipoDespesaMap;
    }

    /**
     * @param tipoDespesaMap
     *            the tipoDespesaMap to set
     */
    public void setTipoDespesaMap(final Map<String, Long> tipoDespesaMap) {
        this.tipoDespesaMap = tipoDespesaMap;
    }

    /**
     * @return the grupoCusto
     */
    public GrupoCusto getGrupoCusto() {
        return grupoCusto;
    }

    /**
     * @param grupoCusto
     *            the grupoCusto to set
     */
    public void setGrupoCusto(final GrupoCusto grupoCusto) {
        this.grupoCusto = grupoCusto;
    }

    /**
     * @return the tipoDespesa
     */
    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    /**
     * @param tipoDespesa
     *            the tipoDespesa to set
     */
    public void setTipoDespesa(final TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    /**
     * @return the dataInicio
     */
    public Date getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataInicio
     *            the dataInicio to set
     */
    public void setDataInicio(final Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the dataFim
     */
    public Date getDataFim() {
        return dataFim;
    }

    /**
     * @param dataFim
     *            the dataFim to set
     */
    public void setDataFim(final Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao
     *            the descricao to set
     */
    public void setDescricao(final String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the resultList
     */
    public List<UploadDespesa> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<UploadDespesa> resultList) {
        this.resultList = resultList;
    }

    /**
     * @return the uploadDespesaFilter
     */
    public UploadDespesa getUploadDespesaFilter() {
        return uploadDespesaFilter;
    }

    /**
     * @param uploadDespesaFilter
     *            the uploadDespesaFilter to set
     */
    public void setUploadDespesaFilter(final UploadDespesa uploadDespesaFilter) {
        this.uploadDespesaFilter = uploadDespesaFilter;
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
     * @return the moeda
     */
    public Moeda getMoeda() {
        return moeda;
    }

    /**
     * @param moeda
     *            the moeda to set
     */
    public void setMoeda(final Moeda moeda) {
        this.moeda = moeda;
    }

	/**
	 * @return the contratoPraticaList
	 */
	public List<String> getContratoPraticaList() {
		return contratoPraticaList;
	}

	/**
	 * @param contratoPraticaList the contratoPraticaList to set
	 */
	public void setContratoPraticaList(List<String> contratoPraticaList) {
		this.contratoPraticaList = contratoPraticaList;
	}

	/**
	 * @return the contratoPraticaMap
	 */
	public Map<String, Long> getContratoPraticaMap() {
		return contratoPraticaMap;
	}

	/**
	 * @param contratoPraticaMap the contratoPraticaMap to set
	 */
	public void setContratoPraticaMap(Map<String, Long> contratoPraticaMap) {
		this.contratoPraticaMap = contratoPraticaMap;
	}

	/**
	 * @return the contratoPratica
	 */
	public ContratoPratica getContratoPratica() {
		return contratoPratica;
	}

	/**
	 * @param contratoPratica the contratoPratica to set
	 */
	public void setContratoPratica(ContratoPratica contratoPratica) {
		this.contratoPratica = contratoPratica;
	}

}