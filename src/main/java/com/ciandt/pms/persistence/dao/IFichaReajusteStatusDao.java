package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:luizsjn@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Repository
public interface IFichaReajusteStatusDao extends IAbstractDao<Long, FichaReajusteStatus> {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FichaReajusteStatus> findAll();

    /**
     * Busca todos os status de {@link FichaReajuste} ativos.
     *
     * @return Lista de {@link FichaReajusteStatus}
     */
    List<FichaReajusteStatus> findAllActive();
    
    /**
     * Busca status de {@link FichaReajuste} com sigla igual a {@code siglaStatus}.
     *
     * @param siglaStatus
     * @return Lista de {@link FichaReajusteStatus}
     */
    FichaReajusteStatus findBySiglaStatus(String siglaStatus);
}