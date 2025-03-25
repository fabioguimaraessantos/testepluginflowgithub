package com.ciandt.pms.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAlocacaoFotoService;
import com.ciandt.pms.model.AlocacaoFoto;
import com.ciandt.pms.persistence.dao.IAlocacaoFotoDao;


/**
 * 
 * A classe AlocacaoFotoService proporciona as funcionalidades de serviço para a
 * entidade alocacaoFoto.
 * 
 * @since 19/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class AlocacaoFotoService implements IAlocacaoFotoService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private IAlocacaoFotoDao alocacaoFotoDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createAlocacaoFoto(final AlocacaoFoto entity) {
        alocacaoFotoDao.create(entity);
    }

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateAlocacaoFoto(final AlocacaoFoto entity) {
        alocacaoFotoDao.update(entity);
    }

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    public void removeAlocacaoFoto(final AlocacaoFoto entity) {
        alocacaoFotoDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public AlocacaoFoto findAlocacaoFotoById(final Long id) {
        return alocacaoFotoDao.findById(id);
    }

}