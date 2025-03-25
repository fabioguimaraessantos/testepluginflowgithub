/**
 * IContratoPraticaDao - Classe de interface DAO para a entidade ContratoPratica
 */
package com.ciandt.pms.persistence.dao;

import com.ciandt.pms.model.*;
import com.ciandt.pms.model.vo.MapaProspectComAlocacaoResultsetVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * A classe IContratoPraticaDao proporciona a interface de acesso para a camada
 * DAO da entidade ContratoPratica.
 * 
 * @since 13/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IContratoPraticaDao extends
		IAbstractDao<Long, ContratoPratica> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findAll();

	List<ContratoPratica> findAllForComboBox();

	List<ContratoPratica> findAllCompleteForCombobox();

	List<ContratoPratica> findAllContratoPraticaActive();

	List<ContratoPratica> findByAproverRescinded();

	/**
	 * Busca o ContratoPratica por Contrato e Pratica.
	 * 
	 * @param idPratica
	 *            id da entidade pratica
	 * @param idMsa
	 *            id do entidade Msa
	 * 
	 * @return o ContratoPratica caso exista
	 * 
	 */
	ContratoPratica findByPraticaAndMsa(final Long idPratica, final Long idMsa);

	/**
	 * Busca o ContratoPratica por Contrato e Pratica.
	 *
	 * @param idPratica id da entidade pratica
	 * @return o ContratoPratica caso exista
	 */
	List<ContratoPratica> findByPratica(final Long idPratica);

	/**
	 * Busca uma lista de entidades pelo filtro.
	 * 
	 * @param filter
	 *            entidade populada com os valores do filtro.
	 * @param cli
	 *            entidade Cliente
	 * @param natureza
	 *            entidade Natureza
	 * @param cl
	 *            entidade CentroLucro
	 * @param cl
	 *            entidade GrupoCusto
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<ContratoPratica> findByFilterFetch(
			final ContratoPratica filter,
			final Cliente cli,
			final NaturezaCentroLucro natureza,
			final CentroLucro cl,
			final GrupoCusto grupoCusto,
			final List<String> indicadorWorkAtRiskList);

	/**
	 * Busca por todos os ContratoPraticas completos.
	 * 
	 * @return uma lista com todos os ContratoPraticas.
	 */
	List<ContratoPratica> findAllComplete();

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os ContratoPratica
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findByCliente(final Cliente cliente);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cl
	 *            - CentroLucro que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findByCentroLucro(final CentroLucro cl);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param natureza
	 *            - NaturezaCentroLucro que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findByNatureza(final NaturezaCentroLucro natureza);

	/**
	 * Retorna todas as entidades relacionadas com o Contrato passado por
	 * parametro.
	 * 
	 * @param msa
	 *            - Msa que se deseja buscar os ContratoPraticas
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findByMsa(final Msa msa);

	/**
	 * Retorna todas as entidades sem Fiscal Deal associado.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findSemFiscalDeal();

	/**
	 * Busca uma lista de entidades que possuem MapaAlocacao.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<ContratoPratica> findAllWithMapaAlocacao();

	/**
	 * Retorna todos os ContratoPratica relacionados com o chargeback e dentro
	 * de um periodo.
	 * 
	 * @param tiRecurso
	 *            - TiRecurso que se deseja buscar os ContratoPraticas
	 * 
	 * @param startDate
	 *            - Data inicio do periodo.
	 * 
	 * @param endDate
	 *            - Data fim do periodo
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ContratoPratica> findByTiRecursoAndPeriod(final TiRecurso tiRecurso,
			final Date startDate, final Date endDate);

	/**
	 * Busca um contrato pratica pelo nome.
	 * 
	 * @param nomeContratoPratica
	 *            - nome do ContratoPratica
	 * @return {@link ContratoPratica}
	 */
	ContratoPratica findByName(final String nomeContratoPratica);

	/**
	 * Busca todos os clobs comepletos e ativos.
	 * @return listResult
	 */
	List<ContratoPratica> findAllCompleteAndActive();

	List<ContratoPraticaAud> findHistoryByCodigo(Long codigoContratoPratica);

	List<String> findManagerOfContratoPraticaWithoutDealFiscal();

	List<Map<String,String>> findContratoPraticaWithoutDealFiscal(String loginManager);

	/**
	 * Busca todos os Mapas de Aloca��o com o status "Prospect" que possuem alocacao
	 * 	de algum recurso no m�s corrente.
	 *
	 * @return {@link List} of {@link MapaProspectComAlocacaoResultsetVO}
	 */
	List<MapaProspectComAlocacaoResultsetVO> findProspectMapWithAllocation();

	String findActualBusinessManagerContratoPratica(Long codigoContratoPratica);
}