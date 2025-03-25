package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.VwPMSProjeto;

/**
 * A classe IVwPMSProjetoDao proporciona as funcionalidades interface de acesso
 * ao banco de dados referente a entidade {@link VwPMSProjeto}.
 * 
 * @since 04/09/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 * 
 */
public interface IVwPMSProjetoDao extends IAbstractDao<Long, VwPMSProjeto> {
	/**
	 * Retorna um {@link VwPMSProjeto} quando a empresa e o codigo do projeto
	 * coincidem
	 * 
	 * @param codigoProjeto
	 * @param empresa
	 * @return VwPMSProjeto a entidade {@link VwPMSProjeto} caso seja
	 *         encontrado, caso contrario retorna null
	 */
	VwPMSProjeto findByProjetoEmpresa(Long codigoProjeto, Empresa empresa);

}