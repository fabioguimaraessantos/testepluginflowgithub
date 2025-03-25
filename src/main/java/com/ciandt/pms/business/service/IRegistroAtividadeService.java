package com.ciandt.pms.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.richfaces.model.UploadItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.model.RegistroAtividade;
import com.ciandt.pms.model.UploadArquivo;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Service
public interface IRegistroAtividadeService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     *            
     * @return retorna true se criado com sucesso, caso contrario retorna false.           
     */
    @Transactional
    Boolean createRegistroAtividade(final RegistroAtividade entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateRegistroAtividade(final RegistroAtividade entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    @Transactional
    void removeRegistroAtividade(final RegistroAtividade entity);

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
    @Transactional
    UploadArquivo uploadRegistroAtividade(final UploadItem uploadItem, 
            final PadraoArquivo padraoArq) throws IOException;
    
    /**
     * Salva os dados importados do arquivo e salva o arquivo no
     * file system.
     * 
     * @param uploadArquivo - comtem os dados do arquivo importados
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
    RegistroAtividade findRegistroAtividadeById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<RegistroAtividade> findRegistroAtividadeByFilter(
            final RegistroAtividade filter);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    RegistroAtividade findRegistroAtividadeUnique(final RegistroAtividade filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<RegistroAtividade> findRegistroAtividadeAll();
    
    /**
     * Busca uma lista de entidades pela dataMesChp.
     * 
     * @param dataMesChp
     *            data mes corrente.
     * 
     * @return lista de entidades que atendem ao criterio do filtro.
     */
    List<RegistroAtividade> findRegistroAtividadeByDataMesChp(final Date dataMesChp);

}