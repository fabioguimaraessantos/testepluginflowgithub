package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableFlow;
import com.ciandt.pms.control.jsf.interfaces.priceTable.util.FlowUtil;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.model.ItemPriceTableHistory;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("approvedStatus")
public class ApprovedPriceTableStatus extends PriceTableStatus {

    @Autowired
    private IPerfilVendidoService perfilVendidoService;
    private Map<Long, ItemPriceTableHistory> mapItens;

    @Override
    public void read(PriceTablePojo pojo) {
        mapItens = itemPriceTableHistoryService.findMapItemsToApproveByPriceTableId(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco());
        pojo.getItemTabelaPrecoBean().setListaItemTabelaPrecoRow(loadListItemTabPrecoRow(pojo.getTabelaPrecoBean().getTo()));
    }

    @Override
    public void write(PriceTablePojo pojo) {
        if (pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow() != null &&
                (pojo.getStatusPms().equals(IPriceTableFlow.STATUS_DRAFT) ||
                        pojo.getStatusPms().equals(IPriceTableFlow.STATUS_READY_FOR_APRROVAL))) {

            mapItens = itemPriceTableHistoryService.findMapItemsToApproveByPriceTableId(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco());
            Long idHistory = getHistoryId(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco(), pojo.getTabelaPrecoBean().getTo().getPriceTableStatusFlowPms().getCode(), pojo.getLogin());

            List<ItemPriceTableHistory> listItemsHistory = new ArrayList<ItemPriceTableHistory>();
            Map<Long, ItemTabelaPreco> mapItemsTabelaPreco = itemPriceTableService.findMapItensByTabelaPreco(pojo.getTabelaPrecoBean().getTo());
            for (ItemTabelaPrecoRow row : pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow()) {

                ItemPriceTableHistory itemHistory = mapItens.get(row.getItemTabelaPreco().getPerfilVendido().getCodigoPerfilVendido());
                if (itemHistory != null) {

                    if (util.isItemHistoryChanged(itemHistory, row))
                        listItemsHistory.add(getItemHistoryChanged(itemHistory, row, idHistory, itemHistory.getUpdatedValue()));

                    continue;
                }

                ItemTabelaPreco item = mapItemsTabelaPreco.get(row.getItemTabelaPreco().getPerfilVendido().getCodigoPerfilVendido());
                if (util.isItemChanged(item, row)) {
                    itemHistory = util.createItem(item, row, idHistory, Boolean.FALSE);
                    itemHistory.setUpdatedValue(util.getItemValueChanged(item.getValorItemTbPreco(), row.getItemTabelaPreco().getValorItemTbPreco(), row.getFte()));
                    listItemsHistory.add(itemHistory);
                }
            }

            itemPriceTableHistoryService.saveAll(listItemsHistory);
        }
    }

    @Override
    protected ItemTabelaPrecoRow createRow(ItemTabelaPreco item) {
        ItemTabelaPrecoRow row = new ItemTabelaPrecoRow();
        if (mapItens.containsKey(item.getPerfilVendido().getCodigoPerfilVendido())) {

            if (FlowUtil.INDICADOR_ACTION_STATUS_DELETED.equals(mapItens.get(item.getPerfilVendido().getCodigoPerfilVendido()).getIndicadorActionStatus()))
                return null;

            item.setValorItemTbPreco(mapItens.get(item.getPerfilVendido().getCodigoPerfilVendido()).getUpdatedValue());
            item.setPercentualDespesa(mapItens.get(item.getPerfilVendido().getCodigoPerfilVendido()).getUpdatedPercent());
            item.setIndicadorApproved(mapItens.get(item.getPerfilVendido().getCodigoPerfilVendido()).getIndicadorApproved());
            row.setEdited(isItemEdited(item.getPerfilVendido().getCodigoPerfilVendido()));
        }

        row.setItemTabelaPreco(item);
        return row;
    }

