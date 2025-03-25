package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ProcessoDependencia;
import com.ciandt.pms.persistence.dao.IProcessoDependenciaDao;

/**
 * 
 * A classe IProcessoDependenciaDao proporciona a interface de acesso a camada
 * de persistencia referente a entidade {@link ProcessoDependencia}.
 * 
 * @since 17/10/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class ProcessoDependenciaDao extends
		AbstractDao<Long, ProcessoDependencia> implements
		IProcessoDependenciaDao {

	/**
	 * Contrutor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	@Autowired
	public ProcessoDependenciaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ProcessoDependencia.class);
	}

}
