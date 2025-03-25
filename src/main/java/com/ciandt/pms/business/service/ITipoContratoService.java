package com.ciandt.pms.business.service;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.TipoContrato;


/**
 * 
 * A classe ITipoContratoService proporciona a interface de acesso referente a
 * entidade TipoContrato.
 * 
 * @since 26/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface ITipoContratoService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            a ser inserida
     */
    @Transactional
    void createTipoContrato(final TipoContrato entity);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<TipoContrato> findTipoContratoAll();

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    List<TipoContrato> findTipoContratoAllActive();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    TipoContrato findTipoContratoById(final Long id);

    /**
     * Busca uma lista de entidades por filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio do filtro
     */
    List<TipoContrato> findTipoContratoByFilter(final TipoContrato filter);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * @exception IntegrityConstraintException
     *                lança exceção caso a Pratica possua ContratoPratica
     *                associados e tente inativá-la.
     */
    @Transactional
    void updateTipoContrato(final TipoContrato entity)
            throws IntegrityConstraintException;

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @throws IntegrityConstraintException
     *             lança exceção caso a Pratica possua ContratoPratica
     *             associados e tente inativá-la.
     * @throws ConstraintViolationException
     *             - violação de constraint
     * @throws DataIntegrityViolationException
     *             - violação de integridade
     * 
     */
    @Transactional
    void removeTipoContrato(final TipoContrato entity)
            throws IntegrityConstraintException, ConstraintViolationException,
            DataIntegrityViolationException;
}
