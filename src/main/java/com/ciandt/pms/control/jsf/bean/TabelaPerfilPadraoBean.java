package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.ItemTabPerPadrao;
import com.ciandt.pms.model.TabelaPerfilPadrao;


/**
 * Define o BackingBean da entidade TabelaPerfilPadrao.
 * 
 * @since 19/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class TabelaPerfilPadraoBean implements Serializable {

    /** Defaul serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /** to do backbean. */
    private TabelaPerfilPadrao to = new TabelaPerfilPadrao();

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();
    
    /** Propriedade do mes de inicio da vigencia. */
    private String mesInicioVigencia;

    /** Propriedade do ano de inicio da vigencia. */
    private String anoInicioVigencia;
    
    /** Propriedade da moeda. */
    private String moeda;
    
    /** Lista de todas as entidades do banco. */
    private List<TabelaPerfilPadrao> listaTabelaPerfilPadraoAll = new ArrayList<TabelaPerfilPadrao>();
    
    /** Data do HistoryLock. */
    private Date historyLockDate;
    
    /** Lista de Pmg para combobox. */
    private List<String> listaPmgCombobox = new ArrayList<String>();
    
    /** Lista de CidadeBase para combobox. */
    private List<String> listaCidadeBaseCombobox = new ArrayList<String>();
    
    /** Lista de Cargos para combobox. */
    private List<String> listaCargoCombobox = new ArrayList<String>();
    
    /** Lista de moedas para combobox. */
    private List<String> listaMoedaCombobox = new ArrayList<String>();
    
    /** Flag para mostrar botao de update e cancel. */
    private Boolean flagEdicaoTabelaPerPadrao = Boolean.valueOf(false);
    
    /** ItemTabPerPadrao. */
    private ItemTabPerPadrao itemTabPerPadrao;
    
    /** Flag para edição da data. */
    private Boolean flagEdicaoData = Boolean.valueOf(false);
    
    /** Map para combo de moeda. */
    private Map<String, Long> mapMoeda = new HashMap<String, Long>();
    

        /**
         * @return the to
         */
        public TabelaPerfilPadrao getTo() {
            return to;
        }
    
        /**
         * @param to the to to set
         */
        public void setTo(final TabelaPerfilPadrao to) {
            this.to = to;
        }
    
        /**
         * @return the monthList
         */
        public List<String> getMonthList() {
            return monthList;
        }
    
        /**
         * @param yearList
         *            the yearList to set
         */
        public void setYearList(final List<String> yearList) {
            this.yearList = yearList;
        }
    
        /**
         * @return the yearList
         */
        public List<String> getYearList() {
    
            int yearBegin =
                    Integer
                            .parseInt(systemProperties
                                    .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
            int range =
                    Integer
                            .parseInt(systemProperties
                                    .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));
    
            List<String> localYearList = new ArrayList<String>();
    
            for (int i = yearBegin; i <= yearBegin + range; i++) {
                localYearList.add("" + i);
            }
    
            this.yearList = localYearList;
    
            return this.yearList;
    
        }
    
        /**
         * @return the mesInicioVigencia
         */
        public String getMesInicioVigencia() {
            return mesInicioVigencia;
        }
    
        /**
         * @param mesInicioVigencia the mesInicioVigencia to set
         */
        public void setMesInicioVigencia(final String mesInicioVigencia) {
            this.mesInicioVigencia = mesInicioVigencia;
        }
    
        /**
         * @return the anoInicioVigencia
         */
        public String getAnoInicioVigencia() {
            return anoInicioVigencia;
        }
    
        /**
         * @param anoInicioVigencia the anoInicioVigencia to set
         */
        public void setAnoInicioVigencia(final String anoInicioVigencia) {
            this.anoInicioVigencia = anoInicioVigencia;
        }

        /**
         * @return the listaTabelaPerfilPadraoAll
         */
        public List<TabelaPerfilPadrao> getListaTabelaPerfilPadraoAll() {
            return listaTabelaPerfilPadraoAll;
        }

        /**
         * @param listaTabelaPerfilPadraoAll the listaTabelaPerfilPadraoAll to set
         */
        public void setListaTabelaPerfilPadraoAll(
                final List<TabelaPerfilPadrao> listaTabelaPerfilPadraoAll) {
            this.listaTabelaPerfilPadraoAll = listaTabelaPerfilPadraoAll;
        }
        
        /**
         * Reset Bean.
         */
        public void resetBean() {
            resetTo();
            this.moeda = "";
            this.flagEdicaoData = Boolean.FALSE;
            this.mesInicioVigencia = "";
            this.anoInicioVigencia = "";
            this.flagEdicaoTabelaPerPadrao = Boolean.FALSE;
        }
        
        /**
         * Reseta o bean.
         */
        public void resetTo() {
            this.to = new TabelaPerfilPadrao();
        }

        /**
         * @return the historyLockDate
         */
        public Date getHistoryLockDate() {
            return historyLockDate;
        }

        /**
         * @param historyLockDate the historyLockDate to set
         */
        public void setHistoryLockDate(final Date historyLockDate) {
            this.historyLockDate = historyLockDate;
        }

        /**
         * @return the listaPmgCombobox
         */
        public List<String> getListaPmgCombobox() {
            return listaPmgCombobox;
        }

        /**
         * @param listaPmgCombobox the listaPmgCombobox to set
         */
        public void setListaPmgCombobox(final List<String> listaPmgCombobox) {
            this.listaPmgCombobox = listaPmgCombobox;
        }

        /**
         * @return the listaCidadeBaseCombobox
         */
        public List<String> getListaCidadeBaseCombobox() {
            return listaCidadeBaseCombobox;
        }

        /**
         * @param listaCidadeBaseCombobox the listaCidadeBaseCombobox to set
         */
        public void setListaCidadeBaseCombobox(final List<String> listaCidadeBaseCombobox) {
            this.listaCidadeBaseCombobox = listaCidadeBaseCombobox;
        }

        /**
         * @return the listaCargoCombovox
         */
        public List<String> getListaCargoCombobox() {
            return listaCargoCombobox;
        }

        /**
         * @param listaCargoCombobox the listaCargoCombovox to set
         */
        public void setListaCargoCombobox(final List<String> listaCargoCombobox) {
            this.listaCargoCombobox = listaCargoCombobox;
        }

        /**
         * @return the flagEdicaoTabelaPerPadrao
         */
        public Boolean getFlagEdicaoTabelaPerPadrao() {
            return flagEdicaoTabelaPerPadrao;
        }

        /**
         * @param flagEdicaoTabelaPerPadrao the flagEdicaoTabelaPerPadrao to set
         */
        public void setFlagEdicaoTabelaPerPadrao(final Boolean flagEdicaoTabelaPerPadrao) {
            this.flagEdicaoTabelaPerPadrao = flagEdicaoTabelaPerPadrao;
        }

        /**
         * @return the itemTabPerPadrao
         */
        public ItemTabPerPadrao getItemTabPerPadrao() {
            return itemTabPerPadrao;
        }

        /**
         * @param itemTabPerPadrao the itemTabPerPadrao to set
         */
        public void setItemTabPerPadrao(final ItemTabPerPadrao itemTabPerPadrao) {
            this.itemTabPerPadrao = itemTabPerPadrao;
        }

        /**
         * @return the flagEdicaoData
         */
        public Boolean getFlagEdicaoData() {
            return flagEdicaoData;
        }

        /**
         * @param flagEdicaoData the flagEdicaoData to set
         */
        public void setFlagEdicaoData(final Boolean flagEdicaoData) {
            this.flagEdicaoData = flagEdicaoData;
        }

        /**
         * @return the listaMoedaCombobox
         */
        public List<String> getListaMoedaCombobox() {
            return listaMoedaCombobox;
        }

        /**
         * @param listaMoedaCombobox the listaMoedaCombobox to set
         */
        public void setListaMoedaCombobox(final List<String> listaMoedaCombobox) {
            this.listaMoedaCombobox = listaMoedaCombobox;
        }

        /**
         * @return the mapMoeda
         */
        public Map<String, Long> getMapMoeda() {
            return mapMoeda;
        }

        /**
         * @param mapMoeda the mapMoeda to set
         */
        public void setMapMoeda(final Map<String, Long> mapMoeda) {
            this.mapMoeda = mapMoeda;
        }

        /**
         * @return the moeda
         */
        public String getMoeda() {
            return moeda;
        }

        /**
         * @param moeda the moeda to set
         */
        public void setMoeda(final String moeda) {
            this.moeda = moeda;
        }
        
        
        
        
    
    
}
