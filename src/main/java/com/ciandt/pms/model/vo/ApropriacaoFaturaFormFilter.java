package com.ciandt.pms.model.vo;

import com.ciandt.pms.util.DateUtil;

import java.util.Date;


/**
 * Classe que representa a linha na tela de filtro do 'FiscalBalance'. 
 *
 * @since 18/01/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public class ApropriacaoFaturaFormFilter implements java.io.Serializable {

    /** Default seraul version UID.*/
    private static final long serialVersionUID = 1L;
    
    /** codigo do contrato-pratica. */
    private Long codigoContratoPratica;

    /** nome do contrato-pratica. */
    private String nomeContratoPratica;

    /** codigo da receita. */
    private Long codigoReceita;

    /** data mes inicio. */
    private Date dataMesInicio;

    /** mes inicio. */
    private String mesInicio;
    
    /** ano inicio. */
    private String anoInicio;
    
    /** mes fim. */
    private String mesFim;
    
    /** ano fim. */
    private String anoFim;
    
    /** data mes fim. */
    private Date dataMesFim;
    
    /** codigo do cliente. */
    private Long codigoCliente;

    /** nome do cliente. */
    private String nomeCliente;

    /** codigo da natureza. */
    private Long codigoNatureza;

    /** codigo ddo centro-lucro. */
    private Long codigoCentroLucro;

    /** codigo da pratica. */
    private Long codigoPratica;

    /** nome do centro-lucro. */
    private String nomeCentroLucro;

    /** nome da pratica. */
    private String nomePratica;

    /** nome da natureza. */
    private String nomeNatureza;

    /** indicador do status da versão. */
    private String indicadorStatus;
    
    /** codigo do deal. */
    private Long codigoDealFiscal;
    
    /** nome deal fiscal. */
    private String nomeDealFiscal;
    

    /**
     * @param codigoContratoPratica the codigoContratoPratica to set
     */
    public void setCodigoContratoPratica(final Long codigoContratoPratica) {
        this.codigoContratoPratica = codigoContratoPratica;
    }

    /**
     * @return the codigoContratoPratica
     */
    public Long getCodigoContratoPratica() {
        return codigoContratoPratica;
    }

    /**
     * @param nomeContratoPratica the nomeContratoPratica to set
     */
    public void setNomeContratoPratica(final String nomeContratoPratica) {
        this.nomeContratoPratica = nomeContratoPratica;
    }

    /**
     * @return the nomeContratoPratica
     */
    public String getNomeContratoPratica() {
        return nomeContratoPratica;
    }

    /**
     * @param codigoReceita the codigoReceita to set
     */
    public void setCodigoReceita(final Long codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    /**
     * @return the codigoReceita
     */
    public Long getCodigoReceita() {
        return codigoReceita;
    }

    /**
     * @param codigoCliente the codigoCliente to set
     */
    public void setCodigoCliente(final Long codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    /**
     * @return the codigoCliente
     */
    public Long getCodigoCliente() {
        return codigoCliente;
    }

    /**
     * @param nomeCliente the nomeCliente to set
     */
    public void setNomeCliente(final String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * @return the nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * @return the codigoNatureza
     */
    public Long getCodigoNatureza() {
        return codigoNatureza;
    }

    /**
     * @param codigoNatureza the codigoNatureza to set
     */
    public void setCodigoNatureza(final Long codigoNatureza) {
        this.codigoNatureza = codigoNatureza;
    }

    /**
     * @return the codigoCentroLucro
     */
    public Long getCodigoCentroLucro() {
        return codigoCentroLucro;
    }

    /**
     * @param codigoCentroLucro the codigoCentroLucro to set
     */
    public void setCodigoCentroLucro(final Long codigoCentroLucro) {
        this.codigoCentroLucro = codigoCentroLucro;
    }

    /**
     * @return the codigoPratica
     */
    public Long getCodigoPratica() {
        return codigoPratica;
    }

    /**
     * @param codigoPratica the codigoPratica to set
     */
    public void setCodigoPratica(final Long codigoPratica) {
        this.codigoPratica = codigoPratica;
    }

    /**
     * @return the nomeCentroLucro
     */
    public String getNomeCentroLucro() {
        return nomeCentroLucro;
    }

    /**
     * @param nomeCentroLucro the nomeCentroLucro to set
     */
    public void setNomeCentroLucro(final String nomeCentroLucro) {
        this.nomeCentroLucro = nomeCentroLucro;
    }

    /**
     * @return the nomePratica
     */
    public String getNomePratica() {
        return nomePratica;
    }

    /**
     * @param nomePratica the nomePratica to set
     */
    public void setNomePratica(final String nomePratica) {
        this.nomePratica = nomePratica;
    }

    /**
     * @return the nomeNatureza
     */
    public String getNomeNatureza() {
        return nomeNatureza;
    }

    /**
     * @param nomeNatureza the nomeNatureza to set
     */
    public void setNomeNatureza(final String nomeNatureza) {
        this.nomeNatureza = nomeNatureza;
    }

    /**
     * @return the codigoDealFiscal
     */
    public Long getCodigoDealFiscal() {
        return codigoDealFiscal;
    }

    /**
     * @param codigoDealFiscal the codigoDealFiscal to set
     */
    public void setCodigoDealFiscal(final Long codigoDealFiscal) {
        this.codigoDealFiscal = codigoDealFiscal;
    }

    /**
     * @return the nomeDealFiscal
     */
    public String getNomeDealFiscal() {
        return nomeDealFiscal;
    }

    /**
     * @param nomeDealFiscal the nomeDealFiscal to set
     */
    public void setNomeDealFiscal(final String nomeDealFiscal) {
        this.nomeDealFiscal = nomeDealFiscal;
    }

    /**
     * @param dataMesInicio the dataMesInicio to set
     */
    public void setDataMesInicio(final Date dataMesInicio) {
        this.dataMesInicio = dataMesInicio;
    }

    /**
     * @return the dataMesInicio
     */
    public Date getDataMesInicio() {
        dataMesInicio = DateUtil.getDate(
                getMesInicio(), getAnoInicio());
        
        return dataMesInicio;
    }

    /**
     * @return the dataMesFim
     */
    public Date getDataMesFim() {
        dataMesFim = DateUtil.getDate(
                getMesFim(), getAnoFim());
        return dataMesFim;
    }

    /**
     * @return the mesInicio
     */
    public String getMesInicio() {
        return mesInicio;
    }

    /**
     * @param anoInicio the anoInicio to set
     */
    public void setAnoInicio(final String anoInicio) {
        this.anoInicio = anoInicio;
    }

    /**
     * @return the anoInicio
     */
    public String getAnoInicio() {
        return anoInicio;
    }

    /**
     * @param mesFim the mesFim to set
     */
    public void setMesFim(final String mesFim) {
        this.mesFim = mesFim;
    }

    /**
     * @return the mesFim
     */
    public String getMesFim() {
        return mesFim;
    }

    /**
     * @param anoFim the anoFim to set
     */
    public void setAnoFim(final String anoFim) {
        this.anoFim = anoFim;
    }

    /**
     * @return the anoFim
     */
    public String getAnoFim() {
        return anoFim;
    }

    /**
     * @param mesInicio the mesInicio to set
     */
    public void setMesInicio(final String mesInicio) {
        this.mesInicio = mesInicio;
    }

    /**
     * @param indicadorStatus the indicadorStatus to set
     */
    public void setIndicadorStatus(final String indicadorStatus) {
        this.indicadorStatus = indicadorStatus;
    }

    /**
     * @return the indicadorStatus
     */
    public String getIndicadorStatus() {
        return indicadorStatus;
    }

}
