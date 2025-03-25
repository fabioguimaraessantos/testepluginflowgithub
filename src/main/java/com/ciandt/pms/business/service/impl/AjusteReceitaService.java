package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.Constants;
import com.ciandt.pms.business.service.IAjusteReceitaService;
import com.ciandt.pms.business.service.IModuloService;
import com.ciandt.pms.business.service.IReceitaDealFiscalService;
import com.ciandt.pms.business.service.ITipoAjusteService;
import com.ciandt.pms.control.jsf.util.Messages;
import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.model.TipoAjuste;
import com.ciandt.pms.persistence.dao.IAjusteReceitaDao;
import com.ciandt.pms.util.DateUtil;

/**
 * 
 * A classe AjusteReceitaService proporciona as funcionalidades da camada de
 * negócio referente a entidade AjusteReceita.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
@Service
public class AjusteReceitaService implements IAjusteReceitaService {

	/** Instancia do Dao da entidade AjusteReceita. */
	@Autowired
	private IAjusteReceitaDao ajusteReceitaDao;

	/** Instancia do Servico. */
	@Autowired
	private IReceitaDealFiscalService receitaDealFiscalService;

	/** Instancia do Servico. */
	@Autowired
	private ITipoAjusteService tipoAjusteService;

	/**
	 * Serviço de Modulo.
	 */
	@Autowired
	private IModuloService moduloService;

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	public void createAjusteReceita(final AjusteReceita entity) {
		ajusteReceitaDao.create(entity);
	}

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	public void updateAjusteReceita(final AjusteReceita entity) {
		ajusteReceitaDao.update(entity);
	}

	/**
	 * Atualiza varias entidades AjusteReceita.
	 * 
	 * @param ajusteReceitaShared
	 *            - entidade com os atributos comuns compartilhados
	 * @param ajusteReceitaList
	 *            lista com os AjusteReceita.
	 * @return true se atualizado com sucesso
	 */
	public Boolean updateAjusteReceita(final AjusteReceita ajusteReceitaShared,
			final List<AjusteReceita> ajusteReceitaList) {

		List<AjusteReceita> updateList = new ArrayList<AjusteReceita>();

		for (AjusteReceita ajusteReceita : ajusteReceitaList) {

			double valorAjuste = ajusteReceita.getValorAjuste().doubleValue();

			if (valorAjuste != 0
					&& ajusteReceita.getTipoAjuste().getCodigoTipoAjuste() == null) {
				Messages.showError("updateAjusteReceita",
						Constants.MSG_WARN_ADJUST_TYPE_NULL);

				return Boolean.valueOf(false);

			} else {

				AjusteReceita newAjusteReceita = new AjusteReceita();

				// atribuicao dos valores comuns
				newAjusteReceita.setCodigoAjusteReceita(ajusteReceita
						.getCodigoAjusteReceita());
				newAjusteReceita.setCodigoLoginAutor(ajusteReceitaShared
						.getCodigoLoginAutor());
				newAjusteReceita.setDataMesAjuste(ajusteReceitaShared
						.getDataMesAjuste());
				newAjusteReceita.setDataCriacao(ajusteReceitaShared
						.getDataCriacao());
				newAjusteReceita.setDataAtualizacao(new Date());

				// atribuicao dos valores particulares
				ReceitaDealFiscal receitaDealFiscal = receitaDealFiscalService
						.findReceitaDealById(ajusteReceita
								.getReceitaDealFiscal()
								.getCodigoReceitaDfiscal());
				newAjusteReceita.setReceitaDealFiscal(receitaDealFiscal);

				newAjusteReceita.setTextoObservacao(ajusteReceita
						.getTextoObservacao());

				TipoAjuste tipoAjuste = null;

				if (valorAjuste != 0) {

					tipoAjuste = tipoAjusteService
							.findTipoAjusteById(ajusteReceita.getTipoAjuste()
									.getCodigoTipoAjuste());

				}

				newAjusteReceita.setTipoAjuste(tipoAjuste);

				newAjusteReceita.setValorAjuste(ajusteReceita.getValorAjuste());

				updateList.add(newAjusteReceita);

			}
		}

		for (AjusteReceita update : updateList) {
			// atualizacao do AjusteReceita
			this.updateAjusteReceita(update);
		}

		return Boolean.valueOf(true);
	}

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	public void removeAjusteReceita(final AjusteReceita entity) {
		ajusteReceitaDao.remove(entity);
	}

	/**
	 * Remove os ajustes de receita passada uma receita.
	 * 
	 * @param receita
	 *            the receita
	 */
	@Transactional
	public void removeAjusteReceitaByReceita(final Receita receita) {
		for (AjusteReceita ajusteReceita : this
				.findAjusteReceitaByReceita(receita)) {
			this.removeAjusteReceita(ajusteReceita);
		}
	}

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	public AjusteReceita findAjusteReceitaById(final Long id) {
		return ajusteReceitaDao.findById(id);
	}

	/**
	 * Busca os AjusteReceita pelo filtro.
	 * 
	 * @param filter
	 *            - entidade contendo o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByFilter(
			final AjusteReceita filter) {
		return ajusteReceitaDao.findByFilter(filter);
	}

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	public List<AjusteReceita> findAjusteReceitaAll() {
		return ajusteReceitaDao.findAll();
	}

	/**
	 * Busca os AjusteReceita pela data de ajuste.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByDateAjuste(final Date date) {
		return ajusteReceitaDao.findByDateAjuste(date);
	}

	/**
	 * Busca os AjusteReceita pela data de ajuste e pelo ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByDateAjusteAndContratoPratica(
			final Date date, final ContratoPratica contratoPratica) {

		if (contratoPratica.getCodigoContratoPratica() == null) {
			return ajusteReceitaDao.findByDateAjuste(date);
		} else {
			return ajusteReceitaDao.findByDateAjusteAndContratoPratica(date,
					contratoPratica);
		}
	}

	/**
	 * Busca os AjusteReceita pela data de receita.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByDateReceita(final Date date) {
		return ajusteReceitaDao.findByDateReceita(date);
	}

	/**
	 * Busca os AjusteReceita pela data de receita e pelo ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByDateReceitaAndContratoPratica(
			final Date date, final ContratoPratica contratoPratica) {

		if (contratoPratica.getCodigoContratoPratica() == null) {
			return ajusteReceitaDao.findByDateReceita(date);
		} else {
			return ajusteReceitaDao.findByDateReceitaAndContratoPratica(date,
					contratoPratica);
		}
	}

	/**
	 * Busca os AjusteReceita pela data de receita e data de ajuste.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data do ajuste para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByDateReceitaAndDateAjuste(
			final Date dateReceita, final Date dateAjuste) {
		return ajusteReceitaDao.findByDateReceitaAndDateAjuste(dateReceita,
				dateAjuste);
	}

	/**
	 * Busca os AjusteReceita pela data de receita, data de ajuste e pelo
	 * ContratoPratica.
	 * 
	 * @param dateReceita
	 *            - data de receita para o filtro
	 * @param dateAjuste
	 *            - data de ajuste para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByDateReceitaAndDateAjusteAndContratoPratica(
			final Date dateReceita, final Date dateAjuste,
			final ContratoPratica contratoPratica) {

		if (contratoPratica.getCodigoContratoPratica() == null) {
			return ajusteReceitaDao.findByDateReceitaAndDateAjuste(dateReceita,
					dateAjuste);
		} else {
			return ajusteReceitaDao
					.findByDateReceitaAndDateAjusteAndContratoPratica(
							dateReceita, dateAjuste, contratoPratica);
		}
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
	public List<AjusteReceita> findAjusteReceitaByRangeDate(
			final Date dataInicio, final Date dataFim) {
		return ajusteReceitaDao.findByRangeDate(dataInicio, dataFim);
	}

	/**
	 * Validação do ClosingDateRevenue. Caso startDate (data de início da
	 * vigência) seja maior do que a data Revenue, a vigência é válida e pode
	 * ser adicionada ou removida. Caso contrário, a vigência não pode ser
	 * adicionada nem removida.
	 * 
	 * @param startDate
	 *            - data de início da vigência
	 * @param showMsgError
	 *            - mostra mensagem de erro caso startDate não seja válido
	 * 
	 * @return true se startDate for maior do que ClosingDateRevenue. false caso
	 *         contrário
	 */
	public Boolean verifyClosingDateRevenue(final Date startDate,
			final Boolean showMsgError) {
		Date closingRevenueDate = moduloService.getClosingDateRevenue();
		if (startDate.compareTo(closingRevenueDate) > 0) {
			return Boolean.valueOf(true);
		} else {
			if (showMsgError) {
				Messages.showError("verifyClosingDateRevenue",
						Constants.MSG_ERROR_AJUSTE_RECEITA_ADD_CLOS_DATE,
						DateUtil.formatDate(closingRevenueDate,
								Constants.DEFAULT_DATE_PATTERN_SIMPLE,
								Constants.DEFAULT_CALENDAR_LOCALE));
			}

			return Boolean.valueOf(false);
		}
	}

	/**
	 * Busca os AjusteReceita de um dealFiscal.
	 * 
	 * @param receitaDealFiscal
	 *            recebe um receitaDEalFiscal
	 * 
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	public List<AjusteReceita> findAjusteReceitaByReceitaDealFiscal(
			final ReceitaDealFiscal receitaDealFiscal) {
		return ajusteReceitaDao.findByReceitaDealFiscal(receitaDealFiscal);
	}

	/**
	 * Busca AjusteReceita por Receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	public List<AjusteReceita> findAjusteReceitaByReceita(final Receita receita) {
		return ajusteReceitaDao.findByReceita(receita);
	}

	/**
	 * Busca AjusteReceita por ReceitaMoeda.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	public List<AjusteReceita> findByReceitaMoeda(
			final ReceitaMoeda receitaMoeda) {
		return ajusteReceitaDao.findByReceitaMoeda(receitaMoeda);
	}

	/**
	 * Busca AjusteReceita pelo ajusteReceitaPai
	 * 
	 * @param ajusteReceitaPai
	 * @return
	 */
	public AjusteReceita findByAjusteReceitaPai(
			final AjusteReceita ajusteReceitaPai) {
		return ajusteReceitaDao.findByAjusteReceitaPai(ajusteReceitaPai);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IAjusteReceitaService#
	 * getTotalPublishedByDealFiscalAndDate(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	public BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal dealFiscal, final Date dataFim) {
		return ajusteReceitaDao.getTotalPublishedByDealFiscalAndDate(
				dealFiscal, dataFim);
	}
}
