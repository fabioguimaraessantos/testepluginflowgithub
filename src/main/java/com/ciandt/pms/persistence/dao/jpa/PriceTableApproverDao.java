package com.ciandt.pms.persistence.dao.jpa;


import com.ciandt.pms.model.PriceTableApprover;
import com.ciandt.pms.persistence.dao.IPriceTableApproverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PriceTableApproverDao extends AbstractDao<Long, PriceTableApprover> implements IPriceTableApproverDao {

    @Autowired
    public PriceTableApproverDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PriceTableApprover.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PriceTableApprover> findByMsaCode(Long msaCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msaCode", msaCode);

        return getJpaTemplate().findByNamedQueryAndNamedParams(PriceTableApprover.FIND_BY_MSA, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTableApprover findByLogin(String login) {
        List<PriceTableApprover> listResult = getJpaTemplate().findByNamedQuery(
                PriceTableApprover.FIND_BY_LOGIN, login);

        if (listResult.isEmpty()) {
            return null;
        }
        return listResult.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTableApprover findByLoginAndMsaCode(String login, Long msaCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("login", login);
        params.put("msaCode", msaCode);
        List<PriceTableApprover> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(
                PriceTableApprover.FIND_BY_LOGIN_AND_MSA, params);

        return listResult != null && !listResult.isEmpty() ? listResult.get(0) : null;
    }
}
