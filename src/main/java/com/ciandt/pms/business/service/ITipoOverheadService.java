package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.TipoOverhead;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 19/07/2010
 */
@Service
public interface ITipoOverheadService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TipoOverhead findTipoOverheadById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoOverhead> findTipoOverheadAll();

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<TipoOverhead> findTipoOverheadAllActive();

}