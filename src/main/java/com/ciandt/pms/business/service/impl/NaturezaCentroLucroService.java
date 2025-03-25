package com.ciandt.pms.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.INaturezaCentroLucroService;
import com.ciandt.pms.enums.NaturezaCentroLucroSigla;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.persistence.dao.INaturezaCentroLucroDao;

/**
 * Define o Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public class NaturezaCentroLucroService implements INaturezaCentroLucroService {

    /**
     * Instancia do DAO da entidade.
     */
    @Autowired
    private INaturezaCentroLucroDao dao;

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
	@Override
    public void createNaturezaCentroLucro(final NaturezaCentroLucro entity) {
        dao.create(entity);
    }

    /**
     * Executa um update na entidade passado por parametro.
     * 
     * @param entity
     *            que ser� atualizada.
     * @throws IntegrityConstraintException
     *             lan�a exce��o caso a NaturezaCentroLucro possua CentroLucro
     *             associados e tente inativ�-la
     * 
     */
	@Override
    public void updateNaturezaCentroLucro(final NaturezaCentroLucro entity)
            throws IntegrityConstraintException {
        dao.update(entity);
    }

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que ser� apagada.
     * @throws IntegrityConstraintException
     *             - tratamento erro de refer�ncia de integridade
     * 
     */
	@Override
    public void removeNaturezaCentroLucro(final NaturezaCentroLucro entity)
            throws IntegrityConstraintException {
        // verifica se existem CentroLucro relacionados, se sim lan�a exce��o
        if (entity.getCentroLucros().size() > 0) {
            throw new IntegrityConstraintException(
                    Constants.DEFAULT_MSG_ERROR_INTEGRITY_CONSTRAINT_REMOVE);
        }

        dao.remove(entity);
    }

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
	@Override
    public NaturezaCentroLucro findNaturezaCentroLucroById(final Long id) {
        return dao.findById(id);
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
	@Override
    public List<NaturezaCentroLucro> findNaturezaCentroLucroByFilter(
            final NaturezaCentroLucro filter) {
        return dao.findByFilter(filter);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
	@Override
    public List<NaturezaCentroLucro> findNaturezaCentroLucroAll() {

        return dao.findAll();
    }

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param codigoGcPeriodo
     *            - codigo do GrupoCustoPeriodo
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
	@Override
    public List<NaturezaCentroLucro> findNaturezaAllNotInGrupoCusto(
            final Long codigoGcPeriodo) {
        return dao.findAllNotInGrupoCusto(codigoGcPeriodo);
    }

	/**
	 * Busca uma {@link NaturezaCentroLucro} por uma determinada
	 * {@link NaturezaCentroLucroSigla}.
	 * 
	 * @param sigla
	 *            {@link NaturezaCentroLucroSigla}
	 * @return lista de {@link NaturezaCentroLucro}
	 */
	@Override
	public NaturezaCentroLucro findBySigla(final NaturezaCentroLucroSigla sigla) {
		return dao.findBySigla(sigla);
	}

}