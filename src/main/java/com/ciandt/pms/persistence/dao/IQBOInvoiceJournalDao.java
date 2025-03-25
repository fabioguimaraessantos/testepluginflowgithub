package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.QBOInvoiceJournal;

import java.util.List;

public interface IQBOInvoiceJournalDao extends IAbstractDao<Long, QBOInvoiceJournal> {

    List<QBOInvoiceJournal> findByInvoiceNumber(String invoiceNumber);
}
