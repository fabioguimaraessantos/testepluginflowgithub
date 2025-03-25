package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.Pessoa;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de Pessoa.
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public class PessoaCombo extends ComboFactory<Pessoa, Pessoa> {

	/**
	 * @param t
	 */
	public PessoaCombo(List<Pessoa> t) {
		super(t);
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Pessoa input) {
		return input.getCodigoLogin();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public Pessoa getValue(Pessoa input) {
		return input;
	}

	

}
