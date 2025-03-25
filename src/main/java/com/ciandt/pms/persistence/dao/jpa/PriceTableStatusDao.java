package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PriceTableStatus;
import com.ciandt.pms.persistence.dao.IPriceTableStatusDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Define o DAO da entidade.
 *
 * @since 15/08/2022
 * @author <a href="mailto:pricaldeira@ciandt.com">Priscilla Caldeira</a>
 *
 */

@Repository
public class PriceTableStatusDao extends AbstractDao<Long, PriceTableStatus>  implements
        IPriceTableStatusDao {


    /**
     * Construtor padrão.
     *
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public PriceTableStatusDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PriceTableStatus.class);
    }

    @Override
    public List<PriceTableStatus> findPriceTableStatusAll() {
        List<PriceTableStatus> listResult =
                getJpaTemplate().findByNamedQuery(PriceTableStatus.FIND_PRICE_TABLE_STATUS_ALL);

        return listResult;
    }

    @Override
    public PriceTableStatus findByAcronym(final String acronym) {
        List<PriceTableStatus> listResult =
                getJpaTemplate().findByNamedQuery(PriceTableStatus.FIND_PRICE_TABLE_STATUS_BY_ACRONYM
                , new Object[] { acronym });

        if(listResult != null && listResult.get(0) != null){
            return listResult.get(0);
        }
        return null;
    }

    @Override
    public List<PriceTableStatus> findPriceTableStatus(final List<String> acronyms) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("acronyms", acronyms);
        return getJpaTemplate().findByNamedQueryAndNamedParams(PriceTableStatus.FIND_PRICE_TABLE_STATUS, params);
    }

}
