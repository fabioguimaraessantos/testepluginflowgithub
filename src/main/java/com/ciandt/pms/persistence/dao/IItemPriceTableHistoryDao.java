package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.ItemPriceTableHistory;

import java.util.List;

public interface IItemPriceTableHistoryDao extends IAbstractDao<Long, ItemPriceTableHistory>{

    List<ItemPriceTableHistory> findItemsToApproveByPriceTableId(final Long priceTableId);

    List<ItemPriceTableHistory> findItemsHistoryByPriceTableId(final Long priceTableId);

    List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableId(final Long priceTableId);

    List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistory(final Long priceTableHistory);

    List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistories(List<Long> histories);
}
