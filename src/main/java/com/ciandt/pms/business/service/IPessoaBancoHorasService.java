package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.PessoaBancoHoras;
import com.ciandt.pms.model.UploadArquivo;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 12/08/2010
 */
@Service
public interface IPessoaBancoHorasService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * @return retorna true se criado com sucesso, caso contrario retorna false.            
     */
    @Transactional
    Boolean createPessBcoHrs(final PessoaBancoHoras entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updatePessBcoHrs(final PessoaBancoHoras entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removePessBcoHrs(final PessoaBancoHoras entity);

    
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
    UploadArquivo uploadBancoHoras(final UploadItem uploadItem, 
            final PadraoArquivo padraoArq) throws IOException;
    
    /**
     * Salva os dados importados do arquivo e salva o arquivo no
     * file system.
     * 
     * @param uploadArquivo - contem os dados do arquivo importados
     * @param fileBytes - bytes dos arquivos lidos
     * @throws IOException - lança a exception de I/O
     */
    @Transactional(rollbackFor = IOException.class)
    void saveUploadFile(final UploadArquivo uploadArquivo, 
            final byte[] fileBytes) throws IOException;
    
    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    PessoaBancoHoras findPessBcoHrsById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<PessoaBancoHoras> findPessBcoHrsByFilter(final PessoaBancoHoras filter);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    PessoaBancoHoras findPessBcoHrsUnique(final PessoaBancoHoras filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PessoaBancoHoras> findPessBcoHrsAll();
    
    /**
     * Busca uma lista de entidades pela dataMes.
     * 
     * @param dataMes
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<PessoaBancoHoras> findPessBcoHrsByDataMes(final Date dataMes);

}