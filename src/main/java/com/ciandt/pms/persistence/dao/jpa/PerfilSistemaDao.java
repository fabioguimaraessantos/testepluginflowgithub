package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PerfilSistema;
import com.ciandt.pms.persistence.dao.IPerfilSistemaDao;


/**
 * 
 * A classe PerfilSistemaDao proporciona as funcionalidades de acesso ao banco
 * para referentes a entidade PerfilSistema.
 * 
 * @since 03/01/2011
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class PerfilSistemaDao extends AbstractDao<Long, PerfilSistema>
        implements IPerfilSistemaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public PerfilSistemaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, PerfilSistema.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<PerfilSistema> findAll() {
        List<PerfilSistema> resultList = getJpaTemplate().findByNamedQuery(
                PerfilSistema.FIND_ALL);

        return resultList;
    }

}