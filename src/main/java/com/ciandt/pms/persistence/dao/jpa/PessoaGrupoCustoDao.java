package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaGrupoCusto;
import com.ciandt.pms.persistence.dao.IPessoaGrupoCustoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.*;


/**
 * 
 * A classe PessoaGrupoCustoDao proporciona as funcionalidades da 
 * camada de persistencia referente a entidade PessoaGrupoCusto.
 *
 * @since 15/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class PessoaGrupoCustoDao extends AbstractDao<Long, PessoaGrupoCusto>
        implements IPessoaGrupoCustoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade.
     */
    public PessoaGrupoCustoDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, PessoaGrupoCusto.class);
    }
    
    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaGc - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o proximo PessoaGrupoCusto.
     */
    @SuppressWarnings("unchecked")
    public PessoaGrupoCusto findNext(final PessoaGrupoCusto pessoaGc) {

        Long codigo = pessoaGc.getPessoa().getCodigoPessoa();
        
        List<PessoaGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                PessoaGrupoCusto.FIND_NEXT,
                new Object[] {codigo, codigo,
                    pessoaGc.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaGc - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto anterior.
     */
    @SuppressWarnings("unchecked")
    public PessoaGrupoCusto findPrevious(final PessoaGrupoCusto pessoaGc) {

        Long codigo = pessoaGc.getPessoa().getCodigoPessoa();
        
        List<PessoaGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                PessoaGrupoCusto.FIND_PREVIOUS,
                new Object[] {codigo, codigo,
                    pessoaGc.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo fator de reajuste referente a 
     * data de inicio.
     * 
     * @param pessoaGc - entidade do tipo PessoaGrupoCusto.
     * 
     * @return retorna o PessoaGrupoCusto.
     */
    @SuppressWarnings("unchecked")
    public PessoaGrupoCusto findByStartDate(final PessoaGrupoCusto pessoaGc) {

        List<PessoaGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                PessoaGrupoCusto.FIND_BY_START_DATE,
                new Object[] {pessoaGc.getDataInicio(),
                        pessoaGc.getPessoa().getCodigoPessoa()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
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
    @SuppressWarnings("unchecked")
    public PessoaGrupoCusto findByPessoaAndDate(
            final Pessoa pessoa, final Date dataMes) {

        List<PessoaGrupoCusto> listResult = getJpaTemplate()
                .findByNamedQuery(
                        PessoaGrupoCusto.FIND_BY_PESSOA_AND_DATE,
                        new Object[] {pessoa.getCodigoPessoa(),
                                dataMes, dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    public List<PessoaGrupoCusto> findByPeopleCodeInAndDate(final Set<Long> peopleCodes, final Date month) {
        List<Long> codes = new ArrayList<Long>();
        List<PessoaGrupoCusto> peopleCostCenters = new ArrayList<PessoaGrupoCusto>();

        for (Long code : peopleCodes) {
            codes.add(code);

            if (codes.size() >= 999) {
                peopleCostCenters.addAll(this.executeFindByPeopleCodeInAndDate(codes, month));
                codes.clear();
            }
        }

        peopleCostCenters.addAll(this.executeFindByPeopleCodeInAndDate(codes, month));

        return peopleCostCenters;
    }

    private List<PessoaGrupoCusto> executeFindByPeopleCodeInAndDate(List<Long> codes, Date month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("peopleCodes", codes);
        params.put("month", month);
        return getJpaTemplate().findByNamedQueryAndNamedParams(PessoaGrupoCusto.FIND_BY_PESSOA_IN_AND_DATE, params);
    }

}