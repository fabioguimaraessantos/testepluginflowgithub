package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.FonteReceita;


/**
 * 
 * A classe IFonteReceitaDao proporciona a interface de acesso
 * a camada de persistencia, referente a entidade FonteReceita.
 *
 * @since 03/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IFonteReceitaDao extends IAbstractDao<Long, FonteReceita> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FonteReceita> findAll();
    
}
