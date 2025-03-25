package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CustoTceGrupoCusto;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.ICustoTceGrupoCustoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 24/02/2010
 */
@Repository
public class CustoTceGrupoCustoDao extends
        AbstractDao<Long, CustoTceGrupoCusto> implements ICustoTceGrupoCustoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo CustoTceGrupoCusto
     */
    @Autowired
    public CustoTceGrupoCustoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CustoTceGrupoCusto.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CustoTceGrupoCusto> findAll() {
        List<CustoTceGrupoCusto> listResult = getJpaTemplate()
                .findByNamedQuery(CustoTceGrupoCusto.FIND_ALL);

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
    public List<CustoTceGrupoCusto> findByPessoaAndDataMes(final Pessoa pessoa,
            final Date dataMes) {

        List<CustoTceGrupoCusto> listResult = getJpaTemplate()
                .findByNamedQuery(
                        CustoTceGrupoCusto.FIND_BY_PESSOA_AND_DATA_MES,
                        new Object[] {pessoa.getCodigoPessoa(), dataMes });

        return listResult;
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
    public CustoTceGrupoCusto findMaxStartDateByPessoa(final Pessoa pessoa) {

        List<CustoTceGrupoCusto> listResult = getJpaTemplate().findByNamedQuery(
                CustoTceGrupoCusto.FIND_MAX_START_DATE_BY_PESSOA,
                new Object[] {pessoa.getCodigoPessoa(),
                        pessoa.getCodigoPessoa() });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}