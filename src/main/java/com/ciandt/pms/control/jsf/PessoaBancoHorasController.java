package com.ciandt.pms.control.jsf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPadraoArquivoService;
import com.ciandt.pms.business.service.IPessoaBancoHorasService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.bean.PessoaBancoHorasBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBancoHoras;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.util.DateUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 13/08/2010
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"ROLE_PMS_ADMIN", "ROLE_PMS_PD"})
public class PessoaBancoHorasController {

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_PESS_BCO_HRS_ADD =
            "pessoaBancoHoras_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_PESS_BCO_HRS_EDIT =
            "pessoaBancoHoras_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_PESS_BCO_HRS_DELETE =
            "pessoaBancoHoras_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_PESS_BCO_HRS_SEARCH =
            "pessoaBancoHoras_search";

    /** outcome tela upload de banco de horas. */
    private static final String OUTCOME_PESS_BCO_HRS_UPLOAD =
            "pessoaBancoHoras_upload";

    /** Instancia do servico. */
    @Autowired
    private IPessoaBancoHorasService pessBcoHrsService;

    /** Instancia do servico Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do PadraoArquivoService. */
    @Autowired
    private IPadraoArquivoService padraoArquivoService;

    /** Instancia do servico. */
    @Autowired
    private IUploadArquivoService uploadArquivoService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Instancia do bean.
     */
    @Autowired
    private PessoaBancoHorasBean bean = null;

