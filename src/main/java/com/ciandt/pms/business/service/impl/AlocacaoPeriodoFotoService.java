package com.ciandt.pms.business.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IAlocacaoPeriodoFotoService;
import com.ciandt.pms.model.AlocacaoPeriodoFoto;
import com.ciandt.pms.persistence.dao.IAlocacaoPeriodoFotoDao;


/**
 * 
 * A classe AlocacaoPeriodoFotoService proporciona as funcionalidades de serviço
 * para a entidade AlocacaoPeriodoFoto.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class AlocacaoPeriodoFotoService implements IAlocacaoPeriodoFotoService {

    /** DAO da entidade PeriodoAlocacaoFoto. */
    @Autowired
    private IAlocacaoPeriodoFotoDao alocacaoPeriodoFotoDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    public void createAlocacaoPeriodoFoto(final AlocacaoPeriodoFoto entity) {
        alocacaoPeriodoFotoDao.create(entity);
    }

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    public void updateAlocacaoPeriodoFoto(final AlocacaoPeriodoFoto entity) {
        alocacaoPeriodoFotoDao.update(entity);
    }

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    public void removeAlocacaoPeriodoFoto(final AlocacaoPeriodoFoto entity) {
        alocacaoPeriodoFotoDao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public AlocacaoPeriodoFoto findAlocacaoPeriodoFotoById(final Long id) {
        return alocacaoPeriodoFotoDao.findById(id);
    }

}