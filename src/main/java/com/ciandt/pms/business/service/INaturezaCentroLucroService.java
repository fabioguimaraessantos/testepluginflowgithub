package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.enums.NaturezaCentroLucroSigla;
import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.NaturezaCentroLucro;

/**
 * Define a interface do Service da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Service
public interface INaturezaCentroLucroService {

    /**
     * Insere uma entidade.
     * 
     * @param entity
     *            entidade a ser inserida.
     */
    @Transactional
    void createNaturezaCentroLucro(final NaturezaCentroLucro entity);

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
    @Transactional
    void updateNaturezaCentroLucro(final NaturezaCentroLucro entity)
            throws IntegrityConstraintException;

    /**
     * Deleta uma entidade.
     * 
     * @param entity
     *            que ser� apagada.
     * @throws IntegrityConstraintException
     *             - tratamento erro de refer�ncia de integridade
     * 
     */
    @Transactional
    void removeNaturezaCentroLucro(final NaturezaCentroLucro entity)
            throws IntegrityConstraintException;

    /**
     * Busca uma entidade pelo id.
     * 
     * @param id
     *            da entidade.
     * 
     * @return entidade com o id passado por parametro.
     */
    NaturezaCentroLucro findNaturezaCentroLucroById(final Long id);

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<NaturezaCentroLucro> findNaturezaCentroLucroByFilter(
            final NaturezaCentroLucro filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<NaturezaCentroLucro> findNaturezaCentroLucroAll();

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param codigoGcPeriodo
     *            - codigo do GrupoCustoPeriodo
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<NaturezaCentroLucro> findNaturezaAllNotInGrupoCusto(final Long codigoGcPeriodo);

	/**
	 * Busca uma {@link NaturezaCentroLucro} por uma determinada
	 * {@link NaturezaCentroLucroSigla}.
	 * 
	 * @param sigla
	 *            {@link NaturezaCentroLucroSigla}
	 * @return lista de {@link NaturezaCentroLucro}
	 */
	NaturezaCentroLucro findBySigla(final NaturezaCentroLucroSigla sigla);

}