package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaSaldoMoeda;
import com.ciandt.pms.persistence.dao.IMsaSaldoMoedaDao;

/**
 * 
 * A classe MsaSaldoMoedaDao proporciona as funcionalidades de persistencia
 * referente a entidade MsaSaldoMoeda.
 * 
 * @since 28/09/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Repository
public class MsaSaldoMoedaDao extends AbstractDao<Long, MsaSaldoMoeda>
        implements IMsaSaldoMoedaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica do entity manager
     */
    @Autowired
    public MsaSaldoMoedaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaSaldoMoeda.class);
    }

    /**
     * Busca todas as entidades.
     * 
     * @return retorna uma lista de MsaSaldoMoeda.
     */
    @SuppressWarnings("unchecked")
    public List<MsaSaldoMoeda> findAll() {
        List<MsaSaldoMoeda> listResult =
                getJpaTemplate().findByNamedQuery(MsaSaldoMoeda.FIND_ALL);

        return listResult;
    }

    /**
     * Busca todas as entidades pelo Msa.
     * 
     * @return retorna uma lista de MsaSaldoMoeda do Msa passado por parâmetro.
     */
    @SuppressWarnings("unchecked")
    public List<MsaSaldoMoeda> findByMsa(final Msa msa) {
        List<MsaSaldoMoeda> listResult =
                getJpaTemplate().findByNamedQuery(MsaSaldoMoeda.FIND_BY_MSA,
                        new Object[]{msa.getCodigoMsa()});

        return listResult;
    }
    
    /**
     * Busca todas as entidades pelo Msa e ativos.
     * 
     * @return retorna uma lista de MsaSaldoMoeda do Msa passado por parâmetro.
     */
    @SuppressWarnings("unchecked")
    public List<MsaSaldoMoeda> findByMsaAndActive(final Msa msa) {
        List<MsaSaldoMoeda> listResult =
                getJpaTemplate().findByNamedQuery(MsaSaldoMoeda.FIND_BY_MSA_AND_ACTIVE,
                        new Object[]{msa.getCodigoMsa()});

        return listResult;
    }

}