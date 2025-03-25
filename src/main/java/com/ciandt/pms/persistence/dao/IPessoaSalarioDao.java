package com.ciandt.pms.persistence.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaSalario;


/**
 * 
 * A classe IPessoaSalarioDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade PessoaSalario.
 * 
 * @since 02/08/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public interface IPessoaSalarioDao extends IAbstractDao<Long, PessoaSalario> {

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario.
     */
    PessoaSalario findNext(final PessoaSalario pessoaSalario);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario anterior.
     */
    PessoaSalario findPrevious(final PessoaSalario pessoaSalario);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaSalario
     *            - entidade do tipo PessoaSalario.
     * 
     * @return retorna o PessoaSalario anterior.
     */
    PessoaSalario findByStartDate(final PessoaSalario pessoaSalario);

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
    PessoaSalario findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);

}