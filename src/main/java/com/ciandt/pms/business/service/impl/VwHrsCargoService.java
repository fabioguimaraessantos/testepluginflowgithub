package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwHrsCargoService;
import com.ciandt.pms.model.VwHrsCargo;
import com.ciandt.pms.persistence.dao.IVwHrsCargoDao;


/**
 * 
 * A classe TabelaPerfilPadraoService proporciona as funcionalidades da camada
 * de serviço referentes a entidade VwHrsCargo.
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public class VwHrsCargoService implements IVwHrsCargoService {
    
    /** Dao de VwHrsCargo. */
    @Autowired
    private IVwHrsCargoDao dao;
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<VwHrsCargo> findVwHrsCargoAll() {

        return dao.findAll();
    }
    
    /**
     * Retorna a entidade buscada pelo id.
     * @param id id
     * @return entidade
     */
    public VwHrsCargo findVwHrsCargoById(final Long id) {
        return dao.findById(id);
    }
    
    /**
     * Busca pelo nome.
     * 
     * @param name
     *            nome
     * @return entidade
     */
    public VwHrsCargo findVwHrsCargoByName(final String name) {
        return dao.findVwHrsCargoByName(name);
    }
    
    /**
     * Busca por codigo da view.
     * @param codigo codigo
     * @return entidade
     */
    public VwHrsCargo findVwHrsCargoByCodigo(final Long codigo) {
        return dao.findByCodigo(codigo);
    }
    
    /**
     * Busca por todos os ativos.
     * @return lista.
     */
    public List<VwHrsCargo> findAllActive() {
        return dao.findAllActive();
    }

}
