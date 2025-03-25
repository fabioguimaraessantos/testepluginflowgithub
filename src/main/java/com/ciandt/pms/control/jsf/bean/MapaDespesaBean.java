package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.MapaDespesa;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.vo.MapaDespesaRow;


/**
 * 
 * Define o BackingBean da entidade Despesa.
 *
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MapaDespesaBean implements Serializable {

    /** Default seraul version UID. */
    private static final long serialVersionUID = 1L;

    /** TO do backingBean. */
    private MapaDespesa to = new MapaDespesa();
    
    /** filter do backingBean. */
    private MapaDespesa filter = new MapaDespesa();

    /** lista de resultados da pesquisa. */
    private List<MapaDespesa> resultList = new ArrayList<MapaDespesa>();
    
    /** Instancia da Entidade ContratoPratica. */
    private ContratoPratica contratoPratica = new ContratoPratica();

    /** Lista para o combobox com clientes. */
    private List<String> clienteList = new ArrayList<String>();

    /** Lista para o combobox com as clientes. */
    private Map<String, Long> clienteMap = new HashMap<String, Long>();

    /** Lista para o combobox com as NaturezaCentroLucro. */
    private List<String> naturezaList = new ArrayList<String>();

    /** Lista para o combobox com as NaturezaCentroLucro. */
    private Map<String, Long> naturezaMap = new HashMap<String, Long>();

    /** Lista para o combobox com as CentroLucro. */
    private List<String> centroLucroList = new ArrayList<String>();

    /** Lista para o combobox com as CentroLucro. */
    private Map<String, Long> centroLucroMap = new HashMap<String, Long>();
    
    /** Lista para o combobox com os Msa. */
    private List<String> msaList = new ArrayList<String>();

    /** Lista para o combobox com os Msa. */
    private Map<String, Long> msaMap = new HashMap<String, Long>();
    
    /** Lista de MapaDespesaRow. */
    private List<MapaDespesaRow> despesaRowList;
    
    /** Mes inicio vigencia. */
    private String monthBeg = null;

    /** Mes fim vigencia. */
    private String monthEnd = null;

    /** Ano inicio vigencia. */
    private String yearBeg = null;

    /** Ano fim vigencia. */
    private String yearEnd = null;
    
    /** Lista com as datas referentes a vigencia. */
    private List<Date> dateList;
    
    /** Moeda da tabela de custos. */
    private Moeda moeda;
    
    /** Lista de Total por mes. */
    private List<BigDecimal> totalPerMonthList = new ArrayList<BigDecimal>();
    
    /** Lista para o combobox com as ContratoPratica. */
    private List<String> contratoPraticaList = new ArrayList<String>();

    /** Lista para o combobox com as ContratoPratica. */
    private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
    
    /** Entidade do tipo Cliente utilizado no filtro. */
    private Cliente cliente = new Cliente();
    
    /** Entidade do tipo Contrato utilizado no filtro. */
    private Msa msa = new Msa();
    
    /** Entidade do tipo NaturezaCentroLucro utilizado no filtro. */
    private NaturezaCentroLucro natureza = new NaturezaCentroLucro();
    
    /** Entidade do tipo NaturezaCentroLucro utilizado no filtro. */
    private CentroLucro centroLucro = new CentroLucro();
    
    /** Representa o número da página corrente. */
    private int currentPageId = 0;
    
    /** Indica se foi clicado o botão de delete. */
    private Boolean isRemove = Boolean.FALSE;
    
    /**
     * Reseta o bean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        setIsRemove(Boolean.FALSE);
    }
    
    /**
     * Reseta o TO do bean.
     */
    public void resetTo() {
        this.to = new MapaDespesa();
        this.monthBeg = "";
        this.monthEnd = "";
        this.yearBeg = "";
        this.yearEnd = "";
    }
    
    /**
     * Reseta o filtro do Bean.
     */
    public void resetFilter() {
        this.filter = new MapaDespesa();
        resultList = new ArrayList<MapaDespesa>();
        cliente = new Cliente();
        msa = new Msa();
        natureza = new NaturezaCentroLucro();
        centroLucro = new CentroLucro();
    }
    
    /**
     * retorna a quantidade de colunas dos meses.
     * 
     * @return retorna um inteiro com o numero de colunas
     */
    public int getNumColumn() {
        
        if (dateList != null) {
            return dateList.size() + 3;
        }
        
        return 0;
    }
    
    /**
     * @param to the to to set
     */
    public void setTo(final MapaDespesa to) {
        this.to = to;
    }

    /**
     * @return the to
     */
    public MapaDespesa getTo() {
        return to;
    }

    /**
     * @param filter the filter to set
     */
    public void setFilter(final MapaDespesa filter) {
        this.filter = filter;
    }

    /**
     * @return the filter
     */
    public MapaDespesa getFilter() {
        if (filter != null && filter.getContratoPratica() == null) {
            filter.setContratoPratica(new ContratoPratica());
        }
        return filter;
    }

    /**
     * @param resultList the resultList to set
     */
    public void setResultList(final List<MapaDespesa> resultList) {
        this.resultList = resultList;
        //Workaround para inicializar o objeto cliente
        for (MapaDespesa mapaDespesa : resultList) {
            Hibernate.initialize(mapaDespesa.getContratoPratica().getMsa().getCliente());
        }
    }

    /**
     * @return the resultList
     */
    public List<MapaDespesa> getResultList() {
        return resultList;
    }

    /**
     * @param contratoPratica the contratoPratica to set
     */
    public void setContratoPratica(final ContratoPratica contratoPratica) {
        this.contratoPratica = contratoPratica;
    }

    /**
     * @return the contratoPratica
     */
    public ContratoPratica getContratoPratica() {
        return contratoPratica;
    }

    /**
     * @return the monthBeg
     */
    public String getMonthBeg() {
        return monthBeg;
    }

    /**
     * @param monthBeg the monthBeg to set
     */
    public void setMonthBeg(final String monthBeg) {
        this.monthBeg = monthBeg;
    }

    /**
     * @return the monthEnd
     */
    public String getMonthEnd() {
        return monthEnd;
    }

    /**
     * @param monthEnd the monthEnd to set
     */
    public void setMonthEnd(final String monthEnd) {
        this.monthEnd = monthEnd;
    }

    /**
     * @return the yearBeg
     */
    public String getYearBeg() {
        return yearBeg;
    }

    /**
     * @param yearBeg the yearBeg to set
     */
    public void setYearBeg(final String yearBeg) {
        this.yearBeg = yearBeg;
    }

    /**
     * @return the yearEnd
     */
    public String getYearEnd() {
        return yearEnd;
    }

    /**
     * @param yearEnd the yearEnd to set
     */
    public void setYearEnd(final String yearEnd) {
        this.yearEnd = yearEnd;
    }

    /**
     * @param despesaRowList the despesaRowList to set
     */
    public void setDespesaRowList(final List<MapaDespesaRow> despesaRowList) {
        this.despesaRowList = despesaRowList;
    }

    /**
     * @return the despesaRowList
     */
    public List<MapaDespesaRow> getDespesaRowList() {
        return despesaRowList;
    }

    /**
     * @param dateList the dateList to set
     */
    public void setDateList(final List<Date> dateList) {
        this.dateList = dateList;
    }

    /**
     * @return the dateList
     */
    public List<Date> getDateList() {
        return dateList;
    }

    /**
     * @param moeda the moeda to set
     */
    public void setMoeda(final Moeda moeda) {
        this.moeda = moeda;
    }

    /**
     * @return the moeda
     */
    public Moeda getMoeda() {
        return moeda;
    }

    /**
     * @param totalPerMonthList the totalPerMesList to set
     */
    public void setTotalPerMonthList(final List<BigDecimal> totalPerMonthList) {
        this.totalPerMonthList = totalPerMonthList;
    }

    /**
     * @return the totalPerMesList
     */
    public List<BigDecimal> getTotalPerMonthList() {
        return totalPerMonthList;
    }

    /**
     * @param contratoPraticaList the contratoPraticaList to set
     */
    public void setContratoPraticaList(final List<String> contratoPraticaList) {
        this.contratoPraticaList = contratoPraticaList;
    }

    /**
     * @return the contratoPraticaList
     */
    public List<String> getContratoPraticaList() {
        return contratoPraticaList;
    }

    /**
     * @param contratoPraticaMap the contratoPraticaMap to set
     */
    public void setContratoPraticaMap(final Map<String, Long> contratoPraticaMap) {
        this.contratoPraticaMap = contratoPraticaMap;
    }

    /**
     * @return the contratoPraticaMap
     */
    public Map<String, Long> getContratoPraticaMap() {
        return contratoPraticaMap;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(final Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @return the msa
     */
    public Msa getMsa() {
        return msa;
    }

    /**
     * @param msa the msa to set
     */
    public void setMsa(final Msa msa) {
        this.msa = msa;
    }

    /**
     * @return the natureza
     */
    public NaturezaCentroLucro getNatureza() {
        return natureza;
    }

    /**
     * @param natureza the natureza to set
     */
    public void setNatureza(final NaturezaCentroLucro natureza) {
        this.natureza = natureza;
    }

    /**
     * @return the centroLucro
     */
    public CentroLucro getCentroLucro() {
        return centroLucro;
    }

    /**
     * @param centroLucro the centroLucro to set
     */
    public void setCentroLucro(final CentroLucro centroLucro) {
        this.centroLucro = centroLucro;
    }

    /**
     * @param currentPageId the currentPageId to set
     */
    public void setCurrentPageId(final int currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the currentPageId
     */
    public int getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @return the clienteList
     */
    public List<String> getClienteList() {
        return clienteList;
    }

    /**
     * @param clienteList the clienteList to set
     */
    public void setClienteList(final List<String> clienteList) {
        this.clienteList = clienteList;
    }

    /**
     * @return the clienteMap
     */
    public Map<String, Long> getClienteMap() {
        return clienteMap;
    }

    /**
     * @param clienteMap the clienteMap to set
     */
    public void setClienteMap(final Map<String, Long> clienteMap) {
        this.clienteMap = clienteMap;
    }

    /**
     * @return the naturezaList
     */
    public List<String> getNaturezaList() {
        return naturezaList;
    }

    /**
     * @param naturezaList the naturezaList to set
     */
    public void setNaturezaList(final List<String> naturezaList) {
        this.naturezaList = naturezaList;
    }

    /**
     * @return the naturezaMap
     */
    public Map<String, Long> getNaturezaMap() {
        return naturezaMap;
    }

    /**
     * @param naturezaMap the naturezaMap to set
     */
    public void setNaturezaMap(final Map<String, Long> naturezaMap) {
        this.naturezaMap = naturezaMap;
    }

    /**
     * @return the centroLucroList
     */
    public List<String> getCentroLucroList() {
        return centroLucroList;
    }

    /**
     * @param centroLucroList the centroLucroList to set
     */
    public void setCentroLucroList(final List<String> centroLucroList) {
        this.centroLucroList = centroLucroList;
    }

    /**
     * @return the centroLucroMap
     */
    public Map<String, Long> getCentroLucroMap() {
        return centroLucroMap;
    }

    /**
     * @param centroLucroMap the centroLucroMap to set
     */
    public void setCentroLucroMap(final Map<String, Long> centroLucroMap) {
        this.centroLucroMap = centroLucroMap;
    }

    /**
     * @return the msaList
     */
    public List<String> getMsaList() {
        return msaList;
    }

    /**
     * @param msaList the msaList to set
     */
    public void setMsaList(final List<String> msaList) {
        this.msaList = msaList;
    }

    /**
     * @return the msaMap
     */
    public Map<String, Long> getMsaMap() {
        return msaMap;
    }

    /**
     * @param msaMap the msaMap to set
     */
    public void setMsaMap(final Map<String, Long> msaMap) {
        this.msaMap = msaMap;
    }

    /**
     * @param isRemove the isRemove to set
     */
    public void setIsRemove(final Boolean isRemove) {
        this.isRemove = isRemove;
    }

    /**
     * @return the isRemove
     */
    public Boolean getIsRemove() {
        return isRemove;
    }

}
