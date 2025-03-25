package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.FollowCliente;
import com.ciandt.pms.persistence.dao.IFollowClienteDao;


/**
 * 
 * A classe FollowClienteDao proporciona as funcionalidades de DAO para
 * FollowCliete.
 * 
 * @since 30/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class FollowClienteDao extends AbstractDao<Long, FollowCliente>
        implements IFollowClienteDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public FollowClienteDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FollowCliente.class);
    }

    /**
     * Busca por codigoLogin.
     * 
     * @param login
     *            codigoLogin.
     * @return listResult.
     */
    @SuppressWarnings("unchecked")
    public List<FollowCliente> findByCodigoLogin(final String login) {
        List<FollowCliente> listResult =
                getJpaTemplate()
                        .findByNamedQuery(FollowCliente.FIND_BY_CODIGO_LOGIN,
                                new Object[]{login});
        return listResult;
    }

    /**
     * Busca por Cliente e login.
     * 
     * @param cliente
     *            cliente.
     * @param login
     *            login.
     * @return FollowCliente.
     */
    @SuppressWarnings("unchecked")
    public FollowCliente findByClienteAndCodLogin(final Cliente cliente,
            final String login) {
        List<FollowCliente> listResult =
                getJpaTemplate().findByNamedQuery(
                        FollowCliente.FIND_BY_CLIENTE_AND_CODIGO_LOGIN,
                        new Object[]{cliente.getCodigoCliente(), login});
        return listResult.get(0);
    }

    /**
     * Busca por cliente.
     * @param cliente cliente
     * @return listResult.
     */
    @SuppressWarnings("unchecked")
    public List<FollowCliente> findByCliente(final Cliente cliente) {
        List<FollowCliente> listResult =
                getJpaTemplate().findByNamedQuery(
                        FollowCliente.FIND_BY_CLIENTE,
                        new Object[]{cliente.getCodigoCliente()});
        return listResult;
    }

}
