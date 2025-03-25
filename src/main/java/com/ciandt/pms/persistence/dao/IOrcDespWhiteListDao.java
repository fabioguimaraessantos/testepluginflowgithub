package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.OrcDespWhiteList;
import com.ciandt.pms.model.OrcamentoDespesa;


/**
 * 
 * A classe IOrcDespWhiteListDao proporciona as funcionalidades de interface
 * para OrcDespWhiteListDao.
 * 
 * @since 25/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
public interface IOrcDespWhiteListDao extends
        IAbstractDao<Long, OrcDespWhiteList> {

    /**
     * Busca por orcamentoDespesa.
     * 
     * @param entity
     *            orcamentoDespesa
     * @return listResult
     */
    List<OrcDespWhiteList> findByOrcamentoDespesa(final OrcamentoDespesa entity);
    
    /**
     * Busca por orcamentoDespesa e nome.
     * @param orcDesp orcamentoDespesa
     * @param name nome
     * @return listResult.
     */
    List<OrcDespWhiteList> findByOrcDespAndName(
            final OrcamentoDespesa orcDesp, final String name);

}
