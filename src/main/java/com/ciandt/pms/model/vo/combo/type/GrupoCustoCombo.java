/**
 * 
 */
package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.Empresa;
import com.ciandt.pms.model.GrupoCusto;

import java.util.List;

/**
 * Define uma entidade que representa um combo box de GrupoCusto.
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public class GrupoCustoCombo extends ComboFactory<GrupoCusto, GrupoCusto> {
	
	/**
	 * @param t
	 */
	public GrupoCustoCombo(List<GrupoCusto> t) {
		super(t);
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(GrupoCusto input) {
		StringBuilder bd = new StringBuilder();
		bd.append(input.getNomeGrupoCusto());
		Empresa empresa = input.getEmpresa();
		if (empresa != null) {
		bd.append(" - ");
			bd.append(empresa.getNomeEmpresa());
		}
		return bd.toString();
	}

	/* (non-Javadoc)
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public GrupoCusto getValue(GrupoCusto input) {
		return input;
	}
}
