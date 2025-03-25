package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.QBOInvoiceJournal;
import com.ciandt.pms.persistence.dao.IQBOInvoiceJournalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
public class QBOInvoiceJournalDao extends AbstractDao<Long, QBOInvoiceJournal> implements IQBOInvoiceJournalDao {

    /** Intancia do entity manager do hibernate. */
    private EntityManager entityManager;

    @Autowired
    public QBOInvoiceJournalDao(@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, QBOInvoiceJournal.class);
        entityManager = factory.createEntityManager();
    }

    public List<QBOInvoiceJournal> findByInvoiceNumber(String invoiceNumber) {

        List<QBOInvoiceJournal> listResult = getJpaTemplate().findByNamedQuery(QBOInvoiceJournal.FIND_BY_INVOICE_NUMBER, new Object[]{ invoiceNumber });
        return listResult;
    }
}
