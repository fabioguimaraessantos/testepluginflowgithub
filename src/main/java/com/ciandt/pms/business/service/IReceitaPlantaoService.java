package com.ciandt.pms.business.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaPlantao;

/**
 * 
 * A classe IReceitaPlantaoService proporciona a interface de acesso a camada de
 * serviço referente a entidade ReceitaPlantaoService.
 * 
 * @since 14/07/2015
 * @author luizsj
 * 
 */
@Service
public interface IReceitaPlantaoService {

	/**
	 * Insere uma entidade.
	 * 
	 * @param entity
	 *            entidade a ser inserida.
	 */
	@Transactional
	void create(final ReceitaPlantao entity);

	/**
	 * Executa um update na entidade passado por parametro.
	 * 
	 * @param entity
	 *            que sera atualizada.
	 * 
	 */
	@Transactional
	void update(final ReceitaPlantao entity);

	/**
	 * Deleta uma entidade.
	 * 
	 * @param entity
	 *            que sera apagada.
	 */
	@Transactional
	void remove(final ReceitaPlantao entity);

	/**
	 * Busca uma entidade pelo id.
	 * 
	 * @param id
	 *            da entidade.
	 * 
	 * @return entidade com o id passado por parametro.
	 */
	ReceitaPlantao findById(final Long id);

	/**
	 * Retorna todas as entidades.
	 * 
	 * @return retorna uma lista com todas as entidades.
	 */
	List<ReceitaPlantao> findAll();

	ReceitaPlantao findByReceitaDealFiscal(ReceitaDealFiscal receitaDealFiscal);

	Boolean updateDutyHourRevenueFromMega(ReceitaPlantao receitaPlantao, String revenueStatus, BigDecimal orderID, String errorMessage);

	/**
	 * Percorre todos os itens da lista e os cria ou atualiza.
	 * 
	 * @param receitaPlantaos
	 * @param receita 
	 */
	@Transactional
	void createOrUpdate(List<ReceitaPlantao> receitaPlantaos);
}
