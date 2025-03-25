package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status;

import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.stereotype.Service;

@Service("draftStatus")
public class DraftPriceTableStatus extends PriceTableStatus {

    @Override
    public void read(PriceTablePojo pojo) {
        pojo.getItemTabelaPrecoBean().setListaItemTabelaPrecoRow(loadListItemTabPrecoRow(pojo.getTabelaPrecoBean().getTo()));
    }

    @Override
    public void write(PriceTablePojo pojo) {
        itemPriceTableService.updateListItemTabPrecoRow(pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow());
    }

    @Override
    protected ItemTabelaPrecoRow createRow(ItemTabelaPreco item) {
        ItemTabelaPrecoRow row = new ItemTabelaPrecoRow();
        row.setEdited(isItemEdited(item.getPerfilVendido().getCodigoPerfilVendido()));

        row.setItemTabelaPreco(item);
        return row;
    }

    @Override
    public Boolean removeItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history) {

        Boolean removed = itemPriceTableService.removeItemTabelaPreco(item);
        if (removed && history != null)
            itemPriceTableHistoryService.createItemActionDeleted(item, history.getId(), Boolean.FALSE);

        return removed;
    }

    @Override
    public Boolean createItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history) {
        Boolean created = itemPriceTableService.createItemTabelaPreco(item);
        if (created && history != null)
            itemPriceTableHistoryService.createItemActionCreated(item, history.getId(), Boolean.FALSE);

        return created;
    }

    @Override
    protected Boolean isApproved() {
        return Boolean.FALSE;
    }
}