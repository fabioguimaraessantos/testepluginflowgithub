package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITipoOverheadService;
import com.ciandt.pms.model.TipoOverhead;
import com.ciandt.pms.persistence.dao.ITipoOverheadDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public class TipoOverheadService implements ITipoOverheadService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private ITipoOverheadDao dao;

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TipoOverhead findTipoOverheadById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoOverhead> findTipoOverheadAll() {
        return dao.findAll();
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    public List<TipoOverhead> findTipoOverheadAllActive() {
        return dao.findAllActive();
    }

}