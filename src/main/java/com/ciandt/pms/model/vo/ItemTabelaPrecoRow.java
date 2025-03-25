package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.ItemTabelaPreco;

import javax.persistence.Transient;


/**
 * 
 * A classe ItemTabelaPrecoRow proporciona as funcionalidades de ... para ...
 *
 * @since 26/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public class ItemTabelaPrecoRow {
    
    /** ItemTabelaPreco. */
    private ItemTabelaPreco itemTabelaPreco;
    
    /** Nome do cargo. */
    private String nomeCargo;
    
    /** Valor em fte. */
    private float fte = 0;

    /** Indica se o item foi editado. */
    private boolean edited;

    /** Indica se o item foi removido. */
    private boolean removed;

    /** Boolean para checkbox de item tabela de preço aprovado. */

    @Transient
    private Boolean isApproved = Boolean.FALSE;

    /**
     * @return the itemTabelaPreco
     */
    public ItemTabelaPreco getItemTabelaPreco() {
        return itemTabelaPreco;
    }

    /**
     * @param itemTabelaPreco the itemTabelaPreco to set
     */
    public void setItemTabelaPreco(final ItemTabelaPreco itemTabelaPreco) {
        this.itemTabelaPreco = itemTabelaPreco;
    }

    /**
     * @return the fte
     */
    public float getFte() {
        return fte;
    }

    /**
     * @param fte the fte to set
     */
    public void setFte(final float fte) {
        this.fte = fte;
    }

    /**
     * @return the nomeCargo
     */
    public String getNomeCargo() {
        return nomeCargo;
    }

    /**
     * @param nomeCargo the nomeCargo to set
     */
    public void setNomeCargo(final String nomeCargo) {
        this.nomeCargo = nomeCargo;
    }

    /**
     * @return the isApproved
     */
    public Boolean getApproved() {
        return isApproved;
    }

    /**
     * @param isApproved the isApproved to set
     */
    public void setApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * @return the edited
     */
    public boolean isEdited() {
        return edited;
    }

    /**
     * @param edited the edited to set
     */
    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    /**
     * @return the removed
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * @param removed the removed to set
     */
    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    /**
     * Background color based on item state (e.g. edited or approved)
     * @return Warning color for edited items
     */
    public String getBackgroundColor() {
        return BundleUtil.getBundle(
                isEdited() ? Constants.PRICE_TABLE_ITEM_EDITED_BG_COLOR : Constants.PRICE_TABLE_ITEM_DEFAULT_BG_COLOR);
    }

    /**
     * Background color based on item state (e.g. removed)
     * @return Warning color for edited items
     */
    public String getRemovedBackgroundColor() {
        return BundleUtil.getBundle(
                isRemoved() ? Constants.PRICE_TABLE_ITEM_REMOVED_BG_COLOR : Constants.PRICE_TABLE_ITEM_DEFAULT_BG_COLOR);
    }
}
