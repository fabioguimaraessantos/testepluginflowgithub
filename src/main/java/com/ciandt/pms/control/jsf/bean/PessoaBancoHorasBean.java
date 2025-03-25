package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.model.UploadItem;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBancoHoras;
import com.ciandt.pms.model.UploadArquivo;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 13/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PessoaBancoHorasBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private PessoaBancoHoras to = new PessoaBancoHoras();

    /** filter do backingBean. */
    private PessoaBancoHoras filter = new PessoaBancoHoras();

    /** lista de resultados da pesquisa. */
    private List<PessoaBancoHoras> resultList;

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** representa o mes selecionado. */
    private String monthBeg;

    /** representa o ano selecionado. */
    private String yearBeg;
    
    /** representa o mes selecionado - tela search. */
    private String monthBegFilter;

    /** representa o ano selecionado - tela search. */
    private String yearBegFilter;

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
    
    /** Lista com os erroas da importação. */
    private List<String> errorList;
    
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
    public void setMonthBeg(final String monthBeg) {
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
    public void setYearBeg(final String yearBeg) {
        this.yearBeg = yearBeg;
    }
    
    /**
     * @return the monthBegFilter
     */
    public String getMonthBegFilter() {
        return monthBegFilter;
    }

    /**
     * @param monthBegFilter
     *            the monthBegFilter to set
     */
    public void setMonthBegFilter(final String monthBegFilter) {
        this.monthBegFilter = monthBegFilter;
    }

    /**
     * @return the yearBegFilter
     */
    public String getYearBegFilter() {
        return yearBegFilter;
    }

    /**
     * @param yearBegFilter
     *            the yearBegFilter to set
     */
    public void setYearBegFilter(final String yearBegFilter) {
        this.yearBegFilter = yearBegFilter;
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
        resetDataMes();
        resetDataMesFilter();
        uploadArquivo = null;
        errorList = null;
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new PessoaBancoHoras();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new PessoaBancoHoras();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = null;
    }

    /**
     * Reseta os combos de mes e ano.
     */
    public void resetDataMes() {
        this.monthBeg = "";
        this.yearBeg = "";
    }
    
    /**
     * Reseta os combos de mes e ano - tela search.
     */
    public void resetDataMesFilter() {
        this.monthBegFilter = "";
        this.yearBegFilter = "";
    }

    /**
     * @return the to
     */
    public PessoaBancoHoras getTo() {
        if (to == null) {
            to = new PessoaBancoHoras();
        }
        if (to.getPessoa() == null) {
            to.setPessoa(new Pessoa());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final PessoaBancoHoras to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public PessoaBancoHoras getFilter() {
        if (filter == null) {
            filter = new PessoaBancoHoras();
        }
        if (filter.getPessoa() == null) {
            filter.setPessoa(new Pessoa());
        }

        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final PessoaBancoHoras filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<PessoaBancoHoras> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<PessoaBancoHoras> resultList) {
        this.resultList = resultList;
    }

    /**
     * @param uploadArquivo the uploadArquivo to set
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
     * @param uploadItem the uploadItem to set
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
     * @param padraoArquivoList the padraoArquivoList to set
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
     * @param padraoArquivoMap the padraoArquivoMap to set
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
     * @param padraoArquivo the padraoArquivo to set
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
     * @param errorList the errorList to set
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

}