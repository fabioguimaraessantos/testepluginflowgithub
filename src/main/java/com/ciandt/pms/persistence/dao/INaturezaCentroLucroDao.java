package com.ciandt.pms.persistence.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.enums.NaturezaCentroLucroSigla;
import com.ciandt.pms.model.NaturezaCentroLucro;


/**
 * Define a interface do DAO da entidade.
 * 
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * @since 01/08/2009
 */
@Repository
public interface INaturezaCentroLucroDao extends
        IAbstractDao<Long, NaturezaCentroLucro> {

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param filter
     *            entidade populada com os valores do filtro.
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<NaturezaCentroLucro> findByFilter(final NaturezaCentroLucro filter);

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    List<NaturezaCentroLucro> findAll();

    /**
     * Busca uma lista de entidades pelo filtro.
     * 
     * @param codigoGcPeriodo
     *            - codigo do GrupoCustoPeriodo
     * 
     * @return lista de entidades que atendem ao criterio de filtro.
     */
    List<NaturezaCentroLucro> findAllNotInGrupoCusto(final Long codigoGcPeriodo);

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