package com.ciandt.pms.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "MODELO_AREA_ORCAMENTARIA")
@NamedQueries({
        @NamedQuery(name=ModeloAreaOrcamentaria.FIND_ALL, query="SELECT ma FROM ModeloAreaOrcamentaria ma"),
        @NamedQuery(name=ModeloAreaOrcamentaria.FIND_CURRENT_BY_AREA_ORCAMENTARIA, query="SELECT ma FROM ModeloAreaOrcamentaria ma WHERE ma.areaOrcamentaria.codigoAreaOrcamentaria = :areaOrcamentaria and ma.dataFim is null")
})
public class ModeloAreaOrcamentaria implements Serializable {

    public static final String FIND_ALL = "ModeloAreaOrcamentaria.findAll";
    public static final String FIND_CURRENT_BY_AREA_ORCAMENTARIA = "ModeloAreaOrcamentaria.findCurrentByAreaOrcamentaria";

    @Id
    @GeneratedValue(generator = "modeloAreaOrcamentariaSeq")
    @SequenceGenerator(name = "modeloAreaOrcamentariaSeq", sequenceName = "SQ_MOAR_CD_MODELO_AREA_ORCA", allocationSize = 1)
    @Column(name = "MOAR_CD_MODELO_AREA_ORCA", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoModeloAreaOrcamentaria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MODE_CD_MODELO")
    private Modelo modelo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AROR_CD_AREA_ORCAMENTARIA")
    private AreaOrcamentaria areaOrcamentaria;

    @Temporal(TemporalType.DATE)
    @Column(name = "MOAR_DT_INICIO", length = 7)
    private Date dataInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "MOAR_DT_FIM", length = 7)
    private Date dataFim;

    public Long getCodigoModeloAreaOrcamentaria() {
        return codigoModeloAreaOrcamentaria;
    }

    public void setCodigoModeloAreaOrcamentaria(Long codigoModeloAreaOrcamentaria) {
        this.codigoModeloAreaOrcamentaria = codigoModeloAreaOrcamentaria;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public AreaOrcamentaria getAreaOrcamentaria() {
        return areaOrcamentaria;
    }

    public void setAreaOrcamentaria(AreaOrcamentaria areaOrcamentaria) {
        this.areaOrcamentaria = areaOrcamentaria;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }
}
