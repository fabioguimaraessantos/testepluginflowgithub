package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaStatusContrato;
import com.ciandt.pms.persistence.dao.IMsaStatusContratoDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class MsaStatusContratoDao extends AbstractDao<Long, MsaStatusContrato> implements IMsaStatusContratoDao {

    /**
     * Construtor padrão.
     *
     * @param factory     EntityManagerFactory
     */
    public MsaStatusContratoDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaStatusContrato.class);
    }

    @Override
    public List<MsaStatusContrato> findAllActive() {
        return getJpaTemplate().findByNamedQuery(MsaStatusContrato.FIND_ALL_ACTIVE);
    }

    @Override
    public MsaStatusContrato findByName(String name) {
        List<MsaStatusContrato> results = getJpaTemplate().findByNamedQuery(MsaStatusContrato.FIND_BY_NAME, new Object[]{name});
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }


}
