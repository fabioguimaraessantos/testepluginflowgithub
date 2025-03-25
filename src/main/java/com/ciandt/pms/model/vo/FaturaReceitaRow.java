package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.FaturaReceita;

/**
 * 
 * A classe FaturaReceitaRow que representa a linha de cada FaturaReceita.
 * 
 * @since 25/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class FaturaReceitaRow implements java.io.Serializable,
        Comparable<FaturaReceitaRow> {

    /** Default seraul version UID. */
    private static final long serialVersionUID = 1L;

    /** Intancia da entidade FaturaReceita. */
    private FaturaReceita faturaReceita;

    /** total associado a receita. */
    private Double invoiceBalance;

    /** total associado a receita. */
    private Double totalValueInvoice;
    
	/** Valor pendente. */
	private Double pendingInvoiceValue;

    /**
     * @param faturaReceita
     *            the faturaReceita to set
     */
    public void setFaturaReceita(final FaturaReceita faturaReceita) {
        this.faturaReceita = faturaReceita;
    }

    /**
     * @return the faturaReceita
     */
    public FaturaReceita getFaturaReceita() {
        return faturaReceita;
    }

    /**
     * @param invoiceBalance
     *            the invoiceBalance to set
     */
    public void setInvoiceBalance(final Double invoiceBalance) {
        this.invoiceBalance = invoiceBalance;
    }

    /**
     * @return the invoiceBalance
     */
    public Double getInvoiceBalance() {
        return invoiceBalance;
    }

    /**
     * @param totalValueInvoice
     *            the totalValueInvoice to set
     */
    public void setTotalValueInvoice(final Double totalValueInvoice) {
        this.totalValueInvoice = totalValueInvoice;
    }

    /**
     * @return the totalValueInvoice
     */
    public Double getTotalValueInvoice() {
        return totalValueInvoice;
    }
    
	/**
	 * @return the pendingInvoiceValue
	 */
	public Double getPendingInvoiceValue() {
		return pendingInvoiceValue;
	}

	/**
	 * @param pendingInvoiceValue
	 *            the pendingInvoiceValue to set
	 */
	public void setPendingInvoiceValue(Double pendingInvoiceValue) {
		this.pendingInvoiceValue = pendingInvoiceValue;
	}

	/**
     * Implementação do metodo de comparação.
     * 
     * @param otherRow
     *            - entidade do tipo FaturaReceitaRow.
     * 
     * @return a comparação dos dois objetos
     */
    @Override
    public int compareTo(final FaturaReceitaRow otherRow) {

        // ordena pela data
        if (this.faturaReceita.getFatura().getDataPrevisao().before(
                otherRow.getFaturaReceita().getFatura().getDataPrevisao())) {
            return -1;
        }

        if (this.faturaReceita.getFatura().getDataPrevisao().after(
                otherRow.getFaturaReceita().getFatura().getDataPrevisao())) {
            return 1;
        }

        return 0;
    }

}