package com.ciandt.pms.persistence.dao.jpa;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.FonteReceita;
import com.ciandt.pms.persistence.dao.IFonteReceitaDao;


/**
 * 
 * A classe FonteReceitaDao proporciona as funcionalidades
 * de persistenca referente a entidade FonteReceita.
 *
 * @since 03/02/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class FonteReceitaDao extends 
        AbstractDao<Long, FonteReceita> implements IFonteReceitaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            do tipo da entidade
     */
    @Autowired
    public FonteReceitaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, FonteReceita.class);
    }

    /**
     * Busca por todos os ContratoPraticas.
     * 
     * @return uma lista com todos os ContratoPraticas.
     */
    @SuppressWarnings("unchecked")
    public List<FonteReceita> findAll() {

        List<FonteReceita> listResult = getJpaTemplate().findByNamedQuery(
                FonteReceita.FIND_ALL);

        return listResult;
    }
}
