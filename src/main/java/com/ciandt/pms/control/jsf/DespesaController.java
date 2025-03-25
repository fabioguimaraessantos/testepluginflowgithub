package com.ciandt.pms.control.jsf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.IPadraoArquivoService;
import com.ciandt.pms.business.service.ITipoDespesaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.business.service.IUploadDespesaService;
import com.ciandt.pms.control.jsf.bean.DespesaBean;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.model.UploadDespesa;
import com.ciandt.pms.util.DateUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author cmantovani
 * @since 29/06/2011
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"FIN.COST_CENTER_EXPENSES:VW", "FIN.COST_CENTER_EXPENSES:ED"})
public class DespesaController {

    /** outcome tela de upload de despesas. */
    private static final String OUTCOME_DESPESA_UPLOAD = "despesa_upload";

    /** outcome tela de busca de despesas. */
    private static final String OUTCOME_DESPESA_SEARCH = "despesa_search";

    /** outcome tela de delete de despesas. */
    private static final String OUTCOME_DESPESA_DELETE = "despesa_delete";

    /** outcome tela de delete de despesas. */
    private static final String OUTCOME_DESPESA_PREVIEW = "despesa_preview";

    /** outcome tela de adicao de despesas. */
    private static final String OUTCOME_DESPESA_ADD = "despesa_add";

    /** outcome tela de edicao de despesas. */
    private static final String OUTCOME_DESPESA_EDIT = "despesa_edit";

    /** Instancia do servico. */
    @Autowired
    private IUploadDespesaService uploadDespesaService;

    /** Instancia do servico. */
    @Autowired
    private IUploadArquivoService uploadArquivoService;

    /** Instancia do servico. */
    @Autowired
    private IModuloService moduloService;

    /** Instancia do servico. */
    @Autowired
    private IMoedaService moedaService;
    
    /** Instancia do servico. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do servico. */
    @Autowired
    private IGrupoCustoService grupoCustoService;

    /** Instancia do servico. */
    @Autowired
    private ITipoDespesaService tipoDespesaService;

    /** Instancia do PadraoArquivoService. */
    @Autowired
    private IPadraoArquivoService padraoArquivoService;

    /** Instancia do EmpresaService. */
    @Autowired
    private IEmpresaService empresaService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Instancia do bean.
     */
    @Autowired
    private DespesaBean bean = null;

    /** Padrao de formatacao das datas. */
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
            new SimpleDateFormat("MMM/yyyy", new Locale("en/US"));

    /**
     * @return the bean
     */
    public DespesaBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final DespesaBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de load do arquivo.
     * 
     * @return retorna a página de upload.
     */
    public String prepareUpload() {
        bean.reset();

        loadPadraoArquivoList(padraoArquivoService.findPadraoArquivoAll());

        loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());
        this.loadContratoPraticaList(contratoPraticaService.findContratoPraticaAllCompleteAndActive());
        loadTipoDespesaList(tipoDespesaService.findTipoDespesaAllActive());
        loadMoedaList(moedaService.findMoedaAll());

        // carrega o combo com as datas possiveis para validacao
        bean.setDataMesList(DateUtil.getDateMonthListString(moduloService
                .getClosingDateExpense(), SIMPLE_DATE_FORMAT));

        bean.setSelectedMonthDate(bean.getDataMesList().get(
                bean.getDataMesList().size() - 1));

