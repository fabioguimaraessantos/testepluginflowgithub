package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.model.UploadDespesa;


/**
 * Define a interface do Service da entidade.
 * 
 * @author cmantovani
 * @since 29/06/2011
 */
@Service
public interface IUploadDespesaService {

    /**
     * Cria uma nova entidade.
     * 
     * @param entity
     *            - entidade a ser criada
     * 
     * @return true se sucesso
     */
    @Transactional
    Boolean createUploadDespesa(final UploadDespesa entity);

    /**
     * Atualiza uma entidade.
     * 
     * @param entity
     *            - entidade a ser atualizada
     */
    @Transactional
    void updateUploadDespesa(final UploadDespesa entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeUploadDespesa(final UploadDespesa entity);

    /**
     * Busca uma entidade pelo Id.
     * 
     * @param id
     *            - id da entidade a ser encontrada
     * @return entidade encontrada na busca
     */
    UploadDespesa findUploadDespesaById(final Long id);

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
     * @param dataSelecioanda
     *            - dataSelecionada no combo
     * 
     * @throws IOException
     *             lnaça exceção caso não seja possível salvar o arquivo de
     *             upload
     * 
     * @return retorna true se salvo com sucesso, caso contrario false.
     */
    UploadArquivo uploadDespesas(final UploadItem uploadItem,
            final PadraoArquivo padraoArq, final Empresa empresa,
            final Date dataSelecioanda) throws IOException;

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
    @Transactional(rollbackFor = IOException.class)
    void saveUploadFile(final UploadArquivo uploadArquivo,
            final byte[] fileBytes) throws IOException;

    /**
     * Busca os Itens de Upload de Despesa do mes.
     * 
     * @param dataMes
     *            - data a ser pesquisada
     * @return lista de UploadDespesas
     */
    List<UploadDespesa> findUploadDespesasByDataMes(final Date dataMes);

    /**
     * Busca os Itens de Upload de Despesa do mes.
     * 
     * @param dataMes
     *            - data a ser pesquisada
     * @param empresa
     *            - empresa a ser consultada
     * @return lista de UploadDespesas
     */
    List<UploadDespesa> findUploadDespesasByDataMesAndEmpresa(
            final Date dataMes, final Empresa empresa);

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
    List<UploadDespesa> findUploadDespesasByFilter(final Date dataInicio,
            final Date dataFim, final String descricao,
            final UploadDespesa filter);

    /**
     * Remove os UploadDespesas da lista.
     * 
     * @param uploadDespesas
     *            - lista de Upload Despesas a ser removida
     */
    @Transactional
    void removeListUploadDespesa(final List<UploadDespesa> uploadDespesas);

    /**
     * Remove os UploadDespesas da lista.
     * 
     * @param uploadDespesas
     *            - lista de Upload Despesas a ser removida
     */
    @Transactional
    void removeListUploadDespesaSelected(
            final List<UploadDespesa> uploadDespesas);

}