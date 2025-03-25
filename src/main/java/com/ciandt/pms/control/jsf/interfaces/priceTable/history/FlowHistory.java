package com.ciandt.pms.control.jsf.interfaces.priceTable.history;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IItemPriceTableHistoryService;
import com.ciandt.pms.business.service.IItemTabelaPrecoService;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.business.service.IPriceTableHistoryService;
import com.ciandt.pms.control.jsf.interfaces.priceTable.util.FlowUtil;
import com.ciandt.pms.model.ItemPriceTableHistory;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class FlowHistory {

    @Autowired
    private FlowUtil util;

    @Autowired
    protected IItemTabelaPrecoService itemPriceTableService;

    @Autowired
    protected IPriceTableHistoryService historyService;

    @Autowired
    protected IItemPriceTableHistoryService itemHistoryService;

    @Autowired
    protected IPerfilVendidoService perfilService;

    /**
     * @param idHistory
     * @param priceTable
     * @return
     */
    public List<ItemTabelaPrecoRow> findHistoryPriceTableById(Long idHistory, TabelaPreco priceTable) {

        /* ITEMS */
        List<ItemTabelaPreco> items = getItems(priceTable);
        if (items != null && !items.isEmpty()) {

            /* HISTORY */
            List<Long> historico = historyService.findHistoryIdsFromPriceTable(priceTable.getCodigoTabelaPreco(), idHistory);
            if (historico != null) {

                /* V0 */
                items = getItemsV0(items, priceTable);

                /* VX */
                Map<Long, ItemPriceTableHistory> itemsHistory = itemHistoryService.findMapItemsHistoryByPriceTableHistoriesOrdered(historico);

                /* RESULT */
                return getHistory(items, itemsHistory, idHistory, historico);
            }
        }

        return Collections.emptyList();
    }

    /**
     * @param priceTable - Price Table to search histories
     * @return List - PriceTableHistory - Histories from Price Table, except the last one.
     */
    public List<PriceTableHistory> findHistoryPriceTable(TabelaPreco priceTable) {
        List<PriceTableHistory> histories = historyService.findHistoryByPriceTable(priceTable.getCodigoTabelaPreco());
        return histories;
    }

    /**
     * @param items
     * @param itemsHistory
     * @param idHistory
     * @param historico
     * @return
     */
    private List<ItemTabelaPrecoRow> getHistory(List<ItemTabelaPreco> items, Map<Long, ItemPriceTableHistory> itemsHistory, Long idHistory, List<Long> historico) {
        Map<Long, ItemTabelaPrecoRow> result = new HashMap<>();

        if (items != null && !items.isEmpty()) {
            for (ItemTabelaPreco item : items) {

                if (itemsHistory.containsKey(item.getPerfilVendido().getCodigoPerfilVendido())) {

                    Boolean removed = FlowUtil.INDICADOR_ACTION_STATUS_DELETED.equals(itemsHistory.get(item.getPerfilVendido().getCodigoPerfilVendido()).getIndicadorActionStatus());
                    if(removed && !showRemovedItem(idHistory, itemsHistory.get(item.getPerfilVendido().getCodigoPerfilVendido()), historico))
                        continue;

                    setValues(item, itemsHistory.get(item.getPerfilVendido().getCodigoPerfilVendido()), Boolean.TRUE);
                    result.put(item.getPerfilVendido().getCodigoPerfilVendido(), createRow(item, removed));
                    continue;
                }

                result.put(item.getPerfilVendido().getCodigoPerfilVendido(), createRow(item, Boolean.FALSE));
            }

            for (Long key : itemsHistory.keySet()) {
                if (result.containsKey(key))
                    continue;

                if(FlowUtil.INDICADOR_ACTION_STATUS_DELETED.equals(itemsHistory.get(key).getIndicadorActionStatus())){
                    if(isActivityBefore(idHistory, itemsHistory.get(key), historico))
                        result.put(key, createItemRowFromHistory(itemsHistory.get(key), key, Boolean.TRUE));

                    continue;
                }

                if(showCreatedItem(idHistory, itemsHistory.get(key), historico)){
                    result.put(key, createItemRowFromHistory(itemsHistory.get(key), key, Boolean.FALSE));
                }
            }
        }

        return new ArrayList<ItemTabelaPrecoRow>(result.values());
    }

    /**
     * Returns initial state of items by Price Table History
     *
     * @param items      List with actual Items
     * @param priceTable Price Table Entity to Search History
     * @return Lista dos items com os valores iniciais
     */
    public List<ItemTabelaPreco> getItemsV0(List<ItemTabelaPreco> items, TabelaPreco priceTable) {

        Map<Long, ItemPriceTableHistory> itemsHistory = itemHistoryService.findMapItemsHistoryByPriceTableId(priceTable.getCodigoTabelaPreco());
        if (itemsHistory != null && !itemsHistory.isEmpty()) {
            prepareItemsV0(items, itemsHistory);
            for (ItemTabelaPreco item : items) {
                if (itemsHistory.containsKey(item.getPerfilVendido().getCodigoPerfilVendido())) {
                    setValues(item, itemsHistory.get(item.getPerfilVendido().getCodigoPerfilVendido()), Boolean.FALSE);
                }
            }
        }

        return items;
    }

    /**
     * @param items
     * @param itemsHistory
     */
    private void prepareItemsV0(List<ItemTabelaPreco> items, Map<Long, ItemPriceTableHistory> itemsHistory) {
        for (Long key : itemsHistory.keySet()) {

            if (FlowUtil.INDICADOR_ACTION_STATUS_DELETED.equals(itemsHistory.get(key).getIndicadorActionStatus())) {
                items.add(util.createItemFromHistoryAndPerfil(itemsHistory.get(key), perfilService.findPerfilVendidoById(key)));
                continue;
            }

            ItemTabelaPreco item = findItemBySaleProfile(items, key);
            if (FlowUtil.INDICADOR_ACTION_STATUS_CREATED.equals(itemsHistory.get(key).getIndicadorActionStatus())) {
                if (item != null) items.remove(item);
                continue;
            }

            if(item == null)
                items.add(util.createItemFromHistoryAndPerfil(itemsHistory.get(key), perfilService.findPerfilVendidoById(key)));
        }
    }

    /**
     * @param items
     * @param saleProfile
     * @return
     */
    private ItemTabelaPreco findItemBySaleProfile(List<ItemTabelaPreco> items, Long saleProfile) {

        for (ItemTabelaPreco item : items) {
            if (item.getPerfilVendido().getCodigoPerfilVendido().equals(saleProfile))
                return item;
        }

        return null;
    }

    /**
     * @param item Item to create new Row
     * @param isRemoved Flag that indicate if row must be Deleted or Default.
     * @return ItemTabelaPrecoRow  - New History Row Created
     */
    private ItemTabelaPrecoRow createRow(ItemTabelaPreco item, Boolean isRemoved) {
        ItemTabelaPrecoRow row = util.createRowByItem(item);
        row.setRemoved(isRemoved);
        return row;
    }

    /**
     * @param idHistory
     * @param itemHistory
     * @param historicos
     * @return
     */
    private boolean showRemovedItem(Long idHistory, ItemPriceTableHistory itemHistory, List<Long> historicos){

        if(idHistory <= itemHistory.getIdPriceTableHistory())
            return Boolean.TRUE;

        return isActivityBefore(idHistory, itemHistory, historicos);
    }

    /**
     * @param idHistory
     * @param itemHistory
     * @param historicos
     * @return
     */
    private boolean showCreatedItem(Long idHistory, ItemPriceTableHistory itemHistory, List<Long> historicos){

        if(FlowUtil.INDICADOR_ACTION_STATUS_CREATED.equals(itemHistory.getIndicadorActionStatus()))
            return Boolean.TRUE;

        if(idHistory >= itemHistory.getIdPriceTableHistory())
            return Boolean.TRUE;

        return isActivityBefore(idHistory, itemHistory, historicos);
    }

    /**
     * @param idHistory History ID to verify if action was in before activity.
     * @param itemHistory History that contains action from Activity.
     * @param historicos List of history IDs to locate idHistory.
     * @return
     */
    private boolean isActivityBefore(Long idHistory, ItemPriceTableHistory itemHistory, List<Long> historicos){

        for (int i=0; i < historicos.size(); i++)
            if(i != historicos.size() - 1 && historicos.get(i).equals(idHistory))
                return itemHistory.getIdPriceTableHistory().equals(historicos.get(i+1));

        return false;
    }

    /**
     * @param item Item to set new values
     * @param itemHistory Item History to get values
     * @param isUpdate Indicates if values are Initials or Updateds
     */
    private void setValues(ItemTabelaPreco item, ItemPriceTableHistory itemHistory, Boolean isUpdate){

        if(isUpdate){
            item.setValorItemTbPreco(itemHistory.getUpdatedValue());
            item.setPercentualDespesa(itemHistory.getUpdatedPercent());
        }else{
            item.setValorItemTbPreco(itemHistory.getInitialValue());
            item.setPercentualDespesa(itemHistory.getInitialPercent());
        }

        item.setIndicadorApproved(itemHistory.getIndicadorApproved());
    }

    /**
     * @param itemHistory Item History to convert in Item
     * @param perfil - Profile Code to Search Sale Profile
     * @param removed - Flag that indicate if row must be Deleted or Default.
     * @return ItemTabelaPrecoRow - New History Row Created
     */
    private ItemTabelaPrecoRow createItemRowFromHistory(ItemPriceTableHistory itemHistory, Long perfil, Boolean removed){
        ItemTabelaPreco item = util.createItemFromHistoryAndPerfil(itemHistory, perfilService.findPerfilVendidoById(perfil));
        setValues(item, itemHistory, Boolean.TRUE);
        return createRow(item, removed);
    }

    /**
     * @param priceTable - Price table to Search Items
     * @return List - ItemTabelaPreco - List of Price Tables Items with Evict
     */
    private List<ItemTabelaPreco> getItems(TabelaPreco priceTable){
        List<ItemTabelaPreco> result = new ArrayList<>();

        List<ItemTabelaPreco> items = itemPriceTableService.findItemTabelaPrecoByTabelaPreco(priceTable);
        if(items != null && !items.isEmpty()){
            for (ItemTabelaPreco item : items) {
                ItemTabelaPreco obj = new ItemTabelaPreco();
                obj.setCodigoItemTbPreco(item.getCodigoItemTbPreco());
                obj.setTabelaPreco(item.getTabelaPreco());
                obj.setPerfilVendido(item.getPerfilVendido());
                obj.setValorItemTbPreco(item.getValorItemTbPreco());
                obj.setPercentualDespesa(item.getPercentualDespesa() != null ?
                        item.getPercentualDespesa() : BigDecimal.ZERO);
                obj.setIndicadorApproved(item.getIndicadorApproved());
                result.add(obj);
            }
        }

        return result;
    }
}