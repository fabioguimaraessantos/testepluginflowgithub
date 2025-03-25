package com.ciandt.pms.business.service;

import com.ciandt.pms.model.QBOInvoiceJournal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IQBOInvoiceJournalService {

    List<QBOInvoiceJournal> findByInvoiceNumber(String invoiceNumber);
}
