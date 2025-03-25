package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Potencial;


/**
 * 
 * A classe PotencialService proporciona as funcionalidades de Servico para
 * entidade Potencial.
 * 
 * @since 05/04/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public interface IPotencialService {

    /**
     * Busca todas as entidades ativas.
     * 
     * @return retorna uma lista de Potencial.
     */
    List<Potencial> findPotencialAllActive();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Potencial findPotencialById(final Long id);

}