package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PessoaGrupoCustoMG;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IPessoaGrupoCustoMGDao extends IAbstractDao<Long, PessoaGrupoCustoMG> {

    /**
     * Finds association {@link PessoaGrupoCustoMG} by params
     * @param codigoPessoa {@link com.ciandt.pms.model.Pessoa} unique code
     * @param codigoGrupoCusto {@link com.ciandt.pms.model.GrupoCusto} unique code
     * @param dataInicio association start date
     * @return {@link PessoaGrupoCustoMG} if existent
     */
    PessoaGrupoCustoMG findByPessoaAndGrupoCustoAndStartDate(Long codigoPessoa, Long codigoGrupoCusto, Date dataInicio);

    /**
     * Finds previous association based on people and start date
     * @param existent {@link PessoaGrupoCustoMG} existent to compare
     * @return previous {@link PessoaGrupoCustoMG} of this person before existent start date
     */
    PessoaGrupoCustoMG findPrevious(PessoaGrupoCustoMG existent);

    /**
     * Finds next association based on people and start date
     * @param existent {@link PessoaGrupoCustoMG} existent to compare
     * @return next {@link PessoaGrupoCustoMG} of this person after existent start date
     */
    PessoaGrupoCustoMG findNext(PessoaGrupoCustoMG existent);
}