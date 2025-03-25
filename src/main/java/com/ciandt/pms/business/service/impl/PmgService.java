package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPmgService;
import com.ciandt.pms.model.Pmg;
import com.ciandt.pms.persistence.dao.IPmgDao;


/**
 * 
 * A classe TabelaPerfilPadraoService proporciona as funcionalidades da camada
 * de serviço referentes a entidade Pmg.
 *
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public class PmgService implements IPmgService {
    
    /** Instancia do DAO de PMG. */
    @Autowired
    private IPmgDao dao;
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<Pmg> findPmgAll() {

        return dao.findAll();
    }
    
    /**
     * Busca pelo id do Pmg.
     * @param id id
     * @return Pmg
     */
    public Pmg findPmgById(final Long id) {
        return dao.findById(id);
    }

}
