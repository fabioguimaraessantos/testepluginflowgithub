package com.ciandt.pms.business.service;

import java.util.List;

import com.ciandt.pms.exception.MoreThanOneActiveEntityException;
import com.ciandt.pms.model.Convergencia;
import com.ciandt.pms.model.GrupoCusto;
import com.ciandt.pms.model.vo.FormFilter;

/**
 * 
 * A classe IConvergenciaService proporciona a interface para acesso ao servicos
 * relacionados a entidade {@link Convergencia}.
 * 
 * @since Aug 15, 2014
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
public interface IConvergenciaService {

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param codigoConvergencia
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	Convergencia findById(final Long codigoConvergencia);

	/**
	 * Busca todos as {@link Convergencia}.
	 * 
	 * @return lista com todos os {@link Convergencia}
	 */
	List<Convergencia> findAll();

	/**
	 * Cria uma entidade {@link Convergencia}.
	 * 
	 * @param convergencia
	 *            {@link Convergencia}
	 */
	void create(final Convergencia convergencia);

	/**
	 * Atualiza uma entidade {@link Convergencia}.
	 * 
	 * @param convergencia
	 *            {@link Convergencia}
	 */
	void update(final Convergencia convergencia);

	/**
	 * Deleta uma entidade {@link Convergencia}.
	 * 
	 * @param convergencia
	 *            {@link Convergencia}
	 */
	void delete(final Convergencia convergencia);

	/**
	 * Lista todos os projetos inativos utilizando como filtro os dados do
	 * formulario
	 * 
	 * @param filter
	 * @return list of {@link Convergencia}
	 */
	List<Convergencia> findInativeProjectByFormFilter(FormFilter filter);

	/**
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	List<Convergencia> findActiveProjectByFormFilter(FormFilter filter);

	/**
	 * @see com.ciandt.pms.business.service.IConvergenciaService#findInativeProjectByFormFilter(com.ciandt.pms.model.vo.FormFilter)
	 */
	List<Convergencia> findAllProjectByFormFilter(FormFilter filter);

	/**
	 * Busca entidade pelo id contrato pratica
	 * 
	 * @param idContratoPratica
	 */
	Convergencia findByContratoPraticaId(Long idContratoPratica);

	/**
	 * Verifica se existem projetos para serem ativados
	 * 
	 * @param grupoCusto
	 * @return true se existem projetos para serem ativados, false caso
	 *         contrario
	 */
	boolean hasProjectToActivate(GrupoCusto grupoCusto);

	/**
	 * Encontra grupo de custo
	 * 
	 * @param codigoGrupoCusto
	 * @return grupo de custo se existir, caso contrario retorna um grupo de
	 *         custo vazio
	 */
	Convergencia findByCostGroupId(Long codigoGrupoCusto);

	/**
	 * Encontra CLobs associados com o o grupo de custo
	 * 
	 * @param codigoGrupoCusto
	 * @return lista de CLobs associados
	 */
	List<Convergencia> findByCLCostGroupId(Long codigoGrupoCusto);

	/**
	 * Encontra projetos associados
	 * 
	 * @param codigoProjetoMega
	 * @return
	 */
	List<Convergencia> findConvergenciaByProjeto(Long codigoProjetoMega);

}