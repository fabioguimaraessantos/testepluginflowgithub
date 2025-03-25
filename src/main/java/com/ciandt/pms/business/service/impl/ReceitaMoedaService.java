package com.ciandt.pms.business.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.business.service.IReceitaMoedaService;
import com.ciandt.pms.model.ContratoPratica;
import com.ciandt.pms.model.Moeda;
import com.ciandt.pms.model.Receita;
import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaMoeda;
import com.ciandt.pms.persistence.dao.IReceitaMoedaDao;

/**
 * 
 * A classe ReceitaMoedaService proporciona as funcionalidades da camada de
 * serviço referente a entidade ReceitaMoeda.
 * 
 * @since 22/10/2012
 * @author <a href="mailto:diegos@ciandt.com">Diego Henrique Mila da Silva</a>
 * 
 */
@Service
public class ReceitaMoedaService implements IReceitaMoedaService {

	/** Instancia do DAO do ReceitaMoeda. */
	@Autowired
	private IReceitaMoedaDao dao;

	/**
	 * Retorna todas as entidades ativas.
	 * 
	 * @return retorna uma lista de ReceitaMoeda.
	 */
	public List<ReceitaMoeda> findReceitaMoedaAll() {
		return dao.findAll();
	}

	/**
	 * Busca a entidade pelo id.
	 * 
	 * @param id
	 *            - id da entidade
	 * 
	 * @return retorna uma instancia do tipo ReceitaMoeda
	 */
	public ReceitaMoeda findReceitaMoedaById(final Long id) {
		return dao.findById(id);
	}

	/**
	 * Calcula o total de ajustes realizados nos Fiscal deals de uma
	 * ReceitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda
	 * @return totalAjuste - Double
	 */
	public Double calculateTotalAjuste(final ReceitaMoeda receitaMoeda) {
		Double totalAjuste = Double.valueOf(0);

		if (receitaMoeda != null) {
			ReceitaMoeda recMoeda = this.findReceitaMoedaById(receitaMoeda
					.getCodigoReceitaMoeda());

			for (ReceitaDealFiscal receitaDealFiscal : recMoeda
					.getReceitaDealFiscals()) {
				totalAjuste += receitaDealFiscal.getTotalAdjustmentValue()
						.doubleValue();
			}
		}

		return totalAjuste;
	}
	
	/**
	 * Calcula o total de ReceitaPlantao realizados nos Fiscal deals de uma
	 * ReceitaMoeda.
	 * 
	 * @param receitaMoeda
	 *            - receitaMoeda
	 * @return totalReceitaPlantao - Double
	 */
	public Double calculateTotalReceitaPlantao(final ReceitaMoeda receitaMoeda) {
		Double totalReceitaPlantao = Double.valueOf(0);
		
		if (receitaMoeda != null) {
			ReceitaMoeda recMoeda = this.findReceitaMoedaById(receitaMoeda
					.getCodigoReceitaMoeda());
			
			for (ReceitaDealFiscal receitaDealFiscal : recMoeda
					.getReceitaDealFiscals()) {
				totalReceitaPlantao += receitaDealFiscal.getTotalReceitaPlantao()
						.doubleValue();
			}
		}
		
		return totalReceitaPlantao;
	}

	/**
	 * Remove a entidade.
	 * 
	 * @param receitaMoeda
	 *            - entidade
	 */
	@Transactional
	public void removeReceitaMoeda(final ReceitaMoeda receitaMoeda) {
		dao.remove(receitaMoeda);
	}

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
	public List<ReceitaMoeda> findByPeriodoAndContratoPratica(
			final Date dataInicio, final Date dataFim,
			final Long codigoContratoPratica) {
		return dao.findByPeriodoAndContratoPratica(dataInicio, dataFim,
				codigoContratoPratica);
	}

	/**
	 * Busca ReceitaMoeda por receita.
	 * 
	 * @param receita
	 *            the receita
	 * @return listResult.
	 */
	public List<ReceitaMoeda> findReceitaMoedaByReceita(final Receita receita) {
		return dao.findByReceita(receita);
	}

	/**
	 * Atualiza o objeto.
	 * 
	 * @param receitaMoeda
	 *            receitaMoeda
	 */
	public void updateReceitaMoeda(final ReceitaMoeda receitaMoeda) {
		dao.update(receitaMoeda);
	}

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
	@Override
	public ReceitaMoeda findReceitaMoedaByClobAndMoedaAndDataMes(
			final ContratoPratica contratoPratica, final Moeda moeda,
			final Date dataMes) {
		return dao
				.findByClobAndMoedaAndDataMes(contratoPratica, moeda, dataMes);
	}
}