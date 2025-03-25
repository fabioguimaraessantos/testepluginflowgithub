package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ChbackPessLogSincronizacao;


/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 26/08/2011
 */
@Service
public interface IChbackPessLogSincService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     * 
     */
    @Transactional
    void createChbackPessLogSinc(final ChbackPessLogSincronizacao entity);

}