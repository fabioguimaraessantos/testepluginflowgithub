package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.enums.SgTipoServico;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;

/**
 * 
 * A classe IDealFiscalDao proporciona a interface para o acesso ao banco da
 * entidade Deal.
 * 
 * @since 15/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IDealFiscalDao extends IAbstractDao<Long, DealFiscal> {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findAll();

	/**
	 * Retorna todas as entidades com o estado igual a ativo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findAllActive();

	/**
	 * Retorna o maior numero de sequencia do deal referente a um Msa.
	 * 
	 * @param msa
	 *            - Intancia de Msa
	 * 
	 * @return retorna um long referente ao numero sequencia.
	 */
	Long findMaxByMsa(final Msa msa);

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<DealFiscal> findByContratoPratica(final ContratoPratica cp);

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<DealFiscal> findByContratoPraticaAndActive(final ContratoPratica cp);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os Dealfiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findByCliente(final Cliente cliente);

	/**
	 * Retorna todas as entidades ativas relacionadas com o Msa passado por
	 * parametro.
	 * 
	 * @param msa
	 *            - Msa que se deseja buscar os Dealfiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findActiveByMsa(final Msa msa);

	/**
	 * Busca por contrato pratica, moeda e ativos.
	 * 
	 * @param cp
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return lista de Deal Fiscals
	 */
	List<DealFiscal> findActiveByClobAndCurrency(final ContratoPratica cp,
			final Moeda moeda);

	/**
	 * Busca por clob, moeda, ativos e nao deletados logicamente.
	 * 
	 * @param cp
	 *            clob
	 * @param moeda
	 *            moeda
	 * @return listReuslt
	 */
	List<DealFiscal> findActiveByClobAndCurrencyAndNotLogicDeleted(
			final ContratoPratica cp, final Moeda moeda);

	/**
	 * Busca msa e nao deletados logicamente. clob
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	List<DealFiscal> findByMsaAndNotLogicalDelete(final Msa msa);

	/**
	 * Busca por msa.
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	List<DealFiscal> findByMsa(final Msa msa);

	/**
	 * Busca por msa active.
	 *
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	List<DealFiscal> findByMsaActive(final Msa msa);

	/**
	 * Busca por msa, ativo e nao deletado logicamente.
	 * 
	 * @param msa
	 *            - the {@link Msa}
	 * 
	 * @return listresult
	 */
	List<DealFiscal> findByMsaAndActiveAndNotLogicalDelete(final Msa msa);
	
	/**
	 * Busca pelo filtro definido na tela de Fiscal Balance > Search. Pode ser
	 * pelo codigo do MSA e/ou codigo do Cliente.
	 * 
	 * @param msa
	 *            Objeto {@link Msa}
	 * @return Lista de {@link DealFiscal}
	 */
	List<DealFiscal> findByFilter(final Msa msa);

	/**
	 * Busca {@link DealFiscal} ativos e nao deletados logicamente por {@link ContratoPratica}, {@link Moeda},
	 * {@link SgTipoServico}.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} associado ao  {@link DealFiscal}
	 * @param moeda
	 *            {@link Moeda} associado ao  {@link DealFiscal}
	 * @param siglasTipoServiao
	 * 			{@link SgTipoServico} associado ao  {@link DealFiscal}
	 *
	 * @return Lista de {@link DealFiscal}
	 */
	List<DealFiscal> findActiveAndNotLogicDeletedByClobAndCurrencyAndTipoServico(
			final ContratoPratica contratoPratica, final Moeda moeda, final List<String> siglasTipoServico);


	List<DealFiscal> findFiscalDealWithActiveAllocationsInAllocationMapByFiscalDealAndClosingMapDate(
			final Long codigoDealFiscal, final Date closingMapDate, final Long codigoEmpresaERP);
}