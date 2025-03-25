package com.ciandt.pms.persistence.dao.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import com.ciandt.pms.model.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.persistence.dao.IClienteDao;


/**
 * 
 * Define o DAO da entidade.
 * 
 * @since 31/07/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ClienteDao extends AbstractDao<Long, Cliente> implements
        IClienteDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public ClienteDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Cliente.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro - somente ClientePai.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Cliente> findByFilterClientePai(final Cliente filter) {

        // checa se existe potencial
        Long codigoPotencial = Long.valueOf(0);
        if (filter.getPotencial() != null) {
            codigoPotencial = filter.getPotencial().getCodigoPotencial();
        }

        List<Cliente> listResult =
                getJpaTemplate().findByNamedQuery(
                        Cliente.FIND_BY_FILTER_CLIENTE_PAI,
                        new Object[]{filter.getNomeCliente(),
                                filter.getNomeCliente(),
                                filter.getIndicadorAtivo(),
                                filter.getIndicadorAtivo(), codigoPotencial,
                                codigoPotencial});
        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro - somente ClienteFilho.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<Cliente> findByFilterClienteFilho(final Cliente filter) {
        Long codigoClientePai = Long.valueOf(0);
        if (filter.getCliente() != null) {
            codigoClientePai = filter.getCliente().getCodigoCliente();
        }

        List<Cliente> listResult =
                getJpaTemplate().findByNamedQuery(
                        Cliente.FIND_BY_FILTER_CLIENTE_FILHO,
                        new Object[]{filter.getNomeCliente(),
                                filter.getNomeCliente(),
                                filter.getIndicadorAtivo(),
                                filter.getIndicadorAtivo(), codigoClientePai,
                                codigoClientePai, filter.getCodigoCnpj(),
                                filter.getCodigoCnpj(), filter.getTextoPais(),
                                filter.getTextoPais(), filter.getTextoEstado(),
                                filter.getTextoEstado(),
                                filter.getTextoCidade(),
                                filter.getTextoCidade(),
                                filter.getCodigoMnemonico(),
                                filter.getCodigoMnemonico(),
                                filter.getIndicadorTipo(),
                                filter.getIndicadorTipo()});
        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<Cliente> findAll() {
        List<Cliente> listResult =
                getJpaTemplate().findByNamedQuery(Cliente.FIND_ALL);

        return listResult;
    }

    /**
     * Retorna todas os clientes pai.
     * 
     * @return retorna uma lista com todos os clientes pai.
     */
    @SuppressWarnings("unchecked")
    public List<Cliente> findAllClientePai() {
        List<Cliente> listResult =
                getJpaTemplate().findByNamedQuery(Cliente.FIND_ALL_CLIENTE_PAI);

        return listResult;
    }

    /**
     * Retorna todas os clientes filho.
     * 
     * @return retorna uma lista com todos os clientes filho.
     */
    @SuppressWarnings("unchecked")
    public List<Cliente> findAllClienteFilho() {
        List<Cliente> listResult =
                getJpaTemplate().findByNamedQuery(
                        Cliente.FIND_ALL_CLIENTE_FILHO);

        return listResult;
    }

    public List<Cliente> findAllClientePaiForComboBox() {

        List<Cliente> listResult = getJpaTemplate().findByNamedQuery(
                Cliente.FIND_ALL_CLIENTE_PAI_FOR_COMBOBOX);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo cliente pai.
     * 
     * @param clientePai
     *            entidade do tipo Cliente pai de outras entidades.
     * 
     * @return lista de entidades filhas, referente ao pai passado por
     *         parametro.
     */
    @SuppressWarnings("unchecked")
    public List<Cliente> findByClientePai(final Cliente clientePai) {

        if (clientePai == null) {
            return null;
        }

        List<Cliente> listResult =
                getJpaTemplate().findByNamedQuery(Cliente.FIND_BY_CLIENTE_PAI,
                        new Object[]{clientePai.getCodigoCliente()});

        return listResult;
    }

    /**
     * Retorna um {@link Cliente} relacionado a {@code centroLucro}.
     *
     * @param centroLucro
     * @return {@link Cliente}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Cliente> findByCentroLucro(final CentroLucro centroLucro) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoCentroLucro", centroLucro.getCodigoCentroLucro());

		List<Cliente> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						Cliente.FIND_BY_CENTRO_LUCRO,
						params);

		return results;
    }

    /**
     * Retorna um {@link Cliente} relacionado a {@code codigoOracleTaxpayerCustomer}.
     *
     * @param codigoOracleTaxpayerCustomer
     * @return {@link Cliente}
     */
    @Override
    public List<Cliente> findByCodigoOracleTaxpayerCustomer(String codigoOracleTaxpayerCustomer) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("codigoOracleTaxpayerCustomer", codigoOracleTaxpayerCustomer);
        return getJpaTemplate()
                .findByNamedQueryAndNamedParams(
                        Cliente.FIND_BY_CODIGO_ORACLE_TAXPAYER_CUSTOMER,
                        params);
    }
}