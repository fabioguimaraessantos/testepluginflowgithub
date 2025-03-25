package com.ciandt.pms.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "INVOICE_JOURNAL")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "QBOInvoiceJournal.findByInvoiceNumber", query = "SELECT qij FROM QBOInvoiceJournal qij "
                + "WHERE invoiceNumber = TRIM(?) ")
})
public class QBOInvoiceJournal {

    public static final String FIND_BY_INVOICE_NUMBER = "QBOInvoiceJournal.findByInvoiceNumber";

    @Id
    @Column(name = "INJO_CD_INVOICE_JOURNAL")
    private Long code;

    @Column(name = "INJO_NR_INVOICE")
    private String invoiceNumber;

    @Column(name = "INJO_NR_JOURNAL")
    private String journalNumber;

    @Column(name = "INJO_DT_CREATE")
    private Date createdAt;
}
