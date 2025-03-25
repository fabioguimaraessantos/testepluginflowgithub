package com.ciandt.pms.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ciandt.pms.model.DealFiscal;
import com.ciandt.pms.model.TipoServicoDealFiscal;

/**
 * 
 * @author peter
 * 
 */
@Service
public interface ITipoServicoDealFiscalService {

	/**
	 * Cria uma entidade.
	 * 
	 * @param tipoSevicoDealFiscal
	 */
	void createTipoServicoDealFiscal(
			final TipoServicoDealFiscal tipoSevicoDealFiscal);

	/**
	 * Remove uma entidade
	 * 
	 * @param tipoServicoDealFiscal
	 */
	void remove(final TipoServicoDealFiscal tipoServicoDealFiscal);

	/**
	 * Busca por id.
	 * 
	 * @param id
	 * @return
	 */
	TipoServicoDealFiscal findById(final Long id);

	/**
	 * Busca por dealfiscal.
	 * 
	 * @param dealFiscal
	 * @return
	 */
	List<TipoServicoDealFiscal> findByDealFiscal(final DealFiscal dealFiscal);
}
