package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.GrupoCustoStatus;
import com.ciandt.pms.persistence.dao.IGrupoCustoStatusDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GrupoCustoStatusDao extends AbstractDao<Long, GrupoCustoStatus> implements IGrupoCustoStatusDao {

    private EntityManager entityManager;

    public GrupoCustoStatusDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, GrupoCustoStatus.class);
        entityManager = factory.createEntityManager();
    }

    public GrupoCustoStatus findBySiglaStatusGrupoCusto(final String siglaStatusGrupoCusto) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("siglaStatusGrupoCusto", siglaStatusGrupoCusto);

        List<GrupoCustoStatus> grupoCustoStatus = getJpaTemplate().findByNamedQueryAndNamedParams(GrupoCustoStatus.FIND_BY_SIGLA, params);

        if (grupoCustoStatus.isEmpty())
            throw new EntityNotFoundException("No entity found with sigla "
                    + siglaStatusGrupoCusto);

        return grupoCustoStatus.get(0);
    }


    public List<GrupoCustoStatus> findAllActive() {

        return getJpaTemplate().findByNamedQuery(
                GrupoCustoStatus.FIND_ALL_ACTIVE);
    }

    @Override
    public List findByStatusActiveAndRequestInac(String siglaActive, String siglaRequestInact) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("siglaActive", siglaActive);
        params.put("siglaRequestInact", siglaRequestInact);

        return getJpaTemplate().
                findByNamedQueryAndNamedParams(GrupoCustoStatus.FIND_BY_ACTIVE_AND_REQUEST_INAC, params);
    }

    @Override
    public List<GrupoCustoStatus> findByStatusACTIAndCLOSAndREINAndININ(final String siglaActive, final String siglaClosed,
                                                                        final String siglaRequestInact,
                                                                        final String siglaIntegInactivation) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("siglaActive", siglaActive);
        params.put("siglaClosed", siglaClosed);
        params.put("siglaRequestInact", siglaRequestInact);
        params.put("siglaIntegInactivation", siglaIntegInactivation);

        return getJpaTemplate().
                findByNamedQueryAndNamedParams(GrupoCustoStatus.FIND_BY_ACTI_AND_CLOS_AND_REQ_INAC_AND_INTEG_INAC, params);
    }

    @Override
    public List<GrupoCustoStatus> findByStatusRECRAndINCR(String siglaRequestCreation, String siglaIntegCreation) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("siglaRequestCreation", siglaRequestCreation);
        params.put("siglaIntegCreation", siglaIntegCreation);

        return getJpaTemplate().
                findByNamedQueryAndNamedParams(GrupoCustoStatus.FIND_BY_REQ_CREAT_AND_INTEG_CREAT, params);
    }

    @Override
    public List<GrupoCustoStatus> findByStatusINAC(final String siglaInactive) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("siglaInactive", siglaInactive);

        return getJpaTemplate().
                findByNamedQueryAndNamedParams(GrupoCustoStatus.FIND_BY_INAC, params);
    }

    @Override
    public List<GrupoCustoStatus> findBySiglaInList(final List<String> listSigla) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("listSigla", listSigla);

        return getJpaTemplate().
                findByNamedQueryAndNamedParams(GrupoCustoStatus.FIND_BY_SIGLA_LIST, params);
    }

}
