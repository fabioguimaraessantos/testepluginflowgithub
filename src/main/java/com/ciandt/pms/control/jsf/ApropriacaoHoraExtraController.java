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
import com.ciandt.pms.business.service.IApropriacaoHoraExtraService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPadraoArquivoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.bean.ApropriacaoHoraExtraBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ApropriacaoHoraExtra;
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
@RolesAllowed({"PER.OVERTIME_BY_VALUE:VW", "PER.OVERTIME_BY_VALUE:CR", "PER.OVERTIME_BY_VALUE:ED", "PER.OVERTIME_BY_VALUE:DE"})
public class ApropriacaoHoraExtraController extends ApropriacaoController {

	private static final String APPLICATION_CSV = "application/csv";

	/** outcome tela upload de banco de horas. */
	private static final String OUTCOME_APRO_HOR_EXT_UPLOAD_VL = "apropriacaoHorasExtras_upload";

	/** outcome tela adicionar hora extra. */
	private static final String OUTCOME_APRO_HOR_EXT_ADD = "apropriacaoHorasExtras_add";

	/** outcome tela pesquisa de hora extra. */
	private static final String OUTCOME_APRO_HOR_EXT_SEARCH = "apropriacaoHorasExtras_search";

	/** outcome tela edição de plantão por valor. */
	private static final String OUTCOME_APRO_HOR_EXT_EDIT = "apropriacaoHorasExtras_edit";

	/** Instancia do servico. */
	@Autowired
	private IApropriacaoHoraExtraService apropriacaoHoraExtraService;

	/** Instancia do servico Pessoa. */
	@Autowired
	private IPessoaService pessoaService;

	/** Instancia do PadraoArquivoService. */
	@Autowired
	private IPadraoArquivoService padraoArquivoService;

	/** Instancia do servico. */
	@Autowired
	private IUploadArquivoService uploadArquivoService;

	/** Instancia do servico MoedaService. */
	@Autowired
	private IMoedaService moedaService;

	/** Arquivo de configuracoes. */
	@Autowired
	private Properties systemProperties;

	/**
	 * Instancia do bean.
	 */
	@Autowired
	private ApropriacaoHoraExtraBean bean = null;

	/**
	 * @return the bean
	 */
	public ApropriacaoHoraExtraBean getBean() {
		return bean;
	}

	/**
	 * @param bean
	 *            the bean to set
	 */
	public void setBean(final ApropriacaoHoraExtraBean bean) {
		this.bean = bean;
	}

