package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.ICargoPmsCargoService;
import com.ciandt.pms.business.service.ICargoService;
import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CargoPmsCargo;
import com.ciandt.pms.persistence.dao.ICargoDao;


/**
 * 
 * A classe CargoService proporciona as funcionalidades de servico para a entidade CargoPms.
 *
 * @since 05/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public class CargoService implements ICargoService {
    
    /** Instancia de CargoDao. */
    @Autowired
    private ICargoDao dao;
    
    /** Instancia de cargoPmsCargo. */
    @Autowired
    private ICargoPmsCargoService cargoPmsCargoService;
    
    
    /**
     * Cria entidade no banco.
     * @param entity entidade
     * @return true.
     */
    @Transactional
    public Boolean createCargo(final CargoPms entity) {
        dao.create(entity);
        return Boolean.valueOf(true);
    }
    
    /**
     * Busca por id.
     * @param id id
     * @return CargoPms.
     */
    @Transactional
    public CargoPms findCargoPmsById(final Long id) {
        return dao.findById(id);
    }
    
    /**
     * Busca todas as entidades.
     * @return lista
     */
    public List<CargoPms> findAllCargoPms() {
        return dao.findAll();
    }
    
    /**
     * Remove cargoPms e seus filhos.
     * @param cargo cargoPms.
     * @return true.
     */
    @Transactional
    public Boolean removeCargo(final CargoPms cargo) {        
        for (CargoPmsCargo cargoPms : cargoPmsCargoService.findCargoPmsCargoByCargoPms(cargo)) {
            cargoPmsCargoService.removeCargoPmsCargo(cargoPms);
        }        
        dao.remove(cargo);
        return true;
    }
    
    /**
     * Atualiza cargoPms.
     * @param cargo cargoPms
     * @return true.
     */
    @Transactional
    public Boolean updateCargo(final CargoPms cargo) {
        dao.update(cargo);
        return true;
    }
        

}
