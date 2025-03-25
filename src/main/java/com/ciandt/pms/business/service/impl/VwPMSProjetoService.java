package com.ciandt.pms.business.service.impl;

import com.ciandt.pms.business.service.IVwPMSProjetoService;
import com.ciandt.pms.model.VwPMSProjeto;
import com.ciandt.pms.persistence.dao.IVwPMSProjetoDao;
import com.ciandt.pms.persistence.dao.jpa.VwPMSProjetoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A classe VwPMSProjetoService proporciona as funcionalidades de servico para
 * a entidade {@link VwPMSProjeto}.
 * 
 * @since 04/09/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 * 
 */
@Service
public class VwPMSProjetoService implements IVwPMSProjetoService {

	/** Instancia de {@link VwPMSProjetoDao}. */
	@Autowired
	private IVwPMSProjetoDao dao;

	/**
	 * Retorna um {@link VwPMSProjeto} de acordo com o {@param codigoProjeto}
	 *
	 * @param codigoProjeto
	 * @return VwPMSProjeto a entidade {@link VwPMSProjeto} caso seja
	 *         encontrado, caso contrario retorna null
	 */
	@Override
	public VwPMSProjeto findByCodigoProjeto(Long codigoProjeto) {
		return dao.findById(codigoProjeto);
	}
}