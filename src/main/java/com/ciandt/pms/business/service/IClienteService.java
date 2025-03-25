package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Define a interface do Service da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IClienteService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createCliente(final Cliente entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @throws IntegrityConstraintException
     *             lança uma exceção caso este cliente seja pai e tenha cliente
     *             pai atribuido ou caso o cliente seja pai de outros clientes e
     *             tente inativá-lo
     * 
     */
    @Transactional
    void updateCliente(final Cliente entity)
            throws IntegrityConstraintException;

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @throws IntegrityConstraintException
     *             lança uma exception caso este cliente seja pai de outros
     *             clientes
     * 
     */
    @Transactional
    void removeCliente(final Cliente entity)
            throws IntegrityConstraintException;

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Cliente findClienteById(final Long id);
    
    /**
     * Busca uma lista de entidades pelo filtro - somente ClientePai.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Cliente> findClientePaiByFilter(final Cliente filter);

    /**
     * Busca uma lista de entidades pelo filtro - somente ClienteFilho.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<Cliente> findClienteFilhoByFilter(final Cliente filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Cliente> findClienteAll();

    /**
     * Retorna todas os clientes pai.
     * 
     * @return retorna uma lista com todos os clientes pai.
     */
    List<Cliente> findClienteAllClientePai();

    List<Cliente> findClienteAllClientePaiForComboBox();
    
    /**
     * Retorna todas os clientes filho.
     * 
     * @return retorna uma lista com todos os clientes filho.
     */
    List<Cliente> findClienteAllClienteFilho();
    
    /**
     * Busca uma lista de entidades pelo cliente pai.
     * 
     * @param clientePai
     *            entidade do tipo Cliente pai de outras entidades.
     * 
     * @return lista de entidades filhas, referente ao pai passado por parametro.
     */
    List<Cliente> findClienteByClientePai(final Cliente clientePai);
    
    /**
     * Cria e envia uma mensagem de email de um Cliente. 
     * 
     * @param cliente - entidade do tipo Cliente.
     */
    void sendClienteMail(final Cliente cliente);


    /**
     * Retorna um {@link Cliente} relacionado a {@code centroLucro}.
     *
     * @param centroLucro
     * @return {@link Cliente}
     */
    List<Cliente> findClienteByCentroLucro(final CentroLucro centroLucro);

    Long getCompanyCodeByClientName(String clientName);

    List<Cliente> findClienteByCodigoOracleTaxpayerCustomer(final String codigoOracleTaxpayerCustomer);
}