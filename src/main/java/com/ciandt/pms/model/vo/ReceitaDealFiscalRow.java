package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ReceitaDealFiscal;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 
 * A classe ReceitaDealFiscalRow utlizada para listar a associção da Revenue com
 * DealFiscal.
 * 
 * @since 05/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class ReceitaDealFiscalRow implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** instancia do TO (ReceitaDealFiscal). */
    private ReceitaDealFiscal to;

    /** Variavel representa se o item esta selecionado. */
    private Boolean isSelected = Boolean.valueOf(false);
    
    /**
     * Flag para indicar se a receita é editavel ou nao (pagina de receita
     * edit).
     */
    private Boolean isReceitaEditavel = Boolean.valueOf(false);

    private Boolean isReceitaPlantaoEditavel = Boolean.valueOf(false);
    
    /** Valor referente ao total de fiscalBalance. */
    private Double publishedFiscalBalance = new Double(0);

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final ReceitaDealFiscal to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public ReceitaDealFiscal getTo() {
        return to;
    }

	/**
     * @param isSelected
     *            the isSelected to set
     */
    public void setIsSelected(final Boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }
    
    /**
     * @return the totalAjusteFiscalDeal
     */
    public BigDecimal getAjusteFiscalDeal() {
    	BigDecimal totalResult = BigDecimal.valueOf(0);
        for (AjusteReceita ajusteReceita : this.to.getAjusteReceitas()) {
			totalResult = totalResult.add(ajusteReceita.getValorAjuste());
		}
        
        return totalResult;
    }

    /**
     * @return the BalancoFiscalDeal
     */
    public BigDecimal getBalancoFiscalDeal() {
        
        BigDecimal totalResult = BigDecimal.valueOf(0);
        
        totalResult = this.to.getValorReceita().add(
                this.getAjusteFiscalDeal());
        if (this.getTo().getReceitaPlantao() != null) {
			totalResult = totalResult.add(this.getTo().getReceitaPlantao().getValorReceitaPlantao());
		}

        return totalResult;
    }

    /**
     * @return the isReceitaPlantaoEditavel
     */
    public Boolean getIsReceitaPlantaoEditavel() {
        return isReceitaPlantaoEditavel;
    }

    /**
     * @param receitaPlantaoEditavel
     *            the receitaEditavel to set
     */
    public void setIsReceitaPlantaoEditavel(final Boolean isReceitaPlantaoEditavel) {
        this.isReceitaPlantaoEditavel = isReceitaPlantaoEditavel;
    }

    /**
     * @return the isReceitaEditavel
     */
    public Boolean getIsReceitaEditavel() {
        return isReceitaEditavel;
    }

    /**
     * @param receitaEditavel
     *            the receitaEditavel to set
     */
    public void setIsReceitaEditavel(final Boolean isReceitaEditavel) {
        this.isReceitaEditavel = isReceitaEditavel;
    }

	/**
	 * @return the publishedFiscalBalance
	 */
	public Double getPublishedFiscalBalance() {
		return publishedFiscalBalance;
	}

	/**
	 * @param publishedFiscalBalance the publishedFiscalBalance to set
	 */
	public void setPublishedFiscalBalance(Double publishedFiscalBalance) {
		this.publishedFiscalBalance = publishedFiscalBalance;
	}
    
    

}
