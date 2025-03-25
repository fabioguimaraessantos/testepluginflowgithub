/*
 * @(#) ReceitaFilter.java
 * Copyright (c) 2010 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ReceitaMoeda;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 
 * A classe ReceitaFilter proporciona é utlizado uma VO na tela de filtro das
 * receitas.
 * 
 * @since 08/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public class ReceitaFilter implements java.io.Serializable,
        Comparable<ReceitaFilter> {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Atributo gerado a partir da coluna COPR_CD_CONTRATO_PRATICA. */
    private Long codigoContratoPratica;

    /** Atributo gerado a partir da coluna COPR_NM_CONTRATO_PRATICA. */
    private String nomeContratoPratica;

    /** Atributo gerado a partir da coluna RECE_CD_RECEITA. */
    private Long codigoReceita;

    /** Atributo gerado a partir da coluna RECE_DT_MES. */
    private Date dataMes;

    /** Atributo gerado a partir da coluna CLIE_CD_CLIENTE. */
    private Long codigoCliente;

    /** Atributo gerado a partir da coluna CLIE_NM_CLIENTE. */
    private String nomeCliente;

    /** Atributo gerado a partir da coluna NACL_CD_NATUREZA. */
    private Long codigoNatureza;

    /** Atributo gerado a partir da coluna CELU_CD_CENTRO_LUCRO. */
    private Long codigoCentroLucro;

    /** Atributo gerado a partir da coluna PRAT_CD_PRATICA. */
    private Long codigoPratica;

    /** Atributo gerado a partir da coluna CELU_NM_CENTRO_LUCRO. */
    private String nomeCentroLucro;

    /** Atributo gerado a partir da coluna PRAT_NM_PRATICA. */
    private String nomePratica;

    /** Atributo gerado a partir da coluna NACL_NM_NATUREZA. */
    private String nomeNatureza;

    /** Atributo gerado a partir da coluna RECE_IN_VERSAO. */
    private String indicadorVersao;

    /** Indicador se a receita ja teve UR estratificada. */
    private boolean stratified = Boolean.valueOf(false);
    
    /** Lista de Receita Moedas da Receita. */
    private List<ReceitaMoeda> receitaMoedas = new ArrayList<ReceitaMoeda>();
    
    /** Número de linhas que devem ser espandidas. */
    private Integer rowspan = 1;
    
    /** Flag que indica se a linha deve ser mostrada. */
    private Boolean showRow = Boolean.valueOf(true);
    
    /** Receita Moeda desta receita. */
    private ReceitaMoeda receitaMoeda = new ReceitaMoeda();
    
    /** Nome Moeda. */
    private String nomeMoeda = "";

    /** Tipo Receita. */
    private String tipoReceita = Constants.RECEITA_TYPE_SERVICE;

    private Double valorReceita = 0.0;

    private Boolean afterClosingDate = false;

    /**
     * @return the stratified
     */
    public boolean isStratified() {
        return stratified;
    }

    /**
     * @param stratified
     *            the stratified to set
     */
    public void setStratified(final boolean stratified) {
        this.stratified = stratified;
    }

    /**
     * Obtem o valor do atributo codigoContratoPratica.<BR>
     * 
     * @return Valor do atributo codigoContratoPratica.
     */
    public Long getCodigoContratoPratica() {
        return this.codigoContratoPratica;
    }

    /**
     * Atualiza o valor do atributo codigoContratoPratica.<BR>
     * 
     * @param codigoContratoPratica
     *            Novo valor para o atributo codigoContratoPratica.
     */
    public void setCodigoContratoPratica(final Long codigoContratoPratica) {
        this.codigoContratoPratica = codigoContratoPratica;
    }

    /**
     * Obtem o valor do atributo nomeContratoPratica.<BR>
     * 
     * @return Valor do atributo nomeContratoPratica.
     */
    public String getNomeContratoPratica() {
        return this.nomeContratoPratica;
    }

    /**
     * Atualiza o valor do atributo nomeContratoPratica.<BR>
     * 
     * @param nomeContratoPratica
     *            Novo valor para o atributo nomeContratoPratica.
     */
    public void setNomeContratoPratica(final String nomeContratoPratica) {
        this.nomeContratoPratica = nomeContratoPratica;
    }

    /**
     * Obtem o valor do atributo codigoReceita.<BR>
     * 
     * @return Valor do atributo codigoReceita.
     */
    public Long getCodigoReceita() {
        return this.codigoReceita;
    }

    /**
     * Atualiza o valor do atributo codigoReceita.<BR>
     * 
     * @param codigoReceita
     *            Novo valor para o atributo codigoReceita.
     */
    public void setCodigoReceita(final Long codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    /**
     * Obtem o valor do atributo dataMes.<BR>
     * 
     * @return Valor do atributo dataMes.
     */
    public Date getDataMes() {
        return this.dataMes;
    }

    /**
     * Atualiza o valor do atributo dataMes.<BR>
     * 
     * @param dataMes
     *            Novo valor para o atributo dataMes.
     */
    public void setDataMes(final Date dataMes) {
        this.dataMes = dataMes;
    }

    /**
     * Obtem o valor do atributo codigoCliente.<BR>
     * 
     * @return Valor do atributo codigoCliente.
     */
    public Long getCodigoCliente() {
        return this.codigoCliente;
    }

    /**
     * Atualiza o valor do atributo codigoCliente.<BR>
     * 
     * @param codigoCliente
     *            Novo valor para o atributo codigoCliente.
     */
    public void setCodigoCliente(final Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    /**
     * Obtem o valor do atributo nomeCliente.<BR>
     * 
     * @return Valor do atributo nomeCliente.
     */
    public String getNomeCliente() {
        return this.nomeCliente;
    }

    /**
     * Atualiza o valor do atributo nomeCliente.<BR>
     * 
     * @param nomeCliente
     *            Novo valor para o atributo nomeCliente.
     */
    public void setNomeCliente(final String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * Obtem o valor do atributo codigoNatureza.<BR>
     * 
     * @return Valor do atributo codigoNatureza.
     */
    public Long getCodigoNatureza() {
        return this.codigoNatureza;
    }

    /**
     * Atualiza o valor do atributo codigoNatureza.<BR>
     * 
     * @param codigoNatureza
     *            Novo valor para o atributo codigoNatureza.
     */
    public void setCodigoNatureza(final Long codigoNatureza) {
        this.codigoNatureza = codigoNatureza;
    }

    /**
     * Obtem o valor do atributo codigoCentroLucro.<BR>
     * 
     * @return Valor do atributo codigoCentroLucro.
     */
    public Long getCodigoCentroLucro() {
        return this.codigoCentroLucro;
    }

    /**
     * Atualiza o valor do atributo codigoCentroLucro.<BR>
     * 
     * @param codigoCentroLucro
     *            Novo valor para o atributo codigoCentroLucro.
     */
    public void setCodigoCentroLucro(final Long codigoCentroLucro) {
        this.codigoCentroLucro = codigoCentroLucro;
    }

    /**
     * Obtem o valor do atributo codigoPratica.<BR>
     * 
     * @return Valor do atributo codigoPratica.
     */
    public Long getCodigoPratica() {
        return this.codigoPratica;
    }

    /**
     * Atualiza o valor do atributo codigoPratica.<BR>
     * 
     * @param codigoPratica
     *            Novo valor para o atributo codigoPratica.
     */
    public void setCodigoPratica(final Long codigoPratica) {
        this.codigoPratica = codigoPratica;
    }

    /**
     * Obtem o valor do atributo nomeCentroLucro.<BR>
     * 
     * @return Valor do atributo nomeCentroLucro.
     */
    public String getNomeCentroLucro() {
        return this.nomeCentroLucro;
    }

    /**
     * Atualiza o valor do atributo nomeCentroLucro.<BR>
     * 
     * @param nomeCentroLucro
     *            Novo valor para o atributo nomeCentroLucro.
     */
    public void setNomeCentroLucro(final String nomeCentroLucro) {
        this.nomeCentroLucro = nomeCentroLucro;
    }

    /**
     * Obtem o valor do atributo nomePratica.<BR>
     * 
     * @return Valor do atributo nomePratica.
     */
    public String getNomePratica() {
        return this.nomePratica;
    }

    /**
     * Atualiza o valor do atributo nomePratica.<BR>
     * 
     * @param nomePratica
     *            Novo valor para o atributo nomePratica.
     */
    public void setNomePratica(final String nomePratica) {
        this.nomePratica = nomePratica;
    }

    /**
     * Obtem o valor do atributo nomeNatureza.<BR>
     * 
     * @return Valor do atributo nomeNatureza.
     */
    public String getNomeNatureza() {
        return this.nomeNatureza;
    }

    /**
     * Atualiza o valor do atributo nomeNatureza.<BR>
     * 
     * @param nomeNatureza
     *            Novo valor para o atributo nomeNatureza.
     */
    public void setNomeNatureza(final String nomeNatureza) {
        this.nomeNatureza = nomeNatureza;
    }

    /**
     * Obtem o valor do atributo indicadorVersao.<BR>
     * 
     * @return Valor do atributo indicadorVersao.
     */
    public String getIndicadorVersao() {
        return this.indicadorVersao;
    }

    /**
     * Atualiza o valor do atributo indicadorVersao.<BR>
     * 
     * @param indicadorVersao
     *            Novo valor para o atributo indicadorVersao.
     */
    public void setIndicadorVersao(final String indicadorVersao) {
        this.indicadorVersao = indicadorVersao;
    }

    /**
     * Compara duas entidades do tipo ReceitaFilter. Realiza a comparação
     * ordenado pelo cliente e contrato-pratica;
     * 
     * @param rf
     *            entidade do tipo ReceitaFilter.
     * 
     * @return retorna zero se igual, maior que zero se maior, caso contrario
     *         false.
     */
    public int compareTo(final ReceitaFilter rf) {

        int result =
                this.getNomeCliente().compareToIgnoreCase(rf.getNomeCliente());
        if (result == 0) {
            result = this.getNomeContratoPratica().compareToIgnoreCase(
                    rf.getNomeContratoPratica());

            if (result == 0) {
            	if (this.getTipoReceita().equals(Constants.RECEITA_TYPE_ABBREVIATION_LICENCE)) {
            		if (rf.getTipoReceita().equals(Constants.RECEITA_TYPE_ABBREVIATION_LICENCE)) {
						return 1;
					} else {
						return 0;
					}
				}

			}
        }

        return result;

    }

	/**
	 * @return the receitaMoedas
	 */
	public List<ReceitaMoeda> getReceitaMoedas() {
		return receitaMoedas;
	}

	/**
	 * @param receitaMoedas the receitaMoedas to set
	 */
	public void setReceitaMoedas(final List<ReceitaMoeda> receitaMoedas) {
		this.receitaMoedas = receitaMoedas;
	}

	/**
	 * @return the rowspan
	 */
	public Integer getRowspan() {
		return rowspan;
	}

	/**
	 * @param rowspan the rowspan to set
	 */
	public void setRowspan(final Integer rowspan) {
		this.rowspan = rowspan;
	}

	/**
	 * @return the showRow
	 */
	public Boolean getShowRow() {
		return showRow;
	}

	/**
	 * @param showRow the showRow to set
	 */
	public void setShowRow(final Boolean showRow) {
		this.showRow = showRow;
	}

	/**
	 * @return the receitaMoeda
	 */
	public ReceitaMoeda getReceitaMoeda() {
		return receitaMoeda;
	}

	/**
	 * @param receitaMoeda the receitaMoeda to set
	 */
	public void setReceitaMoeda(final ReceitaMoeda receitaMoeda) {
		this.receitaMoeda = receitaMoeda;
	}

	/**
	 * @return the nomeMoeda
	 */
	public String getNomeMoeda() {
		return nomeMoeda;
	}

	/**
	 * @param nomeMoeda the nomeMoeda to set
	 */
	public void setNomeMoeda(String nomeMoeda) {
		this.nomeMoeda = nomeMoeda;
	}

	/**
	 * @return the tipoReceita
	 */
	public String getTipoReceita() {
		return tipoReceita;
	}

	/**
	 * @param tipoReceita the tipoReceita to set
	 */
	public void setTipoReceita(String tipoReceita) {
		this.tipoReceita = tipoReceita;
	}

	/**
	 * @return the valorReceita
	 */
	public Double getValorReceita() {
		return valorReceita;
	}

	/**
	 * @param valorReceita the valorReceita to set
	 */
	public void setValorReceita(Double valorReceita) {
		this.valorReceita = valorReceita;
	}

	/**
	 * @return the afterClosingDate
	 */
	public Boolean getAfterClosingDate() {
		return afterClosingDate;
	}

	/**
	 * @param afterClosingDate the afterClosingDate to set
	 */
	public void setAfterClosingDate(Boolean afterClosingDate) {
		this.afterClosingDate = afterClosingDate;
	}
}