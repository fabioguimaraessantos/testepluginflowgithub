package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ChargebackContratoPratica;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TiRecurso;


/**
 * 
 * A classe IChargebackCPDao proporciona a interface de 
 * acesso a camada de persistencia referente a entidade ChargebackContratoPratica.
 *
 * @since 16/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IChargebackCPDao extends IAbstractDao<Long, ChargebackContratoPratica> {

    /**
     * Realiza uma busca por ChargebackContratoPratica referentes 
     * a um recurso em um determinado periodo, passados por parametro.
     * 
     * @param recurso do tipo TiRecurso.
     * @param startDate data inicio do periodo.
     * @param endDate data fim do periodo.
     * 
     * @return lista de CentroLucro que atendem ao criterio de filtro
     */
    List<ChargebackContratoPratica> findByRecursoAndPeriod(
            final TiRecurso recurso, final Date startDate, final Date endDate);
    
    /**
     * Realiza uma busca por ChargebackContratoPratica referentes 
     * a um contrato Pratica em um determinado periodo, passados por parametro.
     * 
     * @param cp do tipo ContratoPratica.
     * @param startDate data inicio do periodo.
     * @param endDate data fim do periodo.
     * 
     * @return lista de CentroLucro que atendem ao criterio de filtro
     */
    List<ChargebackContratoPratica> findByContractAndPeriod(
            final ContratoPratica cp, final Date startDate, final Date endDate);
    
    /**
     * Realiza uma busca por ChargebackContratoPratica por
     * ContratoPratica, TiRecurso e Data. Estes tres compoem uma cahva unica.
     * 
     * @param recurso do tipo TiRecurso.
     * 
     * @param cp do tipo ContratoPratica.
     * 
     * @param monthDate data mes.
     * 
     * @return lista de CentroLucro que atendem ao criterio de filtro
     */
    ChargebackContratoPratica findByUniqueKey(
            final TiRecurso recurso, final ContratoPratica cp, final Date monthDate);
}
