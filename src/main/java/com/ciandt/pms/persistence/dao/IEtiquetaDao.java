/**
 * Interface da camada de DAO da entidade Etiqueta
 */
package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Etiqueta;


/**
 * 
 * A classe IEtiquetaDao proporciona a interface de acesso para a camada DAO da
 * entidade Etiqueta.
 * 
 * @since 15/10/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IEtiquetaDao extends IAbstractDao<Long, Etiqueta> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param clienteId
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Etiqueta> findByCliente(final Long clienteId);
    
    /**
     * Busca etiquetas ativas por cliente.
     * 
     * @param codigoCliente
     *            id do Cliente.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Etiqueta> findAtivaByCliente(final Long codigoCliente);
    
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
    List<Etiqueta> findByNomeAndCliente(final String nomeEtiqueta, final Long codigoCliente);
}
