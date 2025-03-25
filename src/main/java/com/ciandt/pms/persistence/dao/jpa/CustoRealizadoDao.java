package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.CustoRealizado;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.ICustoRealizadoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 24/02/2010
 */
@Repository
public class CustoRealizadoDao extends AbstractDao<Long, CustoRealizado>
        implements ICustoRealizadoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo CustoRealizado
     */
    @Autowired
    public CustoRealizadoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CustoRealizado.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CustoRealizado> findAll() {
        List<CustoRealizado> listResult = getJpaTemplate().findByNamedQuery(
                CustoRealizado.FIND_ALL);

        return listResult;
    }

    /**
     * Busca uma lista de entidades pela Pessoa e dataMes.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public List<CustoRealizado> findByPessoaAndDataMes(final Pessoa pessoa,
            final Date dataMes) {

        List<CustoRealizado> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoRealizado.FIND_BY_PESSOA_AND_DATA_MES,
                        new Object[]{pessoa.getCodigoPessoa(), dataMes});

        return listResult;
    }

    /**
     * Busca uma lista de entidades pela Pessoa, dataMes e ContratoPratica.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public List<CustoRealizado> findByPessoaAndContratoPratica(
            final Pessoa pessoa, final Date dataMes, final ContratoPratica cp) {

        List<CustoRealizado> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoRealizado.FIND_BY_PESSOA_AND_CONTRATO_PRATICA,
                        new Object[]{pessoa.getCodigoPessoa(),
                                cp.getCodigoContratoPratica(), dataMes});

        return listResult;
    }

    /**
     * Busca uma lista de entidades pela Pessoa, dataMes e ContratoPratica.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public Double getTotalAlocacaoPessoa(final Pessoa pessoa,
            final Date dataMes, final ContratoPratica cp) {

        List<BigDecimal> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoRealizado.GET_TOTAL_ALOCACAO_PESSOA,
                        new Object[]{pessoa.getCodigoPessoa(),
                                cp.getCodigoContratoPratica(), dataMes});

        if (listResult.isEmpty()) {
            return Double.valueOf(0);
        }

        return ((BigDecimal) listResult.get(0)).doubleValue();
    }

    /**
     * Busca a entidade com maior data inicio da vigencia.
     * 
     * @param pessoa
     *            entidade populada com os valores da Pessoa.
     * 
     * @return lista de entidades que atendem ao criterio da Pessoa.
     */
    @SuppressWarnings("unchecked")
    public CustoRealizado findMaxStartDateByPessoa(final Pessoa pessoa) {

        List<CustoRealizado> listResult =
                getJpaTemplate().findByNamedQuery(
                        CustoRealizado.FIND_MAX_START_DATE_BY_PESSOA,
                        new Object[]{pessoa.getCodigoPessoa(),
                                pessoa.getCodigoPessoa()});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

    /**
     * Busca uma lista de entidades pelo ContratoPratica e dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * @param cp
     *            entidade contrato-pratica.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    @SuppressWarnings("unchecked")
    public List<CustoRealizado> findByContratoPraticaAndDate(
            final ContratoPratica cp, final Date dataMes) {

        List<CustoRealizado> result =
                getJpaTemplate().findByNamedQuery(
                        CustoRealizado.FIND_BY_CONTRATO_PRATICA_AND_DATE,
                        new Object[]{cp.getCodigoContratoPratica(), dataMes});

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

}