    @Override
    public Boolean removeItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history) {

        if (history != null) {
            ItemTabelaPreco entity = updateValuesFromItem(item);
            closeItemHistoryInActionStatus(item);
            itemPriceTableHistoryService.createItemActionDeleted(entity, history.getId(), Boolean.TRUE);
        }

        return Boolean.TRUE;
    }

    @Override
    public Boolean createItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history) {
        if (history != null) {
            closeItemHistoryInActionStatus(item);
            itemPriceTableHistoryService.createItemActionCreated(item, history.getId(), Boolean.TRUE);
        }

        return Boolean.TRUE;
    }

    @Override
    protected Boolean isApproved() {
        return Boolean.TRUE;
    }

    @Override
    protected List<ItemTabelaPreco> getItems(TabelaPreco priceTable) {

        List<ItemTabelaPreco> items = super.getItems(priceTable);
        items.addAll(getItemsCreated(priceTable));
        return items;
    }

    /**
     * @param priceTable
     * @param status
     * @param login
     * @return
     */
    private Long getHistoryId(Long priceTable, Long status, String login) {

        PriceTableHistory lastHistory = historyService.findLastOneByPriceTable(priceTable);
        if (lastHistory != null)
            if (lastHistory.getStatus().equals(Constants.PRICE_TABLE_STATUS_DRAFT) || lastHistory.getStatus().equals(Constants.PRICE_TABLE_STATUS_READY_FOR_APPROVAL))
                return lastHistory.getId();

        PriceTableHistory history = util.prepareHistory(priceTable, (status == 2l) ? Constants.PRICE_TABLE_STATUS_READY_FOR_APPROVAL : Constants.PRICE_TABLE_STATUS_DRAFT);
        history.setLogin(login);
        history.setUpdatedIn(new Date());
        return historyService.merge(history).getId();
    }

    /**
     * @param history
     * @param row
     * @param idHistory
     * @return
     */
    private ItemPriceTableHistory closeValueAndCreatesANewOne(ItemPriceTableHistory history, ItemTabelaPrecoRow row, Long idHistory) {
        history.setUpdatedIn(new Date());
        itemPriceTableHistoryService.merge(history);
        return util.createItemHistory(history, row, idHistory);
    }

    /**
     * @param itemHistory Item History to update value changes
     * @param row - Row with values from user
     * @param idHistory - History ID responsible for citem changes
     * @param actualValue - Actual value from Item History
     * @return ItemPriceTableHistory - Item Price Table with values changed
     */
    private ItemPriceTableHistory getItemHistoryChanged(ItemPriceTableHistory itemHistory, ItemTabelaPrecoRow row, Long idHistory, BigDecimal actualValue) {
        if (!itemHistory.getIdPriceTableHistory().equals(idHistory))
            return closeValueAndCreatesANewOne(itemHistory, row, idHistory);

        ItemPriceTableHistory item = util.updateItem(itemHistory, row, Boolean.FALSE);
        item.setUpdatedValue(util.getItemValueChanged(actualValue, row.getItemTabelaPreco().getValorItemTbPreco(), row.getFte()));
        return item;
    }

    /**
     * @param item - Close Item's History Date
     */
    private void closeItemHistoryInActionStatus(ItemTabelaPreco item) {
        Map<Long, ItemPriceTableHistory> mapItemsToApprove = itemPriceTableHistoryService.findMapItemsToApproveByPriceTableId(item.getTabelaPreco().getCodigoTabelaPreco());
        if (mapItemsToApprove != null && !mapItemsToApprove.isEmpty()) {
            ItemPriceTableHistory itemHistory = mapItemsToApprove.get(item.getPerfilVendido().getCodigoPerfilVendido());
            if (itemHistory != null) {
                itemHistory.setUpdatedIn(new Date());
                itemPriceTableHistoryService.merge(itemHistory);
            }
        }
    }

    /**
     * @param tabelaPreco - Price Table to Set on new Item
     * @return List - ItemTabelaPreco - List of new items createds
     */
    private List<ItemTabelaPreco> getItemsCreated(TabelaPreco tabelaPreco) {

        List<ItemTabelaPreco> items = new ArrayList<ItemTabelaPreco>();
        if (mapItens != null && !mapItens.isEmpty()) {
            for (Long key : mapItens.keySet()) {
                if (FlowUtil.INDICADOR_ACTION_STATUS_CREATED.equals(mapItens.get(key).getIndicadorActionStatus())) {
                    ItemTabelaPreco item = new ItemTabelaPreco();
                    item.setTabelaPreco(tabelaPreco);
                    item.setPerfilVendido(perfilVendidoService.findPerfilVendidoById(mapItens.get(key).getIdSaleProfile()));
                    items.add(item);
                }
            }
        }

        return items;
    }

    /**
     * @param item - Item to update Values from History
     * @return ItemTabelaPreco new entity created from param item
     */
    private ItemTabelaPreco updateValuesFromItem(ItemTabelaPreco item) {

        ItemTabelaPreco entity = new ItemTabelaPreco();
        entity.setTabelaPreco(item.getTabelaPreco());
        entity.setPerfilVendido(item.getPerfilVendido());
        entity.setValorDespesa(item.getValorDespesa());
        entity.setIndicadorApproved(item.getIndicadorApproved());
        entity.setPercentualDespesa(item.getPercentualDespesa());
        entity.setValorItemTbPreco(item.getValorItemTbPreco());

        Map<Long, ItemPriceTableHistory> mapItemsToApprove = itemPriceTableHistoryService.findMapItemsToApproveByPriceTableId(item.getTabelaPreco().getCodigoTabelaPreco());
        if (mapItemsToApprove != null && !mapItemsToApprove.isEmpty()) {
            ItemPriceTableHistory itemHistory = mapItemsToApprove.get(item.getPerfilVendido().getCodigoPerfilVendido());
            if (itemHistory != null) {
                entity.setPercentualDespesa(itemHistory.getUpdatedPercent());
                entity.setValorItemTbPreco(itemHistory.getUpdatedValue());
                entity.setIndicadorApproved(itemHistory.getIndicadorApproved());
            }
        }

        return entity;
    }
}