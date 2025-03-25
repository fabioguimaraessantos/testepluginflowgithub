package com.ciandt.pms.business.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IVwPmsFinancialsService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsFinancials;
import com.ciandt.pms.persistence.dao.IVwPmsFinancialsDao;

/**
 * 
 * A classe IVwPmsFinancialsService proporciona as funcionalidades de serviço
 * para a entidade VwPmsFinancials.
 * 
 * @since 23/01/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Service
public class VwPmsFinancialsService implements IVwPmsFinancialsService {

	/** Instancia do dao . */
	@Autowired
	private IVwPmsFinancialsDao dao;

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
	@Override
	public List<VwPmsFinancials> findVwPmsFinancialsByClobAndDataMes(
			final ContratoPratica contratoPratica, final Date dataMes) {
		return dao.findByClobAndDataMes(contratoPratica, dataMes);
	}

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
	@Override
	public List<VwPmsFinancials> findVwPmsFinancialsByClobAndDataMesList(
			final ContratoPratica contratoPratica, final List<Date> dataMesList) {
		List<VwPmsFinancials> result = new ArrayList<VwPmsFinancials>();
		for (Date date : dataMesList) {
			result.addAll(this.findVwPmsFinancialsByClobAndDataMes(
					contratoPratica, date));
		}
		return result;
	}

}