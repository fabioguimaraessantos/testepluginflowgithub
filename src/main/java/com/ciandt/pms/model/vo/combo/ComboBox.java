package com.ciandt.pms.model.vo.combo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComboBox<T> {

	private List<String> list = new ArrayList<String>();
	private Map<String, T> map = new HashMap<String, T>();
	private String selected;

	public void createComboBox(Map<String, T> map) {
		this.map = map;
		this.list = new ArrayList<String>(map.keySet());
	}

	public List<String> getList() {
		return list;
	}

	public Map<String, T> getMap() {
		return map;
	}

	public String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		this.selected = selected;
	}
	
	public T getSelectedItem() {
		if (selected != null) {
			return map.get(selected);
		}
		return null;
	}

	public void clear() {
		this.selected = null;
		this.map.clear();
		this.list.clear();
	}
}
