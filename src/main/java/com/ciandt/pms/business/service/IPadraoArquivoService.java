package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.PadraoArquivo;


/**
 * 
 * A classe IPadraoArquivoService proporciona a 
 * interface de acesso a camada de serviço referente a entidade PadraoArquivo.
 *
 * @since 19/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public interface IPadraoArquivoService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<PadraoArquivo> findPadraoArquivoAll();

    /**
     * Busca uma entidade pelo Id.
     * 
     * @param entityId id da entidade
     * 
     * @return retorna a entidade referente ao id passado 
     * por parametro, caso não encontrado retorna null.
     */
    PadraoArquivo findPadraoArquivoById(final Long entityId);

}
