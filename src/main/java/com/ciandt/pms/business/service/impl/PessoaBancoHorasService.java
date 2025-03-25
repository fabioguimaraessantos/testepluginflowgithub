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
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IPessoaBancoHorasService;
import com.ciandt.pms.business.service.IPessoaService;
import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.control.jsf.util.BundleUtil;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.csv.util.CsvUtil;
import com.ciandt.pms.model.Modulo;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.model.PessoaBancoHoras;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IPessoaBancoHorasDao;
import com.ciandt.pms.util.DateUtil;
import com.ciandt.pms.util.LoginUtil;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 12/08/2010
 */
@Service
public class PessoaBancoHorasService implements IPessoaBancoHorasService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IPessoaBancoHorasDao dao;

    /** Instancia do servico PessoaService. */
    @Autowired
    private IPessoaService pessoaService;
    
    /** Instancia do servico UploadArquivoService. */
    @Autowired
    private IUploadArquivoService uploadArquivoService;
    
    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;
    
    /** Instancia do servico Modulo. */
    @Autowired
    private IModuloService moduloService;
    
    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     *            
     * @return retorna true se criado com sucesso, caso contrario retorna false.           
     */
    public Boolean createPessBcoHrs(final PessoaBancoHoras entity) {
        
        entity.setLogin(entity.getPessoa().getCodigoLogin());
        
        String result = validadeteBancoHoras(entity);
        if (StringUtils.isEmpty(result)) {
            dao.create(entity);
            
            Messages.showSucess("createPessBcoHrs", Constants.DEFAULT_MSG_SUCCESS_CREATE,
                    Constants.ENTITY_NAME_PESS_BCO_HRS);
            
            return true;
        } else {
            Messages.showError("createPessBcoHrs", result);
            
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
    public void updatePessBcoHrs(final PessoaBancoHoras entity) {
        dao.update(entity);
    }
    
    /**
     * Salva o upload do arquivo de banco de horas e salva.
     * 
     * @param uploadItem - contem o valor do upload
     * 
     * @param padraoArq - padrão do arquivo.
     * 
     * @throws IOException lnaça exceção caso não seja possível salvar
     * o arquivo de upload
     * 
     * @return retorna true se salvo com sucesso, caso contrario false.
     */
    public UploadArquivo uploadBancoHoras(final UploadItem uploadItem, 
            final PadraoArquivo padraoArq) throws IOException {
        
        UploadArquivo arquivoBcHrs = new UploadArquivo();
        
        
        List<PessoaBancoHoras> pBcHrsList = new ArrayList<PessoaBancoHoras>();
        
        List<PessoaBancoHoras> bancoHorasList = 
            CsvUtil.getElementList(new String(uploadItem.getData()), 
                PessoaBancoHoras.class, CsvUtil.getCsvConfig(padraoArq));
        
        int count = 1, errorCount = 0;
        String strError = "";
        for (PessoaBancoHoras pessoaBancoHoras : bancoHorasList) {
            count++;
            
            String error = validadeteBancoHoras(pessoaBancoHoras);
            if (error != null && !"".equals(error)) {
                strError += "Line " + count + ": " + error + "\n";
                errorCount++;
            } else {
                //trunca a data pelo mês, pois o dia não deve ser considerado
                pessoaBancoHoras.setDataMes(DateUtils.truncate(
                        pessoaBancoHoras.getDataMes(), Calendar.MONTH));
                //relaciona com o arquivo de upload.
                pBcHrsList.add(pessoaBancoHoras);
                pessoaBancoHoras.setUploadArquivo(arquivoBcHrs);
            }
            
        }
        
        String fileName = uploadItem.getFileName();
        arquivoBcHrs.setNomeArquivo(fileName);
        arquivoBcHrs.setCodigoAutor(LoginUtil.getLoggedUsername());
        arquivoBcHrs.setDataDataHoraUpload(new Date());
        arquivoBcHrs.setValorBytes(String.valueOf(uploadItem.getFileSize()));
        arquivoBcHrs.setDataDataHoraUpload(new Date());
        arquivoBcHrs.setSiglaTabelaAlvo("PEBH");
        arquivoBcHrs.setTextoExtArquivo(fileName.substring(
                fileName.lastIndexOf('.') + 1, fileName.length()));
        arquivoBcHrs.setPadraoArquivo(padraoArq);
        arquivoBcHrs.setTextoErro(strError);
        arquivoBcHrs.setPessoaBancoHoras(pBcHrsList);
        
        
        if (errorCount == 0) {
            Messages.showSucess("uploadBancoHoras", 
                    Constants.UPLOAD_MSG_SUCCESS_UPLOAD);
        } else if (errorCount == bancoHorasList.size()) {
            Messages.showError("uploadBancoHoras", 
                    Constants.MSG_ERROR_UPLOAD_PADRAO_ARQUIVO);
        } else {
            Messages.showError("uploadBancoHoras", 
                    Constants.MSG_ERROR_UPLOAD);
        }
        
        return arquivoBcHrs;
    }
    
    /**
     * Valida o banco de horas.
     * 
     * @param bancoHoras registro banco de horas a ser validado.
     * 
     * @return retorna a descrição dos erros.
     */
    private String validadeteBancoHoras(
            final PessoaBancoHoras bancoHoras) {
        
        String login = bancoHoras.getLogin();
        String srtErro = "";
        Pessoa pessoa = null; 
        if (login != null) {
            pessoa = pessoaService.findPessoaByLogin(login);
            if (bancoHoras.getDataMes() == null) { 
                srtErro += BundleUtil.getBundle(
                    Constants.UPLOAD_MSG_ERROR_INVALID_DATE_PATTERN);
            } else if (pessoa != null) {
                bancoHoras.setPessoa(pessoa);
                if (findPessBcoHrsUnique(bancoHoras) != null) {
                    srtErro += BundleUtil.getBundle(Constants.UPLOAD_MSG_ERROR_ALREAD_EXISTS);
                } else if (!DateUtil.after(bancoHoras.getDataMes(), getClosingDateBancoHoras())) {
                    srtErro += BundleUtil.getBundle(Constants.UPLOAD_MSG_ERROR_BEFORE_CLOSING_DATE);
                }
           
            } else {
                srtErro += BundleUtil.getBundle(Constants.UPLOAD_ERROR_INVALID_LOGIN);
            }
        } else {
            srtErro += BundleUtil.getBundle(Constants.UPLOAD_MSG_ERROR_READING);
        } 
      
        return srtErro;
    }
    
    /**
     * Pega a data de fechamento do Módulo de horas extras e plantão.
     * 
     * @return retorna a Data de Fechamento.
     */
    public Date getClosingDateBancoHoras() {
        // pega a data de fechamento do modulo do MapaAlocacao
        Modulo modulo = moduloService.findModuloById(new Long(systemProperties
                        .getProperty(Constants.MODULO_LEAVES_OVERTIME_CODE)));

        return DateUtils.truncate(
                modulo.getDataFechamento(), Calendar.MONTH);
    }

    /**
     * Salva os dados importados do arquivo e salva o arquivo no
     * file system.
     * 
     * @param uploadArquivo - contem os dados do arquivo importados
     * @param fileBytes - bytes dos arquivos lidos
     * @throws IOException - lança a exception de I/O
     */
    public void saveUploadFile(final UploadArquivo uploadArquivo, 
            final byte[] fileBytes) throws IOException {
        
        uploadArquivoService.createUploadArquivo(uploadArquivo);
        
        String path = getUploadPathDestination();
        String fileName = uploadArquivo.getCodigoUploadArquivo() 
            + "-" + uploadArquivo.getNomeArquivo();
        
        uploadArquivo.setTextoCaminho(path);
        uploadArquivo.setNomeArquivo(fileName);
        uploadArquivoService.updateUploadArquivo(uploadArquivo);
        
        FileOutputStream fos = new FileOutputStream(path + fileName);
   
        fos.write(fileBytes);
    }
    
    /**
     * Retorna o caminho de destino dos uploads.
     * 
     * @return retorna uma string q representa o destino
     * do upload.
     */
    private String getUploadPathDestination() {
        String server = System.getProperty(Constants.SERVER_ENVIRONMENT_KEY);
        String path = "";
        
        if (Constants.SERVER_ENVIRONMENT_PROD.equals(server)) {
            path = (String) systemProperties.get("upload.banco_horas.destination.path");
        }
        
        return path;
    }
    
    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removePessBcoHrs(final PessoaBancoHoras entity) {
        dao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public PessoaBancoHoras findPessBcoHrsById(final Long id) {
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
    public List<PessoaBancoHoras> findPessBcoHrsByFilter(
            final PessoaBancoHoras filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    public PessoaBancoHoras findPessBcoHrsUnique(
            final PessoaBancoHoras filter) {
        return dao.findUnique(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<PessoaBancoHoras> findPessBcoHrsAll() {
        return dao.findAll();
    }
    
    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    public List<PessoaBancoHoras> findPessBcoHrsByDataMes(final Date dataMes) {
        return dao.findByDataMes(dataMes);
    }

}