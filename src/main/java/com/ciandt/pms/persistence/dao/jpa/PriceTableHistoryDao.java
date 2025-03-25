package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.persistence.dao.IPriceTableHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class PriceTableHistoryDao extends AbstractDao<Long, PriceTableHistory> implements IPriceTableHistoryDao {

    /**
     * Default.
     *
     * @param factory da entidade
     */
    @Autowired
    public PriceTableHistoryDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, PriceTableHistory.class);
    }


    @Override
    public List<PriceTableHistory> findHistoryByPriceTable(Long priceTable) {
        return  getJpaTemplate().findByNamedQuery(PriceTableHistory.FIND_ALL_BY_PRICE_TABLE, priceTable);
    }
}