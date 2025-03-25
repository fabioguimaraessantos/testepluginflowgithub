package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ciandt.pms.model.BasePapelAlocacao;
import com.ciandt.pms.model.PapelAlocacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.CustoBasePapelAlocacao;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 13/07/2011
 * @author cmantovani
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class CustoBasePapelAlocacaoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** to do backingBean. */
    private CustoBasePapelAlocacao to = new CustoBasePapelAlocacao();

    /** toEdit do backingBean. */
    private CustoBasePapelAlocacao toEdit = new CustoBasePapelAlocacao();

    private String nomeEmpresaMatriz = null;

    /** lista de resultados da pesquisa. */
    private List<CustoBasePapelAlocacao> resultList = new ArrayList<CustoBasePapelAlocacao>();

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista dos possiveis valores de meses. */
    private List<String> validityMonthList = Arrays.asList("01", "02", "03",
            "04", "05", "06", "07", "08", "09", "10", "11", "12");

    /** Lista dos possiveis valores de anos. */
    private List<String> validityYearList = new ArrayList<String>();

    /** Mes vigencia - inicio. */
    private String validityMonthBeg = null;

    /** Ano vigencia - inicio. */
    private String validityYearBeg = null;

    /** Data do HistoryLock. */
    private Date historyLockDate;

    /** Flag para indicar se a tela exibida é de Inclusão ou Edição de Resource Position */
    private Boolean isUpdate;

    /** Flag para indicar se os valores editados devem se replicar
     *   para todos os meses posteriores ao do Start Date editado */
    private Boolean updateAllMonthsAhead;

    public void setNomeEmpresaMatriz(final String nomeEmpresaMatriz){this.nomeEmpresaMatriz = nomeEmpresaMatriz;}

    public String getNomeEmpresaMatriz() {return this.nomeEmpresaMatriz;}

    /**
     * @return the historyLockDate
     */
    public Date getHistoryLockDate() {
        return historyLockDate;
    }

    /**
     * @param historyLockDate
     *            the historyLockDate to set
     */
    public void setHistoryLockDate(final Date historyLockDate) {
        this.historyLockDate = historyLockDate;
    }

    /**
     * @return the validityMonthBeg
     */
    public String getValidityMonthBeg() {
        return validityMonthBeg;
    }

    /**
     * @param validityMonthBeg
     *            the validityMonthBeg to set
     */
    public void setValidityMonthBeg(final String validityMonthBeg) {
        this.validityMonthBeg = validityMonthBeg;
    }

    /**
     * @return the validityYearBeg
     */
    public String getValidityYearBeg() {
        return validityYearBeg;
    }

    /**
     * @param validityYearBeg
     *            the validityYearBeg to set
     */
    public void setValidityYearBeg(final String validityYearBeg) {
        this.validityYearBeg = validityYearBeg;
    }

    /**
     * @return the validityMonthList
     */
    public List<String> getValidityMonthList() {
        return validityMonthList;
    }

    /**
     * @return lista de anos da vigencia
     */
    public List<String> getValidityYearList() {

        int yearBegin = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(systemProperties
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> yearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            yearList.add("" + i);
        }

        validityYearList = yearList;

        return validityYearList;
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
        resetToEdit();
        resetResultList();
        resetValidityDate();
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new CustoBasePapelAlocacao();
        this.isUpdate = Boolean.FALSE;
        this.updateAllMonthsAhead = Boolean.TRUE;
    }

    public void resetToEdit() {
        this.toEdit = new CustoBasePapelAlocacao();
        this.isUpdate = Boolean.FALSE;
        this.updateAllMonthsAhead = Boolean.TRUE;
    }
    /**
     * Reseta a data de vigencia.
     */
    public void resetValidityDate() {
        this.validityMonthBeg = "";
        this.validityYearBeg = "";
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<CustoBasePapelAlocacao>();
    }

    /**
     * @return the to
     */
    public CustoBasePapelAlocacao getTo() {
        if (to == null) {
            to = new CustoBasePapelAlocacao();
        }
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final CustoBasePapelAlocacao to) {
        this.to = to;
    }

    /**
     * @return the resultList
     */
    public List<CustoBasePapelAlocacao> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<CustoBasePapelAlocacao> resultList) {
        this.resultList = resultList;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean update) {
        isUpdate = update;
    }

    public Boolean getUpdateAllMonthsAhead() {
        return updateAllMonthsAhead;
    }

    public void setUpdateAllMonthsAhead(Boolean updateAllMonthsAhead) {
        this.updateAllMonthsAhead = updateAllMonthsAhead;
    }

    public CustoBasePapelAlocacao getToEdit() {
        return toEdit;
    }

    public void setToEdit(CustoBasePapelAlocacao toEdit) {
        this.toEdit = toEdit;
    }
}