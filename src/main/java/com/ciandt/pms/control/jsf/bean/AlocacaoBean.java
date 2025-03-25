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

import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.Recurso;


/**
 * 
 * Define o BackingBean da entidade.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class AlocacaoBean implements Serializable {

    /** Serial Version. */
    private static final long serialVersionUID = 1L;

    /** to do backingBean. */
    private Alocacao to = new Alocacao();

    /** Lista para o combobox com os PerfilVendido. */
    private List<String> perfilVendidoList = new ArrayList<String>();

    /** Map para o combobox com os PerfilVendido. */
    private Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();

    /** Lista para o combobox com os CidadeBase. */
    private List<String> cidadeBaseList = new ArrayList<String>();

    /** Map para o combobox com os CidadeBase. */
    private Map<String, Long> cidadeBaseMap = new HashMap<String, Long>();

    /** Lista para o combobox com os recurso. */
    private List<String> recursoList = new ArrayList<String>();

    /** Map para o combobox com os recurso. */
    private Map<String, Long> recursoMap = new HashMap<String, Long>();

    /** Lista para o combobox com as sugestoes - indicadores de Estagio. */
    private List<String> suggestionsListInEstagio = new ArrayList<String>();

    /** Percentual Alocacao do recurso. */
    private BigDecimal percentualAlocacao = BigDecimal.valueOf(1);

    /** Valor Utilization Rate do recurso. */
    private BigDecimal valorUr = BigDecimal.valueOf(1);

    /** Indicador do modo em tempo de execucao - create/update. */
    private Boolean isUpdate = Boolean.valueOf(false);

    /** Indicador se deseja adicionar pessoa. */
    private Boolean isAddPessoa = Boolean.TRUE;
    
    /**
     * Reseta o backingBean.
     */
    public void reset() {
        resetTo();
        this.percentualAlocacao = BigDecimal.valueOf(1);
        this.valorUr = BigDecimal.valueOf(1);
    }

    /**
     * Reseta o to.
     */
    public void resetTo() {
        this.to = new Alocacao();
    }

    /**
     * @return the to
     */
    public Alocacao getTo() {
        if (to == null) {
            to = new Alocacao();
        }
        if (to.getRecurso() == null) {
            to.setRecurso(new Recurso());
        }
        if (to.getCidadeBase() == null) {
            to.setCidadeBase(new CidadeBase());
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
    public void setTo(final Alocacao to) {
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
     * @return the cidadeBaseList
     */
    public List<String> getCidadeBaseList() {
        return cidadeBaseList;
    }

    /**
     * @param cidadeBaseList
     *            the cidadeBaseList to set
     */
    public void setCidadeBaseList(final List<String> cidadeBaseList) {
        this.cidadeBaseList = cidadeBaseList;
    }

    /**
     * @return the cidadeBaseMap
     */
    public Map<String, Long> getCidadeBaseMap() {
        return cidadeBaseMap;
    }

    /**
     * @param cidadeBaseMap
     *            the cidadeBaseMap to set
     */
    public void setCidadeBaseMap(final Map<String, Long> cidadeBaseMap) {
        this.cidadeBaseMap = cidadeBaseMap;
    }

    /**
     * @return the suggestionsListInEstagio
     */
    public List<String> getSuggestionsListInEstagio() {
        return suggestionsListInEstagio;
    }

    /**
     * @param suggestionsListInEstagio
     *            the suggestionsListInEstagio to set
     */
    public void setSuggestionsListInEstagio(
            final List<String> suggestionsListInEstagio) {
        this.suggestionsListInEstagio = suggestionsListInEstagio;
    }

    /**
     * @return the percentualAlocacao
     */
    public BigDecimal getPercentualAlocacao() {
        return percentualAlocacao;
    }

    /**
     * @param percentualAlocacao
     *            the percentualAlocacao to set
     */
    public void setPercentualAlocacao(final BigDecimal percentualAlocacao) {
        this.percentualAlocacao = percentualAlocacao;
    }

    /**
     * @return the valorUr
     */
    public BigDecimal getValorUr() {
        return valorUr;
    }

    /**
     * @param valorUr
     *            the valorUr to set
     */
    public void setValorUr(final BigDecimal valorUr) {
        this.valorUr = valorUr;
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
     * @return the recursoList
     */
    public List<String> getRecursoList() {
        return recursoList;
    }

    /**
     * @param recursoList
     *            the recursoList to set
     */
    public void setRecursoList(final List<String> recursoList) {
        this.recursoList = recursoList;
    }

    /**
     * @return the recursoMap
     */
    public Map<String, Long> getRecursoMap() {
        return recursoMap;
    }

    /**
     * @param recursoMap
     *            the recursoMap to set
     */
    public void setRecursoMap(final Map<String, Long> recursoMap) {
        this.recursoMap = recursoMap;
    }

    /**
     * @param isAddPessoa the isAddPessoa to set
     */
    public void setIsAddPessoa(final Boolean isAddPessoa) {
        this.isAddPessoa = isAddPessoa;
    }

    /**
     * @return the isAddPessoa
     */
    public Boolean getIsAddPessoa() {
        return isAddPessoa;
    }

}