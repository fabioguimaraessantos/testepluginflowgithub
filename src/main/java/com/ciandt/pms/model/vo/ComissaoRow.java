package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ComissaoFatura;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 
 * A classe ComissaoRow representa a linha no resultado da busca.
 *
 * @since 11/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ComissaoRow implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** Flag que indica se item está slecionado. */
    private Boolean isSelected = Boolean.FALSE;
    
    /** Entidade do tipo ComissaoAcelerador. */
    private ComissaoAcelerador comissaoAcelerador = new ComissaoAcelerador();
    
    /** Entidade do tipo ComissaoFatura. */
    private ComissaoFatura comissaoFatura = new ComissaoFatura();

    /** Entidade do tipo Comissao. */
    private Comissao comissao;
    
    /** Total acumulado. */
    private BigDecimal totalAcumulado;
    
    /** Valor convertido na moeda padrão. */
    private BigDecimal convertedComissionValue;
    
    /** Valor liquido (sem impostos) comissíonavel. */
    private BigDecimal netValue;
    
    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setIsSelected(final Boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * @return the comissaoAcelerador
     */
    public ComissaoAcelerador getComissaoAcelerador() {
        return comissaoAcelerador;
    }

    /**
     * @param comissaoAcelerador the comissaoAcelerador to set
     */
    public void setComissaoAcelerador(
            final ComissaoAcelerador comissaoAcelerador) {
        
        this.comissaoAcelerador = comissaoAcelerador;
        
        comissao = comissaoAcelerador.getComissao();
    }

    /**
     * @return the comissaoFatura
     */
    public ComissaoFatura getComissaoFatura() {
        return comissaoFatura;
    }

    /**
     * @param comissaoFatura the comissaoFatura to set
     */
    public void setComissaoFatura(
            final ComissaoFatura comissaoFatura) {
        this.comissaoFatura = comissaoFatura;
        
        if (comissaoFatura.getComissao() != null) {
            comissao = comissaoFatura.getComissao();
        }
    }

    /**
     * @param comissao the comissao to set
     */
    public void setComissao(final Comissao comissao) {
        this.comissao = comissao;
        
        ComissaoAcelerador ca = this.getComissaoAcelerador();
        if (ca != null) {
            ca.setComissao(comissao);
        }
        ComissaoFatura cf = this.getComissaoFatura();
        if (ca != null) {
            cf.setComissao(comissao);
        }
        
    }

    /**
     * @return the comissao
     */
    public Comissao getComissao() {
        
        return comissao;
    }

    /**
     * @param totalAcumulado the totalAcumulado to set
     */
    public void setTotalAcumulado(final BigDecimal totalAcumulado) {
        this.totalAcumulado = totalAcumulado;
    }

    /**
     * @return the totalAcumulado
     */
    public BigDecimal getTotalAcumulado() {
        return totalAcumulado;
    }

    /**
     * @param convertedComissionValue the convertedComissionValue to set
     */
    public void setConvertedComissionValue(
            final BigDecimal convertedComissionValue) {
        this.convertedComissionValue = convertedComissionValue;
    }

    /**
     * @return the convertedComissionValue
     */
    public BigDecimal getConvertedComissionValue() {
        return convertedComissionValue;
    }

    /**
     * @param valorLiquido the valorLiquido to set
     */
    public void setNetValue(final BigDecimal valorLiquido) {
        this.netValue = valorLiquido;
    }

    /**
     * @return the valorLiquido
     */
    public BigDecimal getNetValue() {
        return netValue;
    }
}
