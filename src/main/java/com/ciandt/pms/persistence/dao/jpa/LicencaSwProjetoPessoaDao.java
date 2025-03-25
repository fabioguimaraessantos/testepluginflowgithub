package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.LicencaSwProjeto;
import com.ciandt.pms.model.LicencaSwProjetoPessoa;
import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.ILicencaSwProjetoPessoaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class LicencaSwProjetoPessoaDao extends AbstractDao<Long, LicencaSwProjetoPessoa> implements ILicencaSwProjetoPessoaDao {
    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    @Autowired
    public LicencaSwProjetoPessoaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, LicencaSwProjetoPessoa.class);
        entityManager = factory.createEntityManager();
    }

    public List<LicencaSwProjetoPessoa> findAll() {
        List<LicencaSwProjetoPessoa> listResult = getJpaTemplate().findByNamedQuery(
                LicencaSwProjetoPessoa.FIND_ALL);

        return listResult;
    }

    public List<LicencaSwProjetoPessoa> findByLicencaSwProjeto(
            final LicencaSwProjeto entity) {
        List<LicencaSwProjetoPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        LicencaSwProjetoPessoa.FIND_BY_LICENCA_SW_PROJETO,
                        new Object[]{entity.getCodigoLicencaSwProjeto()});
        return listResult;
    }

    public List<LicencaSwProjetoPessoa> findByLicencaSwProjetoAndName(
            final LicencaSwProjeto licencaSwProjeto, final String name) {
        List<LicencaSwProjetoPessoa> listResult =
                getJpaTemplate().findByNamedQuery(
                        LicencaSwProjetoPessoa.FIND_BY_LICENCA_SW_PROJETO_AND_NAME,
                        new Object[]{licencaSwProjeto.getCodigoLicencaSwProjeto(), name});
        return listResult;
    }
}
