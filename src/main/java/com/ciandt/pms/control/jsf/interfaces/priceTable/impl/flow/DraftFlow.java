package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPerfilVendidoService;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("draftFlow")
public class DraftFlow extends PriceTableFlow {

    @Autowired
    private IPerfilVendidoService perfilVendidoService;

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_DRAFT);
    }

    @Override
    protected String description() {
        return Constants.PRICE_TABLE_STATUS_DRAFT;
    }

    @Override
    protected List<String> findAcronyms(PriceTableMemberType member) {
        List<String> acronyms = new ArrayList<String>();
        acronyms.add("D");
        if (member == PriceTableMemberType.EDITOR) {
            acronyms.add("REAP");
        }
        return acronyms;
    }

    @Override
    public void prepareUpdate(PriceTablePojo pojo) {

        super.prepareUpdate(pojo);
        if (pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow().isEmpty()) {

            List<PerfilVendido> listaPerfilVendido = perfilVendidoService.findPerfilVendidoByMsaAndMoedaAndActive(pojo.getMsa(), pojo.getTabelaPrecoBean().getTo().getMoeda());
            List<ItemTabelaPrecoRow> rows = new ArrayList<ItemTabelaPrecoRow>();

            // Percore a lista e cria os itens (usabilidade)
            for (PerfilVendido perfilVendido : listaPerfilVendido) {
                ItemTabelaPrecoRow itemTabelaPrecoRow = new ItemTabelaPrecoRow();
                ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();

                itemTabelaPreco.setPerfilVendido(perfilVendido);
                itemTabelaPreco.setTabelaPreco(pojo.getTabelaPrecoBean().getTo());
                itemTabelaPreco.setValorItemTbPreco(new BigDecimal(0));
                itemTabelaPreco.setValorDespesa(BigDecimal.valueOf(0));
                itemPriceTableService.createItemTabelaPreco(itemTabelaPreco);

                itemTabelaPrecoRow.setItemTabelaPreco(itemTabelaPreco);
                itemTabelaPrecoRow.setFte(0);
                itemTabelaPrecoRow.setApproved(Boolean.FALSE);

                rows.add(itemTabelaPrecoRow);
            }

            pojo.getItemTabelaPrecoBean().setListaItemTabelaPrecoRow(rows);
        }
    }

    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }

    @Override
    protected void createItemHistory(PriceTableHistory history, PriceTablePojo pojo, Map<Long, ItemTabelaPreco> mapItens) {

        if (STATUS_DRAFT.equals(pojo.getTabelaPrecoBean().getTo().getPriceTableStatus().getCode()) &&
                pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow() != null) {

            Map<Long, ItemPriceTableHistory> itemsHistory = itemHistoryService.findMapItemsByPriceTableHistory(history.getId());
            List<ItemPriceTableHistory> listItemsHistory = new ArrayList<ItemPriceTableHistory>();

            boolean isDraft = pojo.getTabelaPrecoBean().getTo().getPriceTableStatus().getCode().equals(STATUS_DRAFT);
            for (ItemTabelaPrecoRow row : pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow()) {

                ItemTabelaPreco item = mapItens.get(row.getItemTabelaPreco().getPerfilVendido().getCodigoPerfilVendido());
                if (util.isItemChanged(item, row)) {
                    ItemPriceTableHistory itemHistory = itemsHistory.get(item.getPerfilVendido().getCodigoPerfilVendido());
                    itemHistory = (itemHistory == null) ? util.createItem(item, row, history.getId(), isDraft) : util.updateItem(itemHistory, row, isDraft);
                    itemHistory.setUpdatedValue(util.getItemValueChanged(item.getValorItemTbPreco(), row.getItemTabelaPreco().getValorItemTbPreco(), row.getFte()));
                    listItemsHistory.add(itemHistory);
                }
            }

            itemHistoryService.saveAll(listItemsHistory);
        }
    }


}
