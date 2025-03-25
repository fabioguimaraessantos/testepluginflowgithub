package com.ciandt.pms.model;

import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "LICENCA_SW_PROJETO_PESSOA")
@NamedQueries({
        @NamedQuery(name = "LicencaSwProjetoPessoa.findAll", query = "SELECT t FROM LicencaSwProjetoPessoa t "
        ),
        @NamedQuery(name = "LicencaSwProjetoPessoa.findByLicencaSwProjeto", query = "SELECT t FROM LicencaSwProjetoPessoa t "
                + "WHERE t.licencaSwProjeto.codigoLicencaSwProjeto = ? "
                + "ORDER BY t.pessoa.codigoLogin"),
        @NamedQuery(name = "LicencaSwProjetoPessoa.findByLicencaSwProjetoAndName", query = "SELECT t FROM LicencaSwProjetoPessoa t "
                + "WHERE t.licencaSwProjeto.codigoLicencaSwProjeto = ? "
                + "AND t.pessoa.codigoLogin = ? ")})
public class LicencaSwProjetoPessoa implements java.io.Serializable {

    public static final String FIND_ALL = "LicencaSwProjetoPessoa.findAll";

    public static final String FIND_BY_LICENCA_SW_PROJETO = "LicencaSwProjetoPessoa.findByLicencaSwProjeto";

    public static final String FIND_BY_LICENCA_SW_PROJETO_AND_NAME = "LicencaSwProjetoPessoa.findByLicencaSwProjetoAndName";


    /**
     * Valor do serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "LicencaSwProjetoPessoaSeq")
    @SequenceGenerator(name = "LicencaSwProjetoPessoaSeq", sequenceName = "SQ_LIPE_CD_LICENCA_SW_PROJ_PE", allocationSize = 1)
    @Column(name = "LIPE_CD_LICENCA_SW_PROJ_PESSOA", unique = true, nullable = false, precision = 18, scale = 0)
    private Long codigoLicencaSwProjetoPessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LIPR_CD_LICENCA_SW_PROJETO", nullable = false)
    private LicencaSwProjeto licencaSwProjeto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PESS_CD_PESSOA", nullable = false)
    private Pessoa pessoa;


    /**
     * Construtor default.
     */
    public LicencaSwProjetoPessoa() {
    }

   public Long getCodigoLicencaSwProjetoPessoa() {
        return codigoLicencaSwProjetoPessoa;
    }

    public void setCodigoLicencaSwProjetoPessoa(Long codigoLicencaSwProjetoPessoa) {
        this.codigoLicencaSwProjetoPessoa = codigoLicencaSwProjetoPessoa;
    }

    public LicencaSwProjeto getLicencaSwProjeto() {
        return licencaSwProjeto;
    }

    public void setLicencaSwProjeto(LicencaSwProjeto licencaSwProjeto) {
        this.licencaSwProjeto = licencaSwProjeto;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
