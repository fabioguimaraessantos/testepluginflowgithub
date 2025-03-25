package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IPadraoArquivoService;
import com.ciandt.pms.model.PadraoArquivo;
import com.ciandt.pms.persistence.dao.IPadraoArquivoDao;


/**
 * 
 * A classe PadraoArquivoService proporciona as funcionalidades da
 * camada de serviço referente a entidade PadraoArquivo.
 *
 * @since 19/08/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class PadraoArquivoService implements IPadraoArquivoService {

    /** Instancia do DAO. */
    @Autowired
    private IPadraoArquivoDao dao;
    
    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<PadraoArquivo> findPadraoArquivoAll() {

        return dao.findAll();
    }
    
    /**
     * Busca uma entidade pelo Id.
     * 
     * @param entityId id da entidade
     * 
     * @return retorna a entidade referente ao id passado 
     * por parametro, caso não encontrado retorna null.
     */
    public PadraoArquivo findPadraoArquivoById(final Long entityId) {
        return dao.findById(entityId);
    }
}
