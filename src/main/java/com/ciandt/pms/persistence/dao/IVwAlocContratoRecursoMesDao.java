package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.VwAlocContratoRecursoMes;


/**
 * 
 * A classe IVwAlocContratoRecursoMes proporciona a interface
 * de acesso camada de persistencia referente a entidade VwAlocContratoRecursoMes.
 *
 * @since 14/09/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IVwAlocContratoRecursoMesDao 
    extends IAbstractDao <Long, VwAlocContratoRecursoMes> {

    /**
     * Busca pelo mapa de pelo periodo inicio e fim.
     * 
     * @param mapaAlocacao - entidade MapaAlocacao
     * @param startDate - data inicio
     * @param endDate - data fim
     * 
     * @return retorna uma lista de VwAlocContratoRecursoMes
     */
    List<VwAlocContratoRecursoMes> findByMapaAndPeriod(
            final MapaAlocacao mapaAlocacao, final Date startDate, final Date endDate);
}
