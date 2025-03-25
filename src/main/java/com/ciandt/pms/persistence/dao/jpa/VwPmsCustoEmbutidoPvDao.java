package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.VwPmsCustoEmbutidoPv;
import com.ciandt.pms.persistence.dao.IVwPmsCustoEmbutidoPvDao;

/**
 * 
 * @author peter
 * 
 */
@Repository
public class VwPmsCustoEmbutidoPvDao extends
		AbstractDao<Long, VwPmsCustoEmbutidoPv> implements
		IVwPmsCustoEmbutidoPvDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public VwPmsCustoEmbutidoPvDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwPmsCustoEmbutidoPv.class);
	}

	/**
	 * Busca por clob, moeda e dataMes.
	 * 
	 * @param clob
	 * @param moeda
	 * @param dataMes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<VwPmsCustoEmbutidoPv> findByClobAndMes(ContratoPratica clob,
			Date dataMes) {
		List<VwPmsCustoEmbutidoPv> listResult = getJpaTemplate()
				.findByNamedQuery(
						VwPmsCustoEmbutidoPv.FIND_BY_CLOB_DATAMES,
						new Object[] { clob.getCodigoContratoPratica(), dataMes });

		return listResult;
	}
}
