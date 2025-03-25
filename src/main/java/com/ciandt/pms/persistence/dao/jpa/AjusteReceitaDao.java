package com.ciandt.pms.persistence.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.persistence.dao.IAjusteReceitaDao;

/**
 * 
 * A classe AjusteReceita proporciona as funcionalidades da camada de
 * persistencia referente a entidade Area.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
@Repository
public class AjusteReceitaDao extends AbstractDao<Long, AjusteReceita>
		implements IAjusteReceitaDao {

	/**
	 * Construtor padrão.
	 * 
	 * @param factory
	 *            - Fabrica da entidade.
	 */
	@Autowired
	public AjusteReceitaDao(
			@Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
		super(factory, AjusteReceita.class);
	}

	/**
	 * Busca todas as entidades.
	 * 
	 * @return retorna uma lista de AjusteReceita.
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findAll() {

		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_ALL);

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pela data de ajuste.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByDateAjuste(final Date date) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_DATE_AJUSTE, new Object[] { date });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pela data de ajuste e ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByDateAjusteAndContratoPratica(
			final Date date, final ContratoPratica contratoPratica) {
		List<AjusteReceita> listResult = getJpaTemplate()
				.findByNamedQuery(
						AjusteReceita.FIND_BY_DATE_AJUSTE_AND_CONTRATO_PRATICA,
						new Object[] { date,
								contratoPratica.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pela data de receita.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByDateReceita(final Date date) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_DATE_RECEITA, new Object[] { date });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pela data de receita e ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByDateReceitaAndContratoPratica(
			final Date date, final ContratoPratica contratoPratica) {
		List<AjusteReceita> listResult = getJpaTemplate()
				.findByNamedQuery(
						AjusteReceita.FIND_BY_DATE_RECEITA_AND_CONTRATO_PRATICA,
						new Object[] { date,
								contratoPratica.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pela data de receita e data de ajuste.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data de ajuste para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByDateReceitaAndDateAjuste(
			final Date dateReceita, final Date dateAjuste) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_DATE_RECEITA_AND_DATE_AJUSTE,
				new Object[] { dateReceita, dateAjuste });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pela data de receita, data de ajuste e
	 * ContratoPratica.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data de ajuste para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByDateReceitaAndDateAjusteAndContratoPratica(
			final Date dateReceita, final Date dateAjuste,
			final ContratoPratica contratoPratica) {
		List<AjusteReceita> listResult = getJpaTemplate()
				.findByNamedQuery(
						AjusteReceita.FIND_BY_DATE_RECEITA_AND_DATE_AJUSTE_AND_CONTRATO_PRATICA,
						new Object[] { dateReceita, dateAjuste,
								contratoPratica.getCodigoContratoPratica() });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita entre duas datas.
	 * 
	 * @param dataInicio
	 *            - data de inicio para o filtro
	 * @param dataFim
	 *            - data de fim para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByRangeDate(final Date dataInicio,
			final Date dataFim) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_RANGE_DATE,
				new Object[] { dataInicio, dataFim });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita pelo filtro.
	 * 
	 * @param filter
	 *            - entidade contendo o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByFilter(final AjusteReceita filter) {

		if (filter.getCodigoAjusteReceita() == null) {
			filter.setCodigoAjusteReceita(0L);
		}

		if (filter.getTipoAjuste().getCodigoTipoAjuste() == null) {
			filter.getTipoAjuste().setCodigoTipoAjuste(0L);
		}

		if (filter.getReceitaDealFiscal().getCodigoReceitaDfiscal() == null) {
			filter.getReceitaDealFiscal().setCodigoReceitaDfiscal(0L);
		}

		List<AjusteReceita> listResult = getJpaTemplate()
				.findByNamedQuery(
						AjusteReceita.FIND_BY_FILTER,
						new Object[] {
								filter.getCodigoAjusteReceita(),
								filter.getCodigoLoginAutor(),
								filter.getDataMesAjuste(),
								filter.getReceitaDealFiscal()
										.getCodigoReceitaDfiscal(),
								filter.getReceitaDealFiscal()
										.getCodigoReceitaDfiscal(),
								filter.getTipoAjuste().getCodigoTipoAjuste(),
								filter.getTipoAjuste().getCodigoTipoAjuste() });

		return listResult;
	}

	/**
	 * Busca os AjusteReceita de um dealFiscal.
	 * 
	 * @param receitaDealFiscal
	 *            recebe um receitaDEalFiscal
	 * 
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByReceitaDealFiscal(
			final ReceitaDealFiscal receitaDealFiscal) {
		List<AjusteReceita> result = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_RECEITA_DEAL_FISCAL,
				new Object[] { receitaDealFiscal.getCodigoReceitaDfiscal() });

		return result;
	}

	/**
	 * Busca AjusteReceita por Receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByReceita(final Receita receita) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_RECEITA,
				new Object[] { receita.getCodigoReceita() });
		return listResult;
	}

	/**
	 * Busca AjusteReceita por ReceitaMoeda.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	@SuppressWarnings("unchecked")
	public List<AjusteReceita> findByReceitaMoeda(
			final ReceitaMoeda receitaMoeda) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_RECEITA_MOEDA,
				new Object[] { receitaMoeda.getCodigoReceitaMoeda() });
		return listResult;
	}

	/**
	 * Busca AjusteReceita pelo ajusteReceitaPai
	 * 
	 * @param ajusteReceitaPai
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AjusteReceita findByAjusteReceitaPai(
			final AjusteReceita ajusteReceitaPai) {
		List<AjusteReceita> listResult = getJpaTemplate().findByNamedQuery(
				AjusteReceita.FIND_BY_AJUSTE_RECEITA_PAI,
				new Object[] { ajusteReceitaPai.getCodigoAjusteReceita() });
		if (!listResult.isEmpty())
			return listResult.get(0);

		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.persistence.dao.IAjusteReceitaDao#
	 * getTotalPublishedByDealFiscalAndDate(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal dealFiscal, final Date dataFim) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("codigoDealFiscal", dealFiscal.getCodigoDealFiscal());
		params.put("dataFim", dataFim);
		
		List<AjusteReceita> ajusteReceitas = getJpaTemplate().findByNamedQueryAndNamedParams(
				AjusteReceita.GET_TOTAL_PUBLISHED_BY_DEAL_FISCAL_AND_DATE, params);

		if (ajusteReceitas.isEmpty()) {
			return BigDecimal.valueOf(0);
		}
		
		BigDecimal total = new BigDecimal(0d);
		
		for (AjusteReceita ajusteReceita : ajusteReceitas) {
			if (ajusteReceita.getValorAjuste() != null) {
				total = total.add(ajusteReceita.getValorAjuste());
			}
		}

		return total;
	}
}
