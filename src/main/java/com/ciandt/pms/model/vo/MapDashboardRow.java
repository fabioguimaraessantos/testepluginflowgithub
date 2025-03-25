package com.ciandt.pms.model.vo;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Moeda;

import java.io.Serializable;
import java.util.*;

/**
 * 
 * A classe MapDashboardRow representa uma linha do dashboard do mapa de
 * alocação.
 * 
 * @since 05/01/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public class MapDashboardRow implements Serializable {

	/** Default serial Version UID. */
	private static final long serialVersionUID = 1L;

	/** Data (mes/ano) da linha do Dashboard. */
	private Date dataMes = new Date();

	/** Total de FTE da linha do Dashboard. */
	private Double fteTotal;

	/** Média de UR. */
	private Double averageUtilizationRate;

	/** Indicador - true realizado / false planejado. */
	private Boolean isActual = Boolean.valueOf(false);

	/** Indicador do estilo css a ser aplicado na linha. */
	private String styleClass = "";

	/** Map de Moedas para este mês. */
	private SortedMap<String, MapDashboardMoeda> mapDashboardMoedaMap = new TreeMap<String, MapDashboardMoeda>();

	private HashMap<Moeda, Double> mapMoeda = new HashMap<Moeda, Double>();

	/** Construtor padrão. */
	public MapDashboardRow() {
	}

	/**
	 * Construtor padrão.
	 * 
	 * @param dataMes
	 *            - data (mes/ano) da linha do Dashboard
	 * @param isActual
	 *            - indicador se eh Realizado ou nao
	 */
	public MapDashboardRow(final Date dataMes, final Boolean isActual) {
		this.dataMes = dataMes;
		this.isActual = isActual;
		if (isActual) {
			this.styleClass = BundleUtil
					.getBundle(Constants.BUNDLE_KEY_LABEL_STYLE_INACTIVE_COLOR);
		}
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
	 * @return the dataMes
	 */
	public Date getDataMes() {
		return dataMes;
	}

	/**
	 * @param dataMes
	 *            the dataMes to set
	 */
	public void setDataMes(final Date dataMes) {
		this.dataMes = dataMes;
	}

	/**
	 * @return the isActual
	 */
	public Boolean getIsActual() {
		return isActual;
	}

	/**
	 * @param isActual
	 *            the isActual to set
	 */
	public void setIsActual(final Boolean isActual) {
		this.isActual = isActual;
	}

	/**
	 * @return the mapDashboardMoedaMap
	 */
	public SortedMap<String, MapDashboardMoeda> getMapDashboardMoedaMap() {
		return mapDashboardMoedaMap;
	}

	/**
	 * @param mapDashboardMoedaMap
	 *            the mapDashboardMoedaMap to set
	 */
	public void setMapDashboardMoedaMap(
			final SortedMap<String, MapDashboardMoeda> mapDashboardMoedaMap) {
		this.mapDashboardMoedaMap = mapDashboardMoedaMap;
	}

	/**
	 * Retorna as propriedades do MapDashboardMoeda em um map de chave Integer.
	 * 
	 * @return propriedades do MapDashboardMoeda
	 */
	public Map<Integer, Double> getMapDashboardMoedaMapInteger() {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		Integer i = 0;
		for (MapDashboardMoeda m : this.mapDashboardMoedaMap.values()) {
			map.put(i, m.getRevenueTotal());
			i++;
		}
		return map;
	}

	/**
	 * Retorna a Moeda do MapDashboardMoeda em um map de chave Integer.
	 * 
	 * @return moeda do MapDashboardMoeda
	 */
	public Map<Integer, Moeda> getMoedaMapDashboardMoeda() {
		Map<Integer, Moeda> map = new HashMap<Integer, Moeda>();
		Integer i = 0;
		for (MapDashboardMoeda m : this.mapDashboardMoedaMap.values()) {
			map.put(i, m.getMoeda());
			i++;
		}
		return map;
	}

	/**
	 * @return the fteTotal
	 */
	public Double getFteTotal() {
		return fteTotal;
	}

	/**
	 * @param fteTotal
	 *            the fteTotal to set
	 */
	public void setFteTotal(final Double fteTotal) {
		this.fteTotal = fteTotal;
	}

	/**
	 * @return the averageUtilizationRate
	 */
	public Double getAverageUtilizationRate() {
		return averageUtilizationRate;
	}

	/**
	 * @param averageUtilizationRate
	 *            the averageUtilizationRate to set
	 */
	public void setAverageUtilizationRate(final Double averageUtilizationRate) {
		this.averageUtilizationRate = averageUtilizationRate;
	}

	/**
	 * @return the mapMoeda
	 */
	public HashMap<Moeda, Double> getMapMoeda() {
		return mapMoeda;
	}

	/**
	 * @param mapMoeda
	 *            the mapMoeda to set
	 */
	public void setMapMoeda(HashMap<Moeda, Double> mapMoeda) {
		this.mapMoeda = mapMoeda;
	}

}