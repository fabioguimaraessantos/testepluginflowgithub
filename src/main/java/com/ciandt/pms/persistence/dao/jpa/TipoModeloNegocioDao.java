package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.TipoModeloNegocio;
import com.ciandt.pms.persistence.dao.ITipoModeloNegocioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 * Created by josef on 02/02/2017.
 */
@Repository
public class TipoModeloNegocioDao  extends AbstractDao<Long, TipoModeloNegocio> implements ITipoModeloNegocioDao {
    /**
     * Contrutor padrão.
     *
     * @param factory
     *            da entidade
     */
    @Autowired
    public TipoModeloNegocioDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, TipoModeloNegocio.class);
    }

    /**
     * Find All TipoPratica
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<TipoModeloNegocio> findAllActive() {
        List<TipoModeloNegocio> resultList = getJpaTemplate().findByNamedQuery(TipoModeloNegocio.FIND_ALL_ACTIVE);
        return resultList;
    }
}
