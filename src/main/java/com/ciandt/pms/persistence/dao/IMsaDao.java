package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.CentroLucro;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Msa;

import java.util.List;

/**
 * 
 * A classe IMsaDao proporciona a interface de acesso para a camada DAO da
 * entidade Msa.
 * 
 * @since 01/10/2012
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IMsaDao extends IAbstractDao<Long, Msa> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Msa> findAll();

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro
	 */
	List<Msa> findByFilter(final Msa filter);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os Msa
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Msa> findByCliente(final Cliente cliente);

	/**
	 * Retorna todas as entidades no estado completo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Msa> findAllComplete();

	/**
	 * Retorna uma busca rapida por parte do nome do msa.(AutoComplete)
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	List<Msa> findbyNameQuick(final String name);

	/**
	 * Retorna uma busca nome do msa.
	 * 
	 * @param name
	 *            - nome do msa
	 * @return listResult
	 */
	Msa findByName(String name);

	/**
	 * Retorna um Msa relacionado a {@link CentroLucro}.
	 *
	 * @param centroLucro
	 * @return {@link Msa}
	 */
	List<Msa> findByCentroLucro(CentroLucro centroLucro);

	/**
	 * Retorna uma busca nome do msa.
	 *
	 * @param msa
	 *            - nome do msa
	 * @return listResult
	 */
	Msa findAllByName(Msa msa);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades mas apenas o codigo e
	 *         nome estao preenchidos.
	 */
	List<Msa> findAllReturnCodigoAndNomeMsa();


	/**
	 * Retorna um Msa relacionado a Bmdn.
	 *
	 * @param bmdn
	 * @return {@link Msa}
	 */
	List<Msa> findByBmDn(Long bmdn);

	/**
	 * Retorna um Msa relacionado a IndustryType.
	 *
	 * @param industryType
	 * @return {@link Msa}
	 */
	List<Msa> findByIndustryType(Long industryType);
}
