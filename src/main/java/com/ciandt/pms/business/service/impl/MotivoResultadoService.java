package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IMotivoResultadoService;
import com.ciandt.pms.model.MotivoResultado;
import com.ciandt.pms.persistence.dao.IMotivoResultadoDao;


/**
 * 
 * A classe MotivoResultadoService proporciona a interface de acesso para a
 * camada de serviço referente a entidade {@link MotivoResultado}.
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public class MotivoResultadoService implements IMotivoResultadoService {

    /** Instancia do DAO. */
    @Autowired
    private IMotivoResultadoDao dao;

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    public List<MotivoResultado> findMotivoResultadoAll() {
        return dao.findAll();
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    @Override
    public List<MotivoResultado> findMotivoResultadoAllAtivos() {
        return dao.findAllAtivos();
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
    public MotivoResultado findMotivoResultadoById(final Long id) {
        return dao.findById(id);
    }

}