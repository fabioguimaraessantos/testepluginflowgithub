package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PessoaGrupoCustoMG;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoMGDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.HashMap;

@Repository
public class PessoaGrupoCustoMGDao extends AbstractDao<Long, PessoaGrupoCustoMG>
        implements IPessoaGrupoCustoMGDao {

    public PessoaGrupoCustoMGDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, PessoaGrupoCustoMG.class);
    }

    @Override
    public PessoaGrupoCustoMG findByPessoaAndGrupoCustoAndStartDate(Long codigoPessoa, Long codigoGrupoCusto, Date dataInicio) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("codigoPessoa", codigoPessoa);
        params.put("codigoGrupoCusto", codigoGrupoCusto);
        params.put("dataInicio", dataInicio);
        return firstResultOrNull(getJpaTemplate().findByNamedQueryAndNamedParams(
                PessoaGrupoCustoMG.FIND_BY_PESSOA_GRUPO_CUSTO_START_DATE, params));
    }

    @Override
    public PessoaGrupoCustoMG findPrevious(PessoaGrupoCustoMG existent) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("codigoPessoa", existent.getPessoa().getCodigoPessoa());
        params.put("dataInicio", existent.getDataInicio());
        return firstResultOrNull(getJpaTemplate().findByNamedQueryAndNamedParams(
                PessoaGrupoCustoMG.FIND_PREVIOUS, params));
    }

    @Override
    public PessoaGrupoCustoMG findNext(PessoaGrupoCustoMG existent) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("codigoPessoa", existent.getPessoa().getCodigoPessoa());
        params.put("dataInicio", existent.getDataInicio());
        return firstResultOrNull(getJpaTemplate().findByNamedQueryAndNamedParams(
                PessoaGrupoCustoMG.FIND_NEXT, params));
    }

}