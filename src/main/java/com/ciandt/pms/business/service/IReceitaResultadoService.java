package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ReceitaResultado;

/**
 * 
 * A classe IReceitaResultadoService proporciona a interface de acesso para a
 * camada de serviço referente a entidade {@link ReceitaResultado}.
 * 
 * @since 12/01/2012
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public interface IReceitaResultadoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createReceitaResultado(final ReceitaResultado entity);

	/**
	 * Atualiza a entidade.
	 * 
	 * @param entity
	 *            entidade a ser atualizada.
	 * 
	 */
	@Transactional
	void updateReceitaResultado(final ReceitaResultado entity);

	/**
	 * Remove a entidade.
	 * 
	 * @param entity
	 *            entidade a ser removida.
	 */
	@Transactional
	void removeReceitaResultado(final ReceitaResultado entity);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ReceitaResultado> findReceitaResultadoAll();

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ReceitaResultado findReceitaResultadoById(final Long id);

	/**
	 * Retorna uma entidade.
	 * 
	 * @param entity
	 *            filtro {@link ReceitaResultado}
	 * 
	 * @return retorna uma entidade referente ao filtro passado.
	 */
	ReceitaResultado findReceitaResultadoByContratoAndMoedaAndDate(
			final ReceitaResultado entity);

	/**
	 * Cria a foto da Receita a partir da lista recebida.
	 * 
	 * @param receitaResultadoList
	 *            recebe uma lista a ser persistida
	 */
	@Transactional
	void snapshotReceitaResultado(
			final List<ReceitaResultado> receitaResultadoList);

	/**
	 * Mantem o registro da {@link ReceitaResultado} no banco porem, apaga o
	 * conteudo inserido pelo usuario referente a justificativa da receita. O
	 * registro volta a ficar como vindo da foto. Este caso e util quando uma
	 * receita é excluida e suas justificativas devem ser apagadas (mantendo a
	 * foto).
	 * 
	 * @param receitaResultado
	 *            registro a ser manipulado
	 */
	@Transactional
	void removeShortTermRevenue(final ReceitaResultado receitaResultado);

}