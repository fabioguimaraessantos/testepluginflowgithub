package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Atividade;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 17/08/2010
 */
@Repository
public interface IAtividadeDao extends IAbstractDao<Long, Atividade> {

    /**
     * Retorna todas as entidades - ativas.
     * 
     * @return retorna uma lista com todas as entidades - ativas.
     */
    List<Atividade> findAllActive();

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Atividade> findAll();
    
    /**
     * Retorna a Atividade referente a sigla.
     * 
     * @param sigla da atividade
     * 
     * @return retorna uma entidade do tipo Atividade 
     */
    Atividade findBySigla(final String sigla);

}