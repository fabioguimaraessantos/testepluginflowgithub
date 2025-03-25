package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.VwHrsCargo;


/**
 * 
 * A classe ITabelaPerfilPadraoDao proporciona a interfece de acesso para o
 * banco de dados referente a entidade VwHrsCargo.
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public interface IVwHrsCargoDao extends IAbstractDao<Long, VwHrsCargo> {
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<VwHrsCargo> findAll();

    /**
     * Busca pelo nome.
     * 
     * @param name
     *            nome
     * @return entidade
     */
    VwHrsCargo findVwHrsCargoByName(final String name);
    
    /**
     * Busca por codigo da view.
     * @param codigo codigo
     * @return entidade
     */
    VwHrsCargo findByCodigo(final Long codigo);
    
    /**
     * Busca todas as entidades ativas.
     * 
     * @return lista.
     */
    List<VwHrsCargo> findAllActive();
}
