package com.ciandt.pms.control.jsf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.security.RolesAllowed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAtividadeService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IPadraoArquivoService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRegistroAtividadeService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.bean.RegistroAtividadeBean;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.Atividade;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.RegistroAtividade;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.util.DateUtil;


/**
 * Define o Controller da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST)
@RolesAllowed({"ROLE_PMS_ADMIN", "ROLE_PMS_PD"})
public class RegistroAtividadeController {

    /** outcome tela criacao da entidade. */
    private static final String OUTCOME_REGISTRO_ATIVIDADE_ADD =
            "registroAtividade_add";

    /** outcome tela alteracao da entidade. */
    private static final String OUTCOME_REGISTRO_ATIVIDADE_EDIT =
            "registroAtividade_edit";

    /** outcome tela remocao da entidade. */
    private static final String OUTCOME_REGISTRO_ATIVIDADE_DELETE =
            "registroAtividade_delete";

    /** outcome tela pesquisa da entidade. */
    private static final String OUTCOME_REGISTRO_ATIVIDADE_SEARCH =
            "registroAtividade_search";

    /** outcome tela upload da entidade. */
    private static final String OUTCOME_REGISTRO_ATIVIDADE_UPLOAD =
            "registroAtividade_upload";

    /** Instancia do servico. */
    @Autowired
    private IRegistroAtividadeService regAtivService;

    /** Instancia do servico ContratoPratica. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do servico GrupoCusto. */
    @Autowired
    private IGrupoCustoService grupoCustoService;

    /** Instancia do servico Pessoa. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico Atividade. */
    @Autowired
    private IAtividadeService atividadeService;

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
    private RegistroAtividadeBean bean = null;

    /**
     * @return the bean
     */
    public RegistroAtividadeBean getBean() {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(final RegistroAtividadeBean bean) {
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

        return OUTCOME_REGISTRO_ATIVIDADE_UPLOAD;
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
    public void uploadRegistroAtividadeListener(final UploadEvent event)
            throws Exception {

        UploadItem item = event.getUploadItem();

        UploadArquivo upload =
                regAtivService.uploadRegistroAtividade(item,
                        getSelectedPadraoArquivo());

        if (upload.getRegistroAtividades().isEmpty()) {
            upload.setRegistroAtividades(null);
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
    public void saveUploadRegistroAtividade() {
        try {
            regAtivService.saveUploadFile(bean.getUploadArquivo(), bean
                    .getUploadItem().getData());
        } catch (IOException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }

        Messages.showSucess("saveUploadBancoHoras",
                Constants.DEFAULT_MSG_SUCCESS_SAVE,
                Constants.ENTITY_NAME_REGISTRO_ATIVIDADE);

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
     * Prepara a tela de pesquisa da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareSearch() {
        bean.reset();
        this.loadContratoPraticaList(contratoPraticaService
                .findContratoPraticaAllComplete());

        this.loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());

        this.loadAtividadeList(atividadeService.findAtividadeAllActive());
        return OUTCOME_REGISTRO_ATIVIDADE_SEARCH;
    }

    /**
     * Prepara a tela de insercao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareCreate() {
        bean.reset();
        this.loadContratoPraticaList(contratoPraticaService
                .findContratoPraticaAllComplete());

        this.loadGrupoCustoList(grupoCustoService.findGrupoCustoAllActive());

        this.loadAtividadeList(atividadeService.findAtividadeAllActive());
        bean.setIsUpdate(Boolean.valueOf(false));
        return OUTCOME_REGISTRO_ATIVIDADE_ADD;
    }

    /**
     * Insere uma entidade.
     * 
     */
    public void create() {
        RegistroAtividade to = this.refreshAttributesValues();

        // Boolean existsRegAtiv = this.existsRegAtiv(to);
        // if (!existsRegAtiv) {

        if (to != null) {
            if (regAtivService.createRegistroAtividade(to)) {
                bean.resetTo();
                bean.resetDataMes();
            }
        } else {
            Messages
                    .showError(
                            "create",
                            Constants.MSG_ERROR_REGISTRO_ATIVIDADE_CONTRATO_PRATICA_AND_GRUPO_CUSTO_NULL);
        }
        // } else {
        // Messages.showError("create",
        // Constants.MSG_ERROR_REGISTRO_ATIVIDADE_ALREADY_EXISTS);
        // }
    }

    /**
     * Prepara a tela de edicao da entidade.
     * 
     * @return pagina de destino
     */
    public String prepareUpdate() {
        findById(bean.getCurrentRowId());
        this.loadContratoPraticaList(contratoPraticaService
                .findContratoPraticaAllComplete());
        this.loadAtividadeList(atividadeService.findAtividadeAllActive());
        bean.setIsUpdate(Boolean.valueOf(true));
        return OUTCOME_REGISTRO_ATIVIDADE_EDIT;
    }

    /**
     * Executa um update da entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String update() {
        RegistroAtividade to = this.refreshAttributesValues();

        if (to != null) {

            Boolean existsRegAtiv = this.existsRegAtiv(to);
            if (!existsRegAtiv) {
                regAtivService.updateRegistroAtividade(to);

                Messages.showSucess("update",
                        Constants.DEFAULT_MSG_SUCCESS_UPDATE,
                        Constants.ENTITY_NAME_REGISTRO_ATIVIDADE);
                bean.resetTo();
                findByFilter();
                return OUTCOME_REGISTRO_ATIVIDADE_SEARCH;
            } else {
                Messages.showError("update",
                        Constants.MSG_ERROR_REGISTRO_ATIVIDADE_ALREADY_EXISTS);
            }
        } else {
            Messages
                    .showError(
                            "create",
                            Constants.MSG_ERROR_REGISTRO_ATIVIDADE_CONTRATO_PRATICA_AND_GRUPO_CUSTO_NULL);
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
        return OUTCOME_REGISTRO_ATIVIDADE_DELETE;
    }

    /**
     * Deleta uma entidade.
     * 
     * @return pagina de destino
     * 
     */
    public String remove() {
        regAtivService.removeRegistroAtividade(regAtivService
                .findRegistroAtividadeById(bean.getTo()
                        .getCodigoRegistroAtividade()));
        Messages.showSucess("remove", Constants.DEFAULT_MSG_SUCCESS_REMOVE,
                Constants.ENTITY_NAME_REGISTRO_ATIVIDADE);
        bean.resetTo();
        findByFilter();
        return OUTCOME_REGISTRO_ATIVIDADE_SEARCH;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     */
    public void findById(final Long id) {
        bean.setTo(regAtivService.findRegistroAtividadeById(id));
        if (bean.getTo() == null
                || bean.getTo().getCodigoRegistroAtividade() == null) {
            Messages.showWarning("findById",
                    Constants.DEFAULT_MSG_WARNG_NO_RESULT);
        }
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     */
    public void findByFilter() {
        RegistroAtividade filter = bean.getFilter();

        Long codigoContratoPratica =
                bean.getContratoPraticaMap().get(
                        filter.getContratoPratica().getNomeContratoPratica());
        if (codigoContratoPratica != null) {
            filter.setContratoPratica(contratoPraticaService
                    .findContratoPraticaById(codigoContratoPratica));
        } else {
            filter.getContratoPratica().setCodigoContratoPratica(0L);
        }

        Long codigoGrupoCusto =
                bean.getGrupoCustoMap().get(
                        filter.getGrupoCusto().getNomeGrupoCusto());
        if (codigoGrupoCusto != null) {
            filter.setGrupoCusto(grupoCustoService
                    .findGrupoCustoById(codigoGrupoCusto));
        } else {
            filter.getGrupoCusto().setCodigoGrupoCusto(0L);
        }

        String codigoLogin = filter.getPessoa().getCodigoLogin();
        if (!StringUtils.isEmpty(codigoLogin)) {
            filter.setPessoa(pessoaService.findPessoaByLogin(codigoLogin));
        } else {
            filter.getPessoa().setCodigoPessoa(0L);
        }

        Long codigoAtividade =
                bean.getAtividadeMap().get(
                        filter.getAtividade().getNomeAtividade());
        if (codigoAtividade != null) {
            filter.setAtividade(atividadeService
                    .findAtividadeById(codigoAtividade));
        } else {
            filter.getAtividade().setCodigoAtividade(0L);
        }

        filter.setDataMesChp(DateUtil.getDate(bean.getMonthBeg(), bean
                .getYearBeg()));

        bean
                .setResultList(regAtivService
                        .findRegistroAtividadeByFilter(filter));

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
     * Verifica se existe RegistroAtividade com o mesmo ContratoPratica, Pessoa,
     * Atividade e data.
     * 
     * @param to
     *            - objeto PessoaBancoHoras populado
     * @return true caso existir o registro no banco, caso contrario false
     */
    public Boolean existsRegAtiv(final RegistroAtividade to) {
        Long codigoRegistroAtividade = to.getCodigoRegistroAtividade();
        if (codigoRegistroAtividade == null) {
            codigoRegistroAtividade = Long.valueOf(0);
        }

        RegistroAtividade result =
                regAtivService.findRegistroAtividadeUnique(to);

        if (result != null
                && result.getCodigoRegistroAtividade().compareTo(
                        codigoRegistroAtividade) != 0) {
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
     * Atualiza as referencias dos atributos que sao entidades.
     * 
     * @return to (RegistroAtividade) da tela com os atributos (entidades)
     *         atualizados.
     */
    private RegistroAtividade refreshAttributesValues() {
        RegistroAtividade to = bean.getTo();

        ContratoPratica contratoPratica = null;

        if (to.getContratoPratica().getNomeContratoPratica() != null
                && to.getContratoPratica().getNomeContratoPratica() != "") {
            Long codigoContratoPratica =
                    bean.getContratoPraticaMap().get(
                            to.getContratoPratica().getNomeContratoPratica());
            contratoPratica =
                    contratoPraticaService
                            .findContratoPraticaById(codigoContratoPratica);
        }

        GrupoCusto grupoCusto = null;

        if (to.getGrupoCusto().getNomeGrupoCusto() != null
                && to.getGrupoCusto().getNomeGrupoCusto() != "") {
            Long codigoGrupoCusto =
                    bean.getGrupoCustoMap().get(
                            to.getGrupoCusto().getNomeGrupoCusto());

            grupoCusto = grupoCustoService.findGrupoCustoById(codigoGrupoCusto);

        }

        if (contratoPratica == null && grupoCusto == null) {
            return null;
        } else {

            // Somente um dos dois deve ser persistido (ContratoPratica e
            // GrupoCusto). Se os dois forem selecionados, ignorar o GrupoCusto
            if (contratoPratica != null) {
                to.setContratoPratica(contratoPratica);
                to.setGrupoCusto(null);
            } else {
                to.setContratoPratica(contratoPratica);
                to.setGrupoCusto(grupoCusto);
            }

            to.setPessoa(pessoaService.findPessoaByLogin(to.getPessoa()
                    .getCodigoLogin()));

            Long codigoAtividade =
                    bean.getAtividadeMap().get(
                            to.getAtividade().getNomeAtividade());
            to
                    .setAtividade(atividadeService
                            .findAtividadeById(codigoAtividade));

            return to;
        }
    }

    /**
     * Popula a lista de ContratoPratica para combobox.
     * 
     * @param contratosPratica
     *            lista de ContratoPratica.
     * 
     */
    private void loadContratoPraticaList(
            final List<ContratoPratica> contratosPratica) {

        Map<String, Long> contratoPraticaMap = new HashMap<String, Long>();
        List<String> contratoPraticaList = new ArrayList<String>();

        for (ContratoPratica cp : contratosPratica) {
            contratoPraticaMap.put(cp.getNomeContratoPratica(), cp
                    .getCodigoContratoPratica());
            contratoPraticaList.add(cp.getNomeContratoPratica());
        }

        bean.setContratoPraticaMap(contratoPraticaMap);
        bean.setContratoPraticaList(contratoPraticaList);
    }

    /**
     * Popula a lista de GrupoCusto para combobox.
     * 
     * @param gruposCusto
     *            lista de GrupoCusto.
     * 
     */
    private void loadGrupoCustoList(final List<GrupoCusto> gruposCusto) {

        Map<String, Long> grupoCustoMap = new HashMap<String, Long>();
        List<String> grupoCustoList = new ArrayList<String>();

        for (GrupoCusto gc : gruposCusto) {
            grupoCustoMap.put(gc.getNomeGrupoCusto(), gc.getCodigoGrupoCusto());
            grupoCustoList.add(gc.getNomeGrupoCusto());
        }

        bean.setGrupoCustoMap(grupoCustoMap);
        bean.setGrupoCustoList(grupoCustoList);
    }

    /**
     * Popula a lista de Atividade para combobox.
     * 
     * @param atividades
     *            lista de Atividade.
     * 
     */
    private void loadAtividadeList(final List<Atividade> atividades) {

        Map<String, Long> atividadeMap = new HashMap<String, Long>();
        List<String> atividadeList = new ArrayList<String>();

        for (Atividade a : atividades) {
            atividadeMap.put(a.getNomeAtividade(), a.getCodigoAtividade());
            atividadeList.add(a.getNomeAtividade());
        }

        bean.setAtividadeMap(atividadeMap);
        bean.setAtividadeList(atividadeList);
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
            fileName = "sample_upload_plantao_open_office.csv";
        } else if (codigoPadraoArquivo.equals(new Long(2))) {
            fileName = "sample_upload_plantao_excel.csv";
        }

        String path =
                (String) systemProperties
                        .get("upload.plantao.destination.path");

        String contentType = "application/csv";

        uploadArquivoService.downloadFile(path, fileName, contentType);

    }

    /**
     * Recupera o arquivo do filesystem para download.
     */
    public void downloadFile() {
        RegistroAtividade registroAtividade = bean.getTo();

        // Write the output to a file
        String fileName = registroAtividade.getUploadArquivo().getNomeArquivo();

        String path =
                (String) systemProperties
                        .get("upload.plantao.destination.path");

        String contentType = "application/csv";

        uploadArquivoService.downloadFile(path, fileName, contentType);
    }

    /**
     * Seta o nome do contrato pratica.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void setContratoPratica(final FacesContext context,
            final UIComponent component, final Object value) {

        String nomeContratoPratica = (String) value;

        if (nomeContratoPratica != null && nomeContratoPratica != "") {
            bean.getTo().getContratoPratica().setNomeContratoPratica(
                    nomeContratoPratica);
        } else {
            bean.getTo().setContratoPratica(new ContratoPratica());
        }
    }

    /**
     * Seta o nome do grupo de custo.
     * 
     * @param context
     *            contexto do faces.
     * @param component
     *            componente faces.
     * @param value
     *            valor do componente.
     */
    public void setGrupoCusto(final FacesContext context,
            final UIComponent component, final Object value) {

        String nomeGrupoCusto = (String) value;

        if (nomeGrupoCusto != null) {
            bean.getTo().getGrupoCusto().setNomeGrupoCusto(nomeGrupoCusto);
        } else {
            bean.getTo().setGrupoCusto(new GrupoCusto());
        }
    }

}