package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * 
 * A classe IPessoaGrupoCustoDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade PessoaGrupoCusto.
 * 
 * @since 15/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public interface IPessoaGrupoCustoDao extends
        IAbstractDao<Long, PessoaGrupoCusto> {

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto.
     */
    PessoaGrupoCusto findNext(final PessoaGrupoCusto pessoaGc);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    PessoaGrupoCusto findPrevious(final PessoaGrupoCusto pessoaGc);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaGc
     *            - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    PessoaGrupoCusto findByStartDate(final PessoaGrupoCusto pessoaGc);

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
    PessoaGrupoCusto findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);

    List<PessoaGrupoCusto> findByPeopleCodeInAndDate(final Set<Long> peopleCodes, final Date month);
}