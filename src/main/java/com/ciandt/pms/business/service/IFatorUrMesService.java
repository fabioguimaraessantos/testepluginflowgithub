package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.MapaAlocacao;


/**
 * 
 * A classe IFatorUrMesService proporciona a interface de acesso
 * a camada de serviço referente a entidade FatorUrMes.
 *
 * @since 09/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IFatorUrMesService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    FatorUrMes findFatorUrMesById(final Long id);
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createFatorUrMes(final FatorUrMes entity);
    
    /**
     * realiza o update de uma entidade.
     * 
     * @param entity
     *            entidade a realizar o update.
     */
    @Transactional
    void updateFatorUrMes(final FatorUrMes entity);
    
    /**
     * remove uma entidade.
     * 
     * @param entity
     *            entidade a realizar a remoção.
     */
    @Transactional
    void removeFatorUrMes(final FatorUrMes entity);
    
    /**
     * Deleta os FatoresUrMes, que não estão dentro 
     * do periodo do mapa de alocação.
     * 
     * @param mapa
     *          MapaAlocacao que se deseja deletar os FatorUrMes.              
     */
    @Transactional
    void deleteFatorUrByMapaAndNotInRange(final MapaAlocacao mapa);
     
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
    FatorUrMes findFatorUrMesByMapaAndDate(
            final MapaAlocacao mapa, final Date dataMes);
    
    /**
     * Busca a maior data referente ao mapa de alocacao.
     * 
     * @param mapa que se deseja obter a maior data de alocação
     * 
     * @return retorna a marior data
     */
    Date findFatorUrMesMaxDateByMapa(final MapaAlocacao mapa);
    
    /**
     * Busca a menor data referente ao mapa de alocacao.
     *  
     * @param mapa que se deseja obter a menor data de alocação 
     *  
     * @return retorna a menor data
     */
    Date findFatorUrMesMinDateByMapa(final MapaAlocacao mapa);
    
    /**
     * Busca todos os fatores de um mapa.
     * 
     * @param mapa que se deseja obter os fatores.
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findByMapa(final MapaAlocacao mapa);
    
    /**
     * Busca todos os fatores de um mapa por periodo.
     * 
     * @param mapa - que se deseja obter os fatores.
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findFatorUrMesByMapaAndPeriod(final MapaAlocacao mapa);
    
    /**
     * Retorna os UR mes a partir da data passada por parametro.
     * 
     * @param mapa - que se deseja obter os fatores.
     * @param startDate - data inicial
     * 
     * @return retorna uma lista de FatorUrMes.
     */
    List<FatorUrMes> findFatorUrByMapaAndStartDate(
            final MapaAlocacao mapa, final Date startDate);
    
}
