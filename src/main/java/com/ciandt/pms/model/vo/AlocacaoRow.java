/*
 * @(#) AlocacaoRow.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Alocacao;

import java.util.ArrayList;
import java.util.List;


/**
 * Classe que representa a linha da matriz de Alocacao.
 * 
 */
public class AlocacaoRow implements java.io.Serializable, Comparable<AlocacaoRow> {
 
    /** Valor do serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** Indica por qual  compo se deseja fazer a ordenção. */
    private Integer comparableField = 1;
    
    /** Salva qual foi a ultima ordenção realizada. */
    private Integer lastComparableField = 1;

    /** Index da linha. */
    private Integer rowId = Integer.valueOf(0);

    /** Entidade Alocacao que a linha representa. */
    private Alocacao alocacao = new Alocacao();

    /** Lista de AlocacaoPeriodo relacionado com a Alocacao corrente. */
    private List<AlocacaoCell> alocacaoCellList = new ArrayList<AlocacaoCell>(0);

    /** Indicador do modo em tempo de execucao - remove. */
    private Boolean isRemove = Boolean.valueOf(false);

    /** Indicador do modo em tempo de execucao - view (tags). */
    private Boolean isView = Boolean.TRUE;

    /** Indicador selecionado / nao selecionado. */
    private Boolean isSelected = Boolean.valueOf(false);

    /** Mostra as alocações dos outros mapas. */
    private Boolean showAlocations = Boolean.TRUE;
    
    /** Mostra as alocações dos outros mapas. */
    private Boolean existsAlocation = Boolean.TRUE;

    /**
     * Indicador se alocacao pode ser editada/excluida em tempo de execucao,
     * habilita/desabilita botões editar/excluir Alocacao.
     */
    private Boolean isEnabled = Boolean.TRUE;

    /** Indicador do estilo css a ser aplicado na linha. */
    private String styleClass = "";

    /**
     * Lista de AlocacaoContratoPraticaRow referente a todas as alocacações do
     * recurso em outros contrato-praticas.
     */
    private List<AlocacaoContratoPraticaRow> alocacaoCPRowList = new ArrayList<AlocacaoContratoPraticaRow>(
            0);

    /** Nomes das Etiqueta (tags). */
    private String etiquetaNames = "";

    /** Nomes das Etiqueta (tags) para exibir na tela. */
    private String etiquetaNamesPart = "";

    /** Valor da coluna, para ordenção. */
    private static final int COLUMN_LOGIN = 1;
    /** Valor da coluna, para ordenção. */
    private static final int COLUMN_SITE = 2;
    /** Valor da coluna, para ordenção. */
    private static final int COLUMN_SALE_PROFILE = 3;
    /** Valor da coluna, para ordenção. */
    private static final int COLUMN_STAGE = 4;
    
    /**
     * @return the isView
     */
    public Boolean getIsView() {
        return isView;
    }

    /**
     * @param isView
     *            the isView to set
     */
    public void setIsView(final Boolean isView) {
        this.isView = isView;
    }

    /**
     * @return the etiquetaNames
     */
    public String getEtiquetaNames() {
        return etiquetaNames;
    }

    /**
     * @param etiquetaNames
     *            the etiquetaNames to set
     */
    public void setEtiquetaNames(final String etiquetaNames) {
        this.etiquetaNames = etiquetaNames;
    }

    /**
     * @return the etiquetaNamesPart
     */
    public String getEtiquetaNamesPart() {
        return etiquetaNamesPart;
    }

    /**
     * @param etiquetaNamesPart
     *            the etiquetaNamesPart to set
     */
    public void setEtiquetaNamesPart(final String etiquetaNamesPart) {
        this.etiquetaNamesPart = etiquetaNamesPart;
    }