	/**
	 * Prepara a tela de search de hora extra.
	 * 
	 * @return retorna a página de seach.
	 */
	public String prepareSearch() {

		bean.reset();

		return OUTCOME_APRO_HOR_EXT_SEARCH;
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

		bean.setResultList(apropriacaoHoraExtraService.findApropriacaoHoraExtraByDataAndCdPessoa(
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
		bean.setTo(apropriacaoHoraExtraService
				.findApropriacaoHoraExtraById(bean.getCurrentRowId()));

		return OUTCOME_APRO_HOR_EXT_EDIT;
	}

	/**
	 * Salva tela de edição
	 */
	public void update() {
		ApropriacaoHoraExtra horaExtra = apropriacaoHoraExtraService
				.findApropriacaoHoraExtraById(bean.getTo()
						.getCodigoApropriacaoHoraExtra());

		horaExtra.setDataAtualizacao(new Date());
		// Seta usuario que esta realizado alteração
		horaExtra.setCodigoAutorAtualizacao(getUsuarioLogado());
		horaExtra.setValorHoraExtra(bean.getTo().getValorHoraExtra());
		horaExtra.setMoeda(moedaService.findMoedaById(bean.getMoedaMap().get(
				bean.getTo().getMoeda().getNomeMoeda())));

		// Salva a entidade ApropriacaoHoraExtra
		apropriacaoHoraExtraService.updateApropriacaoHoraExtra(horaExtra);

		Messages.showSucess("updateOvertime",
				Constants.DEFAULT_MSG_SUCCESS_SAVE,
				Constants.ENTITY_NAME_APROPRIACAO_HORA_EXTRA);
	}

	/**
	 * Cancela edição e volta para tela de search
	 */
	public String cancelUpdate() {
		search();
		bean.setEditItem(false);
		return OUTCOME_APRO_HOR_EXT_SEARCH;
	}

	/**
	 * Prepara a tela de inserção
	 * 
	 * @return retorna a página de create.
	 */
	public String prepareCreate() {

		reset();

		return OUTCOME_APRO_HOR_EXT_ADD;
	}

	/**
	 * Cria registro de hora extra
	 */
	public void create() {

		ApropriacaoHoraExtra horaExtra = bean.getTo();
		horaExtra.setDataAtualizacao(new Date());
		horaExtra.setCodigoAutorAtualizacao(getUsuarioLogado());

		// Consulta moeda pela a moeda selecionada
		Moeda moeda = moedaService.findMoedaById(bean.getMoedaMap().get(
				bean.getTo().getMoeda().getNomeMoeda()));
		// Seta a moeda
		horaExtra.setMoeda(moeda);

		// Consulta pessoa pelo login
		Pessoa pessoa = pessoaService.findPessoaByLogin(horaExtra.getPessoa()
				.getCodigoLogin());
		// Seta a pessoa
		horaExtra.setPessoa(pessoa);

		// Seta a mes e ano na dataMes
		horaExtra.setDataMes(DateUtil.getDate(bean.getMonthBeg(),
				bean.getYearBeg()));

		if (apropriacaoHoraExtraService.createApropriacaoHoraExtra(horaExtra)) {
			reset();
		}
	}

	/**
	 * Exclui um registro de hora extra
	 */
	public void remove() {

		ApropriacaoHoraExtra horaExtra = apropriacaoHoraExtraService
				.findApropriacaoHoraExtraById(bean.getCurrentRowId());

		apropriacaoHoraExtraService.removeApropriacaoHoraExtra(horaExtra);

		Messages.showSucess("apropriacaoHoraExtraRemove",
				Constants.MSG_SUCCESS_APROPRIACAO_HORA_EXTRA_REMOVE, horaExtra
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
	 * Prepara a tela de load do arquivo.
	 * 
	 * @return retorna a página de upload.
	 */
	public String prepareUpload() {
		bean.reset();

		loadPadraoArquivoList(padraoArquivoService.findPadraoArquivoAll(), bean);

		return OUTCOME_APRO_HOR_EXT_UPLOAD_VL;
	}

	/**
	 * Listener para o upload de arquivo.
	 * 
	 * @param event
	 *            evento do upload
	 * 
	 * @throws IOException
	 */
	public void uploadHoraExtraFileListener(final UploadEvent event)
			throws IOException {

		UploadItem item = event.getUploadItem();
		UploadArquivo upload = apropriacaoHoraExtraService
				.uploadApropriacaoHoraExtra(item,
						this.getSelectedPadraoArquivo());

		bean.setUploadArquivo(upload);
		bean.setUploadItem(item);
		bean.setErrorList(upload.getErrors());

		if (upload.getErrors().size() == 0) {
			Messages.showSucess("uploadApropriacaoHoraExtra",
					Constants.UPLOAD_MSG_SUCCESS_UPLOAD);
		} else if (upload.getErrors().size() == upload
				.getApropriacaoHoraExtras().size()) {
			Messages.showError("uploadApropriacaoHoraExtra",
					Constants.MSG_ERROR_UPLOAD_PADRAO_ARQUIVO);
		} else {
			Messages.showError("uploadApropriacaoHoraExtra",
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
	 * Salva o upload do arquivo de banco de horas.
	 */
	public void saveUploadHoraExtra() {
		try {
			apropriacaoHoraExtraService.saveUploadFile(bean.getUploadArquivo(),
					bean.getUploadItem().getData());
		} catch (IOException e) {
			e.printStackTrace();
			throw new NullPointerException();
		}

		Messages.showSucess("saveUploadBancoHoras",
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
			fileName = "sample_upload_hora_extra_open_office.csv";
		} else if (Long.valueOf(2).equals(codigoPadraoArquivo)) {
			fileName = "sample_upload_hora_extra_excel.csv";
		}

		String path = (String) systemProperties
				.get(Constants.UPLOAD_HORA_EXTRA_POR_VALOR_DESTINATION_PAHT);

		String contentType = APPLICATION_CSV;

		uploadArquivoService.downloadFile(path, fileName, contentType);
	}

	/**
	 * Recupera o arquivo do filesystem para download.
	 */
	public void downloadFile() {
		ApropriacaoHoraExtra apropriacaoHoraExtra = bean.getTo();

		// Write the output to a file
		String fileName = apropriacaoHoraExtra.getUploadArquivo()
				.getNomeArquivo();

		String path = (String) systemProperties
				.get(Constants.UPLOAD_HORA_EXTRA_POR_VALOR_DESTINATION_PAHT);

		String contentType = APPLICATION_CSV;

		uploadArquivoService.downloadFile(path, fileName, contentType);
	}

	/**
	 * Limpa a lista de "Records not imported" e "Records successfully imported"
	 */
	public void clear() {
		if (bean.getUploadArquivo() != null) {
			if (bean.getUploadArquivo().getApropriacaoHoraExtras() != null) {
				bean.getUploadArquivo().getApropriacaoHoraExtras().clear();
			}
			if (bean.getUploadArquivo().getErrors() != null) {
				bean.getUploadArquivo().getErrors().clear();
			}
		}
	}
}