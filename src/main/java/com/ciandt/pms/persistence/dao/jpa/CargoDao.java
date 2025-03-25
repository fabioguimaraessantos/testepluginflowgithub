package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.persistence.dao.ICargoDao;


/**
 * 
 * A classe CargoDao proporciona as funcionalidades de DAO para a entidade CargoPms.
 *
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Repository
public class CargoDao extends AbstractDao<Long, CargoPms> implements ICargoDao {

    /**
     * Construtor.
     * @param factory
     *          factory
     */
    @Autowired
    public CargoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CargoPms.class);
    }
    
    /**
     * Busca todas as entidades.
     * @return lista.
     */
    @SuppressWarnings("unchecked")
    public List<CargoPms> findAll() {
        List<CargoPms> listResult = getJpaTemplate().findByNamedQuery(CargoPms.FIND_ALL);
        
        return listResult;
    }

}
