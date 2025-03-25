package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.FollowCliente;


/**
 * 
 * A classe IFollowClienteDao proporciona as funcionalidades de interface para FollowClienteDao.
 *
 * @since 30/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 *
 */
public interface IFollowClienteDao extends IAbstractDao<Long, FollowCliente> {

    /**
     * Busca por codigoLogin.
     * @param login codigoLogin.
     * @return listResult.
     */
    List<FollowCliente> findByCodigoLogin(final String login);
    
    /**
     * Busca por Cliente e login.
     * @param cliente cliente.
     * @param login login.
     * @return FollowCliente.
     */
    FollowCliente findByClienteAndCodLogin(final Cliente cliente,
            final String login);
    
    /**
     * Busca por cliente.
     * @param cliente cliente
     * @return listResult.
     */
    List<FollowCliente> findByCliente(final Cliente cliente);
    
}
