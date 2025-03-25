package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CidadeBase;
import com.ciandt.pms.model.CustoInfraBase;
import com.ciandt.pms.persistence.dao.ICustoInfraBaseDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 01/08/2009
 */
@Repository
public class CustoInfraBaseDao extends AbstractDao<Long, CustoInfraBase>
        implements ICustoInfraBaseDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo CustoInfraBase
     */
    @Autowired
    public CustoInfraBaseDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, CustoInfraBase.class);
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
    public List<CustoInfraBase> findByFilter(final CustoInfraBase filter) {
        List<CustoInfraBase> listResult = getJpaTemplate().findByNamedQuery(
                CustoInfraBase.FIND_BY_FILTER,
                new Object[] {filter.getCidadeBase().getCodigoCidadeBase(),
                        filter.getCidadeBase().getCodigoCidadeBase() });
        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<CustoInfraBase> findAll() {
        List<CustoInfraBase> listResult = getJpaTemplate().findByNamedQuery(
                CustoInfraBase.FIND_ALL);

        return listResult;
    }

    /**
     * Busca a entidade filtrando pelos parametros.
     * 
     * @param dataMes
     *            do CustoInfraBase.
     * @param cidadeBase
     *            entidade populada com os valores da CidadeBase.
     * 
     * @return lista de entidades que atendem ao criterio da CidadeBase e
     *         dataMes.
     */
    @SuppressWarnings("unchecked")
    public CustoInfraBase findByDateAndCidadeBase(final Date dataMes,
            final CidadeBase cidadeBase) {

        List<CustoInfraBase> listResult = getJpaTemplate().findByNamedQuery(
                CustoInfraBase.FIND_BY_DATE_AND_CIDADE_BASE,
                new Object[] {cidadeBase.getCodigoCidadeBase(), dataMes });

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}