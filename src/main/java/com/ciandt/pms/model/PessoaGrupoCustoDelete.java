package com.ciandt.pms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "PESSOA_GRUPO_CUSTO_DELETE")
public class PessoaGrupoCustoDelete implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PEGC_CD_PESSOA_GRUPO_CUSTO")
    private Long deletedCode;

    @Column(name = "PGCD_TX_TICKET_ID")
    private String ticketId;

    public Long getDeletedCode() {
        return deletedCode;
    }

    public void setDeletedCode(Long deletedCode) {
        this.deletedCode = deletedCode;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
