package com.ciandt.pms.control.jsf.interfaces.priceTable;

import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.PriceTableHistory;

public interface IPriceTableStatus {

    void read(PriceTablePojo pojo);

    void write(PriceTablePojo pojo);

    Boolean removeItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history);

    Boolean createItemTabelaPreco(ItemTabelaPreco item, PriceTableHistory history);
}