    /**
     * @return the styleClass
     */
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * @param styleClass
     *            the styleClass to set
     */
    public void setStyleClass(final String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * Construtor padrao.
     * 
     * @param alocacao
     *            - Alocacao da linha corrente da matriz
     */
    public AlocacaoRow(final Alocacao alocacao) {
        this.alocacao = alocacao;
    }
    
    /**
     * Contrutor default.
     */
    public AlocacaoRow() {
        
    }

    /**
     * Construtor padrao.
     * 
     * @param alocacao
     *            - Alocacao da linha corrente da matriz
     * @param rowId
     *            - id da linha da matriz
     */
    public AlocacaoRow(final Alocacao alocacao, final Integer rowId) {
        this.alocacao = alocacao;
        this.rowId = rowId;
    }

    /**
     * @return the alocacaoCellList
     */
    public List<AlocacaoCell> getAlocacaoCellList() {
        return alocacaoCellList;
    }

    /**
     * @param alocacaoCellList
     *            the alocacaoCellList to set
     */
    public void setAlocacaoCellList(final List<AlocacaoCell> alocacaoCellList) {
        this.alocacaoCellList = alocacaoCellList;
    }

    /**
     * @return the rowId
     */
    public Integer getRowId() {
        return rowId;
    }

    /**
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(final Integer rowId) {
        this.rowId = rowId;
    }

    /**
     * @return the alocacao
     */
    public Alocacao getAlocacao() {
        return alocacao;
    }

    /**
     * @param alocacao
     *            the alocacao to set
     */
    public void setAlocacao(final Alocacao alocacao) {
        this.alocacao = alocacao;
    }

    /**
     * @return the isRemove
     */
    public Boolean getIsRemove() {
        return isRemove;
    }

    /**
     * @param isRemove
     *            the isRemove to set
     */
    public void setIsRemove(final Boolean isRemove) {
        this.isRemove = isRemove;
    }

    /**
     * @return the isEnabled
     */
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * @param isEnabled
     *            the isEnabled to set
     */
    public void setIsEnabled(final Boolean isEnabled) {
        this.isEnabled = isEnabled;
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
     * @param alocacaoCPRowList
     *            the alocacaoCPRowList to set
     */
    public void setAlocacaoCPRowList(
            final List<AlocacaoContratoPraticaRow> alocacaoCPRowList) {
        this.alocacaoCPRowList = alocacaoCPRowList;
    }

    /**
     * @return the alocacaoCPRowList
     */
    public List<AlocacaoContratoPraticaRow> getAlocacaoCPRowList() {
        return alocacaoCPRowList;
    }

    /**
     * @param showAlocations
     *            the showAlocations to set
     */
    public void setShowAlocations(final Boolean showAlocations) {
        this.showAlocations = showAlocations;
    }

    /**
     * @return the showAlocations
     */
    public Boolean getShowAlocations() {
        return showAlocations;
    }

    /**
     * @param existsAlocation the existsAlocation to set
     */
    public void setExistsAlocation(final Boolean existsAlocation) {
        this.existsAlocation = existsAlocation;
    }

    /**
     * @return the existsAlocation
     */
    public Boolean getExistsAlocation() {
        return existsAlocation;
    }
    
    /**
     * Implementação do metodo de comparação.
     * 
     * @param row - entidade do tipo AlocacaoRow.
     * 
     * @return a comparação dos dois objetos
     */
    @Override
    public int compareTo(final AlocacaoRow row) {

        lastComparableField = comparableField;
        switch (comparableField) {
            case COLUMN_LOGIN:
                return this.alocacao.getRecurso().
                    getCodigoMnemonico().compareTo(
                            row.alocacao.getRecurso().getCodigoMnemonico());
            case COLUMN_SITE:
                return this.alocacao.getCidadeBase().
                    getNomeCidadeBase().compareTo(
                            row.alocacao.getCidadeBase().getNomeCidadeBase());    
            case COLUMN_SALE_PROFILE:
                return this.alocacao.getPerfilVendido().
                    getNomePerfilVendido().compareTo(
                            row.alocacao.getPerfilVendido().getNomePerfilVendido());
            case COLUMN_STAGE:
                return this.alocacao.getIndicadorEstagio().compareTo(
                        row.alocacao.getIndicadorEstagio());
            default:
                
                return 0;
        }
    }

    /**
     * @param comparableFild the comparableFild to set
     */
    public void setComparableField(final Integer comparableFild) {
        this.comparableField = comparableFild;
    }

    /**
     * @return the comparableFild
     */
    public Integer getComparableField() {
        return comparableField;
    }
    
    /**
     * @return the comparableFild
     */
    public Integer getStaticComparableField() {
        return comparableField;
    }

    /**
     * @return the lastComparableField
     */
    public Integer getLastComparableField() {
        return lastComparableField;
    }

}