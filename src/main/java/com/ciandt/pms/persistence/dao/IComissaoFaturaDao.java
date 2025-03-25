package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ComissaoFatura;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.ItemFatura;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.Pessoa;

/**
 * 
 * A classe IComissaoFaturaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade ComissaoFatura.
 * 
 * @since 05/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
public interface IComissaoFaturaDao extends IAbstractDao<Long, ComissaoFatura> {

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
	 * @return retorna uma lista de ComissaoAcelerador
	 */
	List<ComissaoFatura> findByFilter(final Date startDate, final Date endDate,
			final String loginDn, final String loginAe, final String status,
			final Msa msa, final Long invoiceCode);

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
	List<ComissaoFatura> findByFilter(final Date startDate, final Date endDate,
			final Cliente c, final ContratoPratica cp, final Pessoa ae,
			final Pessoa dn, final String status);

	/**
	 * Busca pelo item fatura.
	 * 
	 * @param itemFatura
	 *            - entidade ItemFatura
	 * 
	 * @return retorna uma lista de ComissaoFatura
	 */
	List<ComissaoFatura> findByItemFatura(final ItemFatura itemFatura);

	List<ComissaoFatura> findByCodigoItemFatura(final Long codigoItemFatura);

}