package com.ciandt.pms.business.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ciandt.pms.business.service.IItemFaturaApagadoService;
import com.ciandt.pms.model.FaturaApagada;
import com.ciandt.pms.model.ItemFaturaApagado;
import com.ciandt.pms.persistence.dao.IItemFaturaApagadoDao;

/**
 * 
 * A classe ItemFaturaService proporciona as funcionalidades de serviço para a
 * entidade ItemFatura.
 * 
 * @since 03/11/2009
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Service
public class ItemFaturaApagadoService implements IItemFaturaApagadoService {

	/**
	 * Instancia do DAO da entidade.
	 */
	@Autowired
	private IItemFaturaApagadoDao dao;

	@Override
	public void create(ItemFaturaApagado itemFaturaApagado) {
		dao.create(itemFaturaApagado);
	}

	@Override
	public ItemFaturaApagado findById(Long id) {
		return dao.findById(id);
	}

	@Override
	public List<ItemFaturaApagado> findByFaturaApagada(FaturaApagada faturaApagada) {
		return dao.findByFaturaApagada(faturaApagada);
	}
	
	@Override
	public List<ItemFaturaApagado> findByCodigoFaturaApagada(Long codigoFaturaApagada) {
		return dao.findByCodigoFaturaApagada(codigoFaturaApagada);
	}

	@Override
	public BigDecimal getTotalByFaturaApagada(FaturaApagada faturaApagada) {
		List<ItemFaturaApagado> itemFaturaApagados = this.findByFaturaApagada(faturaApagada);

		BigDecimal totalItems = BigDecimal.ZERO;
		if (itemFaturaApagados != null) {

			for (ItemFaturaApagado itemFaturaApagado : itemFaturaApagados) {

				totalItems.add(itemFaturaApagado.getValorItem());
			}
		}

		return totalItems;
	}

}