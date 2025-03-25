package com.ciandt.pms.business.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AlocacaoFoto;


/**
 * 
 * A classe IAlocacaoFotoService proporciona a interface de servico para a
 * entidade AlocacaoFoto.
 * 
 * @since 18/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IAlocacaoFotoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createAlocacaoFoto(final AlocacaoFoto entity);

    /**
     * Atualiza a entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     */
    @Transactional
    void updateAlocacaoFoto(final AlocacaoFoto entity);

    /**
     * Remove a entidade passado por parametro.
     * 
     * @param entity
     *            que será removida.
     * 
     */
    @Transactional
    void removeAlocacaoFoto(final AlocacaoFoto entity);

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    AlocacaoFoto findAlocacaoFotoById(final Long id);

}