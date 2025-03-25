package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteStatus;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 11/12/2013
 */
@Service
public interface IControleReajusteStatusService {

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	ControleReajusteStatus findControleReajusteStatusById(final Long id);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<ControleReajusteStatus> findControleReajusteStatusAll();

    /**
     * Retorna todos os status de {@link ControleReajuste} ativos.
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
    List<ControleReajusteStatus> findControleReajusteAllActive();
    
    /**
     * Retorna o status de {@link ControleReajuste} em que possui a sigla igual a .
     *
     * @return Lista de {@link ControleReajusteStatus}
     */
    ControleReajusteStatus findControleReajusteStatusBySiglaControleReajusteStatus(String siglaStatus);
}