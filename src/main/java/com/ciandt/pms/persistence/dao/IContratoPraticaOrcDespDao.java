package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.ContratoPraticaOrcDesp;
import com.ciandt.pms.model.OrcamentoDespesa;


/**
 * 
 * A classe IContratoPraticaOrcDescDao proporciona as funcionalidades de intereface para ContratoPraticaOrdDesc.
 *
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Carlos Augusto Ribeiro Mantovani</a>
 *
 */
public interface IContratoPraticaOrcDespDao extends IAbstractDao<Long, ContratoPraticaOrcDesp> {
    
    /**
     * Busca por OrcamentoDespesa.
     * @param entity orcamentoDespesa.
     * @return listResult.
     */
    List<ContratoPraticaOrcDesp> findByOrcDesp(
            final OrcamentoDespesa entity);

}
