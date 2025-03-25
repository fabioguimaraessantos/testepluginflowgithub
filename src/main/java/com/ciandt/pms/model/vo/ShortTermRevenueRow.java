package com.ciandt.pms.model.vo;

import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.model.ReceitaResultado;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Classe que representa a linha de uma justificativa ({@link ReceitaResultado})
 * da {@link ReceitaMoeda}.
 * 
 * @author mvidolin
 * @since 30/10/2012
 */
public class ShortTermRevenueRow implements Serializable {

	/** SerialVersionID. */
	private static final long serialVersionUID = 1L;

	/** Instancia de um {@link ReceitaResultado}. */
	private ReceitaResultado receitaResultado;

	/**
	 * Percentual diferenca entre valor {@link ReceitaMoeda}
	 * planejada/realizada.
	 */
	private BigDecimal percentualDiferenca = BigDecimal.valueOf(0);

	/** Motivo resultado selecionado no combo. */
	private String motivoResultadoSelected = "";

	/**
	 * @return the receitaResultado
	 */
	public ReceitaResultado getReceitaResultado() {
		return receitaResultado;
	}

	/**
	 * @param receitaResultado
	 *            the receitaResultado to set
	 */
	public void setReceitaResultado(final ReceitaResultado receitaResultado) {
		this.receitaResultado = receitaResultado;
	}

	/**
	 * @return the percentualDiferenca
	 */
	public BigDecimal getPercentualDiferenca() {
		return percentualDiferenca;
	}

	/**
	 * @param percentualDiferenca
	 *            the percentualDiferenca to set
	 */
	public void setPercentualDiferenca(final BigDecimal percentualDiferenca) {
		this.percentualDiferenca = percentualDiferenca;
	}

	/**
	 * @return the motivoResultadoSelected
	 */
	public String getMotivoResultadoSelected() {
		return motivoResultadoSelected;
	}

	/**
	 * @param motivoResultadoSelected
	 *            the motivoResultadoSelected to set
	 */
	public void setMotivoResultadoSelected(final String motivoResultadoSelected) {
		this.motivoResultadoSelected = motivoResultadoSelected;
	}

}