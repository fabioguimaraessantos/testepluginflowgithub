package com.ciandt.pms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "GRUPO_CUSTO_STATUS")
@NamedQueries({
        @NamedQuery(name = "GrupoCustoStatus.findAll", query = "SELECT t FROM GrupoCustoStatus t"),
        @NamedQuery(name = "GrupoCustoStatus.findAllActive", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.indicadorAtivo = 'A'"),
        @NamedQuery(name = "GrupoCustoStatus.findBySigla", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.siglaStatusGrupoCusto = :siglaStatusGrupoCusto"),
        @NamedQuery(name = "GrupoCustoStatus.findByStatusActiveAndRequestInac", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.siglaStatusGrupoCusto = :siglaActive OR t.siglaStatusGrupoCusto = :siglaRequestInact"),
        @NamedQuery(name = "GrupoCustoStatus.findByStatusRECRAndINCR", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.siglaStatusGrupoCusto = :siglaRequestCreation OR t.siglaStatusGrupoCusto = :siglaIntegCreation"),
        @NamedQuery(name = "GrupoCustoStatus.findByStatusACTIAndCLOSAndREINAndININ", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.siglaStatusGrupoCusto = :siglaActive OR t.siglaStatusGrupoCusto = :siglaClosed OR" +
                " t.siglaStatusGrupoCusto = :siglaRequestInact OR t.siglaStatusGrupoCusto = :siglaIntegInactivation"),
        @NamedQuery(name = "GrupoCustoStatus.findByStatusINAC", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.siglaStatusGrupoCusto = :siglaInactive"),
        @NamedQuery(name = "GrupoCustoStatus.findBySiglaInList", query = "SELECT t FROM GrupoCustoStatus t"
                + " WHERE t.siglaStatusGrupoCusto IN :listSigla")
})
public class GrupoCustoStatus implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "GrupoCustoStatus.findAll";

    public static final String FIND_ALL_ACTIVE = "GrupoCustoStatus.findAllActive";

    public static final String FIND_BY_SIGLA = "GrupoCustoStatus.findBySigla";

    public static final String FIND_BY_ACTIVE_AND_REQUEST_INAC = "GrupoCustoStatus.findByStatusActiveAndRequestInac";

    public static final String FIND_BY_REQ_CREAT_AND_INTEG_CREAT = "GrupoCustoStatus.findByStatusRECRAndINCR";

    public static final String FIND_BY_ACTI_AND_CLOS_AND_REQ_INAC_AND_INTEG_INAC = "GrupoCustoStatus.findByStatusACTIAndCLOSAndREINAndININ";

    public static final String FIND_BY_INAC = "GrupoCustoStatus.findByStatusINAC";

    public static final String FIND_BY_SIGLA_LIST = "GrupoCustoStatus.findBySiglaInList";

    @Id
    @GeneratedValue(generator = "GrupoCustoStatusSeq")
    @SequenceGenerator(name = "GrupoCustoStatusSeq", sequenceName = "SQ_GRCS_CD_GRUPO_CUSTO_STATUS", allocationSize = 1)
    @Column(name = "GRCS_CD_GRUPO_CUSTO_STATUS", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoGrupoCustoStatus;

    @Column(name = "GRCS_NM_GRUPO_CUSTO_STATUS", length = 50)
    private String nomeStatus;

    @Column(name = "GRCS_IN_STATUS", length = 1)
    private String indicadorAtivo;

    @Column(name = "GRCS_SG_GRUPO_CUSTO_STATUS")
    private String siglaStatusGrupoCusto;

    @Column(name = "GRCS_MAP_GRCU_IN_ATIVO", length = 1)
    private String mapIndicadorAtivoGrupoCusto;


    /**
     * Construtor default.
     */
    public GrupoCustoStatus() {}

    public GrupoCustoStatus(Long codigoGrupoCustoStatus) {
        this.codigoGrupoCustoStatus = codigoGrupoCustoStatus;
    }

    public GrupoCustoStatus(Long codigoGrupoCustoStatus, String nomeStatus, String indicadorAtivo,
                            String siglaStatusGrupoCusto, String mapIndicadorAtivoGrupoCusto) {
        this.codigoGrupoCustoStatus = codigoGrupoCustoStatus;
        this.nomeStatus = nomeStatus;
        this.indicadorAtivo = indicadorAtivo;
        this.siglaStatusGrupoCusto = siglaStatusGrupoCusto;
        this.mapIndicadorAtivoGrupoCusto = mapIndicadorAtivoGrupoCusto;
    }

}
