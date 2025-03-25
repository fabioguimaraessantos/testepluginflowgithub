package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.ExplicacaoDesvio;


/**
 * 
 * Define a interface do DAO da entidade.
 * 
 * @since 19/04/2011
 * @author cmantovani
 * 
 */
public interface IExplicacaoDesvioDao extends
        IAbstractDao<Long, ExplicacaoDesvio> {

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<ExplicacaoDesvio> findAtivos();

    /**
     * Busca a entidade selecionada.
     * 
     * @return uma ExplicacaoDesvio.
     */
    ExplicacaoDesvio findSelecionado();

}