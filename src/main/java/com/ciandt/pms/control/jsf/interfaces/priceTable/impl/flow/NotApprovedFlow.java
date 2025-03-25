package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("notApprovedFlow")
public class NotApprovedFlow extends PriceTableFlow {

    private String reason;

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_NOT_APPROVED);
    }

    @Override
    protected String description() {
        return Constants.PRICE_TABLE_STATUS_NOT_APPROVED;
    }

    protected List<String> findAcronyms(PriceTableMemberType member) {
        List<String> acronyms = new ArrayList<String>();
        acronyms.add("D");
        acronyms.add("REAP");

        if (member == PriceTableMemberType.APPROVER)
            acronyms.add("NAPP");

        return acronyms;
    }

    @Override
    public void update(PriceTablePojo pojo) throws PriceTableException {
        if (pojo.getTabelaPrecoBean().getNotApproveReasonsDescription() != null)
            this.reason = pojo.getTabelaPrecoBean().getNotApproveReasonsDescription();

        super.update(pojo);
    }

    @Override
    public void validateBeforeStatusChange(PriceTableStatus newStatus, TabelaPreco priceTable, PriceTablePojo pojo)
            throws PriceTableException {
        super.validateBeforeStatusChange(newStatus, priceTable, pojo);
        boolean isAtLeatstOneChecked = Boolean.FALSE;

        List<ItemTabelaPrecoRow> listaItemTabelaPrecoRowList = pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow();
        for(ItemTabelaPrecoRow  itemTabelaPrecoRow: listaItemTabelaPrecoRowList){
            if(!itemTabelaPrecoRow.getApproved())
                isAtLeatstOneChecked = Boolean.TRUE;
        }

        if(!isAtLeatstOneChecked)
          throw new PriceTableException("If you don't want to approve this price table you need to uncheck at least one option in the \"Approve\" column of a price table item.",
                Constants.MSG_APPROVE_PRICE_TABLE_SELECT_AT_LEAST_ONE_APPROVE_COLUMN);

    }

    @Override
    protected String reason() {
        return reason;
    }

    @Override
    protected void createItemHistory(PriceTableHistory history, PriceTablePojo pojo, Map<Long, ItemTabelaPreco> mapItens) {
        if (STATUS_APPROVED.equals(pojo.getTabelaPrecoBean().getTo().getPriceTableStatus().getCode()) &&
                pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow() != null) {

            Map<Long, ItemPriceTableHistory> mapItemsToApprove = itemHistoryService.findMapItemsToApproveByPriceTableId(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco());
            if (mapItemsToApprove != null && !mapItemsToApprove.isEmpty()) {
                for (ItemTabelaPrecoRow row : pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow()) {
                    ItemPriceTableHistory itemHistory = mapItemsToApprove.get(row.getItemTabelaPreco().getPerfilVendido().getCodigoPerfilVendido());
                    if (itemHistory != null) {
                        itemHistory.setIndicadorApproved(row.getApproved().equals(Boolean.TRUE) ? Constants.YES : Constants.NO);
                        itemHistoryService.merge(itemHistory);
                    }
                }
            }
        }
    }

    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW;
    }
}