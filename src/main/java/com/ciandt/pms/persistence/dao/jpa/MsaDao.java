package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IMsaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * A classe MsaDao proporciona as funcionalidades de acesso ao banco de dados
 * referentes a entidade Msa.
 * 
 * @since 01/10/2012
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class MsaDao extends AbstractDao<Long, Msa> implements IMsaDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public MsaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Msa.class);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<Msa> findAll() {
		List<Msa> listResult = getJpaTemplate().findByNamedQuery(Msa.FIND_ALL);

		return listResult;
	}
	
	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades mas apenas o codigo e
	 *         nome estao preenchidos.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Msa> findAllReturnCodigoAndNomeMsa() {
		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_ALL_RETURN_CODIGO_AND_NOME_MSA);

		return listResult;
	}

	@Override
	public List<Msa> findByBmDn(Long bmdn) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bmdn", bmdn);

		return  getJpaTemplate().findByNamedQueryAndNamedParams(Msa.FIND_BY_BMDN, params);
	}

	@Override
	public List<Msa> findByIndustryType(Long industryType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("industryType", industryType);

		return  getJpaTemplate().findByNamedQueryAndNamedParams(Msa.FIND_BY_INDUSTRY_TYPE, params);
	}

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	@SuppressWarnings("unchecked")
	public List<Msa> findByFilter(final Msa filter) {

		Long idCliente = filter.getCliente().getCodigoCliente();
		if (idCliente == null) {
			idCliente = 0L;
		}

		Long idBmDn = filter.getBmDn();
		if (idBmDn == null) {
			idBmDn = 0L;
		}

		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_BY_FILTER,
				new Object[] { filter.getNomeMsa(), filter.getNomeMsa(),
						idCliente, idCliente, idBmDn, idBmDn, filter.getIndustryType(), filter.getIndustryType(), filter.getIndicadorStatus(),
						filter.getIndicadorStatus() });
		return listResult;
	}

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os Msa
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<Msa> findByCliente(final Cliente cliente) {

		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_BY_CLIENTE,
				new Object[] { cliente.getCodigoCliente() });

		return listResult;
	}

	/**
	 * Retorna todas as entidades no estado completo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	@SuppressWarnings("unchecked")
	public List<Msa> findAllComplete() {
		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_ALL_COMPLETE);
		return listResult;
	}

	/**
	 * Retorna uma busca rapida por parte do nome do msa.(AutoComplete)
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<Msa> findbyNameQuick(final String name) {
		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_QUICK_BY_NAME, new Object[] { name });
		return listResult;
	}

	/**
	 * Retorna uma busca nome do msa.
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public Msa findByName(String name) {
		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_BY_NAME, new Object[] { name });
		if (listResult.isEmpty()) {
			return null;
		}
		return listResult.get(0);
	}

	/**
	 * Retorna uma busca nome do msa.
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public Msa findAllByName(Msa msa) {
		List<Msa> listResult = getJpaTemplate().findByNamedQuery(
				Msa.FIND_ALL_BY_NAME, new Object[] { msa.getNomeMsa() });
		if (listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

	/**
	 * Retorna um Msa relacionado a {@link CentroLucro}.
	 *
	 * @param centroLucro
	 * @return {@link Msa}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Msa> findByCentroLucro(CentroLucro centroLucro) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoCentroLucro", centroLucro.getCodigoCentroLucro());

		List<Msa> results = getJpaTemplate().findByNamedQueryAndNamedParams(
				Msa.FIND_BY_CENTRO_LUCRO, params);

		return results;
	}
}