package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwAuditFollow;
import com.ciandt.pms.model.VwAuditFollowId;
import com.ciandt.pms.persistence.dao.IVwAuditFollowDao;


/**
 * 
 * A classe VwAuditFollowDao proporciona as funcionalidades da camada de
 * persistencia referente a entidade VwAuditFollow.
 * 
 * @since 28/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwAuditFollowDao extends
        AbstractDao<VwAuditFollowId, VwAuditFollow> implements
        IVwAuditFollowDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            - fabrica da entidade.
     */
    @Autowired
    public VwAuditFollowDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {

        super(factory, VwAuditFollow.class);
    }

    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param codigoMapaAloc
     *            - codigo do MapaAlocacao da revisao
     * @param revDate
     *            - data da revisao
     * 
     * @return retorna uma lista com as entidades filtradas de acordo com os
     *         parametros.
     */
    @SuppressWarnings("unchecked")
    public List<VwAuditFollow> findByMapaAlocAndDate(final Long codigoMapaAloc,
            final Date revDate) {
        List<VwAuditFollow> listResult = getJpaTemplate().findByNamedQuery(
                VwAuditFollow.FIND_BY_MAPA_ALOC_AND_DATE,
                new Object[] {codigoMapaAloc, revDate });

        return listResult;
    }
    
    /**
     * Busca as entidades filtradas de acordo com os parametros.
     * 
     * @param codigoRecurso
     *            - codigo do Recurso da revisao
     * @param revDate
     *            - data da revisao
     * 
     * @return retorna uma lista com as entidades filtradas de acordo com os
     *         parametros.
     */
    @SuppressWarnings("unchecked")
    public List<VwAuditFollow> findByRecursoAndDate(final Long codigoRecurso,
            final Date revDate) {
        List<VwAuditFollow> listResult = getJpaTemplate().findByNamedQuery(
                VwAuditFollow.FIND_BY_RECURSO_AND_DATE,
                new Object[] {codigoRecurso, revDate });

        return listResult;
    }

}