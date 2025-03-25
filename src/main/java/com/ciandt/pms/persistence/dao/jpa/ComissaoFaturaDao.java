package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;
import com.ciandt.pms.persistence.dao.IComissaoFaturaDao;

/**
 * 
 * A classe ComissaoFaturaDao proporciona as funcionalidades de persistencia
 * referente a entidade ComissaoFatura.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class ComissaoFaturaDao extends AbstractDao<Long, ComissaoFatura>
		implements IComissaoFaturaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica do entity manager.
	 */
	@Autowired
	public ComissaoFaturaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, ComissaoFatura.class);
	}

	/**
	 * Busca pelo filtro.
	 * 
	 * @param startDate
	 *            - data inicial
	 * @param endDate
	 *            - data final
	 * @param loginAe
	 *            - AE da comissao
	 * @param loginDn
	 *            - DN aprovador da comissão
	 * @param status
	 *            - status da comissão.
	 * 
	 * @return retorna uma lista de ComissaoFatura
	 */
	@SuppressWarnings("unchecked")
	public List<ComissaoFatura> findByFilter(final Date startDate,
			final Date endDate, final String loginDn, final String loginAe,
			final String status, final Msa msa, final Long invoiceCode) {

		// Busca codigo do MSA
		Long codigoMsa = Long.valueOf(0L);
		if (msa != null) {
			codigoMsa = msa.getCodigoMsa();
		}

		List<ComissaoFatura> listResult = getJpaTemplate().findByNamedQuery(
				ComissaoFatura.FIND_BY_FILTER,
				new Object[] { startDate, endDate, loginAe, loginDn, status,
						codigoMsa, invoiceCode });

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
	 * @param dn
	 *            - DN aprovador da comissão
	 * @param status
	 *            - status da comissão
	 * 
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	@SuppressWarnings("unchecked")
	public List<ComissaoFatura> findByFilter(final Date startDate,
			final Date endDate, final Cliente c, final ContratoPratica cp,
			final Pessoa ae, final Pessoa dn, final String status) {


		Long codigoCliente = Long.valueOf(0);
		if (c != null) {
			codigoCliente = c.getCodigoCliente();
		}
		Long codigoAe = Long.valueOf(0);
		if (ae != null) {
			codigoAe = ae.getCodigoPessoa();
		}
		Long codigoDn = Long.valueOf(0);
		if (dn != null) {
			codigoDn = dn.getCodigoPessoa();
		}

		List<ComissaoFatura> listResult = getJpaTemplate().findByNamedQuery(
				ComissaoFatura.FIND_BY_FILTER_2,
				new Object[] { codigoCliente, startDate, endDate, codigoAe,
						codigoDn, status });
		return listResult;
	}

	/**
	 * Busca pelo item fatura.
	 * 
	 * @param itemFatura
	 *            - entidade ItemFatura
	 * 
	 * @return retorna uma lista de ComissaoFatura
	 */
	@SuppressWarnings("unchecked")
	public List<ComissaoFatura> findByItemFatura(final ItemFatura itemFatura) {

		List<ComissaoFatura> listResult = getJpaTemplate().findByNamedQuery(
				ComissaoFatura.FIND_BY_ITEM_FATURA,
				new Object[] { itemFatura.getCodigoItemFatura() });

		return listResult;
	}

	@SuppressWarnings("unchecked")
	public List<ComissaoFatura> findByCodigoItemFatura(final Long codigoItemFatura) {

		List<ComissaoFatura> listResult = getJpaTemplate().findByNamedQuery(
				ComissaoFatura.FIND_BY_ITEM_FATURA,
				new Object[] { codigoItemFatura });

		return listResult;
	}
}
