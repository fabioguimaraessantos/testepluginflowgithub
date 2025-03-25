package com.ciandt.pms.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by josef on 01/11/2017.
 */
@Entity
@Table(name = "DIAS_UTEIS_CIDADE_BASE")
@NamedQueries({
        @NamedQuery(name = "DiasUteisCidadeBase.findByCidadeBaseAndMonth", query = "SELECT t FROM DiasUteisCidadeBase t "
                + "WHERE t.codigoCidadeBase = ? AND t.dataMes = ? ")

    })

public class DiasUteisCidadeBase implements java.io.Serializable {

    /** Constante para NamedQuery "DiasUteisCidadeBase.findByCidadeBaseAndMonth". */
    public static final String FIND_BY_CIDADE_BASE_MONTH = "DiasUteisCidadeBase.findByCidadeBaseAndMonth";

    @Id
    @GeneratedValue(generator = "DiasUteisCidadeBaseSeq")
    @SequenceGenerator(name = "DiasUteisCidadeBaseSeq", sequenceName = "SQ_DUCB_CD_DU_CIDADE_BASE", allocationSize = 1)
    @Column(name = "DUCB_CD_DU_CIDADE_BASE", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoDiasUteisCidadeBase;

    /**
     * Atributo gerado a partir da coluna CIBA_CD_CIDADE_BASE.
     */
    @Column(name = "CIBA_CD_CIDADE_BASE")
    private Long codigoCidadeBase;

    /**
     * Atributo gerado a partir da coluna DUCB_DT_MES.
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DUCB_DT_MES", nullable = false, length = 7)
    private Date dataMes;

    /**
     * Atributo gerado a partir da coluna DUCB_QTDE_DIAS_UTEIS.
     */
    @Column(name = "DUCB_QTDE_DIAS_UTEIS")
    private Long quantidadeDiasUteis;


    public void setCodigoCidadeBase(Long codigoCidadeBase){this.codigoCidadeBase = codigoCidadeBase;}
    public Long getCodigoCidadeBase() {return this.codigoCidadeBase;}

    public void setDataMes (Date dataMes){this.dataMes = dataMes;}
    public Date getDataMes () {return this.dataMes;}

    public void setQuantidadeDiasUteis (Long quantidadeDiasUteis) { this.quantidadeDiasUteis = quantidadeDiasUteis;}
    public Long getQuantidadeDiasUteis () {return this.quantidadeDiasUteis;}
}
