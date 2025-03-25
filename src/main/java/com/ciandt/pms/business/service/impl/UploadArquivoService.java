package com.ciandt.pms.business.service.impl;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IUploadArquivoService;
import com.ciandt.pms.model.UploadArquivo;
import com.ciandt.pms.persistence.dao.IUploadArquivoDao;
import com.ciandt.pms.util.FileUtil;

/**
 * 
 * A classe UploadArquivoService proporciona as funcionalidades da camada de
 * serviço referente a entidade UploadArquivo.
 * 
 * @since 23/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class UploadArquivoService implements IUploadArquivoService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IUploadArquivoDao dao;

    /** Arquivo de configuracoes. */
    @Autowired
    private Properties systemProperties;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createUploadArquivo(final UploadArquivo entity) {
        
        String textoErro = entity.getTextoErro();
		if (textoErro != null && textoErro.length() > 240) {
            entity.setTextoErro(textoErro.substring(0, 230) + "...");
        }
        
      dao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     */
    public void updateUploadArquivo(final UploadArquivo entity) {
        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     */
    public void removeUploadArquivo(final UploadArquivo entity) {
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
    public UploadArquivo findUploadArquivoById(final Long id) {
        return dao.findById(id);
    }

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
	@Override
	public void downloadFile(String path, String fileName, String contentType) {
		FileUtil.downloadFile(path, fileName, contentType);
    }

}