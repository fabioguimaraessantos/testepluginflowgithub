package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.FatorReajuste;
import com.ciandt.pms.model.TipoContrato;


/**
 * 
 * A classe IFatorReajusteDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade FatorReajuste.
 *
 * @since 25/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IFatorReajusteDao extends IAbstractDao<Long, FatorReajuste> {

    /**
     * Busca por todos os FatorRajuste associados a 
     * um tipo de contrato.
     * 
     * @param tipo - entidade do tipo TipoContrato.
     * 
     * @return uma lista de FatorRajuste.
     */
    List<FatorReajuste> findByTipoContrato(final TipoContrato tipo);
    
    /**
     * Busca pelo proximo fator de reajuste, referente a 
     * data de inicio.
     * 
     * @param fatorReajuste - entidade do tipo FatorReajuste.
     * 
     * @return retorna o proximo FatorRajuste.
     */
    FatorReajuste findNext(final FatorReajuste fatorReajuste);
    
    /**
     * Busca pelo fator de reajuste anterior, referente a 
     * data de inicio.
     * 
     * @param fatorReajuste - entidade do tipo FatorReajuste.
     * 
     * @return retorna o FatorRajuste anterior.
     */
    FatorReajuste findPrevious(final FatorReajuste fatorReajuste);
    
    /**
     * Busca pelo fator de reajuste referente a 
     * data de inicio.
     * 
     * @param fatorReajuste - entidade do tipo FatorReajuste.
     * 
     * @return retorna o FatorRajuste anterior.
     */
    FatorReajuste findByStartDate(final FatorReajuste fatorReajuste);
    
    /**
     * Busca os reajuste entre periodos de data.
     * 
     * @param tipoContrato - TipoContrato.
     * 
     * @param startDate - data inicial.
     * 
     * @param endDate - data fim.
     * 
     * @return retorna uma lista de FatorRajuste.
     */
    List<FatorReajuste> findByPeriod(final TipoContrato tipoContrato,
            final Date startDate, final Date endDate);
}
