package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PapelAlocacao;
import com.ciandt.pms.model.TcePapelAlocacao;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 27/01/2010
 */
@Repository
public interface ITcePapelAlocacaoDao extends
        IAbstractDao<Long, TcePapelAlocacao> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TcePapelAlocacao> findAll();
    
    /**
     * Busca uma lista de entidades pelo PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    List<TcePapelAlocacao> findByPapelAlocacao(
            final PapelAlocacao papelAlocacao);
    
    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    TcePapelAlocacao findMaxStartDateByPapelAlocacao(
            final PapelAlocacao papelAlocacao);
    
    /**
     * Busca a entidade no qual intervalo possua a 
     * pada passado por parametro, referente ao PapelAlocacao.
     * 
     * @param papelAlocacao
     *            entidade populada com os valores do PapelAlocacao.
     * @param date 
     *            Data que se deseja saber o valor do TCE do PapelAlocacao.
     * 
     * @return lista de entidades que atendem ao criterio do PapelAlocacao.
     */
    TcePapelAlocacao findByPapelAndDate(
            final PapelAlocacao papelAlocacao , final Date date);

}