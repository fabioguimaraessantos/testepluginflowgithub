package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DespesaMes;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.TipoDespesa;


/**
 * 
 * A classe IDespesaDao proporciona a interface de acesso a 
 * camada de persistencia referente a entidade Despesa.
 *
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IDespesaMesDao extends IAbstractDao<Long, DespesaMes> {

    /**
     * Busca uma lista de entidades pelo contrato-pratica
     * e tipo de despesa.
     * 
     * @param cp entidade do tipo ContratoPratica.
     * 
     * @param tipo entidade do tipo TipoDespesa.
     * 
     * @return lista de entidades do tipo Despesa
     */
    List<DespesaMes> findByContratoPraticaAndTipo(
            final ContratoPratica cp, final TipoDespesa tipo);
    
    /**
     * Busca uma lista de entidades pelo contrato-pratica
     * e tipo de despesa.
     * 
     * @param mapDesp entidade do tipo MapaDespesa.
     * 
     * @param tipo entidade do tipo TipoDespesa.
     * 
     * @return lista de entidades do tipo Despesa
     */
    List<DespesaMes> findByMapaDespesaAndTipo(
            final MapaDespesa mapDesp, final TipoDespesa tipo);
    
    /**
     * Busca a maior data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a maior data da despesa
     * 
     * @return retorna a marior data
     */
    Date findMaxDateByMapa(final MapaDespesa mapa);

    /**
     * Busca a menor data referente ao mapa de despesa.
     * 
     * @param mapa
     *            que se deseja obter a menor data da despesa
     * 
     * @return retorna a menor data
     */
    Date findMinDateByMapa(final MapaDespesa mapa);
    
    /**
     * Busca uma despesa de debito pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a menor data
     */
    DespesaMes findDebitoByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes);
    
    /**
     * Busca as despesas, que não estão dentro 
     * do periodo passado por aprametro.
     * 
     * @param mapa
     *          entidade do tipo MapaDespesa.
     * @param startDate
     *          Data referente ao inicio do periodo.
     * @param endDate
     *          Data referente ao fim do periodo.
     *                
     * @return retorna uma lista de DespesaMes.
     */
    List<DespesaMes> findByMapaAndNotInRange(
            final MapaDespesa mapa, final Date startDate, final Date endDate);
    
    /**
     * Busca uma despesa de pelo mapa, tipo e mes.
     * 
     * @param mapa que se deseja obter a menor data da despesa
     * @param tipoDesp entidade TipoDespesa
     * @param dataMes data do tipo Date.
     * 
     * @return retorna a lista de DespesaMes
     */
    List<DespesaMes> findByMapaTipoAndDate(final MapaDespesa mapa, 
            final TipoDespesa tipoDesp, final Date dataMes);
}
