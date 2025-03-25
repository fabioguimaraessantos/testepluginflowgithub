package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PriceTableEditor;
import com.ciandt.pms.persistence.dao.IPriceTableEditorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PriceTableEditorDao extends AbstractDao<Long, PriceTableEditor> implements IPriceTableEditorDao {


    @Autowired
    public PriceTableEditorDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PriceTableEditor.class);
    }

    @Override
    public List findByMsaCode(Long msaCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msaCode", msaCode);

        return getJpaTemplate().findByNamedQueryAndNamedParams(PriceTableEditor.FIND_BY_MSA, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PriceTableEditor findByLogin(String login) {
        List<PriceTableEditor> listResult = getJpaTemplate().findByNamedQuery(
                PriceTableEditor.FIND_BY_LOGIN, login);

        if (listResult.isEmpty()) {
            return null;
        }
        return listResult.get(0);
    }

    @Override
    public PriceTableEditor findByLoginAndMsaCode(String login, Long msaCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        params.put("msaCode", msaCode);

        List<PriceTableEditor> listResult = getJpaTemplate().findByNamedQueryAndNamedParams(
                PriceTableEditor.FIND_BY_LOGIN_AND_MSA, params);
        if(listResult.isEmpty()) {
            return null;
        }
        return listResult.get(0);
    }

}
