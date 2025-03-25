package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;

/**
 * 
 * A classe IItemFaturaDao proporciona a interface de DAO para a entidade
 * ItemFatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
public interface IItemFaturaDao extends IAbstractDao<Long, ItemFatura> {

	/**
	 * Busca uma lista de ItemFatura associado a uma Fatura passada por
	 * parametro.
	 * 
	 * @param fatura
	 *            do tipo Fatura para realizar a busca
	 * @return lista de ItemFatura que estao associados a Fatura
	 */
	List<ItemFatura> findByFatura(final Fatura fatura);

	/**
	 * Pega o total associado pela fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	BigDecimal getTotalByFatura(final Fatura fatura);

	/**
	 * Busca pelos itens comissões que atendem ao critério do filtro.
	 * 
	 * @param startDate
	 *            - data inicio
	 * @param endDate
	 *            - data fim
	 * @param loginAe
	 *            - login do AE
	 * @param loginloginDn
	 *            - login do DN
	 * 
	 * @return retorna a lista de ItemFatura que atende ao criterio do filtro.
	 */
	List<ItemFatura> findByFilterComissao(final Date startDate,
			final Date endDate, final String loginAe, final Msa msa,
			final Long invoiceCode);

	/**
	 * Busca pelo filtro.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param c
	 *            - cliente
	 * @param cp
	 *            - contrato pratica
	 * @param ae
	 *            - AE da comissao
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	List<ItemFatura> findByFilterComissao(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Pessoa ae);

	/**
	 * Busca pelo filtro.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param c
	 *            - cliente
	 * @param f
	 *            - fatura
	 * @param item
	 *            - item fatura
	 * @param e
	 *            - empresa
	 * @param onlyNotPaid
	 *            - flag de somente não pago
	 * @param filtroData
	 *            - Opçao de qual data pegar
	 * @param msa
	 *            - o {@link Msa}
	 * @param dataPagamentoDe
	 *            - data de pagamento "de" para filtrar por range
	 * @param dataPagamentoAte
	 *            - data de pagamento "ate" para filtrar por range
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 * 
	 */
	List<ItemFatura> findByFilter(final Date startDate, final Date endDate,
			final Fatura f, final ItemFatura item, final Cliente c,
			final Empresa e, final Boolean onlyNotPaid,
			final Boolean filtroData, final Msa msa,
			final Date dataPagamentoDe, final Date dataPagamentoAte);

}