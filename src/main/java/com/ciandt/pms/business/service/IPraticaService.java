package com.ciandt.pms.business.service;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.Pratica;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 
 * A classe IPraticaService proporciona a interface de acesso a camada de
 * serviço referente a entidade Pratica.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IPraticaService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            a ser inserida
     */
    @Transactional
    void createPratica(final Pratica entity);

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * 
     * @throws IntegrityConstraintException
     *             lança exceção caso a Pratica possua ContratoPratica
     *             associados e tente inativá-la.
     */
    @Transactional
    void updatePratica(final Pratica entity)
            throws IntegrityConstraintException, MoreThanOneActiveEntityException;

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @throws IntegrityConstraintException
     *             lança exceção caso a Pratica possua ContratoPratica
     *             associados e tente inativá-la.
     */
    @Transactional
    void removePratica(final Pratica entity)
            throws IntegrityConstraintException;

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<Pratica> findPraticaAll();

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    Pratica findPraticaById(final Long id);

    /**
     * Busca uma lista de entidades por filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio do filtro
     */
    List<Pratica> findPraticaByFilter(final Pratica filter);
}
