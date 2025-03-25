package com.ciandt.pms.control.jsf.bean;

import com.ciandt.pms.Constants;
import com.ciandt.pms.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;


/**
 * 
 * A classe PessoaBean define o BackingBean da entidade Pessoa.
 * 
 * @since 11/03/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class PessoaBean implements Serializable {

    /** Default serial version UID. */
    private static final long serialVersionUID = 1L;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties appConfig;

    /** Instancia do TO (Pessoa) do backingbean. */
    private Pessoa to = new Pessoa();

    /** Instancia da lista de PessoaBillability. */
    private List<PessoaBillability> pessoaBillabilityList = new ArrayList<PessoaBillability>();

    /** Instancia do GrupoCusto. */
    private GrupoCusto grupoCusto = new GrupoCusto();

    /** Instancia do GrupoCusto. */
    private CidadeBase cidadeBase = new CidadeBase();

    /** Instancia do TipoContrato. */
    private TipoContrato tipoContrato = new TipoContrato();

    /** Instancia da Empresa. */
    private Empresa empresa = new Empresa();

    /** Instancia do Moeda. */
    private Moeda moeda = new Moeda();

    /** Map utilizado no combo de GrupoCusto. */
    private Map<String, Long> grupoCustoMap = new HashMap<String, Long>();

    /** Lista de GrupoCusto. */
    private List<GrupoCusto> grupoCustos = new ArrayList<GrupoCusto>();

    /** List utilizado no combo de GrupoCusto. */
    private List<String> grupoCustoList = new ArrayList<String>();

    /** Map utilizado no combo de CidadeBase. */
    private Map<String, Long> cidadeBaseMap = new HashMap<String, Long>();

    /** Lista de CidadeBase. */
    private List<PessoaCidadeBase> pessoaCidadeBases = new ArrayList<PessoaCidadeBase>();

    /** List utilizado no combo de CidadeBase. */
    private List<String> cidadeBaseList = new ArrayList<String>();

    /** Map utilizado no combo de TipoContrato. */
    private Map<String, Long> tipoContratoMap = new HashMap<String, Long>();

    /** Lista de TipoContrato. */
    private List<TipoContrato> tipoContratos = new ArrayList<TipoContrato>();

    /** List utilizado no combo de TipoContrato. */
    private List<String> tipoContratoList = new ArrayList<String>();

    /** List untilizado no combo de Empresa. */
    private List<String> empresaList = new ArrayList<String>();

    /** Map utilizado no combo de Empresa. */
    private Map<String, Long> empresaMap = new HashMap<String, Long>();

    /** Instancia do filtro (Pessoa) do backingbean. */
    private Pessoa filter = new Pessoa();

    /** Lista de resultado da busca. */
    private List<Pessoa> resultList = new ArrayList<Pessoa>();

    /** Id da pagina corrente na lista de pesquisa. */
    private Integer currentPageId = Integer.valueOf(0);

    /** Lista com os grupo custos associados a pessoa. */
    private List<PessoaGrupoCusto> pessoaGrupoCustoList = new ArrayList<PessoaGrupoCusto>();

    /** entidade PessoaGrupoCusto. */
    private PessoaGrupoCusto pessoaGrupoCusto = new PessoaGrupoCusto();

    /** Lista com as cidades base associados a pessoa. */
    private List<PessoaCidadeBase> pessoaCidadeBaseList = new ArrayList<PessoaCidadeBase>();

    /** entidade PessoaCidadeBase. */
    private PessoaCidadeBase pessoaCidadeBase = new PessoaCidadeBase();

    /** entidade PessoaTipoContrato. */
    private PessoaTipoContrato pessoaTipoContrato = new PessoaTipoContrato();

    /** Lista dos possiveis valores de meses. */
    private List<String> monthList = Constants.MONTH_DAY_LIST;

    /** Lista dos possiveis valores de anos. */
    private List<String> yearList = new ArrayList<String>();

    /** valor do mês inicio. */
    private String startMonth = null;

    /** valor do ano inicio. */
    private String startYear = null;

    /** valor do billability. */
    private String billabilitySelected;

    /** valor do billability. */
    private String newBillabilitySelected;

    /** Lista para o combobox de billability. */
    private List<String> billabilityOptions = Constants.BILLABILITY_LIST;

    /** Lista para o combobox com os recurso. */
    private List<String> pessoaList = new ArrayList<String>();

    /** Map para o combobox com os recurso. */
    private Map<String, Long> pessoaMap = new HashMap<String, Long>();

    /** Indicador do flag para buscar apenas Pessoa com status Ativo. */
    private Boolean isActiveOnly = Boolean.valueOf(true);

    /**
     * Indicador do flag para buscar apenas Pessoa que a Pessoa corrente está
     * seguindo.
     */
    private Boolean isFollowingOnly = Boolean.valueOf(false);

    /** percentualAlocacao da Pessoa no mes corrente. */
    private BigDecimal percentualAlocavel = BigDecimal.valueOf(0);

    /** valorJornada da Pessoa no mes corrente. */
    private BigDecimal valorJornada = BigDecimal.valueOf(0);

    /** Indicador do padrão para exibição de valores de moeda. */
    private String patternCurrency = "";

    /** valorSalario da Pessoa no mes corrente. */
    private BigDecimal valorSalario = BigDecimal.valueOf(0);

    /** nomeGrupoCusto da Pessoa no mes corrente. */
    private String nomeGrupoCusto = "";

    /** nomeCidadeBase da Pessoa no mes corrente. */
    private String nomeCidadeBase = "";

    /** nomeTipoContrato da Pessoa no mes corrente. */
    private String nomeTipoContrato = "";

    /** List utilizado no combo de Moeda. */
    private List<String> moedaList = new ArrayList<String>();

    /** Map utilizado no combo de Moeda. */
    private Map<String, Long> moedaMap = new HashMap<String, Long>();

    /** Array de itens com as roles do usuários. */
    private String[] grantedAuthorities;

    /** Array de itens com as areas dos usuários. */
    private String[] areasOrcamentariasAtribuidas;

    private String[] empresasOrcamentariasAtribuidas;

    /** Lista de itens com todas as roles. */
    private List<SelectItem> authorityList = new ArrayList<SelectItem>();

    /** Lista de itens com todas as areasOrcamentarias. */
    private List<SelectItem> areasOrcamentarias = new ArrayList<SelectItem>();

    private List<SelectItem> empresasOrcamentarias = new ArrayList<SelectItem>();

    /** Indica se a pessoa e people manager da pessoa a ser editada. */
    private Boolean isPeopleManager = Boolean.valueOf(false);

    /**
     * Map da relacao de Pessoa que a PessoaFlwer corrente está seguindo. Long -
     * código da Pessoa Long - código da Pessoa follower.
     */
    private Map<Long, Long> mapFollow = new HashMap<Long, Long>();

    /** Data do HistoryLock. */
    private Date historyLockDate;

    private PessoaBillability pessoaBillability;

    private PessoaBillability toPessoaBillabilty;

    private String deletePessoaGrupoCustoTicketId = "";

    public PessoaBillability getToPessoaBillabilty() {
        return toPessoaBillabilty;
    }

    public void setToPessoaBillabilty(PessoaBillability toPessoaBillabilty) {
        this.toPessoaBillabilty = toPessoaBillabilty;
    }

    /**
     * @return the historyLockDate
     */
    public Date getHistoryLockDate() {
        return historyLockDate;
    }

    /**
     * @param historyLockDate
     *            the historyLockDate to set
     */
    public void setHistoryLockDate(final Date historyLockDate) {
        this.historyLockDate = historyLockDate;
    }

    /**
     * @return the mapFollow
     */
    public Map<Long, Long> getMapFollow() {
        return mapFollow;
    }

    /**
     * @param mapFollow
     *            the mapFollow to set
     */
    public void setMapFollow(final Map<Long, Long> mapFollow) {
        this.mapFollow = mapFollow;
    }

    /**
     * @return the valorJornada
     */
    public BigDecimal getValorJornada() {
        return valorJornada;
    }

    /**
     * @param valorJornada
     *            the valorJornada to set
     */
    public void setValorJornada(final BigDecimal valorJornada) {
        this.valorJornada = valorJornada;
    }

    /**
     * @return the empresaList
     */
    public List<String> getEmpresaList() {
        return empresaList;
    }

    /**
     * @param empresaList
     *            the empresaList to set
     */
    public void setEmpresaList(final List<String> empresaList) {
        this.empresaList = empresaList;
    }

    /**
     * @return the empresaMap
     */
    public Map<String, Long> getEmpresaMap() {
        return empresaMap;
    }

    /**
     * @param empresaMap
     *            the empresaMap to set
     */
    public void setEmpresaMap(final Map<String, Long> empresaMap) {
        this.empresaMap = empresaMap;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa
     *            the empresa to set
     */
    public void setEmpresa(final Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @param patternCurrency
     *            the patternCurrency to set
     */
    public void setPatternCurrency(final String patternCurrency) {
        this.patternCurrency = patternCurrency;
    }

    /**
     * @return the patternCurrency
     */
    public String getPatternCurrency() {
        return patternCurrency + " ";
    }

    /**
     * @return the valorSalario
     */
    public BigDecimal getValorSalario() {
        return valorSalario;
    }

    /**
     * @param valorSalario
     *            the valorSalario to set
     */
    public void setValorSalario(final BigDecimal valorSalario) {
        this.valorSalario = valorSalario;
    }

    /**
     * @return the moeda
     */
    public Moeda getMoeda() {
        return moeda;
    }

    /**
     * @param moeda
     *            the moeda to set
     */
    public void setMoeda(final Moeda moeda) {
        this.moeda = moeda;
    }

    /**
     * @return the moedaList
     */
    public List<String> getMoedaList() {
        return moedaList;
    }

    /**
     * @param moedaList
     *            the moedaList to set
     */
    public void setMoedaList(final List<String> moedaList) {
        this.moedaList = moedaList;
    }

    /**
     * @return the moedaMap
     */
    public Map<String, Long> getMoedaMap() {
        return moedaMap;
    }

    /**
     * @param moedaMap
     *            the moedaMap to set
     */
    public void setMoedaMap(final Map<String, Long> moedaMap) {
        this.moedaMap = moedaMap;
    }

    /**
     * @return the percentualAlocavel
     */
    public BigDecimal getPercentualAlocavel() {
        return percentualAlocavel;
    }

    /**
     * @param percentualAlocavel
     *            the percentualAlocavel to set
     */
    public void setPercentualAlocavel(final BigDecimal percentualAlocavel) {
        this.percentualAlocavel = percentualAlocavel;
    }

    /**
     * @return the nomeGrupoCusto
     */
    public String getNomeGrupoCusto() {
        return nomeGrupoCusto;
    }

    /**
     * @param nomeGrupoCusto
     *            the nomeGrupoCusto to set
     */
    public void setNomeGrupoCusto(final String nomeGrupoCusto) {
        this.nomeGrupoCusto = nomeGrupoCusto;
    }

    /**
     * @return the nomeTipoContrato
     */
    public String getNomeTipoContrato() {
        return nomeTipoContrato;
    }

    /**
     * @param nomeTipoContrato
     *            the nomeTipoContrato to set
     */
    public void setNomeTipoContrato(final String nomeTipoContrato) {
        this.nomeTipoContrato = nomeTipoContrato;
    }

    /**
     * @return the tipoContratoMap
     */
    public Map<String, Long> getTipoContratoMap() {
        return tipoContratoMap;
    }

    /**
     * @param tipoContratoMap
     *            the tipoContratoMap to set
     */
    public void setTipoContratoMap(final Map<String, Long> tipoContratoMap) {
        this.tipoContratoMap = tipoContratoMap;
    }

    /**
     * @return the tipoContratos
     */
    public List<TipoContrato> getTipoContratos() {
        return tipoContratos;
    }

    /**
     * @param tipoContratos
     *            the tipoContratos to set
     */
    public void setTipoContratos(final List<TipoContrato> tipoContratos) {
        this.tipoContratos = tipoContratos;
    }

    /**
     * @return the tipoContratoList
     */
    public List<String> getTipoContratoList() {
        return tipoContratoList;
    }

    /**
     * @param tipoContratoList
     *            the tipoContratoList to set
     */
    public void setTipoContratoList(final List<String> tipoContratoList) {
        this.tipoContratoList = tipoContratoList;
    }

    /**
     * @return the tipoContrato
     */
    public TipoContrato getTipoContrato() {
        return tipoContrato;
    }

    /**
     * @param tipoContrato
     *            the tipoContrato to set
     */
    public void setTipoContrato(final TipoContrato tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    /**
     * @return the pessoaTipoContrato
     */
    public PessoaTipoContrato getPessoaTipoContrato() {
        return pessoaTipoContrato;
    }

    /**
     * @param pessoaTipoContrato
     *            the pessoaTipoContrato to set
     */
    public void setPessoaTipoContrato(
            final PessoaTipoContrato pessoaTipoContrato) {
        this.pessoaTipoContrato = pessoaTipoContrato;
    }

    /**
     * @return the isActiveOnly
     */
    public Boolean getIsActiveOnly() {
        return isActiveOnly;
    }

    /**
     * @param isActiveOnly
     *            the isActiveOnly to set
     */
    public void setIsActiveOnly(final Boolean isActiveOnly) {
        this.isActiveOnly = isActiveOnly;
    }

    /**
     * @return the isFollowingOnly
     */
    public Boolean getIsFollowingOnly() {
        return isFollowingOnly;
    }

    /**
     * @param isFollowingOnly
     *            the isFollowingOnly to set
     */
    public void setIsFollowingOnly(final Boolean isFollowingOnly) {
        this.isFollowingOnly = isFollowingOnly;
    }

    /**
     * @return the pessoaList
     */
    public List<String> getPessoaList() {
        return pessoaList;
    }

    /**
     * @param pessoaList
     *            the pessoaList to set
     */
    public void setPessoaList(final List<String> pessoaList) {
        this.pessoaList = pessoaList;
    }

    /**
     * @return the pessoaMap
     */
    public Map<String, Long> getPessoaMap() {
        return pessoaMap;
    }

    /**
     * @param pessoaMap
     *            the pessoaMap to set
     */
    public void setPessoaMap(final Map<String, Long> pessoaMap) {
        this.pessoaMap = pessoaMap;
    }

    /**
     * @return the to
     */
    public Pessoa getTo() {
        return to;
    }

    /**
     * @param to
     *            the to to set
     */
    public void setTo(final Pessoa to) {
        this.to = to;
    }

    /**
     * @return the filter
     */
    public Pessoa getFilter() {
        return filter;
    }

    /**
     * @param filter
     *            the filter to set
     */
    public void setFilter(final Pessoa filter) {
        this.filter = filter;
    }

    /**
     * @return the resultList
     */
    public List<Pessoa> getResultList() {
        return resultList;
    }

    /**
     * @param resultList
     *            the resultList to set
     */
    public void setResultList(final List<Pessoa> resultList) {
        this.resultList = resultList;
    }

    public List<PessoaBillability> getPessoaBillabilityList() {
        return pessoaBillabilityList;
    }

    public void setPessoaBillabilityList(List<PessoaBillability> pessoaBillabilityList) {
        this.pessoaBillabilityList = pessoaBillabilityList;
    }

    public PessoaBillability getPessoaBillability() {
        return pessoaBillability;
    }

    public void setPessoaBillability(PessoaBillability pessoaBillability) {
        this.pessoaBillability = pessoaBillability;
    }

    /**
     * Reseta o bean.
     */
    public void reset() {
        resetTo();
        resetFilter();
        resetExtraFields();
        resetMapFollow();
        this.isActiveOnly = Boolean.valueOf(true);
        this.isFollowingOnly = Boolean.valueOf(false);
        this.deletePessoaGrupoCustoTicketId = "";
    }

    /**
     * Reseta os campos extra.
     */
    public void resetExtraFields() {
        this.nomeGrupoCusto = "";
        this.nomeTipoContrato = "";
        this.patternCurrency = "";
        this.valorSalario = BigDecimal.valueOf(0);
        this.percentualAlocavel = BigDecimal.valueOf(0);
    }

    /**
     * Reseta o TO.
     */
    public void resetTo() {
        this.to = new Pessoa();

    }

    /**
     * Reseta o formulario do grupo de custo.
     */
    public void resetGrupoCustoForm() {
        this.grupoCusto = new GrupoCusto();
        this.startMonth = "";
        this.startYear = "";
        this.pessoaGrupoCusto = new PessoaGrupoCusto();
        this.deletePessoaGrupoCustoTicketId = "";
    }

    /**
     * Reseta o formulario de cidade base.
     */
    public void resetCidadeBaseForm() {
        this.cidadeBase = new CidadeBase();
        this.startMonth = "";
        this.startYear = "";
        this.pessoaCidadeBase = new PessoaCidadeBase();
    }

    /**
     * Reseta o formulario do TipoContrato.
     */
    public void resetTipoContratoForm() {
        this.tipoContrato = new TipoContrato();
        this.empresa = new Empresa();
        this.startMonth = "";
        this.startYear = "";
        this.pessoaTipoContrato = new PessoaTipoContrato();
    }

    /**
     * Reseta o formulario do TipoContrato.
     */
    public void resetPessoaBillabityForm() {
        this.billabilitySelected = "";
        this.newBillabilitySelected = "";
        this.startMonth = "";
        this.startYear = "";
        this.pessoaBillability = new PessoaBillability();
    }

    public void loadBillabilityActionsConfiguration(Date historyLockDate) {

        if (!this.pessoaBillabilityList.isEmpty()) {
            if (pessoaBillabilityList.size() > 1) {
                for (PessoaBillability pessoaBillability : this.pessoaBillabilityList) {
                    if (pessoaBillability.getDataInicio().after(historyLockDate) && pessoaBillability.getDataFim() == null) {
                        pessoaBillability.setCanDelete(true);
                    }
                }
            }
        }
    }

    /**
     * Reseta o filtro.
     */
    public void resetFilter() {
        this.filter = new Pessoa();
        this.resultList = new ArrayList<Pessoa>();
    }

    /**
     * Reseta o Map de relacao de Pessoa que estão sendo seguidos.
     */
    public void resetMapFollow() {
        this.mapFollow = new HashMap<Long, Long>();
    }

    /**
     * @param currentPageId
     *            the currentPageId to set
     */
    public void setCurrentPageId(final Integer currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * @return the currentPageId
     */
    public Integer getCurrentPageId() {
        return currentPageId;
    }

    /**
     * @param pessoaGrupoCustoList
     *            the pessoaGrupoCustoList to set
     */
    public void setPessoaGrupoCustoList(
            final List<PessoaGrupoCusto> pessoaGrupoCustoList) {
        this.pessoaGrupoCustoList = pessoaGrupoCustoList;
    }

    /**
     * @return the pessoaGrupoCustoList
     */
    public List<PessoaGrupoCusto> getPessoaGrupoCustoList() {
        return pessoaGrupoCustoList;
    }

    /**
     * @param grupoCusto
     *            the grupoCusto to set
     */
    public void setGrupoCusto(final GrupoCusto grupoCusto) {
        this.grupoCusto = grupoCusto;
    }

    /**
     * @return the grupoCusto
     */
    public GrupoCusto getGrupoCusto() {
        return grupoCusto;
    }

    /**
     * @param monthList
     *            the monthList to set
     */
    public void setMonthList(final List<String> monthList) {
        this.monthList = monthList;
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
        int yearBegin = Integer.parseInt(appConfig
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
        int range = Integer.parseInt(appConfig
                .getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

        List<String> localYearList = new ArrayList<String>();

        for (int i = yearBegin; i <= yearBegin + range; i++) {
            localYearList.add("" + i);
        }

        this.yearList = localYearList;

        return this.yearList;
    }

    /**
     * @return the startMonth
     */
    public String getStartMonth() {
        return startMonth;
    }

    /**
     * @param startMonth
     *            the startMonth to set
     */
    public void setStartMonth(final String startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * @return the startYear
     */
    public String getStartYear() {
        return startYear;
    }

    /**
     * @param startYear
     *            the startYear to set
     */
    public void setStartYear(final String startYear) {
        this.startYear = startYear;
    }

    /**
     * @return the grupoCustoMap
     */
    public Map<String, Long> getGrupoCustoMap() {
        return grupoCustoMap;
    }

    /**
     * @param grupoCustoMap
     *            the grupoCustoMap to set
     */
    public void setGrupoCustoMap(final Map<String, Long> grupoCustoMap) {
        this.grupoCustoMap = grupoCustoMap;
    }

    /**
     * @return the grupoCustoList
     */
    public List<String> getGrupoCustoList() {
        return grupoCustoList;
    }

    /**
     * @param grupoCustoList
     *            the grupoCustoList to set
     */
    public void setGrupoCustoList(final List<String> grupoCustoList) {
        this.grupoCustoList = grupoCustoList;
    }

    /**
     * @param grupoCustos
     *            the grupoCustos to set
     */
    public void setGrupoCustos(final List<GrupoCusto> grupoCustos) {
        this.grupoCustos = grupoCustos;
    }

    /**
     * @return the grupoCustos
     */
    public List<GrupoCusto> getGrupoCustos() {
        return grupoCustos;
    }

    /**
     * @param pessoaGrupoCusto
     *            the pessoaGrupoCusto to set
     */
    public void setPessoaGrupoCusto(final PessoaGrupoCusto pessoaGrupoCusto) {
        this.pessoaGrupoCusto = pessoaGrupoCusto;
    }

    /**
     * @return the pessoaGrupoCusto
     */
    public PessoaGrupoCusto getPessoaGrupoCusto() {
        return pessoaGrupoCusto;
    }

    /**
     * @param grantedAuthorities
     *            the grantedAuthorities to set
     */
    public void setGrantedAuthorities(final String[] grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    /**
     * @return the grantedAuthorities
     */
    public String[] getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public String[] getEmpresasOrcamentariasAtribuidas() {
        return empresasOrcamentariasAtribuidas;
    }

    public void setEmpresasOrcamentariasAtribuidas(String[] empresasOrcamentariasAtribuidas) {
        this.empresasOrcamentariasAtribuidas = empresasOrcamentariasAtribuidas;
    }

    /**
     * @param areasOrcamentariasAtribuidas
     *            the grantedAuthorities to set
     */
    public void setAreasOrcamentariasAtribuidas(final String[] areasOrcamentariasAtribuidas) {
        this.areasOrcamentariasAtribuidas = areasOrcamentariasAtribuidas;
    }

    /**
     * @return the areasOrcamentariasAtribuidas
     */
    public String[] getAreasOrcamentariasAtribuidas() {
        return areasOrcamentariasAtribuidas;
    }

    /**
     * @param authorityList
     *            the authorityList to set
     */
    public void setAuthorityList(final List<SelectItem> authorityList) {
        this.authorityList = authorityList;
    }

    /**
     * @return the authorityList
     */
    public List<SelectItem> getAuthorityList() {
        return authorityList;
    }

    public List<SelectItem> getEmpresasOrcamentarias() {
        return empresasOrcamentarias;
    }

    public void setEmpresasOrcamentarias(List<SelectItem> empresasOrcamentarias) {
        this.empresasOrcamentarias = empresasOrcamentarias;
    }

    /**
     * @param areasOrcamentarias
     *            the authorityList to set
     */
    public void setAreasOrcamentarias(final List<SelectItem> areasOrcamentarias) {
        this.areasOrcamentarias = areasOrcamentarias;
    }

    /**
     * @return the authorityList
     */
    public List<SelectItem> getAreasOrcamentarias() {
        return areasOrcamentarias;
    }

    /**
     * @return the isPeopleManager
     */
    public Boolean getIsPeopleManager() {
        return isPeopleManager;
    }

    /**
     * @param isPeopleManager
     *            the isPeopleManager to set
     */
    public void setIsPeopleManager(final Boolean isPeopleManager) {
        this.isPeopleManager = isPeopleManager;
    }

    /**
     * @return the cidadeBase
     */
    public CidadeBase getCidadeBase() {
        return cidadeBase;
    }

    /**
     * @param cidadeBase
     *            the cidadeBase to set
     */
    public void setCidadeBase(final CidadeBase cidadeBase) {
        this.cidadeBase = cidadeBase;
    }

    /**
     * @return the cidadeBaseMap
     */
    public Map<String, Long> getCidadeBaseMap() {
        return cidadeBaseMap;
    }

    /**
     * @param cidadeBaseMap
     *            the cidadeBaseMap to set
     */
    public void setCidadeBaseMap(final Map<String, Long> cidadeBaseMap) {
        this.cidadeBaseMap = cidadeBaseMap;
    }

    /**
     * @return the cidadeBases
     */
    public List<PessoaCidadeBase> getPessoaCidadeBases() {
        return pessoaCidadeBases;
    }

    /**
     * @param pessoaCidadeBases
     *            the cidadeBases to set
     */
    public void setPessoaCidadeBases(final List<PessoaCidadeBase> pessoaCidadeBases) {
        this.pessoaCidadeBases = pessoaCidadeBases;
    }

    /**
     * @return the cidadeBaseList
     */
    public List<String> getCidadeBaseList() {
        return cidadeBaseList;
    }

    /**
     * @param cidadeBaseList
     *            the cidadeBaseList to set
     */
    public void setCidadeBaseList(final List<String> cidadeBaseList) {
        this.cidadeBaseList = cidadeBaseList;
    }

    /**
     * @return the pessoaCidadeBaseList
     */
    public List<PessoaCidadeBase> getPessoaCidadeBaseList() {
        return pessoaCidadeBaseList;
    }

    /**
     * @param pessoaCidadeBaseList
     *            the pessoaCidadeBaseList to set
     */
    public void setPessoaCidadeBaseList(
            final List<PessoaCidadeBase> pessoaCidadeBaseList) {
        this.pessoaCidadeBaseList = pessoaCidadeBaseList;
    }

    /**
     * @return the pessoaCidadeBase
     */
    public PessoaCidadeBase getPessoaCidadeBase() {
        return pessoaCidadeBase;
    }

    /**
     * @param pessoaCidadeBase
     *            the pessoaCidadeBase to set
     */
    public void setPessoaCidadeBase(final PessoaCidadeBase pessoaCidadeBase) {
        this.pessoaCidadeBase = pessoaCidadeBase;
    }

    /**
     * @return the nomeCidadeBase
     */
    public String getNomeCidadeBase() {
        return nomeCidadeBase;
    }

    /**
     * @param nomeCidadeBase
     *            the nomeCidadeBase to set
     */
    public void setNomeCidadeBase(final String nomeCidadeBase) {
        this.nomeCidadeBase = nomeCidadeBase;
    }

    public String getBillabilitySelected() {
        return billabilitySelected;
    }

    public void setBillabilitySelected(String billabilitySelected) {
        this.billabilitySelected = billabilitySelected;
    }

    public String getNewBillabilitySelected() {
        return newBillabilitySelected;
    }

    public void setNewBillabilitySelected(String newBillabilitySelected) {
        this.newBillabilitySelected = newBillabilitySelected;
    }

    public List<String> getBillabilityOptions() {
        return billabilityOptions;
    }

    public void setBillabilityOptions(List<String> billabilityOptions) {
        this.billabilityOptions = billabilityOptions;
    }

    public String getDeletePessoaGrupoCustoTicketId() {
        return deletePessoaGrupoCustoTicketId;
    }

    public void setDeletePessoaGrupoCustoTicketId(String deletePessoaGrupoCustoTicketId) {
        this.deletePessoaGrupoCustoTicketId = deletePessoaGrupoCustoTicketId;
    }
}
