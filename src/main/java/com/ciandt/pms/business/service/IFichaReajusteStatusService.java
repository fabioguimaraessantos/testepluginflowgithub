package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.FichaReajusteStatus;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IFichaReajusteStatusService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	FichaReajusteStatus findFichaReajusteStatusById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<FichaReajusteStatus> findFichaReajusteStatusAll();

    /**
     * Busca todos os status de {@link FichaReajuste} ativos.
     *
     * @return Lista de {@link FichaReajusteStatus}
     */
    List<FichaReajusteStatus> findAllFichaReajusteStatusActive();
    
    /**
     * Busca status de {@link FichaReajuste} com sigla igual a {@code siglaStatus}.
     *
     * @param siglaStatus
     * @return Lista de {@link FichaReajusteStatus}
     */
    FichaReajusteStatus findFichaReajusteStatusBySiglaStatus(String siglaStatus);
}