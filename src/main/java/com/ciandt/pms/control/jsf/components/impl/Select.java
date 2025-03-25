package com.ciandt.pms.control.jsf.components.impl;

import com.ciandt.pms.control.jsf.components.ISelect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Select<T> implements ISelect<T>{

    protected Map<String, Long> map;
    protected List<String> list;
    protected String selected;
    protected String filter;
    protected List<T> entities;
    protected T entity;

    /**
     * @param entities - Entities
     */
    protected Select(List<T> entities){
        this.entities = entities;
        build();
    }

    /* Build */
    protected void build() {
        this.map = new HashMap<String, Long>();
        this.list = new ArrayList<String>();
        for (T t : entities) {
            this.list.add(objectKey(t));
            this.map.put(objectKey(t), objectValue(t));
        }
    }

    /* private */
    private boolean isEmptyFilter(){
        return filter == null || filter.trim().equals("");
    }
    private boolean isEmpty(){
        return selected == null || selected.trim().equals("");
    }

    /* Overrides */
    public Map<String, Long> map() {
        return map;
    }
    public List<String> list() {
        return list;
    }
    public Long filter(){
        return isEmptyFilter() ? 0L : map.get(filter);
    }
    public Long value(){
        return isEmpty() ? null : map.get(selected);
    }
    public T entity() {
        return entity;
    }
    public void select(Long code) {
        this.selected = "";
        entity = entityByValue(code);
        if(entity != null)
            this.selected = objectKey(entity);
    }

    /* Getters and Setters */
    public void setMap(Map<String, Long> map) {
        this.map = map;
    }
    public void setList(List<String> list) {
        this.list = list;
    }
    public Map<String, Long> getMap() {
        return map;
    }
    public List<String> getList() {
        return list;
    }
    public String getSelected() {
        return selected;
    }
    public void setSelected(String value) {
        selected = value;
    }
    public T getEntity() {
        return entity;
    }
    public void setEntity(T entity) {
        this.entity = entity;
    }
    public String getFilter() {
        return filter;
    }
    public void setFilter(String filter) {
        this.filter = filter;
    }

    /* Abstracts */
    protected abstract Long objectValue(T entity);
    protected abstract String objectKey(T entity);
    protected abstract T entityByValue(Long code);
}