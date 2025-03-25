package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.VwItemHrsFatDeal;


/**
 * 
 * A classe IVwItemHrsFatDealDao proporciona a interface de DAO para a
 * entidade VwItemHrsFatDeal.
 * 
 * @since 14/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IVwItemHrsFatDealDao extends
        IAbstractDao<Long, VwItemHrsFatDeal> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param dataAlocacaoPeriodo
     *            data mes/ano da Alocacao.
     * @param codigoContratoPratica
     *            código do ContratoPratica.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<VwItemHrsFatDeal> findByFilter(final Date dataAlocacaoPeriodo,
            final Long codigoContratoPratica);

}
