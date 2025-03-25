package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;

/**
 * 
 * A classe IItemFaturaService proporciona a interface de servico para a
 * entidade ItemFatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public interface IItemFaturaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createItemFatura(final ItemFatura entity);

	/**
	 * Atualiza a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será atualizada.
	 * 
	 */
	@Transactional
	void updateItemFatura(final ItemFatura entity);

	/**
	 * Remove a entidade passado por parametro.
	 * 
	 * @param entity
	 *            que será removida.
	 * @param log
	 *            true deve logar e false nao.
	 * 
	 */
	@Transactional
	void removeItemFatura(final ItemFatura entity, final Boolean log);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ItemFatura findItemFaturaById(final Long id);

	/**
	 * Busca uma lista de ItemFatura associado a uma Fatura passada por
	 * parametro.
	 * 
	 * @param fatura
	 *            do tipo Fatura para realizar a busca
	 * @return lista de ItemFatura que estao associados a Fatura
	 */
	List<ItemFatura> findItemFaturaByFatura(final Fatura fatura);

	/**
	 * Pega o total associado pela fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	BigDecimal getItemFaturaTotalByFatura(final Fatura fatura);

	/**
	 * Busca pelos items da fatura que atendem o criterio do filtro.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param f
	 *            - fatura
	 * @param item
	 *            - item fatura
	 * @param c
	 *            - cliente
	 * @param e
	 *            - empresa
	 * @param onlyNotPaid
	 *            - flag que indica se somente pagas
	 * @param msa
	 *            - the {@link Msa}
	 * @param dataPagamentoDe
	 *            - data de pagamento "de" para filtrar por range
	 * @param dataPagamentoAte
	 *            - data de pagamento "ate" para filtrar por range
	 * 
	 * @return retorna a lista com os resultados.
	 */
	List<ItemFatura> findItemFaturaByFilter(final Date startDate,
			final Date endDate, final Fatura f, final ItemFatura item,
			final Cliente c, final Empresa e, final Boolean onlyNotPaid,
			final Boolean filtroData, final Msa msa,
			final Date dataPagamentoDe, final Date dataPagamentoAte);

}