package com.ciandt.pms.control.jsf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IMediaCotacaoMoedaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.control.jsf.bean.MediaCotacaoMoedaBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.MediaCotacaoMoeda;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.vo.MediaCotacaoMoedaRow;

/**
 * Define o Controller da entidade.
 * 
 * @since 05/09/2013
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "ROLE_PMS_ADMIN", "ROLE_PMS_CONTRACT", "ROLE_PMS_FINANCIAL", "ROLE_PMS_MANAGER", "ROLE_PMS_SR_MANAGER" })
public class MediaCotacaoMoedaController {

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** outcome tela visualização das moedas disponíveis. */
	private static final String OUTCOME_MEDIA_COTACAO_MOEDA_VIEW = "mediaCotacaoMoeda_view";

	/** outcome tela criacao/edição da entidade. */
	private static final String OUTCOME_MEDIA_COTACAO_MOEDA_ADD = "mediaCotacaoMoeda_add";

	/** outcome tela de visualisação de cotações. */
	private static final String OUTCOME_MEDIA_COTACAO_MOEDA_VIEW_RATES = "mediaCotacaoMoeda_view_rates";

	/** Instancia do servico da entidade MediaCotacaoMoeda. */
	@Autowired
	private IMediaCotacaoMoedaService mediaCotacaoMoedaService;

	/** Instancia do serviço da entidade Moeda. */
	@Autowired
	private IMoedaService moedaService;

	/** Instancia do servico Modulo. */
	@Autowired
	private IModuloService moduloService;

	/** Instancia do bean. */
	@Autowired
	private MediaCotacaoMoedaBean bean;

	/**
	 * Prepara tela com a lista de moedas disponíveis
	 * 
	 * @return Outcome para visualizar visualização das moedas disponíveis.
	 */
	public String prepareView() {

		bean.reset();

		bean.setListaMoeda(moedaService.findMoedaAll());

		return OUTCOME_MEDIA_COTACAO_MOEDA_VIEW;
	}
	
	/**
	 * Prepara tela com a lista de cotações
	 * 
	 * @return Outcome para visualização das cotações.
	 */
	public String prepareViewRates() {
		
//		bean.setListaMoeda(moedaService.findMoedaAll());
		bean.setListaMediaCotacaoMoedaRow(this
				.loadListaMediaCotacaoMoedaRow(mediaCotacaoMoedaService
						.findByMoeda(bean.getTo().getMoeda())));
		
		return OUTCOME_MEDIA_COTACAO_MOEDA_VIEW_RATES;
	}

	/**
	 * Prepara tela de inclusãoedição e visualização de MediaCotacaoMoeda
	 * 
	 * @return Outcome para adicionar/visualizar entidade.
	 */
	public String prepareConfigure() {
		bean.setFlagUpdate(Boolean.valueOf(false));

		bean.setListaMediaCotacaoMoedaRow(this
				.loadListaMediaCotacaoMoedaRow(mediaCotacaoMoedaService
						.findByMoeda(bean.getTo().getMoeda())));
		bean.setAno("");
		bean.getTo().setValorCotacao(null);

		return OUTCOME_MEDIA_COTACAO_MOEDA_ADD;
	}

	/**
	 * Salva entidade MediaCotacaoMoeda para cada mês do ano selecionado
	 */
	public void addMediaCotacaoMoeda() {

		// Captura o to.
		MediaCotacaoMoeda to = bean.getTo();

		try {
			// Adiciona o ano selecionado a data da entidade
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, Integer.valueOf(bean.getAno()));
			to.setDataData(cal.getTime());

			// Verifica se a cotação já existe
			List<MediaCotacaoMoeda> listaMediaCotacaoMoeda = mediaCotacaoMoedaService
					.findByDataAndMoeda(to);

			if (listaMediaCotacaoMoeda != null
					&& listaMediaCotacaoMoeda.size() > 0) {
				Messages.showError("updateMediaCotacaoMoeda",
						Constants.MSG_ERROR_MEDIA_COTACAO_MOEDA_ALREADY_EXISTS,
						Constants.ENTITY_NAME_MEDIA_COTACAO_MOEDA);
			} else {

				// Insere no banco
				mediaCotacaoMoedaService.createMediaCotacaoMoeda(to,
						bean.getAno());

				// Limpa os campos da tela
				bean.setAno("");
				bean.getTo().setValorCotacao(null);
				// bean.setListaMediaCotacaoMoedaRow(new
				// ArrayList<MediaCotacaoMoedaRow>());

				bean.setListaMediaCotacaoMoedaRow(this
						.loadListaMediaCotacaoMoedaRow(mediaCotacaoMoedaService
								.findByMoeda(bean.getTo().getMoeda())));

				// Seta mensagem de sucesso
				Messages.showSucess("create",
						Constants.DEFAULT_MSG_SUCCESS_CREATE,
						Constants.ENTITY_NAME_MEDIA_COTACAO_MOEDA);
			}
		} catch (Exception e) {
			Messages.showError("updateMediaCotacaoMoeda",
					Constants.MSG_ERROR_MEDIA_COTACAO_MOEDA_ADD,
					Constants.ENTITY_NAME_MEDIA_COTACAO_MOEDA);
		}
	}

	/**
	 * Prepara tela para ediçãoo de MediaCotacaoMoeda
	 */
	public void prepareEdit() {
		// Seta flag que indica se a ação é atualização para true
		bean.setFlagUpdate(Boolean.valueOf(true));

		// Seta valores a serem editados no bean
		MediaCotacaoMoedaRow row = bean.getMediaCotacaoMoedaRow();
		bean.setTo(row.getMediaCotacaoMoeda());
		bean.setAno(bean.getTo().getAno());
	}

	/**
	 * Atualiza entidade MediaCotacaoMoeda para cada mês do ano editado
	 */
	public void updateMediaCotacaoMoeda() {
		// Seta flag que indica se a ação é atualização para false
		bean.setFlagUpdate(Boolean.valueOf(false));

		try {
			// Atualiza a entidade
			mediaCotacaoMoedaService.updateMediaCotacaoMoeda(bean.getTo());

			// Limpa os campos da tela
			bean.setAno("");
			bean.getTo().setValorCotacao(null);

			// Atualiza a lista no grid para mostrar o valor inserido
			bean.setListaMediaCotacaoMoedaRow(this
					.loadListaMediaCotacaoMoedaRow(mediaCotacaoMoedaService
							.findByMoeda(bean.getTo().getMoeda())));
			// Seta mensagem de sucesso
			Messages.showSucess("updateMediaCotacaoMoeda",
					Constants.GENEREC_MSG_SUCCESS_UPDATE);
		} catch (Exception e) {
			Messages.showError("updateMediaCotacaoMoeda",
					Constants.MSG_ERROR_MEDIA_COTACAO_MOEDA_UPDATE,
					Constants.ENTITY_NAME_MEDIA_COTACAO_MOEDA);
		}
	}

	/**
	 * Mostra ou esconde a lista de MediaCotacaoMoeda por mes
	 */
	public void showHideMediaCotacaoMoedaMeses() {
		MediaCotacaoMoedaRow mediaCotacaoMoedaRow = bean
				.getMediaCotacaoMoedaRow();
		mediaCotacaoMoedaRow.setShowMediaCotacaoMoeda(!mediaCotacaoMoedaRow
				.getShowMediaCotacaoMoeda());
	}

	/**
	 * Carrega lista de MediaCotacaoMoeda para MediaCotacaoMoedaRow
	 * 
	 * @param listaMediaCotacaoMoeda
	 *            List of {@link MediaCotacaoMoeda}
	 * @return lista List of {@link MediaCotacaoMoedaRow}.
	 */
	private List<MediaCotacaoMoedaRow> loadListaMediaCotacaoMoedaRow(
			final List<MediaCotacaoMoeda> listaMediaCotacaoMoeda) {

		List<MediaCotacaoMoedaRow> listResult = new ArrayList<MediaCotacaoMoedaRow>();
		List<MediaCotacaoMoeda> listaMeses = null;
		MediaCotacaoMoedaRow row = null;

		for (MediaCotacaoMoeda mediaCotacaoMoedaAno : listaMediaCotacaoMoeda) {

			row = new MediaCotacaoMoedaRow();
			row.setMediaCotacaoMoeda(mediaCotacaoMoedaAno);

			// Adiciona na lista somente uma cotação por ano na lista principal
			if (!listResult.contains(row)) {

				listaMeses = new ArrayList<MediaCotacaoMoeda>();

				for (MediaCotacaoMoeda mediaCotacaoMoedaMes : listaMediaCotacaoMoeda) {
					// Adiciona na lista as cotações mensais
					if (mediaCotacaoMoedaAno.getAno().equals(
							mediaCotacaoMoedaMes.getAno())) {
						listaMeses.add(mediaCotacaoMoedaMes);
					}

				}

				row.setListaMediaCotacaoMoeda(listaMeses);
				
				row.setShowEditButton(Boolean.TRUE);
				if (this.getClosingDate().after(mediaCotacaoMoedaAno.getDataData())) {
					row.setShowEditButton(Boolean.FALSE);
				}

				listResult.add(row);
			}
		}

		return listResult;

	}
	
	/**
	 * Pega a data de fechamento do horas faturadas.
	 * 
	 * @return retorna a Data de Fechamento.
	 */
	private Date getClosingDate() {
		// pega a data de fechamento do modulo do mapa de alocacao
		Modulo moduloRevenue = moduloService.findModuloById(new Long(
				systemProperties.getProperty(Constants.MODULO_EXCHANGE_CODE)));

		return moduloRevenue.getDataFechamento();
	}

	/**
	 * @return the bean
	 */
	public MediaCotacaoMoedaBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final MediaCotacaoMoedaBean bean) {
		this.bean = bean;
	}

}