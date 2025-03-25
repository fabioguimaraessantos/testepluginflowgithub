/**
 * 
 */
package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Processo;
import com.ciandt.pms.persistence.dao.IProcessoDao;

/**
 * A classe ProcessoDao proporciona as funcionalidades da camada de persistencia
 * referente a entidade Processo.
 * 
 * @since 10/10/2013
 * @author <a href="mailto:etanigawa@ciandt.com">Erika Tanigawa</a>
 */
@Repository
public class ProcessoDao extends AbstractDao<Long, Processo> implements
		IProcessoDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica do entity manager.
	 */
	@Autowired
	public ProcessoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, Processo.class);
	}

	/**
	 * Busca os processos por indicador
	 * 
	 * @param indicador
	 *            "A" para Ativo, "I" para Inativo
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Processo> findAllByIndicadorAtivo(final String indicador) {

		List<Processo> processos = getJpaTemplate().findByNamedQuery(
				Processo.FIND_ALL_ATIVO, new Object[] { indicador });

		return processos;

	}

}
