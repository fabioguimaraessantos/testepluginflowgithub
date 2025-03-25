package com.ciandt.pms.persistence.dao.jpa;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.ciandt.pms.model.AlocacaoPeriodoFoto;
import com.ciandt.pms.persistence.dao.IAlocacaoPeriodoFotoDao;


/**
 * 
 * A classe AlocacaoPeriodoFotoDao proporciona as funcionalidades de acesso ao
 * banco de dadso para a entidade AlocacaoPeriodoFoto.
 * 
 * @since 17/02/2011
 * @author <a href="mailto:alisson@ciandt.com">Alisson Fernando da Silva</a>
 * 
 */
@Repository
public class AlocacaoPeriodoFotoDao extends
        AbstractDao<Long, AlocacaoPeriodoFoto> implements
        IAlocacaoPeriodoFotoDao {

    /**
     * Contrutor padrão.
     * 
     * @param factory
     *            da entidade
     */
    @Autowired
    public AlocacaoPeriodoFotoDao(
            @Qualifier("entityManagerFactory") final EntityManagerFactory factory) {
        super(factory, AlocacaoPeriodoFoto.class);
    }

}