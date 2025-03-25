package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaTipoContrato;
import com.ciandt.pms.persistence.dao.IMsaTipoContratoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class MsaTipoContratoDao extends AbstractDao<Long, MsaTipoContrato> implements IMsaTipoContratoDao {
    /**
     * Construtor padrão.
     *
     * @param factory     EntityManagerFactory
     */
    @Autowired
    public MsaTipoContratoDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaTipoContrato.class);
    }

    @Override
    public List<MsaTipoContrato> findAllActive() {
        return getJpaTemplate().findByNamedQuery(MsaTipoContrato.FIND_ALL_ACTIVE);

    }
    @Override
    public MsaTipoContrato findActiveByAcronimo(String acronimo) {
        List<MsaTipoContrato> results = getJpaTemplate().findByNamedQuery(MsaTipoContrato.FIND_ACTIVE_BY_ACRONIMO, new Object[]{acronimo});
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

}
