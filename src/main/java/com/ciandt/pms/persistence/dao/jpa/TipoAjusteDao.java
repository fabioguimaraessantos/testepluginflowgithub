package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.TipoAjuste;
import com.ciandt.pms.persistence.dao.ITipoAjusteDao;


/**
 * 
 * A classe TipoAjuste proporciona as funcionalidades da camada de persistencia
 * referente a entidade Area.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
@Repository
public class TipoAjusteDao extends AbstractDao<Long, TipoAjuste> implements
        ITipoAjusteDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - Fabrica da entidade.
     */
    @Autowired
    public TipoAjusteDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoAjuste.class);
    }

    /**
     * Busca todas as entidades.
     * 
     * @return retorna uma lista de TipoAjuste.
     */
    @SuppressWarnings("unchecked")
    public List<TipoAjuste> findAll() {

        List<TipoAjuste> listResult = getJpaTemplate().findByNamedQuery(
                TipoAjuste.FIND_ALL);

        return listResult;
    }

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de TipoAjuste.
     */
    @SuppressWarnings("unchecked")
    public List<TipoAjuste> findAllActive() {

        List<TipoAjuste> listResult = getJpaTemplate().findByNamedQuery(
                TipoAjuste.FIND_ALL_ACTIVE);

        return listResult;
    }

}
