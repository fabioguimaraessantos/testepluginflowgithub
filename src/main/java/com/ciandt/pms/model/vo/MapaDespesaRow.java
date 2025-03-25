package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.DespesaMes;
import com.ciandt.pms.model.TipoDespesa;

import java.io.Serializable;
import java.util.List;


/**
 * 
 * A classe MapaDespesaRow representa a linha do mapa de custos. 
 *
 * @since 03/05/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class MapaDespesaRow implements Serializable {

    /** Default Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Tipo da Despesa. */
    private TipoDespesa tipoDespesa;
    
    /** Lista de Despesa de Credito (reembolso). */
    private List<DespesaMes> despesaCreditoList;
    
    /** Lista de despesa .*/
    private List<DespesaMes> despesaDebitoList;

    /** Lista com o valor calculado do reembolso.*/
    private List<Double> valorReembolsoList;
    
    /** Indicador se o elemento esta selecionado. */
    private boolean isSelected = false;
    
    /** Indicador da unidade da despesa. */
    private String unidadeDespesa;
    
    /** Indicador da unidade de reembolso. */
    private String unidadeReembolso;
    
    /** Total de despesas. */
    private Double totalDespesa;
    
    /** Total de reembolso da despesa. */
    private Double totalReembolso;
    
    /**
     * @return the tipoDespesa
     */
    public TipoDespesa getTipoDespesa() {
        return tipoDespesa;
    }

    /**
     * @param tipoDespesa the tipoDespesa to set
     */
    public void setTipoDespesa(final TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    /**
     * @return the despesaCredito
     */
    public List<DespesaMes> getDespesaCreditoList() {
        return despesaCreditoList;
    }

    /**
     * @param despesaCredito the despesaCredito to set
     */
    public void setDespesaCreditoList(final List<DespesaMes> despesaCredito) {
        this.despesaCreditoList = despesaCredito;
    }

    /**
     * @return the despesaDebito
     */
    public List<DespesaMes> getDespesaDebitoList() {
        return despesaDebitoList;
    }

    /**
     * @param despesaDebito the despesaDebito to set
     */
    public void setDespesaDebitoList(final List<DespesaMes> despesaDebito) {
        this.despesaDebitoList = despesaDebito;
    }
    
    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(final boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }
    
    /**
     * @return the isSelected
     */
    public boolean getIsSelected() {
        return isSelected;
    }

    /**
     * @param valorReembolsoList the valorReembolsoList to set
     */
    public void setValorReembolsoList(
            final List<Double> valorReembolsoList) {
        this.valorReembolsoList = valorReembolsoList;
    }

    /**
     * @return the valorReembolsoList
     */
    public List<Double> getValorReembolsoList() {
        return valorReembolsoList;
    }

    /**
     * @param unidadeDespesa the unidadeDespesa to set
     */
    public void setUnidadeDespesa(final String unidadeDespesa) {
        this.unidadeDespesa = unidadeDespesa;
    }

    /**
     * @return the unidadeDespesa
     */
    public String getUnidadeDespesa() {
        return unidadeDespesa;
    }

    /**
     * @param unidadeReembolso the unidadeReembolso to set
     */
    public void setUnidadeReembolso(final String unidadeReembolso) {
        this.unidadeReembolso = unidadeReembolso;
    }

    /**
     * @return the unidadeReembolso
     */
    public String getUnidadeReembolso() {
        return unidadeReembolso;
    }

    /**
     * @param totalDespesa the totalDespesa to set
     */
    public void setTotalDespesa(final Double totalDespesa) {
        this.totalDespesa = totalDespesa;
    }

    /**
     * @return the totalDespesa
     */
    public Double getTotalDespesa() {
        return totalDespesa;
    }

    /**
     * @param totalReembolso the totalReembolso to set
     */
    public void setTotalReembolso(final Double totalReembolso) {
        this.totalReembolso = totalReembolso;
    }

    /**
     * @return the totalReembolso
     */
    public Double getTotalReembolso() {
        return totalReembolso;
    }
   
}
