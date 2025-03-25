package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.control.jsf.interfaces.priceTable.util.FlowUtil;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("approvedFlow")
public class ApprovedFlow extends PriceTableFlow {

    @Autowired
    private IModuloService moduloService;

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_APPROVED);
    }

    @Override
    protected String description() {
        return Constants.PRICE_TABLE_STATUS_APPROVED;
    }

    @Override
    public void update(PriceTablePojo pojo) throws PriceTableException {
        super.update(pojo);
    }

    @Override
    protected String reason() {
        return "";
    }

    protected List<String> findAcronyms(PriceTableMemberType member) {
        List<String> acronyms = new ArrayList<String>();

        if (member == PriceTableMemberType.EDITOR) {
            acronyms.add("D");
            acronyms.add("REAP");
        } else {
            acronyms.add("APP");
        }

        return acronyms;
    }

    @Override
    protected void createItemHistory(PriceTableHistory history, PriceTablePojo pojo, Map<Long, ItemTabelaPreco> mapItens) {
        if (STATUS_APPROVED.equals(pojo.getTabelaPrecoBean().getTo().getPriceTableStatus().getCode()) &&
                pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow() != null) {

            Map<Long, ItemPriceTableHistory> mapItemsToApprove = itemHistoryService.findMapItemsToApproveByPriceTableId(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco());
            if (mapItemsToApprove != null && !mapItemsToApprove.isEmpty()) {
                List<ItemTabelaPreco> items = itemPriceTableService.findItemTabelaPrecoByTabelaPreco(pojo.getTabelaPrecoBean().getTo());
                List<ItemPriceTableHistory> histories = getListFromMap(mapItemsToApprove);

                if (items != null && !items.isEmpty()) {
                    List<ItemTabelaPreco> listToUpdate = new ArrayList<ItemTabelaPreco>();
                    List<ItemTabelaPreco> listToRemove = new ArrayList<ItemTabelaPreco>();

                    for (ItemTabelaPreco item : items) {
                        ItemPriceTableHistory itemHistory = mapItemsToApprove.get(item.getPerfilVendido().getCodigoPerfilVendido());
                        if (itemHistory != null) {

                            if (FlowUtil.INDICADOR_ACTION_STATUS_DELETED.equals(itemHistory.getIndicadorActionStatus())) {
                                listToRemove.add(item);
                                continue;
                            }

                            prepareUpdateItem(item, itemHistory);
                            listToUpdate.add(item);
                        }
                    }

                    removeItems(listToRemove);
                    createItems(getListToCreate(histories));
                    itemPriceTableService.updateListItemTabelaPreco(listToUpdate);
                }

                updateDateInListItemHistory(histories);
            }
        }
    }


    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }

    /**
     * @param map Map of Item Price Table History
     * @return List - ItemPriceTableHistory - Returns List From MAP
     */
    private List<ItemPriceTableHistory> getListFromMap(Map<Long, ItemPriceTableHistory> map) {
        List<ItemPriceTableHistory> histories = new ArrayList<ItemPriceTableHistory>();
        for (Long key : map.keySet())
            histories.add(map.get(key));

        return histories;
    }

    /**
     * @param items Items to remove
     */
    private void removeItems(List<ItemTabelaPreco> items) {
        try {
            for (ItemTabelaPreco item : items)
                itemPriceTableService.removeItemTabelaPreco(item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param items Items to create
     */
    private void createItems(List<ItemTabelaPreco> items) {
        try {
            for (ItemTabelaPreco item : items)
                itemPriceTableService.createItemTabelaPreco(item);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param histories histories to filter Create items
     */
    private List<ItemTabelaPreco> getListToCreate(List<ItemPriceTableHistory> histories) {
        List<ItemTabelaPreco> items = new ArrayList<ItemTabelaPreco>();

        for (ItemPriceTableHistory history : histories)
            if (FlowUtil.INDICADOR_ACTION_STATUS_CREATED.equals(history.getIndicadorActionStatus()))
                items.add(getItemFromHistory(history));

        return items;
    }

    /**
     * @param history History to Convert Item
     * @return Item Created By param history
     */
    private ItemTabelaPreco getItemFromHistory(ItemPriceTableHistory history) {
        ItemTabelaPreco item = new ItemTabelaPreco();

        TabelaPreco tabelaPreco = new TabelaPreco();
        tabelaPreco.setCodigoTabelaPreco(history.getIdPriceTable());
        tabelaPreco.setRecalculaReceita(Boolean.TRUE);
        item.setTabelaPreco(tabelaPreco);

        PerfilVendido perfilVendido = new PerfilVendido();
        perfilVendido.setCodigoPerfilVendido(history.getIdSaleProfile());
        item.setPerfilVendido(perfilVendido);

        prepareUpdateItem(item, history);
        return item;
    }

    /**
     * @param item    - Item to Update history values
     * @param history - History values
     */
    private void prepareUpdateItem(ItemTabelaPreco item, ItemPriceTableHistory history) {
        item.setValorItemTbPreco(history.getUpdatedValue());
        item.setPercentualDespesa(history.getUpdatedPercent());
        item.setIndicadorApproved(Constants.YES);
    }

    @Override
    public void validateBeforeStatusChange(PriceTableStatus newStatus, TabelaPreco priceTable, PriceTablePojo pojo)
            throws PriceTableException {
        super.validateBeforeStatusChange(newStatus, priceTable, pojo);

        List<ItemTabelaPrecoRow> listaItemTabelaPrecoRowList = pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow();
        for(ItemTabelaPrecoRow  itemTabelaPrecoRow: listaItemTabelaPrecoRowList){
            if(!itemTabelaPrecoRow.getApproved())
                throw new PriceTableException("If you want to approve this price table you need to select the \"Approve\" column of all price table itens.",
                                            Constants.MSG_APPROVE_PRICE_TABLE_SELECT_ALL_APPROVE_COLUMN);
        }

        if (priceTable.getPriceTableStatus().getCode().equals(STATUS_DRAFT) &&
                !moduloService.dateAfterHistoryLock(priceTable.getDataInicioVigencia())) {
            throw new PriceTableException("Cannot approve this price table because start is not after History Lock.",
                    Constants.MSG_APPROVE_PRICE_TABLE_START_BEFORE_CLOSING);
        }
    }
}