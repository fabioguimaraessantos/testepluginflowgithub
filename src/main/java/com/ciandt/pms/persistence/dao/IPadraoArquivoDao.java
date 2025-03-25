package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.PadraoArquivo;


/**
 * 
 * A classe IPadraoArquivoDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade PadraoArquivo.
 *
 * @since 19/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IPadraoArquivoDao extends IAbstractDao<Long, PadraoArquivo> {

    /**
     * Busca todos os padrões de arquivo.
     * 
     * @return retorna uma lista de padrões de arquivos.
     */
    List<PadraoArquivo> findAll();
}
