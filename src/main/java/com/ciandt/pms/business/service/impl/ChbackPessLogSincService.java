package com.ciandt.pms.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IChbackPessLogSincService;
import com.ciandt.pms.model.ChbackPessLogSincronizacao;
import com.ciandt.pms.persistence.dao.IChbackPessLogSincDao;


/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 26/08/2010
 */
@Service
public class ChbackPessLogSincService implements IChbackPessLogSincService {

    /** Instancia do DAO da entidade. */
    @Autowired
    private IChbackPessLogSincDao chbackPessLogSincDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createChbackPessLogSinc(final ChbackPessLogSincronizacao entity) {
        chbackPessLogSincDao.create(entity);
    }

}