package com.ciandt.pms.business.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.richfaces.model.UploadItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IContratoPraticaService;
import com.ciandt.pms.business.service.IEmpresaService;
import com.ciandt.pms.business.service.IGrupoCustoService;
import com.ciandt.pms.business.service.IMoedaService;
import com.ciandt.pms.business.service.ITipoDespesaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.business.service.IUploadDespesaService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.csv.util.CsvUtil;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.TipoDespesa;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.model.UploadDespesa;
import com.ciandt.pms.persistence.dao.IUploadDespesaDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;


/**
 * Define o Service da entidade.
 * 
 * @author cmantovani
 * @since 29/06/2011
 */
@Service
public class UploadDespesaService implements IUploadDespesaService {

    /** Instancia do servico TipoDespesaService. */
    @Autowired
    private ITipoDespesaService tipoDespesaService;

    /** Instancia do servico MoedaService. */
    @Autowired
    private IMoedaService moedaService;

    /** Instancia do servico GrupoCustoService. */
    @Autowired
    private IGrupoCustoService grupoCustoService;
    
    /** Instancia do servico ContratoPraticaService. */
    @Autowired
    private IContratoPraticaService contratoPraticaService;

    /** Instancia do servico EmpresaService. */
    @Autowired
    private IEmpresaService empresaService;

    /** Instancia do servico UploadArquivoService. */
    @Autowired
    private IUploadArquivoService uploadArquivoService;

