package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.*;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.VwMegaCCusto;
import com.ciandt.pms.model.vo.ConvergenciaRow;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.model.vo.GenericFormFilter;
import com.ciandt.pms.model.vo.combo.ComboBox;

/**
 * Define o BackingBean para ativacao de projetos no ERP.
 * 
 * @since 15/08/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class ActiveProjectBean implements Serializable {

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;

	/** to Convergencia. */
	private ConvergenciaRow to = null;

	/**
	 * lista de projetos inativos
	 */
	private List<ConvergenciaRow> inactiveList;
	
	/**
	 * Combo do Empresa
	 */
	private ComboBox<Empresa> empresaCombo;

	/**
	 * Combo do Cliente
	 */
	private ComboBox<Cliente> clienteCombo;

	/**
	 * Combo do Centro de Custo
	 */
	private ComboBox<VwMegaCCusto> costCenterCombo;

	/**
	 * Combo do Contrato Pratico
	 */
	private ComboBox<ContratoPratica> contratoPraticaCombo;

	/**
	 * Status selecionado na combo.
	 */
	private String statusSelected = "";

	/**
	 * Codigo do Projeto Mega
	 */
	private String codigoProjetoMega;

	/**
	 * Flag usada para sinalizar pre-carregamento
	 */
	private boolean update;

	private String nomeProjetoMega;

	private String nomeContratoPratica = null;

	private String nomeCliente = null;

	private String nomeCentroCusto = null;

	private Boolean isUpdate = Boolean.FALSE;

	/** Lista para o combobox com as ContratoPratica. */
	private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as ContratoPratica. */
	private List<String> contratoPraticaList = new ArrayList<String>();

	/** Lista para o combobox com as Cliente. */
	private Map<String, Long> clienteMap = new HashMap<String, Long>();

	/** Lista para o combobox com as Cliente. */
	private List<String> clienteList = new ArrayList<>();

	/** Lista para o combobox com as ContratoPratica. */
	private Collection<String> costCenterList = new ArrayList<>();

	/**
	 * reseta o bean.
	 */
	public void reset() {
		this.to = null;
		if (inactiveList != null)
			this.inactiveList.clear();
		resetFilter(false);
		this.isUpdate = Boolean.FALSE;
	}

	/**
	 * limpa elementos do filtro
	 * 
	 * @param update
	 */
	public void resetFilter(boolean update) {
		if (nomeCliente != null)
			nomeCliente = null;
		if (nomeContratoPratica != null)
			nomeContratoPratica = null;
		if (nomeCentroCusto != null)
			nomeCentroCusto = null;
		this.update = update;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final ConvergenciaRow to) {
		this.to = to;
	}

	/**
	 * @return the to Convergencia
	 */
	public ConvergenciaRow getTo() {
		return to;
	}

	/**
	 * @return the inactiveList
	 */
	public List<ConvergenciaRow> getInactiveList() {
		return inactiveList;
	}

	/**
	 * @param inactiveList
	 *            the inactiveList to set
	 */
	public void setInactiveList(List<ConvergenciaRow> inactiveList) {
		this.inactiveList = inactiveList;
		this.update = false;
	}

	public FormFilter getSearchFilter() {
		FormFilter filter = new GenericFormFilter();
		filter.addParam("nomeEmpresa", this.empresaCombo.getSelected());
		filter.addParam("nomeCliente", this.nomeCliente);
		filter.addParam("nomeContratoPratica", this.nomeContratoPratica);
		filter.addParam("nomeGrupoCusto", this.nomeCentroCusto);
		return filter;
	}

	/**
	 * @return the empresaCombo
	 */
	public ComboBox<Empresa> getEmpresaCombo() {
		return empresaCombo;
	}

	/**
	 * @param empresaCombo the empresaCombo to set
	 */
	public void setEmpresaCombo(ComboBox<Empresa> empresaCombo) {
		this.empresaCombo = empresaCombo;
	}

	/**
	 * @param clienteCombo
	 *            the clienteCombo to set
	 */
	public void setClienteCombo(ComboBox<Cliente> clienteCombo) {
		this.clienteCombo = clienteCombo;
	}

	/**
	 * @return the clienteCombo
	 */
	public ComboBox<Cliente> getClienteCombo() {
		return clienteCombo;
	}

	/**
	 * @param costCenterCombo
	 */
	public void setCostCenterCombo(ComboBox<VwMegaCCusto> costCenterCombo) {
		this.costCenterCombo = costCenterCombo;
	}

	/**
	 * @return the costCenterCombo
	 */
	public ComboBox<VwMegaCCusto> getCostCenterCombo() {
		return costCenterCombo;
	}

	/**
	 * @return the contratoPraticaCombo
	 */
	public ComboBox<ContratoPratica> getContratoPraticaCombo() {
		return contratoPraticaCombo;
	}

	/**
	 * @param contratoPraticaCombo
	 *            the contratoPraticaCombo to set
	 */
	public void setContratoPraticaCombo(
			ComboBox<ContratoPratica> contratoPraticaCombo) {
		this.contratoPraticaCombo = contratoPraticaCombo;
	}

	/**
	 * @return the codigoProjetoMega
	 */
	public String getCodigoProjetoMega() {
		return codigoProjetoMega;
	}

	/**
	 * @param codigoProjetoMega
	 *            the codigoProjetoMega to set
	 */
	public void setCodigoProjetoMega(String codigoProjetoMega) {
		this.codigoProjetoMega = codigoProjetoMega;
	}

	/**
	 * Obtem o valor do atributo {@link ActiveProjectBean#update}.<BR>
	 *
	 * @return the update
	 */
	public boolean isUpdate() {
		return update;
	}

	/**
	 * Obtem o valor do atributo {@link ActiveProjectBean#nomeProjetoMega}.<BR>
	 *
	 * @return the nomeProjetoMega
	 */
	public String getNomeProjetoMega() {
		return nomeProjetoMega;
	}

	/**
	 * Atualiza o valor do atributo nomeProjetoMega.<BR>
	 *
	 * @param nomeProjetoMega
	 *            Novo valor para o atributo
	 *            {@link ActiveProjectBean#nomeProjetoMega}.
	 */
	public void setNomeProjetoMega(String nomeProjetoMega) {
		this.nomeProjetoMega = nomeProjetoMega;
	}

	/**
	 * 
	 * @return
	 */
	public String getStatusSelected() {
		return statusSelected;
	}

	/**
	 * 
	 * @param statusSelected
	 */
	public void setStatusSelected(String statusSelected) {
		this.statusSelected = statusSelected;
	}

	/**
	 * @return the isUpdate
	 */
	public Boolean getIsUpdate() {
		return isUpdate;
	}

	/**
	 * @param isUpdate the isUpdate to set
	 */
	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
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

	//
	/**
	 * @param clienteMap
	 *            the clienteMap to set
	 */
	public void setClienteMap(final Map<String, Long> clienteMap) {
		this.clienteMap = clienteMap;
	}

	/**
	 * @return the clienteMap
	 */
	public Map<String, Long> getClienteMap() {
		return clienteMap;
	}

	/**
	 * @param clienteList
	 *            the clienteList to set
	 */
	public void setClienteList(final List<String> clienteList) {
		this.clienteList = clienteList;
	}

	/**
	 * @return the clienteList
	 */
	public List<String> getClienteList() {
		return clienteList;
	}

	public void setNomeContratoPratica(String nomeContratoPratica) {
		this.nomeContratoPratica = nomeContratoPratica;
	}

	public String getNomeContratoPratica() {
		return nomeContratoPratica;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public String getNomeCentroCusto() {
		return nomeCentroCusto;
	}

	public void setNomeCentroCusto(String nomeCentroCusto) {
		this.nomeCentroCusto = nomeCentroCusto;
	}

	public Collection<String> getCostCenterList() {
		return costCenterList;
	}

	public void setCostCenterList(Collection<String> costCenterList) {
		this.costCenterList = costCenterList;
	}
}