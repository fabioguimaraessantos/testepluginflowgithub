package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.Imposto;


/**
 * Define o BackingBean da entidade.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ImpostoBean implements Serializable {

    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1L;

    /** Constante com o valor para ativar a aba do imposto. */
    public static final Integer ACTIVE_TAB_IMPOSTO = Integer.valueOf(1);

    /** Constante com o valor para ativar a aba da aliquota. */
    public static final Integer ACTIVE_TAB_ALIQUOTA = Integer.valueOf(3);

    /** to do backingBean. */
    private Imposto to = new Imposto();

    /** lista de resultados da pesquisa. */
    private List<Imposto> resultList = null;

    /** Lista para o combobox com os impostos. */
    private List<String> impostoList = new ArrayList<String>();

    /** Lista para o combobox com os impostos. */
    private Map<String, Long> impostoMap = new HashMap<String, Long>();

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(1);

    /** Id da entidade corrente selecionada na lista de pesquisa. */
    private Long currentRowId = Long.valueOf(0);

    /** Numero da linha selecionada. */
    private Integer rowNumber = Integer.valueOf(0);

    /**
     * @return the rowNumber
     */
    public Integer getRowNumber() {
        return rowNumber;
    }

    /**
     * @param rowNumber
     *            the rowNumber to set
     */
    public void setRowNumber(final Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    /**
     * Pega o TO.
     * 
     * @return to Imposto
     */
    public Imposto getTo() {
        if (to == null) {
            to = new Imposto();
        }
        return to;
    }

    /**
     * Seta o TO.
     * 
     * @param to
     *            Imposto
     */
    public void setTo(final Imposto to) {
        this.to = to;
    }

    /**
     * Retorna a lista de resultado.
     * 
     * @return a lista de resultado
     */
    public List<Imposto> getResultList() {
        return resultList;
    }

    /**
     * Seta a lista de resultado.
     * 
     * @param resultList
     *            obtida na busca
     */
    public void setResultList(final List<Imposto> resultList) {
        this.resultList = resultList;
    }

    /**
     * Seta a lista de imposto para o combo.
     * 
     * @param impostoList
     *            the impostoList to set
     */
    public void setImpostoList(final List<String> impostoList) {
        this.impostoList = impostoList;
    }

    /**
     * Pega a lista de imposto do combo.
     * 
     * @return a lista
     */
    public List<String> getImpostoList() {
        return impostoList;
    }

    /**
     * Pega o map do combo.
     * 
     * @return o map
     */
    public Map<String, Long> getImpostoMap() {
        return impostoMap;
    }

    /**
     * Seta o map do combo.
     * 
     * @param impostoMap
     *            the impostoMap to set
     */
    public void setImpostoMap(final Map<String, Long> impostoMap) {
        this.impostoMap = impostoMap;
    }

    /**
     * Pega a pagina corrente.
     * 
     * @return o id da pagina corrente.
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * Seta a pagina corrente.
     * 
     * @param currentPageId
     *            pagina corrente
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @param currentRowId
     *            the currentRowId to set
     */
    public void setCurrentRowId(final Long currentRowId) {
        this.currentRowId = currentRowId;
    }

    /**
     * @return the currentRowId
     */
    public Long getCurrentRowId() {
        return currentRowId;
    }

    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        resetResultList();
    }

    /**
     * Reseta a lista de to.
     */
    public void resetResultList() {
        this.resultList = new ArrayList<Imposto>();
    }

    /** Reseta o Bean. */
    public void resetTo() {
        this.to = null;
    }
}
