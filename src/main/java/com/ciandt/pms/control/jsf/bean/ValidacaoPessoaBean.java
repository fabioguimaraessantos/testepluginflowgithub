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

import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.AlocacaoPeriodoOh;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.vo.ValidacaoPessoaRow;


/**
 * 
 * Define o BackingBean da entidade ValidacaoPessoa.
 * 
 * @since 08/12/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ValidacaoPessoaBean implements Serializable {

    /** Serail version UID. */
    private static final long serialVersionUID = 1L;

    /** Instancia do TO da entidade Pessoa. */
    private Pessoa to;

    /** Instancia da classe ValidacaoPessoaRow. */
    private ValidacaoPessoaRow row;

    /** Lista de resultados da entidade pessoa. */
    private List<ValidacaoPessoaRow> rowResultList = new ArrayList<ValidacaoPessoaRow>();

    /** Lista de resultados da entidade pessoa. */
    private List<ValidacaoPessoaRow> delegatedResultList = new ArrayList<ValidacaoPessoaRow>();

    /** Lista de resultados da entidade AlocacaoPeriodo. */
    private List<AlocacaoPeriodo> alocacaoPeriodoList = new ArrayList<AlocacaoPeriodo>();

    /** Lista de resultados da entidade AlocacaoPeriodoOh. */
    private List<AlocacaoPeriodoOh> alocacaoPeriodoOhList = new ArrayList<AlocacaoPeriodoOh>();

    /** Lista de String de nomes do PerfilVendido. */
    private List<String> perfilVendidoList = new ArrayList<String>();

    /** Map de String de nomes do PerfilVendido. */
    private Map<String, Long> perfilVendidoMap = new HashMap<String, Long>();

    /** Nome do PerfilVendido. */
    private String nomePerfilVendido = null;

    /** Nome do MapaAlocacao. */
    private String nomeMapaAlocacao = null;

    /** Lista de String de nomes do MapaAlocacao. */
    private List<String> mapaAlocacaoList = new ArrayList<String>();

    /** Map de String de nomes do MapaAlocacao. */
    private Map<String, Long> mapaAlocacaoMap = new HashMap<String, Long>();

    /** Data mês corrente da validação - selecionado. */
    private String selectedMonthDate = null;

    /** Lista de Data mês disponiveis para validação. */
    private List<String> dataMesList = new ArrayList<String>();

    /** Porcentagem total de Alocacao de uma pessoa em um mes. */
    private BigDecimal totalAlocation = null;

    /** Estido / Cor da porcentagem total de Alocacao de uma pessoa em um mes. */
    private String totalAlocationStyle = "";

    /** Porcentagem de Alocacao. */
    private BigDecimal percentualAlocacao = BigDecimal.valueOf(1);

    /** Lista para o combobox com os recurso. */
    private List<String> pessoaList = new ArrayList<String>();

    /** Map para o combobox com os recurso. */
    private Map<String, Long> pessoaMap = new HashMap<String, Long>();

    /** Atributo de define a pagina de retorno (botão back). */
    private String redirectPage = "";

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
     * @return the totalAlocationStyle
     */
    public String getTotalAlocationStyle() {
        return totalAlocationStyle;
    }

    /**
     * @param totalAlocationStyle
     *            the totalAlocationStyle to set
     */
    public void setTotalAlocationStyle(final String totalAlocationStyle) {
        this.totalAlocationStyle = totalAlocationStyle;
    }

    /**
     * Reseta o row.
     */
    public void resetRow() {
        this.row = null;
    }

    /**
     * Reseta o TO.
     */
    public void resetTo() {
        this.to = null;
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        this.resetRow();
        this.resetTo();
        this.selectedMonthDate = null;
        this.dataMesList = new ArrayList<String>();
    }

    /**
     * Reseta os campos do formulário do Add.
     */
    public void resetAdd() {
        this.nomePerfilVendido = null;
        this.nomeMapaAlocacao = null;
        this.percentualAlocacao = BigDecimal.valueOf(1);
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final Pessoa to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public Pessoa getTo() {
        if (this.to == null) {
            this.to = new Pessoa();
        }
        return to;
    }

    /**
     * @param row
     *            the row to set
     */
    public void setRow(final ValidacaoPessoaRow row) {
        this.row = row;
    }

    /**
     * @return the row
     */
    public ValidacaoPessoaRow getRow() {
        return row;
    }

    /**
     * @param rowResultList
     *            the rowResultList to set
     */
    public void setRowResultList(final List<ValidacaoPessoaRow> rowResultList) {
        this.rowResultList = rowResultList;
    }

    /**
     * @return the rowResultList
     */
    public List<ValidacaoPessoaRow> getRowResultList() {
        return rowResultList;
    }

    /**
     * @param alocacaoPeriodoList
     *            the alocacaoPeriodoList to set
     */
    public void setAlocacaoPeriodoList(
            final List<AlocacaoPeriodo> alocacaoPeriodoList) {
        this.alocacaoPeriodoList = alocacaoPeriodoList;
    }

    /**
     * @return the alocacaoPeriodoList
     */
    public List<AlocacaoPeriodo> getAlocacaoPeriodoList() {
        return alocacaoPeriodoList;
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
     * @return the mapaAlocacaoList
     */
    public List<String> getMapaAlocacaoList() {
        return mapaAlocacaoList;
    }

    /**
     * @param mapaAlocacaoList
     *            the mapaAlocacaoList to set
     */
    public void setMapaAlocacaoList(final List<String> mapaAlocacaoList) {
        this.mapaAlocacaoList = mapaAlocacaoList;
    }

    /**
     * @return the mapaAlocacaoMap
     */
    public Map<String, Long> getMapaAlocacaoMap() {
        return mapaAlocacaoMap;
    }

    /**
     * @param mapaAlocacaoMap
     *            the mapaAlocacaoMap to set
     */
    public void setMapaAlocacaoMap(final Map<String, Long> mapaAlocacaoMap) {
        this.mapaAlocacaoMap = mapaAlocacaoMap;
    }

    /**
     * @param nomePerfilVendido
     *            the nomePerfilVendido to set
     */
    public void setNomePerfilVendido(final String nomePerfilVendido) {
        this.nomePerfilVendido = nomePerfilVendido;
    }

    /**
     * @return the nomePerfilVendido
     */
    public String getNomePerfilVendido() {
        return nomePerfilVendido;
    }

    /**
     * @param nomeMapaAlocacao
     *            the nomeMapaAlocacao to set
     */
    public void setNomeMapaAlocacao(final String nomeMapaAlocacao) {
        this.nomeMapaAlocacao = nomeMapaAlocacao;
    }

    /**
     * @return the nomeMapaAlocacao
     */
    public String getNomeMapaAlocacao() {
        return nomeMapaAlocacao;
    }

    /**
     * @param totalAlocation
     *            the totalAlocation to set
     */
    public void setTotalAlocation(final BigDecimal totalAlocation) {
        this.totalAlocation = totalAlocation;
    }

    /**
     * @return the totalAlocation
     */
    public BigDecimal getTotalAlocation() {
        return totalAlocation;
    }

    /**
     * @param totalAlocation
     *            the totalAlocation to set
     */
    public void setTotalAlocationAsDouble(final Double totalAlocation) {
        this.totalAlocation = new BigDecimal(totalAlocation);
    }

    /**
     * @return the totalAlocation
     */
    public Double getTotalAlocationAsDouble() {
        if (totalAlocation != null) {
            return totalAlocation.doubleValue();
        }

        return null;
    }

    /**
     * @param percentualAlocacao
     *            the percentualAlocacao to set
     */
    public void setPercentualAlocacao(final BigDecimal percentualAlocacao) {
        this.percentualAlocacao = percentualAlocacao;
    }

    /**
     * @return the percentualAlocacao
     */
    public BigDecimal getPercentualAlocacao() {
        return percentualAlocacao;
    }

    /**
     * @param alocacaoPeriodoOhList
     *            the alocacaoPeriodoOhList to set
     */
    public void setAlocacaoPeriodoOhList(
            final List<AlocacaoPeriodoOh> alocacaoPeriodoOhList) {
        this.alocacaoPeriodoOhList = alocacaoPeriodoOhList;
    }

    /**
     * @return the alocacaoPeriodoOhList
     */
    public List<AlocacaoPeriodoOh> getAlocacaoPeriodoOhList() {
        return alocacaoPeriodoOhList;
    }

    /**
     * @param pessoaList
     *            the pessoaList to set
     */
    public void setPessoaList(final List<String> pessoaList) {
        this.pessoaList = pessoaList;
    }

    /**
     * @return the pessoaList
     */
    public List<String> getPessoaList() {
        return pessoaList;
    }

    /**
     * @param pessoaMap
     *            the pessoaMap to set
     */
    public void setPessoaMap(final Map<String, Long> pessoaMap) {
        this.pessoaMap = pessoaMap;
    }

    /**
     * @return the pessoaMap
     */
    public Map<String, Long> getPessoaMap() {
        return pessoaMap;
    }

    /**
     * @param redirectPage
     *            the redirectPage to set
     */
    public void setRedirectPage(final String redirectPage) {
        this.redirectPage = redirectPage;
    }

    /**
     * @return the redirectPage
     */
    public String getRedirectPage() {
        return redirectPage;
    }

    /**
     * @return the delegatedResultList
     */
    public List<ValidacaoPessoaRow> getDelegatedResultList() {
        return delegatedResultList;
    }

    /**
     * @param delegatedResultList
     *            the delegatedResultList to set
     */
    public void setDelegatedResultList(
            final List<ValidacaoPessoaRow> delegatedResultList) {
        this.delegatedResultList = delegatedResultList;
    }

}
