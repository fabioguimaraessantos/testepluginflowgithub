package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.GrupoCustoAreaOrcamentariaAud;
import com.ciandt.pms.model.GrupoCustoPeriodo;
import com.ciandt.pms.model.GrupoCustoPeriodoAud;

import java.util.List;

/**
 * 
 * A classe IGrupoCustoPeriodoDao proporciona a interface de acesso 
 * a camada de persistencia referente a entidade GurpoCustoPeriodo.
 *
 * @since 15/05/2010
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 *
 */
public interface IGrupoCustoPeriodoDao extends IAbstractDao<Long, GrupoCustoPeriodo> {
    
    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param grupoCusto
     *            entidade populada com os valores do GrupoCusto.
     * 
     * @return lista de entidades que atendem ao criterio do GrupoCusto.
     */
    GrupoCustoPeriodo findMaxStartDateByGrupoCusto(
            final GrupoCusto grupoCusto);

    List<GrupoCustoPeriodoAud> findHistoryByCodigo(Long codigoGrupoCusto);
    
}
