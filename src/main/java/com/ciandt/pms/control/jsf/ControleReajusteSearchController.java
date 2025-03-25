package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.ICentroLucroService;
import com.ciandt.pms.business.service.IClienteService;
import com.ciandt.pms.business.service.IControleReajusteService;
import com.ciandt.pms.business.service.IControleReajusteStatusService;
import com.ciandt.pms.business.service.IFichaReajusteService;
import com.ciandt.pms.business.service.IMsaService;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.control.jsf.bean.ControleReajusteSearchBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.ControleReajusteStatus;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.model.vo.HistoricoControleReajusteRow;

/**
 * Define o Controller da entidade FichaReajuste.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 06/01/2014
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "BUS.READJUSTMENT:VW", "BUS.READJUSTMENT:ED" })
public class ControleReajusteSearchController {

	/** outcome tela de busca de ControleReajuste. */
	private static final String OUTCOME_CONTROLE_REAJUSTE_SEARCH = "controle_reajuste_search";

	/** outcome tela de criação de ControleReajuste. */
	private static final String OUTCOME_CONTROLE_REAJUSTE_SEARCH_EDIT = "controle_reajuste_search_edit";

	/** outcome tela de criação de ControleReajuste. */
	private static final String OUTCOME_CONTROLE_REAJUSTE_SEARCH_VIEW = "controle_reajuste_search_view";

	/** Instancia do servico. */
	@Autowired
	private IControleReajusteService controleReajusteService;

	/** Instancia do servico. */
	@Autowired
	private IClienteService clienteService;

	/** Instancia do servico. */
	@Autowired
	private INaturezaCentroLucroService naturezaService;

	/** Instancia do servico. */
	@Autowired
	private IMsaService msaService;

	/** Instancia do servico CentroLucro. */
	@Autowired
	private ICentroLucroService centroLucroService;
	
	/** Instancia do servico ControleReajusteStatusService. */
	@Autowired
	private IControleReajusteStatusService controleReajusteStatusService;
	
	/** Instancia do servico FichaReajusteService. */
	@Autowired
	private IFichaReajusteService fichaReajusteService;

	/**
	 * Instancia do bean.
	 */
	@Autowired
	private ControleReajusteSearchBean bean = null;

	/**
	 * @return the bean
	 */
	public ControleReajusteSearchBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ControleReajusteSearchBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de busca.
	 *
	 * @return tela de destino.
	 */
	public String prepareSearch() {
		bean.reset();
		this.loadSearchForm();

		return OUTCOME_CONTROLE_REAJUSTE_SEARCH;
	}

	/**
	 * Realiza a busca de {@link ControleReajuste}.
	 *
	 * @return tela de destino.
	 */
	public String search() {
		Long clienteId = bean.getClientesMap().get(bean.getClienteSelected());
		Cliente cliente = new Cliente();
		if (clienteId != null) {
			cliente = clienteService.findClienteById(clienteId);			
		}

		Long msaId = bean.getMsaMap().get(bean.getMsaSelected());
		Msa msa = new Msa();
		if (msaId != null) {
			msa = msaService.findMsaById(msaId);			
		}

		Long statusId = bean.getStatusMap().get(bean.getSearchStatusSelected());

		bean.setControlesReajuste(controleReajusteService
				.findControleReajusteByDateIntervalAndMsaAndCliente(
						bean.getDateFrom(), bean.getDateTo(), msa, cliente, statusId));
		
		if (bean.getControlesReajuste() == null
				|| bean.getControlesReajuste().size() == 0) {
			Messages.showWarning("search",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		return OUTCOME_CONTROLE_REAJUSTE_SEARCH;
	}

	/**
	 * Carrega os combos e prepara o formulário de filtro.
	 * 
	 */
	private void loadSearchForm() {
		bean.reset();

		this.loadNaturezaCentroLucroList(naturezaService
				.findNaturezaCentroLucroAll());
		this.loadClienteList(clienteService.findClienteAllClientePai());
		this.loadMsaList(msaService.findMsaAll());
		this.loadStatusList(controleReajusteStatusService
				.findControleReajusteAllActive());
	}

	/**
	 * Prepara a tela de ControleReajusteSearchView.xhtml.
	 *
	 * @return tela de destino.
	 */
	public String prepareView() {

		bean.setMsa(msaService.findMsaById(bean.getTo().getFichaReajuste()
				.getMsaForDataTable().getCodigoMsa()));

		return OUTCOME_CONTROLE_REAJUSTE_SEARCH_VIEW;
	}

	/**
	 * Prepara a telad e ControleReajusteSearchEdit.xhtml.
	 *
	 * @return tela de destino.
	 */
	public String prepareUpdate() {

		this.loadControleReajusteStatus(controleReajusteStatusService
				.findControleReajusteAllActive());

		bean.setMsa(msaService.findMsaById(bean.getTo().getFichaReajuste()
				.getMsaForDataTable().getCodigoMsa()));

		bean.setStatusSelected(bean.getTo().getControleReajusteStatus()
				.getNomeControleReajStatus());

		return OUTCOME_CONTROLE_REAJUSTE_SEARCH_EDIT;
	}

	/**
	 * Atualiza as modificações realizadas na tela de edição de
	 * {@link ControleReajuste}
	 * 
	 * @return página de destino
	 */
	public String update() {
		ControleReajusteStatus status = controleReajusteStatusService
				.findControleReajusteStatusById(bean.getStatusMap().get(
						bean.getStatusSelected()));
		bean.getTo().setControleReajusteStatus(status);

		bean.getTo().setFichaReajuste(
				fichaReajusteService.findFichaReajusteById(bean.getTo()
						.getFichaReajuste().getCodigoFichaReajuste()));

		if (this.validFieldsForStatus()) {
			controleReajusteService.updateControleReajuste(bean.getTo());
			
			Messages.showSucess("update", Constants.GENEREC_MSG_SUCCESS_UPDATE);

			return this.search();
		} else {
			return OUTCOME_CONTROLE_REAJUSTE_SEARCH_EDIT;
		}
	}

	/**
	 * Verifica se todos os campos obrigatórios para um determinado status estão
	 * preenchidos.
	 * 
	 * @return {@link Boolean}
	 */
	private Boolean validFieldsForStatus() {
		ControleReajuste controleReajuste = bean.getTo();
		if (Constants.CONTROLE_REAJUSTE_STATUS_SG_EXECUTED
				.equals(controleReajuste.getControleReajusteStatus()
						.getSiglaControleReajStatus())) {
			if (controleReajuste.getPercentualPrevista() == null
					|| controleReajuste.getPercentualRealizada() == null
					|| controleReajuste.getDataReajuste() == null) {
				Messages.showError("requiredFields",
						Constants.BUNDLE_ALL_FIELDS_REQUIRED);

				return false;
			}
		}

		return true;
	}

	/**
	 * Popula listas para combo de status para telas que possuem
	 * {@link ControleReajusteStatus}.
	 * 
	 * @param statusAll
	 */
	public void loadControleReajusteStatus(
			List<ControleReajusteStatus> statusAll) {

		List<String> statusList = new ArrayList<String>();
		Map<String, Long> statusMap = new HashMap<String, Long>();

		for (ControleReajusteStatus status : statusAll) {
			statusList.add(status.getNomeControleReajStatus());
			statusMap.put(status.getNomeControleReajStatus(),
					status.getCodigoControleReajStatus());
		}

		bean.setStatus(statusList);
		bean.setStatusMap(statusMap);
	}

	/**
	 * Popula a lista de naturezas para combobox da natureza.
	 * 
	 * @param naturezas
	 *            lista de naturezas.
	 * 
	 */
	private void loadNaturezaCentroLucroList(
			final List<NaturezaCentroLucro> naturezas) {

		Map<String, Long> naturezaCentroLucroMap = new HashMap<String, Long>();
		List<String> naturezaCentroLucroList = new ArrayList<String>();

		for (NaturezaCentroLucro natureza : naturezas) {
			naturezaCentroLucroMap.put(natureza.getNomeNatureza(),
					natureza.getCodigoNatureza());
			naturezaCentroLucroList.add(natureza.getNomeNatureza());
		}

		bean.setNaturezaMap(naturezaCentroLucroMap);
		bean.setNaturezaList(naturezaCentroLucroList);
	}

	/**
	 * Popula a lista de clientes para combobox.
	 * 
	 * @param clientes
	 *            lista de clientes.
	 * 
	 */
	private void loadClienteList(final List<Cliente> clientes) {

		Map<String, Long> clienteMap = new HashMap<String, Long>();
		List<String> clienteList = new ArrayList<String>();

		for (Cliente cliente : clientes) {
			clienteMap
					.put(cliente.getNomeCliente(), cliente.getCodigoCliente());
			clienteList.add(cliente.getNomeCliente());
		}

		bean.setClientesMap(clienteMap);
		bean.setClientes(clienteList);
	}

	private void loadMsaList(final List<Msa> msas) {
		List<String> msaList = new ArrayList<String>();
		Map<String, Long> msaMap = new HashMap<String, Long>();

		for (Msa msa : msas) {
			msaList.add(msa.getNomeMsa());
			msaMap.put(msa.getNomeMsa(), msa.getCodigoMsa());
		}

		bean.setMsaList(msaList);
		bean.setMsaMap(msaMap);
	}

	private void loadStatusList(final List<ControleReajusteStatus> controleReajusteStatus) {
		List<String> controleReajusteStatusList = new ArrayList<String>();
		Map<String, Long> controleReajusteStatusMap = new HashMap<String, Long>();
		
		for (ControleReajusteStatus status : controleReajusteStatus) {
			controleReajusteStatusList.add(status.getNomeControleReajStatus());
			controleReajusteStatusMap.put(status.getNomeControleReajStatus(), status.getCodigoControleReajStatus());
		}
		
		bean.setStatus(controleReajusteStatusList);
		bean.setStatusMap(controleReajusteStatusMap);
	}

	/**
	 * Popula a lista de centroLucros para combobox.
	 * 
	 * @param centroLucros
	 *            lista de contratos.
	 * 
	 */
	private void loadCentroLucroList(final List<CentroLucro> centroLucros) {

		Map<String, Long> centroLucroMap = new HashMap<String, Long>();
		List<String> centroLucroList = new ArrayList<String>();

		for (CentroLucro centroLucro : centroLucros) {
			centroLucroMap.put(centroLucro.getNomeCentroLucro(),
					centroLucro.getCodigoCentroLucro());
			centroLucroList.add(centroLucro.getNomeCentroLucro());
		}

		bean.setCentrosLucroMap(centroLucroMap);
		bean.setCentrosLucro(centroLucroList);
	}

	/**
	 * Verifica se o valor do atributo NaturezaCentroLucro é valido. Se o valor
	 * não é nulo e existe no naturezaMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateNaturezaCentroLucro(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoNatureza = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoNatureza = bean.getNaturezaMap().get(strValue);
			if (codigoNatureza == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo CentroLucro é valido. Se o valor não é
	 * nulo e existe no centroLucroMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCentroLucro(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoCentroLucro = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCentroLucro = bean.getCentrosLucroMap().get(strValue);
			if (codigoCentroLucro == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}

	/**
	 * Verifica se o valor do atributo Cliente é valido. Se o valor não é
	 * nulo e existe no ClientesMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateCliente(final FacesContext context,
			final UIComponent component, final Object value) {

		Long codigoCliente = null;
		String strValue = (String) value;

		if ((strValue != null) && (!"".equals(strValue))) {
			codigoCliente = bean.getClientesMap().get(strValue);
			if (codigoCliente == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}
	
	/**
	 * Verifica se o valor do atributo Cliente é valido. Se o valor não é
	 * nulo e existe no ClientesMap, então o valor é valido.
	 * 
	 * @param context
	 *            contexto do faces.
	 * @param component
	 *            componente faces.
	 * @param value
	 *            valor do componente.
	 */
	public void validateMsa(final FacesContext context,
			final UIComponent component, final Object value) {
		
		Long codigoMsa = null;
		String strValue = (String) value;
		
		if ((strValue != null) && (!"".equals(strValue))) {
			codigoMsa = bean.getMsaMap().get(strValue);
			if (codigoMsa == null) {
				String label = (String) component.getAttributes().get("label");
				throw new ValidatorException(Messages.getMessageError(
						Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
			}
		}
	}


	/**
	 * Popula o combobox do centro-lucro de acordo com o a natureza selecionada.
	 * 
	 * @param e
	 *            - evento de mudança
	 */
	public void prepareCentroLucroCombo(final ValueChangeEvent e) {
		bean.setCentroLucroSelected("");
		String value = (String) e.getNewValue();

		if (value != null) {
			Long codNatureza = bean.getNaturezaMap().get(value);
			NaturezaCentroLucro natureza = null;

			// se o centro de lucro existir
			if (codNatureza != null) {
				natureza = naturezaService
						.findNaturezaCentroLucroById(codNatureza);
				this.loadCentroLucroList(centroLucroService
						.findCentroLucroByNatureza(natureza));
				// senao existir cria uma lista vazia de centro-lucro
			} else {
				this.loadCentroLucroList(new ArrayList<CentroLucro>());
			}
		}
	}

	public void prepareClienteAndMsaCombo(final ValueChangeEvent e) {
		bean.setClienteSelected("");
		bean.setMsaSelected("");

		String value = (String) e.getNewValue();

		if (value != null) {
			Long codigoCentroLucro = bean.getCentrosLucroMap().get(value);

			if (codigoCentroLucro != null) {
				CentroLucro centroLucro = centroLucroService.findCentroLucroById(codigoCentroLucro);

				this.loadClienteList(clienteService.findClienteByCentroLucro(centroLucro));
				this.loadMsaList(msaService.findMsaByCentroLucro(centroLucro));
			}
		}
	}

	/**
	 * prepare aba de historico de controle de reajuste.
	 */
	public void prepareHistory() {
		List<HistoricoControleReajusteRow> listResult = new ArrayList<HistoricoControleReajusteRow>();

		List<ControleReajusteAud> listaAud = controleReajusteService
				.findHistoryByCodigoControleReajuste(bean.getTo()
						.getCodigoControleReajuste());

		HistoricoControleReajusteRow histRow = null;
		for (ControleReajusteAud controleReajusteAud : listaAud) {
			histRow = new HistoricoControleReajusteRow(
					controleReajusteStatusService.findControleReajusteStatusById(controleReajusteAud
							.getCodigoControleReajStatus()),
					controleReajusteAud);

			listResult.add(histRow);
		}

		bean.setHistoryList(listResult);
	}
}