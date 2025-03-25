package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AlocacaoPeriodoFoto;


/**
 * 
 * A classe IAlocacaoPeriodoFotoService proporciona a interface para acesso ao
 * servicos relacionados a entidade AlocacaoPeriodoFotoService.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IAlocacaoPeriodoFotoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createAlocacaoPeriodoFoto(final AlocacaoPeriodoFoto entity);

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateAlocacaoPeriodoFoto(final AlocacaoPeriodoFoto entity);

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    @Transactional
    void removeAlocacaoPeriodoFoto(final AlocacaoPeriodoFoto entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    AlocacaoPeriodoFoto findAlocacaoPeriodoFotoById(final Long id);

}