package com.ciandt.pms.persistence.dao.jpa;

import java.util.*;

import javax.persistence.EntityManagerFactory;

import com.ciandt.pms.model.*;
import com.ciandt.pms.persistence.dao.IPessoaBillabilityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class PessoaBillabilityDao extends AbstractDao<PessoaBillabilityId, PessoaBillability>
        implements IPessoaBillabilityDao {

    /**
     * Construtor padrão.
     *
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public PessoaBillabilityDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PessoaBillability.class);
    }

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
    @Override
    @SuppressWarnings("unchecked")
    public PessoaBillability findById(final Long codigoPessoa,
                                      final Long codigoBillability,
                                      final Date dataInicio) {

        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(
                PessoaBillability.FIND_BY_ID,
                new Object[] { codigoPessoa, codigoBillability, dataInicio });

        if (result.isEmpty()) {
            return null;
        }

        return result.get(0);
    }

    /**
     * Busca {@link PessoaBillability} pelo pessoaBillability.
     *
     * @param pessoa
     * @return Lista de {@link PessoaBillability}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<PessoaBillability> findByPessoa(Pessoa pessoa) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoPessoa", pessoa.getCodigoPessoa());

        List<PessoaBillability> results = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        PessoaBillability.FIND_BY_PESSOA,
                        params);

        return results;
    }

    public PessoaBillability findByPessoaAndDataInicio(Long codigoPessoa, Date startDate) {
        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(PessoaBillability.FIND_BY_PESSOA_AND_STARTDATE, new Object[]{startDate, codigoPessoa});
        if(result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    public PessoaBillability findByPessoaAndDataFimIsNull(Long codigoPessoa) {
        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(PessoaBillability.FIND_BY_PESSOA_AND_ENDDATE_ISNULL, new Object[]{codigoPessoa});
        if(result.size() > 0) {
            PessoaBillability pessoaBillability = new PessoaBillability();
            pessoaBillability = result.get(0);
            return pessoaBillability;
        }
        return null;
    }

    /**
     * Busca {@link PessoaBillability} por pessoa e data fim
     *
     * @param codigoPessoa
     * @para dataFim
     * @return {@link PessoaBillability}
     */
    public PessoaBillability findByPessoaAndDataFim(Long codigoPessoa, Date dataFim) {
        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(PessoaBillability.FIND_BY_PESSOA_AND_ENDDATE, new Object[]{codigoPessoa, dataFim});
        if(result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * Busca {@link PessoaBillability} por pessoa e por data entre a vigencia cadastrada
     *
     * @param codigoPessoa
     * @param date
     * @return {@link PessoaBillability}
     */
    public PessoaBillability findByPessoaAndDateBetween(Long codigoPessoa, Date date) {
        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(PessoaBillability.FIND_BY_PESSOA_AND_DATE, new Object[]{codigoPessoa, date, date});
        if(result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * Busca {@link PessoaBillability} por pessoa e por data entre a vigencia cadastrada
     *
     * @param codigoPessoa
     * @param date
     * @return {@link PessoaBillability}
     */
    public PessoaBillability findByPessoaAndDateExists(Long codigoPessoa, Date date) {
        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(PessoaBillability.FIND_BY_PESSOA_AND_DATE_BETWEEN, new Object[]{codigoPessoa, date});
        if(result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    /**
     * Busca {@link PessoaBillability} por pessoa e por data maior ou igual DataInicio
     *
     * @param codigoPessoa
     * @param dataInicio
     * @return {@link PessoaBillability}
     */
    public PessoaBillability findByPessoaAndAfterOrEqualDataInicio(Long codigoPessoa, Date dataInicio) {
        List<PessoaBillability> result = getJpaTemplate().findByNamedQuery(PessoaBillability.FIND_BY_PESSOA_AND_AFTER_OR_EQUAL_STARTDATE, new Object[]{codigoPessoa, dataInicio});
        if(result.size() > 0) {
            PessoaBillability pessoaBillability = new PessoaBillability();
            pessoaBillability = result.get(0);
            return pessoaBillability;
        }
        return null;
    }
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
    public PessoaBillability findByPessoaAndDate(
            final Pessoa pessoa, final Date dataMes) {

        List<PessoaBillability> listResult = getJpaTemplate()
                .findByNamedQuery(
                        PessoaBillability.FIND_BY_PESSOA_AND_DATE,
                        new Object[] {pessoa.getCodigoPessoa(),
                                dataMes, dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    public List<PessoaBillability> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month) {
        List<Long> codes = new ArrayList<Long>();
        List<PessoaBillability> billabilities = new ArrayList<PessoaBillability>();

        for (Long code : peopleCodes) {
            codes.add(code);
            if (codes.size() >= 999) {
                billabilities.addAll(this.executeFindByPeopleCodeInAndMonth(codes, month));
                codes.clear();
            }
        }
        billabilities.addAll(this.executeFindByPeopleCodeInAndMonth(codes, month));

        return billabilities;
    }

    private List<PessoaBillability> executeFindByPeopleCodeInAndMonth(List<Long> peopleCodes, Date month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("peopleCodes", peopleCodes);
        params.put("month", month);

        return getJpaTemplate().findByNamedQueryAndNamedParams(PessoaBillability.FIND_BY_PESSOA_IN_AND_DATE, params);
    }
}