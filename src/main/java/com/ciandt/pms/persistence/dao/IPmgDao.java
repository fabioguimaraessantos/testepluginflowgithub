package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Pmg;



/**
 * 
 * A classe ITabelaPerfilPadraoDao proporciona a interfece de acesso para o
 * banco de dados referente a entidade TabelaPerfilPadrao.
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public interface IPmgDao extends IAbstractDao<Long, Pmg> {
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Pmg> findAll();

}
