package com.ciandt.pms.business.service;

import com.ciandt.pms.model.Billability;
import com.ciandt.pms.model.CpraticaDfiscal;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBillability;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * A classe IPessoaBillabilityService proporciona a interface de acesso a camada de
 * serviço referente a entidade PessoaBillability.
 *
 */
@Service
public interface IPessoaBillabilityService {

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
    PessoaBillability findPessoaBillabilityById(final Long codigoPessoa,
                                              final Long codigoBillability,
                                              final Date dataInicio);

    /**
     * Remove a entity.
     *
     * @param pessoaBillability
     *            the {@link PessoaBillability}
     */
    void removePessoaBillability(final PessoaBillability pessoaBillability);

    /**
     * Add a entity.
     *
     * @param pessoaBillability
     *            the {@link PessoaBillability}
     */
    void createPessoaBillability(final PessoaBillability pessoaBillability);

    /**
     * Busca {@link CpraticaDfiscal} pelo codigoContratoPratica.
     *
     * @param pessoa
     * @return Lista de {@link PessoaBillability}
     */
    List<PessoaBillability> findByPessoa(Pessoa pessoa);

    /**
     * Aplica as validações necessarias antes de criar um novo registro
     *
     * @param pessoaBillability
     * @return true se for valido criar um novo registro
     */
    boolean isValidToCreate(PessoaBillability pessoaBillability);

    /**
     * Busca pela Pessoa e pela Data de vigencia.
     *
     * @param pessoa
     *            - Pessoa utilizado na busca.
     * @param dataMes
     *            - data da vigencia.
     *
     * @return a entidade que atende aos criterios do filtro
     */
    PessoaBillability findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);

    void addPessoaBillability(final PessoaBillability pessoaBillability);

    List<PessoaBillability> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month);

}