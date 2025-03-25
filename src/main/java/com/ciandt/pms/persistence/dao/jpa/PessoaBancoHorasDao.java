package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.PessoaBancoHoras;
import com.ciandt.pms.persistence.dao.IPessoaBancoHorasDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 12/08/2010
 */
@Repository
public class PessoaBancoHorasDao extends AbstractDao<Long, PessoaBancoHoras>
        implements IPessoaBancoHorasDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo PessoaBancoHoras
     */
    @Autowired
    public PessoaBancoHorasDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PessoaBancoHoras.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<PessoaBancoHoras> findByFilter(final PessoaBancoHoras filter) {

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Date dataMes = filter.getDataMes();

        List<PessoaBancoHoras> listResult = getJpaTemplate().findByNamedQuery(
                PessoaBancoHoras.FIND_BY_FILTER,
                new Object[] {codigoLogin, codigoLogin, dataMes });

        return listResult;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public PessoaBancoHoras findUnique(final PessoaBancoHoras filter) {

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Date dataMes = filter.getDataMes();
        BigDecimal valorFator = filter.getValorFator();

        List<PessoaBancoHoras> listResult = getJpaTemplate().findByNamedQuery(
                PessoaBancoHoras.FIND_UNIQUE,
                new Object[] {codigoLogin, dataMes, valorFator });

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<PessoaBancoHoras> findAll() {
        List<PessoaBancoHoras> listResult = getJpaTemplate().findByNamedQuery(
                PessoaBancoHoras.FIND_ALL);

        return listResult;
    }
    
    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public List<PessoaBancoHoras> findByDataMes(final Date dataMes) {

        List<PessoaBancoHoras> listResult = getJpaTemplate().findByNamedQuery(
                PessoaBancoHoras.FIND_BY_DATA_MES,
                new Object[] {dataMes });

        return listResult;
    }

}