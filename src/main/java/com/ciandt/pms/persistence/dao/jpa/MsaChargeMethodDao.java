package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaChargeMethod;
import com.ciandt.pms.persistence.dao.IMsaChargeMethodDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class MsaChargeMethodDao extends AbstractDao<Long, MsaChargeMethod> implements IMsaChargeMethodDao {

    @Autowired
    public MsaChargeMethodDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaChargeMethod.class);
    }

    @Override
    public List<MsaChargeMethod> findAll() {
        return getJpaTemplate().findByNamedQuery(MsaChargeMethod.FIND_ALL);
    }
}
