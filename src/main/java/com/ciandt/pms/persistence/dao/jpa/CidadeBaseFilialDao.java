package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.CidadeBaseFilial;
import com.ciandt.pms.model.CidadeBaseFilialId;
import com.ciandt.pms.persistence.dao.ICidadeBaseFilialDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CidadeBaseFilialDao extends AbstractDao<CidadeBaseFilialId, CidadeBaseFilial> implements ICidadeBaseFilialDao {

    @Autowired
    public CidadeBaseFilialDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CidadeBaseFilial.class);
    }

    @SuppressWarnings("unchecked")
    public List<CidadeBaseFilial> findAll() {
        return getJpaTemplate().findByNamedQuery(CidadeBaseFilial.FIND_ALL);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CidadeBaseFilial> findByFilter(final CidadeBaseFilial filter) {
        Map<String, Object> params = new HashMap<>();
        if (filter.getId() != null) {
            params.put("cidadeBase", filter.getId().getCidadeBase());
            params.put("empresa", filter.getId().getEmpresa());
        }
        params.put("empresaFilial", filter.getEmpresaFilial());
        params.put("empresaMatriz", filter.getEmpresaMatriz());

        return getJpaTemplate().findByNamedQueryAndNamedParams(CidadeBaseFilial.FIND_BY_FILTER, params);
    }
}
