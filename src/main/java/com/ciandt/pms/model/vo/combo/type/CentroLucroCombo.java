package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.CentroLucro;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de {@link CentroLucro}.
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since Aug 19, 2014
 */
public class CentroLucroCombo extends ComboFactory<CentroLucro, CentroLucro> {

	/**
	 * @param t
	 */
	public CentroLucroCombo(List<CentroLucro> t) {
		super(t);
	}

	/*
	 * @see
	 * com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(CentroLucro input) {
		return input.getNomeCentroLucro();
	}

	/*
	 * @see
	 * com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object
	 * )
	 */
	@Override
	public CentroLucro getValue(CentroLucro input) {
		return input;
	}

}
