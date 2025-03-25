package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Parametro;

/**
 * A interface IParametroService proporciona a interface de acesso a
 * camada de serviço referente a entidade {@link Parametro}.
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since 03/02/2014
 */
@Service
public interface IParametroService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createParametro(final Parametro entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Transactional
	void updateParametro(final Parametro entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Transactional
	void removeParametro(final Parametro entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	Parametro findParametroById(final Long id);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<Parametro> findParametroAll();

	/**
	 * Busca {@link Parametro} pelo atributo {@code nomeParametro}.
	 *
	 * @param nomeParametro
	 * @return {@link Parametro}
	 */
	Parametro findParametroByNomeParametro(final String nomeParametro);
}
