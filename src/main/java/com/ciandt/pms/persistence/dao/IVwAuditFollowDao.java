package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.VwAuditFollow;
import com.ciandt.pms.model.VwAuditFollowId;


/**
 * 
 * A classe IVwAuditFollowDao proporciona a interface de acesso para a camada de
 * persistencia referente a entidade VwAuditFollow.
 * 
 * @since 28/06/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwAuditFollowDao extends
        IAbstractDao<VwAuditFollowId, VwAuditFollow> {

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
    List<VwAuditFollow> findByMapaAlocAndDate(final Long codigoMapaAloc,
            final Date revDate);
    
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
    List<VwAuditFollow> findByRecursoAndDate(final Long codigoRecurso,
            final Date revDate);

}