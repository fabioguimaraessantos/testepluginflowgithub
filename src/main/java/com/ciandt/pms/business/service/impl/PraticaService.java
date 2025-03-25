package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IPraticaService;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.Pratica;
import com.ciandt.pms.persistence.dao.IPraticaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 
 * A classe PraticaService proporciona as funcionalidades da camada de serviço
 * referente a entidade Pratica.
 * 
 * @since 26/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public class PraticaService implements IPraticaService {

    /** instancia do DAO da entidade Pratica. */
    @Autowired
    private IPraticaDao praticaDao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida
     */
    public void createPratica(final Pratica entity) {
        praticaDao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que será atualizada.
     * @throws IntegrityConstraintException
     *             lança exceção caso a Pratica possua ContratoPratica
     *             associados e tente inativá-la
     */
    public void updatePratica(final Pratica entity) throws IntegrityConstraintException {
        // busca a entidade do banco por causa da sessão de conexão (hibernate)
        Pratica pratica = findPraticaById(entity.getCodigoPratica());
        // verifica se a entidade da tela está sendo inativada
        if (entity.getIndicadorAtivo().equals(Constants.INACTIVE)) {
            // se sim, usa a entidade do banco para verificar se existem filhos,
            // e lança exceção
            if (pratica.getContratoPraticas().size() > 0) {
                throw new IntegrityConstraintException(
                        Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_INACTIVE);
            }
        }
        praticaDao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que será apagada.
     * 
     * @throws IntegrityConstraintException
     *             lança exceção caso a Pratica possua ContratoPratica
     *             associados e tente inativá-la
     */
    public void removePratica(final Pratica entity)
            throws IntegrityConstraintException {
        // verifica se existem CentroLucro relacionados, se sim lança exceção
        if (entity.getContratoPraticas().size() > 0) {
            throw new IntegrityConstraintException(
                    Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
        }
        praticaDao.remove(entity);
    }

    /**
     * Busca todas as entidades.
     * 
     * @return retorna todas as entidades.
     */
    public List<Pratica> findPraticaAll() {
        return praticaDao.findAll();
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    public Pratica findPraticaById(final Long id) {
        return praticaDao.findById(id);
    }

    /**
     * Busca uma lista de entidades por filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio do filtro
     */
    public List<Pratica> findPraticaByFilter(final Pratica filter) {
        return praticaDao.findByFilter(filter);
    }
}
