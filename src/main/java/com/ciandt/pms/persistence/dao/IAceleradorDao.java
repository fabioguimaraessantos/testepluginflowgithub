package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.Acelerador;


/**
 * 
 * A classe IAceleradorDao proporciona a interface de acesso
 * a camada de persistencia referente a entidade Acelerador.
 *
 * @since 06/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
public interface IAceleradorDao
    extends IAbstractDao<Long, Acelerador> {

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Area.
     */
    List<Acelerador> findAllActive();
}
