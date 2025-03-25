package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.ExplicacaoDesvio;
import com.ciandt.pms.persistence.dao.IExplicacaoDesvioDao;


/**
 * 
 * Define o DAO da entidade.
 * 
 * @since 19/04/2011
 * @author cmantovani
 * 
 */
@Repository
public class ExplicacaoDesvioDao extends AbstractDao<Long, ExplicacaoDesvio>
        implements IExplicacaoDesvioDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public ExplicacaoDesvioDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, ExplicacaoDesvio.class);
    }

    /**
     * Retorna todas as entidades ativas.
     * 
     * @return retorna uma lista com todas as entidades ativas.
     */
    @SuppressWarnings("unchecked")
    public List<ExplicacaoDesvio> findAtivos() {

        List<ExplicacaoDesvio> listResult = getJpaTemplate().findByNamedQuery(
                ExplicacaoDesvio.FIND_ATIVOS, new Object[] {});

        return listResult;
    }

    /**
     * Busca a entidade selecionada.
     * 
     * @return uma ExplicacaoDesvio.
     */
    @SuppressWarnings("unchecked")
    public ExplicacaoDesvio findSelecionado() {
        List<ExplicacaoDesvio> listResult = getJpaTemplate().findByNamedQuery(
                ExplicacaoDesvio.FIND_SELECIONADO, new Object[] {});

        if (listResult.isEmpty()) {
            return null;
        }

        return listResult.get(0);
    }

}