package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaContratoFilial;
import com.ciandt.pms.persistence.dao.IMsaContratoFilialDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MsaContratoFilialDao extends AbstractDao<Long, MsaContratoFilial> implements IMsaContratoFilialDao {

    /**
     * Construtor padrão.
     *
     * @param factory     EntityManagerFactory
     */
    public MsaContratoFilialDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaContratoFilial.class);
    }

    @Override
    public List<MsaContratoFilial> findByMsaContratoCode(Long msaContratoCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoMsaContrato", msaContratoCode);

        List<MsaContratoFilial> result = getJpaTemplate().findByNamedQueryAndNamedParams(MsaContratoFilial.FIND_BY_MSA_CODE, params);

        if (null != result && !result.isEmpty()) {
            return result;
        }
        return new ArrayList<MsaContratoFilial>();
    }

    @Override
    @Transactional
    public void merge(List<MsaContratoFilial> msaContratoFiliais) {
        if (null != msaContratoFiliais && !msaContratoFiliais.isEmpty()) {
            for (MsaContratoFilial filial : msaContratoFiliais) {
                this.merge(filial);
                this.flush();
            }
        }
    }
}
