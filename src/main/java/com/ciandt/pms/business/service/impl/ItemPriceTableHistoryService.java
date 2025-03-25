package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IItemPriceTableHistoryService;
import com.ciandt.pms.model.ItemPriceTableHistory;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.persistence.dao.IItemPriceTableHistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ItemPriceTableHistoryService implements IItemPriceTableHistoryService {

    private static final String INDICADOR_ACTION_STATUS_CREATED = "C";
    private static final String INDICADOR_ACTION_STATUS_DELETED = "D";

    @Autowired
    private IItemPriceTableHistoryDao dao;

    @Override
    public List<ItemPriceTableHistory> findItemsToApproveByPriceTableId(Long priceTableId) {
        return dao.findItemsToApproveByPriceTableId(priceTableId);
    }

    @Override
    public List<ItemPriceTableHistory> findItemsHistoryByPriceTableId(Long priceTableId) {
        return dao.findItemsHistoryByPriceTableId(priceTableId);
    }

    @Override
    public List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableId(Long priceTableId) {
        return dao.findAllItemsHistoryByPriceTableId(priceTableId);
    }

    @Override
    public Map<Long, ItemPriceTableHistory> findMapItemsToApproveByPriceTableId(Long priceTableId) {
        return getMapByList(findItemsToApproveByPriceTableId(priceTableId));
    }

    @Override
    public ItemPriceTableHistory merge(ItemPriceTableHistory entity) {
        return dao.merge(entity);
    }

    @Override
    public void saveAll(List<ItemPriceTableHistory> entities) {
        if (entities != null && !entities.isEmpty()) {
            for (ItemPriceTableHistory entity : entities)
                dao.merge(entity);
        }
    }

    @Override
    public List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistory(Long priceTableHistory) {
        return dao.findAllItemsHistoryByPriceTableHistory(priceTableHistory);
    }

    @Override
    public Map<Long, ItemPriceTableHistory> findMapItemsByPriceTableHistory(Long priceTableHistory) {
        return getMapByList(findAllItemsHistoryByPriceTableHistory(priceTableHistory));
    }

    @Override
    public List<ItemPriceTableHistory> findAllItemsHistoryByPriceTableHistories(List<Long> histories) {
        return dao.findAllItemsHistoryByPriceTableHistories(histories);
    }

    @Override
    public Map<Long, ItemPriceTableHistory> findMapItemsHistoryByPriceTableHistoriesOrdered(List<Long> histories) {
        return getMapByList(getItemsOrderByHistory(this.findAllItemsHistoryByPriceTableHistories(histories), Boolean.FALSE));
    }

    @Override
    public Map<Long, ItemPriceTableHistory> findMapItemsByPriceTableHistories(List<PriceTableHistory> histories, Boolean isApproved) {

        List<ItemPriceTableHistory> items = new ArrayList<ItemPriceTableHistory>();
        if (histories != null && !histories.isEmpty()) {
            List<Long> ids = new ArrayList<Long>();
            for (PriceTableHistory history : histories)
                ids.add(history.getId());

            if (!ids.isEmpty()) {

                List<ItemPriceTableHistory> itemsHistories = findAllItemsHistoryByPriceTableHistories(ids);
                if (itemsHistories != null && !itemsHistories.isEmpty()) {
                    for (ItemPriceTableHistory itemHistory : itemsHistories) {
                        if (isApproved && itemHistory.getUpdatedIn() != null)
                            continue;

                        items.add(itemHistory);
                    }
                }
            }
        }

        return getMapByList(items);
    }

    @Override
    public void createItemActionCreated(ItemTabelaPreco item, Long priceTableHistory, boolean isApproved) {

        ItemPriceTableHistory history = prepareItemHistory(item, priceTableHistory, isApproved);
        history.setInitialValue(BigDecimal.ZERO);
        history.setInitialPercent(BigDecimal.ZERO);
        history.setUpdatedValue(BigDecimal.ZERO);
        history.setUpdatedPercent(BigDecimal.ZERO);

        if (isApproved) {
            history.setUpdatedValue(item.getValorItemTbPreco());
            history.setUpdatedPercent(item.getPercentualDespesa());
        }

        history.setIndicadorActionStatus(INDICADOR_ACTION_STATUS_CREATED);
        dao.create(history);
    }

    @Override
    public void createItemActionDeleted(ItemTabelaPreco item, Long priceTableHistory, boolean isApproved) {

        ItemPriceTableHistory history = prepareItemHistory(item, priceTableHistory, isApproved);
        history.setInitialValue(item.getValorItemTbPreco());
        history.setInitialPercent(item.getPercentualDespesa());
        history.setUpdatedValue(item.getValorItemTbPreco());
        history.setUpdatedPercent(item.getPercentualDespesa());
        history.setIndicadorActionStatus(INDICADOR_ACTION_STATUS_DELETED);

        dao.create(history);
    }

    @Override
    public Map<Long, ItemPriceTableHistory> findMapAllItemsHistoryByPriceTableId(Long priceTableId) {
        return getMapByList(getItemsOrderByHistory(this.findAllItemsHistoryByPriceTableId(priceTableId), Boolean.TRUE));
    }

    @Override
    public Map<Long, ItemPriceTableHistory> findMapItemsHistoryByPriceTableId(Long priceTableId) {
        return getMapByList(getItemsOrderByHistory(this.findItemsHistoryByPriceTableId(priceTableId), Boolean.TRUE));
    }

    /**
     * @param items
     * @return
     */
    private Map<Long, ItemPriceTableHistory> getMapByList(List<ItemPriceTableHistory> items) {
        Map<Long, ItemPriceTableHistory> map = new HashMap<Long, ItemPriceTableHistory>();

        if (items != null && !items.isEmpty()) {
            for (ItemPriceTableHistory item : items)
                map.put(item.getIdSaleProfile(), item);
        }

        return map;
    }

    /**
     * @param item
     * @param priceTableHistory
     * @param isApproved
     * @return
     */
    private ItemPriceTableHistory prepareItemHistory(ItemTabelaPreco item, Long priceTableHistory, boolean isApproved) {
        ItemPriceTableHistory history = new ItemPriceTableHistory();
        history.setIdPriceTable(item.getTabelaPreco().getCodigoTabelaPreco());
        history.setIdSaleProfile(item.getPerfilVendido().getCodigoPerfilVendido());
        history.setIdPriceTableHistory(priceTableHistory);
        history.setIndicadorApproved(item.getIndicadorApproved());

        if (!isApproved)
            history.setUpdatedIn(new Date());

        return history;
    }

    /**
     * @param itemsHistory
     * @param isReverse
     * @return
     */
    private List<ItemPriceTableHistory> getItemsOrderByHistory(List<ItemPriceTableHistory> itemsHistory, boolean isReverse) {

        if (itemsHistory != null && !itemsHistory.isEmpty()) {

            Comparator<ItemPriceTableHistory> comparator = comparator();
            if(isReverse)
                comparator = Collections.reverseOrder(comparator);

            Collections.sort(itemsHistory, comparator);
        }
        return itemsHistory;
    }

    /**
     * @return
     */
    private Comparator<ItemPriceTableHistory> comparator(){
        return new Comparator<ItemPriceTableHistory>() {
            @Override
            public int compare(ItemPriceTableHistory o1, ItemPriceTableHistory o2) {
                return o1.getIdPriceTableHistory().compareTo(o2.getIdPriceTableHistory());
            }
        };
    }
}