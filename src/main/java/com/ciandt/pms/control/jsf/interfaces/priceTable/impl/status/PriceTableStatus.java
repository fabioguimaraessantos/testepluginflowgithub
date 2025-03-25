package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.status;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IItemPriceTableHistoryService;
import com.ciandt.pms.business.service.IItemTabelaPrecoService;
import com.ciandt.pms.business.service.IPriceTableHistoryService;
import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableStatus;
import com.ciandt.pms.control.jsf.interfaces.priceTable.util.FlowUtil;
import com.ciandt.pms.model.ItemPriceTableHistory;
import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class PriceTableStatus implements IPriceTableStatus {

    @Autowired
    protected IItemTabelaPrecoService itemPriceTableService;

    @Autowired
    protected IItemPriceTableHistoryService itemPriceTableHistoryService;

    @Autowired
    protected IPriceTableHistoryService historyService;

    @Autowired
    protected FlowUtil util;

    private Map<Long, ItemPriceTableHistory> editedItems;

    /**
     * @param priceTable
     * @return
     */
    protected final List<ItemTabelaPrecoRow> loadListItemTabPrecoRow(TabelaPreco priceTable) {

        List<ItemTabelaPreco> lista = getItems(priceTable);
        editedItems = itemPriceTableHistoryService.findMapItemsByPriceTableHistories(historyService.findLastEditedHistories(priceTable.getCodigoTabelaPreco()), isApproved());

        if (lista != null && !lista.isEmpty()) {
            List<ItemTabelaPrecoRow> listaItens = new ArrayList<ItemTabelaPrecoRow>();

            // Monta Vo para view
            for (ItemTabelaPreco item : lista) {
                ItemTabelaPrecoRow row = createRow(item);

                if (row == null)
                    continue;

                // Seta o total de FTEs
                row.setFte((row.getItemTabelaPreco().getValorItemTbPreco().floatValue() * 168));

                // Cria BigDecimal do total de FTE para calcular o valor da despesa
                BigDecimal valorFte = BigDecimal.valueOf(row.getFte());

                // Calcula o valor da despesa (somente para mostrar na tela, nao persistido)
                if (row.getItemTabelaPreco().getPercentualDespesa() != null) {
                    row.getItemTabelaPreco().setValorDespesa(valorFte.multiply(row.getItemTabelaPreco().getPercentualDespesa().divide(BigDecimal.valueOf(100))));
                } else {
                    row.getItemTabelaPreco().setValorDespesa(BigDecimal.valueOf(0));
                }

                row.setApproved(row.getItemTabelaPreco().getIndicadorApproved() != null ? row.getItemTabelaPreco().getIndicadorApproved().equals(Constants.YES) : Boolean.FALSE);
                listaItens.add(row);
            }

            return listaItens;
        }

        return Collections.<ItemTabelaPrecoRow>emptyList();
    }

    /**
     * @param perfil
     * @return
     */
    protected final boolean isItemEdited(Long perfil) {
        return editedItems.containsKey(perfil);
    }

    /**
     * @param item
     * @return
     */
    protected abstract ItemTabelaPrecoRow createRow(ItemTabelaPreco item);

    /**
     * @return
     */
    protected abstract Boolean isApproved();

    /**
     *
     * @param priceTable
     * @return
     */
    protected List<ItemTabelaPreco> getItems(TabelaPreco priceTable){
        return itemPriceTableService.findItemTabelaPrecoByTabelaPreco(priceTable);
    }
}