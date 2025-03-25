package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsFinancials;

/**
 * 
 * A interface IVwPmsFinancialsService proporciona as funcionalidades de serviço
 * para a entidade VwPmsFinancials.
 * 
 * @since 23/01/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public interface IVwPmsFinancialsService {

	/**
	 * Busca as entidades filtradas de acordo com os parametros.
	 * 
	 * @param contratoPratica
	 *            - {@link ContratoPratica} para o filtro.
	 * 
	 * @param dataMes
	 *            - data base para busca
	 * 
	 * @return retorna uma lista de VwPmsFinancials
	 */
	List<VwPmsFinancials> findVwPmsFinancialsByClobAndDataMes(
			final ContratoPratica contratoPratica, final Date dataMes);

	/**
	 * Busca as entidades filtradas de acordo com os parametros.
	 * 
	 * @param contratoPratica
	 *            - {@link ContratoPratica} para o filtro.
	 * 
	 * @param dataMesList
	 *            - lista de data base para a busca
	 * 
	 * @return retorna uma lista de VwPmsFinancials
	 */
	List<VwPmsFinancials> findVwPmsFinancialsByClobAndDataMesList(
			final ContratoPratica contratoPratica, final List<Date> dataMesList);

}