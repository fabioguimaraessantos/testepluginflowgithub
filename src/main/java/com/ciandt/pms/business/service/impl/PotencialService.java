package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPotencialService;
import com.ciandt.pms.model.Potencial;
import com.ciandt.pms.persistence.dao.IPotencialDao;


/**
 * 
 * A classe PotencialService proporciona as funcionalidades de Servico para
 * entidade Potencial.
 * 
 * @since 05/04/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public class PotencialService implements IPotencialService {

    /** Instancia do DAO. */
    @Autowired
    private IPotencialDao dao;

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Potencial.
     */
    @Override
    public List<Potencial> findPotencialAllActive() {
        return dao.findAllActive();
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    @Override
    public Potencial findPotencialById(final Long id) {
        return dao.findById(id);
    }

}