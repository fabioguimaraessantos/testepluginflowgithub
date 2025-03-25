package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAceleradorService;
import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.persistence.dao.IAceleradorDao;


/**
 * 
 * A classe AceleradorService proporciona as funcionalidades 
 * da camada de serviço referente a entidade Acelerador.
 *
 * @since 06/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class AceleradorService implements IAceleradorService {
    
    /** Instancia do DAO do acelerador. */
    @Autowired
    private IAceleradorDao dao;
    
    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista de Acelerador.
     */
    public List<Acelerador> findAceleradorAllActive() {
        return dao.findAllActive();
    }
    
    /**
     * Busca a entidade pelo id.
     * 
     * @param id - id da entidade
     * 
     * @return retorna uma instancia do tipo Acelerador
     */
    public Acelerador findAceleradorById(final Long id) {
        return dao.findById(id);
    }

}
