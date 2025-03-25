package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.Pmg;


/**
 * 
 * A classe IPmgService proporciona as funcionalidades de ... para ...
 * 
 * @since 20/06/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Service
public interface IPmgService {

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Pmg> findPmgAll();
    
    /**
     * Busca pelo id do Pmg.
     * @param id id
     * @return Pmg
     */
    Pmg findPmgById(final Long id);

}
