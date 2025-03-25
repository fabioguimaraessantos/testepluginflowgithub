/**
 * Classe VwItemHrsFatDealDao que implementa a camada de DAO da entidade VwItemHrsFatDeal 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.VwItemHrsFatDeal;
import com.ciandt.pms.persistence.dao.IVwItemHrsFatDealDao;


/**
 * 
 * A classe VwItemHrsFatDealDao proporciona as funcionalidades de acesso ao
 * banco de dados para a entidade VwItemHrsFatDeal.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class VwItemHrsFatDealDao extends
        AbstractDao<Long, VwItemHrsFatDeal> implements
        IVwItemHrsFatDealDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public VwItemHrsFatDealDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, VwItemHrsFatDeal.class);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param dataAlocacaoPeriodo
     *            data mes/ano da Alocacao.
     * @param codigoContratoPratica
     *            código do ContratoPratica.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    @SuppressWarnings("unchecked")
    public List<VwItemHrsFatDeal> findByFilter(
            final Date dataAlocacaoPeriodo, final Long codigoContratoPratica) {
        List<VwItemHrsFatDeal> listResult = getJpaTemplate()
                .findByNamedQuery(
                        VwItemHrsFatDeal.FIND_BY_FILTER,
                        new Object[] {dataAlocacaoPeriodo,
                                codigoContratoPratica });
        return listResult;
    }

}
