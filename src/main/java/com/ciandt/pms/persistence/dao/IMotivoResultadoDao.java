package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.MotivoResultado;


/**
 * 
 * A classe IMotivoResultadoDao proporciona as funcionalidades de ... para ...
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public interface IMotivoResultadoDao extends
        IAbstractDao<Long, MotivoResultado> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<MotivoResultado> findAll();

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<MotivoResultado> findAllAtivos();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    MotivoResultado findById(final Long id);

}