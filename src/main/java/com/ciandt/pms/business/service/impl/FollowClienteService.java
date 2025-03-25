package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IFollowClienteService;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.FollowCliente;
import com.ciandt.pms.persistence.dao.IFollowClienteDao;


/**
 * 
 * A classe FollowClienteService proporciona as funcionalidades de serviço para FollowCliente.
 *
 * @since 30/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
@Service
public class FollowClienteService implements IFollowClienteService {

    /** Instancia de dao. */
    @Autowired
    private IFollowClienteDao dao;
    
    /**
     * Cria Seguidor de cliente.
     * @param entity FollowCliente
     */
    @Transactional
    public void createFollowCliente(final FollowCliente entity) {
        dao.create(entity);
    }
    
    /**
     * Remove seguidor.
     * @param entity seguidor.
     */
    @Transactional
    public void removeFollowCliente(final FollowCliente entity) {
        dao.remove(entity);
    }
    
    /**
     * Busca por codigoLogin.
     * @param login codigoLogin.
     * @return listResult.
     */
    public List<FollowCliente> findByCodigoLogin(final String login) {
        return dao.findByCodigoLogin(login);
    }
    
    /**
     * Busca por cliente e login.
     * @param cliente cliente.
     * @param login login.
     * @return FollowCliente.
     */
    public FollowCliente findByClienteAndCodLogin(final Cliente cliente, final String login) {
        return dao.findByClienteAndCodLogin(cliente, login);
    }
    
    /**
     * Busca por cliente.
     * @param cliente cliente
     * @return listResult
     */
    public List<FollowCliente> findByCliente(final Cliente cliente) {
        return dao.findByCliente(cliente);
    }
}
