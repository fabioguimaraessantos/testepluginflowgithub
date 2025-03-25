package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CargoPmsCargo;
import com.ciandt.pms.persistence.dao.ICargoPmsCargoDao;


/**
 * 
 * A classe CargoPmsCargoDao proporciona as funcionalidades de ... para ...
 * 
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class CargoPmsCargoDao extends AbstractDao<Long, CargoPmsCargo>
        implements ICargoPmsCargoDao {

    /**
     * Construtor.
     * 
     * @param factory
     *            factory
     */
    @Autowired
    public CargoPmsCargoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CargoPmsCargo.class);
    }

    /**
     * Busca por CargoPms.
     * @param cargoPms cargoPms.
     * @return lista.
     */
    @SuppressWarnings("unchecked")
    public List<CargoPmsCargo> findCargoPmsCargoByCargoPms(
            final CargoPms cargoPms) {
        List<CargoPmsCargo> listResult =
                getJpaTemplate().findByNamedQuery(
                        CargoPmsCargo.FIND_BY_CARGO_PMS,
                        new Object[]{cargoPms.getCodigoCargoPms()});
        return listResult;
    }
}
