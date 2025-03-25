package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.TceLogSincronizacao;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 13/06/2011
 */
@Service
public interface ITceLogSincronizacaoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     */
    @Transactional
    void createTceLogSinc(final TceLogSincronizacao entity);

}