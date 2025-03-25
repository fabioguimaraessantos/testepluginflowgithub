package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.Billability;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBillability;
import com.ciandt.pms.model.PessoaBillabilityId;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface IPessoaBillabilityDao extends
        IAbstractDao<PessoaBillabilityId, PessoaBillability> {

    /**
     * Busca a entidade pelo id (codigoPessoa e codigoBillability).
     *
     * @param codigoPessoa
     *            - codigo da {@link Pessoa}
     * @param codigoBillability
     *            - codigo do {@link Billability}
     *
     * @return retorna a entidade
     */
    PessoaBillability findById(final Long codigoPessoa,
                               final Long codigoBillability,
                               final Date dataInicio);

    /**
     * Busca {@link PessoaBillability} pelo pessoa.
     *
     * @param pessoa
     * @return Lista de {@link Pessoa}
     */
    List<PessoaBillability> findByPessoa(Pessoa pessoa);

    /**
     * Busca {@link PessoaBillability} por data.
     *
     * @param startDate
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndDataInicio(Long codigoPessoa, Date dataInicio);

    /**
     * Busca {@link PessoaBillability} ultima vigencia
     *
     * @param codigoPessoa
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndDataFimIsNull(Long codigoPessoa);

    /**
     * Busca {@link PessoaBillability} por pessoa e data fim
     *
     * @param codigoPessoa
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndDataFim(Long codigoPessoa, Date dataFim);

    /**
     * Busca {@link PessoaBillability} por pessoa e por data entre a vigencia cadastrada
     *
     * @param codigoPessoa
     * @param date
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndDateBetween(Long codigoPessoa, Date date);

    /**
     * Busca {@link PessoaBillability} por pessoa e por data entre a vigencia cadastrada
     *
     * @param codigoPessoa
     * @param date
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndDateExists(Long codigoPessoa, Date date);

    /**
     * Busca {@link PessoaBillability} por pessoa e por data maior ou igual DataInicio
     *
     * @param codigoPessoa
     * @param dataInicio
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndAfterOrEqualDataInicio(Long codigoPessoa, Date dataInicio);

    /**
     * Busca pela Pessoa e pela Data de vigencia.
     *
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     *
     * @return {@link PessoaBillability}
     */
    PessoaBillability findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);

    List<PessoaBillability> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month);
}