    /**
     * @return the bean
     */
    public PessoaBancoHorasBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final PessoaBancoHorasBean bean) {
        this.bean = bean;
    }

    /**
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareSearch() {
        bean.reset();
        return OUTCOME_PESS_BCO_HRS_SEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        bean.setIsUpdate(Boolean.valueOf(false));
        return OUTCOME_PESS_BCO_HRS_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        PessoaBancoHoras to = bean.getTo();
        to.setPessoa(pessoaService.findPessoaByLogin(to.getPessoa()
                .getCodigoLogin()));
        to.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg()));

        // Boolean existsPessBcoHrs = this.existsPessBcoHrs(to);
        // if (!existsPessBcoHrs) {
        if (pessBcoHrsService.createPessBcoHrs(to)) {
            bean.resetTo();
            bean.resetDataMes();
        }
        // } else {
        // Messages.showError("create",
        // Constants.MSG_ERROR_PESS_BCO_HRS_ALREADY_EXISTS);
        // }
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        findById(bean.getCurrentRowId());
        bean.setIsUpdate(Boolean.valueOf(true));

        Date dataMes = bean.getTo().getDataMes();
        bean.setMonthBeg(DateUtil.getMonthString(dataMes));
        bean.setYearBeg(DateUtil.getYearString(dataMes));

        return OUTCOME_PESS_BCO_HRS_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        PessoaBancoHoras to = bean.getTo();
        to.setPessoa(pessoaService.findPessoaByLogin(to.getPessoa()
                .getCodigoLogin()));
        to.setDataMes(DateUtil.getDate(bean.getMonthBeg(), bean.getYearBeg()));

        Boolean existsPessBcoHrs = this.existsPessBcoHrs(to);
        if (!existsPessBcoHrs) {
            pessBcoHrsService.updatePessBcoHrs(to);

            Messages.showSucess("update", Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                    Constants.ENTITY_NAME_PESS_BCO_HRS);
            bean.resetTo();
            findByFilter();
            return OUTCOME_PESS_BCO_HRS_SEARCH;
        } else {
            Messages.showError("update",
                    Constants.MSG_ERROR_PESS_BCO_HRS_ALREADY_EXISTS);
        }

        return null;
    }

    /**
     * Prepara a tela de remocao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareRemove() {
        findById(bean.getCurrentRowId());
        return OUTCOME_PESS_BCO_HRS_DELETE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        pessBcoHrsService.removePessBcoHrs(pessBcoHrsService
                .findPessBcoHrsById(bean.getTo().getCodigoPessoaBcoHrs()));
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_PESS_BCO_HRS);
        bean.resetTo();
        findByFilter();
        return OUTCOME_PESS_BCO_HRS_SEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(pessBcoHrsService.findPessBcoHrsById(id));
        if (bean.getTo() == null
                || bean.getTo().getCodigoPessoaBcoHrs() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        PessoaBancoHoras filter = bean.getFilter();
        filter.setDataMes(DateUtil.getDate(bean.getMonthBegFilter(), bean
                .getYearBegFilter()));

        bean.setResultList(pessBcoHrsService.findPessBcoHrsByFilter(filter));

        if (bean.getResultList().size() == 0) {
            Messages.showWarning("findByFilter",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }

        // volta para a primeira pagina da paginacao
        bean.setCurrentPageId(0);
    }

    /**
     * Realiza a validaçao do campo Login.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void validatePessoa(final FacesContext context,
            final UIComponent component, final Object value) {

        String login = (String) value;

        if ((login != null) && (!"".equals(login))) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(login);

            if (pessoa == null) {
                String label = (String) component.getAttributes().get("label");
                throw new ValidatorException(Messages.getMessageError(
                        Constants.DEFAULT_MSG_ERROR_NOT_FOUND, label));
            }
        }

    }

    /**
     * Ação utilizada no autocomplete da Pessoa. Retorna uma lista de Pessoas.
     * 
     * @param value
     *            - valor (login) utilizado na busca das Pessoas
     * 
     * @return retorna uma lista de recurso
     */
    public List<Pessoa> autoCompletePessoa(final Object value) {
        return pessoaService.findPessoaByLikeLogin((String) value);
    }

    /**
     * Prepara a tela de load do arquivo.
     * 
     * @return retorna a página de upload.
     */
    public String prepareUpload() {
        bean.reset();

        loadPadraoArquivoList(padraoArquivoService.findPadraoArquivoAll());

        return OUTCOME_PESS_BCO_HRS_UPLOAD;
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
    public void uploadBancoHorasListener(final UploadEvent event)
            throws Exception {

        UploadItem item = event.getUploadItem();

        UploadArquivo upload =
                pessBcoHrsService.uploadBancoHoras(item,
                        getSelectedPadraoArquivo());

        if (upload.getPessoaBancoHoras().isEmpty()) {
            upload.setPessoaBancoHoras(null);
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
     * Salva o upload do arquivo de banco de horas.
     */
    public void saveUploadBancoHoras() {
        try {
            pessBcoHrsService.saveUploadFile(bean.getUploadArquivo(), bean
                    .getUploadItem().getData());
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
     * Verifica se existe PessoaBancoHoras com a mesma Pessoa, data e
     * valorFator.
     * 
     * @param to
     *            - objeto PessoaBancoHoras populado
     * @return true caso existir o registro no banco, caso contrario false
     */
    public Boolean existsPessBcoHrs(final PessoaBancoHoras to) {
        PessoaBancoHoras result = pessBcoHrsService.findPessBcoHrsUnique(to);

        Long codigoPessoaBcoHrs = to.getCodigoPessoaBcoHrs();
        if (codigoPessoaBcoHrs == null) {
            codigoPessoaBcoHrs = Long.valueOf(0);
        }

        if (result != null
                && result.getCodigoPessoaBcoHrs().compareTo(codigoPessoaBcoHrs) != 0) {
            return Boolean.valueOf(true);
        } else {
            return Boolean.valueOf(false);
        }
    }

    /**
     * Popula a lista de GrupoCusto para combobox de Grupo de Custo.
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
     * Recupera o arquivo do filesystem para download.
     */
    public void getSampleFile() {
        // Write the output to a file
        String fileName = "";

        Long codigoPadraoArquivo =
                bean.getPadraoArquivoMap().get(
                        bean.getPadraoArquivo().getNomePadraoArquivo());

        if (codigoPadraoArquivo.equals(new Long(1))) {
            fileName = "sample_upload_pessoa_banco_horas_open_office.csv";
        } else if (codigoPadraoArquivo.equals(new Long(2))) {
            fileName = "sample_upload_pessoa_banco_horas_excel.csv";
        }

        String path =
                (String) systemProperties
                        .get("upload.banco_horas.destination.path");

        String contentType = "application/csv";

        uploadArquivoService.downloadFile(path, fileName, contentType);

    }

    /**
     * Recupera o arquivo do filesystem para download.
     */
    public void downloadFile() {
        PessoaBancoHoras pessoaBancoHoras = bean.getTo();

        // Write the output to a file
        String fileName = pessoaBancoHoras.getUploadArquivo().getNomeArquivo();

        String path =
                (String) systemProperties
                        .get("upload.banco_horas.destination.path");

        String contentType = "application/csv";

        uploadArquivoService.downloadFile(path, fileName, contentType);
    }

}