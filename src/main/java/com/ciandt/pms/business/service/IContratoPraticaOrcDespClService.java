package com.ciandt.pms.business.service;

import java.util.List;

import javax.persistence.Transient;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ContratoPraticaOrcDespCl;
import com.ciandt.pms.model.OrcDespesaCl;

/**
 * 
 * A classe ContratoPraticaOrcDesp proporciona a interface de acesso a camada de
 * serviço referente a entidade ContratoPraticaOrcDesp.
 * 
 * @since 12/04/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Service
public interface IContratoPraticaOrcDespClService {

	/**
	 * Busca uma entidade ContratoPraticaOrcDespCl.
	 * 
	 * @param codigoContratoPratica
	 *            - o codigo do {@link ContratoPratica}
	 * @param codigoOrcDespesaCl
	 *            - o codigo do {@link OrcDespesaCl}
	 * 
	 * @return {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	ContratoPraticaOrcDespCl findById(final Long codigoContratoPratica,
			final Long codigoOrcDespesaCl);

	/**
	 * Persiste um {@link ContratoPraticaOrcDespCl}.
	 * 
	 * @param entity
	 *            {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Transient
	public void createContratoPraticaOrcDespCl(
			final ContratoPraticaOrcDespCl entity);

	/**
	 * Atualiza um {@link ContratoPraticaOrcDespCl}.
	 * 
	 * @param entity
	 *            {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Transient
	public void updateContratoPraticaOrcDespCl(
			final ContratoPraticaOrcDespCl entity);

	/**
	 * Remove um {@link ContratoPraticaOrcDespCl}.
	 * 
	 * @param entity
	 *            {@link ContratoPraticaOrcDespCl}
	 * 
	 */
	@Transient
	public void removeContratoPraticaOrcDespCl(
			final ContratoPraticaOrcDespCl entity);
	
	/**
	 * Busca por contratoPraticaOrcDespCl.
	 * @param contratoPraticaOrcDespCl contratoPraticaOrcDespCl
	 * @return
	 */
	public List<ContratoPraticaOrcDespCl> findByOrcDespesaCl(final OrcDespesaCl orcDespesaCl);

}
