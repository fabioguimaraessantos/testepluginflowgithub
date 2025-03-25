package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Area;
import com.ciandt.pms.persistence.dao.IAreaDao;


/**
 * 
 * A classe AreaDao proporciona as funcionalidades da camada
 * de persistencia referente a entidade Area.
 *
 * @since 13/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class AreaDao extends AbstractDao<Long, Area> implements IAreaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - Fabrica da entidade.
     */
    @Autowired
    public AreaDao(@Qualifier("entityManagerFactory") 
            final EntityManagerFactory factory) {
        super(factory, Area.class);
    }

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Area.
     */
    @SuppressWarnings("unchecked")
    public List<Area> findAllActive() {
        
        List<Area> listResult = getJpaTemplate().findByNamedQuery(
                Area.FIND_ALL_ACTIVE);

        return listResult;
    }

}
