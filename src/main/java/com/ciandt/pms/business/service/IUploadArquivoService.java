package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.UploadArquivo;


/**
 * 
 * A classe IUploadArquivoService proporciona a interface de acesso para a
 * camada de servico referente a entidade Upload.
 * 
 * @since 23/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IUploadArquivoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    void createUploadArquivo(final UploadArquivo entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    void updateUploadArquivo(final UploadArquivo entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    void removeUploadArquivo(final UploadArquivo entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    UploadArquivo findUploadArquivoById(final Long id);

    /**
	 * Realiza o download de um arquivo que contido no servidor.
     * 
     * @param path
     *            - caminho do arquivo no file system
     * @param fileName
     *            - nome do arquivo a ser baixado
     * @param contentType
     *            - tipo do arquivo
     */
	void downloadFile(String path, String fileName, String contentType);
}
