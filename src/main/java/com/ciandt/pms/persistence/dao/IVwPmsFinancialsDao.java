package com.ciandt.pms.persistence.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsFinancials;

/**
 * 
 * A interface IVwPmsVwPmsFinancialsDao proporciona as funcionalidades de acesso
 * ao banco para a entidade VwPmsFinancials.
 * 
 * @since 23/01/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public interface IVwPmsFinancialsDao extends
		IAbstractDao<Long, VwPmsFinancials> {

	/**
	 * Busca as entidades filtradas de acordo com os parametros.
	 * 
	 * @param contratoPratica
	 *            - contrato pratica a ser consultado
	 * 
	 * @param dataMes
	 *            - data base para busca
	 * 
	 * @return retorna uma lista de VwPmsFinancials
	 */
	List<VwPmsFinancials> findByClobAndDataMes(
			final ContratoPratica contratoPratica, final Date dataMes);

}