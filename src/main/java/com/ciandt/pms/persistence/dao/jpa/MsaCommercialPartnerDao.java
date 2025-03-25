package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaChargeMethod;
import com.ciandt.pms.model.MsaCommercialPartner;
import com.ciandt.pms.persistence.dao.IMsaCommercialPartnerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MsaCommercialPartnerDao extends AbstractDao<Long, MsaCommercialPartner> implements IMsaCommercialPartnerDao {

    @Autowired
    public MsaCommercialPartnerDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaCommercialPartner.class);
    }

    @Override
    public List<MsaCommercialPartner> findByMsaCode(Long msaCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msaCode", msaCode);

        return getJpaTemplate().findByNamedQueryAndNamedParams(MsaCommercialPartner.FIND_BY_MSA, params);
    }
}
