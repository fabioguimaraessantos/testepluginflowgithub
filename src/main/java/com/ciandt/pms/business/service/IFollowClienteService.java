package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.FollowCliente;


/**
 * 
 * A classe IFollowClienteService proporciona as funcionalidades de interface
 * para FollowClienteService.
 * 
 * @since 30/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IFollowClienteService {

    /**
     * Cria Seguidor de cliente.
     * 
     * @param entity
     *            FollowCliente
     */
    void createFollowCliente(final FollowCliente entity);

    /**
     * Busca por codigoLogin.
     * 
     * @param login
     *            codigoLogin.
     * @return listResult.
     */
    List<FollowCliente> findByCodigoLogin(final String login);

    /**
     * Remove seguidor.
     * 
     * @param entity
     *            seguidor.
     */
    void removeFollowCliente(final FollowCliente entity);

    /**
     * Busca por cliente e login.
     * 
     * @param cliente
     *            cliente.
     * @param login
     *            login.
     * @return FollowCliente.
     */
    FollowCliente findByClienteAndCodLogin(final Cliente cliente,
            final String login);

    /**
     * Busca por cliente.
     * 
     * @param cliente
     *            cliente
     * @return listResult
     */
    List<FollowCliente> findByCliente(final Cliente cliente);

}
