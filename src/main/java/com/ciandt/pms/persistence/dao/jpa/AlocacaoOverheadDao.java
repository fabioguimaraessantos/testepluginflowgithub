package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AlocacaoOverhead;
import com.ciandt.pms.persistence.dao.IAlocacaoOverheadDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 19/07/2010
 */
@Repository
public class AlocacaoOverheadDao extends AbstractDao<Long, AlocacaoOverhead>
        implements IAlocacaoOverheadDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo AlocacaoOverhead
     */
    @Autowired
    public AlocacaoOverheadDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AlocacaoOverhead.class);
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
    public List<AlocacaoOverhead> findByFilter(final AlocacaoOverhead filter) {

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        Long codigoTipoOverhead = filter.getTipoOverhead()
                .getCodigoTipoOverhead();
        String indicadorStatus = filter.getIndicadorStatus();

        List<AlocacaoOverhead> listResult = getJpaTemplate()
                .findByNamedQuery(
                        AlocacaoOverhead.FIND_BY_FILTER,
                        new Object[] {codigoLogin, codigoLogin,
                                codigoTipoOverhead, codigoTipoOverhead,
                                filter.getDataInicio(), filter.getDataFim(),
                                indicadorStatus, indicadorStatus });

        return listResult;
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<AlocacaoOverhead> findAll() {
        List<AlocacaoOverhead> listResult = getJpaTemplate().findByNamedQuery(
                AlocacaoOverhead.FIND_ALL);

        return listResult;
    }

}