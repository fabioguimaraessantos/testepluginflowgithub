package com.ciandt.pms.persistence.dao;

import java.util.List;

import com.ciandt.pms.model.TipoDespesa;


/**
 * 
 * A classe ITipoDespesaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade TipoDespesa.
 * 
 * @since 12/04/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface ITipoDespesaDao extends IAbstractDao<Long, TipoDespesa> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<TipoDespesa> findAllActive();

    /**
     * Busca por Nome.
     * 
     * @param nomeTipoDespesa
     *            nome do TipoDespesa
     * 
     * @return retorna um tipoDespesa.
     */
    TipoDespesa findByName(String nomeTipoDespesa);
}
