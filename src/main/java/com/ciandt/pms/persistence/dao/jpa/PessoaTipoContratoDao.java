package com.ciandt.pms.persistence.dao.jpa;

import java.util.*;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaTipoContrato;
import com.ciandt.pms.persistence.dao.IPessoaTipoContratoDao;


/**
 * 
 * A classe PessoaTipoContratoDao proporciona as funcionalidades da 
 * camada de persistencia referente a entidade PessoaTipoContrato.
 *
 * @since 26/07/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 *
 */
@Repository
public class PessoaTipoContratoDao extends AbstractDao<Long, PessoaTipoContrato>
        implements IPessoaTipoContratoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade.
     */
    public PessoaTipoContratoDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, PessoaTipoContrato.class);
    }
    
    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaTC - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o proximo PessoaTipoContrato.
     */
    @SuppressWarnings("unchecked")
    public PessoaTipoContrato findNext(final PessoaTipoContrato pessoaTC) {

        Long codigo = pessoaTC.getPessoa().getCodigoPessoa();
        
        List<PessoaTipoContrato> listResult = getJpaTemplate().findByNamedQuery(
                PessoaTipoContrato.FIND_NEXT,
                new Object[] {codigo, codigo,
                    pessoaTC.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaTC - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato anterior.
     */
    @SuppressWarnings("unchecked")
    public PessoaTipoContrato findPrevious(final PessoaTipoContrato pessoaTC) {

        Long codigo = pessoaTC.getPessoa().getCodigoPessoa();
        
        List<PessoaTipoContrato> listResult = getJpaTemplate().findByNamedQuery(
                PessoaTipoContrato.FIND_PREVIOUS,
                new Object[] {codigo, codigo,
                    pessoaTC.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo fator de reajuste referente a 
     * data de inicio.
     * 
     * @param pessoaTC - entidade do tipo PessoaTipoContrato.
     * 
     * @return retorna o PessoaTipoContrato.
     */
    @SuppressWarnings("unchecked")
    public PessoaTipoContrato findByStartDate(final PessoaTipoContrato pessoaTC) {

        List<PessoaTipoContrato> listResult = getJpaTemplate().findByNamedQuery(
                PessoaTipoContrato.FIND_BY_START_DATE,
                new Object[] {pessoaTC.getDataInicio(),
                        pessoaTC.getPessoa().getCodigoPessoa()});

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
    public PessoaTipoContrato findByPessoaAndDate(
            final Pessoa pessoa, final Date dataMes) {

        List<PessoaTipoContrato> listResult = getJpaTemplate()
                .findByNamedQuery(
                        PessoaTipoContrato.FIND_BY_PESSOA_AND_DATE,
                        new Object[] {pessoa.getCodigoPessoa(),
                                dataMes, dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    @Override
    public List<PessoaTipoContrato> findByPeopleCodeInAndMonth(final Set<Long> peopleCodes, final Date month) {
        List<Long> codes = new ArrayList<Long>();
        List<PessoaTipoContrato> peopleContractTypes = new ArrayList<PessoaTipoContrato>();

        for (Long code : peopleCodes) {
            codes.add(code);

            if (codes.size() >= 999) {
                peopleContractTypes.addAll(this.executeFindByPeopleCodeInAndMonth(codes, month));
                codes.clear();
            }
        }

        peopleContractTypes.addAll(this.executeFindByPeopleCodeInAndMonth(codes, month));

        return peopleContractTypes;
    }

    private List<PessoaTipoContrato> executeFindByPeopleCodeInAndMonth(List<Long> codes, Date month) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("peopleCodes", codes);
        params.put("month", month);

        return getJpaTemplate().findByNamedQueryAndNamedParams(PessoaTipoContrato.FIND_BY_PESSOA_IN_AND_DATE, params);
    }

}