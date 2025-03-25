package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsFinancials;
import com.ciandt.pms.persistence.dao.IVwPmsFinancialsDao;

/**
 * 
 * A classe IVwPmsFinancialsDao proporciona as funcionalidades de acesso ao
 * banco para a entidade VwPmsFinancials.
 * 
 * @since 23/01/2013
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Alexandre Vidolin de
 *         Lima</a>
 * 
 */
@Repository
public class VwPmsFinancialsDao extends AbstractDao<Long, VwPmsFinancials>
		implements IVwPmsFinancialsDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - fabrica da entidade.
	 */
	@Autowired
	public VwPmsFinancialsDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwPmsFinancials.class);
	}

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
	@Override
	@SuppressWarnings("unchecked")
	public List<VwPmsFinancials> findByClobAndDataMes(
			final ContratoPratica contratoPratica, final Date dataMes) {
		List<VwPmsFinancials> result = getJpaTemplate().findByNamedQuery(
				VwPmsFinancials.FIND_BY_CLOB_AND_DATAMES,
				new Object[] { contratoPratica.getCodigoContratoPratica(),
						dataMes });
		return result;
	}

}