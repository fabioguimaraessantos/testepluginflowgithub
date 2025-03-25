package com.ciandt.pms.persistence.dao.jpa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Cliente;
import com.ciandt.pms.model.ControleReajuste;
import com.ciandt.pms.model.ControleReajusteAud;
import com.ciandt.pms.model.FichaReajuste;
import com.ciandt.pms.model.Msa;
import com.ciandt.pms.persistence.dao.IControleReajusteDao;


/**
 * A classe ClassNameDao proporciona as funcionalidades de acesso ao banco de dados
 * referente a entidade ClassName.
 *
 * @since 11/12/2013
 * @author <a href="mailto:luizsj@ciandt.com">Alisson Fernando da Silva</a>
 */
@Repository
public class ControleReajusteDao extends AbstractDao<Long, ControleReajuste>
        implements IControleReajusteDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public ControleReajusteDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ControleReajuste.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<ControleReajuste> findAll() {
        List<ControleReajuste> listResult = getJpaTemplate().findByNamedQuery(
        		ControleReajuste.FIND_ALL);

        return listResult;
    }

	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre
	 * {@code startDate} e {@code endDate}.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link ControleReajuste}
	 */
    @Override
    @SuppressWarnings("unchecked")
	public List<ControleReajuste> findByDateIntervalAndFichaReajuste(
			Date startDate, Date endDate, FichaReajuste fichaReajuste) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("fichaReajuste", fichaReajuste);

		List<ControleReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajuste.FIND_BY_DATE_INTERVAL_AND_FICHA_REAJUSTE,
						params);

		if (results.isEmpty()) {
			return null;
		}

		return results;
	}

	/**
	 * Busca {@link ControleReajuste} onde sua Data prevista é maior que
	 * {@code date}.
	 * 
	 * @param date
	 * @return Lista de {@link ControleReajuste}
	 */
    @Override
    @SuppressWarnings("unchecked")
	public List<ControleReajuste> findGreaterThanDateAndFichaReajuste(
			Date date, FichaReajuste fichaReajuste) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("date", date);
		params.put("fichaReajuste", fichaReajuste);

		List<ControleReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajuste.FIND_GREATER_THAN_DATE_AND_FICHA_REAJUSTE,
						params);

		if (results.isEmpty()) {
			return null;
		}

		return results;
	}
    
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IControleReajusteDao#
	 * findHistoryByCodigoControleReajuste(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ControleReajusteAud> findHistoryByCodigoControleReajuste(
			final Long codigoControleReajuste) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoControleReajuste", codigoControleReajuste);

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ControleReajusteAud.FIND_BY_CODIGO_CONTROLE_REAJUSTE, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IControleReajusteDao#
	 * findHistoryByCdControleReajusteAndRevtype(java.lang.Long, java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<ControleReajusteAud> findHistoryByCdControleReajusteAndRevtype(
			final Long codigoControleReajuste, final Long revtype) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoControleReajuste", codigoControleReajuste);
		params.put("revtype", revtype);

		return getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajusteAud.FIND_BY_CODIGO_CONTROLE_REAJUSTE_AND_REVTYPE,
						params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ciandt.pms.persistence.dao.IControleReajusteDao#findToSendEmail(java
	 * .util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<ControleReajuste> findToSendEmail(
			final Date dataInicioLembrete, final Date dataUltimoEnvioEmail) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dataInicioLembrete", dataInicioLembrete);
		params.put("dataUltimoEnvioEmail", dataUltimoEnvioEmail);

		return getJpaTemplate().findByNamedQueryAndNamedParams(
				ControleReajuste.FIND_TO_SEND_EMAIL, params);
	}

	/**
	 * Retorna o ultimo {@link ControleReajuste} com {@code fichasResjustes}.
	 * 
	 * @param fichasReajustes
	 * @return List<ControleReajuste>
	 */
	@SuppressWarnings("unchecked")
	public ControleReajuste findLastbyFichaReajuste(
			final FichaReajuste fichasReajustes) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fichaReajuste", fichasReajustes);

		List<ControleReajuste> controleReajustes = new ArrayList<ControleReajuste>();
		controleReajustes =  getJpaTemplate().findByNamedQueryAndNamedParams(
				ControleReajuste.FIND_LAST_BY_FICHA_REAJUSTE, params);

		if (controleReajustes == null || controleReajustes.isEmpty()) {
			return new ControleReajuste();
		}

		return controleReajustes.get(0);
	}

	/**
	 * Busca por ficha de reajuste.
	 * 
	 * @param fichaReajuste
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ControleReajuste> findByFichaReajuste(
			final FichaReajuste fichaReajuste) {
		List<ControleReajuste> listResult = getJpaTemplate().findByNamedQuery(
				ControleReajuste.FIND_BY_FICHA_REAJUSTE,
				new Object[] { fichaReajuste.getCodigoFichaReajuste() });
		return listResult;
	}


	/**
	 * Busca {@link ControleReajuste} onde sua Data Prevista está entre
	 * {@code startDate} e {@code endDate}.
	 * 
	 * @param startDate
	 * @param endDate
	 * @return Lista de {@link ControleReajuste}
	 */
    @Override
    @SuppressWarnings("unchecked")
	public List<ControleReajuste> findByDateInterval(
			Date startDate, Date endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);

		List<ControleReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajuste.FIND_BY_DATE_INTERVAL,
						params);

		return results;
	}

	/**
	 * Busca {@link ControleReajuste} onde sua {@code dataPrevista} está entre
	 * {@code startDate} e {@code endDate} e está relacionado a {@link Cliente}
	 * e {@link Msa}.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param msa
	 * @param cliente
	 * @return List<ControleReajuste>
	 */
    @Override
    @SuppressWarnings("unchecked")
	public List<ControleReajuste> findByDateIntervalAndMsaAndCliente(
			final Date startDate, final Date endDate, final Msa msa,
			final Cliente cliente, final Long controleReajusteStatusId) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("cliente", cliente.getCodigoCliente());
		params.put("msa", msa.getCodigoMsa());
		params.put("controleReajusteStatusId", controleReajusteStatusId);

		List<ControleReajuste> results = getJpaTemplate()
				.findByNamedQueryAndNamedParams(
						ControleReajuste.FIND_BY_DATE_INTERVAL_AND_MSA_AND_CLIENTE,
						params);

		return results;
    }
}