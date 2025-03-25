package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Recurso;
import com.ciandt.pms.persistence.dao.IRecursoDao;


/**
 * 
 * A classe RecursoDao proporciona as funcionalidades de acesso ao banco de
 * dados referente a entidade Recurso.
 * 
 * @since 21/08/2009
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 * 
 */
@Repository
public class RecursoDao extends AbstractDao<Long, Recurso> implements
        IRecursoDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public RecursoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Recurso.class);
    }

    /**
     * Realiza uma busca rápida.
     * 
     * @param value
     *            utilizado na busca
     * @param tipoRecurso
     *          tipo do recurso (Pessoa ou Papel)
     * 
     * @return com os resultados
     */
    @SuppressWarnings("unchecked")
    public List<Recurso> quickSearch(final String value, final String tipoRecurso) {

        List<Recurso> listResult = getJpaTemplate().findByNamedQuery(
                Recurso.FIND_QUICK_SEARCH, new Object[] {
                        value, tipoRecurso, tipoRecurso});

        return listResult;
    }

    /**
     * Retorna todas os recursos.
     * 
     * @return retorna uma lista com todos os recursos.
     */
    @SuppressWarnings("unchecked")
    public List<Recurso> findAll() {
        List<Recurso> listResult = getJpaTemplate().findByNamedQuery(
                Recurso.FIND_ALL);

        return listResult;
    }
    
    /**
     * Realiza uma busca pelo mnemonico do recurso.
     * 
     * @param value
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    @SuppressWarnings("unchecked")
    public Recurso findByMnemonico(final String value) {

        List<Recurso> listResult = getJpaTemplate().findByNamedQuery(
                Recurso.FIND_BY_MNEMONICO, new Object[] {value});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }
    
    /**
     * Realiza uma busca pelo tipo.
     * 
     * @param value
     *            utilizado na busca
     * 
     * @return com os resultados
     */
    @SuppressWarnings("unchecked")
    public List<Recurso> findByTipo(final String value) {

        List<Recurso> listResult = getJpaTemplate().findByNamedQuery(
                Recurso.FIND_BY_TIPO, new Object[] {value});
        
        return listResult;
    }

}
