package com.ciandt.pms.model.vo;

import com.ciandt.pms.enums.StatusDreProcesso;
import com.ciandt.pms.model.DreProcesso;
import com.ciandt.pms.model.ProcessoDependencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * A classe ProcessoRow proporciona as funcionalidades de VO para Processo
 * Fechamento.
 * 
 * @since 15/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 * 
 */
public class ProcessoRow {

	/** Código do processo */
	private Long codigoProcesso;

	/** Nome do processo fechamento */
	private String nomeProcesso;

	/** Indicador se o processo possui dependência com outro processo. */
	private boolean locked = Boolean.valueOf(false);

	/** Status */
	private StatusDreProcesso statusEnum;
	
	/** Lista de Processos dependentes */
	private List<ProcessoDependencia> processosDependentes = new ArrayList<ProcessoDependencia>();
	
	/** Lista de Processos dependencia */
	private List<ProcessoDependencia> processosDependencias = new ArrayList<ProcessoDependencia>();

	/** Entidade DreProcesso */
	private DreProcesso dreProcesso;

	/** Data MM/yyyy processamento */
	private Date monthYear;

	/**
	 * Default Constructor.
	 */
	public ProcessoRow() {

	}

	/**
	 * @return the codigoProcesso
	 */
	public Long getCodigoProcesso() {
		return codigoProcesso;
	}

	/**
	 * @param codigoProcesso
	 *            the codigoProcesso to set
	 */
	public void setCodigoProcesso(Long codigoProcesso) {
		this.codigoProcesso = codigoProcesso;
	}

	/**
	 * @return the nomeProcesso
	 */
	public String getNomeProcesso() {
		return nomeProcesso;
	}

	/**
	 * @param nomeProcesso
	 *            the nomeProcesso to set
	 */
	public void setNomeProcesso(String nomeProcesso) {
		this.nomeProcesso = nomeProcesso;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the statusEnum
	 */
	public StatusDreProcesso getStatusEnum() {
		return statusEnum;
	}

	/**
	 * @param statusEnum
	 *            the statusEnum to set
	 */
	public void setStatusEnum(StatusDreProcesso statusEnum) {
		this.statusEnum = statusEnum;
	}

	/**
	 * @return the processosDependentes
	 */
	public List<ProcessoDependencia> getProcessosDependentes() {
		return processosDependentes;
	}

	/**
	 * @param processosDependentes
	 *            the processosDependentes to set
	 */
	public void setProcessosDependentes(
			List<ProcessoDependencia> processosDependentes) {
		this.processosDependentes = processosDependentes;
	}

	/**
	 * @return the processosDependencia
	 */
	public List<ProcessoDependencia> getProcessosDependencias() {
		return processosDependencias;
	}

	/**
	 * @param processosDependencia the processosDependencia to set
	 */
	public void setProcessosDependencias(List<ProcessoDependencia> processosDependencia) {
		this.processosDependencias = processosDependencia;
	}

	/**
	 * @return the dreProcesso
	 */
	public DreProcesso getDreProcesso() {
		return dreProcesso;
	}

	/**
	 * @param dreProcesso
	 *            the dreProcesso to set
	 */
	public void setDreProcesso(DreProcesso dreProcesso) {
		this.dreProcesso = dreProcesso;
	}

	/**
	 * @return the monthYear
	 */
	public Date getMonthYear() {
		return monthYear;
	}

	/**
	 * @param monthYear
	 *            the monthYear to set
	 */
	public void setMonthYear(Date monthYear) {
		this.monthYear = monthYear;
	}
}
