package com.ciandt.pms.model;

import com.ciandt.pms.persistence.dao.jpa.GrupoCustoAreaOrcamentaria;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * The persistent class for the AREA_ORCAMENTARIA database table.
 * 
 */
@Entity
@Table(name="AREA_ORCAMENTARIA")
@NamedQueries({
	@NamedQuery(name="AreaOrcamentaria.findAll", query="SELECT a FROM AreaOrcamentaria a"),
	@NamedQuery(name="AreaOrcamentaria.findAllActiveAreaOrcamentariaPai", query="SELECT a FROM AreaOrcamentaria a "
			+ "WHERE a.areaOrcamentariaPai.codigoAreaOrcamentaria IS NULL"
			+ "  AND a.indicadorStatus = 'A'"),
	@NamedQuery(name="AreaOrcamentaria.findAllAreaOrcamentariaFilho", query="SELECT a FROM AreaOrcamentaria a "
			+ "WHERE a.areaOrcamentariaPai.codigoAreaOrcamentaria IS NOT NULL"),
	@NamedQuery(name="AreaOrcamentaria.findByAreaOrcamentariaPai", query="SELECT a FROM AreaOrcamentaria a "
			+ "WHERE a.areaOrcamentariaPai.codigoAreaOrcamentaria = :codigoAreaOrcamentariaPai"),
	@NamedQuery(name = "AreaOrcamentaria.findByFilter", query = "SELECT ao FROM AreaOrcamentaria ao "
			+ "WHERE (UPPER(ao.nomeAreaOrcamentaria) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
			+ "AND (UPPER(ao.areaOrcamentariaPai.nomeAreaOrcamentaria) = TRIM(UPPER(?)) OR (? is null)) "
			+ "AND (UPPER(ao.indicadorStatus) like '%'||TRIM(UPPER(?))||'%' OR (? is null)) "
			+ "AND ao.areaOrcamentariaPai.nomeAreaOrcamentaria IS NOT NULL "
			+ "ORDER BY ao.nomeAreaOrcamentaria ASC ")
})
public class AreaOrcamentaria implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Constante para NamedQuery "AreaOrcamentaria.findAll".
	 */
	public static final String FIND_ALL = "AreaOrcamentaria.findAll";
	
	/**
	 * Constante para NamedQuery "AreaOrcamentaria.findAllActiveAreaOrcamentariaPai".
	 */
	public static final String FIND_ALL_ACTIVE_AREA_ORCAMENTARIA_PAI = "AreaOrcamentaria.findAllActiveAreaOrcamentariaPai";

	/**
	 * Constante para NamedQuery "AreaOrcamentaria.findAllAreaOrcamentariaFilho".
	 */
	public static final String FIND_ALL_AREA_ORCAMENTARIA_FILHO = "AreaOrcamentaria.findAllAreaOrcamentariaFilho";
	
	/**
	 * Constante para NamedQuery "AreaOrcamentaria.findByAreaOrcamentariaPai".
	 */
	public static final String FIND_BY_AREA_ORCAMENTARIA_PAI = "AreaOrcamentaria.findByAreaOrcamentariaPai";

	public static final String FIND_BY_FILTER = "AreaOrcamentaria.findByFilter";

	@Id
	@GeneratedValue(generator = "AreaOrcamentariaSeq")
	@SequenceGenerator(name="AreaOrcamentariaSeq", sequenceName="SQ_AROR_AREA_ORCAMENTARIA", allocationSize = 1)
	@Column(name="AROR_CD_AREA_ORCAMENTARIA", unique = true, nullable = false, precision = 18, scale = 0)
	private Long codigoAreaOrcamentaria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AROR_CD_AREA_ORCAMENTARIA_PAI")
	private AreaOrcamentaria areaOrcamentariaPai;

	@Column(name="AROR_NM_AREA_ORCAMENTARIA", nullable = false, length = 256)
	private String nomeAreaOrcamentaria;

	@Column(name="AROR_IN_STATUS", nullable = false, length = 1)
	private String indicadorStatus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "areaOrcamentaria")
	private Set<GrupoCustoAreaOrcamentaria> grupoCustoAreaOrcamentarias = 
		new HashSet<GrupoCustoAreaOrcamentaria>(0);

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "areaOrcamentaria")
	private Set<ModeloAreaOrcamentaria> modeloAreaOrcamentaria = new HashSet<>();

	public AreaOrcamentaria() {
	}

	public String getTodosNomesModelos() {
		StringBuilder nomesModelos = new StringBuilder();
		for (ModeloAreaOrcamentaria mao : this.modeloAreaOrcamentaria) {
			if (mao.getModelo() != null) {
				if (nomesModelos.length() > 0) {
					nomesModelos.append(", ");
				}
				nomesModelos.append(mao.getModelo().getNomeModelo());
			}
		}
		return nomesModelos.toString();
	}
	public Set<ModeloAreaOrcamentaria> getModeloAreaOrcamentaria() {
		return modeloAreaOrcamentaria;
	}

	public void setModeloAreaOrcamentaria(Set<ModeloAreaOrcamentaria> modeloAreaOrcamentaria) {
		this.modeloAreaOrcamentaria = modeloAreaOrcamentaria;
	}

	/**
	 * @return the codigoAreaOrcamentaria
	 */
	public Long getCodigoAreaOrcamentaria() {
		return codigoAreaOrcamentaria;
	}

	/**
	 * @param codigoAreaOrcamentaria the codigoAreaOrcamentaria to set
	 */
	public void setCodigoAreaOrcamentaria(Long codigoAreaOrcamentaria) {
		this.codigoAreaOrcamentaria = codigoAreaOrcamentaria;
	}

	/**
	 * @return the areaOrcamentariaPai
	 */
	public AreaOrcamentaria getAreaOrcamentariaPai() {
		return areaOrcamentariaPai;
	}

	/**
	 * @param areaOrcamentariaPai the areaOrcamentariaPai to set
	 */
	public void setAreaOrcamentariaPai(AreaOrcamentaria areaOrcamentariaPai) {
		this.areaOrcamentariaPai = areaOrcamentariaPai;
	}

	/**
	 * @return the nomeAreaOrcamentaria
	 */
	public String getNomeAreaOrcamentaria() {
		return nomeAreaOrcamentaria;
	}

	/**
	 * @param nomeAreaOrcamentaria the nomeAreaOrcamentaria to set
	 */
	public void setNomeAreaOrcamentaria(String nomeAreaOrcamentaria) {
		this.nomeAreaOrcamentaria = nomeAreaOrcamentaria;
	}

	/**
	 * @return the indicadorStatus
	 */
	public String getIndicadorStatus() {
		return indicadorStatus;
	}

	/**
	 * @param indicadorStatus the indicadorStatus to set
	 */
	public void setIndicadorStatus(String indicadorStatus) {
		this.indicadorStatus = indicadorStatus;
	}
}