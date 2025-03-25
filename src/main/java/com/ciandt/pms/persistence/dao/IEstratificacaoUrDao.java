package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.EstratificacaoUr;


/**
 * 
 * A classe IEstratificacaoUrDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade EstratificacaoUr.
 * 
 * @since 15/04/2011
 * @author cmantovani
 * 
 */
public interface IEstratificacaoUrDao extends
        IAbstractDao<Long, EstratificacaoUr> {

    /**
     * Busca pelo id da receita.
     * 
     * @param idReceita
     *            id da receita
     * 
     * @return retorna uma lista de EstratificacaoUr
     */
    List<EstratificacaoUr> findByIdReceita(final Long idReceita);

    /**
     * Busca pelo id do MapaAlocacao.
     * 
     * @param idMapaAlocacao
     *            - id do MapaAlocacao
     * @param data
     *            - data da estratificação
     * 
     * @return retorna uma lista de EstratificacaoUr
     */
    EstratificacaoUr findByIdMapaAlocacaoData(final Long idMapaAlocacao,
            final Date data);

}