package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.MsaTipoMontanteDespesa;
import com.ciandt.pms.persistence.dao.IMsaTipoMontanteDespesaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class MsaTipoMontanteDespesaDao extends AbstractDao<Long, MsaTipoMontanteDespesa> implements IMsaTipoMontanteDespesaDao {

    /**
     * Construtor padrão.
     *
     * @param factory     EntityManagerFactory
     */
    @Autowired
    public MsaTipoMontanteDespesaDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, MsaTipoMontanteDespesa.class);
    }

    @Override
    public List<MsaTipoMontanteDespesa> findAllActive() {
        return getJpaTemplate().findByNamedQuery(MsaTipoMontanteDespesa.FIND_ALL_ACTIVE);
    }

    @Override
    public MsaTipoMontanteDespesa findByName(String name) {
        return (MsaTipoMontanteDespesa) getJpaTemplate().findByNamedQuery(MsaTipoMontanteDespesa.FIND_BY_NAME,  new Object[]{name}).get(0);
    }
}
