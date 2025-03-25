package com.ciandt.pms.control.jsf.interfaces.priceTable.util;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IParametroService;
import com.ciandt.pms.business.service.IPriceTableApproverService;
import com.ciandt.pms.business.service.IPriceTableEditorService;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.ItemTabelaPrecoRow;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FlowUtil {

    @Autowired
    private IParametroService parametroService;

    @Autowired
    private IPriceTableApproverService priceTableApproverService;

    @Autowired
    private IPriceTableEditorService priceTableEditorService;

    public static final String INDICADOR_ACTION_STATUS_CREATED = "C";
    public static final String INDICADOR_ACTION_STATUS_DELETED = "D";

    /**
     * @param itens
     * @return
     */
    public Map<Long, ItemTabelaPreco> getMapFromItens(List<ItemTabelaPreco> itens) {
        Map<Long, ItemTabelaPreco> map = new HashMap<Long, ItemTabelaPreco>();
        if (itens != null && !itens.isEmpty())
            for (ItemTabelaPreco item : itens) {
                ItemTabelaPreco obj = new ItemTabelaPreco();
                obj.setCodigoItemTbPreco(item.getCodigoItemTbPreco());
                obj.setTabelaPreco(item.getTabelaPreco());
                obj.setPerfilVendido(item.getPerfilVendido());
                obj.setValorItemTbPreco(item.getValorItemTbPreco());
                obj.setPercentualDespesa(item.getPercentualDespesa());
                obj.setIndicadorApproved(item.getIndicadorApproved());

                map.put(item.getPerfilVendido().getCodigoPerfilVendido(), obj);
            }

        return map;
    }

    /**
     * Creates a Price Table Status Based on Parameter Status Code
     *
     * @param status Price Table Status Code
     * @return PriceTableStatus - Instance of Price Table Status
     */
    public final PriceTableStatus getStatus(Long status) {
        PriceTableStatus ptStatus = new PriceTableStatus();
        ptStatus.setCode(status);
        return ptStatus;
    }

    /**
     * @param item
     * @param idHistory
     * @return
     */
    public final ItemPriceTableHistory createItem(ItemTabelaPreco item, ItemTabelaPrecoRow row, Long idHistory, boolean isDraft) {
        ItemPriceTableHistory itemHistory = new ItemPriceTableHistory();
        itemHistory.setIdPriceTable(item.getTabelaPreco().getCodigoTabelaPreco());
        itemHistory.setIdSaleProfile(item.getPerfilVendido().getCodigoPerfilVendido());
        itemHistory.setIdPriceTableHistory(idHistory);
        itemHistory.setInitialValue(item.getValorItemTbPreco());
        itemHistory.setInitialPercent(item.getPercentualDespesa());

        updateItem(itemHistory, row, isDraft);
        return itemHistory;
    }

    /**
     * @param itemHistory
     * @param row
     * @param isDraft
     * @return
     */
    public final ItemPriceTableHistory updateItem(ItemPriceTableHistory itemHistory, ItemTabelaPrecoRow row, boolean isDraft) {

        itemHistory.setUpdatedValue(new BigDecimal(row.getFte() / 168));
        itemHistory.setUpdatedPercent(row.getItemTabelaPreco().getPercentualDespesa());
        itemHistory.setIndicadorApproved(row.getItemTabelaPreco().getIndicadorApproved());

        if (isDraft)
            itemHistory.setUpdatedIn(new Date());

        return itemHistory;
    }

    /**
     * @param item
     * @param idHistory
     * @return
     */
    public final ItemPriceTableHistory createItemHistory(ItemPriceTableHistory item, ItemTabelaPrecoRow row, Long idHistory) {
        ItemPriceTableHistory itemHistory = new ItemPriceTableHistory();
        itemHistory.setIdPriceTable(item.getIdPriceTable());
        itemHistory.setIdSaleProfile(item.getIdSaleProfile());
        itemHistory.setIdPriceTableHistory(idHistory);
        itemHistory.setInitialValue(item.getUpdatedValue());
        itemHistory.setInitialPercent(item.getUpdatedPercent());
        itemHistory.setIndicadorActionStatus(item.getIndicadorActionStatus());

        updateItem(itemHistory, row, Boolean.FALSE);
        return itemHistory;
    }

    /**
     * @param item
     * @param row
     * @return
     */
    public final boolean isItemChanged(ItemTabelaPreco item, ItemTabelaPrecoRow row) {

        if (item == null || row == null)
            return Boolean.FALSE;

        if (isValueChanged(row.getItemTabelaPreco().getValorItemTbPreco(), item.getValorItemTbPreco(), row.getFte()))
            return Boolean.TRUE;

        if (item.getPercentualDespesa() != null && !item.getPercentualDespesa().equals(row.getItemTabelaPreco().getPercentualDespesa()))
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    public final boolean isItemHistoryChanged(ItemPriceTableHistory itemHistory, ItemTabelaPrecoRow row) {

        if (itemHistory == null || row == null)
            return Boolean.FALSE;

        if (isValueChanged(row.getItemTabelaPreco().getValorItemTbPreco(), itemHistory.getUpdatedValue(), row.getFte()))
            return Boolean.TRUE;

        if (itemHistory.getUpdatedPercent() != null && !itemHistory.getUpdatedPercent().equals(row.getItemTabelaPreco().getPercentualDespesa()))
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    public String getCpsEmail() {
        Parametro parametro = parametroService.findParametroByNomeParametro(Constants.CPS_EMAILS_PARAMETER);

        return parametro != null ? parametro.getTextoParametro() : "";
    }

    public String getEmailSubject(String previousStatusDescription, String currentStatusDescription, TabelaPreco priceTable) {
        return BundleUtil.getBundle(
                Constants.EMAIL_SUBJECT_PRICE_TABLE_STATUS,
                priceTable.getMsa().getNomeMsa(),
                priceTable.getDescricaoTabelaPreco(),
                previousStatusDescription,
                currentStatusDescription
        );
    }

    public String getEmailMessage(String previousStatusDescription, String currentStatusDescription, TabelaPreco priceTable, PriceTablePojo pojo) {
        return BundleUtil.getBundle(
                Constants.EMAIL_MSG_PRICE_TABLE_STATUS,
                priceTable.getDescricaoTabelaPreco(),
                priceTable.getMsa().getNomeMsa(),
                previousStatusDescription,
                currentStatusDescription,
                pojo.getLogin()
        );
    }

    public String getEmailsToNotify(TabelaPreco priceTable) {
        List<String> approversEmailList = priceTableApproverService.getApproversEmailByMsaCode(priceTable.getMsa().getCodigoMsa());
        String emails = StringUtils.join(approversEmailList, ", ");
        if (!emails.isEmpty()) {
            emails = emails + ", ";
        }
        List<String> editorsEmail = priceTableEditorService.getEditorsEmailByMsaCode(priceTable.getMsa().getCodigoMsa());
        emails = emails + StringUtils.join(editorsEmail, ", ");

        return emails;
    }

    /**
     * privates
     */
    private boolean isValueChanged(BigDecimal changedValue, BigDecimal actualValue, float fte) {

        if (actualValue.setScale(2, RoundingMode.HALF_UP).equals(changedValue.setScale(2, RoundingMode.HALF_UP))) {
            BigDecimal valor = new BigDecimal(fte / 168);
            return !valor.setScale(2, RoundingMode.HALF_UP).equals(actualValue.setScale(2, RoundingMode.HALF_UP));
        }

        return !actualValue.equals(changedValue);
    }

    /**
     * @param priceTable
     * @param status
     * @return
     */
    public final PriceTableHistory prepareHistory(Long priceTable, String status) {
        PriceTableHistory history = new PriceTableHistory();
        history.setIdPriceTable(priceTable);
        history.setStatus(status);

        return history;
    }

    /**
     * @param history
     * @return
     */
    public ItemTabelaPreco createItemFromHistory(ItemPriceTableHistory history) {
        ItemTabelaPreco item = new ItemTabelaPreco();

        TabelaPreco tabelaPreco = new TabelaPreco();
        tabelaPreco.setCodigoTabelaPreco(history.getIdPriceTable());
        item.setTabelaPreco(tabelaPreco);

        PerfilVendido perfilVendido = new PerfilVendido();
        perfilVendido.setCodigoPerfilVendido(history.getIdSaleProfile());
        item.setPerfilVendido(perfilVendido);

        item.setValorItemTbPreco(history.getInitialValue());
        item.setPercentualDespesa(history.getInitialPercent());
        item.setIndicadorApproved(history.getIndicadorApproved());

        return item;
    }

    /**
     *
     * @param history
     * @param perfil
     * @return
     */
    public ItemTabelaPreco createItemFromHistoryAndPerfil(ItemPriceTableHistory history, PerfilVendido perfil) {
        ItemTabelaPreco item = createItemFromHistory(history);
        item.setPerfilVendido(perfil);

        return item;
    }

    /**
     *
     * @param item
     * @return
     */
    public ItemTabelaPrecoRow createRowByItem(ItemTabelaPreco item){
        ItemTabelaPrecoRow row = new ItemTabelaPrecoRow();
        row.setItemTabelaPreco(item);

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
        return row;
    }

    /**
     * @param actual Value in system
     * @param update Value from user
     * @param fte    - Fte if necessary
     * @return - BigDecimal - Value changed
     */
    public BigDecimal getItemValueChanged(BigDecimal actual, BigDecimal update, float fte) {

        if (actual.setScale(2, RoundingMode.HALF_UP).equals(update.setScale(2, RoundingMode.HALF_UP)))
            return new BigDecimal(fte / 168);

        return update;
    }
}