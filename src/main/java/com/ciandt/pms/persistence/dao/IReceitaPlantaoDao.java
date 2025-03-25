package com.ciandt.pms.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import com.ciandt.pms.model.ReceitaDealFiscal;
import com.ciandt.pms.model.ReceitaPlantao;

/**
 * 
 * A classe IAjusteReceitaDao proporciona a interface de acesso a camada de
 * persistencia referente a entidade ReceitaPlantao.
 * 
 * @since 14/07/2015
 * @author luizsj
 * 
 */
public interface IReceitaPlantaoDao extends IAbstractDao<Long, ReceitaPlantao> {

	/**
	 * Busca todos os ReceitaPlantao.
	 * 
	 * @return lista de ReceitaPlantao com todos os ReceitaPlantao
	 */
	List<ReceitaPlantao> findAll();

	/**
	 * Busca os ReceitaPlantao de um dealFiscal.
	 * 
	 * @param receitaDealFiscal
	 *            recebe um receitaDEalFiscal
	 * 
	 * @return ReceitaPlantao de acordo com o filtro
	 */
	ReceitaPlantao findByReceitaDealFiscal(final Long receitaDealFiscalCode);

	Boolean updateStatusReceitaPlantao(Long revenueCode, String statusReceitaPlantao, BigDecimal orderID);
}
