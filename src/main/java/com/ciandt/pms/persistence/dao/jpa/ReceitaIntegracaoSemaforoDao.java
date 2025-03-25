package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ReceitaIntegracaoSemaforo;
import com.ciandt.pms.persistence.dao.IReceitaIntegracaoSemaforoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class ReceitaIntegracaoSemaforoDao extends AbstractDao<Long, ReceitaIntegracaoSemaforo>
        implements IReceitaIntegracaoSemaforoDao {

    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    @Autowired
    public ReceitaIntegracaoSemaforoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ReceitaIntegracaoSemaforo.class);

        entityManager = factory.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public List<ReceitaIntegracaoSemaforo> findByReceitaDealFiscal(Long codigoReceitaDealFiscal) {

        List<ReceitaIntegracaoSemaforo> listResult = getJpaTemplate().findByNamedQuery(
                ReceitaIntegracaoSemaforo.FIND_BY_RECEITA_DEAL_FISCAL,
                new Object[] { codigoReceitaDealFiscal });

        return listResult;
    }
}
