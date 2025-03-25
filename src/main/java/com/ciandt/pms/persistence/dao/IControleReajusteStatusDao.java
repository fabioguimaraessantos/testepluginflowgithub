package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteStatus;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Repository
public interface IControleReajusteStatusDao extends IAbstractDao<Long, ControleReajusteStatus> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ControleReajusteStatus> findAll();

    /**
     * Retorna todos os status de {@link ControleReajuste} ativos.
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
    List<ControleReajusteStatus> findAllActive();
    
    /**
     * Retorna o status de {@link ControleReajuste} em que possui a sigla igual a .
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
    ControleReajusteStatus findBySiglaControleReajusteStatus(String siglaStatus);
}