package com.ciandt.pms.persistence.dao.jpa;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ItemTabelaPreco;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.PerfilVendido;
import com.ciandt.pms.model.TabelaPreco;
import com.ciandt.pms.persistence.dao.IItemTabelaPrecoDao;

/**
 * 
 * A classe ItemTabelaPrecoDao proporciona as funcionalidades de acesso ao banco
 * de dados referente a entidade ItemTabelaPreco.
 * 
 * @since 03/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ItemTabelaPrecoDao extends AbstractDao<Long, ItemTabelaPreco>
		implements IItemTabelaPrecoDao {

	/**
	 * Contrutor Padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public ItemTabelaPrecoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ItemTabelaPreco.class);

		entityManager = factory.createEntityManager();
	}

	/** Intancia do entity manager do hibernate. */
	private EntityManager entityManager;

	/**
	 * Busca uma lista de ItemTabelaPreco associado a uma TabelaPreco passada
	 * por parametro.
	 * 
	 * @param tabelaPreco
	 *            do tipo TabelaPreco para realizar a busca
	 * @return lista de ItemTabelaPreco que estao associados a TabelaPreco
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTabelaPreco> findByTabelaPreco(final TabelaPreco tabelaPreco) {

		List<ItemTabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				ItemTabelaPreco.FIND_BY_TABELA_PRECO,
				new Object[] { tabelaPreco.getCodigoTabelaPreco() });

		return listResult;
	}

	/**
	 * Busca um ItemTabelaPreco associado a um Msa e PerfilVendido passada por
	 * parametro.
	 * 
	 * @param msa
	 *            do tipo {@link Msa} para realizar a busca
	 * @param perfil
	 *            do tipo {@link PerfilVendido} para realizar a busca
	 * @param dataMes
	 *            do tipo Date com o mes/ano para realizar a busca
	 * 
	 * @return {@link ItemTabelaPreco} que esta associados a {@link Msa} e
	 *         {@link PerfilVendido}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ItemTabelaPreco findByMsaAndPerfil(final Msa msa,
			final PerfilVendido perfil, final Date dataMes) {

		List<ItemTabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				ItemTabelaPreco.FIND_BY_MSA_AND_PERFIL,
				new Object[] { msa.getCodigoMsa(),
						perfil.getCodigoPerfilVendido(), dataMes, dataMes });

		if (listResult.isEmpty()) {
			return null;
		}
		return listResult.get(0);
	}

	/**
	 * Busca um ItemTabelaPreco associado a uma TabelaPreco e PerfilVendido
	 * passada por parametro.
	 * 
	 * @param tabelaPreco
	 *            do tipo TabelaPreco para realizar a busca
	 * @param perfil
	 *            do tipo PerfilVendido para realizar a busca
	 * 
	 * @return ItemTabelaPreco que esta associados a TabelaPreco e PerfilVendido
	 */
	@SuppressWarnings("unchecked")
	public ItemTabelaPreco findByTabelaPrecoAndPerfil(
			final TabelaPreco tabelaPreco, final PerfilVendido perfil) {

		List<ItemTabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				ItemTabelaPreco.FIND_BY_TABELA_PRECO_AND_PERFIL,
				new Object[] { tabelaPreco.getCodigoTabelaPreco(),
						perfil.getCodigoPerfilVendido() });

		if (listResult.isEmpty()) {
			return null;
		}
		return listResult.get(0);
	}

	/**
	 * Busca uma lista de ItemTabelaPreco associado a um perfil vendido passado
	 * por parametro.
	 * 
	 * @param perfilVendido
	 *            do tipo PerfilVendido para realizar a busca
	 * 
	 * @return lista de ItemTabelaPreco que estao associados a PerfilVendido
	 */
	@SuppressWarnings("unchecked")
	public List<ItemTabelaPreco> findByPerfilVendido(
			final PerfilVendido perfilVendido) {

		List<ItemTabelaPreco> listResult = getJpaTemplate().findByNamedQuery(
				ItemTabelaPreco.FIND_BY_PERFIL_VENDIDO,
				new Object[] { perfilVendido.getCodigoPerfilVendido() });

		return listResult;
	}

	public Long findAllocationByPerfilVendidoAndTabelaPreco(Long codigoPerfilVendido, Long codigoTabelaPreco) {
		EntityManager entityManager = null;
		try {
            entityManager = getJpaTemplate().getEntityManagerFactory().createEntityManager();
            Query query = entityManager.createNamedQuery(ItemTabelaPreco.FIND_ALLOCATION_BY_PERFIL_VENDIDO_AND_TABELA_PRECO);

            query.setParameter("codigoPerfilVendido", codigoPerfilVendido);
            query.setParameter("codigoTabelaPreco",codigoTabelaPreco);

            Long count = ((Number)query.getSingleResult()).longValue();
            return ((Number) query.getSingleResult()).longValue();


        } catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerFactoryUtils.closeEntityManager(entityManager);
		}
			return  null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ItemTabelaPreco> findByPerfilAndMoeda (final PerfilVendido perfilVendido) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoPerfilVendido", perfilVendido.getCodigoPerfilVendido());
		params.put("codigoMoeda", perfilVendido.getMoeda().getCodigoMoeda());

		List<ItemTabelaPreco> results = getJpaTemplate()
		  .findByNamedQueryAndNamedParams(
		    ItemTabelaPreco.FIND_BY_PERFIL_AND_MOEDA,
		    params);

		return results;
	}	

}
