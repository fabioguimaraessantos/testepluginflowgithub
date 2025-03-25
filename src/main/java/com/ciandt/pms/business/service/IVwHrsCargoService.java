package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.VwHrsCargo;


/**
 * 
 * A classe IVwHrsCargoService proporciona as funcionalidades de ... para ...
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public interface IVwHrsCargoService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<VwHrsCargo> findVwHrsCargoAll();
    
    /**
     * Retorna a entidade buscada pelo id.
     * @param id id
     * @return entidade
     */
    VwHrsCargo findVwHrsCargoById(final Long id);
    
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
    VwHrsCargo findVwHrsCargoByCodigo(final Long codigo);
    
    /**
     * Busca por todos os ativos.
     * @return lista.
     */
    List<VwHrsCargo> findAllActive();
    
}
