package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.enums.SgTipoServico;
import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.TipoServico;

/**
 * 
 * A classe IDealService proporciona a interface de acesso a camada de serviço
 * referente a entidade Deal.
 * 
 * @since 15/09/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Service
public interface IDealFiscalService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createDealFiscal(final DealFiscal entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void updateDealFiscal(final DealFiscal entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que será apagada.
	 * 
	 * @return retorna true se removido com sucesso, caso contrario false.
	 */
	@Transactional
	Boolean removeDealFiscal(final DealFiscal entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param entityId
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	DealFiscal findDealFiscalById(final Long entityId);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findDealFiscalAll();

	/**
	 * Retorna todas as entidades com o estado igual a ativo.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findDealFiscalAllActive();

	/**
	 * Retorna o maior numero de sequencia do deal referente a um Msa.
	 * 
	 * @param msa
	 *            - Intancia de Msa
	 * 
	 * @return retorna um long referente ao numero sequencia.
	 */
	Long findDealFiscalMaxByMsa(final Msa msa);

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<DealFiscal> findDealFiscalByContratoPratica(final ContratoPratica cp);

	/**
	 * Busca uma lista de entidades relacionadas com o ContratoPratica.
	 * 
	 * @param cp
	 *            entidade do tipo ContratoPratica.
	 * 
	 * @return lista de entidades que atendem ao criterio de filtro.
	 */
	List<DealFiscal> findDealFiscalByContratoPraticaAndActive(
			final ContratoPratica cp);

	/**
	 * Retorna todas as entidades relacionadas com o Cliente passado por
	 * parametro.
	 * 
	 * @param cliente
	 *            - Cliente que se deseja buscar os DealFiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findDealFiscalByCliente(final Cliente cliente);

	/**
	 * Retorna todas as entidades ativas relacionadas com o Msa passado por
	 * parametro.
	 * 
	 * @param msa
	 *            - Msa que se deseja buscar os DealFiscal
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<DealFiscal> findDealFiscalActiveByMsa(final Msa msa);

	/**
	 * Busca por contrato pratica, moeda e ativos.
	 * 
	 * @param cp
	 *            - contratoPratica
	 * @param moeda
	 *            - moeda
	 * @return lista de Deal Fiscals
	 */
	List<DealFiscal> findActiveDealFiscalByClobAndCurrency(
			final ContratoPratica cp, final Moeda moeda);

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
	List<DealFiscal> findDealFiscalByMsaAndNotLogicalDelete(final Msa msa);

	/**
	 * Busca por msa.
	 * 
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	List<DealFiscal> findDealFiscalByMsa(final Msa msa);

	/**
	 * Busca por msa ativo.
	 *
	 * @param msa
	 *            msa
	 * @return listReuslt
	 */
	List<DealFiscal> findDealFiscalByMsaActive(final Msa msa);

	/**
	 * Busca por msa, ativo e nao deletado logicamente.
	 * 
	 * @param msa
	 *            - the {@link Msa}
	 * 
	 * @return listresult
	 */
	List<DealFiscal> findDealFiscalByMsaAndActiveAndNotLogicalDelete(
			final Msa msa);
	
	/**
	 * Busca pelo filtro definido na tela de Fiscal Balance > Search. Pode ser
	 * pelo código do MSA e/ou código do Cliente.
	 * 
	 * @param msa
	 *            Objeto {@link Msa}
	 * @return Lista de {@link DealFiscal}
	 */
	List<DealFiscal> findByFilter(final Msa msa);

	/**
	 * Busca {@link DealFiscal} ativos e não deletados logicamente por {@link ContratoPratica}, {@link Moeda}, {@link SgTipoServico}.
	 * 
	 * @param contratoPratica
	 *            {@link ContratoPratica} associado ao  {@link DealFiscal}
	 * @param moeda
	 *            {@link Moeda} associado ao  {@link DealFiscal}
	 * @param siglasTipoServico
	 * 			{@link SgTipoServico} associado ao  {@link DealFiscal}
	 *
	 * @return Lista de {@link DealFiscal}
	 */
	List<DealFiscal> findActiveAndNotLogicDeletedByClobAndCurrencyAndTipoServico(
			final ContratoPratica contratoPratica, final Moeda moeda, final List<String> siglasTipoServico);

	/**
	 * Busca {@link DealFiscal} por {@code contratoPratica} e {@code tipoServico} que estão ativos
	 *
	 * @param contratoPratica
	 * @param tipoServico
	 * @return Lista de {@link DealFiscal}
	 */
	List<DealFiscal> findByContratoPraticaAndTipoServicoAndActive(
			ContratoPratica contratoPratica, TipoServico tipoServico);

	List<DealFiscal> findFiscalDealWithActiveAllocationsInAllocationMapByFiscalDealAndClosingMapDate(
			final Long codigoDealFiscal, final Date closingMapDate, final Long codigoEmpresaERP);
}