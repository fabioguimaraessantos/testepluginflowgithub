/**
 * 
 */
package com.ciandt.pms.model.vo.combo.type;

import java.util.Arrays;

/**
 * Define um combo de ativo e inativo
 *
 * @since 20/08/2014
 * @author <a href="mailto:alan@ciandt.com">Alan Thiago do Prado</a>
 *
 */
public class AtivoInativoCombo extends ComboFactory<String, String> {

	/**
	 * @param t
	 */
	public AtivoInativoCombo(String[] value) {
		super(Arrays
				.asList(value));
	}

	/* 
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(String input) {
		return input;
	}

	/* 
	 * @see com.ciandt.pms.model.vo.combo.type.ComboFactory#getValue(java.lang.Object)
	 */
	@Override
	public String getValue(String input) {
		return input;
	}

}
