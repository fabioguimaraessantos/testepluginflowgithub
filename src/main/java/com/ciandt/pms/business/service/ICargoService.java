package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.CargoPms;


/**
 * 
 * A classe ICargoService proporciona as funcionalidades de interface para
 * CargoService.
 * 
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface ICargoService {

    /**
     * Cria entidade no banco.
     * 
     * @param entity
     *            entidade
     * @return true.
     */
    Boolean createCargo(final CargoPms entity);

    /**
     * Busca por id.
     * 
     * @param id
     *            id
     * @return CargoPms.
     */
    CargoPms findCargoPmsById(final Long id);
    
    /**
     * Busca todas as entidades.
     * @return lista
     */
    List<CargoPms> findAllCargoPms();
    
    /**
     * Remove cargo.
     * @param cargo cargo.
     * @return true.
     */
    Boolean removeCargo(final CargoPms cargo);
    
    /**
     * Atualiza cargoPms.
     * @param cargo cargoPms
     * @return true.
     */
    Boolean updateCargo(final CargoPms cargo);

}