        return OUTCOME_DESPESA_UPLOAD;
    }

    /**
     * Listener para o upload de arquivo.
     * 
     * @param event
     *            evento do upload
     * 
     * @throws Exception
     *             lança uma exception
     */
    public void uploadDespesaListener(final UploadEvent event) throws Exception {

        UploadItem item = event.getUploadItem();

        Long codigoEmpresa =
                bean.getEmpresaMap().get(bean.getEmpresa().getNomeEmpresa());
        if (codigoEmpresa == null) {
			Messages.showError("upload", Constants.EMPRESA_DESPESA_REQUIRED);
			return;
		}

        Empresa empresa = empresaService.findEmpresaById(codigoEmpresa);

        bean.setEmpresa(empresa);

        Date dataSelecionada =
                DateUtil.getDateParse(bean.getSelectedMonthDate(),
                        SIMPLE_DATE_FORMAT);

        UploadArquivo upload =
                uploadDespesaService.uploadDespesas(item,
                        getSelectedPadraoArquivo(), empresa, dataSelecionada);

        bean.setDataRemocao(bean.getSelectedMonthDate());

        if (upload.getUploadDespesas().isEmpty()) {
            upload.setUploadDespesas(null);
        }

        bean.setUploadArquivo(upload);
        bean.setUploadItem(item);
        setUploadErrorList(upload);
    }

    /**
     * Cria a lista de erros do upload e seta no bean.
     * 
     * @param upload
     *            arquivo upload realizado.
     */
    private void setUploadErrorList(final UploadArquivo upload) {

        String textoErro = upload.getTextoErro();
        if (textoErro != null && !"".equals(textoErro)) {
            String[] arrayError = textoErro.split("\n");

            bean.setErrorList(Arrays.asList(arrayError));
        } else {
            bean.setErrorList(null);
        }
    }

    /**
     * Pega o padrao arquivo selecionado.
     * 
     * @return retorna o pradrão de arquivo selecionado.
     */
    private PadraoArquivo getSelectedPadraoArquivo() {
        Long id =
                bean.getPadraoArquivoMap().get(
                        bean.getPadraoArquivo().getNomePadraoArquivo());

        PadraoArquivo padraoArq = null;
        if (id != null) {
            padraoArq = padraoArquivoService.findPadraoArquivoById(id);

            bean.setPadraoArquivo(padraoArq);
        }

        return padraoArq;
    }

    /**
     * Salva o upload do arquivo de despesas.
     */
    public void saveUploadDespesa() {
        try {

            Empresa empresa =
                    empresaService.findEmpresaById(bean.getEmpresa()
                            .getCodigoEmpresa());

            Date dataSelecionada =
                    DateUtil.getDateParse(bean.getDataRemocao(),
                            SIMPLE_DATE_FORMAT);

            List<UploadDespesa> uploadDespesas =
                    uploadDespesaService.findUploadDespesasByDataMesAndEmpresa(
                            dataSelecionada, empresa);

            uploadDespesaService.removeListUploadDespesa(uploadDespesas);

            uploadDespesaService.saveUploadFile(bean.getUploadArquivo(), bean
                    .getUploadItem().getData());
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }

        Messages.showSucess("saveUploadDespesa",
                Constants.DEFAULT_MSG_SUCCESS_SAVE,
                Constants.ENTITY_NAME_PESS_BCO_HRS);

        this.prepareUpload();
    }

    /**
     * Evento que seta a lista de padraoArquivo.
     * 
     * @param e
     *            - evento
     */
    public void changeValuePadraoArquivo(final ValueChangeEvent e) {

        String nomePadraoArq = (String) e.getNewValue();

        bean.getPadraoArquivo().setNomePadraoArquivo(nomePadraoArq);
    }

    /**
     * Evento que seta a lista de empresas.
     * 
     * @param e
     *            - evento
     */
    public void changeValueEmpresa(final ValueChangeEvent e) {

        String nomeEmpresa = (String) e.getNewValue();

        bean.getEmpresa().setNomeEmpresa(nomeEmpresa);
    }

    /**
     * Evento que seta a lista de grupoCusto.
     * 
     * @param e
     *            - evento
     */
    public void changeValueGrupoCusto(final ValueChangeEvent e) {

        String nomeGrupoCusto = (String) e.getNewValue();

        bean.getGrupoCusto().setNomeGrupoCusto(nomeGrupoCusto);
    }

    /**
     * Evento que seta a lista de tipoDespesa.
     * 
     * @param e
     *            - evento
     */
    public void changeValueTipoDespesa(final ValueChangeEvent e) {

        String nomeTipoDespesa = (String) e.getNewValue();

        bean.getTipoDespesa().setNomeTipoDespesa(nomeTipoDespesa);
    }

    /**
     * Evento que seta a lista de empresas.
     * 
     * @param e
     *            - evento
     */
    public void changeValueDate(final ValueChangeEvent e) {

        String data = (String) e.getNewValue();

        bean.setSelectedMonthDate(data);
    }

    /**
     * Evento que seta a lista de empresas.
     * 
     * @param e
     *            - evento
     */
    public void changeValueEmpresaForm(final ValueChangeEvent e) {

        String nomeEmpresa = (String) e.getNewValue();

        bean.getUploadDespesa().getEmpresa().setNomeEmpresa(nomeEmpresa);
    }

    /**
     * Evento que seta a lista de grupoCusto.
     * 
     * @param e
     *            - evento
     */
    public void changeValueGrupoCustoForm(final ValueChangeEvent e) {

        String nomeGrupoCusto = (String) e.getNewValue();

        bean.getUploadDespesa().getGrupoCusto().setNomeGrupoCusto(
                nomeGrupoCusto);
    }

    /**
     * Evento que seta a lista de tipoDespesa.
     * 
     * @param e
     *            - evento
     */
    public void changeValueTipoDespesaForm(final ValueChangeEvent e) {

        String nomeTipoDespesa = (String) e.getNewValue();

        bean.getUploadDespesa().getTipoDespesa().setNomeTipoDespesa(
                nomeTipoDespesa);
    }

    /**
     * Evento que seta a lista de padraoArquivo.
     * 
     * @param e
     *            - evento
     */
    public void changeValueMoedaForm(final ValueChangeEvent e) {

        String nomeMoeda = (String) e.getNewValue();

        bean.getUploadDespesa().getMoeda().setNomeMoeda(nomeMoeda);
    }

    /**
     * Evento que seta a lista de grupoCusto.
     * 
     * @param e
     *            - evento
     */
    public void changeValueGrupoCustoList(final ValueChangeEvent e) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();
        String codigo = myRequest.getParameter("grupoCustoP");

        UploadDespesa uploadDespesa =
                bean.getUploadArquivo().getUploadDespesas().get(
                        Integer.valueOf(codigo) - 1);

        String nomeGrupoCusto = (String) e.getNewValue();

        Long codigoGrupoCusto = bean.getGrupoCustoMap().get(nomeGrupoCusto);

        uploadDespesa.setGrupoCusto(grupoCustoService
                .findGrupoCustoById(codigoGrupoCusto));

        uploadDespesa.setIndicadorOrigem(BundleUtil
                .getBundle(Constants.INPUT_INDICATOR_MANUAL));

    }
    
    /**
     * Evento que seta a lista de contratoPratica.
     * 
     * @param e
     *            - evento
     */
    public void changeValueContratosPratica(final ValueChangeEvent e) {
    	
    	FacesContext facesContext = FacesContext.getCurrentInstance();
    	HttpServletRequest myRequest =
    			(HttpServletRequest) facesContext.getExternalContext()
    			.getRequest();
    	String codigo = myRequest.getParameter("contratoPraticaP");
    	
    	UploadDespesa uploadDespesa =
    			bean.getUploadArquivo().getUploadDespesas().get(
    					Integer.valueOf(codigo) - 1);
    	
    	String nomeContratoPratica = (String) e.getNewValue();
    	
		Long codigoContratoPratica = bean.getContratoPraticaMap().get(
				nomeContratoPratica);
    	
		uploadDespesa.setContratoPratica(contratoPraticaService
				.findContratoPraticaById(codigoContratoPratica));
    	
    	uploadDespesa.setIndicadorOrigem(BundleUtil
    			.getBundle(Constants.INPUT_INDICATOR_MANUAL));
    	
    }

    /**
     * Evento que seta a lista de tipoDespesa.
     * 
     * @param e
     *            - evento
     */
    public void changeValueTipoDespesaList(final ValueChangeEvent e) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();
        String codigo = myRequest.getParameter("tipoDespesaP");

        UploadDespesa uploadDespesa =
                bean.getUploadArquivo().getUploadDespesas().get(
                        Integer.valueOf(codigo) - 1);

        String nomeTipoDespesa = (String) e.getNewValue();

        Long codigoTipoDespesa = bean.getTipoDespesaMap().get(nomeTipoDespesa);

        uploadDespesa.setTipoDespesa(tipoDespesaService
                .findTipoDespesaById(codigoTipoDespesa));

        uploadDespesa.setIndicadorOrigem(BundleUtil
                .getBundle(Constants.INPUT_INDICATOR_MANUAL));

    }

    /**
     * Evento que seta a lista de padraoArquivo.
     * 
     * @param e
     *            - evento
     */
    public void changeValueMoedaList(final ValueChangeEvent e) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletRequest myRequest =
                (HttpServletRequest) facesContext.getExternalContext()
                        .getRequest();
        String codigo = myRequest.getParameter("moedaP");

        UploadDespesa uploadDespesa =
                bean.getUploadArquivo().getUploadDespesas().get(
                        Integer.valueOf(codigo) - 1);

        String nomeMoeda = (String) e.getNewValue();

        Long codigoMoeda = bean.getMoedaMap().get(nomeMoeda);

        uploadDespesa.setMoeda(moedaService.findMoedaById(codigoMoeda));

        uploadDespesa.setIndicadorOrigem(BundleUtil
                .getBundle(Constants.INPUT_INDICATOR_MANUAL));
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareSearch() {
        bean.reset();
        this.loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        this.loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());
        this.loadContratoPraticaList(contratoPraticaService.findContratoPraticaAllCompleteAndActive());
        this.loadTipoDespesaList(tipoDespesaService.findTipoDespesaAllActive());

        return OUTCOME_DESPESA_SEARCH;
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     */
    public void findByFilter() {
        UploadDespesa filter = new UploadDespesa();
        filter.setEmpresa(new Empresa());
        filter.setGrupoCusto(new GrupoCusto());
        filter.setTipoDespesa(new TipoDespesa());
        Long codigoEmpresa =
                bean.getEmpresaMap().get(bean.getEmpresa().getNomeEmpresa());
        if (codigoEmpresa != null) {
            filter.setEmpresa(empresaService.findEmpresaById(codigoEmpresa));
        } else {
            filter.getEmpresa().setCodigoEmpresa(0L);
        }

        Long codigoGrupoCusto =
                bean.getGrupoCustoMap().get(
                        bean.getGrupoCusto().getNomeGrupoCusto());
        if (codigoGrupoCusto != null) {
            filter.setGrupoCusto(grupoCustoService
                    .findGrupoCustoById(codigoGrupoCusto));
        } else {
            filter.getGrupoCusto().setCodigoGrupoCusto(0L);
        }

        Long codigoTipoDespesa =
                bean.getTipoDespesaMap().get(
                        bean.getTipoDespesa().getNomeTipoDespesa());
        if (codigoTipoDespesa != null) {
            filter.setTipoDespesa(tipoDespesaService
                    .findTipoDespesaById(codigoTipoDespesa));
        } else {
            filter.getTipoDespesa().setCodigoTipoDespesa(0L);
        }

        bean.setUploadDespesaFilter(filter);
        bean.setResultList(uploadDespesaService.findUploadDespesasByFilter(bean
                .getDataInicio(), bean.getDataFim(), bean.getDescricao(),
                filter));

        if (bean.getResultList().isEmpty()) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            - entidade com o filtro
     */
    public void findByFilter(final UploadDespesa filter) {
        bean.setResultList(uploadDespesaService.findUploadDespesasByFilter(bean
                .getDataInicio(), bean.getDataFim(), bean.getDescricao(),
                filter));
    }

    /**
     * Popula a lista de Padrao de Arquivo para combobox.
     * 
     * @param padraoArquivos
     *            lista de PardaoArquivo.
     * 
     */
    private void loadPadraoArquivoList(final List<PadraoArquivo> padraoArquivos) {

        Map<String, Long> padraoArquivoMap = new HashMap<String, Long>();
        List<String> padraoArquivoList = new ArrayList<String>();

        for (PadraoArquivo pr : padraoArquivos) {
            padraoArquivoMap.put(pr.getNomePadraoArquivo(), pr
                    .getCodigoPadraoArquivo());
            padraoArquivoList.add(pr.getNomePadraoArquivo());
        }

        if (!padraoArquivos.isEmpty()) {
            bean.getPadraoArquivo().setNomePadraoArquivo(
                    padraoArquivos.get(0).getNomePadraoArquivo());
        }

        bean.setPadraoArquivoMap(padraoArquivoMap);
        bean.setPadraoArquivoList(padraoArquivoList);
    }

    /**
     * Popula a lista de Empresa para combobox.
     * 
     * @param empresas
     *            lista de Empresa.
     * 
     */
    private void loadEmpresaList(final List<Empresa> empresas) {

        Map<String, Long> empresaMap = new HashMap<String, Long>();
        List<String> empresaList = new ArrayList<String>();

        for (Empresa empresa : empresas) {
            empresaMap
                    .put(empresa.getNomeEmpresa(), empresa.getCodigoEmpresa());
            empresaList.add(empresa.getNomeEmpresa());
        }

        bean.setEmpresaMap(empresaMap);
        bean.setEmpresaList(empresaList);
    }

    /**
     * Popula a lista de Grupo de Custo para combobox.
     * 
     * @param grupoCustos
     *            lista de Grupos de Custo.
     * 
     */
    private void loadGrupoCustoList(final List<GrupoCusto> grupoCustos) {

        Map<String, Long> grupoCustoMap = new HashMap<String, Long>();
        List<String> grupoCustoList = new ArrayList<String>();

        for (GrupoCusto grupoCusto : grupoCustos) {
            grupoCustoMap.put(grupoCusto.getNomeGrupoCusto(), grupoCusto
                    .getCodigoGrupoCusto());
            grupoCustoList.add(grupoCusto.getNomeGrupoCusto());
        }

        bean.setGrupoCustoMap(grupoCustoMap);
        bean.setGrupoCustoList(grupoCustoList);
    }
    
    /**
     * Popula a lista de Grupo de Custo para combobox.
     * 
     * @param grupoCustos
     *            lista de Grupos de Custo.
     * 
     */
    private void loadContratoPraticaList(final List<ContratoPratica> contratosPratica) {
    	
    	Map<String, Long> contratosPraticaMap = new HashMap<String, Long>();
    	List<String> contratoPraticaList = new ArrayList<String>();
    	
    	for (ContratoPratica contratoPratica : contratosPratica) {
    		contratosPraticaMap.put(contratoPratica.getNomeContratoPratica(), contratoPratica.getCodigoContratoPratica());
    		contratoPraticaList.add(contratoPratica.getNomeContratoPratica());
    	}
    	
    	bean.setContratoPraticaMap(contratosPraticaMap);
    	bean.setContratoPraticaList(contratoPraticaList);
    }

    /**
     * Popula a lista de Tipo de Despesa para combobox.
     * 
     * @param tipoDespesas
     *            lista de Tipos de Despesa.
     * 
     */
    private void loadTipoDespesaList(final List<TipoDespesa> tipoDespesas) {

        Map<String, Long> tipoDespesaMap = new HashMap<String, Long>();
        List<String> tipoDespesaList = new ArrayList<String>();

        for (TipoDespesa tipoDespesa : tipoDespesas) {
            tipoDespesaMap.put(tipoDespesa.getNomeTipoDespesa(), tipoDespesa
                    .getCodigoTipoDespesa());
            tipoDespesaList.add(tipoDespesa.getNomeTipoDespesa());
        }

        bean.setTipoDespesaMap(tipoDespesaMap);
        bean.setTipoDespesaList(tipoDespesaList);
    }

    /**
     * Popula a lista de Moedas para combobox.
     * 
     * @param moedas
     *            lista de moedas.
     * 
     */
    private void loadMoedaList(final List<Moeda> moedas) {

        Map<String, Long> moedaMap = new HashMap<String, Long>();
        List<String> moedaList = new ArrayList<String>();

        for (Moeda moeda : moedas) {
            moedaMap.put(moeda.getNomeMoeda(), moeda.getCodigoMoeda());
            moedaList.add(moeda.getNomeMoeda());
        }

        bean.setMoedaMap(moedaMap);
        bean.setMoedaList(moedaList);
    }

    /**
     * Recupera o arquivo do filesystem para download.
     */
    public void downloadFile() {
        UploadDespesa despesa = bean.getUploadDespesa();

        // Write the output to a file
        String fileName = despesa.getUploadArquivo().getNomeArquivo();

        String path =
                (String) systemProperties
                        .get("upload.despesa.destination.path");

        String contentType = "application/csv";

        uploadArquivoService.downloadFile(path, fileName, contentType);
    }

    /**
     * Recupera o arquivo do filesystem para download.
     */
    public void getSampleFile() {
        // Write the output to a file
        String fileName = "";

        Long codigoPadraoArquivo =
                bean.getPadraoArquivoMap().get(
                        bean.getPadraoArquivo().getNomePadraoArquivo());

        if (codigoPadraoArquivo.equals(new Long(1))) {
            fileName = "sample_upload_despesa_open_office.csv";
        } else if (codigoPadraoArquivo.equals(new Long(2))) {
            fileName = "sample_upload_despesa_excel.csv";
        }

        String path =
                (String) systemProperties
                        .get("upload.despesa.destination.path");

        String contentType = "application/csv";

        uploadArquivoService.downloadFile(path, fileName, contentType);

    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        this.loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        this.loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());
        this.loadContratoPraticaList(contratoPraticaService.findContratoPraticaAllCompleteAndActive());
        this.loadTipoDespesaList(tipoDespesaService.findTipoDespesaAllActive());
        this.loadMoedaList(moedaService.findMoedaAll());

        // carrega o combo com as datas possiveis para validacao
        bean.setDataMesList(DateUtil.getDateMonthListString(moduloService
                .getClosingDateExpense(), SIMPLE_DATE_FORMAT));

        bean.setSelectedMonthDate(bean.getDataMesList().get(
                bean.getDataMesList().size() - 1));

        bean.setIsUpdate(Boolean.valueOf(false));

        return OUTCOME_DESPESA_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        UploadDespesa uploadDespesa = bean.getUploadDespesa();

        Long codigoEmpresa =
                bean.getEmpresaMap().get(
                        bean.getUploadDespesa().getEmpresa().getNomeEmpresa());

        Empresa empresa = empresaService.findEmpresaById(codigoEmpresa);

        Long codigoGrupoCusto =
                bean.getGrupoCustoMap().get(
                        bean.getUploadDespesa().getGrupoCusto()
                                .getNomeGrupoCusto());

        GrupoCusto grupoCusto =
                grupoCustoService.findGrupoCustoById(codigoGrupoCusto);

        Long codigoTipoDespesa =
                bean.getTipoDespesaMap().get(
                        bean.getUploadDespesa().getTipoDespesa()
                                .getNomeTipoDespesa());

        TipoDespesa tipoDespesa =
                tipoDespesaService.findTipoDespesaById(codigoTipoDespesa);

        Long codigoMoeda =
                bean.getMoedaMap().get(
                        bean.getUploadDespesa().getMoeda().getNomeMoeda());

        Moeda moeda = moedaService.findMoedaById(codigoMoeda);

        uploadDespesa.setEmpresa(empresa);
        uploadDespesa.setGrupoCusto(grupoCusto);
        uploadDespesa.setTipoDespesa(tipoDespesa);
        uploadDespesa.setMoeda(moeda);

        Date dataSelecionada =
                DateUtil.getDateParse(bean.getSelectedMonthDate(),
                        SIMPLE_DATE_FORMAT);

        uploadDespesa.setDataLancamento(dataSelecionada);

        if (uploadDespesaService.createUploadDespesa(uploadDespesa)) {
            bean.reset();
        }
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        uploadDespesaService.findUploadDespesaById(bean.getUploadDespesa()
                .getCodigoUploadDespesa());
        this.loadEmpresaList(empresaService.findEmpresaAllSubsidiary());
        this.loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());
        this.loadContratoPraticaList(contratoPraticaService.findContratoPraticaAllCompleteAndActive());
        this.loadTipoDespesaList(tipoDespesaService.findTipoDespesaAllActive());
        this.loadMoedaList(moedaService.findMoedaAll());

        bean.getUploadDespesa().setEmpresa(
                empresaService.findEmpresaById(bean.getUploadDespesa()
                        .getEmpresa().getCodigoEmpresa()));
        bean.getUploadDespesa().setGrupoCusto(
                grupoCustoService.findGrupoCustoById(bean.getUploadDespesa()
                        .getGrupoCusto().getCodigoGrupoCusto()));
        bean.getUploadDespesa().setTipoDespesa(
                tipoDespesaService.findTipoDespesaById(bean.getUploadDespesa()
                        .getTipoDespesa().getCodigoTipoDespesa()));
        bean.getUploadDespesa().setMoeda(
                moedaService.findMoedaById(bean.getUploadDespesa().getMoeda()
                        .getCodigoMoeda()));
		bean.getUploadDespesa().setContratoPratica(
				contratoPraticaService.findContratoPraticaById(bean
						.getUploadDespesa().getContratoPratica()
						.getCodigoContratoPratica()));

        // carrega o combo com as datas possiveis para validacao
        bean.setDataMesList(DateUtil.getDateMonthListString(moduloService
                .getClosingDateExpense(), SIMPLE_DATE_FORMAT));

        bean.setSelectedMonthDate(DateUtil.getStringDate(bean
                .getUploadDespesa().getDataLancamento(), SIMPLE_DATE_FORMAT));

        bean.setIsUpdate(Boolean.valueOf(true));
        return OUTCOME_DESPESA_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        UploadDespesa uploadDespesa = this.refreshAttributesValues();

        Date dataSelecionada =
                DateUtil.getDateParse(bean.getSelectedMonthDate(),
                        SIMPLE_DATE_FORMAT);

        uploadDespesa.setDataLancamento(dataSelecionada);
        uploadDespesa.setUploadArquivo(null);

        uploadDespesaService.updateUploadDespesa(uploadDespesa);

        Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                Constants.ENTITY_NAME_DESPESA);
        findByFilter(bean.getUploadDespesaFilter());
        return OUTCOME_DESPESA_SEARCH;

    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        uploadDespesaService.findUploadDespesaById(bean.getUploadDespesa()
                .getCodigoUploadDespesa());
        return OUTCOME_DESPESA_DELETE;
    }

    /**
     * Prepara a tela de visualizacao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareView() {
        uploadDespesaService.findUploadDespesaById(bean.getUploadDespesa()
                .getCodigoUploadDespesa());
        return OUTCOME_DESPESA_PREVIEW;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        uploadDespesaService.removeUploadDespesa(uploadDespesaService
                .findUploadDespesaById(bean.getUploadDespesa()
                        .getCodigoUploadDespesa()));
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_DESPESA);
        findByFilter(bean.getUploadDespesaFilter());
        return OUTCOME_DESPESA_SEARCH;
    }

    /**
     * Atualiza as referencias dos atributos que sao entidades.
     * 
     * @return uploadDespesa da tela com os atributos (entidades) atualizados.
     */
    private UploadDespesa refreshAttributesValues() {
        UploadDespesa uploadDespesa = bean.getUploadDespesa();

        Long codigoEmpresa =
                bean.getEmpresaMap().get(
                        uploadDespesa.getEmpresa().getNomeEmpresa());
        uploadDespesa.setEmpresa(empresaService.findEmpresaById(codigoEmpresa));

        Long codigoGrupoCusto =
                bean.getGrupoCustoMap().get(
                        uploadDespesa.getGrupoCusto().getNomeGrupoCusto());
        uploadDespesa.setGrupoCusto(grupoCustoService
                .findGrupoCustoById(codigoGrupoCusto));

        Long codigoTipoDespesa =
                bean.getTipoDespesaMap().get(
                        uploadDespesa.getTipoDespesa().getNomeTipoDespesa());
        uploadDespesa.setTipoDespesa(tipoDespesaService
                .findTipoDespesaById(codigoTipoDespesa));

        Long codigoMoeda =
                bean.getMoedaMap().get(uploadDespesa.getMoeda().getNomeMoeda());
        uploadDespesa.setMoeda(moedaService.findMoedaById(codigoMoeda));

        return uploadDespesa;
    }

    /**
     * Remove todas as despesas selecionadas.
     */
    public void deleteDespesaAll() {
        // verifica se algum item foi selecionado
        if (isSomeDespesaSelected()) {
            List<UploadDespesa> uploadDespesas = bean.getResultList();

            uploadDespesaService
                    .removeListUploadDespesaSelected(uploadDespesas);

            this.findByFilter();

            Messages.showSucess("deleteDespesaAll",
                    Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                    Constants.ENTITY_NAME_DESPESA);
        } else {
            Messages.showWarning("deleteDespesaAll",
                    Constants.DEFAULT_MSG_ERROR_SELECT_ITEM);
        }
    }

    /**
     * Verifica se algum item foi selecionado.
     * 
     * @return true se algum item selecionado, caso contrario retorna false.
     */
    private Boolean isSomeDespesaSelected() {
        List<UploadDespesa> despesaList = bean.getResultList();
        for (UploadDespesa uploadDespesa : despesaList) {
            if (uploadDespesa.getIsSelected()) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }
}