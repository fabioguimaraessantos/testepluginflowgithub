package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.ItemPriceTableHistory;
import com.ciandt.pms.persistence.dao.IItemPriceTableHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemPriceTableHistoryDao extends AbstractDao<Long, ItemPriceTableHistory> implements IItemPriceTableHistoryDao {

    /**
     * Default.
     *
     * @param factory da entidade
     */
    @Autowired
    public ItemPriceTableHistoryDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ItemPriceTableHistory.class);
    }


    @Override
    public List<ItemPriceTableHistory> findItemsToApproveByPriceTableId(Long priceTableId) {
        return  getJpaTemplate().findByNamedQuery(ItemPriceTableHistory.FIND_TO_APPROVE_BY_PRICE_TABLE, priceTableId);
    }

    @Override
    public List<ItemPriceTableHistory> findItemsHistoryByPriceTableId(Long priceTableId) {
        return  getJpaTemplate().findByNamedQuery(ItemPriceTableHistory.FIND_HISTORY_BY_PRICE_TABLE, priceTableId);
    }

    @Override
    public List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableId(Long priceTableId) {
        return  getJpaTemplate().findByNamedQuery(ItemPriceTableHistory.FIND_ALL_BY_PRICE_TABLE, priceTableId);
    }

    @Override
    public List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistory(Long priceTableHistory) {
        return  getJpaTemplate().findByNamedQuery(ItemPriceTableHistory.FIND_ALL_BY_PRICE_TABLE_HISTORY, priceTableHistory);
    }

    @Override
    public List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistories(List<Long> histories) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("histories", histories);
        return getJpaTemplate().findByNamedQueryAndNamedParams(ItemPriceTableHistory.FIND_ALL_BY_PRICE_TABLE_HISTORIES, params);
    }
}