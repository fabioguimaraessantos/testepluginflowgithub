package com.ciandt.pms.integration.service;

import com.ciandt.pms.exception.BusinessException;
import com.ciandt.pms.integration.queue.InvoiceProducer;
import com.ciandt.pms.model.QBOInvoiceJournal;
import com.ciandt.pms.util.DateUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class CancelFaturaServiceTest {

    @InjectMocks
    CancelFaturaService service;

    @Mock
    InvoiceProducer invoiceProducer;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void cancelOnQuickbooks_withSameMonthInvoice_mustSendMessageToCancel() throws BusinessException, IOException, TimeoutException {
        // given
        List<QBOInvoiceJournal> journals = mockInvoiceJournals();
        String invoiceCode = journals.get(0).getInvoiceNumber();

        // when
        service.cancelInQuickbooks(invoiceCode, journals);

        // then
        Mockito.verify(invoiceProducer, Mockito.times(1)).send(Mockito.any(), Mockito.any());
    }

    @Test(expected = BusinessException.class)
    public void cancelOnQuickbooks_withOldInvoice_mustThrowBusinessException() throws BusinessException, IOException, TimeoutException {
        // given
        Date createdAt = DateUtil.getDateFirstDayOfMonth(DateUtil.addMonths(new Date(), -1));
        List<QBOInvoiceJournal> journals = mockInvoiceJournals();
        String invoiceCode = journals.get(0).getInvoiceNumber();
        journals.get(0).setCreatedAt(createdAt);
        journals.get(1).setCreatedAt(createdAt);

        // when
        service.cancelInQuickbooks(invoiceCode, journals);

        // then
        Mockito.verify(invoiceProducer, Mockito.times(0)).send(Mockito.any(), Mockito.any());
    }

    @Test
    public void getJournalNumbers_withValidInvoiceJornals_mustExtractJournalCodes() {
        // given
        List<QBOInvoiceJournal> journals = mockInvoiceJournals();

        // when
        List<String> result = service.getJournalNumbers(journals);

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public void checkJournalCreationDate_withOldInvoice_mustReturnFalse() {
        // given
        Date createdAt = DateUtil.getDateFirstDayOfMonth(DateUtil.addMonths(new Date(), -1));

        // when
        boolean result = service.checkJournalCreationDate(createdAt);

        // then
        assertFalse(result);
    }

    @Test
    public void checkJournalCreationDate_withSameMonthInvoice_mustReturnTrue() {
        // given
        Date createdAt = DateUtil.getDateFirstDayOfMonth(new Date());

        // when
        boolean result = service.checkJournalCreationDate(createdAt);

        // then
        assertTrue(result);
    }

    @Test
    public void checkJournalCreationDate_withFutureInvoice_mustReturnFalse() {
        // given
        Date createdAt = DateUtil.getDateFirstDayOfMonth(DateUtil.addMonths(new Date(), 1));

        // when
        boolean result = service.checkJournalCreationDate(createdAt);

        // then
        assertFalse(result);
    }

    private List<QBOInvoiceJournal> mockInvoiceJournals() {
        QBOInvoiceJournal qboInvoiceJournal = new QBOInvoiceJournal();
        qboInvoiceJournal.setCode(1L);
        qboInvoiceJournal.setInvoiceNumber("INV-1");
        qboInvoiceJournal.setJournalNumber("JRN-1");
        qboInvoiceJournal.setCreatedAt(DateUtil.getDateFirstDayOfMonth(new Date()));
        QBOInvoiceJournal qboInvoiceJournal2 = new QBOInvoiceJournal();
        qboInvoiceJournal2.setCode(2L);
        qboInvoiceJournal2.setInvoiceNumber("INV-1");
        qboInvoiceJournal2.setJournalNumber("JRN-2");
        qboInvoiceJournal2.setCreatedAt(DateUtil.getDateFirstDayOfMonth(new Date()));
        return Arrays.asList(qboInvoiceJournal, qboInvoiceJournal2);
    }
}