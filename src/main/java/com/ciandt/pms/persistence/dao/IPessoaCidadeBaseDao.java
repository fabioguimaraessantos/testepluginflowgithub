package com.ciandt.pms.persistence.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaCidadeBase;


/**
 * 
 * A classe IPessoaCidadeBaseDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade PessoaCidadeBase.
 * 
 * @since 06/06/2011
 * @author cmantovani
 * 
 */
@Repository
public interface IPessoaCidadeBaseDao extends
        IAbstractDao<Long, PessoaCidadeBase> {

    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase.
     */
    PessoaCidadeBase findNext(final PessoaCidadeBase pessoaCidadeBase);

    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase anterior.
     */
    PessoaCidadeBase findPrevious(final PessoaCidadeBase pessoaCidadeBase);

    /**
     * Busca pelo data de inicio e pessoa.
     * 
     * @param pessoaCidadeBase
     *            - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase anterior.
     */
    PessoaCidadeBase findByStartDate(final PessoaCidadeBase pessoaCidadeBase);

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
    PessoaCidadeBase findByPessoaAndDate(final Pessoa pessoa, final Date dataMes);
    
}