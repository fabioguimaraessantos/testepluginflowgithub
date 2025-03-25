package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.TipoServico;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 05/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ItemFaturaBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private ItemFatura to = new ItemFatura();

    /** Lista para o combobox com os TipoServico. */
    private List<String> tipoServicoList = new ArrayList<String>();

    /** Map para o combobox com os TipoServico. */
    private Map<String, Long> tipoServicoMap = new HashMap<String, Long>();
    
    /** Lista para o combobox com os FonteReceita. */
    private List<String> fonteReceitaList = new ArrayList<String>();

    /** Map para o combobox com os FonteReceita. */
    private Map<String, Long> fonteReceitaMap = new HashMap<String, Long>();

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Soma dos valores dos ItemFatura listadas. */
    private BigDecimal totalGeralItemFatura = BigDecimal.valueOf(0);

    /** Indicador do padrão para exibição de valores de moeda. */
    private String patternCurrency = "";
    
    /** Indicador de combo de Contrato Prática habilitado. */
    private Boolean enableCLob = Boolean.TRUE;

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
     * @return the totalGeralItemFatura
     */
    public BigDecimal getTotalGeralItemFatura() {
        return totalGeralItemFatura;
    }

    /**
     * @param totalGeralItemFatura
     *            the totalGeralItemFatura to set
     */
    public void setTotalGeralItemFatura(final BigDecimal totalGeralItemFatura) {
        this.totalGeralItemFatura = totalGeralItemFatura;
    }

    /**
     * Reseta os valures totais.
     */
    public void resetTotalValues() {
        this.totalGeralItemFatura = BigDecimal.valueOf(0);
    }

    /**
     * Atualiza os valores dos totais.
     * 
     * @param totalGeralItemFatura
     *            - valor total dos ItemFatura
     */
    public void updateTotalValues(final double totalGeralItemFatura) {
        this.totalGeralItemFatura = BigDecimal.valueOf(totalGeralItemFatura);
    }

    /**
     * @return the tipoServicoList
     */
    public List<String> getTipoServicoList() {
        return tipoServicoList;
    }

    /**
     * @param tipoServicoList
     *            the tipoServicoList to set
     */
    public void setTipoServicoList(final List<String> tipoServicoList) {
        this.tipoServicoList = tipoServicoList;
    }

    /**
     * @return the tipoServicoMap
     */
    public Map<String, Long> getTipoServicoMap() {
        return tipoServicoMap;
    }

    /**
     * @param tipoServicoMap
     *            the tipoServicoMap to set
     */
    public void setTipoServicoMap(final Map<String, Long> tipoServicoMap) {
        this.tipoServicoMap = tipoServicoMap;
    }

    /**
     * @return the to
     */
    public ItemFatura getTo() {
        if (to == null) {
            to = new ItemFatura();
        }
        if (to.getTipoServico() == null) {
            to.setTipoServico(new TipoServico());
        }
        if (to.getFonteReceita() == null) {
            to.setFonteReceita(new FonteReceita());
        }
        if (to.getContratoPratica() == null) {
            to.setContratoPratica(new ContratoPratica());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ItemFatura to) {
        this.to = to;
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
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        this.isUpdate = Boolean.valueOf(false);
        this.enableCLob = Boolean.FALSE;
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new ItemFatura();
    }

    /**
     * @param fonteReceitaList the fonteReceitaList to set
     */
    public void setFonteReceitaList(final List<String> fonteReceitaList) {
        this.fonteReceitaList = fonteReceitaList;
    }

    /**
     * @return the fonteReceitaList
     */
    public List<String> getFonteReceitaList() {
        return fonteReceitaList;
    }

    /**
     * @param fonteReceitaMap the fonteReceitaMap to set
     */
    public void setFonteReceitaMap(final Map<String, Long> fonteReceitaMap) {
        this.fonteReceitaMap = fonteReceitaMap;
    }

    /**
     * @return the fonteReceitaMap
     */
    public Map<String, Long> getFonteReceitaMap() {
        return fonteReceitaMap;
    }

	/**
	 * @return the enableCLob
	 */
	public Boolean getEnableCLob() {
		return enableCLob;
	}

	/**
	 * @param enableCLob the enableCLob to set
	 */
	public void setEnableCLob(final Boolean enableCLob) {
		this.enableCLob = enableCLob;
	}

}