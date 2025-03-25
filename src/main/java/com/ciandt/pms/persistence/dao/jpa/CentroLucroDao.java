/**
 * CentroLucroDAO - Centro de Lucro DAO
 */
package com.ciandt.pms.persistence.dao.jpa;

/**
 * CentroLucroDAO, classe que possui as funcionalidades responsaveis 
 * por manipular a entidade CentroLucro no banco de dados
 */
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.NaturezaCentroLucro;
import com.ciandt.pms.persistence.dao.ICentroLucroDao;

/**
 * A classe CentroLucroDAO proporciona as funcionalidades de acesso ao banco
 * para manipular a entidade de CentroLucro.
 * 
 * @since 03/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class CentroLucroDao extends AbstractDao<Long, CentroLucro> implements
		ICentroLucroDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            fabrica do dao
	 */
	@Autowired
	public CentroLucroDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, CentroLucro.class);
	}

	/**
	 * Busca uma lista de CentroLucro pelo filtro.
	 * 
	 * @param centroLucro
	 *            do tipo CentroLucro populada com os valores do filtro
	 * @return lista de CentroLucro que atendem ao criterio de filtro
	 */
	@SuppressWarnings("unchecked")
	public List<CentroLucro> findByFilter(final CentroLucro centroLucro) {
		List<CentroLucro> listResult = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_BY_FILTER,
				new Object[] {
						centroLucro.getNomeCentroLucro(),
						centroLucro.getNomeCentroLucro(),
						centroLucro.getNaturezaCentroLucro()
								.getCodigoNatureza(),
						centroLucro.getNaturezaCentroLucro()
								.getCodigoNatureza() });
		return listResult;
	}

	/**
	 * Busca uma lista de CentroLucro pelo filtro. E tambem carrega previamente
	 * a entidade NaturezaCentroLucro
	 * 
	 * @param centroLucro
	 *            do tipo CentroLucro populada com os valores do filtro
	 * @return lista de CentroLucro que atendem ao criterio de filtro
	 */
	@SuppressWarnings("unchecked")
	public List<CentroLucro> findByFilterFetch(final CentroLucro centroLucro) {

		return getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_BY_FILTER_FETCH,
				new Object[] {
						centroLucro.getNomeCentroLucro(),
						centroLucro.getNomeCentroLucro(),
						centroLucro.getNaturezaCentroLucro()
								.getCodigoNatureza(),
						centroLucro.getNaturezaCentroLucro()
								.getCodigoNatureza() });
	}

	/**
	 * Busca por todos os CentroLucros.
	 * 
	 * @return uma lista com todos os CentroLucros.
	 */
	@SuppressWarnings("unchecked")
	public List<CentroLucro> findAll() {
		List<CentroLucro> listResult = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_ALL);

		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<CentroLucro> findByNatureza(final NaturezaCentroLucro natureza) {

		List<CentroLucro> listResult = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_BY_NATUREZA,
				new Object[] { natureza.getCodigoNatureza() });

		return listResult;
	}
	
	/**
	 * Retorna todas as entidades ativas relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 * 
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CentroLucro> findActiveByNatureza(final NaturezaCentroLucro natureza) {
		
		List<CentroLucro> listResult = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_ACTIVE_BY_NATUREZA,
				new Object[] { natureza.getCodigoNatureza() });
		
		return listResult;
	}

	/**
	 * Busca por dealfiscal e natureza.
	 * 
	 * @param df
	 *            the {@link DealFiscal}
	 * 
	 * @param codigoNatureza
	 *            codigo da natureza
	 * @param dataVigencia
	 *            - data da vigencia
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<CentroLucro> findByDealFiscalAndNatureza(final DealFiscal df,
			final Long codigoNatureza, final Date dataVigencia) {

		List<CentroLucro> listResult = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_BY_DEAL_FISCAL_AND_NATUREZA,
				new Object[] { df.getCodigoDealFiscal(), codigoNatureza,
						dataVigencia, dataVigencia, dataVigencia });
		return listResult;
	}

	/**
	 * Retorna todas as entidades ativas relacionadas com a NaturezaCentroLocro passado
	 * por parametro.
	 *
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 *
	 * @return retorna uma lista com name
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> findNameActiveByNatureza(final NaturezaCentroLucro natureza) {

		List<String> listResult = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_NAME_ACTIVE_BY_NATUREZA,
				new Object[] { natureza.getCodigoNatureza() });

		return listResult;
	}

	/**
	 * Retorna o centrolucro relacionado ao name e natureza recebida por parametro
	 *
	 * @param natureza
	 *            - natureza que se deseja buscar os CentroLucro
	 *         name
	 *
	 * @return retorna uma lista o Centro Lucro encontrado
	 */
	@Override
	public List<CentroLucro> findCentroLucroBusinessManagerByName(final NaturezaCentroLucro natureza,String name) {

		List<CentroLucro> centroLucro = getJpaTemplate().findByNamedQuery(
				CentroLucro.FIND_BUSINESS_MANAGER_BY_NAME,
				new Object[] { natureza.getCodigoNatureza(), name });


		return centroLucro;
	}


}
