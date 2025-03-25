package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CargoPmsCargo;


/**
 * 
 * A classe ICargoPmsCargoService proporciona as funcionalidades de interface
 * para CargoPmsCargoService.
 * 
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface ICargoPmsCargoService {

    /**
     * Cria entidade no banco.
     * 
     * @param entity
     *            entidade
     */
    void createCargoPmsCargo(final CargoPmsCargo entity);

    /**
     * Cusca por cargoPms.
     * 
     * @param cargoPms
     *            cargo.
     * @return lista de cargoPms.
     */
    List<CargoPmsCargo> findCargoPmsCargoByCargoPms(final CargoPms cargoPms);

    /**
     * remoce cargo.
     * 
     * @param cargoPmsCargo
     *            cargo.
     */
    void removeCargoPmsCargo(final CargoPmsCargo cargoPmsCargo);

}
