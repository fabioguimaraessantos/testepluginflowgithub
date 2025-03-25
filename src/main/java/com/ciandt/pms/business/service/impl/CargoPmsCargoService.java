package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.ICargoPmsCargoService;
import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CargoPmsCargo;
import com.ciandt.pms.persistence.dao.ICargoPmsCargoDao;


/**
 * 
 * A classe CargoPmsCargoService proporciona as funcionalidades de servico para a entidade CargoPmsCargo.
 *
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Carlos Augusto Ribeiro Mantovani</a>
 *
 */
@Service
public class CargoPmsCargoService implements ICargoPmsCargoService {
    
    /** Instancia de dao. */
    @Autowired
    private ICargoPmsCargoDao dao;
    
    /**
     * Cria entidade no banco.
     * @param entity entidade
     */
    @Transactional
    public void createCargoPmsCargo(final CargoPmsCargo entity) {
        dao.create(entity);
    }

    /**
     * Cusca por cargoPms.
     * @param cargoPms cargo.
     * @return lista de cargoPms.
     */
    @Transactional
    public List<CargoPmsCargo> findCargoPmsCargoByCargoPms(final CargoPms cargoPms) {
        return dao.findCargoPmsCargoByCargoPms(cargoPms);
    }
    
    /**
     * remoce cargo.
     * @param cargoPmsCargo cargo.
     */
    @Transactional
    public void removeCargoPmsCargo(final CargoPmsCargo cargoPmsCargo) {
        dao.remove(cargoPmsCargo);
    }
}
