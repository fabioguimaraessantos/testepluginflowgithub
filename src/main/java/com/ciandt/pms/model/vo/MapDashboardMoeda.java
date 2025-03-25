package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.Moeda;

import java.io.Serializable;


/**
 * 
 * A classe MapDashboardMoeda representa cada moeda do dashboard do mapa de
 * alocação.
 * 
 * @since 29/10/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
public class MapDashboardMoeda implements Serializable {

    /** Default serial Version UID. */
    private static final long serialVersionUID = 1L;
    
    /** Moeda do dashboard. */
    private Moeda moeda;
    
    /** Total da ReceitaMoeda. */
    private Double revenueTotal;

    /** Construtor padrão. */
    public MapDashboardMoeda() {
    }

	/**
	 * @return the moeda
	 */
	public Moeda getMoeda() {
		return moeda;
	}

	/**
	 * @param moeda the moeda to set
	 */
	public void setMoeda(final Moeda moeda) {
		this.moeda = moeda;
	}

	/**
	 * @return the revenueTotal
	 */
	public Double getRevenueTotal() {
		return revenueTotal;
	}

	/**
	 * @param revenueTotal the revenueTotal to set
	 */
	public void setRevenueTotal(final Double revenueTotal) {
		this.revenueTotal = revenueTotal;
	}
}