package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.ITipoContratoService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.TipoContrato;
import com.ciandt.pms.persistence.dao.ITipoContratoDao;


/**
 * 
 * A classe TipoContratoService proporciona as funcionalidades da camada de
 * serviço referentwe a entidade TipoServico.
 * 
 * @since 26/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class TipoContratoService implements ITipoContratoService {

    /** Entidade dao do TipoContrato. */
    @Autowired
    private ITipoContratoDao dao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            a ser inserida
     */
    public void createTipoContrato(final TipoContrato entity) {
        dao.create(entity);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    public List<TipoContrato> findTipoContratoAll() {
        return dao.findAll();
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    public List<TipoContrato> findTipoContratoAllActive() {
        return dao.findAllActive();
    }

    /**
     * Busca uma lista de entidades por filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio do filtro
     */
    public List<TipoContrato> findTipoContratoByFilter(final TipoContrato filter) {
        return null;
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public TipoContrato findTipoContratoById(final Long id) {
        return dao.findById(id);
    }

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
     */
    public void removeTipoContrato(final TipoContrato entity)
            throws IntegrityConstraintException, ConstraintViolationException,
            DataIntegrityViolationException {
        dao.remove(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * @exception IntegrityConstraintException
     *                lança exceção caso a Pratica possua ContratoPratica
     *                associados e tente inativá-la.
     */
    public void updateTipoContrato(final TipoContrato entity)
            throws IntegrityConstraintException {
        dao.update(entity);
    }

}
