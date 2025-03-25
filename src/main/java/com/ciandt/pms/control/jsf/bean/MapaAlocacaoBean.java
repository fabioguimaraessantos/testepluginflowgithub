package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.model.Alocacao;
import com.ciandt.pms.model.AlocacaoPeriodo;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ExplicacaoDesvio;
import com.ciandt.pms.model.FatorUrMes;
import com.ciandt.pms.model.ItemEstratificacaoUr;
import com.ciandt.pms.model.MapaAlocacao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.VwPmsFinancials;
import com.ciandt.pms.model.vo.AlocacaoRow;
import com.ciandt.pms.model.vo.MapDashboardRow;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe MapaAlocacaoBean é utilizado como backingBean para a entidade
 * MapaAlocacao.
 * 
 * @since 12/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class MapaAlocacaoBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** to do backingBean. */
	private MapaAlocacao to = new MapaAlocacao();

	/** filter do backingBean. */
	private MapaAlocacao filter = new MapaAlocacao();

	/** to Alocacao do backingBean. */
	private Alocacao toAlocacao = new Alocacao();

	/** lista de resultados da pesquisa. */
	private List<MapaAlocacao> resultList = new ArrayList<MapaAlocacao>();

	/** Lista para o combobox com as ContratoPratica. */
	private List<String> contratoPraticaList = new ArrayList<String>();

	/** Lista para o combobox com as ContratoPratica. */
	private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

	/** Lista para o combobox com os Msa. */
	private List<String> msaList = new ArrayList<String>();

	/** Lista para o combobox com os Msa. */
	private Map<String, Long> msaMap = new HashMap<String, Long>();

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

	/** Lista para a tabela de EstratificacaoUr. */
	private List<ItemEstratificacaoUr> itensExtratificacaoUr = new ArrayList<ItemEstratificacaoUr>();

	/** Lista para a combobox com as ExplicacoesDesvio. */
	private List<String> explicacoesDesvio = new ArrayList<String>();

	/** Lista para a combobox com as ExplicacoesDesvio. */
	private Map<String, Long> explicacoesDesvioMap = new HashMap<String, Long>();

	/** Lista para o combobox com as CentroLucro. */
	private Map<String, Long> centroLucroMap = new HashMap<String, Long>();

	/** Id da entidade corrente selecionada na lista de pesquisa. */
	private Long currentRowId = Long.valueOf(0);

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(0);

	/** Lista de datas da vigencia do mapa de alocacao. */
	private List<Date> validityDateList = new ArrayList<Date>();

	/** Lista de totais do mapa de alocacao. */
	private List<BigDecimal> totalValuesList = new ArrayList<BigDecimal>();

	/** Lista de AlocacaoRow - matriz de Alocacao X AlocacaoPeriodo. */
	private List<AlocacaoRow> alocacaoRowList = new ArrayList<AlocacaoRow>();

	/** Lista dos possiveis valores de meses. */
	private List<String> validityMonthList = Arrays.asList("01", "02", "03",
			"04", "05", "06", "07", "08", "09", "10", "11", "12");

	/** Lista dos possiveis valores de anos. */
	private List<String> validityYearList = new ArrayList<String>();

	/** Flag para desabilitar os campos de vigencia inicial do mapa. */
	private boolean disabledValidityFieldsBeg = false;

	/** Flag para desabilitar os campos de vigencia final do mapa. */
	private boolean disabledValidityFieldsEnd = false;

	/** Mes inicio vigencia. */
	private String validityMonthBeg = null;

	/** Mes fim vigencia. */
	private String validityMonthEnd = null;

	/** Ano inicio vigencia. */
	private String validityYearBeg = null;

	/** Ano fim vigencia. */
	private String validityYearEnd = null;

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isUpdate = Boolean.valueOf(false);

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isUpdateReviewUr = Boolean.valueOf(false);

	/** Indicador do modo em tempo de execucao - create/update. */
	private Boolean isViewReviewUr = Boolean.valueOf(false);

	/** Indicador do modo em tempo de execucao - remove/view. */
	private Boolean isRemove = Boolean.valueOf(false);

	/** Indicador para mostar o modal de 'Save As'. */
	private Boolean showModalSaveAs = Boolean.valueOf(false);

	/** Armazena a versão de um MapaAlocacao. */
	private String mapaAlocacaoVersion = "";

	/**
	 * Armazena a opcao default do combobox de explicacao desvio da
	 * estratificacao da ur.
	 */
	private ExplicacaoDesvio explicacaoDesvioDefault;

	/**
	 * Armazena a opcao do combobox de explicacao desvio para aplicar a todos os
	 * itens da estratificacao da ur.
	 */
	private String explicacaoDesvioApply = "";

	/** linha de uma alocacao. */
	private AlocacaoRow alocacaoRow = new AlocacaoRow();

	/** Indica se a ordenção é crescente ou decrecente. */
	private boolean ascending = true;

	/** Data inicio do mapa. */
	private Date startDateWindow;

	/** Date fim do mapa. */
	private Date endDateWindow;

	/**
	 * Flag que indica se é para mover a janela para frente quando do método
	 * 'moveWindow' do controller do Mapa é acionado.
	 */
	private Boolean moveWindowToFront;

	/**
	 * Indica o numero de meses que se deseja mover.
	 */
	private Integer numberMonthsToMove;

	/**
	 * Indicador habilitado / desabilitado dos botões (editar/deletar) da lista
	 * de Alocacao.
	 */
	private Boolean isAlocacaoListButtonsEnabled = Boolean.valueOf(false);

	/** Indicador se o recurso é uma Pessoa. */
	private Boolean isPessoa = Boolean.FALSE;

	/** Valor total do Utilization Rate. */
	private BigDecimal totalUr = BigDecimal.valueOf(0);

	/** Valor da atualizacao do U.R. */
	private BigDecimal updateUr = BigDecimal.valueOf(0);

	/** Valor da atualizacao do FTE. */
	private BigDecimal updateFte = BigDecimal.valueOf(0);

	/** Valor total do FTE Alocado. */
	private BigDecimal totalAlocado = BigDecimal.valueOf(0);

	/** Valor total do FTE Receitado. */
	private BigDecimal totalReceitado = BigDecimal.valueOf(0);

	/** Valor total da diferenca entre FTE Receitado e FTE Alocado. */
	private BigDecimal totalDifference = BigDecimal.valueOf(0);

	/** Lista para o combobox com as Etiqueta. */
	private List<String> etiquetaList = new ArrayList<String>();

	/** Mapa para o combobox com as Etiqueta. */
	private Map<String, Long> etiquetaMap = new HashMap<String, Long>();

	/** Armazena o nome da Etiqueta para ser adicionada ao combo. */
	private String nomeEtiquetaAdd = "";

	/** Armazena o nome da Etiqueta selecionada no combo. */
	private String nomeEtiquetaSelected = "";

	/** Indicador de lock/unlock do MapaAlocacao. */
	private Boolean isLocked = Boolean.FALSE;

	/** Indicador de follow/unfollow do MapaAlocacao. */
	private Boolean isFollowed = Boolean.valueOf(false);

	/** Instancia do MapaAlocacao Dashboard. */
	private SortedMap<Date, MapDashboardRow> mapDashboard;

	/** Lista da tela de Dashboard. */
	private List<VwPmsFinancials> vwPmsFinancialsList = new ArrayList<VwPmsFinancials>();

	/** Indicador do padrão para exibição de valores de moeda. */
	private String patternCurrency = "";

	/** Entidade fatorUr. */
	private FatorUrMes fatorUr;

	/**
	 * contem o valor (outcome) da pagina que se deseja fazer o
	 * redirecionamento.
	 */
	private String redirectPage = "";

	/** Flag para o botão 'Save As'. */
	private Boolean isSaveAs = Boolean.valueOf(false);

	/**
	 * Indicador do flag para buscar apenas MapaAlocacao que a Pessoa corrente
	 * está seguindo.
	 */
	private Boolean isFollowingOnly = Boolean.valueOf(false);

	/** Determina o PerfilVendido do clone da Alocacao. */
	private PerfilVendido perfilVendidoAlocClone = new PerfilVendido();

	/** Determina o PerfilVendido do clone da Alocacao. */
	private String indicadorEstagioClone = null;

	/** Lista para o combobox com os PerfilVendido. */
	private List<String> perfilVendidoListAlocClone = new ArrayList<String>();

	/** Map para o combobox com os PerfilVendido. */
	private Map<String, Long> perfilVendidoMapAlocClone = new HashMap<String, Long>();

	/** Mes inicio clone Alocacao. */
	private String startMonthAlocClone = null;;

	/** Ano inicio clone Alocacao. */
	private String startYearAlocClone = null;

	/** Entidade cliente, utilizado no filtro. */
	private Cliente cliente = new Cliente();

	/** Entidade NaturezaCentroLucro, utilizado no filtro. */
	private NaturezaCentroLucro natureza = new NaturezaCentroLucro();

	/** Entidade CentroLucro, utilizado no filtro. */
	private CentroLucro centroLucro = new CentroLucro();

	/** Entidade Msa, utilizado no filtro. */
	private Msa msa = new Msa();

	/** Indicador mostra modal copia da Alocacao ou nao. */
	private boolean showModalCopy = true;

	/** Entidade alocacao peridodo. */
	private AlocacaoPeriodo alocacaoPeriodo;

	/**
	 * Map da relacao de MapaAlocacao que a Pessoa corrente está seguindo. Long
	 * - código do MapaAlocacao Long - código da Pessoa follower.
	 */
	private Map<Long, Long> mapFollow = new HashMap<Long, Long>();

	/** Data de closing date da Revenue. */
	private Date closingDateRevenue = null;

	/** Valor total por moeda. */
	private Map<Moeda, Double> totalCurrencyFinancialMap = new HashMap<Moeda, Double>();

	/** Valor total por moeda. */
	private Map<String, String> siglaMoedaMap = new HashMap<String, String>();

	/** Lista para a tela de financials. */
	private List<MapDashboardRow> listResult = new ArrayList<MapDashboardRow>();

	/** Lista de moedas para a tela de financials. */
	private List<Moeda> listaMoedas = new ArrayList<Moeda>();

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
	 * @return the showModalCopy
	 */
	public boolean getShowModalCopy() {
		return showModalCopy;
	}

	/**
	 * @param showModalCopy
	 *            the showModalCopy to set
	 */
	public void setShowModalCopy(final boolean showModalCopy) {
		this.showModalCopy = showModalCopy;
	}

	/**
	 * @return the indicadorEstagioClone
	 */
	public String getIndicadorEstagioClone() {
		return indicadorEstagioClone;
	}

	/**
	 * @param indicadorEstagioClone
	 *            the indicadorEstagioClone to set
	 */
	public void setIndicadorEstagioClone(final String indicadorEstagioClone) {
		this.indicadorEstagioClone = indicadorEstagioClone;
	}

	/** Lista com os fatores UR mes. */
	private List<FatorUrMes> fatorUrMesList = new ArrayList<FatorUrMes>();

	/**
	 * @return the perfilVendidoListAlocClone
	 */
	public List<String> getPerfilVendidoListAlocClone() {
		return perfilVendidoListAlocClone;
	}

	/**
	 * @param perfilVendidoListAlocClone
	 *            the perfilVendidoListAlocClone to set
	 */
	public void setPerfilVendidoListAlocClone(
			final List<String> perfilVendidoListAlocClone) {
		this.perfilVendidoListAlocClone = perfilVendidoListAlocClone;
	}

	/**
	 * @return the perfilVendidoMapAlocClone
	 */
	public Map<String, Long> getPerfilVendidoMapAlocClone() {
		return perfilVendidoMapAlocClone;
	}

	/**
	 * @param perfilVendidoMapAlocClone
	 *            the perfilVendidoMapAlocClone to set
	 */
	public void setPerfilVendidoMapAlocClone(
			final Map<String, Long> perfilVendidoMapAlocClone) {
		this.perfilVendidoMapAlocClone = perfilVendidoMapAlocClone;
	}

	/**
	 * @return the startMonthAlocClone
	 */
	public String getStartMonthAlocClone() {
		return startMonthAlocClone;
	}

	/**
	 * @param startMonthAlocClone
	 *            the startMonthAlocClone to set
	 */
	public void setStartMonthAlocClone(final String startMonthAlocClone) {
		this.startMonthAlocClone = startMonthAlocClone;
	}

	/**
	 * @return the startYearAlocClone
	 */
	public String getStartYearAlocClone() {
		return startYearAlocClone;
	}

	/**
	 * @param startYearAlocClone
	 *            the startYearAlocClone to set
	 */
	public void setStartYearAlocClone(final String startYearAlocClone) {
		this.startYearAlocClone = startYearAlocClone;
	}

	/**
	 * @return the perfilVendidoAlocClone
	 */
	public PerfilVendido getPerfilVendidoAlocClone() {
		return perfilVendidoAlocClone;
	}

	/**
	 * @param perfilVendidoAlocClone
	 *            the perfilVendidoAlocClone to set
	 */
	public void setPerfilVendidoAlocClone(
			final PerfilVendido perfilVendidoAlocClone) {
		this.perfilVendidoAlocClone = perfilVendidoAlocClone;
	}

	/**
	 * @return the nomeEtiquetaAdd
	 */
	public String getNomeEtiquetaAdd() {
		return nomeEtiquetaAdd;
	}

	/**
	 * @param nomeEtiquetaAdd
	 *            the nomeEtiquetaAdd to set
	 */
	public void setNomeEtiquetaAdd(final String nomeEtiquetaAdd) {
		this.nomeEtiquetaAdd = nomeEtiquetaAdd;
	}

	/**
	 * @return the nomeEtiquetaSelected
	 */
	public String getNomeEtiquetaSelected() {
		return nomeEtiquetaSelected;
	}

	/**
	 * @param nomeEtiquetaSelected
	 *            the nomeEtiquetaSelected to set
	 */
	public void setNomeEtiquetaSelected(final String nomeEtiquetaSelected) {
		this.nomeEtiquetaSelected = nomeEtiquetaSelected;
	}

	/**
	 * @return the etiquetaList
	 */
	public List<String> getEtiquetaList() {
		return etiquetaList;
	}

	/**
	 * @param etiquetaList
	 *            the etiquetaList to set
	 */
	public void setEtiquetaList(final List<String> etiquetaList) {
		this.etiquetaList = etiquetaList;
	}

	/**
	 * @return the etiquetaMap
	 */
	public Map<String, Long> getEtiquetaMap() {
		return etiquetaMap;
	}

	/**
	 * @param etiquetaMap
	 *            the etiquetaMap to set
	 */
	public void setEtiquetaMap(final Map<String, Long> etiquetaMap) {
		this.etiquetaMap = etiquetaMap;
	}

	/**
	 * @return o tamanho da lista de datas
	 */
	public int getValidityDateListSize() {
		return this.validityDateList.size();
	}

	/**
	 * Tamanho do mapa.
	 * 
	 * @return retorna oa tamanho no formato de string
	 */
	public String getMapaHeightValue() {
		String retValue = "auto";

		if (alocacaoRowList != null && alocacaoRowList.size() > 15) {
			retValue = "400px";
		}

		return retValue;
	}

	/**
	 * @return the updateUr
	 */
	public BigDecimal getUpdateUr() {
		return updateUr;
	}

	/**
	 * @param updateUr
	 *            the updateUr to set
	 */
	public void setUpdateUr(final BigDecimal updateUr) {
		this.updateUr = updateUr;
	}

	/**
	 * @return the updateFte
	 */
	public BigDecimal getUpdateFte() {
		return updateFte;
	}

	/**
	 * @param updateFte
	 *            the updateFte to set
	 */
	public void setUpdateFte(final BigDecimal updateFte) {
		this.updateFte = updateFte;
	}

	/**
	 * @return the totalAlocado
	 */
	public BigDecimal getTotalAlocado() {
		return totalAlocado;
	}

	/**
	 * @param totalAlocado
	 *            the totalAlocado to set
	 */
	public void setTotalAlocado(final BigDecimal totalAlocado) {
		this.totalAlocado = totalAlocado;
	}

	/**
	 * @return the totalReceitado
	 */
	public BigDecimal getTotalReceitado() {
		return totalReceitado;
	}

	/**
	 * @param totalReceitado
	 *            the totalReceitado to set
	 */
	public void setTotalReceitado(final BigDecimal totalReceitado) {
		this.totalReceitado = totalReceitado;
	}

	/**
	 * @return the totalDifference
	 */
	public BigDecimal getTotalDifference() {
		return totalDifference;
	}

	/**
	 * @param totalDifference
	 *            the totalDifference to set
	 */
	public void setTotalDifference(final BigDecimal totalDifference) {
		this.totalDifference = totalDifference;
	}

	/**
	 * @return the totalValuesList
	 */
	public List<BigDecimal> getTotalValuesList() {
		return totalValuesList;
	}

	/**
	 * @param totalValuesList
	 *            the totalValuesList to set
	 */
	public void setTotalValuesList(final List<BigDecimal> totalValuesList) {
		this.totalValuesList = totalValuesList;
	}

	/**
	 * @return the totalUr
	 */
	public BigDecimal getTotalUr() {
		return totalUr;
	}

	/**
	 * @param totalUr
	 *            the totalUr to set
	 */
	public void setTotalUr(final BigDecimal totalUr) {
		this.totalUr = totalUr;
	}

	/**
	 * @return the isAlocacaoListButtonsEnabled
	 */
	public Boolean getIsAlocacaoListButtonsEnabled() {
		return isAlocacaoListButtonsEnabled;
	}

	/**
	 * @param isAlocacaoListButtonsEnabled
	 *            the isAlocacaoListButtonsEnabled to set
	 */
	public void setIsAlocacaoListButtonsEnabled(
			final Boolean isAlocacaoListButtonsEnabled) {
		this.isAlocacaoListButtonsEnabled = isAlocacaoListButtonsEnabled;
	}

	/**
	 * @return the disabledValidityFieldsBeg
	 */
	public boolean isDisabledValidityFieldsBeg() {
		return disabledValidityFieldsBeg;
	}

	/**
	 * @param disabledValidityFieldsBeg
	 *            the disabledValidityFieldsBeg to set
	 */
	public void setDisabledValidityFieldsBeg(
			final boolean disabledValidityFieldsBeg) {
		this.disabledValidityFieldsBeg = disabledValidityFieldsBeg;
	}

	/**
	 * @return the disabledValidityFieldsEnd
	 */
	public boolean isDisabledValidityFieldsEnd() {
		return disabledValidityFieldsEnd;
	}

	/**
	 * @param disabledValidityFieldsEnd
	 *            the disabledValidityFieldsEnd to set
	 */
	public void setDisabledValidityFieldsEnd(
			final boolean disabledValidityFieldsEnd) {
		this.disabledValidityFieldsEnd = disabledValidityFieldsEnd;
	}

	/**
	 * @return the isRemove
	 */
	public Boolean getIsRemove() {
		return isRemove;
	}

	/**
	 * @param isRemove
	 *            the isRemove to set
	 */
	public void setIsRemove(final Boolean isRemove) {
		this.isRemove = isRemove;
	}

	/**
	 * @return the isUpdateReviewUr
	 */
	public Boolean getIsUpdateReviewUr() {
		return isUpdateReviewUr;
	}

	/**
	 * @param isUpdateReviewUr
	 *            the isUpdateReviewUr to set
	 */
	public void setIsUpdateReviewUr(final Boolean isUpdateReviewUr) {
		this.isUpdateReviewUr = isUpdateReviewUr;
	}

	/**
	 * @return the isViewReviewUr
	 */
	public Boolean getIsViewReviewUr() {
		return isViewReviewUr;
	}

	/**
	 * @param isViewReviewUr
	 *            the isViewReviewUr to set
	 */
	public void setIsViewReviewUr(final Boolean isViewReviewUr) {
		this.isViewReviewUr = isViewReviewUr;
	}

	/**
	 * @return the toAlocacao
	 */
	public Alocacao getToAlocacao() {
		return toAlocacao;
	}

	/**
	 * @param toAlocacao
	 *            the toAlocacao to set
	 */
	public void setToAlocacao(final Alocacao toAlocacao) {
		this.toAlocacao = toAlocacao;
	}

	/**
	 * @return the isUpdate
	 */
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate
	 *            the isUpdate to set
	 */
	public void setIsUpdate(final Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	/**
	 * @return the alocacaoRowList
	 */
	public List<AlocacaoRow> getAlocacaoRowList() {
		return alocacaoRowList;
	}

	/**
	 * @param alocacaoRowList
	 *            the alocacaoRowList to set
	 */
	public void setAlocacaoRowList(final List<AlocacaoRow> alocacaoRowList) {
		this.alocacaoRowList = alocacaoRowList;
	}

	/**
	 * @return the validityDateList
	 */
	public List<Date> getValidityDateList() {
		return validityDateList;
	}

	/**
	 * @param validityDateList
	 *            the validityDateList to set
	 */
	public void setValidityDateList(final List<Date> validityDateList) {
		this.validityDateList = validityDateList;
	}

	/**
	 * @return the currentRowId
	 */
	public Long getCurrentRowId() {
		return currentRowId;
	}

	/**
	 * @param currentRowId
	 *            the currentRowId to set
	 */
	public void setCurrentRowId(final Long currentRowId) {
		this.currentRowId = currentRowId;
	}

	/**
	 * Seta o TO.
	 * 
	 * @param to
	 *            the to to set
	 */
	public void setTo(final MapaAlocacao to) {

		this.to = to;
	}

	/**
	 * 
	 * @return retorna o TO
	 */
	public MapaAlocacao getTo() {
		// Verifica se o ContratoPratica é nulo,
		// se verdade instancia um novo ContratoPratica
		// Isto é necessario na criacao de uma entidade, pois o a referencia da
		// ContratoPratica não pode ser nulo
		if (to != null && to.getContratoPratica() == null) {
			to.setContratoPratica(new ContratoPratica());
		}
		return to;
	}

	/**
	 * Seta o filtro do TO.
	 * 
	 * @param filter
	 *            the filter to set
	 */
	public void setFilter(final MapaAlocacao filter) {
		this.filter = filter;
	}

	/**
	 * 
	 * @return o filtro do TO
	 */
	public MapaAlocacao getFilter() {
		if (filter != null && filter.getContratoPratica() == null) {
			filter.setContratoPratica(new ContratoPratica());
		}
		return filter;
	}

	/**
	 * Seta a lista de resultado.
	 * 
	 * @param resultList
	 *            - lista de resultados
	 */
	public void setResultList(final List<MapaAlocacao> resultList) {
		this.resultList = resultList;
	}

	/**
	 * 
	 * @return a lista de resultados de TO
	 */
	public List<MapaAlocacao> getResultList() {
		return resultList;
	}

	/**
	 * Seta a Lista de ContratoPratica, para o combobox.
	 * 
	 * @param contratoPraticaList
	 *            - lista de ContratoPratica
	 */
	public void setContratoPraticaList(final List<String> contratoPraticaList) {
		this.contratoPraticaList = contratoPraticaList;
	}

	/**
	 * 
	 * @return a lista de ItemEstratificacaoUr
	 */
	public List<ItemEstratificacaoUr> getItensExtratificacaoUr() {
		return itensExtratificacaoUr;
	}

	/**
	 * Seta a Lista de ItemEstratificacaoUr, para a dataTable.
	 * 
	 * @param itensExtratificacaoUr
	 *            - lista de ItemEstratificacaoUr
	 */
	public void setItensExtratificacaoUr(
			final List<ItemEstratificacaoUr> itensExtratificacaoUr) {
		this.itensExtratificacaoUr = itensExtratificacaoUr;
	}

	/**
	 * 
	 * @return a lista de ExplicacaoDesvio
	 */
	public List<String> getExplicacoesDesvio() {
		return explicacoesDesvio;
	}

	/**
	 * Seta a Lista de ExplicacaoDesvio, para o combobox.
	 * 
	 * @param explicacoesDesvio
	 *            - lista de ExplicacaoDesvio
	 */
	public void setExplicacoesDesvio(final List<String> explicacoesDesvio) {
		this.explicacoesDesvio = explicacoesDesvio;
	}

	/**
	 * 
	 * @return a lista de ContratoPratica, para o combobox
	 */
	public List<String> getContratoPraticaList() {
		return contratoPraticaList;
	}

	/**
	 * Seta a Map de ContratoPratica, utilizado no combobox.
	 * 
	 * @param contratoPraticaMap
	 *            - mapa de ContratoPratica
	 */
	public void setContratoPraticaMap(final Map<String, Long> contratoPraticaMap) {
		this.contratoPraticaMap = contratoPraticaMap;
	}

	/**
	 * @return a Map de ContratoPratica, para o combobox
	 */
	public Map<String, Long> getContratoPraticaMap() {
		return contratoPraticaMap;
	}

	/**
	 * Seta a pagina corrente da tabela de resultados.
	 * 
	 * @param currentPageId
	 *            - id da pagina corrente (paginação)
	 */
	public void setCurrentPageId(final Integer currentPageId) {
		this.currentPageId = currentPageId;
	}

	/**
	 * @return a página corrente da tabela de resultados.
	 */
	public Integer getCurrentPageId() {
		return currentPageId;
	}

	/**
	 * @return the validityMonthList
	 */
	public List<String> getValidityMonthList() {
		return validityMonthList;
	}

	/**
	 * @param validityMonthList
	 *            the validityMonthList to set
	 */
	public void setValidityMonthList(final List<String> validityMonthList) {
		this.validityMonthList = validityMonthList;
	}

	/**
	 * @return the validityMonthBeg
	 */
	public String getStartMonth() {

		if (this.getStartDateWindow() == null) {
			return "";
		}

		return DateUtil.getMonthString(this.getStartDateWindow());
	}

	/**
	 * @param validityMonthBeg
	 *            the validityMonthBeg to set
	 */
	public void setValidityMonthBeg(final String validityMonthBeg) {
		this.validityMonthBeg = validityMonthBeg;
	}

	/**
	 * @return the validityMonthEnd
	 */
	public String getEndMonth() {

		if (this.getEndDateWindow() == null) {
			return "";
		}

		return DateUtil.getMonthString(this.getEndDateWindow());
	}

	/**
	 * @param validityMonthEnd
	 *            the validityMonthEnd to set
	 */
	public void setValidityMonthEnd(final String validityMonthEnd) {
		this.validityMonthEnd = validityMonthEnd;
	}

	/**
	 * @return the validityYearBeg
	 */
	public String getStartYear() {

		if (this.getStartDateWindow() == null) {
			return "";
		}

		return "" + DateUtil.getYear(this.getStartDateWindow());
	}

	/**
	 * @param validityYearBeg
	 *            the validityYearBeg to set
	 */
	public void setValidityYearBeg(final String validityYearBeg) {
		this.validityYearBeg = validityYearBeg;
	}

	/**
	 * @return the validityYearEnd
	 */
	public String getEndYear() {

		if (this.getEndDateWindow() == null) {
			return "";
		}

		return "" + DateUtil.getYear(this.getEndDateWindow());
	}

	/**
	 * @param validityYearEnd
	 *            the validityYearEnd to set
	 */
	public void setValidityYearEnd(final String validityYearEnd) {
		this.validityYearEnd = validityYearEnd;
	}

	/**
	 * Reseta o backingBean.
	 */
	public void reset() {
		resetTo();
		resetFilter();
		resetResultList();
		resetVigencia();
		resetValidityDateList();
		resetAlocacaoRowList();
		resetTotalValues();
		resetDisabledValidityFields();
		resetEtiquetaFileds();
		resetAlocCloneFields();
		resetMapFollow();
		resetItensEstratificacaoUr();
		this.showModalSaveAs = Boolean.valueOf(false);
		this.isAlocacaoListButtonsEnabled = Boolean.valueOf(false);
		this.isUpdateReviewUr = Boolean.valueOf(false);
		this.isFollowingOnly = Boolean.valueOf(false);
		this.closingDateRevenue = null;
	}

	/**
	 * Reseta os controladores dos combos da vigencia.
	 */
	public void resetDisabledValidityFields() {
		this.disabledValidityFieldsBeg = false;
		this.disabledValidityFieldsEnd = false;
	}

	/**
	 * Reseta a data de vigencia.
	 */
	public void resetVigencia() {
		this.validityMonthBeg = "";
		this.validityMonthEnd = "";
		this.validityYearBeg = "";
		this.validityYearEnd = "";
	}

	/**
	 * Reseta o startData do clone da Alocacao.
	 */
	public void resetAlocCloneFields() {
		this.startMonthAlocClone = "";
		this.startYearAlocClone = "";
		this.perfilVendidoAlocClone = new PerfilVendido();
		this.indicadorEstagioClone = null;
		this.showModalCopy = true;
	}

	/**
	 * Reseta o to.
	 */
	public void resetTo() {
		this.to = new MapaAlocacao();
	}

	/**
	 * Reseta o filter.
	 */
	public void resetFilter() {
		this.cliente = new Cliente();
		this.msa = new Msa();
		this.centroLucro = new CentroLucro();
		this.natureza = new NaturezaCentroLucro();

		this.filter = new MapaAlocacao();
	}

	/**
	 * Reseta a lista de to.
	 */
	public void resetResultList() {
		this.resultList = new ArrayList<MapaAlocacao>();
	}

	/**
	 * Reseta a lista de itens da estratificacao de ur.
	 */
	public void resetItensEstratificacaoUr() {
		this.itensExtratificacaoUr = new ArrayList<ItemEstratificacaoUr>();
	}

	/**
	 * Reseta a lista de datas da vigencia do mapa de alocacao.
	 */
	public void resetValidityDateList() {
		this.validityDateList = new ArrayList<Date>();
	}

	/**
	 * Reseta a lista de AlocacaoRow - matriz de Alocacao X AlocacaoPeriodo.
	 */
	public void resetAlocacaoRowList() {
		this.alocacaoRowList = new ArrayList<AlocacaoRow>();
	}

	/**
	 * Reseta os valures dos campos da Etiqueta (tag).
	 */
	public void resetEtiquetaFileds() {
		this.nomeEtiquetaAdd = "";
		this.nomeEtiquetaSelected = "";
	}

	/**
	 * Reseta os valures totais.
	 */
	public void resetTotalValues() {
		this.totalUr = BigDecimal.valueOf(0);
		this.totalValuesList = new ArrayList<BigDecimal>();
	}

	/**
	 * Reseta o Map de relacao de MapaAlocacao que estão sendo seguidos.
	 */
	public void resetMapFollow() {
		this.mapFollow = new HashMap<Long, Long>();
	}

	/**
	 * Atualiza os valores dos totais.
	 * 
	 * @param totalUr
	 *            - total de Utilization Rate
	 * @param totalValuesList
	 *            - lista de totais
	 */
	public void updateTotalValues(final double totalUr,
			final List<BigDecimal> totalValuesList) {
		this.totalUr = BigDecimal.valueOf(totalUr);
		this.totalValuesList = totalValuesList;
	}

	/**
	 * @return lista de anos da vigencia
	 */
	public List<String> getValidityYearList() {

		int yearBegin = Integer.parseInt(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
		int range = Integer.parseInt(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

		List<String> yearList = new ArrayList<String>();

		for (int i = yearBegin; i <= yearBegin + range; i++) {
			yearList.add("" + i);
		}

		validityYearList = yearList;

		return validityYearList;
	}

	/**
	 * @param showModalSaveAs
	 *            the showModalSaveAs to set
	 */
	public void setShowModalSaveAs(final Boolean showModalSaveAs) {
		this.showModalSaveAs = showModalSaveAs;
	}

	/**
	 * @return the showModalSaveAs
	 */
	public Boolean getShowModalSaveAs() {
		return showModalSaveAs;
	}

	/**
	 * @param mapaAlocacaoVersion
	 *            the mapaAlocacaoVersion to set
	 */
	public void setMapaAlocacaoVersion(final String mapaAlocacaoVersion) {
		this.mapaAlocacaoVersion = mapaAlocacaoVersion;
	}

	/**
	 * @return the mapaAlocacaoVersion
	 */
	public String getMapaAlocacaoVersion() {
		return mapaAlocacaoVersion;
	}

	/**
	 * @return the explicacaoDesvioDefault
	 */
	public ExplicacaoDesvio getExplicacaoDesvioDefault() {
		return explicacaoDesvioDefault;
	}

	/**
	 * @param explicacaoDesvioDefault
	 *            the explicacaoDesvioDefault to set
	 */
	public void setExplicacaoDesvioDefault(
			final ExplicacaoDesvio explicacaoDesvioDefault) {
		this.explicacaoDesvioDefault = explicacaoDesvioDefault;
	}

	/**
	 * @return the explicacaoDesvioApply
	 */
	public String getExplicacaoDesvioApply() {
		return explicacaoDesvioApply;
	}

	/**
	 * @param explicacaoDesvioApply
	 *            the explicacaoDesvioApply to set
	 */
	public void setExplicacaoDesvioApply(final String explicacaoDesvioApply) {
		this.explicacaoDesvioApply = explicacaoDesvioApply;
	}

	/**
	 * @param isPessoa
	 *            the isPessoa to set
	 */
	public void setIsPessoa(final Boolean isPessoa) {
		this.isPessoa = isPessoa;
	}

	/**
	 * @return the isPessoa
	 */
	public Boolean getIsPessoa() {
		return isPessoa;
	}

	/**
	 * @param alocacaoRow
	 *            the alocacaoRow to set
	 */
	public void setAlocacaoRow(final AlocacaoRow alocacaoRow) {
		this.alocacaoRow = alocacaoRow;
	}

	/**
	 * @return the alocacaoRow
	 */
	public AlocacaoRow getAlocacaoRow() {
		return alocacaoRow;
	}

	/**
	 * @param isLocked
	 *            the isLocked to set
	 */
	public void setIsLocked(final Boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * @return the isLocked
	 */
	public Boolean getIsLocked() {
		return isLocked;
	}

	/**
	 * @return the isFollowed
	 */
	public Boolean getIsFollowed() {
		return isFollowed;
	}

	/**
	 * @param isFollowed
	 *            the isFollowed to set
	 */
	public void setIsFollowed(final Boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	/**
	 * @return the vwPmsFinancialsList
	 */
	public List<VwPmsFinancials> getVwPmsFinancialsList() {
		return vwPmsFinancialsList;
	}

	/**
	 * @param vwPmsFinancialsList
	 *            the vwPmsFinancialsList to set
	 */
	public void setVwPmsFinancialsList(
			final List<VwPmsFinancials> vwPmsFinancialsList) {
		this.vwPmsFinancialsList = vwPmsFinancialsList;
	}

	/**
	 * @param mapDashboard
	 *            the mapDashboard to set
	 */
	public void setMapDashboard(
			final SortedMap<Date, MapDashboardRow> mapDashboard) {
		this.mapDashboard = mapDashboard;
	}

	/**
	 * @return the mapDashboard
	 */
	public SortedMap<Date, MapDashboardRow> getMapDashboard() {
		return mapDashboard;
	}

	/**
	 * Retorna a lista de chaves do MapDashboard.
	 * 
	 * @return lista de chaves.
	 */
	public List<Date> getMapDashboardItemKeys() {
		return new ArrayList<Date>(mapDashboard.keySet());
	}

	/**
	 * Retorna uma lista com o nome das colunas de moeda.
	 * 
	 * @return lista de colunas
	 */
	public List<String> getColunasMapDashboard() {

		List<String> colunas = new ArrayList<String>();

		for (String moeda : mapDashboard.get(mapDashboard.firstKey())
				.getMapDashboardMoedaMap().keySet()) {

			String coluna = moeda
					+ " "
					+ BundleUtil
							.getBundle(Constants.BUNDLE_MAP_DASHBOARD_REVENUE);

			colunas.add(coluna);

			String sigaleMoeda = mapDashboard.get(mapDashboard.firstKey())
					.getMapDashboardMoedaMap().get(moeda).getMoeda()
					.getSiglaMoeda();
			this.siglaMoedaMap.put(coluna, sigaleMoeda);

		}
		Collections.sort(colunas);
		return colunas;
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
		return patternCurrency;
	}

	/**
	 * @param redirectPage
	 *            the redirectPage to set
	 */
	public void setRedirectPage(final String redirectPage) {
		this.redirectPage = redirectPage;
	}

	/**
	 * @return the redirectPage
	 */
	public String getRedirectPage() {
		return redirectPage;
	}

	/**
	 * @param isSaveAs
	 *            the isSaveAs to set
	 */
	public void setIsSaveAs(final Boolean isSaveAs) {
		this.isSaveAs = isSaveAs;
	}

	/**
	 * @return the isSaveAs
	 */
	public Boolean getIsSaveAs() {
		return isSaveAs;
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
	 * @return the msaList
	 */
	public List<String> getMsaList() {
		return msaList;
	}

	/**
	 * @param msaList
	 *            the msaList to set
	 */
	public void setMsaList(final List<String> msaList) {
		this.msaList = msaList;
	}

	/**
	 * @return the explicacoesDesvioMap
	 */
	public Map<String, Long> getExplicacoesDesvioMap() {
		return explicacoesDesvioMap;
	}

	/**
	 * @param explicacoesDesvioMap
	 *            the explicacoesDesvioMap to set
	 */
	public void setExplicacoesDesvioMap(
			final Map<String, Long> explicacoesDesvioMap) {
		this.explicacoesDesvioMap = explicacoesDesvioMap;
	}

	/**
	 * @return the msaMap
	 */
	public Map<String, Long> getMsaMap() {
		return msaMap;
	}

	/**
	 * @param msaMap
	 *            the msaMap to set
	 */
	public void setMsaMap(final Map<String, Long> msaMap) {
		this.msaMap = msaMap;
	}

	/**
	 * @return the clienteList
	 */
	public List<String> getClienteList() {
		return clienteList;
	}

	/**
	 * @param clienteList
	 *            the clienteList to set
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
	 * @param clienteMap
	 *            the clienteMap to set
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
	 * @param naturezaList
	 *            the naturezaList to set
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
	 * @param naturezaMap
	 *            the naturezaMap to set
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
	 * @param centroLucroList
	 *            the centroLucroList to set
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
	 * @param centroLucroMap
	 *            the centroLucroMap to set
	 */
	public void setCentroLucroMap(final Map<String, Long> centroLucroMap) {
		this.centroLucroMap = centroLucroMap;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente
	 *            the cliente to set
	 */
	public void setCliente(final Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the natureza
	 */
	public NaturezaCentroLucro getNatureza() {
		return natureza;
	}

	/**
	 * @param natureza
	 *            the natureza to set
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
	 * @param centroLucro
	 *            the centroLucro to set
	 */
	public void setCentroLucro(final CentroLucro centroLucro) {
		this.centroLucro = centroLucro;
	}

	/**
	 * @return the msa
	 */
	public Msa getMsa() {
		return msa;
	}

	/**
	 * @param msa
	 *            the msa to set
	 */
	public void setMsa(final Msa msa) {
		this.msa = msa;
	}

	/**
	 * @param ascending
	 *            the ascending to set
	 */
	public void setAscending(final boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * @return the ascending
	 */
	public boolean isAscending() {
		return ascending;
	}

	/**
	 * @param fatorUrMesList
	 *            the fatorUrMesList to set
	 */
	public void setFatorUrMesList(final List<FatorUrMes> fatorUrMesList) {
		this.fatorUrMesList = fatorUrMesList;
	}

	/**
	 * @return the fatorUrMesList
	 */
	public List<FatorUrMes> getFatorUrMesList() {
		return fatorUrMesList;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDateWindow(final Date startDate) {
		this.startDateWindow = startDate;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDateWindow() {
		return startDateWindow;
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public void setEndDateWindow(final Date endDate) {
		this.endDateWindow = endDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDateWindow() {

		return endDateWindow;
	}

	/**
	 * @return the validityMonthBeg
	 */
	public String getValidityMonthBeg() {
		return validityMonthBeg;
	}

	/**
	 * @return the validityMonthEnd
	 */
	public String getValidityMonthEnd() {
		return validityMonthEnd;
	}

	/**
	 * @return the validityYearBeg
	 */
	public String getValidityYearBeg() {
		return validityYearBeg;
	}

	/**
	 * @return the validityYearEnd
	 */
	public String getValidityYearEnd() {
		return validityYearEnd;
	}

	/**
	 * @param moveWindowToFront
	 *            the moveWindowToFront to set
	 */
	public void setMoveWindowToFront(final Boolean moveWindowToFront) {
		this.moveWindowToFront = moveWindowToFront;
	}

	/**
	 * @return the moveWindowToFront
	 */
	public Boolean getMoveWindowToFront() {
		return moveWindowToFront;
	}

	/**
	 * @param numberMonthsToMove
	 *            the numberMonthsToMove to set
	 */
	public void setNumberMonthsToMove(final Integer numberMonthsToMove) {
		this.numberMonthsToMove = numberMonthsToMove;
	}

	/**
	 * @return the numberMonthsToMove
	 */
	public Integer getNumberMonthsToMove() {
		return numberMonthsToMove;
	}

	/**
	 * @param alocacaoPeriodo
	 *            the alocacaoPeriodo to set
	 */
	public void setAlocacaoPeriodo(final AlocacaoPeriodo alocacaoPeriodo) {
		this.alocacaoPeriodo = alocacaoPeriodo;
	}

	/**
	 * @return the alocacaoPeriodo
	 */
	public AlocacaoPeriodo getAlocacaoPeriodo() {
		return alocacaoPeriodo;
	}

	/**
	 * @param fatorUr
	 *            the fatorUr to set
	 */
	public void setFatorUr(final FatorUrMes fatorUr) {
		this.fatorUr = fatorUr;
	}

	/**
	 * @return the fatorUr
	 */
	public FatorUrMes getFatorUr() {
		return fatorUr;
	}

	/**
	 * @return the closingDateRevenue
	 */
	public Date getClosingDateRevenue() {
		return closingDateRevenue;
	}

	/**
	 * @param closingDateRevenue
	 *            the closingDateRevenue to set
	 */
	public void setClosingDateRevenue(final Date closingDateRevenue) {
		this.closingDateRevenue = closingDateRevenue;
	}

	/**
	 * @return the Map
	 */
	public Map<Moeda, Double> getTotalCurrencyFinancialMap() {
		return totalCurrencyFinancialMap;
	}

	/**
	 * @param totalCurrencyFinancialMap
	 *            the Map
	 */
	public void setTotalCurrencyFinancialMap(
			final Map<Moeda, Double> totalCurrencyFinancialMap) {
		this.totalCurrencyFinancialMap = totalCurrencyFinancialMap;
	}

	/**
	 * @return the Map
	 */
	public Map<String, String> getSiglaMoedaMap() {
		return siglaMoedaMap;
	}

	/**
	 * @param siglaMoedaMap
	 */
	public void setSiglaMoedaMap(final Map<String, String> siglaMoedaMap) {
		this.siglaMoedaMap = siglaMoedaMap;
	}

	/**
	 * @return the listResult
	 */
	public List<MapDashboardRow> getListResult() {
		return listResult;
	}

	/**
	 * @param listResult
	 *            the listResult to set
	 */
	public void setListResult(List<MapDashboardRow> listResult) {
		this.listResult = listResult;
	}

	/**
	 * @return the listaMoedas
	 */
	public List<Moeda> getListaMoedas() {
		return listaMoedas;
	}

	/**
	 * @param listaMoedas
	 *            the listaMoedas to set
	 */
	public void setListaMoedas(List<Moeda> listaMoedas) {
		this.listaMoedas = listaMoedas;
	}

}