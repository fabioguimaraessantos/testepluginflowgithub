package com.ciandt.pms.business.service;

import com.ciandt.pms.model.ItemPriceTableHistory;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public interface IItemPriceTableHistoryService {

    List<ItemPriceTableHistory> findItemsToApproveByPriceTableId(final Long priceTableId);

    List<ItemPriceTableHistory> findItemsHistoryByPriceTableId(final Long priceTableId);

    List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableId(final Long priceTableId);

    List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistory(final Long priceTableHistory);

    Map<Long, ItemPriceTableHistory> findMapItemsToApproveByPriceTableId(final Long priceTableId);

    Map<Long, ItemPriceTableHistory> findMapItemsByPriceTableHistory(final Long priceTableHistory);

    List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistories(List<Long> histories);

    Map<Long, ItemPriceTableHistory> findMapItemsByPriceTableHistories(List<PriceTableHistory> histories, Boolean isApproved);

    Map<Long, ItemPriceTableHistory> findMapAllItemsHistoryByPriceTableId(Long priceTableId);

    Map<Long, ItemPriceTableHistory> findMapItemsHistoryByPriceTableId(Long priceTableId);

    Map<Long, ItemPriceTableHistory> findMapItemsHistoryByPriceTableHistoriesOrdered(List<Long> histories);

    /**
     * Merge a new row in history of item price table
     * @param entity - Item Price Table History
     * @return ItemPriceTableHistory - Updated Item Price Table History Updated
     */
    @Transactional
    ItemPriceTableHistory merge(ItemPriceTableHistory entity);

    /**
     * Creates a news rows in history of item price table
     * @param entities - List Items Price Table History
     */
    @Transactional
    void saveAll(final List<ItemPriceTableHistory> entities);

    /**
     *
     * @param item
     * @param priceTableHistory
     * @param isApproved
     */
    @Transactional
    void createItemActionCreated(ItemTabelaPreco item, Long priceTableHistory, boolean isApproved);

    /**
     *
     * @param item
     * @param priceTableHistory
     * @param isApproved
     */
    @Transactional
    void createItemActionDeleted(ItemTabelaPreco item, Long priceTableHistory, boolean isApproved);
}
