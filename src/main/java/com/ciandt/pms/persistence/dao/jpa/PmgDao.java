package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Pmg;
import com.ciandt.pms.persistence.dao.IPmgDao;


/**
 * 
 A classe TabelaPrecoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade PMG.
 * 
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Repository
public class PmgDao extends AbstractDao<Long, Pmg> implements IPmgDao {

    /**
     * Construtor.
     * 
     * @param factory
     *            factory
     */
    @Autowired
    public PmgDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Pmg.class);
    }
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<Pmg> findAll() {
        List<Pmg> listResult =
                getJpaTemplate().findByNamedQuery(Pmg.FIND_ALL);

        return listResult;
    }

    
}
