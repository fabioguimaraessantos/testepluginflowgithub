package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IQBOInvoiceJournalService;
import com.ciandt.pms.model.QBOInvoiceJournal;
import com.ciandt.pms.persistence.dao.IQBOInvoiceJournalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QBOInvoiceJournalService implements IQBOInvoiceJournalService {

    @Autowired
    private IQBOInvoiceJournalDao dao;

    public List<QBOInvoiceJournal> findByInvoiceNumber(String invoiceNumber) {
        List<QBOInvoiceJournal> qboInvoiceJournals = dao.findByInvoiceNumber(invoiceNumber);

        if (qboInvoiceJournals != null) {
            return qboInvoiceJournals;
        }
        return Collections.emptyList();
    }
}
