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

import com.ciandt.pms.model.ItemReceita;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Pessoa;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 29/12/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ItemReceitaBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private ItemReceita to = new ItemReceita();

    /** Lista para o combobox com os PerfilVendido. */
    private List<String> perfilVendidoList = new ArrayList<String>();

    /** Map para o combobox com os PerfilVendido. */
    private Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();

    /** Lista para o combobox com os Pessoa. */
    private List<String> pessoaList = new ArrayList<String>();

    /** Map para o combobox com os Pessoa. */
    private Map<String, Long> pessoaMap = new HashMap<String, Long>();

    /** Percentual Faturamento da Pessoa. */
    private BigDecimal percentualFaturamento = BigDecimal.valueOf(1);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Numero de horas, utilizado na tela de Add. */
    private BigDecimal numHoras = BigDecimal.valueOf(0.0);
    
    /**
     * @return the pessoaList
     */
    public List<String> getPessoaList() {
        return pessoaList;
    }

    /**
     * @param pessoaList
     *            the pessoaList to set
     */
    public void setPessoaList(final List<String> pessoaList) {
        this.pessoaList = pessoaList;
    }

    /**
     * @return the pessoaMap
     */
    public Map<String, Long> getPessoaMap() {
        return pessoaMap;
    }

    /**
     * @param pessoaMap
     *            the pessoaMap to set
     */
    public void setPessoaMap(final Map<String, Long> pessoaMap) {
        this.pessoaMap = pessoaMap;
    }

    /**
     * @return the to
     */
    public ItemReceita getTo() {
        if (to == null) {
            to = new ItemReceita();
        }
        if (to.getPessoa() == null) {
            to.setPessoa(new Pessoa());
        }
        if (to.getPerfilVendido() == null) {
            to.setPerfilVendido(new PerfilVendido());
        }

        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ItemReceita to) {
        this.to = to;
    }

    /**
     * @return the perfilVendidoList
     */
    public List<String> getPerfilVendidoList() {
        return perfilVendidoList;
    }

    /**
     * @param perfilVendidoList
     *            the perfilVendidoList to set
     */
    public void setPerfilVendidoList(final List<String> perfilVendidoList) {
        this.perfilVendidoList = perfilVendidoList;
    }

    /**
     * @return the perfilVendidoMap
     */
    public Map<String, Long> getPerfilVendidoMap() {
        return perfilVendidoMap;
    }

    /**
     * @param perfilVendidoMap
     *            the perfilVendidoMap to set
     */
    public void setPerfilVendidoMap(final Map<String, Long> perfilVendidoMap) {
        this.perfilVendidoMap = perfilVendidoMap;
    }

    /**
     * @return the percentualFaturamento
     */
    public BigDecimal getPercentualFaturamento() {
        return percentualFaturamento;
    }

    /**
     * @param percentualFaturamento
     *            the percentualFaturamento to set
     */
    public void setPercentualFaturamento(final BigDecimal percentualFaturamento) {
        this.percentualFaturamento = percentualFaturamento;
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
        this.getTo().setNumeroFte(BigDecimal.valueOf(1));
        this.percentualFaturamento = BigDecimal.valueOf(1);
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new ItemReceita();
    }

    /**
     * @param numHoras the numHoras to set
     */
    public void setNumHoras(final BigDecimal numHoras) {
        this.numHoras = numHoras;
    }

    /**
     * @return the numHoras
     */
    public BigDecimal getNumHoras() {
        return numHoras;
    }

}