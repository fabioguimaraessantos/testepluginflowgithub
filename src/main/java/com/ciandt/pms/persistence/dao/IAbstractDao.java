/**
 * @(#) IAbstractDao.java
 * Copyright (c) 2009 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.persistence.dao;

/**
 * Define a interface de um DAO genérico.
 * 
 * @param <K>
 *            Tipo da chave
 * @param <E>
 *            Tipo da entidade
 */
public interface IAbstractDao<K, E> {

    /**
     * Busca a entidade pelo id.
     * 
     * @see EntityManager#find(Class, Object)
     * @param id
     *            Chave da entidade
     * @return Entidade
     */
    E findById(final K id);

    /**
     * Remove uma entidade.
     * 
     * @see EntityManager#remove(Object)
     * @param entity
     *            Entidade
     */
    void remove(final E entity);

    /**
     * Remove a entidade filha.
     * 
     * @see EntityManager#remove(Object)
     * @param entity
     *            Entidade
     */
    void removeChild(final Object entity);

    /**
     * Persiste, cria a entidade.
     * 
     * @see EntityManager#persist(Object)
     * @param entity
     *            Entidade
     */
    void persist(final E entity);

    /**
     * Atualiza a entidade.
     * 
     * @see EntityManager#merge(Object)
     * @param entity
     *            Entidade
     */
    void update(final E entity);

    /**
     * Atualiza a entidade.
     * 
     * @see EntityManager#merge(Object)
     * @param entity
     *            Entidade
     * @return Entidade atualizada
     */
    E merge(final E entity);

    /**
     * Persiste, cria a entidade.
     * 
     * @see EntityManager#persist(Object)
     * @param entity
     *            Entidade
     */
    void create(final E entity);

    /**
     * Faz flush no banco de dados das alterações pendentes.
     * 
     * @see EntityManager#flush()
     */
    void flush();

}
