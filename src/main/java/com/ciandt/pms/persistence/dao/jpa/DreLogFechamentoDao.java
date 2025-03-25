package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DreLogFechamento;
import com.ciandt.pms.persistence.dao.IDreLogFechamentoDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 07/06/2010
 */
@Repository
public class DreLogFechamentoDao extends AbstractDao<Long, DreLogFechamento>
        implements IDreLogFechamentoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo DreLogFechamento
     */
    @Autowired
    public DreLogFechamentoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, DreLogFechamento.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<DreLogFechamento> findAll() {
        List<DreLogFechamento> listResult = getJpaTemplate().findByNamedQuery(
                DreLogFechamento.FIND_ALL);

        return listResult;
    }

    /**
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do DreLogFechamento.
     * 
     * @return lista de entidades que atendem ao criterio da dataMes.
     */
    @SuppressWarnings("unchecked")
    public DreLogFechamento findByDataMes(final Date dataMes) {

        List<DreLogFechamento> listResult = getJpaTemplate().findByNamedQuery(
                DreLogFechamento.FIND_BY_DATA_MES, new Object[] {dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}