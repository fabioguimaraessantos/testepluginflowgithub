package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.*;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe SaldoFiscalRow a linha no resultado do filtro do SaldoFiscal.
 * 
 * @since 19/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class ApropriacaoFaturaRow implements java.io.Serializable,
        Comparable<ApropriacaoFaturaRow> {

    /** Default seraul version UID. */
    private static final long serialVersionUID = 1L;

    /** instancia de cliente. */
    private Cliente cliente;

    /** instancia de contrato-pratica. */
    private ContratoPratica contratoPratica;

    /** instancia de contrato-pratica. */
    private Receita receita;

    /** instancia de contrato-pratica. */
    private ReceitaDealFiscal receitaDealFiscal;

    /** instancia de deal-fiscal. */
    private DealFiscal deal;

    /** total da receita. */
    private Double totalReceita;

    /** total da fatura. */
    private Double totalFatura;

    /** padrao da moeda. */
    private String patternCurrency = "";

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente
     *            the cliente to set
     */
    public void setCliente(final Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the contratoPratica
     */
    public ContratoPratica getContratoPratica() {
        return contratoPratica;
    }

    /**
     * @param contratoPratica
     *            the contratoPratica to set
     */
    public void setContratoPratica(final ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    /**
     * @return the deal
     */
    public DealFiscal getDeal() {
        return deal;
    }

    /**
     * @param deal
     *            the deal to set
     */
    public void setDeal(final DealFiscal deal) {
        this.deal = deal;
    }

    /**
     * @return the totalReceita
     */
    public Double getTotalReceita() {
        return totalReceita;
    }

    /**
     * @param totalReceita
     *            the totalReceita to set
     */
    public void setTotalReceita(final Double totalReceita) {
        this.totalReceita = totalReceita;
    }

    /**
     * @return the totalFatura
     */
    public Double getTotalFatura() {
        return totalFatura;
    }

    /**
     * @param totalFatura
     *            the totalFatura to set
     */
    public void setTotalFatura(final Double totalFatura) {
        this.totalFatura = totalFatura;
    }

    /**
     * @param receita
     *            the receita to set
     */
    public void setReceita(final Receita receita) {
        this.receita = receita;
    }

    /**
     * @return the receita
     */
    public Receita getReceita() {
        return receita;
    }

    /**
     * @param receitaDealFiscal
     *            the receitaDealFiscal to set
     */
    public void setReceitaDealFiscal(final ReceitaDealFiscal receitaDealFiscal) {
        this.receitaDealFiscal = receitaDealFiscal;
    }

    /**
     * @return the receitaDealFiscal
     */
    public ReceitaDealFiscal getReceitaDealFiscal() {
        return receitaDealFiscal;
    }

    /**
     * @param patternCurrency
     *            the patternCurrency to set
     */
    public void setPatternCurrency(final String patternCurrency) {
        this.patternCurrency = patternCurrency;
    }

    /**
     * @return the patternCurrency
     */
    public String getPatternCurrency() {
        return patternCurrency;
    }

    /**
     * Implementação do metodo de comparação.
     * 
     * @param otherRow
     *            - entidade do tipo ApropriacaoFaturaRow.
     * 
     * @return a comparação dos dois objetos
     */
    @Override
    public int compareTo(final ApropriacaoFaturaRow otherRow) {

        // compara por nome cliente
        int nomeClienteCompare = this.cliente
                .getNomeCliente()
                .toUpperCase()
                .compareTo(otherRow.getCliente().getNomeCliente().toUpperCase());
        if (nomeClienteCompare != 0) {
            return nomeClienteCompare;
        }

        // se igual, compara por nome contrato pratica
        int nomeContratoPraticaCompare = this.contratoPratica
                .getNomeContratoPratica().toUpperCase().compareTo(
                        otherRow.getContratoPratica().getNomeContratoPratica()
                                .toUpperCase());
        if (nomeContratoPraticaCompare != 0) {
            return nomeContratoPraticaCompare;
        }

        // se igual, compara data
        if (DateUtil.before(this.receita.getDataMes(), otherRow.getReceita()
                .getDataMes())) {
            return -1;
        }

        if (DateUtil.after(this.receita.getDataMes(), otherRow.getReceita()
                .getDataMes())) {
            return 1;
        }

        return 0;
    }

}