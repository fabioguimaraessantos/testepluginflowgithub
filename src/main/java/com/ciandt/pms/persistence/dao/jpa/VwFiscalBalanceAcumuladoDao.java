package com.ciandt.pms.persistence.dao.jpa;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.model.VwFiscalBalanceAcumulado;
import com.ciandt.pms.persistence.dao.IVwFiscalBalanceAcumuladoDao;

/**
 * A classe IVwFiscalBalanceAcumuladoDao proporciona a interface de acesso a
 * camada de DAO referente a entidade {@link VwFiscalBalanceAcumulado}.
 * 
 * @since Jun 29, 2014
 * @author <a href="mailto:vidolin@ciandt.com">Marcos Vidolin</a>
 * 
 */
@Repository
public class VwFiscalBalanceAcumuladoDao extends
		AbstractDao<Long, VwFiscalBalanceAcumulado> implements
		IVwFiscalBalanceAcumuladoDao {

	/**
	 * Construtor.
	 * 
	 * @param factory
	 *            factory
	 */
	@Autowired
	public VwFiscalBalanceAcumuladoDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, VwFiscalBalanceAcumulado.class);
	}

	/**
	 * Retorna todas as entidades de acordo com o fiscalDeal.
	 * 
	 * @return retorna uma lista de {@link VwFiscalBalanceAcumulado}.
	 */
	@SuppressWarnings("unchecked")
	public List<VwFiscalBalanceAcumulado> findByfiscalDeal(
			final DealFiscal fiscalDeal) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoFiscalDeal", fiscalDeal.getCodigoDealFiscal());

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				VwFiscalBalanceAcumulado.FIND_ALL_BY_DEAL_FISCAL, params);
	}

	/**
	 * Retorna {@link VwFiscalBalanceAcumulado} com o Cliente e/ou MSA indicado
	 *
	 * @param msa
	 * @param cliente
	 * @return Lista de {@link VwFiscalBalanceAcumulado}
	 */
	@SuppressWarnings("unchecked")
	public List<VwFiscalBalanceAcumulado> findByMsaAndCliente(final Msa msa,
			final Cliente cliente) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("codigoMsa", msa.getCodigoMsa());
		params.put("codigoCliente", cliente.getCodigoCliente());

		List<VwFiscalBalanceAcumulado> vwFiscalBalanceAcumulados = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						VwFiscalBalanceAcumulado.FIND_BY_MSA_AND_CLIENTE,
						params);

		return vwFiscalBalanceAcumulados;
	}

	/**
	 * Retorna {@link VwFiscalBalanceAcumulado} com {@code DealFiscal},
	 * {@code startDate} e {@code endDate} indicados.
	 * 
	 * @param dealFiscal
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link VwFiscalBalanceAcumulado}
	 */
	@SuppressWarnings("unchecked")
	public List<VwFiscalBalanceAcumulado> findByDealFiscalAndPeriod(
			final DealFiscal dealFiscal, final Date startDate,
			final Date endDate) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("codigoDealFiscal", dealFiscal.getCodigoDealFiscal());
		params.put("startDate", startDate);
		params.put("endDate", endDate);

		List<VwFiscalBalanceAcumulado> vwFiscalBalanceAcumulados = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						VwFiscalBalanceAcumulado.FIND_ALL_BY_DEAL_FISCAL_AND_PERIOD,
						params);

		return vwFiscalBalanceAcumulados;
	}

	@SuppressWarnings("unchecked")
	public List<VwFiscalBalanceAcumulado> findByDealFiscalAndStartDate(
			final DealFiscal dealFiscal, final Date startDate) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("codigoDealFiscal", dealFiscal.getCodigoDealFiscal());
		params.put("startDate", startDate);

		List<VwFiscalBalanceAcumulado> vwFiscalBalanceAcumulados = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						VwFiscalBalanceAcumulado.FIND_BY_DEAL_FISCAL_AND_STARTDATE,
						params);

		return vwFiscalBalanceAcumulados;
	}
	
	public Double findPublishedFiscalBalanceByDealFiscal(
			final DealFiscal dealFiscal) {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("codigoDealFiscal", dealFiscal.getCodigoDealFiscal());

		Double sumPublishedFiscalBalance = (Double) getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						VwFiscalBalanceAcumulado.FIND_PUBLICSHED_FISCAL_BALANCE_BY_DEAL_FISCAL,
						params).get(0);

//		TypedQuery<Double> findByNamedQuery = getJpaTemplate().getEntityManager().createNamedQuery(VwFiscalBalanceAcumulado.FIND_PUBLICSHED_FISCAL_BALANCE_BY_DEAL_FISCAL, Double.class);
//		findByNamedQuery.setParameter("codigoDealFiscal",
//				dealFiscal.getCodigoDealFiscal());
//		Double sumPublishedFiscalBalance = findByNamedQuery.getSingleResult();

		return sumPublishedFiscalBalance;
	}
}