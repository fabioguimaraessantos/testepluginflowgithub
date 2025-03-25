package com.ciandt.pms.tree.interfaces;

import org.richfaces.model.TreeNodeImpl;

import java.util.List;

public interface Tree<T> {

    List<T> getRoots();

    List<T> getChildren(T node);

    TreeNodeImpl<T> build();
}