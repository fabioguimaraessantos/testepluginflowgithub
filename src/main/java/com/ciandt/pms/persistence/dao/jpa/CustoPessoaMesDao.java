package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CustoPessoaMes;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.ICustoPessoaMesDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 30/08/2010
 */
@Repository
public class CustoPessoaMesDao extends AbstractDao<Long, CustoPessoaMes>
        implements ICustoPessoaMesDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo CustoRealizado
     */
    @Autowired
    public CustoPessoaMesDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CustoPessoaMes.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CustoPessoaMes> findAll() {
        List<CustoPessoaMes> listResult = getJpaTemplate().findByNamedQuery(
                CustoPessoaMes.FIND_ALL);

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
    public List<CustoPessoaMes> findByPessoaAndDataMes(final Pessoa pessoa,
            final Date dataMes) {

        List<CustoPessoaMes> listResult = getJpaTemplate().findByNamedQuery(
                CustoPessoaMes.FIND_BY_PESSOA_AND_DATA_MES,
                new Object[] {pessoa.getCodigoPessoa(), dataMes });

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
    public List<CustoPessoaMes> findByDataMes(final Date dataMes) {

        List<CustoPessoaMes> listResult = getJpaTemplate().findByNamedQuery(
                CustoPessoaMes.FIND_BY_DATA_MES,
                new Object[] {dataMes });

        return listResult;
    }

}