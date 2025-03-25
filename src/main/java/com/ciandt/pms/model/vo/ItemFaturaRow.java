package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ItemFatura;

/**
 * 
 * A classe ItemFaturaRow representa a linha da lista de item fatura.
 *
 * @since 25/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ItemFaturaRow {

    /** ItemFaturaRow. */
    private ItemFatura itemFatura = new ItemFatura();
    
    /** Indicador selecionado / nao selecionado. */
    private Boolean isSelected = Boolean.FALSE;
    
    /**
     * @param isSelected the isSelected to set
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
     * @param itemFatura the itemFatura to set
     */
    public void setItemFatura(final ItemFatura itemFatura) {
        this.itemFatura = itemFatura;
    }

    /**
     * @return the itemFatura
     */
    public ItemFatura getItemFatura() {
        return itemFatura;
    }

}
