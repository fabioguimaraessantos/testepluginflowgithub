package com.ciandt.pms.business.service;

import com.ciandt.pms.model.VwPMSProjeto;
import org.springframework.stereotype.Service;

/**
 * A classe IVwPMSProjetoService proporciona as funcionalidades interface de acesso
 * ao banco de dados referente a entidade {@link VwPMSProjeto}.
 * 
 * @since 04/09/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 * 
 */
@Service
public interface IVwPMSProjetoService {

	/**
	 * Retorna um {@link VwPMSProjeto} de acordo com o {@param codigoProjeto}
	 *
	 * @param codigoProjeto
	 * @return VwPMSProjeto a entidade {@link VwPMSProjeto} caso seja
	 *         encontrado, caso contrario retorna null
	 */
	VwPMSProjeto findByCodigoProjeto(Long codigoProjeto);
}