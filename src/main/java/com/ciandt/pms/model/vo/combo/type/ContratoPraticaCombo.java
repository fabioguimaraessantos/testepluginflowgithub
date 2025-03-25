/**
 * 
 */
package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.ContratoPratica;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de ContratoPraticaCombo.
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public class ContratoPraticaCombo extends ComboFactory<ContratoPratica, ContratoPratica> {

	/**
	 * @param t
	 */
	public ContratoPraticaCombo(List<ContratoPratica> t) {
		super(t);
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(ContratoPratica input) {
		return input.getNomeContratoPratica();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public ContratoPratica getValue(ContratoPratica input) {
		return input;
	}


}
