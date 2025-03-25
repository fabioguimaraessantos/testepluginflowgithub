package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.Fatura;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IItemFaturaDao;

/**
 * 
 * A classe ItemFaturaDao proporciona as funcionalidades de acesso ao banco para
 * referentes a entidade ItemFatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class ItemFaturaDao extends AbstractDao<Long, ItemFatura> implements
		IItemFaturaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            do tipo da entidade
	 */
	@Autowired
	public ItemFaturaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ItemFatura.class);
	}

	/**
	 * Busca uma lista de ItemFatura associado a uma Fatura passada por
	 * parametro.
	 * 
	 * @param fatura
	 *            do tipo Fatura para realizar a busca
	 * @return lista de ItemFatura que estao associados a Fatura
	 */
	@SuppressWarnings("unchecked")
	public List<ItemFatura> findByFatura(final Fatura fatura) {

		List<ItemFatura> listResult = getJpaTemplate().findByNamedQuery(
				ItemFatura.FIND_BY_FATURA,
				new Object[] { fatura.getCodigoFatura() });

		return listResult;
	}

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
	@SuppressWarnings("unchecked")
	public List<ItemFatura> findByFilter(final Date startDate,
			final Date endDate, final Fatura f, final ItemFatura item,
			final Cliente c, final Empresa e, final Boolean onlyNotPaid,
			final Boolean filtroData, final Msa msa,
			final Date dataPagamentoDe, final Date dataPagamentoAte) {

		Long codCliente = Long.valueOf(0);
		if (c != null) {
			codCliente = c.getCodigoCliente();
		}
		Long codEmpresa = Long.valueOf(0);
		if (e != null) {
			codEmpresa = e.getCodigoEmpresa();
		}
		Long codigoMsa = Long.valueOf(0);
		if (msa != null) {
			codigoMsa = msa.getCodigoMsa();
		}
		if (filtroData) {
			List<ItemFatura> listResult = getJpaTemplate().findByNamedQuery(
					ItemFatura.FIND_BY_FILTER_DATA_VENC,
					new Object[] { startDate, endDate, codEmpresa,
							item.getNumeroNotaFiscal(), codCliente,
							!onlyNotPaid, f.getIndicadorStatus(), codigoMsa,
							dataPagamentoDe, dataPagamentoAte });
			return listResult;
		} else {
			List<ItemFatura> listResult = getJpaTemplate().findByNamedQuery(
					ItemFatura.FIND_BY_FILTER,
					new Object[] { startDate, endDate, codEmpresa,
							item.getNumeroNotaFiscal(), codCliente,
							!onlyNotPaid, f.getIndicadorStatus(), codigoMsa,
							dataPagamentoDe, dataPagamentoAte });
			return listResult;
		}
	}

	/**
	 * Pega o total associado pela fatura.
	 * 
	 * @param fatura
	 *            - Fatura em questão
	 * 
	 * @return retorna um double com o valor total associado da fatura
	 */
	@SuppressWarnings("all")
	public BigDecimal getTotalByFatura(final Fatura fatura) {

		List total = getJpaTemplate().findByNamedQuery(
				ItemFatura.GET_TOTAL_BY_FATURA,
				new Object[] { fatura.getCodigoFatura() });

		// verifica se a lista esta vazia
		// a segunda coição é necessária pois a lista
		// pode conter com um unico elemento nulo
		if (total.isEmpty() || total.get(0) == null) {
			return BigDecimal.valueOf(0);
		}

		return (BigDecimal) total.get(0);
	}

	/**
	 * Busca pelo filtro.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param loginAe
	 *            - login do AE
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	@SuppressWarnings("unchecked")
	public List<ItemFatura> findByFilterComissao(final Date startDate,
			final Date endDate, final String loginAe, final Msa msa,
			final Long invoiceCode) {

		// Busca codigo do MSA
		Long codigoMsa = Long.valueOf(0L);
		if (msa != null) {
			codigoMsa = msa.getCodigoMsa();
		}

		List<ItemFatura> listResult = getJpaTemplate().findByNamedQuery(
				ItemFatura.FIND_BY_FILTER_COMISSAO,
				new Object[] { startDate, endDate, loginAe, codigoMsa,
						invoiceCode });

		return listResult;
	}

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
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemFatura> findByFilterComissao(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Pessoa ae) {

		Long codigoContratoPratica = Long.valueOf(0);
		if (cp != null) {
			codigoContratoPratica = cp.getCodigoContratoPratica();
		}
		Long codigoCliente = Long.valueOf(0);
		if (c != null) {
			codigoCliente = c.getCodigoCliente();
		}
		String codigoLoginAe = null;
		if (ae != null) {
			codigoLoginAe = ae.getCodigoLogin();
		}

		List<ItemFatura> listResult = getJpaTemplate().findByNamedQuery(
				ItemFatura.FIND_BY_FILTER_COMISSAO_2,
				new Object[] { codigoContratoPratica, codigoCliente, startDate,
						endDate, codigoLoginAe });

		return listResult;
	}

}
