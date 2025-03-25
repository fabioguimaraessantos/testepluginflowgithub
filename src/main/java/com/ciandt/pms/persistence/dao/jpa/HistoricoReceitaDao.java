package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.HistoricoReceita;
import com.ciandt.pms.persistence.dao.IHistoricoReceitaDao;


/**
 * 
 * A classe HistoricoReceitaDao proporciona as funcionalidades de acesso ao
 * banco de dados referente a entidade HistoricoReceita.
 * 
 * @since 26/05/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class HistoricoReceitaDao extends AbstractDao<Long, HistoricoReceita>
        implements IHistoricoReceitaDao {

    /**
     * Construtor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public HistoricoReceitaDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, HistoricoReceita.class);
    }

}