package com.ciandt.pms.control.jsf;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.ITaxaImpostoService;
import com.ciandt.pms.business.service.ITipoServicoService;
import com.ciandt.pms.control.jsf.bean.TaxaImpostoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.TaxaImposto;
import com.ciandt.pms.model.TipoServico;

/**
 * Define o Controller da entidade.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:fmaximino@ciandt.com">Felipe Almeida Maximino</a>
 * 
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BOF.TAXES:VW", "BOF.TAXES:ED", "BOF.TAXES:CR", "BOF.TAXES:DE" })
public class TaxaImpostoController {

	/*********** SERVICES **************************/

	/** Instancia do serviço da entidade Taxa Imposto. */
	@Autowired
	private ITaxaImpostoService taxaImpostoService;

	/** Instancia do serviço da entidade Empresa. */
	@Autowired
	private IEmpresaService empresaService;

	/** Instancia do serviço da entidade TipoServico. */
	@Autowired
	private ITipoServicoService tipoServicoService;

	/** Instancia do serviço da entidade Modulo. */
	@Autowired
	private IModuloService moduloService;


	/*********** BEANS **************************/

	/** Instancia do bean. */
	@Autowired
	private TaxaImpostoBean bean = null;

	/*********** OUTCOMES **************************/

	/** outcome tela de inclusão da entidade. */
	private static final String OUTCOME_TAXA_IMPOST_ADD = "taxaImposto_add";

	/** outcome tela de inclusão da entidade. */
	private static final String OUTCOME_TAXA_IMPOSTO_SEARCH = "taxaImposto_search";

	/** outcome tela de exclusão da entidade. */
	private static final String OUTCOME_TAXA_IMPOSTO_DELETE = "taxaImposto_delete";

	/** outcome tela de deleção da entidade. */
	private static final String OUTCOME_TAXA_IMPOST_EDIT = "taxaImposto_edit";

	/**
	 * Pega o bean.
	 * 
	 * @return o bean TaxaImposto
	 */
	public TaxaImpostoBean getBean() {
		return bean;
	}

	/**
	 * Seta o bean.
	 * 
	 * @param pBean
	 *            do TaxaImposto
	 */
	public void setBean(final TaxaImpostoBean pBean) {
		this.bean = pBean;
	}

	/**
	 * Prepara a tela para add um TaxaImposto.
	 * 
	 * @return retorna a pagina de Add do TaxaImposto
	 */
	public String prepareAdd() {
		bean.reset();

		// seta modo de criação
		bean.setIsUpdate(Boolean.FALSE);

		loadCombos();

		return OUTCOME_TAXA_IMPOST_ADD;
	}

	/**
	 * Popula os comboBox da tela.
	 */
	private void loadCombos() {
		loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
		loadTipoServicoList(tipoServicoService.findTipoServicoAll());
	}

	/**
	 * popula a lista de tipoServico para o combobox.
	 * 
	 * @param tiposServico
	 *            - Lista de TipoServico
	 */
	private void loadTipoServicoList(final List<TipoServico> tiposServico) {
		List<String> listaTipoServico = new ArrayList<String>();
		Map<String, Long> mapaTipoServico = new HashMap<String, Long>();

		for (TipoServico tipoServico : tiposServico) {
			mapaTipoServico.put(tipoServico.getNomeTipoServico(),
					tipoServico.getCodigoTipoServico());
			listaTipoServico.add(tipoServico.getNomeTipoServico());
		}

		bean.setTipoServicoList(listaTipoServico);
		bean.setTipoServicoMap(mapaTipoServico);
	}

	/**
	 * popula a lista de empresas para o combobox.
	 * 
	 * @param empresas
	 *            - Lista de Empresas
	 */
	private void loadEmpresaList(final List<Empresa> empresas) {
		List<String> listaEmpresa = new ArrayList<String>();
		Map<String, Long> mapaEmpresa = new HashMap<String, Long>();

		for (Empresa empresa : empresas) {
			mapaEmpresa.put(empresa.getNomeEmpresa(),
					empresa.getCodigoEmpresa());
			listaEmpresa.add(empresa.getNomeEmpresa());
		}

		bean.setEmpresaList(listaEmpresa);
		bean.setEmpresaMap(mapaEmpresa);
	}

	/**
	 * Insere uma entidade.
	 * 
	 */
	public void create() {
		TaxaImposto taIm = bean.getTo();

		// pega empresa
		Empresa empresa = empresaService.findEmpresaById(bean.getEmpresaMap()
				.get(taIm.getEmpresa().getNomeEmpresa()));

		// pega tipo serviço
		TipoServico tipoServico = tipoServicoService.findTipoServicoById(bean
				.getTipoServicoMap().get(
						taIm.getTipoServico().getNomeTipoServico()));

		// gera data inicio vigencia
		Date data = criaData(bean.getMesInicioVigencia(),
				bean.getAnoInicioVigencia());

		// verifica o History Lock. Se startDate não for válido, dá mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(data, Boolean.valueOf(true), Boolean.FALSE)) {
			return;
		}

		taIm.setEmpresa(empresa);
		taIm.setTipoServico(tipoServico);
		taIm.setDataInicio(data);

		boolean existe = verifyTaxaImposto(taIm);
		if (!existe) {

			taxaImpostoService.createTaxaImposto(taIm);
			taxaImpostoService.updateTaxAndNetRevenueForecast(taIm.getDataInicio());
			bean.reset();

			Messages.showSucess("taxaImpostoAdd",
					Constants.DEFAULT_MSG_SUCCESS_SAVE);
		} else {
			Messages.showError("taxaImpostoAdd",
					Constants.MSG_ERROR_ADD_EDIT_TAXA_IMPOSTO);
		}

	}

	/**
	 * cria objeto Date a partir das strings de mes e ano.
	 * 
	 * @param mes
	 *            - mes desejado valores de 1 à 12.
	 * 
	 * @param ano
	 *            - ano desejado
	 * 
	 * @return retorna uma data a partir do mes e ano passado por parametro
	 * 
	 */
	private Date criaData(final String mes, final String ano) {

		if (!mes.equals("") && !ano.equals("")) {
			String dateStr = mes + "/" + ano;

			String[] dateFormat = { "MM/yyyy" };

			try {
				return DateUtils.parseDate(dateStr, dateFormat);
			} catch (ParseException e) {
				e.printStackTrace();
				Messages.showError("criaData",
						Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
			}
		}

		return null;
	}

	/**
	 * cria objeto Date a partir das strings de mes e ano para ser utilizado em
	 * uma query. se a data criada é nula, retorna a data 01/1000, q é utilizada
	 * como coringa na query.
	 * 
	 * @param mes
	 *            - mes desejado valores de 1 à 12.
	 * 
	 * @param ano
	 *            - ano desejado
	 * 
	 * @return retorna uma data a partir do mes e ano passado por parametro
	 * 
	 */
	private Date criaDataParaQuery(final String mes, final String ano) {
		Date data = criaData(mes, ano);

		if (data == null) {
			try {
				return DateUtils.parseDate("01/1000",
						new String[] { "MM/yyyy" });
			} catch (ParseException e) {
				e.printStackTrace();
				Messages.showError("criaDataParaQuery",
						Constants.DEFAULT_MSG_ERROR_INVALID_DATE);
			}
		}

		return data;
	}

	/**
	 * verifica se ja existe taxa imposto com os campos 'empresa', 'tipo
	 * servico' e 'data inicio' iguais ao do taxa imposto passado por parâmetro.
	 * 
	 * @param taIm
	 *            taxaImposto a ser comparado
	 * @return true, se ja existe; false, caso contrário
	 */
	private boolean verifyTaxaImposto(final TaxaImposto taIm) {
		List<TaxaImposto> taxaImposto = taxaImpostoService
				.findTaxaImpostoByEmpresaAndTipoServicoAndDataInicio(taIm
						.getEmpresa().getCodigoEmpresa(), taIm.getTipoServico()
						.getCodigoTipoServico(), taIm.getDataInicio());

		return !taxaImposto.isEmpty();
	}

	/**
	 * Prepara a tela de pesquisa da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareSearch() {
		bean.reset();

		loadCombos();

		// guarda a data do HistoryLock
		bean.setHistoryLockDate(moduloService.getClosingDateHistoryLock());

		return OUTCOME_TAXA_IMPOSTO_SEARCH;
	}

	/**
	 * Retorna a tela de pesquisa sem apagar os filtros.
	 * 
	 * @return pagina de destino
	 */
	public String backToSearch() {
		loadCombos();

		search();

		return OUTCOME_TAXA_IMPOSTO_SEARCH;
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 */
	public void search() {

		TaxaImposto filter = loadFilter();

		bean.setResultList(taxaImpostoService.findTaxaImpostoByFilter(filter));

		// se nenhum resultado for encontrado
		if (bean.getResultList().isEmpty()) {
			Messages.showWarning("findByFilter", Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// volta para a primeira pagina
		bean.setCurrentPageId(0);

	}

	/**
	 * carrega o filtro de busca de acordo com as entradas do usuário.
	 * 
	 * @return retorna ama entidade do tipo TaxaImposto
	 */
	private TaxaImposto loadFilter() {
		TaxaImposto filter = bean.getFilter();

		// codigo empresa
		Long codigoEmpresa = bean.getEmpresaMap().get(
				filter.getEmpresa().getNomeEmpresa());

		filter.getEmpresa().setCodigoEmpresa(getFilterParameter(codigoEmpresa));

		// tipo servico
		Long codigoTipoServico = bean.getTipoServicoMap().get(
				filter.getTipoServico().getNomeTipoServico());

		filter.getTipoServico().setCodigoTipoServico(
				getFilterParameter(codigoTipoServico));

		// datas
		Date dataInicio = criaDataParaQuery(bean.getMesInicioVigenciaFiltro(),
				bean.getAnoInicioVigenciaFiltro());
		filter.setDataInicio(dataInicio);

		Date dataFim = criaDataParaQuery(bean.getMesFimVigencia(),
				bean.getAnoFimVigencia());
		filter.setDataFim(dataFim);

		return filter;
	}

	/**
	 * se codigo esta setado, retorna ele mesmo. cc, retorna 0L indicando que o
	 * parâmetro não vai ser usado na busca
	 * 
	 * @param code
	 *            - código.
	 * 
	 * @return retorna o código.
	 * 
	 */
	private Long getFilterParameter(final Long code) {
		if (code == null) {
			return 0L;
		}

		return code;
	}

	/**
	 * Prepara a tela de remocao da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareRemove() {
		TaxaImposto taIm = taxaImpostoService.findTaxaImpostoById(bean
				.getCurrentRowId());
		bean.setTo(taIm);

		if (bean.getTo() == null || bean.getTo().getCodigoTaxaImposto() == null) {
			Messages.showWarning("findById",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		Date startDate = bean.getTo().getDataInicio();

		// verifica o History Lock. Se startDate não for válido, dá mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
			return null;
		}

		return OUTCOME_TAXA_IMPOSTO_DELETE;
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @return pagina de destino
	 * 
	 */
	public String remove() {
		TaxaImposto taImRemove = taxaImpostoService.findTaxaImpostoById(bean
				.getTo().getCodigoTaxaImposto());

		if (taxaImpostoService.removeTaxaImposto(taImRemove)) {
			taxaImpostoService.updateTaxAndNetRevenueForecast(moduloService.getClosingDateMapaAlocacao());
			// caso seja removido com sucesso,
			// exibe mensagem de sucesso
			Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
					Constants.ENTITY_NAME_DEAL);

			// limpa o to
			bean.resetTo();
			// realiza a busca novamente
			return backToSearch();
		}

		return null;
	}

	/**
	 * Prepara a tela de edicao da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String prepareUpdate() {
		// busca o Deal que será alterado
		TaxaImposto taim = taxaImpostoService.findTaxaImpostoById(bean
				.getCurrentRowId());
		bean.setTo(taim);
		bean.setIsUpdate(Boolean.TRUE);

		Date startDate = bean.getTo().getDataInicio();

		// verifica o History Lock. Se startDate não for válido, dá mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(startDate, Boolean.valueOf(true), Boolean.FALSE)) {
			return null;
		}

		return OUTCOME_TAXA_IMPOST_EDIT;
	}

	/**
	 * Executa um update da entidade.
	 * 
	 * @return pagina de destino
	 */
	public String update() {
		TaxaImposto taimEditado = bean.getTo();
		TaxaImposto taim = taxaImpostoService.findTaxaImpostoById(taimEditado
				.getCodigoTaxaImposto());

		// gera nova data inicio vigencia
		Date dataNova = criaData(bean.getMesInicioVigencia(),
				bean.getAnoInicioVigencia());
		Date dataAntiga = bean.getTo().getDataInicio();

		// verifica o History Lock. Se startDate não for válido, dá mensagem de
		// erro
		if (!moduloService.verifyHistoryLock(dataNova, Boolean.valueOf(true), Boolean.FALSE)) {
			return null;
		}

		// seta valores editados
		taim.setDataInicio(dataNova);
		// taim.setValorTaxa(taimEditado.getValorTaxa());
		taim.setValorTaxaFederal(taimEditado.getValorTaxaFederal());

		taim.setValorTaxaMunicipal(taimEditado.getValorTaxaMunicipal());

		// se as datas não forem iguais, calcula nova data de fim. cc, so
		// atualiza valor da taxa
		if (dataNova.compareTo(dataAntiga) != 0) {
			/*
			 * verifica se ja nao existe uma entidade com a mesma empresa, tipo
			 * servico e data de inicio da vigencia
			 */
			boolean existe = verifyTaxaImposto(taim);
			if (!existe) {
				// atualiza entidade
				taim.setDataInicio(dataAntiga);
				taxaImpostoService.updateTaxaImpostoVigencia(taim, dataNova);
				taxaImpostoService.updateTaxAndNetRevenueForecast(dataNova);
				Messages.showSucess("taxaImpostoEdit",
						Constants.DEFAULT_MSG_SUCCESS_UPDATE);
			} else {
				Messages.showError("taxaImpostoEdit",
						Constants.MSG_ERROR_ADD_EDIT_TAXA_IMPOSTO);

				return null;
			}
		} else {
			taxaImpostoService.updateTaxaImposto(taim);
			taxaImpostoService.updateTaxAndNetRevenueForecast(taim.getDataInicio());
		}

		// limpa o to
		bean.resetTo();

		// faz a busca novamente para recarregar a lista da pesquisa
		return backToSearch();
	}

}