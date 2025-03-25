package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.HistoricoPercentualIntercomp;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/12/2015
 */
@Repository
public interface IHistoricoPercentualIntercompDao extends IAbstractDao<Long, HistoricoPercentualIntercomp> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<HistoricoPercentualIntercomp> findAll();


    /**
     * Retorna todas as entidades.
     *
     * @return retorna uma lista com todas as entidades.
     */
    List<HistoricoPercentualIntercomp> findByDealFiscal(Long dealFiscalCode);
}