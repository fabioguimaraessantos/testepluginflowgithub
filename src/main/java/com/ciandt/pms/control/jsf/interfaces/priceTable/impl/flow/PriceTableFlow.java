package com.ciandt.pms.control.jsf.interfaces.priceTable.impl.flow;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IItemPriceTableHistoryService;
import com.ciandt.pms.business.service.IItemTabelaPrecoService;
import com.ciandt.pms.business.service.IPriceTableHistoryService;
import com.ciandt.pms.business.service.IPriceTableStatusService;
import com.ciandt.pms.business.service.ITabelaPrecoService;
import com.ciandt.pms.control.jsf.interfaces.priceTable.IPriceTableFlow;
import com.ciandt.pms.control.jsf.interfaces.priceTable.handler.PriceTableStatusHandler;
import com.ciandt.pms.control.jsf.interfaces.priceTable.util.FlowUtil;
import com.ciandt.pms.control.jsf.pojo.PriceTablePojo;
import com.ciandt.pms.enums.PriceTableMemberType;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.exception.PriceTableException;
import com.ciandt.pms.model.*;
import com.ciandt.pms.util.MailSenderUtil;

import java.util.*;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PriceTableFlow implements IPriceTableFlow {

    @Autowired
    protected ITabelaPrecoService priceTableService;

    @Autowired
    protected IItemTabelaPrecoService itemPriceTableService;

    @Autowired
    protected PriceTableStatusHandler statusHandler;

    @Autowired
    protected IPriceTableHistoryService historyService;

    @Autowired
    protected IPriceTableStatusService priceTableStatusService;

    @Autowired
    protected IItemPriceTableHistoryService itemHistoryService;

    @Autowired
    protected FlowUtil util;

    @Autowired
    private MailSenderUtil mailSender;

    @Override
    public final boolean create(TabelaPreco priceTable) {

        if (priceTable != null) {
            priceTable.setIndicadorDeleteLogico(Constants.NO);
            priceTable.setPriceTableStatus(util.getStatus(STATUS_DRAFT));
            priceTable.setPriceTableStatusFlowPms(util.getStatus(STATUS_DRAFT));
            return priceTableService.createTabelaPreco(priceTable);
        }

        return false;
    }

    @Override
    public List<PriceTableStatus> priceTableStatusList(PriceTableMemberType member) {

        try {
            return priceTableStatusService.findPriceTableStatus(findAcronyms(member));
        } catch (BusinessException e) {
            //TODO incluir log
            return Collections.emptyList();
        }
    }


    @Override
    public void prepareUpdate(PriceTablePojo pojo) {
        if (pojo.getTabelaPrecoBean().getTo() != null) {
            TabelaPreco tp = priceTableService.findTabelaPrecoById(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco());
            statusHandler.findPriceTableStatus(tp.getPriceTableStatus().getCode()).read(pojo);
        }
    }

    @Override
    public void update(PriceTablePojo pojo) throws PriceTableException {

        if (pojo.getTabelaPrecoBean().getTo() != null) {

            /* Buscar Price table */
            TabelaPreco tp = priceTableService.findTabelaPrecoById(pojo.getTabelaPrecoBean().getTo().getCodigoTabelaPreco());

            /* Externaliza status anterior */
            PriceTableStatus priceTablePreviousStatus = tp.getPriceTableStatusFlowPms();

            /* Valida informa��es do status */
            validateBeforeStatusChange(status(), tp, pojo);

            /* Busca dos itens com valores antes da atualiza��o */
            Map<Long, ItemTabelaPreco> mapItens = util.getMapFromItens(itemPriceTableService.findItemTabelaPrecoByTabelaPreco(tp));

            /* Atualizar Itens */
            pojo.setStatusPms(status().getCode());
            statusHandler.findPriceTableStatus(tp.getPriceTableStatus().getCode()).write(pojo);

            /* Atualizar PRICE TABLE */
            if(STATUS_APPROVED.equals(status().getCode()))
                tp.setRecalculaReceita(Boolean.TRUE);
            tp.setNotApproveReasonsDescription(STATUS_APPROVED.equals(status().getCode()) ? "" : pojo.getTabelaPrecoBean().getTo().getNotApproveReasonsDescription());
            verifyEndDateApprovedPriceTable(tp);

            if (flowHasChanged(tp)) {
                sendEmailToThoseResponsibleForThePriceTable(priceTablePreviousStatus, tp, pojo);
                makeStatusChange(tp);
                createItemHistory(createHistory(tp.getCodigoTabelaPreco(), pojo.getLogin()), pojo, mapItens);
            }

            priceTableService.updateTabelaPreco(tp);
        }
    }

    /**
     * Returns "In Progress" status codes (e.g. not approved yet)
     *
     * @return list of status codes
     */
    @Override
    public List<Long> findPriceTableStatusInProgress() {
        return Arrays.asList(STATUS_DRAFT, STATUS_READY_FOR_APRROVAL, STATUS_ON_APRROVAL);
    }

    /**
     * Create a row on Price Table History Based on Code and Login
     *
     * @param priceTable Price Table Code
     * @param login      Login
     */
    @Override
    public PriceTableHistory createHistory(Long priceTable, String login) {
        PriceTableHistory history = historyService.findLastOneByPriceTable(priceTable);

        if (history == null || !history.getStatus().equals(description())) {
            history = util.prepareHistory(priceTable, description());
            history.setReason(reason());
        }

        history.setUpdatedIn(new Date());
        history.setLogin(login);

        return historyService.merge(history);
    }

    @Override
    public Boolean removeItemTabelaPreco(ItemTabelaPreco item, String login) {

        PriceTableHistory history = null;

        if (shouldCreateHistory(item)) {
            history = createHistory(item.getTabelaPreco().getCodigoTabelaPreco(), login);
        }

        return statusHandler.findPriceTableStatus(item.getTabelaPreco().getPriceTableStatus().getCode()).removeItemTabelaPreco(item, history);
    }

    @Override
    public Boolean createItemTabelaPreco(ItemTabelaPreco item, String login) {

        PriceTableHistory history = null;

        if(shouldCreateHistory(item))
            history = createHistory(item.getTabelaPreco().getCodigoTabelaPreco(), login);

        return statusHandler.findPriceTableStatus(item.getTabelaPreco().getPriceTableStatus().getCode()).createItemTabelaPreco(item, history);
    }

    private Boolean shouldCreateHistory(ItemTabelaPreco item) {
        boolean statusIsDraft = Objects.equals(item.getTabelaPreco().getPriceTableStatus().getCode(), STATUS_DRAFT);

        return !(isFirstDraft(item.getTabelaPreco()) && statusIsDraft);
    }

    /**
     * Updates Status from Price Table
     *
     * @param tp - Tabela Preco
     */
    private void makeStatusChange(TabelaPreco tp) {
        tp.setPriceTableStatusFlowPms(status());

        if (changeStatus(tp))
            tp.setPriceTableStatus(util.getStatus(STATUS_APPROVED));

    }

    /**
     * Verify if has Status Flow Change OR is a action in Status Draft
     *
     * @param tabelaPreco - Price Table
     * @return Boolean - True - If has changed
     */
    private boolean flowHasChanged(TabelaPreco tabelaPreco) {
        return changeStatusFlow(tabelaPreco) || !isFirstDraft(tabelaPreco);
    }

    /**
     * @param tabelaPreco - Price table to verify is first Draft
     * @return True - if first draft
     */
    private boolean isFirstDraft(TabelaPreco tabelaPreco) {

        if(!STATUS_DRAFT.equals(status().getCode()))
            return Boolean.FALSE;

        List<PriceTableHistory> histories = historyService.findHistoryByPriceTable(tabelaPreco.getCodigoTabelaPreco());
        return (histories == null || histories.size() == 1);
    }

    private void sendEmailToThoseResponsibleForThePriceTable(PriceTableStatus priceTablePreviousStatus, TabelaPreco priceTable, PriceTablePojo pojo) {
        if (status().getCode().equals(STATUS_DRAFT)) return;

        if (status().getCode().equals(STATUS_READY_FOR_APRROVAL) && priceTablePreviousStatus.getCode().equals(STATUS_READY_FOR_APRROVAL)) return;

        if (status().getCode().equals(STATUS_ON_APRROVAL) && priceTablePreviousStatus.getCode().equals(STATUS_ON_APRROVAL)) return;

        String subject = util.getEmailSubject(priceTablePreviousStatus.getName(), description(), priceTable);
        String message = util.getEmailMessage(priceTablePreviousStatus.getName(), description(), priceTable, pojo);
        String emails = util.getEmailsToNotify(priceTable);
        String aliasCpsEmail = util.getCpsEmail();

        mailSender.sendHtmlMail(emails, subject, message, aliasCpsEmail);
    }

    /**
     * Verify if has Status Flow Change
     *
     * @param tabelaPreco - Price Table
     * @return Boolean - True - If has status change
     */
    protected final boolean changeStatusFlow(TabelaPreco tabelaPreco) {

        if (tabelaPreco != null && !tabelaPreco.getPriceTableStatusFlowPms().getCode().equals(status().getCode()))
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    /**
     * Verify if has General Status Change
     *
     * @param tabelaPreco - Price Table
     * @return Boolean - True - If has status change
     */
    protected final boolean changeStatus(TabelaPreco tabelaPreco) {

        if (tabelaPreco == null)
            return Boolean.FALSE;

        if (STATUS_APPROVED.equals(tabelaPreco.getPriceTableStatus().getCode()))
            return Boolean.FALSE;

        if (!STATUS_APPROVED.equals(status().getCode()))
            return Boolean.FALSE;

        return Boolean.TRUE;
    }

    /**
     * @param history
     * @param pojo
     * @param mapItens
     */
    protected void createItemHistory(PriceTableHistory history, PriceTablePojo pojo, Map<Long, ItemTabelaPreco> mapItens) {
    }

    /**
     * @param itens
     */
    protected final void updateDateInListItemHistory(List<ItemPriceTableHistory> itens) {

        if(itens != null && !itens.isEmpty()) {
            List<ItemPriceTableHistory> listItemsHistory = new ArrayList<ItemPriceTableHistory>();
            for (ItemPriceTableHistory itemHistory : itens) {
                itemHistory.setUpdatedIn(new Date());
                listItemsHistory.add(itemHistory);
            }

            itemHistoryService.saveAll(listItemsHistory);
        }
    }

    /**
     * Abstracts
     */
    protected abstract PriceTableStatus status();

    protected abstract String description();

    protected abstract List<String> findAcronyms(PriceTableMemberType member);

    /**
     * @return String - Reason of Status Change
     */
    protected String reason() {
        return null;
    }

    /**
     * Run validations before save status changes
     *
     * @param newStatus  new price table flow status to validate
     * @param priceTable price table to verify
     * @throws PriceTableException if any validation not pass
     */
    public void validateBeforeStatusChange(PriceTableStatus newStatus, TabelaPreco priceTable, PriceTablePojo pojo)
            throws PriceTableException {
        if (newStatus == null || priceTable == null) {
            throw new PriceTableException("Received an invalid status to validate.", Constants.GENERIC_ERROR);
        }
    }

    /**
     *
     * @param tabelaPreco
     */
    private void verifyEndDateApprovedPriceTable(TabelaPreco tabelaPreco){

        if(tabelaPreco.getPriceTableStatus().getCode().equals(STATUS_DRAFT) && status().getCode().equals(STATUS_APPROVED)){
            TabelaPreco entity = priceTableService.findMaxStartDatebyMsaAndCurrencyApproved(tabelaPreco.getMsa(), tabelaPreco.getMoeda());
            if(entity != null) {
                if (tabelaPreco.getDataInicioVigencia().after(entity.getDataInicioVigencia())) {
                    entity.setDataFimVigencia(DateUtils.addMonths(tabelaPreco.getDataInicioVigencia(), -1));
                    priceTableService.updateTabelaPreco(entity);
                }
            }
        }
    }
}