package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.VwPmsGrupoContaContabil;
import com.ciandt.pms.persistence.dao.IVwPmsGrupoContaContabilDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VwPmsGrupoContaContabilDao extends AbstractDao<Long, VwPmsGrupoContaContabil> implements IVwPmsGrupoContaContabilDao {

    @Autowired
    public VwPmsGrupoContaContabilDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwPmsGrupoContaContabil.class);
    }

    @Override
    public VwPmsGrupoContaContabil findByCodigoContratoPratica(final Long codigoContratoPratica){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("codigoContratoPratica", codigoContratoPratica);
        List<VwPmsGrupoContaContabil> list = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        VwPmsGrupoContaContabil.FIND_BY_CONTRATO_PRATICA, map);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public VwPmsGrupoContaContabil findByCodigoCentroCusto(final Long codigoCentroCusto){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("codigoCentroCusto", codigoCentroCusto);
        List<VwPmsGrupoContaContabil> list = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        VwPmsGrupoContaContabil.FIND_BY_CENTRO_CUSTO, map);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public VwPmsGrupoContaContabil findByCodigoGrupoCusto(final Long codigoCentroCusto){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("codigoCentroCusto", codigoCentroCusto);
        List<VwPmsGrupoContaContabil> list = getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        VwPmsGrupoContaContabil.FIND_BY_GRUPO_CUSTO, map);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
