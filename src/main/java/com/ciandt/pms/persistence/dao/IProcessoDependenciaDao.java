package com.ciandt.pms.persistence.dao;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ProcessoDependencia;

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
public interface IProcessoDependenciaDao extends
		IAbstractDao<Long, ProcessoDependencia> {

}
