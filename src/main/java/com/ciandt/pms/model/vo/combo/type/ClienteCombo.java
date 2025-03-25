/**
 * 
 */
package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.Cliente;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de ClienteCombo.
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public class ClienteCombo extends ComboFactory<Cliente, Cliente> {

	/**
	 * @param t
	 */
	public ClienteCombo(List<Cliente> t) {
		super(t);
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Cliente input) {
		return input.getNomeCliente();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public Cliente getValue(Cliente input) {
		return input;
	}

}
