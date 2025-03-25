package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CargoPmsCargo;


/**
 * 
 * A classe ICargoPmsCargoDao proporciona as funcionalidades de interface para CargoPmsCargo.
 *
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public interface ICargoPmsCargoDao extends IAbstractDao<Long, CargoPmsCargo> {
    
    /**
     * Busca por CargoPms.
     * @param cargoPms cargoPms.
     * @return lista.
     */

    List<CargoPmsCargo> findCargoPmsCargoByCargoPms(final CargoPms cargoPms);

}
