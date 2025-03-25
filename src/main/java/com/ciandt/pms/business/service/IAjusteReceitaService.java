package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.AjusteReceita;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaMoeda;

/**
 * 
 * A classe IAjusteReceitaService proporciona a interface de acesso a camada de
 * serviço referente a entidade AjusteReceitaService.
 * 
 * @since 14/07/2011
 * @author cmantovani
 * 
 */
@Service
public interface IAjusteReceitaService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void createAjusteReceita(final AjusteReceita entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Transactional
	void updateAjusteReceita(final AjusteReceita entity);

	/**
	 * Atualiza varias entidades AjusteReceita.
	 * 
	 * @param ajusteReceitaShared
	 *            - entidade com os atributos comuns compartilhados
	 * @param ajusteReceitaList
	 *            lista com os AjusteReceita.
	 * @return true se atualizado com sucesso
	 */
	@Transactional
	Boolean updateAjusteReceita(final AjusteReceita ajusteReceitaShared,
			final List<AjusteReceita> ajusteReceitaList);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Transactional
	void removeAjusteReceita(final AjusteReceita entity);

	/**
	 * Remove os ajustes de receita passada uma receita.
	 * 
	 * @param receita
	 *            the receita
	 */
	@Transactional
	void removeAjusteReceitaByReceita(final Receita receita);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	AjusteReceita findAjusteReceitaById(final Long id);

	/**
	 * Busca os AjusteReceita pelo filtro.
	 * 
	 * @param filter
	 *            - entidade contendo o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByFilter(final AjusteReceita filter);

	/**
	 * Busca os AjusteReceita pela data de ajuste.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByDateAjuste(final Date date);

	/**
	 * Busca os AjusteReceita pela data de ajuste e pelo ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByDateAjusteAndContratoPratica(
			final Date date, final ContratoPratica contratoPratica);

	/**
	 * Busca os AjusteReceita pela data de receita.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByDateReceita(final Date date);

	/**
	 * Busca os AjusteReceita pela data de receita e pelo ContratoPratica.
	 * 
	 * @param date
	 *            - data para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByDateReceitaAndContratoPratica(
			final Date date, final ContratoPratica contratoPratica);

	/**
	 * Busca os AjusteReceita pela data de receita e data de ajuste.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data do ajuste para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByDateReceitaAndDateAjuste(
			final Date dateReceita, final Date dateAjuste);

	/**
	 * Busca os AjusteReceita pela data de receita, data de ajuste e pelo
	 * ContratoPratica.
	 * 
	 * @param dateReceita
	 *            - data da receita para o filtro
	 * @param dateAjuste
	 *            - data do ajuste para o filtro
	 * @param contratoPratica
	 *            - contratoPratica para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByDateReceitaAndDateAjusteAndContratoPratica(
			final Date dateReceita, final Date dateAjuste,
			final ContratoPratica contratoPratica);

	/**
	 * Busca os AjusteReceita entre duas datas.
	 * 
	 * @param dataInicio
	 *            - data de inicio para o filtro
	 * @param dataFim
	 *            - data de fim para o filtro
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByRangeDate(final Date dataInicio,
			final Date dataFim);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<AjusteReceita> findAjusteReceitaAll();

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
	Boolean verifyClosingDateRevenue(final Date startDate,
			final Boolean showMsgError);

	/**
	 * Busca os AjusteReceita de um dealFiscal.
	 * 
	 * @param receitaDealFiscal
	 *            recebe um receitaDEalFiscal
	 * 
	 * @return lista de AjusteReceita de acordo com o filtro
	 */
	List<AjusteReceita> findAjusteReceitaByReceitaDealFiscal(
			final ReceitaDealFiscal receitaDealFiscal);

	/**
	 * Busca AjusteReceita por Receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	List<AjusteReceita> findAjusteReceitaByReceita(final Receita receita);

	/**
	 * Busca AjusteReceita por ReceitaMoeda.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult
	 */
	List<AjusteReceita> findByReceitaMoeda(final ReceitaMoeda receitaMoeda);

	/**
	 * Busca AjusteReceita pelo ajusteReceitaPai
	 * 
	 * @param ajusteReceitaPai
	 * @return
	 */
	AjusteReceita findByAjusteReceitaPai(final AjusteReceita ajusteReceitaPai);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ciandt.pms.business.service.IAjusteReceitaService#
	 * getTotalPublishedByDealFiscalAndDate(com.ciandt.pms.model.DealFiscal,
	 * java.util.Date)
	 */
	BigDecimal getTotalPublishedByDealFiscalAndDate(
			final DealFiscal dealFiscal, final Date dataFim);

}
