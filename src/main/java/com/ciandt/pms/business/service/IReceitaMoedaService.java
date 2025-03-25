package com.ciandt.pms.business.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaMoeda;

/**
 * 
 * A classe IReceitaMoedaService proporciona a interface de acesso a camada de
 * serviço referente a entidade ReceitaMoeda.
 * 
 * @since 22/10/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Service
public interface IReceitaMoedaService {

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	List<ReceitaMoeda> findReceitaMoedaAll();

	/**
	 * Busca a entidade pelo id.
	 * 
	 * @param id
	 *            - id da entidade
	 * 
	 * @return retorna uma instancia do tipo ReceitaMoeda
	 */
	ReceitaMoeda findReceitaMoedaById(final Long id);

	/**
	 * Calcula o total de ajustes realizados nos Fiscal deals de uma
	 * ReceitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda
	 * @return totalAjuste - Double
	 */
	Double calculateTotalAjuste(final ReceitaMoeda receitaMoeda);

	/**
	 * Calcula o total de ReceitaPlantao realizados nos Fiscal deals de uma
	 * ReceitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda
	 * @return totalReceitaPlantao - Double
	 */
	Double calculateTotalReceitaPlantao(final ReceitaMoeda receitaMoeda);

	/**
	 * Remove a entidade.
	 * 
	 * @param receitaMoeda
	 *            - entidade
	 */
	void removeReceitaMoeda(final ReceitaMoeda receitaMoeda);

	/**
	 * Busca ReceitaMoeda por um período e contrato prática.
	 * 
	 * @param dataInicio
	 *            dataInicio
	 * @param dataFim
	 *            dataFim
	 * @param codigoContratoPratica
	 *            codigoContratoPratica
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	List<ReceitaMoeda> findByPeriodoAndContratoPratica(final Date dataInicio,
			final Date dataFim, final Long codigoContratoPratica);

	/**
	 * Busca ReceitaMoeda por receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult.
	 */
	List<ReceitaMoeda> findReceitaMoedaByReceita(final Receita receita);

	/**
	 * Atualiza o objeto.
	 * 
	 * @param receitaMoeda
	 *            receitaMoeda
	 */
	void updateReceitaMoeda(final ReceitaMoeda receitaMoeda);

	/**
	 * Obtem uma {@link ReceitaMoeda} a partir de uma {@link ContratoPratica},
	 * {@link Moeda} e Mes.
	 * 
	 * @param contratoPratica
	 *            - o {@link ContratoPratica} em questao
	 * 
	 * @param moeda
	 *            - a {@link Moeda} em questao
	 * 
	 * @param dataMes
	 *            - mes de referencia
	 * 
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	ReceitaMoeda findReceitaMoedaByClobAndMoedaAndDataMes(
			final ContratoPratica contratoPratica, final Moeda moeda,
			final Date dataMes);
}