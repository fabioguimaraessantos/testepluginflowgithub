package com.ciandt.pms.control.jsf.components;

import java.util.List;
import java.util.Map;

public interface ISelect<T> {

    Map<String, Long> map();

    List<String> list();

    Long filter();

    Long value();

    T entity();

    void select(Long code);
}