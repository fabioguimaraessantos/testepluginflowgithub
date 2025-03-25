package com.ciandt.pms.control.jsf;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.lang.StringUtils;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IApropriacaoPlantaoService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPadraoArquivoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.bean.ApropriacaoPlantaoBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ApropriacaoPlantao;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.util.DateUtil;

/**
 * Define o Controller da entidade Apropriacao Plantão.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 22/10/2013
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({ "PER.ON_DUTY_HOURS_BY_VALUE:VW",
				"PER.ON_DUTY_HOURS_BY_VALUE:CR",
				"PER.ON_DUTY_HOURS_BY_VALUE:ED",
				"PER.ON_DUTY_HOURS_BY_VALUE:DE"})
public class ApropriacaoPlantaoController extends ApropriacaoController {

	private static final String APPLICATION_CSV = "application/csv";

	/** outcome tela upload de plantão por valor. */
	private static final String OUTCOME_APRO_PLA_UPLOAD_VL = "apropriacaoPlantao_upload";

	/** outcome tela adicionar plantão por valor. */
	private static final String OUTCOME_APRO_PLA_ADD = "apropriacaoPlantao_add";

	/** outcome tela pesquisa de plantão por valor. */
	private static final String OUTCOME_APRO_PLA_RESEARCH = "apropriacaoPlantao_search";

	/** outcome tela edição de plantão por valor. */
	private static final String OUTCOME_APRO_PLA_EDIT = "apropriacaoPlantao_edit";

	/** Instancia do servico. */
	@Autowired
	private IApropriacaoPlantaoService apropriacaoPlantaoService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do PadraoArquivoService. */
	@Autowired
	private IPadraoArquivoService padraoArquivoService;

	/** Instancia do servico UploadArquivoService. */
	@Autowired
	private IUploadArquivoService uploadArquivoService;

	/** Instancia do servico MoedaService. */
	@Autowired
	private IMoedaService moedaService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/** Instancia do bean. */
	@Autowired
	private ApropriacaoPlantaoBean bean = null;

	/**
	 * @return the bean
	 */
	public ApropriacaoPlantaoBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ApropriacaoPlantaoBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de search de plantao.
	 * 
	 * @return retorna a página de seach.
	 */
	public String prepareSearch() {

		bean.reset();

		return OUTCOME_APRO_PLA_RESEARCH;
	}

	/**
	 * Realiza busca pelo filtro
	 */
	public void search() {

		Pessoa pessoa = null;
		String codigoLogin = bean.getPessoa().getCodigoLogin();
		if (!StringUtils.isEmpty(codigoLogin)) {
			pessoa = pessoaService.findPessoaByLogin(codigoLogin);
		} else {
			pessoa = new Pessoa();
			pessoa.setCodigoPessoa(0L);
		}

		bean.setResultList(apropriacaoPlantaoService.findApropriacaoPlantaoByDataAndCdPessoa(
				DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg()), pessoa));

		if (bean.getResultList().size() == 0) {
			Messages.showWarning("findByFilter",
					Constants.DEFAULT_MSG_WARNG_NO_RESULT);
		}

		// volta para a primeira pagina da paginacao
		bean.setCurrentPageId(0);
	}

	/**
	 * Prepara tela de edição
	 */
	public String prepareUpdate() {

		loadMoedaList(bean);

		// Seta flag para indicar que é edição
		bean.setEditItem(true);

		// Consulta plantão por id e seta no to
		bean.setTo(apropriacaoPlantaoService.findApropriacaoPlantaoById(bean
				.getCurrentRowId()));

		return OUTCOME_APRO_PLA_EDIT;
	}

	/**
	 * Salva tela de edição
	 */
	public void update() {
		ApropriacaoPlantao plantao = apropriacaoPlantaoService
				.findApropriacaoPlantaoById(bean.getTo()
						.getCodigoApropriacaoPlantao());
		plantao.setDataAtualizacao(new Date());
		// Seta usuario que esta realizado alteração
		plantao.setCodigoAutorAtualizacao(getUsuarioLogado());
		plantao.setValorPlantao(bean.getTo().getValorPlantao());

		// Seta a moeda
		plantao.setMoeda(moedaService.findMoedaById(bean.getMoedaMap().get(
				bean.getTo().getMoeda().getNomeMoeda())));

		// Salva a entidade ApropriacaoPlantao
		apropriacaoPlantaoService.updateApropriacaoPlantao(plantao);

		Messages.showSucess("updateDutyHours",
				Constants.DEFAULT_MSG_SUCCESS_SAVE,
				Constants.ENTITY_NAME_APROPRIACAO_PANTAO);
	}

	/**
	 * Cancela edição e volta para tela de search
	 */
	public String cancelUpdate() {
		search();
		bean.setEditItem(false);
		return OUTCOME_APRO_PLA_RESEARCH;
	}

	/**
	 * Prepara a tela de inserção
	 * 
	 * @return retorna a página de create.
	 */
	public String prepareCreate() {

		reset();

		return OUTCOME_APRO_PLA_ADD;
	}

	/**
	 * Cria o plantão
	 */
	public void create() {

		ApropriacaoPlantao plantao = bean.getTo();
		plantao.setCodigoAutorAtualizacao(getUsuarioLogado());

		// Consulta moeda pela a moeda selecionada
		Moeda moeda = moedaService.findMoedaById(bean.getMoedaMap().get(
				bean.getTo().getMoeda().getNomeMoeda()));
		// Seta a moeda
		plantao.setMoeda(moeda);

		// Consulta pessoa pelo login
		Pessoa pessoa = pessoaService.findPessoaByLogin(plantao.getPessoa()
				.getCodigoLogin());
		// Seta a pessoa
		plantao.setPessoa(pessoa);

		// Seta a mes e ano na dataMes
		plantao.setDataMes(DateUtil.getDate(bean.getMonthBeg(),
				bean.getYearBeg()));

		if (apropriacaoPlantaoService.createApropriacaoPlantao(plantao)) {
			reset();
		}
	}

	/**
	 * Exclui um registro de plantão
	 */
	public void remove() {

		ApropriacaoPlantao plantao = apropriacaoPlantaoService
				.findApropriacaoPlantaoById(bean.getCurrentRowId());

		apropriacaoPlantaoService.removeApropriacaoPlantao(plantao);

		Messages.showSucess("apropriacaoPlantaoRemove",
				Constants.MSG_SUCCESS_APROPRIACAO_PLANTAO_REMOVE, plantao
						.getPessoa().getCodigoLogin());

		search();
	}

	/**
	 * Reseta bean e popula lista de moeda
	 */
	private void reset() {
		bean.reset();
		super.loadMoedaList(bean);
	}

	/**
	 * Prepara a tela de upload do arquivo.
	 * 
	 * @return retorna a página de upload.
	 */
	public String prepareUpload() {
		reset();

		loadPadraoArquivoList(padraoArquivoService.findPadraoArquivoAll(), bean);

		return OUTCOME_APRO_PLA_UPLOAD_VL;
	}

	/**
	 * Listener para o upload de arquivo.
	 * 
	 * @param event
	 *            evento do upload
	 * 
	 * @throws IOException
	 */
	public void uploadPlantaoFileListener(final UploadEvent event)
			throws IOException {

		UploadItem item = event.getUploadItem();
		UploadArquivo upload = apropriacaoPlantaoService
				.uploadApropriacaoPlantao(item, this.getSelectedPadraoArquivo());

		bean.setUploadArquivo(upload);
		bean.setUploadItem(item);
		bean.setErrorList(upload.getErrors());

		if (upload.getErrors().size() == 0) {
			Messages.showSucess("uploadApropriacaoPlantao",
					Constants.UPLOAD_MSG_SUCCESS_UPLOAD);
		} else if (upload.getErrors().size() == upload.getApropriacaoPlantaos()
				.size()) {
			Messages.showError("uploadApropriacaoPlantao",
					Constants.MSG_ERROR_UPLOAD_PADRAO_ARQUIVO);
		} else {
			Messages.showError("uploadApropriacaoPlantao",
					Constants.MSG_ERROR_UPLOAD);
		}
	}

	/**
	 * Pega o padrao arquivo selecionado.
	 * 
	 * @return retorna o pradrão de arquivo selecionado.
	 */
	private PadraoArquivo getSelectedPadraoArquivo() {
		Long id = bean.getPadraoArquivoMap().get(
				bean.getPadraoArquivo().getNomePadraoArquivo());

		PadraoArquivo padraoArq = null;
		if (id != null) {
			padraoArq = padraoArquivoService.findPadraoArquivoById(id);

			bean.setPadraoArquivo(padraoArq);
		}

		return padraoArq;
	}

	/**
	 * Salva o upload do arquivo de plantão.
	 */
	public void saveUploadPlantao() {
		try {
			apropriacaoPlantaoService.saveUploadFile(bean.getUploadArquivo(),
					bean.getUploadItem().getData());
		} catch (IOException e) {
			e.printStackTrace();
			throw new NullPointerException();
		}

		Messages.showSucess("saveUploadPlantao",
				Constants.DEFAULT_MSG_SUCCESS_SAVE,
				Constants.ENTITY_NAME_PESS_BCO_HRS);

		this.prepareUpload();
	}

	/**
	 * Evento que seta a lista de empresas intercomp.
	 * 
	 * @param e
	 *            - evento
	 */
	public void changeValuePadraoArquivo(final ValueChangeEvent e) {

		String nomePadraoArq = (String) e.getNewValue();

		bean.getPadraoArquivo().setNomePadraoArquivo(nomePadraoArq);
	}

	/**
	 * Recupera o arquivo do filesystem para download.
	 */
	public void getSampleFile() {
		// Write the output to a file
		String fileName = "";

		Long codigoPadraoArquivo = bean.getPadraoArquivoMap().get(
				bean.getPadraoArquivo().getNomePadraoArquivo());

		if (Long.valueOf(1).equals(codigoPadraoArquivo)) {
			fileName = "sample_upload_plantao_by_value_open_office.csv";
		} else if (Long.valueOf(2).equals(codigoPadraoArquivo)) {
			fileName = "sample_upload_plantao_by_value_excel.csv";
		}

		String path = (String) systemProperties
				.get(Constants.UPLOAD_PLANTAO_POR_VALOR_DESTINATION_PAHT);

		uploadArquivoService.downloadFile(path, fileName, APPLICATION_CSV);
	}

	/**
	 * Recupera o arquivo do filesystem para download.
	 */
	public void downloadFile() {
		ApropriacaoPlantao apropriacaoPlantao = bean.getTo();

		// Write the output to a file
		String fileName = apropriacaoPlantao.getUploadArquivo()
				.getNomeArquivo();

		String path = (String) systemProperties
				.get(Constants.UPLOAD_PLANTAO_POR_VALOR_DESTINATION_PAHT);

		uploadArquivoService.downloadFile(path, fileName, APPLICATION_CSV);
	}

	/**
	 * Limpa a lista de "Records not imported" e "Records successfully imported"
	 */
	public void clear() {
		if (bean.getUploadArquivo() != null) {
			if (bean.getUploadArquivo().getApropriacaoPlantaos() != null) {
				bean.getUploadArquivo().getApropriacaoPlantaos().clear();
			}
			if (bean.getUploadArquivo().getErrors() != null) {
				bean.getUploadArquivo().getErrors().clear();
			}
		}
	}

}