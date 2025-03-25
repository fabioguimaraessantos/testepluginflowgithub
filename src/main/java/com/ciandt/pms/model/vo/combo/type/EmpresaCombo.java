/**
 * 
 */
package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.Empresa;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de ClienteCombo.
 * 
 * @author <a href="luizsj@ciandt.com">Luiz Souza</a>
 * @since Mar 28, 2016
 */
public class EmpresaCombo extends ComboFactory<Empresa, Empresa> {

	/**
	 * @param t
	 */
	public EmpresaCombo(List<Empresa> t) {
		super(t);
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Empresa input) {
		return input.getNomeEmpresa();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public Empresa getValue(Empresa input) {
		return input;
	}

}