    /** Instancia do Dao. */
    @Autowired
    private IUploadDespesaDao uploadDespesaDao;

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
    public Boolean createUploadDespesa(final UploadDespesa entity) {

        // seta a data mes com o trunk da data de lancamento
        entity.setDataLancamento(DateUtil.getDate(entity.getDataLancamento()));

        entity.setIndicadorOrigem(BundleUtil
                .getBundle(Constants.INPUT_INDICATOR_MANUAL));

        entity.setUploadArquivo(null);

        uploadDespesaDao.create(entity);

        Messages.showSucess("createUploadDespesa",
                Constants.DEFAULT_MSG_SUCCESS_SAVE,
                Constants.ENTITY_NAME_REGISTRO_ATIVIDADE);

        return true;
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            - entidade que será atualizada.
     * 
     */
    public void updateUploadDespesa(final UploadDespesa entity) {
        entity.setIndicadorOrigem(BundleUtil
                .getBundle(Constants.INPUT_INDICATOR_MANUAL));
        uploadDespesaDao.update(entity);
    }

    /**
     * Busca uma entidade pelo Id.
     * 
     * @param id
     *            - id da entidade a ser encontrada
     * @return entidade encontrada na busca
     */
    public UploadDespesa findUploadDespesaById(final Long id) {
        return uploadDespesaDao.findById(id);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeUploadDespesa(final UploadDespesa entity) {
        uploadDespesaDao.remove(entity);
    }

    /**
     * Salva o upload do arquivo de despesas e salva.
     * 
     * @param uploadItem
     *            - contem o valor do upload
     * 
     * @param padraoArq
     *            - padrão do arquivo.
     * @param empresa
     *            - empresa da despesa
     * @param dataSelecionada
     *            - data selecionada no combobox
     * 
     * @throws IOException
     *             lnaça exceção caso não seja possível salvar o arquivo de
     *             upload
     * 
     * @return retorna true se salvo com sucesso, caso contrario false.
     */
    public UploadArquivo uploadDespesas(final UploadItem uploadItem,
            final PadraoArquivo padraoArq, final Empresa empresa,
            final Date dataSelecionada) throws IOException {

        UploadArquivo arquivoDespesas = new UploadArquivo();

        List<UploadDespesa> uploadDespesas = new ArrayList<UploadDespesa>();

        List<UploadDespesa> uploadDespesasList =
                CsvUtil.getElementList(new String(uploadItem.getData()),
                        UploadDespesa.class, CsvUtil.getCsvConfig(padraoArq));

        int count = 1, errorCount = 0, warningCount = 0, okCount = 0;
        String strError = "";
        for (UploadDespesa uploadDespesa : uploadDespesasList) {
            count++;

            uploadDespesa.setIndicadorOrigem(BundleUtil
                    .getBundle(Constants.INPUT_INDICATOR_IMPORTADO));

            String importRow = uploadDespesa.getImportRow();

            Date data = uploadDespesa.getDataLancamento();
            Date dataLancamento = null;

            if (data != null) {
                dataLancamento =
                        DateUtils.truncate(uploadDespesa.getDataLancamento(),
                                Calendar.MONTH);
            }

            if (importRow != null) {
                if (importRow.equals("yes")
                        && dataLancamento.equals(dataSelecionada)) {
                    String error = validateUploadDespesa(uploadDespesa);

                    if (error.equals(Constants.UPLOAD_MSG_ERROR_READING)) {
                        strError +=
                                "[ERROR] Line " + count + ": " + error + "\n";
                        errorCount++;
                    } else {
                        if (error != null && !error.equals("")) {
                            strError +=
                                    "[WARNING] Line " + count + ": " + error
                                            + "\n";
                            warningCount++;
                        }
                        okCount++;
                        uploadDespesa.setCount(okCount);
                        // trunca a data pelo mês, pois o dia não deve ser
                        // considerado
                        uploadDespesa.setDataLancamento(DateUtils.truncate(
                                uploadDespesa.getDataLancamento(),
                                Calendar.MONTH));
                        // relaciona com o arquivo de upload.
                        uploadDespesas.add(uploadDespesa);
                        uploadDespesa.setUploadArquivo(arquivoDespesas);
                        uploadDespesa.setEmpresa(empresa);
                    }
                }
            }
        }

        String fileName = uploadItem.getFileName();
        arquivoDespesas.setNomeArquivo(fileName);
        arquivoDespesas.setCodigoAutor(LoginUtil.getLoggedUsername());
        arquivoDespesas.setDataDataHoraUpload(new Date());
        arquivoDespesas.setValorBytes(String.valueOf(uploadItem.getFileSize()));
        arquivoDespesas.setDataDataHoraUpload(new Date());
        arquivoDespesas.setSiglaTabelaAlvo("UPDE");
        arquivoDespesas.setTextoExtArquivo(fileName.substring(fileName
                .lastIndexOf('.') + 1, fileName.length()));
        arquivoDespesas.setPadraoArquivo(padraoArq);
        arquivoDespesas.setTextoErro(strError);
        arquivoDespesas.setUploadDespesas(uploadDespesas);

        if (errorCount == 0) {
            if (warningCount == 0) {
                if (uploadDespesas.isEmpty()) {
                    strError =
                            BundleUtil
                                    .getBundle(Constants.UPLOAD_MSG_EMPTY_UPLOAD);
                    arquivoDespesas.setTextoErro(strError);
                } else {
                    Messages.showSucess("uploadDespesas",
                            Constants.UPLOAD_MSG_SUCCESS_UPLOAD);
                }
            } else {
                Messages.showWarning("uploadDespesas",
                        Constants.MSG_WARNING_UPLOAD);
            }
        } else if (errorCount == uploadDespesasList.size()) {
            Messages.showError("uploadDespesas",
                    Constants.MSG_ERROR_UPLOAD_PADRAO_ARQUIVO);
        } else {
            Messages.showError("uploadDespesas", Constants.MSG_ERROR_UPLOAD);
        }

        return arquivoDespesas;
    }

    /**
     * Valida o uploadDespesa.
     * 
     * @param uploadDespesa
     *            registro uploadDespesa a ser validado.
     * 
     * @return retorna a descrição dos erros.
     */
    private String validateUploadDespesa(final UploadDespesa uploadDespesa) {

        String strError = "";

        String importRow = uploadDespesa.getImportRow();

        TipoDespesa tipoDespesa = null;
        Moeda moeda = null;
        GrupoCusto grupoCusto = null;
        ContratoPratica contratoPratica = null;

        if (importRow != null) {
            if (importRow.equals("yes")) {
                tipoDespesa =
                        tipoDespesaService.findTipoDespesaByName(uploadDespesa
                                .getNomeTipoDespesa());

                moeda =
                        moedaService.findMoedaByAcronym(uploadDespesa
                                .getSiglaMoeda());

                grupoCusto =
                        grupoCustoService.findGrupoCustoByAcronym(uploadDespesa
                                .getSiglaGrupoCusto());

				contratoPratica = 
						contratoPraticaService.findContratoPraticaByName(uploadDespesa
								.getNomeContratoPratica());

                if (uploadDespesa.getDataLancamento() == null) {
                    strError +=
                            BundleUtil
                                    .getBundle(Constants.UPLOAD_MSG_ERROR_INVALID_DATE_PATTERN);

                } else {
                    if (grupoCusto != null) {
                        uploadDespesa.setGrupoCusto(grupoCusto);
                        uploadDespesa.setEditGrupoCusto(Boolean.valueOf(false));
                    } else {
                        uploadDespesa.setGrupoCusto(new GrupoCusto());
                        uploadDespesa.setEditGrupoCusto(Boolean.valueOf(true));
                        strError +=
                                BundleUtil
                                        .getBundle(Constants.UPLOAD_ERROR_INVALID_COST_GROUP);
                    }
                    if (tipoDespesa != null) {
                        uploadDespesa.setTipoDespesa(tipoDespesa);
                        uploadDespesa
                                .setEditTipoDespesa(Boolean.valueOf(false));
                    } else {
                        uploadDespesa.setTipoDespesa(new TipoDespesa());
                        uploadDespesa.setEditTipoDespesa(Boolean.valueOf(true));
                        strError +=
                                BundleUtil
                                        .getBundle(Constants.UPLOAD_ERROR_INVALID_CATEGORY);
                    }
                    if (moeda != null) {
                        uploadDespesa.setMoeda(moeda);
                        uploadDespesa.setEditMoeda(Boolean.valueOf(false));
                    } else {
                        uploadDespesa.setMoeda(new Moeda());
                        uploadDespesa.setEditMoeda(Boolean.valueOf(true));
                        strError +=
                                BundleUtil
                                        .getBundle(Constants.UPLOAD_ERROR_INVALID_CURRENCY);
                    }
                    if (contratoPratica != null) {
						uploadDespesa.setContratoPratica(contratoPratica);
						uploadDespesa.setEditContratoPratica(false);
					} else {
						uploadDespesa.setContratoPratica(new ContratoPratica());
						uploadDespesa.setEditContratoPratica(true);
						strError += BundleUtil
								.getBundle(Constants.UPLOAD_ERROR_INVALID_C_LOB);
					}
                }
            }
        } else {
            strError +=
                    BundleUtil.getBundle(Constants.UPLOAD_MSG_ERROR_READING);
        }

        return strError;
    }

    /**
     * Salva os dados importados do arquivo e salva o arquivo no file system.
     * 
     * @param uploadArquivo
     *            - contem os dados do arquivo importados
     * @param fileBytes
     *            - bytes dos arquivos lidos
     * @throws IOException
     *             - lança a exception de I/O
     */
    public void saveUploadFile(final UploadArquivo uploadArquivo,
            final byte[] fileBytes) throws IOException {

        for (UploadDespesa uploadDespesa : uploadArquivo.getUploadDespesas()) {
            if (uploadDespesa.getGrupoCusto().getCodigoGrupoCusto() == null) {
                uploadDespesa.setGrupoCusto(null);
            }
            if (uploadDespesa.getTipoDespesa().getCodigoTipoDespesa() == null) {
                uploadDespesa.setTipoDespesa(null);
            }
            if (uploadDespesa.getMoeda().getCodigoMoeda() == null) {
                uploadDespesa.setMoeda(null);
            }

            uploadDespesa.setEmpresa(empresaService
                    .findEmpresaById(uploadDespesa.getEmpresa()
                            .getCodigoEmpresa()));

        }

        uploadArquivoService.createUploadArquivo(uploadArquivo);

        String path = getUploadPathDestination();
        String fileName =
                uploadArquivo.getCodigoUploadArquivo() + "-"
                        + uploadArquivo.getNomeArquivo();

        uploadArquivo.setTextoCaminho(path);
        uploadArquivo.setNomeArquivo(fileName);
        uploadArquivoService.updateUploadArquivo(uploadArquivo);

        FileOutputStream fos = new FileOutputStream(path + fileName);

        fos.write(fileBytes);
    }

    /**
     * Busca os Itens de Upload de Despesa pelo filtro no periodo.
     * 
     * @param dataInicio
     *            - data de Inicio
     * @param dataFim
     *            - data de Fim
     * @param descricao
     *            - texto de descricao
     * @param filter
     *            - entidade para pesquisa
     * @return lista de UploadDespesas
     */
    public List<UploadDespesa> findUploadDespesasByFilter(
            final Date dataInicio, final Date dataFim, final String descricao,
            final UploadDespesa filter) {

        List<UploadDespesa> result =
                uploadDespesaDao.findByFilter(dataInicio, dataFim, descricao,
                        filter);

        for (UploadDespesa uploadDespesa : result) {
            if (uploadDespesa.getUploadArquivo() != null) {
                uploadDespesa.setUploadArquivo(uploadArquivoService
                        .findUploadArquivoById(uploadDespesa.getUploadArquivo()
                                .getCodigoUploadArquivo()));
            }
        }

        return result;

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
                            .get("upload.despesa.destination.path");
        }

        return path;
    }

    /**
     * Busca os Itens de Upload de Despesa do mes.
     * 
     * @param dataMes
     *            - data a ser pesquisada
     * @return lista de UploadDespesas
     */
    public List<UploadDespesa> findUploadDespesasByDataMes(final Date dataMes) {
        return uploadDespesaDao.findByDataMes(dataMes);
    }

    /**
     * Busca os Itens de Upload de Despesa do mes.
     * 
     * @param dataMes
     *            - data a ser pesquisada
     * @param empresa
     *            - empresa a ser consultada
     * @return lista de UploadDespesas
     */
    public List<UploadDespesa> findUploadDespesasByDataMesAndEmpresa(
            final Date dataMes, final Empresa empresa) {
        return uploadDespesaDao.findByDataMesAndEmpresa(dataMes, empresa);
    }

    /**
     * Remove os UploadDespesas da lista.
     * 
     * @param uploadDespesas
     *            - lista de Upload Despesas a ser removida
     */
    public void removeListUploadDespesa(final List<UploadDespesa> uploadDespesas) {
        for (UploadDespesa uploadDespesa : uploadDespesas) {
            uploadDespesaDao.remove(uploadDespesa);
        }
    }

    /**
     * Remove os UploadDespesas da lista.
     * 
     * @param uploadDespesas
     *            - lista de Upload Despesas a ser removida
     */
    public void removeListUploadDespesaSelected(
            final List<UploadDespesa> uploadDespesas) {
        for (UploadDespesa uploadDespesa : uploadDespesas) {
            if (uploadDespesa.getIsSelected()) {
                uploadDespesa =
                        findUploadDespesaById(uploadDespesa
                                .getCodigoUploadDespesa());
                uploadDespesaDao.remove(uploadDespesa);
            }
        }
    }
}