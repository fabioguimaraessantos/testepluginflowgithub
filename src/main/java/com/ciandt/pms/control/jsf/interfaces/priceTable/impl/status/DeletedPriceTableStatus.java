package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status;

import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.stereotype.Service;

@Service("deletedStatus")
public class DeletedPriceTableStatus extends PriceTableStatus{

    @Override
    public void read(PriceTablePojo pojo) {
        pojo.getItemTabelaPrecoBean().setListaItemTabelaPrecoRow(loadListItemTabPrecoRow(pojo.getTabelaPrecoBean().getTo()));
    }

    @Override
    public void write(PriceTablePojo pojo) {

    }

    @Override
    protected ItemTabelaPrecoRow createRow(ItemTabelaPreco item) {
        ItemTabelaPrecoRow row = new ItemTabelaPrecoRow();
        row.setEdited(Boolean.FALSE);

        row.setItemTabelaPreco(item);
        return row;
    }

    @Override
    public Boolean removeItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean createItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history) {
        return Boolean.FALSE;
    }

    @Override
    protected Boolean isApproved() {
        return Boolean.FALSE;
    }
}