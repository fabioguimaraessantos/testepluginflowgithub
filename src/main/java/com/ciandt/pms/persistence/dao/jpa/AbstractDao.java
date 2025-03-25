/**
 * @(#) AbstractDao.java
 * Copyright (c) 2008 Ci&T Software S/A.
 * All Rights Reserved.
 */
package com.ciandt.pms.persistence.dao.jpa;

import com.ciandt.pms.persistence.dao.IAbstractDao;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;


/**
 * DAO Generico que implementa os metodos basicos utilizando Spring.
 * 
 * @author Dagoberto S. de Souza
 * @since 01/08/2008
 * @param <K>
 *            Key - chave da entidade
 * @param <E>
 *            Entity - entidade
 */
public abstract class AbstractDao<K, E> extends JpaDaoSupport implements
        IAbstractDao<K, E> {

    /**
     * Class da entidade.
     */
    private Class<E> entityClass;

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            EntityManagerFactory
     * @param entityClass
     *            Class da entidade
     */
    public AbstractDao(final EntityManagerFactory factory,
            final Class<E> entityClass) {
        setEntityManagerFactory(factory);
        this.entityClass = entityClass;
    }

    /**
     * Busca a entidade pelo id.
     * 
     * @param id
     *            Chave da entidade
     * @return Entidade
     */
    public E findById(final K id) {
        return (E) getJpaTemplate().find(entityClass, id);
    }

    /**
     * Remove a entidade.
     * 
     * @param entity
     *            Entidade
     */
    public void remove(final E entity) {
        getJpaTemplate().remove(entity);
    }

    /**
     * Remove a entidade filha.
     * 
     * @param entity
     *            Entidade
     */
    public void removeChild(final Object entity) {
        getJpaTemplate().remove(entity);
    }

    /**
     * Persiste, cria a entidade.
     * 
     * @param entity
     *            Entidade
     */
    public void persist(final E entity) {
        getJpaTemplate().persist(entity);
    }

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            Entidade
     */
    public void update(final E entity) {
        getJpaTemplate().merge(entity);
    }

    /**
     * Atualiza a entidade.
     * 
     * @param entity
     *            Entidade
     * @return Entidade atualizada
     */
    public E merge(final E entity) {
        return getJpaTemplate().merge(entity);
    }

    /**
     * Persiste, cria a entidade.
     * 
     * @param entity
     *            Entidade
     */
    public void create(final E entity) {
        getJpaTemplate().persist(entity);
    }

    /**
     * Faz flush no banco de dados das alterações pendentes.
     */
    public void flush() {
        getJpaTemplate().flush();
    }

    /**
     * 
     * Seta os parametros na query apartir de um array de objetos ordenado.
     * 
     * @param query
     *            - Query a ser executada
     * @param params
     *            - Array de parametros a ser setados
     */
    public void setParameters(final Query query, final Object[] params) {

        if (query != null && params != null) {
            for (int i = 1; i <= params.length; i++) {
                query.setParameter(i, params[i - 1]);
            }
        }

    }

    /**
     * Utility method to get first result of entity list or null if empty result
     * @param result result of query/select statement
     * @return first entity found
     */
    E firstResultOrNull(List<E> result) {
        return result != null && !result.isEmpty() ? result.get(0) : null;
    }
}
