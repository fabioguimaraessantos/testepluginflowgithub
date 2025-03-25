package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaCidadeBase;
import com.ciandt.pms.persistence.dao.IPessoaCidadeBaseDao;


/**
 * 
 * A classe PessoaCidadeBaseDao proporciona as funcionalidades da 
 * camada de persistencia referente a entidade PessoaCidadeBase.
 *
 * @since 06/06/2011
 * @author cmantovani
 *
 */
@Repository
public class PessoaCidadeBaseDao extends AbstractDao<Long, PessoaCidadeBase>
        implements IPessoaCidadeBaseDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade.
     */
    public PessoaCidadeBaseDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, PessoaCidadeBase.class);
    }
    
    /**
     * Busca pelo proximo, referente a data de inicio.
     * 
     * @param pessoaCidadeBase - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o proximo PessoaCidadeBase.
     */
    @SuppressWarnings("unchecked")
    public PessoaCidadeBase findNext(final PessoaCidadeBase pessoaCidadeBase) {

        Long codigo = pessoaCidadeBase.getPessoa().getCodigoPessoa();
        
        List<PessoaCidadeBase> listResult = getJpaTemplate().findByNamedQuery(
                PessoaCidadeBase.FIND_NEXT,
                new Object[] {codigo, codigo,
                    pessoaCidadeBase.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo anterior, referente a data de inicio.
     * 
     * @param pessoaCidadeBase - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase anterior.
     */
    @SuppressWarnings("unchecked")
    public PessoaCidadeBase findPrevious(final PessoaCidadeBase pessoaCidadeBase) {

        Long codigo = pessoaCidadeBase.getPessoa().getCodigoPessoa();
        
        List<PessoaCidadeBase> listResult = getJpaTemplate().findByNamedQuery(
                PessoaCidadeBase.FIND_PREVIOUS,
                new Object[] {codigo, codigo,
                    pessoaCidadeBase.getDataInicio()});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Busca pelo fator de reajuste referente a 
     * data de inicio.
     * 
     * @param pessoaCidadeBase - entidade do tipo PessoaCidadeBase.
     * 
     * @return retorna o PessoaCidadeBase.
     */
    @SuppressWarnings("unchecked")
    public PessoaCidadeBase findByStartDate(final PessoaCidadeBase pessoaCidadeBase) {

        List<PessoaCidadeBase> listResult = getJpaTemplate().findByNamedQuery(
                PessoaCidadeBase.FIND_BY_START_DATE,
                new Object[] {pessoaCidadeBase.getDataInicio(),
                        pessoaCidadeBase.getPessoa().getCodigoPessoa()});

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
    public PessoaCidadeBase findByPessoaAndDate(
            final Pessoa pessoa, final Date dataMes) {

        List<PessoaCidadeBase> listResult = getJpaTemplate()
                .findByNamedQuery(
                        PessoaCidadeBase.FIND_BY_PESSOA_AND_DATE,
                        new Object[] {pessoa.getCodigoPessoa(),
                                dataMes, dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }
    
}