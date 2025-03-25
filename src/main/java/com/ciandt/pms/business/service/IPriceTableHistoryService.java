package com.ciandt.pms.business.service;

import com.ciandt.pms.model.PriceTableHistory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface IPriceTableHistoryService {

    /**
     * Create a new row in history of price table
     * @param entity - Price Table History
     */
    @Transactional
    void create(final PriceTableHistory entity);

    /**
     *
     * @param entity
     * @return
     */
    @Transactional
    PriceTableHistory merge(PriceTableHistory entity);

    /**
     *
     * @param priceTable
     * @return
     */
    List<PriceTableHistory> findHistoryByPriceTable(Long priceTable);

    /**
     *
     * @param priceTable
     * @return
     */
    PriceTableHistory findLastOneByPriceTable(Long priceTable);

    /**
     *
     * @param priceTable
     * @return
     */
    Boolean hasHistory(Long priceTable);

    /**
     *
     * @param priceTable
     * @return
     */
    List<PriceTableHistory> findLastEditedHistories(Long priceTable);

    /**
     *
     * @param priceTable
     * @param historyId
     * @return
     */
    List<Long> findHistoryIdsFromPriceTable(Long priceTable, Long historyId);
}