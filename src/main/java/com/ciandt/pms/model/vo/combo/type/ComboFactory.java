package com.ciandt.pms.model.vo.combo.type;

import com.ciandt.pms.model.vo.combo.ComboBox;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstrai a criacao de um combo box a partir de uma fabrica.
 * T e o tipo de entrada e R e o tipo de saida
 * 
 * @author <a href="alan@ciandt.com">Alan Thiago do Prado</a>
 * @since Aug 18, 2014
 */
public abstract class ComboFactory<T, R> {


	protected List<T> list;

	/**
	 * Cria uma instancia de um combobox a partir de uma fabrica
	 * 
	 * @param factory
	 * @return ComboBox
	 */
	public static <T, R> ComboBox<R> create(ComboFactory<T, R> factory) {
		Map<String, R> map = new LinkedHashMap<String, R>();
		for (T input : factory.getList()) {
			map.put(factory.getKey(input), factory.getValue(input));
		}
		ComboBox<R> combo = new ComboBox<R>();
		combo.createComboBox(map);

		return combo;
	}

	/**
	 * 
	 */
	public ComboFactory(List<T> t) {
		this.list = t;
	}
	
	public List<T> getList(){
		return list;
	}
	
	/**
	 * @param input
	 * @return a texto do combo
	 */
	public abstract String getKey(T input);
	/**
	 * @param input
	 * @return o objeto correspondente ao valor do texto do combo
	 */
	public abstract R getValue(T input);

}
