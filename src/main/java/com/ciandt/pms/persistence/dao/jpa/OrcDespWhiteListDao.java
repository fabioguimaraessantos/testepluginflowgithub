package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcDespWhiteListDao;


/**
 * 
 * A classe OrcDespWhiteListDao proporciona as funcionalidades de DAO para
 * OrcDespWhiteList.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class OrcDespWhiteListDao extends AbstractDao<Long, OrcDespWhiteList>
        implements IOrcDespWhiteListDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public OrcDespWhiteListDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, OrcDespWhiteList.class);
    }

    /**
     * Busca por orcamentoDespesa.
     * 
     * @param entity
     *            orcamentoDespesa
     * @return listResult
     */
    @SuppressWarnings("unchecked")
    public List<OrcDespWhiteList> findByOrcamentoDespesa(
            final OrcamentoDespesa entity) {
        List<OrcDespWhiteList> listResult =
                getJpaTemplate().findByNamedQuery(
                        OrcDespWhiteList.FIND_BY_ORCAMENTO_DESPESA,
                        new Object[]{entity.getCodigoOrcaDespesa()});
        return listResult;
    }

    /**
     * Busca por orcamentoDespesa e nome.
     * @param orcDesp orcamentoDespesa
     * @param name nome
     * @return listResult.
     */
    @SuppressWarnings("unchecked")
    public List<OrcDespWhiteList> findByOrcDespAndName(
            final OrcamentoDespesa orcDesp, final String name) {
        List<OrcDespWhiteList> listResult =
                getJpaTemplate().findByNamedQuery(
                        OrcDespWhiteList.FIND_BY_ORC_DESP_AND_NAME,
                        new Object[]{orcDesp.getCodigoOrcaDespesa(), name});
        return listResult;
    }
}
