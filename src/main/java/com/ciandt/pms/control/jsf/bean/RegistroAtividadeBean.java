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

import com.ciandt.pms.model.Atividade;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.RegistroAtividade;
import com.ciandt.pms.model.UploadArquivo;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 17/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class RegistroAtividadeBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private RegistroAtividade to = new RegistroAtividade();

    /** filter do backingBean. */
    private RegistroAtividade filter = new RegistroAtividade();

    /** lista de resultados da pesquisa. */
    private List<RegistroAtividade> resultList =
            new ArrayList<RegistroAtividade>();

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

    /** Lista para o combobox com as ContratoPratica. */
    private List<String> contratoPraticaList = new ArrayList<String>();

    /** Lista para o combobox com as ContratoPratica. */
    private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

    /** Lista para o combobox com os GrupoCusto. */
    private List<String> grupoCustoList = new ArrayList<String>();

    /** Lista para o combobox com os GrupoCusto. */
    private Map<String, Long> grupoCustoMap = new HashMap<String, Long>();

    /** Lista para o combobox com as Atividade. */
    private List<String> atividadeList = new ArrayList<String>();

    /** Lista para o combobox com as Atividade. */
    private Map<String, Long> atividadeMap = new HashMap<String, Long>();

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
     * @return the atividadeList
     */
    public List<String> getAtividadeList() {
        return atividadeList;
    }

    /**
     * @param atividadeList
     *            the atividadeList to set
     */
    public void setAtividadeList(final List<String> atividadeList) {
        this.atividadeList = atividadeList;
    }

    /**
     * @return the atividadeMap
     */
    public Map<String, Long> getAtividadeMap() {
        return atividadeMap;
    }

    /**
     * @param atividadeMap
     *            the atividadeMap to set
     */
    public void setAtividadeMap(final Map<String, Long> atividadeMap) {
        this.atividadeMap = atividadeMap;
    }

    /**
     * @return the contratoPraticaList
     */
    public List<String> getContratoPraticaList() {
        return contratoPraticaList;
    }

    /**
     * @param contratoPraticaList
     *            the contratoPraticaList to set
     */
    public void setContratoPraticaList(final List<String> contratoPraticaList) {
        this.contratoPraticaList = contratoPraticaList;
    }

    /**
     * @return the contratoPraticaMap
     */
    public Map<String, Long> getContratoPraticaMap() {
        return contratoPraticaMap;
    }

    /**
     * @param contratoPraticaMap
     *            the contratoPraticaMap to set
     */
    public void setContratoPraticaMap(final Map<String, Long> contratoPraticaMap) {
        this.contratoPraticaMap = contratoPraticaMap;
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
        uploadArquivo = null;
        errorList = null;
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new RegistroAtividade();
    }

    /**
     * Reseta o filter.
     */
    public void resetFilter() {
        this.filter = new RegistroAtividade();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<RegistroAtividade>();
    }

    /**
     * Reseta os combos de mes e ano.
     */
    public void resetDataMes() {
        this.monthBeg = "";
        this.yearBeg = "";
    }

    /**
     * @return the to
     */
    public RegistroAtividade getTo() {
        if (to == null) {
            to = new RegistroAtividade();
        }
        if (to.getContratoPratica() == null) {
            to.setContratoPratica(new ContratoPratica());
        }
        if (to.getGrupoCusto() == null) {
            to.setGrupoCusto(new GrupoCusto());
        }
        if (to.getPessoa() == null) {
            to.setPessoa(new Pessoa());
        }
        if (to.getAtividade() == null) {
            to.setAtividade(new Atividade());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final RegistroAtividade to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public RegistroAtividade getFilter() {
        if (filter == null) {
            filter = new RegistroAtividade();
        }
        if (filter.getContratoPratica() == null) {
            filter.setContratoPratica(new ContratoPratica());
        }
        if (filter.getGrupoCusto() == null) {
            filter.setGrupoCusto(new GrupoCusto());
        }
        if (filter.getPessoa() == null) {
            filter.setPessoa(new Pessoa());
        }
        if (filter.getAtividade() == null) {
            filter.setAtividade(new Atividade());
        }

        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final RegistroAtividade filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<RegistroAtividade> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<RegistroAtividade> resultList) {
        this.resultList = resultList;
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

}