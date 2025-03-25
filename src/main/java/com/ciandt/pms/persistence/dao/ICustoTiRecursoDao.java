package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.TiRecurso;


/**
 * 
 * A classe ICustoTiRecursoDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade CustoTiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public interface ICustoTiRecursoDao extends IAbstractDao<Long, CustoTiRecurso> {

    /**
     * Busca pelo proximo custo, referente a data de inicio.
     * 
     * @param custo
     *            - entidade do tipo CustoTiRecurso.
     * 
     * @return retorna o proximo CustoTiRecurso.
     */
    CustoTiRecurso findNext(final CustoTiRecurso custo);

    /**
     * Busca pelo custo anterior, referente a data de inicio.
     * 
     * @param custo
     *            - entidade do tipo CustoTiRecurso.
     * 
     * @return retorna o CustoTiRecurso anterior.
     */
    CustoTiRecurso findPrevious(final CustoTiRecurso custo);

    /**
     * Busca os custoTiRecurso por tiRecurso.
     * 
     * @param tiRecurso
     *            - tiRecurso com a busca
     * @return lista com o resultado da consulta
     */
    List<CustoTiRecurso> findByTiRecurso(final TiRecurso tiRecurso);
}
