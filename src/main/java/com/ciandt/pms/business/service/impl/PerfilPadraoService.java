package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPerfilPadraoService;
import com.ciandt.pms.model.CargoPms;
import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.PerfilPadrao;
import com.ciandt.pms.model.Pmg;
import com.ciandt.pms.persistence.dao.IPerfilPadraoDao;


/**
 * 
 * A classe PerfilPadraoService proporciona as funcionalidades de ... para ...
 * 
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public class PerfilPadraoService implements IPerfilPadraoService {

    /** Instancia de dao. */
    @Autowired
    private IPerfilPadraoDao dao;

    /**
     * Persiste um objeto no banco.
     * 
     * @param entity
     *            entidade
     * @return true
     */
    public Boolean createPerfilPadrao(final PerfilPadrao entity) {
        dao.create(entity);
        return Boolean.valueOf(true);
    }

    /**
     * 
     * @param cargo
     *            cargo
     * @param pmg
     *            pmg
     * @param base
     *            base
     * @return PerfilPadrao
     */
    public List<PerfilPadrao> findByCargoPmgAndCidadeBase(final CargoPms cargo,
            final Pmg pmg, final CidadeBase base) {
        return dao.findByCargoPmgAndCidadeBase(cargo, pmg, base);
    }
    
    /**
     * Busca por id.
     * @param id id
     * @return entidade
     */
    public PerfilPadrao findPerfilPadraoById(final Long id) {
        return dao.findById(id);
    }
    
    /**
     * 
     * @param cargoPms cargoPms.
     * @return lista.
     */
    public List<PerfilPadrao> findByCargoPms(final CargoPms cargoPms) {
        return dao.findByCargoPms(cargoPms);
    }

}
