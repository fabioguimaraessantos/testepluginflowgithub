package com.ciandt.pms.integration.service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IQBOInvoiceJournalService;
import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.integration.queue.InvoiceProducer;
import com.ciandt.pms.integration.vo.CancelInvoice;
import com.ciandt.pms.model.QBOInvoiceJournal;
import com.ciandt.pms.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CancelFaturaService implements ICancelFaturaService {

    private final Logger logger = LoggerFactory.getLogger(CancelFaturaService.class);

    @Autowired
    private IQBOInvoiceJournalService qboInvoiceJournalService;

    @Autowired
    private InvoiceProducer invoiceProducer;

    @Override
    public void cancelExternalERP(String invoiceCode) throws BusinessException {
        logger.info("Cancelling invoice {}.", invoiceCode);

        if (invoiceCode == null || invoiceCode.trim().isEmpty()) {
            throw new BusinessException("Invalid invoice number " + invoiceCode);
        }

        List<QBOInvoiceJournal> journals = qboInvoiceJournalService.findByInvoiceNumber(invoiceCode);
        if (journals != null && !journals.isEmpty()) {
            logger.info("Invoice has Quickbooks relationship, sending to cancel.");
            this.cancelInQuickbooks(invoiceCode, journals);
        }
    }

    /**
     * Invoice cancelation on Quickbooks ERP.
     * @param invoiceCode QBO invoice code.
     * @param journals QBO related journals.
     * @throws BusinessException if validation or error on sending message.
     */
    protected void cancelInQuickbooks(String invoiceCode, List<QBOInvoiceJournal> journals) throws BusinessException {
        List<String> journalNumbers = this.getJournalNumbers(journals);
        logger.info("Found {} journal(s) related with invoice {}.", journalNumbers.size(), invoiceCode);

        QBOInvoiceJournal firstJournal = journals.get(0);
        if (firstJournal != null && !this.checkJournalCreationDate(firstJournal.getCreatedAt())) {
            String dateMessage = DateUtil.formatDate(firstJournal.getCreatedAt(), Constants.DEFAULT_DATE_PATTERN_SIMPLE, Constants.DEFAULT_CALENDAR_LOCALE);
            logger.warn("Journal was not created at same month of invoice cancelation: {}.", dateMessage);
            String message = String.format("Cannot process automatic cancelation on Quickbooks because Journal Entry was created at %s. Please check journal(s) %s.",
                    dateMessage, journalNumbers);
            throw new BusinessException(message);
        }

        try {
            logger.info("Sending CancelInvoice message to queue.");
            CancelInvoice cancelInvoice = new CancelInvoice(invoiceCode, journalNumbers);
            String json = cancelInvoice.toJson();
            invoiceProducer.send(json, Constants.INVOICE_CANCEL);
            logger.info("Message sent successfully {}.", json);
        } catch (Exception e) {
            logger.error("Error when sending cancel invoice message.", e);
            throw new BusinessException("An error occurred when sending cancel invoice message to exchange. " + e.getMessage());
        }
    }

    /**
     * Extract journal number to build queue message.
     * @param journals journals related with invoice.
     * @return list of journal number/codes.
     */
    protected List<String> getJournalNumbers(List<QBOInvoiceJournal> journals) {
        List<String> qboRelatedJournals = new ArrayList<>();
        for (QBOInvoiceJournal journal : journals) {
            qboRelatedJournals.add(journal.getJournalNumber());
        }
        return qboRelatedJournals;
    }

    /**
     * Validate if journal was creates at same month that is being cancelled.
     * @param journalCreatedAt journal creation date.
     * @return true if journal created at current month.
     */
    protected boolean checkJournalCreationDate(Date journalCreatedAt) {
        return journalCreatedAt != null &&
                DateUtil.isSameDate(journalCreatedAt, new Date(), Calendar.MONTH);
    }
}
