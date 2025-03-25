package com.ciandt.pms.business.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAtividadeService;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IRegistroAtividadeService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.csv.util.CsvUtil;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.RegistroAtividade;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IRegistroAtividadeDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Service
public class RegistroAtividadeService implements IRegistroAtividadeService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IRegistroAtividadeDao dao;

    /** Instancia do servico PessoaService. */
    @Autowired
    private IPessoaService pessoaService;

    /** Instancia do servico UploadArquivoService. */
    @Autowired
    private IUploadArquivoService uploadArquivoService;

    /** Instancia do servico AtividadeService. */
    @Autowired
    private IAtividadeService atividadeService;

    /** Instancia do servico ContratoPraticaService. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do servico GrupoCustoService. */
    @Autowired
    private IGrupoCustoService grupoCustoService;

    /** Instancia do servico AtividadeService. */
    @Autowired
    private IAtividadeService atvService;

    /** Instancia do servico Modulo. */
    @Autowired
    private IModuloService moduloService;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     * @return retorna true se criado com sucesso, caso contrario retorna false.
     */
    public Boolean createRegistroAtividade(final RegistroAtividade entity) {

        String result = validateRegistroAtividade(entity);
        if (StringUtils.isEmpty(result)) {
            // seta a data mes com o trunk da data de registro da atividade
            entity.setDataMes(DateUtil.getDate(entity
                    .getDataRegistroAtividade()));

            // seta a data mes CHP com o trunk da data de registro da atividade
            // considerando range de 21/mm até 20/mm+1: o custo irá para
            // 01/mm+1/YYYY
            entity.setDataMesChp(DateUtil.getDateTruncChp(entity
                    .getDataRegistroAtividade(), Integer
                    .valueOf(systemProperties
                            .getProperty(Constants.CHP_RANGE_INITIAL_DAY))));

            dao.create(entity);

            Messages.showSucess("createRegistroAtividade",
                    Constants.DEFAULT_MSG_SUCCESS_SAVE,
                    Constants.ENTITY_NAME_REGISTRO_ATIVIDADE);

            return true;
        } else {

            Messages.showError("createRegistroAtividade", result);

            return false;
        }

    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateRegistroAtividade(final RegistroAtividade entity) {
        // seta a data mes com o trunk da data de registro da atividade
        entity.setDataMes(DateUtils.truncate(entity.getDataRegistroAtividade(),
                Calendar.MONTH));

        // seta a data mes CHP com o trunk da data de registro da atividade
        // considerando range de 21/mm até 20/mm+1: o custo irá para
        // 01/mm+1/YYYY
        entity.setDataMesChp(DateUtil.getDateTruncChp(entity
                .getDataRegistroAtividade(), Integer.valueOf(systemProperties
                .getProperty(Constants.CHP_RANGE_INITIAL_DAY))));

        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeRegistroAtividade(final RegistroAtividade entity) {
        dao.remove(entity);
    }

    /**
     * Salva o upload do arquivo de banco de horas e salva.
     * 
     * @param uploadItem
     *            - contem o valor do upload
     * 
     * @param padraoArq
     *            - padrão do arquivo.
     * 
     * @throws IOException
     *             lnaça exceção caso não seja possível salvar o arquivo de
     *             upload
     * 
     * @return retorna true se salvo com sucesso, caso contrario false.
     */
    public UploadArquivo uploadRegistroAtividade(final UploadItem uploadItem,
            final PadraoArquivo padraoArq) throws IOException {

        UploadArquivo arquivoReqAtv = new UploadArquivo();

        List<RegistroAtividade> resultList = new ArrayList<RegistroAtividade>();

        List<RegistroAtividade> regAtivList =
                CsvUtil.getElementList(new String(uploadItem.getData()),
                        RegistroAtividade.class, CsvUtil
                                .getCsvConfig(padraoArq));

        int count = 1, errorCount = 0;
        String strError = "";
        for (RegistroAtividade registroAtividade : regAtivList) {
            count++;

            String error = validateRegistroAtividade(registroAtividade);
            if (error != null && !"".equals(error)) {
                strError += "Line " + count + ": " + error + "\n";
                errorCount++;
            } else {
                // relaciona com a entidade UploadArquivo,
                // que representa o arquivo que foi feito o upload
                resultList.add(registroAtividade);
                registroAtividade.setUploadArquivo(arquivoReqAtv);
            }
        }

        String fileName = uploadItem.getFileName();
        arquivoReqAtv.setNomeArquivo(fileName);
        arquivoReqAtv.setCodigoAutor(LoginUtil.getLoggedUsername());
        arquivoReqAtv.setDataDataHoraUpload(new Date());
        arquivoReqAtv.setValorBytes(String.valueOf(uploadItem.getFileSize()));
        arquivoReqAtv.setDataDataHoraUpload(new Date());
        arquivoReqAtv.setSiglaTabelaAlvo("REAT");
        arquivoReqAtv.setTextoExtArquivo(fileName.substring(fileName
                .lastIndexOf('.') + 1, fileName.length()));
        arquivoReqAtv.setPadraoArquivo(padraoArq);
        arquivoReqAtv.setTextoErro(strError);
        arquivoReqAtv.setRegistroAtividades(resultList);

        if (errorCount == 0) {
            Messages.showSucess("uploadBancoHoras",
                    Constants.UPLOAD_MSG_SUCCESS_UPLOAD);
        } else if (errorCount == regAtivList.size()) {
            Messages.showError("uploadBancoHoras",
                    Constants.MSG_ERROR_UPLOAD_PADRAO_ARQUIVO);
        } else {
            Messages.showError("uploadBancoHoras", Constants.MSG_ERROR_UPLOAD);
        }

        return arquivoReqAtv;
    }

    /**
     * Valida o registro de atividade.
     * 
     * @param registroAtividade
     *            registro de atividade a ser validado.
     * 
     * @return retorna a descrição dos erros.
     */
    private String validateRegistroAtividade(
            final RegistroAtividade registroAtividade) {

        ContratoPratica contratoPratica = null;

        if (registroAtividade.getIdContratoPratica() != null) {
            contratoPratica =
                    contratoPraticaService
                            .findContratoPraticaById(registroAtividade
                                    .getIdContratoPratica());
        }

        GrupoCusto grupoCusto = null;

        if (registroAtividade.getIdGrupoCusto() != null) {
            grupoCusto =
                    grupoCustoService.findGrupoCustoById(registroAtividade
                            .getIdGrupoCusto());
        }

        String login = registroAtividade.getLogin();
        String srtErros = "";
        if (login != null) {
            Pessoa pessoa = pessoaService.findPessoaByLogin(login);

            if (pessoa != null) {

                if (registroAtividade.getIdAtividade() == null
                        || atvService.findAtividadeById(registroAtividade
                                .getIdAtividade()) == null) {
                    srtErros +=
                            BundleUtil
                                    .getBundle(Constants.UPLOAD_ERROR_INVALID_ACTIVITY);

                } else if (registroAtividade.getDataRegistroAtividade() == null) {
                    srtErros +=
                            BundleUtil
                                    .getBundle(Constants.UPLOAD_MSG_ERROR_INVALID_DATE_PATTERN);

                } else if (!DateUtil.after(registroAtividade
                        .getDataRegistroAtividade(),
                        getClosingDateRegistroAtividade())) {
                    srtErros +=
                            BundleUtil
                                    .getBundle(Constants.UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE);

                } else if (contratoPratica == null && grupoCusto == null) {
                    srtErros +=
                            BundleUtil
                                    .getBundle(Constants.UPLOAD_ERROR_INVALID_CLOB_AND_COST_GROUP);

                } else {
                    populateRegistroAtividade(registroAtividade);

                    if (findRegistroAtividadeUnique(registroAtividade) != null) {
                        srtErros +=
                                BundleUtil
                                        .getBundle(Constants.UPLOAD_MSG_ERROR_ALREAD_EXISTS);
                    }
                }
            } else {
                srtErros +=
                        BundleUtil
                                .getBundle(Constants.UPLOAD_ERROR_INVALID_LOGIN);
            }
        } else {
            srtErros +=
                    BundleUtil.getBundle(Constants.UPLOAD_MSG_ERROR_READING);
        }

        return srtErros;
    }

    /**
     * Pega a data de fechamento do Módulo de horas extras e plantão.
     * 
     * @return retorna a Data de Fechamento.
     */
    public Date getClosingDateRegistroAtividade() {
        // pega a data de fechamento do modulo
        Modulo modulo =
                moduloService.findModuloById(new Long(systemProperties
                        .getProperty(Constants.MODULO_LEAVES_OVERTIME_CODE)));

        return DateUtils.truncate(modulo.getDataFechamento(), Calendar.MONTH);
    }

    /**
     * Popula o registro das atividade.
     * 
     * @param regAtividade
     *            - RequistroAtividade.
     */
    private void populateRegistroAtividade(final RegistroAtividade regAtividade) {

        regAtividade.setPessoa(pessoaService.findPessoaByLogin(regAtividade
                .getLogin()));

        regAtividade.setAtividade(atividadeService
                .findAtividadeById(regAtividade.getIdAtividade()));

        if (regAtividade.getIdContratoPratica() != null) {
            regAtividade.setContratoPratica(contratoPraticaService
                    .findContratoPraticaById(regAtividade
                            .getIdContratoPratica()));
        } else if (regAtividade.getIdGrupoCusto() != null) {
            regAtividade.setGrupoCusto(grupoCustoService
                    .findGrupoCustoById(regAtividade.getIdGrupoCusto()));
        }

        // trunca a data pelo mês, pois o dia não deve ser considerado
        regAtividade.setDataMes(DateUtils.truncate(regAtividade
                .getDataRegistroAtividade(), Calendar.MONTH));
    }

    /**
     * Salva os dados importados do arquivo e salva o arquivo no file system.
     * 
     * @param uploadArquivo
     *            - comtem os dados do arquivo importados
     * @param fileBytes
     *            - bytes dos arquivos lidos
     * @throws IOException
     *             - lança a exception de I/O
     */
    public void saveUploadFile(final UploadArquivo uploadArquivo,
            final byte[] fileBytes) throws IOException {

        // seta a data mes CHP com o trunk da data de registro da atividade
        // considerando range de 21/mm até 20/mm+1: o custo irá para
        // 01/mm+1/YYYY
        Integer chpRangInitialDay =
                Integer.valueOf(systemProperties
                        .getProperty(Constants.CHP_RANGE_INITIAL_DAY));
        List<RegistroAtividade> regAtivList =
                uploadArquivo.getRegistroAtividades();
        for (RegistroAtividade regAtiv : regAtivList) {
            regAtiv.setDataMesChp(DateUtil.getDateTruncChp(regAtiv
                    .getDataRegistroAtividade(), chpRangInitialDay));
        }

        uploadArquivoService.createUploadArquivo(uploadArquivo);

        String fileName =
                uploadArquivo.getCodigoUploadArquivo() + "-"
                        + uploadArquivo.getNomeArquivo();
        String path = getUploadPathDestination();

        uploadArquivo.setNomeArquivo(fileName);
        uploadArquivo.setTextoCaminho(path);

        // realiza o update para setar o nome do arquivo
        uploadArquivoService.updateUploadArquivo(uploadArquivo);
        

        FileOutputStream fos = new FileOutputStream(path + fileName);

        fos.write(fileBytes);
    }

    /**
     * Retorna o caminho de destino dos uploads.
     * 
     * @return retorna uma string q representa o destino do upload.
     */
    private String getUploadPathDestination() {
        String server = System.getProperty(Constants.SERVER_ENVIRONMENT_KEY);
        String path = "";

        if (Constants.SERVER_ENVIRONMENT_PROD.equals(server)) {
            path =
                    (String) systemProperties
                            .get("upload.plantao.destination.path");
        }

        return path;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public RegistroAtividade findRegistroAtividadeById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public List<RegistroAtividade> findRegistroAtividadeByFilter(
            final RegistroAtividade filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return entidade que atendem ao criterio de filtro.
     */
    public RegistroAtividade findRegistroAtividadeUnique(
            final RegistroAtividade filter) {
        return dao.findUnique(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<RegistroAtividade> findRegistroAtividadeAll() {
        return dao.findAll();
    }

    /**
     * Busca uma lista de entidades pela dataMesChp.
     * 
     * @param dataMesChp
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public List<RegistroAtividade> findRegistroAtividadeByDataMesChp(
            final Date dataMesChp) {
        return dao.findByDataMesChp(dataMesChp);
    }

}