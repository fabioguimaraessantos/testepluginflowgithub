package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ModeloAreaOrcamentaria;
import com.ciandt.pms.persistence.dao.IModeloAreaOrcamentariaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;

@Repository
public class ModeloAreaOrcamentariaDao extends AbstractDao<Long, ModeloAreaOrcamentaria> implements IModeloAreaOrcamentariaDao {


    @Autowired
    public ModeloAreaOrcamentariaDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ModeloAreaOrcamentaria.class);
    }

    public List<ModeloAreaOrcamentaria> findAll() {

        return (List<ModeloAreaOrcamentaria>) getJpaTemplate().findByNamedQuery(
                ModeloAreaOrcamentaria.FIND_ALL);
    }

    public ModeloAreaOrcamentaria findByCurrentAreaOrcamentaria(Long areaOrcamentaria) {

        HashMap<String, Long> params = new HashMap<>();
        params.put("areaOrcamentaria", areaOrcamentaria);

        return firstResultOrNull(getJpaTemplate().findByNamedQueryAndNamedParams(
                ModeloAreaOrcamentaria.FIND_CURRENT_BY_AREA_ORCAMENTARIA, params));
    }

    @Override
    public void update(ModeloAreaOrcamentaria modeloAreaOrcamentaria) {
        getJpaTemplate().merge(modeloAreaOrcamentaria);
    }
}
