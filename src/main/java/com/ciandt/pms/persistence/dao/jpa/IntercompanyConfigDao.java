package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.IntercompanyConfig;
import com.ciandt.pms.persistence.dao.IIntercompanyConfigDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class IntercompanyConfigDao extends AbstractDao<Long, IntercompanyConfig> implements
        IIntercompanyConfigDao {

    /**
     * Construtor padrão.
     *
     * @param factory do tipo da entidade
     */
    @Autowired
    public IntercompanyConfigDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, IntercompanyConfig.class);
    }

    /**
     * Retorna uma list de configs intercompany entre 2 empresas
     *
     * @param mainCompany  Id da empresa principal.
     * @param interCompany Id da empresa intercompany.
     * @return Configs de intercompany, se existir
     */
    @SuppressWarnings("unchecked")
    public List<IntercompanyConfig> findByCompanies(Long mainCompany, Long interCompany) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("mainCompany", mainCompany);
        params.put("interCompany", interCompany);

        return getJpaTemplate().findByNamedQueryAndNamedParams(
                IntercompanyConfig.FIND_BY_COMPANIES, params);
    }
}
