package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.HedgeMoedaMes;
import com.ciandt.pms.model.Moeda;


/**
 * 
 * A classe IHedgeDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade HedgeMoedaMes.
 *
 * @since 24/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IHedgeDao extends IAbstractDao<Long, HedgeMoedaMes> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<HedgeMoedaMes> findByFilter(final HedgeMoedaMes filter);
    
    /**
     * Busca a entidade pela chave unica.
     * 
     * @param monthDate
     *            entidade populada com os valores do filtro.
     *            
     * @param currency intancia da moeda           
     *            
     * 
     * @return entidade referente a chave unica
     */
    HedgeMoedaMes findUnique(final Date monthDate, final Moeda currency);
}
