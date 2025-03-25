package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.Recurso;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 26/01/2010
 */
public interface IPapelAlocacaoDao extends IAbstractDao<Long, PapelAlocacao> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PapelAlocacao> findByFilter(final PapelAlocacao filter);
    
    /**
     * Busca uma lista de entidades pelo filtro - Fetch.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PapelAlocacao> findByFilterFetch(final PapelAlocacao filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PapelAlocacao> findAll();
    
    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PapelAlocacao> findAllActive();
    
    /**
     * Retorna todas as entidades.
     * 
     * @param recurso - Recurso associado ao PapelAlocacao.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    PapelAlocacao findByRecurso(final Recurso recurso);
    
    /**
     * Busca uma lista de entidades sem associacao com o TcePapelAlocacao.
     * 
     * @param dataMes
     *            - data do fechamento.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PapelAlocacao> findAllNotTceAssociated(final Date dataMes);

}