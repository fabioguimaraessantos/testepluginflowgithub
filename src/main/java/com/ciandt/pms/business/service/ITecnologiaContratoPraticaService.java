package com.ciandt.pms.business.service;

import java.util.List;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.TecnologiaContratoPratica;

/**
 * 
 * A interface ITecnologiaContratoPraticaaService proporciona a interface de
 * acesso para o servicos referentes a entidade
 * {@link TecnologiaContratoPratica}.
 * 
 * @since Sep 23, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public interface ITecnologiaContratoPraticaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 * 
	 * @return true se criado com sucesso, caso contrario retorna false
	 */
	TecnologiaContratoPratica findTecnologiaContratoPraticaById(
			final Long entityId);

	/**
	 * Executa um update na entidade passada por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 * @return true se update com sucesso, caso contrario retorna false
	 */
	boolean updateTecnologiaContratoPratica(
			final TecnologiaContratoPratica entity);

	/**
	 * Create a TecnologiaContratoPratica
	 * 
	 * @param TecnologiaContratoPratica
	 */
	void createTecnologiaContratoPratica(
			final TecnologiaContratoPratica TecnologiaContratoPratica);

	/**
	 * Return all TecnologiaContratoPratica associated with the given
	 * ContratoPratica.
	 * 
	 * @return TecnologiaContratoPratica List.
	 */
	List<TecnologiaContratoPratica> findAllByClob(
			final ContratoPratica contratoPratica);

	/**
	 * Remove uma determinada tecnologia.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} base
	 */
	void remove(TecnologiaContratoPratica tecnologia);

	/**
	 * Remove todos as tecnologias vinculadas ao CLOB informado.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} base
	 */
	void removeAllByClob(ContratoPratica contratoPratica);

}