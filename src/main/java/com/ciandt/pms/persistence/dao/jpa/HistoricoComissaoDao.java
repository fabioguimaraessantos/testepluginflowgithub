package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.HistoricoComissao;
import com.ciandt.pms.persistence.dao.IHistoricoComissaoDao;


/**
 * 
 * A classe HistoricoComissaoDao proporciona as funcionalidades
 * da persistencia referente a entidade HistoricoComissao.
 *
 * @since 07/10/2010
 * @author <a href="mailto:ronaldon@ciandt.com">Ronaldo Ronie Nascimento</a>
 *
 */
@Repository
public class HistoricoComissaoDao extends AbstractDao<Long, HistoricoComissao> 
    implements IHistoricoComissaoDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory - fabrica do entity manager
     */
    @Autowired
    public HistoricoComissaoDao(@Qualifier("entityManagerFactory")
            final EntityManagerFactory factory) {
        super(factory, HistoricoComissao.class);
    }

}
