/**
 * 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.persistence.dao.IModuloDao;


/**
 * A classe ModuloDao proporciona as funcionalidades de acesso ao 
 *  banco de dados, referente a entidade Modulo.
 *
 * @since 26/11/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class ModuloDao extends AbstractDao<Long, Modulo> implements IModuloDao {

    /**
     * Construtor padrão.
     * 
     * @param factory - fabrica da entidade.
     */
    @Autowired
    public ModuloDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Modulo.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<Modulo> findAll() {
        
        List<Modulo> listResult = 
            getJpaTemplate().findByNamedQuery(Modulo.FIND_ALL);

        return listResult;
    }


}
