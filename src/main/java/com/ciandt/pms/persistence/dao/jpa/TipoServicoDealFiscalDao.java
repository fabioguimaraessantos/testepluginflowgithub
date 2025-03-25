package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServicoDealFiscal;
import com.ciandt.pms.persistence.dao.ITipoServicoDealFiscalDao;

/**
 * 
 * @author peter
 * 
 */
@Repository
public class TipoServicoDealFiscalDao extends
		AbstractDao<Long, TipoServicoDealFiscal> implements
		ITipoServicoDealFiscalDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            da entidade
	 */
	public TipoServicoDealFiscalDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, TipoServicoDealFiscal.class);
	}

	/**
	 * Busca por dealfiscal.
	 * 
	 * @param dealFiscal
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TipoServicoDealFiscal> findByDealFiscal(final DealFiscal dealFiscal) {
		List<TipoServicoDealFiscal> listResult = getJpaTemplate()
				.findByNamedQuery(TipoServicoDealFiscal.FIND_BY_DEAL_FISCAL,
						new Object[] { dealFiscal.getCodigoDealFiscal() });

		
		return listResult;
	}

}
