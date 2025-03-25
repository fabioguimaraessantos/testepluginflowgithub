package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPriceTableHistoryService;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.persistence.dao.IPriceTableHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriceTableHistoryService implements IPriceTableHistoryService {

    /**
     * DAO Instance From Price Table History.
     */
    @Autowired
    private IPriceTableHistoryDao dao;

    @Override
    public void create(PriceTableHistory entity) {
        dao.create(entity);
    }

    @Override
    public PriceTableHistory merge(PriceTableHistory entity) {
        return dao.merge(entity);
    }

    @Override
    public List<PriceTableHistory> findHistoryByPriceTable(Long priceTable) {
        return dao.findHistoryByPriceTable(priceTable);
    }

    @Override
    public PriceTableHistory findLastOneByPriceTable(Long priceTable) {

        List<PriceTableHistory> history = findHistoryByPriceTable(priceTable);
        if (history != null && !history.isEmpty())
            return history.get(0);

        return null;
    }

    @Override
    public Boolean hasHistory(Long priceTable) {

        List<PriceTableHistory> history = findHistoryByPriceTable(priceTable);
        if (history != null && !history.isEmpty())
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    @Override
    public List<PriceTableHistory> findLastEditedHistories(Long priceTable) {

        List<PriceTableHistory> editedHistories = new ArrayList<PriceTableHistory>();
        List<PriceTableHistory> histories = findHistoryByPriceTable(priceTable);

        if (histories != null && !histories.isEmpty()) {
            for (PriceTableHistory history : histories) {
                if (Constants.PRICE_TABLE_STATUS_APPROVED.equals(history.getStatus()) ||
                        Constants.PRICE_TABLE_STATUS_NOT_APPROVED.equals(history.getStatus()))
                    return editedHistories;

                if (!Constants.PRICE_TABLE_STATUS_ON_APPROVAL.equals(history.getStatus()))
                    editedHistories.add(history);
            }
        }

        return editedHistories;
    }

    /**
     * @param priceTable
     * @param historyId
     * @return List IDs from Price Table
     */
    public List<Long> findHistoryIdsFromPriceTable(Long priceTable, Long historyId) {

        List<PriceTableHistory> histories = findHistoryByPriceTable(priceTable);
        if (histories != null && !histories.isEmpty()) {
            List<Long> ids = new ArrayList<>();
            for (PriceTableHistory history : histories) {
                if (history.getId() <= historyId)
                    ids.add(history.getId());
            }

            if (!ids.isEmpty())
                return ids;
        }

        return null;
    }
}