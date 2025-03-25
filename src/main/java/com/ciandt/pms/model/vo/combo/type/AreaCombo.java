package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.Area;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de Area..
 * 
 * @author <a href="mailto:mvidolin@ciandt.com">Marcos Vidolin</a>
 * @since Aug 18, 2014
 */
public class AreaCombo extends ComboFactory<Area, Area> {

	/**
	 * @param t
	 */
	public AreaCombo(List<Area> t) {
		super(t);
	}

	/*
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Area input) {
		return input.getNomeArea();
	}

	/*
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public Area getValue(Area input) {
		return input;
	}



}
