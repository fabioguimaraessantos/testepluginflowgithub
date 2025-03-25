package com.ciandt.pms.tree.interfaces.impl;

import com.ciandt.pms.tree.interfaces.Tree;
import org.richfaces.model.TreeNodeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TreeImpl<T> implements Tree<T> {

    private final List<T> list;

    /**
     *
     */
    TreeImpl(List<T> list){
        this.list = list;
    }

    /* Contracts */
    @Override
    public List<T> getRoots() {
        List<T> result = new ArrayList<>();

        for (T t : list)
            if(Objects.isNull(parent(t)) || element(t).equals(parent(t)))
                result.add(t);

        return result;
    }

    @Override
    public List<T> getChildren(T node) {
        List<T> result = new ArrayList<>();

        for (T t : list)
            if(element(node).equals(parent(t)) && !element(t).equals(parent(t)))
                result.add(t);

        return result;
    }

    @Override
    public TreeNodeImpl<T> build() {
        TreeNodeImpl<T> root = new TreeNodeImpl<>();
        root.setData(root());

        Integer index=0;
        TreeNodeImpl<T> tree = new TreeNodeImpl<>();
        tree.addChild(index++, root);

        List<TreeNodeImpl<T>> nodes = new ArrayList<>();
        for (T t: getRoots())
            nodes.add(leaf(root, t, index++));

        for (TreeNodeImpl<T> t : nodes)
            build(t,index++);

        return tree;
    }

    /*
     * Privates
     */
    private void build(TreeNodeImpl<T> element, Integer index) {

        List<T> childrens = getChildren(element.getData());
        if(childrens.isEmpty())
            return;

        for (T child : childrens)
            build(leaf(element, child, index), index++);
    }

    private TreeNodeImpl<T> leaf( TreeNodeImpl<T> parent, T data, Integer index){
        TreeNodeImpl<T> node = new TreeNodeImpl<>();
        node.setData(data);

        parent.addChild(index++ ,node);
        return node;
    }

    /* Abstracts */
    public abstract T root();
    public abstract String element(T entity);
    public abstract String parent(T entity);
}