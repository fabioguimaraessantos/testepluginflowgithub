package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IFonteReceitaService;
import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.persistence.dao.IFonteReceitaDao;


/**
 * 
 * A classe FonteRecietaService proporciona as funcionalidades
 * da camada de serviço referente a entidade FonteReceita.
 *
 * @since 03/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Service
public class FonteRecietaService implements IFonteReceitaService {

    /** Instancia de FonteReceitaDao. */
    @Autowired
    private IFonteReceitaDao dao;
    
    /**
     * Pega todas as entidades ativas no sistema.
     * 
     * @return lista com todas as entidades.
     */
    public List<FonteReceita> findFonteReceitaAll() {
        return dao.findAll();
    }
    
    /**
     * Busca a entidade pelo id.
     *
     * @param id - Id da entidade.
     * 
     * @return a entidade com o id correspondente, ou null caso não exista.
     */
    public FonteReceita findFonteReceitaById(final Long id) {
        return dao.findById(id);
    }

}
