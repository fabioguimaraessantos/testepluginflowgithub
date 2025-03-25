package com.ciandt.pms.persistence.dao.jpa;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.OrcDespesaMes;
import com.ciandt.pms.persistence.dao.IOrcDespesaMesDao;

/**
 * 
 * @author peter
 * 
 */
@Repository
public class OrcDespesaMesDao extends AbstractDao<Long, OrcDespesaMes>
		implements IOrcDespesaMesDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public OrcDespesaMesDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, OrcDespesaMes.class);
	}

	/**
	 * Busca os orcDespesaMes para saber o total já emitido de TravelBudget por
	 * clob e meses.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<OrcDespesaMes> findOrcDespesaMesByClobAndPeriod(
			ContratoPratica clob, Date dataInicial, Date dataFinal) {

		SimpleDateFormat formatador = new SimpleDateFormat("MM/yyyy");

		List<OrcDespesaMes> listReuslt = getJpaTemplate().findByNamedQuery(
				OrcDespesaMes.FIND_SUM_PERC_MES_BY_CLOB_AND_PERIOD,
				new Object[] { clob.getCodigoContratoPratica(),
						formatador.format(dataInicial),
						formatador.format(dataFinal) });

		return listReuslt;
	}
}
