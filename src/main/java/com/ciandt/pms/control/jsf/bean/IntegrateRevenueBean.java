package com.ciandt.pms.control.jsf.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.ciandt.pms.enums.ReceitaTipo;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaLicenca;
import com.ciandt.pms.model.vo.FormFilter;
import com.ciandt.pms.model.vo.GenericFormFilter;
import com.ciandt.pms.model.vo.ReceitaDealFiscalRow;
import com.ciandt.pms.model.vo.ReceitaLicencaIntegravelRow;
import com.ciandt.pms.util.DateUtil;

/**
 * Define o BackingBean de integra��o das revenues.
 * 
 * @since 07/10/2009
 * @author <a href="mailto:hkushima@ciandt.com">Henrique Takashi Kushima</a>
 * 
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
public class IntegrateRevenueBean implements Serializable {

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Lista dos possiveis valores de meses. */
	private List<String> monthList = Arrays.asList("01", "02", "03", "04",
			"05", "06", "07", "08", "09", "10", "11", "12");

	/** Lista dos possiveis valores de anos. */
	private List<String> yearList = new ArrayList<String>();

	/** to ReceitaDealFiscal. */
	private ReceitaDealFiscal to = null;

	/** TO para {@link ReceitaLicenca}. */
	private ReceitaLicenca toReceitaLicenca;

	/** nome do contratoPratica selecionado. */
	private String nomeContratoPratica = null;

	/** nome da empresa selecionado. */
	private String nomeEmpresa = null;

	/** status selecionado. */
	private String status = null;

	/** lista de resultados da pesquisa. */
	private List<ReceitaDealFiscalRow> receitaDealFiscalRowList = new ArrayList<ReceitaDealFiscalRow>();

	/** Lista para o combobox com as ContratoPratica. */
	private List<String> contratoPraticaList = new ArrayList<String>();

	/** Lista para o combobox com as Empresas. */
	private List<String> empresaList = new ArrayList<String>();

	/** Lista para o combobox com as ContratoPratica. */
	private Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();

	/** Lista para o combobox com as Empresas. */
	private Map<String, Long> empresaMap = new HashMap<String, Long>();

	/** Mes selecionado. */
	private String month;

	/** Ano selecionado. */
	private String year;

	/** Tipo de receita a ser pesquisada. */
	private String tipoReceita = "SERVICO";

	/** Checa se o tipo de receita selecionado no combo eh de Licenca. */
	private boolean isLicenca;

	private boolean isAllPending;

	/** Lista de resultados da pesquisa de Receita. */
	private List<ReceitaLicencaIntegravelRow> receitasLicencaIntegraveisRow = new ArrayList<ReceitaLicencaIntegravelRow>();

	/**
	 * @return lista de anos da vigencia
	 */
	public List<String> getYearList() {

		int yearBegin = Integer.parseInt(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_BEGIN));
		int range = Integer.parseInt(systemProperties
				.getProperty(Constants.DEFAULT_PROPERTY_COMBOBOX_YEAR_RANGE));

		List<String> yList = new ArrayList<String>();

		for (int i = yearBegin; i <= yearBegin + range; i++) {
			yList.add("" + i);
		}

		yearList = yList;

		return yearList;
	}

	/**
	 * @param validityMonthList
	 *            the validityMonthList to set
	 */
	public void setMonthList(final List<String> validityMonthList) {
		this.monthList = validityMonthList;
	}

	/**
	 * @return the validityMonthList
	 */
	public List<String> getMonthList() {
		return monthList;
	}

	/**
	 * reseta o bean.
	 */
	public void reset() {
		this.to = null;
		this.toReceitaLicenca = null;
		this.month = null;
		this.year = null;
		this.status = null;
		this.nomeContratoPratica = "";
		this.nomeEmpresa = "";
		this.receitaDealFiscalRowList = new ArrayList<ReceitaDealFiscalRow>();
		this.receitasLicencaIntegraveisRow = new ArrayList<ReceitaLicencaIntegravelRow>();
		this.tipoReceita = "SERVICO";
        this.isAllPending = true;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(final ReceitaDealFiscal to) {
		this.to = to;
	}

	/**
	 * @return the to
	 */
	public ReceitaDealFiscal getTo() {
		return to;
	}

	/**
	 * @param toReceitaLicenca
	 *            the to toReceitaLicenca set
	 */
	public void setToReceitaLicenca(final ReceitaLicenca toReceitaLicenca) {
		this.toReceitaLicenca = toReceitaLicenca;
	}

	/**
	 * @return the toReceitaLicenca
	 */
	public ReceitaLicenca getToReceitaLicenca() {
		return toReceitaLicenca;
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
	 * @param empresaList
	 *            the empresaList to set
	 */
	public void setEmpresaList(final List<String> empresaList) {
		this.empresaList = empresaList;
	}

	/**
	 * @return the empresaList
	 */
	public List<String> getEmpresaList() {
		return empresaList;
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
	 * @param empresaMap
	 *            the empresaMap to set
	 */
	public void setEmpresaMap(final Map<String, Long> empresaMap) {
		this.empresaMap = empresaMap;
	}

	/**
	 * @return the empresaMap
	 */
	public Map<String, Long> getEmpresaMap() {
		return empresaMap;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(final String month) {
		this.month = month;
	}

	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(final String year) {
		this.year = year;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param nomeContratoPratica
	 *            the nomeContratoPratica to set
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
	 * @param nomeEmpresa
	 *            the nomeEmpresa to set
	 */
	public void setNomeEmpresa(final String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	/**
	 * @return the nomeEmpresa
	 */
	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	/**
	 * @param receitaDealFiscalRowList
	 *            the receitaDealFiscalRowList to set
	 */
	public void setReceitaDealFiscalRowList(
			final List<ReceitaDealFiscalRow> receitaDealFiscalRowList) {
		this.receitaDealFiscalRowList = receitaDealFiscalRowList;
	}

	/**
	 * @return the receitaDealFiscalRowList
	 */
	public List<ReceitaDealFiscalRow> getReceitaDealFiscalRowList() {
		return receitaDealFiscalRowList;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @return the tipoReceita
	 */
	public String getTipoReceita() {
		return tipoReceita;
	}

	/**
	 * @param tipoReceita
	 *            the tipoReceita to set
	 */
	public void setTipoReceita(String tipoReceita) {
		this.tipoReceita = tipoReceita;
	}

	/**
	 * @return the isLicenca
	 */
	public boolean getIsLicenca() {
		return "LICENCA".equalsIgnoreCase(this.tipoReceita);
	}

	public ReceitaTipo getReceitaTipo() {

		if (this.tipoReceita.equals("SERVICO")) {
			return ReceitaTipo.SERVICO;
		} else if (this.tipoReceita.equals("LICENCA")) {
			return ReceitaTipo.LICENCA;			
		}

		return null;
	}

    public boolean getIsAllPending() {
        return isAllPending;
    }

    public void setAllPending(boolean allPending) {
        isAllPending = allPending;
    }

    /**
	 * @return the receitasLicencaIntegraveisRow
	 */
	public List<ReceitaLicencaIntegravelRow> getReceitasLicencaIntegraveisRow() {
		return receitasLicencaIntegraveisRow;
	}

	/**
	 * @param revenues
	 *            the receitasLicencaIntegraveisRow to set
	 */
	public void setReceitasLicencaIntegraveisRow(
			List<ReceitaLicencaIntegravelRow> revenues) {
		this.receitasLicencaIntegraveisRow = revenues;
	}

	/**
	 * Obtem o filtro generico da tela de Integracao de Receitas.
	 * 
	 * @return {@link FormFilter}
	 */
	public FormFilter getIntegrableRevenuesFormFilter() {

		Long codigoContratoPratica = this.getContratoPraticaMap().get(
				this.getNomeContratoPratica());
		Long codigoEmpresa = this.getEmpresaMap().get(
				this.getNomeEmpresa());
		Date dataMes = DateUtil.getDate(this.getMonth(), this.getYear());

		FormFilter filter = new GenericFormFilter();
		filter.addParam("dataMes", dataMes);
		filter.addParam("codigoContratoPratica", codigoContratoPratica);
		filter.addParam("codigoEmpresa", codigoEmpresa);
		filter.addParam("status", this.status);
		return filter;
	}

	/**
	 * Retorna o numero de licenca que o usuario selecionou para serem
	 * integradas
	 * 
	 * @return numero de receitas licenca selecionada
	 */
	public List<ReceitaLicencaIntegravelRow> getAllReceitaLicencaSelected() {
		List<ReceitaLicencaIntegravelRow> selecteds = new ArrayList<ReceitaLicencaIntegravelRow>();
		for (ReceitaLicencaIntegravelRow row : this.receitasLicencaIntegraveisRow) {
			if (row.getIsSelected()) {
				selecteds.add(row);
			}
		}
		return selecteds;
	}

	/**
	 * Reseta as listas de resultados da busca.
	 */
	public void resetResultList() {
		this.receitaDealFiscalRowList = new ArrayList<ReceitaDealFiscalRow>();
		this.receitasLicencaIntegraveisRow = new ArrayList<ReceitaLicencaIntegravelRow>();
	}

}