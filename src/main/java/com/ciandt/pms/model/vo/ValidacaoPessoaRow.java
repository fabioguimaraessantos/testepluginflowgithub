package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Pessoa;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 
 * A classe ValidacaoPessoaRow representa uma linha na lista de validação pessoa.
 *
 * @since 08/12/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ValidacaoPessoaRow implements Serializable {
    
    /** Default Serial Version UID. */
    private static final long serialVersionUID = 1L;

    /** Instancia de Pessoa. */
    private Pessoa pessoa;
    
    /** Flag para mes validado. */
    private Boolean isMesValidado = Boolean.valueOf(false);
    
    /** Percentual alocado no mes. */
    private BigDecimal percentualAlocado;

    /** Flag que representa se a linha esta selecionada. */
    private Boolean isSelected = Boolean.valueOf(false);
    
    /** Atributo que indica o percentual alocavel no mes. */
    private BigDecimal percentualDisponivelMes;
    
    /** Indicador status do GrupoCusto associado a Pessoa. */
    private String indStatusPessGrupoCusto;
    
    /**
     * @return the indStatusPessGrupoCusto
     */
    public String getIndStatusPessGrupoCusto() {
        return indStatusPessGrupoCusto;
    }

    /**
     * @param indStatusPessGrupoCusto the indStatusPessGrupoCusto to set
     */
    public void setIndStatusPessGrupoCusto(final String indStatusPessGrupoCusto) {
        this.indStatusPessGrupoCusto = indStatusPessGrupoCusto;
    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(final Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @return the isMesValidado
     */
    public Boolean getIsMesValidado() {
        return isMesValidado;
    }

    /**
     * @param isMesValidado the isMesValidado to set
     */
    public void setIsMesValidado(final Boolean isMesValidado) {
        this.isMesValidado = isMesValidado;
    }

    /**
     * @return the percentualAlocado
     */
    public BigDecimal getPercentualAlocado() {
        return percentualAlocado;
    }

    /**
     * @param percentualAlocado the percentualAlocado to set
     */
    public void setPercentualAlocado(final BigDecimal percentualAlocado) {
        this.percentualAlocado = percentualAlocado;
    }

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
     * @param percentualDisponivelMes the percentualDisponivelMes to set
     */
    public void setPercentualDisponivelMes(
            final BigDecimal percentualDisponivelMes) {
        this.percentualDisponivelMes = percentualDisponivelMes;
    }

    /**
     * @return the percentualDisponivelMes
     */
    public BigDecimal getPercentualDisponivelMes() {
        return percentualDisponivelMes;
    }

}
