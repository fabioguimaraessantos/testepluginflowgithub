package com.ciandt.pms.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITceLogSincronizacaoService;
import com.ciandt.pms.model.TceLogSincronizacao;
import com.ciandt.pms.persistence.dao.ITceLogSincronizacaoDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 07/06/2010
 */
@Service
public class TceLogSincronizacaoService implements ITceLogSincronizacaoService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private ITceLogSincronizacaoDao tceLogSincDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createTceLogSinc(final TceLogSincronizacao entity) {
        tceLogSincDao.create(entity);
    }

}