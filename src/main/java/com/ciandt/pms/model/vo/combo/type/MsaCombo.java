package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.Msa;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de MsaCombo.
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public class MsaCombo extends ComboFactory<Msa, Long> {

	/**
	 * @param t
	 */
	public MsaCombo(List<Msa> t) {
		super(t);
	}
	
	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Msa input) {
		return input.getNomeMsa();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public Long getValue(Msa input) {
		return input.getCodigoMsa();
	}


}
