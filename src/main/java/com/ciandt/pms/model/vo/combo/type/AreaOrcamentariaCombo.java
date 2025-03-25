package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.AreaOrcamentaria;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de Area..
 * 
 * @author <a href="mailto:luizsj@ciandt.com">Luiz Souza</a>
 * @since Aug 18, 2015
 */
public class AreaOrcamentariaCombo extends ComboFactory<AreaOrcamentaria, AreaOrcamentaria> {

	/**
	 * @param t
	 */
	public AreaOrcamentariaCombo(List<AreaOrcamentaria> t) {
		super(t);
	}

	/*
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(AreaOrcamentaria input) {
		return input.getNomeAreaOrcamentaria();
	}

	/*
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public AreaOrcamentaria getValue(AreaOrcamentaria input) {
		return input;
	}



}
