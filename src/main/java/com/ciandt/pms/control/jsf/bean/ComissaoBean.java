package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.model.Acelerador;
import com.ciandt.pms.model.Comissao;
import com.ciandt.pms.model.ComissaoAcelerador;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.vo.ComissaoRow;

/**
 * Define o BackingBean da entidade.
 * 
 * @since 31/09/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ComissaoBean implements Serializable {

	/** Serial Version. */
	private static final long serialVersionUID = 1L;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties appConfig;

	/** Moeda service. */
	@Autowired
	private IMoedaService moedaService;

	/** ComissaoAcelerador do backingBean. */
	private ComissaoAcelerador comissaoAcelerador = new ComissaoAcelerador();

	/** filtro ComissaoAcelerador do backingBean. */
	private ComissaoAcelerador comissaoAceleradorFilter = new ComissaoAcelerador();

	/** ComissaoFatura do backingBean. */
	private ComissaoFatura comissaoFatura = new ComissaoFatura();

	/** Comissao do backingBean. */
	private Comissao comissao = new Comissao();

	/** lista de resultados da pesquisa. */
	private List<String> clienteList = null;

	/** Lista para o combobox com os Cliente pais. */
	private Map<String, Long> clienteMap = new HashMap<String, Long>();

	/** lista de resultados da pesquisa. */
	private List<String> aceleradorList = null;

	/** Lista para o combobox com os Acelerador. */
	private Map<String, Long> aceleradorMap = new HashMap<String, Long>();

	/** lista de resultados da pesquisa. */
	private List<String> contratoPraticaList = null;

	/** Lista para o combobox com os Cliente pais. */
	private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as Moeda. */
	private List<String> moedaList = new ArrayList<String>();

	/** Lista para o combobox com as Moeda. */
	private Map<String, Long> moedaMap = new HashMap<String, Long>();

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageAcelerador = Integer.valueOf(0);

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageInvoice = Integer.valueOf(0);

	/** Id da pagina corrente na lista de pesquisa. */
	private Integer currentPageId = Integer.valueOf(0);

	/**
	 * Utlizado no campo de cliente, na tela de criação de comissao acelerador.
	 */
	private String nomeCliente;

	/**
	 * Utlizado no campo de cliente, na tela de update de comissao acelerador.
	 */
	private String nomeCli;

	/** Moeda do ContratoPratica. */
	private Moeda moeda = new Moeda();

	/** data inicio. */
	private Date dataBeg = null;

	/** data fim. */
	private Date dataEnd = null;

	/** Linha selecionada. */
	private int rowIndex;

	/** Indicador para modo de exclusão. */
	private Boolean isDelete = Boolean.FALSE;

	/** Estado Selecionado. */
	private String selectedStatus = null;

	/** Comentario. */
	private String comments = null;

	/** Lista de ComissaoAcelerador. */
	private List<ComissaoAcelerador> comissaoAceleradorList = new ArrayList<ComissaoAcelerador>();

	/** ComissaoRow list. */
	private List<ComissaoRow> comissaoRowList = new ArrayList<ComissaoRow>();

	/** ComissaoRow list. */
	private List<ComissaoRow> comissaoFaturaRowList = new ArrayList<ComissaoRow>();

	/** Linha selecionada. */
	private ComissaoRow selectedRow;

	/** Login do AE. */
	private String loginAe;

	/** Login do AE filter. */
	private String loginAeFilter;

	/** Login do DN filter. */
	private String loginDnFilter;

	/** Total comissão. */
	private BigDecimal total;

	/** Lista para a busca. */
	private List<String> listaPessoas = new ArrayList<String>();

	/** Mapa para a busca. */
	private Map<String, String> mapPessoas = new HashMap<String, String>();

	/** Nome do Msa para o filtro de search. */
	private String nameMsa;

	/** Mapa de MSA para combobox. */
	private Map<String, Long> mapMsa = new HashMap<String, Long>();

	/** Lista de MSA para combobox. */
	private List<String> listaMsa = new ArrayList<String>();

	/** Codigo da Fatura para o filtro de search. */
	private Long codeInvoice;

	/** Valor total do Net Value. */
	private BigDecimal totalNetValue = BigDecimal.ZERO;

	/** Valor total do Converted Comission Value Commission (BRL). */
	private BigDecimal totalConvertedComissionValue = BigDecimal.ZERO;

	/**
	 * Pega a moeda default.
	 * 
	 * @return retorna a moeda default
	 */
	public Moeda getDefaultCurrency() {
		Long defaultCurrencyId = Long.parseLong((String) appConfig
				.get(Constants.DEFAULT_PROPERTY_CURRENCY_CODE));

		return moedaService.findMoedaById(defaultCurrencyId);
	}

	/**
	 * Reseta o bean.
	 */
	public void reset() {
		resetTo();
		resetFilter();
		moeda = new Moeda();
		nomeCliente = null;
		total = null;
	}

	/**
	 * reseta o filtro.
	 */
	public void resetFilter() {
		comissaoAceleradorFilter = new ComissaoAcelerador();
		comissaoAceleradorList = new ArrayList<ComissaoAcelerador>();
		comissaoFaturaRowList = new ArrayList<ComissaoRow>();
		comissaoRowList = new ArrayList<ComissaoRow>();
		mapMsa = null;
		listaMsa = null;
		nameMsa = "";
		codeInvoice = null;
		comments = null;
		loginAe = null;
		loginAeFilter = null;
		loginDnFilter = null;

	}

	/**
	 * reseta o TO.
	 */
	public void resetTo() {
		comissao = new Comissao();
		comissaoAcelerador = new ComissaoAcelerador();
		comissaoFatura = new ComissaoFatura();
	}

	/**
	 * @return the cAcelerador
	 */
	public ComissaoAcelerador getComissaoAcelerador() {
		if (comissaoAcelerador == null) {
			comissaoAcelerador = new ComissaoAcelerador();
		}
		if (comissaoAcelerador.getAcelerador() == null) {
			comissaoAcelerador.setAcelerador(new Acelerador());
		}
		if (comissaoAcelerador.getComissao() == null) {
			comissaoAcelerador.setComissao(new Comissao());
		}
		if (comissaoAcelerador.getComissao().getContratoPratica() == null) {
			comissaoAcelerador.getComissao().setContratoPratica(
					new ContratoPratica());
		}
		if (comissaoAcelerador.getComissao().getContratoPratica().getMsa() == null) {
			comissaoAcelerador.getComissao().getContratoPratica()
					.setMsa(new Msa());
		}
		if (comissaoAcelerador.getComissao().getMoeda() == null) {
			comissaoAcelerador.getComissao().setMoeda(new Moeda());
		}

		return comissaoAcelerador;
	}

	/**
	 * @param acelerador
	 *            the cAcelerador to set
	 */
	public void setComissaoAcelerador(final ComissaoAcelerador acelerador) {
		comissaoAcelerador = acelerador;
	}

	/**
	 * @return the cFatura
	 */
	public ComissaoFatura getComissaoFatura() {
		return comissaoFatura;
	}

	/**
	 * @param fatura
	 *            the cFatura to set
	 */
	public void setComissaoFatura(final ComissaoFatura fatura) {
		comissaoFatura = fatura;
	}

	/**
	 * @return the comissao
	 */
	public Comissao getComissao() {
		if (comissao == null) {
			comissao = new Comissao();
		}
		if (comissao.getContratoPratica() == null) {
			comissao.setContratoPratica(new ContratoPratica());
		}
		if (comissao.getMoeda() == null) {
			comissao.setMoeda(new Moeda());
		}

		return comissao;
	}

	/**
	 * @param comissao
	 *            the comissao to set
	 */
	public void setComissao(final Comissao comissao) {
		this.comissao = comissao;
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
	 * @return the currentPageId
	 */
	public Integer getCurrentPageAcelerador() {
		return currentPageAcelerador;
	}

	/**
	 * @param currentPageId
	 *            the currentPageId to set
	 */
	public void setCurrentPageAcelerador(final Integer currentPageId) {
		this.currentPageAcelerador = currentPageId;
	}

	/**
	 * @param contratoPraticaList
	 *            the contratoPraticaList to set
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
	 * @param contratoPraticaMap
	 *            the contratoPraticaMap to set
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
	 * @param aceleradorList
	 *            the aceleradorList to set
	 */
	public void setAceleradorList(final List<String> aceleradorList) {
		this.aceleradorList = aceleradorList;
	}

	/**
	 * @return the aceleradorList
	 */
	public List<String> getAceleradorList() {
		return aceleradorList;
	}

	/**
	 * @param aceleradorMap
	 *            the aceleradorMap to set
	 */
	public void setAceleradorMap(final Map<String, Long> aceleradorMap) {
		this.aceleradorMap = aceleradorMap;
	}

	/**
	 * @return the aceleradorMap
	 */
	public Map<String, Long> getAceleradorMap() {
		return aceleradorMap;
	}

	/**
	 * @param nomeCliente
	 *            the nomeCliente to set
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
	 * @param moeda
	 *            the moeda to set
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
	 * @param moedaList
	 *            the moedaList to set
	 */
	public void setMoedaList(final List<String> moedaList) {
		this.moedaList = moedaList;
	}

	/**
	 * @return the moedaList
	 */
	public List<String> getMoedaList() {
		return moedaList;
	}

	/**
	 * @param moedaMap
	 *            the moedaMap to set
	 */
	public void setMoedaMap(final Map<String, Long> moedaMap) {
		this.moedaMap = moedaMap;
	}

	/**
	 * @return the moedaMap
	 */
	public Map<String, Long> getMoedaMap() {
		return moedaMap;
	}

	/**
	 * @param dataBeg
	 *            the dataBeg to set
	 */
	public void setDataBeg(final Date dataBeg) {
		this.dataBeg = dataBeg;
	}

	/**
	 * @return the dataBeg
	 */
	public Date getDataBeg() {
		return dataBeg;
	}

	/**
	 * @param dataEnd
	 *            the dataEnd to set
	 */
	public void setDataEnd(final Date dataEnd) {
		this.dataEnd = dataEnd;
	}

	/**
	 * @return the dataEnd
	 */
	public Date getDataEnd() {
		return dataEnd;
	}

	/**
	 * @param comissaoAceleradorList
	 *            the comissaoAceleradorList to set
	 */
	public void setComissaoAceleradorList(
			final List<ComissaoAcelerador> comissaoAceleradorList) {
		this.comissaoAceleradorList = comissaoAceleradorList;
	}

	/**
	 * @return the comissaoAceleradorList
	 */
	public List<ComissaoAcelerador> getComissaoAceleradorList() {
		return comissaoAceleradorList;
	}

	/**
	 * @param comissaoRowList
	 *            the comissaoRowList to set
	 */
	public void setComissaoRowList(final List<ComissaoRow> comissaoRowList) {
		this.comissaoRowList = comissaoRowList;
	}

	/**
	 * @return the comissaoRowList
	 */
	public List<ComissaoRow> getComissaoRowList() {
		return comissaoRowList;
	}

	/**
	 * @param rowIndex
	 *            the rowIndex to set
	 */
	public void setRowIndex(final int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * @return the rowIndex
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param isDelete
	 *            the isDelete to set
	 */
	public void setIsDelete(final Boolean isDelete) {
		this.isDelete = isDelete;
	}

	/**
	 * @return the isDelete
	 */
	public Boolean getIsDelete() {
		return isDelete;
	}

	/**
	 * @param comissaoAceleradorFilter
	 *            the comissaoAceleradorFilter to set
	 */
	public void setComissaoAceleradorFilter(
			final ComissaoAcelerador comissaoAceleradorFilter) {
		this.comissaoAceleradorFilter = comissaoAceleradorFilter;
	}

	/**
	 * @return the comissaoAceleradorFilter
	 */
	public ComissaoAcelerador getComissaoAceleradorFilter() {
		if (comissaoAceleradorFilter == null) {
			comissaoAceleradorFilter = new ComissaoAcelerador();
		}
		if (comissaoAceleradorFilter.getAcelerador() == null) {
			comissaoAceleradorFilter.setAcelerador(new Acelerador());
		}
		if (comissaoAceleradorFilter.getComissao() == null) {
			comissaoAceleradorFilter.setComissao(new Comissao());
		}
		if (comissaoAceleradorFilter.getComissao().getContratoPratica() == null) {
			comissaoAceleradorFilter.getComissao().setContratoPratica(
					new ContratoPratica());
		}

		return comissaoAceleradorFilter;
	}

	/**
	 * @param selectedStatus
	 *            the selectedStatus to set
	 */
	public void setSelectedStatus(final String selectedStatus) {
		this.selectedStatus = selectedStatus;
	}

	/**
	 * @return the selectedStatus
	 */
	public String getSelectedStatus() {
		return selectedStatus;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(final String comments) {
		this.comments = comments;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comissaoFaturaRowList
	 *            the comissaoFaturaRowList to set
	 */
	public void setComissaoFaturaRowList(
			final List<ComissaoRow> comissaoFaturaRowList) {
		this.comissaoFaturaRowList = comissaoFaturaRowList;
	}

	/**
	 * @return the comissaoFaturaRowList
	 */
	public List<ComissaoRow> getComissaoFaturaRowList() {
		return comissaoFaturaRowList;
	}

	/**
	 * @param selectedRow
	 *            the selectedRow to set
	 */
	public void setSelectedRow(final ComissaoRow selectedRow) {
		this.selectedRow = selectedRow;
	}

	/**
	 * @return the selectedRow
	 */
	public ComissaoRow getSelectedRow() {
		return selectedRow;
	}

	/**
	 * @param nomeCli
	 *            the nomeCli to set
	 */
	public void setNomeCli(final String nomeCli) {
		this.nomeCli = nomeCli;
	}

	/**
	 * @return the nomeCli
	 */
	public String getNomeCli() {
		return nomeCli;
	}

	/**
	 * @param currentPageInvoice
	 *            the currentPageInvoice to set
	 */
	public void setCurrentPageInvoice(final Integer currentPageInvoice) {
		this.currentPageInvoice = currentPageInvoice;
	}

	/**
	 * @return the currentPageInvoice
	 */
	public Integer getCurrentPageInvoice() {
		return currentPageInvoice;
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
	 * @param loginAe
	 *            the loginAe to set
	 */
	public void setLoginAe(final String loginAe) {
		this.loginAe = loginAe;
	}

	/**
	 * @return the loginAe
	 */
	public String getLoginAe() {
		return loginAe;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(final BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param loginAeFilter
	 *            the loginAeFilter to set
	 */
	public void setLoginAeFilter(final String loginAeFilter) {
		this.loginAeFilter = loginAeFilter;
	}

	/**
	 * @return the loginAeFilter
	 */
	public String getLoginAeFilter() {
		return loginAeFilter;
	}

	/**
	 * @param loginDnFilter
	 *            the loginAeFilter to set
	 */
	public void setLoginDnFilter(final String loginDnFilter) {
		this.loginDnFilter = loginDnFilter;
	}

	/**
	 * @return the loginDnFilter
	 */
	public String getLoginDnFilter() {
		return loginDnFilter;
	}

	/**
	 * @return the listaVwPMSPessoas
	 */
	public List<String> getListaPessoas() {
		return listaPessoas;
	}

	/**
	 * @param listaPessoas
	 *            the listaPessoas to set
	 */
	public void setListaPessoas(final List<String> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	/**
	 * @return the mapVwPMSPessoas
	 */
	public Map<String, String> getMapPessoas() {
		return mapPessoas;
	}

	/**
	 * @param mapPessoas
	 *            the mapVwPMSPessoas to set
	 */
	public void setMapPessoas(final Map<String, String> mapPessoas) {
		this.mapPessoas = mapPessoas;
	}

	/**
	 * @return the nameMsa
	 */
	public String getNameMsa() {
		return nameMsa;
	}

	/**
	 * @param nameMsa
	 *            the nameMsa to set
	 */
	public void setNameMsa(final String nameMsa) {
		this.nameMsa = nameMsa;
	}

	/**
	 * @return the codeInvoice
	 */
	public Long getCodeInvoice() {
		return codeInvoice;
	}

	/**
	 * @param codeInvoice
	 *            the codeInvoice to set
	 */
	public void setCodeInvoice(final Long codeInvoice) {
		this.codeInvoice = codeInvoice;
	}

	/**
	 * @return the mapMsa
	 */
	public Map<String, Long> getMapMsa() {
		return mapMsa;
	}

	/**
	 * @param mapMsa
	 *            the mapMsa to set
	 */
	public void setMapMsa(final Map<String, Long> mapMsa) {
		this.mapMsa = mapMsa;
	}

	/**
	 * @return the listaMsa
	 */
	public List<String> getListaMsa() {
		return listaMsa;
	}

	/**
	 * @param listaMsa
	 *            the listaMsa to set
	 */
	public void setListaMsa(final List<String> listaMsa) {
		this.listaMsa = listaMsa;
	}

	/**
	 * Calcula o total de Net Value da lista de {@link ComissaoRow} do bean.
	 * 
	 * @return {@link BigDecimal}
	 * 
	 */
	public BigDecimal getTotalNetValue() {
		this.totalNetValue = BigDecimal.ZERO;
		for (ComissaoRow comissao : this.comissaoFaturaRowList) {
			if (comissao.getComissaoFatura().getValorItemFatura() != null) {
				this.totalNetValue = this.totalNetValue.add(comissao
						.getComissaoFatura().getValorItemFatura());
			}
		}
		return this.totalNetValue;
	}

	/**
	 * Calcula o total de Converted Comission Value (Comissao BLR) da lista de
	 * {@link ComissaoRow} do bean.
	 * 
	 * @return {@link BigDecimal}
	 * 
	 */
	public BigDecimal getTotalConvertedComissionValue() {
		this.totalConvertedComissionValue = BigDecimal.ZERO;
		for (ComissaoRow comissao : this.comissaoFaturaRowList) {
			if (comissao.getConvertedComissionValue() != null) {
				this.totalConvertedComissionValue = this.totalConvertedComissionValue
						.add(comissao.getConvertedComissionValue());
			}
		}
		return this.totalConvertedComissionValue;
	}

}
