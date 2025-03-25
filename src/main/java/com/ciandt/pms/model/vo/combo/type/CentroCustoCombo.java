package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.VwMegaCCusto;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de CentroCustoCombo.
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public class CentroCustoCombo extends ComboFactory<VwMegaCCusto, VwMegaCCusto> {

	/**
	 * @param t
	 */
	public CentroCustoCombo(List<VwMegaCCusto> t) {
		super(t);
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(VwMegaCCusto input) {
		return input.getDescricao();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public VwMegaCCusto getValue(VwMegaCCusto input) {
		return input;
	}




}
