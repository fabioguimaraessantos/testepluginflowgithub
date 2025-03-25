package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("readyForApprovalFlow")
public class ReadyForApprovalFlow extends PriceTableFlow {

    @Override
    protected PriceTableStatus status() {
        return util.getStatus(STATUS_READY_FOR_APRROVAL);
    }

    @Override
    protected String description() {
        return Constants.PRICE_TABLE_STATUS_READY_FOR_APPROVAL;
    }

    protected List<String> findAcronyms(PriceTableMemberType member) {
        List<String> acronyms = new ArrayList<String>();
        acronyms.add("REAP");
        if (member == PriceTableMemberType.APPROVER) {
            acronyms.add("ONAP");
            acronyms.add("APP");
            acronyms.add("NAPP");
        }
        return acronyms;
    }

    @Override
    protected void createItemHistory(PriceTableHistory history, PriceTablePojo pojo, Map<Long, ItemTabelaPreco> mapItens) {

        if (STATUS_DRAFT.equals(pojo.getTabelaPrecoBean().getTo().getPriceTableStatus().getCode()) &&
                pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow() != null) {

            if (!isFirstReadyForApprovalHistorico(historyService.findHistoryByPriceTable(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco()))) {
                List<ItemPriceTableHistory> listItemsHistory = new ArrayList<ItemPriceTableHistory>();
                boolean isDraft = pojo.getTabelaPrecoBean().getTo().getPriceTableStatus().getCode().equals(STATUS_DRAFT);

                for (ItemTabelaPrecoRow row : pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow()) {
                    ItemTabelaPreco item = mapItens.get(row.getItemTabelaPreco().getPerfilVendido().getCodigoPerfilVendido());

                    if (util.isItemChanged(item, row)){
                        ItemPriceTableHistory itemHistory = util.createItem(item, row, history.getId(), isDraft);
                        itemHistory.setUpdatedValue(util.getItemValueChanged(item.getValorItemTbPreco(), row.getItemTabelaPreco().getValorItemTbPreco(), row.getFte()));
                        listItemsHistory.add(itemHistory);
                    }
                }

                itemHistoryService.saveAll(listItemsHistory);
            }
        }
    }

    @Override
    public String outcome(boolean isApprover) {
        return isApprover ? Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW : Constants.OUTCOME_ITEM_PRICE_TABLE_VIEW_DISABLED;
    }

    @Override
    public void validateBeforeStatusChange(PriceTableStatus newStatus, TabelaPreco priceTable, PriceTablePojo pojo) throws PriceTableException {
        super.validateBeforeStatusChange(newStatus, priceTable, pojo);
        if(!isBeforeDateAudit(priceTable.getDataInicioVigencia()) && pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow() != null && !pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow().isEmpty()){
            for (ItemTabelaPrecoRow row : pojo.getItemTabelaPrecoBean().getListaItemTabelaPrecoRow()) {
                if( ((row.getItemTabelaPreco().getValorItemTbPreco() == null || row.getItemTabelaPreco().getValorItemTbPreco().equals(BigDecimal.ZERO)) &&
                        row.getFte() == 0f )){

                    throw new PriceTableException("One or more items that do not have their rates filled", Constants.MSG_ERROR_PRICE_TABLE_RATES_NOT_FILLED);
                }
            }
        }
    }

    /**
     * @param histories - List of Histories from Price Table
     * @return True - If is First Activity Ready For Approval
     */
    private boolean isFirstReadyForApprovalHistorico(List<PriceTableHistory> histories) {
        return histories != null && histories.size() <= 2;
    }

    /**
     * @return Boolean - True if Price Table Date Start Before Audit Date.
     */
    private boolean isBeforeDateAudit(Date priceTableDateStart){
        try {
            Date dateAudit = new SimpleDateFormat("yyyy-MM-dd").parse(Constants.PRICE_TABLE_DATE_AUDIT);
            return priceTableDateStart.before(dateAudit);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}