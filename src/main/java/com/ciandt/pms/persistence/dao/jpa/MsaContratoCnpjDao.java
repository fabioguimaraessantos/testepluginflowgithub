package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.MsaContrato;
import com.ciandt.pms.model.MsaContratoCnpj;
import com.ciandt.pms.persistence.dao.IMsaContratoCnpjDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class MsaContratoCnpjDao extends AbstractDao<Long, MsaContratoCnpj> implements IMsaContratoCnpjDao {

    /**
     * Construtor padrão.
     *
     * @param factory EntityManagerFactory
     */
    @Autowired
    public MsaContratoCnpjDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaContratoCnpj.class);
    }

    public List<MsaContratoCnpj> findByMsaContrato(MsaContrato msaContrato) {
        List<MsaContratoCnpj> listResult = getJpaTemplate().findByNamedQuery(MsaContratoCnpj.FIND_BY_MSA_CONTRATO,
                new Object[] { msaContrato.getCodigo() });

        return listResult;
    }
}
