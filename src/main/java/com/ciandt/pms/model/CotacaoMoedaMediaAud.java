package com.ciandt.pms.model;

import lombok.Builder;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Table(name = "COTACAO_MOEDA_MEDIA_AUD")
public class CotacaoMoedaMediaAud {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cotacaoMoedaMediaAudSeq")
    @SequenceGenerator(name = "cotacaoMoedaMediaAudSeq", sequenceName = "COTACAO_MOEDA_MEDIA_AUD_SEQ", allocationSize = 1)
    @Column(name = "CMMA_CD_COTACAO_MOEDA_MEDIA_AUD")
    private Long codigoCotacaoMoedaMediaAud;

    @Column(name = "CMMA_IN_OPERATION")
    private String operation;

    @Column(name = "CMMA_DT_CREATE")
    private Date dateCreate;

    @Column(name = "CMMA_DT_BEGIN")
    private Date dateBegin;

    @Column(name = "CMMA_DT_END")
    private Date dateEnd;

    @Column(name = "CMMA_CD_LOGIN")
    private String login;

    @Column(name = "CMMA_CD_LOGIN_AUTH")
    private String loginAuth;

    @Column(name = "CMMA_CD_TICKET_AUTH")
    private String ticketAuth;

    public CotacaoMoedaMediaAud(Long CMMA_CD_COTACAO_MOEDA_MEDIA_AUD, String operation, Date dateCreate, Date dateBegin, Date dateEnd, String login, String loginAuth, String ticketAuth) {
        this.codigoCotacaoMoedaMediaAud = CMMA_CD_COTACAO_MOEDA_MEDIA_AUD;
        this.operation = operation;
        this.dateCreate = dateCreate;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.login = login;
        this.loginAuth = loginAuth;
        this.ticketAuth = ticketAuth;
    }

    public Long getCodigoCotacaoMoedaMediaAud() {
        return codigoCotacaoMoedaMediaAud;
    }

    public void setCodigoCotacaoMoedaMediaAud(Long codigoCotacaoMoedaMediaAud) {
        this.codigoCotacaoMoedaMediaAud = codigoCotacaoMoedaMediaAud;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLoginAuth() {
        return loginAuth;
    }

    public void setLoginAuth(String loginAuth) {
        this.loginAuth = loginAuth;
    }

    public String getTicketAuth() {
        return ticketAuth;
    }

    public void setTicketAuth(String ticketAuth) {
        this.ticketAuth = ticketAuth;
    }
}
