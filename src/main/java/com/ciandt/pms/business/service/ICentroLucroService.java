/**
 * ICentroLucroService - Interface para o servico de CentroLucro
 */
package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.exception.IntegrityConstraintException;
import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.NaturezaCentroLucro;

/**
 * 
 * A classe ICentroLucroService proporciona a interface para as funcionalidades
 * de servico para referentes ao centro de lucro.
 * 
 * @since 04/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface ICentroLucroService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createCentroLucro(final CentroLucro entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Transactional
	void updateCentroLucro(final CentroLucro entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 * 
	 * @throws IntegrityConstraintException
	 *             lança exceção caso o CentroLucro possua ContratoPratica
	 *             associados e tente inativá-la.
	 */
	@Transactional
	void removeCentroLucro(final CentroLucro entity)
			throws IntegrityConstraintException;

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	CentroLucro findCentroLucroById(final Long id);

	/**
	 * Busca uma lista de entidades pelo filtro. Porem nao carrega previamente o
	 * atributo 'naturezaCentroLucro'.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<CentroLucro> findCentroLucroByFilter(final CentroLucro filter);

	/**
	 * Busca uma lista de entidades pelo filtro. Porem carrega previamente o
	 * atributo 'naturezaCentroLucro'.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<CentroLucro> findCentroLucroByFilterFetch(final CentroLucro filter);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<CentroLucro> findCentroLucroAll();

	/**
	 * Retorna todas as entidades relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<CentroLucro> findCentroLucroByNatureza(
			final NaturezaCentroLucro natureza);

	/**
	 * Busca por dealfiscal e natureza.
	 * 
	 * @param df
	 *            the {@link DealFiscal}
	 * 
	 * @param codigoNatureza
	 *            codigo da natureza
	 * @param dataVigencia
	 *            data Vigencia
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
	 * Busca uma entidade pelo name.
	 *
	 * @param name
	 *
	 * @return entidade com o name passado por parametro.
	 */
	List<CentroLucro> findCentroLucroBusinessManagerByName(NaturezaCentroLucro natureza, String name);


}

