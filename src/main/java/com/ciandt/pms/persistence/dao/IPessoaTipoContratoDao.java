package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaTipoContrato;


/**
 * 
 * A classe IPessoaTipoContratoDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade PessoaTipoContrato.
 * 
 * @since 27/07/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public interface IPessoaTipoContratoDao extends
        IAbstractDao<Long, PessoaTipoContrato> {

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato.
     */
    PessoaTipoContrato findNext(final PessoaTipoContrato pessoaTC);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    PessoaTipoContrato findPrevious(final PessoaTipoContrato pessoaTC);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaTC
     *            - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    PessoaTipoContrato findByStartDate(final PessoaTipoContrato pessoaTC);

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
    PessoaTipoContrato findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);

    List<PessoaTipoContrato> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month);
}