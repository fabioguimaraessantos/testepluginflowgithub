package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.CustoTiRecurso;
import com.ciandt.pms.model.TiRecurso;


/**
 * 
 * A classe ICustoTiRecursoService proporciona a interface de acesso a camada de
 * serviço referente a entidade CustoTiRecurso.
 * 
 * @since 14/07/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public interface ICustoTiRecursoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createCustoTiRec(final CustoTiRecurso entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateCustoTiRec(final CustoTiRecurso entity);

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     */
    @Transactional
    void removeCustoTiRec(final CustoTiRecurso entity);

    /**
     * Busca os custoTiRecurso por tiRecurso.
     * 
     * @param tiRecurso
     *            - tiRecurso com a busca
     * @return lista com o resultado da consulta
     */
    List<CustoTiRecurso> findCustoTiRecursoByTiRecurso(final TiRecurso tiRecurso);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    CustoTiRecurso findCustoTiRecById(final Long id);

}
