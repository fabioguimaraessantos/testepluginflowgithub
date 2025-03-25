package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.OrcamentoDespesa;
import com.ciandt.pms.persistence.dao.IOrcamentoDespesaDao;

/**
 * 
 * A classe OrcamentoDespesaDao proporciona as funcionalidades de DAO para a
 * entidade OrcamentoDespesa.
 * 
 * @since 24/07/2012
 * @author <a href="mailto:peter@ciandt.com">Peter Pennings</a>
 * 
 */
@Repository
public class OrcamentoDespesaDao extends AbstractDao<Long, OrcamentoDespesa>
		implements IOrcamentoDespesaDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            factory
	 */
	@Autowired
	public OrcamentoDespesaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, OrcamentoDespesa.class);
	}

	/**
	 * Busca todas as entidades do banco.
	 * 
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<OrcamentoDespesa> findAll() {
		List<OrcamentoDespesa> listResult = getJpaTemplate().findByNamedQuery(
				OrcamentoDespesa.FIND_ALL);
		return listResult;
	}

	/**
	 * Obtem um {@link OrcamentoDespesa} de acordo com o nome e tipo de
	 * orcamento informado.
	 * 
	 * @param nome
	 *            nome do {@link OrcamentoDespesa}
	 * 
	 * @param tipoOrcDesp
	 *            a sigla referente ao tipo de {@link OrcamentoDespesa}
	 * 
	 * @return {@link OrcamentoDespesa}
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public OrcamentoDespesa findByNameAndTipoOrcDespesa(final String nome,
			final String tipoOrcDesp) {
		List<OrcamentoDespesa> result = getJpaTemplate().findByNamedQuery(
				OrcamentoDespesa.FIND_BY_NAME_AND_TIPO_ORC_DESP,
				new Object[] { nome, tipoOrcDesp });

		if (result == null || result.isEmpty()) {
			return null;
		}

		return result.get(0);
	}

}
