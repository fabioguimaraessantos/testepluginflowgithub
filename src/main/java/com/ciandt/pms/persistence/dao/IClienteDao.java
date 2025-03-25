package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;


/**
 * 
 * Define a interface do DAO da entidade.
 *
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IClienteDao extends IAbstractDao <Long, Cliente> {

    /**
     * Busca uma lista de entidades pelo filtro - somente ClientePai.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Cliente> findByFilterClientePai(final Cliente filter);
    
    /**
     * Busca uma lista de entidades pelo filtro - somente ClienteFilho.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Cliente> findByFilterClienteFilho(final Cliente filter);
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Cliente> findAll();
    
    /**
     * Retorna todas os clientes pai.
     * 
     * @return retorna uma lista com todos os clientes pai.
     */
    List<Cliente> findAllClientePai();

    List<Cliente> findAllClientePaiForComboBox();

    /**
     * Retorna todas os clientes filho.
     * 
     * @return retorna uma lista com todos os clientes filho.
     */
    List<Cliente> findAllClienteFilho();
    
    /**
     * Busca uma lista de entidades pelo cliente pai.
     * 
     * @param clientePai
     *            entidade do tipo Cliente pai de outras entidades.
     * 
     * @return lista de entidades filhas, referente ao pai passado por parametro.
     */
    List<Cliente> findByClientePai(final Cliente clientePai);

    /**
     * Retorna um {@link Cliente} relacionado a {@code centroLucro}.
     *
     * @param centroLucro
     * @return {@link Cliente}
     */
    List<Cliente> findByCentroLucro(final CentroLucro centroLucro);

    /**
     * Retorna um {@link Cliente} relacionado a {@code codigoOracleTaxpayerCustomer}.
     *
     * @param codigoOracleTaxpayerCustomer
     * @return {@link Cliente}
     */
    List<Cliente> findByCodigoOracleTaxpayerCustomer(String codigoOracleTaxpayerCustomer);
}