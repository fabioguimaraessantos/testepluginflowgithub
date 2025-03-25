package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPraticaOrcDesp;
import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IContratoPraticaOrcDespDao;


/**
 * 
 * A classe ContratoPraticaOrcDespDao proporciona as funcionalidades de DAO para
 * a enitdade ContratoPraticaOrcDesp.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class ContratoPraticaOrcDespDao extends
        AbstractDao<Long, ContratoPraticaOrcDesp> implements
        IContratoPraticaOrcDespDao {

    /**
     * Construtor.
     * 
     * @param factory
     *            factory
     */
    @Autowired
    public ContratoPraticaOrcDespDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ContratoPraticaOrcDesp.class);
    }

    /**
     * Busca por OrcamentoDespesa.
     * @param entity orcamentoDespesa.
     * @return listResult.
     */
    @SuppressWarnings("unchecked")
    public List<ContratoPraticaOrcDesp> findByOrcDesp(
            final OrcamentoDespesa entity) {
        List<ContratoPraticaOrcDesp> listResult =
                getJpaTemplate().findByNamedQuery(
                        ContratoPraticaOrcDesp.FIND_BY_ORCAMENTO_DESPESA,
                        new Object[]{entity.getCodigoOrcaDespesa()});
        return listResult;
    }

}
