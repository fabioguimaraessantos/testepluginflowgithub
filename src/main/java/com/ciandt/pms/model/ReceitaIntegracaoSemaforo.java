package com.ciandt.pms.model;

import javax.persistence.*;

@Entity
@Table(name = "RECEITA_INTEGRACAO_SEMAFORO")
@SqlResultSetMappings(@SqlResultSetMapping(name = "scalarReceitaIntegracaoSemaforo"))
@NamedQueries({
        @NamedQuery(name = "ReceitaIntegracaoSemaforo.findByReceitaDealFiscal", query = "SELECT rts FROM ReceitaIntegracaoSemaforo rts"
                + " JOIN FETCH rts.receitaDealFiscal rdf "
                + " WHERE rdf.codigoReceitaDfiscal = ?")
})
public class ReceitaIntegracaoSemaforo implements java.io.Serializable {

    public static final String FIND_BY_RECEITA_DEAL_FISCAL = "ReceitaIntegracaoSemaforo.findByReceitaDealFiscal";

    @Id
    @GeneratedValue(generator = "ReceitaIntegracaoSemaforoSeq")
    @SequenceGenerator(name = "ReceitaIntegracaoSemaforoSeq", sequenceName = "SQ_REIS_CD_RECEITA_INTEG_SEMAF", allocationSize = 1)
    @Column(name = "REIS_CD_RECEITA_INTEG_SEMAFORO", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoReceitaIntegracaoSemaforo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REDF_CD_RECEITA_DFISCAL", nullable = false)
    private ReceitaDealFiscal receitaDealFiscal;

    public Long getCodigoReceitaIntegracaoSemaforo() {
        return this.codigoReceitaIntegracaoSemaforo;
    }

    public void setCodigoReceitaIntegracaoSemaforo(Long codigoReceitaIntegracaoSemaforo) {
        this.codigoReceitaIntegracaoSemaforo = codigoReceitaIntegracaoSemaforo;
    }

    public ReceitaDealFiscal getReceitaDealFiscal() {
        return this.receitaDealFiscal;
    }

    public void setReceitaDealFiscal(ReceitaDealFiscal receitaDealFiscal) {
        this.receitaDealFiscal = receitaDealFiscal;
    }
}
