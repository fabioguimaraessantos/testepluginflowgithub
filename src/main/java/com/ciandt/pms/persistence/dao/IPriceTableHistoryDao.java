package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.PriceTableHistory;

import java.util.List;

public interface IPriceTableHistoryDao extends IAbstractDao<Long, PriceTableHistory>{

    /**
     *
     * @param priceTable
     * @return
     */
    List<PriceTableHistory> findHistoryByPriceTable(Long priceTable);
}