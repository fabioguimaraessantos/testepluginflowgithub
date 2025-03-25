package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Etiqueta;
import com.ciandt.pms.persistence.dao.IEtiquetaDao;


/**
 * 
 * A classe EtiquetaDao proporciona as funcionalidades de acesso ao banco de
 * dados para a entidade Etiqueta.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class EtiquetaDao extends AbstractDao<Long, Etiqueta> implements
        IEtiquetaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public EtiquetaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Etiqueta.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param clienteId
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Etiqueta> findByCliente(final Long clienteId) {
        List<Etiqueta> listResult = getJpaTemplate().findByNamedQuery(
                Etiqueta.FIND_BY_CLIENTE, new Object[] {clienteId, clienteId});

        return listResult;
    }
    
    /**
     * Busca etiquetas ativas por cliente.
     * 
     * @param clienteId
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Etiqueta> findAtivaByCliente(final Long clienteId) {
        List<Etiqueta> listResult = getJpaTemplate().findByNamedQuery(
                Etiqueta.FIND_ATIVA_BY_CLIENTE, new Object[] {clienteId});

        return listResult;
    }
    
    /**
     * Busca etiquetas que atendem ao criterio nome e cliente.
     * 
     * @param codigoCliente
     *            id do Cliente.
     * @param nomeEtiqueta
     *            nome da Etiqueta.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Etiqueta> findByNomeAndCliente(final String nomeEtiqueta, final Long codigoCliente) {
        List<Etiqueta> listResult = getJpaTemplate().findByNamedQuery(
                Etiqueta.FIND_BY_NOME_CLIENTE, new Object[] {nomeEtiqueta, codigoCliente});

        return listResult;
    }
}
