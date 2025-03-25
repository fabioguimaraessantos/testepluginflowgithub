package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.Atividade;
import com.ciandt.pms.persistence.dao.IAtividadeDao;


/**
 * Define o DAO da entidade.
 * 
 * @author Alisson Fernando da Silva
 * @since 17/08/2010
 */
@Repository
public class AtividadeDao extends AbstractDao<Long, Atividade>
        implements IAtividadeDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            Entidade do tipo Atividade
     */
    @Autowired
    public AtividadeDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, Atividade.class);
    }

    /**
     * Retorna todas as entidades.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<Atividade> findAll() {
        List<Atividade> listResult = getJpaTemplate().findByNamedQuery(
                Atividade.FIND_ALL);

        return listResult;
    }
    
    /**
     * Retorna todas as entidades - ativas.
     * 
     * @return retorna uma lista com todas as entidades.
     */
    @SuppressWarnings("unchecked")
    public List<Atividade> findAllActive() {
        List<Atividade> listResult = getJpaTemplate().findByNamedQuery(
                Atividade.FIND_ALL_ACTIVE);

        return listResult;
    }
    
    /**
     * Retorna a Atividade referente a sigla.
     * 
     * @param sigla da atividade
     * 
     * @return retorna uma entidade do tipo Atividade 
     */
    @SuppressWarnings("unchecked")
    public Atividade findBySigla(final String sigla) {
        
        List<Atividade> listResult = getJpaTemplate().findByNamedQuery(
                Atividade.FIND_BY_SIGLA, new Object[] {sigla});

        if (listResult.isEmpty()) {
            return null;
        }
        
        return listResult.get(0);
    }

}