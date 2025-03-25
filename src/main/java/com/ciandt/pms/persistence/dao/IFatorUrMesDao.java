package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.MapaAlocacao;


/**
 * 
 * A classe IFatorUrMesDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade FatorUrMes.
 *
 * @since 09/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IFatorUrMesDao extends IAbstractDao<Long, FatorUrMes> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param mapa
     *            entidade Mapaalocacao.
     * @param dataMes
     *            entidade do tipo java.util.Date.           
     * 
     * @return lista de entidades que atendem ao criterio de busca.
     */
    FatorUrMes findByMapaAndDate(final MapaAlocacao mapa, final Date dataMes);
    
    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    Date findMaxDateByMapa(MapaAlocacao mapa);
    
    /**
     * Busca a menor data referente ao mapa de alocacao.
     *  
     * @param mapa que se deseja obter a menor data de alocação 
     *  
     * @return retorna a menor data
     */
    Date findMinDateByMapa(MapaAlocacao mapa);
    
    /**
     * Busca todos os fatores de um mapa.
     * 
     * @param mapa
     *            que se deseja obter os fatores.
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findByMapa(final MapaAlocacao mapa);
    
    /**
     * Busca todos os fatores de um mapa por periodo.
     * 
     * @param mapa - que se deseja obter os fatores.
     * @param startDate - data inicial
     * @param endDate - data final
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findByMapaAndPeriod(final MapaAlocacao mapa, 
            final Date startDate, final Date endDate);
    
    /**
     * Retorna os UR mes a partir da data passada por parametro.
     * 
     * @param mapa - que se deseja obter os fatores.
     * @param startDate - data inicial
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findByMapaAndStartDate(
            final MapaAlocacao mapa, final Date startDate);
    
    /**
     * Busca os FatorUrMes, que não estão dentro 
     * do periodo do mapa de alocação passado por aprametro.
     * 
     * @param mapa
     *          MapaAlocacao que se deseja deletar as AlocacaoPeriodo.
     *                
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findByMapaAndNotInRange(final MapaAlocacao mapa);
}
