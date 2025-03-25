package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.persistence.dao.IAceleradorDao;


/**
 * 
 * A classe AceleradorDao proporciona as funcionalidades de
 * persistencia referente a entidade Acelerador.
 *
 * @since 06/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class AceleradorDao extends AbstractDao<Long, Acelerador> 
    implements IAceleradorDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica do entity manager
     */
    @Autowired
    public AceleradorDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, Acelerador.class);
    }
    
    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Area.
     */
    @SuppressWarnings("unchecked")
    public List<Acelerador> findAllActive() {
        
        List<Acelerador> listResult = getJpaTemplate().findByNamedQuery(
                Acelerador.FIND_ALL_ACTIVE);

        return listResult;
    }

}
