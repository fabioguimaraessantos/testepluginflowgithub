/**
 * ICentroLucro - Interface para o DAO do Centro de Lucro
 */
package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.NaturezaCentroLucro;

/**
 * A classe ICentroLucro proporciona a interface das funcionalidades de acesso
 * ao banco para a entidade CentroLucro.
 * 
 * @since 03/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface ICentroLucroDao extends IAbstractDao<Long, CentroLucro> {

	/**
	 * Busca uma lista de CentroLucro pelo filtro.
	 * 
	 * @param centroLucro
	 *            do tipo CentroLucro populada com os valores do filtro
	 * @return lista de CentroLucro que atendem ao criterio de filtro
	 */
	List<CentroLucro> findByFilter(final CentroLucro centroLucro);

	/**
	 * Busca uma lista de CentroLucro pelo filtro e carrega a entidade
	 * NaturezaCentroLucro.
	 * 
	 * @param centroLucro
	 *            do tipo CentroLucro populada com os valores do filtro
	 * @return lista de CentroLucro que atendem ao criterio de filtro
	 */
	List<CentroLucro> findByFilterFetch(final CentroLucro centroLucro);

	/**
	 * Busca todos os CentroLucro.
	 * 
	 * @return lista de CentroLucro com todos os centro de lucros
	 */
	List<CentroLucro> findAll();

	/**
	 * Retorna todas as entidades relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<CentroLucro> findByNatureza(final NaturezaCentroLucro natureza);

	/**
	 * Busca por dealfiscal e natureza.
	 * 
	 * @param df
	 *            the {@link DealFiscal}
	 * 
	 * @param codigoNatureza
	 *            codigo da natureza
	 * @Param dataVigencia data de vigencia
	 * @return listResult
	 */
	List<CentroLucro> findByDealFiscalAndNatureza(final DealFiscal df,
			final Long codigoNatureza, final Date dataVigencia);

	/**
	 * Retorna todas as entidades ativas relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<CentroLucro> findActiveByNatureza(NaturezaCentroLucro natureza);


	/**
	 * Retorna todas as entidades ativas relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 *
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 *
	 * @return retorna uma lista com todas as entidades.
	 */
	List<String> findNameActiveByNatureza(NaturezaCentroLucro natureza);

	/**
	 * Retorna a entidade relacionada ao name recebido
	 ** @param name
	 * @return
	 */
	List<CentroLucro> findCentroLucroBusinessManagerByName(NaturezaCentroLucro natureza,String name);
}
